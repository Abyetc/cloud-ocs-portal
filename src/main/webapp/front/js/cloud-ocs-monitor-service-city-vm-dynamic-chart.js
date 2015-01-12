/*
  系统前端页面业务服务监控模块-City vm监控动态监控图实现入口
*/

$("body").on("click", "#city-vm-request-num-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控并发请求数");
		cityVmRequestNumMonitorChart.addSeries({
			name: "接收的请求数",
			data: []
		});
		cityVmRequestNumMonitorChart.addSeries({
			name: "处理完成的请求数",
			data: []
		});
		cityVmRequestNumMonitorChartCurrSeries++;
		cityVmRequestNumMonitorChartCurrSeries++;
		startMonitoringCityVmRequestNum();
	} else {
		//设置button文字
		$(this).text("点击开始监控并发请求数");
		stopMonitoringCityVmRequestNum();
	}
});

function startMonitoringCityVmRequestNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	// //请求数据
	// $.ajax({
	// 	type: "GET",
	// 	url: "monitor/",
	// 	dataType: "json",
	// 	data: {
	// 		cityVmId: window.curMonitorCityVmId
	// 	},
	// 	success: function(data) {
	// 		console.log("startMonitoringCityVmRequestNum:" + data);
	// 		y = data;
	// 		var shiftFlag = cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].data.length > 100;
	// 		var point = [x, y];

	// 		cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	// 		cityVmRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	// 		cityVmRequestNumMonitorChart.redraw();
	// 		window.cityVmRequestNumMonitorTimer = setTimeout(startMonitoringCityVmRequestNum, 1000);
	// 	},
	// 	error: function(xhr, status) {
	// 		clearTimeout(window.cityVmRequestNumMonitorTimer);
	// 		alert(status);
	// 	}
	// });
	y = 10;
	y1 = 5;
	var shiftFlag = cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];
	var point1 = [x, y1];

	cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 2].addPoint(point, false, shiftFlag);
	cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].addPoint(point1, false, shiftFlag);

	cityVmRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	cityVmRequestNumMonitorChart.redraw();
	window.cityVmRequestNumMonitorTimer = setTimeout(startMonitoringCityVmRequestNum, 1000);
}

function stopMonitoringCityVmRequestNum() {
	console.log("stop timer");
	clearTimeout(window.cityVmRequestNumMonitorTimer);
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
		startMonitoringCityVmRxbpsTxbps();
	} else {
		//设置button文字
		$(this).text("点击开始监控数据吞吐率");
		stopMonitoringCityVmRxbpsTxbps();
	}
});

function startMonitoringCityVmRxbpsTxbps() {
	var x = (new Date()).getTime();
	var y = 0.0;
	// //请求数据
	// $.ajax({
	// 	type: "GET",
	// 	url: "monitor/",
	// 	dataType: "json",
	// 	data: {
	// 		cityVmId: window.curMonitorCityVmId
	// 	},
	// 	success: function(data) {
	// 		console.log("startMonitoringCityVmRxbpsTxbps:" + data);
	// 		y = data;
	// 		var shiftFlag = cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
	// 		var point = [x, y];

	// 		cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	// 		cityVmRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	// 		cityVmRxbpsTxbpsMonitorChart.redraw();
	// 		window.cityVmRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityVmRxbpsTxbps, 1000);
	// 	},
	// 	error: function(xhr, status) {
	// 		clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
	// 		alert(status);
	// 	}
	// });
	y = 500;
	y1 = 300;
	var shiftFlag = cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];
	var point1 = [x, y1];

	cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 2].addPoint(point, false, shiftFlag);
	cityVmRxbpsTxbpsMonitorChart.series[cityVmRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(point1, false, shiftFlag);
	
	cityVmRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	cityVmRxbpsTxbpsMonitorChart.redraw();
	window.cityVmRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityVmRxbpsTxbps, 1000);
}

function stopMonitoringCityVmRxbpsTxbps() {
	console.log("stop timer");
	clearTimeout(window.cityVmRxbpsTxbpsMonitorTimer);
}
