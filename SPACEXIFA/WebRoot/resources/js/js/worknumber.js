$(function () {
    Coincidence();
    //延迟2秒初始化数据
    setTimeout(function(){
        loadAddSupergage(0);
        loadJobNumberInfo(0);
        loadJobSetNormRate(0);
    }, 2000);
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
    if (format == 1) {
        times = year + "-" + month + "-" + date;
    }
    //精确到分
    else if (format == 2) {
        times = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec;
    }
    return times;
}

function one(data) {
    if (data == 1) {//当日
        var div = document.getElementById("one_day");
        div.style.display = 'none';
        var d = document.getElementById("one_day1");
        d.style.display = 'block';
    } else {
        var d = document.getElementById("one_day1");
        d.style.display = 'none';
        var div = document.getElementById("one_day");
        div.style.display = 'block';
    }
    loadAddSupergage(data);
}

function one_1(data) {
    if (data == 1) {//当日
        var div = document.getElementById("one_day2");
        div.style.display = 'none';
        var d = document.getElementById("one_day3");
        d.style.display = 'block';
    } else {
        var d = document.getElementById("one_day3");
        d.style.display = 'none';
        var div = document.getElementById("one_day2");
        div.style.display = 'block';
    }
    loadJobSetNormRate(data);
}

function one_2(data) {
    if (data == 1) {//当日
        var div = document.getElementById("one_day4");
        div.style.display = 'none';
        var d = document.getElementById("one_day5");
        d.style.display = 'block';
    } else {
        var d = document.getElementById("one_day5");
        d.style.display = 'none';
        var div = document.getElementById("one_day4");
        div.style.display = 'block';
    }
    loadJobNumberInfo(data);
}

function Coincidence() {
    var charts1 = echarts.init(document.querySelector("#Coincidence_rate"));
    var option = {
        tooltip: {
            // 通过坐标轴来触发
            trigger: "axis",
            formatter: '{a} <br/>{b} : {c}%'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,//1
            name: '工作号',
            nameTextStyle: {
                color: "#fff" //name颜色
            },
            data: ['工作号1', '工作号2', '工作号3', '工作号4', '工作号5', '工作号6', '工作号7'],
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff'
                },
                color: 'rgb(92, 183, 215)',
                formatter: function (params) {
                    return params.split(" ").join("\n");
                }
            }
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            left: '9%',
            bottom: -12,
            start: 0,
            end: 100 //初始化滚动条
        }],
        yAxis: [{
            type: 'value',
            name: '符合率%',
            nameTextStyle: {
                color: "#fff" //name颜色
            },
            axisLabel: {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                }
            },
            min: 0,
            max: 100, //y轴最大刻度
            interval: 20,
            // 修改y轴分割线的颜色
            splitLine: {
                lineStyle: { // 分割线样式
                    color: 'rgb(14, 33, 127)', // 统一分割线颜色
                }
            }
        }],
        series: [{
            name: '工作号',
            data: [10, 20, 30, 40, 20, 60, 70],
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbolSize: 6
        }]
    };
    //为echarts对象加载数据
    charts1.setOption(option);
    //隐藏动画加载效果
    charts1.hideLoading();
    window.addEventListener("resize", function () {
        charts1.resize();
    });
}

//进入全屏
function requestFullScreen() {
    var de =  document.documentElement;
    if (de.requestFullscreen) {
        de.requestFullscreen();
    } else if (de.mozRequestFullScreen) {
        de.mozRequestFullScreen();
    } else if (de.webkitRequestFullScreen) {
        de.webkitRequestFullScreen();
    }
    //隐藏全屏按钮
    var quanping = document.getElementById("quanping");
    quanping.style.display="none";
}

function loadJobSetNormRate(day){
    if (day != null) {nowDatetime = day};
    //异步加载工作号/布套号焊接工艺规范符合率
    var data = [];
    var dataList = [];
    $.ajax({
        type: "post",
        async: true,
        url: "frontEnd/findJobSetNormRate",
        data: {
            startTime: nowDatetime
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                for (var i in result.ary) {
                    var name = result.ary[i].job_number +" "+ result.ary[i].set_number +" "+result.ary[i].part_name;
                    data.push(name);
                    dataList.push(result.ary[i].normRate);
                }
                var Coincidence_rate = echarts.init(document.querySelector("#Coincidence_rate"));
                var option = Coincidence_rate.getOption();
                option.xAxis[0].data = data;
                option.series[0].data = dataList;
                Coincidence_rate.setOption(option);
            }
        }
    });
}


