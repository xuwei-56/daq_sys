package com.collectInfo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.collectInfo.dao.ManageDao;
import com.collectInfo.model.Manage;
import com.collectInfo.service.IManageService;

@Service("manageService")
public class ManageServiceImpl implements IManageService{

	@Resource
	private ManageDao manageDao;

	@Override
	public int insertManage(Manage manage) {
		// TODO Auto-generated method stub
		try {
			return manageDao.insert(manage);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int updateManage(Manage manage) {
		// TODO Auto-generated method stub
		try {
			return manageDao.updateByPrimaryKeySelective(manage);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public int deleteManage(int deviceId) {
		// TODO Auto-generated method stub
		try {
			return manageDao.deleteManageByDeviceId(deviceId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
}
