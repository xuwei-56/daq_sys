package com.collectInfo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.service.IDataService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;

@Controller
@RequestMapping(value="/data")
public class DataController {
	@Resource
	private IDataService dataService;
    private static Logger logger= LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value="/getData")
	@ResponseBody
	public JSONObject getDataByIp_Datetime(String device_ip, String start_time,
			String end_time){
		try {
			if(device_ip==null||device_ip.equals("")){
				return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "设备ip不能为空", null);
			}
			logger.info(device_ip);
			List<HashMap<String,Object>> dataList = new ArrayList<HashMap<String,Object>>();
			dataList = dataService.getDataByIp_Datetime(device_ip, start_time, end_time);
			return  CommonUtil.constructResponse(EnumUtil.OK, "查询数据成功", dataList);
		} catch (Exception e) {
			throw e;
		}
		
	}
	

}
