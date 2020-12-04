var gettimes="";
$(function(){
	 var times = "";
	Coincidence();
	getCurrentDate(2);
});

//时间
function getCurrentDate(format) {
      var now = new Date();
      var year = now.getFullYear(); //得到年份
      var month = now.getMonth();//得到月份
      var date = now.getDate();//得到日期
      var day = now.getDay();//得到周几
      var hour = now.getHours();//得到小时
      var minu = now.getMinutes();//得到分钟
      var sec = now.getSeconds();//得到秒
      month = month + 1;
      if (month < 10) month = "0" + month;
      if (date < 10) date = "0" + date;
      if (hour < 10) hour = "0" + hour;
      if (minu < 10) minu = "0" + minu;
      if (sec < 10) sec = "0" + sec;
      //var times = "";
      //精确到天
      if(format==1){
        times = year + "-" + month + "-" + date;
      }
      //精确到分
      else if(format==2){
        times = year + "-" + month + "-" + date+ " " + hour + ":" + minu + ":" + sec;
      }
      return times;
    }

function one(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day");
		div.style.display='none';
	 var d=document.getElementById("one_day1");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day1");
		d.style.display='none';
		 var div=document.getElementById("one_day");
		div.style.display='block';
	}
}
function one_1(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day2");
		div.style.display='none';
	 var d=document.getElementById("one_day3");
		d.style.display='block';
		gettimes = getCurrentDate(2);
		alert(gettimes);
	}else{
		var d=document.getElementById("one_day3");
		d.style.display='none';
		 var div=document.getElementById("one_day2");
		div.style.display='block';
		
	}
}
function one_2(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day4");
		div.style.display='none';
	 var d=document.getElementById("one_day5");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day5");
		d.style.display='none';
		 var div=document.getElementById("one_day4");
		div.style.display='block';
	}
}

//焊接规范符合率
/*
function useRatio(id){
	$.ajax({
		type : 'post',
		asyn : false,
		url : 'datastatistics/getUseRatio?parent='+id,
		dataType : 'json',
		success : function(result){
			for(var i=0;i<result.ary.length;i++){
				array1.push(result.ary[i].itemname);
				array2.push(result.ary[i].worknum);
				array3.push(result.ary[i].machinenum);
				array4.push(result.ary[i].useratio);
			}
			usechart();
		},
		error : function(errorMsg){
		      alert("数据请求失败，请联系系统管理员!");  
		}
	});
}*/
function Coincidence(){
var charts1,charts2,flag1=0,flag2=0;
var colors = ["#E6AF08", "#e8501b", "#0e6de9", "#00af6d"];
	var ctr = document.getElementById("Coincidence_rate");
	var charts1 = echarts.init(ctr);
	option = {
		xAxis: [{
				type : 'category',
				name : '工作号',
				nameTextStyle : {
					color: "#fff" //name颜色
				},
				data: ['工作号1', '工作号2', '工作号3', '工作号4', '工作号5', '工作号6', '工作号7'],
				axisLabel: {
					show: true,
					textStyle: {
						color: '#fff'
					}
				}
		}],
	    dataZoom: [{
				  type: 'slider',
				  show: true,
				  xAxisIndex: [0],
				  left: '9%',
				  bottom: -5,
				  start: 10,
				  end: 90 //初始化滚动条
		}],
		yAxis: [{
				type : 'value',
				name : '符合率%',
				 nameTextStyle : {
	            color: "#fff" //name颜色
				},
				axisLabel : {
					formatter: '{value}',
					textStyle: {
						color: '#fff'
						}
					},
				min: 0,
				max: 100,
				interval: 20,
				axisLabel:{
					//inside:true,//刻度显示在内侧
					textStyle: {
						color:'#fff'//y轴文字颜色
					}
				}
            }],
		series: [{
			name :'工作号',
			data: [10, 20, 30, 40, 20, 60, 70],
			type: 'line',
			smooth: true
		}]
	};
	//为echarts对象加载数据
	charts1.setOption(option);
	//隐藏动画加载效果
	charts1.hideLoading();

}


