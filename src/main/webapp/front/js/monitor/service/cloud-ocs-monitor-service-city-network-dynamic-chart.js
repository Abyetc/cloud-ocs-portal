/*
  系统前端页面业务服务监控模块-City所属network监控动态监控图实现入口
*/

$("body").on("click", "#city-network-request-num-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控并发请求数");
		cityNetworkRequestNumMonitorChart.addSeries({
			name: "并发请求连接数",
			data: []
		});
		cityNetworkRequestNumMonitorChartCurrSeries++;
		window.cityNetworkRequestNumMonitorTimer = setTimeout(startMonitoringCityNetworkRequestNum, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控并发请求数");
		stopMonitoringCityNetworkRequestNum();
	}
});

function startMonitoringCityNetworkRequestNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityNetworkConcurrencyRequestNum",
		dataType: "json",
		data: {
			cityNetworkId: window.curMonitorCityNetworkId
		},
		success: function(data) {
			console.log("startMonitoringCityNetworkRequestNum:" + data);
			y = data;
			var shiftFlag = cityNetworkRequestNumMonitorChart.series[cityNetworkRequestNumMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			cityNetworkRequestNumMonitorChart.series[cityNetworkRequestNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			cityNetworkRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityNetworkRequestNumMonitorChart.redraw();
			if (window.cityNetworkRequestNumMonitorTimer != null) {
				window.cityNetworkRequestNumMonitorTimer = setTimeout(startMonitoringCityNetworkRequestNum, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityNetworkRequestNumMonitorTimer);
			window.cityNetworkRequestNumMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityNetworkRequestNum() {
	console.log("stop timer");
	clearTimeout(window.cityNetworkRequestNumMonitorTimer);
	window.cityNetworkRequestNumMonitorTimer = null;
}

//==========================================================================================

$("body").on("click", "#city-network-rxbps-txbps-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控数据吞吐率");
		cityNetworkRxbpsTxbpsMonitorChart.addSeries({
			name: "接收数据吞吐率",
			data: []
		});
		cityNetworkRxbpsTxbpsMonitorChart.addSeries({
			name: "发送数据吞吐率",
			data: []
		});
		cityNetworkRxbpsTxbpsMonitorChartCurrSeries++;
		cityNetworkRxbpsTxbpsMonitorChartCurrSeries++;
		window.cityNetworkRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityNetworkRxbpsTxbps, 0);
	} else {
		//设置button文字
		stopMonitoringCityNetworkRxbpsTxbps();
		$(this).text("点击开始监控数据吞吐率");
	}
});

function startMonitoringCityNetworkRxbpsTxbps() {
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityNetworkRxbpsTxbps",
		dataType: "json",
		data: {
			cityNetworkId: window.curMonitorCityNetworkId,
			interfaceName: "eth0"
		},
		success: function(data) {
			console.log("startMonitoringCityNetworkRxbpsTxbps:" + data);
			var rxKBpsY = data.rxbps/(8*1024);
			var txKBpsY = data.txbps/(8*1024);
			var x = (new Date()).getTime();
			var shiftFlag = cityNetworkRxbpsTxbpsMonitorChart.series[cityNetworkRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
			var rxKbpsPoint = [x, rxKBpsY];
			var txKbpsPoint = [x, txKBpsY];

			cityNetworkRxbpsTxbpsMonitorChart.series[cityNetworkRxbpsTxbpsMonitorChartCurrSeries - 2].addPoint(rxKbpsPoint, false, shiftFlag);
			cityNetworkRxbpsTxbpsMonitorChart.series[cityNetworkRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(txKbpsPoint, false, shiftFlag);

			cityNetworkRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityNetworkRxbpsTxbpsMonitorChart.redraw();
			if (window.cityNetworkRxbpsTxbpsMonitorTimer != null) {
				window.cityNetworkRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityNetworkRxbpsTxbps, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityNetworkRxbpsTxbpsMonitorTimer);
			window.cityNetworkRxbpsTxbpsMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityNetworkRxbpsTxbps() {
	console.log("stop cityNetworkRxbpsTxbpsMonitorTimer");
	clearTimeout(window.cityNetworkRxbpsTxbpsMonitorTimer);
	window.cityNetworkRxbpsTxbpsMonitorTimer = null;
}