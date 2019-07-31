package com.security.session.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User {
	public UserDetailsImpl(com.security.session.model.User user) {
		super(user.getId(), user.getPassword(), authorities(user));
	}

	private static Collection<? extends GrantedAuthority> authorities(com.security.session.model.User user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		switch(user.getAuth_id()) {
		case "AUTH004": authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		case "AUTH005": authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		}
		
		return authorities;
	}
}
