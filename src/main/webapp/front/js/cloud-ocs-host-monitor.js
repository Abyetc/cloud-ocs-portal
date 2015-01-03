/*
  系统前端页面物理资源监控模块-Host监控逻辑入口
  包括：slide、breadcrumb、及一些简单逻辑
*/

//对breadcrumb的点击行为进行处理
$("body").on("click", 'ol.breadcrumb.breadcrumb-ocs-host-monitor li a', function() {
  var cmd = $(this).attr("cmd");
  if (cmd == "monitorHostsHome") {
    $(this).parent().next().nextAll().remove();
    $(this).parent().next().addClass("active");
    $(this).parent().next().text($(this).parent().next().text());
    listHostMonitorZones();
  }
  if (cmd == "listHostMonitorZones") {
    $(this).parent().nextAll().remove();
    $(this).parent().addClass("active");
    $(this).parent().text($(this).text());
    listHostMonitorZones();
  }
  if (cmd == "listMonitorHosts") {
    $(this).parent().nextAll().remove();
    var zoneName = $(this).parent().text();
    var zoneId = $(this).attr("zoneId");
    $(this).parent().remove();
    listMonitorHosts(zoneId, zoneName);
  }
  if (cmd == "listMonitorVms") {
    $(this).parent().nextAll().remove();
    var zoneId = $(this).attr("zoneId");
    var hostId = $(this).attr("hostId");
    var nameAndIp = $(this).text();
    var hostName = nameAndIp.substring(0, nameAndIp.indexOf('('));
    var hostIp = nameAndIp.substring(nameAndIp.indexOf('(') + 1, nameAndIp.indexOf(')'));
    $(this).parent().remove();
    listVMsOnHost(zoneId, hostId, hostName, hostIp);
  }
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-ocs-host-monitor" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-ocs-host-monitor' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").empty();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li><a href='javascript:void(0);' cmd='monitorHostsHome'><span class='glyphicon glyphicon-eye-open'></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class='active'>" + secondLevelTitle + "</li>");

  listHostMonitorZones();
});

function listHostMonitorZones() {
  $("#content-area").empty();

  var zoneListTable = $("<table class='table table-bordered text-center'>"
    + "<caption><strong>系统区域（机房）列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>区域（机房）名称</th><th>网络类型</th><th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");  
  $("#content-area").append(zoneListTable);
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });
  //请求获取区域列表数据
  $.ajax({
    type: "GET",
    url: "resource/infrastructure/listZones",
    dataType: "json",
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        $(".table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].zoneName + "</td><td>" + data[i].networkType + "</td>"
          + (data[i].allocationState == "Enabled" ? "<td><span class='label label-success'>Enabled</span></td>" : "<td><span class='label label-danger'>Disabled</span></td>")
          + "<td><button type='button' class='btn btn-primary btn-xs' onclick=\"listMonitorHosts('" + data[i].zoneId + "','" + data[i].zoneName + "');\">点击进入</button></td></tr>");
      }
    },
    error: function( xhr, status ) {
      alert(status); 
    } 
  });
}

function listMonitorHosts(zoneId, zoneName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href='javascript:void(0);' cmd='listHostMonitorZones'>" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class='active'>" + zoneName + "</li>");
  
  $("#content-area").empty();

  var monitorHostListTable = $("<table class='table table-bordered text-center'>"
    + "<caption><strong>区域" + zoneName + "主机列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>提供点名称</th><th>集群名称</th>" 
    + "<th>主机名称</th><th>主机IP</th>"
    + "<th>状态</th><th>详细信息</th><th>VM</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(monitorHostListTable);
  //转菊花
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });
  //请求数据
  $.ajax({
    type: "GET",
    url: "monitor/host/listHostDetail",
    dataType: "json",
    data: {
      zoneId: zoneId 
    },
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        $(".table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].podName + "</td>"
          + "<td>" + data[i].clusterName + "</td><td>" + data[i].hostName + "</td><td>" + data[i].ipAddress + "</td>"
          + (data[i].state == "Up" ? "<td><span class='label label-success'>Up</span></td>" : "<td><span class='label label-danger'>"+ data[i].state + "</span></td>")
          + "<td><button type='button' class='btn btn-xs btn-primary host-detail-btn-" + i + "'>查看</button></td>"
          + "<td><button type='button' class='btn btn-xs btn-link' onclick=\"listVMsOnHost('" + data[i].zoneId + "','" + data[i].hostId + "','" + data[i].hostName + "','" + data[i].ipAddress + "')\">" + data[i].vmNumOnHost + "</button></td>"
          + "</tr>");
        $(".table tbody button.btn.btn-xs.btn-primary.host-detail-btn-" + i).on("click", {hostDetail: data[i]}, monitorHostDetail);
      }
    },
    error: function( xhr, status ) {
      alert(status); 
    } 
  });
}

//==================================================================================================
//以下是主机详情和监控主机的页面逻辑实现

//用于Host监控图表的全局变量
var hostCpuUsageMonitorChart;
var hostCpuUsageMonitorChartCurrSeries = 0;
var hostMemoryUsageMonitorChart;
var hostMemoryUsageMonitorChartCurrSeries = 0;

//获取主机详细信息
function monitorHostDetail(event) {
  var hostDetail = event.data.hostDetail;
  var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href='javascript:void(0);' cmd='listMonitorHosts' zoneId='" + hostDetail.zoneId + "'>" + thirdLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class='active'>" + hostDetail.hostName + "(" + hostDetail.ipAddress + ")" + "</li>");
  $("#content-area").empty();

  var hostDetailTable = $("<table class='table table-hover text-left-table'>"
    + "<caption><strong>主机" + hostDetail.hostName + "详细信息</strong></caption>"
    + "</table>");
  $("#content-area").append(hostDetailTable);

  //插入主机详细信息
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>主机名称</td><td>" + hostDetail.hostName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>主机IP</td><td>" + hostDetail.ipAddress + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>状态</td>" + (hostDetail.state == "Up" ? "<td><span class='label label-success'>Up</span></td>" : "<td><span class='label label-danger'>Down</span></td>") + "</tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>虚拟机管理程序</td><td>" + hostDetail.hypervisor + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>提供点名称</td><td>" + hostDetail.podName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>集群名称</td><td>" + hostDetail.clusterName + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>CPU总量</td><td>" + hostDetail.cpuNum + "*" + hostDetail.cupSpeed + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>已分配给VM的CPU</td><td><div style='margin-bottom:7px;'>" + hostDetail.cpuAllocated + "</div>"
    + "<div class='progress'>"
    +   "<div class='progress-bar progress-bar-danger' role='progressbar'"
    +      "aria-valuenow='60' aria-valuemin='0' aria-valuemax='100'"
    +      "style='width: " + hostDetail.cpuAllocated + ";'>"
    +     "<span class=''>已分配(" + hostDetail.cpuAllocated + ")</span>"
    +  "</div>"
    +"</div>"
    + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>内存总量</td><td>" + hostDetail.memoryTotal + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>已分配的内存</td><td><div style='margin-bottom:7px;'>" + hostDetail.memoryAllocatedPercentage + "</div>"
    + "<div class='progress'>"
    +   "<div class='progress-bar progress-bar-danger' role='progressbar'"
    +      "aria-valuenow='60' aria-valuemin='0' aria-valuemax='100'"
    +      "style='width: " + hostDetail.memoryAllocatedPercentage + ";'>"
    +     "<span class=''>已分配(" + hostDetail.memoryAllocatedPercentage + ")</span>"
    +  "</div>"
    +"</div>"
    + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>网络读取量</td><td>" + hostDetail.networkRead + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>网络写入量</td><td>" + hostDetail.networkWrite + "</td></tr>");

  //将当前host Id设置为全局变量
  window.curMonitorHostId = hostDetail.hostId;

  //主机CPU使用率实时监控区域
  $("#content-area").append("<div id='host-cpu-usage-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='host-cpu-usage-monitor-btn'>点击开始监控CPU使用情况</button>"
    +   "</div>"
    +   "<div id='host-cpu-usage-monitor-chart'></div>"
    + "</div>");
  hostCpuUsageMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'host-cpu-usage-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: "主机" + hostDetail.hostName + $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text() + " CPU使用率实时曲线"
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
  hostCpuUsageMonitorChartCurrSeries = 0;


  //主机内存使用率实时监控区域
  $("#content-area").append("<div id='host-memory-usage-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='host-memory-usage-monitor-btn'>点击开始监控内存使用情况</button>"
    +   "</div>"
    +   "<div id='host-memory-usage-monitor-chart'></div>"
    + "</div>");
  hostMemoryUsageMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'host-memory-usage-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: "主机" + hostDetail.hostName + $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text() + " 内存使用情况实时曲线"
    },
    xAxis: {
      type: 'datetime',
      tickInterval: 10 * 1000, //十秒钟一个间隔
    },
    yAxis: {
      min: 0,
      max: parseFloat(hostDetail.memoryTotal.slice(0, hostDetail.memoryTotal.indexOf(' '))),
      title: {
        text: '内存使用情况(GB)'
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
          Highcharts.numberFormat(this.y, 6) + 'GB';
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
  hostMemoryUsageMonitorChartCurrSeries = 0;
}