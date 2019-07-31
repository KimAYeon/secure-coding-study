package com.security.session.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.security.session.model.Menu;

@Mapper
public interface MenuMapper {
	public Menu selectMenuDetail(String path);
	public Menu selectMenuByAuth(Menu menu);
}
