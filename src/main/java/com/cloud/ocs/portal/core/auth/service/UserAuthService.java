package com.cloud.ocs.portal.core.auth.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.core.auth.constant.LoginStatus;

/**
 * 系统用户service接口
 * @author Wang Chao
 *
 * @date 2014-12-10 下午7:35:42
 *
 */
public interface UserAuthService {
	public Employee findUserByAccount(String accountId);
	public Employee updateLoginDate(Employee user);
	public LoginStatus checkLogin(String accountId, String accountPassword);
	
	/**
	 * 返回所有计费业务管理人员
	 * @return
	 */
	public List<Employee> getAllBizEmployee();
}
