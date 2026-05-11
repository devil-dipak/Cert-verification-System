package com.devSoft.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devSoft.Model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long>{

	List<Certificate> findByStudentName(String studentName);

	Certificate findByCertificateHash(String certificateHash);

}
