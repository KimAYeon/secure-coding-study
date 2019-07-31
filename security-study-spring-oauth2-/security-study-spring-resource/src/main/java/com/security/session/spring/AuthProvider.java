package com.security.session.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.security.session.service.UserServiceImpl;

public class AuthProvider extends DaoAuthenticationProvider implements AuthenticationProvider {
	@Autowired UserServiceImpl userServiceImpl;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = super.getUserDetailsService().loadUserByUsername(authentication.getName());
		String password = authentication.getCredentials().toString();

		if(!super.getPasswordEncoder().matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}
		
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
