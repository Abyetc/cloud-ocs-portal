package com.cloud.ocs.portal.core.business.dao;

import java.util.List;

import com.cloud.ocs.portal.common.dao.GenericDao;
import com.cloud.ocs.portal.core.business.bean.OcsEngine;

/**
 * Ocs引擎程序Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 下午3:59:56
 *
 */
public interface OcsEngineDao extends GenericDao<OcsEngine> {

	/**
	 * 找出数据库中所有的记录列表
	 * @return
	 */
	public List<OcsEngine> findAllOcsEngines();
}
