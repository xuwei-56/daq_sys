package com.collectInfo.controller;

import javax.annotation.Resource;  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.model.User;
import com.collectInfo.service.IUserService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;
import com.collectInfo.util.MD5;  
  
  
@Controller  
@RequestMapping("/user")  
public class UserController {  
    @Resource  
    private IUserService userService;
    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login")
	@ResponseBody
	public JSONObject userLogin(Integer userId,String password, HttpSession session)  {
		try {
			logger.info("开始验证用户是否存在");
			User user = userService.getUserById(userId);
			if (user == null) {
				logger.info("该用户未注册，user={}"+user);
                return CommonUtil.constructResponse(EnumUtil.NO_DATA, "该账号没有注册", null);
            }			
			if (password != null && password.equals(user.getPassword())) {
                user.setPassword(null);
                session.setAttribute("user", user);
				logger.info("登录成功，user="+user);
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
}  
