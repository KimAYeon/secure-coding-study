package com.security.session.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.session.mapper.MenuMapper;
import com.security.session.model.Menu;

@Service
public class MenuServiceImpl {
	@Autowired MenuMapper menuMapper;
	
	public Menu getMenuDetail(String path) {
		return menuMapper.selectMenuDetail(path);
	}
	
	public Menu getMenuByAuth(Menu menu) {
		return menuMapper.selectMenuByAuth(menu);
	}
	
}
