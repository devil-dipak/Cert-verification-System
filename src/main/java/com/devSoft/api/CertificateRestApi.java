package com.devSoft.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devSoft.Model.Certificate;
import com.devSoft.Service.CertificateService;

@RestController
public class CertificateRestApi {

	@Autowired
	private CertificateService certService;
	
	@GetMapping("/api/cert/list")
	public ResponseEntity<List<Certificate>> getAll() {

		return ResponseEntity.ok(certService.getAllCerts());
	}
	
	@PostMapping("/api/cert/add")
	public ResponseEntity<Map<String, Object>> add(@RequestBody Certificate cert) {

		certService.addCert(cert);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Certificate added successfully");
		response.put("certificateHash", cert.getCertificateHash());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/api/cert/{id}")
	public ResponseEntity<Certificate> getOne(@PathVariable("id") Long id) {

		return ResponseEntity.ok(certService.getCertById(id));
	}
	
	@PutMapping("/api/cert/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Certificate cert) {
		
		certService.updateCert(cert);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Certificate updated successfully");

		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/api/cert/delete/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id")Long id) {
		
		certService.deleteCert(id);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Certificate deleted successfully");

		return ResponseEntity.ok(response);
	}
}
