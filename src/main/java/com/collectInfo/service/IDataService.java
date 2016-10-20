package com.collectInfo.service;

import java.util.HashMap;
import java.util.List;

public interface IDataService {
	//根据设备号和时间查询数据
	List<HashMap<String,Object>> getDataByIp_Date(String device_ip,String date);
}
