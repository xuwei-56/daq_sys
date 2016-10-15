package com.collectInfo.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.collectInfo.dao.DataDao;
import com.collectInfo.service.IDataService;

@Service("dataService")
public class DataServiceImpl implements IDataService {
	@Resource
	private DataDao dataDao;
	@Override
	public List<HashMap<String, Object>> getDataByIp_Date(String device_ip, String date) {
		// TODO Auto-generated method stub
		try {
			List<HashMap<String,Object>> data=dataDao.selectDataByIp_Date(device_ip, date);
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

}
