//去除空格
function removeAllSpace(str) {
	return str.replace(/\s+/g, "");
}

$(document).ready(function() {
	var isroot = 0;
	var userId;

	
	//请求管理员名称
	$.ajax({
		url:"/user/getSessionUser",
		type:"POST",
		data:{},
		datatype:"json",
		success:function(data){
			data = JSON.parse(data);
			console.log(data.data.userName);
			if(data.code == 1){
				$("#admin_name").html(data.data.userName);
				$('#UserId').val(data.data.userId);
				$('#PhoneNumber').val(data.data.phoneNumber);
				isroot = data.data.isRoot;
				userId = data.data.userId;
				if (isroot == 1) {
					$('#power').val("超级管理员");
				}else{
					$('#power').val("初级管理员");
				}
			}else{
				console.log(data.msg);

				//$("#userCue").html("<font color='red'><b>"+data.msg+"</b></font>");
		}
		}
	})

	//加载设备信息列表
	var pageSize = 20;
	var count;
	$.ajax({
		url:'/device/getDevice',
		type:'POST',
		data:{"offset":1,"pageSize":pageSize},
		datatype:'json',
		success:function(data){
			data = JSON.parse(data);
			if(data.code == 1){
				//getPage(data.data[0].count,1,pageSize);
				count = data.data[0].count;
				$('#pageTool').Paging({pagesize:pageSize,count:count,callback:function(page,size,count){
					$.ajax({
						url:'/device/getDevice',
						type:'POST',
						data:{"offset":page,"pageSize":size},
						datatype:'json',
						success:function(data){
							data = JSON.parse(data);
							if(data.code == 1){
								//getPage(data.data[0].count,1,pageSize);
								count = data.data[0].count;
								var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
								data.data.forEach(function(device){
									devicedata += "<tr><td><a href='/data/getdataByDeviceId?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
				  				})
				  				$('#deviceTable').html(devicedata);
							}
						}
					})
				}});
							var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
				data.data.forEach(function(device){
					devicedata += "<tr><td><a href='/data/getdataByDeviceId?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
  				})
  				$('#deviceTable').html(devicedata);
			}
		}
	})
	

	//加载提示图标
	$(".loading_area").fadeIn();
        $(".loading_area").fadeOut(1500);

    //添加active
    $(".admin_tab li a").click(function(){
	  	var liindex = $(".admin_tab li a").index(this);
	  	$(this).addClass("active").parent().siblings().find("a").removeClass("active");
	  	$(".admin_tab_cont").eq(liindex).fadeIn(150).siblings(".admin_tab_cont").hide();
	});

	//安全退出
	$('#logout').click(function(){

		$.ajax({
			url:"/user/logout",
			type:"POST",
			data:{},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if(data.code == 1){
					console.log(data.msg+isroot);
					location.href="/";
				}else{
					console.log(data.msg+isroot);
			}
			}
		})
	})

	//添加设备参数
	var deviceIp;
	var address;
	var userName;

	//匹配ip
	$("#addDeviceIp").blur(function(){
		deviceIp = removeAllSpace($('#addDeviceIp').val());
		//var marry = /((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
		var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		console.log(deviceIp);
		if (deviceIp == "" || !marry.test(deviceIp)) {
			$('#addDeviceIp').css({
				border: "1px solid red"
			});
			$('#userCue').html("<font color='red'><b>请输入正确的ip地址</b></font>");
			return false;
		}else{
			$.ajax({
				url:"/device/judgeDeviceIp",
				type:"POST",
				data:{"deviceIp":deviceIp},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						console.log(data.msg);
						$('#userCue').html("<font color='red'><b>此IP地址设备已添加</b></font>")
					}else{
						$('#userCue').html("<font color='#19a97b'><b>ip地址正确</b></font>");
						$('#addDeviceIp').css({
							border: "1px solid #d2d2d2"
						});
					}
				}

			})
		}
	})

	//过滤地址
	$('#addAddress').blur(function(){
		address = removeAllSpace($('#addAddress').val());
		if (address == "" ) {
			$('#addAddress').css({
				border: "1px solid red"
			});
			$('#userCue').html("<font color='red'><b>请输入地址</b></font>");
			return false;
		}else{
			$('#userCue').html("");
			$('#addAddress').css({
				border: "1px solid #d2d2d2"
			});
		}
	})

	//匹配管理员
	$("#addUserName").blur(function(){
		userName = removeAllSpace($('#addUserName').val());
		if (userName == "") {
			$('#addUserName').css({
				border: "1px solid red"
			});
			$('#userCue').html("<font color='red'><b>请输入管理员</b></font>");
			return false;
		}else{
			$.ajax({
				url:"/user/judgeUserName",
				type:"POST",
				data:{"userName":userName},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						console.log(data.msg);
						$('#userCue').html("<font color='red'><b>没有找到对应管理员</b></font>")
					}else{
						$('#addDevice_true').removeAttr("disabled");
						$('#userCue').html("<font color='#19a97b'><b>管理员名称正确</b></font>");
						$('#addUserName').css({
							border: "1px solid #d2d2d2"
						});
					}
				}

			})
		}

	})

	//弹出文本性提示框
	$("#addDevice").click(function(){
		if (isroot == 1) {
			$("#addDevice_pop").fadeIn();
		}else{
			alert("没有权限")
		}
		
	});


	 //弹出：确认按钮
	$("#addDevice_true").click(function(){
		 //alert("你点击了确认！");//测试
		$.ajax({
			url:"/device/setDevice",
			type:"POST",
			data:{"userName":userName,"deviceIp":deviceIp,"address":address},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					$('#userCue').html("<font color='red'><b>"+ data.msg +"</b></font>")
				}
			}

		})
		$(".pop_bg").fadeOut();
		location.reload();
	});

	//弹出：取消或关闭按钮
	$("#addDevice_false").click(function(){
		$(".pop_bg").fadeOut();
	});
	

	//账号设置
	$('#set_admin').click(function() {
		$("#setAdmin_pop").fadeIn();
	})

	//关闭帐号设置弹框
	$("#setAdmin_false").click(function(){
		$("#setAdmin_pop").fadeOut();
	});

	//修改密码
	$('#editPassword_button').click(function(){
		$('#editPassword_pop').fadeIn();
	})

	$('#password').blur(function(){
		var password = $('#password').val();
		if (password == "") {
			$('#password').css({
				border: "1px solid red"
			});
			$('#editPasswordCue').html("<font color='red'><b>请输入登录密码</b></font>");
			return false;
		}
		$("#editPasswordCue").html("");
		$('#password').css({
				border: "1px solid #d2d2d2"
			});
	})
	//判断密码是否符合要求
	$('#new_password').blur(function(){
		var new_passwordlength = $('#new_password').val().length;
		if (new_passwordlength < 6 || new_passwordlength > 12) {
			$('#new_password').css({
				border: "1px solid red"
			});
			$('#editPasswordCue').html("<font color='red'><b>×密码长度介于 6 到 12 位</b></font>");
			return false;
		}
		$("#editPasswordCue").html("密码格式正确");
		$('#new_password').css({
				border: "1px solid #d2d2d2"
			});
	})
	$('#new_password1').blur(function(){
		if ($('#new_password1').val() != $('#new_password').val()) {
			$('#new_password1').css({
				border: "1px solid red"
			});
			$('#editPasswordCue').html("<font color='red'><b>×两次密码不一致！</b></font>");
			return false;
		}
		$("#editPasswordCue").html("两次密码一致");
		$('#new_password1').css({
				border: "1px solid #d2d2d2"
			});
		$('#editPassword_true').removeAttr("disabled");
	})

	//确认修改密码
	$("#editPassword_true").click(function(){
		$.ajax({
			url:"/user/editPassword",
			type:"POST",
			data:{"userId":userId,"password":$('#password').val(),"new_password":$('#new_password').val()},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					$('#editPasswordCue').html("<font color='red'><b>" + data.msg + "</b></font>")
				}else{
					alert("修改密码成功");
					$("#editPassword_pop").fadeOut();
				}
			}
		})
	});

	//关闭密码修改弹框
	$("#editPassword_false").click(function(){
		$("#editPassword_pop").fadeOut();
	});

	//修改电话号码
	$('#editPhoneNumber_button').click(function(){
		$('#editPhoneNumber_pop').fadeIn();
	})

	$('#loginPassword').blur(function(){
		var password = $('#loginPassword').val();
		if (password == "") {
			$('#loginPassword').css({
				border: "1px solid red"
			});
			$('#editPhoneNumberCue').html("<font color='red'><b>请输入登录密码</b></font>");
			return false;
		}
		$("#editPhoneNumberCue").html("");
		$('#loginPassword').css({
				border: "1px solid #d2d2d2"
			});
	})

	//判断电话号码是否符合要求
	$('#new_phoneNumber').blur(function(){
		var new_phoneNumber = $('#new_phoneNumber').val();
		var marry = /(^1(3|4|5|7|8)\d{9}$)/;
		if (new_phoneNumber.length != 11 || !(marry.test(new_phoneNumber))) {
			//$('#passwd').focus();
			$('#editPhoneNumberCue').html("<font color='red'><b>请输入正确的11位手机号码</b></font>");
			return false;
		}
		$("#editPhoneNumberCue").html("手机号码格式正确");
		$('#editPhoneNumber_true').removeAttr("disabled");
	})

	//确认修改电话号码
	$("#editPhoneNumber_true").click(function(){
		$.ajax({
			url:"/user/editPhoneNumber",
			type:"POST",
			data:{"userId":userId,"password":$('#loginPassword').val(),"new_phoneNumber":$('#new_phoneNumber').val()},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					$('#editPhoneNumberCue').html("<font color='red'><b>" + data.msg + "</b></font>")
				}else{
					alert("修改手机号码成功");
					$("#editPhoneNumber_pop").fadeOut();
				}
			}
		})
	});

	//关闭修改电话号码弹框
	$("#editPhoneNumber_false").click(function(){
		$("#editPhoneNumber_pop").fadeOut();
	});
	
})