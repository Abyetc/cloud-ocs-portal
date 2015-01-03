/*
  系统前端页面云业务管理模块 逻辑入口
  包括：slide、breadcrumb、及城市列表、添加城市相关逻辑
*/

//对breadcrumb的点击行为进行处理
$("body").on("click", 'ol.breadcrumb.breadcrumb-ocs-service li a', function(){
  var cmd = $(this).attr("cmd");
  if (cmd == "listCities") {
    var firstLevelTitle = $(this).parent().text(); // $(this)是<a>，$(this).parent()才是<li>
    $(this).parent().next().remove();
    $(this).parent().addClass("active");
    $(this).parent().empty();
    $("ol.breadcrumb.breadcrumb-ocs-service li.active").append("<span class='glyphicon glyphicon-cloud'></span>" + firstLevelTitle);
    listCloudOCSServiceCities();
  }
});

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
  $("#content-area").empty();
  //添加服务城市按钮
  $("#content-area").append("<button type='button' class='btn btn-success btn-sm pull-right' id='add-city-btn'><span class='glyphicon glyphicon-plus'></span>  点击新增服务城市</button>");
  //弹出添加城市表单的模态框
  $("#add-city-btn").after("<div class='modal fade' id='add-city-modal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
                     +  "<div class='modal-dialog modal-dialog-center'>"
                     +    "<div class='modal-content'>"
                     +      "<div class='modal-header'>"
                     +        "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
                     +        "<h4 class='modal-title'>新增云计费系统服务城市</h4>"
                     +      "</div>"
                     +      "<div class='modal-body'>"
                     +            "<div class='form-horizontal' role='form'>"
                     +          "<div class='form-group'>"
                     +            "<label for='' class='col-lg-4 control-label'>城市名称：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<input type='text' class='form-control' id='city-name' placeholder='请输入城市名称'>"
                     +            "</div>"
                     +          "</div>"
                     +          "<div class='form-group'>"
                     +            "<label for='' class='col-lg-4 control-label'>描述：</label>"
                     +            "<div class='col-lg-8'>"
                     +              "<textarea rows='5' class='form-control' id='city-description' placeholder='请输入相关描述'></textarea>"
                     +            "</div>"
                     +          "</div>"
                     +        "</div>"
                     +      "</div>"
                     +      "<div class='modal-footer'>"
                     +        "<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>"
                     +        "<button type='button' class='btn btn-primary' onclick=\"addCity()\">" + "提交</button>"
                     +      "</div>"
                     +    "</div>"
                     +  "</div>"
                     + "</div>");
      
  //已有的服务城市列表
  var cloudOCSServiceCitiesTable = $("<table class='table table-bordered text-center'>"
    + "<caption><strong>云在线计费系统服务城市列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>城市名称</th><th>状态</th><th>创建时间</th><th>查看详情</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");
  $("#content-area").append(cloudOCSServiceCitiesTable);
  $("#content-area").append("<div class='loader'></div>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });

  //请求获取服务城市列表数据
  $.ajax({
    type: "GET",
    url: "business/listCities",
    dataType: "json",
    success: function(data) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      for (var i = 0; i < data.length; i++) {
        var state;
        var created = new Date(data[i].created);
        switch (data[i].state) {
          case 0:
            state = "<td><span class='label label-default'>无服务</span></td>";
            break;
          case 1:
            state = "<td><span class='label label-success'>正常服务中</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>发生故障</span></td>";
            break;
          default:
            break;
        }
        $(".table tbody").append("<tr><td>" + (i+1) + "</td><td>" + data[i].name + "</td>" 
          + state 
          + "<td>" + created.Format("yyyy-MM-dd hh:mm:ss") + "</td>" 
          + "<td><button type='button' class='btn btn-primary btn-xs city-detail-btn-" + (i+1) + "'>点击查看</button></td></tr>");
        $(".table tbody button.btn.btn-primary.btn-xs.city-detail-btn-" + (i+1)).on("click", {cityDetail: data[i]}, listNetworks);
      }
    },
    error: function(xhr, status) {
      $("div.loader").shCircleLoader('destroy');
      $("div.loader").remove();
      alert(status);
    }
  });
}

//对“新增服务城市”按钮进行监听(弹出模态框)
$("body").on("click", '#add-city-btn', function() {
  $('#add-city-modal').modal({
    keyboard: true
  });
});

//处理添加城市表单提交
function addCity() {
  var cityName = $("#city-name").val();
  var cityDescription = $("#city-description").val();

  if (cityName.trim() == "") {
    alert("城市名字不能为空！");
    return;
  }

  //添加转菊花
  $("#add-city-modal div.modal-body").append("<div class='loader'></div>");
  $("#add-city-modal div.modal-body").append("<span class='text-primary pull-right'>正在添加，请耐心等待...</span>");
  $("div.loader").shCircleLoader({
    duration: 0.75
  });

  $.ajax({
    type: "POST",
    url: "business/addCity",
    dataType: "json",
    data: {
      cityName: cityName.trim(),
      cityDescription: cityDescription.trim()
    },
    success: function(data) {
      $("#add-city-modal div.loader").shCircleLoader('destroy');
      $("#add-city-modal div.loader").remove();
      $("#add-city-modal span.text-primary.pull-right").remove();
      if (data.code == 20000000) { //添加成功
        $("#city-name").val('');
        $("#city-description").val('');
        alert("新城市添加成功！");
        $('#add-city-modal').modal('hide');

        //添加新增城市到列表中
        var state;
        var created = new Date(data.city.created);
        switch (data.city.state) {
          case 0:
            state = "<td><span class='label label-default'>无服务</span></td>";
            break;
          case 1:
            state = "<td><span class='label label-success'>正常服务中</span></td>";
            break;
          case 2:
            state = "<td><span class='label label-danger'>发生故障</span></td>";
            break;
          default:
            break;
        }
        $(".table tbody").append("<tr><td>" + data.index 
          + "</td><td>" + data.city.name + "</td>" 
          + state + "<td>" + created.Format("yyyy-MM-dd hh:mm:ss") + "</td>" 
          + "<td><button type='button' class='btn btn-primary btn-xs city-detail-btn-" + data.index + "'>点击查看</button></td></tr>");
        $(".table tbody button.btn.btn-primary.btn-xs.city-detail-btn-" + data.index).on("click", {cityDetail: data.city}, listNetworks);
      } else {
        alert("城市添加失败！消息：" + data.message);
      }
    },
    error: function(xhr, status) {
      $("#add-city-modal div.loader").shCircleLoader('destroy');
      $("#add-city-modal div.loader").remove();
      $("#add-city-modal span.text-primary.pull-right").remove();
      alert(status);
    }
  });
}