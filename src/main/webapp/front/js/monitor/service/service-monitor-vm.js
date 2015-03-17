/*
  系统前端页面业务服务监控模块-City所属VM监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/

//用于City Vm监控图表的全局变量
var cityVmRequestNumMonitorChart;
var cityVmRequestNumMonitorChartCurrSeries = 0;
var cityVmMessageThroughputMonitorChart;
var cityVmMessageThroughputMonitorChartCurrSeries = 0;
var cityVmMessageProcessTimeChart;
var cityVmMessageProcessTimeChartCurrSeries = 0;
var vmHistoryMessageThroughputChart;
var vmHistoryMessageProcessTimeChart;

function monitorVM(cityId, networkId, VMId, VMName) {
  var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href='javascript:void(0);' cmd='MonitorNetwork' cityId='" + cityId + "' networkId='" + networkId + "'>" + thirdLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class='active'>" + VMName + "</li>");
  $("#content-area").empty();
  
  $("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;实时数据监控</strong></span></div>");

  //将当前city vm Id设置为全局变量
  window.curMonitorCityVmId = VMId;
  window.cityVmMessageThroughputCounter = 0;
  window.cityVmMessageThroughputLastReceived = 0;
  window.cityVmMessageThroughputLastFinished = 0;

  //city vm并发请求数实时监控区域
  $("#content-area").append("<div id='city-vm-request-num-monitor-area' style=''>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-vm-request-num-monitor-btn'>点击开始监控并发请求数</button>"
    +   "</div>"
    +   "<div id='city-vm-request-num-monitor-chart'></div>"
    + "</div>");
  cityVmRequestNumMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-vm-request-num-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: VMName + " 正在处理的并发请求连接数"
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
  cityVmRequestNumMonitorChartCurrSeries = 0;
  
//city vm包吞吐量实时监控区域
  $("#content-area").append("<div id='city-vm-message-throughput-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-vm-message-throughput-monitor-btn'>点击开始监控包吞吐量</button>"
    +   "</div>"
    +   "<div id='city-vm-message-throughput-monitor-chart'></div>"
    + "</div>");
  cityVmMessageThroughputMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-vm-message-throughput-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: VMName + " 包吞吐量"
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
  cityVmMessageThroughputMonitorChartCurrSeries = 0;

   //city vm包处理平均时长实时监控区域
  $("#content-area").append("<div id='city-vm-message-process-time-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-vm-message-process-time-monitor-btn'>点击开始监控包处理平均时长</button>"
    +   "</div>"
    +   "<div id='city-vm-message-process-time-monitor-chart'></div>"
    + "</div>");
  cityVmMessageProcessTimeChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-vm-message-process-time-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: VMName + " 包处理平均时长"
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
  cityVmMessageProcessTimeChartCurrSeries = 0;
  
  //============================================================================================================

  $("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;历史数据监控</strong></span></div>");

  //vm的历史包吞吐量监控区域
  $("#content-area").append("<div id='vm-history-message-throughput-monitor-area' style=''>"
		    +   "<div class='form-group'>"
		    +       "<div class='input-group pull-right' style='width:20%; margin-left:10px;'>"
		    +		   "<input class='form-control' type='text' id='vm-history-message-throughput-datepicker'>"
		    +		"</div>"
		    +   "</div>"
		    +   "<div>"
		    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='vm-history-message-throughput-monitor-btn'>查询包吞吐量历史数据</button>"
		    +   "</div>"
		    +   "<div id='vm-history-message-throughput-monitor-chart'></div>"
		    + "</div>");
  $( "#vm-history-message-throughput-datepicker" ).datepicker();
  
  //vm的历史包处理时长监控区域
  $("#content-area").append("<br /><div id='vm-history-message-process-time-monitor-area' style='margin-top:80px;'>"
		    +   "<div class='form-group'>"
		    +       "<div class='input-group pull-right' style='width:20%; margin-left:10px;'>"
		    +		   "<input class='form-control' type='text' id='vm-history-message-process-time-datepicker'>"
		    +		"</div>"
		    +   "</div>"
		    +   "<div>"
		    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='vm-history-message-process-time-monitor-btn'>查询包处理时长历史数据</button>"
		    +   "</div>"
		    +   "<div id='vm-history-message-process-time-monitor-chart'></div>"
		    + "</div>");
  $( "#vm-history-message-process-time-datepicker" ).datepicker();
}