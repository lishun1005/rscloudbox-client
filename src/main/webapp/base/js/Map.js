
/**
 * 自定義js的Map類型
 * 
 * @returns {Map}
 */

function Map(){
	this.container = new Object();
}

/**
 * 根据Map类的tostring方法，构造将string类型强制转化为Map类型
 */
function parseMap(str){
	var obj=str.split(";\n");
	var map=new Map();
	for(var i=0;i<obj.length;i++){
		var attr=obj[i].split("=");
		map.put(attr[0],attr[1]);
	}
	return map;
}
function parseMap2(str){
	var obj=str.split(",");
	var map=new Map();
	for(var i=0;i<obj.length;i++){
		var attr=obj[i].split(":");
		map.put(attr[0],attr[1]);
	}
	return map;
}
/**
 * 特殊字符串的转译方法 str 需要转换的字符串 splitChar 字符、字符串数组 取消str字符串中不需要的一个或者多个字符 objChar
 * splitChar flag 是否需要去掉value值中的双引号
 */
Map.prototype.specialParseMap=function(str,objChar,valueChar,splitChar){
	if(undefined!=splitChar&&undefined!=splitChar.length&&splitChar.length>0){
		for(var i=0;i<splitChar.length;i++){
			var regS = new RegExp("\\"+splitChar[i],"g");
			str=str.replace(regS,"");
		}
		
	}
	var obj=str.split(objChar);
	var map=new Map();
	for(var i=0;i<obj.length;i++){
		var attr=obj[i].split(valueChar);
		map.put(attr[0],attr[1]);
	}
	return map;
}
Map.prototype.put = function(key, value){
	this.container[key] = value;
}


Map.prototype.get = function(key){
	return this.container[key];
}

Map.prototype.getContainsIdList = function(id){
	var keyset = new Array();
	var count = 0;
	for (var key in this.container) {
		// 跳过object的extend函数
		if (key.indexOf(id)>=0) {
			keyset[count] = key;
			count++;
		}
	}
	return keyset;
}

Map.prototype.keySet = function() {
	var keyset = new Array();
	var count = 0;
	for (var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend') {
			continue;
		}
		keyset[count] = key;
		count++;
	}
	return keyset;
}


Map.prototype.size = function() {
	var count = 0;
	for (var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend'){
			continue;
		}
		count++;
	}
	return count;
}


Map.prototype.remove = function(key) {
	delete this.container[key];
}


Map.prototype.toString = function(){
	var str = "";
	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
		str = str + keys[i] + "=" + this.container[keys[i]] ;
		if(i!=len-1){
		str = str + ";\n"
		}
	}
	return str;
}

Map.prototype.valueToString = function(){
	var str = "";
	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
		str = str + this.container[keys[i]] ;
		if(i!=len-1){
			str = str + "_";
		}
	}
	return str;
}

