$( "#login-btn" ).click(function() {
	$("#login-warn").remove();
	var accountId = $("#accountId").val();
	var accountPassword = $("#accountPassword").val();

	if (accountId == "") {
		$(".submit-area").append("<span class=\"text-danger\" id=\"login-warn\">账号不能为空！</span>");
		return;
	}
	if (accountPassword == "") {
		$(".submit-area").append("<span class=\"text-danger\" id=\"login-warn\">密码不能为空！</span>");
		return;
	}

	$.ajax({
		type: "POST",
		url: "login",
		data: {
			accountId: $("#accountId").val(), 
			accountPassword: $("#accountPassword").val()
		},
		dataType: "json",
		success: function(data) {
			$("#login-warn").remove();
			switch (data) {
				case 1:
					$(".submit-area").append("<span class=\"text-success\" id=\"login-warn\">验证成功！正在跳转......</span>");
					window.location = "index";
					break;
				case 2:
					$(".submit-area").append("<span class=\"text-danger\" id=\"login-warn\">账号错误！</span>");
					break;
				case 3:
					$(".submit-area").append("<span class=\"text-danger\" id=\"login-warn\">密码错误！</span>");
					break;
				default:
					break;	
			}
		},
		error: function( xhr, status ) {
			alert(status); 
		} 
	});
});