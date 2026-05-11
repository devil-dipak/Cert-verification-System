package com.devSoft.Model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name= "certificates_tbl")
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Student name is required")
	private String studentName;

	@NotBlank(message = "Issue date is required")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String issueDate;

	@NotBlank(message = "Course name is required")
	private String courseName;

	@NotBlank(message = "Issuer is required")
	private String issuer;

	@NotBlank(message = "Department is required")
	private String department;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt = LocalDate.now();

	private String certificateHash;
	
	}

