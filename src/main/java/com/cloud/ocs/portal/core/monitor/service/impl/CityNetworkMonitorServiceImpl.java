package com.cloud.ocs.portal.core.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.cloud.ocs.monitor.dao.SessionRecordDao;
import com.cloud.ocs.monitor.dao.ThroughputRecordDao;
import com.cloud.ocs.monitor.dto.MessageAverageProcessTimeWrapper;
import com.cloud.ocs.monitor.service.MessageRecordService;
import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
import com.cloud.ocs.portal.core.monitor.service.CityNetworkMonitorService;
import com.cloud.ocs.portal.utils.DateUtil;

@Service
public class CityNetworkMonitorServiceImpl implements CityNetworkMonitorService {
	
	@Resource
	private MessageRecordService messageRecordService;
	
	@Resource
	private SessionRecordDao sessionRecordDao;
	
	@Resource
	private ThroughputRecordDao throughputRecordDao;
	
	@Resource
	private CityNetworkService cityNetworkService;
	
//	@Resource
//	private OcsVmForwardingPortService vmForwardingPortService;
//	
//	@Resource
//	private OcsVmMonitorService vmMonitorService;
	
	@Override
	public Long getNetworkRealtimeSessionNum(String networkId) {
		CityNetwork cityNetwork = cityNetworkService.getCityNetworkByNetworkId(networkId);
		
		if (cityNetwork == null) {
			return null;
		}
		
		return sessionRecordDao.getNetworkCurSessionNum(cityNetwork.getPublicIp());
	}
	
	@Override
	public MessageThroughputDto getNetworkRealtimeMessageThroughput(String networkId) {
		CityNetwork cityNetwork = cityNetworkService.getCityNetworkByNetworkId(networkId);
		
		if (cityNetwork == null) {
			return null;
		}
		
		return throughputRecordDao.getNetworkCurMessageThroughput(cityNetwork.getPublicIp());
	}

	@Override
	public MessageProcessTimeDto getNetworkRealtimeMessageAverageProcessTime(String networkId) {
		CityNetwork cityNetwork = cityNetworkService.getCityNetworkByNetworkId(networkId);
		
		if (cityNetwork == null) {
			return null;
		}
		final String networkIp = cityNetwork.getPublicIp();
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfNetwork(networkIp, messageType);
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
	public Map<String, List<List<Object>>> getNetworkHistoryMessageAverageProcessTime(
			String networkId, Date date) {
		Map<String, List<List<Object>>> result = new HashMap<String, List<List<Object>>>();
		
		List<List<Object>> allMesg = new ArrayList<List<Object>>();
		List<List<Object>> iMesg = new ArrayList<List<Object>>();
		List<List<Object>> uMesg = new ArrayList<List<Object>>();
		List<List<Object>> tMesg = new ArrayList<List<Object>>();
		for (int i = 0; i < 24*60; i+=120) {
			Date dt = DateUtil.transferDateInMinuteField(date, i+120);
			MessageProcessTimeDto oneMesgProcessTime = this.getNetworkMessageAverageProcessTimeAtSpecificDate(networkId, dt);
			List<Object> oneAllMesg = new ArrayList<Object>();
			List<Object> oneIMesg = new ArrayList<Object>();
			List<Object> oneUMesg = new ArrayList<Object>();
			List<Object> oneTMesg = new ArrayList<Object>();
			oneAllMesg.add(dt.getTime());
			oneAllMesg.add(oneMesgProcessTime.getAllMessageProcessTime());
			oneIMesg.add(dt.getTime());
			oneIMesg.add(oneMesgProcessTime.getMessageIProcessTime());
			oneUMesg.add(dt.getTime());
			oneUMesg.add(oneMesgProcessTime.getMessageUProcessTime());
			oneTMesg.add(dt.getTime());
			oneTMesg.add(oneMesgProcessTime.getMessageTProcessTime());
			allMesg.add(oneAllMesg);
			iMesg.add(oneIMesg);
			uMesg.add(oneUMesg);
			tMesg.add(oneTMesg);
		}
		
		result.put("allMsg", allMesg);
		result.put("iMsg", iMesg);
		result.put("uMsg", uMesg);
		result.put("tMsg", tMesg);
		
		return result;
	}

	@Override
	public Map<String, List<List<Object>>> getNetworkHistoryMessageThroughput(
			String networkId, Date date) {
		Date from = date;
		Date to = DateUtil.transferDateInDayField(from, 1);
		String networkIp = cityNetworkService.getCityNetworkByNetworkId(networkId).getPublicIp();
		Map<Date, MessageThroughputDto> messageThroughputList = throughputRecordDao.getNetworkHistoryMessageThroughput(networkIp, from, to);
		
		Map<String, List<List<Object>>> result = new HashMap<String, List<List<Object>>>();
		if (messageThroughputList != null) {
			List<List<Object>> received = new ArrayList<List<Object>>();
			List<List<Object>> finished = new ArrayList<List<Object>>();
			for (Entry<Date, MessageThroughputDto> entry : messageThroughputList.entrySet()) {
				List<Object> oneReceived = new ArrayList<Object>();
				List<Object> oneFinished = new ArrayList<Object>();
				oneReceived.add(entry.getKey().getTime());
				oneReceived.add(entry.getValue().getReceivedMessageNum());
				oneFinished.add(entry.getKey().getTime());
				oneFinished.add(entry.getValue().getFinishedMessageNum());
				received.add(oneReceived);
				finished.add(oneFinished);
			}
			
			result.put("received", received);
			result.put("finished", finished);
		}
		
		return result;
	}

	@Override
	public List<List<Object>> getNetworkHistorySessionNum(String networkId,
			Date date) {
		String networkIp = cityNetworkService.getCityNetworkByNetworkId(networkId).getPublicIp();
		List<List<Object>> ret = new ArrayList<List<Object>>();
		for (int i = 0; i < 24*60; i+=30) {
			Date from = DateUtil.transferDateInMinuteField(date, i);
			Date to = DateUtil.transferDateInMinuteField(from, 30);
			Long sessionNum = sessionRecordDao.getNetworkHistorySessionNum(networkIp, from, to);
			List<Object> onePoint = new ArrayList<Object>();
			onePoint.add(to.getTime());
					
			onePoint.add(sessionNum);
			ret.add(onePoint);
		}
		
		return ret;
	}

//	@Override
//	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(String networkId, final String interfaceName) {
//		RxbpsTxbpsDto result = new RxbpsTxbpsDto();
//		
//		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByNetworkId(networkId);
//		
//		if (vmForwardingPorts == null) {
//			return result;
//		}
//		
//		ExecutorService executor = Executors.newCachedThreadPool();
//		CompletionService<RxbpsTxbpsDto> comp = new ExecutorCompletionService<RxbpsTxbpsDto>(executor);
//		for (final OcsVmForwardingPort vmForwardingPort : vmForwardingPorts) {
//			comp.submit(new Callable<RxbpsTxbpsDto>() {
//				public RxbpsTxbpsDto call() throws Exception {
//					return vmMonitorService.getVmCurRxbpsTxbps(vmForwardingPort.getVmId(), interfaceName);
//				}
//			});
//		}
//		executor.shutdown();
//		int count = 0;
//		while (count < vmForwardingPorts.size()) {
//			Future<RxbpsTxbpsDto> future = comp.poll();
//			if (future == null) {
//				continue;
//			}
//			else {
//				try {
//					RxbpsTxbpsDto rxbpsTxbpsDto = future.get();
//					result.setRxbps(result.getRxbps() + rxbpsTxbpsDto.getRxbps());
//					result.setTxbps(result.getTxbps() + rxbpsTxbpsDto.getTxbps());
//					count++;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		return result;
//	}
//
//	@Override
//	public Long getCityNetworkConcurrencyRequestNum(String networkId) {
//		Long result = 0L;
//		
//		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByNetworkId(networkId);
//		
//		if (vmForwardingPorts == null) {
//			return result;
//		}
//		
//		ExecutorService executor = Executors.newCachedThreadPool();
//		CompletionService<Long> comp = new ExecutorCompletionService<Long>(executor);
//		for (final OcsVmForwardingPort vmForwardingPort : vmForwardingPorts) {
//			comp.submit(new Callable<Long>() {
//				public Long call() throws Exception {
//					return vmMonitorService.getVmCurConcurrencyRequestNum(vmForwardingPort.getVmId());
//				}
//			});
//		}
//		executor.shutdown();
//		int count = 0;
//		while (count < vmForwardingPorts.size()) {
//			Future<Long> future = comp.poll();
//			if (future == null) {
//				continue;
//			}
//			else {
//				try {
//					Long requestNum = future.get();
//					result += requestNum;
//					count++;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		return result;
//	}
	
	private MessageProcessTimeDto getNetworkMessageAverageProcessTimeAtSpecificDate(final String networkId, final Date date) {
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		final String networkIp = cityNetworkService.getCityNetworkByNetworkId(networkId).getPublicIp();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfNetworkAtSpecificDate(networkIp, messageType, date);
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

}
