package com.security.session.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.security.session.model.User;

@Mapper
public interface UserMapper {
	public User selectUserDetail(User user);
	public int insertUserDetail(User user);
	public int insertUserToken(User user);
	public int updateUserToken(User user);
}
