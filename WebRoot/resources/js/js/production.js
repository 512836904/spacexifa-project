var gettimes="";
$(function(){
	 var times = "";
	workgas();
	workwelder();
	workmateral();
	workradio();
	getCurrentDate(2);
});
// 左一
(function () {
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Left1"));
  // 2. 指定配置和数据
  var index = 0;
  var colorList = ['#f36c6c', '#e6cf4e', '#20d180', '#0093ff'];
  var dataList = [20, 16, 14, 11, 9, 7, 3];
  var option = {
    grid: {
      left: '1%',
      right: '5%',
      bottom: '0%',
      top: '16%',
      containLabel: true
    },

    tooltip: {
      trigger: "axis",
      formatter: '{b} : {c}',
      axisPointer: {
        type: "none",
      }
    },
	title: [{
		text: '排名',
	    left: '4%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	},{
		text: '焊工姓名',
	    left: '12%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	},{
		text: '焊材消耗量（G）',
	    left: '30%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	}],
    // backgroundColor: 'rgb(20,28,52)',
    xAxis: {
      show: false,
      type: 'value'
    },
    yAxis: [{
      type: 'category',
      inverse: true,
      splitLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      axisPointer: {
        label: {
          show: false,
        }
      },
      data: ['张三', '张飞', '李伟', '张天', '宋龙', '胖虎', '刘伟'],

      axisLabel: {
        margin: 100,
        padding: [0, 0, 0, 0],
        // fontSize: 14,
        align: 'left',
        color: '#00f2f1',
        rich: {
          num: {
            color: '#00f2f1',   // 左边数字的样式
            width: 5,
            align: 'left',
            padding: [0, 35, 0, 0]

          }
        },
        formatter: function (params) {
          if (index == 7) {
            index = 0;
          }
          index++;
          return [
            '{num|' + index + '}' + params
          ].join('\n')

        }
      }
    }, {
      type: 'category',
      inverse: true,
      axisTick: 'none',
      axisLine: 'none',
      show: true,
      axisLabel: {
        textStyle: {
          color: '#00f2f1',
          // fontSize: '12'
        },
        // formatter: function(value) {
        //     if (value >= 10000) {
        //         return (value / 10000).toLocaleString() + '万';
        //     } else {
        //         return value.toLocaleString();
        //     }
        // },
      },
      data: dataList,
    },],
    series: [{
      name: '金额',
      type: 'bar',
      barWidth: 10,
      zlevel: 1,
      itemStyle: {
        normal: {
          barBorderRadius: 30,
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [ {
            offset: 0.4,
            color: 'rgb(19,96,184,1)'
          }, {
            offset: 1,
            color: 'rgb(0,184,225,1)'
          }]),
        },
      },
      data: dataList,
    },
    {
      name: '背景',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      data: [24, 24, 24, 24, 24, 24, 24],
      itemStyle: {
        normal: {
          color: '#121e47',   //背景柱状条颜色
          barBorderRadius: 30,
        }
      },
    },
    ]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
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

//按钮
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
function one_4(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day8");
		div.style.display='none';
	 var d=document.getElementById("one_day9");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day9");
		d.style.display='none';
		 var div=document.getElementById("one_day8");
		div.style.display='block';
	}
}
function one_5(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day10");
		div.style.display='none';
	 var d=document.getElementById("one_day11");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day11");
		d.style.display='none';
		 var div=document.getElementById("one_day10");
		div.style.display='block';
	}
}
function one_6(data){
	if(data == 1){//当日
	 var div=document.getElementById("one_day12");
		div.style.display='none';
	 var d=document.getElementById("one_day13");
		d.style.display='block';
	}else{
		var d=document.getElementById("one_day13");
		d.style.display='none';
		 var div=document.getElementById("one_day12");
		div.style.display='block';
	}
}
//跳转班组
function gobutton_8(){
	 var div=document.getElementById("vchart7");
		div.style.display='none';
	 var d=document.getElementById("vchart8");
		d.style.display='block';
	var c=document.getElementById("allworkradio");
		c.style.display='none';
	var e=document.getElementById("allteamradio");
		e.style.display='block';
	teamradio();
}
//跳转工段
function gobutton_7(){
	 var div=document.getElementById("vchart8");
		div.style.display='none';
	 var d=document.getElementById("vchart7");
		d.style.display='block';
     var c=document.getElementById("allworkradio");
		c.style.display='block';
	var e=document.getElementById("allteamradio");
		e.style.display='none';
		workradio();
}
//跳转班组
function gobutton_6(){
	 var div=document.getElementById("vchart5");
		div.style.display='none';
	 var d=document.getElementById("vchart6");
		d.style.display='block';
	var c=document.getElementById("allworkmateral");
		c.style.display='none';
	var e=document.getElementById("allteammateral");
		e.style.display='block';
	teammateral();
}
//跳转工段
function gobutton_5(){
	 var div=document.getElementById("vchart6");
		div.style.display='none';
	 var d=document.getElementById("vchart5");
		d.style.display='block';
     var c=document.getElementById("allworkmateral");
		c.style.display='block';
	var e=document.getElementById("allteammateral");
		e.style.display='none';
		workmateral();
}
//跳转班组
function gobutton_4(){
	 var div=document.getElementById("vchart3");
		div.style.display='none';
	 var d=document.getElementById("vchart4");
		d.style.display='block';
	var c=document.getElementById("allworkwelder");
		c.style.display='none';
	var e=document.getElementById("allteamwelder");
		e.style.display='block';
	teamwelder();
}
//跳转工段
function gobutton_3(){
	 var div=document.getElementById("vchart4");
		div.style.display='none';
	 var d=document.getElementById("vchart3");
		d.style.display='block';
     var c=document.getElementById("allworkwelder");
		c.style.display='block';
	var e=document.getElementById("allteamwelder");
		e.style.display='none';
		workwelder();
}
//跳转班组
function gobutton_2(){
	 var div=document.getElementById("vchart1");
		div.style.display='none';
	 var d=document.getElementById("vchart2");
		d.style.display='block';
	var c=document.getElementById("allworkgas");
		c.style.display='none';
	var e=document.getElementById("allteamgas");
		e.style.display='block';
	teamgas();
}
//跳转工段
function gobutton_1(){
	 var div=document.getElementById("vchart2");
		div.style.display='none';
	 var d=document.getElementById("vchart1");
		d.style.display='block';
     var c=document.getElementById("allworkgas");
		c.style.display='block';
	var e=document.getElementById("allteamgas");
		e.style.display='none';
		workgas();
}
function teamgas(){
	  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#teamgas"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
        label: {
          show: true
        }
      }
    },
    grid: {
      top: "30%",
      left: "5%",
      right: "5%",
      bottom: "0%",
      containLabel: true
    },
    legend: {
      data: ['电能消耗量', '气体消耗量'],
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
      data: [
        '二电一', '二电二', '三电一', '四电一', '五电一'
      ],
      axisLine: {
        show: false, //隐藏X轴轴线
        // lineStyle: {
        //     color: "#3d5269",
        //     width: 1
        // }
      },
      axisTick: {
        show: false, //隐藏X轴刻度
        // alignWithLabel: true
      },
      axisLabel: {
        show: true,
        textStyle: {
          color: 'rgb(92, 183, 215)', //X轴文字颜色
          // fontWeight: 'bold'
        },
        interval: 0,
      }
    },
    yAxis: [{
      type: "value",
      name: "电能消耗(KWH)",
      nameLocation: 'end', // 坐标轴名称显示位置
      min: 0,
      max: 100,
      interval: 20,

      nameTextStyle: {
        // left: '2%',
        color: 'rgb(92, 183, 215)',
      },
      splitLine: {
        show: true,
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      axisLabel: {
        show: true,
        textStyle: {
          color: 'rgb(92, 183, 215)',
        }
      }
    },
    {
      type: "value",
      name: "气体消耗(L)",
      min: 0,
      max: 2000,
      interval: 400,
      nameTextStyle: {
        color: 'rgb(92, 183, 215)',
      },
      position: "right",
      splitLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "#396A87",
          width: 2
        }
      },
      axisLabel: {
        show: true,
        formatter: "{value}", //右侧Y轴文字显示
        textStyle: {
          color: 'rgb(92, 183, 215)',
        }
      }
    }
    ],
    series: [{
      name: "电能消耗量",
      type: "bar",
      yAxisIndex: 0,
      barWidth: 18,
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: "#ff5d1f"
          },
          {
            offset: 0,
            color: "#ffc11f"
          }
          ])
        }
      },
      data: [85, 45, 15, 85.2, 78,]
    },
    {
      name: "气体消耗量",
      type: "bar",
      yAxisIndex: 1,
      barWidth: 18,
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: "#058bf0"
          },
          {
            offset: 0,
            color: "#27d7e8"
          }
          ])
        }
      },
      data: [400.07, 856.78, 2632.57, 1800, 2235.91,]
    },

    ]
  }

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}
function workgas(){
	  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#workgas"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
        label: {
          show: true
        }
      }
    },
    grid: {
      top: "30%",
      left: "5%",
      right: "5%",
      bottom: "0%",
      containLabel: true
    },
    legend: {
      data: ['电能消耗量', '气体消耗量'],
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
      data: [
        '1月', '2月', '3月', '4月', '5月'
      ],
      axisLine: {
        show: false, //隐藏X轴轴线
        // lineStyle: {
        //     color: "#3d5269",
        //     width: 1
        // }
      },
      axisTick: {
        show: false, //隐藏X轴刻度
        // alignWithLabel: true
      },
      axisLabel: {
        show: true,
        textStyle: {
          color: 'rgb(92, 183, 215)', //X轴文字颜色
          // fontWeight: 'bold'
        },
        interval: 0,
      }
    },
    yAxis: [{
      type: "value",
      name: "电能消耗(KWH)",
      nameLocation: 'end', // 坐标轴名称显示位置
      min: 0,
      max: 100,
      interval: 20,

      nameTextStyle: {
        // left: '2%',
        color: 'rgb(92, 183, 215)',
      },
      splitLine: {
        show: true,
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      axisLabel: {
        show: true,
        textStyle: {
          color: 'rgb(92, 183, 215)',
        }
      }
    },
    {
      type: "value",
      name: "气体消耗(L)",
      min: 0,
      max: 2000,
      interval: 400,
      nameTextStyle: {
        color: 'rgb(92, 183, 215)',
      },
      position: "right",
      splitLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "#396A87",
          width: 2
        }
      },
      axisLabel: {
        show: true,
        formatter: "{value}", //右侧Y轴文字显示
        textStyle: {
          color: 'rgb(92, 183, 215)',
        }
      }
    }
    ],
    series: [{
      name: "电能消耗量",
      type: "bar",
      yAxisIndex: 0,
      barWidth: 18,
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: "#ff5d1f"
          },
          {
            offset: 0,
            color: "#ffc11f"
          }
          ])
        }
      },
      data: [25, 45, 55, 25.2, 58,]
    },
    {
      name: "气体消耗量",
      type: "bar",
      yAxisIndex: 1,
      barWidth: 18,
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 1,
            color: "#058bf0"
          },
          {
            offset: 0,
            color: "#27d7e8"
          }
          ])
        }
      },
      data: [835.07, 1256.78, 1632.57, 800, 1235.91,]
    },

    ]
  }

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}
//工区焊工焊接时长
function workwelder(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#workwelders"));
  // 2. 指定配置和数据
  const data = [13, 16, 13, 11, 10, 8.5];
  const sideData = data.map(item => item + 0.53);

  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      }
    },
    grid: {
      top: '18%',
      left: '6%',
      right: '6%',
      bottom: '0%',
      containLabel: true,
    },
    xAxis: {
      data: ['一工区', '二工区', '三工区', '四工区', '五工区', '六工区'],
      //坐标轴
      axisLine: {
        show: false,
        // lineStyle: {
        //     color: '#3eb2e8'
        // }
      },
      //坐标值标注
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold'
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      // max: 18,
      interval: 2,
      //坐标轴
      axisLine: {
        show: false
      },
      //坐标值标注
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      //分格线
      splitLine: {
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      }
    },
    series: [{
      name: 'a',
      type: 'bar',
      barWidth: 20,  // 柱子正面边宽度
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
            offset: 0,
            color: "rgb(77, 90, 0)" // 0% 处的颜色
          }, {
            offset: 0.6,
            color: "rgb(153, 178, 0)" // 60% 处的颜色
          }, {
            offset: 1,
            color: "rgb(217, 252, 1)" // 100% 处的颜色
          }], false)
        }
      },
      data: data,
      barGap: 0
    }, {
      type: 'bar',
      barWidth: 6,  // 柱子侧边的宽度
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
            offset: 0,
            color: "rgb(77, 90, 0)" // 0% 处的颜色
          }, {
            offset: 0.6,
            color: "rgb(153, 178, 0)" // 60% 处的颜色
          }, {
            offset: 1,
            color: "rgb(217, 252, 1)" // 100% 处的颜色
          }], false)
        }
      },
      barGap: 0,
      data: sideData
    }, {
      name: 'b',
      tooltip: {
        show: false
      },
      type: 'pictorialBar',
      itemStyle: {
        borderWidth: 0.9,
        borderColor: 'rgb(137, 153, 37)',
        color: 'rgb(212, 247, 1)',
      },
      symbol: 'path://M 0,0 l 120,0 l -30,60 l -120,0 z',
      symbolSize: ['25', '10'],
      symbolOffset: ['0', '-10'],
      symbolRotate: -3,
      symbolPosition: 'end',
      data: data,
      z: 3
    }]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}

//班组焊工
function teamwelder(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#teamwelder"));
  // 2. 指定配置和数据
  const data = [13, 16, 13, 11, 10, 8.5];
  const sideData = data.map(item => item + 0.53);

  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      }
    },
    grid: {
      top: '18%',
      left: '6%',
      right: '6%',
      bottom: '0%',
      containLabel: true,
    },
    xAxis: {
      data: ['一电一', '二电一', '三电一', '四电一', '五电一', '六电一'],
      //坐标轴
      axisLine: {
        show: false,
        // lineStyle: {
        //     color: '#3eb2e8'
        // }
      },
      //坐标值标注
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold'
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      // max: 18,
      interval: 2,
      //坐标轴
      axisLine: {
        show: false
      },
      //坐标值标注
      axisLabel: {
        color: 'rgb(92, 183, 215)',
        // fontWeight: 'bold',
      },
      //分格线
      splitLine: {
        lineStyle: { // 分割线样式
          color: 'rgb(14, 33, 127)', // 统一分割线颜色
        }
      }
    },
    series: [{
      name: 'a',
      type: 'bar',
      barWidth: 20,  // 柱子正面边宽度
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
            offset: 0,
            color: "rgb(77, 90, 0)" // 0% 处的颜色
          }, {
            offset: 0.6,
            color: "rgb(153, 178, 0)" // 60% 处的颜色
          }, {
            offset: 1,
            color: "rgb(217, 252, 1)" // 100% 处的颜色
          }], false)
        }
      },
      data: data,
      barGap: 0
    }, {
      type: 'bar',
      barWidth: 6,  // 柱子侧边的宽度
      itemStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
            offset: 0,
            color: "rgb(77, 90, 0)" // 0% 处的颜色
          }, {
            offset: 0.6,
            color: "rgb(153, 178, 0)" // 60% 处的颜色
          }, {
            offset: 1,
            color: "rgb(217, 252, 1)" // 100% 处的颜色
          }], false)
        }
      },
      barGap: 0,
      data: sideData
    }, {
      name: 'b',
      tooltip: {
        show: false
      },
      type: 'pictorialBar',
      itemStyle: {
        borderWidth: 0.9,
        borderColor: 'rgb(137, 153, 37)',
        color: 'rgb(212, 247, 1)',
      },
      symbol: 'path://M 0,0 l 120,0 l -30,60 l -120,0 z',
      symbolSize: ['25', '10'],
      symbolOffset: ['0', '-10'],
      symbolRotate: -3,
      symbolPosition: 'end',
      data: data,
      z: 3
    }]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}

function teammateral(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#teammaterals"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      },
    },
    title: {
      text: '焊材消耗',
      left: '1%',
      /* y轴取四位数时，取grid的left-1% */
      textStyle: {
        //文字颜色
        color: 'rgb(92, 183, 215)',
        //字体风格,'normal','italic','oblique'
        fontStyle: 'normal',
        //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
        fontWeight: 'bold',
        //字体系列
        fontFamily: 'sans-serif',
        //字体大小
        fontSize: 12
      }
    },
    grid: {
      top: '20%',
      left: '5%',
      right: '5%',
      bottom: '0%',
      containLabel: true,
    },
    xAxis: {
      data: ['一电一', '一电二', '二电一', '三电一', '五电一', '六电一'],
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
            offset: 0,
            color: '#5ef3ff'
          }, {
            offset: 1,
            color: '#06a4f4'
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

function workmateral(){
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#workmaterals"));
  // 2. 指定配置和数据
  var option = {
    tooltip: {
      trigger: 'axis',
      formatter: "{b} : {c}",
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      },
    },
    title: {
      text: '焊材消耗',
      left: '1%',
      /* y轴取四位数时，取grid的left-1% */
      textStyle: {
        //文字颜色
        color: 'rgb(92, 183, 215)',
        //字体风格,'normal','italic','oblique'
        fontStyle: 'normal',
        //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
        fontWeight: 'bold',
        //字体系列
        fontFamily: 'sans-serif',
        //字体大小
        fontSize: 12
      }
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
            offset: 0,
            color: '#5ef3ff'
          }, {
            offset: 1,
            color: '#06a4f4'
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

// 左二
(function () {
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector("#Left2"));
  // 2. 指定配置和数据
  var index = 0;
  var colorList = ['#f36c6c', '#e6cf4e', '#20d180', '#0093ff'];
  var dataList = [20, 16, 14, 11, 9, 7, 3];
  var option = {
    grid: {
      left: '1%',
      right: '5%',
      bottom: '0%',
      top: '16%',
      containLabel: true
    },

    tooltip: {
      trigger: "axis",
      formatter: '{b} : {c}',
      axisPointer: {
        type: "none",
      }
    },
	title: [{
		text: '排名',
	    left: '4%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	},{
		text: '焊工姓名',
	    left: '12%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	},{
		text: '工作时长',
	    left: '30%',
	    //y轴取四位数时，取grid的left-1%
	    textStyle: {
		  //文字颜色
		  color: 'rgb(92, 183, 215)',
		  //字体风格,'normal','italic','oblique'
		  fontStyle: 'normal',
		  //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
		  fontWeight: 'bold',
		  //字体系列
		  fontFamily: 'sans-serif',
		  //字体大小
		  fontSize: 12
	    }
	}],
    // backgroundColor: 'rgb(20,28,52)',
    xAxis: {
      show: false,
      type: 'value'
    },
    yAxis: [{
      type: 'category',
      inverse: true,
      splitLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      axisPointer: {
        label: {
          show: false,
        }
      },
      data: ['张三', '张飞', '李伟', '张天', '宋龙', '胖虎', '刘伟'],

      axisLabel: {
        margin: 100,
        padding: [0, 0, 0, 0],
        // fontSize: 14,
        align: 'left',
        color: '#00f2f1',
        rich: {
          num: {
            color: '#00f2f1',   // 左边数字的样式
            width: 5,
            align: 'left',
            padding: [0, 35, 0, 0]

          }
        },
        formatter: function (params) {
          if (index == 7) {
            index = 0;
          }
          index++;
          return [
            '{num|' + index + '}' + params
          ].join('\n')

        }
      }
    }, {
      type: 'category',
      inverse: true,
      axisTick: 'none',
      axisLine: 'none',
      show: true,
      axisLabel: {
        textStyle: {
          color: '#00f2f1',
          // fontSize: '12'
        },
        // formatter: function(value) {
        //     if (value >= 10000) {
        //         return (value / 10000).toLocaleString() + '万';
        //     } else {
        //         return value.toLocaleString();
        //     }
        // },
      },
      data: dataList,
    },],
    series: [{
      name: '金额',
      type: 'bar',
      barWidth: 10,
      zlevel: 1,
      itemStyle: {
        normal: {
          barBorderRadius: 30,
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
            offset: 0,
            color: 'rgb(1,249,35,1)'
          }, {
            offset: 0.4,
            color: 'rgb(0,184,225,1)'
          }, {
            offset: 1,
            color: 'rgb(19,96,184,1)'
          }]),
        },
      },
      data: dataList,
    },
    {
      name: '背景',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      data: [24, 24, 24, 24, 24, 24, 24],
      itemStyle: {
        normal: {
          color: '#121e47',   //背景柱状条颜色
          barBorderRadius: 30,
        }
      },
    },
    ]
  };
  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
})();


// 左下



// 中一
(function () {
  // 1实例化对象
  var myChart = echarts.init(document.querySelector("#Middle1"));
  // 2. 指定配置项和数据
  var option = {
    // backgroundColor:"#0B1837",
    color: ["#E6AF08", "#e8501b", "#0e6de9", "#00af6d"],
    title: [
      {
        text: '设备状态',
        left: '25%',
        top: '12%',
        textAlign: 'center',
        textStyle: {
          fontWeight: 'normal',
          fontSize: '14',
          color: '#fff',
          textAlign: 'center',
        },
      }, {
        text: '焊工状态',
        left: '75%',
        top: '12%',
        textAlign: 'center',
        textStyle: {
          color: '#fff',
          fontWeight: 'normal',
          fontSize: '14',
          textAlign: 'center',
        },
      }],
    // grid: {
    //     left: "0%",
    //     top: "0%",
    //     bottom: "0%",
    //     right: "0%",
    //     containLabel: true
    // },
    tooltip: {
      trigger: 'item',
      formatter: "{b} : {c} ({d}%)"
    },

    calculable: true,

    series: [{
      type: 'pie',
      radius: ["16%", "17%"],
      center: ['25%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#0B4A6B"
          }
        }
      }]
    }, {
      type: 'pie',
      radius: ["0%", "13%"],
      center: ['25%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
          length: 30,
          length2: 55
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#3A496D"
          }
        }
      }]
    }, {
      type: 'pie',
      radius: ["60%", "61%"],
      center: ['25%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
          length: 30,
          length2: 55
        },
        emphasis: {
          show: false
        }
      },
      name: "",
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#0B4A6B"
          }
        }
      }]
    }, {
      stack: 'a',
      type: 'pie',
      radius: ['20%', '50%'],
      center: ['25%', '65%'],
      roseType: 'area', // radius
      zlevel: 10,
      itemStyle: {
        normal: {
          // borderColor: '#373454',
          // borderWidth: '5',
        },
      },
      label: {
        normal: {
          show: true,
          formatter: function (params) {
            return params.value + '\n' + params.name
          },
          textStyle: {
            // fontSize: 28,
          },
          position: 'outside'
        },
        emphasis: {
          show: true
        }
      },
      labelLine: {
        normal: {
          show: true,
          length: 5,
          length2: 15,
          lineStyle: {
            width: 1
          }
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        value: 30,
        name: '工作'
      },
      {
        value: 21,
        name: '故障'
      },
      {
        value: 40,
        name: '关机'
      },
      {
        value: 25,
        name: '待机'
      }
      ]
    },
    {
      type: 'pie',
      radius: ["16%", "17%"],
      center: ['75%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
          length: 30,
          length2: 55
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#0B4A6B"
          }
        }
      }]
    }, {
      type: 'pie',
      radius: ["0%", "13%"],
      center: ['75%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
          length: 30,
          length2: 55
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#3A496D"
          }
        }
      }]
    }, {
      type: 'pie',
      radius: ["60%", "61%"],
      center: ['75%', '65%'],
      hoverAnimation: false,
      labelLine: {
        normal: {
          show: false,
          length: 30,
          length2: 55
        },
        emphasis: {
          show: false
        }
      },
      name: "",
      data: [{
        name: '',
        value: 0,
        itemStyle: {
          normal: {
            color: "#0B4A6B"
          }
        }
      }]
    }, {
      stack: 'a',
      type: 'pie',
      radius: ['20%', '50%'],
      center: ['75%', '65%'],
      roseType: 'area',
      zlevel: 10,
      label: {
        normal: {
          show: true,
          formatter: function (params) {
            return params.value + '\n' + params.name
          },
          textStyle: {
            // fontSize: 28,
          },
          position: 'outside'
        },
        emphasis: {
          show: true
        }
      },
      labelLine: {
        normal: {
          show: true,
          length: 5,
          length2: 15,
          lineStyle: {
            width: 1
          }
        },
        emphasis: {
          show: false
        }
      },
      data: [{
        value: 30,
        name: '夜班'
      },
      {
        value: 21,
        name: '请假'
      },
      {
        value: 40,
        name: '在线'
      },
      {
        value: 25,
        name: '公出'
      }
      ]
    },
    ]
  }

  // 3. 把配置项给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function () {
    myChart.resize();
  });
})();
//工区
function workradio(){
  // 1.基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.querySelector("#workradios"));

  //  准备数据
  var usageRate = [64, 86, 40, 73, 56, 63, 41, 46, 90, 63, 81, 52];
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
      bottom: "3%",
      borderColor: "#012f4a",
      containLabel: true
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
      data: [
        "一工区",
        "二工区",
        "三工区",
        "四工区",
        "五工区",
        "六工区",
        "七工区"
      ],
      // 去除刻度
      axisTick: {
        show: false,
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
}
//班组
function teamradio(){
  // 1.基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.querySelector("#teamradios"));

  //  准备数据
  var usageRate = [64, 86, 40, 73, 56, 63, 41, 46, 90, 63, 81, 52];
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
      bottom: "3%",
      borderColor: "#012f4a",
      containLabel: true
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
      data: ['一电一', '一电二', '二电一', '三电一', '五电一', '六电一'],
      // 去除刻度
      axisTick: {
        show: false,
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
}
// 右二



