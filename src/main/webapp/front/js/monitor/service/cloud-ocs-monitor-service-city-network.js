/*
  系统前端页面业务服务监控模块-City所属network监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/

//用于City Network监控图表的全局变量
var cityNetworkRequestNumMonitorChart;
var cityNetworkRequestNumMonitorChartCurrSeries = 0;
var cityNetworkRxbpsTxbpsMonitorChart;
var cityNetworkRxbpsTxbpsMonitorChartCurrSeries = 0;

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
				$(".table tbody").append("<tr>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].zoneName + "</td>"
					+ "<td>" + data[i].hostName + "</td>"
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
  $("#content-area").append("<div id='city-network-request-num-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-network-request-num-monitor-btn'>点击开始监控并发请求数</button>"
    +   "</div>"
    +   "<div id='city-network-request-num-monitor-chart'></div>"
    + "</div>");
  cityNetworkRequestNumMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-network-request-num-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: networkName + " 正在处理的并发请求连接数"
    },
    xAxis: {
      type: 'datetime',
      // tickPixelInterval: 5,
      // tickLength: 20,
      tickInterval: 10 * 1000, //十秒钟一个间隔
    },
    yAxis: {
      min: 0,
      max: 100,
      title: {
        text: '计费请求数量(个)'
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
  cityNetworkRequestNumMonitorChartCurrSeries = 0;

  //city network数据吞吐率实时监控区域
  $("#content-area").append("<div id='city-network-rxbps-txbps-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-network-rxbps-txbps-monitor-btn'>点击开始监控数据吞吐率</button>"
    +   "</div>"
    +   "<div id='city-network-rxbps-txbps-monitor-chart'></div>"
    + "</div>");
  cityNetworkRxbpsTxbpsMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-network-rxbps-txbps-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: networkName + " 接收/发送 数据吞吐率"
    },
    xAxis: {
      type: 'datetime',
      // tickPixelInterval: 5,
      // tickLength: 20,
      tickInterval: 10 * 1000, //十秒钟一个间隔
    },
    yAxis: {
      min: 0,
      max: 1500,
      title: {
        text: '接收/发送数据吞吐率(KBps)'
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
  cityNetworkRxbpsTxbpsMonitorChartCurrSeries = 0;
}