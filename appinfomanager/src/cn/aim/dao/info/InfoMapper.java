package cn.aim.dao.info;
/**
 * 基础信息接口
 * @author gengguoliang
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aim.pojo.AppInfo;
public interface InfoMapper {
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
	public int getAppInfoCount(@Param("softwareName") String softwareName,
							   @Param("status") Integer status,
			                   @Param("flatformId") Integer flatformId,
			                   @Param("categoryLevel1")Integer categoryLevel1,
			                   @Param("categoryLevel2")Integer categoryLevel2,
			                   @Param("categoryLevel3")Integer categoryLevel3);
	/**
	 * 查询所有app信息或根据条件查询
	 * @param softwareName
	 * @param status
	 * @param flatformId
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @return
	 */
	public List<AppInfo> getAllAppInfo(@Param("softwareName") String softwareName,
									   @Param("status") Integer status,
									   @Param("flatformId") Integer flatformId,
									   @Param("categoryLevel1")Integer categoryLevel1,
									   @Param("categoryLevel2")Integer categoryLevel2,
									   @Param("categoryLevel3")Integer categoryLevel3,
									   @Param("from")Integer currentPageNo,
									   @Param("pageSize")Integer pageSize);
	/**
	 * 验证APKName是否存在
	 * @param APKName
	 * @return
	 */
	public AppInfo APKNameExist(@Param("APKName")String APKName);
}
