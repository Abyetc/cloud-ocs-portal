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
			name: "并发请求连接数",
			data: []
		});
		cityRequestNumMonitorChartCurrSeries++;
		// startMonitoringCityRequestNum();
		window.cityRequestNumMonitorTimer = setTimeout(startMonitoringCityRequestNum, 0);
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
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityConcurrencyRequestNum",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId
		},
		success: function(data) {
			console.log("startMonitoringCityRequestNum:" + data);
			y = data;
			var shiftFlag = cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			cityRequestNumMonitorChart.series[cityRequestNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			cityRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityRequestNumMonitorChart.redraw();
			if (window.cityRequestNumMonitorTimer != null) {
				window.cityRequestNumMonitorTimer = setTimeout(startMonitoringCityRequestNum, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityRequestNumMonitorTimer);
			window.cityRequestNumMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityRequestNum() {
	console.log("stop timer");
	clearTimeout(window.cityRequestNumMonitorTimer);
	window.cityRequestNumMonitorTimer = null;
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
		//startMonitoringCityRxbpsTxbps();
		window.cityRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityRxbpsTxbps, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控数据吞吐率");
		stopMonitoringCityRxbpsTxbps();
	}
});

function startMonitoringCityRxbpsTxbps() {
	// var x = (new Date()).getTime();
	// var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityRxbpsTxbps",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId,
			interfaceName: "eth0"
		},
		success: function(data) {
			console.log("startMonitoringCityRxbpsTxbps:" + data);
			var rxKBpsY = data.rxbps/(8*1024);
			var txKBpsY = data.txbps/(8*1024);
			var x = (new Date()).getTime();
			var shiftFlag = cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].data.length > 100;
			var rxKbpsPoint = [x, rxKBpsY];
			var txKbpsPoint = [x, txKBpsY];

			cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 2].addPoint(rxKbpsPoint, false, shiftFlag);
			cityRxbpsTxbpsMonitorChart.series[cityRxbpsTxbpsMonitorChartCurrSeries - 1].addPoint(txKbpsPoint, false, shiftFlag);

			cityRxbpsTxbpsMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityRxbpsTxbpsMonitorChart.redraw();
			if (window.cityRxbpsTxbpsMonitorTimer != null) {
				window.cityRxbpsTxbpsMonitorTimer = setTimeout(startMonitoringCityRxbpsTxbps, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
			window.cityRxbpsTxbpsMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityRxbpsTxbps() {
	console.log("stop timer");
	clearTimeout(window.cityRxbpsTxbpsMonitorTimer);
	window.cityRxbpsTxbpsMonitorTimer = null;
}