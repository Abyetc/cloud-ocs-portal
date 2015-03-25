package com.cloud.ocs.schedule.request.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.schedule.request.dto.VmLoadData;
import com.cloud.ocs.schedule.request.dto.VmLoadNormalizedData;
import com.cloud.ocs.schedule.request.properties.RequestScheduleProperties;
import com.cloud.ocs.schedule.request.service.HAProxyService;
import com.cloud.ocs.schedule.request.service.VmLoadDataAsker;

/**
 * 更新调度权值的定时任务
 * 
 * @author Wang Chao
 * 
 * @date 2015-3-25 下午3:08:56
 * 
 */
public class UpdateWeightJob {

	@Resource
	private OcsVmService ocsVmServcie;
	
	@Resource
	private VmLoadDataAsker vmLoadDataAsker;
	
	@Resource
	private HAProxyService haproxyService;

	@Resource
	private OcsVmForwardingPortService ocsVmForwardingPortService;

	public void executeUpdatingWeight() {

		// 首先获取城市ID
		int cityId = RequestScheduleProperties.getCityId();

		// 获取该城市内所有虚拟机
		List<OcsVmDto> allVmsInCity = ocsVmServcie.getOcsVmListByCityId(cityId);

		Map<String, OcsVmForwardingPort> allVmsForwardingPort = ocsVmForwardingPortService
				.getVmForwardingPortMap(this.getAllAvailableVmIds(allVmsInCity));
		
		
		Map<String, VmLoadData> allVmLoadData = new HashMap<String, VmLoadData>();
		Map<String, Double> allVmResponseTime = new HashMap<String, Double>();
		for (OcsVmDto ocsVm : allVmsInCity) {
			
		}

	}
	
	/**
	 * 将负载数据归一化
	 * @param allVmLoadData
	 * @param allVmResponseTime
	 * @return
	 */
	public Map<String, VmLoadNormalizedData> normalizeVmLoadData(Map<String, VmLoadData> allVmLoadData, Map<String, Double> allVmResponseTime) {
		return null;
	}
	
	/**
	 * 计算权重值
	 * @param normalizedVmLoadData
	 * @return
	 */
	public Map<String, Integer> calculateVmWeight(Map<String, VmLoadNormalizedData> normalizedVmLoadData) {
		return null;
	}

	private List<String> getAllAvailableVmIds(List<OcsVmDto> allOcsVms) {
		List<String> result = new ArrayList<String>();

		for (OcsVmDto one : allOcsVms) {
			result.add(one.getVmId());
		}

		return result;
	}
}
