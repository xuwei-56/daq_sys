package com.collectInfo.interceptor;
/**
 * 权限过滤，过滤非超级管理员
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.collectInfo.model.User;
import com.collectInfo.util.CommonUtil;
import com.collectInfo.util.EnumUtil;

public class AuthorityInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if (user.getIsRoot() == 0){
            response.getWriter().write(CommonUtil.constructResponse(EnumUtil.NOT_POWER,null,null).toJSONString());
            return false;
        }else {
            return true;
        }
	}

}
