var imagebbox = new Map();
function queryproductInformationByAreaName() {
	// 判断筛选条件是否选中“行政区划”
	var r = Math.random();
	var url = "queryproductInformationByAreaName?r=" + r;
	var provinceSelectId = $("#provinceSelect option:selected").attr("id");
	var citySelectId = $("#citySelect option:selected").attr("id");
	var countySelectId = $("#countySelect option:selected").attr("id");
	var productsourceType = $("#productsourcetype input:checked").val();
	var productType = [];
	$(".saillite_sensor ul li").find("input:checked[name='checkb_cld']").each(
			function() {
				productType.push($(this).val());
			});
	var cloudsrange = $(".start_cloud").val() + " and " + $(".end_cloud").val();
	var Timerange = $("#starttime").val() + "," + $("#endtime").val();
	var params = {
		provinceId : provinceSelectId,
		cityId : citySelectId,
		CountyId : countySelectId,
		productsourceType : productsourceType,
		productType : productType.join(","),
		cloudsrange : cloudsrange,
		Timerange : Timerange,
	};
	var parentdiv = $(".searchlist_div table tbody");
	parentdiv.empty();
	$
			.ajax({
				type : "GET",
				url : url,
				data : params,
				dataType : "json",
				success : function(data) {
					if (data.code == "1") {
						$(data.list)
								.each(
										function(e) {
											var tr = $("<tr>");
											tr.attr("id", this.id);
											tr.attr("name", this.name);
											tr.attr("path", this.file_path);
											var td = $("<td>");
											var input = $("<input type=\"checkbox\" name=\"table_checkb\">");
											td.clone().append(input).appendTo(
													tr);
											var img = $("<img src=\"home/img/person_white.png\" name=\"table_img_pase\" >");
											td.clone().append(img).appendTo(tr);
											td
													.clone()
													.addClass("satetype")
													.text(
															this.image_satellite_type)
													.appendTo(tr);
											td.clone().text(this.sensor_id)
													.appendTo(tr);
											td.clone().text(
													this.image_resolution)
													.appendTo(tr);
											td.clone().text(this.image_row_col)
													.appendTo(tr);
											td.clone().text(this.collect_time)
													.appendTo(tr);
											td.clone().text(this.product_id)
													.appendTo(tr);
											parentdiv.append(tr);
											imagebbox.put(this.id, this.gemo);
											var geom = wkt_c.read(this.gemo);
											geom.fid = this.id;
											featurelayer.addFeatures(geom);
										});
						parentdiv
								.find("input:checkbox[name='table_checkb']")
								.on(
										"click",
										function() {
											var id = $(this).parent().parent(
													"tr").attr("id");
											var geom = imagebbox.get(id);
											unselectfeaturestyle(geom, id);
											/* ToPastImageGeomF(geom,id); */
											// 如果选中，调用后台接口更新元数据
											if ($(this).prop("checked")) {
												selectfeaturestyle(geom, id);
												var path = $(this).parent()
														.parent("tr").attr(
																"path");
												var name = $(this).parent()
														.parent("tr").attr(
																"name");
												var urlGetXML = "getXMLInfo.htm";
												var dataType = $(this).parent(
														"td").siblings(
														".satetype").text();
												var params = {
													id : id,
													filepath : path,// 路径
													name : name,// 文件名称
													DataType : dataType
												// 数据类型，GF-1/ZY3 etc
												};
												$
														.ajax({
															type : "GET",
															url : urlGetXML,
															data : params,
															dataType : "json",
															success : function(
																	data) {
																if (data.code == "1") {
																	// 更新元数据列表
																	var parentTable = $(".datainfo_table tbody");
																	var xmlModel = data.list[0];
																	parentTable
																			.empty();
																	for ( var key in xmlModel) {
																		var tr = $("<tr>");
																		var td = $("<td>");
																		td.clone().text(key).appendTo(tr);// 遍历Key-value
																		td.clone().text(xmlModel[key]).appendTo(tr);
																		parentTable
																				.append(tr);

																	}

																	var imgbox = $(".second_div div img");// .siblings().children("img");
																	var imgbox2 = $("#metaDataImg");

																	/*
																	 * var
																	 * testObj=$(".second_div");
																	 * var
																	 * testObj2=$(".second_div").parents("li");
																	 */
																	// 显示元数据div，隐藏其他
																	// 同类div
																	$(
																			".second_div")
																			.show();
																	$(
																			".second_div")
																			.parents(
																					"li")
																			.siblings()
																			.children(
																					"div")
																			.hide();

																	//
																	$(
																			".second_div")
																			.parents(
																					"li")
																			.addClass(
																					"on");
																	$(
																			".second_div")
																			.parents(
																					"li")
																			.siblings()
																			.removeClass(
																					"on");

																	$(
																			"#metaDataImg")
																			.attr(
																					"src",
																					"getImage.htm?filepath="
																							+ path
																							+ "&name="
																							+ name);

																}
															}
														});
											}
										});
						parentdiv.find("img[name='table_img_pase']").on(
								"click",
								function() {
									var id = $(this).parent().parent("tr")
											.attr("id");
									var geom = imagebbox.get(id);
									var path = $(this).parent().parent("tr")
											.attr("path");
									var name = $(this).parent().parent("tr")
											.attr("name");
									PasteImageTomap(geom, path, name);
									// alert($(this).val());
								});
						parentdiv
								.find("tr")
								.on(
										"dblclick",
										function() {
											var id = $(this).attr("id");
											var geom = imagebbox.get(id);
											ToPastImageGeomF(geom, id);
											var path = $(this).attr("path");
											var name = $(this).attr("name");
											var urlGetXML = "getXMLInfo.htm";
											var dataType = $(this).children(
													"td").siblings(".satetype")
													.text();
											var params = {
												id : id,
												filepath : path,// 路径
												name : name,// 文件名称
												DataType : dataType
											// 数据类型，GF-1/ZY3 etc
											};
											$
													.ajax({
														type : "GET",
														url : urlGetXML,
														data : params,
														dataType : "json",
														success : function(data) {
															if (data.code == "1") {
																// 更新元数据列表
																var parentTable = $(".datainfo_table tbody");
																var xmlModel = data.list[0];
																parentTable
																		.empty();
																for ( var key in xmlModel) {
																	var tr = $("<tr>");
																	var td = $("<td>");
																	td
																			.clone()
																			.text(
																					key)
																			.appendTo(
																					tr);// 遍历Key-value
																	td
																			.clone()
																			.text(
																					xmlModel[key])
																			.appendTo(
																					tr);
																	parentTable
																			.append(tr);

																}
																var imgbox = $(".second_div div img");// .siblings().children("img");
																var imgbox2 = $("#metaDataImg");
																/*
																 * var
																 * testObj=$(".second_div");
																 * var
																 * testObj2=$(".second_div").parents("li");
																 */
																// 显示元数据div，隐藏其他
																// 同类div
																$(".second_div")
																		.show();
																$(".second_div")
																		.parents(
																				"li")
																		.siblings()
																		.children(
																				"div")
																		.hide();
																$(".second_div")
																		.parents(
																				"li")
																		.addClass(
																				"on");
																$(".second_div")
																		.parents(
																				"li")
																		.siblings()
																		.removeClass(
																				"on");
																$(
																		"#metaDataImg")
																		.attr(
																				"src",
																				"getImage.htm?filepath="
																						+ path
																						+ "&name="
																						+ name);
															}
														}
													});
										});

					} else {
						alert(data.message);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					location=getRootPath()+"/login.html"
				}
			});
}

// 通过中四点坐标
function queryproductInformationbyLonAndLat() {
	// 判断筛选条件是否选中“经纬度”
	var r = Math.random();
	var url = "queryproductInformationByGeom?r=" + r;
	var inputs = $(".longtit_a_latit form div input");// .chidrens("input");
	var left = inputs[0].value;// 经度（左）
	var top = inputs[1].value;// 纬度（上）
	var right = inputs[2].value;// 经度（右）
	var bottom = inputs[3].value;// 纬度（下）
	var TopLeftLongitude = left;
	var TopLeftLatitude = top;
	var BottomLeftLongitude = left;
	var BottomLeftLatitude = bottom;
	var BottomRightLongitude = right;
	var BottomRightLatitude = bottom;
	var TopRightLongitude = right;
	var TopRightLatitude = top;

	var geom = "POLYGON((" + TopLeftLongitude + " " + TopLeftLatitude + ","
			+ BottomLeftLongitude + " " + BottomLeftLatitude + ","
			+ BottomRightLongitude + " " + BottomRightLatitude + ","
			+ TopRightLongitude + " " + TopRightLatitude + ","
			+ TopLeftLongitude + " " + TopLeftLatitude + "))";

	var productsourceType = $("#productsourcetype input:checked").val();
	var productType = [];
	$(".saillite_sensor ul li").find("input:checked[name='checkb_cld']").each(
			function() {
				productType.push($(this).val());
			});
	var cloudsrange = $(".start_cloud").val() + " and " + $(".end_cloud").val();
	var Timerange = $("#starttime").val() + "," + $("#endtime").val();
	var params = {
		geom : geom,
		productsourceType : productsourceType,
		productType : productType.join(","),
		cloudsrange : cloudsrange,
		Timerange : Timerange,
	};
	var parentdiv = $(".searchlist_div table tbody");
	parentdiv.empty();
	$
			.ajax({
				type : "GET",
				url : url,
				data : params,
				dataType : "json",
				success : function(data) {
					if (data.code == "1") {
						$(data.list)
								.each(
										function(e) {
											var tr = $("<tr>");
											tr.attr("id", this.id);
											tr.attr("name", this.name);
											tr.attr("path", this.file_path);
											var td = $("<td>");
											var input = $("<input type=\"checkbox\" name=\"table_checkb\">");
											td.clone().append(input).appendTo(
													tr);
											var img = $(
													"<img src=\"home/img/person_white.png\" name=\"table_img_pase\" >")
													.val(this.name);
											td.clone().append(img).appendTo(tr);
											td
													.clone()
													.addClass("satetype")
													.text(
															this.image_satellite_type)
													.appendTo(tr);
											td.clone().text(this.sensor_id)
													.appendTo(tr);
											td.clone().text(
													this.image_resolution)
													.appendTo(tr);
											td.clone().text(this.image_row_col)
													.appendTo(tr);
											td.clone().text(this.collect_time)
													.appendTo(tr);
											td.clone().text(this.product_id)
													.appendTo(tr);
											parentdiv.append(tr);
											imagebbox.put(this.id, this.gemo);
											var geom = wkt_c.read(this.gemo);
											geom.fid = this.id;
											featurelayer.addFeatures(geom);
										});
						// checkbox事件
						parentdiv.find("input:checkbox[name='table_checkb']")
								.on("click", checkBox_OnClick);
						parentdiv.find("img[name='table_img_pase']").on(
								"click", Img_OnClick);
						parentdiv.find("tr").on("dblclick", tr_Onclick);

					}
				}
			});// ajax结束

}// function结束
function tr_Onclick() {
	var id = $(this).attr("id");
	var geom = imagebbox.get(id);
	ToPastImageGeomF(geom, id);
	var path = $(this).attr("path");
	var name = $(this).attr("name");
	var urlGetXML = "getXMLInfo.htm";
	var dataType = $(this).children("td").siblings(".satetype").text();
	var params = {
		id : id,
		filepath : path,// 路径
		name : name,// 文件名称
		DataType : dataType
	// 数据类型，GF-1/ZY3 etc
	};
	$.ajax({
		type : "GET",
		url : urlGetXML,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				// 更新元数据列表
				var parentTable = $(".datainfo_table tbody");
				var xmlModel = data.list[0];
				parentTable.empty();
				for ( var key in xmlModel) {
					var tr = $("<tr>");
					var td = $("<td>");
					td.clone().text(key).appendTo(tr);// 遍历Key-value
					td.clone().text(xmlModel[key]).appendTo(tr);
					parentTable.append(tr);

				}
				var imgbox = $(".second_div div img");// .siblings().children("img");
				var imgbox2 = $("#metaDataImg");
				/*
				 * var testObj=$(".second_div"); var
				 * testObj2=$(".second_div").parents("li");
				 */
				// 显示元数据div，隐藏其他
				// 同类div
				$(".second_div").show();
				$(".second_div").parents("li").siblings().children("div")
						.hide();
				$(".second_div").parents("li").addClass("on");
				$(".second_div").parents("li").siblings().removeClass("on");
				$("#metaDataImg").attr("src",
						"getImage.htm?filepath=" + path + "&name=" + name);
			}
		}
	});
}
function Img_OnClick() {
	var tr = $(this).parent().parent("tr");
	var id = tr.attr("id");
	var geom = imagebbox.get(id);
	var name = tr.attr("name");
	var filepath = tr.attr("path");
	PasteImageTomap(geom, filepath, name);
}

function checkBox_OnClick() {

	var id = $(this).parent().parent("tr").attr("id");
	var geom = imagebbox.get(id);
	ToPastImageGeomF(geom, id);

	// 如果选中，调用后台接口更新元数据
	if ($(this).prop("checked")) {

		var path = $(this).parent().parent("tr").attr("path");
		var name = $(this).parent().parent("tr").attr("name");

		var urlGetXML = "getXMLInfo.htm";
		var dataType = $(this).parent("td").siblings(".satetype").text();
		// alert(dataType);
		var params = {
			id : id,
			filepath : path,// 路径
			name : name,// 文件名称
			DataType : dataType
		// 数据类型，GF-1/ZY3 etc
		};

		$
				.ajax({
					type : "GET",
					url : urlGetXML,
					data : params,
					dataType : "json",
					success : function(data) {
						if (data.code == "1") {
							// 更新元数据列表
							var parentTable = $(".datainfo_table tbody");
							var xmlModel = data.list[0];
							parentTable.empty();
							for ( var key in xmlModel) {
								var tr = $("<tr>");
								var td = $("<td>");
								td.clone().text(key).appendTo(tr);// 遍历Key-value
								td.clone().text(xmlModel[key]).appendTo(tr);
								parentTable.append(tr);
							}
							// 显示元数据div，隐藏其他 同类div
							$(".second_div").show();
							$(".second_div").parents("li").siblings().children(
									"div").hide();
							//
							$(".second_div").parents("li").addClass("on");
							$(".second_div").parents("li").siblings()
									.removeClass("on");
							$("#metaDataImg").attr(
									"src",
									"getImage.htm?filepath=" + path + "&name="
											+ name);

						}
					}
				});

	}
}

function queryproductInformationbyFigure() {
	var r = Math.random();
	var url = "queryproductInformationByGeom?r=" + r;
	var feature = wfst.getFeatureByFid("graph");
	var geomWKT = wkt_c.write(feature)
	var productsourceType = $("#productsourcetype input:checked").val();
	var productType = [];
	$(".saillite_sensor ul li").find("input:checked[name='checkb_cld']").each(
			function() {
				productType.push($(this).val());
			});
	var cloudsrange = $(".start_cloud").val() + " and " + $(".end_cloud").val();
	var Timerange = $("#starttime").val() + "," + $("#endtime").val();
	var params = {
		geom : geomWKT,
		productsourceType : productsourceType,
		productType : productType.join(","),
		cloudsrange : cloudsrange,
		Timerange : Timerange,
	};
	var parentdiv = $(".searchlist_div table tbody");
	parentdiv.empty();
	$
			.ajax({
				type : "POST",
				url : url,
				data : params,
				dataType : "json",
				success : function(data) {
					if (data.code == "1") {
						$(data.list)
								.each(
										function(e) {
											var tr = $("<tr>");
											tr.attr("id", this.id);
											tr.attr("name", this.name);
											tr.attr("path", this.file_path);
											var td = $("<td>");
											var input = $("<input type=\"checkbox\" name=\"table_checkb\">");
											td.clone().append(input).appendTo(
													tr);
											var img = $(
													"<img src=\"home/img/person_white.png\" name=\"table_img_pase\" >")
													.val(this.name);
											td.clone().append(img).appendTo(tr);
											td
													.clone()
													.addClass("satetype")
													.text(
															this.image_satellite_type)
													.appendTo(tr);
											td.clone().text(this.sensor_id)
													.appendTo(tr);
											td.clone().text(
													this.image_resolution)
													.appendTo(tr);
											td.clone().text(this.image_row_col)
													.appendTo(tr);
											td.clone().text(this.collect_time)
													.appendTo(tr);
											td.clone().text(this.product_id)
													.appendTo(tr);
											parentdiv.append(tr);
											imagebbox.put(this.id, this.gemo);
											var geom = wkt_c.read(this.gemo);
											geom.fid = this.id;
											featurelayer.addFeatures(geom);
										});
						// checkbox事件
						parentdiv.find("input:checkbox[name='table_checkb']")
								.on("click", checkBox_OnClick);
						parentdiv.find("img[name='table_img_pase']").on(
								"click", Img_OnClick);
						parentdiv.find("tr").on("dblclick", tr_Onclick);
					}
				}
			});// ajax结束
}
function gettargzfileonline() {
	var parentdiv = $(".searchlist_div table tbody");
	var input = parentdiv.find("input:checked[name='table_checkb']");
	parentdiv.find("input:checked[name='table_checkb']").each(
			function() {
				var path = $(this).parent().parent("tr").attr("path");
				var name = path.substring(path.lastIndexOf("/") + 1,
						path.length);
				var url = "gettargzfileonline.htm?r=" + Math.random()
						+ "&filepath=" + path + "&name=" + name;
				var form = $("<form>"); // 定义一个form表单
				form.attr('style', 'display:none'); // 在form表单中添加查询参数
				form.attr('target', '');
				form.attr('method', 'post');
				form.attr('action', url);
				$('body').append(form); // 将表单放置在web中
				form.submit();

			});
}
function exportshpfile() {
	var r = Math.random();
	var feature = wfst.getFeatureByFid("graph");
	var geomWKT = wkt_c.write(feature);
	var url = "Exportshpfile?r=" + r;
	var form = $("<form>"); // 定义一个form表单
	form.attr('style', 'display:none'); // 在form表单中添加查询参数
	form.attr('target', '');
	form.attr('method', 'POST');
	form.attr('action', url);
	// <input name="uploads" id="imgFile"type="file" />
	var input = "<input type=\"text\" name=\"geomWKT\" value=\"" + geomWKT
			+ "\" />";//
	$(input).appendTo(form);
	$('body').append(form); // 将表单放置在web中
	form.submit();
}
function coveragestatistics() {
	var GeoJSON_c =new OpenLayers.Format.GeoJSON();
	var r = Math.random();
	var features = featurelayer.features;
	var MultiPolygon = new OpenLayers.Geometry.MultiPolygon();
	$(features).each(function(e) {
		MultiPolygon.addComponent(this.geometry);
	});
	//var MultiPolygonSTR = MultiPolygon.toString();
	var imagejson=GeoJSON_c.write(MultiPolygon);
	var feature = wfst.getFeatureByFid("graph");
	var areajson = GeoJSON_c.write(feature.geometry);
	var url = "coveragestatistics.htm?r=" + r;
	var input = $("<input id=\"imagejson\" type=\"text\" name=\"imagejson\"  />");//
	input.val(imagejson);
	input.attr('style', 'display:none');
	var input2 = $("<input id=\"areajson\"  type=\"text\" name=\"areajson\" />");//
	input2.val(areajson);
	input2.attr('style', 'display:none');
	$('body').append(input);
	$('body').append(input2);
	var arrId = [];
	arrId[0] = input[0].id;
	arrId[1] = input2[0].id;
	$.ajaxFileUpload({
		url : url,
		fileElementId : arrId,
		dataType : 'json',
		success : function(data) {
			if (data.code == 1) {
				ListencoveragestatisticsCallback(data.jobID);
			} else {
				alert(data.massege);
			}
		},
		error : function(data) {
			alert("error");
		}
	});
}
function ListencoveragestatisticsCallback(jobID) {
	var url="getcoveragestatistics.htm"
	var r = Math.random();
	var params = {
		r : r,
		jobId : jobID
	}
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if(data.status=="success"){
				alert(data.coverage);
			}else {
				setTimeout(ListencoveragestatisticsCallback(jobID),100);  
			}
		}
	});

}
