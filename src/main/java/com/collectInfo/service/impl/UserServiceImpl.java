package com.collectInfo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.collectInfo.dao.UserDao;
import com.collectInfo.model.User;
import com.collectInfo.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService{
	
	@Resource
	private UserDao userDao;

	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return this.userDao.selectByPrimaryKey(userId); 
	}

}
