package com.cloud.ocs.schedule;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.core.monitor.service.HostMonitorService;
import com.cloud.ocs.portal.core.monitor.service.OcsVmMonitorService;
import com.cloud.ocs.schedule.ga.GAExecutor;
import com.cloud.ocs.schedule.ga.Individual;

/**
 * 云环境中虚拟机资源分布调度器
 * 
 * @author Wang Chao
 *
 * @date 2015-3-5 下午5:05:04
 *
 */
@Service
public class VmDistributionScheduler {

	private GAExecutor gaExecutor;
	
	@Resource
	private OcsHostDao ocsHostDao;
	
	@Resource
	private OcsVmDao ocsVmDao;
	
	@Resource
	private HostMonitorService hostMonitorService;
	
	@Resource
	private OcsVmMonitorService ocsVmMonitorService;
	
	public VmDistributionScheduler() {
		this.gaExecutor = new GAExecutor();
	}
	
	/**
	 * 收集系统当前负载数据
	 * @return
	 */
	public List<LoadData> collectSystemLoadData() {
		List<LoadData> result = null;
		List<String> allHostIds = ocsHostDao.findAllHostIds();
		
		if (allHostIds == null) {
			return null;
		}
		
		result = new ArrayList<LoadData>();
		int vmNo = 0;
		for (int i = 0; i < allHostIds.size(); i++) {
			List<String> allVmIdsOnHost = ocsVmDao.findAllRunningVmsOnHost(allHostIds.get(i));
			if (allVmIdsOnHost == null) {
				continue;
			}
			for (int j = 0; j < allVmIdsOnHost.size(); j++) {
				long hostTotalCpuCapacity = hostMonitorService.getHostTotalCpuCapacity(allHostIds.get(i));
				long vmTotalCpuCapacity = ocsVmMonitorService.getVmTotalCpuCapacity(allVmIdsOnHost.get(j));
				double vmCurCpuUsagePercentage = ocsVmMonitorService.getVmCurCpuUsagePercentage(allVmIdsOnHost.get(j));
				double vmLoadData = (double)((vmTotalCpuCapacity*vmCurCpuUsagePercentage*100.0) / hostTotalCpuCapacity);
				
				LoadData loadData = new LoadData();
				loadData.setHostId(allHostIds.get(i));
				loadData.setVmId(allVmIdsOnHost.get(j));
				PmLoad pmLoad = new PmLoad();
				pmLoad.setPmNo(i);
				VmLoad vmLoad = new VmLoad(vmNo, vmLoadData);
				vmNo++;
				loadData.setPmLoad(pmLoad);
				loadData.setVmLoad(vmLoad);
				result.add(loadData);
			}
		}

		return result;
	}

	/**
	 * 检测参数中的分布是否需要进行虚拟机分布调度
	 * @param individual
	 * @return
	 */
	public boolean checkWheatherNeedToExecutingScheduling(Individual individual) {
		individual.computeFitness();
		
		if (individual.getFitness() >= GAExecutor.MIN_TERMINAL_FITNESS) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 计算得到当前负载下的最优分布
	 * @param pmNum
	 * @param vmNum
	 * @param vmsLoad
	 * @return
	 */
	public Individual computeOptimalDistribution(int pmNum, int vmNum, double[] vmsLoad) {
		return gaExecutor.executeGA(pmNum, vmNum, vmsLoad);
	}
	
	/**
	 * 根据参数执行最优分布的虚拟机调度
	 * @param optimalDistribution
	 */
	public void executeScheduling(Individual optimalDistribution) {
		
	}
	
	/**
	 * 动态迁移虚拟机
	 * @param vmId
	 * @param srcHostId
	 * @param desHostId
	 */
	public void dynamicMigrateVm(String vmId, String srcHostId, String desHostId) {
		
	}
}
