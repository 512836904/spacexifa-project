$(function () {
    document.getElementById("cardnumber").focus();
    $("#cardnumber").keydown(function (e) {
        if (e.keyCode == 13) {
            alert($("#cardnumber").val());
        }
    });
    //根据IP查采集编号
    loadRequestPath();
    //工艺焊缝联动查询
    loadProduction();
    //mq连接
    mqttConnect();
});

var client, clientId;
var iphostport = "http://localhost:8080";
var mqIpPort = "localhost:8083";
var minele = 0;
var maxele = 0;
var minvol = 0;
var maxvol = 0;
var taskResultId = 0;
var weldLineId = 0;
var wpsId = 0;
var cardId = 0;
var repairType = 0;

//根据手持终端IP查询采集编号
function loadRequestPath() {
    $.ajax({
        type: "post",
        dataType: 'jsonp',
        jsonp: 'jsonpCallback',
        data: {},
        url: iphostport + "/SPACEXIFA/terminal/getRequestPath",
        success: function (result) {
            if (result) {
                //查询并赋值采集编号
                $("#fgatherno").val(result.fgather_no);
                if (result.fgather_no != null && result.fgather_no !== '') {
                } else {
                    alert("该终端设备未进行绑定，请联系管理员！");
                }
            } else {
                $("#start").attr("disabled", "disabled");
                alert("该终端设备未进行绑定，请联系管理员！");
            }
        },
        error: function (e) {
            console.log(e);
            $("#start").attr("disabled", "disabled");
            alert("该终端设备未进行绑定，请联系管理员！");
        }
    });
}

//工艺名下拉框及焊缝联动
function loadProduction() {
    //查询所有工艺名
    $.ajax({
        type: "POST",
        dataType: 'jsonp',
        jsonp: 'jsonpCallback',
        data: {},
        url: iphostport + "/SPACEXIFA/terminal/findAllProductionName",
        success: function (result) {
            if (result != null && result.array !== undefined) {
                $("#fname").combobox({
                    valueField: 'id',
                    textField: 'text',
                    editable: true,
                    required: true,
                    mode: 'local',
                    data: result.array
                });
            }
        }
    });
    //工艺和焊缝联动事件
    $('#fname').combobox({
        onChange: function (n) {
            $.ajax({
                type: "POST",
                dataType: 'jsonp',
                jsonp: 'jsonpCallback',
                data: {
                    fname: n
                },
                url: iphostport + "/SPACEXIFA/terminal/findJunctionByFname",
                success: function (result) {
                    if (result != null && result.array !== undefined) {
                        //工艺扫码对焊缝不进行重新赋值
                        if (num !== 2) {
                            $("#fjunction").combobox({
                                valueField: 'id',
                                textField: 'text',
                                editable: true,
                                required: true,
                                mode: 'local',
                                data: result.array
                            });
                        }
                    }
                }
            });
        }
    });
    //根据焊缝选择查询工艺参数
    $('#fjunction').combobox({
        onChange: function (n) {
            //判断焊缝值是否为数字，数字：焊缝id（焊缝选择改变），非数字：工艺扫码
            if (isRealNum(n)) {
                $.ajax({
                    type: "POST",
                    dataType: 'jsonp',
                    jsonp: 'jsonpCallback',
                    data: {
                        junction_id: n
                    },
                    url: iphostport + "/SPACEXIFA/terminal/findCraftByJunctionId",
                    success: function (result) {
                        if (result != null && result.craft_param !== undefined) {
                            var craftParam = result.craft_param;
                            $("#wpsparameter").val(craftParam);
                            if (craftParam !== '') {
                                var str = craftParam.split(",");
                                for (var index in str) {
                                    var jun_name = str[index].split(":");
                                    if (jun_name[0].indexOf("电流") !== -1) {
                                        var ele = jun_name[1].split("-");
                                        minele = ele[0];
                                        maxele = ele[1];
                                    }
                                    if (jun_name[0].indexOf("电压") !== -1) {
                                        var vol = jun_name[1].split("-");
                                        minvol = vol[0];
                                        maxvol = vol[1];
                                    }
                                    if (jun_name[0].indexOf("其他") !== -1) {
                                        if (jun_name[1].indexOf("返修") !== -1) {
                                            repairType = 1;
                                        } else {
                                            repairType = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    });
}

function isRealNum(val) {
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if (val === "" || val == null) {
        return false;
    }
    if (!isNaN(val)) {
        return true;
    } else {
        return false;
    }
}

//扫描跟踪卡获取信息
var datalist = "";
var num = 0;

//打开摄像头
function mission_onclick(number) {
    document.getElementById("canvas").style.display = "block";
    document.getElementById("loadingMessage").style.display = "block";
    navigator.mediaDevices.getUserMedia({video: {facingMode: "environment"}}).then(function (stream) {
        video.srcObject = stream;
        video.setAttribute("playsinline", true); // required to tell iOS safari we don't want fullscreen
        video.play();
        requestAnimationFrame(tick);
    });
    num = number;
}

function tick() {
    loadingMessage.innerText = "⌛ Loading video...";
    if (video.readyState === video.HAVE_ENOUGH_DATA) {
        //识别到二维码信息处理
        document.getElementById("loadingMessage").style.display = "none";
        canvasElement.height = video.videoHeight;
        canvasElement.width = video.videoWidth;
        canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);
        var imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);
        var code = jsQR(imageData.data, imageData.width, imageData.height, {
            inversionAttempts: "dontInvert",
        });
        if (code && code.data) {
            drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#FF3B58");
            drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#FF3B58");
            drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#FF3B58");
            drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#FF3B58");
            datalist = code.data;
            canvasElement.style.display = "none";
            //画布清空
            canvas.clearRect(0, 0, canvasElement.width, canvasElement.height);
            video.srcObject.getTracks().forEach(track => {
                track.stop();
            });
            video.srcObject = null;
        }
    }
    if (datalist !== '') {
        canvasElement.style.display = "none";
        canvas.clearRect(0, 0, canvasElement.width, canvasElement.height);
        if (num === 1) {
            //工票编号赋值
            $("#cardnumber").val(datalist);
            findWorkticketByno(datalist);
        } else if (num === 2) {
            //工艺参数赋值
            $("#wpsparameter").val(datalist);
            document.getElementById("wpsparameter").focus();
            setJunction(datalist);
        }
        datalist = "";
    } else {
        requestAnimationFrame(tick);
    }
}

//根据工票编号查询MES工作号信息
function findWorkticketByno(workcode) {
    showTip('数据拼命加载中...');
    $.ajax({
        url: iphostport + "/SPACEXIFA/terminal/findWorkticketByno",
        type: "POST",
        async: true,
        dataType: 'jsonp',
        jsonp: 'jsonpCallback',
        data: {
            worksheetcode: workcode,
            fgatherno: $("#fgatherno").val()
        },
        success: function (result) {
            if (result != null) {
                if (result.mesworkno != null && result.mesworkno !== '') {
                    $("#productNumber").val(result.mesworkno);//工作号
                    $("#fwelding_area").val(result.comcode);//布套号
                    $("#productDrawNo").val(result.partnumber);//零件图号
                    $("#productName").val(result.materialname);//零件名称
                    closeTip();
                    alert("查询成功！");
                    //扫码后启用开始任务按钮
                    if ($("#fgatherno").val() != null && $("#fgatherno").val() !== '') {
                        $("#start").removeAttr("disabled");
                    }
                } else {
                    closeTip();
                    alert("查询不到工作号布套号信息！请稍后再试！");
                }
            } else {
                closeTip();
                alert("查询不到工作号布套号信息！请稍后再试！");
            }
        },
        error: function (e) {
            closeTip();
            alert("后台查询异常！请联系管理员！");
        }
    });
}

//工艺、焊缝信息赋值
function setJunction(datalist) {
    if (datalist !== '') {
        var str = datalist.split(",");
        for (var index in str) {
            var jun_name = str[index].split(":");
            if (jun_name[0].indexOf("WPS") !== -1) {
                //工艺名选中
                var fname = $("#fname").combobox('getData');
                var wpsFlag = false;
                if (fname.length > 0) {
                    for (var i in fname) {
                        if (fname[i].text == jun_name[1]) {
                            wpsFlag = true; //有值
                        }
                    }
                }
                //有值选中，没值赋值
                if (wpsFlag) {
                    $("#fname").combobox("select", jun_name[1]);
                } else {
                    $("#fname").combobox("setValue", jun_name[1]);
                }
            }
            if (jun_name[0].indexOf("焊缝") !== -1) {
                //焊缝名称赋值
                var fjunction = $("#fjunction").combobox('getData');
                var nameFlag = false;
                if (fjunction.length > 0) {
                    for (var i in fjunction) {
                        if (fjunction[i].text == jun_name[1]) {
                            nameFlag = true; //有值
                        }
                    }
                }
                //有值选中，没值赋值
                if (nameFlag) {
                    $("#fjunction").combobox("select", jun_name[1]);
                } else {
                    $("#fjunction").combobox("setValue", jun_name[1]);
                }
            }
            if (jun_name[0].indexOf("电流") !== -1) {
                var ele = jun_name[1].split("-");
                minele = ele[0];
                maxele = ele[1];
            }
            if (jun_name[0].indexOf("电压") !== -1) {
                var vol = jun_name[1].split("-");
                minvol = vol[0];
                maxvol = vol[1];
            }
            if (jun_name[0].indexOf("其他") !== -1) {
                if (jun_name[1].indexOf("返修") !== -1) {
                    repairType = 1;
                } else {
                    repairType = 0;
                }
            }
        }
    }
}

//点击开始任务按钮
function start_onclick() {
    /**
     * 1.存焊缝
     * 2.存工艺
     * 3.存电子跟踪卡
     * 4.存手持终端任务数据
     * 5.通过mq发送到pc,下发到采集盒和存入实时表
     */
    if ($("#cardnumber").val() == '') {
        alert("请扫描工票信息！");
        return;
    } else if ($("#wpsparameter").val() == '') {
        alert("请扫描工艺信息！");
        return;
    } else {
        showTip('数据保存中...');
        var data = {
            cardnumber: $("#cardnumber").val(),
            productNumber: $("#productNumber").val(),
            fwelding_area: $("#fwelding_area").val(),
            productDrawNo: $("#productDrawNo").val(),
            productName: $("#productName").val(),
            wpsparameter: $("#wpsparameter").val(),
            fgatherno: $("#fgatherno").val(),
            repairType: repairType
        }
        $.ajax({
            type: "POST",
            dataType: 'jsonp',
            jsonp: 'jsonpCallback',
            async: false,
            data: data,
            url: iphostport + "/SPACEXIFA/terminal/saveWorkAndJunction",
            success: function (result) {
                if (result != null) {
                    if (result.flag) {
                        weldLineId = result.junctiob_id;
                        wpsId = result.produ_id;
                        cardId = result.welded_jun_id;
                        taskResultId = Number(result.task_id);  //任务id
                        var machinemodel = result.machinemodel;
                        //alert("数据保存成功！");
                        if (machinemodel == "182") {
                            //模拟焊机-直接下发
                            //alert("模拟焊机");
                            document.getElementById("tipInfo").innerText = "数据保存成功,等待下发数据...";
                            var gatherno = $("#fgatherno").val();
                            gatherno = parseInt(gatherno).toString(16);
                            if (gatherno.length < 4) {
                                var length = 4 - gatherno.length;
                                for (var i = 0; i < length; i++) {
                                    gatherno = "0" + gatherno;
                                }
                            }
                            var strdata = "7E360101015600010101001E0001010E00BE000000EB001A0000010E00BE000000010000010C0000000319040000000000B400000000B57D";
                            var data = strdata.substring(0, 12) + gatherno + strdata.substring(16, 112);
                            jointCode(data);
                        } else if (machinemodel == "180" || machinemodel == "181") {
                            //数字焊机-先索取再下发
                            //alert("数字焊机");
                            document.getElementById("tipInfo").innerText = "数据保存成功,等待索取数据...";
                            suoquData();
                        }
                    } else {
                        alert("数据保存失败！");
                    }
                }
            }
        });
    }
}

//通过mq将数据发送到PC，并存入实时表
function sendMQSaveLiveData(resultType, cardId, wpsId, weldLineId) {
    //开始/结束任务
    var taskResultData = {
        type: resultType,
        machine: "m-" + $("#fgatherno").val(),
        welderid: 0,            //焊工id
        cardid: cardId,         //电子跟踪卡id
        wpsid: wpsId,           //工艺id
        productid: repairType,  //产品号id（返修状态）
        workprocedureid: 0,     //工序号id
        workstepid: 0,          //工步号id
        weldlineid: weldLineId  //焊缝号id
    }
    var message = new Paho.MQTT.Message(JSON.stringify(taskResultData));
    message.destinationName = "padDataSetLiveData";
    client.send(message);
}

//数据索取
function suoquData() {
    var symbol = 0;
    var fgatherno = $("#fgatherno").val();
    if (fgatherno == null || fgatherno === '') {
        alert("采集编号为空！无法下发数据");
        return;
    }
    var gatherno = parseInt(fgatherno).toString(16);
    if (gatherno.length < 4) {
        var length = 4 - gatherno.length;
        for (var i = 0; i < length; i++) {
            gatherno = "0" + gatherno;
        }
    }
    var xxx = "7E0901010156" + gatherno + "01";
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
    var str = "00" + xxx + checksend + "7D";
    var message = new Paho.MQTT.Message(str);
    message.destinationName = "hand-held-terminal-askFor";
    client.send(message);
    //订阅消息主题
    client.subscribe("weldmes/upparams", {
        qos: 0,
        onSuccess: function (e) {
            console.log("主题订阅成功");
        },
        onFailure: function (e) {
            console.log("主题订阅失败：" + e.errorCode);
        }
    });
    //5秒内索取不到即为超时,取消订阅
    var oneMinuteTimer = window.setTimeout(function () {
        if (symbol === 0) {
            mqttClientUnsubscribe();
            alert("索取超时");
            closeTip();
        }
    }, 5000);
    //客户端收到消息时执行的方法
    client.onMessageArrived = function (e) {
        var da = e.payloadString;
        var gatherNo = parseInt(da.substring(12, 16), 16).toString();
        if (gatherNo.length < 4) {
            var length = 4 - gatherNo.length;
            for (var i = 0; i < length; i++) {
                gatherNo = "0" + gatherNo;
            }
        }
        if (da.substring(0, 2) === "7E" && da.substring(10, 12) === "56" && gatherNo === fgatherno) {
            if (da.substring(18, 20) === "FF") {
                symbol = 1;
                mqttClientUnsubscribe();
                window.clearTimeout(oneMinuteTimer);
                alert("此通道没有规范!");
            } else {
                symbol = 1;
                mqttClientUnsubscribe();
                window.clearTimeout(oneMinuteTimer);
                //alert("索取成功!");
                document.getElementById("tipInfo").innerText = "数据索取成功,正在下发数据...";
                jointCode(da);
            }
        }
    }
}

//下发控制命令
function controlCommandIssue() {
    var symbol = 0;
    var gatherno = $("#fgatherno").val();
    gatherno = parseInt(gatherno).toString(16);
    if (gatherno.length < 4) {
        var length = 4 - gatherno.length;
        for (var i = 0; i < length; i++) {
            gatherno = "0" + gatherno;
        }
    }
    var gatherno_control_command = gatherno + "01";
    var xxx = gatherno_control_command.toUpperCase();
    var data_length = ((parseInt(xxx.length) + 12) / 2).toString(16);
    if (data_length.length < 2) {
        var length = 2 - data_length.length;
        for (var i = 0; i < length; i++) {
            data_length = "0" + data_length;
        }
    }
    xxx = "7E" + data_length + "01010154" + xxx;
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
    console.log("控制命令包：" + str);
    console.log("控制命令包length：" + str.length);

    var message = new Paho.MQTT.Message(str);
    message.destinationName = "control-command-issue";
    client.send(message);
    //订阅消息主题
    client.subscribe("weldmes/upparams", {
        qos: 0,
        onSuccess: function (e) {
            console.log("主题订阅成功");
        },
        onFailure: function (e) {
            console.log("主题订阅失败：" + e.errorCode);
        }
    });
    //5秒内索取不到即为超时,取消订阅
    var oneMinuteTimer = window.setTimeout(function () {
        if (symbol === 0) {
            mqttClientUnsubscribe();
            alert("控制命令下发超时");
        }
    }, 5000);
    //客户端收到消息时执行的方法
    client.onMessageArrived = function (e) {
        var da = e.payloadString;
        var gatherNo = parseInt(da.substring(12, 16), 16).toString();
        if (gatherNo.length < 4) {
            var length = 4 - gatherNo.length;
            for (var i = 0; i < length; i++) {
                gatherNo = "0" + gatherNo;
            }
        }
        if (da.substring(0, 2) === "7E" && da.substring(10, 12) === "54" && gatherNo === $("#fgatherno").val()) {
            if (parseInt(da.substring(16, 18), 16) === 1) {
                symbol = 1;
                mqttClientUnsubscribe();
                window.clearTimeout(oneMinuteTimer);
                alert("控制命令下发失败!!!");
            } else {
                symbol = 1;
                mqttClientUnsubscribe();
                window.clearTimeout(oneMinuteTimer);
                alert("控制命令下发成功！");
            }
        }
    }

}

//下发数据的字节码包拼接
function jointCode(strdata) {
    var data = "";
    if (strdata !== '' && minele !== 0 && minvol !== 0) {
        var ele = (Number(minele) + Number(maxele)) / 2;
        var tiny_ele = Number(maxele) - Number(ele);
        ele = parseInt(ele).toString(16);
        tiny_ele = parseInt(tiny_ele).toString(16);
        if (ele.length < 4) {
            var lenele = 4 - ele.length;
            for (var i = 0; i < lenele; i++) {
                ele = "0" + ele;
            }
        }
        if (tiny_ele.length < 2) {
            var lenele = 2 - tiny_ele.length;
            for (var i = 0; i < lenele; i++) {
                tiny_ele = "0" + tiny_ele;
            }
        }
        var vol = (Number(minvol) + Number(maxvol)) / 2;
        var tiny_vol = Number(maxvol) - Number(vol);
        vol = parseInt(vol * 10).toString(16);
        tiny_vol = parseInt(tiny_vol * 10).toString(16);
        if (vol.length < 4) {
            var lenvol = 4 - vol.length;
            for (var i = 0; i < lenvol; i++) {
                vol = "0" + vol;
            }
        }
        if (tiny_vol.length < 2) {
            var lenvol = 2 - tiny_vol.length;
            for (var i = 0; i < lenvol; i++) {
                tiny_vol = "0" + tiny_vol;
            }
        }
        //有小写字母转大写
        ele = ele.toUpperCase();
        vol = vol.toUpperCase();
        tiny_ele = tiny_ele.toUpperCase();
        tiny_vol = tiny_vol.toUpperCase();

        data = "00" + strdata.substring(0, 10) + "52" + strdata.substring(12, 16) + strdata.substring(18, 40) + ele + vol +
            strdata.substring(48, 84) + tiny_ele + tiny_vol + strdata.substring(88, 112);
        if (data.length === strdata.length) {
            issueData(data);
        }
        console.log(strdata.length);
        console.log(data.length);
        //data:007E3601010152000201001E000100BD00C2000000EB001A000000F6010000000001FFEC010C0000000319040000000000B4000000009E7D
    } else {
        console.log("电流电压或索取数据为空");
        console.log("strdata：" + strdata);
        console.log("minele：" + minele);
        console.log("minvol：" + minvol);
    }
}

//数据下发
function issueData(dataCode) {
    var data = "";
    data = dataCode;
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
    //数据下发每秒执行一次，成功关闭
    var timer = window.setInterval(function () {
        if (data.length !== 0) {
            var message = new Paho.MQTT.Message(data);
            message.destinationName = "hand-held-terminal-issue";
            client.send(message);
            data = "";
            console.log("手持终端数据下发中！");
        } else {
            window.clearInterval(timer);
        }
    }, 1000);
    //5秒内下发无返回,即为下发超时
    var oneMinuteTimer = window.setTimeout(function () {
        data = "";
        mqttClientUnsubscribe();
        closeTip();
        alert("下发超时！");
    }, 5000);
    //客户端收到消息时执行的方法
    client.onMessageArrived = function (e) {
        var fan = e.payloadString;
        var gatherNo = parseInt(fan.substring(12, 16), 16).toString();
        if (gatherNo.length < 4) {
            var length = 4 - gatherNo.length;
            for (var i = 0; i < length; i++) {
                gatherNo = "0" + gatherNo;
            }
        }
        if (fan.substring(0, 2) === "7E" && fan.substring(10, 12) === "52" && gatherNo === $("#fgatherno").val()) {
            //下发结果：1：失败
            if (parseInt(fan.substring(18, 20), 16) === 1) {
                window.clearTimeout(oneMinuteTimer);
                mqttClientUnsubscribe();
                closeTip();
                alert("下发失败！");
            } else {
                window.clearTimeout(oneMinuteTimer);
                mqttClientUnsubscribe();
                closeTip();
                alert("下发成功！");
                //下发成功后启用结束任务按钮,禁用开始任务按钮
                $("#start").attr("disabled", "disabled");
                $("#over").removeAttr("disabled");
                //下发成功后通过mq存入实时表
                sendMQSaveLiveData("starttask", cardId, wpsId, weldLineId);
                //控制命令下发
                //controlCommandIssue();
            }
        }
    }
}

//结束任务按钮
function over_onclick() {
    /**
     * 1.更新任务完成表状态和时间
     * 2.通过mq发送PC存入实时表
     */
    showTip('加载中...');
    if (taskResultId !== 0) {
        $.ajax({
            url: iphostport + "/SPACEXIFA/terminal/overTaskResult",
            type: 'POST',
            async: true,
            dataType: 'jsonp',
            jsonp: 'jsonpCallback',
            data: {
                taskResultId: taskResultId
            },
            success: function (result) {
                closeTip();
                if (result != null && result.flag) {
                    alert("恭喜任务已完成！");
                    $("#over").attr("disabled", "disabled");
                    sendMQSaveLiveData("overtask", 0, 0, 0);
                } else {
                    alert("任务完成失败！请稍后再试！");
                    console.log("flag：" + result.flag);
                }
            },
            error: function (e) {
                closeTip();
                alert("网络异常！请稍后再试！");
                console.log("error：" + e);
            }
        });
    } else {
        closeTip();
        alert("查询不到任务,请稍后再试!");
        console.log("taskResultId：" + taskResultId);
    }
}

//mqqt模块
function mqttConnect() {
    clientId = Math.random().toString().substr(3, 8) + Date.now().toString(36);
    client = new Paho.MQTT.Client(mqIpPort.split(":")[0], parseInt(mqIpPort.split(":")[1]), clientId);
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
    console.log("onConnect");
}

//断线重连
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

//接收到消息
function onMessageArrived(message) {
    console.log("onMessageArrived:" + message.payloadString);
}

//取消消息订阅
function mqttClientUnsubscribe() {
    client.unsubscribe("weldmes/upparams", {
        onSuccess: function (e) {
            console.log("取消订阅成功");
        },
        onFailure: function (e) {
            console.log("取消订阅失败：" + e.errorCode);
        }
    });
}