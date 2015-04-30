package com.cloud.ocs.portal.core.auth.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.ha.service.WarmStandbyVmPool;
import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.core.auth.constant.LoginStatus;
import com.cloud.ocs.portal.core.auth.constant.LoginUserConstant;
import com.cloud.ocs.portal.core.auth.service.UserAuthService;
import com.cloud.ocs.portal.core.monitor.service.OcsEngineMonitorService;
import com.cloud.ocs.schedule.vm.VmDistributionSchedulingJob;

/**
 * 系统用户行为Controller入口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:10:27
 *
 */
@Controller
public class UserAuthController {
	
	@Resource
	private UserAuthService userService;
	
	@Resource
	private VmDistributionSchedulingJob vmDistributionSchedulingJob;
	
	@Resource
	private WarmStandbyVmPool warmStandbyVmPool;
	
	@Resource
	private OcsEngineMonitorService ocsEngineMonitorServiceImpl;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String base(HttpSession session) {
		if (session != null && session.getAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME) != null) {
			session.removeAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME);
		}
		
		return "login";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String userLoginGet(HttpSession session) {
		if (session != null && session.getAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME) != null) {
			session.removeAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME);
		}
		
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Integer userLoginPost(HttpSession session,
			@RequestParam("accountId") String accountId, 
			@RequestParam("accountPassword") String accountPassword) {
		LoginStatus res = userService.checkLogin(accountId, accountPassword);
		if (res.equals(LoginStatus.LOGIN_ACCOUNT_ID_ERROR)) {
			return LoginStatus.LOGIN_ACCOUNT_ID_ERROR.getCode();
		}
		if (res.equals(LoginStatus.LOGIN_ACCOUNT_PASSWORD_ERROE)) {
			return LoginStatus.LOGIN_ACCOUNT_PASSWORD_ERROE.getCode();
		}
		
		Employee loginUser =  userService.findUserByAccount(accountId);
		session.setAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME, loginUser);
		userService.updateLoginDate(loginUser);
		return LoginStatus.LOGIN_SUCCESS.getCode();
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String userIndex() {
//		vmDistributionSchedulingJob.executeJob();
//		warmStandbyVmPool.fillWarmStandbyVmPoolJob();
		ocsEngineMonitorServiceImpl.checkAndUpdateOcsEngineStateOnAllVms();
		return "index";
	}
	
}
