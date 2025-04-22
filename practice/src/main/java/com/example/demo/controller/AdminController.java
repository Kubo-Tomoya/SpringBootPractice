package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	//管理者新規登録画面表示
	@GetMapping("/signup")
	public String showSignupForm(Model model) {

		model.addAttribute("admin", new Admin());
		return "admin_signup";
	}
	//管理者登録処理

	@PostMapping("/signup")
	public String processSignup(@ModelAttribute Admin admin) {

		adminService.register(admin);


		return "redirect:/admin/signin";
	}

	//管理者ログイン画面表示
	@GetMapping("/signin")
	public String showSigninForm() {
		return "admin_signin";
	}

	//ログアウト処理
	@GetMapping("/signout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/admin/signin";
	}
}
