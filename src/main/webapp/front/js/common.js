//监听sidebar点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("#sidebar ul.dropdown-menu a").click(function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	window.hostCpuUsageMonitorTimer = null;

	clearTimeout(window.hostMemoryUsageMonitorTimer);
	window.hostMemoryUsageMonitorTimer = null;

	//=====

	clearTimeout(window.vmCpuUsageMonitorTimer);
	window.vmCpuUsageMonitorTimer = null;

	//=====

	clearTimeout(window.cityRequestNumMonitorTimer);
	window.cityRequestNumMonitorTimer = null;
	clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
	window.cityRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityNetworkRequestNumMonitorTimer);
	window.cityNetworkRequestNumMonitorTimer = null;
	clearTimeout(window.cityNetworkRxbpsTxbpsMonitorTimer);
	window.cityNetworkRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityVmRequestNumMonitorTimer);
	window.cityVmRequestNumMonitorTimer = null;
	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	window.cityVmRxbpsTxbpsMonitorTimer = null;
});

//退出连接，取消掉任何的监控请求
$("#logout").click(function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	window.hostCpuUsageMonitorTimer = null;

	clearTimeout(window.hostMemoryUsageMonitorTimer);
	window.hostMemoryUsageMonitorTimer = null;

	//=====

	clearTimeout(window.vmCpuUsageMonitorTimer);
	window.vmCpuUsageMonitorTimer = null;

	//=====

	clearTimeout(window.cityRequestNumMonitorTimer);
	window.cityRequestNumMonitorTimer = null;
	clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
	window.cityRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityNetworkRequestNumMonitorTimer);
	window.cityNetworkRequestNumMonitorTimer = null;
	clearTimeout(window.cityNetworkRxbpsTxbpsMonitorTimer);
	window.cityNetworkRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityVmRequestNumMonitorTimer);
	window.cityVmRequestNumMonitorTimer = null;
	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	window.cityVmRxbpsTxbpsMonitorTimer = null;
});

//监听breadcrumb点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("body").on("click", "#nav-breadcrumb li a", function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	window.hostCpuUsageMonitorTimer = null;

	clearTimeout(window.hostMemoryUsageMonitorTimer);
	window.hostMemoryUsageMonitorTimer = null;

	//=====

	clearTimeout(window.vmCpuUsageMonitorTimer);
	window.vmCpuUsageMonitorTimer = null;

	//=====

	clearTimeout(window.cityRequestNumMonitorTimer);
	window.cityRequestNumMonitorTimer = null;
	clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
	window.cityRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityNetworkRequestNumMonitorTimer);
	window.cityNetworkRequestNumMonitorTimer = null;
	clearTimeout(window.cityNetworkRxbpsTxbpsMonitorTimer);
	window.cityNetworkRxbpsTxbpsMonitorTimer = null;

	clearTimeout(window.cityVmRequestNumMonitorTimer);
	window.cityVmRequestNumMonitorTimer = null;
	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	window.cityVmRxbpsTxbpsMonitorTimer = null;
});

//=========================================================================

//虚拟机状态描述
var vmStateArray = ["Starting", "Running", "Stopping", "Stopped", "Migrating", "Error", "Unknown", "Shutdowned", "Destroyed", "Expunging"];