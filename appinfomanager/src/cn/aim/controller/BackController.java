package cn.aim.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
/**
 * 后台前段控制层
 * @author gengguoliang
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.aim.pojo.BackendUser;
import cn.aim.service.backenduser.BackUserService;
import cn.aim.tools.Constants;
@Controller
@RequestMapping("/manager")
public class BackController {
	private Logger logger=Logger.getLogger(BackController.class);
	@Resource
	private BackUserService backUserService;
	
	/**
	 * 登录
	 * @param userCode
	 * @param userPassword
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String backDologin(String userCode,String userPassword,HttpSession session,Model model) {
		logger.debug("userCode======="+userCode);
		logger.debug("userPassword======="+userPassword);
		BackendUser backUser=backUserService.findLoginBackUser(userCode, userPassword);
		if(backUser!=null) {
			session.setAttribute(Constants.BACKUSER_SESSION, backUser);
			return "redirect:/manager/sys/main";
		}else {
			model.addAttribute("error", "账号或密码错误！");
			return "backendlogin";
		}
	}
	/**
	 * 跳转到开发者首页
	 * @return
	 */
	@RequestMapping(value="/sys/main",method=RequestMethod.GET)
	public String devMain() {
		return "backend/main";
	}
	/**
	 * 退出登录
	 * 清除session
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String appLogout(HttpSession session) {
		session.removeAttribute(Constants.BACKUSER_SESSION);
		return "redirect:/backlogin";
	}
}
