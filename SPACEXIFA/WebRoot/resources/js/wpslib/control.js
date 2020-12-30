/**
 *
 */
var WebSocket_Url;
$(function () {
    $.ajax({
        type: "post",
        async: false,
        url: "td/AllTdbf",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                WebSocket_Url = eval(result.web_socket);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
})

//控制命令下发（字节码拼接）
function controlfun() {
    var con = $("input[name='free']:checked").val();
    if (con.length < 2) {
        var length = 2 - con.length;
        for (var i = 0; i < length; i++) {
            con = "0" + con;
        }
    }
    var machine;
    var selectMachine = $('#weldingmachineWpsTable').datagrid('getSelected');
    if (selectMachine.gatherId == null || selectMachine.gatherId == "") {
        alert("该焊机未对应采集编号!!!");
        return;
    } else {
        machine = selectMachine.gatherId.toString(16);
        if (machine.length < 4) {
            var length = 4 - machine.length;
            for (var i = 0; i < length; i++) {
                machine = "0" + machine;
            }
        }
    }
    var xiafasend1 = machine + con;
    var xxx = xiafasend1.toUpperCase();
    var data_length = ((parseInt(xxx.length) + 12) / 2).toString(16);
    if (data_length.length < 2) {
        var length = 2 - data_length.length;
        for (var i = 0; i < length; i++) {
            data_length = "0" + data_length;
        }
    }
    xxx = "7E" + data_length + "01010154" + xiafasend1;
    var check = 0;
    for (var i = 0; i < (xxx.length / 2); i++) {
        var tstr1 = xxx.substring(i * 2, i * 2 + 2);
        var k = parseInt(tstr1, 16);
        check += k;
    }
    var checksend = parseInt(check).toString(16);
    var a2 = checksend.length;
    checksend = checksend.substring(a2 - 2, a2);
    checksend = checksend.toUpperCase();
    var xiafasend2 = (xxx + checksend).substring(2);
    var str = "007E" + xiafasend2 + "7D";
    var data = new Array();
    data.push(str);
    dataIssueByMQTT(data);
}

//密码下发（字节码拼接）
function passfun() {
    if ($('#passwd').numberbox('getValue')) {
        if (parseInt($('#passwd').numberbox('getValue')) < 1 || parseInt($('#passwd').numberbox('getValue')) > 999) {
            alert("密码范围是1~999");
        } else {
            var con = parseInt($('#passwd').numberbox('getValue')).toString(16);
            if (con.length < 4) {
                var length = 4 - con.length;
                for (var i = 0; i < length; i++) {
                    con = "0" + con;
                }
            }
            var machine;
            var selectMachine = $('#weldingmachineWpsTable').datagrid('getSelected');
            if (selectMachine.gatherId == null || selectMachine.gatherId == "") {
                alert("该焊机未对应采集编号!!!");
                // websocket.close();
                return;
            } else {
                machine = selectMachine.gatherId.toString(16);
                if (machine.length < 4) {
                    var length = 4 - machine.length;
                    for (var i = 0; i < length; i++) {
                        machine = "0" + machine;
                    }
                    ;
                }
            }
            var xiafasend1 = machine + con;
            var xxx = xiafasend1.toUpperCase();
            var data_length = ((parseInt(xxx.length) + 12) / 2).toString(16);
            if (data_length.length < 2) {
                var length = 2 - data_length.length;
                for (var i = 0; i < length; i++) {
                    data_length = "0" + data_length;
                }
            }
            xxx = "7E" + data_length + "01010153" + xiafasend1;
            var check = 0;
            for (var i = 0; i < (xxx.length / 2); i++) {
                var tstr1 = xxx.substring(i * 2, i * 2 + 2);
                var k = parseInt(tstr1, 16);
                check += k;
            }
            var checksend = parseInt(check).toString(16);
            var a2 = checksend.length;
            checksend = checksend.substring(a2 - 2, a2);
            checksend = checksend.toUpperCase();
            var xiafasend2 = (xxx + checksend).substring(2);
            var str = "007E" + xiafasend2 + "7D";
            var data = new Array();
            data.push(str);
            dataIssueByMQTT(data);
        }
    } else {
        alert("密码不能为空");
    }
}

function openPassDlg() {
    $('#pwd').window({
        title: "密码下发",
        modal: true
    });
    $('#pwd').window('open');
}

//通过mq下发数据
function dataIssueByMQTT(data) {
    var datalength = 0;
    var oneMinuteTimer = window.setTimeout(function () {
        alert("下发失败");
        $('#smdlg').window("close")
        $('#pwd').window('close');
        $('#condlg').window("close");
        data.length = 0;
    }, 5000);
    var timer = window.setInterval(function () {
        if (data.length != 0) {
            var popdata = data.pop();
            var message = new Paho.MQTT.Message(popdata);
            message.destinationName = "spacexifa-downparams";
            client.send(message);
            console.log("下发成功");
            data.length = 0;
        } else {
            window.clearInterval(timer);
        }
    }, 300);
    client.subscribe("weldmes/upparams", {
        qos: 0,
        onSuccess: function (e) {
            console.log("订阅成功");
        },
        onFailure: function (e) {
            console.log("failed:"+e.errorCode);
        }
    });
    //数据返回
    client.onMessageArrived = function (e) {
        var fan = e.payloadString;
        //53：密码返回,54:控制命令返回
        console.log("fan.length:" + fan.length);
        if (fan.substring(0, 2) == "7E" && fan.length == 22) {
            if (fan.substring(10, 12) == "54") {
                alert("控制命令下发成功");
                datalength++;
            } else if (fan.substring(10, 12) == "53") {
                alert("密码下发成功");
                datalength++;
            }
            if (datalength != 0) {
                client.unsubscribe("weldmes/upparams", {
                    onSuccess: function (e) {
                        console.log("取消订阅成功");
                    },
                    onFailure: function (e) {
                        console.log("failed:"+e.errorCode);
                    }
                });
                window.clearTimeout(oneMinuteTimer);
                datalength = 0;
            }
            $('#smdlg').window("close");
            $('#pwd').window('close');
            $('#condlg').window("close");
            data.length = 0;
        }
    }
}