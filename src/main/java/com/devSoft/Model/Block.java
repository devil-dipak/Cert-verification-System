package com.devSoft.Model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Block_tbl")
public class Block {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String previousHash;
	private String currentHash;
	private String data;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt = LocalDate.now();
	private long timestamp;
	
	

}
