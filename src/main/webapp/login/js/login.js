function usercenter_register() {
	// alert("用户注册！");
	usercenter_login_main_close();
}
function usercenter_login() {
	 $("#login-main").show(); 
	/*var r=Math.random(); 
	var url = "login.htm?r=" + r;
	var params = {
		username:"123",
		userpassword:"1223"
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			alert(data.code);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath();
		}
	});*/
}
function usercenter_login_main_close() {
	$("#login-main").hide();
}