/*
  系统前端页面云业务管理模块 
  包括：城市所属VM列表、添加VM逻辑的实现
*/

function listNetworkVms(event) {
	var networkId = event.data.networkId;
	var networkName = event.data.networkName;

	//先处理面包屑导航栏
	var secondLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-service li.active").text();
	$("ol.breadcrumb.breadcrumb-ocs-service li.active").text("");
	$("ol.breadcrumb.breadcrumb-ocs-service li.active").append("<a href='javascript:void(0);' cmd='listNetworks'>" + secondLevelTitle + "</a>");
	$("ol.breadcrumb.breadcrumb-ocs-service li.active").removeClass("active");
	$("ol.breadcrumb.breadcrumb-ocs-service").append("<li class='active'>" + networkName + "</li>");
	$("#content-area").empty();

	//添加服务城市网络按钮
	$("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-city-vm-btn'><span class='glyphicon glyphicon-plus'></span>  点击添加服务VM</button>");

	//弹出添加城市VM表单的模态框
    $("#add-city-vm-btn").after("<div class='modal fade' id='add-city-vm-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>新增云计费系统服务VM</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +            "<div class='form-horizontal' role='form'>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-vm-name' class='col-lg-4 control-label'>VM名称：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<input type='text' class='form-control' id='city-vm-name' placeholder='请输入VM名称'>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-vm-zone' class='col-lg-4 control-label'>区域：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-vm-zone'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-vm-host' class='col-lg-4 control-label'>主机：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-vm-host'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-vm-serviceoffering' class='col-lg-4 control-label'>计算方案：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-vm-serviceoffering'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-vm-template' class='col-lg-4 control-label'>模板：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-vm-template'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +        "</div>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +        "<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>"
                     +        "<button type='button' class='btn btn-primary' onclick=\"addOcsVm('" + networkId + "')\">" + "提交</button>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");
	
	//删除Vm的模态框
	$("#add-city-vm-btn").after("<div class='modal fade' id='remove-city-vm-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>删除云计费系统服务VM</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +      	"<h3 class='text-center text-info'>确认删除？删除后将无法恢复！</h3>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");

	//停止Vm的模态框
	$("#add-city-vm-btn").after("<div class='modal fade' id='stop-city-vm-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>停止云计费系统服务VM</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +      	"<h3 class='text-center text-info'>确认停止该服务VM？</h3>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");

	//开启Vm的模态框
	$("#add-city-vm-btn").after("<div class='modal fade' id='start-city-vm-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>开启云计费系统服务VM</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +      	"<h3 class='text-center text-info'>确认开启该服务VM？</h3>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");
	
	//已有的服务城市VM列表
	var cityVmsTable = $("<table class='table table-bordered text-center' id='city-vm-table'>"
	    + "<caption><strong>服务网络" + networkName + " OCS VM列表</strong></caption>"
	    + "<thead><tr><th>序号</th><th>VM名称</th><th>区域</th><th>主机</th>"
	    + "<th>创建时间</th><th>状态</th><th>停止/开启</th><th>删除</th></tr></thead>"
	    + "<tbody></tbody>"
	    + "</table>");
	$("#content-area").append(cityVmsTable);
	//转菊花
	$("#content-area").append("<div class='loader'></div>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});
	$.ajax({
		type: "GET",
		url: "business/listOcsVmsByNetwork",
		dataType: "json",
		data: {
			networkId: networkId
		},
		success: function(data) {
			$("div.loader").shCircleLoader('destroy');
			$("div.loader").remove();
			for (var i = 0; i < data.length; i++) {
				var state = "";
				var stopOrStartBtn = '';
				switch (data[i].vmState) {
					case 0:
					case 2:
					case 4:
					case 6:
						state = "<span class='label label-warning'>" + vmStateArray[data[i].vmState] + "</span>";
						break;
					case 3:
					case 5:
					case 7:
					case 8:
					case 9:
						state = "<span class='label label-danger'>" + vmStateArray[data[i].vmState] + "</span>";
						stopOrStartBtn = "<button type='button' class='btn btn-xs btn-success' onclick='startOcsVm(\"" + data[i].vmId + "\")'>开启</button>";
						break;
					case 1:
						state = "<span class='label label-success'>" + vmStateArray[data[i].vmState] + "</span>";
						stopOrStartBtn = "<button type='button' class='btn btn-xs btn-warning' onclick='stopOcsVm(\"" + data[i].vmId + "\")'>停止</button>";
						break;
					default:
						break;
				}
				var hostName = (!data[i].hostName ? '-' : data[i].hostName);
				$("#city-vm-table").append("<tr id='" + data[i].vmId + "'>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].vmName + "</td>"
					+ "<td>" +  data[i].zoneName + "</td>"
					+ "<td class='vm-hostname'>" + hostName + "</td>"
					+ "<td>" + data[i].created + "</td>"
					+ "<td class='vm-state-lable'>" + state + "</td>"
					+ "<td class='vm-stop-or-start-btn'>" + stopOrStartBtn + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeOcsVm(\"" + data[i].vmId + "\")'>删除</button>" + "</td>"
					+ "</tr>");
			}
		},
		error: function(xhr, status) {
				$("div.loader").shCircleLoader('destroy');
				$("div.loader").remove();
				alert(status);
		}
	});
}

//===================================================================================
//对“新增服务Vm”按钮进行监听(弹出模态框)
$("body").on("click", '#add-city-vm-btn', function() {
	$('#add-city-vm-modal').modal({
		keyboard: true
	});
	var zoneSelectOption = "";
	var hostSelectOption = "";
	var serviceOfferingSelectOption = "";
	var templateSelectOption = "";
	$("#city-vm-zone").empty();
	//获取zone列表
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listZones",
		dataType: "json",
		data: {},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				zoneSelectOption += "<option value=\"" + data[i].zoneId + "\">" + data[i].zoneName + "</option>";
			}
			$("#city-vm-zone").append(zoneSelectOption);
		},
		error: function(xhr, status) {}
	});
	//获取host列表
	$("#city-vm-host").empty();
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listHosts",
		dataType: "json",
		data: {
			clusterId: "e6470d5a-d30f-4c80-b384-4a256686f69c"
		},
		success: function(data) {
			hostSelectOption += "<option value=''></option>";
			for (var i = 0; i < data.length; i++) {
				hostSelectOption += "<option value=\"" + data[i].hostId + "\">" + data[i].hostName + "</option>";
			}
			$("#city-vm-host").append(hostSelectOption);
		},
		error: function(xhr, status) {}
	});
	//获取service offering列表
	$("#city-vm-serviceoffering").empty();
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listNonSystemServiceOffering",
		dataType: "json",
		data: {},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				serviceOfferingSelectOption += "<option value='" + data[i].serviceOfferingId + "'>" + data[i].serviceOfferingName + "(" + data[i].displayText + ")</option>";
			}
			$("#city-vm-serviceoffering").append(serviceOfferingSelectOption);
		},
		error: function(xhr, status) {}
	});
	//获取template列表
	$("#city-vm-template").empty();
	$.ajax({
		type: "GET",
		url: "resource/infrastructure/listSelfExecutableTemplates",
		dataType: "json",
		data: {},
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				templateSelectOption += "<option value='" + data[i].templateId + "'>" + data[i].templateName + "(" + data[i].displayText + ")</option>";
			}
			$("#city-vm-template").append(templateSelectOption);
		},
		error: function(xhr, status) {}
	});
});

//添加OCS VM提交表单
function addOcsVm(networkId) {
	var vmName = $("#city-vm-name").val();
	var zoneId = $("#city-vm-zone").val();
	var hostId = $("#city-vm-host").val();
	var serviceOfferingId = $("#city-vm-serviceoffering").val();
	var templateId = $("#city-vm-template").val();

	if (vmName.trim() == "") {
		alert("VM名字不能为空！");
		return;
	}

	//添加转菊花
	$("#add-city-vm-modal div.modal-body").append("<div class='loader'></div>");
	$("#add-city-vm-modal div.modal-body").append("<span class='text-primary pull-right'>正在添加，过程稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	$.ajax({
		type: "POST",
		url: "business/addOcsVm",
		dataType: "json",
		data: {
			networkId: networkId,
			vmName: vmName.trim(),
			serviceOfferingId: serviceOfferingId,
			zoneId: zoneId,
			templateId: templateId,
			hostId: hostId
		},
		success: function(data) {
			$("#add-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#add-city-vm-modal div.loader").remove();
			$("#add-city-vm-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //添加成功
				$("#city-vm-name").val('');
				alert("OCS VM添加成功！");
				$('#add-city-vm-modal').modal('hide');
				//添加新增数据到表格中
				var state = "";
				switch (data.ocsVmDto.vmState) {
					case 0:
					case 2:
					case 4:
					case 6:
						state = "<span class='label label-warning'>" + vmStateArray[data.ocsVmDto.vmState] + "</span>";
						break;
					case 3:
					case 5:
					case 7:
					case 8:
					case 9:
						state = "<span class='label label-danger'>" + vmStateArray[data.ocsVmDto.vmState] + "</span>";
						break;
					case 1:
						state = "<span class='label label-success'>" + vmStateArray[data.ocsVmDto.vmState] + "</span>";
						break;
					default:
						break;
				}
				$("#city-vm-table").append("<tr id='" + data.ocsVmDto.vmId + "'>"
					+ "<td>" + data.index + "</td>"
					+ "<td>" + data.ocsVmDto.vmName + "</td>"
					+ "<td>" +  data.ocsVmDto.zoneName + "</td>"
					+ "<td class='vm-hostname'>" + data.ocsVmDto.hostName + "</td>"
					+ "<td>" + data.ocsVmDto.created + "</td>"
					+ "<td class='vm-state-lable'>" + state + "</td>"
					+ "<td class='vm-stop-or-start-btn'>" + "<button type='button' class='btn btn-xs btn-warning' onclick='stopOcsVm(\"" + data.ocsVmDto.vmId + "\")'>停止</button>" + "</td>"
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger' onclick='removeOcsVm(\"" + data.ocsVmDto.vmId + "\")'>删除</button>" + "</td>"
					+ "</tr>");
			}
			else {
				$("#city-vm-name").val('');
				alert("OCS VM添加失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#add-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#add-city-vm-modal div.loader").remove();
			$("#add-city-vm-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}
//===================================================================================

//===================================================================================
//弹出删除Vm模态框
function removeOcsVm(vmId) {
	$('#remove-city-vm-modal').modal({
		keyboard: true
	});
	$("#remove-city-vm-modal div.modal-footer").empty();
	$("#remove-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>");
	$("#remove-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-primary' onclick=\"removeOcsVmSubmit('" + vmId + "')\">" + "确认</button>");
}

//提交删除Vm请求到后台
function removeOcsVmSubmit(vmId) {
	//添加转菊花
	$("#remove-city-vm-modal div.modal-body").append("<div class='loader'></div>");
	$("#remove-city-vm-modal div.modal-body").append("<span class='text-primary pull-right'>正在删除，过程稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	$.ajax({
		type: "GET",
		url: "business/removeOcsVm",
		dataType: "json",
		data: {
			vmId: vmId
		},
		success: function(data) {
			$("#remove-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#remove-city-vm-modal div.loader").remove();
			$("#remove-city-vm-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //删除成功
				alert("OCS VM删除成功！");
				$('#remove-city-vm-modal').modal('hide');
				$('#' + vmId + '').remove();
			}
			else {
				alert("OCS VM删除失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#remove-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#remove-city-vm-modal div.loader").remove();
			$("#remove-city-vm-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}
//===================================================================================

//===================================================================================
//弹出停止虚拟机模态框
function stopOcsVm(vmId) {
	$('#stop-city-vm-modal').modal({
		keyboard: true
	});
	$("#stop-city-vm-modal div.modal-footer").empty();
	$("#stop-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>");
	$("#stop-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-primary' onclick=\"stopOcsVmSubmit('" + vmId + "')\">" + "确认</button>");
}

function stopOcsVmSubmit(vmId) {
	//添加转菊花
	$("#stop-city-vm-modal div.modal-body").append("<div class='loader'></div>");
	$("#stop-city-vm-modal div.modal-body").append("<span class='text-primary pull-right'>正在停止，过程稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	$.ajax({
		type: "GET",
		url: "business/stopOcsVm",
		dataType: "json",
		data: {
			vmId: vmId
		},
		success: function(data) {
			$("#stop-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#stop-city-vm-modal div.loader").remove();
			$("#stop-city-vm-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //停止成功
				alert("OCS VM停止成功！");
				$('#stop-city-vm-modal').modal('hide');
				$('#' + vmId + ' td.vm-hostname').empty();
				$('#' + vmId + ' td.vm-hostname').append('-');
				$('#' + vmId + ' td.vm-state-lable').empty();
				$('#' + vmId + ' td.vm-state-lable').append("<span class='label label-danger'>Stopped</span>");
				$('#' + vmId + ' td.vm-stop-or-start-btn').empty();
				$('#' + vmId + ' td.vm-stop-or-start-btn').append("<button type='button' class='btn btn-xs btn-success' onclick='startOcsVm(\"" + vmId + "\")'>开启</button>");
			}
			else {
				alert("OCS VM停止失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#stop-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#stop-city-vm-modal div.loader").remove();
			$("#stop-city-vm-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}
//===================================================================================

//===================================================================================
//弹出开启虚拟机模态框
function startOcsVm(vmId) {
	$('#start-city-vm-modal').modal({
		keyboard: true
	});
	$("#start-city-vm-modal div.modal-footer").empty();
	$("#start-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>");
	$("#start-city-vm-modal div.modal-footer").append("<button type='button' class='btn btn-primary' onclick=\"startOcsVmSubmit('" + vmId + "')\">" + "确认</button>");
}

function startOcsVmSubmit(vmId) {
	//添加转菊花
	$("#start-city-vm-modal div.modal-body").append("<div class='loader'></div>");
	$("#start-city-vm-modal div.modal-body").append("<span class='text-primary pull-right'>正在开启，过程稍长，请耐心等待...</span>");
	$("div.loader").shCircleLoader({
		duration: 0.75
	});

	$.ajax({
		type: "GET",
		url: "business/startOcsVm",
		dataType: "json",
		data: {
			vmId: vmId
		},
		success: function(data) {
			$("#start-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#start-city-vm-modal div.loader").remove();
			$("#start-city-vm-modal span.text-primary.pull-right").remove();
			if (data.code == 20000000) { //开启成功
				alert("OCS VM开启成功！");
				$('#start-city-vm-modal').modal('hide');
				$('#' + vmId + ' td.vm-hostname').empty();
				$('#' + vmId + ' td.vm-hostname').append(data.operatedObject);
				$('#' + vmId + ' td.vm-state-lable').empty();
				$('#' + vmId + ' td.vm-state-lable').append("<span class='label label-success'>Running</span>");
				$('#' + vmId + ' td.vm-stop-or-start-btn').empty();
				$('#' + vmId + ' td.vm-stop-or-start-btn').append("<button type='button' class='btn btn-xs btn-warning' onclick='stopOcsVm(\"" + vmId + "\")'>停止</button>");
			}
			else {
				alert("OCS VM开启失败！Msg:" + data.message);
			}
		},
		error: function(xhr, status) {
			$("#start-city-vm-modal div.loader").shCircleLoader('destroy');
			$("#start-city-vm-modal div.loader").remove();
			$("#start-city-vm-modal span.text-primary.pull-right").remove();
			alert(status);
		}
	});
}