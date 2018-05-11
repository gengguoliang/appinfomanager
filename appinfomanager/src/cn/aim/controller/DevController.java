package cn.aim.controller;
/**
 * 开发者前端控制层
 * @author gengguoliang
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.aim.dao.version.VersionMapper;
import cn.aim.pojo.AppCategory;
import cn.aim.pojo.AppInfo;
import cn.aim.pojo.AppVersion;
import cn.aim.pojo.DataDictionary;
import cn.aim.pojo.DevUser;
import cn.aim.service.category.CategoryService;
import cn.aim.service.devuser.DevUserService;
import cn.aim.service.dictionary.DictionaryService;
import cn.aim.service.info.AppInfoService;
import cn.aim.service.version.VersionService;
import cn.aim.tools.Constants;
import cn.aim.tools.PageSupport;
import jdk.nashorn.internal.ir.RuntimeNode.Request;
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
	@Resource
	private VersionService versionService;
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
	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value="/flatform/app/appinfoadd")
	public String appAdd() {
		return "developer/appinfoadd";
	}
	/**
	 * 验证APKName是否存在
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="/apkexist.json")
	@ResponseBody
	public Object ApkNameExist(@RequestParam(value="APKName") String APKName) {
		Map<String , String>APKMap=new HashMap<String,String>();
		if(APKName==null||APKName=="") {
			APKMap.put("APKName", "empty");
		}
		AppInfo appInfo=appInfoService.APKNameExist(APKName);
		if(appInfo!=null) {
			APKMap.put("APKName", "exist");
		}else {
			APKMap.put("APKName", "noexist");
		}
		return APKMap;
	}
	/**
	 * AppInfo新增
	 * @param appInfo
	 * @param session
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfoaddsave")
	public String AppInfoAdd(AppInfo appInfo,HttpSession session,Model model,
					@RequestParam(value="a_logoPicPath",required=false) MultipartFile attach) {
		//文件名
		String logoPicPath=null;
		//实际地址
		String apkLocPath=null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//上传路径
			String path="D:"+File.separator+"upload";
			logger.info("uploadfile path==============>"+path);
			String oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadfile oldfileName==================>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//获取原文件后缀
			logger.debug("upploadfile prefix==========>"+prefix);
			int fileSize=50000;
			logger.debug("uploadFile size================>"+attach.getSize());
			if(attach.getSize()>fileSize) {//上传大小不能超过50k
				model.addAttribute("fileUploadError", "上传文件的大小不能超过50k");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")){
				//指定图片名字
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"logo.jpg";
				logger.debug("new fileName========"+attach.getName());
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()) {//判断文件地址是否存在如果不存在则创建
					targetFile.mkdirs();
				}
				try {
					//将图片保存到指定的路径中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("fileUploadError", "*上传文件失败");
					return "developer/appinfoadd";
				}
				logoPicPath=fileName;
			}else {
				model.addAttribute("fileUploadError", "*上传图片的格式不正确");
				return "developer/appinfoadd";
			}
		}
		int devId=((DevUser)session.getAttribute(Constants.DEVUSER_SESSION)).getId();
		appInfo.setDevId(devId);
		//获取创建者id
		appInfo.setCreatedBy(devId);
		//获取创建时间
		appInfo.setCreationDate(new Date());
		//获取上传的图片名字
		appInfo.setLogoPicPath(logoPicPath);
		if(appInfoService.appInfoAdd(appInfo)) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfoadd";
	}
	/**
	 * 跳转到修改app基础信息页面
	 * @param appinfoid
	 * @return
	 */
	@RequestMapping(value="/flatform/app/appinfomodify")
	public String appInfoModif(@RequestParam(value="id",required=false)Integer appInfoId,Model model) {
		logger.debug("appInfoId===============>"+appInfoId);
		//根据ID获取appInfo信息
		List<AppInfo> appInfo=appInfoService.findAppInfoById(appInfoId);
		model.addAttribute("appInfo", appInfo);
		logger.debug("appInfo=================>"+appInfo);
		return "developer/appinfomodify";
	}
	/**
	 * 删除指定文件
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delfile.json")
	@ResponseBody
	public Object delFile(@RequestParam("id")Integer id) {
		Map<String, String>result=new HashMap<String,String>();
		//根据id获取文件名字
		List<AppInfo> appInfo=appInfoService.findAppInfoById(id);
		String fileName="";
		for (AppInfo appInfo2 : appInfo) {
			fileName=appInfo2.getLogoPicPath();
		}
		//文件路径
		String path="D:"+File.separator+"upload"+File.separator+fileName;
		File file=new File(path);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if(file.exists()) {
			if(file.delete()) {
				if(appInfoService.APpInfoDelPath(id)) {
					logger.debug("<=============图片路径等新成功============>");
				}
				result.put("result", "success");
			}else {
				result.put("result", "failed");
			}
		}
		return result;
	}
	/**
	 * 更新appInfo
	 * @param appInfo
	 * @param session
	 * @param model
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfomodifysave")
	public String appInfoModifySave(AppInfo appInfo,HttpSession session,Model model,
			@RequestParam(value="attach",required=false) MultipartFile attach) {
		String logoPicPath=null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//上传路径
			String path="D:"+File.separator+"upload";
			logger.info("uploadfile path==============>"+path);
			String oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadfile oldfileName==================>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//获取原文件后缀
			logger.debug("upploadfile prefix==========>"+prefix);
			int fileSize=50000;
			logger.debug("uploadFile size================>"+attach.getSize());
			if(attach.getSize()>fileSize) {//上传大小不能超过50k
				model.addAttribute("fileUploadError", "上传文件的大小不能超过50k");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")){
				//指定图片名字
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"logo.jpg";
				logger.debug("new fileName========"+attach.getName());
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()) {//判断文件地址是否存在如果不存在则创建
					targetFile.mkdirs();
				}
				try {
					//将图片保存到指定的路径中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("fileUploadError", "*上传文件失败");
					return "developer/appinfoadd";
				}
				logoPicPath=fileName;
			}else {
				model.addAttribute("fileUploadError", "*上传图片的格式不正确");
				return "developer/appinfoadd";
			}
		}
		/**
		 * 获取修改者id
		 */
		int modifyId=((DevUser)session.getAttribute(Constants.DEVUSER_SESSION)).getId();
		appInfo.setModifyBy(modifyId);
		appInfo.setModifyDate(new Date());
		appInfo.setUpdateDate(new Date());
		if(logoPicPath!=null) {
			appInfo.setLogoPicPath(logoPicPath);
		}
		if(appInfoService.AppInfomodify(appInfo)) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfomodify";
	}
	/**
	 * 跳转到新增APP管理页面
	 * 并且查询历史版本
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/appversionadd")
	public String appInfoVersion(@RequestParam(value="id")Integer id,Model model) {
		logger.debug("appId===================>"+id);
		List<AppVersion>list=versionService.findAppVersion(id);
		model.addAttribute("appVersionList", list);
		model.addAttribute("appId", id);
		return "developer/appversionadd";
	}
	/**
	 * 新增App版本信息
	 * @param appVersion
	 * @return
	 */
	@RequestMapping(value="/addversionsave")
	public String addVersionSave(HttpSession session,AppVersion appVersion,Model model,
			@RequestParam(value="a_downloadLink",required=false)MultipartFile attach) {
		String apkFileName=null;
		String apkLocPath=null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//上传路径
			String path="D:"+File.separator+"upload";
			logger.info("uploadfile path==============>"+path);
			String oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadfile oldfileName==================>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//获取原文件后缀
			logger.debug("upploadfile prefix==========>"+prefix);
			//设定上传文件的大小为500M
			int fileSize=500*1024*1024;
			logger.debug("uploadFile size================>"+attach.getSize());
			if(attach.getSize()>fileSize) {//上传大小不能超过500M
				model.addAttribute("fileUploadError", "上传文件的大小不能超过500M");
				return "developer/appversionadd";
			}else if(prefix.equalsIgnoreCase("apk")){
				//指定文件名字
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"logo.apk";
				logger.debug("new fileName========"+attach.getName());
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()) {//判断文件地址是否存在如果不存在则创建
					targetFile.mkdirs();
				}
				try {
					//将文件保存到指定的路径中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("fileUploadError", "*上传文件失败");
					return "developer/appversionadd";
				}
				apkFileName=fileName;
				apkLocPath=path+File.separator+fileName;
			}else {
				model.addAttribute("fileUploadError", "*上传文件的格式不正确");
				return "developer/appversionadd";
			}
		}
		String downloadLink=File.separator+"appinfomanager"+File.separator+"demo"+File.separator+"file"+File.separator+apkFileName;
		logger.debug("downloadLink====================>"+downloadLink);
		int createdBy=((DevUser)session.getAttribute(Constants.DEVUSER_SESSION)).getId();
		logger.debug("appId===================>"+appVersion.getAppId());
		appVersion.setCreatedBy(createdBy);
		appVersion.setCreationDate(new Date());
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		appVersion.setDownloadLink(downloadLink);
		if(versionService.addAppVersion(appVersion)) {
			return "redirect:/dev/flatform/app/list";
		}
		return"developer/appversionadd";
	}
	/**
	 * 跳转到修改页面
	 * @param versionid
	 * @param appinfoid
	 * @return
	 */
	@RequestMapping(value="/appversionmodify")
	public String appVersionModify(@RequestParam(value="vid")Integer versionid,
								@RequestParam(value="aid")Integer appinfoid,Model model) {
		logger.debug("versionid===============>"+versionid);
		logger.debug("appinfoid================>"+appinfoid);
		//根据appid查询当前版本信息
		List<AppVersion>list=versionService.findAppVersion(appinfoid);
		model.addAttribute("appVersionList", list);
		//根据versionId获取要修改版本的信息
		AppVersion appVersionInfo=versionService.findAppVersionInfo(versionid);
		model.addAttribute("appVersion", appVersionInfo);
		return "developer/appversionmodify";
	}
	/**
	 * 修改最新版本信息
	 * @param appVersion
	 * @param session
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appversionmodifysave")
	public String appVersionModifySave(AppVersion appVersion,HttpSession session,
			@RequestParam(value="attach",required=false)MultipartFile attach,Model model) {
		//文件名
		String apkFileName=null;
		//实际地址
		String apkLocPath=null;
		//服务器地址
		String downloadLink=null;
		//判断文件是否为空
		if(!attach.isEmpty()) {
			//上传路径
			String path="D:"+File.separator+"upload";
			logger.info("uploadfile path==============>"+path);
			String oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadfile oldfileName==================>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//获取原文件后缀
			logger.debug("upploadfile prefix==========>"+prefix);
			//设定上传文件的大小为500M
			int fileSize=500*1024*1024;
			logger.debug("uploadFile size================>"+attach.getSize());
			if(attach.getSize()>fileSize) {//上传大小不能超过500M
				model.addAttribute("fileUploadError", "上传文件的大小不能超过500M");
				return "developer/appversionadd";
			}else if(prefix.equalsIgnoreCase("apk")){
				//指定文件名字
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"logo.apk";
				logger.debug("new fileName========"+attach.getName());
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()) {//判断文件地址是否存在如果不存在则创建
					targetFile.mkdirs();
				}
				try {
					//将文件保存到指定的路径中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("fileUploadError", "*上传文件失败");
					return "developer/appversionadd";
				}
				apkFileName=fileName;
				apkLocPath=path+File.separator+fileName;
			}else {
				model.addAttribute("fileUploadError", "*上传文件的格式不正确");
				return "developer/appversionadd";
			}
		}
		//服务器地址
		if(apkFileName!=null) {
			downloadLink=File.separator+"appinfomanager"+File.separator+"demo"+File.separator+"file"+File.separator+apkFileName;
		}
		logger.debug("downloadLink====================>"+downloadLink);
		//获取修改者id
		int modifyBy=((DevUser)session.getAttribute(Constants.DEVUSER_SESSION)).getId();
		logger.debug("appId===================>"+appVersion.getAppId());
		appVersion.setModifyBy(modifyBy);
		//文件名
		appVersion.setApkFileName(apkFileName);
		//文件实际地址
		appVersion.setApkLocPath(apkLocPath);
		//文件服务器地址
		appVersion.setDownloadLink(downloadLink);
		//修改日期
		appVersion.setModifyDate(new Date());
		if(versionService.updAppVersionInfo(appVersion)) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appversionmodifysave";
	}
	/**
	 * 删除文件路径
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delFileVersion(@RequestParam(value="id",required=false)Integer id) {
		Map<String,String> result=new HashMap<String,String>();
		logger.debug("id==================>"+id);
		if(versionService.updAppVersionPath(id)) {
			result.put("result","success");
		}else {
			result.put("result","failed");
		}
		return result;
	}
	/**
	 * 跳转到查看页面
	 * @param appId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appview")
	public String appView(@RequestParam("appId")Integer appId,Model model) {
		//获取历史版本
		List<AppVersion>list=versionService.findAppVersion(appId);
		model.addAttribute("appVersionList", list);
		//根据id获取基础信息
		List<AppInfo> listInfo=appInfoService.findAppInfoById(appId);
		for (AppInfo appInfo : listInfo) {
			model.addAttribute("appInfo", appInfo);
		}
		return "developer/appinfoview";
	}
	/**
	 * 根据id删除app基础信息
	 * 并且删除当前APP所有历史版本
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delApp(@RequestParam("id") Integer appId) {
		logger.debug("appId====================>"+appId);
		Map<String,String> delResult=new HashMap<String,String>();
		if(appInfoService.delAppInfo(appId)) {
			delResult.put("delResult", "true");
		}else {
			delResult.put("delResult", "false");
		}
		return delResult;
	}
	@RequestMapping(value="/sale.json",method=RequestMethod.PUT)
	@ResponseBody
	public Object Sale(@RequestParam("appId")Integer id) {
		logger.debug("appId=====================>"+id);
		List<AppInfo>lists=appInfoService.findAppInfoById(id);
		Map<String,String> result=new HashMap<String,String>();
		//当前状态
		int status=0;
		//要更新的状态
		int statusupd=0;
		for (AppInfo appInfo : lists) {
			//获取当前状态
			status=appInfo.getStatus();
			logger.debug("status==================>"+status);
		}
		if(status==4) {
			logger.debug("当前状态已上架");
			statusupd=5;
		}else if(status==5) {
			logger.debug("当前状态已下架");
			statusupd=4;
		}
		result.put("errorCode","0");
		if(appInfoService.UpdSale(id, statusupd)) {			
			result.put("resultMsg", "success");
		}else {			
			result.put("resultMsg", "failed");
		}
		return result;
	}
}