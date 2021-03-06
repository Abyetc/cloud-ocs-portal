<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<title>中国移动广东公司云在线计费系统管理平台</title>
<!-- 包含头部信息用于适应不同设备 -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<!-- 包含 bootstrap 样式表 -->
<link rel="stylesheet" href="front/css/style.css">
<link rel="stylesheet" href="front/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="front/jQuery/jquery-ui/jquery-ui.css">
</head>

<body>
	<div class="container">
		<div class="row header">
			<div class="col-lg-10">
				<img src="front/img/logo.gif">
			</div>
			<div class="col-lg-2">
				<p class="text-right">
					欢迎, ${loginUser.name} | <a href="login" id="logout">退出</a>
				</p>
			</div>
		</div>

		<div class="row content-warpper">
			<div class="col-xs-3" id="sidebar">
				<ul class="nav nav-pills nav-stacked">
					<li class="dropdown active"><a href="javascript:void(0);"
						class="dropdown-toggle" data-toggle="dropdown"><span
							class="glyphicon glyphicon-home"></span>&nbsp;&nbsp;基础设施资源管理<b
							class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li class="sidebar-cloud-resource-infrastructure"><a
								href="javascript:void(0);">系统基础设施</a></li>
							<li class="divider"></li>
							<li class="sidebar-cloud-resource-capacity"><a
								href="javascript:void(0);">系统容量</a></li>
						</ul>
					</li>
					<li class="sidebar-cloud-ocs-service"><a
						href="javascript:void(0);"><span
							class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;计费业务管理</a></li>
					<li class="dropdown"><a href="javascript:void(0);"
						class="dropdown-toggle" data-toggle="dropdown"><span
							class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;系统监控<b
							class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li class="sidebar-cloud-ocs-host-monitor"><a
								href="javascript:void(0);">物理资源监控</a></li>
							<li class="divider"></li>
							<li class="sidebar-cloud-ocs-vm-monitor"><a
								href="javascript:void(0);">业务服务监控</a></li>
						</ul>
					</li>
					<li class="dropdown "><a href="javascript:void(0);"
						class="dropdown-toggle" data-toggle="dropdown"><span
							class="glyphicon glyphicon-tasks"></span>&nbsp;&nbsp;警报<b
							class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li class="sidebar-infrastructure-alert">
							<a href="javascript:void(0);">基础设施警报</a>
							</li>
							<li class="divider"></li>
							<li class="sidebar-business-alert">
							<a href="javascript:void(0);">计费业务警报</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<div class="col-xs-9 content">
				<div class="alert alert-info">
					<span class="glyphicon glyphicon-bullhorn"></span>&nbsp;&nbsp;欢迎进入
					中国移动广东公司云在线计费系统管理平台!
				</div>

			</div>
		</div>
	</div>

	<div class="row footer">
		<p class="text-center">©2014-2015 中国移动通信版权所有</p>
	</div>

	<!-- JavaScript 放置在文档最后面可以使页面加载速度更快 -->
	<!-- jQuery 库 -->
	<script src="front/jQuery/jquery-2.1.1.min.js"></script>
	<script src="front/jQuery/jquery-ui/jquery-ui.js"></script>
	<!-- Bootstrap JavaScript 插件 -->
	<script src="front/bootstrap/js/bootstrap.min.js"></script>
	<!-- Highcharts 插件 -->
<!-- 	<script src="front/Highcharts/highcharts.js"></script> -->
	<script src="front/Highcharts/Highstock/highstock.js"></script>
	<script src="front/Highcharts/exporting.js"></script>
	<!-- 		<script src="front/Highcharts/theme/grid-light.js"></script> -->
	<!-- JQuery用于loading效果的插件shCircleLoader -->
	<script src="front/jQuery/shCircleLoader/jquery.shCircleLoader-min.js"></script>

	<!-- Cloud OCS Portal的JS文件 -->
	<script src="front/js/infrastructure/cloud-infrastructure.js"></script>
	<script src="front/js/infrastructure/cloud-capacity.js"></script>
	
	<script src="front/js/service/cloud-ocs-service.js"></script>
	<script src="front/js/service/cloud-ocs-service-network.js"></script>
	<script src="front/js/service/cloud-ocs-service-vm.js"></script>
	
	<script src="front/js/monitor/physics/physics-monitor-host.js"></script>
	<script src="front/js/monitor/physics/physics-monitor-host-dynamic-chart.js"></script>
	<script src="front/js/monitor/physics/physics-monitor-vm.js"></script>
	<script src="front/js/monitor/physics/physics-monitor-vm-dynamic-chart.js"></script>
	
	<script src="front/js/monitor/service/service-monitor-city.js"></script>
	<script src="front/js/monitor/service/service-monitor-city-dynamic-chart.js"></script>
	<script src="front/js/monitor/service/service-monitor-network.js"></script>
	<script src="front/js/monitor/service/service-monitor-network-dynamic-chart.js"></script>
	<script src="front/js/monitor/service/service-monitor-vm.js"></script>
	<script src="front/js/monitor/service/service-monitor-vm-dynamic-chart.js"></script>
	
	<script src="front/js/alert/infrastructure-alert.js"></script>
	<script src="front/js/alert/business-alert.js"></script>
	
	<script src="front/js/utils.js"></script>
	<script src="front/js/common.js"></script>
</body>

</html>
