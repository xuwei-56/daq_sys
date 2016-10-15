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
  }  
});  
 

$(document).ready(function(){

	var deviceIp = $.getUrlVar("deviceIp");
	var dateArray = getSevenDay();
	console.log(deviceIp+" " +dateArray[0])

	//添加active
    $(".admin_tab li a").click(function(){
	  	var liindex = $(".admin_tab li a").index(this);
	  	$(this).addClass("active").parent().siblings().find("a").removeClass("active");
  		$.ajax({
  			url:"/data/getData",
  			type:"POST",
  			data:{"device_ip":deviceIp,"data":dateArray[liindex]},
  			datatype:"json",
  			beforeSend:function(){
  				$(".loading_area").fadeIn();
  			},
  			success:function(data){
  				data = JSON.parse(data);
  				if(data.code == 1){
  					var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:25%;'>时间</th><th style='width:12.5%;'>脉冲电流（uA）</th><th style='width:12.5%;'>累计脉冲次数</th><th style='width:12.5%;'>电压（V）</th><th style='width:12.5%;'>阻性电流（mA）</th></tr>";
  					data.data.forEach(function(device){
  						devicedata += "<tr><td>"+device.device_ip+"</td><td>"+device.generate_time+"</td><td>"+device.pulse_current+"</td><td>"+device.pulse_accumulation+"</td><td>"+device.voltage+"</td><td>"+device.resistance_current+"</td>";
  	  				})
  	  				$('#deviceTable').html(devicedata);
  				}
  				//$(".loading_area").fadeOut();
  			}
  		})
	  	//$(".admin_tab_cont").eq(liindex).fadeIn(150).siblings(".admin_tab_cont").hide();
	});

	$.ajax({
		url:"/data/getData",
		type:"POST",
		data:{"device_ip":deviceIp,"data":dateArray[0]},
		datatype:"json",
		beforeSend:function(){
			$(".loading_area").fadeIn();
		},
		success:function(data){
			data = JSON.parse(data);
			if(data.code == 1){
				var devicedata = "<tr><th style='width:25%;'>IP</th><th style='width:25%;'>时间</th><th style='width:12.5%;'>脉冲电流（uA）</th><th style='width:12.5%;'>累计脉冲次数</th><th style='width:12.5%;'>电压（V）</th><th style='width:12.5%;'>阻性电流（mA）</th></tr>";
				data.data.forEach(function(device){
					devicedata += "<tr><td>"+device.device_ip+"</td><td>"+device.generate_time+"</td><td>"+device.pulse_current+"</td><td>"+device.pulse_accumulation+"</td><td>"+device.voltage+"</td><td>"+device.resistance_current+"</td>";
  				})
  				$('#deviceTable').html(devicedata);
			}
			$(".loading_area").fadeOut();
		}
	})

})