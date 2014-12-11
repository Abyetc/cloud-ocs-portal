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
  if (cmd == "listCityVms") {
    $(this).parent().nextAll().remove();
    var cityName = $(this).parent().text();
    var cityId = $(this).attr("cityId");
    $(this).parent().remove();
    listCityVms(cityId, cityName);
  }
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-ocs-vm-monitor" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id=\"content-area\"></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class=\"breadcrumb breadcrumb-ocs-vm-monitor\"></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").empty();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li><a href=\"javascript:void(0);\" cmd=\"MonitorCityVMsHome\"><span class=\"glyphicon glyphicon-eye-open\"></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class=\"active\">" + secondLevelTitle + "</li>");

  listServiceMonitorCities();
});

function listServiceMonitorCities() {
  $("#content-area").empty();

  var cloudOCSServiceCitiesTable = $("<table class=\"table table-bordered text-center\">"
    + "<caption>云在线计费系统服务城市列表</caption>"
    + "<thead><tr><th>序号</th><th>城市名称</th><th>说明</th><th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cloudOCSServiceCitiesTable);

  $(".table tbody").append("<tr><td>1</td><td>广州</td><td>100</td>"
    + "<td><span class=\"label label-success\">成功标签</span></td>"
    + "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listCityVms('cityId', 'cityName');\">点击监控</button></td></tr>");
}

function listCityVms(cityId, cityName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listServiceMonitorCities\">" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class=\"active\">" + cityName + "</li>");
  
  $("#content-area").empty();

  var monitorVMListTable = $("<table class=\"table table-bordered text-center\">"
    + "<caption>城市" + cityName + "虚拟机列表"
    + "<thead><tr><th>序号</th><th>区域</th><th>提供点</th><th>集群</th>" 
    + "<th>名称</th><th>IP</th><th>CPU</th><th>内存</th>"
    + "<th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(monitorVMListTable);
  $(".table tbody").append("<tr><td>1</td><td>Zone Name</td><td>Pod Name</td><td>Cluster Name</td>"
    + "<td>Host Name</td><td>192.168.0.3</td><td>4*2.5GHz</td><td>4.00GB</td>"
    + "<td><span class=\"label label-success\">标签</span></td>"
    + "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"monitorVM('cityId', 'VMId', 'VMName', '192.168.0.33');\">监控</button></td></tr>");
}

function monitorVM(cityId, VMId, VMName, VMIP) {
  var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listCityVms\" cityId=\"" + cityId + "\">" + thirdLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-vm-monitor").append("<li class=\"active\">" + VMName + "(" + VMIP + ")" + "</li>");
  $("#content-area").empty();

  var VMCPUMonitorLineOption = {
  chart: {
    type: 'spline',
    animation: Highcharts.svg, // don't animate in old IE
    marginRight: 10,
    plotBorderWidth: 1,
    events: {
      load: function() {
          // set up the updating of the chart each second
          var series = this.series[0];
          //setInterval(function() {
          //    var x = (new Date()).getTime(), // current time
          //        y = Math.random();  
          //    series.addPoint([x, y], true, true);
          //  }, 1000);
          var getMonitorVMCPUData = function() {
             var x = (new Date()).getTime(), // current time
                 y = Math.random(); 
             series.addPoint([x, y], true, true);
          };
          getMonitorVMCPUData();
          setInterval(getMonitorVMCPUData, 1000);
        }
      }
  },
  title: {
    text: "VM " + $("ol.breadcrumb.breadcrumb-ocs-vm-monitor li.active").text() + " CPU使用率实时曲线"
  },
  xAxis: {
    type: 'datetime',
    tickPixelInterval: 150
  },
  yAxis: {
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
      return '<b>'+ this.series.name +'</b><br/>'+
      Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
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
    enabled:false
  },
  series: [{
    name: 'CPU Usage Percentage',
    data: (function() {
          // generate an array of random data
          var data = [],
          time = (new Date()).getTime(),
          i;

          for (i = -19; i <= 0; i++) {
            data.push({
              x: time + i * 1000,
              y: Math.random()
            });
          }
          return data;
        })()
      }]
};

  $("#content-area").append("<div class=\"vm-cpu-monitor-line\"></div>");
  $(".vm-cpu-monitor-line").highcharts(VMCPUMonitorLineOption);
}