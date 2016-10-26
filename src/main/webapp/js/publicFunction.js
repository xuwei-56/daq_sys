//去除空格
function removeAllSpace(str) {
	return str.replace(/\s+/g, "");
}


//得到七天时间
//设置日期，当前日期的前七天
function getSevenDay(){
	var myDate = new Date(); //获取今天日期
	myDate.setDate(myDate.getDate());
	var dateArray = []; 
	var dateTemp; 
	var flag = 1; 
	for (var i = 0; i < 7; i++) {
	    dateTemp = myDate.getFullYear().toString()+(myDate.getMonth()+1)+""+(myDate.getDate()>9?myDate.getDate().toString():'0' + myDate.getDate());
	    dateArray.push(dateTemp);
	    myDate.setDate(myDate.getDate() - flag);
	}
	return dateArray;
}

function getNow(){
	var myDate = new Date(); //获取今天日期
	myDate.setDate(myDate.getDate());
	var dateTemp; 
	dateTemp = myDate.getFullYear().toString()+(myDate.getMonth()+1)+""+(myDate.getDate()>9?myDate.getDate().toString():'0' + myDate.getDate());
	return dateTemp;
}

function getLastYear(){
	var myDate = new Date(); //获取今天日期
	myDate.setDate(myDate.getDate());
	var dateTemp; 
	dateTemp = (myDate.getFullYear()-1).toString()+(myDate.getMonth()+1)+""+(myDate.getDate()>9?myDate.getDate().toString():'0' + myDate.getDate());
	return dateTemp;
}

function getExcelIsNull(){
	$.ajax({
		url:"./report/getExcelIsNull",
		type:"POST",
		data:{},
		datatype:"json",
		success:function(data){
			data = JSON.parse(data);
			if (data.code == 1) {
				$('#getExcel').css({display:"none"})
			} 
		}
	})
}
