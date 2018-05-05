package cn.aim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 控制首页跳转
 * @author gengguoliang
 *
 */
@Controller
public class IndexController {
	/**
	 * 后台页面跳转
	 * @return
	 */
	@RequestMapping(value="/manager/login",method=RequestMethod.GET)
	public String backendLogin() {
		return "backendlogin";
	}
	/**
	 * 开发者页面跳转
	 * @return
	 */
	@RequestMapping(value="/dev/login",method=RequestMethod.GET)
	public String devLogin() {
		return "devlogin";
	}
}
