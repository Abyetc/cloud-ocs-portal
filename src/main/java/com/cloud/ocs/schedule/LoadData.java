package com.cloud.ocs.schedule;

/**
 * 用于描述物理主机-虚拟机的负载的工具类，并将其ID与运行遗传算法时的逻辑编号进行对应
 * 
 * @author Wang Chao
 *
 * @date 2015-3-7 上午10:48:36
 *
 */
public class LoadData {

	private String hostId;
	private String vmId;
	private PmLoad pmLoad;
	private VmLoad vmLoad;

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public PmLoad getPmLoad() {
		return pmLoad;
	}

	public void setPmLoad(PmLoad pmLoad) {
		this.pmLoad = pmLoad;
	}

	public VmLoad getVmLoad() {
		return vmLoad;
	}

	public void setVmLoad(VmLoad vmLoad) {
		this.vmLoad = vmLoad;
	}

}
