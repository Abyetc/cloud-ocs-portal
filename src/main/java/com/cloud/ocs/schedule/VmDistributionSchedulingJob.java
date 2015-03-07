package com.cloud.ocs.schedule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cloud.ocs.schedule.ga.Individual;

/**
 * 用于虚拟机分布调度的定时任务类
 * 
 * @author Wang Chao
 *
 * @date 2015-3-7 下午2:05:47
 *
 */
public class VmDistributionSchedulingJob {
	
	@Resource
	private VmDistributionScheduler scheduler;
	
	public void executeJob() {
		//采集数据
		List<LoadData> loadData = scheduler.collectSystemLoadData();
		
		if (loadData == null) {
			return;
		}
		
		int vmNum = loadData.size();
		if (vmNum == 0) {
			return;
		}
		Set<String> hostIdSet = new HashSet<String>();
		double[] vmsLoad = new double[vmNum];
		for (int i = 0; i < loadData.size(); i++) {
			hostIdSet.add(loadData.get(i).getHostId());
			vmsLoad[i] = loadData.get(i).getVmLoad().getLoad();
		}
		int pmNum = hostIdSet.size();
		
		//将采集来的数据转换为Individual对象
		Individual originIndividual = new Individual(pmNum, vmNum, vmsLoad);
		for (int i = 0; i < loadData.size(); i++) {
			originIndividual.addVM(loadData.get(i).getPmLoad().getPmNo(), loadData.get(i).getVmLoad().getVmNo());
		}
		
		//看当前分布是否均衡是否需要调用GA算法
		if (!scheduler.checkWheatherNeedToExecutingScheduling(originIndividual)) {
			System.out.println(11111);
			return;
		}
		
		//若需要，则调用computeOptimalDistribution(int pmNum, int vmNum, double[] vmsLoad)
		Individual optimalDistributionIndividual = scheduler.computeOptimalDistribution(pmNum, vmNum, vmsLoad);
		
		//最后执行完成VM动态迁移工作
		scheduler.executeScheduling(optimalDistributionIndividual);
	}
}
