package com.collectInfo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



//@WebFilter(filterName = "dd",dispatcherTypes ={ DispatcherType.REQUEST},servletNames = "dd" ,value = {"/*"})
public class XssFilter  implements Filter {

    FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request , ServletResponse response , FilterChain chain) throws IOException , ServletException {

        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    public void destroy() {
        this.filterConfig = null;
    }


}
