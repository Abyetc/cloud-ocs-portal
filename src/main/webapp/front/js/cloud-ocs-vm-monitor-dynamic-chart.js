/*
  系统前端页面物理资源监控模块-VM监控动态监控图实现入口
*/

$("body").on("click", "#vm-cpu-usage-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控CPU使用情况");
		vmCpuUsageMonitorChart.addSeries({
			name: "CPU使用率",
			data: []
		});
		vmCpuUsageMonitorChartCurrSeries++;
		startMonitoringVmCpuUsage();
	} else {
		//设置button文字
		$(this).text("点击开始监控CPU使用情况");
		stopMonitoringVmCpuUsage();
	}
});

function startMonitoringVmCpuUsage() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCurVmCpuUsagePercentage",
		dataType: "json",
		data: {
			vmId: window.curMonitorVmId
		},
		success: function(data) {
			console.log("getCurVmCpuUsagePercentage:" + data);
			y = data;
			var shiftFlag = vmCpuUsageMonitorChart.series[vmCpuUsageMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			vmCpuUsageMonitorChart.series[vmCpuUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			vmCpuUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			vmCpuUsageMonitorChart.redraw();
			window.vmCpuUsageMonitorTimer = setTimeout(startMonitoringVmCpuUsage, 1000);
		},
		error: function(xhr, status) {
			clearTimeout(window.vmCpuUsageMonitorTimer);
			alert(status);
		}
	});
}

function stopMonitoringVmCpuUsage() {
	console.log("stop timer");
	clearTimeout(window.vmCpuUsageMonitorTimer);
}