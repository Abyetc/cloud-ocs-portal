//对sidebar的点击行为进行处理
$( ".sidebar-infrastructure-alert" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-infrastructure-alert' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-infrastructure-alert").empty();
  $("ol.breadcrumb.breadcrumb-infrastructure-alert").append("<li><a href='javascript:void(0);' cmd=''><span class='glyphicon glyphicon-tasks'></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-infrastructure-alert").append("<li class='active'>" + secondLevelTitle + "</li>");

  listInfrastructureAlertPoint();
  
  listInfrastructureAlerts();
});

function listInfrastructureAlertPoint() {
	$("#content-area").empty();
	
	$("#content-area").append("<div class='content-header' style='margin-top:40px;'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;报警服务监控点</strong></span></div>");

	$("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-city-btn'><span class='glyphicon glyphicon-plus'></span>  点击添加报警监控点</button>");
	
	var infrastructureAlertPointTable = $("<table class='table table-bordered text-center'>"
			    + "<caption><strong></strong></caption>"
			    + "<thead><tr><th>序号</th><th>对象</th><th>负责人</th><th>创建时间</th><th>查看详情</th><th>删除</th></tr></thead>"
			    + "<tbody></tbody>"
			    + "</table>");
	$("#content-area").append(infrastructureAlertPointTable);
}

function listInfrastructureAlerts() {
	$("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;警报列表</strong></span></div>");
	
	var infrastructureAlertTable = $("<table class='table table-bordered text-center'>"
		    + "<caption><strong></strong></caption>"
		    + "<thead><tr><th>序号</th><th>来源</th><th>描述</th><th>报警时间</th><th>查看详情</th><th>删除</th></tr></thead>"
		    + "<tbody></tbody>"
		    + "</table>");
	$("#content-area").append(infrastructureAlertTable);
}