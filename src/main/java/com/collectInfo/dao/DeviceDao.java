package com.collectInfo.dao;

import com.collectInfo.model.Device;

public interface DeviceDao {
    int deleteByPrimaryKey(Integer deviceId);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer deviceId);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
    
    /**
     * 根据ip地址得到设备信息
     * @param deviceIp
     * @return
     */
    Device getDeviceByIp(String deviceIp);
}