package com.collectInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.collectInfo.controller.UserController;
import com.collectInfo.dao.DeviceDao;
import com.collectInfo.model.Device;
import com.collectInfo.service.IDeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements IDeviceService{
	
	@Resource
	private DeviceDao deviceDao;

	@Override
	public HashMap<String, Object> getDeviceByIp(String deviceIp) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceByIp(deviceIp);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int setDevice(Device device) {
		// TODO Auto-generated method stub
		
		try {
			return deviceDao.insert(device);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int deleteDevice(int deviceId) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.deleteByPrimaryKey(deviceId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int updateDevice(Device device) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.updateByPrimaryKeySelective(device);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDeviceByAddress(String address) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceByAddress(address);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public HashMap<String, Object> getUserByDeviceIp(String deviceIp) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getUserByDeviceIp(deviceIp);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	
}
