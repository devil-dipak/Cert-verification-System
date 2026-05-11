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


import com.devSoft.Model.Block;
import com.devSoft.Model.User;
import com.devSoft.Service.BlockService;
import com.devSoft.Utils.BlockExcelView;
import com.devSoft.Utils.BlockPdfView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/block")
public class BlockController {
	
	@Autowired
	private BlockService blcService;

	private boolean isAdmin(HttpSession session) {
		User user = (User) session.getAttribute("activeuser");
		return user != null && !user.getRole().equalsIgnoreCase("student") && !user.getRole().equalsIgnoreCase("HR");
	}

	private boolean isLoggedIn(HttpSession session) {
		return session.getAttribute("activeuser") != null;
	}
	
	@GetMapping("/add")
	public String getBlc(HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		return "BlockForm";
		
	}
	
	@PostMapping("/add")
	public String postBlc(@ModelAttribute Block blc, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		blcService.addBlc(blc);
		
		return "BlockForm";
	}
	@GetMapping("/list")
	public String getBlcList(Model model, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		model.addAttribute("blcList", blcService.getAllBlcs());
		return "BlockListForm";
	}
	
	@GetMapping("/delete")
	public String deleteBlc(@RequestParam("id") Long id, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		blcService.deleteBlc(id);
		
		return "redirect:list";
	}
	
	@GetMapping("/edit")
	public String edit(@RequestParam("id") Long id, Model model, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		model.addAttribute("blcObject", blcService.getBlcById(id));
		
		return "EditBlockForm";
	}
	@PostMapping("/update")
	public String update(@ModelAttribute Block blc, HttpSession session) {
		if (!isAdmin(session)) return "LoginForm";
		blcService.updateBlc(blc);
		
		return "redirect:list";
	}
	
	@GetMapping("/excel")
	public ModelAndView excel(HttpSession session) {
		if (!isAdmin(session)) return null;
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("bList", blcService.getAllBlcs());
		mv.setView(new BlockExcelView());
		
		return mv;
	}
	
	@GetMapping("/pdf")
	public ModelAndView pdf(HttpSession session) {
		if (!isAdmin(session)) return null;
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("bList", blcService.getAllBlcs());
		mv.setView(new BlockPdfView());
		
		
		return mv;
	}
	
	@GetMapping("/explorer")
	public String explorer(Model model, HttpSession session) {
		if (!isLoggedIn(session)) return "LoginForm";
		model.addAttribute("blocks", blcService.getAllBlcs());

		return "BlockchainExplorer";
	}

}
