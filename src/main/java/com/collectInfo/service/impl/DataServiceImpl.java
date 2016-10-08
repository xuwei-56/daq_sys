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
	public List<HashMap<String, Object>> getDataByIp_Datetime(String device_ip, String start_time,
			String end_time,int pageNumber) {
		// TODO Auto-generated method stub
		try {
			return dataDao.selectDataByIp_Datetime(device_ip, start_time, end_time,(pageNumber-1)*10);
		} catch (Exception e) {
			throw e;
		}
	}

}
