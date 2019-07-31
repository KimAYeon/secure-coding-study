package com.security.session.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.session.model.User;
import com.security.session.service.UserServiceImpl;

@RestController
@RequestMapping(value="/auth")
public class AuthRestController {
	@Autowired UserServiceImpl userServiceImpl;
	
	@PostMapping("/login")
	public User login(@RequestBody User user, HttpServletRequest request, Model model) {
		System.out.println(user.getId());
		User loginUser = userServiceImpl.getUserDetail(user.getId());
		if(!ObjectUtils.isEmpty(loginUser)) {
			request.getSession().setAttribute("loginUser", loginUser);
		}
		return loginUser;
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, Model model) {
		request.getSession().removeAttribute("loginUser");
	}
}
