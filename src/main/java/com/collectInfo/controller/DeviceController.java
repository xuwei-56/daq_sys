package com.collectInfo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.model.Device;
import com.collectInfo.model.Manage;
import com.collectInfo.model.User;
import com.collectInfo.service.IDeviceService;
import com.collectInfo.service.IManageService;
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
	
	@Resource
	private IManageService manageService;
	
	private static Logger logger= LoggerFactory.getLogger(DeviceController.class);
	
	
	@RequestMapping("/getDevice")
	@ResponseBody
	public JSONObject getDevice(HttpSession session, Integer offset, Integer pageSize){
		logger.info("开始调用getDevice");
		User user = (User) session.getAttribute("user");
		if (offset == null) {
			offset = 1;
		}
		if (pageSize == null) {
			pageSize = EnumUtil.PAGE_SIZE;
		}
		List<HashMap<String, Object>> deviceList = null;
		int count = 0;
		try {
			if (user.getIsRoot() == 1) {
				logger.info("超级管理员获取设备列表");
				deviceList = deviceService.getDevice((offset-1)*pageSize, pageSize);
				count = deviceService.getDeviceCount();
				deviceList.get(0).put("count", count);
			} else {
				logger.info("初级管理员获取设备列表");
				deviceList = deviceService.getDeviceByUserName(user.getUserName(), (offset-1)*pageSize, pageSize);
				count = deviceService.getDeviceCountByUserId(user.getUserId());
				deviceList.get(0).put("count", count);
			}
			return CommonUtil.constructResponse(EnumUtil.OK, "查询成功", deviceList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			//System.out.println(e);
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	
	@RequestMapping("/findDevice")
	@ResponseBody
	public JSONObject findDevice(String deviceIp, String address, String userName, Integer offset, Integer pageSize, HttpSession session){
		logger.info("开始调用findDevice");
		if (userName == null && deviceIp == null && address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "请输入对应值", null);
		}
		if (offset == null) {
			offset = 1;
		}
		if (pageSize == null) {
			pageSize = EnumUtil.PAGE_SIZE;
		}
		User user = (User) session.getAttribute("user");
		ArrayList<HashMap<String, Object>> device = new ArrayList<>();
		if (deviceIp != null) {
			try {
				HashMap<String, Object> devicetemp = new HashMap<>();
				devicetemp = deviceService.getDeviceByIp(deviceIp);
				if (devicetemp.size() == 0 || devicetemp == null) {
					return CommonUtil.constructResponse(EnumUtil.NO_DATA, "没有查找到对应数据数据", null);
				}
				devicetemp.put("count", 1);
				device.add(devicetemp);
				logger.info("根据IP为"+deviceIp+"查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
				
			}
		}
		if (address != null) {
			try {
				int count = 0;
				if (user.getIsRoot() == 1) {
					device = deviceService.getDeviceByAddress(address, (offset-1)*pageSize, pageSize);
					count = deviceService.getDeviceCountByAddress(address);
				} else {
					device = deviceService.getDeviceByUserIdAndAddress(address, user.getUserId(), offset, pageSize);
					count = deviceService.getDeviceCountByUserIdAndAddress(address, user.getUserId());
				}
				if (device.size() == 0 || device == null) {
					return CommonUtil.constructResponse(EnumUtil.NO_DATA, "没有查找到对应数据数据", null);
				}
				device.get(0).put("count", count);
				logger.info("根据address查询成功"+ offset+""+ pageSize+""+device.size());
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
			}
		}
		if (userName != null) {
			try {
				device = deviceService.getDeviceByUserName(userName, (offset-1)*pageSize, pageSize);
				if (device.size() == 0 || device == null) {
					return CommonUtil.constructResponse(EnumUtil.NO_DATA, "没有查找到对应数据数据", null);
				}
				int count = deviceService.getDeviceCountByUserName(userName);
				device.get(0).put("count", count);
				logger.info("根据userName查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", device);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
			}
		}
		return CommonUtil.constructResponse(EnumUtil.FALSE,"未知原因，查询失败", null);
	}
	
	@RequestMapping("/setDevice")
	@ResponseBody
	public JSONObject setDevice(String deviceIp, String address, String userName){
		logger.info("开始调用setDevice");
		if (deviceIp == null || address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "请输入对应值", null);
		}
		User user = userService.getUserByUserName(userName);
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NO_DATA, "没有该管理员", null);
		}
		Device device = new Device();
		device.setAddress(address);
		device.setDeviceIp(deviceIp);
		try {
			if (deviceService.getDeviceByIp(deviceIp) == null) {
				int temp = deviceService.setDevice(device);
				if (temp > 0) {
					Manage manage = new Manage();
					manage.setDeviceId(device.getDeviceId());
					manage.setUserId(user.getUserId());
					if (manageService.insertManage(manage) > 0) {
						logger.info("请求成功");
						return CommonUtil.constructResponse(EnumUtil.OK, "success", null);
					}
				}
				logger.info("添加失败");
				return CommonUtil.constructResponse(EnumUtil.FALSE,"未知原因，添加失败", null);
			}
			logger.info("添加失败，重复ip");
			return CommonUtil.constructResponse(EnumUtil.REPEAT,"此IP设备已添加，无需重复添加", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
					
		}
	}
	
	@RequestMapping("/updateDevice")
	@ResponseBody
	public JSONObject updateDevice(Integer deviceId, String deviceIp, String address, String userName){
		logger.info("开始调用updateDevice");
		if (deviceId == null || deviceIp == null || address == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "请输入对应值", null);
		}
		User user = userService.getUserByUserName(userName);
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NO_DATA, "没有该管理员", null);
		}
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceIp(deviceIp);
		device.setAddress(address);
		try {
			int temp = deviceService.updateDevice(device);
			if (temp > 0) {
				int i = manageService.updateManageByDeviceId(deviceId, user.getUserId());
				if (i > 0) {
					logger.info("请求成功");
					return CommonUtil.constructResponse(EnumUtil.OK, "success", null);
				}
			}
			logger.info("更新失败");
			return CommonUtil.constructResponse(EnumUtil.FALSE,"未知原因，更新失败", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	@RequestMapping("/deleteDevice")
	@ResponseBody
	public JSONObject deleteDevice(Integer deviceId){
		logger.info("删除设备");
		try {
			int device = deviceService.deleteDevice(deviceId);
			int manage = manageService.deleteManage(deviceId);
			if (device > 0 && manage > 0) {
				logger.info("请求成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", null);
			}
			logger.info("删除失败");
			return CommonUtil.constructResponse(EnumUtil.FALSE,"未知原因，删除失败", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	@RequestMapping("/judgeDeviceIp")
	@ResponseBody
	public JSONObject judgeDeviceIp(String deviceIp){
		logger.info("判断ip是否存在");
		try {
			if (deviceService.getDeviceByIp(deviceIp) == null) {
				logger.info("根据IP查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "不存在此IP地址设备", null);
			}
			return CommonUtil.constructResponse(EnumUtil.FALSE, "存在此IP地址设备", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	@RequestMapping("/judgeAuthrity")
	@ResponseBody
	public JSONObject judgeAuthrity(HttpSession session, String deviceIp){
		logger.info("判断登录管理员是否管理此设备");
		try {
			User user = (User) session.getAttribute("user");
			if (user.getIsRoot() == 1) {
				if (deviceService.getDeviceByIp(deviceIp) == null) {
					return CommonUtil.constructResponse(EnumUtil.FALSE, "不存在此IP地址设备", null);
				}
				return CommonUtil.constructResponse(EnumUtil.OK, "具有权限", null);
			} else {
				HashMap<String, Object> userTemp = deviceService.getUserByDeviceIp(deviceIp);
				if (user.getUserId() == userTemp.get("user_id")) {
					return CommonUtil.constructResponse(EnumUtil.OK, "具有权限", null);
				}
				return CommonUtil.constructResponse(EnumUtil.FALSE, "没有权限", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
}
