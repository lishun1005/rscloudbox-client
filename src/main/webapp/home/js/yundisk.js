$(function() {
	TargzputinstorageInitdiv();
	inityunfileusedprogress();
});
function TargzputinstorageInitdiv() {
	openfiles("/storage");
}
function openfiles(path) {
	var r = Math.random();
	var url = "getAllFileByPath.htm?r=" + r;
	var params = {
		path : path
	};
	var parentdiv = $(".clouddisklist tbody ");
	var parentPatha = $(".disk_rightcontent p[class='tab_title']");
	var parentRtopa = $(".disk_rightcontent div[class='ovcl yunfile-top mt10 rel'] a");
	var parentRtopdivstorary = $(".disk_rightcontent div[class='ovcl yunfile-top mt10 rel']  div[name='storary_tip']");
	$
			.ajax({
				type : "GET",
				url : url,
				data : params,
				dataType : "json",
				success : function(data) {
					if (data.result == "1") {
						parentdiv.empty();
						parentPatha.empty();
						parentRtopdivstorary.show();
						parentRtopa.hide();
						$(parentPatha).append("<span>当前路径：</span>");
						if (path.indexOf("storage") < 0) {
							parentRtopa.show();
							parentRtopdivstorary.hide();
						}
						var parentPathaArray = path.split("/");
						var parentPathastr = "";
						for ( var i = 0; i < parentPathaArray.length; i++) {
							parentPathastr += parentPathaArray[i] + "/"
							var a = $("<a onclick=\"openfiles('"
									+ parentPathastr + "')\">");
							a.clone().text(parentPathaArray[i]).appendTo(
									parentPatha);
							$(parentPatha).append("/");
						}
						$(parentPatha).attr("filepath", parentPathastr);
						$(data.list)
								.each(
										function(e) {
											var tr = $("<tr>");
											tr.attr("filepath", this.path);
											tr.attr("filename", this.filename);
											tr.attr("size", this.size);
											var td = $("<td>");
											var input = $("<input type=\"checkbox\" style=\"float:left;margin:20px;\">");
											if (this.size == -1)
												var span = $("<span class=\"folder_icon\">");
											else if (istargzfile(this.filename))
												var span = $("<span class=\"zip_icon\">");
											else
												var span = $("<span class=\"file_icon\">");
											input.appendTo(td);
											span.appendTo(td);
											var div_fileIcon_div = $("<div class=\"fileIcon_div\">");
											var p = $("<p class=\"file_title\">");
											p.text(this.filename);
											p.appendTo(div_fileIcon_div);
											var div_file_menue = $("<div class=\"file_menue\">");
											var ul = $("<ul class=\"file_menue_ul\">");
											var lidelet = $("<li onclick=\"deleteDirectoryOrFile('"
													+ this.path
													+ "','"
													+ this.filename + "')\">");
											if (path.indexOf("storage") < 0) {
												var lirename = $("<li onclick=\"renameDirectoryOrFile('"
														+ this.path
														+ "','"
														+ this.filename
														+ "')\">");
												lirename.text("重命名").appendTo(
														ul);
												var limovafile = $("<li onclick=\"moveDirectoryOrFile('"
														+ this.path
														+ "','"
														+ this.filename
														+ "')\">");
												limovafile.text("移动").appendTo(
														ul);
											}
											var lidelet = $("<li onclick=\"deleteDirectoryOrFile('"
													+ this.path
													+ "','"
													+ this.filename + "')\">");
											lidelet.text("删除").appendTo(ul);
											ul.appendTo(div_file_menue);
											div_file_menue
													.appendTo(div_fileIcon_div);
											div_fileIcon_div.appendTo(td);
											td.appendTo(tr);
											var td2 = $("<td>");
											td2.clone().text(this.size)
													.appendTo(tr);
											td2.clone().text(this.time)
													.appendTo(tr);
											tr.appendTo(parentdiv);
										});
						activeclouddisklist(path);
					} else {
						alert(data.message);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					location=getRootPath()+"/login.html"
				}
			});
}
function activeclouddisklist(path) {
	if (path.indexOf("storage") >= 0) {
		var clouddisklisttr = $(".clouddisklist tbody tr[size='-1']");
	} else {
		var clouddisklisttr = $(".clouddisklist tbody tr");
	}
	// 鼠标移过显示菜单
	clouddisklisttr.hover(function() {
		$(this).children("td").children(".fileIcon_div")
				.children(".file_menue").show();
		$(this).siblings().children("td").children(".fileIcon_div").children(
				".file_menue").hide();
	}, function() {
		$(this).children("td").children(".fileIcon_div")
				.children(".file_menue").hide();
	});
	$(".clouddisklist tbody tr[size='-1']").on('dblclick', function() {
		openfiles($(this).attr("filepath"));
	});
}
function createNewDirectory() {
	var parentPatha = $(".disk_rightcontent p[class='tab_title']");
	var r = Math.random();
	var url = "createNewDirectory.htm?r=" + r;
	var name = $("#newFolderName").val();
	var params = {
		filename : parentPatha.attr("filepath") + name,
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.result == "1") {
				openfiles(parentPatha.attr("filepath"));
				$(".creatfile_dialog").hide();
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
function renameDirectoryOrFile(path, oldname) {
	alert("我是重命名函数！");
}
function moveDirectoryOrFile() {
	alert("我是移动函数！");
}
function deleteDirectoryOrFile(path, name) {
	var parentPatha = $(".disk_rightcontent p[class='tab_title']");
	var r = Math.random();
	var url = "deleteDirectoryOrFile.htm?r=" + r;
	var params = {
		filename : path,
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.result == "1") {
				openfiles(parentPatha.attr("filepath"));
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
function uploadFile() {
	var parentPatha = $(".disk_rightcontent p[class='tab_title']");
	var uplist = $("input[name^=uploads]");
	var filename = getFileName(uplist);
	if (filename == null || filename == "undefined" || filename == "") {
		alert("请选择文件");
		return;
	}
	var arrId = [];
	for ( var i = 0; i < uplist.length; i++) {
		if (uplist[i].value) {
			arrId[i] = uplist[i].id;
		}
	}
	var data = {
		filePath : parentPatha.attr("filepath"),
		filename : filename
	}
	showandhideuploadLoading(2);
	$.ajaxFileUpload({
		url : 'uploadFile.htm',
		secureuri : false,
		fileElementId : arrId,
		dataType : 'json',
		data : data,
		success : function(data) {
			showandhideuploadLoading(1);
			closeicon();
			if (data.result == "1") {
				alert(data.message);
				clearuploadfileprogressListenner(filename);
				openfiles(parentPatha.attr("filepath"));
			} else {
				alert(data.message);
			}
			$(".uploadfile_dialog").hide();
		},
		error : function(data) {
			showandhideuploadLoading(1);
			closeicon();
			alert("error");
		}
	});
	uploadfileprogressListenner(filename);
}
function showputinstoragediv() {
	$(".putinstorage_dialog").show();
	$(".putinstorage_dialog").siblings(".dialog").hide();
	initputinstoragesourcetype("productsourcetype");
}
function putinstorage() {
	var datatype = $("#dataType_putinstorages_datatype").val();
	var parentPatha = $(".disk_rightcontent p[class='tab_title']");
	var parentdiv = $(".clouddisklist tbody input:checked");
	var tr = parentdiv.parent().parent("tr");
	var r = Math.random();
	var filename = $(tr).attr("filename");
	var filepath = $(tr).attr("filepath");
	if (istargzfile(filename)) {
		var url = "Targzputinstorage.htm";
	} else if (tr.attr("size") == -1) {
		var url = "Directoryputinstorage.htm";
	} else {
		alert("入库类型为tar.gz或文件夹！");
		return false;
	}
	var params = {
		r : r,
		filePath : filepath,
		fileName : filename,
		dataType : datatype
	};
	showandhideputinstorageLoading(2);
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			showandhideputinstorageLoading(1);
			closeicon();
			if (data.code == "1") {
				alert(data.message);
				openfiles(parentPatha.attr("filepath"));
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			showandhideputinstorageLoading(1);
			closeicon();
			location=getRootPath()+"/login.html"
		}
	});
}
function getFileName(obj) {
	var fileName = "";
	if (typeof (fileName) != "undefined") {
		fileName = $(obj).val().split("\\").pop();
		// fileName = fileName.substring(0, fileName.lastIndexOf("."));
	}
	return fileName;
}
function istargzfile(filename) {
	filename = filename.split(".").pop();
	if (filename == "tar.gz" || filename == "tar" || filename == "zip"
			|| filename == "gz" || filename == "bz")
		return true;
	else
		return false;
}
function initputinstoragesourcetype(id) {
	var r = Math.random();
	var url = "productSourcetype.htm?r=" + r;
	var params = {};
	var select = $("#dataType_putinstorages_source")
	select.empty();
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				$(data.list).each(function(e) {
					var option = $("<option>");
					option.attr("id", this.id);
					option.text(this.typename);
					option.appendTo(select);
				});
				activeputinstoragesourcetype(data.list[0].id);
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
function activeputinstoragesourcetype(parentId) {
	$("#dataType_putinstorages_source").on("change", function() {
		initputinstorageTree($(this).find("option:selected").attr("id"));
	});
	initputinstorageTree(parentId)
}
var maplist;
function initputinstorageTree(id) {
	var r = Math.random();
	var url = "producttypelist.htm?r=" + r;
	var params = {
		Sourcetypeid : id
	};
	var select = $("#dataType_putinstorages_datatypec");
	select.empty();
	/* parentdiv.empty(); */
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				// var select = $("<select
				// id=\"dataType_putinstorages_datatypec\">");
				maplist = new Map();
				for ( var key in data.list[0]) {
					var option = $("<option>");
					option.text(key);
					option.appendTo(select);
					var values = data.list[0][key];
					maplist.put(key, values);
				}
				activeinitputinstorageTree("高程数据");
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {

			location=getRootPath()+"/login.html"
		}
	});
}
function activeinitputinstorageTree(key) {
	$("#dataType_putinstorages_datatypec").on("change", function() {
		initputinstorageTreelist($(this).val());
	});
	initputinstorageTreelist(key)
}
function initputinstorageTreelist(key) {
	var values = maplist.get(key);
	var selectsub = $("#dataType_putinstorages_datatype");
	selectsub.empty();
	$(values).each(function(e) {
		var optionsub = $("<option>");
		optionsub.text(this.typename);
		optionsub.appendTo(selectsub);
	});
}
function showandhideputinstorageLoading(type) {
	var dialog_content_select = $(".putinstorage_dialog #dataTypeselet");
	var dialog_content_loading = $(".putinstorage_dialog .loading");
	if (type == 1) {
		dialog_content_select.show();
		dialog_content_loading.hide();
	} else if (type == 2) {
		dialog_content_select.hide();
		dialog_content_loading.show();
	}
}
function showandhideuploadLoading(type) {
	var dialog_content_uploads = $(".uploadfile_dialog .uploads");
	var dialog_content_loading = $(".uploadfile_dialog .loading");
	if (type == 1) {
		dialog_content_uploads.prepend($("<input name=\"uploads\" id=\"imgFile\" type=\"file\" />"));
		dialog_content_uploads.show();
		dialog_content_loading.hide();
	} else if (type == 2) {
		dialog_content_uploads.hide();
		dialog_content_loading.show();
	}
}

/************初始化容量大小栏*************************华丽的分割线**********************************/
function  inityunfileusedprogress(){
	var r = Math.random();
	var url = "groupUserGetCommonUserInfo.htm?r=" + r;
	var params = {
		username : "DatamanagementSys",
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.result == "1") {
				$(".progress span").empty();
				var UsedSize=data.UsedSize/Math.pow(1024,3);
				var TotalSize=data.TotalSize/Math.pow(1024,3);
				$(".progress span").html("已使用"+UsedSize.toFixed(2)+"G/"+TotalSize.toFixed(2)+"G");
				$(".progress").progressbar({
					value : UsedSize/TotalSize*100
				});
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
var interval;
function uploadfileprogressListenner(name){
	var r = Math.random();
	var url = "upfile/progress.htm?r=" + r;
	var dialog_content_loading_span = $(".uploadfile_dialog .loading .bottom_text");
	var dialog_content_loading_span_span = $(".uploadfile_dialog .loading .bottom_text span");
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(data) {
			clearTimeout(interval);
			interval = setInterval(function(){uploadfileprogressListenner(name)}, "5000");   
			$(dialog_content_loading_span[0]).show();
			$(dialog_content_loading_span[1]).hide();
			if (data.code == "1") {
				for(var key in data.ProgressListenerMap) {  
					for(var i in data.ProgressListenerMap[key]){  
						if(data.ProgressListenerMap[key][i].pItemsName==name){
							if(data.ProgressListenerMap[key][i].pBytesRead!=data.ProgressListenerMap[key][i].pContentLength)
							{
								$(dialog_content_loading_span_span[0]).text(data.ProgressListenerMap[key][i].pBytesRead);
								$(dialog_content_loading_span_span[1]).text(data.ProgressListenerMap[key][i].pContentLength);
							}else{
								clearTimeout(interval);
								$(dialog_content_loading_span[0]).hide();
								$(dialog_content_loading_span[1]).show();
							}
						}
					}}
			}else{
				uploadfileprogressListenner(name);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}	
function clearuploadfileprogressListenner(name){
	var r = Math.random();
	var url = "clearupfile/progress.htm?r=" + r;
	var params = {
		filename : name,
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
	});
}	

