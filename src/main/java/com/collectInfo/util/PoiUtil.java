package com.collectInfo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class PoiUtil {

	public static boolean getExcel(List<HashMap<String,Object>> report,HttpServletResponse response) throws IOException{
	//创建HSSFWorkbook对象(excel的文档对象)
	HSSFWorkbook wb = new HSSFWorkbook();
	//建立新的sheet对象（excel的表单）
	HSSFSheet sheet=wb.createSheet("设备数据报表");
	//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
	HSSFRow row1=sheet.createRow(0);
	//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
	HSSFCell cell=row1.createCell(0);
	      //设置单元格内容
	cell.setCellValue("设备数据报表");
	//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
	sheet.addMergedRegion(new CellRangeAddress(0,0,0,7));
	//在sheet里创建第二行
	HSSFRow row2=sheet.createRow(1);
	      //创建单元格并设置单元格内容
	      row2.createCell(0).setCellValue("设备ip");
	      row2.createCell(1).setCellValue("时间");    
	      row2.createCell(2).setCellValue("脉冲电流");
	      row2.createCell(3).setCellValue("累计脉冲数");
	      row2.createCell(4).setCellValue("电压");
	      row2.createCell(5).setCellValue("阻性电流");
	      //在sheet里创建第三行
	      for(int i=0;i<report.size();i++){
	    	  HSSFRow row = sheet.createRow(2+i);
	    	  row.createCell(0).setCellValue(report.get(i).get("device_ip").toString());
		      row.createCell(1).setCellValue(report.get(i).get("generate_time").toString());    
		      row.createCell(2).setCellValue(report.get(i).get("pulse_current").toString()+"uA");
		      row.createCell(3).setCellValue(report.get(i).get("pulse_accumulation").toString());
		      row.createCell(4).setCellValue(report.get(i).get("voltage").toString()+"V");
		      row.createCell(5).setCellValue(report.get(i).get("resistance_current").toString()+"mA");
	      }
 
	//输出Excel文件
	    OutputStream output=response.getOutputStream();
	    response.reset();
	    response.setHeader("Content-disposition", "attachment; filename=details.xls");
	    response.setContentType("application/msexcel");        
	    wb.write(output);
	    output.close();
	    wb.close();
		return false;
	}

}
