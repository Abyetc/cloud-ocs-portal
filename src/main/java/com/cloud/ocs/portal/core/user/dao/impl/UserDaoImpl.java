package com.cloud.ocs.portal.core.user.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.dao.impl.GenericDaoImpl;
import com.cloud.ocs.portal.core.user.bean.User;
import com.cloud.ocs.portal.core.user.dao.UserDao;

/**
 * 系统用户Dao实现类
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:06:32
 *
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

	@Override
	public User findUserByAccountId(String accountId) {
		Query query = em.createQuery("select user from User user where user.accountId='" + accountId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while( iterator.hasNext() ) {
			return (User)iterator.next();
		}
		
		return null;
	}
}
