package cn.aim.dao.category;
/**
 * 所属分类接口
 * @author gengguoliang
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aim.pojo.AppCategory;

public interface CategoryMapper {
	/**
	 * 查询所有一级分类
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getCategory1();
	/**
	 * 查询子集分类
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getCategory(@Param("parentId")Integer parentId);
}
