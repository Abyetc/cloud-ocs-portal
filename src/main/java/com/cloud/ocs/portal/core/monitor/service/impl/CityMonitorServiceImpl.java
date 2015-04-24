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
import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
import com.cloud.ocs.portal.core.monitor.service.CityMonitorService;
import com.cloud.ocs.portal.utils.DateUtil;

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
	private MessageRecordService messageRecordService;
	
	@Resource
	private SessionRecordDao sessionRecordDao;
	
	@Resource
	private ThroughputRecordDao throughputRecordDao;
	
//	@Resource
//	private OcsVmForwardingPortService vmForwardingPortService;
//	
//	@Resource
//	private OcsVmMonitorService vmMonitorService;
	
	@Override
	public Long getCityRealtimeSessionNum(Integer cityId) {
		return sessionRecordDao.getCityCurSessionNum(cityId);
	}
	
	@Override
	public MessageThroughputDto getCityRealtimeMessageThroughput(Integer cityId) {
		return throughputRecordDao.getCityCurMessageThroughput(cityId);
	}
	
	@Override
	public MessageProcessTimeDto getCityRealtimeMessageAverageProcessTime(final Integer cityId) {
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
	public List<List<Object>> getCityHistoryeSessionNum(final Integer cityId,
			final Date date) {
		List<List<Object>> ret = new ArrayList<List<Object>>();
		for (int i = 0; i < 24*60; i+=30) {
			Date from = DateUtil.transferDateInMinuteField(date, i);
			Date to = DateUtil.transferDateInMinuteField(from, 30);
			Long sessionNum = sessionRecordDao.getCityHistorySessionNum(cityId, from, to);
			List<Object> onePoint = new ArrayList<Object>();
			onePoint.add(to.getTime());
			
			//==============
//			sessionNum = (long)RandomNumUtil.randInt(10, 50);
//			
//			if (i >= 400 && i <= 1260) {
//				sessionNum += (long)RandomNumUtil.randInt(100, 300);
//			}
			//==============
					
			onePoint.add(sessionNum);
			ret.add(onePoint);
		}
		
//		ExecutorService executor = Executors.newCachedThreadPool();
//		CompletionService<List<List<Object>>> comp = new ExecutorCompletionService<List<List<Object>>>(executor);
//		List<Integer> startList = new ArrayList<Integer>();
//		for (int i = 0; i < 24*60; i += 120) {
//			startList.add(i);
//		}
//		for (final Integer start : startList) {
//			comp.submit(new Callable<List<List<Object>>>() {
//				public List<List<Object>> call() throws Exception {
//					return getCityHistoryeSessionNumInTwoHours(cityId, date, start);
//				}
//			});
//		}
//		executor.shutdown();
//		int count = 0;
//		while (count < 12) {
//			Future<List<List<Object>>> future = comp.poll();
//			if (future == null) {
//				continue;
//			}
//			else {
//				try {
//					List<List<Object>> messageProcessTimeWrapper = future.get();
//					ret.addAll(messageProcessTimeWrapper);
//					count++;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		return ret;
	}
	
	public List<List<Object>> getCityHistoryeSessionNumInTwoHours(Integer cityId,
			Date date, int start) {
		List<List<Object>> ret = new ArrayList<List<Object>>();
		for (int i = start; i < start + 2*60; i+=5) {
			Date from = DateUtil.transferDateInMinuteField(date, i);
			Date to = DateUtil.transferDateInMinuteField(from, 5);
			Long sessionNum = sessionRecordDao.getCityHistorySessionNum(cityId, from, to);
			List<Object> onePoint = new ArrayList<Object>();
			onePoint.add(to.getTime());
					
			onePoint.add(sessionNum);
			ret.add(onePoint);
		}
		
		return ret;
	}
	
	@Override
	public Map<String, List<List<Object>>> getCityHistoryMessageThroughput(Integer cityId,
			Date date) {
		Date from = date;
		Date to = DateUtil.transferDateInDayField(from, 1);
		Map<Date, MessageThroughputDto> messageThroughputList = throughputRecordDao.getCityHistoryMessageThroughput(cityId, from, to);
		
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
			
//			result.put("received", getCityHistoryeSessionNum(cityId, date));
//			result.put("finished", getCityHistoryeSessionNum(cityId, date));
			result.put("received", received);
			result.put("finished", finished);
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<List<Object>>> getCityHistoryMessageAverageProcessTime(
			Integer cityId, Date date) {
		Map<String, List<List<Object>>> result = new HashMap<String, List<List<Object>>>();
		
		List<List<Object>> allMesg = new ArrayList<List<Object>>();
		List<List<Object>> iMesg = new ArrayList<List<Object>>();
		List<List<Object>> uMesg = new ArrayList<List<Object>>();
		List<List<Object>> tMesg = new ArrayList<List<Object>>();
		for (int i = 0; i < 24*60; i+=120) {
			Date dt = DateUtil.transferDateInMinuteField(date, i+120);
			MessageProcessTimeDto oneMesgProcessTime = this.getCityMessageAverageProcessTimeAtSpecificDate(cityId, dt);
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
		
//		result.put("allMsg", getCityHistoryeSessionNum(cityId, date));
//		result.put("iMsg", getCityHistoryeSessionNum(cityId, date));
//		result.put("uMsg", getCityHistoryeSessionNum(cityId, date));
//		result.put("tMsg", getCityHistoryeSessionNum(cityId, date));
		
		result.put("allMsg", allMesg);
		result.put("iMsg", iMesg);
		result.put("uMsg", uMesg);
		result.put("tMsg", tMesg);
		
		return result;
	}
	
	private MessageProcessTimeDto getCityMessageAverageProcessTimeAtSpecificDate(final Integer cityId, final Date date) {
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfCityAtSpecificDate(cityId, messageType, date);
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
 

//	@Override
//	public RxbpsTxbpsDto getCityRxbpsTxbps(Integer cityId, final String interfaceName) {
//		RxbpsTxbpsDto result = new RxbpsTxbpsDto();
//		
//		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByCityId(cityId);
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
//	public Long getCityConcurrencyRequestNum(Integer cityId) {
//		Long result = 0L;
//		
//		final List<OcsVmForwardingPort> vmForwardingPorts = vmForwardingPortService.getVmForwardingPortListByCityId(cityId);
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

}
