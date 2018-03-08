$(document).ready(function(){
	//设置地图底图
	//setBaseLayers();
	// 初始化地区选择框中的一系列相关控件的事件
	setAreaSelectedDivFun(true);
	// 初始化省份一级的地区选择
	initProvinceDiv();


});

/**
* 根据所选的地区及所选的数据类型后台发送申请，得到数据
* 
* 注：另producttypeid='b' 或者producttypeid='a' 就是页面初始化成功后信息产品、影像数据 模块中【查看更多】的功能
*/
function getSelectedDataParams(){
	// 得到CheckedSelectedArea保存的最小范围的地区Id
	var areaid=getSelectedLitteAreaId();
	//根据地区id查询地区的面积和在地图上显示地图的范围
	 //显示地区
	chooseAreaType = 1;
	chooseAreaId = areaid;
	showAreaBounds(areaid);
}

/**
 * 显示出地区的范围
 * @param areaid
 */
function showAreaBounds(areaid)
{
	//请求后台数据库
	var r = Math.floor(Math.random() * 9999 + 1);
	var params = {r:r,areaId:areaid};
	var url = "./queryAreaGeomByAreaId_dataProduct.htm";
	$.getJSON(url,params,function(data) {
			showLocationBounds(data.data.geom);
	});
}

/**
* 得到CheckedSelectedArea保存的最小范围的地区Id
* 
* @returns
*/
function getSelectedLitteAreaId(){
	if(CheckedSelectedArea.hasOwnProperty("town")){
		return CheckedSelectedArea.town.townid;
	}
	if(CheckedSelectedArea.hasOwnProperty("city")){
		return CheckedSelectedArea.city.cityid;
	}
	if(CheckedSelectedArea.hasOwnProperty("pro")){
		return CheckedSelectedArea.pro.proid;
	}
}
