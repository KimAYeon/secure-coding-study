package com.security.session.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import com.google.gson.JsonObject;
import com.security.session.model.User;
import com.security.session.service.GoogleOAuth2ServiceImpl;
import com.security.session.service.JwtTokenProvider;
import com.security.session.service.UserServiceImpl;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	GoogleOAuth2ServiceImpl googleOAuth2ServiceImpl;

	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("JwtAuthenticationFilter");

		Cookie cookie = new Cookie("Authorization", request.getAttribute("access_token").toString());
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24);

		response.addCookie(cookie);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		chain.doFilter(request, response);
	}

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		// 토큰 발급
		JsonObject jsonObject = googleOAuth2ServiceImpl.obtainToken(request, response);
		
		// User 업데이트
		User user = googleOAuth2ServiceImpl.setUserByIdToken(jsonObject);
		
		String accessToken = jsonObject.get("access_token").getAsString();
		user.setAccess_token(accessToken);
		
		if(!ObjectUtils.isEmpty(jsonObject.get("refresh_token"))) {
			String refreshToken = jsonObject.get("refresh_token").getAsString();
			user.setRefresh_token(refreshToken);
		}
		
		user.setPassword("admin");
		user.setAuth_id("AUTH005");
		userServiceImpl.saveUserDetail(user);
		
		request.setAttribute("access_token", accessToken);
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());

		return authenticationManager.authenticate(authenticationToken);
	}

}
