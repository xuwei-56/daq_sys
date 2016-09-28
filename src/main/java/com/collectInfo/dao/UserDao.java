package com.collectInfo.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.collectInfo.model.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);
    //根据id查找用户
    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    /**
     * 根据用户名得到设备信息
     * @param userName
     * @return
     */
    ArrayList<HashMap<String, Object>> getDeviceByUserName(String userName);
    /**
     * 根据用户名得到用户信息
     * @param userName
     * @return
     */
	HashMap<String, Object> getUserByUserName(String userName);
	/**
	 * 根据手机号得到用户信息
	 * @param phoneNumber
	 * @return
	 */
	HashMap<String, Object> getUserByPhoneNumber(String phoneNumber);

}