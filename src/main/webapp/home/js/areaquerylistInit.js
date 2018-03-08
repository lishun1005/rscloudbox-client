$(function() {
	//地图左侧导航栏点击切换
	$(".home_right_content_ul li select")
			.change(
					function() {
						if ($(this)[0].id == "provinceSelect") {
							Areaquery("AreaqueryforCity", $(this).children(
									'option:selected').val());
							AreaGeomqueryByareaId($(this).children(
									'option:selected')[0].id);
						} else if ($(this)[0].id == "citySelect") {
							Areaquery("AreaqueryforCounty", $(this).children(
									'option:selected').val());
							AreaGeomqueryByareaId($(this).children(
									'option:selected')[0].id);
						} else {
							AreaGeomqueryByareaId($(this).children(
									'option:selected')[0].id);
						}
					});
	Areaquery("AreaqueryforProvince", null);
});
function Areaquery(urlN, whatK) {
	var r = Math.random();
	var url = urlN + ".htm?r=" + r;
	var params = {};
	var selectid = "provinceSelect";
	if (urlN == "AreaqueryforCity") {
		params = {
			province : whatK
		};
		selectid = "citySelect"
	} else if (urlN == "AreaqueryforCounty") {
		params = {
			city : whatK
		};
		selectid = "countySelect"
	}
	var selectdiv = $(".home_right_content_ul li select[id='" + selectid + "']");
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				selectdiv.empty();
				$(data.list).each(function(e) {
					var option = $("<option>");
					option.text(this.name);
					option.attr("id", this.admincode);
					option.appendTo(selectdiv);
				});
				if (urlN == "AreaqueryforProvince") {
					Areaquery("AreaqueryforCity", data.list[0].name)
				} else if (urlN == "AreaqueryforCity") {
					Areaquery("AreaqueryforCounty", data.list[0].name)
				}
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}
function AreaGeomqueryByareaId(areaid) {
	var r = Math.random();
	var url = "AreaqueryByareaId?r=" + r;
	var params = {
		areaId : areaid
	};
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				ToGeom(data.list[0].geom);
			} else {
				alert(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
}