package com.cloud.ocs.portal.core.business.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.dao.impl.GenericDaoImpl;
import com.cloud.ocs.portal.core.business.bean.OcsVm;
import com.cloud.ocs.portal.core.business.dao.OcsVmDao;

/**
 * Ocs Vm Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-21 下午4:10:19
 *
 */
@Repository
public class OcsVmDaoImpl extends GenericDaoImpl<OcsVm> implements OcsVmDao {

	@Override
	public OcsVm findByVmId(String vmId) {
		Query query = em.createQuery("select model from OcsVm model where model.vmId='" + vmId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while( iterator.hasNext() ) {
			return (OcsVm)iterator.next();
		}
		
		return null;
	}

}
