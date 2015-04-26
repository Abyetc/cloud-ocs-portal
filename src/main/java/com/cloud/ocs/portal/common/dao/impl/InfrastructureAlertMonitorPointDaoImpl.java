package com.cloud.ocs.portal.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.InfrastructureAlertMonitorPointDao;

@Repository
public class InfrastructureAlertMonitorPointDaoImpl extends
		GenericDaoImpl<InfrastructureAlertMonitorPoint> implements
		InfrastructureAlertMonitorPointDao {

	@Override
	public List<InfrastructureAlertMonitorPoint> findAll() {
		Query query = em
				.createQuery("select infraAlertMonitorPoint from InfrastructureAlertMonitorPoint infraAlertMonitorPoint");

		return query.getResultList();
	}

	@Override
	public InfrastructureAlertMonitorPoint findById(
			Integer infraAlertMonitorPointId) {
		Query query = em
				.createQuery("select infraAlertMonitorPoint from InfrastructureAlertMonitorPoint infraAlertMonitorPoint where infraAlertMonitorPoint.id='"
						+ infraAlertMonitorPointId + "'");

		return (InfrastructureAlertMonitorPoint) query.getSingleResult();
	}

}
