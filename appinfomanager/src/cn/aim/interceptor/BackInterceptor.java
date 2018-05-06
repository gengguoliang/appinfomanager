package cn.aim.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.aim.pojo.BackendUser;
import cn.aim.tools.Constants;

public class BackInterceptor extends HandlerInterceptorAdapter{
	private Logger logger=Logger.getLogger(SysInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("backInterceptor preHandle!");
		HttpSession session=request.getSession();
		BackendUser backendUser=(BackendUser)session.getAttribute(Constants.BACKUSER_SESSION);
		if(null==backendUser) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}
