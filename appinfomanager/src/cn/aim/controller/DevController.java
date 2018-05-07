package cn.aim.controller;
/**
 * 开发者前端控制层
 * @author gengguoliang
 *
 */

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.aim.pojo.AppCategory;
import cn.aim.pojo.AppInfo;
import cn.aim.pojo.DataDictionary;
import cn.aim.pojo.DevUser;
import cn.aim.service.category.CategoryService;
import cn.aim.service.devuser.DevUserService;
import cn.aim.service.dictionary.DictionaryService;
import cn.aim.service.info.AppInfoService;
import cn.aim.tools.Constants;
import cn.aim.tools.PageSupport;
@Controller
@RequestMapping("/dev")
public class DevController {
	private Logger logger=Logger.getLogger(DevController.class);
	@Resource
	private DevUserService devUserService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private AppInfoService appInfoService;
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
			return "redirect:/dev/sys/flatform/main";
		}else {
			model.addAttribute("error", "账号或密码错误！");
			return "devlogin";
		}
	}
	/**
	 * 跳转到开发者首页
	 * @return
	 */
	@RequestMapping(value="/sys/flatform/main",method=RequestMethod.GET)
	public String devMain() {
		return "developer/main";
	}
//	/**
//	 * 跳转到app维护页面
//	 * @return
//	 */
//	@RequestMapping(value="/flatform/app/list",method=RequestMethod.GET)
//	public String appList(Model model) {
//		this.commList(model, null, null, null, null, null, null, null);
//		return "developer/appinfolist";
//	}
	/**
	 * 公共查询页面
	 * @param model
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param currentPageNo
	 * @param pageSize
	 */
	public void commList(Model model,String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer currentPageNo) {
		//查询一级菜单
		List<AppCategory>list=categoryService.findCategory1();
		for (AppCategory appCategory : list) {
			logger.debug("appCategory =====CategoryName"+appCategory.getCategoryName());
		}
		//查询APP状态信息
		List<DataDictionary>statusList=dictionaryService.getAllDictionary(Constants.APP_STATUS);
		//查询所属平台
		List<DataDictionary>flatFormList=dictionaryService.getAllDictionary(Constants.APP_FLATFORM);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("categoryLevel1List", list);
		List<AppInfo>appInfoList=null;
		//设置页面容量
		int pageSize=Constants.pageSize;
		//当前页码
		if(currentPageNo==null) {
			currentPageNo=1;
		}
		//总数量（表）
		int totalCount=appInfoService.findAppInfoCount(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
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
		appInfoList=appInfoService.findAppInfoList(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, (pages.getCurrentPageNo()-1)*pages.getPageSize(), pages.getPageSize());
		model.addAttribute("pages", pages);
		model.addAttribute("appInfoList", appInfoList);
	}
	
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping(value="/flatform/app/list")
	public String appList(Model model,@RequestParam(value="querySoftwareName",required=false) String softwareName,
							@RequestParam(value="queryStatus",required=false) Integer status,
							@RequestParam(value="queryFlatformId",required=false) Integer flatformId,
							@RequestParam(value="queryCategoryLevel1",required=false) Integer categoryLevel1,
							@RequestParam(value="queryCategoryLevel2",required=false) Integer categoryLevel2,
							@RequestParam(value="queryCategoryLevel3",required=false) Integer categoryLevel3,
							@RequestParam(value="pageIndex",required=false) Integer currentPageNo) {
		
		this.commList(model, softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, currentPageNo);
		return "developer/appinfolist";
	}
	
	
	
	/**
	 * 子集联动
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object appSonList(@RequestParam(value="pid")Integer parentId) {
		List<AppCategory>list=categoryService.findCategory(parentId);		
		return JSONArray.toJSONString(list);
	}
	/**
	 * 退出登录
	 * 清除session
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String appLogout(HttpSession session) {
		session.removeAttribute(Constants.DEVUSER_SESSION);
		return "redirect:/devlogin";
	}
}