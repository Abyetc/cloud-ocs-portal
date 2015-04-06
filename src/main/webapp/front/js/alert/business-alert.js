//对sidebar的点击行为进行处理
$( ".sidebar-business-alert" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-business-alert' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-business-alert").empty();
  $("ol.breadcrumb.breadcrumb-business-alert").append("<li><a href='javascript:void(0);' cmd=''><span class='glyphicon glyphicon-tasks'></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-business-alert").append("<li class='active'>" + secondLevelTitle + "</li>");

  listBusinessAlertPoint();
  
  listBusinessAlerts();
});

function listBusinessAlertPoint() {
	$("#content-area").empty();
	
	$("#content-area").append("<div class='content-header' style='margin-top:40px;'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;报警服务监控点</strong></span></div>");

	$("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-biz-alert-monitor-point-btn'><span class='glyphicon glyphicon-plus'></span>  点击添加报警监控点</button>");
	
	//弹出添加城市表单的模态框
	$("#add-biz-alert-monitor-point-btn").after("<div class='modal fade' id='add-biz-alert-monitor-point-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
	                     +  "<div class='modal-dialog modal-dialog-center'>"
	                     +    "<div class='modal-content'>"
	                     +      "<div class='modal-header'>"
	                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
	                     +        "<h4 class='modal-title'>新增计费业务报警监控点</h4>"
	                     +      "</div>"
	                     +      "<div class='modal-body'>"
	                     +            "<div class='form-horizontal' role='form'>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>报警监控对象类型：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='biz-alert-monitor-object-type'>"
	                     +                 "<option value='0'>城市</optino>"
	                     +				   "<option value='1'>网络</optino>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>报警监控对象列表：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='biz-alert-monitor-object'>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>包处理时长阈值：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<input type='text' class='form-control' id='message-process-time-threshold-value' placeholder='请输入包处理时长阈值'>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>是否启动弹性资源管理：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<select class='form-control' id='active-flexible-resource'>"
	                     +                 "<option value='0'>否</optino>"
	                     +				   "<option value='1'>是</optino>"
	                     +              "</select>"
	                     +            "</div>"
	                     +          "</div>"
	                     +          "<div class='form-group'>"
	                     +            "<label for='' class='col-lg-4 control-label'>资源负载阈值：</label>"
	                     +            "<div class='col-lg-8'>"
	                     +              "<input type='text' class='form-control' id='resource-load-threshold-value' placeholder='资源负载阈值'>"
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
	                     +        "<button type='button' class='btn btn-primary' onclick=\"addBizMonitorPoint()\">" + "提交</button>"
	                     +      "</div>"
	                     +    "</div>"
	                     +  "</div>"
	                     + "</div>");
	var businessAlertPointTable = $("<table class='table table-bordered text-center' id='biz-alert-monitor-point-table'>"
			    + "<caption><strong></strong></caption>"
			    + "<thead><tr><th>序号</th><th>对象</th><th>负责人</th><th>创建时间</th><th>查看详情</th><th>删除</th></tr></thead>"
			    + "<tbody></tbody>"
			    + "</table>");
	$("#content-area").append(businessAlertPointTable);
	
	$.ajax({
		type: "GET",
		url: "alert/listAllBizAlertMonitorPoint",
		dataType: "json",
		data: {
		},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				//添加新增数据到表格中
				$("#biz-alert-monitor-point-table").append("<tr id=''>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].monitorObjectName + "</td>"
					+ "<td>" +  data[i].principalName + "</td>"
					+ "<td>" + data[i].created + "</td>"
					+ "<td class='vm-stop-or-start-btn'>" + "<button type='button' class='btn btn-xs btn-warning' onclick='stopOcsVm(\"" + "" + "\")'>查看详情</button>" + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeOcsVm(\"" + "" + "\")'>删除</button>" + "</td>"
					+ "</tr>");
			}
		},
		error: function(xhr, status) {
			alert(status);
		}
	});
	
}

function listBusinessAlerts() {
	$("#content-area").append("<div class='content-header'><span class='glyphicon glyphicon-hand-right'><strong>&nbsp;警报列表</strong></span></div>");
	
	var businessAlertTable = $("<table class='table table-bordered text-center' id='biz-alert-table'>"
		    + "<caption><strong></strong></caption>"
		    + "<thead><tr><th>序号</th><th>来源</th><th>描述</th><th>报警时间</th><th>查看详情</th><th>删除</th></tr></thead>"
		    + "<tbody></tbody>"
		    + "</table>");
	$("#content-area").append(businessAlertTable);
	
	$("#biz-alert-table").append("<tr><td>1</td><td>广州</td><td>广州包处理时长超过阈值</td><td>2015-03-17 23:33:20</td>" + 
			"<td><button type='button' class='btn btn-xs btn-warning'>查看详情</button></td>" +
			"<td><button type='button' class='btn btn-xs btn-danger'>删除</button></td></tr>");
	$("#biz-alert-table").append("<tr><td>2</td><td>广州</td><td>广州包处理时长超过阈值</td><td>2015-03-17 23:38:40</td>" + 
			"<td><button type='button' class='btn btn-xs btn-warning'>查看详情</button></td>" +
			"<td><button type='button' class='btn btn-xs btn-danger'>删除</button></td></tr>");
	$("#biz-alert-table").append("<tr><td>3</td><td>珠海</td><td>珠海包处理时长超过阈值</td><td>2015-03-20 10:13:30</td>" + 
			"<td><button type='button' class='btn btn-xs btn-warning'>查看详情</button></td>" +
			"<td><button type='button' class='btn btn-xs btn-danger'>删除</button></td></tr>");
}

//对“新增报警点”按钮进行监听(弹出模态框)
$("body").on("click", '#add-biz-alert-monitor-point-btn', function() {
  $('#add-biz-alert-monitor-point-modal').modal({
    keyboard: true
  });
  
  var citySelectOption = '';
  $("#biz-alert-monitor-object").empty();
	//获取城市列表
	$.ajax({
		type: "GET",
		url: "business/listCities",
		dataType: "json",
		data: {},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				citySelectOption += "<option value=\"" + data[i].id + "\">" + data[i].name + "</option>";
			}
			$("#biz-alert-monitor-object").append(citySelectOption);
		},
		error: function(xhr, status) {}
	});
  var bizEmployeeOption = '';
  $("#alert_principal").empty();
  //获取业务人员列表
	$.ajax({
		type: "GET",
		url: "alert/getAllBizEmployee",
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

function addBizMonitorPoint() {
	var objectType = $("#biz-alert-monitor-object-type").val();
	var monitorObject= $("#biz-alert-monitor-object").val();
	var messageProcessTimeThresholdValue= $("#message-process-time-threshold-value").val();
	var activeFlexibleResource = $("#active-flexible-resource").val();
	var resourceLoadThresholdValue = $("#resource-load-threshold-value").val();
	var alertPrincipal = $("#alert_principal").val();
	
	//添加转菊花
	$("#add-biz-alert-monitor-point-modal div.modal-body").append("<div class='loader'></div>");
	$("#add-biz-alert-monitor-point-modal div.modal-body").append("<span class='text-primary pull-right'>正在添加...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});
	
	$.ajax({
		type: "POST",
		url: "alert/addBizAlertMonitorPoint",
		dataType: "json",
		data: {
		    objectType: objectType,
		    monitorObjectId: monitorObject,
		    messageProcessTimeThresholdValue: messageProcessTimeThresholdValue,
		    activeFlexibleResource: activeFlexibleResource,
		    resourceLoadThresholdValue: resourceLoadThresholdValue,
		    alertPrincipalId:alertPrincipal
		},
		success: function(data) {
			$("#add-biz-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#add-biz-alert-monitor-point-modal div.loader").remove();
			$("#add-biz-alert-monitor-point-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //添加成功
				$("#message-process-time-threshold-value").val("");
				$("#resource-load-threshold-value").val("");
				alert("添加成功！");
				$('#add-biz-alert-monitor-point-modal').modal('hide');
				
				//添加新增数据到表格中
				$("#biz-alert-monitor-point-table").append("<tr id=''>"
					+ "<td>" + data.index + "</td>"
					+ "<td>" + data.operatedObject.monitorObjectName + "</td>"
					+ "<td>" +  data.operatedObject.principalName + "</td>"
					+ "<td>" + data.operatedObject.created + "</td>"
					+ "<td class='vm-stop-or-start-btn'>" + "<button type='button' class='btn btn-xs btn-warning' onclick='stopOcsVm(\"" + "" + "\")'>查看详情</button>" + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeOcsVm(\"" + "" + "\")'>删除</button>" + "</td>"
					+ "</tr>");
			}
			else {
				$("#message-process-time-threshold-value").val("");
				$("#resource-load-threshold-value").val("");
				alert("添加失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#add-biz-alert-monitor-point-modal div.loader").shCircleLoader('destroy');
			$("#add-biz-alert-monitor-point-modal div.loader").remove();
			$("#add-biz-alert-monitor-point-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}

