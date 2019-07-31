package com.security.session.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.security.session.model.User;
import com.security.session.service.MenuServiceImpl;
import com.security.session.service.TokenProvider;
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
	@ResponseStatus(value = HttpStatus.OK)
	public String login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		System.out.println(user.getId());
		User loginUser = userServiceImpl.getUserDetail(user.getId());
		String token = "";
		
		if(!ObjectUtils.isEmpty(loginUser)) {
			if(user.getPassword().equals(loginUser.getPassword())) {
				token = TokenProvider.getInstance().createToken(loginUser);
				
				Cookie setCookie = new Cookie("Authorization", URLEncoder.encode(token, "UTF-8"));
				setCookie.setMaxAge(60*60*24);
				setCookie.setHttpOnly(true);
				setCookie.setPath("/");
				response.addCookie(setCookie);
//				response.addHeader("Cache-Control", "no-cache");
//				response.addHeader("Access-Control-Allow-Credentials","true");
//				response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
//				response.addHeader("Access-Control-Allow-Headers", "Set-Cookie");
			} else {
//				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//				response.setCharacterEncoding("UTF-8");
//				response.getWriter().write("아이디 또는 비밀번호가 일치하지 않습니다.");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
		}
		return "";
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("Authorization")) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				response.addCookie(cookie);
				response.addHeader("Cache-Control", "no-cache");
				response.addHeader("Access-Control-Allow-Credentials","true");
				response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
				response.addHeader("Access-Control-Allow-Headers", "Set-Cookie");
			}
		}
		return "success";
	}

}
