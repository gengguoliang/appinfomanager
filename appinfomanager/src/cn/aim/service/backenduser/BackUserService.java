package cn.aim.service.backenduser;
/**
 * 后台业务接口
 * @author gengguoliang
 *
 */

import cn.aim.pojo.BackendUser;

public interface BackUserService {
	/**
	 * 登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public BackendUser findLoginBackUser(String userCode,String userPassword);
}
