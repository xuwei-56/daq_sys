package com.collectInfo.service;


import java.util.List;

import com.collectInfo.model.User;

public interface IUserService {

	/**
	 * 根据用户id得到用户信息
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
    
    /**
     * 根据用户名得到用户信息
     * @param userName
     * @return
     */
    public User getUserByUserName(String userName);
    
    /**
     * 根据手机号得到用户信息
     * @param phoneNumber
     * @return
     */
	public User getUserByPhoneNumber(String phoneNumber);
	public List<User> getUsers(Integer pageSize,Integer page);
}
