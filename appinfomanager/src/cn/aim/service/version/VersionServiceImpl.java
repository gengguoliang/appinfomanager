package cn.aim.service.version;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.version.VersionMapper;
import cn.aim.pojo.AppVersion;
import cn.aim.service.info.AppInfoService;
@Service
public class VersionServiceImpl implements VersionService {
	@Resource
	private VersionMapper versionMapper;
	@Resource
	private AppInfoService appInfoService;
	@Override
	public List<AppVersion> findAppVersion(Integer appId) {
		return versionMapper.getAppVersion(appId);
	}
	@Override
	public boolean addAppVersion(AppVersion appVersion) {
		int num=versionMapper.getAddAppVersion(appVersion);
		if(num>0) {
			//更新appInfo的版本显示最新版本
			if(appInfoService.UpdAppInfoVersionId(appVersion.getAppId(),appVersion.getId())) {
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
	public int findAppVersionId() {
		return versionMapper.getAppVersionId();
	}
	@Override
	public AppVersion findAppVersionInfo(Integer id) {
		return versionMapper.getAppVersionInfo(id);
	}
	@Override
	public boolean updAppVersionInfo(AppVersion appVersion) {
		int num=versionMapper.updAppVersion(appVersion);
		if(num>0) {
			return true;
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}
	@Override
	public boolean updAppVersionPath(Integer id) {
		int num=versionMapper.updAppVersionPath(id);
		if(num>0) {
			return true;
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}
	@Override
	public boolean delAppVersion(Integer id) {
		int num=versionMapper.delAppVersion(id);
		if(num>0) {
			return true;
		}else if(num==0) {
			return false;
		}else {
			return false;
		}
	}

}
