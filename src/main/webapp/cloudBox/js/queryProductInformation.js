function queryproductInformation(recordid,name){ //检索条件的获取
	showOrhidecovergeLoaderdiv(true);
	var provinceId,cityId,countyId;  
	var r = Math.random();  
	var url;
	var params;   
	var AjaxrequestType="GET";
	var selectid=$(".areaList .selected")[0].id;
	var productsourceType=encodeURI($("#shujuType a.seclick")[0].innerHTML);
	var resolutionrange = $(".start_ratio").val() + "," + $(".end_ratio").val();
	var Timerange = $("#starttime").val() + "," + $("#endtime").val();
	var cloudsrange = $(".yun_start_ratio").val() + " and " + $(".yun_end_ratio").val();
	if(recordid!=null){
		 url = "queryproductInformationByRecordid?r=" + r;
		 params = {
				 recordid:recordid,
				 name:name
		 };
	}else if(selectid=="area"){
		 url = "queryproductInformationByAreaName?r=" + r;
		 var provincediv = $("#province  li.selected")[0];
		 var citydiv = $("#city li.selected")[0];
		 var countydiv = $("#county li.selected")[0];
		 if(provincediv!=undefined){
			 provinceId=provincediv.id;
		 }
		 if(citydiv!=undefined){
			 cityId=citydiv.id;
		 }
		 if(countydiv!=undefined){
			 countyId=countydiv.id;
		 }
		 params = {
				 provinceId : provinceId,
				 cityId : cityId,
				 CountyId : countyId,
				 productsourceType : productsourceType,
				 cloudsrange : cloudsrange,
				 Timerange : Timerange,
				 resolutionrange:resolutionrange
		 };
	}
	else if(selectid=="graph_select"){
		url = "queryproductInformationByGeom?r=" + r;
		var feature = wfst.getFeatureByFid("graph");
		if(feature==null){
			alert("请框选图形");
			showOrhidecovergeLoaderdiv(false);
			return false;
		}
		var geomWKT = wkt_c.write(feature);
		params = {
				geom : geomWKT,
				productsourceType : productsourceType,
				cloudsrange : cloudsrange,
				Timerange : Timerange,
				resolutionrange:resolutionrange
		};	
		AjaxrequestType="POST";
	}
	else if(selectid=="shp"){
		url = "queryproductInformationByGeom?r=" + r;
		var feature = wfst.getFeatureByFid("graph");
		if(feature==null){
			alert("请上传或导出shape文件");
			showOrhidecovergeLoaderdiv(false);
			return false;
		}
		var geomWKT = wkt_c.write(feature);
		params = {
				geom : geomWKT,
				productsourceType : productsourceType,
				cloudsrange : cloudsrange,
				Timerange : Timerange,
				resolutionrange:resolutionrange
		};
		
		AjaxrequestType="POST"
			
	}
	else if(selectid=="longitude"){
		url = "queryproductInformationByGeom?r=" + r;
		var inputs = $(".longitude  input");// .chidrens("input");
		var left = $.trim(inputs[0].value);// 经度（左）
		var top = $.trim(inputs[1].value)// 纬度（上）
		var right = $.trim(inputs[2].value);// 经度（右）
		var bottom = $.trim(inputs[3].value);// 纬度（下）
		if(!left || !top || !right || !bottom 
				||isNaN(left) ||isNaN(top) ||isNaN(right) ||isNaN(bottom) ){
			alert("四角点坐标不能为空且只能为数字！");
			showOrhidecovergeLoaderdiv(false);
			return 0;
		}
		var TopLeftLongitude = left;
		var TopLeftLatitude = top;
		var BottomLeftLongitude = left;
		var BottomLeftLatitude = bottom;
		var BottomRightLongitude = right;
		var BottomRightLatitude = bottom;
		var TopRightLongitude = right;
		var TopRightLatitude = top;
		var geom = "POLYGON((" + TopLeftLongitude + " " + TopLeftLatitude + ","
				+ BottomLeftLongitude + " " + BottomLeftLatitude + ","
				+ BottomRightLongitude + " " + BottomRightLatitude + ","
				+ TopRightLongitude + " " + TopRightLatitude + ","
				+ TopLeftLongitude + " " + TopLeftLatitude + "))";
		params = {
				geom : geom,
				productsourceType : productsourceType,
				cloudsrange : cloudsrange,
				Timerange : Timerange,
				resolutionrange:resolutionrange
			};
	}
	$.ajax({
		type : AjaxrequestType,
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			showOrhidecovergeLoaderdiv(false);
			if (data.code == "1") {
				clearfeaturelayer();
				if(data.HtmltableInit=="originalimageresultList"){
					var  parentdiv=$("#"+ data.HtmltableInit +" table");
					parentdiv.empty();
					var  originalimageresultList_statisticsdiv=$(".result_list").find("p.list-foot");
					var tr=$("<tr>");
					var td=$("<td>");
					var i=$("<i>");
					originalimageresultList_statisticsdiv.find(".hei").html(data.list.length);
					originalimageresultList_statisticsdiv.find(".hong").html(0);
					var list=$(data.list).sort(function(a,b){
						var atime=new Date(a.collect_start_time.replace("-", "/"));  
						var btime=new Date(b.collect_start_time.replace("-", "/"));  
						if(atime>btime) return -1;
						if(atime>btime) return 1;
						return 0;
					});
				    var features=new Array();
					$(list).each(function(e) {
						var trclone = tr.clone();
						trclone.attr("id",this.id);
						trclone.attr("geom",this.gemo);
						trclone.attr("file_path",this.file_path);
						trclone.attr("filename",this.name);	
						trclone.attr("DataType",this.image_satellite_type);
						trclone.append(td.clone().addClass("operate").append(i.clone().addClass("ite")).append(i.clone().addClass("hyan").attr("title","贴图")).append(i.clone().addClass("qiu").attr("title","详情")).append(i.clone().addClass("xzai").attr("title","下载")));
						trclone.append(td.clone().addClass("weixin").text(this.image_satellite_type));
						trclone.append(td.clone().addClass("chuanganqi").text(this.sensor_id));
						trclone.append(td.clone().addClass("caijishijian").text(this.collect_start_time));
						trclone.append(td.clone().addClass("fenbilv").text(this.image_start_resolution));
						trclone.appendTo(parentdiv);
						var geom=wkt_c.read(this.gemo);
						geom.fid=this.id;
						features.push(geom);
						//listToPastGeomF(this.gemo,this.id);
					});
					activeControlForOriginalimageResultList(parentdiv);
					showOrhideQueryListByQueryRecordid(0);
				}else if(data.HtmltableInit=="orthoimageresultList"){
					var  parentdiv=$("#"+ data.HtmltableInit +"");
					parentdiv.empty();
					var div=$("<div>");
					var span=$("<span>");
					var a=$("<a>");
					var  orthoimageresultList_statisticsdiv=$(".resultList2_result").find("div.list-foot");
					orthoimageresultList_statisticsdiv.find(".hei").html(data.list.length);
					orthoimageresultList_statisticsdiv.find(".hong").html(0);
					var list=$(data.list).sort(function(a,b){
						var atime=new Date(a.collect_end_time.replace("-", "/"));  
						var btime=new Date(b.collect_end_time.replace("-", "/"));  
						if(atime>btime) return -1;
						if(atime>btime) return 1;
						return 0;
					});
				    var features=new Array();
					$(list).each(function(e) {
					    var divclone= div.clone().addClass("resultList_table");
					    divclone.attr("id",this.id);
					    divclone.attr("geom",this.gemo);
					    divclone.attr("file_path",this.file_path);
					    divclone.attr("filename",this.name);	
					    divclone.attr("DataType",this.image_satellite_type);
					    divclone.attr("image_area",this.image_area);
					    divclone.attr("collect_time",this.collect_end_time);
					    divclone.attr("image_spectrum_type",this.image_spectrum_type);
					    divclone.attr("image_resolution",this.image_start_resolution+"-"+this.image_end_resolution);
					    divclone.attr("img_url",this.img_url);
					    span.clone().addClass("w1").append(a.clone().addClass("square-checkbox square-checkbox-image ").attr("type","button")).appendTo(divclone);
					    span.clone().addClass("w2").attr("title",this.collect_start_time+"-"+this.collect_end_time).text(this.collect_start_time+"-"+this.collect_end_time).appendTo(divclone);
					    var divclone2=div.clone();
					    var spanclone=span.clone().addClass("w3").append(divclone2);
					    spanclone.appendTo(divclone);
					    if(this.area_description!=null||this.area_description!=""){
					    	divclone2.text(this.area_description);
					    	divclone2.attr("title",this.area_description);
							$(divclone2.parent().parent()[0]).attr("area_name",this.area_description);
					    }else{
					    	 AreaqueryForNameByIds(this.area_no_array,divclone2);
					    }
					    span.clone().addClass("w4").append(a.clone().addClass("eye").attr("type","button").attr("title","贴图")).append(a.clone().addClass("showDetail").attr("type","button").attr("title","详情")).append(a.clone().addClass("download").attr("type","button").attr("title","下载")).appendTo(divclone);
					    divclone.appendTo(parentdiv);
						var geom=wkt_c.read(this.gemo);
						geom.fid=this.id;
						features.push(geom);
						//listToPastGeomF(this.gemo,this.id);
					});
					activeControlFororthoimageResultList(parentdiv);
					showOrhideQueryListByQueryRecordid(1);
				}
				listToPastGeomFs(features);
				zoomlistextent();
			} else {
				var  parentdiv=$("#originalimageresultList table");
				parentdiv.empty();
				var  parentdiv=$("#orthoimageresultList table");
				parentdiv.empty();
				var  originalimageresultList_statisticsdiv=$(".result_list").find("p.list-foot");
				originalimageresultList_statisticsdiv.find(".hei").html(0);
				originalimageresultList_statisticsdiv.find(".hong").html(0);	
				alert(data.message);
			}	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html";
		}
	});
}
function AreaqueryForNameByIds(areaIdarray,spanclone){
	var url = "AreaqueryForNameByIds.htm";
	var params = {
		areaIdarray : areaIdarray
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			spanclone.text(data);
			spanclone.attr("title",data);
			$(spanclone.parent().parent()[0]).attr("area_name",data);
			}
		});
}
function activeControlForOriginalimageResultList(parentdiv){
	parentdiv.find('tr').each(function(i,n){
		$(this).find('td:first').children().each(function(i,n){
			$(this).on("click",function(e){
				var id=$(this).parent().parent()[0].getAttribute("id");
				var geom=$(this).parent().parent()[0].getAttribute("geom");
				var filepath=$(this).parent().parent()[0].getAttribute("file_path");
				var filename=$(this).parent().parent()[0].getAttribute("filename");
				var DataType=$(this).parent().parent()[0].getAttribute("DataType");
				var iscorrect=$(this).parent().parent()[0].getAttribute("iscorrect");
				if($(this).hasClass("ite")){
					if(!$(this).hasClass("gxuan")){
						$(this).addClass("gxuan");
						iteGxuan(id,true);
					}else{
						$(this).removeClass("gxuan");
						iteGxuan(id,false);
					}
				}else if($(this).hasClass("hyan")||$(this).hasClass("lyan") ){
					if(checkdatabackopisready(id,true)){
						if($(this).hasClass("hyan")){
							$(this).removeClass("hyan").addClass("lyan");
							hyan(geom,filepath,filename);
						}else{
							$(this).removeClass("lyan").addClass("hyan");
							hyan(geom,filepath,filename);
						}
					}else{
						alert("数据已经入库,等待影像校正返回缩略图!");
					}
				}else if(this.className=="qiu"){
					if(!checkdatabackopisready(id,true)){
						alert("后台正处理影像缩略图校正中,若先查看缩略图,请稍候查询!");
					}
					qiu(id,filepath,filename,DataType);
				}else if(this.className=="xzai"){
					xzai(id);
				}
			});
		});	
	});
}
function activeControlFororthoimageResultList(parentdiv){
	parentdiv.children('div').each(function(i,n){
		$(this).find("a").each(function(i,n){
			$(this).on("click",function(e){
				var id=$(this).parent().parent()[0].getAttribute("id");
				var geom=$(this).parent().parent()[0].getAttribute("geom");
				var filepath=$(this).parent().parent()[0].getAttribute("file_path");
				var filename=$(this).parent().parent()[0].getAttribute("filename");
				var DataType=$(this).parent().parent()[0].getAttribute("DataType");
				var image_area=$(this).parent().parent()[0].getAttribute("image_area");
				var collect_time=$(this).parent().parent()[0].getAttribute("collect_time");
				var area_name=$(this).parent().parent()[0].getAttribute("area_name");
				var image_spectrum_type=$(this).parent().parent()[0].getAttribute("image_spectrum_type");
				var image_resolution=$(this).parent().parent()[0].getAttribute("image_resolution");
				var img_url=$(this).parent().parent()[0].getAttribute("img_url");
				if($(this).hasClass("square-checkbox")){
					if(!$(this).hasClass("checkbox-blue")){
						$(this).addClass("checkbox-blue");
						iteGxuan(id,true);
					}else{
						$(this).removeClass("checkbox-blue");
						iteGxuan(id,false);
					}
					orthoimageresultListgxuanitem();
				}else if($(this).hasClass("eye")){
					if(checkdatabackopisready(id,false)){
					    if(this.img_url==null|this.img_url=="localhost"){
					    	alert("该数据是在单机版入库的,因此无地图服务!");
					    	return false;
					    }
						if(!$(this).hasClass("eye-on")){
							$(this).addClass("eye-on");
							hyanbyorthoimage(geom,img_url);
						}else{
							$(this).removeClass("eye-on");
							hyanbyorthoimage(geom,img_url);
						}
					}
				}else if($(this).hasClass("showDetail")){
					if(!checkdatabackopisready(id,false)){
						alert("后台正处理影像缩略图校正中,若先查看缩略图,请稍候查询!");
					}
					qiu2(collect_time,area_name,image_area,DataType,image_resolution,image_spectrum_type);
				}else if($(this).hasClass("download")){
					xzai(id);
				}
			});
		});
	});
}
function  checkdatabackopisready(id,checkwhat){
	var url = "querydatacallbackopisreadybyId";
	var r = Math.random();
	var params = {
		r:r,
		id : id,
		checkwhat:checkwhat
	};
	var flag=false;
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		async: false,
		success : function(data) {
			flag=data;
		}
	});
	return flag;
}
function  iteGxuan(id,status){
	if(status==true){
		selectfeaturestyle(id);
	}else{
		unselectfeaturestyle(id);
	}
	originalimageresultListgxuanitem();
}
function  hyan(geom,filepath,filename){
	PasteImageTomap(geom,filepath,filename);
}
function hyanbyorthoimage(geom,img_url){
    	PasteImagelayerTomap(geom,img_url);
}
function qiu(id,filepath,filename,DataType){

	/*var urlGetXML = "getXMLInfo.htm";
	var params = {
		id : id,
		filepath : filepath,// 路径
		name : filename,// 文件名称
		DataType : DataType// 数据类型，GF-1/ZY3 etc
	};*/
	var url = getRootPath()+"/getrscmAreaImageById";
	var params = {
		id : id,
	}
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			var parentdiv   =$("#detial_table");
			//parentdiv.empty();
			if (data) {
				//需要显示的字段,顺序不能错,与HTML页面上显示字段对应
				var xmlModelName = ["image_row_col","image_satellite_type","image_start_resolution","image_spectrum_type","begin_time","update_time","image_product_type","name","image_cloudage","sensor_id","product_level","collect_start_time","product_id","image_area","collect_end_time","tip","srid","area_description","downloadsize"];
				var xmlModel=data;
				var tr=$("<tr>");
				var td=$("<td>");
				var img=$("<img>");
				$(".detail-img").html(img.clone().attr("src","getImage.htm?filepath="+ filepath+ "&name="+ filename));
				/*	 */
				/*tr.clone().append(td.clone().addClass("td_one").text("缩略图")).append(td.clone().addClass("td-two").append(img.clone().attr("src","getImage.htm?filepath="+ filepath+ "&name="+ filename))).appendTo(parentdiv);*/ 
				/*for ( var key in xmlModel) {
				 tr.clone().append(td.clone().addClass("td_one").text(key)).append(td.clone().addClass("td-two").text(xmlModel[key])).appendTo(parentdiv); 
				
				}*/
				var j=0;
				for(var i in xmlModelName){
					
					if(xmlModelName[i] != "downloadsize" && xmlModel[xmlModelName[i]] != null){
					  
						$("#detial_table").find(".td-two").eq(j).text(xmlModel[xmlModelName[i]]);
						
						j++;
						
					}
					else if(xmlModel[xmlModelName[i]] != null){
						if(xmlModel[xmlModelName[i]] < 1024){
							$("#detial_table").find(".td-two").eq(j).text(xmlModel[xmlModelName[i]]+"B");
							j++;
						}
						else if(1048576 > xmlModel[xmlModelName[i]] && "10288" >= 1024){
							$("#detial_table").find(".td-two").eq(j).text((xmlModel[xmlModelName[i]]/1024).toFixed(2)+"KB");
							j++;
						}
						else if(1073741824 > xmlModel[xmlModelName[i]] && xmlModel[xmlModelName[i]] >= 1048576){
							$("#detial_table").find(".td-two").eq(j).text((xmlModel[xmlModelName[i]]/1048576).toFixed(2)+"MB");
							j++;
						}else if(1099511627776 > xmlModel[xmlModelName[i]] && xmlModel[xmlModelName[i]] >= 1073741824){
							$("#detial_table").find(".td-two").eq(j).text((xmlModel[xmlModelName[i]]/1073741824).toFixed(2)+"GB");
							j++;
						}else{
							$("#detial_table").find(".td-two").eq(j).text((xmlModel[xmlModelName[i]]/1099511627776).toFixed(2)+"TB");
							j++;
						}
					}else{
						j++;
					}
				}
				showAndhidenLeftTab(3);
				}else{
					alert(data.message);
				}
			}
		});
}
function qiu2(collect_time,area_name,image_area,DataType,image_resolution,image_spectrum_type){
	var parentdiv   = $("div.resultList2_detial table").first();
	parentdiv.empty();
	var tr=$("<tr>");
	var td=$("<td>");
	tr.clone().append(td.clone().text("时间")).append(td.clone().addClass("right").text(collect_time)).appendTo(parentdiv);
	tr.clone().append(td.clone().text("范围区域")).append(td.clone().addClass("right").text(area_name)).appendTo(parentdiv);
	tr.clone().append(td.clone().text("面积（平方公里）")).append(td.clone().addClass("right").text(image_area)).appendTo(parentdiv);
	tr.clone().append(td.clone().text("分辨率")).append(td.clone().addClass("right").text(image_resolution+"m")).appendTo(parentdiv);
	tr.clone().append(td.clone().text("产品类型")).append(td.clone().addClass("right").text(image_spectrum_type)).appendTo(parentdiv);
	tr.clone().append(td.clone().text("卫星")).append(td.clone().addClass("right").text(DataType)).appendTo(parentdiv);
	tr.clone().append(td.clone().text("数据类型")).append(td.clone().addClass("right").text("正射影像")).appendTo(parentdiv);
	showAndhidenLeftTab(4);
}
function xzai(filepath){
	/*var targzfilename="";
	targzfilename=substringfilename(filepath);
	var url = "gettargzfileonline.htm?r=" + Math.random()+ "&filepath=" + filepath + "&name=" + targzfilename;
	var form = $("<form>"); // 定义一个form表单
	form.attr('style', 'display:none'); // 在form表单中添加查询参数
	form.attr('target', '_blank');
	form.attr('method', 'post');
	form.attr('action', url);
	$('body').append(form); // 将表单放置在web中
	form.submit();*/
	
	$("#submit_form").attr("action",getRootPath()+"/checkProcessPage");
	$("#submit_form #ids").attr("value",filepath);
	$("#submit_form").submit();
}
function showAndhidenLeftTab(type){
	if(type=="1"){
		
	}else if(type=="2"){
		
	}else if(type=="3"){
	/*	$(".second .item_b").removeClass("selecteder"); */
		$(".second_leve2 .leve2_item").css("display", "block"); 
	/*	$("#detail_info").addClass("selecteder"); 
		var class_name = $("#detail_info").attr("id"); */
		$(".detail_info").css("display", "block");
		$(".box-bg").css("display", "block");
		$(".close-detail").click(function(){
		$(".detail_info").css("display", "none");
		$(".box-bg").css("display", "none");
		});
	}else if(type=="4"){
		$("div.resultList2_result").css("display", "block");
		$(".resultList2_detial").css("display", "block");
		$(".box-bg").css("display", "block");
		$(".close-detail").click(function(){
		$(".resultList2_detial").css("display", "none");
		$(".box-bg").css("display", "none");
		});
	}	
}
function onlyShowGxuan(){
	var parentdiv=$("#originalimageresultList table");
	parentdiv.find('tr').each(function(i,n){
		$(this).find('td:first').children(".ite").each(function(i,n){
			if(!$(this).hasClass("gxuan")){
				$(this).parent().parent().first().hide();
			}
		});
	});
}
function onlyshowsatellite(){
	showOrhidecovergeLoaderdiv(true);
	var spans=$(".wxin_chose").find("span.ongreen");
	var spanongreenchoose="";
	for(var i=0;i<spans.length;i++){
		spanongreenchoose+=$(spans[i]).next().text();
	}
    var features=new Array();
	var parentdiv=$("#originalimageresultList table");
		parentdiv.find('tr').each(function(i,n){
			$(this).children(".weixin").each(function(i,n){
					if(spanongreenchoose.lastIndexOf($(this).text())==-1){
						$(this).parent().hide();
						$(this).parent().find('td:first').children(".ite").removeClass("gxuan");
						var feature=featurelayer.getFeatureByFid($(this).parent()[0].id);
						feature.style={
							display: "none"
						};
						features.push(feature);  
						//hidelistToPastGeomF2($(this).parent()[0].id);
					}else{
						$(this).parent().show();	
						var feature=featurelayer.getFeatureByFid($(this).parent()[0].id);
						feature.style=null;
						features.push(feature);   
						//showlistToPastGeomF2($(this).parent()[0].id);	
					}
			});
		});
	originalimageresultListgxuanitem();
	featurelayer.redraw(features);
	showOrhidecovergeLoaderdiv(false);
}
function cancelonlyshowsatellite(){
	var parentdiv=$("#originalimageresultList table");
	parentdiv.find('tr').show();
}
function cancelonlyShowGxuan(){
	var parentdiv=$("#originalimageresultList table");
	parentdiv.find('tr').show();
}
function originalimageresultListgxuanitem(){
	var  originalimageresultList_statisticsdiv=$(".result_list").find("p.list-foot");
	var  originalimageresultList_gxuan=$("#originalimageresultList  table tr td i.gxuan ");
	originalimageresultList_statisticsdiv.find(".hong").html(originalimageresultList_gxuan.length);
}
function orthoimageresultListgxuanitem(){
	var  orthoimageresultList_statisticsdiv=$(".resultList2_result").find("div.list-foot");
	var  orthoimageresultList_gxuan=$("#orthoimageresultList  div span.w1 a.checkbox-blue ");
	orthoimageresultList_statisticsdiv.find(".hong").html(orthoimageresultList_gxuan.length);
}
function downloadAll(){
	var  originalimageresultList_gxuan=$("#originalimageresultList  table tr td i.ite.gxuan ");
	var allfileId="";
	var fileIdArray=[];
	originalimageresultList_gxuan.each(function(i,n){
		/*var filepath=$(this).parent().parent()[0].getAttribute("file_path");
		var filename=substringfilename(filepath);
		filename=filename+".tar.gz";
		allfilepath+=filepath+"/"+filename+",";*/
		var fileId=$(this).parent().parent()[0].getAttribute("id");
		fileIdArray[i]=fileId;
		
	})
	//allfileId=allfileId.substring(0,allfileId.lastIndexOf(","));
	/*var url = "getalltargzfileonline.htm?r=" + Math.random()+ "&allfilepath=" + allfilepath ;
	var form = $("<form>"); // 定义一个form表单
	form.attr('style', 'display:none'); // 在form表单中添加查询参数
	form.attr('target', '_blank');
	form.attr('method', 'post');
	form.attr('action', url);
	$('body').append(form); // 将表单放置在web中
*/	
	//alert(allfileId);
	/*for(var i =1 ; i<=100 ; i++){
		fileIdArray[i]=fileIdArray[1];
	}*/
	if(fileIdArray.length>0){
		$("#submit_form").attr("action",getRootPath()+"/checkProcessPage");
		$("#submit_form #ids").attr("value",fileIdArray);
		$("#submit_form").submit();
	}
	else{
		alert('请勾选数据');
	}
}
function showOrhidecovergeLoaderdiv(tag){
	if(tag==true){
		$("#covergediv").show(); 
		$(".myplfp").show(); 
	}else{
		$("#covergediv").hide(); 
	}
}
function showOrhideQueryListByQueryRecordid(type){
	var layers= map.getLayersBy("fid","PLLayers");   
    if(layers!=null){
          $(layers).each(function(){
        	  	this.setVisibility(false);
          });
    }	
	$(".cloudTab .item").removeClass("selecteder");
	$(".rettj").css("display", "none");
	$(".result").css("display", "none");
	$("#result").addClass("selecteder");
	hiddenMask();
	cancelDrawPolygon();
	if (type == 0) {
		$(".resultList2").css("display", "none");
		$(".result").css("display", "block");
/*		$(".map_content").css("marginLeft", "489px"); */
		$(".cloud_left").css("width", "489px");
		$(".cloudMain").css("width", "452px");
	} else if (type == 1)  {
		$(".result").css("display", "none");
		$(".resultList2").css("display", "block");
/*		$(".map_content").css("marginLeft", "448px"); */
		$(".cloud_left").css("width", "448px");
		$(".cloudMain").css("width", "410px");
	}
}
function substringfilename(filepath){
	var targzfilename="";
	filepath=filepath.replaceAll("//","/");
	filepath=filepath.replaceAll("\\\\","/");
	var targzfilenames=  filepath.split("/");
	if((filepath.lastIndexOf("/")+1)==filepath.length){
		targzfilename=targzfilenames[targzfilenames.length-2];
	}else{
		targzfilename=targzfilenames[targzfilenames.length-1];
	}
	return targzfilename;
}