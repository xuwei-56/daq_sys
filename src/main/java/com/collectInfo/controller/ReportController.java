package com.collectInfo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.collectInfo.service.IDataService;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;
import com.collectInfo.util.PoiUtil;

@Controller
@RequestMapping(value="/report")
public class ReportController {
    @Resource
    private IDataService dataService;
    private static Logger logger= LoggerFactory.getLogger(ReportController.class);
    
	@RequestMapping(value="/add")
	@ResponseBody
	public JSONObject report(String device_ip,String date,HttpSession session){
		try {
			if(session.getAttribute("report")==null){
				List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
				report.addAll(dataService.getDataByIp_Date(device_ip, date));
				session.setAttribute("report", report);
				logger.info("产生了一张报表并添加了数据");
			}else{
				List<HashMap<String,Object>> report=(List<HashMap<String, Object>>) session.getAttribute("report");
				report.addAll(dataService.getDataByIp_Date(device_ip, date));
				logger.info("向报表中添加了数据");
			}
			return CommonUtil.constructResponse(EnumUtil.OK, "已将该天数据加入报表", null);
		} catch (Exception e) {
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
		
	}
	@RequestMapping(value="/getReport")
	@ResponseBody
	public JSONObject getReport(HttpSession session){
		logger.info("查看了报表");
			return CommonUtil.constructResponse(EnumUtil.OK, "查看了报表", (List<HashMap<String,Object>>) session.getAttribute("report"));
	}
	@RequestMapping(value="/getExcel")
	@ResponseBody
	public JSONObject getExcel(HttpSession session,HttpServletResponse response) throws IOException{
			logger.info("生成了一次Excel");
		return CommonUtil.constructResponse(EnumUtil.OK, "成功生成了报表",PoiUtil.getExcel((List<HashMap<String,Object>>)session.getAttribute("report"), response));
	}
	
}
