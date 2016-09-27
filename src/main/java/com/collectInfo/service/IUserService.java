package com.collectInfo.service;

import com.collectInfo.model.User;

public interface IUserService {
	
	/**
	 * 根据id得到User
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId);
	/**
	 * 添加一个User
	 * @param user
	 * @return
	 */
	public int addUser(User user);
	/**
	 * 修改User的密码或者手机号
	 * @param user
	 * @return
	 */
	public int editUser(User user);
	
	/**
	 * 删除一个User
	 * @param userId
	 * @return
	 */
	public int deleteUser(int userId);
}
