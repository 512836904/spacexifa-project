var websocketURL;
var redata;
var allWorkNumer;
var allWelderInfo;

$(function () {
    //焊接工艺规范符合率初始化
    Coincidence();
    //延迟2秒初始化数据
    setTimeout(function () {
        loadAddSupergage(0);
        loadJobNumberInfo(0);
        loadJobSetNormRate(0);
    }, 2000);
    //创建定时器每10分钟刷新页面
    setInterval(function(){
        loadAddSupergage(0);
        loadJobNumberInfo(0);
        loadJobSetNormRate(0);
    }, 600000);
    loadUrl();
    mqttTest();
});

function loadUrl() {
    //加载mqIP地址及端口
    $.ajax({
        type: "post",
        async: false,
        url: "td/AllTdbf",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                websocketURL = result.web_socket;
            }
        },
        error: function (errorMsg) {
            console.log("数据加载异常！" + errorMsg);
        }
    });
    //加载所有工作号及焊工信息
    $.ajax({
        type: "post",
        async: false,
        url: "frontEnd/findWorkWelderInfo",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                allWorkNumer = result.allWorkNumer;
                allWelderInfo = result.allWelderInfo;
            }
        },
        error: function (errorMsg) {
            console.log("数据加载异常！" + errorMsg);
        }
    });
}

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
    //焊接工艺规范符合率初始化
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
    var de = document.documentElement;
    if (de.requestFullscreen) {
        de.requestFullscreen();
    } else if (de.mozRequestFullScreen) {
        de.mozRequestFullScreen();
    } else if (de.webkitRequestFullScreen) {
        de.webkitRequestFullScreen();
    }
    //隐藏全屏按钮
    var quanping = document.getElementById("quanping");
    quanping.style.display = "none";
}

function loadJobSetNormRate(day) {
    if (day != null) {
        nowDatetime = day
    }
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
                    var name = result.ary[i].job_number + " " + result.ary[i].set_number + " " + result.ary[i].part_name;
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

var client, clientId;

function mqttTest() {
    var ipHost = websocketURL.split(":")[0];
    var ipPort = websocketURL.split(":")[1];
    clientId = Math.random().toString().substr(3, 8) + Date.now().toString(36);
    client = new Paho.MQTT.Client(ipHost, parseInt(ipPort), clientId);
    var options = {
        timeout: 5,
        keepAliveInterval: 60,
        cleanSession: false,
        useSSL: false,
        onSuccess: onConnect,
        onFailure: function (e) {
            console.log("mq连接失败：" + e.errorCode);
        },
        reconnect: true
    }
    client.onConnectionLost = onConnectionLost;
    client.onMessageArrived = onMessageArrived;
    client.connect(options);
}

//连接
function onConnect() {
    console.log("onConnect worknumber");
    //订阅实时数据主题
    client.subscribe("weldmesrealdata", {
        qos: 0,
        onSuccess: function (e) {
            console.log("订阅成功");
            appTableArray = [];
            appvue.$delete(appvue.tableData);
            Object.assign(appvue.$data,appvue.$options.data());
        },
        onFailure: function (e) {
            console.log("订阅失败：" + e.errorCode);
        }
    });
}

//断线重连
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

//客户端收到消息
function onMessageArrived(message) {
    redata = message.payloadString;
    if (redata.length === 405 || redata.length % 135 === 0) {
        var JOB_NUMBER = "";
        var SET_NUMBER = "";
        var PART_NAME = "";
        var junction_name = "";
        var name = "";
        var iname = "";
        for (var i = 0; i < redata.length; i += 135) {
            var welderid = redata.substring(i, 4 + i).toString(); //焊工id
            var cardid = redata.substring(99 + i, 103 + i).toString(); //电子跟踪卡id
            var ele = redata.substring(38 + i, 42 + i).toString(); //电流
            var vol = redata.substring(42 + i, 46 + i).toString(); //电压
            //工作号信息
            for (var m in allWorkNumer) {
                if (parseInt(cardid) === allWorkNumer[m].fid) {
                    JOB_NUMBER = allWorkNumer[m].JOB_NUMBER;//工作号
                    SET_NUMBER = allWorkNumer[m].SET_NUMBER;//部套号
                    PART_NAME = allWorkNumer[m].PART_NAME;//零件名
                    junction_name = allWorkNumer[m].junction_name;//焊缝名称
                }
            }
            //焊工及班组信息
            for (var n in allWelderInfo) {
                if (parseInt(welderid) === allWelderInfo[n].id) {
                    name = allWelderInfo[n].name;  //焊工姓名
                    iname = allWelderInfo[n].iname;  //班组
                }
            }
            var addOrremove = false;
            var appindex = 0;
            if (JOB_NUMBER !== '' && name !== ''){
                if (appTableArray.length > 0) {
                    for (var index in appTableArray) {
                        if (appTableArray[index].gzh != JOB_NUMBER || appTableArray[index].bth != SET_NUMBER || appTableArray[index].ljm != PART_NAME ||
                            appTableArray[index].junctionName != junction_name || appTableArray[index].welder != name || appTableArray[index].banzu != iname) {
                            addOrremove = true;
                        }
                        appindex = index;
                    }
                } else {
                    addOrremove = true;
                }
            }
            //如果不重复则可以增加新数据
            if (addOrremove) {
                let feild = {};
                feild["gzh"] = JOB_NUMBER;
                feild["bth"] = SET_NUMBER;
                feild["ljm"] = PART_NAME;
                feild["junctionName"] = junction_name;
                feild["banzu"] = iname;
                feild["welder"] = name;
                feild["electricity"] = parseInt(ele);
                feild["voltage"] = parseInt(vol);
                appTableArray.push(feild);
            } else {
                if (JOB_NUMBER !== '' && name !== ''){
                    appTableArray[appindex].electricity = parseInt(ele);
                    appTableArray[appindex].voltage = parseInt(vol);
                }
            }
        }
        appvue.tableData = appTableArray;
    }
}