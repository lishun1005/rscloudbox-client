var codeswitch = 0;// 短信验证码开关 0是关，1是开
var loginFlag = 1;// 登陆开关： 1为弹出框登陆，2为页面登陆
var loginEdparams = undefined; // 登录前需要调用ajax执行的参数loginEdparams
var loginEdFun = undefined; // 登录前需要执行的方法
var loginEdUrl = rscloudmartHost;
var loginFlaging = false; // 是否正在登录
// var layerServerURL="http://10.0.68.183:8080/geowebcache/service/wms";
var layerServerURL = "http://210.77.87.225:8080/geowebcache/service/wms";
var backtoDefault = true;//判断页面右下角返回顶部，是否使用默认

//日期转换格式转换----函数
function formatDate(date,format)
{
	  var o = {
	    "M+" : date.getMonth()+1, //month
	    "d+" : date.getDate(),    //day
	    "h+" : date.getHours(),   //hour
	    "m+" : date.getMinutes(), //minute
	    "s+" : date.getSeconds(), //second
	    "q+" : Math.floor((date.getMonth()+3)/3),  //quarter
	    "S" : date.getMilliseconds() //millisecond
	  }
	  if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(date.getFullYear()+"").substr(4 - RegExp.$1.length));
	  for(var k in o) if(new RegExp("("+ k +")").test(format))
	      format = format.replace(RegExp.$1,
	      RegExp.$1.length==1 ? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
	  return format;
}

/**
 * 获取请求url的参数值
 * @param name   等号左边的key
 * @returns
 */
function getQueryString(name) 
{    
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");   
	var r = window.location.search.substr(1).match(reg);    
	if (r != null) 
		return unescape(r[2]); 
	return null;    
}

/**
 * 获取请求url的参数值
 * @param name   等号左边的key
 * @returns
 */
function isLastPathname(pathname) 
{    
	var pathnames = window.location.pathname.split("/");
	if(pathnames[pathnames.length-1] == pathname) return true;
	else return false;    
}

/**
 * 判断字符串时候为空，空就返回true，否则返回false
 * @param str
 * @returns {Boolean}
 */
function stringIsNull(str)
{
	if("0" == str)
	{
		return false;
	}
	if(null == str || "" == str || undefined == str)
	{
		return true;
	}
	return false;
}

/**
 * 去掉字符串空格
 * 
 * @param stringToTrim
 * @returns
 */
function g_trim(stringToTrim) {
	if (undefined == stringToTrim || null == stringToTrim) {
		return "";
	}
	return stringToTrim.replace(/^\s+|\s+$/g, "");
}

function getBaseLayer() {
	var baseLayer = new OpenLayers.Layer.WMS(
	// "电子地图", "http://58.252.5.46:8080/geowebcache/service/wms", {
	// "电子地图", "http://10.0.68.183:8080/geowebcache/service/wms", {
	"电子地图", layerServerURL, {
		layers : "mv-ne_sw-jpeg_90_2013_world_china-0gtscreen",
		format : "image/jpeg",
		styles : ''//,
//		transparent : true	// 不要设置，否则format为"image/jpeg"没有用
	}, {
		resolutions : [ 0.703125, 0.3515625, 0.17578125, 0.087890625,
				0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625,
				0.00274658203125, 0.001373291015625, 6.866455078125E-4,
				3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5 ], // ,
		// 4.291534423828125E-5, 2.1457672119140625E-5, 1.0728836059570312E-5, 5.364418029785156E-6],
		tileSize : new OpenLayers.Size(256, 256),
		tileOrigin : new OpenLayers.LonLat(-180, 90),
		isBaseLayer : true,
		// wrapDateLine:true,
		maxExtent : new OpenLayers.Bounds(-180.0, -90.0, 180.0, 90.0)
	});
	return baseLayer;
}

/**
 * 如果传递的value为空则返回true，否则为false。以下值均视为empty： - null - undefined - 数组元素内容为0的 -
 * 字符串长度为0的
 * 
 * @param obj
 * @param allowEmptyString
 * @returns boolean
 */
function g_isEmpty(value, allowEmptyString) {
	return (value === null) || (value === undefined)
			|| (!allowEmptyString ? value === '' : false)
			|| (g_isArray(value) && value.length === 0);

}
function g_isArray(value) {
	return Object.prototype.toString.call(value) === '[object Array]';
}

/**
 * 
 * @param phone
 *            检查手机号码格式
 * @returns {Boolean} 如果正确返回true,不正确返回false
 */
function checkPhone(phone) {
	var yidong = /^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$/;
	var liantong = /^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$/;
	var dianxin = /^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[019]{1}))[0-9]{8}$/;
	if (!phone.match(yidong) && !phone.match(liantong) && !phone.match(dianxin)) {
		return false;
	} else {
		return true;
	}
}

/**
 * 
 * @param email
 *            检查电子邮箱格式
 * @returns {Boolean} 如果正确返回true,不正确返回false
 */
function checkEmail(email) {
	return email
			.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);
}

function checkIllegalChar(m) {
	if ((m.indexOf("<") >= 0) || (m.indexOf(">") >= 0)
			|| (m.indexOf("\\") >= 0) || (m.indexOf("/") >= 0)
			|| (m.indexOf(":") >= 0) || (m.indexOf("*") >= 0)
			|| (m.indexOf("?") >= 0) || (m.indexOf("\"") >= 0)
			|| (m.indexOf("|") >= 0)) {
		return 0;
	} else
		return 1;
}

/* 随机数 */
function getRandom(n) {
	return Math.floor(Math.random() * n + 1);
}

/* 成功信息提示框 */
function alertSuccessDialog(content) {
	$("<p class='ok'>" + content + "</p>").dialog({
		title : "提示",
		modal : "true",
		resizable : "false",
		buttons : {
			"确定" : function() {
				$(this).dialog("close");
			}
		}
	});
}

/* 失败信息提示框 */
function alertFailedDialog(content) {
	$("<p class='error'>" + content + "</p>").dialog({
		title : "提示",
		modal : "true",
		resizable : "false",
		buttons : {
			"确定" : function() {
				$(this).dialog("close");
			}
		}
	});
}

/**
 * 限制input输入数字和一个小数点
 * 
 * @param obj
 *            this
 */
function clearNoNum(obj) {

	
	//先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.\-]/g,"");
	//保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	//保证只有出现一个-而没有多个-
	obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
	
	//- 只能是第一个
	if(obj.value.indexOf("-") != 0)
	{
		obj.value = obj.value.replace("-","");
	}
		
	//验证大小
	var id = obj.id;
	var value = parseFloat(obj.value)
	if(id == "ll_in1" || id == "ll_in2")
	{
		if(-180 >= value)
		{
			obj.value = "-180";
		}
		if(180 <= value)
		{
			obj.value = "180";
		}
		
 	}
	else
	{
		if(-90 >= value)
		{
			obj.value = "-90";
		}
		if(90 <= value)
		{
			obj.value = "90"
		}
	}
	
}

/**
 * 用户退出
 */
function userLogout() {
	if (confirm("是否要退出？")) {
		clearCookie("checkLoginImagesCode");
		var r = getRandom(999);
		var datatype = "json";
		var logouturl = userCenterHost + "/userlogout.htm?callback=?";
		if (userCenterHost.substring(0, 1) != '/')
			datatype = "jsonp";
		$.ajax({
			url : logouturl,
			data : {
				r : r
			},
			type : "get",
			dataType : datatype,
			success : function(data) {
//		$.getJSON(logouturl,{},function(data){
				var bbsurl1 = data.bbsurl1;

				// 保存登录token在cookie

				setCookie("um_session_token", "", 0);
				setCookie("um_session_id", "", 0);
				/*setCookie("um_session_token", "", 0,"index.rscloudmart-testzjw.com");
				setCookie("um_session_id", "", 0,"index.rscloudmart-testzjw.com");*/

				var url3 = bbsurl1.substr(bbsurl1.indexOf('src="') + 5);
				var url4 = url3.substr(url3.indexOf('src="') + 5);
				url3 = url3.substr(0, url3.indexOf('"'));
				url4 = url4.substr(0, url4.indexOf('"'));

				$.getScript(url3, function() {
					$.getScript(url4, function() {

					});
				});
				//window.location = rscloudmartHost;
				window.setTimeout("window.location=rscloudmartHost",2000); 
			}
		});
	}
}
$(function() {
	/**
	 * ajax封装 url:url 发送请求的地址 data:data 发送到服务器的数据，数组存储，如：{"date": new
	 * Date().getTime(), "state": 1} async:async 默认值:
	 * true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
	 * 注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。 type:type 请求方式("POST" 或 "GET")， 默认为
	 * "GET" dataType:dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
	 * successfn:successfn 成功回调函数 errorfn:errorfn 失败回调函数
	 */
	jQuery.ilajax = function(params) {
		// url, data, async, type, dataType, successfn, errorfn
		params.async = (params.async == null || params.async == "" || typeof (params.async) == "undefined") ? "true"
				: params.async;
		params.type = (params.type == null || params.type == "" || typeof (params.type) == "undefined") ? "post"
				: params.type;
		params.dataType = (params.dataType == null || params.dataType == "" || typeof (params.dataType) == "undefined") ? "json"
				: params.dataType;
		params.data = (params.data == null || params.data == "" || typeof (params.data) == "undefined") ? {
			"date" : new Date().getTime()
		}
				: params.data;

		// 调用后台的一个方法的返回值，判断用户是否已经登录
		var datatype = "json";
		var checkloginurl = userCenterHost + "/checklogin.htm";
		if (userCenterHost.substring(0, 1) != '/')
			datatype = "jsonp";
		$.ajax({
			url : checkloginurl,
			type : "get",
			dataType : datatype,
			success : function(data) {
				if (data.code == '1') {
					$.ajax({
						type : params.type,
						async : params.async,
						data : params.data,
						url : params.url,
						dataType : params.dataType,
						success : function(d) {
							params.successfn(d);
						}/*,
						error : function(e) {
							params.errorfn(e);
						}*/
					});
				} else {
					closeDiv("waiting");
					loginEdparams = params;
					$(".dialog-window").dialog("close");
					// $(".login-link").addClass("active");
					// $(".top-right .login-box").show();
					$(".login-box").show();
				}
			}
		});
	};
});

/**
 * 检测用户是否已经登录
 * 
 * @param needLogin
 *            是否要马上登录 true：弹出登录框，false：不作任何操作 return false 否登录 true已经登录
 * @param logined_function
 *            需要弹出登录框，登录成功后需要执行的回调函数,如：function(){alert('test');}
 */
function changeLoginFlag(needLogin, logined_function) {
	var datatype = "json";
	var checkloginurl = userCenterHost + "/checklogin.htm";
	if (userCenterHost.substring(0, 1) != '/')
		datatype = "jsonp";
	var result = false;

	$.ajax({
		async : false,
		url : checkloginurl,
		type : "post",
		dataType : datatype,
		success : function(data) {
			if (data.code == "1") {

				$("#loginDiv").show();
				$("#logoutDiv").hide();
				loginEdFun = null;
				// $("#loginDiv").removeClass("dn");
				// $("#logoutDiv").addClass("dn");
				// $(".current-user").html(data.username+"，欢迎回来");
				if (logined_function != null && logined_function != undefined)
					logined_function();

			} else {
				// $("#logoutDiv").removeClass("dn");
				// $("#loginDiv").addClass("dn");
				$("#loginDiv").hide();
				$("#logoutDiv").show();
				if (needLogin) {
					$(".login-box").show();
					if (logined_function != null
							&& logined_function != undefined)
						loginEdFun = logined_function;
				}

			}
		}
	});

	// return result;
}

function userLogin() {
	if (!loginFlaging) {
		loginFlaging=true;
		var url = userCenterHost+"/userlogin.htm";
		var username = $("[name='userName']");
		var password = $("[name='password']");
		var scode = $("[name='loginImageCode']");
		var userLoginErrorInfo=$(".login-box .error-info");
		if (loginFlag == 1) {
			 username = $(username[0]).val();
			 password = $(password[0]).val();
			 scode = $(scode[0]).val();
			 userLoginErrorInfo=$(userLoginErrorInfo[0]);
		}else{
			 username = $(username[1]).val();
			 password = $(password[1]).val();
			 scode = $(scode[1]).val();
			 userLoginErrorInfo=$(userLoginErrorInfo[1]);
		}
	
		if ("" == username) {
			userLoginErrorInfo.html("用户名不能为空哦！");
			userLoginErrorInfo.show();
			loginFlaging = false;
			return false;
		}
		 //拿到cookie的值，看看是否需要验证码
	    var checkLoginImagesCode = getCookie("checkLoginImagesCode");
	    if(false != checkLoginImagesCode && "true" == checkLoginImagesCode)
	    {
			if ("" == scode) {
				userLoginErrorInfo.html("验证码不能为空哦！");
				userLoginErrorInfo.show();
				loginFlaging = false;
				return false;
			}
	    }
		var params;
		var r = Math.floor(Math.random() * 9999 + 1);
		params = {
			r : r,
			userName : username,
			userPassword : hex_md5(password),
			deviceType : "",
			imageCode : scode
		};
		var datatype = "json";
		if (userCenterHost.substring(0, 1) != '/')
			datatype = "jsonp";

		userLoginErrorInfo.html("正在登录中........");
		userLoginErrorInfo.show();
		$
				.ajax({
					url : url,
					data : params,
					type : "get",
					dataType : datatype,
					success : function(data) {
						loginFlaging = false;
						userLoginErrorInfo.html("");
						userLoginErrorInfo.hide(); // 隐藏错误信息

						if (data.code == "1") {
							clearCookie("checkLoginImagesCode");
							var bbsurl = data.bbsurl;
							if (bbsurl != null && bbsurl != undefined
									&& bbsurl != "") {
								var url = bbsurl
										.substr(bbsurl.indexOf('src="') + 5);

								var url2 = url.substr(url.indexOf('src="') + 5);
								url = url.substr(0, url.indexOf('"'));
								url2 = url2.substr(0, url2.indexOf('"'));

								$.getScript(url, function() {
									$.getScript(url2, function() {

									});
								});
							}

							// 保存登录token在cookie
							var um_session_id = data.um_session_id;
							var um_session_token = data.um_session_token;
							if (um_session_token != null
									&& um_session_token != undefined
									&& um_session_token != ''
									&& um_session_id != null
									&& um_session_id != undefined
									&& um_session_id != '') {
								if(!!data.domain && data.domain != "localhost" ){
									setCookie("um_session_token",
											um_session_token, 0, data.domain);
									setCookie("um_session_id", um_session_id,
											0, data.domain);
								}else{
									setCookie("um_session_token",
											um_session_token, 0, '');
									setCookie("um_session_id", um_session_id,
											0, '');
								}
							}
							
							if (loginFlag == 1) {
								// $(".top-right .login-box").hide(); //
								// 隐藏登录的div
								$(".login-box").hide(); // 隐藏登录的div
								// $(".login-link").removeClass("active");//
								// 隐藏登录的div

								$("#person_name").html(data.username);
								// $("#loginDiv").removeClass("dn"); // 隐藏登录div
								// $("#logoutDiv").addClass("dn"); // 显示已经登录div
								$("#loginDiv").show();
								$("#logoutDiv").hide();
								// showUserMenu();

								$("#userlogin_no").hide();
								$("#userlogin_yes").show();
								
								$("#userlogin_d_no").hide();
								$("#userlogin_d_yes").show();

								$(".index2_top_username").text(data.username);


								// 登录后执行ajax的参数loginEdparams
								if (loginEdparams != null
										&& loginEdparams != undefined) {
									$.ajax({
										type : loginEdparams.type,
										async : loginEdparams.async,
										data : loginEdparams.data,
										url : loginEdparams.url,
										dataType : loginEdparams.dataType,
										success : function(d) {
											loginEdparams.successfn(d);
										},
										error : function(e) {
											loginEdparams.errorfn(e);
										}
									});
								}

								updateCartVm();

								if (loginEdFun != null
										&& loginEdFun != undefined)
									loginEdFun();
							} else {
								//window.location = loginEdUrl;
								setTimeout("setTimeoutLogin()",1000);
							
							}
							$("#applicationContext").html("");
							$("#applicationContext").html("<textarea name=\"context\" id = \"context\" class=\"input-control mb10 font2\" autocomplete=\"off\" onkeyup=\"javascript:setShowLength(this,140);\"></textarea>"
							+"<p><a onclick=\"sendComment()\" class=\"font18 fr btn-green\">提交</a></p>"
							+"<span class=\"app-ei-text-length abs color999\"><span>0</span>/140</span>");
						} else if (data.code == "0") {
							setCookie("checkLoginImagesCode","true",0,'');//设置显示验证码
					    	//显示验证码输入框
					    	$(".login_box_imageCode").show();
					    	$(".usercenter_login_imgCode").show();
							
							getLoginRandomImageCode('loginImage_img')
							var errorField = data.errorField;
							userLoginErrorInfo.html(data.message);
							userLoginErrorInfo.show();
						}

					}
				});
	}
}


function setTimeoutLogin(){
	var loginedForm=$("#loginedForm");
	if(loginedForm!=null && loginedForm!=undefined){
		var strAction=loginedForm.attr("action");
		if(strAction!="" && strAction!=null && strAction!=undefined){
			$("#submit").click();
		}else{
			window.location = loginEdUrl;
		}
	}else{
		window.location = loginEdUrl;
	}
}

/**
 * 获取到登录验证码
 */
function getLoginRandomImageCode(divId) {
	scodediv=$("[name='" + divId + "']");
	if (loginFlag == 1) {
		scodediv=$(scodediv[0]);
	}else{
		scodediv=$(scodediv[1]);
	}
	scodediv.attr("src",
			userCenterHost + "/loginImagesCode?" + Math.random());
}

function updateCartVm() {
	// 取购物车和工作台数量
	$.ajax({
		url : rscloudmartHost + "/ILgetVmCountCartCount",
		type : "get",
		dataType : "json",
		success : function(data1) {

			var vmCountString = data1.vmCountString;
			var cartCountString = data1.cartCountString;

			var vmCount = $(".index2_top_operplatform_num");
			if (vmCount.length > 0)
				vmCount.text(vmCountString);

			var shopcart = $(".index2_top_shopcart_num");
			if (shopcart.length > 0)
				shopcart.text(cartCountString);

		}
	});
}

/*
 * 功能：通用TAB菜单 添加：luoqh
 */
function tabMenu(clickElement) {
	$(clickElement).click(
			function() {
				$(this).addClass("active").siblings().removeClass("active");
				var thisIndex = $(this).index();
				$(this).parent().siblings(".tab-content").children("li").eq(
						thisIndex).show();
				$(this).parent().siblings(".tab-content").children("li").eq(
						thisIndex).siblings().hide();
			});
}

/*
 * 功能：显示顶部个人中心下拉菜单 添加：luoqh
 */
function showUserMenu() {
	if ($(".current-user").length > 0) {
		$(".current-user").hover(function() {
			$(this).addClass("open").siblings(".user-topmenu").show();
			$(".user-topmenu").hover(function() {
				$(this).show().siblings(".current-user").addClass("open");
			}, function() {
				$(this).hide().siblings(".current-user").removeClass("open");
			});
		}, function() {
			$(this).removeClass("open").siblings(".user-topmenu").hide();
		});
	}
}

$(document).ready(
		function() {
			// 加载完数据就浮出显示登录框
			if (loginFlag == 1) {
				$(window).load(function() {
					// 浮出登录框
					$(".login-link")
					// .mouseover(function(){
					// $(this).addClass("active");
					// $(".top-right .login-box").show();
					// })
					.click(function() {
						$(this).siblings(".login-box").show();
						event.preventDefault();
					});
				});
			}
			;

			// 登录 表单文本框默认值显示与隐藏
			$(".login-box .form-control").focus(function() {
				$(this).siblings(".label").hide();
			}).blur(function() {

				if ($(this).val() != "") {
					$(this).siblings(".label").hide();
				} else {
					$(this).siblings(".label").show();
				}
			});
			$(".login-box .label").click(function() {
				$(this).hide().siblings("input").focus();
			});

			// 关闭登录框
			$(".close-login").click(function() {
				$(".login-box").hide();
			});

			// 通用 表单文本框默认值显示与隐藏
			if ($(".default-val").length > 0) {
				setControlDefaultVal(".default-val")
			}

			// 设置个人中心主体内容的高度
/*			if ($(".usercenter_main").length > 0) {
				var referH = $(".usercenter_main").height();
				setMainContentH(referH, ".usercenter_main");
				$(window).resize(function() {
					if($(".usercenter_main_out").hasClass("usercenter_main_out")){
						
					}else{
						setMainContentH(referH, ".usercenter_main");
					}
				});
			}*/
			// 判断当前用户名的li长度
			if ($(".header-ul li:last-child").width() < 125) {
				$(".header-ul li:last-child").width("130");
			}
			$(".user-popbtn").mouseover(function() {
				$(".personpop").show();
				$(".header-ul li:last").addClass("active");
				$(".person-a").addClass("active");
			});
			$(".header-ul li:last").mouseleave(function() {
				$(".personpop").hide();
				$(".header-ul li:last").removeClass("active");
				$(".person-a").removeClass("active");
			});

			// 功能：将回车键转tab键
			var $inp = jQuery('input:text,input:password');
			$inp.bind('keydown', function(e) {
				var key = e.which;
				if (key == 13) {
					e.preventDefault();
					var nxtIdx = $inp.index(this) + 1;
					var input = $($inp[nxtIdx]);

					// 当登录框的用户和密码输入控件，回车键触发登录事件
					var logoutdiv = $("#logoutDiv");
					if (logoutdiv != undefined && logoutdiv != null) {
						var pass = $($inp[nxtIdx - 1]);
						//如果验证码隐藏的时候，光标在密码上，也能登录
					
						if($(".login_box_imageCode").is(":hidden") && pass.attr("id") == "password")
						{
							userLogin();
							return;
						}
						if ((!logoutdiv.hasClass("dn") || loginFlag == 2)
								&& ((pass
										.attr("type") == "text" && pass
										.attr("id") == "loginImageCode"))) {
							userLogin();
							return;
						}
					}

					// 如果是密码输入框，并且有label层，要去掉label层
					var type = input.attr("type");
					if (type == "password") {
						var par = input.parent();
						var labs = par.find(".label");
						if (labs.length == 1) {
							$(labs[0]).hide();
						}
					}

					input.focus();

				}
			});

			showUserMenu();

			var baiduimg = $("[src='http://eiv.baidu.com/hmt/icon/21.gif']");
			if (baiduimg.length > 0)
				$(baiduimg[0]).hide();
		});
/**
 * 根据数据类型的id得到数据类型
 * 
 * @param productid
 * @returns
 */
function getDataProudctLevel(producttypeid) {
	if (!(undefined == producttypeid || producttypeid == null)) {
		for (var i = 0; i <= 4; i++) {
			if (producttypeid.length == (2 * i - 1)) {
				return i;
			}
		}
	} else {
		return 0;
	}
}

function browserJudge() {
	var version = $.support.version;
	if (!$.support.opacity) {
		if (version != "10.0" && version != "11.0") {
			alert("您的浏览器版本过低，请使用IE10或以上、谷歌、火狐浏览器");
			return false;

		}
	}
	return true;
}
/*功能：浏览器检测
 *添加：luoqh
 * */
$(window).ready(function(){
	var t;
	if(!getCookie("t")){ //检测cookie
		setCookie("t",true,9999999999,""); //设置cookie
		var version = $.support.version;
		if ($.support.msie) {
			if (version != "10.0" && version != "11.0") {
				$("#browser-check").show();
				$("#continue").click(function(){
					$("#browser-check").hide();
				});
			}
		}
	}else {
		$("#browser-check").hide();
	}
});

/*
 * 功能：表单文本框默认值显示与隐藏 添加：luoqh 说明：(input示例：<input type="text" class="default-val
 * fontgray" value="请输入用户名" data-default="请输入用户名" />)
 * 调用：setControlDefaultVal(".default-val")
 */
function setControlDefaultVal(element) {
	if ($(element).length > 0) {
		var defaultVal = "", currentVal = "";
		$(element).focus(function() {
			$(this).removeClass("fontgray");
			defaultVal = $(this).attr("data-default");
			currentVal = $(this).val();
			if (currentVal == defaultVal) {
				$(this).val("");
			} else {
				$(this).val(currentVal);
			}
		}).blur(function() {
			currentVal = $(this).val();
			if (currentVal == "") {
				$(this).val(defaultVal).addClass("fontgray");
			} else {
				$(this).val(currentVal);
			}
		});
	}
}
/*
 * 功能：设置主体内容的高度 添加：luoqh 调用：setMainContentH(".left", ".mian-content")
 */
function setMainContentH(referH, setElem) {
	var windowW = $(window).width();
	var windowH = $(window).height();
	var headerH = $(".index2-header").outerHeight();
	var navH = 0, footerH = 0;
	var oldMainContentH = referH;
	if ($(".footer-box").length > 0) {
		footerH = $(".footer-box").outerHeight();
	}
	if ($(".nav").length > 0) {
		navH = $(".nav").outerHeight();
	}
	var oldTotalH = headerH + navH + oldMainContentH + footerH + 20; // 20为oldMainContent的margin-top
	if (windowH > oldTotalH) {
		$(setElem).innerHeight(windowH - headerH - navH - footerH);
	} else if (windowH < oldTotalH) {
		$(setElem).innerHeight(oldMainContentH);
	}
	//alert(headerH+","+navH+","+oldMainContentH+","+footerH);
}

function setMainContentHeigth(referElem, setElem) {
    var windowW = $(window).width();
    var windowH = $(window).height();
    var headerH = $(".index2-header").height();
    var navH = 0, footerH = 0;
    var oldMainContentH = $(referElem).innerHeight();
    if ($(".footer-box").length > 0) {
        footerH = $(".footer-box").outerHeight();
    }
    if ($(".nav").length > 0) {
        navH = $(".nav").outerHeight();
    }
    var oldTotalH = headerH + navH + oldMainContentH + footerH;
    if (windowH > oldTotalH) {
        $(setElem).innerHeight(windowH - headerH - navH - footerH);
    } else if (windowH < oldTotalH) {
        $(setElem).innerHeight(oldMainContentH);
    }
   // console.log(windowH+","+headerH+","+navH+","+oldMainContentH+","+footerH);
    //alert(headerH+","+navH+","+oldMainContentH+","+footerH);
}

function heightReduce(setElem,num){
	var oldHeight=$(setElem).height();
	$(setElem).height(oldHeight-num);
}

// 取得cookie
function getCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';'); // 把cookie分割成组
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i]; // 取得字符串
		while (c.charAt(0) == ' ') { // 判断一下字符串有没有前导空格
			c = c.substring(1, c.length); // 有的话，从第二位开始取
		}
		if (c.indexOf(nameEQ) == 0) { // 如果含有我们要的name
			return unescape(c.substring(nameEQ.length, c.length)); // 解码并截取我们要值
		}
	}
	return false;
}

// 清除cookie
function clearCookie(name) {
	setCookie(name, "", -1);
}

// 设置cookie
function setCookie(name, value, seconds, domain) {
	seconds = seconds || 0; // seconds有值就直接赋值，没有为0，这个根php不一样。
	var expires = "";

	if (seconds != 0) { // 设置cookie生存时间
		var date = new Date();
		date.setTime(date.getTime() + (seconds * 1000));
		expires = "; expires=" + date.toGMTString();
	}
	if (domain != null && domain != undefined && domain != '') {
		domain = ';domain=' + domain;
	} else {
		domain = '';
	}
	document.cookie = name + "=" + escape(value) + expires + "; path=/"
			+ domain; // 转码并赋值
}

/**
 * 页面顶部导航对应模块加上底色
 */
function change_main_current_class(indexPage) {
	if ("undefined" == typeof indexPage) {
		return;
	}

	// 设置头部标签选中的js
	// var obj = $("a[name=index_name]");
	var obj = $(".index-name");
	/*for (var i = 0; i < obj.length; i++) {
		$(obj[i]).addClass("index-name");
	}*/
	switch (indexPage){
		case 0 :indexPage=0;;break;
		case 1 :indexPage=1;;break;
		case 2 :indexPage=3;;break;
		case 3 :indexPage=4;;break;
		case 4 :indexPage=5;;break;
		case 22 :indexPage=2;;break;
		default :indexPage=100;
	}
	$(obj[indexPage]).addClass("active");
}

/**
 * 个人中心左边导航对应模块加上底色
 */
function change_usercenter_current_class(userCenterPage) {
	if ("undefined" == typeof userCenterPage) {
		return;
	}
	// 设置头部标签选中的js
	// var obj = $("a[name=index_name]");
	var obj = $(".uc-l-channel-list").children("li");
	for (var i = 0; i < obj.length; i++) {
		$(obj[i]).removeClass("active");
	}
	$(obj[userCenterPage-1]).addClass("active");
}
/**
 * 个人中心用户资料底色
 */
function userCenterShow_userInfoColor(pageType)
{
	var obj = $(".account-menu").children();
	for(var i = 0; i < obj.length; i++)
	{
		$(obj[i]).attr("class","");
	}
	$(obj[pageType]).attr("class","current");
}

// 跳转到数据产品定制服服务页面
function goToDataProductCustom() {
	var r = Math.floor(Math.random() * 9999 + 1);
	var params = {
		r : r
	};
	$.ilajax({
		url : rscloudmartHost,
		data : params,
		dataType : "html",
		successfn : function(data) {
			window.top.location.href = rscloudmartHost
					+ "/customDataOrder_dataProduct";
		}
	});
}

// 跳转到信息产品定制服服务页面
function goToInfoProductCustom() {
	var r = Math.floor(Math.random() * 9999 + 1);
	var params = {
		r : r
	};
	$.ilajax({
		url : rscloudmartHost,
		data : params,
		dataType : "html",
		successfn : function(data) {
			window.top.location.href = rscloudmartHost
					+ "/customInfoOrder_infoProduct";
		}
	});
}

/*$(window).ready(function(){
	$.getJSON(rscloudmartHost+"/updateVmProprietaryNum",{},function(data){
		if(!!data && data.code == 1){
			$("#operplatform_num_id").html(data.vmCount);
		}
	});
});*/

/**
 * var now = new Date(); 
 * var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
 */
Date.prototype.format = function(format){ 
	var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
	} 
	
	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 
	
	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
} 

function formday2Date(str){
	//var str = "2010-08-01";()()
	//转换日期格式
	str = str.replace(/-/g, '/'); // "2010/08/01";
	return new Date(str);
}

function changeTime4dayBegin(date){
	var str = date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate();
	
	//创建日期对象
	var date = new Date(str);
	//加一天
	return date;
}

/**
 * 执行带参数的回调函数的ajax
 */
function doAjax(u,param,callback){
    $.ajax({
          type:'POST',
          url:u,
          data:param,
          success:callback
    });
}

function doSetTimeOut(callback,time){
	setTimeout(callback,time)
}

/**
 * 把 "2015-5-14" 转化为时间
 * @param str
 * @returns {Date}
 */
function NewDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0); 
	return date; 
} 

/**
 * 
 */
function dateAddDay(date,day){
	var date2 = new Date();
	date2.setFullYear(date.getFullYear());
	date2.setMonth(date.getMonth());
	date2.setDate(date.getDate() + day);
	return date2;
}

function ShowDIV(thisObjID) 
{
      $("#BgDiv").css({ display: "block", height: $(document).height() });
      var yscroll = document.documentElement.scrollTop;
     // $("#" + thisObjID ).css("top", "100px");
      $("#" + thisObjID ).show();
      $(".olMap").addClass("active");
	document.documentElement.scrollTop = 0;
}

function closeDiv(thisObjID) 
{
      $("#BgDiv").css("display", "none");
      $("#" + thisObjID).css("display", "none");
      $(".olMap").removeClass("active");
}
