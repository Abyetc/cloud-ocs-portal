package com.cloud.ocs.monitor.common;

/**
 * 访问monitor数据库的通用Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午2:22:09
 *
 */
public interface GenericDao<T> {

	public void persist(T t);
	public void remove(T t);
	public T merge(T t);
	public T find(Class<T> clazz, String id);
}
