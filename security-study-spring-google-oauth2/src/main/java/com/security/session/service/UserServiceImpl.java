package com.security.session.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.session.mapper.UserMapper;
import com.security.session.model.User;

@Service
public class UserServiceImpl {
	
	@Autowired UserMapper userMapper;
	
	public User getUserDetail(User user) {
		return userMapper.selectUserDetail(user);
	}
	
	public int saveUserDetail(User user) {
		return userMapper.insertUserDetail(user);
	}
	
	public int saveUserToken(User user) {
		return userMapper.insertUserToken(user);
	}
	
	public int modifyUserToken(User user) {
		return userMapper.updateUserToken(user);
	}
	
}
