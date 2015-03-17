package com.cloud.ocs.portal.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.BizAlertMonitorPointDao;

@Repository
public class BizAlertMonitorPointDaoImpl extends GenericDaoImpl<BizAlertMonitorPoint> implements BizAlertMonitorPointDao{

	@Override
	public List<BizAlertMonitorPoint> findAll() {
		Query query = em.createQuery("select bizAlertMonitorPoint from BizAlertMonitorPoint bizAlertMonitorPoint");
		
		return query.getResultList();
	}

}
