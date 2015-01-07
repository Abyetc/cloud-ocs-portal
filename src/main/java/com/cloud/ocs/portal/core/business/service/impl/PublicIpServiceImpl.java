package com.cloud.ocs.portal.core.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.CloudOcsServicePublicPort;
import com.cloud.ocs.portal.core.business.dto.LoadBalancerRuleDto;
import com.cloud.ocs.portal.core.business.service.PublicIpService;
import com.cloud.ocs.portal.utils.cs.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;

/**
 * 用于操作Public IP的service的实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-6 下午4:05:00
 *
 */
@Transactional
@Service
public class PublicIpServiceImpl implements PublicIpService {

	@Override
	public List<LoadBalancerRuleDto> getLoadBalancerRulesList(String publicIpId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_LOAD_BALANCER_RULE);
		request.addRequestParams("publicipid", publicIpId);
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<LoadBalancerRuleDto> result = new ArrayList<LoadBalancerRuleDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject rulesListJsonObj = responseJsonObj.getJSONObject("listloadbalancerrulesresponse");
			if (rulesListJsonObj.has("loadbalancerrule")) {
				JSONArray rulesJsonArrayObj = rulesListJsonObj.getJSONArray("loadbalancerrule");
				for (int i = 0; i < rulesJsonArrayObj.length(); i++) {
					JSONObject jsonObj = (JSONObject)rulesJsonArrayObj.get(i);
					LoadBalancerRuleDto loadBalancerRuleDto = new LoadBalancerRuleDto();
					loadBalancerRuleDto.setLoadBalancerRuleId(jsonObj.getString("id"));
					loadBalancerRuleDto.setLoadBalancerRuleName(jsonObj.getString("name"));
					loadBalancerRuleDto.setPrivatePort(CloudOcsServicePublicPort.PUBLIC_SERVICE_PORT.toString());
					loadBalancerRuleDto.setPublicPort(CloudOcsServicePublicPort.PUBLIC_SERVICE_PORT.toString());
					loadBalancerRuleDto.setAlgorithm(jsonObj.getString("algorithm"));
					loadBalancerRuleDto.setState(jsonObj.getString("state"));
					result.add(loadBalancerRuleDto);
				}
			}
		}
		
		return result;
	}

	@Override
	public Integer getFirewallRulesNum(String publicIpId) {
		Integer result = 0;
		
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_FIREWALL_RULE);
		request.addRequestParams("ipaddressid", publicIpId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject rulesListJsonObj = responseJsonObj.getJSONObject("listfirewallrulesresponse");
			if (rulesListJsonObj.has("count")) {
				result = rulesListJsonObj.getInt("count");
			}
		}
		
		return result;
	}

	@Override
	public Integer getLoadBalancerRulesNum(String publicIpId) {
		return null;
	}

	@Override
	public Integer getLoadBalancerRuleInstancesNum(String loadBalancerRuleId) {
		return null;
	}

	@Override
	public String addTCPFirewallRule(String publicIpId, String cidrList, String startPort, String endPort) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_CREATE_FIREWALL_RULE);
		request.addRequestParams("ipaddressid", publicIpId);
		request.addRequestParams("cidrlist", cidrList);
		request.addRequestParams("protocol", "tcp");
		request.addRequestParams("startport", startPort);
		request.addRequestParams("endport", endPort);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createfirewallruleresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}

	@Override
	public String addICMPFirewallRule(String publicIpId, String cidrList, String icmpType, String icmpCode) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_CREATE_FIREWALL_RULE);
		request.addRequestParams("ipaddressid", publicIpId);
		request.addRequestParams("cidrlist", cidrList);
		request.addRequestParams("protocol", "icmp");
		request.addRequestParams("icmptype", icmpType);
		request.addRequestParams("icmpcode", icmpCode);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createfirewallruleresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}

	/**
	 * 异步任务，但是返回的不是JobId，是id(其实就是loadBalancer的id)，用于后续接着异步将新增的Vm添加进去
	 */
	@Override
	public String addLoadBalancerRule(String publicIpId, String networkId,
			String ruleName, String algorithm, String privatePort,
			String publicPort) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_CREATE_LOAD_BALANCER_RULE);
		request.addRequestParams("networkid", networkId);
		request.addRequestParams("publicipid", publicIpId);
		request.addRequestParams("algorithm", algorithm);
		request.addRequestParams("name", ruleName);
		request.addRequestParams("privateport", privatePort);
		request.addRequestParams("publicport", publicPort);
		request.addRequestParams("openfirewall", "false");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		String loadBalancerRuleId = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createloadbalancerruleresponse");
			if (resultJsonObj.has("id")) {
				loadBalancerRuleId = resultJsonObj.getString("id");
			}
		}
		
		return loadBalancerRuleId;
	}

	@Override
	public String assignVmToLoadBalancerRule(String loadBalancerRuleId,
			String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_ASSIGN_TO_LOAD_BALANCER_RULE);
		request.addRequestParams("id", loadBalancerRuleId);
		request.addRequestParams("virtualmachineids", vmId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("assigntoloadbalancerruleresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}

}
