$(function(){
	errorItemsCount();
 });
/*入库失败 提示*/
function errorHint(num){
	if(num!=0){
		$("#errorHint").show();
		$("#errorHint span").text(num);
		$("#errorHint").click(function(){
			window.location.href="record.html"; 
		});
	}else{
		$("#errorHint").hide();
	}
}
function errorItemsCount(){
	var r = Math.random();
	var url = "queryinputrecorderroritems?r="+r;
	$.ajax({
		type:"GET",
		url:url,
		dataType:"json",
		success:function(data){
			if(data.code=="1"){
				//初始化错误条目 
				errorHint(data.erroritemscount);
			}else{
				errorHint(data.erroritemscount);
			}
		}
	});
}