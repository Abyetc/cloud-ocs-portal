/*
  系统前端页面业务服务监控模块-City所属network监控动态监控图实现入口
*/

$("body").on("click", "#city-network-realtime-session-num-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控实时会话数");
		cityNetworkRealtimeSessionNumMonitorChart.addSeries({
			name: "并发请求连接数",
			data: []
		});
		cityNetworkRealtimeSessionNumMonitorChartCurrSeries++;
		window.cityNetworkRealtimeSessionNumMonitorTimer = setTimeout(startMonitoringCityNetworkRealtimeSessionNum, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控实时会话数");
		stopMonitoringCityNetworkRealtimeSessionNum();
	}
});

function startMonitoringCityNetworkRealtimeSessionNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/network/getCityNetworkRealtimeSessionNum",
		dataType: "json",
		data: {
			cityNetworkId: window.curMonitorCityNetworkId
		},
		success: function(data) {
			console.log("startMonitoringCityNetworkRealtimeSessionNum:" + data);
			y = data;
			var shiftFlag = cityNetworkRealtimeSessionNumMonitorChart.series[cityNetworkRealtimeSessionNumMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			cityNetworkRealtimeSessionNumMonitorChart.series[cityNetworkRealtimeSessionNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			cityNetworkRealtimeSessionNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityNetworkRealtimeSessionNumMonitorChart.redraw();
			if (window.cityNetworkRealtimeSessionNumMonitorTimer != null) {
				window.cityNetworkRealtimeSessionNumMonitorTimer = setTimeout(startMonitoringCityNetworkRealtimeSessionNum, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityNetworkRealtimeSessionNumMonitorTimer);
			window.cityNetworkRealtimeSessionNumMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityNetworkRealtimeSessionNum() {
	console.log("stop timer");
	clearTimeout(window.cityNetworkRealtimeSessionNumMonitorTimer);
	window.cityNetworkRealtimeSessionNumMonitorTimer = null;
}

//===============================================================================

$("body").on("click", "#city-network-message-throughput-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包吞吐量");
		cityNetworkMessageThroughputChart.addSeries({
			name: "接收的包数量",
			data: []
		});
		cityNetworkMessageThroughputChartCurrSeries++;
		cityNetworkMessageThroughputChart.addSeries({
			name: "处理完成的包数量",
			data: []
		});
		cityNetworkMessageThroughputChartCurrSeries++;
		window.cityNetworkMessageThroughputMonitorTimer = setTimeout(startMonitoringCityNetworkMessageThroughput, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控包吞吐量");
		stopMonitoringCityNetworkMessageThroughput();
	}
});

function startMonitoringCityNetworkMessageThroughput() {
	var x = (new Date()).getTime();
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/network/getCityNetworkMessageThroughput",
		dataType: "json",
		data: {
			cityNetworkId: window.curMonitorCityNetworkId
		},
		success: function(data) {
			console.log("startMonitoringCityNetworkMessageThroughput:" + data);
			if (data.receivedMessageNum == window.cityNetworkMessageThroughputLastReceived && data.finishedMessageNum == window.cityNetworkMessageThroughputLastFinished) {
				window.cityNetworkMessageThroughputCounter++;
			}
			else {
				window.cityNetworkMessageThroughputCounter = 0;
			}
			
			window.cityNetworkMessageThroughputLastReceived = data.receivedMessageNum;
			window.cityNetworkMessageThroughputLastFinished = data.finishedMessageNum;
			if (window.cityNetworkMessageThroughputCounter >= 3) {
				data.receivedMessageNum = 0;
				data.finishedMessageNum = 0;
			}
			var shiftFlag = cityNetworkMessageThroughputChart.series[cityNetworkMessageThroughputChartCurrSeries - 1].data.length > 100;
			var receivedMessageNumPoint = [x, data.receivedMessageNum];
			var finishedMessageNumPoint = [x, data.finishedMessageNum];

			cityNetworkMessageThroughputChart.series[cityNetworkMessageThroughputChartCurrSeries - 2].addPoint(receivedMessageNumPoint, false, shiftFlag);
			cityNetworkMessageThroughputChart.series[cityNetworkMessageThroughputChartCurrSeries - 1].addPoint(finishedMessageNumPoint, false, shiftFlag);

			cityNetworkMessageThroughputChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityNetworkMessageThroughputChart.redraw();
			if (window.cityNetworkMessageThroughputMonitorTimer != null) {
				window.cityNetworkMessageThroughputMonitorTimer = setTimeout(startMonitoringCityNetworkMessageThroughput, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityNetworkMessageThroughputMonitorTimer);
			window.cityNetworkMessageThroughputMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityNetworkMessageThroughput() {
	console.log("stop timer");
	clearTimeout(window.cityNetworkMessageThroughputMonitorTimer);
	window.cityNetworkMessageThroughputMonitorTimer = null;
}

//==========================================================================================

$("body").on("click", "#city-network-message-process-time-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包处理平均时长");
		cityNetworkMessageProcessTimeMonitorChart.addSeries({
			name: "所有包处理平均时长",
			data: []
		});
		cityNetworkMessageProcessTimeMonitorChartCurrSeries++;
		cityNetworkMessageProcessTimeMonitorChart.addSeries({
			name: "I包处理平均时长",
			data: []
		});
		cityNetworkMessageProcessTimeMonitorChartCurrSeries++;
		cityNetworkMessageProcessTimeMonitorChart.addSeries({
			name: "U包处理平均时长",
			data: []
		});
		cityNetworkMessageProcessTimeMonitorChartCurrSeries++;
		cityNetworkMessageProcessTimeMonitorChart.addSeries({
			name: "T包处理平均时长",
			data: []
		});
		cityNetworkMessageProcessTimeMonitorChartCurrSeries++;
		window.cityNetworkMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityNetworkMessageProcessTime, 0);
	} else {
		//设置button文字
		stopMonitoringCityNetworkMessageProcessTime();
		$(this).text("点击开始监控包处理平均时长");
	}
});

function startMonitoringCityNetworkMessageProcessTime() {
	var x = (new Date()).getTime();
	$.ajax({
		type: "GET",
		url: "monitor/network/getCityNetworkMessageProcessTime",
		dataType: "json",
		data: {
			cityNetworkId: window.curMonitorCityNetworkId
		},
		success: function(data) {
			console.log("startMonitoringCityNetworkMessageProcessTime:" + data);
			var allMessageProcessTime = data.allMessageProcessTime;
			var messageIProcessTime = data.messageIProcessTime;
			var messageUProcessTime = data.messageUProcessTime;
			var messageTProcessTime = data.messageTProcessTime;
			var shiftFlag = cityNetworkMessageProcessTimeMonitorChart.series[cityNetworkMessageProcessTimeMonitorChartCurrSeries - 1].data.length > 100;
			var allMessageProcessTimePoint = [x, allMessageProcessTime];
			var messageIProcessTimePoint = [x, messageIProcessTime];
			var messageUProcessTimePoint = [x, messageUProcessTime];
			var messageTProcessTimePoint = [x, messageTProcessTime];

			cityNetworkMessageProcessTimeMonitorChart.series[cityNetworkMessageProcessTimeMonitorChartCurrSeries - 4].addPoint(allMessageProcessTimePoint, false, shiftFlag);
			cityNetworkMessageProcessTimeMonitorChart.series[cityNetworkMessageProcessTimeMonitorChartCurrSeries - 3].addPoint(messageIProcessTimePoint, false, shiftFlag);
			cityNetworkMessageProcessTimeMonitorChart.series[cityNetworkMessageProcessTimeMonitorChartCurrSeries - 2].addPoint(messageUProcessTimePoint, false, shiftFlag);
			cityNetworkMessageProcessTimeMonitorChart.series[cityNetworkMessageProcessTimeMonitorChartCurrSeries - 1].addPoint(messageTProcessTimePoint, false, shiftFlag);

			cityNetworkMessageProcessTimeMonitorChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityNetworkMessageProcessTimeMonitorChart.redraw();
			if (window.cityNetworkMessageProcessTimeMonitorTimer != null) {
				window.cityNetworkMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityNetworkMessageProcessTime, 3000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityNetworkMessageProcessTimeMonitorTimer);
			window.cityNetworkMessageProcessTimeMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityNetworkMessageProcessTime() {
	console.log("stop cityNetworkMessageProcessTimeMonitorTimer");
	clearTimeout(window.cityNetworkMessageProcessTimeMonitorTimer);
	window.cityNetworkMessageProcessTimeMonitorTimer = null;
}