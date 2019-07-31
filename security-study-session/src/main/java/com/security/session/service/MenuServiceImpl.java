package com.security.session.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.security.session.mapper.MenuMapper;
import com.security.session.model.Menu;

@Component("menuServiceImpl")
public class MenuServiceImpl {
	@Autowired
	MenuMapper menuMapper;

	@PostConstruct
	public void postConstruct() {
		System.out.println("MenuServiceImpl postConstruct");
	}

	public Menu getMenuDetail(String path) {
		return menuMapper.selectMenuDetail(path);
	}

	public Menu getMenuByAuth(Menu menu) {
		return menuMapper.selectMenuByAuth(menu);
	}

	@PreDestroy
	public void destroy() {
		System.out.println("MenuServiceImpl destroy");
	}

}
