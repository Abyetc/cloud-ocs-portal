package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.Employee;

/**
 * 系统用户Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:07:09
 *
 */
public interface EmployeeDao extends GenericDao<Employee> {
	public Employee findUserByAccountId(String accountId);
	
	public List<Employee> findAllBizEmployee();
	
	public List<Employee> findAllMaintenanceEmployee();
}
