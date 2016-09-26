package com.collectInfo.service;

import com.collectInfo.model.Device;

public interface IDeviceService {

	/**
	 * 根据ip地址得到设备信息
	 * @param deviceIp
	 * @return
	 */
	public Device getDeviceByIp(String deviceIp);
	
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
}
