package com.cloud.ocs.portal.core.infrastructure.constant;

/**
 * 系统资源模块调用CloudStack API名称
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:43:59
 *
 */
public class ResourceApiName {

	public final static String RESOURCE_API_LIST_ZONES			   = 	"listZones";
	
	public final static String RESOURCE_API_LIST_PODS 			   =	"listPods";
	public final static String RESOURCE_API_LIST_SECONDARY_STORAGE = 	"listImageStores";
	public final static String RESOURCE_API_ADD_SECONDARY_STORAGE  = 	"addImageStore";
	public final static String RESOURCE_API_REMOVE_SECONDARY_STORAGE =  "deleteImageStore";
	public final static String RESOURCE_API_LIST_SYSTEM_VMS 	   = 	"listSystemVms";
	public final static String RESOURCE_API_LIST_ROUTERS		   = 	"listRouters";
	
	public final static String RESOURCE_API_LIST_CLUSTERS 		   = 	"listClusters";
	
	public final static String RESOURCE_API_ADD_PRIMARY_STORAGE    =	"createStoragePool";
	public final static String RESOURCE_API_LIST_PRIMARY_STORAGE   = 	"listStoragePools";
	public final static String RESOURCE_API_REMOVE_PRIMARY_STORAGE =	"deleteStoragePool";
	public final static String RESOURCE_API_MAINTENANCE_PRIMARY_STORAGE = "enableStorageMaintenance";
	public final static String RESOURCE_API_LIST_HOSTS 			   = 	"listHosts";	
	public final static String RESOURCE_API_ADD_HOST			   =	"addHost";
	public final static String RESOURCE_API_MAINTENANC_HOST		   =	"prepareHostForMaintenance";
	public final static String RESOURCE_API_REMOVE_HOST			   =	"deleteHost";
	
	
	public final static String RESOURCE_API_LIST_CAPACITY		   =	"listCapacity";
	
	public final static String RESOURCE_API_LIST_NETWORK_OFFERINGS =	"listNetworkOfferings";
	public final static String RESOURCE_API_LIST_TEMPLATES		   =	"listTemplates";
	public final static String RESOURCE_API_LIST_SERVICE_OFFERINGS =	"listServiceOfferings";
	
	public final static String RESOURCE_API_FIND_HOST_FOR_MIGRATION=	"findHostsForMigration";
	public final static String RESOURCE_API_MIGRATE_VM			   =	"migrateVirtualMachine";
}
