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
			name: "并发请求连接数",
			data: []
		});
		cityVmRequestNumMonitorChartCurrSeries++;
		// startMonitoringCityVmRequestNum();
		window.cityVmRequestNumMonitorTimer = setTimeout(startMonitoringCityVmRequestNum, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控并发请求数");
		stopMonitoringCityVmRequestNum();
	}
});

function startMonitoringCityVmRequestNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getConcurrencyRequestNum",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorCityVmId
		},
		success: function(data) {
			console.log("startMonitoringCityVmRequestNum:" + data);
			y = data;
			var shiftFlag = cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			cityVmRequestNumMonitorChart.series[cityVmRequestNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			cityVmRequestNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityVmRequestNumMonitorChart.redraw();
			if (window.cityVmRequestNumMonitorTimer != null) {
				window.cityVmRequestNumMonitorTimer = setTimeout(startMonitoringCityVmRequestNum, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityVmRequestNumMonitorTimer);
			window.cityVmRequestNumMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityVmRequestNum() {
	console.log("stop timer");
	clearTimeout(window.cityVmRequestNumMonitorTimer);
	window.cityVmRequestNumMonitorTimer = null;
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
			cityVmId: window.curMonitorCityVmId,
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
