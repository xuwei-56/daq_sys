package com.collectInfo.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.collectInfo.model.User;

public interface IUserService {

	/**
	 * 根据用户id得到用户信息
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId);
	
	/**
     * 根据用户名得到设备信息
     * @param userName
     * @return
     */
    public ArrayList<HashMap<String, Object>> getDeviceByUserName(String userName);

}
