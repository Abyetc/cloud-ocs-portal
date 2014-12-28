//对sidebar的点击行为进行处理
$( ".sidebar-cloud-ocs-service" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-ocs-service' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this);
  curActiveItem.addClass("active");
  var firstLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-ocs-service").empty();
  $("ol.breadcrumb.breadcrumb-ocs-service").append("<li class='active'><span class='glyphicon glyphicon-cloud'></span>&nbsp;" + firstLevelTitle + "</li>");

  listCloudOCSServiceCities();
});

function listCloudOCSServiceCities() {
  $("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right'><span class='glyphicon glyphicon-plus'></span>  点击新增服务城市</button>");
  var cloudOCSServiceCitiesTable = $("<table class='table table-bordered text-center'>"
    + "<caption>云在线计费系统服务城市列表</caption>"
    + "<thead><tr><th>序号</th><th>城市名称</th><th>说明</th><th>状态</th><th>查看详情</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cloudOCSServiceCitiesTable);

  $(".table tbody").append("<tr><td>1</td><td>广州</td><td>100</td>"
    + "<td><span class='label label-success'>成功标签</span></td>"
    + "<td><button type='button' class='btn btn-primary btn-xs' onclick=';'>点击查看</button></td></tr>");
}