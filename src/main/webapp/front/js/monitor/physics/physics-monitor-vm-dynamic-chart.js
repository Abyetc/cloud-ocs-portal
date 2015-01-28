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
		window.vmCpuUsageMonitorTimer = setTimeout(startMonitoringVmCpuUsage, 0);
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
			if (window.vmCpuUsageMonitorTimer != null) {
				window.vmCpuUsageMonitorTimer = setTimeout(startMonitoringVmCpuUsage, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.vmCpuUsageMonitorTimer);
			window.vmCpuUsageMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringVmCpuUsage() {
	console.log("stop timer");
	clearTimeout(window.vmCpuUsageMonitorTimer);
	window.vmCpuUsageMonitorTimer = null;
}

//===============================================================================

$("body").on("click", "#vm-memory-usage-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控内存使用情况");
		vmMemoryUsageMonitorChart.addSeries({
			name: "内存使用率",
			data: []
		});
		vmMemoryUsageMonitorChartCurrSeries++;
		window.vmMemoryUsageMonitorTimer = setTimeout(startMonitoringVmMemoryUsage, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控内存使用情况");
		stopMonitoringVmMemoryUsage();
	}
});

function startMonitoringVmMemoryUsage() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCurVmMemoryUsagePercentage",
		dataType: "json",
		data: {
			vmId: window.curMonitorVmId
		},
		success: function(data) {
			console.log("startMonitoringVmMemoryUsage:" + data);
			y = data;
			var shiftFlag = vmMemoryUsageMonitorChart.series[vmMemoryUsageMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			vmMemoryUsageMonitorChart.series[vmMemoryUsageMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			vmMemoryUsageMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			vmMemoryUsageMonitorChart.redraw();
			if (window.vmMemoryUsageMonitorTimer != null) {
				window.vmMemoryUsageMonitorTimer = setTimeout(startMonitoringVmMemoryUsage, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.vmMemoryUsageMonitorTimer);
			window.vmMemoryUsageMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringVmMemoryUsage() {
	console.log("stop timer");
	clearTimeout(window.vmMemoryUsageMonitorTimer);
	window.vmMemoryUsageMonitorTimer = null;
}

//=============================================================================

$("body").on("click", "#city-vm-rxbps-txbps-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控数据吞吐率");
		cityVmRxbpsTxbpsMonitorChart.addSeries({
			name: "接收数据吞吐率",
			data: []
		});
		cityVmRxbpsTxbpsMonitorChart.addSeries({
			name: "发送数据吞吐率",
			data: []
		});
		cityVmRxbpsTxbpsMonitorChartCurrSeries++;
		cityVmRxbpsTxbpsMonitorChartCurrSeries++;
		// startMonitoringCityVmRxbpsTxbps();
		window.cityVmRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityVmRxbpsTxbps, 0);
	} else {
		//设置button文字
		stopMonitoringCityVmRxbpsTxbps();
		$(this).text("点击开始监控数据吞吐率");
	}
});

function startMonitoringCityVmRxbpsTxbps() {
	//var y = 0.0;
	// //请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getRxbpsTxbps",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorVmId,
			interfaceName: "eth0"
		},
		success: function(data) {
			console.log("startMonitoringCityVmRxbpsTxbps:" + data);
			var rxKBpsY = data.rxbps/(8*1024);
			var txKBpsY = data.txbps/(8*1024);
			var x = (new Date()).getTime();
			var shiftFlag = cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
			var rxKbpsPoint = [x, rxKBpsY];
			var txKbpsPoint = [x, txKBpsY];

			cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 2].addPoint(rxKbpsPoint, false, shiftFlag);
			cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(txKbpsPoint, false, shiftFlag);

			cityVmRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityVmRxbpsTxbpsMonitorChart.redraw();
			if (window.cityVmRxbpsTxbpsMonitorTimer != null) {
				window.cityVmRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityVmRxbpsTxbps, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
			window.cityVmRxbpsTxbpsMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityVmRxbpsTxbps() {
	console.log("stop cityVmRxbpsTxbpsMonitorTimer");
	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	window.cityVmRxbpsTxbpsMonitorTimer = null;
}