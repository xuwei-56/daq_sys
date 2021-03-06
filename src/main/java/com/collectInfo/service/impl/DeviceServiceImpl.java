package com.collectInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

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
	public ArrayList<HashMap<String, Object>> getDeviceByAddress(String address, int offset, int pageSize) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceByAddress(address, offset, pageSize);
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


	@Override
	public ArrayList<HashMap<String, Object>> getDeviceByUserName(String userName, int offset, int pageSize) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceByUserName(userName, offset, pageSize);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDevice(int offset, int pageSize) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDevice(offset, pageSize);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int getDeviceCountByUserId(int userId) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceCountByUserId(userId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int getDeviceCount() {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceCount();
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int getDeviceCountByAddress(String address) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceCountByAddress(address);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int getDeviceCountByUserName(String userName) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceCountByUserName(userName);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDeviceByUserIdAndAddress(String address, int userId, int offset,
			int pageSize) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceByUserIdAndAddress(address, userId, offset, pageSize);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int getDeviceCountByUserIdAndAddress(String address, int userId) {
		// TODO Auto-generated method stub
		try {
			return deviceDao.getDeviceCountByUserIdAndAddress(address, userId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	
}
