/*
  系统前端页面资源管理模块-基础设施 逻辑入口
  包括：slide、breadcrumb、及所有逻辑
*/

//对breadcrumb的点击行为进行处理
$("body").on("click", 'ol.breadcrumb.breadcrumb-resource-infrastructure li a', function(){
	var cmd = $(this).attr("cmd");
	if (cmd == "cloudResourceInfrastructureHome") {
		$(this).parent().next().nextAll().remove();
		$(this).parent().next().addClass("active");
		$(this).parent().next().text($(this).parent().next().text());
		listInfrastructureZones();
	}
	if (cmd == "listInfrastructureZones") {
		$(this).parent().nextAll().remove();
		$(this).parent().addClass("active");
		$(this).parent().text($(this).text());
		listInfrastructureZones();
	}
	if (cmd == "listPods") {
		$(this).parent().nextAll().remove();
		var zoneName = $(this).parent().text();
		var zoneId = $(this).attr("zoneId");
		$(this).parent().remove();
		listPods(zoneId, zoneName);
	}
	if (cmd == "listClusters") {
		$(this).parent().nextAll().remove();
		var zoneId = $(this).attr("zoneId");
		var podId = $(this).attr("podId");
		var podName = $(this).text();
		$(this).parent().remove();
		listClusters(zoneId, podId, podName);
	}
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-resource-infrastructure" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id=\"content-area\"></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class=\"breadcrumb breadcrumb-resource-infrastructure\" id=\"nav-breadcrumb\"></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-resource-infrastructure").empty();
  $("ol.breadcrumb.breadcrumb-resource-infrastructure").append("<li><a href=\"javascript:void(0);\" cmd=\"cloudResourceInfrastructureHome\"><span class=\"glyphicon glyphicon-home\"></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-resource-infrastructure").append("<li class=\"active\">" + secondLevelTitle + "</li>");

  listInfrastructureZones();
});

//处理获取系统基础设施Zone列表的请求
function listInfrastructureZones() {
	$("#content-area").empty();

	var zoneListTable = $("<table class=\"table table-bordered text-center\">"
		+ "<caption><strong>系统区域（机房）列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>区域（机房）名称</th><th>网络类型</th><th>状态</th><th>查看</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");	
	$("#content-area").append(zoneListTable);
	$("#content-area").append("<div class=\"loader\"></div>");
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
					+ (data[i].allocationState == "Enabled" ? "<td><span class=\"label label-success\">Enabled</span></td>" : "<td><span class=\"label label-danger\">Disabled</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listPods('" + data[i].zoneId + "','" + data[i].zoneName + "');\">点击查看</button></td></tr>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
}

//处理获取系统基础设施Pod、主存储、二级存储及系统虚拟机列表的请求
function listPods(zoneId, zoneName) {
	var secondLevelTitle = $("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text();
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text("");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").append("<a href=\"javascript:void(0);\" cmd=\"listInfrastructureZones\">" + secondLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure").append("<li class=\"active\">" + zoneName + "</li>");
	$("#content-area").empty();

	var podListTable = $("<table class=\"table table-bordered text-center pods-list-table\">"
		+ "<caption><strong>区域" + zoneName + "提供点列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>提供点名称</th><th>状态</th><th>查看</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(podListTable);
	$("table.pods-list-table").after("<div class=\"loader\" id=\"pods-list-table-loader\"></div>");
	$("#pods-list-table-loader").shCircleLoader({
		duration: 0.75
	});

	var secondaryStorageList = $("<table class=\"table table-bordered text-center secondary-storage-list-table\">"
		+ "<caption><strong>区域" + zoneName + "二级存储列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>名称</th><th>URL</th>"
		+ "<th>协议</th><th>提供者</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(secondaryStorageList);
	$("table.secondary-storage-list-table").after("<div class=\"loader\" id=\"secondary-storage-list-table-loader\"></div>");
	$("#secondary-storage-list-table-loader").shCircleLoader({
		duration: 0.75
	});

	var systemVMList = $("<table class=\"table table-bordered text-center system-VM-list-table\">"
		+ "<caption><strong>区域" + zoneName + "系统虚拟机列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>名称</th><th>主机名称</th>"
		+ "<th>类型</th><th>创建时间</th><th>状态</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(systemVMList);
	$("table.system-VM-list-table").after("<div class=\"loader\" id=\"system-VM-list-table-loader\"></div>");
	$("#system-VM-list-table-loader").shCircleLoader({
		duration: 0.75
	});

	//请求获取Pods列表数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listPods",
		dataType: "json",
		data: {
			zoneId: zoneId 
		},
		success: function(data) {
			$("#pods-list-table-loader").shCircleLoader('destroy');
			$("table.pods-list-table").addClass("table-interval");
			$("#pods-list-table-loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table.pods-list-table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].podName + "</td>"
					+ (data[i].allocationState == "Enabled" ? "<td><span class=\"label label-success\">Enabled</span></td>" : "<td><span class=\"label label-danger\">Disabled</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listClusters('" + zoneId + "','" + data[i].podId + "','" + data[i].podName + "');\">点击查看</button></td></tr>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
	
	//请求获取辅助存储列表数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listSecondaryStorage",
		dataType: "json",
		data: {
			zoneId: zoneId 
		},
		success: function(data) {
			$("#secondary-storage-list-table-loader").shCircleLoader('destroy');
			$("table.secondary-storage-list-table").addClass("table-interval");
			$("#secondary-storage-list-table-loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table.secondary-storage-list-table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].secondaryStorageName + "</td>"
					+ "<td>" + data[i].url + "</td><td>" + data[i].protocol + "</td><td>" 
					+ data[i].providerName + "</td></tr>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
	
	//请求获取系统虚拟机列表数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listSystemVms",
		dataType: "json",
		data: {
			zoneId: zoneId 
		},
		success: function(data) {
			$("#system-VM-list-table-loader").shCircleLoader('destroy');
			$("table.system-VM-list-table").addClass("table-interval");
			$("#system-VM-list-table-loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table.system-VM-list-table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].systemVmName + "</td><td>" + data[i].hostName + "</td>"
					+ "<td>" + data[i].systemVmType + "</td><td>" + data[i].createdDate + "</td>" 
					+ (data[i].state == "Running" ? "<td><span class=\"label label-success\">Running</span></td>" : "<td><span class=\"label label-danger\">Stoped</span></td>") 
					+ "</tr>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
}

function listClusters(zoneId, podId, podName) {
	var thirdLevelTitle = $("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text();
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text("");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").append("<a href=\"javascript:void(0);\" cmd=\"listPods\" zoneId=\"" + zoneId + "\">" + thirdLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure").append("<li class=\"active\">" + podName + "</li>");
	$("#content-area").empty();

	var clusterListTable = $("<table class=\"table table-bordered text-center\">"
		+ "<caption><strong>提供点" + podName + "集群列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>集群名称</th><th>虚拟机平台</th><th>状态</th><th>查看</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(clusterListTable);
	$("#content-area").append("<div class=\"loader\"></div>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	//请求获取集群数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listClusters",
		dataType: "json",
		data: {
			podId: podId 
		},
		success: function(data) {
			$("div.loader").shCircleLoader('destroy');
			$("div.loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].clusterName + "</td>"
					+ "<td>" + data[i].hypervisorType + "</td>"
					+ (data[i].allocationState == "Enabled" ? "<td><span class=\"label label-success\">Enabled</span></td>" : "<td><span class=\"label label-danger\">Disabled</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listHosts('" + zoneId + "','" + podId + "','" + data[i].clusterId + "','" + data[i].clusterName + "');\">点击查看</button></td></tr>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
}

function listHosts(zoneId, podId, clusterId, clusterName) {
	var fourthLevelTitle = $("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text();
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").text("");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").append("<a href=\"javascript:void(0);\"cmd=\"listClusters\" zoneId=\"" + zoneId + "\" podId=\"" + podId + "\">" + fourthLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-resource-infrastructure").append("<li class=\"active\">" + clusterName + "</li>");
	$("#content-area").empty();

	var primaryStorageList = $("<table class=\"table table-bordered text-center primary-storage-list-table\">"
		+ "<caption><strong>集群" + clusterName + "主存储列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>名称</th><th>主机IP</th>"
		+ "<th>路径</th><th>类型</th><th>状态</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(primaryStorageList);
	$("table.primary-storage-list-table").after("<div class=\"loader\" id=\"primary-storage-list-table-loader\"></div>");
	$("#primary-storage-list-table-loader").shCircleLoader({
		duration: 0.75
	});

	var hostListTable = $("<table class=\"table table-bordered text-center host-list-table\">"
		+ "<caption><strong>集群" + clusterName + "主机列表</strong></caption>"
		+ "<thead><tr><th>序号</th><th>IP地址</th><th>主机名称</th>"
		+ "<th>Hypervisor</th><th>创建时间</th><th>状态</th>"
		+ "<th>操作</th></tr></thead>"
		+ "<tbody></tbody>"
		+ "</table>");
	$("#content-area").append(hostListTable);
	$("table.host-list-table").after("<div class=\"loader\" id=\"host-list-table-loader\"></div>");
	$("#host-list-table-loader").shCircleLoader({
		duration: 0.75
	});
	
	//请求获取主存储数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listPrimaryStorage",
		dataType: "json",
		data: {
			clusterId: clusterId 
		},
		success: function(data) {
			$("#primary-storage-list-table-loader").shCircleLoader('destroy');
			$("table.primary-storage-list-table").addClass("table-interval");
			$("#primary-storage-list-table-loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table.primary-storage-list-table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].primaryStorageName + "</td>"
					+ "<td>" + data[i].hostIpAddress + "</td><td>" + data[i].path + "</td><td>" + data[i].type + "</td>"
					+ (data[i].state == "Up" ? "<td><span class=\"label label-success\">Up</span></td>" : "<td><span class=\"label label-danger\">Down</span></td>"));
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});

	//请求获取Host数据
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listHosts",
		dataType: "json",
		data: {
			clusterId: clusterId 
		},
		success: function(data) {
			$("#host-list-table-loader").shCircleLoader('destroy');
			$("table.host-list-table").addClass("table-interval");
			$("#host-list-table-loader").remove();
			for (var i = 0; i < data.length; i++) {
				$(".table.host-list-table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].ipAddress + "</td>"
					+ "<td>" + data[i].hostName + "</td><td>" + data[i].hypervisor + "</td><td>" + data[i].createdDate + "</td>"
					+ (data[i].state == "Up" ? "<td><span class=\"label label-success\">Up</span></td>" : "<td><span class=\"label label-danger\">Down</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-xs btn-danger\">删除</button></td>");
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});

	$("#content-area").append("<button type=\"button\" class=\"btn btn-primary btn-sm pull-right\" id=\"add-host-btn\" data-toggle=\"modal\" data-target=\"\"><span class=\"glyphicon glyphicon-plus\"></span>  点击添加物理主机</button>");
	//弹出的模态框
	$("#add-host-btn").after("<div class=\"modal fade\" id=\"add-host-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"
   						   + 	"<div class=\"modal-dialog modal-dialog-center\">"
   						   +		"<div class=\"modal-content\">"
   						   +			"<div class=\"modal-header\">"
   						   +				"<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"
   						   +				"<h4 class=\"modal-title\">添加物理主机</h4>"
   						   +			"</div>"
   						   +			"<div class=\"modal-body\">"
          				   +       			"<div class=\"form-horizontal\" role=\"form\">"
          				   +					"<div class=\"form-group\">"
          				   +						"<label for=\"\" class=\"col-lg-4 control-label\">IP地址：</label>"
          				   +						"<div class=\"col-lg-8\">"
          				   +							"<input type=\"text\" class=\"form-control\" id=\"host-ip-address\" placeholder=\"请输入主机IP地址\">"
          				   +						"</div>"
          				   +					"</div>"
          				   +					"<div class=\"form-group\">"
          				   +						"<label for=\"\" class=\"col-lg-4 control-label\">用户名：</label>"
          				   +						"<div class=\"col-lg-8\">"
          				   +							"<input type=\"text\" class=\"form-control\" id=\"host-account\" placeholder=\"请输入用户名\">"
          				   +						"</div>"
          				   +					"</div>"
          				   +					"<div class=\"form-group\">"
          				   +						"<label for=\"\" class=\"col-lg-4 control-label\">密码：</label>"
          				   +						"<div class=\"col-lg-8\">"
          				   +							"<input type=\"password\" class=\"form-control\" id=\"host-password\" placeholder=\"请输入主机密码\">"
          				   +						"</div>"
          				   +					"</div>"
          				   +				"</div>"
         				   +			"</div>"
         				   +			"<div class=\"modal-footer\">"
         				   +				"<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"
         				   +				"<button type=\"button\" class=\"btn btn-primary\" onclick=\"addHost('" + zoneId + "','" + podId + "','" + clusterId + "','" + clusterName + "')\">" + "提交</button>"
         				   +			"</div>"
   						   +		"</div>"
   						   + 	"</div>"
   						   + "</div>");
}

//对“添加物理主机”按钮进行监听(弹出模态框)
$("body").on("click", '#add-host-btn', function(){
	$('#add-host-modal').modal({
		keyboard: true
	});
});

//添加物理主机表单提交按钮触发函数
function addHost(zoneId, podId, clusterId, clusterName) {
	var ipAddress = $("#host-ip-address").val();
	var hostAccount = $("#host-account").val();
	var hostPassword = $("#host-password").val();

	if (ipAddress == "") {
		alert("IP地址不能为空！");
		return;
	}
	if (hostAccount == "") {
		alert("主机用户名不能为空！");
		return;
	}
	if (hostPassword == "") {
		alert("主机密码不能为空！");
		return;
	}

	//添加转菊花
	$("#add-host-modal div.modal-body").append("<div class=\"loader\"></div>");
	$("#add-host-modal div.modal-body").append("<span class=\"text-primary pull-right\">正在添加，事件稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});


	$.ajax({
		type: "POST",
		url: "resource/infrastructure/addHost",
		dataType: "json",
		data: {
			zoneId: zoneId,
			podId: podId,
			clusterId: clusterId,
			ipAddress: ipAddress,
			hostAccount: hostAccount,
			hostPassword: hostPassword 
		},
		success: function(data) {
			$("#add-host-modal div.loader").shCircleLoader('destroy');
			$("#add-host-modal div.loader").remove();
			$("#add-host-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //添加成功
				$("#host-ip-address").val('');
				$("#host-account").val('');
				$("#host-password").val('');
				alert("主机添加成功！");
				$('#add-host-modal').modal('hide');
				$(".table.host-list-table tbody").append("<tr><td>" + data.index + "</td><td>" + data.hostDto.ipAddress + "</td>"
					+ "<td>" + data.hostDto.hostName + "</td><td>" + data.hostDto.hypervisor + "</td><td>" + data.hostDto.createdDate + "</td>"
					+ (data.hostDto.state == "Up" ? "<td><span class=\"label label-success\">Up</span></td>" : "<td><span class=\"label label-danger\">Down</span></td>")
					+ "<td><button type=\"button\" class=\"btn btn-xs btn-danger\">删除</button></td>");
			}
			else {
				alert("主机添加失败！消息：" + data.message);
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
}