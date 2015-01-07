/*
  系统前端页面云业务管理模块 
  包括：城市详情、城市网络列表、添加城市网络逻辑的实现
*/

//获取城市所属网络列表
function listNetworks(event) {
  window.listNetworksEvent = event;
  var cityDetail = event.data.cityDetail;
  //先处理面包屑导航栏
  var firstLevelTitle = $("ol.breadcrumb.breadcrumb-ocs-service li.active").text();
  $("ol.breadcrumb.breadcrumb-ocs-service li.active").text("");
  $("ol.breadcrumb.breadcrumb-ocs-service li.active").append("<a href='javascript:void(0);' cmd='listCities'><span class='glyphicon glyphicon-cloud'></span>" + firstLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-ocs-service li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-ocs-service").append("<li class='active'>" + cityDetail.name + "</li>");
  $("#content-area").empty();

  var cityDetailTable = $("<table class='table table-hover text-left-table'>"
    + "<caption><strong>服务城市 " + cityDetail.name + " 详细信息</strong></caption>"
    + "</table>");
  $("#content-area").append(cityDetailTable);
  //插入城市详细信息
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>城市名称</td><td>" + cityDetail.name + "</td></tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>描述</td><td>" + cityDetail.description + "</td></tr>");
  var state;
  var created = new Date(cityDetail.created);
  switch (cityDetail.state) {
    case 0:
      state = "<td><span class='label label-default'>无服务</span></td>";
      break;
    case 1:
      state = "<td><span class='label label-success'>服务中</span></td>";
      break;
    case 2:
      state = "<td><span class='label label-danger'>发生故障</span></td>";
      break;
    default:
      break;
  }
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>状态</td>" + state + "</tr>");
  $(".table.table-hover.text-left-table").append("<tr><td class='table-left-head'>创建时间</td><td>" + created.Format("yyyy-MM-dd hh:mm:ss") + "</td></tr>");

  //添加服务城市网络按钮
  $("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-city-network-btn' style='margin-top:100px;'><span class='glyphicon glyphicon-plus'></span>  点击新增服务网络</button>");
  
  //弹出添加城市网络表单的模态框
  $("#add-city-network-btn").after("<div class='modal fade' id='add-city-network-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>新增云计费系统服务网络</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +            "<div class='form-horizontal' role='form'>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-network-name' class='col-lg-4 control-label'>网络名称：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<input type='text' class='form-control' id='city-network-name' placeholder='请输入网络名称'>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-network-realm' class='col-lg-4 control-label'>域名：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<input type='text' class='form-control' id='city-network-realm' placeholder='请输入域名'>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-network-zone' class='col-lg-4 control-label'>区域：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-network-zone'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='city-network-networkoffering' class='col-lg-4 control-label'>网络方案：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<select class='form-control' id='city-network-networkoffering'>"
                     +              "</select>"
                     +            "</div>"
                     +          "</div>"
                     +        "</div>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +        "<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>"
                     +        "<button type='button' class='btn btn-primary' onclick=\"addCityNetwork(" + cityDetail.id + ")\">" + "提交</button>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");

  //已有的服务城市网络列表
  var cityNetworksTable = $("<table class='table table-bordered text-center' id='city-network-table'>"
    + "<caption><strong>服务网络列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>网络名称</th><th>公共IP</th><th>域名</th>"
    + "<th>创建时间</th><th>状态</th><th>查看VM</th><th>删除</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cityNetworksTable);
  //转菊花
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });
  $.ajax({
    type: "GET",
    url: "business/listCityNetworks",
    dataType: "json",
    data: {
      cityId: cityDetail.id
    },
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        var created = new Date(data[i].created);
        var state = "";
        switch (data[i].networkState) {
          case 0:
            state = "<td><span class='label label-default'>Allocated</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>Implementing</span></td>";
            break;
          case 3:
            state = "<td><span class='label label-success'>Implemented</span></td>";
            break;
          default:
            break;
        }
        var publicIp = (!data[i].publicIp ? '-' : data[i].publicIp);
        $("#city-network-table").append("<tr>" 
          + "<td>" + (i+1) + "</td>"
          + "<td>" + data[i].networkName + "</td>"
          + "<td>" + publicIp + "</td>"
          + "<td>" + data[i].realmName + "</td>"
          + "<td>" + created.Format("yyyy-MM-dd hh:mm:ss") + "</td>"
          + state
          + "<td><button type='button' class='btn btn-xs btn-link network-vm-btn-" + (i+1) + "'>" + data[i].vmNum + "</button></td>"
          + "<td>" + "<button type='button' class='btn btn-xs btn-danger'>删除</button>" + "</td>"
          + "</tr>");
        $("#city-network-table tbody button.btn.btn-xs.btn-link.network-vm-btn-" + (i+1)).on("click", {networkId: data[i].networkId, networkName: data[i].networkName}, listNetworkVms);
      }
    },
    error: function(xhr, status) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
    }
  });
}

//对“新增服务网络”按钮进行监听(弹出模态框)
$("body").on("click", '#add-city-network-btn', function() {
  $('#add-city-network-modal').modal({
    keyboard: true
  });
  var zoneSelectOption = "";
  var networkOfferingSelectOption = "";
  $("#city-network-zone").empty();
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
      $("#city-network-zone").append(zoneSelectOption);
    },
    error: function(xhr, status) {}
  });
  //获取network offering列表
  $("#city-network-networkoffering").empty();
  $.ajax({
    type: "GET",
    url: "resource/infrastructure/listIsolatedNetworkOfferingsWithSourceNatService",
    dataType: "json",
    data: {},
    success: function(data) {
      for (var i = 0; i < data.length; i++) {
        networkOfferingSelectOption += "<option value='" + data[i].networkOfferingId + "'>" + data[i].networkOfferingName + "</option>";
      }
      $("#city-network-networkoffering").append(networkOfferingSelectOption);
    },
    error: function(xhr, status) {}
  });
});

function addCityNetwork(cityId) {
  var networkName = $("#city-network-name").val();
  var realmName = $("#city-network-realm").val();
  var zoneId = $("#city-network-zone").val();
  var networkOfferingId = $("#city-network-networkoffering").val();

  if (networkName.trim() == "") {
    alert("网络名字不能为空！");
    return;
  }

  //添加转菊花
  $("#add-city-network-modal div.modal-body").append("<div class='loader'></div>");
  $("#add-city-network-modal div.modal-body").append("<span class='text-primary pull-right'>正在添加，请耐心等待...</span>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });

  $.ajax({
    type: "POST",
    url: "business/addCityNetwork",
    dataType: "json",
    data: {
      cityId: cityId,
      networkName: networkName.trim(),
      realmName: realmName.trim(),
      zoneId: zoneId,
      networkOfferingId: networkOfferingId
    },
    success: function(data) {
      $("#add-city-network-modal div.loader").shCircleLoader('destroy');
      $("#add-city-network-modal div.loader").remove();
      $("#add-city-network-modal span.text-primary.pull-right").remove();
      if (data.code == 20000000) { //添加成功
        $("#city-network-name").val('');
        $("#city-network-realm").val('');
        alert("服务城市网络添加成功！");
        $('#add-city-network-modal').modal('hide');
        //添加新增数据到表格中
        var created = new Date(data.cityNetwork.created);
        var state;
        switch (data.cityNetwork.networkState) {
          case 0:
            state = "<td><span class='label label-default'>Allocated</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>Implementing</span></td>";
            break;
          case 3:
            state = "<td><span class='label label-success'>Implemented</span></td>";
            break;
          default:
            break;
        }
        var publicIp = (!data.cityNetwork.publicIp ? '-' : data.cityNetwork.publicIp);
        $("#city-network-table").append("<tr>" 
          + "<td>" + data.index  + "</td>"
          + "<td>" + data.cityNetwork.networkName + "</td>"
          + "<td>" + publicIp + "</td>"
          + "<td>" + data.cityNetwork.realmName + "</td>"
          + "<td>" + created.Format("yyyy-MM-dd hh:mm:ss") + "</td>"
          + state
          + "<td><button type='button' class='btn btn-xs btn-link network-vm-btn-" + data.index + "'>" + data.vmNum + "</button></td>"
          + "<td>" + "<button type='button' class='btn btn-xs btn-danger'>删除</button>" + "</td>"
          + "</tr>");
        $("#city-network-table tbody button.btn.btn-xs.btn-link.network-vm-btn-" + data.index).on("click", {networkId: data.cityNetwork.networkId, networkName: data.cityNetwork.networkName}, listNetworkVms);
      }
      else {
        $("#city-network-name").val('');
        $("#city-network-realm").val('');
        alert("服务城市网络添加失败！");
      }
    },
    error: function(xhr, status) {
      $("#add-city-network-modal div.loader").shCircleLoader('destroy');
      $("#add-city-network-modal div.loader").remove();
      $("#add-city-network-modal span.text-primary.pull-right").remove();
      alert(status);
    }
  });
}