package com.cloud.ocs.portal.core.user.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.user.bean.User;
import com.cloud.ocs.portal.core.user.constant.LoginStatus;
import com.cloud.ocs.portal.core.user.constant.LoginUserConstant;
import com.cloud.ocs.portal.core.user.service.UserService;
import com.cloud.ocs.portal.utils.sigar.NetworkData;
import com.cloud.ocs.portal.utils.sigar.SigarUtil;

/**
 * 系统用户行为Controller入口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-9 下午9:10:27
 *
 */
@Controller
public class UserController {
	
	@Resource
	private UserService userService;
	
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
		
		User loginUser =  userService.findUserByAccount(accountId);
		session.setAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME, loginUser);
		userService.updateLoginDate(loginUser);
		return LoginStatus.LOGIN_SUCCESS.getCode();
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String userIndex() {
		return "index";
	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
//		Sigar sigar = SigarUtil.sigar;
//		try {
//			System.out.println(sigar.getCpuInfoList().length);
//		} catch (SigarException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
        try {
			new NetworkData(SigarUtil.sigar);
			NetworkData.newMetricThread();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return "index";
	}
}
