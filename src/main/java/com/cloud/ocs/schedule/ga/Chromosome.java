package com.cloud.ocs.schedule.ga;

import java.util.ArrayList;
import java.util.List;

/**
 * 遗传算法中个体的染色体类
 * 
 * @author Wang Chao
 *
 * @date 2015-3-5 下午7:43:29
 *
 */
public class Chromosome {

	private List<int[]> genes; //染色体的基因

	public Chromosome(int pmNum, int vmNum) {
		this.genes = new ArrayList<int[]>(pmNum);
		for (int i = 0; i < pmNum; i ++) {
			this.genes.add(new int[vmNum]);
		}
	}
	
	public void addVMToPM(int pmNo, int vmNo) {
		this.genes.get(pmNo)[vmNo] = 1;
	}
	
	public void removeVMFromPM(int pmNo, int vmNo) {
		this.genes.get(pmNo)[vmNo] = 0;
	}
	
	public void printGenes() {
		for (int i = 0; i < genes.size(); i++) {
			for (int j = 0; j < genes.get(i).length; j++) {
				System.out.print(genes.get(i)[j] + " ");
			}
			System.out.println();
		}
	}

	public List<int[]> getGenes() {
		return genes;
	}

	public void setGenes(List<int[]> genes) {
		this.genes = genes;
	}

	
	
}
