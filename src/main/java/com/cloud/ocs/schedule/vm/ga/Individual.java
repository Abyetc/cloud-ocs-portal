package com.cloud.ocs.schedule.vm.ga;

import java.util.List;


/**
 * 遗传算法中的个体(也即虚拟机的一种分布情况)
 * 
 * @author Wang Chao
 *
 * @date 2015-3-5 下午5:22:50
 *
 */
public class Individual {

	private int pmNum;
	private int vmNum;
	private double[] pmsLoad;
	private double[] vmsLoad;
	
	private Chromosome chromosome; //个体的染色体
	private double fitness; //个体适应度
	
	public Individual(int pmNum, int vmNum, double[] vmsLoad) {
		super();
		this.pmNum = pmNum;
		this.vmNum = vmNum;
		this.vmsLoad = vmsLoad;
		this.pmsLoad = new double[pmNum];
		this.chromosome = new Chromosome(pmNum, vmNum);
	}
	
	public int getPmNum() {
		return pmNum;
	}

	public void setPmNum(int pmNum) {
		this.pmNum = pmNum;
	}

	public int getVmNum() {
		return vmNum;
	}

	public void setVmNum(int vmNum) {
		this.vmNum = vmNum;
	}

	public double[] getPmsLoad() {
		return pmsLoad;
	}

	public void setPmsLoad(double[] pmsLoad) {
		this.pmsLoad = pmsLoad;
	}

	public double[] getVmsLoad() {
		return vmsLoad;
	}

	public void setVmsLoad(double[] vmsLoad) {
		this.vmsLoad = vmsLoad;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}

	public void setChromosome(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	/**
	 * 向个体中添加虚拟机
	 * @param pmNo
	 * @param vmNo
	 */
	public void addVM(int pmNo, int vmNo) {
		this.chromosome.addVMToPM(pmNo, vmNo);
	}
	
	/**
	 * 从个体中删除虚拟机
	 * @param pmNo
	 * @param vmNo
	 */
	public void removeVM(int pmNo, int vmNo) {
		this.chromosome.removeVMFromPM(pmNo, vmNo);
	}
	
	public void printChromosome() {
		this.chromosome.printGenes();
	}
	
	/**
	 * 根据染色体中的基因序列计算当前个体中每台物理机的负载
	 */
	public void computePMsLoad() {
		List<int[]> genes = this.chromosome.getGenes();
		for (int i = 0; i < pmNum; i++) {
			int[] geneFragment = genes.get(i);
			double load = 0.0;
			for (int j = 0; j < vmNum; j++) {
				if (geneFragment[j] == 1) {
					load += vmsLoad[j];
				}
			}
			pmsLoad[i] = load;
		}
	}
	
	/**
	 * 返回当前个体中负载最小的物理机编号
	 * @return
	 */
	public int getMinLoadPMNo() {
		double maxLoad = 101.0; //单台物理机的负载不会超过100.0
		int minLoadPMNo = -1;
		
		for (int i = 0; i < pmsLoad.length; i++) {
			if (pmsLoad[i] < maxLoad) {
				maxLoad = pmsLoad[i];
				minLoadPMNo = i;
			}
		}
		
		return minLoadPMNo;
	}
	
	/**
	 * 计算当前个体的适应值
	 */
	public void computeFitness() {
		//需要调用computeHostLoadVariation();
		this.fitness = 1.0 / this.computePMsLoadVariation();
	}
	
	/**
	 * 计算当前个体(分布情况)的物理主机负载数据的方差
	 * @return
	 */
	public double computePMsLoadVariation() {
		double pmsLoadExpectation = computePMsLoadExpectation();
		double sum = 0.0;
		for (int i= 0; i < pmNum; i++) {
			sum += Math.abs((pmsLoad[i] - pmsLoadExpectation) * (pmsLoad[i] - pmsLoadExpectation));
		}
		return Math.sqrt(sum/(double)pmNum);
	}
	
	/**
	 * 计算当前个体(分布情况)的物理主机负载数据的期望
	 * @return
	 */
	public double computePMsLoadExpectation() {
		double sum = 0.0;
		for (int i = 0; i < pmNum; i++) {
			sum += pmsLoad[i];
		}
		return sum / (double)pmNum;
	}
	
	/**
	 * 判断两个个体是否为同一个(染色体的基因序是否相同)
	 * @param individual
	 * @return
	 */
	public boolean isSame(Individual individual) {
		for (int i = 0; i < pmNum; i++) {
			for (int j = 0; j < vmNum; j++) {
				if (this.chromosome.getGenes().get(i)[j] != individual.chromosome.getGenes().get(i)[j]) {
					return false;
				}
			}
		}
		return true;
	}

}
