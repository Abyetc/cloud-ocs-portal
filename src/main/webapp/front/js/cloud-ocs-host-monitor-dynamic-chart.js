/*
  系统前端页面物理资源监控模块-Host监控动态监控图实现入口
*/

$("body").on("click", "#host-cpu-usage-monitor-btn", function() {
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控CPU使用情况");
		hostCpuUsageMonitorChart.addSeries({
			name: "CPU使用率",
			data: []
		});
		hostCpuUsageMonitorChartCurrSeries++;
		startMonitoringHostCpuUsage();
	} else {
		//设置button文字
		$(this).text("点击开始监控CPU使用情况");
		stopMonitoringHostCpuUsage();
	}
});

function startMonitoringHostCpuUsage() {
	var x = (new Date()).getTime();
	var y = Math.random();
	var shiftFlag = hostCpuUsageMonitorChart.series[hostCpuUsageMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];

	hostCpuUsageMonitorChart.series[hostCpuUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	hostCpuUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	hostCpuUsageMonitorChart.redraw();
	window.hostCpuUsageMonitorTimer = setTimeout(startMonitoringHostCpuUsage, 1000);
}

function stopMonitoringHostCpuUsage() {
	clearTimeout(window.hostCpuUsageMonitorTimer);
}


//==============================================================================


$("body").on("click", "#host-memory-usage-monitor-btn", function() {
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控内存使用情况");
		hostMemoryUsageMonitorChart.addSeries({
			name: "内存使用率",
			data: []
		});
		hostMemoryUsageMonitorChartCurrSeries++;
		startMonitoringHostMemoryUsage();
	} else {
		//设置button文字
		$(this).text("点击开始监控内存使用情况");
		stopMonitoringHostMemoryUsage();
	}
});

function startMonitoringHostMemoryUsage() {
	var x = (new Date()).getTime();
	var y = Math.random();
	var shiftFlag = hostMemoryUsageMonitorChart.series[hostMemoryUsageMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];

	hostMemoryUsageMonitorChart.series[hostMemoryUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	hostMemoryUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	hostMemoryUsageMonitorChart.redraw();
	window.hostMemoryUsageMonitorTimer = setTimeout(startMonitoringHostMemoryUsage, 1000);
}

function stopMonitoringHostMemoryUsage() {
	clearTimeout(window.hostMemoryUsageMonitorTimer);
}

