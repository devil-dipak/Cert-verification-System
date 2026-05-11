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

import com.devSoft.Model.Block;
import com.devSoft.Service.BlockService;

@RestController
public class BlockRestApi {
	
	@Autowired
	private BlockService blcService;
	
	@GetMapping("/api/blc/list")
	public ResponseEntity<List<Block>> getAll() {

		return ResponseEntity.ok(blcService.getAllBlcs());
	}
	
	
	@PostMapping("/api/blc/add")
	public ResponseEntity<Map<String, Object>> add(@RequestBody Block blc) {

		blcService.addBlc(blc);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Block added successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/api/blc/{id}")
	public ResponseEntity<Block> getOne(@PathVariable("id") Long id) {

		return ResponseEntity.ok(blcService.getBlcById(id));
	}
	
	@PutMapping("/api/blc/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Block blc) {
		
		blcService.updateBlc(blc);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Block updated successfully");

		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/api/blc/delete/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id")Long id) {
		
		blcService.deleteBlc(id);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Block deleted successfully");

		return ResponseEntity.ok(response);
	}

}
