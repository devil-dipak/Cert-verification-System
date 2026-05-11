package com.devSoft.Controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.devSoft.Model.Certificate;
import com.devSoft.Repository.CertificateRepository;
import com.devSoft.Service.BlockService;
import com.devSoft.Utils.HashUtil;
import com.devSoft.Model.User;
import com.lowagie.text.pdf.PdfReader;

import jakarta.servlet.http.HttpSession;

@Controller
public class VerificationController {

    @Autowired
    private CertificateRepository certificateRepo;

    @Autowired
    private BlockService blockService;

    private boolean isHrOrAdmin(HttpSession session) {
        User user = (User) session.getAttribute("activeuser");
        return user != null && !user.getRole().equalsIgnoreCase("student");
    }

    @GetMapping("/verify")
    public String getVerifyPage(HttpSession session) {
        if (!isHrOrAdmin(session)) return "LoginForm";
        return "VerifyForm";
    }

    @PostMapping("/verify")
    public String verifyCertificate(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "hash", required = false) String manualHash,
            Model model,
            HttpSession session) {

        if (!isHrOrAdmin(session)) return "LoginForm";

        Map<String, Object> result = new LinkedHashMap<>();

        try {
            String extractedHash = null;

            if (file != null && !file.isEmpty()) {

                String fileName = file.getOriginalFilename().toLowerCase();

                if (fileName.endsWith(".pdf")) {
                    extractedHash = extractHashFromPdf(file);
                    if (extractedHash == null) {
                        model.addAttribute("error", "Could not find a valid certificate hash in the PDF.");
                        return "VerifyForm";
                    }
                } else {
                    String content = new BufferedReader(
                            new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    extractedHash = HashUtil.sha256(content);
                }
                result.put("fileHash", extractedHash);

            } else if (manualHash != null && !manualHash.isBlank()) {
                extractedHash = manualHash.trim();
            }

            if (extractedHash == null || extractedHash.isBlank()) {
                model.addAttribute("error", "Please upload a file or enter a hash.");
                return "VerifyForm";
            }

            Certificate matched = certificateRepo.findByCertificateHash(extractedHash);

            boolean chainValid = blockService.isChainValid();

            if (matched != null && chainValid) {
                result.put("valid", true);
                result.put("message", "Certificate is AUTHENTIC and verified on the blockchain");
                result.put("studentName", matched.getStudentName());
                result.put("courseName", matched.getCourseName());
                result.put("department", matched.getDepartment());
                result.put("issuer", matched.getIssuer());
                result.put("issueDate", matched.getIssueDate());
                result.put("certificateHash", matched.getCertificateHash());
            } else {
                result.put("valid", false);
                result.put("message", "Certificate is INVALID or TAMPERED");
                if (matched == null) result.put("reason", "No matching certificate found in database");
                if (!chainValid) result.put("reason", "Blockchain integrity check failed");
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Error processing file: " + e.getMessage());
        }

        model.addAttribute("result", result);
        return "VerifyForm";
    }

    private String extractHashFromPdf(MultipartFile file) throws Exception {
        byte[] pdfBytes = toByteArray(file.getInputStream());
        Pattern hashPattern = Pattern.compile("[0-9a-fA-F]{64}");

        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfBytes);

            // 1. Try PDF metadata Subject field
            String subject = (String) reader.getInfo().get("Subject");
            if (subject != null) {
                Matcher m = hashPattern.matcher(subject);
                if (m.find()) return m.group();
            }

            // 2. Try content stream from ALL pages
            int pages = reader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                byte[] contentBytes = reader.getPageContent(i);
                String content = new String(contentBytes, StandardCharsets.ISO_8859_1);
                Matcher m = hashPattern.matcher(content);
                if (m.find()) return m.group();
            }

            // 3. Try raw bytes as last resort
            String raw = new String(pdfBytes, StandardCharsets.ISO_8859_1);
            Matcher m = hashPattern.matcher(raw);
            if (m.find()) return m.group();

        } finally {
            if (reader != null) reader.close();
        }
        return null;
    }

    private byte[] toByteArray(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        return baos.toByteArray();
    }
}
