package cn.aim.service.version;

import java.util.List;

import cn.aim.pojo.AppVersion;

public interface VersionService {
	/**
	 * 根据appId查询版本信息
	 * @param appId
	 * @return
	 */
	public List<AppVersion>findAppVersion(Integer appId);
	
	/**
	 * 新增appVersion版本
	 * @param appVersion
	 * @return
	 */
	public boolean addAppVersion(AppVersion appVersion);	
	/**
	 * 获取最新版本信息的id
	 * @return
	 */
	public int findAppVersionId();
	/**
	 * 根据id获取最新版本信息
	 * @param id
	 * @return
	 */
	public AppVersion findAppVersionInfo(Integer id);
	/**
	 * 修改最新版本信息
	 * @param appVersion
	 * @return
	 */
	public boolean updAppVersionInfo(AppVersion appVersion);
	/**
	 * 删除文件路径
	 * @param id
	 * @return
	 */
	public boolean updAppVersionPath(Integer id);
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	public boolean delAppVersion(Integer id);
}
