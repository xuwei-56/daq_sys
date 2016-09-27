package com.collectInfo.dao;

import com.collectInfo.model.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);
    //根据id查找用户
    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //多条件组合查询
    User selectByCriteria(User user);
    

}