package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用于返回系统容量数据的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-20 下午8:12:19
 * 
 */
public class CapacityDto {
	
	/** CloudStack用于标识不同容量类型的code */
	private int capacityCSCode;

	private String capacityName;
	private String capacityTotal;
	private String capacityUsed;
	private String unit;
	private double percentUsed;

	public int getCapacityCSCode() {
		return capacityCSCode;
	}

	public void setCapacityCSCode(int capacityCSCode) {
		this.capacityCSCode = capacityCSCode;
	}

	public String getCapacityName() {
		return capacityName;
	}

	public void setCapacityName(String capacityName) {
		this.capacityName = capacityName;
	}

	public String getCapacityTotal() {
		return capacityTotal;
	}

	public void setCapacityTotal(String capacityTotal) {
		this.capacityTotal = capacityTotal;
	}

	public String getCapacityUsed() {
		return capacityUsed;
	}

	public void setCapacityUsed(String capacityUsed) {
		this.capacityUsed = capacityUsed;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPercentUsed() {
		return percentUsed;
	}

	public void setPercentUsed(double percentUsed) {
		this.percentUsed = percentUsed;
	}

}
