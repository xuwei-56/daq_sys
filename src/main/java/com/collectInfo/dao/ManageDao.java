package com.collectInfo.dao;

import com.collectInfo.model.Manage;

public interface ManageDao {
    int deleteByPrimaryKey(Integer manageId);

    int insert(Manage record);

    int insertSelective(Manage record);

    Manage selectByPrimaryKey(Integer manageId);

    int updateByPrimaryKeySelective(Manage record);

    int updateByPrimaryKey(Manage record);
    
    int deleteManageByDeviceId(int deviceId);
    
    int updateByDeviceId(int deviceId, int userId);
}