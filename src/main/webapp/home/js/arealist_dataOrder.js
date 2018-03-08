// JavaScript Document
/**
 * 根据正坤的地区的webservice得到
 */
var areaList="";//存放行政区域的json数据
//var webserviceIP="http://58.252.5.13:8080";//webservice的ip地址
var webserviceIP="http://210.77.87.225:8084";//webservice的ip地址
var specialAreaStr="北京1市_天津1市_上海1市_重庆1市";//特殊地区集  目前特殊地区集只存在于市一级地区

/**
 * 初始化地区选择框中的一系列相关控件的事件\
 * @param falg 点击了按钮，是否触发筛选数据的事件
 */
function setAreaSelectedDivFun(flag){

	
	//选择地区的确定按钮事件
	$(".address-confirm").click(function(){
		$("#government_list,.datapro_index_arealist").hide();
		$(".rihgt-bar-li1").removeClass("active");
		if(undefined!=selectedArea&&undefined!=selectedArea.pro){
	 		totalAddress=selectedArea.pro.proname;
	 		if(undefined!=selectedArea.city){
	 			totalAddress+=selectedArea.city.cityname;
	 		}
	 		if(undefined!=selectedArea.town){
	 			totalAddress+=selectedArea.town.townname;
	 		}
	 	}
	 	CheckedSelectedArea=selectedArea;
    	$(".current-area").removeClass("active");
    	
    	var currentAdd_show="";
    	$(".result-item").each(function(i){
    		if(i<3){
	    		//alert("隐藏?"+$(this).is(":hidden"));
	    		if($(this).css("display")=="inline"){
	    			currentAdd_show+=$(this).text();
	    		}else{}
    		}
    	});
    	$(".localposition span,.datapro_index_select span").text(currentAdd_show);
    	var area_a=$(this).parents(".datapro_index_arealist").siblings("a.datapro_index_select");
    	if($(area_a).hasClass("new_datacenter")){
    		var text_old=$("#show_index_local").html();
    		var text_new="";
    		//$(area_a).children("span").attr("title",text_old);
    		$("#show_index_local").attr("title",text_old);
    	
    		if(text_old.length>15){
    			text_old=text_old.slice(0,15);
    			$("#show_index_local").text(text_old+"...");
    		}
    	}
    	
    	if(flag){
    		getSelectedDataParams();
    	}
    });

	
	//选择地区的确定按钮事件
	$(".address-confirm-address").click(function(){


		$("#government_list,.datapro_index_arealist").hide();

		if(undefined!=selectedArea&&undefined!=selectedArea.pro){
	 		totalAddress=selectedArea.pro.proname;
	 		if(undefined!=selectedArea.city){
	 			totalAddress+=selectedArea.city.cityname;
	 		}
	 		if(undefined!=selectedArea.town){
	 			totalAddress+=selectedArea.town.townname;
	 		}
	 	}
	 	CheckedSelectedArea=selectedArea;
		
		$("#government_list").hide();

    	$(".current-area").removeClass("active");
    	var currentAdd_show="";
    	$(".result-item").each(function(i){
    		if(i<3){
	    		//alert("隐藏?"+$(this).is(":hidden"));
	    		if($(this).css("display")=="inline"){
	    			currentAdd_show+=$(this).text();
	    		}else{}
    		}
    	});
    	$("#show_index_local").text(currentAdd_show);
    });
	
    //显示地区选择列表
/*    $(".current-area").click(function(){
    	$(".select-wrap").toggle();
    	if( $(this).hasClass("active") ){
    		$(this).removeClass("active");
    	}else{
    		$(this).addClass("active");
    	}
    });*/
    //省市切换选择
	$(".result-item").click(function(){
		$(this).hide().next(".result-item").hide().next(".result-item").hide();
		var thisAttr = $(this).attr("data-attr");
		$(".select-"+thisAttr).show().siblings(".select-item").hide();
		$(".please-select").show();
	}); 
    //关闭地区选择列表
    $(".close-areaselect-dataOrder").click(function(){
    	$("#government_list,.datapro_index_arealist").hide();
		//$("#government_list").hide();
    	$(".current-area").removeClass("active");
    	$(".rihgt-bar-li1").removeClass("active");
    });
    
    //改版后行政区域控件
    $(".sel-pro-city-town li").on("click",function(){
    	var forDiv=$(this).attr("data-attr");
    	$(this).addClass("active").siblings("li").removeClass("active");
    	$(".government-list-"+forDiv).show().siblings(".government-list-eacharea").hide();
    });
}

/**
 * 初始化省份一级的地区选择
 */
function initProvinceDiv(){
	//检查全局变量areaList中是否已保存省份地区列表
	var proflag=checkHasAreaList(0);
	if(proflag){//已存在 
		initProvinceFromAreaList();
	}else{//未存在
		getAreaListDivFromWebService(0);
	}
}
/**
 * 初始化市级的地区选择 并设置全局变量中所选择的省份
 * @param proId	 市级的地区所在省份的id
 * @param proname 市级的地区所在省份的名称
 * @param event
 * @returns  
 */
function initCityDiv(proId,proname,event){
	$(event).parent().addClass("active").siblings().removeClass("active");    
	selectedArea={
					pro:{
							proid:proId,
							proname:proname
						}
				};
	
	$(".current-province").show().text(selectedArea.pro.proname);
	$(".select-province").hide();
	$(".select-city").show();
	
	//检查全局变量areaList中是否已保存地区Id为proId/cityId子地区列表
	var cityContent=checkHasAreaList(1,proId);
	if(cityContent){//存在
		initCityDivFromcityList(cityContent);
	}else{//不存在
		//特殊地区判断
		var test=specialAreaStr.indexOf(proname);
		if(specialAreaStr.indexOf(proname)>=0){//是特殊地区
			getSpecialAreaListFromWebService(0,proId,proname);
		}else{
			getAreaListDivFromWebService(1,proId);
		}
		
	}
	
	
}
/**
 * 初始化县/区级的地区选择 并设置全局变量中所选择的市
 * @param cityId	县/区级的地区所在市级的地区的id
 * @param cityname 	县/区级的地区所在市级的地区的名称
 * @param event
 * @returns  
 */
function initTownDiv(cityId,cityname,event){
	$(event).parent().addClass("active").siblings().removeClass("active");    
	$(this).parent().attr("class");
	selectedArea.city={
						cityid:cityId,
						cityname:cityname
				};
	$(".current-city").show().text(selectedArea.city.cityname);
	$(".select-city").hide();
	//检查全局变量areaList中是否已保存地区Id为proId/cityId子地区列表
	var townContent=checkHasAreaList(2,selectedArea.pro.proid,cityId);
	/*if(townContent){//存在
		initTownDivFromcityList(townContent);
	}else{//不存在
*/		getAreaListDivFromWebService(2,selectedArea.pro.proid,cityId);
	//}
	//$(".select-town").show();
/*	var town = $(".select-town");
	var bool = town.is(":hidden");
	if(bool){
		town.hide();
	}else{
		town.show();
	}*/
}
/**
 * 设置全局变量中所选择的县/区级的地区
 * @param townId	所选择的的县/区级的地区的id
 * @param townname 	所选择的的县/区级的地区的名称
 * @param event
 * @returns  
 */
function getTownSelected(townId,townname,event){
	$(event).parent().addClass("active").siblings().removeClass("active");    
	var test=$(this).parent();
	$(this).parent().parent().attr("class");
	selectedArea.town={
			townid:townId,
			townname:townname
	};
	$(".current-town").show().text(selectedArea.town.townname);
	//$(".please-select").hide();
	$(".select-town").hide();
}

/**
 * 根据areaList的内容初始化省级地区的选择框
 */
function initProvinceDivFromAreaList(){
	//根据areaList的内容初始化省份一级的地区选择
	if(undefined!=areaList.prolist&&areaList.prolist.length>0){
		var prostr="";
		for(var i=0;i<areaList.prolist.length;i++){
			var pro=areaList.prolist[i];
			prostr+='<li><a onclick=initCityDiv('+pro.adminId+',\"'+pro.name+'\",this)>'+pro.name+'</a></li>';
		}
		$(".province").html(prostr);
	}
}

/**
 * 根据ciytlist的内容初始化市级地区的选择框
 * @param ciytlist
 * @returns
 */
function initCityDivFromcityList(ciytlist){
	var citystr="";
	if(undefined!=ciytlist&&ciytlist.length>0){
		for(var i=0;i<ciytlist.length;i++){
			citystr+='<li><a onclick=initTownDiv('+ciytlist[i].adminId+',\"'+ciytlist[i].name+'\",this)>'+ciytlist[i].name+'</a></li>';
		}
	}
	$(".city").html(citystr);
	$(".select-city").show();
}

/**
 * 根据ciytlist的内容初始化县/区级地区选择的框
 * @param ciytlist
 * @returns
 */
function initTownDivFromtownList(townlist){
	//根据areaList的内容初始化省份一级的地区选择
	var townstr="";
	if(undefined!=townlist&&townlist.length>0){
	
		for(var i=0;i<townlist.length;i++){
			townstr+='<li><a onclick=getTownSelected('+townlist[i].adminId+',\"'+townlist[i].name+'\",this)>'+townlist[i].name+'</a></li>';
		}
	}
	$(".town").html(townstr);
	$(".select-town").show();
}
/**
 * 检查全局变量areaList中是否已保存省份地区列表，或者是否已保存地区Id为proId/cityId子地区列表
 * @param type	 0 检查全局变量areaList是否保存了省份地区的列表
 * 				 1 检查全局变量areaList是否已保存省份Id为proId市级地区列表
 * 		  		 2 检查全局变量areaList是否已保存省份Id为proId,市级Id为cityId的县区级列表
 * @param proId
 * @param cityId
 * @returns  不存在 返回false
 *			 存在 返回地区列表
 */
function checkHasAreaList(type,proId,cityId){
	//根据areaList的内容初始化省份一级的地区选择
	var json={hasAreaList:false};
	if(undefined!=areaList.prolist&&areaList.prolist.length>0){
		if(type==0){//查找省级列表
			return true;
		}else{
			for(var i=0;i<areaList.prolist.length;i++){
				var pro=areaList.prolist[i];
				if(proId==pro.adminId){
					if(type==1){//查找市级列表
						if(undefined!=pro.citylist&&undefined!=pro.citylist.length&&pro.citylist.length>0){
							return pro.citylist;
						}else if(undefined!=pro.citylist&&undefined!=pro.noChildList){
							return pro.noChildList;
						}
					}else if(type==2){//查找县/区级列表
						if(undefined!=pro.citylist&&undefined!=pro.citylist.length&&pro.citylist.length>0){
							for(var j=0;j<pro.citylist.length;j++){
								var city=pro.citylist[j];
								if(cityId==city.adminId){
									if(undefined!=city.townlist&&undefined!=city.townlist.length&&city.townlist.length>0){
										return city.townlist;
									}else{
										return false;
									}
								}
							}
						}else{
							return false;
						}
					}
				}
			}
		}
	}else{
		return false;
	}
}

/**
 * 访问gt-cloud的webservice得到不同级别的地区列表，并初始化相应的地区选择区域
 * @param type 0 得到省级列表，1 得到市级列表，2 得到县/区级列表
 * @param fatherId 
 */
function getAreaListDivFromWebService(type,proId,cityId){
	var fatherId="";
	if(type==1){
		fatherId=proId;
	}else if(type==2){
		fatherId=cityId;
	}
	var r = Math.floor(Math.random() * 9999 + 1);
	var url=webserviceIP+"/mapsrv/services/rest/geoService/getAreaList?r="+r+"&_type=json&key=&fatherId="+fatherId+"&geomType=1&accuracy=2&_jsonp=?";
	$.getJSON(url,function(data){
		if(data.code==1){
			if(type==0){
				areaList={
						prolist:data.list//省份列表
				}
				initProvinceDivFromAreaList();
			}else if(type==1){
				initCityDivFromcityList(data.list);
				if(undefined!=areaList.prolist&&areaList.prolist.length>0){
					for(var i=0;i<areaList.prolist.length;i++){
						if(areaList.prolist[i].adminId==proId){
							areaList.prolist[i].citylist=data.list;
							break;
						}
					}
				}
			}else if(type==2){
				initTownDivFromtownList(data.list);
				if(undefined!=areaList.prolist&&areaList.prolist.length>0){
					for(var i=0;i<areaList.prolist.length;i++){
						if(areaList.prolist[i].adminId==proId){
							var pro=areaList.prolist[i];
							if(undefined!=pro.citylist&&pro.citylist.length>0){
								for(var j=0;j<pro.citylist.length;j++){
									if(pro.citylist[j].adminId==cityId){
										areaList.prolist[i].citylist[j].townlist=data.list;
									}
								}
							}
						}
					}
				}
			}
		}else if(data.code==0){
			if(type==1){
				if(undefined!=areaList.prolist&&areaList.prolist.length>0){
					for(var i=0;i<areaList.prolist.length;i++){
						if(areaList.prolist[i].adminId==proId){
							areaList.prolist[i].noChildList=true;
							$(".city").html("");
							break;
						}
					}
				}
			}else if(type==2){
				if(undefined!=areaList.prolist&&areaList.prolist.length>0){
					for(var i=0;i<areaList.prolist.length;i++){
						if(areaList.prolist[i].adminId==proId){
							var pro=areaList.prolist[i];
							if(undefined!=pro.citylist&&pro.citylist.length>0){
								for(var j=0;j<pro.citylist.length;j++){
									if(pro.citylist[j].adminId==cityId){
										areaList.prolist[i].citylist[j].noChildList=true;
										$(".town").html("");
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	});
}
/**
 * 获取特殊地区的子地区列表 
 * @param type specialAreaStr="北京市_天津市_上海市_重庆市" 目前这四个直辖市因在数据库中存在“县”，“直辖区”两个类别的地区，此方法是将“县”，“直辖区”两个类别的地区合并为一个地区列表
 * @param proId
 * @param proname
 * @param cityid
 * @param cityname
 */
function getSpecialAreaListFromWebService(type,proId,proname,cityid,cityname){
	var fatherId="";
	if(type==0){
		fatherId=proId;
	}else if(type==1){
		fatherId=cityId;
	}
	var r = Math.floor(Math.random() * 9999 + 1);
	var url=webserviceIP+"/mapsrv/services/rest/geoService/getAreaList?r="+r+"&_type=json&key=&fatherId="+fatherId+"&geomType=1&accuracy=2&_jsonp=?";
	$.getJSON(url,function(data){
		if(data.code==1){//目前的特殊地区将其子地区划分为县/直辖区两种不同类型的地区，需要将此两种不同的地区合并到一起
			var adminIds=[data.list[0].adminId,data.list[1].adminId];
			var datalist=null;
			r = Math.floor(Math.random() * 9999 + 1);
			$.getJSON(webserviceIP+"/mapsrv/services/rest/geoService/getAreaList?r="+r+"&_type=json&key=&fatherId="+adminIds[0]+"&geomType=1&accuracy=2&_jsonp=?",function(arealist1){
				if(arealist1.code==1){
					datalist=arealist1.list;
					r = Math.floor(Math.random() * 9999 + 1);
					$.getJSON(webserviceIP+"/mapsrv/services/rest/geoService/getAreaList?r="+r+"&_type=json&key=&fatherId="+adminIds[1]+"&geomType=1&accuracy=2&_jsonp=?",function(arealist2){
						if(arealist2.code==1){
							for(var i=0;i<arealist2.list.length;i++){
								datalist.push(arealist2.list[i]);
							}
							initCityDivFromcityList(datalist);
							for(var i=0;i<areaList.prolist.length;i++){
								if(areaList.prolist[i].adminId==proId){
									areaList.prolist[i].citylist=datalist;
									break;
								}
							}
						}
					});
				}
			});
		}
	});
} 