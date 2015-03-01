package com.cloud.ocs.portal.common.dao;

import com.cloud.ocs.portal.common.bean.User;

/**
 * 系统用户Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:07:09
 *
 */
public interface UserDao extends GenericDao<User> {
	public User findUserByAccountId(String accountId);
}