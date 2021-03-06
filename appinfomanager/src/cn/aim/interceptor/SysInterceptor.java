package cn.aim.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.aim.pojo.DevUser;
import cn.aim.tools.Constants;

/**
 * 拦截器
 * @author gengguoliang
 *
 */
public class SysInterceptor extends HandlerInterceptorAdapter{
	private Logger logger=Logger.getLogger(SysInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("SysInterceptor preHandle!");
		HttpSession session=request.getSession();
		DevUser devUser=(DevUser)session.getAttribute(Constants.DEVUSER_SESSION);
		if(null==devUser) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}
