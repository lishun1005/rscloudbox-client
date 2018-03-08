var tabsflag=1; //选择上传方式的标示；
var uploaders=new Array(5);　//上传控件，全局变量
var filearray =new Array(null,null,null,null,null);//记录要上传的文件id
var filearray2 =new Array(null,null,null,null,null);
$(function(){
	
	var html= window.location.search;
	//String name= new String(request.getParameter("username").getBytes("ISO-8859-1"), "UTF-8");
	if(html=="?html=1"){
		checkRecord();
	}else{
		$("#recordDiv").hide();
		$("#dataLoaderDiv").show();
		clearInterval(taskid1);
		clearInterval(taskid2);
	}
	//dataLoader record 切换
	$("#headruku").click(function(){
		
		$("#recordDiv").hide();
		$("#dataLoaderDiv").show();
		clearInterval(taskid1);
		clearInterval(taskid2);
	});
	$("#headRecord").click(function(){
		checkRecord();
	});
	//监控页面跳转及关闭
	window.onbeforeunload = onbeforeunload_handler;
	/* 第一步数据类型选择 */
	// 下拉列表框的显示与隐藏
	$(".selectimg").click(function(){
		if($(".yshiAbse").is(":hidden")){
       		$(".yshiAbse").show();    
		}else{
        	$(".yshiAbse").hide();     
        }
	});
	//正射数据 第三步显示与隐藏
	$(".yshiAbse li").click(function(){
		$(".yshiDate").text($(this).text());
		$(".yshiAbse").hide();
		clickInitialize();
		if($(".yshiDate").text()=="正射数据"){
			$(".secondStep2").show();
		}else{
			$(".secondStep2").hide();
		}
	});
	$("#bgwhite .daoru").click(function(){
		initExcelUploadControl("zidongp",$("#zidongp").attr("num"));
	});
	//监听第二步tabs切换
	$("#UploadFile").click(function(){
		tabsflag=1;
		clickInitialize()
	});
	$("#GtdataPath").click(function(){
		tabsflag=2;
		clickInitialize()
	});
	$("#NetworkPath").click(function(){
		tabsflag=3;
		clickInitialize()
	});
	$("#NetworkPath").click(function(){
		tabsflag=3;
		clickInitialize()
	});
	$("#tabsul").children("li").click(function(){
		$("#zidongp").attr("num",$(this).attr("num"));
		initExcelUploadControl("zidongp",$("#zidongp").attr("num"));
	});
	//云盒确认按钮初始化触发
	$(".okbut").click(function(){
		$('#bg').hide();	
		$('#show1').hide();
		var selectedli= $("#slist li[selected='selected']");
		var tabsTwodiv= $("#tabsTwo p[class='Twop1']" );
		if(selectedli.length>1){
			var num=$(".result").attr("num");
			var pnum=$("#tabsTwo p[num='"+num+"']");
			pnum.text($(selectedli[0]).text());
			pnum.attr("filepath",$(selectedli[0]).attr("filepath"));
			if($(".yshiDate").text()=="正射数据"){
				$(".tabsli"+num).show();
				$("#tabs"+num+" input[class='dataname']").val($(selectedli[i]).text());
			}
			$("#tabs"+num+" input[class='dataname']").val($(selectedli).text());
			for (var i=1;i<selectedli.length;i++){
				$("<p class='Twop1' num='"+(tabsTwodiv.length+i-1)+"' filepath='"+$(selectedli[i]).attr("filepath")+"' >"+$(selectedli[i]).text()+"</p><a id='del-p2'>&nbsp;</a>").insertAfter($('#tabsTwo p[num='+($("#tabsTwo p").length-1)+']').next());
				if($(".yshiDate").text()=="正射数据"){
					$(".tabsli"+(tabsTwodiv.length+i-1)).show();
					$("#tabs"+(tabsTwodiv.length+i-1)+" input[class='dataname']").val($(selectedli[i]).text());
				}
			}
		}else{
			var num=$(".result").attr("num");
			$("#tabsTwo p[num='"+num+"']").text(selectedli.text());
			$("#tabsTwo p[num='"+num+"']").attr("filepath",$(selectedli).attr("filepath"));
			if($(".yshiDate").text()=="正射数据"){
				$(".tabsli"+num).show();
				$("#tabs"+num+" input[class='dataname']").val($(selectedli).text());
			}
		}
		if($('#tabsTwo p').length>4){
			$("#tabsTwo input[class='add-p2']").hide();
		}
	})
	//云盒取消按钮初始化触发
	$(".nobut").click(function(){
		$('#bg').hide();
		$('#show1').hide();
		$(".result").val('');
		$('#show1 li').css("background-color","#ffffff");	
	});
	//日期初始化
	initData();
});
$(function(){
	//tab栏初始化
	$('#tabs,#oslsvn,#tabsbox').tabs();
	//初始化第一个本地上传按钮
	addUploadControl("picker0",0);
	//增加本地上传按钮
	$('.add-p1').click(function(){
		if($('#tabsOne p').length<5){
			var uploadnums= $('#tabsOne p').length;
			var id="picker"+uploadnums;
			$("<p id='"+id+"' num='"+uploadnums+"'>点击上传本地的数据包(zip,tar.gz)</p><a class='del-p1'>&nbsp;</a>").insertAfter($("#tabsOne p[id='picker"+(uploadnums-1)+"']").next());
			addUploadControl(id,uploadnums);
			if($(".yshiDate").text()=="正射数据"){
				if($('#tabsOne p').length==1){
					$(".tabsli0").show();
					$(".bao0").show();	
				}else if($('#tabsOne p').length==2){
					$(".tabsli1").show();
				}else if($('#tabsOne p').length==3){
					$(".tabsli2").show();
				}else if($('#tabsOne p').length==4){
					$(".tabsli3").show();
				}else if($('#tabsOne p').length==5){
					$(".tabsli4").show();
				}
			}
			if(uploadnums>=4){
				$(this).hide();
			}
		}else{
			alert("最多能同时上传5个文件");
			$(this).hide();
		}
	})
	//增加云盒位置上传按钮
	$('.add-p2').click(function(){
	   if($('#tabsTwo p').length<5){
		    var num=$("#tabsTwo p").length;
		    $("<p class='Twop1' num='"+num+"' >点击以选择云盒中的数据包</p><a class='del-p2'>&nbsp;</a>").insertAfter($('#tabsTwo p[num='+(num-1)+']').next());
			if($(".yshiDate").text()=="正射数据"){
				if($('#tabsTwo p').length==1){
					$(".tabsli0").show();
					$(".bao0").show();	
				}else if($('#tabsTwo p').length==2){
					$(".tabsli1").show();
				}else if($('#tabsTwo p').length==3){
					$(".tabsli2").show();
				}else if($('#tabsTwo p').length==4){
					$(".tabsli3").show();
				}else if($('#tabsTwo p').length==5){
					$(".tabsli4").show();
				}
			}
			if($('#tabsTwo p').length>4){
				$(this).hide();
			}
			if($('#tabsTwo p').length>4){
				$(this).hide();
			}
		}else{
			$(this).hide();
		}	
	})
	//增加网络位置上传按钮
	$('.add-p3').click(function(){
	  	if($('.inputtext').length<5){
	  		 var num=$("#tabsThree input[type='text']").length;
	  		 $("<input num='"+num +"'  type='text' placeholder='请输出数据包的Internet地址或网络地址' class='inputtext'/><a class='del-p3'>&nbsp;</a><br num='"+num+"'/>").insertAfter($('#tabsThree input[num='+(num-1)+']').next().next());
	  		 if($(".yshiDate").text()=="正射数据"){
				if($("#tabsThree input[type='text']").length==1){
					$(".tabsli0").show();
					$(".bao0").show();	
				}else if($("#tabsThree input[type='text']").length==2){
					$(".tabsli1").show();
				}else if($("#tabsThree input[type='text']").length==3){
					$(".tabsli2").show();
				}else if($("#tabsThree input[type='text']").length==4){
					$(".tabsli3").show();
				}else if($("#tabsThree input[type='text']").length==5){
					$(".tabsli4").show();
				}
			}
			if($("#tabsThree input[type='text']").length>4){
				$(this).hide();
			}
			if($("#tabsThree input[type='text']").length>4){
				$(this).hide();
			}
	  	}else{
			$(this).hide();
		}
	})
})
function  initExcelUploadControl(id,i){
	var ExcelUploader= WebUploader.create({
		// 不压缩image
		resize : false,
		// swf文件路径
		swf : 'baidu/webuploader-0.1.5/js/Uploader.swf',
		// 文件接收服务端。
		server : 'readexcelfile.htm',
		//表格参数
		fileNumLimit:2,
		auto:true,
		// 选择文件的按钮。可选。
		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
		pick : '#'+id
	});
	ExcelUploader.on('beforeFileQueued',function(file){
		var files=ExcelUploader.getFiles();
		for(var j=0;j<files.length;j++){
			ExcelUploader.removeFile(files[j]); 
		}	
		return true;
	});
	ExcelUploader.on('uploadSuccess',function(file,response ){
		$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val(response.collect_start_time);
		$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val(response.collect_end_time);
		$("#tabs"+i+" .scopdiv input[class='scopetxt']").val(response.area_description);
		$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val(response.image_start_resolution);
		$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val(response.image_end_resolution);
		$("#tabs"+i+" .bjtext input[class='biaotxt']").val(response.tip);
	});
	ExcelUploader.on('fileQueued',function(file){
		$("#zidongp").text(file.name);
	});
}

/*添加本地上传控件，触发*/
function addUploadControl(id,i){
	if(i<5){
		uploaders[i] = WebUploader.create({
			// 不压缩image
			resize : false,
			// swf文件路径
			swf : 'baidu/webuploader-0.1.5/js/Uploader.swf',
			// 文件接收服务端。
			server : 'inputstorageuploadfile.htm',
			//表格参数
			accept:{
				title: '只支持tar。gz包',
			    extensions: 'gz',
			    mimeTypes: 'application/x-gtar'},
			fileNumLimit:2,
			duplicate:true,
			// 内部根据当前运行是创建，可能是input元素，也可能是flash.
			pick : '#'+id
		});
		uploaders[i].on('beforeFileQueued',function(file){
			var files=uploaders[i].getFiles();
			for(var j=0;j<files.length;j++){
				if($.inArray(files[j].id, filearray2)==-1){
					uploaders[i].removeFile(files[j],true); 
				}
			}	
			if(file.name.indexOf("untitled")<0){
				filearray[i]=file.id;
				filearray2[i]=file.id;
			}
			return true;
		});
		uploaders[i].on('uploadProgress',function(file,percentage){
			$("#tabsOne p[num='"+i+"']").text((percentage*100).toFixed(2)+"%");
			var tbody=$("#ruDiv tbody");
			var tr=tbody.find("tr[id='"+file.id+"']");
			if(tr.length==0){
				var tr = $("<tr>");
				tr.attr("id",file.id);
				var td = $("<td>");
				var childrentd = td.clone();
				childrentd.text(xulie());
				childrentd.appendTo(tr);
				var childrentd1 = td.clone();
				childrentd1.text($.myTime.DateFormat("yyyy-MM-dd hh:mm:ss", new Date(s)));
				childrentd1.appendTo(tr);
				var childrentd2 = td.clone();
				childrentd2.text(file.name);
				childrentd2.appendTo(tr);
				var childrentd3 = td.clone();
				childrentd3.text((file.size/(1024*1024)).toFixed(2)+"Mb");
				childrentd3.appendTo(tr);
				var childrentd4 = td.clone();
				childrentd4.text("数据上传了"+(percentage*100).toFixed(2)+"%");
				childrentd4.appendTo(tr);
				var childrentd5 = td.clone();;
				childrentd5.appendTo(tr);
				var childrentd6 = td.clone();
				childrentd6.appendTo(tr);
				var childrentd6 = td.clone();
				childrentd6.appendTo(tr);
				tr.appendTo(tbody);
			}else{
				tr.find('td:nth-child(5)').text("数据上传了"+(percentage*100).toFixed(2)+"%");
			}
			
		});
		uploaders[i].on('startUpload',function(file){
			filearray[i]=null;
		});
		// 当有文件添加进来的时候记录文件名字
		uploaders[i].on('fileQueued',function(file){
			$("#"+id +" div[class='webuploader-pick']").text(file.name);
			$("#tabs"+i+" input[class='dataname']").val(file.name);
		});
	}else{
		alert("最多能同时上传5个文件");
	}
}
/*删除del-p1*/
$(document).on('click','.del-p1',function(){	
	var num=parseInt($(this).prev("p").attr("num"));
	var yshiDateText=$(".yshiDate").text();
	var uploadnumsP=$('#tabsOne p');
	if(uploadnumsP.length==1){
		alert("最少上传一个文件！");
		return;
	}
	for(var i=0;i<uploadnumsP.length;i++){
		var uploaderstats=uploaders[i].getStats();
		if(uploaderstats.progressNum!=0){
			alert("目前,有数据正在进行,禁止进行删除操作!");
			return;
		}
	}
	for(var i=num;i<(uploadnumsP.length-1);i++){
		filearray[i]=filearray[i+1];
		filearray2[i]=filearray2[i+1];
		var replayfiles=uploaders[i+1].getFiles();
		uploaders[i].addFiles(replayfiles);
		if(replayfiles.length==0){
			$("#picker"+i +" div[class='webuploader-pick']").text("点击上传本地的数据包(zip,tar.gz)");
		}
		if(yshiDateText=="正射数据"){
			$("#tabs"+i+" input[class='dataname']").val($("#tabs"+(i+1)+" input[class='dataname']").val());
			$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .scopdiv input[class='scopetxt']").val($("#tabs"+(i+1)+" .scopdiv input[class='scopetxt']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='start_ratio']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='end_ratio']").val());
			$("#tabs"+i+" .zbx input[class='bdrdius']").val($("#tabs"+(i+1)+" .zbx input[class='bdrdius']").val());
			$("#tabs"+i+" .bjtext input[class='biaotxt']").val($("#tabs"+(i+1)+" .bjtext input[class='biaotxt']").val());
		}
	}
	filearray[(uploadnumsP.length-1)]=null;
	filearray2[(uploadnumsP.length-1)]=null;
	uploaders[(uploadnumsP.length-1)].destroy();
    $("#picker"+(uploadnumsP.length-1)).remove();
	if(yshiDateText=="正射数据"){
		$(".tabsli0 a").trigger("click");
		$(".tabsli"+(uploadnumsP.length-1)).hide();
		$(".bao"+(uploadnumsP.length-1)).hide();
	}
	if($('#tabsOne p').length<5){
		$('.add-p1').show();
	};
});
$(document).on('click','.del-p2',function(){
	var num=parseInt($(this).prev("p").attr("num"));
	var yshiDateText=$(".yshiDate").text();
	var uploadnumsP=$('#tabsTwo p');
	if(uploadnumsP.length==1){
		alert("最少需提供一条云盒路径！");
		return;
	}
	for(var i=num;i<uploadnumsP.length;i++){
		$("#tabsTwo p[num='"+(i+1)+"']").attr("num",i);
		if(i==num){
			$(this).prev().remove();
			$(this).remove();
		}
		if(yshiDateText=="正射数据"){
			$("#tabs"+i+" input[class='dataname']").val($("#tabs"+(i+1)+" input[class='dataname']").val());
			$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .scopdiv input[class='scopetxt']").val($("#tabs"+(i+1)+" .scopdiv input[class='scopetxt']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='start_ratio']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='end_ratio']").val());
			$("#tabs"+i+" .zbx input[class='bdrdius']").val($("#tabs"+(i+1)+" .zbx input[class='bdrdius']").val());
			$("#tabs"+i+" .bjtext input[class='biaotxt']").val($("#tabs"+(i+1)+" .bjtext input[class='biaotxt']").val());
		}
	}
	if(yshiDateText=="正射数据"){
		$(".tabsli0 a").trigger("click");
		$(".tabsli"+(uploadnumsP.length-1)).hide();
		$(".bao"+(uploadnumsP.length-1)).hide();
	}
	if($('#tabsTwo p').length<5){
		$('.add-p2').show();
	};
});
$(document).on('click','.del-p3',function(){
	var num=parseInt($(this).prev("input").attr("num"));
	var yshiDateText=$(".yshiDate").text();
	var uploadnumsP=$("#tabsThree input[type='text']");
	if(uploadnumsP.length==1){
		alert("最少需提供一条网络路径！");
		return;
	}
	for(var i=num;i<uploadnumsP.length;i++){
		$("#tabsThree input[num='"+(i+1)+"']").attr("num",i);
		if(i==num){
			$(this).prev().remove();
			$(this).remove();
			$("#tabsThree br[num='"+num+"']").remove();
		}
		$("#tabsThree br[num='"+(i+1)+"']").attr("num",i);
		if(yshiDateText=="正射数据"){
			$("#tabs"+i+" input[class='dataname']").val($("#tabs"+(i+1)+" input[class='dataname']").val());
			$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val($("#tabs"+(i+1)+" .datebox input[class='collect_start_time caiji mr6']").val());
			$("#tabs"+i+" .scopdiv input[class='scopetxt']").val($("#tabs"+(i+1)+" .scopdiv input[class='scopetxt']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='start_ratio']").val());
			$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val($("#tabs"+(i+1)+" .rangslider_wrap input[class='end_ratio']").val());
			$("#tabs"+i+" .zbx input[class='bdrdius']").val($("#tabs"+(i+1)+" .zbx input[class='bdrdius']").val());
			$("#tabs"+i+" .bjtext input[class='biaotxt']").val($("#tabs"+(i+1)+" .bjtext input[class='biaotxt']").val());
		}
	}
	if(yshiDateText=="正射数据"){
		$(".tabsli0 a").trigger("click");
		$(".tabsli"+(uploadnumsP.length-1)).hide();
		$(".bao"+(uploadnumsP.length-1)).hide();
	}
	if($("#tabsThree input[type='text']").length<5){
		$('.add-p3').show();
	};
});
/************初始化云盒路径*************/
function initGtdataPaths(){
	var r = Math.random(); 
	var url=" getinputstoragegtdatapath.htm?r=" + r;
	$.ajax({
		type:"GET",
		url:url,
		dataType : "json",
		success: function(data){
			if(data.code=="1"){
				var fatherdiv=$("#slist");
				fatherdiv.empty();
				initGtdataPathstodiv(data.gtdatapath,fatherdiv);
				activeGtdataPathstodiv();
			}else{
				$("#show1 h4").text("加载失败"+map.message);
				}	
			}
	});
}
function initGtdataPathstodiv(childrenpaths,fatherdiv){
	for(var object in childrenpaths){  
		if(childrenpaths[object].filetype==false){
			fatherdiv.append("<li id='"+childrenpaths[object].filename+"' filepath='"+childrenpaths[object].filepath+"'>"+childrenpaths[object].filename+"</li>");
		}else{
			var uldiv=$("<ul id='"+childrenpaths[object].filename+"' filepath='"+childrenpaths[object].filepath+"'><a>"+childrenpaths[object].filename+"</a></ul>");
			fatherdiv.append(uldiv);
			fatherdiv.append(uldiv);
			if(childrenpaths[object].childrenfile!=undefined&&childrenpaths[object].childrenfile!=null){
				initGtdataPathstodiv(childrenpaths[object].childrenfile,uldiv);
			}
			var  aa=uldiv.find("a");
			uldiv.find("a").on("click",function(e){
				$(this).parent().find("li").toggle();
			});
	
		}
	}
}
function activeGtdataPathstodiv(){
	$('#show1 li').on("click",function(e){
		//选择关闭和开启
		var selectedli= $("#slist li[selected='selected']");
		var tabsTwodiv= $("#tabsTwo p[class='Twop1']" );
		var length= selectedli.length+(tabsTwodiv.length-1);
		if($(this).attr("selected")=="selected"||$(this).attr("selected")==true){
			$(this).attr("selected",false);
			 $(this).css("background-color","#ffffff");
		}else{
			if((length)>4){
				alert("最多能同时上传5条数据,已有"+(length)+"条，可选"+(5-length)+"条。");
			}else{
				$(this).attr("selected",true);
				$(this).css("background-color","#3399fe");
			}
		}
	});
}
function uploadfile(){
	var data={};
	for(var i=0;i<uploaders.length;i++){ 
		if(uploaders[i]!=undefined){
			if($(".yshiDate").text()=="原始影像"||$(".yshiDate").text()=="原始数据"){
				//原始影像
				data={
					datatype:encodeURI($(".yshiDate").text()),
					isorthoimage:false
				};
			}else{
				//正射影像
				data={
					datatype:encodeURI($(".yshiDate").text()),
					name:$("#tabs"+i+" input[class='dataname']").val(),
					isorthoimage:true,
					collect_start_time:$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val(),
					collect_end_time:$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val(),
					area_description:encodeURI($("#tabs"+i+" .scopdiv input[class='scopetxt']").val()),
					image_start_resolution:$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val(),
					image_end_resolution:$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val(),
					srid:$("#tabs"+i+" .zbx input[class='bdrdius']").val(),
					tip:encodeURI($("#tabs"+i+" .bjtext input[class='biaotxt']").val())
				};
			}
			var uploadfromData=uploaders[i].options;
			uploadfromData.formData	=data;
			uploaders[i].upload();
		}
	}	
}
/************云盒路径入库*************/
function uploadGtdataPath(){
	 var GtdataPath= $("#tabsTwo p[class='Twop1']");
	 $(GtdataPath).each(function(e) {
		 var r = Math.random(); 
		 var url=" inputstoragegtdatapath.htm?r=" + r;
		 if($(this).text()!="点击以选择云盒中的数据包"&&$(this).text()!=null&&$(this).text()!=""){
			var data={};
			if($(".yshiDate").text()=="原始影像"||$(".yshiDate").text()=="原始数据"){
				data={
						datatype:encodeURI($(".yshiDate").text()),
						gtdatapath:encodeURI($(this).attr("filepath")),
						isorthoimage:false
				};
			}else{
				var i=$(this).attr("num");
				data={
						datatype:encodeURI($(".yshiDate").text()),
						isorthoimage:true,
						name:encodeURI($("#tabs"+i+" input[class='dataname']").val()),
						collect_start_time:$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val(),
						collect_end_time:$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val(),
						area_description: encodeURI($("#tabs"+i+" .scopdiv input[class='scopetxt']").val()),
						image_start_resolution:$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val(),
						image_end_resolution:$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val(),
						srid:encodeURI($("#tabs"+i+" .zbx input[class='bdrdius']").val()),
						tip:encodeURI($("#tabs"+i+" .bjtext input[class='biaotxt']").val()),
						gtdatapath:encodeURI($(this).attr("filepath"))
					};
			}
			$.ajax({
				type:"GET",
				url:url,
				dataType : "json",
				data : data,
				success: function(data){
					if(data.code=="1"){
						
					}else{
						
					}
				}
			});
		 }
	 });
}
function uploadNetworkPath(){
	 var NetworkPath= $("#tabsThree input[type='text']");
	 $(NetworkPath).each(function(e) {
		 var r = Math.random(); 
		 var url=" inputstoragenetworkpath.htm?r=" + r;
		 if($(this).val()!="请输出数据包的Internet地址或网络地址"&&$(this).val()!=null&&$(this).val()!=""){
			var data={};
			if($(".yshiDate").text()=="原始影像"||$(".yshiDate").text()=="原始数据"){
				data={
						datatype:encodeURI($(".yshiDate").text()),
						networkpath:encodeURI($(this).val())
				};
			}else{
				data={
						datatype:encodeURI($(".yshiDate").text()),
						isorthoimage:true,
						name:encodeURI($("#tabs"+i+" input[class='dataname']").val()),
						collect_start_time:$("#tabs"+i+" .datebox input[class='collect_start_time caiji mr6']").val(),
						collect_end_time:$("#tabs"+i+" .datebox input[class='collect_end_time caiji ml6']").val(),
						area_description: encodeURI($("#tabs"+i+" .scopdiv input[class='scopetxt']").val()),
						image_start_resolution:$("#tabs"+i+" .rangslider_wrap input[class='start_ratio']").val(),
						image_end_resolution:$("#tabs"+i+" .rangslider_wrap input[class='end_ratio']").val(),
						srid:encodeURI($("#tabs"+i+" .zbx input[class='bdrdius']").val()),
						tip:encodeURI($("#tabs"+i+" .bjtext input[class='biaotxt']").val()),
						networkpath:encodeURI($(this).val())
				};
			}
			$.ajax({
				type:"GET",
				url:url,
				dataType : "json",
				data : data,
				success: function(data){
					if(data.code=="1"){
						
					}else{
						
					}
				}
			});
		 }
	 });
}
/***********************云盒位置选择******************/
$(document).on('click','#tabsTwo p',function(){
	$('#bg').show();
	$('#show1').show();		
	$(".result").attr("num",$(this).attr("num"));
	initGtdataPaths();
	//click()//事件 
});

/*********入库按钮调用方法*******/
function ruku(){
	if($(".yshiDate").text()=="正射数据"){
		if(feikong()&&timeverify()){
			if(tabsflag==1){
				uploadfile();
			}else if(tabsflag==2){
				uploadGtdataPath();
			}else if(tabsflag==3){
				uploadNetworkPath();
			}
			var height=$(document).height();//监听当前窗口文档的高度
			$("#bg").css("height",height);
			$("#bg").show();
			$("#show").css("top","50%");
			$("#show").show();
		}else{
			if($.trim($('.scopdiv input').val()).length<1){
				$('.scopdiv a').show();
			}
			if($.trim($('.biaotxt').val()).length<1){
				$('.bjtext a').show();
			} 
		}
	}else{
		if(tabsflag==1){
			uploadfile();
		}else if(tabsflag==2){
			uploadGtdataPath();
		}else if(tabsflag==3){
			uploadNetworkPath();
		}
		var height=$(document).height();//监听当前窗口文档的高度
		$("#bg").css("height",height);
		$("#bg").show();
		$("#show").css("top","25%");
		$("#show").show();
		
	}
}
/*********入库弹出框按钮**********/
 //继续入库
 function hidediv() {
	 $("#bg").hide();
	 $("#show").hide();
 }
 //查看记录
 function checkRecord(){
	$("#bg").show();
//	$("#show").show();
	$("#dataLoaderDiv").hide();
	$("#recordDiv").show();
	initRecord();
 }
/*重置*/
function chongz(){
	clickInitialize();
}



/*热门标签，,数量判断*/
$(document).on('click','.biaoji span',function(){
    var bjtxt=$('.biaotxt').val();
	var re=/[，,]/g;
	if($.trim($('.biaotxt').val()).length<1){
		$(".biaotxt").val($(".biaotxt").val()+$(this).text()+",")
	}else if(re.test(bjtxt)){ 
		var n=$('.biaotxt').val().match(re).length;
		if(n<5){
			$(".biaotxt").val($(".biaotxt").val()+$(this).text()+",");
			}else{
				alert("不能超过5个");
				} 
	}else{
		 alert("格式错误")
		}
})
/*超过5个标签禁止输入*/
function biaoUp(obj){
	var value;
	var re=/[，,]/g;
	var n=$('.biaotxt').val().match(re).length;
	if(n==5){
		value=$(obj).val();	
	}
	if(n>5){
		//alert(value)
		$(obj).attr("value",value);
	}
}
/*非空提示*/
function feikong(){
	if($('.scopdiv input').val().length<1){
		return false;
	}
	if($('.biaotxt').val().length<1){
		return false;
	}
	return true;
}
	//范围区域
$(document).on('blur','.scopetxt',function(){
		if($.trim($('.scopdiv input').val()).length<1){
			$('.scopdiv a').show();
			var cla=$(this).parent().parent().attr("num")		  
			$("."+cla+" i").css("visibility","visible");
		}else{
			$('.scopdiv a').hide();
			var cla=$(this).parent().parent().attr("num")		  
			$("."+cla+" i").css("visibility","hidden");
		}
})
$(document).on('focus','.scopetxt',function(){
		$('.scopdiv a').hide();
		var cla=$(this).parent().parent().attr("num")		  
		$("."+cla+" i").css("visibility","hidden");
});
	//标签
$(document).on('blur','.biaotxt',function(){
		if($.trim($('.biaotxt').val()).length<1){
			$('.bjtext a').show();
			var cla=$(this).parent().parent().attr("num")		  
			$("."+cla+" i").css("visibility","visible");
		}else{
			$('.bjtext a').hide();
			var cla=$(this).parent().parent().attr("num")		  
			$("."+cla+" i").css("visibility","hidden");
			$('.biaoji span').click(function(){
				$('.bjtext a').hide();
				var cla=$(this).parent().parent().attr("num")		  
				$("."+cla+" i").css("visibility","hidden");
			})
		}
});
$(document).on('focus','.biaotxt',function(){
		$('.bjtext a').hide();
		var cla=$(this).parent().parent().attr("num")		  
		$("."+cla+" i").css("visibility","hidden");
});
	
/*第三步 包信息 验证*/
function verify(){
	
}
/*时间顺序验证*/
function timeverify(){
	var starDate = new Date($("#starttime").val());
	var endDate = new Date($("#endtime").val());
	if(starDate.getTime()>endDate.getTime()){
		alert("开始时间不能大于结束时间");
		return false;
	}else{
		return true;
	}
}
/*tabs bao 数据选择 点击初始化*/
function clickInitialize(){
	//tabs 位置
	$("#tabsOne p").each(function(){
		addUploadControl("picker0",0);
		//$("#tabsOne p[id='picker0']").text('点击上传本地的数据包(zip,tar.gz)');
		$("#tabsOne p:not([id='picker0'])").next().remove();
		$("#tabsOne p:not([id='picker0'])").remove();
		$(".add-p1").show();
	});
	$("#tabsTwo p").each(function(){
		$("#tabsTwo p:not([num='0'])").next().remove();
		$("#tabsTwo p:not([num='0'])").remove();
		$(".add-p2").show();		
	});
	$("#tabsThree input[type='text']").each(function(){
		$("#tabsThree input[type='text']:not([num='0'])").next().remove();
		$("#tabsThree input[type='text']:not([num='0'])").next().remove();
		$("#tabsThree input[type='text']:not([num='0'])").remove();
		$(".add-p3").show();		
	});
	//录入元数据信息 包
	$(".tabsli1").hide();
	$(".tabsli2").hide();
	$(".tabsli3").hide();
	$(".tabsli4").hide();
	//录入元数据信息
	//$("#tabsbox input[type='text']").val('');
	
}
/*入库失败 提示*/
function errorHint(num){
	if(num!=0){
		$("#errorHint").show();
		$("#errorHint span").text(num);
		$("#errorHint ul li").click(function(){
			window.location.href="dataLoader.html";
		});
	}
}

/*日期初始化*/
function initData(){
    $(".collect_start_time").val(getBeforeDate(90));
    $(".collect_end_time").val(getBeforeDate(0));
}
function getBeforeDate(n){
    var n = n;
    var d = new Date();
    d.setDate(d.getDate()-n);
    var year = d.getFullYear();
    var  mon=d.getMonth()+1;
    if(mon<10){
    	mon="0"+mon;
    }
    var day=d.getDate();
    if(day<10){
    	day="0"+day;
    }
    s = year+"-"+mon+"-"+day;
    return s;
}
function onbeforeunload_handler() {
	var uploadingnum = 0;
	var readytouploadingnum=0;
	for (var i=0; i<uploaders.length;i++){
		if(uploaders[i]!=undefined&&uploaders[i]!=null){
			var files=uploaders[i].getFiles("progress");
			if(files!=null&&files!=undefined){
				for(var j=0;j<files.length;j++){
					var key=$.inArray(files[j].id, filearray2);
					if(i==key){
						uploadingnum++;
					}
				}	
			}
		}
		if(filearray[i]!=null){
			readytouploadingnum++;
		}
	}
	
	var message="您有";
	if(uploadingnum!=0){
		message=message+uploadingnum+"条数据正在上传，";
	}
	if(readytouploadingnum!=0){
		message=message+readytouploadingnum+"条数据准备上传，";
	}
	message=message+"离开页面将终止文件上传入库，您确认取消交易吗？";
	if(uploadingnum!=0||readytouploadingnum!=0){
		return message;
	}
} 
/*日期控制*/
//function getSelectTime(n){
//	var n = n;
//	var sTime=$(".collect_start_time");
//	var eTime=$(".collect_end_time");
//	var starDate = new Date(sTime.val());
//	var endDate = new Date(eTime.val());
//    var Syear = starDate.getFullYear();
//    var Eyear = endDate.getFullYear();
//    var Smon = starDate.getMonth()+1;
//    var Emon = endDate.getMonth()+1;
//    var Sday =starDate.getDay()+1;
//    var Eday =endDate.getDay()+1;
//    if(n==1){
//    	sTime.val(Syear);
//    	eTime.val(Eyear);
//    }else if(n==2){
//    	sTime.val(Syear+"-"+Smon);
//    	eTime.val(Eyear+"-"+Emon);
//    }else if(n==3){
//    	sTime.val(Syear+"-"+Smon+"-"+Sday);
//    	eTime.val(Eyear+"-"+Emon+"-"+Eday);
//    }
//}


