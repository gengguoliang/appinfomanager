package cn.aim.service.info;

import java.util.List;

import cn.aim.pojo.AppInfo;

/**
 * app信息接口
 * @author gengguoliang
 *
 */
public interface AppInfoService {
	/**
	 * 获取总记录数或带条件的记录数
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @return
	 */
	public int findAppInfoCount(String softwareName,Integer status,
							Integer flatformId,Integer categoryLevel1,
							Integer categoryLevel2,Integer categoryLevel3);
	/**
	 * 查询所有app信息或根据条件查询
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppInfo> findAppInfoList(String softwareName,Integer status,
							Integer flatformId,Integer categoryLevel1,
							Integer categoryLevel2,Integer categoryLevel3,
							Integer currentPageNo,Integer pageSize);
	/**
	 * 验证APKName是否存在
	 * @param APKName
	 * @return
	 */
	public AppInfo APKNameExist(String APKName);
}
