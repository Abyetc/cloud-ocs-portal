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
			name: "接收数据速率",
			data: []
		});
		cityVmRxbpsTxbpsMonitorChart.addSeries({
			name: "发送数据速率",
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
		url: "monitor/vm/getCurRxbpsTxbps",
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

//==============================================================================
//==============================================================================

$("body").on("click", "#vm-history-cpu-usage-monitor-btn", function() {
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getVmHistoryCpuUsagePercentage",
		dataType: "json",
		data: {
			vmId: window.curMonitorVmId,
			dayOfMonth: $("#vm-history-cpu-usage-monitor-area select").val()
		},
		success: function(data) {
			 vmHistoryCpuUsagePercentageMonitorChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'vm-history-cpu-usage-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "CPU使用率历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "CPU使用率历史曲线" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 2) + '%';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 100,
				        },
				    exporting: {
				        enabled: false
				      },
				      credits: {
				        enabled: false
				    },
				    series: [{name:"CPU使用率历史曲线", data:data}]
			  });
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
});

$("body").on("click", "#vm-history-memory-usage-monitor-btn", function() {
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getVmHistoryMemoryUsagePercentage",
		dataType: "json",
		data: {
			vmId: window.curMonitorVmId,
			dayOfMonth: $("#vm-history-memory-usage-monitor-area select").val()
		},
		success: function(data) {
			 vmHistoryMemoryUsagePercentageMonitorChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'vm-history-memory-usage-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "内存使用率历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "内存使用率历史曲线" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 2) + '%';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 100,
				        },
				    exporting: {
				        enabled: false
				      },
				      credits: {
				        enabled: false
				    },
				    series: [{name:"内存使用率历史曲线", data:data}]
			  });
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
});

$("body").on("click", "#vm-history-network-usage-monitor-btn", function() {
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/getVmHistoryNetworkUsagePercentage",
		dataType: "json",
		data: {
			vmId: window.curMonitorVmId,
			interfaceName: "eth0",
			dayOfMonth: $("#vm-history-network-usage-monitor-area select").val()
		},
		success: function(data) {
			 vmHistoryNetworkUsagePercentageMonitorChart = new Highcharts.StockChart({
				  chart: {
				      renderTo: 'vm-history-network-usage-monitor-chart',
				      marginRight: 10,
				      plotBorderWidth: 1
				    },
				    title: {
				      text:  "网卡eth0接收/发送数据吞吐率历史曲线"
				    },
				    tooltip: { //鼠标指在线上出现的框
				        formatter: function() {
				          return '<b>' + "速率" + '</b><br/>' +
				            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
				            Highcharts.numberFormat(this.y, 2) + 'KBps';
				        }
				      },
				    yAxis: {
				          min: 0,
				          max: 5000,
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
				    series: [{name:"接收数据速率历史曲线", data:data["rxbps"]},
				             {name:"发送数据速率历史曲线", data:data["txbps"]}]
			  });
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
});