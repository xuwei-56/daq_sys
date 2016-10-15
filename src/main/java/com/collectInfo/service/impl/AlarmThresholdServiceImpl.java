package com.collectInfo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.collectInfo.dao.AlarmThresholdDao;
import com.collectInfo.model.AlarmThreshold;
import com.collectInfo.service.IAlarmThresholdService;

@Service("AlarmThresholdService")
public class AlarmThresholdServiceImpl implements IAlarmThresholdService {
	
	@Resource
	private AlarmThresholdDao alarmThresholdDao;

	@Override
	public AlarmThreshold selectAlarmThreshold() {

		return alarmThresholdDao.selectAlarmThreshold();
	}

	@Override
	public void setAlarmThreshold(AlarmThreshold at) {
		alarmThresholdDao.setAlarmThreshold(at);

	}

}
