package cn.aim.service.category;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.category.CategoryMapper;
import cn.aim.pojo.AppCategory;
@Service
public class CategoryServiceImpl implements CategoryService {
	@Resource
	private CategoryMapper categoryMapper;
	@Override
	public List<AppCategory> findCategory1() {
		return categoryMapper.getCategory1();
	}
	@Override
	public List<AppCategory> findCategory(Integer parentId) {
		return categoryMapper.getCategory(parentId);
	}

}
