/*
  系统前端页面业务服务监控模块-City所属network监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/

//用于City Network监控图表的全局变量
var cityNetworkRealtimeSessionNumMonitorChart;
var cityNetworkRealtimeSessionNumMonitorChartCurrSeries = 0;
var cityNetworkMessageProcessTimeMonitorChart;
var cityNetworkMessageProcessTimeMonitorChartCurrSeries = 0;

function monitorCityNetworks(cityId, networkId, networkName) {
	//先处理breadcrumb
	var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
	$("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
	$("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href='javascript:void(0);' cmd='monitorCity' cityId='" + cityId + "'>" + thirdLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class='active'>" + networkName + "</li>");
	$("#content-area").empty();

	//获取该网络所属的Vm列表
	var monitorVMListTable = $("<table class='table table-bordered text-center'>"
	    + "<caption><strong>网络 " + networkName + " 虚拟机列表</strong>"
	    + "<thead><tr><th>序号</th><th>区域</th><th>主机</th>" 
	    + "<th>VM名称</th><th>CPU</th><th>内存</th>"
	    + "<th>状态</th><th>监控</th></tr></thead>"
	    + "<tbody></tbody>"
	    + "</table>");
    $("#content-area").append(monitorVMListTable);

    //转菊花
	$("#content-area").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});
	$.ajax({
		type: "GET",
		url: "business/listOcsVmsByNetwork",
		dataType: "json",
		data: {
			networkId: networkId
		},
		success: function(data) {
			$("div.loader").shCircleLoader('destroy');
			$("div.loader").remove();
			for (var i = 0; i < data.length; i++) {
				var state = "";
				switch (data[i].vmState) {
					case 0:
					case 2:
					case 4:
					case 6:
						state = "<td><span class='label label-warning'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					case 3:
					case 5:
					case 7:
					case 8:
					case 9:
						state = "<td><span class='label label-danger'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					case 1:
						state = "<td><span class='label label-success'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					default:
						break;
				}
				var hostName = (!data[i].hostName ? '-' : data[i].hostName);
				$(".table tbody").append("<tr>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].zoneName + "</td>"
					+ "<td>" + hostName + "</td>"
					+ "<td>" + data[i].vmName + "</td>"					
					+ "<td>" + data[i].cpuNum + "*" + data[i].cpuSpeed + "</td>"
					+ "<td>" + data[i].memory + "</td>"
					+ state
					+ "<td>" + "<button type='button' class='btn btn-xs btn-primary' onclick=\"monitorVM(" + cityId + ",'" + data[i].networkId + "','" + data[i].vmId + "','" + data[i].vmName + "');\">" + "监控</button>" + "</td>"
					+ "</tr>");
			}
		},
		error: function(xhr, status) {
				$("div.loader").shCircleLoader('destroy');
				$("div.loader").remove();
				alert(status);
		}
	});

	//将当前city network Id设置为全局变量
	window.curMonitorCityNetworkId = networkId;

	//city vm并发请求数实时监控区域
  $("#content-area").append("<div id='city-network-realtime-session-num-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-network-realtime-session-num-monitor-btn'>点击开始监控实时会话数</button>"
    +   "</div>"
    +   "<div id='city-network-realtime-session-num-monitor-chart'></div>"
    + "</div>");
  cityNetworkRealtimeSessionNumMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-network-realtime-session-num-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: networkName + " 实时会话数"
    },
    xAxis: {
      type: 'datetime',
      // tickPixelInterval: 5,
      // tickLength: 20,
      tickInterval: 10 * 1000, //十秒钟一个间隔
    },
    yAxis: {
      min: 0,
      max: 200,
      title: {
        text: '实时会话数(个)'
      },
      plotLines: [{
        value: 0,
        width: 1,
        color: '#808080'
      }]
    },
    tooltip: { //鼠标指在线上出现的框
      formatter: function() {
        return '<b>' + this.series.name + '</b><br/>' +
          Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
          Highcharts.numberFormat(this.y, 0);
      }
    },
    legend: {
      enabled: false
    },
    exporting: {
      enabled: false
    },
    credits: {
      enabled: false
    },
    series: []
  });
  cityNetworkRealtimeSessionNumMonitorChartCurrSeries = 0;

  //city network包处理平均时长实时监控区域
  $("#content-area").append("<div id='city-network-message-process-time-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-network-message-process-time-monitor-btn'>点击开始监控包处理平均时长</button>"
    +   "</div>"
    +   "<div id='city-network-message-process-time-monitor-chart'></div>"
    + "</div>");
  cityNetworkMessageProcessTimeMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-network-message-process-time-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: networkName + " 包处理平均时长"
    },
    xAxis: {
      type: 'datetime',
      // tickPixelInterval: 5,
      // tickLength: 20,
      tickInterval: 10 * 1000, //十秒钟一个间隔
      dateTimeLabelFormats: { 
        day: '%H:%M'
      }
    },
    yAxis: {
      min: 0,
      max: 1000,
      title: {
        text: '时长(ms)'
      },
      plotLines: [{
        value: 0,
        width: 1,
        color: '#808080'
      }]
    },
    tooltip: { //鼠标指在线上出现的框
      formatter: function() {
        return '<b>' + this.series.name + '</b><br/>' +
          Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
          Highcharts.numberFormat(this.y, 2);
      }
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
    series: []
  });
  cityNetworkMessageProcessTimeMonitorChartCurrSeries = 0;
}