//验证码刷新
function refresh(obj){  
    obj.src = "/code/getAuthCode?type=type" + Math.random();  
}  
      
function mouseover(obj){  
    obj.style.cursor = "pointer";  
}

//去除空格
function removeAllSpace(str) {
	return str.replace(/\s+/g, "");
}

$(document).ready(function() {


	$('#user').blur(function(){
		if (removeAllSpace($('#user').val()) == "") {
			$('#user').css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>请输入管理员帐号</b></font>");
			return false;
		}else{
			$('#user').css({
				border: "1px solid #a5a5a5"
			});
		}
	})

	$('#password').blur(function(){
		if (removeAllSpace($('#password').val()) == "") {
			$('#password').css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>请输入管理员密码</b></font>");
			return false;
		}else{
			$('#user').css({
				border: "1px solid #a5a5a5"
			});
		}
	})

	$('#Vcode').blur(function(){
		if (removeAllSpace($('#Vcode').val()) == "") {
			$('#Vcode').css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>请输入管理员密码</b></font>");
			return false;
		}else{
			$('#user').css({
				border: "1px solid #a5a5a5"
			});
		}
	})

	//检测登录
	$('#button_login').click(function() {

		var userId = removeAllSpace($('#user').val());
		var password = removeAllSpace($('#password').val());
		var verifyCode = removeAllSpace($('#Vcode').val());
		
		if (userId == "" || password == "" || verifyCode == "") {
			if (userId == "") {
				$('#user').css({
					border: "1px solid red",
					boxShadow: "0 0 2px red"
				});
				$('#userCue').html("<font color='red'><b>请输入管理员帐号</b></font>");
				return false;
			}
			if (password == "") {
				$('#password').css({
					border: "1px solid red",
					boxShadow: "0 0 2px red"
				});
				$('#userCue').html("<font color='red'><b>请输入管理员密码</b></font>");
				return false;
			}
			if (verifyCode == "") {
				$('#Vcode').css({
					border: "1px solid red",
					boxShadow: "0 0 2px red"
				});
				$('#userCue').html("<font color='red'><b>请输入管理员密码</b></font>");
				return false;
			}
		}else{
			$.ajax({
				url:"/user/login",
				type:"POST",
				data:{"userId":userId,"password":password,"verifyCode":verifyCode},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if(data.code == 1){
						location.href="/index";
					}else{
						refresh(this);
						alert(data.msg)
						//$("#userCue").html("<font color='red'><b>"+data.msg+"</b></font>");
				}
				}
			});
		}
	})
})

