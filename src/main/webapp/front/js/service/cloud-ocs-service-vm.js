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
	
	//已有的服务城市VM列表
	var cityVmsTable = $("<table class='table table-bordered text-center' id='city-vm-table'>"
	    + "<caption><strong>服务网络" + networkName + " OCS VM列表</strong></caption>"
	    + "<thead><tr><th>序号</th><th>VM名称</th><th>区域</th><th>主机</th>"
	    + "<th>创建时间</th><th>状态</th><th>删除</th></tr></thead>"
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
				switch (data[i].vmState) {
					case 0:
					case 2:
					case 4:
					case 6:
						state = "<td><span class='label label-warning'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					case 3:
					case 5:
					case 7:
					case 8:
					case 9:
						state = "<td><span class='label label-danger'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					case 1:
						state = "<td><span class='label label-success'>" + vmStateArray[data[i].vmState] + "</span></td>";
						break;
					default:
						break;
				}
				$("#city-vm-table").append("<tr>"
					+ "<td>" + (i+1) + "</td>"
					+ "<td>" + data[i].vmName + "</td>"
					+ "<td>" +  data[i].zoneName + "</td>"
					+ "<td>" + data[i].hostName + "</td>"
					+ "<td>" + data[i].created + "</td>"
					+ state
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger'>删除</button>" + "</td>"
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

//对“新增服务Vm”按钮进行监听(弹出模态框)
$("body").on("click", '#add-city-vm-btn', function() {
	$('#add-city-vm-modal').modal({
		keyboard: true
	});
	var zoneSelectOption = "";
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
			templateId: templateId
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
						state = "<td><span class='label label-warning'>" + vmStateArray[data.ocsVmDto.vmState] + "</span></td>";
						break;
					case 3:
					case 5:
					case 7:
					case 8:
					case 9:
						state = "<td><span class='label label-danger'>" + vmStateArray[data.ocsVmDto.vmState] + "</span></td>";
						break;
					case 1:
						state = "<td><span class='label label-success'>" + vmStateArray[data.ocsVmDto.vmState] + "</span></td>";
						break;
					default:
						break;
				}
				$("#city-vm-table").append("<tr>"
					+ "<td>" + data.index + "</td>"
					+ "<td>" + data.ocsVmDto.vmName + "</td>"
					+ "<td>" +  data.ocsVmDto.zoneName + "</td>"
					+ "<td>" + data.ocsVmDto.hostName + "</td>"
					+ "<td>" + data.ocsVmDto.created + "</td>"
					+ state
					+ "<td>" + "<button type='button' class='btn btn-xs btn-danger'>删除</button>" + "</td>"
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