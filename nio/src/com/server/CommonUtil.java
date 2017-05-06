package com.server;

import java.util.UUID;

public class CommonUtil {
	public static String GUID()
	  {
	    String a = null;
	    for (int i = 0; i < 5; i++)
	    {
	      UUID uuid = UUID.randomUUID();
	      
	      a = uuid.toString();
	      
	      a = a.toUpperCase();
	      
	      a = a.replaceAll("-", "_");
	    }
	    System.out.println(a + "a");
	    return a;
	  }
}
