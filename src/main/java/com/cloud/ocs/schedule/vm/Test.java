package com.cloud.ocs.schedule.vm;

import com.cloud.ocs.schedule.vm.ga.Individual;

public class Test {

	public static void main(String[] args) {
		int pmNum = 5;
		int vmNum = 16;
		double[] vmsLoad = {28.8, 23.4, 17.9, 16.8, 12.6, 22.3, 13.9, 40.2,
							18.0, 9.2, 8.8, 7.3, 8.1, 28.8, 24.0, 26.9};
		
		Individual originIndividual = new Individual(pmNum,vmNum,vmsLoad);
		originIndividual.addVM(0, 0);
		originIndividual.addVM(0, 1);
		originIndividual.addVM(0, 2);
		originIndividual.addVM(0, 3);
		originIndividual.addVM(1, 4);
		originIndividual.addVM(1, 5);
		originIndividual.addVM(1, 6);
		originIndividual.addVM(2, 7);
		originIndividual.addVM(2, 8);
		originIndividual.addVM(3, 9);
		originIndividual.addVM(3, 10);
		originIndividual.addVM(3, 11);
		originIndividual.addVM(3, 12);
		originIndividual.addVM(4, 13);
		originIndividual.addVM(4, 14);
		originIndividual.addVM(4, 15);
		
		VmDistributionScheduler scheduler = new VmDistributionScheduler();
		scheduler.computeOptimalDistribution(pmNum, vmNum, vmsLoad, originIndividual);
	}

}