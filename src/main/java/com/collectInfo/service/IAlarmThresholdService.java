package com.collectInfo.service;

import com.collectInfo.model.AlarmThreshold;


public interface IAlarmThresholdService {
	public AlarmThreshold selectAlarmThreshold();
	public void setAlarmThreshold(AlarmThreshold at);
}
