function userBlur(){
  if($.trim($('.usertxt').val()).length<1){
	  $('.userlab').html('用户名不能为空 ！');
	  return false;
  }else{
	  return true;
  }
}

function pwdBlur(){
	if($.trim($('.pwdtxt').val()).length<1){
	  $('.pwdlab').html('密码不能为空！');
	  return false;
	}else{
		return true;
	}
}
function userFocus(){
	$('.userlab').html('');
	}
function pwdFocus(){
	$('.pwdlab').html('');
	}
	
$(function(){
	$("#but").click(function(){
		var r = Math.random();
		if(userBlur()&&pwdBlur()){
			var user ={
				username:$('.usertxt').val(),
				password:$('.pwdtxt').val()
			};
			var url="loginAction?r=" + r;
			$.ajax({
				type:"GET",
				url:url,
				data:user,
				dataType : "json",
				success: function(map){
					if(map.code =="1"){
						window.location.href="./";
						}else{
							$('.pwdlab').html(map.message);
						}
					}
				});
			}else{
				$('.pwdlab').html('密码不能为空 ！');
			}
	});
	$(".pwdtxt").keydown(function(event){
		if(event.keyCode ==13){
			event.cancelBubble=true;
			event.returnValue=false;
			$("#but").click();
		}
	})
})
	



