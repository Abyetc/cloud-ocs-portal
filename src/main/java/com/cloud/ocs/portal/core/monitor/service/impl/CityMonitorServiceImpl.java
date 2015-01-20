package com.cloud.ocs.portal.core.monitor.service.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.core.business.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.service.CityMonitorService;
import com.cloud.ocs.portal.core.monitor.service.OcsVmMonitorService;

/**
 * 监控城市Service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-15 上午10:43:37
 *
 */
@Service
public class CityMonitorServiceImpl implements CityMonitorService {
	
	@Resource
	private OcsVmForwardingPortService vmForwardingPortService;
	
	@Resource
	private OcsVmMonitorService vmMonitorService;

	@Override
	public RxbpsTxbpsDto getCityRxbpsTxbps(Integer cityId, final String interfaceName) {
		RxbpsTxbpsDto result = new RxbpsTxbpsDto();
		
		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByCityId(cityId);
		
		if (vmForwardingPorts == null) {
			return result;
		}
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<RxbpsTxbpsDto> comp = new ExecutorCompletionService<RxbpsTxbpsDto>(executor);
		for (final OcsVmForwardingPort vmForwardingPort : vmForwardingPorts) {
			comp.submit(new Callable<RxbpsTxbpsDto>() {
				public RxbpsTxbpsDto call() throws Exception {
					return vmMonitorService.getVmRxbpsTxbps(vmForwardingPort.getVmId(), interfaceName);
				}
			});
		}
		executor.shutdown();
		int count = 0;
		while (count < vmForwardingPorts.size()) {
			Future<RxbpsTxbpsDto> future = comp.poll();
			if (future == null) {
				continue;
			}
			else {
				try {
					RxbpsTxbpsDto rxbpsTxbpsDto = future.get();
					result.setRxbps(result.getRxbps() + rxbpsTxbpsDto.getRxbps());
					result.setTxbps(result.getTxbps() + rxbpsTxbpsDto.getTxbps());
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public Long getCityConcurrencyRequestNum(Integer cityId) {
		Long result = 0L;
		
		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByCityId(cityId);
		
		if (vmForwardingPorts == null) {
			return result;
		}
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<Long> comp = new ExecutorCompletionService<Long>(executor);
		for (final OcsVmForwardingPort vmForwardingPort : vmForwardingPorts) {
			comp.submit(new Callable<Long>() {
				public Long call() throws Exception {
					return vmMonitorService.getVmConcurrencyRequestNum(vmForwardingPort.getVmId());
				}
			});
		}
		executor.shutdown();
		int count = 0;
		while (count < vmForwardingPorts.size()) {
			Future<Long> future = comp.poll();
			if (future == null) {
				continue;
			}
			else {
				try {
					Long requestNum = future.get();
					result += requestNum;
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

}
