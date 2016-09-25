package com.collectInfo.util;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description 定时器任务,每隔一分钟执行一次,查找order_state=1的订单,order_creat_time
 * @author gr
 *
 */
@Transactional(readOnly = true)
public class SpringQtz {
	@Autowired

	protected void executeSource(){
	}
	
	protected void executeOrder(){
	}
}
