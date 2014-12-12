package com.cloud.ocs.portal.common.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cloud.ocs.portal.common.dao.GenericDao;

/**
 * 通用Dao抽象类，实现对实体最基本的“增删改查”的通用功能
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-9 下午8:33:49
 *
 * @param <T>
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T> {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public void persist(T t) {
		em.persist(t);
	}

	@Override
	public void remove(T t) {
		em.remove(t);
	}

	@Override
	public T merge(T t) {
		return em.merge(t);
	}

	@Override
	public T find(Class<T> clazz, String id) {
		return em.find(clazz, id);
	}
}
