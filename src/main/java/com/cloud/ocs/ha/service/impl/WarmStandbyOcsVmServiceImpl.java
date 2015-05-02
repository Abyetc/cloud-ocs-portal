package com.cloud.ocs.ha.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.ha.service.WarmStandbyOcsVmService;
import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.common.cache.OcsVmForwardingPortCache;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;
import com.cloud.ocs.portal.common.cs.asyncjob.dto.AsynJobResultDto;
import com.cloud.ocs.portal.common.cs.asyncjob.service.QueryAsyncJobResultService;
import com.cloud.ocs.portal.common.dao.WarmStandbyOcsVmDao;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;
import com.cloud.ocs.portal.core.business.constant.OcsVmState;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.DateUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

@Transactional(value="portal_em")
@Service
public class WarmStandbyOcsVmServiceImpl implements WarmStandbyOcsVmService {

	private final static String ZONE_ID = "5497e85a-6821-47ca-a6d7-6cad39db8d49";
	private final static String SERVICE_OFFERING_ID = "294612af-a5b6-4f68-9812-1fa6312f0647";
	private final static String TEMPLETE_ID = "b21a4bf5-c432-4162-8fbc-a80236384d65";

	@Resource
	private WarmStandbyOcsVmDao warmStandbyOcsVmDao;
	
	@Resource
	private CityNetworkService cityNetworkService;
	
	@Resource
	private OcsVmForwardingPortService vmForwardingPortService;
	
	@Autowired
	private OcsVmForwardingPortCache vmForwardingPortCache;
	
	@Resource
	private OcsEngineService ocsEngineService;
	
	
	public List<WarmStandbyOcsVm> getAllSuitableWarmStandbyOcsVmList() {
		return warmStandbyOcsVmDao.findAllAvailableWarmStandbyOcsVm();
	}

	@Override
	public WarmStandbyOcsVm getWarmStandbyOcsVmById(String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWarmStandbyOcsVmNumInNetwork(String networkId) {
		return warmStandbyOcsVmDao.findWarmStandbyOcsVmNumInNetwork(networkId);
	}

	@Override
	public void addWarmStandbyOcsVm(String networkId) {
		String jobId = this.sendDeployVmRequestToCs(networkId, ZONE_ID, SERVICE_OFFERING_ID, TEMPLETE_ID);
		
		if (jobId == null) {
			return;
		}

		AsynJobResultDto asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "virtualmachine");
		//每隔3秒发送一次请求，查询Job状态
		while (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.PENDING.getCode()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "virtualmachine");
		}
		
		//Job执行失败
		if (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED.getCode()) {
			if (asyncJobResult.getJobResultType().equals("text")) {
				return;
			}
		}
		
		//Job执行成功
		if (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS.getCode()) {
			if (asyncJobResult.getJobResultType().equals("object")) {
				JSONObject jsonObj = (JSONObject)asyncJobResult.getJobResult();
				
				CityNetwork cityNetwork = cityNetworkService.getCityNetworkByNetworkId(networkId);
				String publicIpId = cityNetwork.getPublicIpId();
				String publicIp = cityNetwork.getPublicIp();
				
				WarmStandbyOcsVm ocsVm = new WarmStandbyOcsVm();
				ocsVm.setVmId(jsonObj.getString("id"));
				ocsVm.setNetworkId(networkId);
				ocsVm.setPublicIpId(publicIpId);
				ocsVm.setPublicIp(publicIp);
				JSONObject nicJsonObj = (JSONObject)jsonObj.getJSONArray("nic").get(0);
				ocsVm.setPrivateIp(nicJsonObj.getString("ipaddress"));
				ocsVm.setHostName(jsonObj.getString("hostname"));
				ocsVm.setHostId(jsonObj.getString("hostid"));
				ocsVm.setState(OcsVmState.getCode(jsonObj.getString("state")));
				ocsVm.setCreated(DateUtil.convertCsDateToTimestamp(jsonObj.getString("created")));
				warmStandbyOcsVmDao.persist(ocsVm);
				
				//添加用于monitor和ssh的两条端口转发规则并记录入库
				this.addPortForwardingRule(networkId, publicIpId, publicIp, ocsVm.getVmId());
				//更新端口转发规则数据缓存
				vmForwardingPortCache.reloadDataFromDB();
				
				//使用SSH远程启动vm上的ocs引擎程序，并将记录入库
				this.startOcsEngineOnOcsVm(ocsVm.getVmId());
			}
		}
	}
	
	private String sendDeployVmRequestToCs(String networkId, String zoneId,
			String serviceOfferingId, String templateId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_DEPLOY_OCS_VM);
		request.addRequestParams("name", "warm-standby-ocs-vm");
		request.addRequestParams("zoneid", zoneId);
		request.addRequestParams("serviceofferingid", serviceOfferingId);
		request.addRequestParams("templateid", templateId);
		request.addRequestParams("iptonetworklist[0].networkid", networkId);
		
		request.addRequestParams("hostid", "94a8b9e1-125d-4dc4-ae52-d0058826e43b");
		
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
