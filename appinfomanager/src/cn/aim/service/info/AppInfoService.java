package cn.aim.service.info;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

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
	/**
	 * 新增appInfo信息
	 * @param appInfo
	 * @return
	 */
	public boolean appInfoAdd(AppInfo appInfo);
	/**
	 * 根据id查询appInfo信息
	 * @param appInfoId
	 * @return
	 */
	public List<AppInfo> findAppInfoById(Integer appInfoId);
	/**
	 * 更新appinfo
	 * @param appInfo
	 * @return
	 */
	public boolean AppInfomodify(AppInfo appInfo);
	/**
	 * 根据id移除图片路径
	 * @param id
	 * @return
	 */
	public boolean APpInfoDelPath(Integer id);
	/**
	 * 根据id更新app版本信息
	 * @param id
	 * @param versionId
	 * @return
	 */
	public boolean UpdAppInfoVersionId(Integer id,Integer versionId);
	/**
	 * 根据id删除APPInfo信息
	 * @param id
	 * @return
	 */
	public boolean delAppInfo(Integer id);
	/**
	 * 商品上下架
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean UpdSale(Integer id,Integer status);
	/*=================================后台=====================================*/
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
	public int findAppInfoCounts(String softwareName,
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
	public List<AppInfo> findAppInfoLists(String softwareName,
							Integer flatformId,Integer categoryLevel1,
							Integer categoryLevel2,Integer categoryLevel3,
							Integer currentPageNo,Integer pageSize);
}
