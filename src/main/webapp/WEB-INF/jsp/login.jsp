<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<title>登陆</title>
<!-- 包含头部信息用于适应不同设备 -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<!-- 包含 bootstrap 样式表 -->
<link rel="stylesheet" href="front/css/style.css">
<link rel="stylesheet" href="front/bootstrap/css/bootstrap.min.css">
</head>

<body>
	<div class="container login-container">
		<div class="row">
			<div class="col-lg-6">
				<img class="pull-right" src="front/img/logo.gif">
			</div>
			<div class="col-lg-6 login-form">
				<div class="form-horizontal " role="form">
					<div class="form-group">
						<label for="accountId" class="col-lg-2 control-label">账号</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" id="accountId"
								placeholder="请输入登陆账号">
						</div>
					</div>
					<div class="form-group">
						<label for="password" class="col-lg-2 control-label">密码</label>
						<div class="col-lg-10">
							<input type="password" class="form-control account-password" id="accountPassword"
								placeholder="请输入密码">
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-offset-2 col-lg-10">
							<div class="checkbox">
								<label> <input type="checkbox"> 请记住我 </label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-offset-2 col-lg-10 submit-area">
							<button type="submit" class="btn btn-primary" id="login-btn">登录</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

		<!-- JavaScript 放置在文档最后面可以使页面加载速度更快 -->
		<!-- jQuery 库 -->
		<script src="front/jQuery/jquery-2.1.1.min.js"></script>
		<!-- Bootstrap JavaScript 插件 -->
		<script src="front/bootstrap/js/bootstrap.min.js"></script>
		<!-- Login JavaScript -->
		<script src="front/js/user/user-login.js"></script>
</body>

</html>
