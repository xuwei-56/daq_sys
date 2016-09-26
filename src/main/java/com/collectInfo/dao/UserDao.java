package com.collectInfo.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.collectInfo.model.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    /**
     * 根据用户名得到设备信息
     * @param userName
     * @return
     */
    ArrayList<HashMap<String, Object>> getDeviceByUserName(String userName);
}