$(function() {
	InitializePushRecord();
});

function InitializePushRecord() {
	var push_record_content_tbody=$(".push_record_content").find("tbody");
	$(push_record_content_tbody).empty();
	var r = Math.random();
	var url = "queryDownloadRecord?r="+r;
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(data) {
			if (data.code == 1) {
				var i=0;
				$(data.list).each(function(e) {
					var tr=$("<tr class=\"orderedlist\">");
					var td=$("<td>");
					var td1=td.clone();
					td1.text(++i);
					td1.appendTo(tr);
					var td2=td.clone();
					td2.text($.myTime.DateFormat("yyyy-MM-dd hh:mm:ss", new Date(this.recordtime)));
					td2.appendTo(tr);
					var td3=td.clone();
					td3.text(this.userordertype);
					td3.appendTo(tr);
					var td4=td.clone();
					td4.text((this.downloadsize/(1024*1024)).toFixed(2)+"Mb");
					td4.appendTo(tr);
					var td5=td.clone();
					var details=$("<a  title=\"详情\" class=\"xinxi_light\" type=\"button\" onclick=\"checkpushtask('"+this.recordid+"','"+this.downloadsize+"','"+this.userordertype+"','"+this.name+"')\">&nbsp</a>");
					var location=$("<a title=\"地图定位\" class=\"dinwei\" type=\"button\">&nbsp</a>");
					var download=$("<a title=\"审批\" class=\"download\" type=\"button\">&nbsp</a>");
					if(this.downloadstatus==null){
						td5.addClass("Op");
						td5.text("等待中...");
						//details=$("<a class=\"xinxi\" type=\"button\">&nbsp</a>");
					} else if(this.downloadstatus==0){
						td5.addClass("Op");
						td5.text("推送中...");
					}else if(this.downloadstatus==1){
						td5.addClass("Op");
						td5.text("解压中...");
					}else if(this.downloadstatus==2){
						td5.addClass("Op");
						td5.text("入库中...");
					}else if(this.downloadstatus==3){
						td5.addClass("Op");
						td5.text("切片中...");
					}
					else if(this.downloadstatus==4){
						td5.addClass("Op");
						td5.text("结束");
						location=$("<a title=\"地图定位\" class=\"dinwei_light\" type=\"button\" href=\""+getRootPath()+"/?name="+this.name+"&recordid="+this.recordid+"\">&nbsp</a>");
						download=$("<a title=\"审批\" class=\"download_light\" type=\"button\"href=\""+getRootPath()+"/checkProcessPageByRecordId/"+this.recordid+"/"+this.name+"\">&nbsp</a>");
					}
					td5.appendTo(tr);
					var td6=td.clone();		
					details.appendTo(td6);
					location.appendTo(td6);
					download.appendTo(td6);
					td6.appendTo(tr);
					tr.appendTo(push_record_content_tbody);
				})
				
			} else {
				alert(data.Msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			window.location.reload();
		}
		});
}

var taskid;
function checkpushtaskthread(recordid,taskDownlodsize,userordertype,name){
	clearInterval(taskid);
	taskid=setInterval(function () { checkpushtask(recordid,taskDownlodsize,userordertype,name); },1000);
}
function checkpushtask(recordid,taskDownlodsize,userordertype,name){
	$(".push_record_content").hide();
	$(".push_task_content").show();
	var r = Math.random();
	var params={
		recordid:recordid,
		name:name
	}
	var push_task_content_table=$(".push_task_content  .head");
	var url = "ALLProgressMonitorListener?r="+r;
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			push_task_content_table.empty();
			var tr = $("<tr>");
			var td = $("<td>");
			var  a = $("<a>");
			var trfist=tr.clone();
			trfist.attr("class","head1");
			trfist.append(td.clone().text("数据类型：").append(a.clone().text(userordertype)));
			trfist.append(td.clone().text("数据包大小：").append(a.clone().text((taskDownlodsize/(1024*1024)).toFixed(2)+"Mb")));
			var trsecond=tr.clone();
			trsecond.attr("class","head2");
			if(data.code == 1) {
				if(data.DownloadAveragespeed==0||data.CompressAveragespeed==0||(userordertype=="正射影像"&&data.CuttingAveragespeed==0)){
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text("初次使用，无法估算性能！")));
				}else{
					var taketime;
					if(userordertype=="原始影像"){
						taketime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CompressAveragespeed;
					}else if(userordertype=="正射影像"){
						taketime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CompressAveragespeed+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
					}
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text(timestransverter(taketime))));
				}
				trfist.append(td.clone().append(a.clone().attr("class","wait")).text("等待中..."));	
				//trsecond.append(td.clone());
				trsecond.append(td.clone().text("（完成0%）"));
				step(-1,"推送任务正在排队等候","",userordertype);
			}else if(data.code == 2) {
				//下载速度或解压速度为0或正射影像的切片速度为0
				if(data.PAveragespeed==0||data.CompressAveragespeed==0||(userordertype=="正射影像"&&data.CuttingAveragespeed==0)){
					var massage="初次使用,无法估算服务器性能,导致无法估算完成时间,敬请谅解！";
					if(data.PAveragespeed==0){
						massage="服务器网络网速较差或下载中断，无法估计完成时间，警请谅解！"
					}
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text(massage)));
					trfist.append(td.clone().append(a.clone().attr("class","wait")).text("推送中..."));
					trsecond.append(td.clone().text("（无法预计！）"));
				}else{
					var lasttime,alltime;
					if(userordertype=="原始影像"){
						//加上原始影像的解压代码
						/*lasttime=data.PResttime+taskDownlodsize/data.CompressAveragespeed;
						alltime=taskDownlodsize/data.PAveragespeed+taskDownlodsize/data.CompressAveragespeed;*/
						lasttime=data.PResttime;
						alltime=taskDownlodsize/data.PAveragespeed;
					}else if(userordertype=="正射影像"){
						 lasttime=data.PResttime+taskDownlodsize/data.CompressAveragespeed+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
						 alltime=taskDownlodsize/data.PAveragespeed+taskDownlodsize/data.CompressAveragespeed+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
					}
					if(lasttime==null){
						return false;
					}
					console.log("lasttime="+lasttime+",,alltime="+alltime);
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text(timestransverter(lasttime))));
					trfist.append(td.clone().append(a.clone().attr("class","wait")).text("推送中..."));
					//trsecond.append(td.clone().text("（完成"+Math.floor((alltime-lasttime)/alltime*100)+"%）"));
					trsecond.append(td.clone().text("（完成"+data.PPercentDone+"%）"));
				}
				var myDate = new Date();
				var tipmessage=data.PBeginTime +"   "+"正在推送中，请耐心等待...";
				var contentmessage=myDate.toLocaleString( )+"   "+"已推送"+data.PPercentDone+"%的数据"
				step(0,tipmessage,contentmessage,userordertype);
			}else if (data.code == 3) {
				var lasttime,alltime;
				if(userordertype=="原始影像"){
					 lasttime=data.CResttime;
					 alltime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CAveragespeed;
				}else if(userordertype=="正射影像"){
					 lasttime=data.CResttime+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
					 alltime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CAveragespeed+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
				}
				if(lasttime==null){
					return false;
				}
				trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text(timestransverter(lasttime))));
				trfist.append(td.clone().append(a.clone().attr("class","wait")).text("解压中..."));
				trsecond.append(td.clone().text("（完成"+Math.floor((alltime-lasttime)/alltime*100)+"%）"));
				var tipmessage=data.CBeginTime +"   "+"正解压中，请耐心等待...";
				var myDate = new Date();
				var contentmessage=myDate.toLocaleString( ) +"   "+"已解压"+data.CPercentDone+"%的数据"
				step(1,tipmessage,contentmessage,userordertype);
				$(".r0_1 a").html(data.PEndTime+" 完成数据推送操作！");
			}else if (data.code == 4) {
				trfist.append(td.clone().append(a.clone().attr("class","wait")).text("入库中..."));
				trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text("0~8秒")));
				trsecond.append(td.clone().text("(入库中)"));
				step(2,"文件入库中,请耐性等候。。。","文件正在入库...",userordertype);
				if(userordertype=="原始数据"){
					$(".r0_1 a").html(data.PEndTime+" 完成数据推送操作");
					$(".r1_1 a").html(data.CEndTime+" 完成数据解压操作！");
				}else{
					$(".r0_1 a").html(data.PEndTime+" 完成数据推送操作");
					$(".r1_1 a").html(data.CEndTime+" 完成数据解压操作！");
					$(".r3_1 a").html(data.CutEndTime+" 完成数据切割、发布操作！");
				}
			}else if (data.code == 5) {
				if(userordertype=="正射影像"&&data.CuttingAveragespeed==0){
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text("初次使用,无法估算服务器性能,导致无法估算完成时间,敬请谅解！")));
					trfist.append(td.clone().append(a.clone().attr("class","wait")).text("切片中...."));
					trsecond.append(td.clone().text("（无法预计！）"));
				}else{
					var lasttime=data.CLasttime;
					if(lasttime==null){
						return false;
					}
					if(lasttime==0){
						lasttime=taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
						var alltime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CompressAveragespeed+taskDownlodsize*Math.pow(2,16)/data.CuttingAveragespeed;
					}else{
						var alltime=taskDownlodsize/data.DownloadAveragespeed+taskDownlodsize/data.CompressAveragespeed+data.CAlltime;
					}
					trfist.append(td.clone().append(a.clone().attr("class","wait")).text("切片中...."));
					trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text(timestransverter(lasttime))));
					trsecond.append(td.clone().text("（完成"+Math.floor((alltime-lasttime)/alltime*100)+"%）"));
				}
				var tipmessage=data.CBeginTime +"   "+"正切割中，请耐心等待...";
				var myDate = new Date();
				var contentmessage=myDate.toLocaleString( ) +"   "+"已完成切割"+data.CPercentDone+"%"
				step(3,tipmessage,contentmessage,userordertype);
				$(".r0_1 a").html(data.PEndTime+" 完成数据推送操作");
				$(".r1_1 a").html(data.CEndTime+" 完成数据解压操作！");
			}else if (data.code == 6){
				trfist.append(td.clone().append(a.clone().attr("class","wait")).text("任务结束！"));
				trsecond.append(td.clone().attr("colspan","2").text("预计完成耗时：").append(a.clone().text("完成！")));
				trsecond.append(td.clone().text("(完成)"));
				step(4,"任务结束","任务结束！",userordertype);
				$(".r0_1 a").html(data.PEndTime+" 完成数据推送操作");
				$(".r1_1 a").html(data.CEndTime+" 完成数据解压操作！");
				$(".r3_1 a").html(data.CutEndTime+" 完成数据切割、发布操作！");
				$(".r2_1 a").html(data.InputStorageEndTime+" 完成数据入库操作！");
				$(".r4_1 a").html(data.TaskEndTime+"已完成任务，您可以<a class='link' target='view_window' href='"+getRootPath()+"/checkProcessPageByRecordId/"+data.recordid+"/"+data.name+"'>申请下载</a>或者<a class='link' href='"+getRootPath()+"/?name="+data.name+"&recordid="+data.recordid+"'>查看</a>数据！");
				clearInterval(taskid);
			}else{
				alert(data.Msg);
				step(-1,"推送任务正在排队等候","",userordertype);
			}
			trfist.appendTo(push_task_content_table);
			trsecond.appendTo(push_task_content_table);		
		},
		error: function(e) { 
			//alert(e.responseText);
			location=getRootPath()+"/login.html"
		} 
	});
	checkpushtaskthread(recordid,taskDownlodsize,userordertype,name);
}
function timestransverter(nMS){
	var times="";
	var nD = Math.floor(nMS / (1000 * 60 * 60 * 24));
	var nH = Math.floor(nMS / (1000 * 60 * 60)) % 24;
	var nM = Math.floor(nMS / (1000 * 60)) % 60;
	var nS = Math.floor(nMS / 1000) % 60;
	//var nSS = Math.floor(nMS / 100) % 10;
	if (nD>=0) {
		if (0<nD&&nD<10) {
			times = times + "0" + nD + "天";
		}else if(nD>=10){
			times = times +  nD + "天";
		}
		if (0<nH&&nH<10) {
			times = times +  "0" + nH + "小时";
		}else if(nH >=10){
			times = times +   nH + "小时";
		}else if(nD!= 0){
			times = times +  "0" + nH + "小时";
		}
		
		if (0<=nM&&nM<10) {
			times = times +  "0" + nM + "分钟";
		}else if(nM>=10){
			times = times +   nM + "分钟";
		}
		
		if (0<=nS&&nS <10) {
			times = times +  "0" + nS + "秒";
		}else if(nS>=10){
			times = times +   nS + "秒";
		}

	}else{
		return "时间统计发生异常!";
	}
	return times;
}
