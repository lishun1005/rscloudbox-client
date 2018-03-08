/*-----------------------------------------------------------------------------------------------------------------/

	功能：日期选择器
	调用示例：<input type="text" id="aa" value="2014-3-2" onfocus="$.openCalendar(this)" />  //文本框获得焦点时，调用日历插件
	作者:luoqh
/-----------------------------------------------------------------------------------------------------------------*/
;(function($){
    $.extend({
		"openCalendar": function(element){
			calendar();
			//把显示日历的控件全部封装在一个函数内
			function calendar(){	
				var currentY;
				var currentM;
				var today = new Date();
				var thisYear = today.getFullYear();
				var todayMonth = today.getMonth(); //获得当前月份
				var thisDay = today.getDate();
				var week = new Array("日","一","二","三","四","五","六"); //定义星期数组
				var monthDays = new Array(31,28,31,30,31,30,31,31,30,31,30,31); //设置月份天数，一三五七八十腊（十二）为大月31天
				//判断页面上是否存在ID为showCalendar的DIV，如果不存在，则添加
				var dataLength=  $("body").find("#showCalendar").length;
				if(dataLength!=0){
//					$("#showCalendar").show();
					$("#showCalendar").remove();
				}
				//重新创建
				$("body").append("<div id='showCalendar'></div>");
				//定义放置日历的相关元素
				$("#showCalendar").show().html(
					"<div class='calendar monthFirst'>"+
						"<div class='current-date'>"+
							"<a href='#' id='preY' title='上一年'> << </a>"+
							"<a href='#' id='pre' title='上一月'> < </a>"+
							"<a href='#' id='nextY' title='下一年'> >> </a>"+
							"<a href='#' id='next' title='下一月'> > </a>"+
							"<select class='currentYear'>"+
								"<option value='1990'>1990年</option>"+
								"<option value='1991'>1991年</option>"+
								"<option value='1992'>1992年</option>"+
								"<option value='1993'>1993年</option>"+
								"<option value='1994'>1994年</option>"+
								"<option value='1995'>1995年</option>"+
								"<option value='1996'>1996年</option>"+
								"<option value='1997'>1997年</option>"+
								"<option value='1998'>1998年</option>"+
								"<option value='1999'>1999年</option>"+	
								"<option value='2000'>2000年</option>"+	
								"<option value='2001'>2001年</option>"+
								"<option value='2002'>2002年</option>"+
								"<option value='2003'>2003年</option>"+
								"<option value='2004'>2004年</option>"+
								"<option value='2005'>2005年</option>"+
								"<option value='2006'>2006年</option>"+
								"<option value='2007'>2007年</option>"+
								"<option value='2008'>2008年</option>"+
								"<option value='2009'>2009年</option>"+
								"<option value='2010'>2010年</option>"+	
								"<option value='2011'>2011年</option>"+
								"<option value='2012'>2012年</option>"+
								"<option value='2013'>2013年</option>"+
								"<option value='2014'>2014年</option>"+
								"<option value='2015'>2015年</option>"+
								"<option value='2016'>2016年</option>"+
								"<option value='2017'>2017年</option>"+
								"<option value='2018'>2018年</option>"+
								"<option value='2019'>2019年</option>"+
								"<option value='2020'>2020年</option>"+
								"<option value='2021'>2021年</option>"+
								"<option value='2022'>2022年</option>"+
								"<option value='2023'>2023年</option>"+
								"<option value='2024'>2024年</option>"+
								"<option value='2025'>2025年</option>"+
							"</select>"+	
							"<select class='currentMonth'>"+
								"<option value='1'>1月</option>"+
								"<option value='2'>2月</option>"+
								"<option value='3'>3月</option>"+
								"<option value='4'>4月</option>"+
								"<option value='5'>5月</option>"+
								"<option value='6'>6月</option>"+
								"<option value='7'>7月</option>"+
								"<option value='8'>8月</option>"+
								"<option value='9'>9月</option>"+
								"<option value='10'>10月</option>"+
								"<option value='11'>11月</option>"+
								"<option value='12'>12月</option>"+
							"</select>"+
						"</div>"+						
						"<ul></ul>"+
						"<p class='databtnlist'><span class='today'>今天</span><span class='closecalendar'>关闭</span></p>"+
						//"<p class='databtnlist'><span class='cleartime'>清空</span><span class='today'>今天</span><span class='closecalendar'>关闭</span></p>"+
					"</div>"
					);
				
				function showCalendar(monthNum,year,dateNum){
					$("#showCalendar .calendar ul").html(""); // 清空日历记录
					//判断年份和月份 (这一步是关键，日历表中的年月日都需要由此步获得的年月来取得相应的值,即currentYear, currentMonth, nextYear, nextMonth)
					var currentYear = year;
					var currentMonth = monthNum; //monthNum%12;
					var nextYear = currentYear+1;
					var nextMonth = currentMonth +1;
					if(currentMonth==0){
						currentMonth=12;
						currentYear = currentYear - 1;
					}
					if(currentMonth>12){
						currentMonth=1;
						currentYear = currentYear + 1;
					}
					//判断是否为闰年，设置2月份的天数
					if ( (currentYear%4==0) && (currentYear%100!=0) || (currentYear%400==0) ) {
						monthDays[1] = 29;
					}else{
						monthDays[1] = 28;
					}
				
					//输出当前年月
					$(".currentYear").val(currentYear);
					$(".currentMonth").val(parseInt(currentMonth/1));
					$(".currentYear option[value='" + currentYear + "']").attr("selected","selected").siblings().removeAttr("selected");
					$(".currentMonth option[value='" + currentMonth + "']").attr("selected","selected").siblings().removeAttr("selected");

					//输出星期
					for(j=0; j<week.length; j++){
						$("#showCalendar .calendar ul").append("<li class='week'>" + week[j] + "</li>");
					}
					
					//判断每个月的第一天是星期几
					var currentMonth_firstDay = new Date(currentYear,(currentMonth-1),1); //设置当前月的日期对象  new Date(年，月，日)设置特定日期对象
					var currentMonth_firstDay_getDay = currentMonth_firstDay.getDay(); //获得当前月第一天是星期几
					var nextMonth_firstDay = new Date(nextYear,(nextMonth-1),1); //设置下个月的日期对象  new Date(年，月，日)设置特定日期对象
					var nextMonth_firstDay_getDay = nextMonth_firstDay.getDay(); //获得下个月第一天是星期几
					for(i=0; i<currentMonth_firstDay_getDay; i++){
						$("#showCalendar .monthFirst ul").append("<li>&nbsp;</li>");
					}

					//输出日历
					var nDays = monthDays[currentMonth-1]; //获得当前月的天数
					var nextMonthDays = monthDays[nextMonth-1]; //获得下个月的天数
					for(i=1; i<=nDays; i++){
						$("#showCalendar .monthFirst ul").append("<li><a href='javascript:;' title='" + currentYear + "-" + currentMonth + "-" + i + "'>" + i + "</a></li>");
					}
					if(currentMonth==parseInt(selectMonth) && year==parseInt(selectYear)){
						$("#showCalendar .monthFirst ul li a").removeClass("active");
						$("#showCalendar .monthFirst ul li a").eq(parseInt(selectDate)-1).addClass("active");
					}

					//点击具体天数时，把当前日期附值给日期文本框
					$("#showCalendar .calendar li").click(function(){
						if( $(this).find("a").html()!=null ){
							var dataValue = $(this).find("a").attr("title");
//							$(element).val(dataValue);	
//							$("#showCalendar").remove();
							var elementid=$(element).attr("id");
							if(elementid!=undefined&&elementid=="starttime"){
								var endtime=$("#endtime").val();
								if(endtime == ""){
									$(element).val(dataValue);
									$("#showCalendar").remove();
								}else{
									
									var flag=comptime(dataValue,endtime);
									if(flag){
										$(element).val(dataValue);
										$("#showCalendar").remove();
									}else{
										alert("开始时间不能大于结束时间！");
									};
								}
							}else if(elementid!=undefined&&elementid=="endtime"){
								var starttime=$("#starttime").val();
								var flag=comptime(starttime,dataValue);
								if(flag){
									$(element).val(dataValue);
									$("#showCalendar").remove();
								}else{
									alert("结束时间不能小于开始时间！");
								};
							}
						}
					});
				}
				
				var estr = $(element).val().split("-");
				var selectYear = estr[0];
				var selectMonth = estr[1]; //monthNum%12;
				var selectDate = estr[2];
				
				if(selectYear == ""){
					
					$(element).val(thisDate);
					var nowYear = thisYear,
					nowMonth = (todayMonth+1).toString(),
					nowsDay = thisDay;
					if(nowMonth<10)
						nowMonth="0"+nowMonth;
					showCalendar(nowMonth,nowYear,nowsDay);
					$("#showCalendar .monthFirst ul li a").removeClass("active");
					$("#showCalendar .monthFirst ul li a").eq(parseInt(nowsDay)-1).addClass("active");
				}else{
					
					showCalendar(selectMonth,selectYear,selectDate);
				}
				
				
				//var monthNum = todayMonth+1; //获得当前月
				//var year = thisYear + parseInt(monthNum/12); /*获得当前年*/
			////	showCalendar(monthNum,year-1); //日历初始化
				var minYear = parseInt($(".currentYear option:first").val());
				var maxYear = parseInt($(".currentYear option:last").val());
				//点击上一年、下一年时调用函数，并且改变年份
				$("#preY").click(function(){
					currentY = parseInt($(".currentYear option:selected").val());
					currentM = parseInt($(".currentMonth option:selected").val());
					if(currentM<10)
						currentM="0"+currentM;
					year = currentY-1;
					if(year<minYear) return;
					showCalendar(currentM,year);
				});
				$("#nextY").click(function(){
					currentY = parseInt($(".currentYear option:selected").val());
					currentM = parseInt($(".currentMonth option:selected").val());
					year = currentY+1;
					if(currentM<10)
						currentM="0"+currentM;
					if(year>maxYear) return;
					showCalendar(currentM,year);
				});
				
				//点击上一月下一月时调用函数，并且改变月份
				$("#pre").click(function(){
					currentY = parseInt($(".currentYear option:selected").val());
					currentM = parseInt($(".currentMonth option:selected").val());
					monthNum = currentM-1;
					if(monthNum<10)
						monthNum="0"+monthNum;
					if(monthNum==0 && currentY==minYear) return;
					showCalendar(monthNum,currentY);
				});
				$("#next").click(function(){
					currentY = parseInt($(".currentYear option:selected").val());
					currentM = parseInt($(".currentMonth option:selected").val());
					monthNum = currentM+1;
					if(monthNum<10)
						monthNum="0"+monthNum;
					if(monthNum>12 && currentY==maxYear) return;
					showCalendar(monthNum,currentY);
				});
				
				//选择年份或月份时调用函数，并且改变年月
				$(".currentYear").change(function(){
					currentY = parseInt($(this).find("option:selected").val());
					currentM = parseInt($(".currentMonth option:selected").val());
					if(currentM<10)
						currentM="0"+currentM;
					showCalendar(currentM,currentY);
				});
				$(".currentMonth").change(function(){
					currentY = parseInt($(".currentYear option:selected").val());
					currentM = parseInt($(this).find("option:selected").val());
					if(currentM<10)
					currentM="0"+currentM;
					showCalendar(currentM,currentY); 
				});
				
				//清空日期
				$(".cleartime").click(function(){
					$(element).val("");
				});
				
				//获取今天日期
				var thisDate = thisYear + "-" + (todayMonth+1) + "-" + thisDay;
				$(".databtnlist .today").click(function(){
					var elementid=$(element).attr("id");
					if(elementid!=undefined&&elementid=="starttime"){
						var endtime=$("#endtime").val();
						if(endtime == ""){
							$(element).val(thisDate);
							$("#showCalendar").remove();
						}else{
							var flag=comptime(thisDate,endtime);
							if(flag){
								$(element).val(thisDate);
								$("#showCalendar").remove();
							}else{
								alert("开始时间不能大于结束时间！");
							};
						}
					}else if(elementid!=undefined&&elementid=="endtime"){
						var starttime=$("#starttime").val();
						var flag=comptime(starttime,thisDate);
						if(flag){
							$(element).val(thisDate);
							$("#showCalendar").remove();
						}else{
							alert("结束时间不能小于开始时间！");
						};
					}
				});
				
				//关闭日期选择器
				$(".closecalendar").click(function(){
					$("#showCalendar").remove();
				});
				
				//点击文档隐藏日期
				$(document).click(function(){
					$("#showCalendar").remove();
				});
				$(element).click(function(e){e.stopPropagation();}); //点击日期输入框时不隐藏
				$("#showCalendar").click(function(e){e.stopPropagation();}); //点击日历本身时不隐藏	
				
				//设置样式
				//日历定位
				var windowW = $(window).width();
				var windowH = $(window).height();
				var elemHeight = $(element).outerHeight();
				var elemWidth = $(element).outerWidth();
				var elemLeft = $(element).offset().left;
				var elemTop = $(element).offset().top;				
				var calendarH = $("#showCalendar").height();
				var calendarW = $("#showCalendar").width();	
				var elemTop2, bottomH, rightW, elemLeft2;
				bottomH = windowH-elemTop;
				rightW = windowW-elemLeft;
				//设置top定位
				if(windowH>calendarH){						
					if(elemTop>calendarH){ //上边够
						elemTop2 = elemTop-calendarH-5;	
						$("#showCalendar").css({top:elemTop2});
					}else if(elemTop<=calendarH && bottomH>calendarH){ //上边不够， 下边够
						elemTop2 = elemTop+elemHeight+5;
						$("#showCalendar").css({top:elemTop2});
					}else if ( elemTop<=calendarH && bottomH<calendarH ) { //上下两边都不够
						//console.log("11");
						elemTop2 = "50%";
						$("#showCalendar").css({top:elemTop2, "margin-top":-calendarH/2 + "px"});
					}
				}else {
					$("#showCalendar").css({top:0});
				}
				
				//设置left定位
				if(windowW>calendarW){
					if(rightW>calendarW){ //右边够
						elemLeft2 = elemLeft
						$("#showCalendar").css({left:elemLeft2});
					}else if(rightW<=calendarW && elemLeft>calendarW){ //右边不够， 左边够
						elemLeft2 = elemLeft-calendarW;
						$("#showCalendar").css({left:elemLeft2});
					}else if(rightW<=calendarW && elemLeft<calendarW){ //左右两边都不够
						elemLeft2 = "50%";
						$("#showCalendar").css({left:elemLeft2, "margin-left":-calendarW/2 + "px"});
					}				
				}else{
					$("#showCalendar").css({left:0});
				}				
			}
		}
	});
})(jQuery);
function comptime(beginTime,endTime) {
    var beginTimes = beginTime.substring(0, 10).split('-');
    var endTimes = endTime.substring(0, 10).split('-');
    beginTime = beginTimes[0] + '/' + beginTimes[1] + '/' + beginTimes[2] + ' ' + beginTime.substring(10, 19);
    endTime = endTimes[0] + '/' + endTimes[1] + '/' + endTimes[2] + ' ' + endTime.substring(10, 19);
    var a = (Date.parse(endTime) - Date.parse(beginTime)) / 3600 / 1000;
    if (a < 0) {
      return false;
    } else if (a > 0) {
        return true
    } else if (a == 0) {
    	 return true
    }
}
