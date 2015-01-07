package com.cloud.ocs.portal.core.business.dto;

/**
 * 用于表示虚拟路由器负载均衡规则的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-6 下午4:12:38
 * 
 */
public class LoadBalancerRuleDto {

	private String loadBalancerRuleId;
	private String loadBalancerRuleName;
	private String publicPort;
	private String privatePort;
	private String algorithm;
	private String state;

	public String getLoadBalancerRuleId() {
		return loadBalancerRuleId;
	}

	public void setLoadBalancerRuleId(String loadBalancerRuleId) {
		this.loadBalancerRuleId = loadBalancerRuleId;
	}

	public String getLoadBalancerRuleName() {
		return loadBalancerRuleName;
	}

	public void setLoadBalancerRuleName(String loadBalancerRuleName) {
		this.loadBalancerRuleName = loadBalancerRuleName;
	}

	public String getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(String publicPort) {
		this.publicPort = publicPort;
	}

	public String getPrivatePort() {
		return privatePort;
	}

	public void setPrivatePort(String privatePort) {
		this.privatePort = privatePort;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
