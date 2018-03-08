function queryproductInformationByGeom() {
	var geom="MULTIPOLYGON(((100.89769417939 17.922544795084,69.221550845607 37.744771947141,81.075472397772 55.156095447864,125.54349550914 56.092300224363,151.10614923198 43.680532190464,131.84177661732 11.819602729869,100.89769417939 17.922544795084)))";
	var productsourceType="原始影像";
	var cloudsrange="0 and 10";
	var Timerange="'2014-01-03 13:31:43' and '2015-06-06 13:31:43' ";
	//var productId="0001bdb5061b6dc2434199da6a1f0b1c62f8";
	var r=Math.random(); 
	var url = "queryproductInformationByGeom?r=" + r;
	var params = {
		geom:geom,
		productsourceType:productsourceType,
		cloudsrange:cloudsrange,
		Timerange:Timerange,
		//productId:productId
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if(data.code==1){
				if(data.HtmltableInit=="InitTable")
				alert();
			}else{
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
function diva(){
	alert("12312");
	
}