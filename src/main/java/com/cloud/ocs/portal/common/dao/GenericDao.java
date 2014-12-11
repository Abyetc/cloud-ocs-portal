package com.cloud.ocs.portal.common.dao;

/**
 * 通用Dao接口
 * @author Wang Chao
 *
 * @date 2014-12-9 下午8:52:47
 *
 * @param <T>
 */
public interface GenericDao<T> {
	public void persist(T t);
	public void remove(T t);
	public T merge(T t);
	public T find(Class<T> clazz, String id);
}
