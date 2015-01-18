/*
  系统前端页面业务服务监控模块-City所属VM监控逻辑入口
  包括：breadcrumb、及相关逻辑
*/

//用于City Vm监控图表的全局变量
var cityVmRequestNumMonitorChart;
var cityVmRequestNumMonitorChartCurrSeries = 0;
var cityVmRxbpsTxbpsMonitorChart;
var cityVmRxbpsTxbpsMonitorChartCurrSeries = 0;

function monitorVM(cityId, networkId, VMId, VMName) {
  var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href='javascript:void(0);' cmd='MonitorNetwork' cityId='" + cityId + "' networkId='" + networkId + "'>" + thirdLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class='active'>" + VMName + "</li>");
  $("#content-area").empty();

  //将当前city vm Id设置为全局变量
  window.curMonitorCityVmId = VMId;

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

  //city vm Rxbps实时监控区域
  $("#content-area").append("<div id='city-vm-rxbps-txbps-monitor-area' style='margin-top:100px;'>"
    +   "<div>"
    +     "<button type='button' class='btn btn-primary btn-sm pull-right' id='city-vm-rxbps-txbps-monitor-btn'>点击开始监控数据吞吐率</button>"
    +   "</div>"
    +   "<div id='city-vm-rxbps-txbps-monitor-chart'></div>"
    + "</div>");
  cityVmRxbpsTxbpsMonitorChart = new Highcharts.Chart({
    chart: {
      renderTo: 'city-vm-rxbps-txbps-monitor-chart',
      type: 'line', //原来是：spline
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10,
      plotBorderWidth: 1
    },
    title: {
      text: VMName + " 接收/发送 数据吞吐率"
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
  cityVmRxbpsTxbpsMonitorChartCurrSeries = 0;
}