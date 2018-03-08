// JavaScript Document
//入库和完成显示和隐藏
function wcClick(a)
{
	var ru=document.getElementById("ruDiv");
	var wc=document.getElementById("wcDiv");
	if(wc.style.display=="none")
	{
		wc.style.display="block";
		ru.style.display="none";
		//a.className +="ahover";
		return true;
		
	}else
	{	
	 wc.style.display="block";
		ru.style.display="none";
		return false;
	}
	
}

function ruClick()
{
	var ru=document.getElementById("ruDiv");
	var wc=document.getElementById("wcDiv");
	if(ru.style.display=="none")
	{
		ru.style.display="block";
		wc.style.display="none";
		//a.className +="ahover";
		return true;
	}else{
		ru.style.display="block";
		wc.style.display="none";
		return false;
		//a.style.backgroundColor = "#fff";
		}
}
//鼠标悬浮 改变标签背景
function ruOver(a)
{

	a.style.backgroundColor = "#fff";
}
function ruOut(a)
{
		a.style.backgroundColor ="#f8f8f8"
}
function wcOver(a)
{
	a.style.backgroundColor = "#fff";
}
function wcOut(a)
{
	a.style.backgroundColor ="#f8f8f8"
}
	
//鼠标悬浮 改变行背景高
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
//delete
function onCkdelete()
{
	if(confirm("确定要删除该入库记录及相关数据包？<br />删除后不可恢复"))
	{
	  alert("是");	
	}else
	{
	  alert("否");
	  return false;
	}
}
//对话框
 function showdiv() { 
 document.getElementById("bg").style.display ="block";
 document.getElementById("show").style.display ="block";
 }
 function hidediv() {
 document.getElementById("bg").style.display ='none';
 document.getElementById("show").style.display ='none';
 }