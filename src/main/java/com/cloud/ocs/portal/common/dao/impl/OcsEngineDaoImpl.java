package com.cloud.ocs.portal.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.common.dao.OcsEngineDao;

/**
 * Ocs引擎程序Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 下午4:01:46
 *
 */
@Repository
public class OcsEngineDaoImpl extends GenericDaoImpl<OcsEngine> implements OcsEngineDao {

	@Override
	public List<OcsEngine> findAllOcsEngines() {
		Query query = em
				.createQuery("select model from OcsEngine model");
		
		return query.getResultList();
	}

}
