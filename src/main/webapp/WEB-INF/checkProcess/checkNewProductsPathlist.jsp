<%@page import="org.apache.poi.ss.formula.functions.Value"%>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="manageHost" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>'></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>触发推送</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="list">
	<link rel="icon" href="cloudBox/img/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="cloudBox/img/favicon.ico" type="image/x-icon"/> 
	<link rel="stylesheet" href="base/css/base.css" />
	<link rel="stylesheet" href="base/css/jquery-ui.css" />
	<link rel="stylesheet" href="base/css/downloadApproveList.css" />
	<script src="base/js/jquery-1.11.1.min.js"></script>
	<script src="cloudBox/js/errorItemsCount.js"></script>
  </head>
  
  <body>
  	
  <div id="header">
		<div class="contWarp">
			<div id="logo"></div>
			<ul class="cloud_menu">
				<li><a class="dinline" href="cloudBox.html">数据查询</a></li>
				<!-- <li><a id="headruku" class="dinline" href="dataLoader.html">入库</a></li> -->
				<li><a href="cloudBox.html">我的云盒</a>
					<ul>
						<li><a href="push.html">我的推送记录</a></li>
						<!-- <li><a href="dataLoader.html?html=1">我的入库记录</a></li> -->
						<li><a href="listCheckProcess">我的下载申请</a></li>
					</ul>
				</li>
				<!-- <li id="errorHint"><a href="dataLoader.html?html=1">&nbsp;</a>
					<ul>
						<li>有<span></span>个数据包出现入库问题!</li>
					</ul>
				</li> -->
				<li id="loginOut"><a >&nbsp;</a></li>
			</ul>
		</div>
	</div>
	<div class="main mar-auto ">
		<div class="Search ovcl">
			<div class="searchTitle  ovcl">
				<form class="searchForm" action="${manageHost }/listCheckProcess" method="get">
					<input class="approvalStatus dn" type="text" name="examinationApprovalStatus" value="">
					<input class="fl dn" type="text" name="keyWord" value=${keyWord}>
					<div id="content" class="fl title">触发成功0秒后跳转到首页</div>
				</form>
			</div>
		</div>
	</div>
</body>
<script> 
	setInterval("refer()",1000); //启动1秒定时 
	var t=5;
	function refer(){  
	    if(t==0){ 
	        location="${manageHost}";
	    } 
	    document.getElementById('content').innerHTML="触发成功;"+t+"秒后跳转到首页"; // 显示倒计时 
	    t--;
	} 
	$(document).ready(function(){
		changeActive();
		$("#loginOut").click(function(){
			window.location.href="${manageHost}/logout";
		});
	})
	$(".searchWords input[name=keyWord]").keyup(function(){
		var keyWords = $(".searchWords input[name=keyWord]").val();
		$(".searchWords input[name=keyWord]").attr("value",keyWords);
	})
	$(".searchTitle .title").click(function(){
		var approvalStatus;
		var keyWords = $(".searchWords input[name=keyWord]").attr("value");
		/* $(".searchTitle .title").each(function(){
			$(this).removeClass("active");
		})
		$(this).addClass("active"); */
		approvalStatus = $(this).attr("value");
		$(".searchForm input[name=keyWord]").attr("value",keyWords);
		$(".approvalStatus").attr("value",approvalStatus);
		$(".searchForm").submit();
	})
	$(".searchBtn").click(function(){
		$(".approvalStatus").attr("value",$(".searchForm .active").attr("value"));
		$(".submitBtn").click();
	})
	
	//改变
	function changeActive(){
		var Status = GetQueryString("examinationApprovalStatus");
		if(Status == 1){
			$(".searchTitle .title").each(function(){
				$(this).removeClass("active");
			})
			$(".title[value=1]").addClass("active");
		}
		else if(Status == 2){
			$(".searchTitle .title").each(function(){
				$(this).removeClass("active");
			})
			$(".title[value=2]").addClass("active");
		}else{
			$(".searchForm .title").eq(0).addClass("active");
		}
	}
	//获取审批状态参数
	function GetQueryString(name) {  
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
		var r = window.location.search.substr(1).match(reg);  //获取url中"?"符后的字符串并正则匹配
		var context = "";  
		if (r != null)  
			context = r[2];  
		reg = null;  
		r = null;  
		return context == null || context == "" || context == "undefined" ? "" : context;  
	}
</script>
</html>
