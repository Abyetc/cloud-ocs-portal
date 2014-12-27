package com.cloud.ocs.portal.core.monitor.service;

import java.util.List;

import com.cloud.ocs.portal.core.monitor.dto.VmDetail;

/**
 * 监控VM service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-26 下午10:06:16
 *
 */
public interface VmMonitorService {
	public int getVmNumOnHost(String hostId);
	public List<VmDetail> getVmDetailList(String hostId);
	public double getCurVmCpuUsagePercentage(String vmId);
}
