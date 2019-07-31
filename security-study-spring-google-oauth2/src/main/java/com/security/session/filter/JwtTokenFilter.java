package com.security.session.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import com.google.gson.JsonObject;
import com.security.session.common.SecurityUtils;
import com.security.session.exception.CustomException;
import com.security.session.exception.InvalidTokenException;
import com.security.session.model.User;
import com.security.session.service.GoogleOAuth2ServiceImpl;
import com.security.session.service.JwtTokenProvider;
import com.security.session.service.UserServiceImpl;

public class JwtTokenFilter extends BasicAuthenticationFilter {

	private JwtTokenProvider jwtTokenProvider;
	@Autowired GoogleOAuth2ServiceImpl googleOAuth2ServiceImpl;
	@Autowired UserServiceImpl userServiceImpl;

	public JwtTokenFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		super(authenticationManager);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("JwtTokenFilter");

		String token = null;
		Cookie cookie = SecurityUtils.searchCookie(request, response, "Authorization");
		if(!ObjectUtils.isEmpty(cookie))	token = cookie.getValue();
		
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				User user = new User();
				user.setAccess_token(token);
				user = userServiceImpl.getUserDetail(user);
				
				request.setAttribute("loginUser", user);
			}
		} catch(InvalidTokenException ex) {
			User user = new User();
			user.setAccess_token(token);
			user = userServiceImpl.getUserDetail(user);
			
			if(ObjectUtils.isEmpty(user)) {
				request.removeAttribute("loginUser");
				SecurityUtils.removeCookie(request, response, "Authorization");
			}

			String refresh_token = user.getRefresh_token();
			
			try {
				if (jwtTokenProvider.validateToken(refresh_token)) {
					request.setAttribute("refresh_token", user.getRefresh_token());
					JsonObject jsonObject = googleOAuth2ServiceImpl.refreshToken(request, response);
					
					// User 업데이트
					String accessToken = jsonObject.get("access_token").getAsString();
					user.setAccess_token(accessToken);
					
					if(!ObjectUtils.isEmpty(jsonObject.get("refresh_token"))) {
						String refreshToken = jsonObject.get("refresh_token").getAsString();
						user.setRefresh_token(refreshToken);
					}
					
					userServiceImpl.saveUserDetail(user);
					
					cookie = SecurityUtils.searchCookie(request, response, "Authorization");
					cookie.setMaxAge(60 * 60 * 24);
					cookie.setValue(accessToken);
					cookie.setPath("/");
					
					response.addCookie(cookie);
					
					Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
					SecurityContextHolder.getContext().setAuthentication(auth);
					
					request.setAttribute("loginUser", user);
				}
			} catch(InvalidTokenException ex2) {
				request.removeAttribute("loginUser");
				SecurityUtils.removeCookie(request, response, "Authorization");
			}
			
		} catch (CustomException ex) {
			response.sendError(ex.getHttpStatus().value(), ex.getMessage());
		} finally {
			filterChain.doFilter(request, response);
		}
		
	}

}
