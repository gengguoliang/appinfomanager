package cn.aim.dao.l_user;
/**
 * 后台用户接口
 * @author gengguoliang
 *
 */

import org.apache.ibatis.annotations.Param;

import cn.aim.pojo.BackendUser;

public interface BackUserMapper {
	/**
	 * 登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public BackendUser getLoginBackendUser(@Param("userCode")String userCode,@Param("userPassword")String userPassword);
}
