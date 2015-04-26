package com.cloud.ocs.portal.common.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.common.dao.EmployeeDao;

/**
 * 系统用户Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:06:32
 *
 */
@Repository
public class EmployeeDaoImpl extends GenericDaoImpl<Employee> implements EmployeeDao {

	@Override
	public Employee findUserByAccountId(String accountId) {
		Query query = em.createQuery("select user from Employee user where user.accountId='" + accountId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while( iterator.hasNext() ) {
			return (Employee)iterator.next();
		}
		
		return null;
	}

	@Override
	public List<Employee> findAllBizEmployee() {
		Query query = em.createQuery("select user from Employee user where user.employeeType='" + 1 + "'");
		
		return query.getResultList();
	}

	@Override
	public List<Employee> findAllMaintenanceEmployee() {
		Query query = em.createQuery("select user from Employee user where user.employeeType='" + 0 + "'");
		
		return query.getResultList();
	}
}
