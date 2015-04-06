/*
  系统前端页面业务服务监控模块-City监控动态监控图实现入口
*/

$("body").on("click", "#city-realtime-session-num-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控实时会话数");
		cityRealtimeSessionNumMonitorChart.addSeries({
			name: "实时会话数",
			data: []
		});
		cityRealtimeSessionNumMonitorChartCurrSeries++;
		window.cityRealtimeSessionNumMonitorTimer = setTimeout(startMonitoringCityRealtimeSessionNum, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控实时会话数");
		stopMonitoringCityRealtimeSessionNum();
	}
});

function startMonitoringCityRealtimeSessionNum() {
	var x = (new Date()).getTime();
	var y = 0.0;
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityRealtimeSessionNum",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId
		},
		success: function(data) {
			console.log("startMonitoringCityRealtimeSessionNum:" + data);
			y = data;
			y = Math.random() * 100;
			var shiftFlag = cityRealtimeSessionNumMonitorChart.series[cityRealtimeSessionNumMonitorChartCurrSeries - 1].data.length > 100;
			var point = [x, y];

			cityRealtimeSessionNumMonitorChart.series[cityRealtimeSessionNumMonitorChartCurrSeries - 1].addPoint(point, false, shiftFlag);

			cityRealtimeSessionNumMonitorChart.xAxis[0].setExtremes(x - 100 * 1000, x, false); //100个点 
			cityRealtimeSessionNumMonitorChart.redraw();
			if (window.cityRealtimeSessionNumMonitorTimer != null) {
				window.cityRealtimeSessionNumMonitorTimer = setTimeout(startMonitoringCityRealtimeSessionNum, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityRealtimeSessionNumMonitorTimer);
			window.cityRealtimeSessionNumMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityRealtimeSessionNum() {
	console.log("stop timer");
	clearTimeout(window.cityRealtimeSessionNumMonitorTimer);
	window.cityRealtimeSessionNumMonitorTimer = null;
}

//===============================================================================

$("body").on("click", "#city-message-throughput-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包吞吐量");
		cityMessageThroughputChart.addSeries({
			name: "接收的包数量",
			data: []
		});
		cityMessageThroughputChartCurrSeries++;
		cityMessageThroughputChart.addSeries({
			name: "处理完成的包数量",
			data: []
		});
		cityMessageThroughputChartCurrSeries++;
		window.cityMessageThroughputMonitorTimer = setTimeout(startMonitoringCityMessageThroughput, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控包吞吐量");
		stopMonitoringCityMessageThroughput();
	}
});

function startMonitoringCityMessageThroughput() {
	var x = (new Date()).getTime();
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityRealtimeMessageThroughput",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId
		},
		success: function(data) {
			console.log("startMonitoringCityMessageThroughput:" + data);
			if (data.receivedMessageNum == window.cityMessageThroughputLastReceived && data.finishedMessageNum == window.cityMessageThroughputLastFinished) {
				window.cityMessageThroughputCounter++;
			}
			else {
				window.cityMessageThroughputCounter = 0;
			}
			
			window.cityMessageThroughputLastReceived = data.receivedMessageNum;
			window.cityMessageThroughputLastFinished = data.finishedMessageNum;
			if (window.cityMessageThroughputCounter >= 3) {
				data.receivedMessageNum = 0;
				data.finishedMessageNum = 0;
			}
			var shiftFlag = cityMessageThroughputChart.series[cityMessageThroughputChartCurrSeries - 1].data.length > 100;
			var receivedMessageNumPoint = [x, data.receivedMessageNum];
			var finishedMessageNumPoint = [x, data.finishedMessageNum];

			cityMessageThroughputChart.series[cityMessageThroughputChartCurrSeries - 2].addPoint(receivedMessageNumPoint, false, shiftFlag);
			cityMessageThroughputChart.series[cityMessageThroughputChartCurrSeries - 1].addPoint(finishedMessageNumPoint, false, shiftFlag);

			cityMessageThroughputChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityMessageThroughputChart.redraw();
			if (window.cityMessageThroughputMonitorTimer != null) {
				window.cityMessageThroughputMonitorTimer = setTimeout(startMonitoringCityMessageThroughput, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityMessageThroughputMonitorTimer);
			window.cityMessageThroughputMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityMessageThroughput() {
	console.log("stop timer");
	clearTimeout(window.cityMessageThroughputMonitorTimer);
	window.cityMessageThroughputMonitorTimer = null;
}

//===============================================================================

$("body").on("click", "#city-message-process-time-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包处理平均时长");
		cityMessageProcessTimeChart.addSeries({
			name: "所有包处理平均时长",
			data: []
		});
		cityMessageProcessTimeChartCurrSeries++;
		cityMessageProcessTimeChart.addSeries({
			name: "I包处理平均时长",
			data: []
		});
		cityMessageProcessTimeChartCurrSeries++;
		cityMessageProcessTimeChart.addSeries({
			name: "U包处理平均时长",
			data: []
		});
		cityMessageProcessTimeChartCurrSeries++;
		cityMessageProcessTimeChart.addSeries({
			name: "T包处理平均时长",
			data: []
		});
		cityMessageProcessTimeChartCurrSeries++;
		window.cityMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityMessageProcessTime, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控包处理平均时长");
		stopMonitoringCityMessageProcessTime();
	}
});

function startMonitoringCityMessageProcessTime() {
	var x = (new Date()).getTime();
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityRealtimeMessageProcessTime",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId
		},
		success: function(data) {
			console.log("startMonitoringCityMessageProcessTime:" + data);
			var allMessageProcessTime = data.allMessageProcessTime;
			var messageIProcessTime = data.messageIProcessTime;
			var messageUProcessTime = data.messageUProcessTime;
			var messageTProcessTime = data.messageTProcessTime;
			var shiftFlag = cityMessageProcessTimeChart.series[cityMessageProcessTimeChartCurrSeries - 1].data.length > 100;
			var allMessageProcessTimePoint = [x, allMessageProcessTime];
			var messageIProcessTimePoint = [x, messageIProcessTime];
			var messageUProcessTimePoint = [x, messageUProcessTime];
			var messageTProcessTimePoint = [x, messageTProcessTime];

			cityMessageProcessTimeChart.series[cityMessageProcessTimeChartCurrSeries - 4].addPoint(allMessageProcessTimePoint, false, shiftFlag);
			cityMessageProcessTimeChart.series[cityMessageProcessTimeChartCurrSeries - 3].addPoint(messageIProcessTimePoint, false, shiftFlag);
			cityMessageProcessTimeChart.series[cityMessageProcessTimeChartCurrSeries - 2].addPoint(messageUProcessTimePoint, false, shiftFlag);
			cityMessageProcessTimeChart.series[cityMessageProcessTimeChartCurrSeries - 1].addPoint(messageTProcessTimePoint, false, shiftFlag);

			cityMessageProcessTimeChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityMessageProcessTimeChart.redraw();
			if (window.cityMessageProcessTimeMonitorTimer != null) {
				window.cityMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityMessageProcessTime, 3000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityMessageProcessTimeMonitorTimer);
			window.cityMessageProcessTimeMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityMessageProcessTime() {
	console.log("stop timer");
	clearTimeout(window.cityMessageProcessTimeMonitorTimer);
	window.cityMessageProcessTimeMonitorTimer = null;
}

//==============================================================================
//==============================================================================

$("body").on("click", "#city-history-session-num-monitor-btn", function() {
	$("#city-history-session-num-monitor-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityHistorySessionNum",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId,
			date: $("#city-history-session-num-datepicker").val()
		},
		success: function(data) {
		     $("div.loader").shCircleLoader('destroy');
		     $("div.loader").remove();
		     cityHistorySessionNumChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'city-history-session-num-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "会话数历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "接入的会话数" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 0) + '个';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 500,
				        },
				    exporting: {
				        enabled: false
				      },
				      credits: {
				        enabled: false
				    },
				    series: [{name:"会话数历史曲线", data:data}]
			  });
		},
		error: function(xhr, status) {
		    $("div.loader").shCircleLoader('destroy');
		    $("div.loader").remove();
			alert(status);
		}
	});
});

$("body").on("click", "#city-history-message-throughput-monitor-btn", function() {
	$("#city-history-message-throughput-monitor-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityHistoryMessageThroughput",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId,
			date: $("#city-history-message-throughput-datepicker").val()
		},
		success: function(data) {
		     $("div.loader").shCircleLoader('destroy');
		     $("div.loader").remove();
		     cityHistoryMessageThroughputChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'city-history-message-throughput-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "包吞吐量历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "包个数" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 0) + '个';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 500,
				        },
				    legend: {
				        enabled: true
				    },
				    exporting: {
				        enabled: false
				      },
				      credits: {
				        enabled: false
				    },
				    series: [{name:"接收到的包个数", data:data.received}, {name:"处理完成的包个数", data:data.finished}]
			  });
		},
		error: function(xhr, status) {
		    $("div.loader").shCircleLoader('destroy');
		    $("div.loader").remove();
			alert(status);
		}
	});
});

$("body").on("click", "#city-history-message-process-time-monitor-btn", function() {
	$("#city-history-message-process-time-monitor-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/city/getCityHistoryMessageProcessTime",
		dataType: "json",
		data: {
			cityId: window.curMonitorCityId,
			date: $("#city-history-message-process-time-datepicker").val()
		},
		success: function(data) {
			console.log(data.allMsg);
		     $("div.loader").shCircleLoader('destroy');
		     $("div.loader").remove();
		     cityHistoryMessageProcessTimeChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'city-history-message-process-time-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "包处理时长历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "包处理时长" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 0) + 'ms';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 1000,
				        },
				    legend: {
				        enabled: true
				    },
				    exporting: {
				        enabled: false
				      },
				      credits: {
				        enabled: false
				    },
				    series: [{name:"所有包", data:data.allMsg}, {name:"I包", data:data.iMsg}, {name:"U包", data:data.uMsg}, {name:"T包", data:data.tMsg}]
			  });
		},
		error: function(xhr, status) {
		    $("div.loader").shCircleLoader('destroy');
		    $("div.loader").remove();
			alert(status);
		}
	});
});