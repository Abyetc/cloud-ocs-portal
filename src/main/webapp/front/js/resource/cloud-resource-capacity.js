/*
  系统前端页面资源管理模块-系统容量 逻辑入口
  包括：slide、breadcrumb、及所有逻辑
*/

//对breadcrumb的点击行为进行处理
$("body").on("click", 'ol.breadcrumb.breadcrumb-resource-capacity li a', function(){
  var cmd = $(this).attr("cmd");
  if (cmd == "cloudResourceCapacityHome") {
    $(this).parent().next().nextAll().remove();
    $(this).parent().next().addClass("active");
    $(this).parent().next().text($(this).parent().next().text());
    listCapacityZones();
  }
  if (cmd == "listCapacityZones") {
    $(this).parent().nextAll().remove();
    $(this).parent().addClass("active");
    $(this).parent().text($(this).text());
    listCapacityZones();
  }
});

//对sidebar的点击行为进行处理
$( ".sidebar-cloud-resource-capacity" ).click(function() {
  //删除原先内容
  $("div.content").empty();

  //插入内容显示区域
  $("div.content").append($("<div id='content-area'></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class='breadcrumb breadcrumb-resource-capacity' id='nav-breadcrumb'></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-resource-capacity").empty();
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li><a href='javascript:void(0);' cmd='cloudResourceCapacityHome'><span class='glyphicon glyphicon-home'></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li class='active'>" + secondLevelTitle + "</li>");

  listCapacityZones();
});

//处理获取系统资源容量Zone列表的请求
function listCapacityZones() {
  $("#content-area").empty();

  var zoneListTable = $("<table class='table table-bordered text-center'>"
    + "<caption><strong>系统区域（机房）列表</strong></caption>"
    + "<thead><tr><th>序号</th><th>区域（机房）名称</th><th>网络类型</th><th>状态</th><th>查看</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");  
  $("#content-area").append(zoneListTable);
  $("#content-area").append("<div class='loader'></div>");
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
          + (data[i].allocationState == "Enabled" ? "<td><span class='label label-success'>Enabled</span></td>" : "<td><span class='label label-danger'>Disabled</span></td>")
          + "<td><button type='button' class='btn btn-primary btn-xs' onclick=\"listCapacity('" + data[i].zoneId + "','" + data[i].zoneName + "');\">点击查看</button></td></tr>");
      }
    },
    error: function( xhr, status ) {
      alert(status); 
    } 
  });
}

Highcharts.setOptions({
  global: {
    useUTC: false
  }
});

var capacityPieOption = {
  chart: {
    plotBackgroundColor: null,
    plotBorderWidth: 1,
    plotShadow: false
  },
  title: {
    //text: '<strong>CPU</strong><br>50/100',
    align: 'center',
    verticalAlign: 'middle',
    y: 50
  },
  tooltip: {
    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
  },
  plotOptions: {
    pie: {
      dataLabels: {
        enabled: true,
        distance: -50,
        style: {
          fontWeight: 'bold',
          color: 'white',
          textShadow: '0px 1px 2px black'
        }
      },
      startAngle: -90,
      endAngle: 90,
      center: ['50%', '75%']
    }
  },
  series: [{
    type: 'pie',
    name: '比重',
    innerSize: '50%',
    data: [{
      name: '已使用',
      color: '#FF7070',
      //y: 25.0
    }, {
      name: '未使用',
      //color: '#00FF00', 
      //y: 75.0
    }]
  }],
  credits: {
    enabled: false
  },
  exporting: {
    enabled: false
  }
};

function listCapacity(zoneId, zoneName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-resource-capacity li.active").text();
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").text("");
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").append("<a href='javascript:void(0);' cmd='listCapacityZones'>" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li class='active'>" + zoneName + "</li>");
  
  $("#content-area").empty();
  $("#content-area").append("<h4 class='text-center'>区域（机房）" + zoneName + "系统容量</h4>");

  $.ajax({
    type: "GET",
    url: "resource/capacity/listCapacity",
    dataType: "json",
    data: {
      zoneId: zoneId
    },
    success: function(data) {
      for (var i = 0; i < data.length; i++) {
        var oneCapacityPieOption = capacityPieOption;
        oneCapacityPieOption.title.text = "<strong>" + data[i].capacityName + "</strong><br><sapn style='font-size:11px;'>" + data[i].capacityUsed + "/" + data[i].capacityTotal + "</span>";
        oneCapacityPieOption.series[0].data[0].y = data[i].percentUsed;
        oneCapacityPieOption.series[0].data[1].y = 100 - data[i].percentUsed;
        if (i % 2 == 0) {
          $("#content-area").append("<div class='row row-" + (i+2)/2 + "'></div>");
          $(".row-" + (i+2)/2).append("<div class='col-xs-6 capacity-pie-" + (i+1) + "'></div>");
          $(".capacity-pie-" + (i+1)).highcharts(oneCapacityPieOption);
        }
        if (i % 2 == 1) {
          $(".row-" + (i+1)/2).append("<div class='col-xs-6 capacity-pie-" + (i+1) + "'></div>");
          $(".capacity-pie-" + (i+1)).highcharts(oneCapacityPieOption);
        }
      }
    },
    error: function( xhr, status ) {
      alert(status); 
    } 
  });
}