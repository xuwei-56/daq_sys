

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
	var pagedevicedata;
	var pageSize = 20;
	var count;
	$.ajax({
		url:'/device/getDevice',
		type:'POST',
		data:{"offset":1,"pageSize":pageSize},
		datatype:'json',
		beforeSend:function(){
			//加载提示图标
			$(".loading_area").fadeIn();
		},
		success:function(data){
			data = JSON.parse(data);
			if(data.code == 1){
				//getPage(data.data[0].count,1,pageSize);
				count = data.data[0].count;
				pagedevicedata = data.data;
				console.log(pagedevicedata);
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
								pagedevicedata = data.data;
								console.log(pagedevicedata);
								var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
								data.data.forEach(function(device){
									devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='deleteDevice'>删除</a><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='changeDevice'>修改</a></td></tr>";
				  				})
				  				$('#deviceTable').html(devicedata);
							}
						}
					})
				}});
				var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
				data.data.forEach(function(device){
					devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='deleteDevice'>删除</a><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='changeDevice'>修改</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a></td></tr>";
  				})
  				$('#deviceTable').html(devicedata);
  				$(".loading_area").fadeOut();
			}
		}
	})
	

    //添加active
    
    /*$(".admin_tab li a").click(function(){
	  	liindex = $(".admin_tab li a").index(this);
	  	$(this).addClass("active").parent().siblings().find("a").removeClass("active");
	  	$(".admin_tab_cont").eq(liindex).fadeIn(150).siblings(".admin_tab_cont").hide();
	});*/
	var deviceIndex = 0;
	
	$('#searchDevice').on('click',function(){
		console.log('123');
	})

	$('#selectDevice').click(function(){
		console.log($('#selectDevice').val());
	});

	//修改设备信息
	var trNum;
	$('#deviceTable').delegate("#changeDevice","click",function(){
		trNum = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		$('#changeDevice_pop').fadeIn();
		$('#changeDeviceIp').val(pagedevicedata[trNum].deviceIp);
		$('#changeAddress').val(pagedevicedata[trNum].address);
		$('#changeUserName').val(pagedevicedata[trNum].userName);
	})

	$('#deviceTable').delegate("#changeDevice","click",function(){
		trNum = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		$('#searchHistory').fadeIn();
		$('#historyDeviceIp').val(pagedevicedata[trNum].deviceIp);
	})

	$('#historytime').blur(function(){
		var time = removeAllSpace($('#historytime').val());
		var marry = /^20[12]\d[01]\d[0-3]\d?/;
		if (time.length != 8 || !marry.test(time)) {
			$('#historytime').css({
				border:"1px solid red"
			});
			('#historyCue').html("<font color='red'><b>请输入正确的时间格式</b></font>");
			return false;
		}
		$('#historyCue').html("<font color='#19a97b'><b>ip地址正确</b></font>");
		$('#history_true').removeAttr("disabled");
		$('#historytime').css({
			border: "1px solid #d2d2d2"
		});
	})

	$('#deviceTable').delegate("#history_true","click",function(){
		location.href = "/data?deviceIp="+pagedevicedata[trNum].deviceIp+"&historytime="+removeAllSpace($('#historytime').val());
	})
	
	$("#changeDeviceIp").blur(function(){
		var changedeviceIp = removeAllSpace($('#changeDeviceIp').val());
		//var marry = /((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
		var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		console.log(changedeviceIp);
		if (changedeviceIp == "" || !marry.test(changedeviceIp)) {
			$('#changeDeviceIp').css({
				border: "1px solid red"
			});
			$('#changeDeviceCue').html("<font color='red'><b>请输入正确的ip地址</b></font>");
			return false;
		}else{
			$.ajax({
				url:"/device/judgeDeviceIp",
				type:"POST",
				data:{"deviceIp":changedeviceIp},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						console.log(data.msg);
						$('#changeDeviceCue').html("<font color='red'><b>此IP地址设备已添加</b></font>")
					}else{
						$('#changeDeviceCue').html("<font color='#19a97b'><b>ip地址正确</b></font>");
						$('#changeDevice_true').removeAttr("disabled");
						$('#changeDeviceIp').css({
							border: "1px solid #d2d2d2"
						});
					}
				}

			})
		}
	})

	//过滤地址
	$('#changeAdderess').blur(function(){
		var changeadderess = removeAllSpace($('#changeAddress').val());
		if (changeaddress == "" ) {
			$('#changeAddress').css({
				border: "1px solid red"
			});
			$('#changeDeviceCue').html("<font color='red'><b>请输入地址</b></font>");
			return false;
		}else{
			$('#changeDeviceCue').html("");
			$('#changeDevice_true').removeAttr("disabled");
			$('#changeAddress').css({
				border: "1px solid #d2d2d2"
			});
		}
	})

	//匹配管理员
	$("#changeUserName").blur(function(){
		var changeuserName = removeAllSpace($('#changeUserName').val());
		if (changeuserName == "") {
			$('#changeUserName').css({
				border: "1px solid red"
			});
			$('#changeDeviceCue').html("<font color='red'><b>请输入管理员</b></font>");
			return false;
		}else{
			$.ajax({
				url:"/user/judgeUserName",
				type:"POST",
				data:{"userName":changeuserName},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						console.log(data.msg);
						$('#changeDeviceCue').html("<font color='red'><b>没有找到对应管理员</b></font>")
					}else{
						$('#changeDevice_true').removeAttr("disabled");
						$('#changeDeviceCue').html("<font color='#19a97b'><b>管理员名称正确</b></font>");
						$('#changeUserName').css({
							border: "1px solid #d2d2d2"
						});
					}
				}

			})
		}

	})

	 //弹出：确认按钮
	$('#changeDevice_pop').delegate("#changeDevice_true","click",function(){

		var changedeviceIp = removeAllSpace($('#changeDeviceIp').val());
		var changeadderess = removeAllSpace($('#changeAddress').val());
		var changeuserName = removeAllSpace($('#changeUserName').val());
		console.log(pagedevicedata[trNum].deviceId);
		$.ajax({
			url:"/device/updateDevice",
			type:"POST",
			data:{"deviceId":pagedevicedata[trNum].deviceId,"userName":changeuserName,"deviceIp":changedeviceIp,"address":changeadderess},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					$('#changeDeviceCue').html("<font color='red'><b>"+ data.msg +"</b></font>");
				}else{
					//$('#deviceTable tr').eq(trNum+1).find('td').eq(0).text(removeAllSpace($('#changeDeviceIp').val()));
					$('#deviceTable tr').eq(trNum+1).find('td').eq(0).html("<a href='/data?deviceIp="+ removeAllSpace($('#changeDeviceIp').val()) +"' class='inner_btn_ip'>"+ removeAllSpace($('#changeDeviceIp').val()) +"</a>");
					$('#deviceTable tr').eq(trNum+1).find('td').eq(1).text(removeAllSpace($('#changeAddress').val()));
					$('#deviceTable tr').eq(trNum+1).find('td').eq(2).text(removeAllSpace($('#changeUserName').val()));
				}
			}

		})
		$("#changeDevice_pop").fadeOut();
	});

	//弹出：取消或关闭按钮
	//$("#changeDevice_false").click(function(){
	$('#changeDevice_pop').delegate("#changeDevice_false","click",function(){
		$("#changeDevice_pop").fadeOut();
	});

	$('#deviceTable').delegate("#deleteDevice","click",function(){
		trNum = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		alert("确定删除ip为" + pagedevicedata[trNum].deviceIp);
		$.ajax({
			url:"/device/deleteDevice",
			type:"POST",
			data:{"deviceId":pagedevicedata[trNum].deviceId},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					alert("<font color='red'><b>"+ data.msg +"</b></font>");
				}else{
					$('#deviceTable tr').eq(trNum+1).css({
						display:"none"
					})
				}
			}
		})
	})

    $('#userList').click(function(){
    	if (isroot == 1) {
    		$('#pageTool').html("");

			$.ajax({
				url:"/user/getUsers",
				type:"POST",
				data:{"pageSize":pageSize,"page":1},
				datatype:"json",
				beforeSend:function(){
					//加载提示图标
					$(".loading_area").fadeIn();
				},
				success:function(data){
					data = JSON.parse(data);
					if(data.code == 1){
						count = data.data[0].count;
						$('#pageTool').Paging({pagesize:pageSize,count:count,callback:function(page,size,count){
							$.ajax({
								url:'/device/getUsers',
								type:'POST',
								data:{"page":page,"pageSize":size},
								datatype:'json',
								success:function(data){
									data = JSON.parse(data);
									if(data.code == 1){
										//getPage(data.data[0].count,1,pageSize);
										count = data.data[0].count;
										var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
										data.data.forEach(function(user){
											userdata += "<tr><td><a href='/device?userId="+ user.userId +"' class='inner_btn_ip'>"+ user.userId +"</a></td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
						  				})
						  				$('#deviceTable').html(userdata);
									}
								}
							})
						}});
						var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
						data.data.forEach(function(user){
							userdata += "<tr><td><a href='/device?userId="+ user.userId +"' class='inner_btn_ip'>"+ user.userId +"</a></td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
		  				})
		  				$('#deviceTable').html(userdata);
					}
					$(".loading_area").fadeOut();
				}
			})
		}else{
			alert("没有权限")
		}
	})
	
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
				}else{
					$(".pop_bg").fadeOut();
					location.reload();
				}
			}

		})
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
		var password = removeAllSpace($('#password').val());
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
		var new_passwordlength = removeAllSpace($('#new_password').val()).length;
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
		var password = removeAllSpace($('#loginPassword').val());
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
		var new_phoneNumber = removeAllSpace($('#new_phoneNumber').val());
		var marry = /(^1(3|4|5|7|8)\d{9}$)/;
		if (new_phoneNumber.length != 11 || !(marry.test(new_phoneNumber))) {
			$('#editPhoneNumberCue').html("<font color='red'><b>请输入正确的11位手机号码</b></font>");
			return false;
			$('#new_phoneNumber').css({
				border: "1px solid red"
			});
		}
		$("#editPhoneNumberCue").html("手机号码格式正确");
		$('#new_phoneNumber').css({
			border: "1px solid red"
		});
		$('#editPhoneNumber_true').removeAttr("disabled");
	})

	//确认修改电话号码
	$("#editPhoneNumber_true").click(function(){
		$.ajax({
			url:"/user/editPhoneNumber",
			type:"POST",
			data:{"userId":userId,"password":removeAllSpace($('#loginPassword').val()),"new_phoneNumber":removeAllSpace($('#new_phoneNumber').val())},
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

	//添加管理员
	$('#addUser').click(function(){
		$('#addUser_pop').fadeIn();
	})

	$('#addUserId').blur(function(){
		var newUserId = removeAllSpace($('#addUserId').val());
		var marry = /(^\d{8}$)/;
		if (newUserId == "" || newUserId.length != 8 || !marry.test(newUserId)) {
			$('#addUserId').css({
				border: "1px solid red"
			});
			$('#addUserCue').html("<font color='red'><b>请输入管正确理员帐号</b></font>");
			return false;
		}
		$("#addUserCue").html("帐号正确");
		$('#addUserId').css({
			border: "1px solid #d2d2d2"
		});
	})

	//判断密码是否符合要求
	$('#addNewPassword').blur(function(){
		var new_passwordlength = removeAllSpace($('#addNewPassword').val()).length;
		if (new_passwordlength < 6 || new_passwordlength > 12) {
			$('#addNewPassword').css({
				border: "1px solid red"
			});
			$('#addUserCue').html("<font color='red'><b>×密码长度介于 6 到 12 位</b></font>");
			return false;
		}
		$("#addUserCue").html("密码格式正确");
		$('#addNewPassword').css({
				border: "1px solid #d2d2d2"
			});
	})
	$('#addNewPassword1').blur(function(){
		if ($('#addNewPassword1').val() != $('#addNewPassword').val()) {
			$('#addNewPassword1').css({
				border: "1px solid red"
			});
			$('#addUserCue').html("<font color='red'><b>×两次密码不一致！</b></font>");
			return false;
		}
		$("#addUserCue").html("两次密码一致");
		$('#addNewPassword1').css({
				border: "1px solid #d2d2d2"
		});
	})

	$('#addNewUserName').blur(function(){
		var newUserName = removeAllSpace($('#addNewUserName').val());
		var marry = /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
		if (newUserName.length > 1 || newUserName.length < 7 || !marry.test(newUserName)) {
			$('#addNewUserName').css({
				border: "1px solid red"
			});
			$('#addUserCue').html("<font color='red'><b>请输入正确的 2 ~ 6 位管理员名称（可以包含 _、字母、汉字）</b></font>");
			return false;
		}
		$("#addUserCue").html("名称格式正确");
		$('#addNewUserName').css({
			border: "1px solid #d2d2d2"
		});

	})

	//判断电话号码是否符合要求
	$('#addPhoneNumber').blur(function(){
		var addPhoneNumber = removeAllSpace($('#addPhoneNumber').val());
		var marry = /(^1(3|4|5|7|8)\d{9}$)/;
		if (addPhoneNumber.length != 11 || !(marry.test(addPhoneNumber))) {
			$('#addUserCue').html("<font color='red'><b>请输入正确的11位手机号码</b></font>");
			$('#addPhoneNumber').css({
				border: "1px solid red"
			});
			return false;
		}
		$("#addUserCue").html("手机号码格式正确");
		$('#addPhoneNumber').css({
			border: "1px solid #d2d2d2"
		});
		$('#addUser_true').removeAttr("disabled");
	})

	//添加管理员
	$("#addUser_true").click(function(){
		var newUserId = removeAllSpace($('#addUserId').val());
		var userPassword = removeAllSpace($('#addNewPassword').val());
		var userPhoneNumber = removeAllSpace($('#addPhoneNumber').val());
		var newUserName = removeAllSpace($('#addUserName').val());
		$.ajax({
			url:"/user/addUser",
			type:"POST",
			data:{"userId":newUserId,"password":userPassword,"phoneNumber":userPhoneNumber,"userName":newUserName},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					console.log(data.msg);
					$('#addUserCue').html("<font color='red'><b>" + data.msg + "</b></font>")
				}else{
					alert("添加管理员成功");
					$("#addUser_pop").fadeOut();
					$('#pageTool').html("");
					$.ajax({
						url:"/user/getUsers",
						type:"POST",
						data:{"pageSize":pageSize,"page":1},
						datatype:"json",
						beforeSend:function(){
							//加载提示图标
							$(".loading_area").fadeIn();
						},
						success:function(data){
							data = JSON.parse(data);
							if(data.code == 1){
								count = data.data[0].count;
								$('#pageTool').Paging({pagesize:pageSize,count:count,callback:function(page,size,count){
									$.ajax({
										url:'/device/getUsers',
										type:'POST',
										data:{"page":page,"pageSize":size},
										datatype:'json',
										success:function(data){
											data = JSON.parse(data);
											if(data.code == 1){
												//getPage(data.data[0].count,1,pageSize);
												count = data.data[0].count;
												var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
												data.data.forEach(function(user){
													userdata += "<tr><td><a href='/device?userId="+ user.userId +"' class='inner_btn_ip'>"+ user.userId +"</a></td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
								  				})
								  				$('#deviceTable').html(userdata);
											}
										}
									})
								}});
								var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
								data.data.forEach(function(user){
									userdata += "<tr><td><a href='/device?userId="+ user.userId +"' class='inner_btn_ip'>"+ user.userId +"</a></td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn'>修改</a><a href='#' class='inner_btn'>删除</a></td></tr>";
				  				})
				  				$('#deviceTable').html(userdata);
							}
							$(".loading_area").fadeOut();
						}
					})
				}
			}
		})
	});

	//关闭修改电话号码弹框
	$("#addUser_false").click(function(){
		$("#addUser_pop").fadeOut();
	});
	

	//打开查询历史数据弹框
	$('#searchDeviceHistory').click(function(){
		$('#searchHistory').fadeIn();
	})

})