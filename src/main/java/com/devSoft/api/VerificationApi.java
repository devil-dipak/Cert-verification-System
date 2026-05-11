package com.devSoft.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devSoft.Model.Certificate;
import com.devSoft.Repository.CertificateRepository;
import com.devSoft.Service.BlockService;
import com.devSoft.Utils.HashUtil;

@RestController
@RequestMapping("/api/verify")
public class VerificationApi {

    @Autowired
    private CertificateRepository certificateRepo;

    @Autowired
    private BlockService blockService;

    @PostMapping("/certificate")
    public ResponseEntity<Map<String, Object>> verifyCertificate(@RequestParam("certId") Long certId) {

        Map<String, Object> response = new LinkedHashMap<>();

        Certificate cert = certificateRepo.findById(certId).orElse(null);

        if (cert == null) {
            response.put("valid", false);
            response.put("message", "Certificate not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String recalculatedHash = HashUtil.sha256(
                cert.getStudentName() +
                cert.getCourseName() +
                cert.getIssueDate()
        );

        boolean hashMatches = recalculatedHash.equals(cert.getCertificateHash());
        boolean isValidChain = blockService.isChainValid();
        boolean isValid = hashMatches && isValidChain;

        response.put("valid", isValid);
        response.put("certificateId", cert.getId());
        response.put("studentName", cert.getStudentName());
        response.put("courseName", cert.getCourseName());
        response.put("issuedBy", cert.getIssuer());

        if (isValid) {
            response.put("message", "Certificate is VALID and AUTHENTIC");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Certificate is TAMPERED or INVALID");
            if (!hashMatches) response.put("reason", "Certificate data hash mismatch");
            if (!isValidChain) response.put("reason", "Blockchain integrity check failed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/blockchain")
    public ResponseEntity<Map<String, Object>> checkBlockchain() {

        Map<String, Object> response = new LinkedHashMap<>();

        boolean valid = blockService.isChainValid();

        response.put("valid", valid);

        if (valid) {
            response.put("message", "Blockchain is SAFE (No Tampering detected)");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "ALERT! Blockchain has been TAMPERED");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

}