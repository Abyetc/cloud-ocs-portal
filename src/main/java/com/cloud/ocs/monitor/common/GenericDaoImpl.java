package com.cloud.ocs.monitor.common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 用于访问monitor数据库的通用Dao抽象类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午2:24:38
 *
 * @param <T>
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T> {

	@PersistenceContext(unitName="monitor_db")
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
