package com.collectInfo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
	@RequestMapping(value="/addReport")
	@ResponseBody
	public JSONObject addReport(String device_ip,String date,HttpSession session){
		try {
			List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
			Set<String> test = new HashSet<String>();
			if(session.getAttribute("report")==null){
				report.addAll(dataService.getDataByIp_Date(device_ip, date));
				test.add(device_ip+date);
				logger.info("产生了一张报表并添加了数据");
			}else{
				test = (Set<String>)session.getAttribute("test");
				if(!test.contains(device_ip+date)){
					report=(List<HashMap<String, Object>>) session.getAttribute("report");
					report.addAll(dataService.getDataByIp_Date(device_ip, date));
					logger.info("向报表中添加了数据");				
					}else{
						logger.info("报表中已存在该数据");
					}
			}
			session.setAttribute("report", report);
			session.setAttribute("test", test);
			return CommonUtil.constructResponse(EnumUtil.OK, "成功", null);
		} catch (Exception e) {
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	@RequestMapping(value="/addReports")
	@ResponseBody
	public JSONObject addReports(String device_ip,String startDate,String endDate,HttpSession session){
		try {
			List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
			Set<String> test = new HashSet<String>();
			if(session.getAttribute("report")==null){
				for(Integer date=Integer.parseInt(startDate);date<Integer.parseInt(endDate)+1;date++){
					report.addAll(dataService.getDataByIp_Date(device_ip, date.toString()));
					test.add(device_ip+date);
				}	
			}else{
				test = (Set<String>)session.getAttribute("test");
				report=(List<HashMap<String, Object>>) session.getAttribute("report");
				for(Integer date=Integer.parseInt(startDate);date<Integer.parseInt(endDate)+1;date++){	
					if(!test.contains(device_ip+date)){
						report.addAll(dataService.getDataByIp_Date(device_ip, date.toString()));
						test.add(device_ip+date);
					}else{
						logger.info("报表中已存在该数据");
					}
				}
			}
			session.setAttribute("report", report);
			session.setAttribute("test", test);
			logger.info("产生了一张报表并添加了数据");
			return CommonUtil.constructResponse(EnumUtil.OK, "已将该时间段数据加入报表", null);
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
	
	@RequestMapping(value="/isInReport")
	@ResponseBody
	public JSONObject isInReport(String device_ip,String date,HttpSession session){
		boolean isInReport=false;
		Set<String> test = new HashSet<String>();
		System.out.println(session.getAttribute("test"));
		System.out.println(session.getAttribute("report"));
		if((Set<String>)session.getAttribute("test")!=null){
			logger.info("得到了test");
			test = (Set<String>)session.getAttribute("test");
			for(String a:test){
				System.out.println(a);
			}
			if(test.contains(device_ip+date)){
				return CommonUtil.constructResponse(EnumUtil.FALSE, "已经添加到报表，无需重复添加", null);
			}
		}
		logger.info("判断该数据是否存在于报表");
		return CommonUtil.constructResponse(EnumUtil.OK, "可以添加到报表", null);
	}
	
	
	/**
	 * 导出报表中的数据到Excel
	 * @param session
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getExcel")
	@ResponseBody
	public JSONObject getExcel(HttpSession session,HttpServletResponse response) throws IOException{
			logger.info("生成了一次Excel");
			List<HashMap<String,Object>> report =(List<HashMap<String,Object>>) session.getAttribute("report");
			if(report==null){
				return CommonUtil.constructResponse(EnumUtil.OK, "报表为空",true);
			}
			PoiUtil.getExcel(report, response);
			session.removeAttribute("report");
			session.removeAttribute("test");
		return CommonUtil.constructResponse(EnumUtil.OK, "成功生成了报表",true);
	}
	
	
	/**
	 * 生成当天数据的Excel
	 * @param device_ip
	 * @param date
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getExcelA")
	@ResponseBody
	public JSONObject getExcelA(String device_ip,String date,HttpSession session,HttpServletResponse response){
		logger.info("生成一张报表");
		try {
				List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
				report.addAll(dataService.getDataByIp_Date(device_ip, date));
				PoiUtil.getExcel(report,response);
			return CommonUtil.constructResponse(EnumUtil.OK, "生成了该天的Excel", null);
		} catch (Exception e) {
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * 生成一段时间数据的Excel
	 * @param device_ip
	 * @param date
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getExcelB")
	@ResponseBody
	public JSONObject getExcelB(String device_ip,String startDate,String endDate,HttpServletResponse response){
		logger.info("生成一张报表");
		try {
				List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
				for(Integer date=Integer.parseInt(startDate);date<Integer.parseInt(endDate)+1;date++){
					report.addAll(dataService.getDataByIp_Date(device_ip, date.toString()));
				}	
				PoiUtil.getExcel(report,response);
			return CommonUtil.constructResponse(EnumUtil.OK, "生成了一段时间的Excel", null);
		} catch (Exception e) {
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * 添加最后一段时间的数据并生成Excel
	 */
	@RequestMapping(value="/addAndGet")
	@ResponseBody
	public JSONObject addAndGet(String device_ip,String startDate,String endDate,HttpSession session,HttpServletResponse response){
		logger.info("生成一张报表");
		try {
			if(endDate==null||endDate.equals("")){
				endDate=startDate;
			}
			List<HashMap<String,Object>> report = new ArrayList<HashMap<String,Object>>();
			Set<String> test = new HashSet<String>();
			if(session.getAttribute("report")==null){
				for(Integer date=Integer.parseInt(startDate);date<Integer.parseInt(endDate)+1;date++){
					report.addAll(dataService.getDataByIp_Date(device_ip, date.toString()));
					test.add(device_ip+date);
				}	
			}else{
				test = (Set<String>)session.getAttribute("test");
				report=(List<HashMap<String, Object>>) session.getAttribute("report");
				for(Integer date=Integer.parseInt(startDate);date<Integer.parseInt(endDate)+1;date++){	
					if(!test.contains(device_ip+date)){
						report.addAll(dataService.getDataByIp_Date(device_ip, date.toString()));
						test.add(device_ip+date);
					}else{
						continue;	
					}
				}
			}
			session.setAttribute("report", report);
			session.setAttribute("report_itmes", test);	
			report=(List<HashMap<String, Object>>) session.getAttribute("report");
			PoiUtil.getExcel(report,response);
			return CommonUtil.constructResponse(EnumUtil.OK, "添加了一段时间的数据并生成Excel", null);
		} catch (Exception e) {
			logger.error("数据库系统错误"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
}
