package com.cloud.ocs.portal.core.business.constant;

/**
 * 系统业务模块调用CloudStack API名称
 * 
 * @author Wang Chao
 *
 * @date 2015-1-3 下午4:51:55
 *
 */
public class BusinessApiName {

	public static final String BUSINESS_API_ADD_NETWORK    				 =    "createNetwork";
	
	public static final String BUSINESS_API_LIST_OCS_VM   				 = 	  "listVirtualMachines";
	public static final String BUSINESS_API_DEPLOY_OCS_VM 				 =	  "deployVirtualMachine";
	
	public static final String BUSINESS_API_CREATE_EGRESS_FIREWALL_RULE  =	  "createEgressFirewallRule";
	
	public static final String BUSINESS_API_LIST_FIREWALL_RULE			 =	  "listFirewallRules";
	public static final String BUSINESS_API_CREATE_FIREWALL_RULE		 =	  "createFirewallRule";
	
	public static final String BUSINESS_API_LIST_LOAD_BALANCER_RULE		 =	  "listLoadBalancerRules";
	public static final String BUSINESS_API_CREATE_LOAD_BALANCER_RULE    =	  "createLoadBalancerRule";
	
	public static final String BUSINESS_API_ASSIGN_TO_LOAD_BALANCER_RULE =    "assignToLoadBalancerRule";
}
