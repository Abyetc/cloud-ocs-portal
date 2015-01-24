/*
  系统前端页面物理资源监控模块-Host监控动态监控图实现入口
*/

$("body").on("click", "#host-cpu-usage-monitor-btn", function() {
	console.log("click btn");
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
		// startMonitoringHostCpuUsage();
		window.hostCpuUsageMonitorTimer = setTimeout(startMonitoringHostCpuUsage, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控CPU使用情况");
		stopMonitoringHostCpuUsage();
	}
});

function startMonitoringHostCpuUsage() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/host/getCurHostCpuUsagePercentage",
		dataType: "json",
		data: {
			hostId: window.curMonitorHostId
		},
		success: function(data) {
			console.log("getCurHostCpuUsagePercentage:" + data);
			y = data;
			var shiftFlag = hostCpuUsageMonitorChart.series[hostCpuUsageMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			hostCpuUsageMonitorChart.series[hostCpuUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			hostCpuUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			hostCpuUsageMonitorChart.redraw();
			if (window.hostCpuUsageMonitorTimer != null) {
				window.hostCpuUsageMonitorTimer = setTimeout(startMonitoringHostCpuUsage, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.hostCpuUsageMonitorTimer);
			window.hostCpuUsageMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringHostCpuUsage() {
	console.log("stop timer");
	clearTimeout(window.hostCpuUsageMonitorTimer);
	window.hostCpuUsageMonitorTimer = null;
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
		// startMonitoringHostMemoryUsage();
		window.hostMemoryUsageMonitorTimer = setTimeout(startMonitoringHostMemoryUsage, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控内存使用情况");
		stopMonitoringHostMemoryUsage();
	}
});

function startMonitoringHostMemoryUsage() {
	var x = (new Date()).getTime();
	var y = 0.0;

	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/host/getCurHostUsedMemory",
		dataType: "json",
		data: {
			hostId: window.curMonitorHostId
		},
		success: function(data) {
			console.log("getCurHostUsedMemory:" + data);
			y = data;
			var shiftFlag = hostMemoryUsageMonitorChart.series[hostMemoryUsageMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			hostMemoryUsageMonitorChart.series[hostMemoryUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			hostMemoryUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			hostMemoryUsageMonitorChart.redraw();
			if (window.hostMemoryUsageMonitorTimer != null) {
				window.hostMemoryUsageMonitorTimer = setTimeout(startMonitoringHostMemoryUsage, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.hostMemoryUsageMonitorTimer);
			window.hostMemoryUsageMonitorTimer = null;
			alert(status);
		}
	});

}

function stopMonitoringHostMemoryUsage() {
	console.log("stop timer");
	clearTimeout(window.hostMemoryUsageMonitorTimer);
	window.hostMemoryUsageMonitorTimer = null;
}