package com.collectInfo.filter;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;


public class FhSiteMeshFilter extends SiteMeshFilter {
   private static final Logger log = LoggerFactory.getLogger(FhSiteMeshFilter.class);

	public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) rq;
		String requestType = req.getHeader("X-Requested-With"); 
		 HttpSession session = req.getSession();
		 String uri = req.getRequestURI();
		 if(uri.equals("/")){
			 session.setAttribute("uri", uri);
		   //  System.out.println("grwdoeguo:"+uri);
		 }
	     
	    if(requestType != null && "XMLHttpRequest".equals(requestType)){  //ajax请求
	    	log.debug("#、iteMeshFilter.doFilter############ignore ajax request:{}", new Object[]{req.getRequestURI()});
	    	chain.doFilter(rq, rs);
	    }else{ //不是ajax请求，走sitemesh过滤
	    	super.doFilter(rq, rs, chain);
	    }
	    return;
	}
}
