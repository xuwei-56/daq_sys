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
import com.collectInfo.model.User;
import com.collectInfo.service.IDeviceService;
import com.collectInfo.service.IManageService;
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
    @Resource
    private IManageService manageService;
    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login")
	@ResponseBody
	public JSONObject userLogin(Integer userId,String password,String verifyCode, HttpSession session)  {
		try {
			if(userId == null || password == null||verifyCode ==null){
				logger.info("用户名密码验证码不能为空");
				return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "用户名密码不能为空", null);
			}
			if(!verifyCode.equals(session.getAttribute("verifyCode"))){
				return CommonUtil.constructResponse(0, "验证码错误", null);
			}
			logger.info("开始验证用户 "+ userId +" 是否存在");
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
    public JSONObject addUser(Integer userId,String userName ,String password,String phoneNumber,HttpSession session){
    	try {
    		//判断权限
    		logger.info("判断是否为超级管理员权限");
        	User curUser = (User) session.getAttribute("user");
        	if (curUser == null||curUser.getIsRoot()!=1) {
        		return CommonUtil.constructResponse(EnumUtil.NOT_POWER,"没有权限添加管理员", curUser);
    		}
    		
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
    public JSONObject editPassword(Integer userId,String password,String new_password,HttpSession session){
    	try{
    		logger.info("判断是否为超级管理员权限");
        	User curUser = (User) session.getAttribute("user");
        	if (curUser == null||curUser.getIsRoot()!=1) {
        		return CommonUtil.constructResponse(EnumUtil.NOT_POWER,"没有权限添加管理员", curUser);
    		}
    		if(userId==null||password==null||password.equals("")||new_password==null||new_password.equals("")){
        		logger.info("参数为空修改失败");
        		logger.info(userId +"  "+ password +"  "+ new_password);
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
    public JSONObject deleteUser(Integer userId,HttpSession session){
    	try {
    		logger.info("判断是否为超级管理员权限");
        	User user = (User) session.getAttribute("user");
        	if (user == null||user.getIsRoot()!=1) {
        		return CommonUtil.constructResponse(EnumUtil.NOT_POWER,"没有权限添加管理员", user);
    		}
    		User deleted_user =userService.getUserById(userId);
    		if(deleted_user==null){
    			logger.info("该用户不存在");
                return CommonUtil.constructResponse(EnumUtil.NO_DATA, "该用户不存在", null);
    		}
    		userService.deleteUser(userId);
    		manageService.afterDeleteUser(userId);
			logger.info("删除了用户:"+deleted_user.getUserName()+"  id:"+deleted_user.getUserId()+"  phone:"+deleted_user.getPhoneNumber());
            return CommonUtil.constructResponse(EnumUtil.OK,"删除用户成功", null);
    	} catch (Exception e) {
			// TODO: handle exception
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
    }
    @RequestMapping("/findUser")
	@ResponseBody
	public JSONObject findUser(String userName, String phoneNumber, String deviceIp){
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
    
    @RequestMapping("getSessionUser")
    @ResponseBody
    public JSONObject getSessionUser(HttpSession session){
    	logger.info("获取管理员登陆信息");
    	User user = (User) session.getAttribute("user");
    	if (user != null) {
    		logger.info(user.getUserName()+"成功");
    		return CommonUtil.constructResponse(EnumUtil.OK,"获取管理员登陆信息成功", user);
		}
    	return CommonUtil.constructResponse(EnumUtil.FALSE,"获取管理员登陆信息失败", null);
    }
    
    @RequestMapping("logout")
    @ResponseBody
    public JSONObject logout(HttpSession session){
    	logger.info("退出操作，销毁session");
    	session.removeAttribute("user");
    	if (session.getAttribute("user") == null) {
    		logger.info("成功");
    		return CommonUtil.constructResponse(EnumUtil.OK,"退出成功", null);
		}
    	return CommonUtil.constructResponse(EnumUtil.FALSE,"退出失败", null);
    }
    
    @RequestMapping(value = "/getUsers")
   	@ResponseBody
       public JSONObject getUsers(Integer pageSize,Integer page,HttpSession session){
       	try {
       		//判断权限
       		logger.info("判断是否为超级管理员权限");
           	User curUser = (User) session.getAttribute("user");
           	if (curUser == null||curUser.getIsRoot()!=1) {
          		return CommonUtil.constructResponse(EnumUtil.NOT_POWER,"没有权限查看所有管理员", curUser);
       		}
       		
       		List<User> userList = new ArrayList<User>();
       		userList = userService.getUsers(pageSize, page);
           	
   			return CommonUtil.constructResponse(EnumUtil.OK,"查看所有用户成功", userList);
   		} catch (Exception e) {
   			// TODO: handle exception
   			logger.error("数据库系统错误"+e);
   			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
   		}
       }
    
    @RequestMapping("judgeUserName")
    @ResponseBody
    public JSONObject judgeUserName(String userName){
    	logger.info("查询用户是否存在");
    	User user = null;
    	try {
			user = userService.getUserByUserName(userName);
			if (user != null) {
				logger.info("成功");
	    		return CommonUtil.constructResponse(EnumUtil.OK,"有该管理员", null);
			}
			return CommonUtil.constructResponse(EnumUtil.FALSE,"没有该管理员", null);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
    }
}  
