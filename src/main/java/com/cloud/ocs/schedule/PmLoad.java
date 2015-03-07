package com.cloud.ocs.schedule;

/**
 * 用于描述物理主机负载的工具类
 * 
 * @author Wang Chao
 * 
 * @date 2015-3-7 上午10:46:42
 * 
 */
public class PmLoad {

	private int pmNo;
	private double load;
	
	public PmLoad() {
		super();
	}

	public PmLoad(int pmNo, double load) {
		super();
		this.pmNo = pmNo;
		this.load = load;
	}

	public int getPmNo() {
		return pmNo;
	}

	public void setPmNo(int pmNo) {
		this.pmNo = pmNo;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

}
