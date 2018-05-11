package cn.aim.service.info;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.info.InfoMapper;
import cn.aim.pojo.AppInfo;
import cn.aim.service.version.VersionService;
@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private InfoMapper infoMapper;
	@Resource
	private VersionService versionService;
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

	@Override
	public boolean appInfoAdd(AppInfo appInfo) {
		int num=infoMapper.AppInfoAdd(appInfo);
		if(num>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<AppInfo> findAppInfoById(Integer appInfoId) {
		return infoMapper.getAppInfoById(appInfoId);
	}

	@Override
	public boolean AppInfomodify(AppInfo appInfo) {
		int num=infoMapper.AppInfoModify(appInfo);
		if(num>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean APpInfoDelPath(Integer id) {
		int num=infoMapper.AppInfoLogoPath(id);
		if(num>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean UpdAppInfoVersionId(Integer id, Integer versionId) {
		int num=infoMapper.AppInfoVersionId(id, versionId);
		if(num>0) {
			return true;
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}

	@Override
	public boolean delAppInfo(Integer id) {
		int num=infoMapper.delAppInfo(id);
		if(num>0) {
			if(versionService.delAppVersion(id)) {
				return true;
			}else {
				return false;
			}
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}

	@Override
	public boolean UpdSale(Integer id, Integer status) {
		int num = infoMapper.appInfoSale(id, status);
		if(num>0) {
			return true;
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}

	
	@Override
	public int findAppInfoCounts(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3) {
		return infoMapper.getAppInfoCounts(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}

	@Override
	public List<AppInfo> findAppInfoLists(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer currentPageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return infoMapper.getAllAppInfos(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, currentPageNo, pageSize);
	}

}
