package com.collectInfo.dao;

import java.util.ArrayList;
import java.util.HashMap;

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
    HashMap<String, Object> getDeviceByIp(String deviceIp);
    
    /**
     * 根据地址得到设备信息
     * @param address
     * @return
     */
    ArrayList<HashMap<String, Object>> getDeviceByAddress(String address);
    
    
}