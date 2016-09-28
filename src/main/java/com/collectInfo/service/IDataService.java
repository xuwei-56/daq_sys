package com.collectInfo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

public interface IDataService {
	//根据设备号和时间查询数据
	List<HashMap<String,Object>> getDataByIp_Datetime(String device_ip,String start_time,String end_time);
}
