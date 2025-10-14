package com.example.hello.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		// Add CSRF token to model for template access
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		if (csrfToken != null) {
			model.addAttribute("_csrf", csrfToken);
		}
		return "login"; // Return Thymeleaf template name
	}
}


