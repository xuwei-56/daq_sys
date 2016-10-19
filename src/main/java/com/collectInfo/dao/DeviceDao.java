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
    ArrayList<HashMap<String, Object>> getDeviceByAddress(String address, int offset, int pageSize);
    /**
     * 根据设备ip得到用户
     * @param deviceIp
     * @return
     */
	HashMap<String, Object> getUserByDeviceIp(String deviceIp);
	
	/**
     * 根据用户名得到设备信息
     * @param userName
     * @return
     */
    ArrayList<HashMap<String, Object>> getDeviceByUserName(String userName, int offset, int pageSize);
    
    /**
     * 根据用户id得到设备
     * @param userId
     * @param offset
     * @param pageSize
     * @return
     */
    public ArrayList<HashMap<String, Object>> getDevice(int offset, int pageSize);
    
    /**
     * 得到设备总数或得到某管理员管理的设备总数
     * @param userId
     * @return
     */
    public int getDeviceCountByUserId(int userId);
    
    /**
     * 得到设备总数或得到某管理员管理的设备总数
     * @param userId
     * @return
     */
    public int getDeviceCount();
    
}