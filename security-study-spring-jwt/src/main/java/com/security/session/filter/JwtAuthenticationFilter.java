package com.security.session.filter;

import java.io.IOException;
import java.util.List;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.session.spring.JwtTokenProvider;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("JwtAuthenticationFilter");
		
		String token = jwtTokenProvider.createToken(authResult.getName(), (List<SimpleGrantedAuthority>) authResult.getAuthorities());
		System.out.println("created token : " + token);
		
		Cookie cookie = new Cookie("Authorization", token);
		cookie.setPath("/");
		cookie.setMaxAge(60*60*24);
		
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
		System.out.println("JwtAuthenticationFilter attemptAuthentication");
        String username = request.getParameter("id");
        String password = request.getParameter("password");
        System.out.println("nm : " + username + " , pw : " + password);
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }
	
}
