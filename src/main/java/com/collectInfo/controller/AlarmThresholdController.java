package com.collectInfo.controller;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.model.AlarmThreshold;
import com.collectInfo.service.IAlarmThresholdService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;

@Controller
@RequestMapping(value="/alarm")
public class AlarmThresholdController {
	@Resource
	private IAlarmThresholdService alarmThroldService;
    private static Logger logger= LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping("/select")
    @ResponseBody
	public JSONObject selectAlarmThreshold(){
    	try {
        	AlarmThreshold at = alarmThroldService.selectAlarmThreshold();
        	logger.info("查询了一次警报阈值数据");
        	return  CommonUtil.constructResponse(EnumUtil.OK, "查询数据成功", at);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
    }
    
    
    @RequestMapping("/set")
    @ResponseBody
	public JSONObject setAlarmThreshold(AlarmThreshold at){
    	try {
    		alarmThroldService.setAlarmThreshold(at);
    		logger.info("修改了警报阈值数据");
    		at = alarmThroldService.selectAlarmThreshold();
        	return  CommonUtil.constructResponse(EnumUtil.OK, "修改警报阈值数据成功", at);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
    }
    
    

}
