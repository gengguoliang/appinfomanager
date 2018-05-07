package cn.aim.service.dictionary;

import java.util.List;


import cn.aim.pojo.DataDictionary;

/**
 * 字典业务接口
 * @author gengguoliang
 *
 */
public interface DictionaryService {
	/**
	 * 根据类型编码查询分类信息
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> getAllDictionary(String typeCode);
}
