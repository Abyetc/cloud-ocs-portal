package com.cloud.ocs.schedule;

/**
 * 用于描述虚拟机负载的工具类
 * 
 * @author Wang Chao
 *
 * @date 2015-3-6 下午3:42:59
 *
 */
public class VmLoad  implements Comparable {
	private int vmNo;
	private double load;
	
	public VmLoad() {
		super();
	}

	public VmLoad(int vmNo, double load) {
		super();
		this.vmNo = vmNo;
		this.load = load;
	}
	
	public int getVmNo() {
		return vmNo;
	}

	public void setVmNo(int vmNo) {
		this.vmNo = vmNo;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	@Override
	public int compareTo(Object o) {
		VmLoad vmLoad = (VmLoad)o;
		if (this.getLoad() > vmLoad.getLoad()) {
			return 1;
		}
		if (this.getLoad() < vmLoad.getLoad()) {
			return -1;
		}
		return 0;
	}
	
}
