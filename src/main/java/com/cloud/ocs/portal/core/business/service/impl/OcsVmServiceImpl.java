package com.cloud.ocs.portal.core.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;
import com.cloud.ocs.portal.common.cs.asyncjob.dto.AsynJobResultDto;
import com.cloud.ocs.portal.common.cs.asyncjob.service.QueryAsyncJobResultService;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.CloudOcsServicePublicPort;
import com.cloud.ocs.portal.core.business.constant.VmState;
import com.cloud.ocs.portal.core.business.dto.AddOcsVmDto;
import com.cloud.ocs.portal.core.business.dto.CityNetworkListDto;
import com.cloud.ocs.portal.core.business.dto.LoadBalancerRuleDto;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.core.business.service.PublicIpService;
import com.cloud.ocs.portal.core.sync.job.SyncCityNetworkStateJob;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;

/**
 * 网络-Vm Service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-3 下午9:07:59
 *
 */
@Transactional
@Service
public class OcsVmServiceImpl implements OcsVmService {
	
	private static final String VM_ASYNC_JOB_RESULT_NAME = "virtualmachine";
	
	@Resource
	private PublicIpService publicIpService;
	
	@Resource
	private CityNetworkService cityNetworkSerivce;

	@Override
	public List<OcsVmDto> getOcsVmsListByNetworkId(String networkId) {	
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_OCS_VM);
		request.addRequestParams("networkid", networkId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<OcsVmDto> result = new ArrayList<OcsVmDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null) {
					for (int i = 0; i < vmsJsonArrayObj.length(); i++) {
						JSONObject jsonObj = (JSONObject)vmsJsonArrayObj.get(i);
						OcsVmDto ocsVmDto = new OcsVmDto();
						ocsVmDto.setVmId(jsonObj.getString("id"));
						ocsVmDto.setVmName(jsonObj.getString("name"));
						ocsVmDto.setZoneId(jsonObj.getString("zoneid"));
						ocsVmDto.setZoneName(jsonObj.getString("zonename"));
						ocsVmDto.setHostId(jsonObj.getString("hostid"));
						ocsVmDto.setHostName(jsonObj.getString("hostname"));
						ocsVmDto.setNetworkId(networkId);
						ocsVmDto.setServiceOfferingId(jsonObj.getString("serviceofferingid"));
						ocsVmDto.setTemplateId(jsonObj.getString("templateid"));
						ocsVmDto.setCpuNum(jsonObj.getInt("cpunumber"));
						ocsVmDto.setCpuSpeed(UnitUtil.formatSizeFromHzToGHz(jsonObj.getLong("cpuspeed")));
						ocsVmDto.setMemory(UnitUtil.formatSizeUnit(jsonObj.getLong("memory")*1024*1024));
						ocsVmDto.setVmState(VmState.getCode(jsonObj.getString("state")));
						ocsVmDto.setCreated(jsonObj.getString("created"));
						result.add(ocsVmDto);
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<OcsVmDto>> getOcsVmsListByCityId(Integer cityId) {
		Map<String, List<OcsVmDto>> result = new HashMap<String, List<OcsVmDto>>();
		
		List<CityNetworkListDto> cityNetworks = cityNetworkSerivce.getCityNetworksList(cityId);
		
		if (cityNetworks != null) {
			for (CityNetworkListDto cityNetwork : cityNetworks) {
				result.put(cityNetwork.getNetworkName(), this.getOcsVmsListByNetworkId(cityNetwork.getNetworkId()));
			}
		}
		
		return result;
	}

	@Override
	public Integer getOcsVmsNum(String networkId) {
		int result = 0;
		
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_OCS_VM);
		request.addRequestParams("networkid", networkId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("count")) {
				result = vmsListJsonObj.getInt("count");
			}
		}
		
		return result;
	}

	@Override
	public AddOcsVmDto addOcsVm(String vmName, String networkId, String zoneId,
			String serviceOfferingId, String templateId) {
		AddOcsVmDto result = new AddOcsVmDto();
		result.setCode(AddOcsVmDto.ADD_OCS_VM_CODE_ERROR); //初始默认为不成功
		
		String jobId = this.sendDeployVmRequestToCs(vmName, networkId, zoneId, serviceOfferingId, templateId);
		
		if (jobId == null) {
			result.setMessage("Send Adding Vm Request to Cloudstack Error.");
			return result;
		}

		AsynJobResultDto asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, VM_ASYNC_JOB_RESULT_NAME);
		//每隔3秒发送一次请求，查询Job状态
		while (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.PENDING.getCode()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, VM_ASYNC_JOB_RESULT_NAME);
		}
		
		//Job执行失败
		if (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED.getCode()) {
			if (asyncJobResult.getJobResultType().equals("text")) {
				result.setMessage((String)asyncJobResult.getJobResult());
			}
			return result;
		}
		
		//Job执行成功
		if (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS.getCode()) {
			if (asyncJobResult.getJobResultType().equals("object")) {
				result.setCode(AddOcsVmDto.ADD_OCS_VM_CODE_SUCCESS);
				result.setMessage("Adding Vm Success.");
				JSONObject jsonObj = (JSONObject)asyncJobResult.getJobResult();
				OcsVmDto ocsVmDto = new OcsVmDto();
				ocsVmDto.setVmId(jsonObj.getString("id"));
				ocsVmDto.setVmName(vmName);
				ocsVmDto.setZoneId(zoneId);
				ocsVmDto.setZoneName(jsonObj.getString("zonename"));
				ocsVmDto.setHostId(jsonObj.getString("hostid"));
				ocsVmDto.setHostName(jsonObj.getString("hostname"));
				ocsVmDto.setNetworkId(networkId);
				ocsVmDto.setServiceOfferingId(serviceOfferingId);
				ocsVmDto.setTemplateId(templateId);
				ocsVmDto.setVmState(VmState.getCode(jsonObj.getString("state")));
				ocsVmDto.setCreated(jsonObj.getString("created"));
				result.setOcsVmDto(ocsVmDto);
				result.setIndex(this.getOcsVmsNum(networkId));
				
				//执行一次更新本地数据库中的城市、网络状态(包括public ip等等字段)
				SyncCityNetworkStateJob syncCityNetworkStateJob = new SyncCityNetworkStateJob();
				syncCityNetworkStateJob.executeSyncCityNetworkStateJob();
				
				CityNetwork cityNetwork = cityNetworkSerivce.getCityNetworkByNetworkId(ocsVmDto.getNetworkId());
				String publicIpId = cityNetwork.getPublicIpId();
				String networkName = cityNetwork.getNetworkName();
				
				//检查网络防火墙是否配置，若无则异步添加防火墙规则
				this.checkAndAddNetworkFirewallRule(publicIpId);
				
				//检查负载均衡是否配置，若无则异步添加负载均衡规则，并将新增的VM添加到负载均衡的规则中
				this.checkAndAddVmToLoadBalancerRule(publicIpId, ocsVmDto.getVmId(), ocsVmDto.getNetworkId(), networkName);
				
				return result;
			}
		}
		
		return result;
	}
	
	/**
	 * 发送部署Vm请求到CloudStack
	 * @param vmName
	 * @param networkId
	 * @param zoneId
	 * @param serviceOfferingId
	 * @param templateId
	 * @return job id
	 */
	private String sendDeployVmRequestToCs(String vmName, String networkId, String zoneId,
			String serviceOfferingId, String templateId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_DEPLOY_OCS_VM);
		request.addRequestParams("name", vmName);
		request.addRequestParams("displayname", vmName);
		request.addRequestParams("zoneid", zoneId);
		request.addRequestParams("serviceofferingid", serviceOfferingId);
		request.addRequestParams("templateid", templateId);
		request.addRequestParams("iptonetworklist[0].networkid", networkId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("deployvirtualmachineresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}
	
	/**
	 * 检查该public IP是否已经存在两条防火墙规则（TCP和ICMP）,若没有则异步请求添加
	 * @param networkId
	 */
	private void checkAndAddNetworkFirewallRule(String publicIpId) {
		if (publicIpId == null) {
			return;
		}
		
		//这里其实单纯从数量上判断有很大问题，为简便，暂且这样处理
		if (publicIpService.getFirewallRulesNum(publicIpId) == 2) {
			return;
		}
		
		publicIpService.addTCPFirewallRule(publicIpId, "0.0.0.0/0", "1", "35535");
		publicIpService.addICMPFirewallRule(publicIpId, "0.0.0.0/0", "-1", "-1");
	}
	
	/**
	 * 将新增Vm添加进入负载均衡规则，若该规则不存在则先创建
	 * @param publicIpId
	 * @return 
	 */
	private void checkAndAddVmToLoadBalancerRule(String publicIpId, String vmId, String networkId, String networkName) {
		if (publicIpId == null) {
			return;
		}
		
		List<LoadBalancerRuleDto> loadBalancerRules = publicIpService.getLoadBalancerRulesList(publicIpId);
		String loadBalancerRuleId = null;
				
		//若原先没有负载均衡规则，则创建规则，返回规则id，并将新增Vm添加到规则中
		if (loadBalancerRules != null && loadBalancerRules.size() == 0) {
			//首先异步创建规则
			loadBalancerRuleId = publicIpService.addLoadBalancerRule(
					publicIpId, networkId, networkName + "-lb-rule", "roundrobin",
					CloudOcsServicePublicPort.PUBLIC_SERVICE_PORT.toString(),
					CloudOcsServicePublicPort.PUBLIC_SERVICE_PORT.toString());
			
			//接着将Vm加入到规则中
			if (loadBalancerRuleId != null) {
				publicIpService.assignVmToLoadBalancerRule(loadBalancerRuleId, vmId);
			}
			return;
		}
		
		//否则，默认取除第一条
		loadBalancerRuleId = loadBalancerRules.get(0).getLoadBalancerRuleId();
		publicIpService.assignVmToLoadBalancerRule(loadBalancerRuleId, vmId);
	}

}
