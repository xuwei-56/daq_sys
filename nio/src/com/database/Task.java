package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task extends TimerTask{
	private static final Logger logger = LoggerFactory.getLogger(Task.class);
	  
	  public void run()
	  {
	    try
	    {
	      Class.forName("com.mysql.jdbc.Driver");
	      String url = "jdbc:mysql://127.0.0.1:3306/daq_sys?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
	      
	      Connection conn = DriverManager.getConnection(url, "root", "123456");
	      Statement stmt = conn.createStatement();
	      
	      Calendar c = Calendar.getInstance();
	      c.setTime(new Date());
	      c.add(5, 1);
	      Date tomorrow = c.getTime();
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      int newTableNum = Integer.parseInt(sdf.format(tomorrow), 10);
	      
	      String createTomorrowTable = "set @sql_create_table = concat('CREATE TABLE IF NOT EXISTS data_" + 
	        newTableNum + 
	        "',\"(" + 
	        "`data_id` varchar(36) NOT NULL," + 
	        "    `device_ip` varchar(15) NOT NULL," + 
	        "   `generate_time` datetime NOT NULL," + 
	        "    `pulse_current` int(11) NOT NULL," + 
	        "    `pulse_accumulation` int(11) NOT NULL," + 
	        "    `voltage` int(11) NOT NULL," + 
	        "    `resistance_current` int(11) NOT NULL,  " + 
	        "INDEX(device_ip)," + 
	        "    PRIMARY KEY (`data_id`) " + 
	        ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8\");" + 
	        "PREPARE sql_create_table FROM @sql_create_table;" + 
	        "EXECUTE sql_create_table;";
	      


	      c.setTime(new Date());
	      c.add(1, -1);
	      Date oldDate = c.getTime();
	      int OldTableNum = Integer.parseInt(sdf.format(oldDate), 10);
	      String deleteOldTable = "DROP TABLE IF EXISTS data_" + OldTableNum;
	      
	      stmt.executeUpdate(createTomorrowTable);
	      stmt.executeUpdate(deleteOldTable);
	      

	      stmt.close();
	      conn.close();
	      logger.info("创建了明天的表，并删除了一年期前今天的表。");
	    }
	    catch (Exception e)
	    {
	      logger.info("创建和删除表失败");
	    }
	  }
}
