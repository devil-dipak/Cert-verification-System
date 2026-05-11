package com.devSoft.Service;

import java.util.List;

import com.devSoft.Model.Certificate;

public interface CertificateService {
	
	void addCert(Certificate cert);
	void deleteCert(Long id);
	void updateCert(Certificate cert);
	Certificate getCertById(Long id);
	List<Certificate> getAllCerts();
	List<Certificate> getCertsByStudent(String studentName);

}
