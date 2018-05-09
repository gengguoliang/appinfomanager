package cn.aim.dao.version;
/**
 * 版本信息接口
 * @author gengguoliang
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aim.pojo.AppVersion;

public interface VersionMapper {
	/**
	 * 根据appid查询appVersion信息
	 * @param appId
	 * @return
	 */
	public List<AppVersion> getAppVersion(@Param("appId")Integer appId);
	/**
	 * 新增APPVersion信息
	 * @param appVersion
	 * @return
	 */
	public int getAddAppVersion(AppVersion appVersion);
	/**
	 * 获取最新版本信息的id
	 * @return
	 */
	public int getAppVersionId();
}
