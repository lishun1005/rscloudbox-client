// JavaScript Document
$(function(){
	$("#btitle").tabs();
	
	
	/*入库中  完成表切换*/
	$(".ruku").click(function(){
		$(this).css("background-color","#fff");
		$(".wanc").css("background-color","#f8f8f8");
		$("#ruDiv").show();
		$("#wcDiv").hide();
	});
	$(".wanc").click(function(){
		$(this).css("background-color","#fff");
		$(".ruku").css("background-color","#f8f8f8");
		$("#ruDiv").hide();
		$("#wcDiv").show();
		
	});
	
});
//删除

/*入库失败 提示*/
//function errorHint(num){
//	if(num!=0){
//		$("#errorHint").show();
//		$("#errorHint span").text(num);
//		$("#errorHint ul li").click(function(){
//			window.location.href="record.html";
//		});
//	}
//}



/*鼠标悬浮 改变行背景高*/
function trhover()
{
	var rows =document.getElementsByClassName("tr")
	for(var i=0;i<rows.length;i++)
	{
		rows[i].onmouseover=function(){//鼠标移上去,添加一个类'hilite'
			this.className +='hilite';
			}
		rows[i].onmouseover=function(){//鼠标移开,改变该类的名称
			this.className +=this.className.replace('hilite','');
			}
	}
}


//对话框
 function showdiv(){ 
	 $("#bg").show();
	 $("#show").show();
 }
 function hidediv(){
	 $("#bg").hide();
	 $("#show").hide();
 }