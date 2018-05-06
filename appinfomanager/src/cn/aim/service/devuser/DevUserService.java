package cn.aim.service.devuser;
/**
 * 开发者业务接口
 * @author gengguoliang
 *
 */


import cn.aim.pojo.DevUser;

public interface DevUserService {
	/**
	 * 登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser findDevUserLogin(String devCode,String devPassword);
}
