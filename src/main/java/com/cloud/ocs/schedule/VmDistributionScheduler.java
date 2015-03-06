package com.cloud.ocs.schedule;

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
public class VmDistributionScheduler {

	private GAExecutor gaExecutor;
	
	public VmDistributionScheduler() {
		this.gaExecutor = new GAExecutor();
	}

	/**
	 * 检测参数中的分布是否需要进行虚拟机分布调度
	 * @param individual
	 * @return
	 */
	public boolean checkWheatherNeedToExecutingScheduling(Individual individual) {
		return false;
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
}
