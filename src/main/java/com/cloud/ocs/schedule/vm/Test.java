package com.cloud.ocs.schedule.vm;

public class Test {

	public static void main(String[] args) {
		int pmNum = 5;
		int vmNum = 16;
		double[] vmsLoad = {28.8, 23.4, 17.9, 16.8, 12.6, 22.3, 13.9, 40.2,
							18.0, 9.2, 8.8, 7.3, 8.1, 28.8, 24.0, 26.9};
		
		VmDistributionScheduler scheduler = new VmDistributionScheduler();
		scheduler.computeOptimalDistribution(pmNum, vmNum, vmsLoad);
	}
	
//	public static double computePMsLoadVariation(double nums[]) {
//		double pmsLoadExpectation = 61.4;
//		double sum = 0.0;
//		for (int i= 0; i < nums.length; i++) {
//			sum += Math.abs((nums[i] - pmsLoadExpectation) * (nums[i] - pmsLoadExpectation));
//		}
//		return Math.sqrt(sum/(double)nums.length);
//	}

}