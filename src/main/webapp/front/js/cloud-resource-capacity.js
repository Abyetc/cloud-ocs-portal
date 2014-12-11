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
  $("div.content").append($("<div id=\"content-area\"></div>"));

  //插入面包屑导航栏
  $("div.content").prepend($("<ol class=\"breadcrumb breadcrumb-resource-capacity\"></ol>"));

  var preActiveItem = $("ul.nav li.active");
  preActiveItem.removeClass("active");
  var curActiveItem = $(this).parentsUntil("ul.nav");
  curActiveItem.addClass("active");
  var firstLevelTitle = curActiveItem.children("a").text().trim();
  var secondLevelTitle = $(this).text();
  $("ol.breadcrumb.breadcrumb-resource-capacity").empty();
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li><a href=\"javascript:void(0);\" cmd=\"cloudResourceCapacityHome\"><span class=\"glyphicon glyphicon-home\"></span>&nbsp;&nbsp;" + firstLevelTitle + "</a></li>");
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li class=\"active\">" + secondLevelTitle + "</li>");

  listCapacityZones();
});

//处理获取系统资源容量Zone列表的请求
function listCapacityZones() {
  $("#content-area").empty();

  var zoneListTable = $("<table class=\"table table-bordered text-center\">"
    + "<caption>系统区域（机房）列表</caption>"
    + "<thead><tr><th>序号</th><th>区域（机房）名称</th><th>网络类型</th><th>状态</th><th>查看</th></tr></thead>"
    + "<tbody></tbody>"
    + "</table>");  
  $("#content-area").append(zoneListTable);
  //请求获取区域列表数据
  $(".table tbody").append("<tr><td>1</td><td>zoneName</td><td>高级网络</td>"
    + "<td><span class=\"label label-success\">成功标签</span></td>"
    + "<td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"listCapacity('zoneId', 'zoneName');\">点击查看</button></td></tr>");
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
    text: '<strong>CPU</strong><br>50/100',
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
    data: [
    ['已使用',   45.0],
    ['未使用',   50.0],
    {
      name: 'Point 1',
      color: '#00FF00',
      y: 5.0
    }
    ]
  }],
  credits: { 
    enabled:false
  },
  exporting: {
    enabled:false
  }
};

function listCapacity(zoneId, zoneName) {
  var secondLevelTitle = $("ol.breadcrumb.breadcrumb-resource-capacity li.active").text();
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").text("");
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").append("<a href=\"javascript:void(0);\" cmd=\"listCapacityZones\">" + secondLevelTitle + "</a>");
  $("ol.breadcrumb.breadcrumb-resource-capacity li.active").removeClass("active");
  $("ol.breadcrumb.breadcrumb-resource-capacity").append("<li class=\"active\">" + zoneName + "</li>");
  
  $("#content-area").empty();
  $("#content-area").append("<h4 class=\"text-center\">区域（机房）" + zoneName + "系统容量</h4>");
  var dataLength = 8;
  for (var i = 0;  i < dataLength; i++) {
    if (i % 2 == 0) {
      $("#content-area").append("<div class=\"row row-" + (i+2)/2 + "\"></div>");
      $(".row-" + (i+2)/2).append("<div class=\"col-xs-6 capacity-pie-" + (i+1) + "\"></div>");
      $(".capacity-pie-" + (i+1)).highcharts(capacityPieOption);
    }
    if (i % 2 == 1) {
      $(".row-" + (i+1)/2).append("<div class=\"col-xs-6 capacity-pie-" + (i+1) + "\"></div>");
      $(".capacity-pie-" + (i+1)).highcharts(capacityPieOption);
    }
  }
}