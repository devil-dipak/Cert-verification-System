package com.devSoft.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devSoft.Model.Certificate;
import com.devSoft.Repository.CertificateRepository;
import com.devSoft.Service.BlockchainService;
import com.devSoft.Service.CertificateService;
import com.devSoft.Utils.HashUtil;

@Service
public class CertificateServiceImpl implements CertificateService{

	@Autowired
	private CertificateRepository certRepo;

	@Autowired
	private BlockchainService blockchainService;

	@Override
	@Transactional
	public void addCert(Certificate cert) {

		String hash = HashUtil.sha256(
				cert.getStudentName() +
				cert.getCourseName() +
				cert.getDepartment() +
				cert.getIssueDate()
		);

		cert.setCertificateHash(hash);
		certRepo.save(cert);

		blockchainService.saveCertificateHash(hash);

	}

	@Override
	public void deleteCert(Long id) {

		certRepo.deleteById(id);

	}

	@Override
	@Transactional
	public void updateCert(Certificate cert) {

		String hash = HashUtil.sha256(
				cert.getStudentName() +
				cert.getCourseName() +
				cert.getDepartment() +
				cert.getIssueDate()
		);

		cert.setCertificateHash(hash);
		certRepo.save(cert);

	}

	@Override
	public Certificate getCertById(Long id) {

		return certRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Certificate not found with id:" + id));
	}

	@Override
	public List<Certificate> getAllCerts() {

		return certRepo.findAll();
	}

	@Override
	public List<Certificate> getCertsByStudent(String studentName) {

		return certRepo.findByStudentName(studentName);
	}

}
