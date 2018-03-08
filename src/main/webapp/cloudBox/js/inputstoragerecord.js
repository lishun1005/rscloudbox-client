/** *初始化入库记录查询** */
$(function() {
	$("#btitle a").click(function(){
		if($(this).attr("class")=="ruku"){
			initRecord();
		}else if($(this).attr("class")=="wanc"){
			finishedRecord();
		}
	});
});

var taskid1;
var taskid2;
function initRecordRead(){
	clearInterval(taskid1);
	clearInterval(taskid2);
	taskid1 = setInterval(function(){initRecord()},5000);
}
function finishedRecordRead(){
	clearInterval(taskid1);
	clearInterval(taskid2);
	taskid2 = setInterval(function(){finishedRecord()},5000);
}

function initRecord() {
	var r = Math.random();
	var url = "queryinputrecord.htm?r=" + r;
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				var datalist = data.recordlist;
				var tbody = $("#ruDiv tbody");
				tbody.empty();
				var tr = $("<tr>");
				var td = $("<td>");
				$.each(datalist, function() {
					var childrentr = tr.clone();
					childrentr.attr("id",$(this).attr("id"));
					var childrentd = td.clone();
					childrentd.text(xulie());
					childrentd.appendTo(childrentr);
					var childrentd1 = td.clone();
					childrentd1.text($.myTime.DateFormat("yyyy-MM-dd hh:mm:ss", new Date(this.starttime)));
					childrentd1.appendTo(childrentr);
					var childrentd2 = td.clone();
					childrentd2.text($(this).attr("dataname"));
					childrentd2.appendTo(childrentr);

					var childrentd3 = td.clone();
					childrentd3.text((this.downloadsize/(1024*1024)).toFixed(2)+"Mb");
					childrentd3.appendTo(childrentr);
					var childrentd4 = td.clone();
					if(this.opstatus==1){
						childrentd4.text($(this).attr("message"));
					} else if(this.opstatus==2){
						childrentd4.text($(this).attr("message"));
					} else if(this.opstatus==3){
						$.ajax({
							type : "GET",
							async: false,
							url : "FiledownloadBreakpointResumeThreadListener.htm?r="+Math.random()+"&&recordid="+this.recordid,
							dataType : "json",
							success : function(data) {
								if(data.code==1){
									childrentd4.text("数据下载了"+data.PPercentDone+"%");
								}else{
									childrentd4.text($(this).attr("message"));
								}
							}
						});
					} else if(this.opstatus==4){
						$.ajax({
							type : "GET",
							async: false,
							url : "TarGzCompressProgressMonitorListener.htm?r="+Math.random()+"&&recordid="+this.recordid,
							dataType : "json",
							success : function(data) {
								if(data.code==1){
									childrentd4.text("数据解压了"+data.PPercentDone+"%");
								}else{
									childrentd4.text($(this).attr("message"));
								}
							}
						});
					}else if(this.opstatus==5||this.opstatus==6){
						childrentd4.text($(this).attr("message"));
					}else if(this.opstatus==8){
						childrentd4.text("出现问题");
					}
					childrentd4.appendTo(childrentr);
					var childrentd5 = $("<td><a class='cxrk'  onclick='reLoad("+$(this).attr("id")+")'>重新入库</a><a class='delete' onclick='onCkdelete("+$(this).attr("id")+")'>删除</a></td>");
					childrentd5.appendTo(childrentr);
					var childrentd6 = td.clone();
				
					if(this.opstatus==8){
						childrentd6.text($(this).attr("message"));
					}
					childrentd6.appendTo(childrentr);

					childrentr.appendTo(tbody);
				});
			} else {
				alert(data.message);
			}
		}
	});
	initRecordRead();
}


function xulie(){
	return $('#ruDiv table tr').length;
}

function onCkdelete(id){
	var r = Math.random();
	if (confirm("确认要“删除”么？")) {
		var url = "deleteinputrecord.htm?r="+r;
		$.ajax({
			type : "GET",
			url : url,
			data:{
				id:id
			},
			dataType : "json",
			success : function(data) {
				if(data.code=="1"){  
					$("#"+id).remove();
				}
			}
		}); 
    }
}

function reLoad(id){
	var r = Math.random();
	if(confirm("确定要“重新入库”吗？\r\n(警告：“重新入库”会删除当前记录！)")){
		var url = "deleteinputrecord.htm?r="+r;
		$.ajax({
			type:"GET",
			url:url,
			data:{
				id:id
			},
			dataType:"json",
			success:function(data){
				if(data.code=="1"){
					$("#"+id).remove();
					window.location.href="dataLoader.html";
				}
			}
		});
	}
}


function finishedRecord() {
	var r = Math.random();
	var url = "queryinputrecordfinished.htm?r=" + r;
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				var datalist = data.finishedlist;
				var tbody = $("#wcDiv tbody");
				tbody.empty();
				var tr = $("<tr>");
				var td = $("<td>");
				$.each(datalist, function() {
					var childrentr = tr.clone();
					var childrentd = td.clone();
					childrentd.text($('#wcDiv table tr').length);
					childrentd.appendTo(childrentr);
					var childrentd1 = td.clone();
					childrentd1.text($.myTime.DateFormat("yyyy-MM-dd hh:mm:ss", new Date(this.starttime)));
					childrentd1.appendTo(childrentr);

					var childrentd2 = td.clone();
					childrentd2.text($(this).attr("dataname"));
					childrentd2.appendTo(childrentr);

					var childrentd3 = td.clone();
					childrentd3.text((this.downloadsize/(1024*1024)).toFixed(2)+"Mb");
					childrentd3.appendTo(childrentr);
					
					var childrentd4 = td.clone();
					childrentd4.text($.myTime.DateFormat("yyyy-MM-dd hh:mm:ss", new Date(this.endtime)));
					childrentd4.appendTo(childrentr);
					
					var	location=$("<a title=\"地图定位\" class=\"cxrk1\" type=\"button\" href=\""+getRootPath()+"/?recordid="+this.recordid+"\">&nbsp</a>");
					var	download=$("<a title=\"下载\" class=\"download\" type=\"button\" href=\"/"+getRootPath()+"checkProcessPageByRecordId?recordid="+this.recordid+"\">&nbsp</a>");
					var childrentd5 = td.clone();
					location.appendTo(childrentd5);
					download.appendTo(childrentd5);
					childrentd5.appendTo(childrentr);
					
					childrentr.appendTo("#wcDiv tbody");
				});
			} else {
				var childrentr = tr.clone();
				var childrentd = td.clone();
				childrentd.text(data.message);
				childrentd.appendTo(childrentr);
				childrentr.appendTo(tbody);
			}
		}
	});
	finishedRecordRead();
}



 




























