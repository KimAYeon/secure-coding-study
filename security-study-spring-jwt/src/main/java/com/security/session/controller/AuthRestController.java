package com.security.session.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.session.service.UserServiceImpl;

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {
	@Autowired
	UserServiceImpl userServiceImpl;

	@PostMapping("/login")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return response.getHeader("Set-Cookie").replace("Authorization=", "");
	}

	@PostMapping("/logout")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("Authorization")) {
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath("/");
					
					response.addCookie(cookie);					
				}
			}
		}
		return response.getHeader("Set-Cookie").replace("Authorization=", "");
	}
	
}
