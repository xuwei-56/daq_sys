package com.collectInfo.dao;

import com.collectInfo.model.AlarmThreshold;

public interface AlarmThresholdDao {
	public AlarmThreshold selectAlarmThreshold();
	public void setAlarmThreshold(AlarmThreshold at);
}
