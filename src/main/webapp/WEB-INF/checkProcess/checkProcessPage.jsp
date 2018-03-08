<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="manageHost" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>'></c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
   
    <title>云盒数据管理系统</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="icon" href="cloudBox/img/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="cloudBox/img/favicon.ico" type="image/x-icon"/> 
	<link rel="stylesheet" href="base/css/base.css"/>
	<link rel="stylesheet" href="base/css/jquery-ui.css" />
	<link rel="stylesheet" href="base/css/downloadApplication.css"/>
	
	<script src="base/js/jquery-1.11.1.min.js"></script>
	<script src="base/js/jquery.form.js"></script>
	<script src="base/js/jquery-validation-1.14.0/jquery.validate.min.js"></script>
	<script src="base/js/jquery-validation-1.14.0/additional-methods-common.js"></script>
	<!-- <script src="downloadApplication/js/downloadApplication.js"></script> -->
  </head>
  <body><div aadda

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
	
	
  	<div class="main">
		<div class="mainWrap mar-auto">
			<div class="title">
				<div class="applicationForm">申请表单</div>
			</div>
			<div class="sell contentWrap ">
				<form id="sellForm" class="sellForm content mar-auto" action="/addCheckProcess" method="post" enctype="multipart/form-data">
					<div class="name">客户名称：<input type="text" autocomplete="off" maxlength="50" name="customer_name" onkeyup="$('.demoForm input[name=customer_name]').val(this.value)"/><span class="must red font14">*必填</span></div>
					<span class="error"></span>
					<div class="customName">客户联系人：<input type="text" autocomplete="off" maxlength="20" name="customer_contacter" onkeyup="$('.demoForm input[name=customer_contacter]').val(this.value)"/><span class="must red font14">*必填</span></div>
					<span class="error"></span>
					<div class="customTel">客户联系方式：<input type="text" autocomplete="off" name="customer_contact" onkeyup="$('.demoForm input[name=customer_contact]').val(this.value)"/><input type="hidden" autocomplete="off" name="backUrl" /><span class="must red font14">*必填</span></div>
					<span class="error"></span>
					<input class="dn" type="text" name="purpose" value="1" />
					<div class="dataUse"><span class="fl">数据用途：</span>
						<div class="applicationChoose fl">
							<span>销售数据</span>
							<ul class="choose dn">
								<li class="chooseLi" value="0">销售数据</li>
								<li class="chooseLi" value="1">样例数据</li>
							</ul>
						</div>
					</div>
					<div class="hetong">合同销售面积（km²）：<span class="totalArea1">0</span></div>
					<span class="error"></span>
					<div class="hetongScan">合同扫描件：<input class="dn" type="file"  name="contractScanning" onchange="ht.value=value"><input class="ht font14 grey" name=ht readonly="true" onclick=contractScanning.click() value="点击上传附件，多个文件压缩后上传"></div>
					<div class="fapiaoScan">发票扫描件：<input class="dn" type="file"  name="invoiceScanning" onchange="fp.value=value"><input class="fp font14 grey" name=fp readonly="true" onclick=invoiceScanning.click() value="点击上传附件，多个文件压缩后上传"></div>
					<div class="dataList-title">
					<div class="data-Title">数据清单：</div>
					<div class="datapricechoose">
						  <div class="form-group">
                         <label class="celect-label">*计算方式：</label>
                              <select style="width:138px;height:25px" id="selectprice" name="price_type">
                              <option value="0"> 按面积计算价格</option>
                              <option value="1">按每景计算价格</option>
                              </select>
                              </div>
					</div>
					</div>
					<section id="pricejin">
					<div class="dataTableTitle ovcl" >
						<div class="tr1 trtitle fl">数据</div>
						<div class="trtitle tr2 fl">数量（景）</div>
						<div class="trtitle tr3 fl">下载面积km²</div>
						<div class="trtitle tr6 fl">*销售面积km²</div>
						<div class="trtitle tr4 fl">*单价（元/km²）</div>
						<div class="trtitle tr5 fl">小计（元）</div>
					</div>
					<c:forEach var="val" varStatus="status"  items="${list}">
						<div class="dataList ovcl" >
							<input class="dn" value="${val.image_ids_list}" name="listDto[${status.index }].image_ids_list" >
			  				<input class="dn" value="${val.image_satellite_type}" type="text" name="listDto[${status.index }].data_type">
							<input class="dn" value="${val.sum_cou}" type="text" name="listDto[${status.index }].num">
							<input class="dn" value="<f:formatNumber value ="${val.sum_area}" pattern="#.0000000"/>" type="text" name="listDto[${status.index }].area">
							<div class="tr1 fl">${val.image_satellite_type}</div>
							<div class="tr2 fl">${val.sum_cou}</div>
							<div class="tr3 fl"><fmt:formatNumber type="number" value="${val.sum_area/1000000}" /></div>
							<div class="tr6 fl"><input type="text" class="sell-area required" autocomplete="off" name="listDto[${status.index}].sales_area" placeholder="*必填"><span class="error sell-arrer"></span></div>
							<div class="tr4 fl"><input class="thePrice required fl" autocomplete="off" type="text" name="listDto[${status.index}].unit_price"  placeholder="*必填" /><span class="error price-error"></span></div>
							<div class="tr5 fl">0.00</div>
							<c:set var="totalSum" scope="session" value="${totalSum+val.sum_cou}"/>
							<c:set var="Area2" scope="session" value="${val.sum_area/1000000}"/>
							<c:set var="totalArea" scope="session" value="${totalArea+Area2}"/>
						</div>
						
					</c:forEach>
					<div class="dataTotal pb25  ovcl" >
						<div class="tr1 fl">总计</div>
						<div class="tr2 fl"><c:out value="${totalSum}"/><c:remove var="totalSum"/></div>
						<div class="tr3 fl"><fmt:formatNumber type="number" value="${totalArea}" /><c:remove var="totalArea"/></div>
						<div class="totalArea1  tr6 fl">0</div><input type="hidden" name="contract_sales_area" class="contract_sales_area" value=""/>
						<div class="trtotal fl">0.00</div>
						
					</div>
				 </section>
				</form>
			</div>
			<div class="demo contentWrap dn">
				<form id="demoForm" class="demoForm content mar-auto" enctype="multipart/form-data" role="form" action="/addCheckProcess" method="post">
 					<div class="name">客户名称：<input type="text" class="customer_name" autocomplete="off" maxlength="50" name="customer_name" /><span class="must red font14">*必填</span></div>
					<span class="error"></span>
					<div class="customName">客户联系人：<input type="text" autocomplete="off" maxlength="20" name="customer_contacter" /><span class="must red font14">*必填</span></div>
					<span class="error"></span>
					<div class="customTel">客户联系方式：<input type="text" autocomplete="off" name="customer_contact" /><input type="hidden" autocomplete="off" name="backUrl" /><span class="must red font14">*必填</span></div>
					<span class="error"></span>
 					<input type="hidden" name="purpose" value="0" />
					<div class="dataUse">数据用途：<span class="grey">已下载了<span class="count">${count}</span>景样例数据</span></div>
					<div class="applicationChoose">
						<span>样例数据</span>
						<ul class="choose dn">
							<li class="chooseLi" value="0">销售数据</li>
							<li class="chooseLi" value="1">样例数据</li>
						</ul>
					</div>
					<div class="dataTitle">数据清单：</div>
					<div class="dataTableTitle ovcl">
						<div class="trtitle tr1 fl">数据</div>
						<div class="trtitle tr2 fl">数量（景）</div>
						<div class="trtitle tr3 fl">面积km²</div>
					</div>
					<c:forEach var="val" varStatus="status"  items="${list }">
						<div class="ovcl">
							<input class="dn" value="${val.image_ids_list}" name="listDto[${status.index }].image_ids_list" >
			  				<input class="dn" value="${val.image_satellite_type}" type="text" name="listDto[${status.index }].data_type">
							<input class="dn" value="${val.sum_cou}" type="text" name="listDto[${status.index }].num">
							<input class="dn" value="<f:formatNumber value ="${val.sum_area}" pattern="#.0000000"/>" type="text" name="listDto[${status.index }].area">
							<div class="tr1 fl">${val.image_satellite_type}</div>
							<div class="tr2 fl">${val.sum_cou}</div>
							<div class="tr3 fl"><fmt:formatNumber type="number" maxFractionDigits="2" value="${val.sum_area/1000000}" /></div>
							<c:set var="totalSum" scope="session" value="${totalSum+val.sum_cou}"/>
							<c:set var="Area2" scope="session" value="${val.sum_area/1000000}"/>
							<c:set var="totalArea" scope="session" value="${totalArea+val.sum_area/1000000}"/>
						</div>
					</c:forEach>	
					<div class="dataTotal pb15 ovcl">
						<div class="tr1 fl">总计</div>
						<div class="totalNum tr2 fl"><c:out value="${totalSum}"/><c:remove var="totalSum"/></div>
						
						<div class="totalPrice tr3 fl"><fmt:formatNumber type="number" maxFractionDigits="2" value="${totalArea}" /><c:remove var="totalArea"/></div>
					</div>
				</form>
			</div>
		</div>		
	</div>
	<div class="foot">
		<!-- <input type="submit" value="提交"> -->
		<div class="commit" formvalue="0">提&nbsp&nbsp交</div>
	</div>
	<!-- 遮罩层 -->
	<div class="mask dn"></div>
	<!-- 弹出框 -->
	<div class="tipDialog theDialog dn">
		<div class="close ovcl">
			<div class="closeBtn fr"></div>
		</div>
		<div class="dialogContent">
			<p>您已成功提交下载申请，请耐心等候审批结果</p>
			<p>您可以在“<a class="goto" href="listCheckProcess">我的下载申请</a>”中跟踪进度</p>
		</div>
	</div>
	<div class="tipDialog2 theDialog dn">
		<div class="close ovcl">
			<div class="closeBtn fr"></div>
		</div>
		<div class="dialogContent">
			<p>提交下载申请失败，请重新提交</p>
			<!-- <p>您可以在“<a class="goto" href="listCheckProcess">我的下载申请</a>”中跟踪进度</p> -->
		</div>
	</div>
	<!-- 提示正在载入GIF图标 -->
	<div id="waiting" style="display:none;" >
	    <img src="base/img/loading.gif" alt="稍等..."/>
	</div>
	<div id="BgDiv" style="display: none; height: 650px;"></div>
	<!-- <div class="tipDialog2 theDialog dn">
		<div class="close ovcl">
			<div class="closeBtn fr"></div>
		</div>
		<div class="dialogContent">
			<p><span class="fillIn">客户联系人</span>是必填</p>
		</div>
	</div> -->
	
	
	
	
		  	<%-- <form action="/addCheckProcess" method="post" enctype="multipart/form-data">
		  		申请人名称:<input type="text" name="applicant_name">
		  		客户名称:<input type="text" name="customer_name">
		  		<br>
		  		客户联系人:<input type="text" name="customer_contacter">
		  		合同销售面积:<input type="text" name="contract_sales_area">
		  		客户联系方式:<input type="text" name="customer_contact">
		  		<select name="purpose">
		  			<option value="0">销售数据</option>
		  			<option value="1">样例数据</option>
		  		</select>
		  		<br>
		  		合同扫描件：<input type="file"  name="contractScanning">
		  		<br>
		  		发票扫描件：<input type="file"  name="invoiceScanning">
		  		<br>
		  		<table style="border: 1px solid red">
		  			<thead>
		  				<td>数据</td>
		  				<td>数据(包)</td>
		  				<td>面积</td>
		  				<td>单价(元)</td>
		  				<td>小鸡</td>
		  			</thead>
		  			<c:forEach var="val" varStatus="status"  items="${list }">
			  			<tr>
			  				<td>
			  					<input type="hidden" value="${val.image_ids_list}" name="listDto[${status.index }].image_ids_list" >
			  					<input value="${val.image_satellite_type}" type="text" name="listDto[${status.index }].data_type">
			  				</td>
			  				<td>
			  					<input value="${val.sum_cou}" type="text" name="listDto[${status.index }].num">
			  				</td>
			  				<td>
			  					<input value="<f:formatNumber value ="${val.sum_area}" pattern="#.0000000"/>" type="text" name="listDto[${status.index }].area">
			  				</td>
			  				<td>
			  					<input type="text" name="listDto[${status.index }].unit_price">
			  				</td>
			  				<td>
			  					<input type="text" name="">
			  				</td>
			  			</tr>
		  			</c:forEach>		  			
		  		</table>
		  		
		  		
		  		<input type="submit" value="提交">
		  	</form> --%>
  </body>
  <script>
  var basePath = "<%=basePath%>";
  var limited = '${numLimit}';
  	$(document).ready(function(){
  		$(".main").css("min-height",document.body.clientHeight-158);
  		$("#loginOut").click(function(){
 			window.location.href="${manageHost}/logout";
 		});
 		$(".demoForm").find("input[name='backUrl']").val(location);
 		$(".sellForm").find("input[name='backUrl']").val(location);

  	});
 	$(".commit").click(function(){
  		if($(this).attr("formValue") == 0){
  			$(".sellForm input[name=customer_name]").val($.trim($(".sellForm input[name=customer_name]").val()));
  			$(".sellForm input[name=customer_contacter]").val($.trim($(".sellForm input[name=customer_contacter]").val()));
		  	$(".sellForm").validate();
		  	$(".sellForm").submit();
  		}
  		else{
  			$(".demoForm input[name=customer_name]").val($.trim($(".demoForm input[name=customer_name]").val()));
  			$(".demoForm input[name=customer_contacter]").val($.trim($(".demoForm input[name=customer_contacter]").val()));
  			$(".demoForm").validate();
  		 	var total = parseInt($(".demo .dataTotal .totalNum").text())+parseInt($(".demo .dataUse .count").text())
  			if(total<=limited){
  				$(".demoForm").submit();
  			}
  			else{
  				alert("下载总数超过了"+limited+"景");
  			}
  			//$(".demoForm").submit();
  		}
  	});
 	function ShowDIV(thisObjID) 
	{
		$("#BgDiv").css({ display: "block", height: $(document).height() });
		var yscroll = document.documentElement.scrollTop;
		// $("#" + thisObjID ).css("top", "100px");
		$("#" + thisObjID ).show();
		document.documentElement.scrollTop = 0;
	}
  	function closeDiv(thisObjID) 
	{
		$("#BgDiv").css("display", "none");
		$("#" + thisObjID).css("display", "none");
	}
 	function formsubmit(form){
 		var url = basePath+"addCheckProcess";
 		if(form == "sellForm"){
 			if($(".sellForm input[name=contractScanning]").val() == "" && $(".sellForm input[name=invoiceScanning]").val() == ""){
 				var url = basePath+"addCheckProcessNoFile";
 			}else if($(".sellForm input[name=contractScanning]").val() == "" && $(".sellForm input[name=invoiceScanning]").val() != ""){
 				var url = basePath+"addCheckProcessInvoiceScanningFile";
 			}else if($(".sellForm input[name=contractScanning]").val() != "" && $(".sellForm input[name=invoiceScanning]").val() == ""){
 				var url = basePath+"addCheckProcessContractScanningFile";
 			}
 		}
 		$("."+form).ajaxSubmit({
			type: "post",
			url: url,
			dataType: "json",
			success: function(data){
				if(data.code == 1){
					closeDiv("waiting");
					tipDialog("tipDialog");
				}else{
					closeDiv("waiting");
					tipDialog("tipDialog2");
				}
			}
		});
 	}
  	/**
	 * 
	 * @param email
	 *            检查电子邮箱格式
	 * @returns {Boolean} 如果正确返回true,不正确返回false
	 */
	function checkEmail(email) {
		return email
				.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.([A-Za-z0-9]{2,4})+$/);
	}
	/**
	 * 
	 * @param phone
	 *            检查手机号码格式
	 * @returns {Boolean} 如果正确返回true,不正确返回false
	 */
	function checkPhone(phone) {
		var yidong = /^[1]{1}[0-9]{10}$/;
		var liantong = /^[1]{1}[0-9]{10}$/;
		var dianxin = /^[1]{1}[0-9]{10}$/;
		/*var yidong = /^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$/;
		var liantong = /^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$/;
		var dianxin = /^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[019]{1}))[0-9]{8}$/;*/
		if (!phone.match(yidong) && !phone.match(liantong) && !phone.match(dianxin)) {
			return false;
		} else {
			return true;
		}
	}
  	//添加认证规则
	jQuery.validator.addMethod("checkEmailOrPhone", function(value, element) {
	   if(checkEmail(value) || checkPhone(value)) return true;
	   return false;
	}, "手机号或邮箱格式错误！");
	jQuery.extend(jQuery.validator.messages, {
		  required: "不能为空",
	});
  	var sellForm = $(".sellForm").validate({
  		submitHandler:function() {
  			ShowDIV("waiting");
  			formsubmit("sellForm");
		},
		rules: {
			"customer_contact":{
				"checkEmailOrPhone":true
				//"mobileOrEmail":true	//手机号或邮箱，additional-methods-common.js中定义
				//"checkEmailOrPhone":true //手机号或邮箱，自定义
			},
			"customer_name":{
				"required":true,
			},
			"customer_contacter":{
				"required":true,
			},
			"contract_sales_area":{
				"required":true,
			},
			"listDto[0].unit_price":{
				"required":true,
			},
			
		},
		messages: {
			"customer_contact":{
			},
			"customer_name":{
				"required":"客户名称不能为空",
			},
			"customer_contacter":{
				"required":"客户联系人不能为空",
			},
			"contract_sales_area":{
				"required":"销售面积不能为空",
			},
			"listDto[0].unit_price":{
				"required":"单价不能为空",
			},
			
		},
		errorPlacement:function(error,element) {
			if(element.next(".error").length <= 0){
				error.appendTo(element.parent().next(".error"));
			}else{
				error.appendTo(element.next(".error"));
			}
		}
	
	});
	
	var demoForm = $(".demoForm").validate({
		submitHandler:function() {
			ShowDIV("waiting");
			formsubmit("demoForm");
		},
		rules: {
			"customer_contact":{
				"checkEmailOrPhone":true
			},
			"customer_name":{
				"required":true,
			},
			"customer_contacter":{
				"required":true,
			},
		},
		messages: {
			"customer_contact":{
			},
			"customer_name":{
				"required":"客户名称不能为空",
			},
			"customer_contacter":{
				"required":"客户联系人不能为空",
			},
			
		},
		errorPlacement:function(error,element) {
			if(element.next(".error").length <= 0){
				error.appendTo(element.parent().next(".error"));
			}else{
				error.appendTo(element.next(".error"));
			}
		}
	});
  	$(".applicationChoose").click(function(){
  		$(this).find(".choose").removeClass("dn");
  	})
  	$(".chooseLi").click(function(e){
  		$(".choose").addClass("dn");
  		changeForm($(this).val());
  		e.stopPropagation();
  	})
  	function changeForm(form){
  		if(form == 0){
  			$(".demo").addClass("dn");
  			$(".sell").removeClass("dn");
  		}
  		else{
  			$(".sell").addClass("dn");
  			$(".demo").removeClass("dn");
  		}
  		$(".commit").attr("formValue",form);
  	}
  	
  	$(document).on("keyup",".thePrice2",function(){
  		amount(this);
  		var thePrice = parseFloat($(this).val());
  		var theNum = parseFloat($(this).parents(".dataList").find(".tr2").text().replace(/,/g,""));
  		var total = thePrice * theNum;
  		var totalPrice=0;
  		if($(this).val() == ""){
  			$(this).parents(".dataList").find(".tr5").text(0);
  		}
  		else{
	  		$(this).parents(".dataList").find(".tr5").text(total.toFixed(2));
  		}
  		$(".sell .dataList").each(function(){
  			var price = $(this).find(".tr5").text();
  			if(price != "")
  				totalPrice += parseFloat(price);
  		})
  		$(".trtotal2").text(totalPrice.toFixed(2));
  	});
  	
  	$(document).on("keyup",".thePrice",function(){
  
  		amount(this);
  		var thePrice = parseFloat($(this).val());
  		var theArea1 = $(this).parents(".dataList").find(".sell-area").val();
  		var total = thePrice * theArea1;
  		var totalPrice=0;
  		if($(this).val() == ""){
  			$(this).parents(".dataList").find(".tr5").text("0.00");
  		}
  		else{
	  		$(this).parents(".dataList").find(".tr5").text(total.toFixed(2));
  		}
  		$(".sell .dataList").each(function(){
  			var price = $(this).find(".tr5").text();
  			if(price != "")
  				totalPrice += parseFloat(price);
  		})
  		$(".trtotal").text(totalPrice.toFixed(2));
  	});
  	 	
  	 $(document).on("keyup",".sell-area",function(){
  		amount2(this);
  		var thePrice2 = parseFloat($(this).parents(".dataList").find(".thePrice").val());
  		var theArea2 = $(this).val();
  		var tota2 = thePrice2 * theArea2;
  		var totalArea1=0;
		if($(this).val() == "" || $(".thePrice").val()==""){
		
  			$(this).parents(".dataList").find(".tr5").text("0.00");
  			$(".trtotal").text("0.00");
  		}
  		else{
	  		$(this).parents(".dataList").find(".tr5").text(tota2.toFixed(2));
	  		$(".trtotal").text(tota2.toFixed(2));	
  		}
  		$(".sell .dataList").each(function(){
  			var totalareas = $(this).find(".sell-area").val();
  			if(totalareas != ""){
  				 totalArea1 += parseFloat(totalareas);
  				}
  		});
  			$(".totalArea1").text(totalArea1);
  			$(".contract_sales_area").attr("value",totalArea1);
  	});
  	
  	 $(document).on("keyup",".sell-area2",function(){
  		amount2(this);
  		var thePrice2 = parseFloat($(this).parents(".dataList").find(".thePrice2").val());
  		var theArea2 = $(this).val();
  		var tota2 = thePrice2 * theArea2;
  		var totalArea1=0;
		
  		$(".sell .dataList").each(function(){
  			var totalareas = $(this).find(".sell-area2").val();
  			if(totalareas != ""){
  				 totalArea1 += parseFloat(totalareas);
  				}
  		});
  			$(".totalArea2").text(totalArea1);
  			$(".contract_sales_area").attr("value",totalArea1);
  	});
  	
  	$(".hetong input").keyup(function(){
  		amount1(this);
  	});
  	//form提交
  	
  	//弹出提示框
  	function tipDialog(dialog){
  		$(".mask").removeClass("dn");
  		$("."+dialog).removeClass("dn");
  	}
  	//关闭提示框
  	$(".theDialog .closeBtn").click(function(){
  		$(".mask").addClass("dn");
  		$(".theDialog").addClass("dn");
  	})
  	//input限制输入
  	function amount(th){
	    var regStrs = [
	        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
	        ['[^\\d\\.]+', ''], //禁止录入任何非数字和点
	        ['\\.(\\d*)\\.+', '.$1'], //禁止录入两个以上的点
	        ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
	    ];
	    for(i=0; i<regStrs.length; i++){
	        var reg = new RegExp(regStrs[i][0]);
	        th.value = th.value.replace(reg, regStrs[i][1]);
	    }
	}
	function amount1(th){
	    var regStrs = [
	        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
	        ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
	        ['\\.(\\d*)\\.+', '.$1'], //禁止录入两个以上的点
	    ];
	    for(i=0; i<regStrs.length; i++){
	        var reg = new RegExp(regStrs[i][0]);
	        th.value = th.value.replace(reg, regStrs[i][1]);
	    }
	}
	function amount2(th){
	    var regStrs = [
	    	['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
	        ['[^\\d\\.]+', ''], //禁止录入任何非数字和点
	        ['\\.(\\d*)\\.+', '.$1'], //禁止录入两个以上的点 
	        ['^(\\d+\\.\\d{3}).+', '$1'] //禁止录入小数点后三位以上
	    ];
	    for(i=0; i<regStrs.length; i++){
	        var reg = new RegExp(regStrs[i][0]);
	        th.value = th.value.replace(reg, regStrs[i][1]);
	    }
	}
	function getQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return unescape(r[2]); return null;
	}
	
 $(document).on("change","#selectprice",function(){
	    if($(this).val()==0){
	    
	      $(".trtitle.tr4").html("*单价（元/km²）");
	      $(".tr6 input").removeClass("sell-area2").addClass("sell-area");
	      $(".tr4 input").removeClass("thePrice2").addClass("thePrice");
	      $(".trtotal2").removeClass("trtotal2").addClass("trtotal");
	      $(".totalArea2").removeClass("totalArea2").addClass("totalArea1");
	      $(".hetong span").removeClass("totalArea2").addClass("totalArea1");
	      var inputs = $(".dataList .fl input");
		  if(inputs!=""){
		  	 $(".tr4 input").val("");
		  }
		  var tr5Text = $(".thePrice").parents(".dataList").find(".tr5").text();
		   var trdataTota = $(".trtotal").text();
		   
		  if(tr5Text>0){
		  	$(".thePrice").parents(".dataList").find(".tr5").text("0.00");
	    	}
	      if(trdataTota >0){
	      	$(".trtotal").text("0.00");
	      }
	    } 
	    else{
	      $(".trtitle.tr4").html("*单价（元/景）");
	      $(".tr6 input").removeClass("sell-area").addClass("sell-area2");
	      $(".tr4 input").removeClass("thePrice").addClass("thePrice2");
	      $(".trtotal").removeClass("trtotal").addClass("trtotal2");
		  $(".totalArea1").removeClass("totalArea1").addClass("totalArea2");
		  $(".hetong span").removeClass("totalArea2").addClass("totalArea1");
		  $(".hetong span").removeClass("totalArea1").addClass("totalArea2");
	      var inputs = $(".dataList .fl input");
		  if(inputs!=""){
		  	 $(".tr4 input").val("");
		  }
		  var tr5Text = $(".thePrice2").parents(".dataList").find(".tr5").text();
		   var trdataTota2 = $(".trtotal2").text();
		   
		  if(tr5Text>0 || tr5Text != 0){
		  	$(".thePrice2").parents(".dataList").find(".tr5").text("0.00");
	    	}
	      if(trdataTota2 >0){
	      	$(".trtotal2").text("0.00");
	      }
	    } 
	  }); 

  </script>
  
  
</html>
