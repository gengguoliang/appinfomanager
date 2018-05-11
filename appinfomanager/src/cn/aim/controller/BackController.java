package cn.aim.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.aim.pojo.AppCategory;
import cn.aim.pojo.AppInfo;
import cn.aim.pojo.AppVersion;
import cn.aim.pojo.BackendUser;
import cn.aim.pojo.DataDictionary;
import cn.aim.service.backenduser.BackUserService;
import cn.aim.service.category.CategoryService;
import cn.aim.service.dictionary.DictionaryService;
import cn.aim.service.info.AppInfoService;
import cn.aim.service.version.VersionService;
import cn.aim.tools.Constants;
import cn.aim.tools.PageSupport;
@Controller
@RequestMapping("/manager")
public class BackController {
	private Logger logger=Logger.getLogger(BackController.class);
	@Resource
	private BackUserService backUserService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private VersionService versionService;
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
	@RequestMapping(value="/backend/app/list")
	public String appInfoLists(Model model,@RequestParam(value="querySoftwareName",required=false) String softwareName,
			@RequestParam(value="queryFlatformId",required=false) Integer flatformId,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer categoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer categoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer categoryLevel3,
			@RequestParam(value="pageIndex",required=false) Integer currentPageNo) {
				//查询一级菜单
				List<AppCategory>list=categoryService.findCategory1();
				for (AppCategory appCategory : list) {
					logger.debug("appCategory =====CategoryName"+appCategory.getCategoryName());
				}
				//查询所属平台
				List<DataDictionary>flatFormList=dictionaryService.getAllDictionary(Constants.APP_FLATFORM);
				model.addAttribute("flatFormList", flatFormList);
				model.addAttribute("categoryLevel1List", list);
				List<AppInfo>appInfoList=null;
				//设置页面容量
				int pageSize=Constants.pageSize;
				//当前页码
				if(currentPageNo==null) {
					currentPageNo=1;
				}
				//总数量（表）
				int totalCount=appInfoService.findAppInfoCounts(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
				//总页数
				PageSupport pages=new PageSupport();
				pages.setTotalCount(totalCount);//总记录书
				pages.setCurrentPageNo(currentPageNo);//当前页数
				pages.setPageSize(pageSize);//页面大小
				//控制首页和尾页
				if(currentPageNo<1){
					currentPageNo=1;
				}else if(currentPageNo>pages.getTotalPageCount()){
					currentPageNo=pages.getTotalPageCount();
				}
				//获取查询信息
				appInfoList=appInfoService.findAppInfoLists(softwareName,flatformId, categoryLevel1, categoryLevel2, categoryLevel3, (pages.getCurrentPageNo()-1)*pages.getPageSize(), pages.getPageSize());
				model.addAttribute("pages", pages);
				model.addAttribute("appInfoList", appInfoList);
				return "backend/applist";
	}
	/**
	 * 异步查询所属平台
	 * @return
	 */
	@RequestMapping(value="/datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object FlatForm() {
		//查询所属平台
		List<DataDictionary>flatFormList=dictionaryService.getAllDictionary(Constants.APP_FLATFORM);
		return JSONArray.toJSONString(flatFormList);
	}
	
	/**
	 * 子集联动
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object appSonList(@RequestParam(value="pid")Integer parentId) {
		List<AppCategory>list=null;
		if(parentId!=null) {
			list=categoryService.findCategory(parentId);
		}else {//查询一级
			list=categoryService.findCategory1();
		}
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 审核APP界面
	 * @param aid
	 * @param vid
	 * @return
	 */
	@RequestMapping(value="/check")
	public String check(@RequestParam("aid") Integer aid,
			@RequestParam("vid") Integer vid,Model model) {
		//根据ID获取appInfo信息
		List<AppInfo> appInfo=appInfoService.findAppInfoById(aid);
		for (AppInfo appInfo2 : appInfo) {
			model.addAttribute("appInfo", appInfo2);
		}
		AppVersion appVersion = versionService.findAppVersionInfo(vid);
		model.addAttribute("appVersion", appVersion);
		return"backend/appcheck";
	}
	@RequestMapping(value="/checksave")
	public String checksave(@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="id",required=false)Integer id) {
		logger.debug("status=====================>"+status);
		logger.debug("id=====================>"+id);
		if(appInfoService.UpdSale(id, status)) {
			return "redirect:/manager/backend/app/list";
		}
		return "backend/appcheck";
	}
}
