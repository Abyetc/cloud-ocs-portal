package com.cloud.ocs.portal.core.business.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;
import com.cloud.ocs.portal.common.cs.asyncjob.dto.AsynJobResultDto;
import com.cloud.ocs.portal.common.cs.asyncjob.service.QueryAsyncJobResultService;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.business.cache.OcsVmForwardingPortCache;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;
import com.cloud.ocs.portal.core.business.constant.OcsVmState;
import com.cloud.ocs.portal.core.business.dto.AddOcsVmDto;
import com.cloud.ocs.portal.core.business.dto.CityNetworkListDto;
import com.cloud.ocs.portal.core.business.dto.LoadBalancerRuleDto;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.core.business.service.PublicIpService;
import com.cloud.ocs.portal.core.sync.service.SyncCityStateService;
import com.cloud.ocs.portal.core.sync.service.SyncNetworkStateService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.DateUtil;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

/**
 * 网络-Vm Service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-3 下午9:07:59
 *
 */
@Transactional(value="portal_em")
@Service
public class OcsVmServiceImpl implements OcsVmService {
	
	private static final String VM_ASYNC_JOB_RESULT_NAME = "virtualmachine";
	
	@Resource
	private OcsVmDao ocsVmDao;
	
	@Resource
	private PublicIpService publicIpService;
	
	@Resource
	private CityNetworkService cityNetworkSerivce;
	
	@Resource
	private OcsVmForwardingPortService vmForwardingPortService;
	
	@Resource
	private SyncNetworkStateService syncNetworkStateService;
	
	@Resource
	private SyncCityStateService syncCityStateService;
	
	@Resource
	private OcsEngineService ocsEngineService;
	
	@Autowired
	private OcsVmForwardingPortCache vmForwardingPortCache;
	
	@Override
	public OcsVm getOcsVmByVmId(String vmId) {
		return ocsVmDao.findByVmId(vmId);
	}
	
	@Override
	public Integer getOcsVmsNum(String networkId) {
		int result = 0;
		
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_OCS_VM);
		request.addRequestParams("networkid", networkId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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
	public List<OcsVmDto> getOcsVmsListByNetworkId(String networkId) {	
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_LIST_OCS_VM);
		request.addRequestParams("networkid", networkId);
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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
						if (jsonObj.has("hostid")) {
							ocsVmDto.setHostId(jsonObj.getString("hostid"));
						}
						if (jsonObj.has("hostname")) {
							ocsVmDto.setHostName(jsonObj.getString("hostname"));
						}
						ocsVmDto.setNetworkId(networkId);
						ocsVmDto.setServiceOfferingId(jsonObj.getString("serviceofferingid"));
						ocsVmDto.setTemplateId(jsonObj.getString("templateid"));
						ocsVmDto.setCpuNum(jsonObj.getInt("cpunumber"));
						ocsVmDto.setCpuSpeed(UnitUtil.formatSizeFromHzToGHz(jsonObj.getLong("cpuspeed")));
						ocsVmDto.setMemory(UnitUtil.formatSizeUnit(jsonObj.getLong("memory")*1024*1024));
						ocsVmDto.setVmState(OcsVmState.getCode(jsonObj.getString("state")));
						String created = jsonObj.getString("created").replace('T', ' ');
						ocsVmDto.setCreated(created.substring(0, created.indexOf('+')));
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
				ocsVmDto.setVmState(OcsVmState.getCode(jsonObj.getString("state")));
				String created = jsonObj.getString("created").replace('T', ' ');
				ocsVmDto.setCreated(created.substring(0, created.indexOf('+')));
				result.setOcsVmDto(ocsVmDto);
				result.setIndex(this.getOcsVmsNum(networkId));
				
				//执行一次更新本地数据库中的城市、网络状态(包括public ip等等字段)
				//同步网络状态
				syncNetworkStateService.syncNetworkState();
				//同步城市状态
				syncCityStateService.syncCityState();
				
				CityNetwork cityNetwork = cityNetworkSerivce.getCityNetworkByNetworkId(ocsVmDto.getNetworkId());
				String publicIpId = cityNetwork.getPublicIpId();
				String publicIp = cityNetwork.getPublicIp();
				String networkName = cityNetwork.getNetworkName();
				
				OcsVm ocsVm = new OcsVm();
				ocsVm.setVmId(jsonObj.getString("id"));
				ocsVm.setNetworkId(networkId);
				ocsVm.setPublicIpId(publicIpId);
				ocsVm.setPublicIp(publicIp);
				JSONObject nicJsonObj = (JSONObject)jsonObj.getJSONArray("nic").get(0);
				ocsVm.setPrivateIp(nicJsonObj.getString("ipaddress"));
				ocsVm.setHostName(jsonObj.getString("hostname"));
				ocsVm.setHsotId(jsonObj.getString("hostid"));
				ocsVm.setState(OcsVmState.getCode(jsonObj.getString("state")));
				ocsVm.setCreated(DateUtil.convertCsDateToTimestamp(jsonObj.getString("created")));
				ocsVmDao.persist(ocsVm);
				
				//检查网络防火墙是否配置，若无则异步添加防火墙规则
				this.checkAndAddNetworkFirewallRule(publicIpId);
				
				//检查负载均衡是否配置，若无则异步添加负载均衡规则，并将新增的VM添加到负载均衡的规则中
				this.checkAndAddVmToLoadBalancerRule(publicIpId, ocsVmDto.getVmId(), ocsVmDto.getNetworkId(), networkName);
				
				//添加用于monitor和ssh的两条端口转发规则并记录入库
				this.addPortForwardingRule(networkId, publicIpId, publicIp, ocsVmDto.getVmId());
				//更新端口转发规则数据缓存
				vmForwardingPortCache.reloadDataFromDB();
				
				//使用SSH远程启动vm上的ocs引擎程序，并将记录入库
				this.startOcsEngineOnOcsVm(ocsVmDto.getVmId());
			}
		}
		
		return result;
	}
	
	@Override
	public OperateObjectDto removeOcsVm(String vmId) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR); //初始化为默认不成功
		
		String jobId = this.sendRemoveVmRequestToCs(vmId);
		
		if (jobId == null) {
			result.setMessage("Send Removing Vm Request to Cloudstack Error.");
			return result;
		}
		
		AsynJobResultDto asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "null");
		//每隔3秒发送一次请求，查询Job状态
		while (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.PENDING.getCode()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "null");
		}
		
		//Job执行失败
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED
						.getCode()) {
			if (asyncJobResult.getJobResultType().equals("text")) {
				result.setMessage((String) asyncJobResult.getJobResult());
			}
			return result;
		}
		
		//Job执行成功
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS
						.getCode()) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Removing Vm Success.");
			
			//将记录从数据库中删除
			ocsVmDao.deleteByVmId(vmId);
			
			//执行一次更新本地数据库中的城市、网络状态(包括public ip等等字段)
			//同步网络状态
			syncNetworkStateService.syncNetworkState();
			//同步城市状态
			syncCityStateService.syncCityState();
			
			//更新端口转发规则数据缓存
			vmForwardingPortCache.reloadDataFromDB();
		}
		
		return result;
	}
	
	@Override
	public OperateObjectDto stopOcsVm(String vmId) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR); //初始化为默认不成功
		
		String jobId = this.sendStopVmRequestToCs(vmId);
		
		if (jobId == null) {
			result.setMessage("Send Stoping Vm Request to Cloudstack Error.");
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
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED
						.getCode()) {
			if (asyncJobResult.getJobResultType().equals("text")) {
				result.setMessage((String) asyncJobResult.getJobResult());
			}
			return result;
		}
		
		//Job执行成功
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS
						.getCode()) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Stoping Vm Success.");
			
			//更新数据库中记录
			OcsVm ocsVm = ocsVmDao.findByVmId(vmId);
			ocsVm.setHsotId(null);
			ocsVm.setHostName(null);
			ocsVm.setState(OcsVmState.STOPPED.getCode());
			ocsVmDao.update(ocsVm);
		}
		
		return result;
	}
	
	@Override
	public OperateObjectDto startOcsVm(String vmId) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR); //初始化为默认不成功
		
		String jobId = this.sendStartVmRequestToCs(vmId);
		
		if (jobId == null) {
			result.setMessage("Send Starting Vm Request to Cloudstack Error.");
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
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED
						.getCode()) {
			if (asyncJobResult.getJobResultType().equals("text")) {
				result.setMessage((String) asyncJobResult.getJobResult());
			}
			return result;
		}
		
		//Job执行成功
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS
						.getCode()) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Starting Vm Success.");
			JSONObject jsonObj = (JSONObject)asyncJobResult.getJobResult();
			result.setOperatedObject(jsonObj.getString("hostname"));
			
			//更新数据库中记录
			OcsVm ocsVm = ocsVmDao.findByVmId(vmId);
			ocsVm.setHsotId(jsonObj.getString("hostid"));
			ocsVm.setHostName(jsonObj.getString("hostname"));
			ocsVm.setState(OcsVmState.getCode(jsonObj.getString("state")));
			ocsVmDao.update(ocsVm);
		}
		
		return result;
	}
	
	public void startOcsEngineOnOcsVm(String vmId) {
		OcsEngine ocsEngine = new OcsEngine();
		ocsEngine.setVmId(vmId);
		ocsEngine.setOcsEngineState(OcsEngineState.STOPPED.getCode()); //初始默认为程序停止
		if (ocsEngineService.startOcsEngineService(vmId)) {
			//引擎启动成功
			ocsEngine.setOcsEngineState(OcsEngineState.RUNNING.getCode());
			Date startDate = new Date();
			ocsEngine.setLastStartDate(new Timestamp(startDate.getTime()));
		}
		ocsEngineService.save(ocsEngine);
	}
	
	@Override
	public Integer getOcsVmstate(String vmId) {
		
		return ocsVmDao.findOcsVmStateByVmId(vmId);
	}

	
	/**
	 * 发送部署Vm请求到CloudStack(异步请求)
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
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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
	 * 发送删除(彻底销毁)Vm的请求到CloudStack(异步请求)
	 * @param vmId
	 * @return
	 */
	private String sendRemoveVmRequestToCs(String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_REMOVE_OCS_VM);
		request.addRequestParams("id", vmId);
		request.addRequestParams("expunge", "true"); //直接销毁
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("destroyvirtualmachineresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}
	
	/**
	 * 发送停止Vm的请求到CloudStack(异步请求)
	 * @param vmId
	 * @return
	 */
	private String sendStopVmRequestToCs(String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_STOP_OCS_VM);
		request.addRequestParams("id", vmId);
		request.addRequestParams("forced", "false"); //直接销毁
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("stopvirtualmachineresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}
	
	/**
	 * 发送开启Vm的请求到CloudStack(异步请求)
	 * @param vmId
	 * @return
	 */
	private String sendStartVmRequestToCs(String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_START_OCS_VM);
		request.addRequestParams("id", vmId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("startvirtualmachineresponse");
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
		
		publicIpService.addTCPFirewallRule(publicIpId, "0.0.0.0/0", "1", "65535");
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
					OcsVmProperties.getOcsVmEngineServicePort().toString(),
					OcsVmProperties.getOcsVmEngineServicePort().toString());
			
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
	
	/**
	 * 分别异步添加用于monitor和ssh的两条端口转发规则，并将规则入库
	 * @param networkId
	 * @param vmId
	 */
	private void addPortForwardingRule(String networkId, String ipAddressId,
			String ipAddress, String vmId) {
		Integer monitorPublicPort = vmForwardingPortService.generateUniquePublicPort(networkId);
		Integer sshPublicPort = vmForwardingPortService.generateUniquePublicPort(networkId);
		while (sshPublicPort.equals(monitorPublicPort)) {
			sshPublicPort = vmForwardingPortService.generateUniquePublicPort(networkId);
		}
		
		// 异步发送用于monitor的端口转发规则请求到CloudStack
		this.sendCreatePortForwardingRequestToCs(networkId, ipAddressId, vmId, monitorPublicPort, OcsVmProperties.getOcsVmMonitorPrivateServicePort());
		this.sendCreatePortForwardingRequestToCs(networkId, ipAddressId, vmId, sshPublicPort, OcsVmProperties.getOcsVmSshPrivateServicePort());
		
		// 然后直接将记录入库(其实这里有问题，应该是等异步任务返回才能入库，先不理)
		vmForwardingPortService.saveForwardingPort(networkId, ipAddress,
				ipAddressId, vmId, monitorPublicPort,
				OcsVmProperties.getOcsVmMonitorPrivateServicePort(),
				sshPublicPort, OcsVmProperties.getOcsVmSshPrivateServicePort());
	}
	
	/**
	 * 异步发送添加端口转发的请求到CloudStack
	 * @param networkId
	 * @param ipAddressId
	 * @param vmId
	 * @param publicPort
	 * @return 异步Job的Id
	 */
	private String sendCreatePortForwardingRequestToCs(String networkId,
			String ipAddressId, String vmId, Integer publicPort, Integer privatePort) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_CREATE_PORT_FORWARDING_RULE);
		request.addRequestParams("ipaddressid", ipAddressId);
		request.addRequestParams("protocol", "tcp");
		request.addRequestParams("virtualmachineid", vmId);
		request.addRequestParams("openfirewall", "false");
		request.addRequestParams("networkid", networkId);
		request.addRequestParams("publicport", publicPort.toString());
		request.addRequestParams("publicendport", publicPort.toString());
		request.addRequestParams("privateport", privatePort.toString());
		request.addRequestParams("privateendport", privatePort.toString());
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createportforwardingruleresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}

}
