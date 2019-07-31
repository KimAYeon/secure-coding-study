package com.security.session.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.security.session.model.User;
import com.security.session.service.UserServiceImpl;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired UserServiceImpl userServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername : " + username);
		User targetUser = new User();
		targetUser.setId(username);
		User user = userServiceImpl.getUserDetail(targetUser);
		
		if(ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
		}
		
		return new UserDetailsImpl(user);
	}

}
