$(function(){
	var message=BrowserChecker();
	if(message!=null){
		alert(message);
	}
})

function flashChecker() { 
	  var hasFlash = 0;　　　　 // 是否安装了flash
	  var flashVersion = 0;　　 // flash版本
	  if(document.all) { 
		  var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
		  if(swf) { 
			  hasFlash = 1; 
			  VSwf = swf.GetVariable("$version"); 
			  flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]); 
		  } 
	  } else { 
		  if(navigator.plugins && navigator.plugins.length > 0) { 
			  var swf = navigator.plugins["Shockwave Flash"]; 
			  if(swf) { 
				  hasFlash = 1; 
				  var words = swf.description.split(" "); 
				  for(var i = 0; i < words.length; ++i) { 
					  if(isNaN(parseInt(words[i]))) continue; 
					  flashVersion = parseInt(words[i]); 
				  } 
			  } 
		  } 
	  }	 
	  return { 
		  f: hasFlash, 
		  v: flashVersion 
	  }; 
} 
function BrowserChecker(){
	var IEVersion=getBrowserVersion();
	if(IEVersion=="undefined"||IEVersion==undefined){
		return "您的浏览器不属于IE浏览器，为避免浏览器版本兼容问题，建议直接使用IE10或更高版本!";
	}else if(IEVersion!="IE10"||IEVersion!="IE11"){
		 return null ;
	}else{
		var flashinfo=flashChecker();
		if(flashinfo==0){
			return "你的IE浏览器"+IEVersion+"版本需安装flash插件，或直接升级IE版本至IE0或更高版本！";
		}else{
			return "你的IE浏览器为"+IEVersion+"，建议升级IE版本至IE0或更高版本！";
		}
		
	}
	
}
function getBrowserVersion(){      
	var userAgent = navigator.userAgent.toLowerCase();   
	if(userAgent.match(/msie ([\d.]+)/)!=null){//ie6--ie9             
		uaMatch = userAgent.match(/msie ([\d.]+)/);             
		return 'IE'+uaMatch[1];        
	}else if(userAgent.match(/(trident)\/([\w.]+)/)){ 
		uaMatch = userAgent.match(/trident\/([\w.]+)/);    
		switch (uaMatch[1]){                            
		case "4.0": return "IE8" ;break;                     
		case "5.0": return "IE9" ;break;                              
		case "6.0": return "IE10";break;                       
		case "7.0": return "IE11";break;                       
		default:return "undefined" ;         
		}  
	}       
	return "undefined";  
}
