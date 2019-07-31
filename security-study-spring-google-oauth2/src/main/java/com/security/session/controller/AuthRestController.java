package com.security.session.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.security.session.common.SecurityUtils;
import com.security.session.service.GoogleOAuth2ServiceImpl;
import com.security.session.service.UserServiceImpl;

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	GoogleOAuth2ServiceImpl googleOAuth2ServiceImpl;

	@PostMapping("/login")
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response) {
	}

	@GetMapping("/login")
	@ResponseBody
	public void loginGoogle2(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/member");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/logout")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		request.removeAttribute("loginUser");
		SecurityUtils.removeCookie(request, response, "Authorization");
		
		return response.getHeader("Set-Cookie").replace("Authorization=", "");
	}

}
