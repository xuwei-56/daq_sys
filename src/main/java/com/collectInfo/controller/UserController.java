package com.collectInfo.controller;

import java.util.HashMap;

import javax.annotation.Resource;  
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.model.User;
import com.collectInfo.service.IDeviceService;
import com.collectInfo.service.IUserService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;
import com.collectInfo.util.MD5;  
  
  
@Controller  
@RequestMapping("/user")  
public class UserController {  
    @Resource  
    private IUserService userService;
    @Resource
    private IDeviceService deviceService;
    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login")
	@ResponseBody
	public JSONObject userLogin(Integer userId,String password, HttpSession session)  {
		try {
			if(userId == null || password == null){
				logger.info("用户名密码不能为空");
				return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "用户名密码不能为空", null);
			}
			logger.info("开始验证用户是否存在");
			User user = userService.getUserById(userId);
			if (user == null) {
				logger.info("该用户不存在");
                return CommonUtil.constructResponse(EnumUtil.NO_DATA, "该用户不存在", null);
            }			
			if (password != null && MD5.MD5Encode(password, "utf-8").equals(user.getPassword())) {
                user.setPassword(null);
                session.setAttribute("user", user);
				logger.info("登录成功");
                return CommonUtil.constructResponse(EnumUtil.OK,"登录成功", null);
            } else {
            	logger.info("登陆失败密码错误");
                return CommonUtil.constructResponse(EnumUtil.PASSWORD_ERROR, "密码错误", null);
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
    
    @RequestMapping(value = "/addUser")
	@ResponseBody
    public JSONObject addUser(Integer userId,String userName ,String password,String phoneNumber){
    	try {
    		if(userId == null || userName == null ||userName.equals("")||password == null
    				||password.equals("")||phoneNumber ==null||phoneNumber.equals("")){
    			logger.info("参数为空注册失败");
    			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "参数为空", null);
    		}
    		logger.info("验证用户是否已被注册");
			User user = userService.getUserById(userId);
			if(user != null){
				logger.info("该id已存在，不能被再次注册");
				return CommonUtil.constructResponse(EnumUtil.REPEAT, "id已存在不能被再次注册", null);
			}
			user = new User();
			user.setUserId(userId);
			user.setUserName(userName);
			user.setPassword(MD5.MD5Encode(password, "utf-8"));
			user.setPhoneNumber(phoneNumber);
			userService.addUser(user);
			logger.info("用户添加成功");
			return CommonUtil.constructResponse(EnumUtil.OK,"添加用户成功", null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
    }
    
    @RequestMapping(value = "/editPassword")
	@ResponseBody
    public JSONObject editPassword(Integer userId,String password,String new_password){
    	try{
    		if(userId==null||password==null||password.equals("")||new_password==null||new_password.equals("")){
        		logger.info("参数为空修改失败");
    			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "参数为空", null);
        	}
        	User user = userService.getUserById(userId);
        	if (MD5.MD5Encode(password, "utf-8").equals(user.getPassword())) {
                user.setPassword(MD5.MD5Encode(new_password, "utf-8"));
                userService.editUser(user);
    			logger.info("密码修改成功");
                return CommonUtil.constructResponse(EnumUtil.OK,"密码修改成功", null);
            }else{
            	logger.info("修改失败,密码错误");
                return CommonUtil.constructResponse(EnumUtil.PASSWORD_ERROR, "密码错误", null);	
            }
    	}catch(Exception e){
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
    	}
    }
    
    @RequestMapping(value = "/editPhoneNumber")
   	@ResponseBody
       public JSONObject addUser(Integer userId,String password,String new_phoneNumber){
    	try {
    		if(userId==null||password==null||password.equals("")||new_phoneNumber==null||new_phoneNumber.equals("")){
           		logger.info("参数为空修改失败");
       			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "参数为空", null);
           	}
           	User user = userService.getUserById(userId);
           	if (MD5.MD5Encode(password, "utf-8").equals(user.getPassword())) {
                   user.setPhoneNumber(new_phoneNumber);
                   userService.editUser(user);
       			logger.info("密码手机号成功");
                   return CommonUtil.constructResponse(EnumUtil.OK,"手机号修改成功", null);
               }else{
               	logger.info("修改失败,密码错误");
                   return CommonUtil.constructResponse(EnumUtil.PASSWORD_ERROR, "密码错误", null);	
               }
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
       }
    @RequestMapping(value="/deleteUser")
    @ResponseBody
    public JSONObject deleteUser(Integer userId){
    	try {		
    		User deleted_user =userService.getUserById(userId);
    		if(deleted_user==null){
    			logger.info("该用户不存在");
                return CommonUtil.constructResponse(EnumUtil.NO_DATA, "该用户不存在", null);
    		}
    		userService.deleteUser(userId);
			logger.info("删除了用户:"+deleted_user.getUserName()+"  id:"+deleted_user.getUserId()+"  phone:"+deleted_user.getPhoneNumber());
            return CommonUtil.constructResponse(EnumUtil.OK,"删除用户成功", null);
    	} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
    }
    @RequestMapping("/getUser")
	@ResponseBody
	public JSONObject getUser(String userName, String phoneNumber, String deviceIp){
		logger.info("开始调用getUser");
		if (userName == null && phoneNumber == null && deviceIp == null) {
			logger.info("请求失败，参数为空");
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "参数为空，请求失败", null);
		}
		User user = null;
		if (userName != null) {
			try {
				user = userService.getUserByUserName(userName);
				logger.info("根据用户名查询成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", user);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
				
			}
		}
		if (phoneNumber != null) {
			try {
				user = userService.getUserByPhoneNumber(phoneNumber);
				logger.info("根据手机号查询用户成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", user);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "false", null);
			}
		}
		if (deviceIp != null) {
			try {
				HashMap<String, Object> userMap = deviceService.getUserByDeviceIp(deviceIp);
				logger.info("根据deviceIp查询用户成功");
				return CommonUtil.constructResponse(EnumUtil.OK, "success", userMap);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.toString());
				return CommonUtil.constructExceptionJSON(EnumUtil.SYSTEM_ERROR, "系统错误", null);
			}
		}
		return CommonUtil.constructResponse(EnumUtil.FALSE,"false", null);
	}
}  
