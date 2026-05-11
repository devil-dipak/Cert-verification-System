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

import com.devSoft.Model.Department;
import com.devSoft.Service.DepartmentService;

@RestController
public class DepartmentRestApi {

	@Autowired
	private DepartmentService deptService;
	
	@GetMapping("/api/dept/list")
	public ResponseEntity<List<Department>> getAll() {

		return ResponseEntity.ok(deptService.getAllDepts());
	}
	
	@PostMapping("/api/dept/add")
	public ResponseEntity<Map<String, Object>> add(@RequestBody Department dept) {
		
		deptService.addDept(dept);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Department added successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/api/dept/{id}")
	public ResponseEntity<Department> getOne(@PathVariable("id")int id) {

		return ResponseEntity.ok(deptService.getDeptById(id));
	}
	
	@PutMapping("/api/dept/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Department dept) {
		
		deptService.updateDept(dept);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Department updated successfully");

		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/api/dept/delete/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id")int id) {
		
		deptService.deleteDept(id);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("success", true);
		response.put("message", "Department deleted successfully");

		return ResponseEntity.ok(response);
	}
}
