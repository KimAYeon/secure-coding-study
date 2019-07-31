package com.security.session.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.session.model.User;
import com.security.session.service.MenuServiceImpl;
import com.security.session.service.UserServiceImpl;

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	MenuServiceImpl menuServiceImpl;

	@PostMapping("/login")
	@ResponseBody
	public User login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		System.out.println(user.getId());
		User loginUser = userServiceImpl.getUserDetail(user.getId());
		request.getSession().setAttribute("loginUser", loginUser);
		if(!ObjectUtils.isEmpty(loginUser)) {
			if(user.getPassword().equals(loginUser.getPassword())) {
				request.getSession().setAttribute("loginUser", loginUser);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("아이디 또는 비밀번호가 일치하지 않습니다.");
				response.getWriter().close();
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
		}
		return loginUser;
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
		request.getSession().removeAttribute("loginUser");
		return "success";
	}

}
