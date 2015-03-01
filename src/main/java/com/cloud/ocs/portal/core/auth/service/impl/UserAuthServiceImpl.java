package com.cloud.ocs.portal.core.auth.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.User;
import com.cloud.ocs.portal.common.dao.UserDao;
import com.cloud.ocs.portal.core.auth.constant.LoginStatus;
import com.cloud.ocs.portal.core.auth.service.UserAuthService;

/**
 * 系统用户service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-10 下午7:38:55
 *
 */
@Transactional(value="portal_em")
@Service
public class UserAuthServiceImpl implements UserAuthService {
	
	@Resource
	private UserDao userDao;

	@Override
	public LoginStatus checkLogin(String accountId, String accountPassword) {
		User user = userDao.findUserByAccountId(accountId);
		if (user == null) {
			return LoginStatus.LOGIN_ACCOUNT_ID_ERROR;
		}
		if (!user.getAccountPassword().equals(accountPassword)) {
			return LoginStatus.LOGIN_ACCOUNT_PASSWORD_ERROE;
		}
		
		return LoginStatus.LOGIN_SUCCESS;
	}

	@Override
	public User findUserByAccount(String accountId) {
		return userDao.findUserByAccountId(accountId);
	}

	@Override
	public User updateLoginDate(User user) {
		Date loginDate = new Date();
		user.setLastLoginDate(new Timestamp(loginDate.getTime()));
		
		return userDao.merge(user);
	}
}
