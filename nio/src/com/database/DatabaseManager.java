package com.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class DatabaseManager
{
  private static final long PERIOD_DAY = 86400000L;
  
  public DatabaseManager()
  {
    Calendar calendar = Calendar.getInstance();
    
    calendar.set(11, 2);
    calendar.set(12, 22);
    calendar.set(13, 22);
    Date date = calendar.getTime();
    Timer timer = new Timer();
    Task task = new Task();
    timer.schedule(task, date, PERIOD_DAY);
  }
}
