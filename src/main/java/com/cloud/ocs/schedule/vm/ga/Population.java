package com.cloud.ocs.schedule.vm.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.cloud.ocs.schedule.vm.VmLoad;


/**
 * 遗传算法中的群体类
 * 
 * @author Wang Chao
 *
 * @date 2015-3-5 下午7:45:24
 *
 */
public class Population {

	private List<Individual> individuals;

	public Population() {
		this.individuals = new ArrayList<Individual>();
	}
	
	public void addIndividual(Individual individual) {
		this.individuals.add(individual);
	}
	
	public List<Individual> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<Individual> individuals) {
		this.individuals = individuals;
	}
	
	/**
	 * 计算种群中每个个体的适应度
	 */
	public void computeFitnessInPopulation() {
		for (Individual individual : individuals) {
			individual.computeFitness();
		}
	}
	
	/**
	 * 找到当前种群中个体适应值得最大值
	 * @return
	 */
	public double getMaxFitnessInPopulation() {
		double max = -1.0;
		for (Individual individual : individuals) {
			if (individual.getFitness() > max) {
				max = individual.getFitness();
			}
		}
		
		return max;
	}
	
	/**
	 * 获取种群中已经达到负载均衡约束条件的个体个数
	 * @return
	 */
	public int getLoadBalancingIndividualNum() {
		int count = 0; 
		for (Individual individual : individuals) {
			if (individual.getLoadStandardDeviation() <= GAExecutor.LOAD_BALANCING_THRESHOLD) {
				count++;
			}
		}
		
		return count;
	}
	
	public Individual getMaxFitnessIndividualInPopulation() {
		Individual maxFitnessIndividual = null;
		double maxFitness = -1.0;
		for (Individual individual : individuals) {
			if (individual.getFitness() > maxFitness) {
				maxFitness = individual.getFitness();
				maxFitnessIndividual = individual;
			}
		}
		
		return maxFitnessIndividual;
	}
	
	/**
	 * 在种群中进行选择操作，返回下一代种群(采用轮盘赌算法)
	 */
	public List<Individual> selectInPopulation() {
		List<Double> accuprobabilityList = this.calculateAccuProbabilityList();
		
		List<Individual> selectionResult = new ArrayList<Individual>();
		int curSelectionNum = 0;
		int totalSelectionNum = (int)(GAExecutor.POPULATION_NUM * GAExecutor.REPLICATION_PROBABILITY);
		while (curSelectionNum < totalSelectionNum) {
			Individual seletedIndividual = this.roulette(accuprobabilityList);
			if (seletedIndividual == null) {
				continue;
			}
			selectionResult.add(seletedIndividual);
			curSelectionNum++;
		}
		
		return selectionResult;
	}
	
	/**
	 * 在种群中进行交叉繁殖
	 */ 
	public List<Individual> crossInPopulation() {
		List<Double> accuprobabilityList = this.calculateAccuProbabilityList();
		
		List<Individual> crossoverResult = new ArrayList<Individual>();
		int curCrossoverNum = 0;
		int totalCrossoverNum = (int)(GAExecutor.POPULATION_NUM * GAExecutor.HYBRIDIZATION_PROBABILITY);
		while (curCrossoverNum < totalCrossoverNum) {
			Individual selected1 = this.roulette(accuprobabilityList);
			Individual selected2 = null;
			do {
				selected2 = this.roulette(accuprobabilityList);
			} while(selected1.isSame(selected2));
			
			int pmNum = selected1.getPmNum();
			int vmNum = selected2.getVmNum();
			double[] vmsLoad = selected1.getVmsLoad();
			Individual crossedIndividual = new Individual(pmNum, vmNum, vmsLoad);
			
			List<int[]> selected1Genes = selected1.getChromosome().getGenes();
			List<int[]> selected2Genes = selected2.getChromosome().getGenes();
			List<Integer> overlapVmsNoList = new ArrayList<Integer>();
			List<Integer> overlapPmsNoList = new ArrayList<Integer>();
 			Set<Integer> singleVmsNoSet = new HashSet<Integer>();
			for (int i = 0; i < pmNum; i++) {
				for (int j = 0; j < vmNum; j++) {
					int check1 = selected1Genes.get(i)[j];
					int check2 = selected2Genes.get(i)[j];
					if (check1 + check2 == 2) {
						overlapPmsNoList.add(i);
						overlapVmsNoList.add(j);
					}
					if (check1 + check2 == 1) {
						singleVmsNoSet.add(j);
					}
				}
			}
			
			for (int i = 0; i < overlapPmsNoList.size(); i++) {
				crossedIndividual.addVM(overlapPmsNoList.get(i), overlapVmsNoList.get(i));
			}
			crossedIndividual.computePMsLoad();
			List<Integer> singleVmNoTobeAdded = this.sortByVmsLoad(singleVmsNoSet, vmsLoad);
			for (int i = 0; i < singleVmNoTobeAdded.size(); i++) {
				int minLoadPMNo = crossedIndividual.getMinLoadPMNo();
				crossedIndividual.addVM(minLoadPMNo, singleVmNoTobeAdded.get(i));
				crossedIndividual.computePMsLoad();
			}
			if (this.checkWheatherSelected(crossedIndividual, crossoverResult)) {
				continue;
			}
			else {
				crossoverResult.add(crossedIndividual);
			}
//			System.out.println(crossedIndividual.computePMsLoadExpectation());
//			System.out.println(crossedIndividual.computePMsLoadVariation());
//			crossedIndividual.computeFitness();
//			System.out.println(crossedIndividual.getFitness());
//			System.out.println("================");
			curCrossoverNum++;
		}
		
		return crossoverResult;
	}
	
	/**
	 * 在种群中进行个体基因变异
	 * @param curGenNum
	 */
	public void mutateInPopulation(int curGenNum, int vmNum, int pmNum) {
		double mutationProbability = Math.exp((-1.5*0.5*curGenNum*Math.sqrt(vmNum)) / GAExecutor.POPULATION_NUM);
		int totalMutationNum = (int)(mutationProbability * GAExecutor.POPULATION_NUM);
		int curMutationNum = 0;
		
		Random random = new Random();
		while (curMutationNum < totalMutationNum) {
			int randomIndividualNo = random.nextInt(this.individuals.size());
			Individual individualTobeMutated = this.individuals.get(randomIndividualNo);
			int randomPMNo1 = random.nextInt(pmNum);
			int randomPMNo2 = random.nextInt(pmNum);
			List<Integer> vmNosInPM1 = new ArrayList<Integer>();
			List<Integer> vmNosInPM2 = new ArrayList<Integer>();
			for (int i = 0; i < vmNum; i++) {
				if (individualTobeMutated.getChromosome().getGenes().get(randomPMNo1)[i] == 1) {
					vmNosInPM1.add(i);
				}
				if (individualTobeMutated.getChromosome().getGenes().get(randomPMNo2)[i] == 1) {
					vmNosInPM2.add(i);
				}
			}
			if (vmNosInPM1.size() == 0 || vmNosInPM2.size() == 0) {
				continue;
			}
			int randomVMNoIndex1 = random.nextInt(vmNosInPM1.size());
			int randomVmNoIndex2 = random.nextInt(vmNosInPM2.size());
			int randomVMNo1 = vmNosInPM1.get(randomVMNoIndex1);
			int randomVMNo2 = vmNosInPM2.get(randomVmNoIndex2);
			individualTobeMutated.removeVM(randomPMNo1, randomVMNo1);
			individualTobeMutated.removeVM(randomPMNo2, randomVMNo2);
			individualTobeMutated.addVM(randomPMNo1, randomVMNo2);
			individualTobeMutated.addVM(randomPMNo2, randomVMNo1);
			curMutationNum++;
		}
	}
	
	/**
	 * 计算用于轮盘赌算法的累积概率
	 * @return
	 */
	public List<Double> calculateAccuProbabilityList() {
		List<Double> fitnessList = new ArrayList<Double>();
		for (Individual one : individuals) {
			fitnessList.add(one.getFitness());
		}
		double sumOfFitness = 0.0;
		for (int i = 0; i < fitnessList.size(); i++) {
			sumOfFitness += fitnessList.get(i);
		}
		
		List<Double> probabilityList = new ArrayList<Double>();
		for (int i = 0; i < fitnessList.size(); i++) {
			probabilityList.add(fitnessList.get(i)/sumOfFitness);
		}
		
		List<Double> accuprobabilityList = new ArrayList<Double>();
		double accuProbability = 0.0;
		for (int i = 0; i < probabilityList.size(); i++) {
			accuProbability += probabilityList.get(i);
			accuprobabilityList.add(accuProbability);
		}
		
		return accuprobabilityList;
	}
	
	/**
	 * 采用轮盘赌算法从种群中选择一个个体
	 * @param accuprobabilityList
	 * @return
	 */
	public Individual roulette(List<Double> accuprobabilityList) {
		Random random = new Random();
		double randomNum = random.nextDouble();
		if (randomNum == 0.0d) {
			return null;
		}
		int selectedIndividualNum = -1;
		if (randomNum <= accuprobabilityList.get(0)) {
			selectedIndividualNum = 0;
		}
		if (randomNum > accuprobabilityList.get(accuprobabilityList.size() - 2)) {
			selectedIndividualNum = accuprobabilityList.size() - 1;
		}
		for (int i = 1; i < accuprobabilityList.size() - 1; i++) {
			if ( randomNum > accuprobabilityList.get(i-1) && randomNum <= accuprobabilityList.get(i)) {
				selectedIndividualNum = i;
			}
		}
		return this.individuals.get(selectedIndividualNum);
	}
	
	/**
	 * 返回符合负载均衡约束条件中迁移开销最小的个体
	 * @param originIndividual
	 * @return
	 */
	public Individual getMinMigrateCostIndividual(Individual originIndividual) {
		Individual result = null;
		int minCost = Integer.MAX_VALUE;
		for (Individual individual : this.individuals) {
			if (individual.computePMsLoadStandardDeviation() <= GAExecutor.LOAD_BALANCING_THRESHOLD) {
				int cost = this.computeMigrateCost(originIndividual, individual);
				if (cost < minCost) {
					result = individual;
					minCost = cost;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 计算迁移开销
	 * @param origin
	 * @param dest
	 * @return
	 */
	private int computeMigrateCost(Individual origin, Individual dest) {
		int cost = 0;
		int vmNum = origin.getVmNum();
		int pmNum = origin.getPmNum();
		List<int []> originGenes = origin.getChromosome().getGenes();
		List<int []> destGenes = dest.getChromosome().getGenes();
		
		for (int i = 0; i < pmNum; i++) {
			for (int j = 0; j < vmNum; j++) {
				if (originGenes.get(i)[j] == 1 && destGenes.get(i)[j] == 0) {
					cost++;
				}
			}
		}
		
		return cost;
	}
	
	/**
	 * 服务于crossInPopulation()方法
	 * 用于将传入的虚拟机编号按照虚拟机负载重新排序
	 * @param singleVmsNoSet
	 * @param vmsLoad
	 * @return
	 */
	private List<Integer> sortByVmsLoad(Set<Integer> singleVmsNoSet, double[] vmsLoad) {
		List<Integer> result = new ArrayList<Integer>();
		
		List<VmLoad> vmLoadList = new ArrayList<VmLoad>();
		for (Integer vmNo : singleVmsNoSet) {
			vmLoadList.add(new VmLoad(vmNo, vmsLoad[vmNo]));
		}
		
		Collections.sort(vmLoadList);
		for (int i = vmLoadList.size() - 1; i >= 0; i--) {
			result.add(vmLoadList.get(i).getVmNo());
		}
		
		return result;
	}
	
	/**
	 * 服务于crossInPopulation()方法
	 * 用于检测新杂交产生的后代之前是否产生过
	 * @param individual
	 * @param individualList
	 * @return
	 */
	private boolean checkWheatherSelected(Individual individual, List<Individual> individualList) {
		for (Individual one : individualList) {
			if (one.isSame(individual)) {
				return true;
			}
		}
		return false;
	}
	
}
