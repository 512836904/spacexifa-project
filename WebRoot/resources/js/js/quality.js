
var gettimes="";
$(function(){
	 var times = "";
	Coincidence1();
	workstanders();
	getCurrentDate(2);
});

// 左一
function Coincidence1(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Left1"));
  //  准备数据
  var passRate = [40, 65, 60, 58, 85, 20, 13];
  // 2. 指定配置和数据
  var option = {
    color: ["#00f2f1", "#ed3f35"],
    tooltip: {
      // 通过坐标轴来触发
      trigger: "axis",
      formatter: '{a} <br/>{b} : {c}%'
    },
    legend: {
      show: false,
      // 距离容器10%
      right: "10%",
      // 修饰图例文字的颜色
      textStyle: {
        color: "#4c9bfd"
      }
      // 如果series 里面设置了name，此时图例组件的data可以省略
      // data: ["邮件营销", "联盟广告"]
    },
    grid: {
      top: "20%",
      left: "6%",
      right: "6%",
      bottom: "0%",
      borderColor: "#012f4a",
      containLabel: true
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
      data: [
        "一厂区",
        "二厂区",
        "三厂区",
        "四厂区",
        "五厂区",
        "六厂区",
        "七厂区"
      ],
      // 去除刻度
      axisTick: {
        show: false, // 不显示刻度
        lineStyle: {
          color: 'rgb(46, 69, 118)'
        }
      },
      // 修饰刻度标签的颜色
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      // 去除x坐标轴的颜色
      axisLine: {
        show: false
      }
    },
    yAxis: {
      type: "value",
      axisLine: {
        show: false,
      },
      // 去除刻度
      axisTick: {
        show: false
      },
      // 修饰刻度标签的颜色
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        formatter: function (value) {
          return value + "%";
        }
        // fontWeight: 'bold',
      },
      // 修改y轴分割线的颜色
      splitLine: {
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      }
    },
    series: {
      name: "规范符合率",
      type: "line",
      // stack: "总量",
      // symbol: 'circle', // 默认是空心圆（中间是白色的），改成实心圆
      // showAllSymbol: true,
      // symbol: 'image://./static/images/guang-circle.png',
      symbolSize: 6,
      lineStyle: {
        opacity: 1, // 图形透明度，为 0 时不绘制该图形。
      },
      // 是否让线条圆滑显示
      smooth: true,
      areaStyle: { //区域填充样式
        normal: {
          //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: 'rgba(37, 126, 160,1)'
          },
          {
            offset: 1,
            color: 'rgba(0,0,0, 0)'
          }
          ], false),
          shadowColor: 'rgba(53,142,215, 0.9)', //阴影颜色
          shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
        }
      },
      data: passRate
    }
  };

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}


// 右一

(function () {
  // 1.基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.querySelector("#Right1"));

  //  准备数据
  var usageRate = [64, 86, 40, 73, 56, 63, 41, 46];
  // 2. 指定配置和数据
  var option = {
    color: ["#00f2f1", "#ed3f35"],
    tooltip: {
      // 通过坐标轴来触发
      trigger: "axis",
      formatter: '{a} <br/>{b} : {c}%'
    },
    legend: {
      show: false,
      // 距离容器10%
      right: "10%",
      // 修饰图例文字的颜色
      textStyle: {
        color: "#4c9bfd"
      }
      // 如果series 里面设置了name，此时图例组件的data可以省略
      // data: ["邮件营销", "联盟广告"]
    },
    grid: {
      top: "20%",
      left: "3%",
      right: "4%",
      bottom: "0%",
      borderColor: "#012f4a",
      containLabel: true
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
	  axisLabel: {//坐标轴刻度标签的相关设置。
		  color: 'rgb(92, 183, 215)',
		  formatter : function(params){
			var newParamsName = "";// 最终拼接成的字符串
			 var paramsNameNumber = params.length;// 实际标签的个数
			 var provideNumber = 10;// 每行能显示的字的个数
			 var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整
			 /**
			  * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
			  */
			 // 条件等同于rowNumber>1
			 if (paramsNameNumber > provideNumber) {
				 /** 循环每一行,p表示行 */
				 for (var p = 0; p < rowNumber; p++) {
					 var tempStr = "";// 表示每一次截取的字符串
					 var start = p * provideNumber;// 开始截取的位置
					 var end = start + provideNumber;// 结束截取的位置
					 // 此处特殊处理最后一行的索引值
					 if (p == rowNumber - 1) {
						 // 最后一次不换行
						 tempStr = params.substring(start, paramsNameNumber);
					 } else {
						 // 每一次拼接字符串并换行
						 tempStr = params.substring(start, end) + "\n";
					 }
					 newParamsName += tempStr;// 最终拼成的字符串
				 }

			 } else {
				 // 将旧标签的值赋给新标签
				 newParamsName = params;
			 }
			 //将最终的字符串返回
			 return newParamsName
		  }
      }, 
      data: [
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰",
        "A001233-1 FTG100601 基础法兰"
      ],
      // 去除刻度
      axisTick: {
        show: false,
        lineStyle: {
          color: 'rgb(46, 69, 118)'
        }
      },
      // 修饰刻度标签的颜色
      //axisLabel: {
        //color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      //},
      // 去除x坐标轴的颜色
      axisLine: {
        show: false
      }
    },
    yAxis: {
      type: "value",
      axisLine: {
        show: false,
      },
      // 去除刻度
      axisTick: {
        show: false
      },
      // 修饰刻度标签的颜色
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        formatter: function (value) {
          return value + "%";
        }
        // fontWeight: 'bold',
      },
      // 修改y轴分割线的颜色
      splitLine: {
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      }
    },
    series: {
      name: "使用率",
      type: "line",
      // stack: "总量",
      // symbol: 'circle', // 默认是空心圆（中间是白色的），改成实心圆
      showAllSymbol: true,
      // symbol: 'image://./static/images/guang-circle.png',
      symbolSize: 6,
      lineStyle: {
        opacity: 0, // 图形透明度，为 0 时不绘制该图形。
      },
      // 是否让线条圆滑显示
      // smooth: true,
      areaStyle: { //区域填充样式
        normal: {
          //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: 'rgba(43, 249, 254,0.6)'
          },
          {
            offset: 1,
            color: 'rgba(41, 77, 157,0.4)'
          }
          ], false),
          shadowColor: 'rgba(53,142,215, 0.9)', //阴影颜色
          shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
        }
      },
      data: usageRate
    },
  };

  // 重新把配置好的新数据给实例对象
  myChart.setOption(option);
  window.addEventListener("resize", function () {
    myChart.resize();
  });
})();


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
function one_3(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day6");
		div.style.display='none';
	 var d=document.getElementById("one_day7");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day7");
		d.style.display='none';
		 var div=document.getElementById("one_day6");
		div.style.display='block';
	}
}
//跳转班组
function gobutton_2(){
	 var div=document.getElementById("pchart1");
		div.style.display='none';
	 var d=document.getElementById("pchart2");
		d.style.display='block';
	var c=document.getElementById("workareas");
		c.style.display='none';
	var e=document.getElementById("teamwork");
		e.style.display='block';
	Coincidence();
	//alert("helloworld");
}
//跳转工段
function gobutton_1(){
	 var div=document.getElementById("pchart2");
		div.style.display='none';
	 var d=document.getElementById("pchart1");
		d.style.display='block';
     var c=document.getElementById("workareas");
		c.style.display='block';
	var e=document.getElementById("teamwork");
		e.style.display='none';
	Coincidence1();
	//alert("helloworld");
}
//超规范跳转工段
function gobutton_3(){
	 var div=document.getElementById("schart2");
		div.style.display='none';
	 var d=document.getElementById("schart1");
		d.style.display='block';
	var c=document.getElementById("workstandr");
		c.style.display='block';
	var e=document.getElementById("teamstander");
		e.style.display='none';
	workstanders();
	//alert("helloworld");
}
//超规范跳转班组
function gobutton_4(){
	 var div=document.getElementById("schart1");
		div.style.display='none';
	 var d=document.getElementById("schart2");
		d.style.display='block';
     var c=document.getElementById("teamstander");
		c.style.display='block';
	var e=document.getElementById("workstandr");
		e.style.display='none';
	teamstanders();
	//alert("helloworld");
}

function Coincidence(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Left2"));
  //  准备数据
  var passRate = [40, 65, 60, 58, 85, 20, 13];
  // 2. 指定配置和数据
  var option = {
    color: ["#00f2f1", "#ed3f35"],
    tooltip: {
      // 通过坐标轴来触发
      trigger: "axis",
      formatter: '{a} <br/>{b} : {c}%'
    },
    legend: {
      show: false,
      // 距离容器10%
      right: "10%",
      // 修饰图例文字的颜色
      textStyle: {
        color: "#4c9bfd"
      }
      // 如果series 里面设置了name，此时图例组件的data可以省略
      // data: ["邮件营销", "联盟广告"]
    },
    grid: {
      top: "20%",
      left: "6%",
      right: "6%",
      bottom: "0%",
      borderColor: "#012f4a",
      containLabel: true
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
      data: [
        "一电一",
        "一电二",
        "三厂区",
        "四厂区",
        "五厂区",
        "六厂区",
        "七厂区"
      ],
      // 去除刻度
      axisTick: {
        show: false, // 不显示刻度
        lineStyle: {
          color: 'rgb(46, 69, 118)'
        }
      },
      // 修饰刻度标签的颜色
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      // 去除x坐标轴的颜色
      axisLine: {
        show: false
      }
    },
    yAxis: {
      type: "value",
      axisLine: {
        show: false,
      },
      // 去除刻度
      axisTick: {
        show: false
      },
      // 修饰刻度标签的颜色
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        formatter: function (value) {
          return value + "%";
        }
        // fontWeight: 'bold',
      },
      // 修改y轴分割线的颜色
      splitLine: {
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      }
    },
    series: {
      name: "规范符合率",
      type: "line",
      // stack: "总量",
      // symbol: 'circle', // 默认是空心圆（中间是白色的），改成实心圆
      // showAllSymbol: true,
      // symbol: 'image://./static/images/guang-circle.png',
      symbolSize: 6,
      lineStyle: {
        opacity: 1, // 图形透明度，为 0 时不绘制该图形。
      },
      // 是否让线条圆滑显示
      smooth: true,
      areaStyle: { //区域填充样式
        normal: {
          //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: 'rgba(37, 126, 160,1)'
          },
          {
            offset: 1,
            color: 'rgba(0,0,0, 0)'
          }
          ], false),
          shadowColor: 'rgba(53,142,215, 0.9)', //阴影颜色
          shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
        }
      },
      data: passRate
    }
  };

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}

function workstanders(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Right2_1"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      },
    },
    
    grid: {
      top: '20%',
      left: '5%',
      right: '5%',
      bottom: '0%',
      containLabel: true,
    },
    xAxis: {
      data: ['一工区', '二工区', '三工区', '四工区', '五工区', '六工区'],
      axisLine: {
        lineStyle: {
          color: 'rgb(14, 33, 127)', // 坐标轴线颜色
        }
      },
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      nameTextStyle: {
        verticalAlign: 'top', // 文字垂直对齐方式，默认自动。可选：'top'|'middle'|'bottom'
      },
      axisTick: { // 坐标轴刻度相关设置
        show: false,
      },
    },
    yAxis: {
      // name: "焊材消耗",
      type: 'value',
      min: 0,
      interval: 200,
      nameTextStyle: {
        color: 'rgb(92, 183, 215)',
      },
      axisLine: {
        show: false,
      },
      axisLabel: {
        color: 'rgb(92, 183, 215)',
      },
      splitLine: {
        show: true,
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 分割线颜色和x轴线颜色相同
        }
      },
      axisTick: {
        show: false, //不显示刻度
      },
    },
    series: [{
      type: 'bar',
      barWidth: '30%',
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: '#ff5d1f'
          }, {
            offset: 0,
            color: '#ffc11f'
          }], false),
          barBorderRadius: [2, 2, 0, 0] //（顺时针左上，右上，右下，左下）
		  
        }

      },

      data: [600, 843, 700, 854, 500, 1000]
    }]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}
function teamstanders(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Right2_0"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      },
    },
    
    grid: {
      top: '20%',
      left: '5%',
      right: '5%',
      bottom: '0%',
      containLabel: true,
    },
	legend: {
      data: ['一班组', '二班组', '三班组', '四班组', '五班组', '六班组'],
      top: "top",
      left: "7%",
      itemWidth: 16, // 图例标记图形宽度
      itemHeight: 10,
      icon: 'rect', // 图例项的icon
      textStyle: {
        color: 'rgb(92, 183, 215)',
      }
    },
    xAxis: {
      data: ['一班组', '二班组', '三班组', '四班组', '五班组', '六班组'],
      axisLine: {
        lineStyle: {
          color: 'rgb(14, 33, 127)', // 坐标轴线颜色
        }
      },
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      nameTextStyle: {
        verticalAlign: 'top', // 文字垂直对齐方式，默认自动。可选：'top'|'middle'|'bottom'
      },
      axisTick: { // 坐标轴刻度相关设置
        show: false,
      },
    },
    yAxis: {
      // name: "焊材消耗",
      type: 'value',
      min: 0,
      interval: 200,
      nameTextStyle: {
        color: 'rgb(92, 183, 215)',
      },
      axisLine: {
        show: false,
      },
      axisLabel: {
        color: 'rgb(92, 183, 215)',
      },
      splitLine: {
        show: true,
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 分割线颜色和x轴线颜色相同
        }
      },
      axisTick: {
        show: false, //不显示刻度
      },
    },
    series: [{
      type: 'bar',
      barWidth: '30%',
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: '#ff5d1f'
          }, {
            offset: 0,
            color: '#ffc11f'
          }], false),
          barBorderRadius: [2, 2, 0, 0] //（顺时针左上，右上，右下，左下）
		  
        }

      },
      data: [{
		  value:600,
		  name:'一班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		}, 
		{
		  value:843,
		  name:'二班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		}, 
		 {
		  value:843,
		  name:'三班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		},
		{
		  value:700,
		  name:'四班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		},
		{
		  value:854,
		  name:'五班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		},
		{
		  value:500,
		  name:'六班组',
		  itemStyle:{
			  color:'#31dd3d'
		  }
		}]
    }]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}
// 右二
