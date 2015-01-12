/*
  系统前端页面业务服务监控模块-City监控动态监控图实现入口
*/

$("body").on("click", "#city-request-num-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控并发请求数");
		cityRequestNumMonitorChart.addSeries({
			name: "接收的请求数",
			data: []
		});
		cityRequestNumMonitorChart.addSeries({
			name: "处理完成的请求数",
			data: []
		});
		cityRequestNumMonitorChartCurrSeries++;
		cityRequestNumMonitorChartCurrSeries++;
		startMonitoringCityRequestNum();
	} else {
		//设置button文字
		$(this).text("点击开始监控并发请求数");
		stopMonitoringCityRequestNum();
	}
});

function startMonitoringCityRequestNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	// $.ajax({
	// 	type: "GET",
	// 	url: "monitor/",
	// 	dataType: "json",
	// 	data: {
	// 		cityId: window.curMonitorCityId
	// 	},
	// 	success: function(data) {
	// 		console.log("startMonitoringCityRequestNum:" + data);
	// 		y = data;
	// 		var shiftFlag = cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].data.length > 100;
	// 		var point = [x, y];

	// 		cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	// 		cityRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	// 		cityRequestNumMonitorChart.redraw();
	// 		window.cityRequestNumMonitorTimer = setTimeout(startMonitoringCityRequestNum, 1000);
	// 	},
	// 	error: function(xhr, status) {
	// 		clearTimeout(window.cityRequestNumMonitorTimer);
	// 		alert(status);
	// 	}
	// });
	y = 50;
	y1 = 30;
	var shiftFlag = cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];
	var point1 = [x, y1];

	cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 2].addPoint(point, false, shiftFlag);
	cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].addPoint(point1, false, shiftFlag);

	cityRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	cityRequestNumMonitorChart.redraw();
	window.cityRequestNumMonitorTimer = setTimeout(startMonitoringCityRequestNum, 1000);
}

function stopMonitoringCityRequestNum() {
	console.log("stop timer");
	clearTimeout(window.cityRequestNumMonitorTimer);
}

//===============================================================================

$("body").on("click", "#city-rxbps-txbps-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控数据吞吐率");
		cityRxbpsTxbpsMonitorChart.addSeries({
			name: "接收数据吞吐率",
			data: []
		});
		cityRxbpsTxbpsMonitorChart.addSeries({
			name: "发送数据吞吐率",
			data: []
		});
		cityRxbpsTxbpsMonitorChartCurrSeries++;
		cityRxbpsTxbpsMonitorChartCurrSeries++;
		startMonitoringCityRxbpsTxbps();
	} else {
		//设置button文字
		$(this).text("点击开始监控数据吞吐率");
		stopMonitoringCityRxbpsTxbps();
	}
});

function startMonitoringCityRxbpsTxbps() {
	var x = (new Date()).getTime();
	var y = 0.0;
	// //请求数据
	// $.ajax({
	// 	type: "GET",
	// 	url: "monitor/",
	// 	dataType: "json",
	// 	data: {
	// 		cityId: window.curMonitorCityId
	// 	},
	// 	success: function(data) {
	// 		console.log("startMonitoringCityRxbpsTxbps:" + data);
	// 		y = data;
	// 		var shiftFlag = cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
	// 		var point = [x, y];

	// 		cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

	// 		cityRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	// 		cityRxbpsTxbpsMonitorChart.redraw();
	// 		window.cityRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityRxbpsTxbps, 1000);
	// 	},
	// 	error: function(xhr, status) {
	// 		clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
	// 		alert(status);
	// 	}
	// });
	y = 500;
	y1 = 200;
	var shiftFlag = cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
	var point = [x, y];
	var point1 = [x, y1];

	cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 2].addPoint(point, false, shiftFlag);
	cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(point1, false, shiftFlag);

	cityRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
	cityRxbpsTxbpsMonitorChart.redraw();
	window.cityRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityRxbpsTxbps, 1000);
}

function stopMonitoringCityRxbpsTxbps() {
	console.log("stop timer");
	clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
}