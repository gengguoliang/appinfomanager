package cn.aim.service.category;

import java.util.List;
import cn.aim.pojo.AppCategory;

public interface CategoryService {
	/**
	 * 查询所有一级分类
	 * @return
	 */
	public List<AppCategory> findCategory1();
	
	/**
	 * 查询所有一级分类
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> findCategory(Integer parentId);
}
