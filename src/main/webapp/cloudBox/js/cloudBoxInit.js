$(function() {
	var recordid=getQueryString("recordid");
	var name=getQueryString("name");
	if(recordid!=null){
		queryproductInformation(recordid,name);
	}
	$('.diqu span').click(function(e) {
		$(this).parent().siblings().find("ul").removeClass("do").addClass("dn");
		var curreattr = $(this).parent().attr("id");
		var curreattrdiv=$("." + curreattr);
		if ($("." + curreattr).hasClass("dn")) {
			$("." + curreattr).removeClass("dn").addClass("do");
			$("#" + curreattr).removeClass("noselectimg").addClass("selectimg");
			 e.stopPropagation();
		} else {
			$("." + curreattr).removeClass("do").addClass("dn");
			$("#" + curreattr).removeClass("selectimg").addClass("noselectimg");
		}
		
	});
	$(document).click(function() {
		$("#diqu1_box").removeClass("do").addClass("dn");
		$("#diqu2_box").removeClass("do").addClass("dn");
		$("#diqu3_box").removeClass("do").addClass("dn");
	});
	Areaquery("AreaqueryforProvince", null);
	initData();
});
function initData(){
    $("#starttime").val(getBeforeDate(90));
    $("#endtime").val(getBeforeDate(0));
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
function Areaquery(urlN, whatK,whatKid) {
	var r = Math.random();
	var url = urlN + "?r=" + r;
	var params = {};
	whatK=encodeURI(whatK);
	var selectid = "diqu1_box";
	if (urlN == "AreaqueryforCity") {
		params = {
			province : whatK,
			provinceid:whatKid
		};
		selectid = "diqu2_box"
	} else if (urlN == "AreaqueryforCounty") {
		params = {
			city : whatK,
			cityid : whatKid
		};
		selectid = "diqu3_box"
	}
	var selectdiv = $("#" + selectid );
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				selectdiv.empty();
				$(data.list).each(function(e) {
					selectdiv.append(
							"<li id='" + this.admincode + "'>"
									+ this.name + "</li>");
				});
				if (urlN == "AreaqueryforProvince") {
					 $('#province span').html("选择省");
					 //$('#province ul >:first-child').addClass("selected");
					 //Areaquery("AreaqueryforCity", data.list[0].name);
					 $('#city span').html("选择市");
					 $('#county span').html("选择县/区");
					activediqu_boxli("diqu1_box","province","AreaqueryforCity");
					
				} else if (urlN == "AreaqueryforCity") {
					 $('#city span').html(data.list[0].name);
					 $('#city span').html("选择市");
					 $('#county span').html("选择县/区");
					 // $('#city ul >:first-child').addClass("selected");
					 //Areaquery("AreaqueryforCounty", data.list[0].name);
					activediqu_boxli("diqu2_box","city","AreaqueryforCounty");
				}else{
					 $('#county span').html(data.list[0].name);
					 $('#county span').html("选择县/区");
					 // $('#county ul >:first-child').addClass("selected");
					activediqu_boxli("diqu3_box","county",null);
				}
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html";
		}
	});
}
function AreaGeomqueryByareaId(areaid) {
	var r = Math.random();
	var url = "AreaqueryByareaId?r=" + r;
	var params = {
		areaId : areaid
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				ToPastGeomF(data.list[0].geom);
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html";
		}
	});
}

function  activediqu_boxli(parentdiv,diqutypedivid,urlN){
	$("#"+parentdiv+" li ").on("click",function(){
				var tidate = $(this).html();
				var tidateid = $(this).attr("id");
				$(this).parent().removeClass("do").addClass("dn");
			    $('#'+diqutypedivid+' span').html(tidate);
			    $("#"+diqutypedivid+" ul").children().each(function(i,n){
			    	if($(this).hasClass("selected")){
			    		$(this).removeClass("selected")
			    	}
			      }); 
			    $(this).addClass("selected");
			    if (urlN!=null) {
			    	Areaquery(urlN,tidate,tidateid);
			    }
			    AreaGeomqueryByareaId(this.id);
	});
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function resultsListTimeSequence(flag){
	var  originalimageresultList=$("#originalimageresultList table tbody tr");
	if(flag){
		originalimageresultList.sort(function(a,b){
			var atimetd=$(a).find("td.caijishijian").html();
			var btimetd=$(b).find("td.caijishijian").html();
			var aDate = new Date(atimetd);
			var bDate = new Date(btimetd);
	        return aDate>bDate?1:-1});
	}else{
		originalimageresultList.sort(function(a,b){
			var atimetd=$(a).find("td.caijishijian").html();
			var btimetd=$(b).find("td.caijishijian").html();
			var aDate = new Date(atimetd);
			var bDate = new Date(btimetd);
	        return aDate<bDate?1:-1});
	}
	return originalimageresultList;
	}
//经纬度输入限制
function jing(){
	var jing =this;
	var zz2=/^(\-|\+)?\d+(\.\d+)?$/;
	return function(){
		if(zz2.test(jing.value))
		{
			if(jing.value>180){
	 	 		jing.value = 180;
		 	}else if(jing.value<-180){
		 		jing.value = -180;
		 	}
		}	
	}
}
function wei(){
	var wei =this;
	var zz2=/^(\-|\+)?\d+(\.\d+)?$/;
	return function(){
		if(zz2.test($.trim(wei.value))){
			if(wei.value>90){
	 	 		wei.value = 90;
			}else if(wei.value<-90){
		 		wei.value = -90;
			}
		}
	}
}
/* 分辨率  云量  输出框 输出限制  只允许输出小数点后两位 */
function sss(obj)
{
if(isNaN(obj.value))
{
obj.value=obj.value.substring(0,obj.value.length-1);
}
}
function standard(obj)
{
 var val=obj.value;
 var kc=window.event.keyCode;
 
 if(val.length==0||val=="0.")
 {
  if(kc==110 || kc==190)          //首位或者0.后不能输入.
  {
   window.event.returnValue = false;
   return;
  }
 }
 
 if(val.length==1&&val=="0")    //第一位为0第二位必须是.
 {
  if(kc==8)
  {
   window.event.returnValue = true;
   return;
  }
  if(kc!=110 && kc!=190)
  {
   window.event.returnValue = false;
   return;
  }
 }
 var index=val.indexOf(".");
 if(val.length>=2&&index<0)
 {
 if(kc==8||kc==110||kc==190)
 {
  window.event.returnValue = true;
  return ;
 }
 else{
   window.event.returnValue = false;
  return ;
 }
 }
 if(index>=0)
 {
   var len=val.substring(index+1,val.length).length;
   if(len>=2)
   {
    if(kc==8)
    {
     window.event.returnValue = true;
     return ;
    }
  else
    {
     window.event.returnValue = false;
     return ;
    }
  }
 }
 //允许输入的数字键0~9和小数点（110,190）和回退键
if( (kc>=48 && kc<=57) || (kc>=96 && kc<=105) || kc==110 || kc==190||kc==8)//如果是数字 或 .
 {
  window.event.returnValue = true;
  return;
 }
else{
  window.event.returnValue = false;
  return;
 }
}

