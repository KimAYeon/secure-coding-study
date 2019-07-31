package com.security.session.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.session.mapper.UserMapper;
import com.security.session.model.User;

@Service
public class UserServiceImpl {
	@Autowired UserMapper userMapper;
	
	public User getUserDetail(String id) {
		return userMapper.selectUserDetail(id);
	}
	
}
