/*
  系统前端页面业务服务监控模块-City监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/

$("body").on("click", 'ol.breadcrumb.breadcrumb-ocs-vm-monitor li a', function(){
  var cmd = $(this).attr("cmd");
  if (cmd == "MonitorCityVMsHome") {
    $(this).parent().next().nextAll().remove();
    $(this).parent().next().addClass("active");
    $(this).parent().next().text($(this).parent().next().text());
    listServiceMonitorCities();
  }
  if (cmd == "listServiceMonitorCities") {
    $(this).parent().nextAll().remove();
    $(this).parent().addClass("active");
    $(this).parent().text($(this).text());
    listServiceMonitorCities();
  }
  if (cmd == "monitorCity") {
    $(this).parent().nextAll().remove();
    var cityName = $(this).parent().text();
    var cityId = $(this).attr("cityId");
    $(this).parent().remove();
    monitorCity(cityId, cityName);
  }
  if (cmd == "MonitorNetwork") {
    $(this).parent().nextAll().remove();
    var networkName = $(this).parent().text();
    var networkId = $(this).attr("networkId");
    var cityId = $(this).attr("cityId");
    $(this).parent().remove();
    monitorCityNetworks(cityId, networkId, networkName);
  }
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-ocs-vm-monitor" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-ocs-vm-monitor' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").empty();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li><a href='javascript:void(0);' cmd='MonitorCityVMsHome'><span class='glyphicon glyphicon-eye-open'></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class='active'>" + secondLevelTitle + "</li>");

  listServiceMonitorCities();
});

function listServiceMonitorCities() {
  $("#content-area").empty();

  var cloudOCSServiceCitiesTable = $("<table class='table table-bordered text-center'>"
    + "<caption><strong>云在线计费系统服务城市列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>城市名称</th><th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cloudOCSServiceCitiesTable);

  $("#content-area").append(cloudOCSServiceCitiesTable);
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });

  //请求获取服务城市列表数据
  $.ajax({
    type: "GET",
    url: "business/listCities",
    dataType: "json",
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        var state = "";
        switch (data[i].state) {
          case 0:
            state = "<td><span class='label label-default'>无服务</span></td>";
            break;
          case 1:
            state = "<td><span class='label label-success'>服务中</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>发生故障</span></td>";
            break;
          default:
            break;
        }
        var monitorBtnDisable = "";
        if (data[i].state != 1) {
          monitorBtnDisable = "disabled";
        }
        $(".table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].name + "</td>" 
          + state
          + "<td><button type='button' class='btn btn-primary btn-xs " + monitorBtnDisable + "'"+ " onclick=\"monitorCity(" + data[i].id + ",'" + data[i].name + "');\">" + "点击监控</button></td></tr>");
      }
    },
    error: function(xhr, status) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      alert(status);
    }
  });
}

//用于City监控图表的全局变量
var cityRealtimeSessionNumMonitorChart;
var cityRealtimeSessionNumMonitorChartCurrSeries = 0;
var cityMessageThroughputChart;
var cityMessageThroughputChartCurrSeries = 0;
var cityMessageProcessTimeChart;
var cityMessageProcessTimeChartCurrSeries = 0;
var cityHistorySessionNumChart;
var cityHistoryMessageThroughputChart;
var cityHistoryMessageProcessTimeChart;

function monitorCity(cityId, cityName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href='javascript:void(0);' cmd='listServiceMonitorCities'>" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class='active'>" + cityName + "</li>");
  
  $("#content-area").empty();

  //获取城市所属网络列表
  var cityNetworksTable = $("<table class='table table-bordered text-center' id='city-network-table'>"
    + "<caption><strong>" + cityName + " 服务网络列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>网络名称</th><th>公共IP</th>"
    + "<th>状态</th><th>VM数量</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cityNetworksTable);
  //转菊花
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });
  $.ajax({
    type: "GET",
    url: "business/listCityNetworks",
    dataType: "json",
    data: {
      cityId: cityId
    },
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        var state = "";
        switch (data[i].networkState) {
          case 0:
            state = "<td><span class='label label-default'>Allocated</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>Implementing</span></td>";
            break;
          case 3:
            state = "<td><span class='label label-success'>Implemented</span></td>";
            break;
          default:
            break;
        }
        var publicIp = (!data[i].publicIp ? '-' : data[i].publicIp);
        $("#city-network-table").append("<tr>" 
          + "<td>" + (i+1) + "</td>"
          + "<td>" + data[i].networkName + "</td>"
          + "<td>" + publicIp + "</td>"
          + state
          + "<td>" + data[i].vmNum + "</td>"
          + "<td><button type='button' class='btn btn-xs btn-primary' onclick=\"monitorCityNetworks(" + cityId + ",'" + data[i].networkId + "','" + data[i].networkName + "')\";>" + "点击监控</button></td>"
          + "</tr>");
      }
    },
    error: function(xhr, status) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
    }
  });
  
  $("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;实时数据监控</strong></span></div>");

  //将当前city Id设置为全局变量
  window.curMonitorCityId = cityId;
  window.cityMessageThroughputCounter = 0;
  window.cityMessageThroughputLastReceived = 0;
  window.cityMessageThroughputLastFinished = 0;

  //city实时会话数监控区域
  $("#content-area").append("<div id='city-realtime-session-num-monitor-area' style=''>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-realtime-session-num-monitor-btn'>点击开始监控实时会话数</button>"
    +   "</div>"
    +   "<div id='city-realtime-session-num-monitor-chart'></div>"
    + "</div>");
  cityRealtimeSessionNumMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-realtime-session-num-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: cityName + " 实时会话数"
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
  cityRealtimeSessionNumMonitorChartCurrSeries = 0;
  
  //city 包吞吐量实时监控区域
  $("#content-area").append("<div id='city-message-throughput-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-message-throughput-monitor-btn'>点击开始监控包吞吐量</button>"
    +   "</div>"
    +   "<div id='city-message-throughput-monitor-chart'></div>"
    + "</div>");
  cityMessageThroughputChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-message-throughput-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: cityName + " 包吞吐量"
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
        text: '包数量(个)'
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
  cityMessageThroughputChartCurrSeries = 0;

  //city 包处理时长实时监控区域
  $("#content-area").append("<div id='city-message-process-time-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-message-process-time-monitor-btn'>点击开始监控包处理平均时长</button>"
    +   "</div>"
    +   "<div id='city-message-process-time-monitor-chart'></div>"
    + "</div>");
  cityMessageProcessTimeChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-message-process-time-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: cityName + " 包处理平均时长"
    },
    xAxis: {
      type: 'datetime',
      // tickPixelInterval: 5,
      // tickLength: 20,
      tickInterval: 10 * 1000, //十秒钟一个间隔
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
  cityMessageProcessTimeChartCurrSeries = 0;
  
  //============================================================================================================

  $("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;历史数据监控</strong></span></div>");

  //city的历史会话数监控区域
  $("#content-area").append("<div id='city-history-session-num-monitor-area' style=''>"
		    +   "<div class='form-group'>"
		    +       "<div class='input-group pull-right' style='width:20%; margin-left:10px;'>"
		    +		   "<input class='form-control' type='text' id='city-history-session-num-datepicker'>"
		    +		"</div>"
		    +   "</div>"
		    +   "<div>"
		    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-history-session-num-monitor-btn'>查询历史会话数</button>"
		    +   "</div>"
		    +   "<div id='city-history-session-num-monitor-chart'></div>"
		    + "</div>");
  $( "#city-history-session-num-datepicker" ).datepicker();
  
  //city的历史包吞吐量监控区域
  $("#content-area").append("<br /><div id='city-history-message-throughput-monitor-area' style='margin-top:80px;'>"
		    +   "<div class='form-group'>"
		    +       "<div class='input-group pull-right' style='width:20%; margin-left:10px;'>"
		    +		   "<input class='form-control' type='text' id='city-history-message-throughput-datepicker'>"
		    +		"</div>"
		    +   "</div>"
		    +   "<div>"
		    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-history-message-throughput-monitor-btn'>查询包吞吐量历史数据</button>"
		    +   "</div>"
		    +   "<div id='city-history-message-throughput-monitor-chart'></div>"
		    + "</div>");
  $( "#city-history-message-throughput-datepicker" ).datepicker();
  
  //city的历史包处理时长监控区域
  $("#content-area").append("<br /><div id='city-history-message-process-time-monitor-area' style='margin-top:80px;'>"
		    +   "<div class='form-group'>"
		    +       "<div class='input-group pull-right' style='width:20%; margin-left:10px;'>"
		    +		   "<input class='form-control' type='text' id='city-history-message-process-time-datepicker'>"
		    +		"</div>"
		    +   "</div>"
		    +   "<div>"
		    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-history-message-process-time-monitor-btn'>查询包处理时长历史数据</button>"
		    +   "</div>"
		    +   "<div id='city-history-message-process-time-monitor-chart'></div>"
		    + "</div>");
  $( "#city-history-message-process-time-datepicker" ).datepicker();

}