package cn.aim.service.dictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.dictionary.DictionaryMapper;
import cn.aim.pojo.DataDictionary;
@Service
public class DictionaryServiceImpl implements DictionaryService {
	@Resource
	private DictionaryMapper dictionaryMapper;
	@Override
	public List<DataDictionary> getAllDictionary(String typeCode) {
		return dictionaryMapper.getAllDictionary(typeCode);
	}

}
