package com.devSoft.Service;

import java.util.List;

import com.devSoft.Model.Department;

public interface DepartmentService {
	
	void addDept(Department dept);
	void deleteDept(int id);
	void updateDept(Department dept);
	Department getDeptById(int id);
	List<Department> getAllDepts();
	
	
	

}
