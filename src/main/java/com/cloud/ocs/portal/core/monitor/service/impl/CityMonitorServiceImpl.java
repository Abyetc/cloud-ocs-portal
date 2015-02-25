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

import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dto.MessageAverageProcessTimeWrapper;
import com.cloud.ocs.monitor.service.MessageRecordService;
import com.cloud.ocs.monitor.service.SessionRecordService;
import com.cloud.ocs.monitor.service.ThroughputRecordService;
import com.cloud.ocs.portal.core.business.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
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
	private SessionRecordService sessionRecordService;
	
	@Resource
	private MessageRecordService messageRecordService;
	
	@Resource
	private ThroughputRecordService throughputRecordService;
	
	@Resource
	private OcsVmForwardingPortService vmForwardingPortService;
	
	@Resource
	private OcsVmMonitorService vmMonitorService;
	
	@Override
	public Long getRealtimeSessionNum(Integer cityId) {
		
		return sessionRecordService.getCityCurSessionNum(cityId);
	}
	
	@Override
	public MessageThroughputDto getMessageThroughput(Integer cityId) {
		return throughputRecordService.getMessageThroughputOfCity(cityId);
	}
	
	@Override
	public MessageProcessTimeDto getMessageProcessTime(final Integer cityId) {
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfCity(cityId, messageType);
				}
			});
		}
		executor.shutdown();
		int count = 0;
		while (count < threeMessageType.size()) {
			Future<MessageAverageProcessTimeWrapper> future = comp.poll();
			if (future == null) {
				continue;
			}
			else {
				try {
					MessageAverageProcessTimeWrapper messageProcessTimeWrapper = future.get();
					MessageType messageType = messageProcessTimeWrapper.getMessageType();
					if (messageType.equals(MessageType.ALL)) {
						result.setAllMessageProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.INITIAL)) {
						result.setMessageIProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.UPDATE)) {
						result.setMessageUProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.TERMINAL)) {
						result.setMessageTProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
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
