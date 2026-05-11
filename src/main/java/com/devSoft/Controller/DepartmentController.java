package com.devSoft.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.devSoft.Model.Department;
import com.devSoft.Model.User;
import com.devSoft.Service.DepartmentService;
import com.devSoft.Utils.DepartmentExcelView;
import com.devSoft.Utils.DepartmentPdfView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	
	@Autowired
	private DepartmentService deptService;

	private boolean isAdmin(HttpSession session) {
		User user = (User) session.getAttribute("activeuser");
		return user != null && !user.getRole().equalsIgnoreCase("student") && !user.getRole().equalsIgnoreCase("HR");
	}
	
	
	@GetMapping("/add")
	public String getDept(HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		return "DepartmentForm";
		
	}
	@PostMapping("/add")
	public String postDept(@ModelAttribute Department dept, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		deptService.addDept(dept);
		
		return "DepartmentForm";
	}
	
	@GetMapping("/list")
	public String getDeptList(Model model, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		model.addAttribute("deptList", deptService.getAllDepts());
		
		return "DepartmentListForm";
	}
	
	@GetMapping("/delete")
	public String deleteDept(@RequestParam("id") int id, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		deptService.deleteDept(id);
		
		return "redirect:list";
	}
	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		model.addAttribute("deptObject", deptService.getDeptById(id));
		
		return "EditDepartmentForm";
	}
	@PostMapping("/update")
	public String update(@ModelAttribute Department dept, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		deptService.updateDept(dept);
		
		return "redirect:list";
	}
	
	@GetMapping("/excel")
	public ModelAndView excel(HttpSession session) {
		if (!isAdmin(session)) return null;
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("dList", deptService.getAllDepts());
		mv.setView(new DepartmentExcelView()); //
		
		return mv;
	}
	
	@GetMapping("/pdf")
	public ModelAndView pdf(HttpSession session) {
		if (!isAdmin(session)) return null;
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("dList", deptService.getAllDepts());
		mv.setView(new DepartmentPdfView());
		
		
		return mv;
	}
}
