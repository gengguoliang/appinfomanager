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
}
