package com.collectInfo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.collectInfo.dao.DeviceDao;
import com.collectInfo.model.Device;
import com.collectInfo.service.IDeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements IDeviceService{
	
	@Resource
	private DeviceDao deviceDao;

	@Override
	public Device getDeviceByIp(String deviceIp) {
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

	
}
