package cn.aim.service.devuser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.f_user.DevUserMapper;
import cn.aim.pojo.DevUser;
@Service
public class DevUserServiceImpl implements DevUserService {
	@Resource
	private DevUserMapper devUserMapper;

	@Override
	public DevUser findDevUserLogin(String devCode, String devPassword) {
		DevUser devUser=devUserMapper.getDevUserLogin(devCode, devPassword);
		return devUser;
	}

}
