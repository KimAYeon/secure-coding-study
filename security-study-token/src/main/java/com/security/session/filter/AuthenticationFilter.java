package com.security.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.session.model.User;
import com.security.session.service.TokenProvider;
import com.security.session.service.UserServiceImpl;

public class AuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	UserServiceImpl userServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("AuthenticationFilter");
		String token = "";
		
		//String token = ((HttpServletRequest) request).getHeader("Authorization");
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("Authorization")) {
				token = cookie.getValue();
			}
		}
		
		if(!ObjectUtils.isEmpty(token)){
			token = TokenProvider.getInstance().parseToToken(token);
			int index = token.indexOf(":");
			String id = token.substring(0, index);
			User loginUser = userServiceImpl.getUserDetail(id);
			
			if(ObjectUtils.isEmpty(loginUser)) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "접근 권한이 없습니다.");
			} else {
				request.setAttribute("loginUser", loginUser);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
