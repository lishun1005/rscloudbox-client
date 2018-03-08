$(function() {
	initproductsourcetype("productsourcetype");
	initData();
});
function initData(){
     $("#starttime").val(getBeforeDate(765));
     $("#endtime").val(getBeforeDate(0));
}
function getBeforeDate(n){
    var n = n;
    var d = new Date();
    var year = d.getFullYear();
    var mon=d.getMonth()+1;
    var day=d.getDate();
    if(day <= n){
            if(mon>1) {
               mon=mon-1;
            }
           else {
             year = year-1;
             mon = 12;
             }
           }
     d.setDate(d.getDate()-n);
     year = d.getFullYear();
     mon=d.getMonth()+1;
     day=d.getDate();
     s = year+"/"+mon+"/"+day;
     return s;
}
function initValueproductsourcetype(parentId){
	$("#" + parentId).find("input:first-child").trigger("click");
}
function initValueproductTreelist(){
	$(".saillite_sensor ul li input[name='imgdata_checkb']:first-child").trigger("click");
}
function initproductsourcetype(parentId) {
	var r = Math.random();
	var url = "productSourcetype.htm?r=" + r;
	var params = {};
	var parentdiv = $("#" + parentId);
	parentdiv.empty();
	$.ajax({
		type : "GET",
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			if (data.code == "1") {
				$(data.list).each(
						function(e) {
							var input = $("<input type=\"radio\" id=\""
									+ this.id + "\" name=\"productsourcetype\">");
							input.val(this.typename);
							input.attr("id", this.id);
							var lable = $("<label>").text(this.typename);
							input.appendTo(parentdiv);
							lable.appendTo(parentdiv);
						});
			} else {
				alert(data.message);
			}
			activeproductsourcetype(parentId);
			initValueproductsourcetype(parentId);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			location=getRootPath()+"/login.html"
		}
	});
	
}
function activeproductsourcetype(parentId) {
	$("#" + parentId).find("input").on("click", function() {
		initproductTreelist(this.id)
	});
}
function initproductTreelist(id) {
	var r = Math.random();
	var url = "producttypelist.htm?r=" + r;
	var params = {
		Sourcetypeid : id
	};
	var parentdiv = $(".saillite_sensor ul");
	parentdiv.empty();
	$.ajax({
				type : "GET",
				url : url,
				data : params,
				dataType : "json",
				success : function(data) {
					if (data.code == "1") {
						for ( var key in data.list[0]) {
							var li = $("<li>");
							var div = $("<div class=\"t_checkb\">");
							var input = $("<input type=\"checkbox\" name=\"imgdata_checkb\" class=\"imgdata_checkb\">");
							var span = $("<span class=\"rotate\">");
							span.html("&gt")
							input.appendTo(div);
							span.appendTo(div);
							var label = $("<label>");
							label.text(key);
							label.appendTo(div);
							div.appendTo(li);
							var values = data.list[0][key];
							var div2 = $("<div class=\"c_checkb_lst\">");
							var ul = $("<ul>");
							$(values)
									.each(
											function(e) {
												var li2 = $("<li>");
												var input = $("<input type=\"checkbox\" name=\"checkb_cld\" class=\"checkb_cld\">");
												input.attr("id", this.id);
												input.attr("parentid",
														this.parentid);
												input.val(this.typename);
												var span = $("<span class=\"rotate\">");
												span.html("&gt");
												var label = $("<label>");
												label.text(this.typename);
												input.appendTo(li2);
												span.appendTo(li2);
												label.appendTo(li2);
												li2.appendTo(ul);
											});
							ul.appendTo(div2);
							div2.appendTo(li);
							li.appendTo(parentdiv);
							ativeproductTreelist();
						}
						initValueproductTreelist();
					} else {
						alert(data.message);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					location=getRootPath()+"/login.html"
				}
			});
}
function ativeproductTreelist() {
	// 地区范围块切换
	$(".saillite_sensor ul li input[name='imgdata_checkb']").click(
			function() {
				// 如果父亲选中，则孩子全部选中
				$(this).parent().parent("li").siblings().children(
						".c_checkb_lst").hide();
				$(this).parent().parent("li").siblings().children().children(
						"span").removeClass("on");
				$(this).parent().parent("li").siblings().find(
						"input:checkbox[name='imgdata_checkb']").removeAttr(
						"checked");
				if ($(this).prop("checked")) {
					$(this).parent().parent("li").children().children("span")
							.addClass("on");
					$(this).parent().parent("li").children(".c_checkb_lst")
							.show();
					$(this).parent().parent("li").find(
							"input:checkbox[name='checkb_cld']").each(
							function() {
								$(this).prop("checked", true);
							});
				} else {
					$(this).parent().parent("li").children(".c_checkb_lst")
							.hide();
					$(this).parent().parent("li").find(
							"input:checkbox[name='checkb_cld']").each(
							function() {
								$(this).prop("checked", false);
							});
				}
			});
}