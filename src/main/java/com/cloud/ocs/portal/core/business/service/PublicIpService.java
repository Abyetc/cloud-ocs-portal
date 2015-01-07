package com.cloud.ocs.portal.core.business.service;

import java.util.List;

import com.cloud.ocs.portal.core.business.dto.LoadBalancerRuleDto;

/**
 * 用于操作Public Ip的service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-6 下午4:04:22
 *
 */
public interface PublicIpService {

	public Integer getFirewallRulesNum(String publicIpId);
	public Integer getLoadBalancerRulesNum(String publicIpId);
	public Integer getLoadBalancerRuleInstancesNum(String loadBalancerRuleId);
	
	public List<LoadBalancerRuleDto> getLoadBalancerRulesList(String publicIpId);
	public String addLoadBalancerRule(String publicIpId, String networkId, String ruleName, String algorithm, String privatePort, String publicPort);
	
	public String assignVmToLoadBalancerRule(String loadBalancerRuleId, String vmId);
	
	public String addTCPFirewallRule(String publicIpId, String cidrList, String startPort, String endPort);
	public String addICMPFirewallRule(String publicIpId, String cidrList, String icmpType, String icmpCode);
}
