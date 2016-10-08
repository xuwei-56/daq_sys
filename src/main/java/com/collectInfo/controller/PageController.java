package com.collectInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.util.AuthCodeUtil;
import com.collectInfo.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

@Controller
public class PageController {


	/**
	 * 前端页面路径:主页
	 */
	@RequestMapping("/")
	public String index() {
		return "login";
	}
	
	

	/**
	 * 获取图片验证码
	 * @return 图片流
	 */
	@RequestMapping(value="code/getAuthCode")
	public void getPictureVerificationCode(HttpServletResponse response, HttpSession session,String type)throws Exception{
		if (type == null){
			return;
		}
		AuthCodeUtil authCodeUtil = AuthCodeUtil.Instance();
		String pictureVerificationCode = authCodeUtil.getString();
		ByteArrayInputStream image = authCodeUtil.getImage();
		session.setAttribute(type, pictureVerificationCode);
		OutputStream stream = response.getOutputStream();
		byte[] data = new byte[image.available()];
		image.read(data);
		stream.write(data);
		stream.flush();
		stream.close();
	}

	/**
	 * 图片验证码验证接口
	 */
	@RequestMapping("code/checkAuthcode")
	@ResponseBody
	public JSONObject checkAuthcode(HttpSession session,String type,String code){
		if (type == null || "".equals(type) || code==null || "".equals(code))
			return CommonUtil.constructResponse(0,"参数错误",null);
		Object sessionCode = session.getAttribute(type);
		if (code.equals(sessionCode)){
			session.removeAttribute(type);
			return CommonUtil.constructResponse(1,null,null);
		}else {
			return CommonUtil.constructResponse(0,"验证码错误",null);
		}

	}
	
	
	
	/*
	 * 返回下载页面
	 */
	@RequestMapping("download")
	public String download(){
		return "download";
	}

}