package com.collectInfo.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.collectInfo.model.Device;

public interface IDeviceService {

	/**
	 * 根据ip地址得到设备信息
	 * @param deviceIp
	 * @return
	 */
	public HashMap<String, Object> getDeviceByIp(String deviceIp);
	
	/**
	 * 设置设备信息 ip+地址
	 * @param device
	 * @return
	 */
	public int setDevice(Device device);
	
	/**
	 * 根据id删除设备
	 * @param deviceId
	 * @return
	 */
	public int deleteDevice(int deviceId);
	
	/**
	 * 修改设备信息 根据id
	 * @param device
	 * @return
	 */
	public int updateDevice(Device device);
	
	/**
	 * 根据地址得到相关设备信息
	 * @param address
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getDeviceByAddress(String address);

	/**
	 * 根据设备ip得到用户信息
	 * @param deviceIp
	 * @return
	 */
	public HashMap<String, Object> getUserByDeviceIp(String deviceIp);
}
