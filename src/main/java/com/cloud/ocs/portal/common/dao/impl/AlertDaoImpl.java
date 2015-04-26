package com.cloud.ocs.portal.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.common.dao.AlertDao;

@Repository
public class AlertDaoImpl extends GenericDaoImpl<Alert> implements AlertDao {

	@Override
	public List<Alert> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Alert findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Alert> findAllBizAlerts() {
		Query query = em.createQuery("select alert from Alert alert where alert.firstLevelType = 1");
		
		return query.getResultList();
	}

	@Override
	public List<Alert> findAllInfraAlerts() {
		Query query = em.createQuery("select alert from Alert alert where alert.firstLevelType = 0");
		
		return query.getResultList();
	}

}
