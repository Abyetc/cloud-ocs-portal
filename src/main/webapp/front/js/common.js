//监听sidebar点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("#sidebar ul.dropdown-menu a").click(function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	clearTimeout(window.hostMemoryUsageMonitorTimer);
});

//退出连接，取消掉任何的监控请求
$("#logout").click(function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	clearTimeout(window.hostMemoryUsageMonitorTimer);
});

//监听breadcrumb点击行为，取消掉任何的监控请求，防止一直发送监控请求
$("body").on("click", "#nav-breadcrumb li a", function() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
	clearTimeout(window.hostMemoryUsageMonitorTimer);
});

//=========================================================================