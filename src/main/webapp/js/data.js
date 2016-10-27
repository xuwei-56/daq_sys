$.extend({  
  getUrlVars: function(){  
    var vars = [], hash;  
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');  
    for(var i = 0; i < hashes.length; i++)  
    {  
      hash = hashes[i].split('=');  
      vars.push(hash[0]);  
      vars[hash[0]] = hash[1];  
    }  
    return vars;  
  },  
  getUrlVar: function(name){  
    return $.getUrlVars()[name];  
  },
  /**
   * 当前时间戳
   * @return <int>    unix时间戳(秒) 
   */
  CurTime: function(){
    return Date.parse(new Date())/1000;
  },
  /**       
   * 日期 转换为 Unix时间戳
   * @param <string> 2014-01-01 20:20:20 日期格式       
   * @return <int>    unix时间戳(秒)       
   */
  DateToUnix: function(string) {
    var f = string.split(' ', 2);
    var d = (f[0] ? f[0] : '').split('-', 3);
    var t = (f[1] ? f[1] : '').split(':', 3);
    return (new Date(
        parseInt(d[0], 10) || null,
        (parseInt(d[1], 10) || 1) - 1,
        parseInt(d[2], 10) || null,
        parseInt(t[0], 10) || null,
        parseInt(t[1], 10) || null,
        parseInt(t[2], 10) || null
        )).getTime() / 1000;
  },
  /**       
   * 时间戳转换日期       
   * @param <int> unixTime  待时间戳(秒)            
   * @param <int> timeZone  时区       
   */
  UnixToDate: function(unixTime) {//由于东八区 所以加8小时
    var time = new Date(unixTime + 8 * 60 * 60 * 1000);
    var ymdhis = "";
    ymdhis += time.getUTCFullYear() + "-";
    ymdhis += (time.getUTCMonth()+1) + "-";
    ymdhis += time.getUTCDate();
    ymdhis += " " + time.getUTCHours() + ":";
  	ymdhis += time.getUTCMinutes() + ":";
  	ymdhis += time.getUTCSeconds();
    return ymdhis;
  }
});  



$(document).ready(function(){

	var deviceIp = $.getUrlVar("deviceIp");
	var historytime = $.getUrlVar("historytime");
	if (historytime == null || historytime == "") {
		console.log(historytime);
		var dateArray = getSevenDay();
		historytime = dateArray[0];
		var dateArrayHtml = "<li><a class='active'>"+ dateArray[0] +"</a></li>";
		for (var i = 1; i < dateArray.length; i++) {
			dateArrayHtml += "<li><a>"+ dateArray[i] +"</a></li>";
		}
	}else{
		var dateArrayHtml = "<li><a class='active'>"+ historytime +"</a></li>";
	}
	$('.admin_tab').html(dateArrayHtml);
	
	

	//添加active
	var liindex;
    $(".admin_tab li a").click(function(){
	  	liindex = $(".admin_tab li a").index(this);
	  	$(this).addClass("active").parent().siblings().find("a").removeClass("active");
	  	historytime = dateArray[liindex];
  		$.ajax({
  			url:"./data/getData",
  			type:"POST",
  			data:{"device_ip":deviceIp,"date":dateArray[liindex]},
  			datatype:"json",
  			beforeSend:function(){
  				$(".loading_area").fadeIn();
  			},
  			success:function(data){
  				data = JSON.parse(data);
  				if(data.code == 1){
  					var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:25%;'>时间</th><th style='width:12.5%;'>脉冲电流（uA）</th><th style='width:12.5%;'>累计脉冲次数</th><th style='width:12.5%;'>电压（V）</th><th style='width:12.5%;'>阻性电流（mA）</th></tr>";
  					data.data.forEach(function(device){
  						devicedata += "<tr><td>"+device.device_ip+"</td><td>"+$.UnixToDate(device.generate_time)+"</td><td>"+device.pulse_current+"</td><td>"+device.pulse_accumulation+"</td><td>"+device.voltage+"</td><td>"+device.resistance_current+"</td>";
  	  				})
  	  				$('#deviceTable').html(devicedata);
  				}
  				$(".loading_area").fadeOut();
  			}
  		})
  		//判断当前数据是否在报表
		$.ajax({
			url:"./report/isInReport",
			type:"POST",
			data:{"device_ip":deviceIp,"date":dateArray[liindex]},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if(data.code == -2){
					$('#addProjectOne').css({ display:"none" })
				}
			}
		})
	});

    //得到当前数据
	$.ajax({
		url:"./data/getData",
		type:"POST",
		data:{"device_ip":deviceIp,"date":historytime},
		datatype:"json",
		beforeSend:function(){
			$(".loading_area").fadeIn();
		},
		success:function(data){
			data = JSON.parse(data);
			if(data.code == 1){
				var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:25%;'>时间</th><th style='width:12.5%;'>脉冲电流（uA）</th><th style='width:12.5%;'>累计脉冲次数</th><th style='width:12.5%;'>电压（V）</th><th style='width:12.5%;'>阻性电流（mA）</th></tr>";
				data.data.forEach(function(device){
					devicedata += "<tr><td>"+device.device_ip+"</td><td>"+$.UnixToDate(device.generate_time)+"</td><td>"+device.pulse_current+"</td><td>"+device.pulse_accumulation+"</td><td>"+device.voltage+"</td><td>"+device.resistance_current+"</td>";
  				})
  				$('#deviceTable').html(devicedata);
			}
			$(".loading_area").fadeOut();
		}
	})

	//判断当前数据是否在报表
	$.ajax({
		url:"./report/isInReport",
		type:"POST",
		data:{"device_ip":deviceIp,"date":historytime},
		datatype:"json",
		success:function(data){
			data = JSON.parse(data);
			if(data.code == -2){
				$('#addProjectOne').css({ display:"none" })
			}
		}
	})

	//将当前数据添加至报表
	$('#addProjectOne').click(function(){
		$.ajax({
			url:"./report/addReport",
			type:"POST",
			data:{"device_ip":deviceIp, "date":historytime},
			datatype:"json",
			success:function(data){
				data = JSON.parse(data);
				if (data.code == 1) {
					alert(data.msg)
				} else {
					alert(data.msg);
					return false;
				}
			}
		})
	})

	//仅生成当前数据报表
	$('#getProjectOne').click(function(){
		window.open("./report/getExcelA?device_ip="+deviceIp+"&date="+historytime);
	})

})