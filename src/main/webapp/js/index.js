

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
	var pageUserdata;
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
								var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
								data.data.forEach(function(device){
									devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a><a href='#' class='inner_btn' id='changeDevice'>修改</a></td></tr>";
				  				})
				  				$('#deviceTable').html(devicedata);
							}
						}
					})
				}});
				var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
				data.data.forEach(function(device){
					devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='changeDevice'>修改</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a></td></tr>";
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
	
	//弹出搜索设备弹窗
	$('#searchDevice').click(function(){
		$('#searchDevice_pop').fadeIn();
	})

	var sdc=0;
	$('#selectDevice').click(function(){
		sdc = $('#selectDevice').val();
	});

	var sdci;
	var sdIP;
	var sdAD;
	var sdUser;
	$('#searchDeviceInput').blur(function(){
		sdci = removeAllSpace($(this).val());
		if (sdc == 0) {
			var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
			if (sdci == "" || !marry.test(sdci)) {
				$('#searchDeviceCue').html("<font color='red'><b>请输入正确的ip地址</b></font>");
				return false;
			}else{
				$.ajax({
					url:"/device/judgeAuthrity",
					type:"POST",
					data:{"deviceIp":sdci},
					datatype:"json",
					success:function(data){
						data = JSON.parse(data);
						console.log(data)
						if (data.code != 1) {
							$('#searchDeviceCue').html("<font color='red'><b>1"+ data.msg +"</b></font>");
							return false;
						}else{
							sdIP = sdci;
							console.log(sdIP);
							$('#searchDeviceCue').html("<font color='#19a97b'><b>具有权限</b></font>");
						}
					}
				})
			}
		};
		if (sdc == 1) {
			if (sdci == "") {
				$('#searchDeviceCue').html("<font color='red'><b>请输入地址</b></font>");
				return false;
			}
			sdAD = sdci;
		};
		if (sdc == 2) {
			if (sdci == "") {
				$('#searchDeviceCue').html("<font color='red'><b>请输入管理员名称</b></font>");
				return false;
			}else{
				$.ajax({
					url:"/user/judgeUserName",
					type:"POST",
					data:{"userName":sdci},
					datatype:"json",
					success:function(data){
						data = JSON.parse(data);
						if (data.code != 1) {
							$('#searchDeviceCue').html("<font color='red'><b>没有找到对应管理员</b></font>");
							return false;
						}else{
							sdUser = sdci;
							$('#searchDeviceCue').html("<font color='#19a97b'><b>管理员名称正确</b></font>");
						}
					}
				})
			}
		};
	})

	$('#searchDevice_true').click(function(){
		if (sdci == null || sdci == "") {
			$('#searchDeviceCue').html("<font color='red'><b>请输入正确的值</b></font>");
			return false;
		} else {
			$('#searchDevice_pop').fadeOut();
    		$('#pageTool').html("");
			$.ajax({
				url:'/device/findDevice',
				type:'POST',
				data:{"offset":1,"pageSize":pageSize,"deviceIp":sdIP, "address":sdAD, "userName":sdUser},
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
								data:{"offset":1,"pageSize":pageSize,"deviceIp":sdIP, "address":sdAD, "userName":sdUser},
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
											devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a><a href='#' class='inner_btn' id='changeDevice'>修改</a></td></tr>";
						  				})
						  				$('#deviceTable').html(devicedata);
									}
									if (data.code == 100011) {
		  								$(".loading_area").fadeOut();
										alert(data.msg);
									}
								}
							})
						}});
						var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
						data.data.forEach(function(device){
							devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='changeDevice'>修改</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a></td></tr>";
		  				})
		  				$('#deviceTable').html(devicedata);
		  				$(".loading_area").fadeOut();
					}
					if (data.code == 100011) {
		  				$(".loading_area").fadeOut();
						alert(data.msg);
					}
				}
			})
		}
	})

	//关闭搜索设备弹窗
	$('#searchDevice_false').click(function(){
		$('#searchDevice_pop').fadeOut();
	})

	//修改设备信息
	var trNum;
	$('#deviceTable').delegate("#changeDevice","click",function(){
		trNum = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		$('#changeDevice_pop').fadeIn();
		$('#changeDeviceIp').val(pagedevicedata[trNum].deviceIp);
		$('#changeAddress').val(pagedevicedata[trNum].address);
		$('#changeUserName').val(pagedevicedata[trNum].userName);
	})


	
	$("#changeDeviceIp").blur(function(){
		var changedeviceIp = removeAllSpace($('#changeDeviceIp').val());
		//var marry = /((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
		var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
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

	//取消修改设备
	$('#changeDevice_pop').delegate("#changeDevice_false","click",function(){
		$("#changeDevice_pop").fadeOut();
	});

	//打开查询历史数据弹框
	$('#deviceTable').delegate("#searchDeviceHistory","click",function(){
		trNum = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		$('#searchHistory').fadeIn();
		$('#historyDeviceIp').val(pagedevicedata[trNum].deviceIp);
	})

	$('#historytime').blur(function(){
		var time = removeAllSpace($('#historytime').val());
		var marry = /^20[1-2]\d(0[1-9]|1[0-2])([0-2]\d|3[0-1])?/;
		var now = getNow();
		var lastYear = getLastYear();
		if (time.length != 8 || !marry.test(time) || time > now || time < lastYear) {
			$('#historytime').css({
				border:"1px solid red"
			});
			$('#historyCue').html("<font color='red'><b>请输入正确的时间格式</b></font>");
			return false;
		}
		$('#historyCue').html("<font color='#19a97b'><b>时间格式正确</b></font>");
		$('#history_true').removeAttr("disabled");
		$('#historytime').css({
			border: "1px solid #d2d2d2"
		});
	})

	$('#searchHistory').delegate("#history_true","click",function(){
		window.open("/data?deviceIp="+pagedevicedata[trNum].deviceIp+"&historytime="+removeAllSpace($('#historytime').val()));
	})

	$('#searchHistory').delegate("#history_false","click",function(){
		$('#searchHistory').fadeOut();
	})

	//阀值
	$('#alarm').click(function(){
		$.ajax({
			url:"/alarm/select",
			type:"POST",
			data:{},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code == 1) {
					$('#pulse_current_MAX').val(data.data.pulse_current_MAX);
					$('#pulse_current_MIN').val(data.data.pulse_current_MIN);
					$('#pulse_accumulation_MAX').val(data.data.pulse_accumulation_MAX);
					$('#pulse_accumulation_MIN').val(data.data.pulse_accumulation_MIN);
					$('#voltage_MAX').val(data.data.voltage_MAX);
					$('#voltage_MIN').val(data.data.voltage_MIN);
					$('#resistance_current_MAX').val(data.data.resistance_current_MAX);
					$('#resistance_current_MIN').val(data.data.resistance_current_MIN);
				}
			}
		})
		if (isroot != 1) {
			$("#alarm_pop .textbox").css({
				disabled:"true"
			});
		}
		$('#alarm_pop').fadeIn();
	})

	$('#alarm_true').click(function(){
		if ($('#pulse_current_MAX').val() < $('#pulse_current_MIN').val()){
			$('#alarmCue').html("<font color='red'><b>请输入正确脉冲电流阀值</b></font>");
			return false;
		}
		if ($('#pulse_accumulation_MAX').val() < $('#pulse_accumulation_MIN').val()){
			$('#alarmCue').html("<font color='red'><b>请输入正确累计脉冲次数阀值</b></font>")
			return false;
		}
		if ($('#voltage_MAX').val() < $('#voltage_MIN').val()){
			$('#alarmCue').html("<font color='red'><b>请输入正确的电压阀值</b></font>")
			return false;
		}
		if($('#resistance_current_MAX').val() < $('#resistance_current_MIN').val()) {
			$('#alarmCue').html("<font color='red'><b>请输入正确的阻性电流阀值</b></font>")
			return false;
		}
		$('#alarm_true').removeAttr("disabled");
		$.ajax({
			url:"/alarm/set",
			type:"POST",
			data:{"pulse_current_MAX":$('#pulse_current_MAX').val(),"pulse_current_MIN":$('#pulse_current_MIN').val(),"pulse_accumulation_MAX":$('#pulse_accumulation_MAX').val(),"pulse_accumulation_MIN":$('#pulse_accumulation_MIN').val(),"voltage_MAX":$('#voltage_MAX').val(),"voltage_MIN":$('#voltage_MIN').val(),"resistance_current_MAX":$('#resistance_current_MAX').val(),"resistance_current_MIN":$('#resistance_current_MIN').val()},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code == 1) {
					$('#alarm_pop').fadeOut();
				}else{
					$('#alarmCue').html("<font color='red'><b>"+ data.msg +"</b></font>")
				}
			}
		})
	})

	$('#alarm_false').click(function(){
		$('#alarm_pop').fadeOut();
	})


	//删除设备
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

	//管理员列表
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
						pageUserdata = data.data;
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
										pageUserdata = data.data;
										var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
										data.data.forEach(function(user){
											userdata += "<tr><td>"+ user.userId +"</td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn' id='lookUserDevice'>查看</a><a href='#' class='inner_btn' id='changeUser'>修改</a><a href='#' class='inner_btn' id='deleteUser'>删除</a></td></td></tr>";
						  				})
						  				$('#deviceTable').html(userdata);
									}
								}
							})
						}});
						var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
						data.data.forEach(function(user){
							userdata += "<tr><td>"+ user.userId +"</td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn' id='lookUserDevice'>查看</a><a href='#' class='inner_btn' id='changeUser'>修改</a><a href='#' class='inner_btn' id='deleteUser'>删除</a></td></tr>";
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

	//弹出搜索设备弹窗
	$('#searchUser').click(function(){
		$('#searchUser_pop').fadeIn();
	})

	var suc;
	$('#selectUser').click(function(){
		console.log($('#selectUser').val());
		suc = $('#selectUser').val();
	});

	var suIP;
	var suPH;
	var suNA;
	$('#searchUserInput').blur(function(){
		var suci = $(this).val();
		if (suc == 0) {
			var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
			if (suci == "" || !marry.test(suci)) {
				$('#searchUserCue').html("<font color='red'><b>请输入正确的ip地址</b></font>");
				return false;
			}else{
				$.ajax({
					url:"/device/judgeAuthrity",
					type:"POST",
					data:{"deviceIp":suci},
					datatype:"json",
					success:function(data){
						data = JSON.parse(data);
						if (data.code != 1) {
							$('#searchUserCue').html("<font color='red'><b>"+ data.msg +"</b></font>");
							return false;
						}else{
							$('#searchUserCue').html("<font color='#19a97b'><b>具有权限</b></font>");
							suIP = suci;
						}
					}
				})
			}
		};
		if (suc == 1) {
			var marry = /(^1(3|4|5|7|8)\d{9}$)/;
			if (suci.length != 11 || !(marry.test(suci))) {
				$('#searchUserCue').html("<font color='red'><b>请输入正确的11位手机号码</b></font>");
				return false;
			}
			suPH = suci;
		};
		if (suc == 2) {
			if (suci == "") {
				$('#searchUserCue').html("<font color='red'><b>请输入管理员名称</b></font>");
				return false;
			}else{
				$.ajax({
					url:"/user/judgeUserName",
					type:"POST",
					data:{"userName":suci},
					datatype:"json",
					success:function(data){
						data = JSON.parse(data);
						if (data.code != 1) {
							console.log(data.msg);
							$('#changeUserCue').html("<font color='red'><b>没有找到对应管理员</b></font>");
							return false;
						}else{
							$('#changeUserCue').html("<font color='#19a97b'><b>管理员名称正确</b></font>");
							suNA = suci;
						}
					}
				})
			}
		};

	})

	$('#searchUser_true').click(function(){

		$('#searchUser_pop').fadeOut();
		if (isroot == 1) {
    		$('#pageTool').html("");
			$.ajax({
				url:"/user/findUser",
				type:"POST",
				data:{"userName":suNA, "phoneNumber":suPH, "deviceIp":suIP},
				datatype:"json",
				beforeSend:function(){
					//加载提示图标
					$(".loading_area").fadeIn();
				},
				success:function(data){
					data = JSON.parse(data);
					if(data.code == 1){
						pageUserdata[0] = data.data;
						var userdata = "<tr><th style='width:25%;'>管理员帐号</th><th style='width:40%;'>管理员名称</th><th style='width:15%;'>联系方式</th><th style='width:20%;'>操作</th></tr>";
						var user = data.data
						userdata += "<tr><td>"+ user.userId +"</td><td>"+ user.userName +"</td><td>"+ user.phoneNumber +"</td><td><a href='#' class='inner_btn' id='lookUserDevice'>查看</a><a href='#' class='inner_btn' id='changeUser'>修改</a><a href='#' class='inner_btn' id='deleteUser'>删除</a></td></tr>";
		  				$('#deviceTable').html(userdata);
					}
					$(".loading_area").fadeOut();
				}
			})
		}else{
			alert("没有权限")
		}
	})

	//关闭搜索设备弹窗
	$('#searchUser_false').click(function(){
		$('#searchUser_pop').fadeOut();
	})

	//修改管理员
	var trNumByUser;
	$('#deviceTable').delegate("#changeUser","click",function(){
		trNumByUser = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		$('#changeUser_pop').fadeIn();
		$('#changeUserId').val(pageUserdata[trNumByUser].userId);
		$('#changeUserName1').val(pageUserdata[trNumByUser].userName);
		$('#changeUserphone').val(pageUserdata[trNumByUser].phoneNumber);
	})

	//判断电话号码是否符合要求
	$('#changeUserPhone').blur(function(){
		var addPhoneNumber = removeAllSpace($(this).val());
		var marry = /(^1(3|4|5|7|8)\d{9}$)/;
		if (addPhoneNumber.length != 11 || !(marry.test(addPhoneNumber))) {
			$('#changeUserCue').html("<font color='red'><b>请输入正确的11位手机号码</b></font>");
			$(this).css({
				border: "1px solid red"
			});
			return false;
		}
		$("#changeUserCue").html("<font color='#19a97b'><b>手机号码格式正确</b></font>");
		$(this).css({
			border: "1px solid #d2d2d2"
		});
	})

	//确认修改管理员弹框
	$('#changeUser_pop').delegate("#changeUser_true","click",function(){
		var cuid = $('#changeUserId').val();
		var cuphone = $('#changeUserphone').val();
		if (cuphone == "" || cuname == "") {
			$('#changeUserCue').html("<font color='red'><b>不能为空</b></font>");
			return false;
		}
		$.ajax({
			url:"/user/editPhoneNumber",
			type:"POST",
			data:{"userId":cuid,"new_phoneNumber":cuphone},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code == 1) {
					$('#changeUserCue').html("<font color='#19a97b'><b>修改成功</b></font>");
				}else{
					$('#changeUserCue').html("<font color='red'><b>"+data.msg+"</b></font>");
				}
			}
		})
	})

	//关闭修改管理员弹框
	$('#changeUser_pop').delegate("#changeUser_false","click",function(){
		$('changeUser_pop').fadeOut();
	})

	//删除管理员
	$('#deviceTable').delegate("#deleteUser","click",function(){
		var num = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		alert("确定删除账号为"+pageUserdata[trNumByUser].userId+"的用户");
		$.ajax({
			url:"/user/deleteUser",
			type:"POST",
			data:{"userId":pageUserdata[trNumByUser].userId},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code == 1) {
					alert("删除成功");
					$('#deviceTable tr').eq(num+1).css({
						display:"none"
					})
				}else{
					alert(data.msg)
				}
			}
		})
	})

	//查看管理员管理的设备
	$('#deviceTable').delegate("#lookUserDevice","click",function(){
		var num = $(this).parent().parent().parent().find('tr').index($(this).parent().parent()[0])-1;
		var luid = pageUserdata[num].userId;
		$('#pageTool').html("");
		$.ajax({
			url:'/device/findDevice',
			type:'POST',
			data:{"offset":1,"pageSize":pageSize, "userName":luid},
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
					$('#pageTool').Paging({pagesize:pageSize,count:count,callback:function(page,size,count){
						$.ajax({
							url:'/device/getDevice',
							type:'POST',
							data:{"offset":1,"pageSize":pageSize,"deviceIp":sdIP, "address":sdAD, "userName":sdUser},
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
										devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a><a href='#' class='inner_btn' id='changeDevice'>修改</a></td></tr>";
					  				})
					  				$('#deviceTable').html(devicedata);
								}
								if (data.code == 100011) {
	  								$(".loading_area").fadeOut();
									alert(data.msg);
								}
							}
						})
					}});
					var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:40%;'>地址</th><th style='width:15%;'>管理员</th><th style='width:20%;'>操作</th></tr>";
					data.data.forEach(function(device){
						devicedata += "<tr><td><a href='/data?deviceIp="+ device.deviceIp +"' class='inner_btn_ip'>"+device.deviceIp+"</a></td><td>"+device.address+"</td><td>"+device.userName+"</td><td><a href='#' class='inner_btn' id='searchDeviceHistory'>历史</a><a href='#' class='inner_btn' id='changeDevice'>修改</a><a href='#' class='inner_btn' id='deleteDevice'>删除</a></td></tr>";
	  				})
	  				$('#deviceTable').html(devicedata);
	  				$(".loading_area").fadeOut();
				}
				if (data.code == 100011) {
	  				$(".loading_area").fadeOut();
					alert(data.msg);
				}
			}
		})
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
		}else{
			$.ajax({
				url:"/user/judgeUserName",
				type:"POST",
				data:{"userName":newUserName},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code == -2) {
						
					} else {
						$('#changeUserCue').html("<font color='red'><b>此名称不可用，已存在</b></font>")
						return false;
					}
				}
			})
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
	

	//报表弹框
	$('#addExcel').click(function(){
		$('#getExcel_pop').fadeIn();
	})

	//匹配ip
	var getExcelDeviceIp;
	$("#getExcelDeviceIp").blur(function(){
		getExcelDeviceIp = removeAllSpace($('#getExcelDeviceIp').val());
		var marry = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		if (getExcelDeviceIp == "" || !marry.test(getExcelDeviceIp)) {
			$('#getExcelDeviceIp').css({
				border: "1px solid red"
			});
			$('#getExcelCue').html("<font color='red'><b>请输入正确的ip地址</b></font>");
			return false;
		}else{
			$.ajax({
				url:"/device/judgeAuthrity",
				type:"POST",
				data:{"deviceIp":getExcelDeviceIp},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						$('#getExcelCue').html("<font color='red'><b>"+data.msg+"</b></font>")
						return false;
					}else{
						$('#getExcelCue').html("<font color='#19a97b'><b>ip地址正确</b></font>");
						$('#getExcelDeviceIp').css({
							border: "1px solid #d2d2d2"
						});
					}
				}
			})
		}
	})

	//判断时间
	var startTime;
	var endTime;
	$('#getExcelStartTime').blur(function(){
		startTime = removeAllSpace($(this).val());
		var marry = /^20[1-2]\d(0[1-9]|1[0-2])([0-2]\d|3[0-1])?/;
		var now = getNow();
		var lastYear = getLastYear();
		if (startTime.length != 8 || !marry.test(startTime) || startTime > now || startTime < lastYear) {
			$('#getExcelStartTime').css({
				border:"1px solid red"
			});
			$('#getExcelCue').html("<font color='red'><b>请输入正确的时间格式</b></font>");
			return false;
		}
		$('#getExcelCue').html("<font color='#19a97b'><b>时间格式正确</b></font>");
		$('#getExcelStartTime').css({
			border: "1px solid #d2d2d2"
		});
	})

	$('#getExcelEndTime').blur(function(){
		endTime = removeAllSpace($(this).val());
		var marry = /^20[1-2]\d(0[1-9]|1[0-2])([0-2]\d|3[0-1])?/;
		var now = getNow();
		var lastYear = getLastYear();
		if (endTime.length != 8 || !marry.test(endTime)) {
			$('#getExcelEndTime').css({
				border:"1px solid red"
			});
			$('#getExcelCue').html("<font color='red'><b>请输入正确的时间格式</b></font>");
			return false;
		}
		if (endTime < startTime || endTime > now || endTime < lastYear) {
			$('#getExcelEndTime').css({
				border:"1px solid red"
			});
			$('#getExcelCue').html("<font color='red'><b>请输入正确的时间范围</b></font>");
			return false;
		}
		$('#getExcelCue').html("<font color='#19a97b'><b>时间正确</b></font>");
		$('#getExcelEndTime').css({
			border: "1px solid #d2d2d2"
		});
	})
	

	$('#getExcel_true').click(function(){
		if (getExcelDeviceIp == "" || endTime == "" || startTime == "") {
			$('getExcelCue').html("<font color='red'><b>请输入相应的值</b></font>");
			return false;
		} else {
			$.ajax({
				url:"/report/addAndGet",
				type:"POST",
				data:{"device_ip":getExcelDeviceIp, "startDate":startTime, "endDate":endTime},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						$('#getExcelCue').html("<font color='red'><b>"+data.msg+"</b></font>")
						return false;
					}
				}
			})
		}
	})

	$('#getExcel_add').click(function(){
		if (getExcelDeviceIp == "" || endTime == "" || startTime == "") {
			$('getExcelCue').html("<font color='red'><b>请输入相应的值</b></font>");
			return false;
		} else {
			$.ajax({
				url:"/report/addReports",
				type:"POST",
				data:{"device_ip":getExcelDeviceIp, "startDate":startTime, "endDate":endTime},
				datatype:"json",
				success:function(data){
					data = JSON.parse(data);
					if (data.code != 1) {
						$('#getExcelCue').html("<font color='red'><b>"+data.msg+"</b></font>")
						return false;
					}
				}
			})
		}
	})

	//关闭报表弹框
	$('#getExcel_false').click(function(){
		$('#getExcel_pop').fadeOut();
	})

	//生成报表弹框
	$('#getExcel').click(function(){
		$.ajax({
			url:"/report/getExcel",
			type:"POST",
			data:{"device_ip":getExcelDeviceIp, "startDate":startTime, "endDate":endTime},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code != 1) {
					$('#getExcelCue').html("<font color='red'><b>"+data.msg+"</b></font>")
					return false;
				}
			}
		})
	})
})