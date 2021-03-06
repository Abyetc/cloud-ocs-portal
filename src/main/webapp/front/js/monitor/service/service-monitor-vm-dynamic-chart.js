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

//===============================================================================

$("body").on("click", "#city-vm-message-throughput-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包吞吐量");
		cityVmMessageThroughputMonitorChart.addSeries({
			name: "接收的包数量",
			data: []
		});
		cityVmMessageThroughputMonitorChartCurrSeries++;
		cityVmMessageThroughputMonitorChart.addSeries({
			name: "处理完成的包数量",
			data: []
		});
		cityVmMessageThroughputMonitorChartCurrSeries++;
		window.cityVmMessageThroughputMonitorTimer = setTimeout(startMonitoringCityVmMessageThroughput, 0);
	} else {
		//设置button文字
		$(this).text("点击开始监控包吞吐量");
		stopMonitoringCityVmMessageThroughput();
	}
});

function startMonitoringCityVmMessageThroughput() {
	var x = (new Date()).getTime();
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCityVmMessageThroughput",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorCityVmId
		},
		success: function(data) {
			console.log("startMonitoringCityVmMessageThroughput:" + data);
			if (data.receivedMessageNum == window.cityVmMessageThroughputLastReceived && data.finishedMessageNum == window.cityVmMessageThroughputLastFinished) {
				window.cityVmMessageThroughputCounter++;
			}
			else {
				window.cityVmMessageThroughputCounter = 0;
			}
			
			window.cityVmMessageThroughputLastReceived = data.receivedMessageNum;
			window.cityVmMessageThroughputLastFinished = data.finishedMessageNum;
			if (window.cityVmMessageThroughputCounter >= 3) {
				data.receivedMessageNum = 0;
				data.finishedMessageNum = 0;
			}
			var shiftFlag = cityVmMessageThroughputMonitorChart.series[cityVmMessageThroughputMonitorChartCurrSeries - 1].data.length > 100;
			var receivedMessageNumPoint = [x, data.receivedMessageNum];
			var finishedMessageNumPoint = [x, data.finishedMessageNum];

			cityVmMessageThroughputMonitorChart.series[cityVmMessageThroughputMonitorChartCurrSeries - 2].addPoint(receivedMessageNumPoint, false, shiftFlag);
			cityVmMessageThroughputMonitorChart.series[cityVmMessageThroughputMonitorChartCurrSeries - 1].addPoint(finishedMessageNumPoint, false, shiftFlag);

			cityVmMessageThroughputMonitorChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityVmMessageThroughputMonitorChart.redraw();
			if (window.cityVmMessageThroughputMonitorTimer != null) {
				window.cityVmMessageThroughputMonitorTimer = setTimeout(startMonitoringCityVmMessageThroughput, 1000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityVmMessageThroughputMonitorTimer);
			window.cityVmMessageThroughputMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityVmMessageThroughput() {
	console.log("stop timer");
	clearTimeout(window.cityVmMessageThroughputMonitorTimer);
	window.cityVmMessageThroughputMonitorTimer = null;
}

//==========================================================================================

$("body").on("click", "#city-vm-message-process-time-monitor-btn", function() {
	console.log("click btn");
	$el = $(this);
	$el.toggleClass("reading");

	if ($el.hasClass("reading")) {
		//设置button文字
		$(this).text("点击暂停监控包处理平均时长");
		cityVmMessageProcessTimeChart.addSeries({
			name: "所有包处理平均时长",
			data: []
		});
		cityVmMessageProcessTimeChartCurrSeries++;
		cityVmMessageProcessTimeChart.addSeries({
			name: "I包处理平均时长",
			data: []
		});
		cityVmMessageProcessTimeChartCurrSeries++;
		cityVmMessageProcessTimeChart.addSeries({
			name: "U包处理平均时长",
			data: []
		});
		cityVmMessageProcessTimeChartCurrSeries++;
		cityVmMessageProcessTimeChart.addSeries({
			name: "T包处理平均时长",
			data: []
		});
		cityVmMessageProcessTimeChartCurrSeries++;
		window.cityVmMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityVmMessageProcessTime, 0);
	} else {
		//设置button文字
		stopMonitoringCityVmMessageProcessTime();
		$(this).text("点击开始监控包处理平均时长");
	}
});

function startMonitoringCityVmMessageProcessTime() {
	var x = (new Date()).getTime();
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCityVmMessageProcessTime",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorCityVmId
		},
		success: function(data) {
			console.log("startMonitoringCityVmMessageProcessTime:" + data);
			var allMessageProcessTime = data.allMessageProcessTime;
			var messageIProcessTime = data.messageIProcessTime;
			var messageUProcessTime = data.messageUProcessTime;
			var messageTProcessTime = data.messageTProcessTime;
			var shiftFlag = cityVmMessageProcessTimeChart.series[cityVmMessageProcessTimeChartCurrSeries - 1].data.length > 100;
			var allMessageProcessTimePoint = [x, allMessageProcessTime];
			var messageIProcessTimePoint = [x, messageIProcessTime];
			var messageUProcessTimePoint = [x, messageUProcessTime];
			var messageTProcessTimePoint = [x, messageTProcessTime];

			cityVmMessageProcessTimeChart.series[cityVmMessageProcessTimeChartCurrSeries - 4].addPoint(allMessageProcessTimePoint, false, shiftFlag);
			cityVmMessageProcessTimeChart.series[cityVmMessageProcessTimeChartCurrSeries - 3].addPoint(messageIProcessTimePoint, false, shiftFlag);
			cityVmMessageProcessTimeChart.series[cityVmMessageProcessTimeChartCurrSeries - 2].addPoint(messageUProcessTimePoint, false, shiftFlag);
			cityVmMessageProcessTimeChart.series[cityVmMessageProcessTimeChartCurrSeries - 1].addPoint(messageTProcessTimePoint, false, shiftFlag);

			cityVmMessageProcessTimeChart.xAxis[0].setExtremes(x - 200 * 1000, x, false); //100个点 
			cityVmMessageProcessTimeChart.redraw();
			if (window.cityVmMessageProcessTimeMonitorTimer != null) {
				window.cityVmMessageProcessTimeMonitorTimer = setTimeout(startMonitoringCityVmMessageProcessTime, 3000);
			}
		},
		error: function(xhr, status) {
			clearTimeout(window.cityVmMessageProcessTimeMonitorTimer);
			window.cityVmMessageProcessTimeMonitorTimer = null;
			alert(status);
		}
	});
}

function stopMonitoringCityVmMessageProcessTime() {
    console.log("stop cityVmMessageProcessTimeMonitorTimer");
	clearTimeout(window.cityVmMessageProcessTimeMonitorTimer);
	window.cityVmMessageProcessTimeMonitorTimer = null;
}

//==============================================================================
//==============================================================================

$("body").on("click", "#vm-history-message-throughput-monitor-btn", function() {
	$("#vm-history-message-throughput-monitor-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCityVmHistoryMessageThroughput",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorCityVmId,
			date: $("#vm-history-message-throughput-datepicker").val()
		},
		success: function(data) {
		     $("div.loader").shCircleLoader('destroy');
		     $("div.loader").remove();
		     vmHistoryMessageThroughputChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'vm-history-message-throughput-monitor-chart',
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

$("body").on("click", "#vm-history-message-process-time-monitor-btn", function() {
	$("#vm-history-message-process-time-monitor-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getCityVmHistoryMessageProcessTime",
		dataType: "json",
		data: {
			cityVmId: window.curMonitorCityVmId,
			date: $("#vm-history-message-process-time-datepicker").val()
		},
		success: function(data) {
			console.log(data.allMsg);
		     $("div.loader").shCircleLoader('destroy');
		     $("div.loader").remove();
		     vmHistoryMessageProcessTimeChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'vm-history-message-process-time-monitor-chart',
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
