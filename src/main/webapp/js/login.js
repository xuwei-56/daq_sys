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
				$('#userCue').html("<font color='red'><b>×</b></font>");
			}
			if (password == "") {
				$('#user').css({
					border: "1px solid red",
					boxShadow: "0 0 2px red"
				});
				$('#pwdCue').html("<font color='red'><b>×</b></font>");
			}
			if (verifyCode == "") {
				$('#user').css({
					border: "1px solid red",
					boxShadow: "0 0 2px red"
				});
				$('#VcodeCue').html("<font color='red'><b>×</b></font>");
			}
			return false;
		}else{
			$.ajax({
				url:"/user/login",
				type:"POST",
				data:{"userId":userId,"password":password,"verifyCode":verifyCode},
				datatype:"json",
				success:function(data){
					alert(data);
					data = JSON.parse(data);
					if(data.msg=="登陆成功"){
						location.href="/index";
					}else{
						alert("<font color='red'><b>"+data.msg+"</b></font>")
						//$("#userCue").html("<font color='red'><b>"+data.msg+"</b></font>");
				}
				}
			});
		}
	})
}

