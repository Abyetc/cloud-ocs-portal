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

	$("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-infra-alert-monitor-point-btn'><span class='glyphicon glyphicon-plus'></span>  点击添加报警监控点</button>");
	//弹出添加城市表单的模态框
	$("#add-infra-alert-monitor-point-btn").after("<div class='modal fade' id='add-infra-alert-monitor-point-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
	                     +  "<div class='modal-dialog modal-dialog-center'>"
	                     +    "<div class='modal-content'>"
	                     +      "<div class='modal-header'>"
	                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
	                     +        "<h4 class='modal-title'>新增基础设施报警监控点</h4>"
	                     +      "</div>"
	                     +      "<div class='modal-body'>"
	                     +            "<div class='form-horizontal' role='form'>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>报警监控对象列表：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='infra-alert-monitor-object'>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>监控类型：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='monitor-type'>"
	                     +                 "<option value='0'>CPU</optino>"
	                     +				   "<option value='1'>内存</optino>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>阈值(%)：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<input type='text' class='form-control' id='usage-threshold-value' placeholder='请输入阈值'>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>负责人列表：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='alert_principal'>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +        "</div>"
	                     +      "</div>"
	                     +      "<div class='modal-footer'>"
	                     +        "<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>"
	                     +        "<button type='button' class='btn btn-primary' onclick=\"addInfraMonitorPoint()\">" + "提交</button>"
	                     +      "</div>"
	                     +    "</div>"
	                     +  "</div>"
	                     + "</div>");
	//删除监控点的模态框
	$("#add-infra-alert-monitor-point-btn").after("<div class='modal fade' id='remove-infra-alert-monitor-point-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>删除基础设施类报警监控点</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +      	"<h3 class='text-center text-info'>确认删除？删除后将无法恢复！</h3>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");
	
	//弹出查看警报详情的模态框
	$("#add-infra-alert-monitor-point-btn").after("<div class='modal fade' id='infra-alert-detail-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
	                     +  "<div class='modal-dialog modal-dialog-center'>"
	                     +    "<div class='modal-content'>"
	                     +      "<div class='modal-header'>"
	                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
	                     +        "<h4 class='modal-title'>警报详情</h4>"
	                     +      "</div>"
	                     +      "<div class='modal-body'>"
	                     +            "<div class='form-horizontal' role='form'>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>警报来源：</label>"
	                     +            "<div class='col-lg-8' id='alert-detail-source'>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>警报触发时间：</label>"
	                     +            "<div class='col-lg-8' id='alert-detail-created'>>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>警报描述：</label>"
	                     +            "<div class='col-lg-8' id='alert-detail-description'>"
	                     +            "</div>"
	                     +          "</div>"
	                     +        "</div>"
	                     +      "</div>"
	                     +      "<div class='modal-footer'>"
	                     +        "<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>"
	                     +      "</div>"
	                     +    "</div>"
	                     +  "</div>"
	                     + "</div>");
	
	var infrastructureAlertPointTable = $("<table class='table table-bordered text-center' id='infrastructure-alert-monitor-point-table'>"
			    + "<caption><strong></strong></caption>"
			    + "<thead><tr><th>序号</th><th>对象</th><th>监控内容</th><th>阈值(%)</th><th>负责人</th><th>创建时间</th><th>查看详情</th></tr></thead>"
			    + "<tbody></tbody>"
			    + "</table>");
	$("#content-area").append(infrastructureAlertPointTable);
	
	$.ajax({
		type: "GET",
		url: "alert/getAllInfraAlertMonitorPoints",
		dataType: "json",
		data: {
		},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				//添加新增数据到表格中
				$("#infrastructure-alert-monitor-point-table").append("<tr id='infra-alert-monitor-point-" + data[i].alertMonitorPointId + "'>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].monitorObjectName + "</td>"
					+ "<td>" + data[i].monitorObjectType + "</td>"
					+ "<td>" + data[i].usagePercentageThreshold + "</td>"
					+ "<td>" +  data[i].principalName + "</td>"
					+ "<td>" + data[i].created + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeInfraAlertMonitorPoint(" + data[i].alertMonitorPointId + ")'>解除</button>" + "</td>"
					+ "</tr>");
			}
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
}

function listInfrastructureAlerts() {
	$("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;警报列表</strong></span></div>");
	
	var infrastructureAlertTable = $("<table class='table table-bordered text-center' id='infrastructure-alert-table'>"
		    + "<caption><strong></strong></caption>"
		    + "<thead><tr><th>序号</th><th>来源</th><th>报警时间</th><th>查看详情</th></tr></thead>"
		    + "<tbody></tbody>"
		    + "</table>");
	$("#content-area").append(infrastructureAlertTable);
	
	
	$.ajax({
		type: "GET",
		url: "alert/getAllInfraAlertList",
		dataType: "json",
		data: {
		},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				//添加新增数据到表格中
				$("#infrastructure-alert-table").append("<tr>" + 
						"<td>" + (i+1) + "</td>" + 
						"<td>" + data[i].source + "</td>" +
						"<td>" + data[i].created + "</td>" + 
						"<td><button type='button' class='btn btn-xs btn-warning' onclick='infraAlertDetail(" + 
						"\"" + data[i].source + "\","
						+ "\"" + data[i].created + "\","
						+ "\"" + data[i].description + "\""
						+ ")'>查看详情</button></td>" +
						"</tr>");
			}
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
	
	$("#content-area").append("<button style='margin-bottom:50px;' type='button' class='btn btn-primary btn-sm pull-right' id='check-infra-alert-btn' data-toggle='modal' data-target=''>点击检测基础设施类报警</button>");
}

//对“新增报警点”按钮进行监听(弹出模态框)
$("body").on("click", '#add-infra-alert-monitor-point-btn', function() {
  $('#add-infra-alert-monitor-point-modal').modal({
    keyboard: true
  });
  
  var hostSelectOption = '';
  $("#infra-alert-monitor-object").empty();
	//获取城市列表
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listHosts",
		dataType: "json",
		data: {
			clusterId: 'e6470d5a-d30f-4c80-b384-4a256686f69c'
		},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				hostSelectOption += "<option value=\"" + data[i].hostId + "\">" + data[i].hostName + "</option>";
			}
			$("#infra-alert-monitor-object").append(hostSelectOption);
		},
		error: function(xhr, status) {}
	});
  var bizEmployeeOption = '';
  $("#alert_principal").empty();
  //获取运维人员列表
	$.ajax({
		type: "GET",
		url: "alert/getAllMaintenanceEmployee",
		dataType: "json",
		data: {},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				bizEmployeeOption += "<option value=\"" + data[i].accountId + "\">" + data[i].name + "</option>";
			}
			$("#alert_principal").append(bizEmployeeOption);
		},
		error: function(xhr, status) {}
	});
});

//提交添加监控点到后台
function addInfraMonitorPoint() {
	var monitorObjectId = $("#infra-alert-monitor-object").val();
	var monitorType = $("#monitor-type").val();
	var usageThresholdValue = $("#usage-threshold-value").val();
	var alertPrincipal = $("#alert_principal").val();
	
	//添加转菊花
	$("#add-infra-alert-monitor-point-modal div.modal-body").append("<div class='loader'></div>");
	$("#add-infra-alert-monitor-point-modal div.modal-body").append("<span class='text-primary pull-right'>正在添加...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});
	
	$.ajax({
		type: "POST",
		url: "alert/addInfraAlertMonitorPoint",
		dataType: "json",
		data: {
			monitorObjectId: monitorObjectId,
			monitorType: monitorType,
			usageThresholdValue: usageThresholdValue,
			alertPrincipal: alertPrincipal,
		},
		success: function(data) {
			$("#add-infra-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#add-infra-alert-monitor-point-modal div.loader").remove();
			$("#add-infra-alert-monitor-point-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //添加成功
				$("#usage-threshold-value").val("");
				alert("添加成功！");
				$('#add-infra-alert-monitor-point-modal').modal('hide');
				
				//添加新增数据到表格中
				$("#infrastructure-alert-monitor-point-table").append("<tr id='infra-alert-monitor-point-" + data.operatedObject.alertMonitorPointId + "'>"
					+ "<td>" + data.index + "</td>"
					+ "<td>" + data.operatedObject.monitorObjectName + "</td>"
					+ "<td>" + data.operatedObject.monitorObjectType + "</td>"
					+ "<td>" + data.operatedObject.usagePercentageThreshold + "</td>"
					+ "<td>" +  data.operatedObject.principalName + "</td>"
					+ "<td>" + data.operatedObject.created + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeInfraAlertMonitorPoint(" + data.operatedObject.alertMonitorPointId + ")'>解除</button>" + "</td>"
					+ "</tr>");
			}
			else {
				$("#usage-threshold-value").val("");
				alert("添加失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#add-infra-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#add-infra-alert-monitor-point-modal div.loader").remove();
			$("#add-infra-alert-monitor-point-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}

function removeInfraAlertMonitorPoint(monitorPointId) {
	$('#remove-infra-alert-monitor-point-modal').modal({
		keyboard: true
	});
	$("#remove-infra-alert-monitor-point-modal div.modal-footer").empty();
	$("#remove-infra-alert-monitor-point-modal div.modal-footer").append("<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>");
	$("#remove-infra-alert-monitor-point-modal div.modal-footer").append("<button type='button' class='btn btn-primary' onclick=\"removeInfraAlertMonitorPointSubmit(" + monitorPointId + ")\">" + "确认</button>");
}

function removeInfraAlertMonitorPointSubmit(monitorPointId) {
	//添加转菊花
	$("#remove-infra-alert-monitor-point-modal div.modal-body").append("<div class='loader'></div>");
	$("#remove-infra-alert-monitor-point-modal div.modal-body").append("<span class='text-primary pull-right'>正在删除，过程稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	$.ajax({
		type: "GET",
		url: "alert/removeInfrastructureMonitorPoint",
		dataType: "json",
		data: {
			monitorPointId: monitorPointId
		},
		success: function(data) {
			$("#remove-infra-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#remove-infra-alert-monitor-point-modal div.loader").remove();
			$("#remove-infra-alert-monitor-point-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //删除成功
				alert("基础设施类监控点删除成功！");
				$('#remove-infra-alert-monitor-point-modal').modal('hide');
				$('#infra-alert-monitor-point-' + monitorPointId + '').remove();
			}
			else {
				alert("基础设施类监控点删除失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#remove-infra-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#remove-infra-alert-monitor-point-modal div.loader").remove();
			$("#remove-infra-alert-monitor-point-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}

$("body").on("click", '#check-infra-alert-btn', function() {
	$("#check-infra-alert-btn").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		    duration: 0.75
	});
	
	$.ajax({
		type: "GET",
		url: "alert/checkInfraAlert",
		dataType: "json",
		data: {
		},
		success: function(data) {
			$("#check-infra-alert-btn div.loader").shCircleLoader('destroy');
			$("#check-infra-alert-btn div.loader").remove();
		},
		error: function(xhr, status) {
			$("#check-infra-alert-btn div.loader").shCircleLoader('destroy');
			$("#check-infra-alert-btn div.loader").remove();
			alert(status);
		}
	});
});

function infraAlertDetail(source, created, description) {
	$('#infra-alert-detail-modal').modal({
		keyboard: true
	});
	$("#alert-detail-source").empty();
	$("#alert-detail-created").empty();
	$("#alert-detail-description").empty();
	$("#alert-detail-source").append(source);
	$("#alert-detail-created").append(created);
	$("#alert-detail-description").append(description);
}