package cn.aim.dao.dictionary;
/**
 * 字典接口
 * @author gengguoliang
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.aim.pojo.DataDictionary;

public interface DictionaryMapper {
	/**
	 * 根据类型编码查询分类信息
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> getAllDictionary(@Param("typeCode") String typeCode);
}
