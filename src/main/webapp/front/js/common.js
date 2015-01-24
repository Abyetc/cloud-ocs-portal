//监听sidebar点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("#sidebar ul.dropdown-menu a").click(function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	window.hostCpuUsageMonitorTimer = null;

	clearTimeout(window.hostMemoryUsageMonitorTimer);
	window.hostMemoryUsageMonitorTimer = null;

	//=====

	clearTimeout(window.vmCpuUsageMonitorTimer);
	window.vmCpuUsageMonitorTimer = null;

	clearTimeout(window.vmMemoryUsageMonitorTimer);
	window.vmMemoryUsageMonitorTimer = null;

	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	window.cityVmRxbpsTxbpsMonitorTimer = null;

	//=====

	clearTimeout(window.cityRealtimeSessionNumMonitorTimer);
	window.cityRealtimeSessionNumMonitorTimer = null;
	clearTimeout(window.cityMessageProcessTimeMonitorTimer);
	window.cityMessageProcessTimeMonitorTimer = null;

	clearTimeout(window.cityNetworkRealtimeSessionNumMonitorTimer);
	window.cityNetworkRealtimeSessionNumMonitorTimer = null;
	clearTimeout(window.cityNetworkMessageProcessTimeMonitorTimer);
	window.cityNetworkMessageProcessTimeMonitorTimer = null;

	clearTimeout(window.cityVmRequestNumMonitorTimer);
	window.cityVmRequestNumMonitorTimer = null;
	clearTimeout(window.cityVmMessageProcessTimeMonitorTimer);
	window.cityVmMessageProcessTimeMonitorTimer = null;
});

//退出连接，取消掉任何的监控请求
$("#logout").click(function() {
	
});

//监听breadcrumb点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("body").on("click", "#nav-breadcrumb li a", function() {
	
});

//=========================================================================

//虚拟机状态描述
var vmStateArray = ["Starting", "Running", "Stopping", "Stopped", "Migrating", "Error", "Unknown", "Shutdowned", "Destroyed", "Expunging"];