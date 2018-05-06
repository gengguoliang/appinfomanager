package cn.aim.service.backenduser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.aim.dao.l_user.BackUserMapper;
import cn.aim.pojo.BackendUser;
@Service
public class BackUserServiceImpl implements BackUserService {
	@Resource
	private BackUserMapper backUserMapper;
	@Override
	public BackendUser findLoginBackUser(String userCode, String userPassword) {
		return backUserMapper.getLoginBackendUser(userCode, userPassword);
	}

}
