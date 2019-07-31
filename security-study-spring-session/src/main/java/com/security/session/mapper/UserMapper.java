package com.security.session.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.security.session.model.User;

@Mapper
public interface UserMapper {
	public User selectUserDetail(String id);
}
