<%@page import="org.apache.poi.ss.formula.functions.Value"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    
    <title>云盒申请详情</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="list">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="icon" href="cloudBox/img/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="cloudBox/img/favicon.ico" type="image/x-icon"/> 
	<link rel="stylesheet" href="base/css/base.css" />
	<link rel="stylesheet" href="base/css/jquery-ui.css" />
	<link rel="stylesheet" href="base/css/downloadDetail.css" />
	<script src="base/js/jquery-1.11.1.min.js"></script>
	<script src="base/js/jquery.form.js"></script>
	<script src="base/js/openlayers/OpenLayers.js"></script>
  </head>
  
  <body>
  <div class="body-box"></div>
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
  	<!-- 待审批内容 -->
	<div class="main mar-auto">
		<div class="baseInfo">
			<div class="title">基本信息</div>
			<div class="content ovcl">
				<div class="mb15 mt10 ovcl"><span class="fl">申请状态：</span>
					<p class="fl">
						<c:choose>
						    <c:when test="${map.examination_approval_status==0 || map.examination_approval_status==3 || map.examination_approval_status==4}">
						       		待审批
						    </c:when>
						    <c:when test="${map.examination_approval_status == 1}">
						        	已通过
						    </c:when>
						    <c:when test="${map.examination_approval_status == 2}">
						        	驳回
						    </c:when>
						</c:choose>
					</p>
				</div>
				<div class="mb15 ovcl"><span class="fl">申请人：</span><p class="fl">${map.applicant_name}</p></div>
				<div class="mb15 ovcl"><span class="fl">申请时间：</span><p class="fl"><fmt:formatDate type="time" value="${map.applicant_time}" pattern="yyyy-MM-dd HH:mm:ss" /></p></div>
			</div>
		</div>
		<div class="application">
			<div class="title">申请内容</div>
			<div class="content ovcl">
				<div class="mb15 mt10 ovcl"><span class="fl">客户名称：</span><p class="fl">${map.customer_name}</p></div>
				<div class="mb15 ovcl"><span class="fl">客户联系人：</span><p class="fl">${map.customer_contacter}</p></div>
				<div class="mb15 ovcl"><span class="fl">客户联系方式：</span><p class="fl">${map.customer_contact}</p></div>
				
				<c:choose>
				    <c:when test="${map.purpose == 1 }">
				    	<div class="mb15 ovcl"><span class="fl">数据用途：</span><p class="fl">销售数据</p></div>
						<div class="mb15 ovcl"><span class="fl">合同销售面积：</span><p class="fl">${map.contract_sales_area}&nbsp km²</p></div>
						<div class="mb15 ovcl"><span class="fl">合同扫描件：</span>
							<c:choose>
							    <c:when test="${ not empty map.contract_scanning_path}">
								    <p class="fl">${fn:substringAfter(map.contract_scanning_path,'_')}</p>
									<a href="${manageHost}/downApplicantInformationFileById?filePath=${map.contract_scanning_path}" class="fl download">点击下载</a>
							    </c:when>
							    <c:otherwise>
									<p class="fl">无</p>
							    </c:otherwise>
							</c:choose>
						</div>
						<div class="mb15 ovcl"><span class="fl">发票扫描件：</span>
							<c:choose>
							    <c:when test="${ not empty map.invoice_scanning_path}">
								    <p class="fl">${fn:substringAfter(map.invoice_scanning_path,'_')}</p>
									<a href="${manageHost}/downApplicantInformationFileById?filePath=${map.invoice_scanning_path}" class="fl download">点击下载</a>
							    </c:when>
							    <c:otherwise>
									<p class="fl">无</p>
							    </c:otherwise>
							</c:choose>
						</div>
					<div class="mb15 ovcl"><span class="fl">计价方式：</span>
						   <c:choose>
						 		<c:when test="${map.price_type == 0}">
									<p class="fl">按面积计算价格</p>
								 </c:when>
								<c:when test="${map.price_type == 1}">
								    <p class="fl">按每景计算价格</p>
					   			</c:when> 
							 </c:choose>
						 </div>
				    </c:when>
				    <c:when test="${map.purpose == 0}">
				        <div class="mb15 ovcl"><span class="fl">数据用途：</span><p class="fl">样例数据（已下载<a>${count}</a>景，本次申请下载<a class="applicationNum1"></a>景）</p></div>
				    </c:when>
				</c:choose>
				
				<div class="mb15 ovcl">
					<span class="fl">数据清单：</span>
					
					<%--样例数据-数据清单--%>
					<c:choose>
					    <c:when test="${map.purpose==0 }">
					       	<div class="demoData fl">
								<div>
									<div class="td1 borderTop fl">数据</div>
									<div class="td2 borderTop fl">数量（景）</div>
									<div class="td3 borderTop fl">面积km²</div>
								</div>
								<div class="dataList ovcl"> 
									<c:forEach var="values" items="${map.listApplicantData }">
										<div>
											<div class="td1 fl">${values.data_type}</div>
											<div class="td2 fl">${values.num}</div>
											<div class="td3 fl"><fmt:formatNumber type="number" value="${values.area/1000000}" /></div>
										</div>
										<c:set var="totalSum" scope="session" value="${totalSum+values.num}"/>
										<c:set var="Area2" scope="session" value="${values.area/1000000}"/>
										<c:set var="totalArea" scope="session" value="${totalArea+Area2}"/>
									</c:forEach>
								</div>
								<div class="dataTotal ovcl">
									<div class="td1 fl">总计</div>
									<div class="td2 fl">${totalSum}<c:remove var="totalSum"/></div>
									<div class="td3 fl"><fmt:formatNumber type="number" value="${totalArea}" /><c:remove var="totalArea" /></div>
								</div>
							</div>
					    </c:when>
					    <%--销售数据-数据清单--%>
					    
					    <c:when test="${map.purpose == 1}">
				        	<div class="sellData fl">
								<div>
									<div class="td1 borderTop fl">数据</div>
									<div class="td2 borderTop fl">数量（景）</div>
									<div class="td3 borderTop fl">面积km²</div>
								
									<div class="td6 borderTop fl">销售面积km²</div>
										<c:choose>
										<c:when test="${map.price_type == 0}">
									<div class="td4 borderTop fl">单价（元/km²）</div>
									 	</c:when>
									 	<c:when test="${map.price_type == 1}">
									<div class="td4 borderTop fl">单价（元/景）</div>
									 	</c:when>
									 	</c:choose>
									<div class="td5 borderTop fl">小计（元）</div>
								</div>
								<div class="dataList ovcl"> 
									<c:forEach var="values" items="${map.listApplicantData }">
										<div>
											<div class="td1 fl">${values.data_type}</div>
											<div class="td2 fl">${values.num}</div>
											<div class="td3 fl"><fmt:formatNumber type="number" value="${values.area/1000000}" /></div>
											<div class="td6 fl">${values.sales_area}</div>
											<div class="td4 fl">${values.unit_price}</div>
											<c:choose>
										 		<c:when test="${map.price_type == 0}">
													<div class="td5 fl"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.sales_area/1*values.unit_price}" /></div>
												 </c:when>
												<c:when test="${map.price_type == 1}">
												   <div class="td5 fl"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.num*values.unit_price}" /></div>
									   			</c:when> 
											 </c:choose>
											 
										
											
										</div>
										<c:set var="totalSum" scope="session" value="${totalSum+values.num}"/>
										<c:set var="Area2" scope="session" value="${values.area/1000000}"/>
										<c:set var="totalArea" scope="session" value="${totalArea+Area2}"/>												
										<c:set var="totalPrice" scope="session" value="${totalPrice+values.num*values.unit_price}"/>
										<c:set var="totalPrice2" scope="session" value="${totalPrice2+values.sales_area/1*values.unit_price}"/>
									</c:forEach>
								</div>
								<div class="dataTotal ovcl">
									<div class="td1 fl">总计</div>
									<div class="td2 fl">${totalSum}<c:remove var="totalSum" /></div>
									<div class="td3 fl"><fmt:formatNumber type="number" value="${totalArea}" /><c:remove var="totalArea" /></div>
									<div class="td6 fl">${map.contract_sales_area}</div>
									<c:choose>
										 <c:when test="${map.price_type == 0}">
									<div class="td4 fl"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPrice2}" /></div>
										 </c:when>
										 <c:when test="${map.price_type == 1}">
									<div class="td4 fl"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPrice}" /></div>
										 </c:when>
									</c:choose>
									<c:remove var="totalPrice2" />
									<c:remove var="totalPrice" />
									
								</div>
							</div>
					    </c:when>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="data mar-auto">
		<div class="data-title">
			<div class="title fl">数据列表 </div>
			
			<div class="all-reapply dn fr"><a id="all-reapply-btn">重新申请所有解密失败文件</a></div>
			</div>
			<div class="content mb15 ovcl">
				<%--<div class="downloadAll mb20" onclick="downloadAll()">批量下载</div>--%>
				<div class="mb15">
					<div class="resultTableTop ovcl">
						<div class="td1 borderTop fl"><%--<div class="checkbox fl" type="button" onclick="changeCheckBox($(this),0)"></div>--%>序号</div>
						<div class="td2 borderTop fl">数据包</div>
						<div class="td3 borderTop fl">数据类型</div>
						<div class="td4 borderTop fl">大小</div>
						<div class="td5 borderTop fl">状态</div>
						<div class="td6 borderTop fl">操作</div>
					</div>
					<div class="resultTable ovcl">
						<c:forEach var="values" items="${map.listAreaImage }" varStatus="status"> 
							<div class="dataList ovcl">
								<div class="td1 fl"><%--<div class="checkbox fl" type="button" onclick="changeCheckBox($(this),1)"></div>--%>${status.index+1 }</div>
								<c:set var="arr" value="${fn:split(values.file_name, '/')}"/>
								<c:set var="arrLength" value="${fn:length(arr)}"/>  
								<div class="td2 fl"><c:out value="${arr[arrLength-1]}" /></div>
								<div class="td3 fl">
									<c:choose>
									    <c:when test="${values.image_product_type==0101 }">
									       		原始影像
									    </c:when>
									    <c:when test="${values.image_product_type == 0102}">
									        	正射影像
									    </c:when>
									</c:choose>
								</div>
								<div class="td4 fl">
								<c:choose>
									<c:when test="${values.downloadsize < 1024}">
										${values.downloadsize }B
									</c:when>
									<c:when test="${1048576 > values.downloadsize && values.downloadsize >= 1024}">
										<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.downloadsize/1024}" />KB
									</c:when>
									<c:when test="${1073741824 > values.downloadsize && values.downloadsize >= 1048576}">
										<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.downloadsize/1048576}" />MB
									</c:when>
									<c:when test="${1099511627776 > values.downloadsize && values.downloadsize >= 1073741824}">
										<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.downloadsize/1073741824}" />GB
									</c:when>	
									<c:when test="${values.downloadsize >= 1099511627776}">
										<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${values.downloadsize/1099511627776}" />TB
									</c:when>
								</c:choose>
								</div>
								<c:choose>
								    <c:when test="${values.op_status== 0 }">
								   	 <div class="td5 fl">未解密</div>
								   	 <div class="td6 fl"><div class="dataRange fl" onclick="show('${values.range}')">数据范围</div><a class="dataDownload grey fl">下载</a></div>
								    </c:when>
								    <c:when test="${values.op_status == 1}">
								    	<div class="td5 fl">已解密</div>
								    	<div class="td6 fl"><div class="dataRange fl" onclick="show('${values.range}')">数据范围</div><a class="dataDownload fl" href="${manageHost}/downDecryptionFile?fileName=${values.file_name}&fileDirectory=${values.file_directory}&examinationApprovalRecordId=${values.examination_approval_record_id}&downloadDatalistId=${values.id}">下载</a></div>
								    </c:when>
								    <c:when test="${values.op_status == 2}">
								    	<div class="td5 fl" style="line-height:normal">已下载<br/>（如需下载请重新申请）</div>
								    	<div class="td6 fl"><div class="dataRange fl" onclick="show('${values.range}')">数据范围</div><a class="dataDownload grey fl">下载</a></div>
								    </c:when>
								    <c:when test="${values.op_status == 3}">
								    	<div class="td5 fl" style="line-height:normal">解密失败<br/>（如需下载请重新申请）</div>
								    	<div class="td6 fl"><div class="reapply-btn dataRange fl" value="${values.id}" file_name="${values.file_name}" recordid="${values.record_id}"><a class="reapply-btn2" target="_blank">重新申请</a></div><div class="dataRange reapplay-margin fl" onclick="show('${values.range}')">数据范围</div><a class="dataDownload grey fl">下载</a></div>
								    </c:when>
								      <c:when test="${values.op_status == 4}">
								    	<div class="td5 fl">已重新申请下载<br/></div>
								    	<div class="td6 fl"><div class="dataRange fl" onclick="show('${values.range}')">数据范围</div><a class="dataDownload grey fl">下载</a></div>
								    </c:when>
								</c:choose>
								
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="mapDialog">
		<div class="mask"></div>
		<div class="close" onclick="$('.mapDialog').addClass('dn')"></div>
	  	<div id="map3"></div>
	</div>
  	<form id="submit_reapply" class="dn" action="" method="POST" target="_blank">
		<input type="hidden" id="recordId" name="recordIds" value="" />
		<input type="hidden" id="downloadDataListIds" name="downloadDataListIds" value="" />
		<input type="hidden" id="names" name="names" value="" />
		<input type="hidden" id="customerName" name="customerName" value="${map.customer_name}" />
		<input type="hidden" id="customerContacter" name="customerContacter" value="${map.customer_contacter}" />
		<input type="hidden" id="customerContact" name="customerContact" value="${map.customer_contact}" />
	</form>

	<div class="all-reapply-dialog" id="reapply-dialog">

	<div class="re-dialog-close"></div>
		<div class="reapply-title-top">
		<p>提示</p>
		</div>
		<div class="reapply-content"><p>确定要重新申请全部解密失败数据吗？</p></div>
		<div class="reapply-dialog-btn1">
		<a class="diolog-comfirm">确定</a><a class="dialog-cencor">取消</a>
		</div>
	</div>

  	<div class="all-reapply-dialog2" id="reapply-dialog2">
	<div class="re-dialog-close"></div>
		<div class="reapply-title-top">
		<p>提示</p>
		</div>
		<div class="reapply-content"><p>确定要重新申请该解密失败数据吗？</p></div>
		<div class="reapply-dialog-btn1">
		<a class="diolog-comfirm2">确定</a><a class="dialog-cencor">取消</a>
		</div>
	</div>
<%--   	<label style="color: red">基本信息</label><br>
  		申请状态:
  		<c:choose>
		    <c:when test="${map.examination_approval_status==0 }">
		       		待审批
		    </c:when>
		    <c:when test="${map.examination_approval_status == 1}">
		        	已审批
		    </c:when>
		    <c:when test="${map.examination_approval_status == 2}">
		        	驳回
		    </c:when>
		</c:choose>
  		<br>
  		
  		申请人名称:${map.applicant_name}<br>
  		申请时间:${map.applicant_time}<br>
  	<label style="color: red">申请内容</label><br>	
  		客户名称:${map.customer_name }<br>
  		客户联系人:${map.customer_contacter }<br>
  		合同销售面积:${map.contract_sales_area }<br>
  		客户联系方式:${map.customer_contact }<br>
  		数据用途:
  		<c:choose>
		    <c:when test="${map.purpose==0 }">
		       		样例数据
		    </c:when>
		    <c:when test="${map.purpose == 1}">
		        	销售数据
		    </c:when>
		</c:choose>
		<br>
  		
  		合同扫描件：
  		<c:choose>
		    <c:when test="${ not empty map.contract_scanning_path  }">
		       		
		       		<a href="${manageHost}/downApplicantInformationFileById?filePath=${map.contract_scanning_path  }">
		       			${fn:substringAfter(map.contract_scanning_path,'_')}
		       		</a>
		    </c:when>
		    <c:otherwise>
		       	无
		    </c:otherwise>
		</c:choose><br>
  		发票扫描件：
			<c:choose>
			    <c:when test="${ not empty map.invoice_scanning_path  }">
			    		<a href="${manageHost}/downApplicantInformationFileById?filePath=${map.invoice_scanning_path  }">
		       				${fn:substringAfter(map.invoice_scanning_path,'_')}
		       			</a>
			    </c:when>
			    <c:otherwise>
			       	无
			    </c:otherwise>
			</c:choose><br>
		<label style="color: red">数据清单</label><br>		
  		<table style="border: 1px solid red">
  			<thead>
  				<td>数据</td>
  				<td>数据(包)</td>
  				<td>面积</td>
  				<td>单价(元)</td>
  			</thead>
  			<c:forEach var="values" items="${map.listApplicantData }">
  				<tr>
  				<td>
  					${values.data_type }
  				</td>
  				<td>
  					${values.num }
  				</td>
  				<td>
  					${values.area }
  				</td>
  				<td>
  					${values.unit_price }
  				</td>
  			</tr>
  			</c:forEach>
  			
  		</table>
  		<label style="color: red">数据类表</label><br>
  		<table style="border: 1px solid red">
  			<thead>
  				<td>序号</td>
  				<td>数据包</td>
  				<td>数据类型</td>
  				<td>大小</td>
  				<td>状态</td>
  				<td>操作</td>
  			</thead>
  			<c:forEach var="values" items="${map.listAreaImage }" varStatus="status">
  				<tr>
  				<td>
  					${status.index+1 }
  				</td>
  				<td style="display: none">
  					${values.record_id }
  				</td>
  				<td>
  					${values.file_path }
  				</td>
  				<td>
  						<c:choose>
						    <c:when test="${values.image_product_type==0101 }">
						       		原始影像
						    </c:when>
						    <c:when test="${values.image_product_type == 0102}">
						        	正射影像
						    </c:when>
						</c:choose>
  				</td>
  				<td>
  					${values.downloadsize }MB
  				</td>
  				<td>
  					
  						<c:choose>
						    <c:when test="${values.op_status==0 }">
						       		未解密
						    </c:when>
						    <c:when test="${values.op_status == 1}">
						        	已解密
						    </c:when>
						    <c:when test="${values.op_status == 2}">
						        	已下载
						    </c:when>
						</c:choose>
  				</td>
  				<td>
  					<c:choose>
					    <c:when test="${values.op_status==0 }">
					       	<lable style="color:gray">下载</lable>
					    </c:when>
					    <c:when test="${values.op_status == 1}">
					    		<a href="${manageHost}/downDecryptionFile?fileName=${values.dlName}">下载</a>
					    </c:when>
					    <c:when test="${values.op_status == 2}">
					        	<lable style="color:gray">下载</lable>
					    </c:when>
					</c:choose>
  				</td>
  			</tr>
  			</c:forEach>
  		</table> --%>	
  		
<script>
	$(document).ready(function(){
		$(".applicationNum1").text($(".demoData .dataTotal .td2").text());
		setBaseLayers3();
		$(".mapDialog").addClass("dn");
		$("#loginOut").click(function(){
 			window.location.href="${manageHost}/logout";
 		});
	})
	function downloadAll(){
		$(".resultTable .active").each(function(){
			var url = $(this).parents(".dataList").find(".dataDownload").attr("href");
			window.open(url);
		})
	}
	function changeCheckBox(obj,allOrOne){
		if(allOrOne == 1){
			if(obj.hasClass("active") == true){
				obj.removeClass("active");
			}
			else{
				if(!(obj.parents(".dataList").find(".dataDownload").hasClass("grey"))){
					obj.addClass("active");
				}
			}
		}
		else{
			if(obj.hasClass("active") == true){
				obj.removeClass("active");
				$(".resultTable div").each(function(){
					$(this).find(".checkbox").removeClass("active")
				});
			}
			else{
				obj.addClass("active");
				$(".resultTable .dataList").each(function(){
					if(!($(this).find(".dataDownload").hasClass("grey"))){
						$(this).find(".checkbox").addClass("active");
					}
				});
			}
		}
	
	}

	function setBaseLayers3(){
	/* 设置中央地图区域的地图 */
    var mapOptions = { 
   		 displayProjection : new OpenLayers.Projection("EPSG:4326"),
   		 units: "degrees",
   		// numZoomLevels:15,
   		 // allOverlays: true,
   		 controls: [],
   		 projection: new OpenLayers.Projection("EPSG:4326"),
	     maxExtent: new OpenLayers.Bounds(-180.0,-90.0,180.0,90.0)
   		 };
    
    map3 = new OpenLayers.Map('map3', mapOptions);
    var baseLayer= getBaseLayer();
    baseLayer.params.format = "image/jpeg";
    map3.addLayer(baseLayer); 
    map3.addControl(new OpenLayers.Control.Navigation());
    //map3.addControl(new OpenLayers.Control.PanZoomBar({position: new OpenLayers.Pixel(2, 15)}));
	// 定位到全国
    map3.moveTo(new OpenLayers.LonLat(105.410562,31.209316),4);
	
	//增加画图控件
	//用户用来勾画想要查找的范围的工具
	drawCustomExtendVector3= new OpenLayers.Layer.Vector("drawExtent",{
		 styleMap: new OpenLayers.StyleMap({
			"default":new OpenLayers.Style({
							 /* strokeColor: "#bb0000",
							 strokeOpacity:0.3,
							 strokeColor : "#aa0000",
							 fillColor : "#993300",
							 fillOpacity:0.15, */
							 strokeColor: "#fd0505",
							  strokeOpacity:1,
				              fillOpacity:0
						})
		 })
	}); 
	map3.addLayer(drawCustomExtendVector3); 
	
}
var layerServerURL = "http://210.77.87.225:8080/geowebcache/service/wms";
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
function showDetailMap3Bounds(data)
{
	 //先清空
	if(drawCustomExtendVector3.features.length > 0) 
	 {
		drawCustomExtendVector3.removeAllFeatures();
	 }
	//现实范围
	//bounds={"type":"Polygon","coordinates":[[[113.148225,42.55438889],[113.148225,36.03571667],[119.6972889,36.03571667],[119.6972889,42.55438889],[113.148225,42.55438889]]]};
	var format=new OpenLayers.Format.WKT();
	var feature=format.read(data);
	drawCustomExtendVector3.addFeatures(feature);
	
	feature[0].geometry.calculateBounds();
	map3.zoomToExtent(feature[0].geometry.bounds)
	map3.moveTo(new OpenLayers.LonLat([map3.center.lon, map3.center.lat]),map3.zoom-1,null);
	var oldzoom = map3.zoom;
			//去到该影像位置
			var b = data;
			var format=new OpenLayers.Format.WKT();
			var feature=format.read(b);
			feature[0].geometry.calculateBounds();
			map3.zoomToExtent(feature[0].geometry.bounds);
			map3.moveTo(new OpenLayers.LonLat([map.center.lon, map.center.lat]),oldzoom,null);
}



function show(data){
	 //先清空
	if(drawCustomExtendVector3.features.length > 0) 
	 {
		drawCustomExtendVector3.removeAllFeatures();
	 }
	$(".mapDialog").removeClass("dn");
	var format=new OpenLayers.Format.WKT();
	var feature=format.read(data);
	drawCustomExtendVector3.addFeatures(feature);
	map3.zoomToExtent(drawCustomExtendVector3.getDataExtent());
}


</script>
<script>

			$(".resultTable").children(".dataList").each(function(){
			if($(".dataList .td6 .dataRange").hasClass("reapply-btn")){
				$(".all-reapply").removeClass("dn");	
				}
			});
			
			
			
			function allreapply(){		
			var applyClass =$(".dataList .td6 .reapply-btn");
			var allfileId="";
			var fileIdArray=[];
			    applyClass.each(function(i,n){
				var fileId=$(this).attr("value");
				fileIdArray[i]=fileId;
				});
			var allfile_name="";
			var filenameArray=[];
			    applyClass.each(function(i,n){
				var allfile_name=$(this).attr("file_name");
				filenameArray[i]=allfile_name;
				});
			var allrecordid=",";
			var recordidArray=[];
			    applyClass.each(function(i,n){
				var allrecordid=$(this).attr("recordid");
				recordidArray[i]=allrecordid;
		});
	
	if(fileIdArray.length>0){
		$("#submit_reapply").attr("action",getRootPath()+"/checkProcessPageReapply");
		$("#submit_reapply #downloadDataListIds").attr("value",fileIdArray);
		$("#submit_reapply #names").attr("value",filenameArray);
		$("#submit_reapply #recordId").attr("value",recordidArray);
		$("#submit_reapply").submit();
	}

}	
			function reapply(obj){
				
				var fileId=obj.parent().attr("value");
				var file_name=obj.parent().attr("file_name");
				var recordid=obj.parent().attr("recordid");

			$("#submit_reapply").attr("action",getRootPath()+"/checkProcessPageReapply");
			$("#submit_reapply #downloadDataListIds").attr("value",fileId);
			$("#submit_reapply #names").attr("value",file_name);
			$("#submit_reapply #recordId").attr("value",recordid);
			$("#submit_reapply").submit();

}
				
			$("#all-reapply-btn").click(function() {
				$("#reapply-dialog").css("display","block");
				$(".body-box").css("display","block");
	      });
			
			$(".diolog-comfirm").click(function() {
		        allreapply();
		        $("#reapply-dialog").css("display","none");
				$(".body-box").css("display","none");
		 });
			
			$(".dialog-cencor,.re-dialog-close").click(function() {
				$("#reapply-dialog").hide();
				$(".body-box").css("display","none");
			});
		

var reapplyab;
			$(".reapply-btn2").click(function() {
				$("#reapply-dialog2").css("display","block");
				$(".body-box").css("display","block");
				reapplyab=$(this);
			});
			
			$(".diolog-comfirm2").click(function() {
				   reapply(reapplyab);
					$("#reapply-dialog2").css("display","none");
					$(".body-box").css("display","none");
			});
			$(".dialog-cencor,.re-dialog-close").click(function() {
				$("#reapply-dialog2").hide();
				$(".body-box").css("display","none");
			});
			

			</script>
  </body>
</html>
