$(function() {
	//var aa=formatDegree("155.11")
/*	var lon="180度2分4秒";
	var aal=formatlonDegree(lon);
	alert(aal);*/
	 /*var patternLongitude = /^((\d|[1-9]\d|1[0-7]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,2})?[\″]?[E]|[W]$)|(180[°]0[′]0[\″]?[E]|[W]$)/;
	  var patternLatitude = /^((\d|[1-8]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,2})?[\″]?[N]|[S]$)|(90[°]0[′]0[\″]?[N]|[S]$)/;*/
	
/*	var lngRe=/^[-]?(\d|([1-9]\d)|(1[0-7]\d)|(180))(\.\d*)?$/g;
	var latRe=/^[-]?(\d|([1-8]\d)|(90))(\.\d*)?$/g;*/
	/*var lon="-28°36'22\"";
	var aal=latDegreeConvertBack(lon);
	alert(aal);*/

});
function formatlonDegree(lon){
	var lngRe=/^[-]?(\d|([1-9]\d)|(1[0-7]\d)|(180))(\.\d*)?$/g;
	if(lon.match(lngRe)!=null&&lon<=180&&lon>=-180){
		return formatDegree(lon);
	}else{
		return false;
	}
}
function formatlatDegree(lat){
	var latRe=/^[-]?(\d|([1-8]\d)|(90))(\.\d*)?$/g;
	if(lat.match(latRe)!=null&&lat<=90&&lat>=-90){
		return formatDegree(lat);
	}else{
		return false;
	}
}
function lonDegreeConvertBack(lonDegree){
	var patternLongitude = "^[-]?[EW]?((\\d|[1-9]\\d|1[0-7]\\d)[\\s\\-,;°度](\\d|[0-5]\\d)[\\s\\-,;'分](\\d|[0-5]\\d)(\\.\\d{1,2})?[\\s\\-,;\"秒]?$)|(180[\\s\\-,;°度]0[\\s\\-,;'分]0[\\s\\-,;\"秒]?$)";
	if(lonDegree.match(patternLongitude)!=null){
		return DegreeConvertBack(lonDegree);
	}else{
		return false;
	}
}
function latDegreeConvertBack(latDegree){
	var patternLatitude = "^[-]?[NS]?((\\d|[1-8]\\d)[\\s\\-,;°度](\\d|[0-5]\\d)[\\s\\-,;'分](\\d|[0-5]\\d)(\\.\\d{1,2})?[\\s\\-,;\"秒]?$)|(90[\\s\\-,;°度]0[\\s\\-,;'分]0[\\s\\-,;\"秒]?$)";
	if(latDegree.match(patternLatitude)!=null){
		return DegreeConvertBack(latDegree);
	}else{
		return false;
	}
}
function formatDegree(value) {
    ///<summary>将度转换成为度分秒</summary>
	var flag;
	if(value.substr(0,1)=="-"){
		flag="-";
	}else{
		flag="";
	}
    value = Math.abs(value);
    var v1 = Math.floor(value);//度
    var v2 = Math.floor((value - v1) * 60);//分
    var v3 = Math.round((value - v1) * 3600 % 60);//秒
    return flag+ v1 + '°' + v2 + '\'' + v3 + '"';
};

function DegreeConvertBack(value)
{ ///<summary>度分秒转换成为度</summary>
	var flag;
    var du = value.split("°")[0];
    if(du.substr(0,1)=="-"){
		flag="-";
	}else{
		flag="";
	}
    var fen = value.split("°")[1].split("'")[0];
    var miao = value.split("°")[1].split("'")[1].split('"')[0];
    return flag+ (Math.abs(du) + (Math.abs(fen)/60 + Math.abs(miao)/3600)); 
}
function loncompare(lon1,lon2){
	var lngRe=/^[-]?(\d|([1-9]\d)|(1[0-7]\d)|(180))(\.\d*)?$/g;
	if(lon1.match(lngRe)!=null&&lon1<=180&&lon1>=-180&&lon2.match(lngRe)!=null&&lon2<=180&&lon2>=-180){
		if(parseFloat(lon1)<parseFloat(lon2)){
			return false;
		}else{
			return true;
		}
	}else{
		return false;
	}
}
function latcompare(lat1,lat2){
	var latRe=/^[-]?(\d|([1-8]\d)|(90))(\.\d*)?$/g;
	if(lat1.match(latRe)!=null&&lat1<=90&&lat1>=-90&&lat2.match(latRe)!=null&&lat2<=90&&lat2>=-90){
		if(parseFloat(lat1)<parseFloat(lat2)){
			return false;
		}else{
			return true;
		}
	}else{
		return false;
	}
}
function lonDegreecompare(lonDegree1,lonDegree2){
	var patternLongitude = "^[-]?[EW]?((\\d|[1-9]\\d|1[0-7]\\d)[\\s\\-,;°度](\\d|[0-5]\\d)[\\s\\-,;'分](\\d|[0-5]\\d)(\\.\\d{1,2})?[\\s\\-,;\"秒]?$)|(180[\\s\\-,;°度]0[\\s\\-,;'分]0[\\s\\-,;\"秒]?$)";
	if(lonDegree2.match(patternLongitude)!=null&&lonDegree2.match(patternLongitude)!=null){
		if(parseFloat(DegreeConvertBack(lonDegree1))<parseFloat(DegreeConvertBack(lonDegree2))){return false;}else{return true;}
	}else{
		return false;
	}
}
function latDegreecompare(latDegree1,latDegree2){
	var patternLatitude = "^[-]?[NS]?((\\d|[1-8]\\d)[\\s\\-,;°度](\\d|[0-5]\\d)[\\s\\-,;'分](\\d|[0-5]\\d)(\\.\\d{1,2})?[\\s\\-,;\"秒]?$)|(90[\\s\\-,;°度]0[\\s\\-,;'分]0[\\s\\-,;\"秒]?$)";
	if(latDegree1.match(patternLatitude)!=null&&latDegree2.match(patternLatitude)!=null){
		//alert(DegreeConvertBack(latDegree1)+":"+DegreeConvertBack(latDegree2))
		if(parseFloat(DegreeConvertBack(latDegree1))<parseFloat(DegreeConvertBack(latDegree2))){
			return false;
		}else{
			return true;
		}
	}else{
		return false;
	}
}