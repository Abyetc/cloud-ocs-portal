package com.cloud.ocs.portal.core.sync.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.dao.CityDao;
import com.cloud.ocs.portal.common.dao.CityNetworkDao;
import com.cloud.ocs.portal.core.business.constant.CityState;
import com.cloud.ocs.portal.core.business.constant.NetworkState;
import com.cloud.ocs.portal.core.sync.service.SyncCityStateService;

/**
 * 用于同步城市状态的service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-7 下午4:01:31
 *
 */
@Transactional(value="portal_em")
@Service
public class SyncCityStateServiceImpl implements SyncCityStateService {
	
	@Resource
	private CityDao cityDao;
	
	@Resource
	private CityNetworkDao cityNetworkDao;

	@Override
	public void syncCityState() {
		List<City> allCityList = cityDao.findAll();
		
		if (allCityList == null) {
			return;
		}
		
		for (City city : allCityList) {
			this.checkCityState(city);
			cityDao.merge(city);
		}
	}
	
	private void checkCityState(City city) {
		if (city == null) {
			return;
		}
		
		//初始默认为城市无服务
		int cityState = CityState.NO_SERVICE;
		List<CityNetwork> allCityNetworkList = cityNetworkDao.findCityNetworksByCityId(city.getId());
		if (allCityNetworkList != null) {
			//只要有一个网络处于Implemented状态，就认为该城市处于服务状态
			for (CityNetwork cityNetwork : allCityNetworkList) {
				if (cityNetwork.getNetworkState() == NetworkState.IMPLEMENTED.getCode()) {
					cityState = CityState.SERVICING;
					break;
				}
			}
		}
		city.setState(cityState);
	}

}
