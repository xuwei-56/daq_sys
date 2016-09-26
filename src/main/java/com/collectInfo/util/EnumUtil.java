package com.collectInfo.util;
/**
 * 
 * @ClassName: EnumUtil 
 * @Description: 主要定义一些常用的常量 
 */
public class EnumUtil {
	
	
	/**
	 * 分页查询 每页条数
	 */
	public static final int PAGE_SIZE = 10;
	
	/**
	 * 系统错误
	 */
   public static final int SYSTEM_ERROR = -5;
   /**
    * 未知错误
    */
   public static final int UNKOWN_ERROR = -6;
   
   /**
    * 正确
    */
   public static final int OK = 1;
   
   /**
    * 重复
    */
   public static final int REPEAT = -3;
   
   /**
    * 请求成功，但是由于某些原因不能得到数据
    */
   public static final int FALSE = -2;
   
   /**
    * 未登录
    */
   public static final int NOT_LOGIN = -1;
    
   /**
    * 不能为空
    */
   public static final int CAN_NOT_NULL = -4;
   
   /**
    * 查无数据
    */
   public static final int NO_DATA = 100011;
   
   /**
    * 用户密码错误
    */
   public static final int PASSWORD_ERROR = 100018;
   
   
}
