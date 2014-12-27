/*
  系统前端页面物理资源监控模块-VM监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/


//==================================================================================================
//以下是从主机列表入口进来的，主机上的VM列表、VM详情和监控VM的页面逻辑实现

function listVMsOnHost(zoneId, hostId, hostName, hostIpAddress) {
	var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listMonitorHosts\" zoneId=\"" + zoneId + "\">" + thirdLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class=\"active\">" + hostName + "(" + hostIpAddress + ")" + "</li>");
	$("#content-area").empty();

	var monitorVmsOnHostListTable = $("<table class=\"table table-bordered text-center\">" 
		+ "<caption><strong>主机" + hostName + " VM列表</strong></caption>" 
		+ "<thead><tr><th>序号</th><th>名称</th><th>内部名称</th>" 
		+ "<th>状态</th><th>详细信息</th></tr></thead>" 
		+ "<tbody></tbody>" 
		+ "</table>");
	$("#content-area").append(monitorVmsOnHostListTable);

	//转菊花
	$("#content-area").append("<div class=\"loader\"></div>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});
	//请求数据
	$.ajax({
		type: "GET",
		url: "monitor/vm/listVmDetail",
		dataType: "json",
		data: {
			hostId: hostId
		},
		success: function(data) {
			$("div.loader").shCircleLoader('destroy');
			$("div.loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table tbody").append("<tr><td>" + (i+1) + "</td>"
					+ "<td>" + data[i].vmName + "</td>"
					+ "<td>" + data[i].instanceName + "</td>"
					+ (data[i].state == "Running" ? "<td><span class=\"label label-success\">Running</span></td>" : "<td><span class=\"label label-danger\">Stoped</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-xs btn-primary vm-detail-btn-" + i + "\">点击查看</button></td>"
					+ "</tr>"
				);
				$(".table tbody button.btn.btn-xs.btn-primary.vm-detail-btn-" + i).on("click", {vmDetail: data[i]}, monitorVmDetail);
			}
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
}

//用于VM监控图表的全局变量
var vmCpuUsageMonitorChart;
var vmCpuUsageMonitorChartCurrSeries = 0;

function monitorVmDetail(event) {
	var vmDetail = event.data.vmDetail;
	var fourthLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listMonitorVms\" zoneId=\"" + vmDetail.zoneId + "\" hostId=\"" + vmDetail.hostId + "\">" + fourthLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class=\"active\">" + vmDetail.vmName + "</li>");
	$("#content-area").empty();

	var vmDetailTable = $("<table class=\"table table-hover text-left-table\">"
    + "<caption><strong>VM " + vmDetail.vmName + "详细信息</strong></caption>"
    + "</table>");
  $("#content-area").append(vmDetailTable);

  //插入VM详细信息
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">VM名称</td><td>" + vmDetail.vmName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">内部名称</td><td>" + vmDetail.instanceName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">状态</td>" + (vmDetail.state == "Running" ? "<td><span class=\"label label-success\">Running</span></td>" : "<td><span class=\"label label-danger\">Stoped</span></td>") + "</tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">模板名称</td><td>" + vmDetail.templateName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">虚拟机管理程序</td><td>" + vmDetail.hypervisor + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">创建时间</td><td>" + vmDetail.created + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">网络名称</td><td>" + vmDetail.networkName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">IP地址</td><td>" + vmDetail.ipAddress + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">VLAN</td><td>" + vmDetail.isolationUri + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">CPU总量</td><td>" + vmDetail.cpuNum + " * " + vmDetail.cupSpeed + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">内存总量</td><td>" + vmDetail.memory + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">网络读取量</td><td>" + vmDetail.networkRead + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">网络写入量</td><td>" + vmDetail.networkWrite + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">磁盘读取量</td><td>" + vmDetail.diskRead + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">磁盘写入量</td><td>" + vmDetail.diskWrite + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">磁盘IO读取量(个)</td><td>" + vmDetail.diskIORead + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class=\"table-left-head\">磁盘IO写入量(个)</td><td>" + vmDetail.diskIOWrite + "</td></tr>");

  //将当前vm Id设置为全局变量
  window.curMonitorVmId = vmDetail.vmId;

  $("#content-area").append("<div id=\"vm-cpu-usage-monitor-area\" style=\"margin-top:100px;\">"
	+   "<div>"
	+     "<button type=\"button\" class=\"btn btn-primary btn-sm pull-right\" id=\"vm-cpu-usage-monitor-btn\">点击开始监控CPU使用情况</button>"
	+   "</div>"
	+   "<div id=\"vm-cpu-usage-monitor-chart\"></div>"
	+ "</div>");
	vmCpuUsageMonitorChart = new Highcharts.Chart({
		chart: {
			renderTo: 'vm-cpu-usage-monitor-chart',
			type: 'line', //原来是：spline
			animation: Highcharts.svg, // don't animate in old IE
			marginRight: 10,
			plotBorderWidth: 1
		},
		title: {
			text: "VM " + vmDetail.vmName + $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text() + " CPU使用率实时曲线"
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
				text: 'CPU使用率(%)'
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
	vmCpuUsageMonitorChartCurrSeries = 0;
}