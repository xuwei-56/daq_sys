package com.collectInfo.service.impl;

import java.util.List;

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
		try {
			return this.userDao.selectByPrimaryKey(userId); 
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
	}

	@Override
	public int addUser(User user) {
		// TODO Auto-generated method stub
		try {
			return userDao.insertSelective(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int editUser(User user) {
		// TODO Auto-generated method stub
		try {
			return userDao.updateByPrimaryKeySelective(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int deleteUser(int userId) {
		// TODO Auto-generated method stub
		try {
			return userDao.deleteByPrimaryKey(userId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public User getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		try {
			return userDao.getUserByUserName(userName);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public User getUserByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		try{
			return userDao.getUserByPhoneNumber(phoneNumber);
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public List<User> getUsers(Integer pageSize,Integer page) {
		// TODO Auto-generated method stub
		int startNumber = pageSize*(page-1);
		return userDao.getUsers(startNumber,pageSize);
	}

}
