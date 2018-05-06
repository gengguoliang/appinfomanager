package cn.aim.dao.f_user;
/**
 * 开发者用户接口
 * @author gengguoliang
 *
 */

import org.apache.ibatis.annotations.Param;
import cn.aim.pojo.DevUser;

public interface DevUserMapper {
	/**
	 * 用户登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser getDevUserLogin(@Param("devCode") String devCode,@Param("devPassword") String devPassword);
}
