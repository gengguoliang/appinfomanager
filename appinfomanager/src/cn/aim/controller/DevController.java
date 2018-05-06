package cn.aim.controller;
/**
 * 开发者前端控制层
 * @author gengguoliang
 *
 */

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.aim.pojo.DevUser;
import cn.aim.service.devuser.DevUserService;
import cn.aim.tools.Constants;
@Controller
@RequestMapping("/dev")
public class DevController {
	private Logger logger=Logger.getLogger(DevController.class);
	@Resource
	private DevUserService devUserService;
	
	/**
	 * 登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String devDologin(String devCode,String devPassword,HttpSession session,Model model) {
		logger.debug("devCode======="+devCode);
		logger.debug("devPassword======="+devPassword);
		DevUser devUser=devUserService.findDevUserLogin(devCode, devPassword);
		if(devUser!=null) {
			session.setAttribute(Constants.DEVUSER_SESSION, devUser);
			return "redirect:/dev/sys/main";
		}else {
			model.addAttribute("error", "账号或密码错误！");
			return "devlogin";
		}
	}
	/**
	 * 跳转到开发者首页
	 * @return
	 */
	@RequestMapping(value="/sys/main",method=RequestMethod.GET)
	public String devMain() {
		return "developer/main";
	}
}
