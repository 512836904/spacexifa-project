var insfid;
var charts;
var websocketURL = null;
var dic, starows, redata, symbol = 0, welderName, socket;
var worknum = 0, standbynum = 0, warnnum = 0, offnum = 0, flag = 0, showflag = 0;
var liveary = new Array(), machine = new Array;
var subscribeTopic = new Array();
var weldersArray = new Array();
var taskNum;
var whiteListVersion;   //白名单版本

$(function () {
    $.ajax({
        type: "post",
        async: false,
        url: "weldedjunction/getWeldTask",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                taskNum = eval(result.rows);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: "pmt/getParameterAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            var res = eval(result.rows);
            for (var i = 0; i < res.length; i++) {
                whiteListVersion = res[i].numversion;
                break;
            }
        }
    });
    //查询所有焊工信息(过滤离职焊工)
    $.ajax({
        type: "post",
        async: false,
        url: "welders/findAllWelders",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                weldersArray = result.ary;
            }
        },
        error: function (errorMsg) {
            console.log("查询所有焊工信息异常！");
        }
    });
    loadtree();
    websocketUrl();
    mqttTest();
    //状态发生改变
    $("#status").combobox({
        onChange: function (newValue, oldValue) {
            statusClick(newValue);
        }
    });
})

function loadtree() {
    $("#myTree").tree({
        url: 'insframework/getConmpany', //请求路径
        onLoadSuccess: function (node, data) {
            var tree = $(this);
            if (data) {
                $(data).each(function (index, d) {
                    if (this.state == 'closed') {
                        tree.tree('expandAll');
                    }
                });
            }
            if (data.length > 0) {
                //找到第一个元素
                var nownodes = $('#myTree').tree('find', data[0].id);
                insfid = nownodes.id;
                $('#myTree').tree('select', nownodes.target);
                getMachine(insfid);
                //初始化
                showChart();
            }

        },
        //树形菜单点击事件,获取项目部id，默认选择当前组织机构下的第一个
        onClick: function (node) {
            showflag = 0;
            document.getElementById("load").style.display = "block";
            var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
            $("#bodydiv").append(sh);
            document.getElementById("show").style.display = "block";
            var nownodes = $('#myTree').tree('find', node.id);
            insfid = nownodes.id;
            $("#curve").html("");
            $("#standby").html(0);
            $("#work").html(0);
            $("#off").html(0);
            $("#overproof").html(0);
            $("#overtime").html(0);
            getMachine(insfid);
            /*var defaultnode = $('#myTree').tree('find', insfid);*/
            $('#itemname').html(nownodes.text);
            flag = 0;
            //清空实时数组
            liveary.length = 0;
            setTimeout(function () {
                if (symbol != 1) {
                    /*alert("未接收到数据!!!");*/
                    document.getElementById("load").style.display = 'none';
                    document.getElementById("show").style.display = 'none';
                }
            }, 10000);
        }
    });
}

function websocketUrl() {
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
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

//获取焊机及焊工信息
function getMachine(insfid) {
    var url, welderurl;
    if (insfid == "" || insfid == null) {
        url = "td/getLiveMachine";
        welderurl = "td/getLiveWelder";
    } else {
        url = "td/getLiveMachine?parent=" + insfid;
        welderurl = "td/getLiveWelder?parent=" + insfid;
    }
    $.ajax({
        type: "post",
        async: false,
        url: url,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                machine = eval(result.rows);
                $("#machinenum").html(machine.length);
                $("#off").html(machine.length);
//				showChart();
                $("#curve").html();
                for (var i = 0; i < machine.length; i++) {
                    subscribeTopic.push(machine[i].fequipment_no);
                    var imgnum = machine[i].model;
                    // console.log("imgnum:"+imgnum);
                    var str = '<div id="machine' + machine[i].fid + '" style="width:250px;height:120px;float:left;margin-right:10px;display:none">' +
                        '<div style="float:left;width:40%;height:100%;">' +
                        '<a href="td/goNextcurve?value=' + machine[i].fgather_no + '&valuename=' + machine[i].fequipment_no + '&type=' + machine[i].model + '">' +
                        '<img id="img' + machine[i].fid + '" src="resources/images/welder_' + imgnum + '3.png" style="height:110px;width:90%;padding-top:10px;"></a>' +
                        '</div>' +
                        '<div style="float:left;width:60%;height:100%;">' +
                        '<ul><li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">设备编号：<span id="m1' + machine[i].fid + '">' + machine[i].fequipment_no + '</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">任务状态：<span id="m2' + machine[i].fid + '">--</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">操作人员：<span id="m3' + machine[i].fid + '">--</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">焊接电流：<span id="m4' + machine[i].fid + '">--A</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">焊接电压：<span id="m5' + machine[i].fid + '">--V</span></li>' +
                        '<li style="width:100%;height:19px;">焊机状态：<span id="m6' + machine[i].fid + '">关机</span></li></ul>' +
                        '<input id="status' + machine[i].fid + '" type="hidden" value="3"></div></div>';
                    $("#curve").append(str);
                    var statusnum = $("#status").combobox('getValue');
                    if (showflag == 0 && (statusnum == 99 || statusnum == 3)) {
                        $("#machine" + machine[i].fid).show();
                    }
                }
                showflag = 1;
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });

    //获取焊工信息
    $.ajax({
        type: "post",
        async: false,
        url: welderurl,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                welderName = eval(result.rows);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

var client, clientId;

function mqttTest() {
    var ipHost = websocketURL.split(":")[0];
    //ipHost = ipHost.replace(/\"/g, "");
    var ipPort = websocketURL.split(":")[1];
    //ipPort = ipPort.replace(/\"/g, "")
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

    //set callback handlers
    client.onConnectionLost = onConnectionLost;
    client.onMessageArrived = onMessageArrived;

    //connect the client
    client.connect(options);
}

//called when the client connects
function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect newCurve");
//	client.publish('/public/TEST/SHTH', 'SHTHCS', 0, false);
    client.subscribe("weldmesrealdata", {
        qos: 0,
        onSuccess: function (e) {
            console.log("订阅成功");
            var loadingMask = document.getElementById('loadingDiv');
            loadingMask.parentNode.removeChild(loadingMask);
        },
        onFailure: function (e) {
            console.log("订阅失败：" + e.errorCode);
            var loadingMask = document.getElementById('loadingDiv');
            loadingMask.parentNode.removeChild(loadingMask);
        }
    })
}

//called when the client loses its connection
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

//called when a message arrives
function onMessageArrived(message) {
//	console.log("onMessageArrived:"+message.payloadString);
    redata = message.payloadString;
    if (redata == null || redata == "" || showflag == 0) {
        for (var i = 0; i < machine.length; i++) {
            $("#machine" + machine[i].fid).show();
        }
        showflag = 1;
    }
    iview();
    symbol++;
    /*	message = new Paho.MQTT.Message("1");
        message.destinationName = "api2";
        client.send(message);*/
}

function iview() {
    if (flag == 0) {
        if (machine != null) {
            document.getElementById("load").style.display = "block";
            var sh = '<div id="show" align="center" style="text-align: center;"><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
            $("#bodydiv").append(sh);
            document.getElementById("show").style.display = "block";
        }
        window.setTimeout(function () {
            tempary = liveary.concat([]);
            worknum = 0, standbynum = 0, warnnum = 0, offnum = machine.length - tempary.length;
            //默认显示所有
            for (var i = 0; i < machine.length; i++) {
                for (var j = 0; j < tempary.length; j++) {
                    if (tempary[j].fid == machine[i].fid) {
                        $("#m4" + machine[i].fid).html(tempary[j].liveele);
                        $("#m5" + machine[i].fid).html(tempary[j].livevol);
                        $("#m6" + machine[i].fid).html(tempary[j].livestatus);
                        $("#status" + machine[i].fid).val(tempary[j].livestatusid);
                        $("#img" + machine[i].fid).attr("src", tempary[j].liveimg);
                    }
                }
                $("#machine" + machine[i].fid).show();
            }
            for (var j = 0; j < tempary.length; j++) {
                var status = $("#status" + tempary[j].fid).val();
                if (status == 0) {
                    worknum += 1;
                } else if (status == 1) {
                    standbynum += 1;
                } else if (status == 2) {
                    warnnum += 1;
                } else if (status == 3) {
                    offnum += 1;
                }
            }
            $("#work").html(worknum);
            $("#standby").html(standbynum);
            $("#warn").html(warnnum);
            $("#off").html(offnum);
            showChart();
            document.getElementById("load").style.display = 'none';
            document.getElementById("show").style.display = 'none';
        }, 5000);
        flag = 2;
    }

    // console.log("405.redata.length:"+redata.length);
    if (redata.length === 405 || redata.length % 135 === 0) {
        for (var i = 0; i < redata.length; i += 135) {
            for (var f = 0; f < machine.length; f++) {
                //根据采集模块编号判断是否同一个焊机
                if (machine[f].fgather_no == redata.substring(8 + i, 12 + i).toString()) { //采集模块编号
                    var imgnum = machine[f].model;
                    $("#m3" + machine[f].fid).html("--");
                    $("#m2" + machine[f].fid).html("--");
                    for (var k = 0; k < welderName.length; k++) {
                        if (welderName[k].fid == parseInt(redata.substring(0 + i, 4 + i))) {    //焊工号
                            $("#m3" + machine[f].fid).html(welderName[k].fname);
                            //alert(welderName[k].fname);
                        }
                    }
                    //$("#m3" + machine[f].fid).html(redata.substring(0 + i, 4 + i));
                    // for (var t = 0; t < taskNum.length; t++) {
                    //     if (taskNum[t].id == parseInt(redata.substring(12 + i, 16 + i), 10)) {  //焊口号
                    //         $("#m2" + machine[f].fid).html(taskNum[t].weldedJunctionno);
                    //     }
                    // }
                    //$("#m2" + machine[f].fid).html(redata.substring(8 + i, 12 + i));
                    if (parseInt(redata.substring(32 + i, 36 + i), 10) == 128) {//焊机型号
                        var liveele = parseInt((parseInt(redata.substring(38 + i, 42 + i), 10)));//实际电流
                    } else {
                        var liveele = parseInt(redata.substring(38 + i, 42 + i), 10);
                    }
                    var livevol = parseFloat(parseInt(redata.substring(42 + i, 46 + i), 10) / 10).toFixed(1);
                    var maxele = parseInt(redata.substring(75 + i, 79 + i), 10);//最大电流
                    var minele = parseInt(redata.substring(79 + i, 83 + i), 10);
                    var maxvol = parseInt(redata.substring(83 + i, 87 + i), 10);
                    var minvol = parseInt(redata.substring(87 + i, 91 + i), 10);
                    var card = parseInt(redata.substring(99 + i, 103 + i), 10);
                    var mstatus = redata.substring(36 + i, 38 + i);
                    if (mstatus == 3 || mstatus == 3 || mstatus == 5 || mstatus == 7) {
                        if (card == "0") {
                            $("#m2" + machine[f].fid).html("任务未绑定");
                        } else {
                            $("#m2" + machine[f].fid).html("任务已下发");
                        }
                    }
                    var livestatus, livestatusid, liveimg;
                    switch (mstatus) {
                        case "00":
                            livestatus = "待机";
                            livestatusid = 1;
                            liveimg = "resources/images/welder_" + imgnum + "1.png";
                            break;
                        case "01":
                            livestatus = "E-010 焊枪开关OFF等待";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "02":
                            livestatus = "E-000工作停止";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "03":
                            livestatus = "工作";
                            livestatusid = 0;
                            liveimg = "resources/images/welder_" + imgnum + "0.png";
                            break;
                        case "04":
                            livestatus = "电流过低";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "05":
                            livestatus = "收弧";
                            livestatusid = 0;
                            liveimg = "resources/images/welder_" + imgnum + "0.png";
                            break;
                        case "06":
                            livestatus = "电流过高";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "07":
                            livestatus = "启弧";
                            livestatusid = 0;
                            liveimg = "resources/images/welder_" + imgnum + "0.png";
                            break;
                        case "08":
                            livestatus = "电压过低";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "09":
                            livestatus = "电压过高";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "10":
                            livestatus = "E-100控制电源异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "15":
                            livestatus = "E-150一次输入电压过高";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "16":
                            livestatus = "E-160一次输入电压过低";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "20":
                            livestatus = "E-200一次二次电流检出异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "21":
                            livestatus = "E-210电压检出异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "22":
                            livestatus = "E-220逆变电路反馈异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "30":
                            livestatus = "E-300温度异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "70":
                            livestatus = "E-700输出过流异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "71":
                            livestatus = "E-710输入缺相异常";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "98":
                            livestatus = "超规范停机";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                        case "99":
                            livestatus = "超规范报警";
                            livestatusid = 2;
                            liveimg = "resources/images/welder_" + imgnum + "2.png";
                            break;
                    }
                    if (liveary.length == 0) {
                        liveary.push(
                            {
                                "fid": machine[f].fid,
                                "liveele": liveele + "A",
                                "livevol": livevol + "V",
                                "livestatus": livestatus,
                                "livestatusid": livestatusid,
                                "liveimg": liveimg,
                                "nowTime": new Date().getTime()
                            })
                    } else {
                        var tempflag = false;
                        for (var x = 0; x < liveary.length; x++) {
                            if (liveary[x].fid == machine[f].fid) {
                                tempflag = true;
                                liveary.splice(x, 1, {
                                    "fid": machine[f].fid,
                                    "liveele": liveele + "A",
                                    "livevol": livevol + "V",
                                    "livestatus": livestatus,
                                    "livestatusid": livestatusid,
                                    "liveimg": liveimg,
                                    "nowTime": new Date().getTime()
                                })
                                break;
                            }
                            if (x == liveary.length - 1) {
                                liveary.push(
                                    {
                                        "fid": machine[f].fid,
                                        "liveele": liveele + "A",
                                        "livevol": livevol + "V",
                                        "livestatus": livestatus,
                                        "livestatusid": livestatusid,
                                        "liveimg": liveimg,
                                        "nowTime": new Date().getTime()
                                    })
                            }
                        }
                    }
                }
            }
        }
        var whiterList = parseInt(redata.substring(119, 123));
        //版本号不是最新版本，取出采集编号，进行下发
        if (whiterList !== whiteListVersion) {
            var gatherno = redata.substring(8, 12).toString();
            joinMachineCode(whiteListVersion, gatherno);
        }
    }
}

//获取选择的焊机进行下发
function joinMachineCode(whiteList, gatherno) {
    var weldercode = "";
    var leaveOverLength = 0;
    //焊工编号字节码数据包处理
    if (weldersArray.length > 0 && weldersArray.length <= 400) {
        for (var index in weldersArray) {
            var welderno = weldersArray[index].welderno;
            var weldernum = parseInt(welderno.substring(4, 9)).toString(16);
            weldercode = weldercode + weldernum;
        }
    } else {
        console.log("查询不到焊工信息或焊工信息过多不支持下发！");
        return false;
    }
    weldercode = weldercode.toUpperCase();
    leaveOverLength = 1600 - weldercode.length;
    console.log("焊工长度:" + weldercode.length);
    console.log("预留长度：" + leaveOverLength);
    var numVersion = parseInt(whiteList).toString(16);
    if (numVersion.length < 4) {
        var len = 4 - numVersion.length;
        for (var i = 0; i < len; i++) {
            numVersion = "0" + numVersion;
        }
    }
    var code = "";
    for (var i = 0; i < leaveOverLength; i++) {
        code = code + "0";
    }
    console.log("卡号总长度：" + (weldercode + code).length);
    var codehead = "007E0000000017";
    //var codefoot = "0000" + weldercode + code + "007D";
    var codefoot = numVersion + weldercode + code + "007D";
    //焊工白名单数据通过mq下发
    sendWelderCodeByMQ(gatherno, codehead, codefoot);
}

//下发
function sendWelderCodeByMQ(gatherno, codehead, codefoot) {
    //订阅消息主题
    client.subscribe("weldmes/upparams", {
        qos: 0,
        onSuccess: function (e) {
            console.log("主题订阅成功");
        },
        onFailure: function (e) {
            console.log("主题订阅失败:" + e.errorCode);
        }
    });
    var fgatherno = parseInt(gatherno).toString(16);
    if (fgatherno.length < 4) {
        var len = 4 - fgatherno.length;
        for (var i = 0; i < len; i++) {
            fgatherno = "0" + fgatherno;
        }
    }
    var datacode = (codehead + fgatherno + codefoot).toUpperCase();
    console.log("数据包总长度：" + datacode.length);
    // console.log(datacode);
    var message = new Paho.MQTT.Message(datacode);
    message.destinationName = "whiteList-dataIssue";
    client.send(message);
    console.log("白名单下发中！");
    window.setTimeout(function () {
        client.unsubscribe("weldmes/upparams", {
            onSuccess: function (e) {
                console.log("取消订阅成功");
            },
            onFailure: function (e) {
                console.log("取消订阅失败：" + e.errorCode);
            }
        });
    }, 1000);
}

var charts, flagnum = 0;

//饼图统计
function showChart() {
    if (flagnum == 0) {
        flagnum = 1;
        //初始化echart实例
        charts = echarts.init(document.getElementById("piecharts"));
    }
    //显示加载动画效果
    charts.showLoading({
        text: '稍等片刻,精彩马上呈现...',
        effect: 'whirling'
    });
    option = {
        tooltip: {
            trigger: 'item',
            formatter: function (param) {
                return '<div>实时统计<div>' + '<div style="float:left;margin-top:5px;border-radius:50px;border:solid rgb(100,100,100) 1px;width:10px;height:10px;background-color:' + param.color + '"></div><div style="float:left;">&nbsp;' + param.name + '：' + param.value + '%<div>';
            }
        },
        toolbox: {
            feature: {
                saveAsImage: {}//保存为图片
            },
            right: '2%'
        },
        series: [{
            name: '实时统计',
            type: 'pie',
            radius: ['45%', '60%'],
            color: ['#7cbc16', '#55a7f3', '#fe0002', '#818181'],
            data: [
                {value: ($("#work").html() / $("#machinenum").html() * 100).toFixed(2), name: '工作', id: 0},
                {value: ($("#standby").html() / $("#machinenum").html() * 100).toFixed(2), name: '待机', id: 1},
                {value: ($("#warn").html() / $("#machinenum").html() * 100).toFixed(2), name: '故障', id: 2},
                {value: ($("#off").html() / $("#machinenum").html() * 100).toFixed(2), name: '关机', id: 3}
            ],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function (param) {
                            return param.name + "：" + param.value + "%";
                        }
                    }
                }
            }
        }]
    }
    // 1、清除画布
    charts.clear();
    // 2、为echarts对象加载数据
    charts.setOption(option);
    //3、在渲染点击事件之前先清除点击事件
    charts.off('click');
    //隐藏动画加载效果
    charts.hideLoading();
    // 4、echarts 点击事件
    charts.on('click', function (param) {
        statusClick(param.data.id);
    });
    $("#chartLoading").hide();
}

//每30'刷新一次
var tempary = new Array();
window.setInterval(function () {
    //清空数组
//	window.setTimeout(function() {
    tempary = liveary.concat([]);
    var statusnum = $("#status").combobox('getValue');
    worknum = 0, standbynum = 0, warnnum = 0, offnum = machine.length - tempary.length;
    for (var i = 0; i < machine.length; i++) {
        var off = true;
        var imgnum = machine[i].model;
        for (var j = 0; j < tempary.length; j++) {
            if (machine[i].fid == tempary[j].fid) {
                off = false;
            }
        }
        if (off) {
            $("#m3" + machine[i].fid).html("--");
            $("#m4" + machine[i].fid).html("--A");
            $("#m5" + machine[i].fid).html("--V");
            $("#m6" + machine[i].fid).html("关机");
            $("#status" + machine[i].fid).val(3);
            $("#img" + machine[i].fid).attr("src", "resources/images/welder_" + imgnum + "3.png");
        }
        if (statusnum == 99) {
            for (var j = 0; j < tempary.length; j++) {
                if (tempary[j].fid == machine[i].fid) {
                    $("#m4" + machine[i].fid).html(tempary[j].liveele);
                    $("#m5" + machine[i].fid).html(tempary[j].livevol);
                    $("#m6" + machine[i].fid).html(tempary[j].livestatus);
                    $("#status" + machine[i].fid).val(tempary[j].livestatusid);
                    $("#img" + machine[i].fid).attr("src", tempary[j].liveimg);
                }
            }
            $("#machine" + machine[i].fid).show();
        } else {
            $("#machine" + machine[i].fid).hide();
            if (statusnum == 3) {
                var offflag = true;
                for (var j = 0; j < tempary.length; j++) {
                    if (machine[i].fid == tempary[j].fid) {
                        offflag = false;
                    }
                }
                if (offflag) {
                    $("#m3" + machine[i].fid).html("--");
                    $("#m4" + machine[i].fid).html("--A");
                    $("#m5" + machine[i].fid).html("--V");
                    $("#m6" + machine[i].fid).html("关机");
                    $("#status" + machine[i].fid).val(3);
                    $("#img" + machine[i].fid).attr("src", "resources/images/welder_04.png");
                    $("#machine" + machine[i].fid).show();
                }
            }
        }
    }
    for (var j = 0; j < tempary.length; j++) {
//			var status = $("#status"+tempary[j].fid).val();
        var status = tempary[j].livestatusid;
        if (status == 0) {
            worknum += 1;
        } else if (status == 1) {
            standbynum += 1;
        } else if (status == 2) {
            warnnum += 1;
        } else if (status == 3) {
            offnum += 1;
        }
        if (status == statusnum) {
            $("#m4" + tempary[j].fid).html(tempary[j].liveele);
            $("#m5" + tempary[j].fid).html(tempary[j].livevol);
            $("#m6" + tempary[j].fid).html(tempary[j].livestatus);
            $("#status" + tempary[j].fid).val(tempary[j].livestatusid);
            $("#img" + tempary[j].fid).attr("src", tempary[j].liveimg);
            $("#machine" + tempary[j].fid).show();
        }
    }
    $("#work").html(worknum);
    $("#standby").html(standbynum);
    $("#warn").html(warnnum);
    $("#off").html(offnum);
    showChart();
//		liveary.length = 0;
    var timeflag = new Date().getTime();
    for (var t = liveary.length - 1; t >= 0; t--) {
        if (timeflag - liveary[t].nowTime >= 30000) {
            liveary.splice(t, 1);
        }
    }
//	}, 6000);
}, 30000);

//状态按钮点击事件
function statusClick(statusnum) {
    $("#status").combobox('setValue', statusnum);
    for (var i = 0; i < machine.length; i++) {
        if (statusnum == 99) {
            for (var j = 0; j < tempary.length; j++) {
                if (tempary[j].fid == machine[i].fid) {
                    $("#m4" + machine[i].fid).html(tempary[j].liveele);
                    $("#m5" + machine[i].fid).html(tempary[j].livevol);
                    $("#m6" + machine[i].fid).html(tempary[j].livestatus);
                    $("#status" + machine[i].fid).val(tempary[j].livestatusid);
                    $("#img" + machine[i].fid).attr("src", tempary[j].liveimg);
                }
            }
            $("#machine" + machine[i].fid).show();
        } else {
            $("#machine" + machine[i].fid).hide();
            if (statusnum == 3) {
                var offflag = true;
                var imgnum = machine[i].model;
                for (var j = 0; j < tempary.length; j++) {
                    if (machine[i].fid == tempary[j].fid) {
                        offflag = false;
                    }
                }
                if (offflag) {
                    $("#m3" + machine[i].fid).html("--");
                    $("#m4" + machine[i].fid).html("--A");
                    $("#m5" + machine[i].fid).html("--V");
                    $("#m6" + machine[i].fid).html("关机");
                    $("#status" + machine[i].fid).val(3);
                    $("#img" + machine[i].fid).attr("src", "resources/images/welder_" + imgnum + "3.png");
                    $("#machine" + machine[i].fid).show();
                }
            }
        }
    }
    for (var j = 0; j < tempary.length; j++) {
//		var status = $("#status"+tempary[j].fid).val();
        var status = tempary[j].livestatusid;
        if (status == statusnum) {
            $("#m4" + tempary[j].fid).html(tempary[j].liveele);
            $("#m5" + tempary[j].fid).html(tempary[j].livevol);
            $("#m6" + tempary[j].fid).html(tempary[j].livestatus);
            $("#status" + tempary[j].fid).val(tempary[j].livestatusid);
            $("#img" + tempary[j].fid).attr("src", tempary[j].liveimg);
            $("#machine" + tempary[j].fid).show();
        }
    }
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变图表高宽
function domresize() {
    charts.resize();
}