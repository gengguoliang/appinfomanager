package cn.aim.service.info;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.info.InfoMapper;
import cn.aim.pojo.AppInfo;
@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private InfoMapper infoMapper;
	@Override
	public int findAppInfoCount(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3) {
		return infoMapper.getAppInfoCount(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}

	@Override
	public List<AppInfo> findAppInfoList(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer currentPageNo, Integer pageSize) {
		return infoMapper.getAllAppInfo(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, currentPageNo, pageSize);
	}

	@Override
	public AppInfo APKNameExist(String APKName) {
		return infoMapper.APKNameExist(APKName);
	}

}
