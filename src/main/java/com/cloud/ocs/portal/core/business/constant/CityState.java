package com.cloud.ocs.portal.core.business.constant;

/**
 * 服务城市状态
 * 
 * @author Wang Chao
 *
 * @date 2014-12-29 下午8:50:33
 *
 */
public class CityState {

	public static final Integer NO_SERVICE = 0; //0：无服务（没有任何来宾网络）；
	public static final Integer SERVICING = 1; //1：服务中（所有来宾网络均工作正常）；
	//public static final Integer FAULTED = 2; //2：故障（只要有任何一个来宾网络工作不正常）
}
