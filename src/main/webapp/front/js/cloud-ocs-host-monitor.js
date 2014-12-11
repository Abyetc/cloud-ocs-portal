$("body").on("click", 'ol.breadcrumb.breadcrumb-ocs-host-monitor li a', function(){
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
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-ocs-host-monitor" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id=\"content-area\"></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class=\"breadcrumb breadcrumb-ocs-host-monitor\"></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").empty();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li><a href=\"javascript:void(0);\" cmd=\"monitorHostsHome\"><span class=\"glyphicon glyphicon-eye-open\"></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class=\"active\">" + secondLevelTitle + "</li>");

  listHostMonitorZones();
});

function listHostMonitorZones() {
  $("#content-area").empty();

  var zoneListTable = $("<table class=\"table table-bordered text-center\">"
    + "<caption>系统区域（机房）列表</caption>"
    + "<thead><tr><th>序号</th><th>区域（机房）名称</th><th>网络类型</th><th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");  
  $("#content-area").append(zoneListTable);
  //请求获取区域列表数据
  $(".table tbody").append("<tr><td>1</td><td>zoneName</td><td>高级网络</td>"
    + "<td><span class=\"label label-success\">成功标签</span></td>"
    + "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listMonitorHosts('zoneId', 'zoneName');\">点击监控</button></td></tr>");
}

function listMonitorHosts(zoneId, zoneName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listHostMonitorZones\">" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class=\"active\">" + zoneName + "</li>");
  
  $("#content-area").empty();

  var monitorHostListTable = $("<table class=\"table table-bordered text-center\">"
    + "<caption>区域" + zoneName + "主机列表"
    + "<thead><tr><th>序号</th><th>提供点名称</th><th>集群名称</th>" 
    + "<th>主机名称</th><th>主机IP</th><th>CPU</th><th>内存容量</th>"
    + "<th>状态</th><th>监控</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(monitorHostListTable);
  $(".table tbody").append("<tr><td>1</td><td>Pod Name</td><td>Cluster Name</td>"
    + "<td>Host Name</td><td>192.168.0.3</td><td>4*2.5GHz</td><td>4.00GB</td>"
    + "<td><span class=\"label label-success\">标签</span></td>"
    + "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"monitorHost('ZoneId', 'HostId', 'HostName', '192.168.0.3');\">监控</button></td></tr>");
}

function monitorHost(zoneId, hostId, hostName, hostIP) {
  var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").append("<a href=\"javascript:void(0);\" cmd=\"listMonitorHosts\" zoneId=\"" + zoneId + "\">" + thirdLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-host-monitor").append("<li class=\"active\">" + hostName + "(" + hostIP + ")" + "</li>");
  $("#content-area").empty();
}