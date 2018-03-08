/*点击查询弹出遮罩层*/
String.prototype.replaceAll  = function(s1,s2){   
    return this.replace(new RegExp(s1,"gm"),s2);   
} 
 function showMask() {
	        var ctl = $(".myplfp");
	        var documentHeight = $(document).height();
	        var windowheight = $(window).height();
	        var width = $(document).width();
	        if (documentHeight < windowheight) {
	            documentHeight = windowheight;
	        }
	        $("body").append("<div class='mask' id='mask'></div>");
	        $("#mask").css("height", documentHeight + "px");
	        ctl.css("left", "50%");
	        ctl.css("top", "50%");
	        var ctlWidth = ctl.css("width").replace("px", "");
	        var ctlHeight = ctl.css("height").replace("px", "");
	        ctl.css("marginTop", -ctlHeight / 2 + "px");
	        ctl.css("marginLeft", -ctlWidth / 2 + "px");
	        ctl.css("display", "block");
	    }
	    ///隐藏弹出框
	    function hiddenMask() {
	        var ctl = $(".myplfp");
	        $("#mask").remove();
	        ctl.css("display", "none");
	    }



/*
 * 初始化屏幕高度
 * */

 
 $(function() {
	resizeView();
});
$(window).resize(function(){
	resizeView();
})
function resizeView(){
	var height = $(window).height();
	if (height <= 600) {
		var cloudCT = $("#cloudCT");
		cloudCT.height(600);
	}else{
		var height = $(document.body).height();
		var header = $("#header").height();
		var cloudCT = $("#cloudCT");
		cloudCT.height(height - header);
	}
	$(window).resize(function() {
		var height = $(window).height();
		if (height <= 600) {
			var cloudCT = $("#cloudCT");
			cloudCT.height(600);
		}else{
			var height = $(document.body).height();
			var header = $("#header").height();
			var cloudCT = $("#cloudCT");
			cloudCT.height(height - header);
		}
	});
}

/*
 * 初始化分辨率、云量控件
 * */
$(document).ready(function() {
	//分辨率滑块控制
	//生成slider变量；
	var slider = $(".ratio_slider").slider({
	    //step:10,
		range : true,
		min : 0,
		max : 100,
		values : [0, 3],
		slide : function(event, ui) {
			$(".start_ratio").val(ui.values[0]);
			$(".end_ratio").val(ui.values[1]);
		}
	});
	
	
	$(".start_ratio").val($(".ratio_slider").slider("values", 0));
	$(".end_ratio").val($(".ratio_slider").slider("values", 1));
	
	//正则函数
	function getRerr(value){
		var re=/^([0-9]|[0-9]{2}|100)$|^[1-9]\d\.\d+$|^\d\.\d+$/;
		if(!re.test(value)){			
			return false;
		}
		else{
			return true;
		}
	}
	
	$(".start_ratio").blur(function() {
		var value=$(this).val();
		if(!getRerr(value))
		{
			alert('请输入0-100范围的数字');
			$(this).val('0');
			return ;
		}
		if(value.indexOf(".")!=-1){
		   value= value.substring(0,value.indexOf(".")+3); 
		}
		var min=parseFloat(value);
		var max=parseFloat($(".end_ratio").val());
		if(min<=max){
			$(".ratio_slider").slider('option', 'values', [min,max]);
			$(this).val(min);
		}
		else{
			alert("开始数字不能大于结束数字");
			$(this).val("0");
			$(".end_ratio").val('3');
			$(".ratio_slider").slider('option', 'values', [0,3]);
		}
	});

	
	$(".end_ratio").blur(function() {
		var value=$(this).val();
		if(!getRerr(value))
		{
			alert('请输入0-100范围的数字');
			$(this).val("3");
			var max=$(this).val();
			var min=$(".start_ratio").val('0');
			$('.ratio_slider').slider('option', 'values', [0,max]);
			return;
		}
		if(value.indexOf(".")!=-1){
			   value= value.substring(0,value.indexOf(".")+3); 
		}
		var max=parseFloat(value);
		var min=parseFloat($(".start_ratio").val());
		if(min<=max){
			$(".ratio_slider").slider('option', 'values', [min,max]);
			$(this).val(max);
		}
		else{
			alert("结束数字不能小于开始数字");
			$(this).val("3");
			$(".start_ratio").val('0');
			$(".ratio_slider").slider('option', 'values', [0,3]);
		}
	});
	/*$(".start_cloud").val($(".cloud_slider").slider("values", 0));
	$(".end_cloud").val($(".cloud_slider").slider("values", 1));
	$(".start_cloud").change(function() {
		sliderc.slider("values[0]", $(this).val());
	});*/

	//云量
	var slider = $(".yun_slider").slider({
		range : true,
		min : 0,
		max : 100,
		values : [ 0, 10 ],
		slide : function(event, ui) {
			$(".yun_start_ratio").val(ui.values[0]);
			$(".yun_end_ratio").val(ui.values[1]);
		}
	});
	
	$(".yun_start_ratio").val($(".yun_slider").slider("values", 0));
	$(".yun_end_ratio").val($(".yun_slider").slider("values", 1));
	$(".yun_start_ratio").change(function() {
		var value=$(this).val();
		if(!getRerr(value))
		{
			alert('请输入0-100范围的数字');
			$(this).val('0');
			return ;
		}
		if(value.indexOf(".")!=-1){
		  value= value.substring(0,value.indexOf(".")+3); 
		}
		var min=parseFloat(value);
		var max=parseFloat($(".yun_end_ratio").val());
		if(min<=max){
			$('.yun_slider').slider('option', 'values', [min,max]);
			$(this).val(min);
		}
		else{
			alert("开始数字不能大于结束数字");
			$(this).val("0");
			$('.yun_slider').slider('option', 'values', [0,max]);
		}
	});
//改变
	$(".yun_end_ratio").change(function() {
		var value=$(this).val();
		if(!getRerr(value))
		{
			alert('请输入0-100范围的数字');
			$(this).val("10");
			return ;
		}
		if(value.indexOf(".")!=-1){
			   value= value.substring(0,value.indexOf(".")+3); 
		}
		var max=parseFloat(value);
		var min=parseFloat($(".yun_start_ratio").val());
		if(max>=min){
			$('.yun_slider').slider('option', 'values', [min,max]);
			$(this).val(max);
		}
		else{
			alert("结束数字不能小于开始数字");
			$(this).val("10");
			$(".yun_start_ratio").val('0');
			$('.yun_slider').slider('option', 'values', [0,10]);
		}
	});
	
	
	/*$(".end_ratio").change(function() {
		if(!getRe($(this).val()))
		{
			alert('请输入1-100范围的数字');
			$(this).val("3");
			return ;
		}
		var max=$(this).val();
		var min=$(".start_ratio").val();
		$('.ratio_slider').slider('option', 'values', [min,max]);
	});*/
	
	
	$(".wxin").click(function() {
		$(".wxin_chose").show();
		return false;//关键是这里，阻止冒泡
	});
	$(".wxin_chose").click(function() {
		return false;
	});
	$(document).click(function() {
		$(".wxin_chose").hide();
	});
	$(".wxin_chose").find("span.nogreen").click(function(){
		if($(this).hasClass("ongreen")){
			$(this).removeClass("ongreen");
		}else{
			$(this).addClass("ongreen");
			$(".ite-all").removeClass("gxuan");
		}
		
		onlyshowsatellite();
	});
	bindInputchange();
});

/*
 * 结果条件、结果列表和详细信息板块切换
 * */

var current_module = "condition";
  /*$(".cloudTab .item").click(function() {
	 $(".cloudTab .item").removeClass("selecteder");
	 $(this).addClass("selecteder");
	 $(".rettj").css("display", "none");
	 if ($("#shujuType").attr("value") == "0") {
		 $(".result").css("display", "block");//数据 类型第一个表格
		 $(".resultList2").css("display", "none");
	 }else{
		 $(".resultList2").css("display", "block");
		 $(".result").css("display", "none");
	 }
	 //判断是原始影像 ？
	 //显示result
	 
 });*/
$(".cloudTab .item").click(function() {
	$(".cloudTab .item").removeClass("selecteder");
	$(".rettj").css("display", "none"); //检索条件整个版面
	$(".result").css("display", "none");//数据 类型第一个表格
	$(this).addClass("selecteder");
	var class_name = $(this).attr("id");
	if ($(this).attr("id") == "condition") {
		$("." + class_name).css("display", "block");
	/*	$(".map_content").css("marginLeft", "404px");  */
		$(".cloud_left").css("width", "404px");
		$(".cloudMain").css("width", "367px");
		$(".resultList").hide();
		current_module = "condition";
	} else if ($("#shujuType").attr("value") == "0") {
		$(".resultList2").css("display", "none");
		$("." + class_name).css("display", "block");
	/*	$(".map_content").css("marginLeft", "489px"); */ 
		$(".cloud_left").css("width", "489px");
		$(".cloudMain").css("width", "452px");
		$(".resultList").show();
		current_module = "result";
	} else {
		$("." + class_name).css("display", "none");
		$("#resultList2").css("display", "block");
	/*	$(".map_content").css("marginLeft", "449px");  */
		$(".cloud_left").css("width", "448px");
		$(".cloudMain").css("width", "411px");
		$(".resultList").show();
		current_module = "result";
	}
	showOrhidecovergeLoaderdiv(false);
});


//原始
$(".result_list .item_b").click(function() {
	$(".second .item_b").removeClass("selecteder");
	$(".second_leve2 .leve2_item").css("display", "none");
	$(this).addClass("selecteder");
	var class_name = $(this).attr("id");
	$(".second_leve2 ." + class_name).css("display", "block");
});
$(".areaList .item_a").click(function() {
	cancelDrawPolygon();
	$(".areaList .item_a").removeClass("selected");
	$(".first_leve2 .leve2_item").css("display", "none");
	$(this).addClass("selected");
	var class_name = $(this).attr("id");
	$(".first_leve2 ." + class_name).css("display", "block");
});
//正射
$("a.resultList2_result").click(function() {
	$("a.resultList2_result").addClass("on");
	$("a.resultList2_detial").removeClass("on");
	$("div.resultList2_detial").css("display", "none");
	$("div.resultList2_result").css("display", "block");
});
$("a.resultList2_detial").click(function() {
	$("a.resultList2_detial").addClass("on");	
	$("a.resultList2_result").removeClass("on");
	$("div.resultList2_detial").css("display", "block");
	$("div.resultList2_result").css("display", "none");
});
function bindInputchange(){
	$(".first_leve2 .shp input").change(
			function() {
				var r = Math.random();
				var url = "Importshpfile?r="+r;
				var uplist = $(".first_leve2 .shp input[name^=uploads]");
				var arrId = [];
				for ( var i = 0; i < uplist.length; i++) {
					if (uplist[i].value) {
						arrId[i] = uplist[i].id;
					}
				}
				
				$.ajaxFileUpload({
							url : url,
							secureuri : false,
							fileElementId : arrId,
							dataType : 'json',
							success : function(data) {
								if (data.code == 1) {
									var wkt = data.data;
									var geom = wkt_c.read(wkt);
									geom.fid="graph";
									wfst.addFeatures(geom);
									map.zoomToExtent(geom.geometry.getBounds());
								} else {
									alert(data.message);
								}
							},
							complete : function(data) {
								$(".first_leve2 .shp ").empty();
								$(".first_leve2 .shp  ").html('<a href="javascript:;" class="schuan"><input type="file" name="uploads" id="imgFile">上传shape文件</a><a id="shpdownloads" class="dru">导出shape文件</a>');
								bindInputchange();
							},
							error: function (data, status, e) {  
				                alert(e);  
				            }
						});
	});
	$("#shpdownloads").click(function(){
		var r = Math.random();
		var feature = wfst.getFeatureByFid("graph");
		if(feature==null){
			alert("地图中无检索使用的几何对象！");
			return false;
		}
		var geomWKT = wkt_c.write(feature);
		var url = "Exportshpfile?r=" + r;
		var form = $("<form>"); // 定义一个form表单
		form.attr('style', 'display:none'); // 在form表单中添加查询参数
		form.attr('target', '');
		form.attr('method', 'POST');
		form.attr('action', url);
		// <input name="uploads" id="imgFile"type="file" />
		var input = "<input type=\"text\" name=\"geomWKT\" value=\"" + geomWKT
				+ "\" />";//
		$(input).appendTo(form);
		$('body').append(form); // 将表单放置在web中
		form.submit();
	});
}
/*
 * 正射影像结果列表  鼠标悬浮高亮
 * */
/*$(".detial tr").hover(function() {
	$(this).addClass("blue");
}, function() {
	$(this).removeClass("blue");
});*/

$(".list_body_1 div").hover(function() {
	$(this).addClass("blue");
}, function() {
	$(this).removeClass("blue");
});


/*
 * 全选
 * */
$(".square-checkboxAll").click(function() {
	showOrhidecovergeLoaderdiv(true);
	var stutes=true;
	if ($(".square-checkboxAll").attr("class").indexOf("checkbox-blue") == -1) {
		$(".square-checkboxAll").addClass("checkbox-blue");
		$(".square-checkbox").addClass("checkbox-blue");
		stutes=true;
	} else {
		$(".square-checkboxAll").removeClass("checkbox-blue");
		$(".square-checkbox").removeClass("checkbox-blue");
		stutes=false;
	}
	orthoimageresultListgxuanitem();
    var  features =new Array();
	$(".square-checkbox").each(function(i,n){
		var geom=featurelayer.getFeatureByFid($(this).parent().parent()[0].getAttribute("id"));
		if(stutes==true){
			selectControl.select(geom);
		}else{
			selectControl.unselect(geom);
		}	
		features.push(geom);
		// iteGxuan($(this).parent().parent()[0].getAttribute("id"),stutes);
	});
	showOrhidecovergeLoaderdiv(false);
	featurelayer.redraw(features);
	map.zoomToExtent(featurelayer.getDataExtent());
         
});
$(".ite-all").click(function() {
	showOrhidecovergeLoaderdiv(true);
	var stutes=true;
	if ($(".ite-all").attr("class").indexOf("gxuan") == -1) {  
		$(".ite-all").addClass("gxuan");
		stutes=true;
	} else {
		$(".ite-all").removeClass("gxuan");

		stutes=false;
	}
	
	var  features =new Array();　
	$(".ite").each(function(i,n){
		if($($(this).parent().parent()[0]).is(":hidden")!=true){
			var parent=$(this).parent().parent()[0];
			var geom=featurelayer.getFeatureByFid($(this).parent().parent()[0].getAttribute("id"));
			if(stutes==true){
				$(this).addClass("gxuan");
				selectControl.select(geom);
			}else{
				$(this).removeClass("gxuan");
				selectControl.unselect(geom);
			}	
		}else{
			$(this).removeClass("gxuan");
			var geom=featurelayer.getFeatureByFid($(this).parent().parent()[0].getAttribute("id"));
			geom.style={
					display: "none"
			};
		}
	    features.push(geom);
	});
	showOrhidecovergeLoaderdiv(false);
	originalimageresultListgxuanitem();
	featurelayer.redraw(features);
	if(featurelayer.getDataExtent()!=null)
	map.zoomToExtent(featurelayer.getDataExtent());
});

/*//眼睛贴图
$(".eye").click(function() {
	if ($(this).attr("class").indexOf("eye-on") == -1)
		$(this).addClass("eye-on");
	else
		$(this).removeClass("eye-on");
});*/
$(".xzaiAll").click(function() {
	downloadAll();
	
});
//仅显示已选择
$(".showChose").on("click", function() {
	if (!$(this).hasClass("gxuan")){
		$(this).addClass("gxuan");
			onlyShowGxuan();
		}
	else{
		$(this).removeClass("gxuan");
			cancelonlyShowGxuan();
		}
});
//单个选中
$(".square-checkbox").on("click",function(){
	if($(this).attr("class").indexOf("checkbox-blue")==-1) //检索是否已存在‘checkbox-blue’这个类=-1，表示不存在此字符串
		$(this).addClass("checkbox-blue");
	else
	$(this).removeClass("checkbox-blue");
});
$(".ite").on("click",function(){
	if($(this).attr("class").indexOf("gxuan")==-1)  //检索是否已存在‘gxuan’这个类=-1，表示不存在此字符串
		$(this).addClass("gxuan");
	else
	$(this).removeClass("gxuan");
});


/*$(".nogreen").click(function() {
	if ($(this).attr("class").indexOf("on") == -1)
		$(this).addClass("ongreen");
	else
		$(this).removeClass("ongreen");
});*/

//原始影像和正射影像选择
/*$(".zshe").click(function() {
	$(".yshi").removeClass("seclick");
	$(".zshe").addClass("seclick");
	$("#shujuType").attr("value", "1");
});
$(".yshi").click(function() {
	$(".zshe").removeClass("seclick");
	$(".yshi").addClass("seclick");
	$("#shujuType").attr("value", "0");
});
*/

/*$(".condition,.results").on('click',function(){
	if($(this).hasClass('condition')){
	$(".result").css("display","none");
	$(".resultList2").css("display","none");
	}
	
	if($(this).hasClass('results')){
		if ($("#shujuType").attr("value") == "0") {
		  $(".result").css("display", "block");
		}else{
		  $(".resultList2").css("display", "block");
		 } 
	}
});*/

//查询



$(".askbt").click(function(){
	$(".resultList").show();
	//增加日期验证
	var starDate = new Date($("#starttime").val());
	var endDate = new Date($("#endtime").val());
	
	if(starDate.getTime()>endDate.getTime()){
		
		alert("开始时间不能大于结束时间");
		return false;
	}
	var layers= map.getLayersBy("fid","PLLayers");   
    if(layers!=null){
          $(layers).each(function(){
        	  	this.setVisibility(false);
          });
    }
	$(".cloudTab .item").removeClass("selecteder");
	$(".rettj").css("display", "none");
	//$(".result").css("display", "block");
	$("#result").addClass("selecteder");
	hiddenMask();
	cancelDrawPolygon();
	if ($("#shujuType").attr("value") == "0") {
		$(".resultList2").css("display", "none");
		$(".result").css("display", "block");
	/*	$(".map_content").css("marginLeft", "489px"); */
		$(".cloud_left").css("width", "489px");
		$(".cloudMain").css("width", "452px");
		queryproductInformation();
	} else {
		$(".result").css("display", "none");
		$(".resultList2").css("display", "block");
	/*	$(".map_content").css("marginLeft", "448px"); */
		$(".cloud_left").css("width", "448px");
		$(".cloudMain").css("width", "410px");
		
		queryproductInformation();
		//queryproductInformation(1);
	}
	
	
});

//采集时间排序
var TimeSequenceflag=false;
$(".caijishijian").click(function(){
	if(TimeSequenceflag){
		TimeSequenceflag=false;
		$(this).css("background","url(cloudBox/img/sanjiao.jpg) no-repeat 95px 13px");
	}else{
		TimeSequenceflag=true;
		$(this).css("background","url(cloudBox/img/xsanjiao.jpg) no-repeat 95px 13px");
	}
	var  originalimageresultList=resultsListTimeSequence(TimeSequenceflag);
	var  originalimageresultListtable=$("#originalimageresultList  table tbody");
	originalimageresultListtable.empty();
	originalimageresultList.appendTo(originalimageresultListtable);
	activeControlForOriginalimageResultList(originalimageresultListtable);
});
function UnitConverter(){
	var bboxarray= new Array();
	var Tbboxarray= new Array();
	bboxarray[0]=$.trim($(".longitude  input")[0].value);
	bboxarray[1]=$.trim($(".longitude  input")[1].value);
	bboxarray[2]=$.trim($(".longitude  input")[2].value);
	bboxarray[3]=$.trim($(".longitude  input")[3].value);
	if(!bboxarray[0] || !bboxarray[1] || !bboxarray[2] || !bboxarray[3] 
			||isNaN(bboxarray[0]) ||isNaN(bboxarray[1]) ||isNaN(bboxarray[2]) ||isNaN(bboxarray[3])){
		alert("四角点坐标不能为空！");
		return false;
	}
	var unit = "degree";
    Tbboxarray[0] =formatlonDegree(bboxarray[0]);
    if(Tbboxarray[0]==false){
    	Tbboxarray[0] =lonDegreeConvertBack(bboxarray[0]);
    	if(Tbboxarray[0]==false){
    		unit = "undefined";
    		alert("左上经度单位转换失败！范围(经度：180~-180，纬度：90~-90)!");
    		return false;
    	}
    	unit = "degreems";
    }
    if(unit=="degree"){
    	Tbboxarray[2] =formatlonDegree(bboxarray[2]);
    	if(Tbboxarray[2]==false){
    		alert("右上经度单位转换失败！");
    		return false;
    	}
    	if(!lonDegreecompare(Tbboxarray[0],Tbboxarray[2])){
    		alert("右下经度不能比左上经度大！");
    		return false;
    	}
    	Tbboxarray[1] =formatlatDegree(bboxarray[1]);
    	if(Tbboxarray[1]==false){
    		alert("左上纬度单位转换失败！");
    		return false;
    	}
    	Tbboxarray[3] =formatlatDegree(bboxarray[3]);
    	if(Tbboxarray[3]==false){
    		alert("右下纬度单位转换失败！");
    		return false;
    	}
    	if(!latDegreecompare(Tbboxarray[1],Tbboxarray[3])){
    		alert("右下纬度不能比左上纬度大！");
    		return false;
    	}
    }else if(unit=="degreems"){
       	Tbboxarray[2] =lonDegreeConvertBack(bboxarray[2]);
    	if(Tbboxarray[2]==false){
    		alert("右上经度单位转换失败！");
    		return false;
    	}
    	if(!loncompare(Tbboxarray[0],Tbboxarray[2])){
    		alert("右下经度不能比左上经度大！");
    		return false;
    	}
    	Tbboxarray[1] =latDegreeConvertBack(bboxarray[1]);
    	if(Tbboxarray[1]==false){
    		alert("左上纬度单位转换失败！");
    		return false;
    	}
    	Tbboxarray[3] =latDegreeConvertBack(bboxarray[3]);
    	if(Tbboxarray[3]==false){
    		alert("右下纬度单位转换失败！");
    		return false;
    	}
    	if(!latcompare(Tbboxarray[1],Tbboxarray[3])){
    		alert("右下纬度不能比左上纬度大！");
    		return false;
    	}
    }else{
    	alert("左上经度单位转换失败！范围(经度：180~-180，纬度：90~-90)!");
    	return false;
    }
	$(".longitude  input")[0].value=Tbboxarray[0];
	$(".longitude  input")[1].value=Tbboxarray[1];
	$(".longitude  input")[2].value=Tbboxarray[2];
	$(".longitude  input")[3].value=Tbboxarray[3];
}
function  ShowUnitConverter(){
	var bboxarray= new Array();
	bboxarray[0]=$.trim($(".longitude  input")[0].value);
	bboxarray[1]=$.trim($(".longitude  input")[1].value);
	bboxarray[2]=$.trim($(".longitude  input")[2].value);
	bboxarray[3]=$.trim($(".longitude  input")[3].value);
	if(!bboxarray[0] || !bboxarray[1] || !bboxarray[2] || !bboxarray[3] 
	||isNaN(bboxarray[0]) ||isNaN(bboxarray[1]) ||isNaN(bboxarray[2]) ||isNaN(bboxarray[3])){
		alert("四角点坐标不能为空且只能为数字！");
		return false;
	}
	wfst.removeAllFeatures();
	var arry = new Array();
	arry[0] = new OpenLayers.Geometry.Point(bboxarray[0], bboxarray[1]);
	arry[1] = new OpenLayers.Geometry.Point(bboxarray[2], bboxarray[1]);
	arry[2] = new OpenLayers.Geometry.Point(bboxarray[2], bboxarray[3]);
	arry[3] = new OpenLayers.Geometry.Point(bboxarray[0], bboxarray[3]);
	arry[4] = new OpenLayers.Geometry.Point(bboxarray[0], bboxarray[1]);
	var lineS = new  OpenLayers.Geometry.LinearRing(arry);
	var pgeom = new OpenLayers.Geometry.Polygon(lineS);
	var Feature = new OpenLayers.Feature.Vector(pgeom);
	wfst.addFeatures(Feature);
	map.zoomToExtent(pgeom.getBounds());
}

//点击正射|原始

$('.zshe,.yshi').on('click',function(){
	$(".resultList").hide();
	if($(this).hasClass('zshe')){
		$(".yshi").removeClass("seclick");
		$(".zshe").addClass("seclick");
		$("#shujuType").attr("value", "1");
		$('.yunslider_wrap').hide();
		
		$(".result").css("display", "none");
		$(".resultList2").css("display", "block");
	}
	else{
		$(".zshe").removeClass("seclick");
		$(".yshi").addClass("seclick");
		$("#shujuType").attr("value", "0");
		$('.yunslider_wrap').show();
		$(".resultList2").css("display", "none");
		$(".result").css("display", "block");
	}

});
var otherHieght = 191;
$("div.gdtiao").css("max-height",$(window).height()-otherHieght+"px");

//ie兼容性
if( !('placeholder' in document.createElement('input')) ){
	    $('input[placeholder],textarea[placeholder]').each(function(){    
	      var that = $(this),    
	      text= that.attr('placeholder');    
	      if(that.val()===""){    
	        that.val(text).addClass('placeholder');    
	      }    
	      that.focus(function(){    
	        if(that.val()===text){    
	          that.val("").removeClass('placeholder');    
	        }    
	      })    
	      .blur(function(){    
	        if(that.val()===""){    
	          that.val(text).addClass('placeholder');    
	        }    
	      })    
	      .closest('form').submit(function(){    
	        if(that.val() === text){    
	          that.val('');    
	        }    
	      });    
	    });    
	  } 
/*$(function(){
	$('tr i[class=xzai],td a[class=download]').each(function(){
				$(this).attr("title","下载");
	})
	$('tr i[class=qiu]').each(function(){
				$(this).attr("title","详情");
				
	})
	$('tr i[class=hyan],a[class=eye]').each(function(){
				$(this).attr("title","贴图");
				
	})
	$('tr i[class=lyan]').each(function(){
				$(this).attr("title","显示");
				
	})
	$('span[class=w3]').each(function(){
		$(this).attr("title",$(this).html());
	})
})
*/
