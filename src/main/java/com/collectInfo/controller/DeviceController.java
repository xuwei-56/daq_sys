package com.collectInfo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.model.Device;
import com.collectInfo.service.IDeviceService;
import com.collectInfo.service.IUserService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;

@Controller
@RequestMapping("/device")
public class DeviceController {

	@Resource
	private IDeviceService deviceService;
	
	@Resource
	private IUserService userService;
	
	private static Logger logger= LoggerFactory.getLogger(DeviceController.class);
	
	@RequestMapping("/getDevice")
	@ResponseBody
	public JSONObject getDevice(String deviceIp, String address, String userName){
		logger.info("开始调用getDevice");
		if (userName == null && deviceIp == null && address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "false", null);
		}
		ArrayList<HashMap<String, Object>> device = new ArrayList<>();
		if (deviceIp != null) {
			try {
				HashMap<String, Object> devicetemp = deviceService.getDeviceByIp(deviceIp);
				device.add(devicetemp);
				logger.info("根据IP查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
				
			}
		}
		if (address != null) {
			try {
				device = deviceService.getDeviceByAddress(address);
				logger.info("根据address查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
			}
		}
		if (userName != null) {
			try {
				device = userService.getDeviceByUserName(userName);
				logger.info("根据userName查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
			}
		}
		return CommonUtil.constructResponse(EnumUtil.FALSE,"false", null);
	}
	
	@RequestMapping("/setDevice")
	@ResponseBody
	public JSONObject setDevice(String deviceIp, String address){
		logger.info("开始调用setDevice");
		if (deviceIp == null || address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "false", null);
		}
		Device device = new Device();
		device.setAddress(address);
		device.setDeviceIp(deviceIp);
		try {
			if (deviceService.getDeviceByIp(deviceIp) == null) {
				int temp = deviceService.setDevice(device);
				if (temp > 0) {
					logger.info("请求成功");
					return CommonUtil.constructResponse(EnumUtil.OK, "success", null);
				}
				logger.info("添加失败");
				return CommonUtil.constructResponse(EnumUtil.FALSE,"false", null);
			}
			logger.info("添加失败，重复ip");
			return CommonUtil.constructResponse(EnumUtil.REPEAT,"false", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
					
		}
	}
	
	@RequestMapping("/updateDevice")
	@ResponseBody
	public JSONObject updateDevice(Integer deviceId, String deviceIp, String address){
		logger.info("开始调用updateDevice");
		if (deviceId == null || deviceIp == null || address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "false", null);
		}
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceIp(deviceIp);
		device.setAddress(address);
		try {
			int temp = deviceService.updateDevice(device);
			if (temp > 0) {
				logger.info("请求成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", null);
			}
			logger.info("添加失败");
			return CommonUtil.constructResponse(EnumUtil.FALSE,"false", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
		}
	}
	
	
}
