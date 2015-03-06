package com.cloud.ocs.portal.common.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.dao.OcsHostDao;

/**
 * Ocs Host Dao实现类
 * 
 * @author Charles
 *
 */
@Repository
public class OcsHostDaoImpl extends GenericDaoImpl<OcsHost> implements OcsHostDao {

	@Override
	public OcsHost findByHostId(String hostId) {
		Query query = em.createQuery("select model from OcsHost model where model.hostId='" + hostId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while( iterator.hasNext() ) {
			return (OcsHost)iterator.next();
		}
		
		return null;
	}

}
