package com.cloud.ocs.portal.core.auth.service;

import com.cloud.ocs.portal.common.bean.User;
import com.cloud.ocs.portal.core.auth.constant.LoginStatus;

/**
 * 系统用户service接口
 * @author Wang Chao
 *
 * @date 2014-12-10 下午7:35:42
 *
 */
public interface UserAuthService {
	public User findUserByAccount(String accountId);
	public User updateLoginDate(User user);
	public LoginStatus checkLogin(String accountId, String accountPassword);
}
