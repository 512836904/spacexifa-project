$(function () {
    weldDatagrid();
    machineInitialize();
    //开启mq连接
    mqttConnect();
    numversion();
});
var weldersArray = new Array();
var client, clientId;
var mqIpPort = "localhost:8083";

function weldDatagrid() {
    $("#welderTable").datagrid({
        height: $("#body").height(),
        width: $("#body").width(),
        idField: 'id',
        pageSize : 200,
        pageList : [ 10, 20, 50, 100, 200 ],
        url: "welders/getAllWelder",
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        pagination: true,
        fitColumns: true,
        columns: [[{
            field: 'id',
            title: '序号',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'name',
            title: '姓名',
            width: 100,
            halign: "center",
            align: "center"
        }, {
            field: 'welderno',
            title: '编号',
            width: 120,
            halign: "center",
            align: "center"
        }, {
            field: 'cellphone',
            title: '手机',
            width: 120,
            halign: "center",
            align: "left"
        }, {
            field: 'leveid',
            title: '级别id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'cardnum',
            title: '卡号',
            width: 150,
            halign: "center",
            align: "left"
        }, {
            field: 'levename',
            title: '级别',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'quali',
            title: '资质id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'qualiname',
            title: '资质',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'owner',
            title: '部门id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'ownername',
            title: '部门',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'DIMISSIONSTATUS',
            title: '焊工离职状态',
            width: 100,
            halign: "center",
            align: "left",
            formatter: function (value, row, index) {
                var str = "";
                if (value === 0) {
                    str += "<span style='color: #6c6c6c'>已离职</span>";
                } else {
                    str += "<span style='color: #012aa5'>在职</span>";
                }
                return str;
            }
        }, {
            field: 'fstatus',
            title: '焊工状态',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'statusName',
            title: '焊工状态',
            width: 100,
            halign: "center",
            align: "left",
            formatter: function (value, row, index) {
                var str = "";
                if (row.fstatus == 270) {
                    str += "<span style='color: #0ea501'>" + value + "</span>";
                } else if (row.fstatus == 271) {
                    str += "<span style='color: #022cff'>" + value + "</span>";
                } else {
                    str += "<span style='color: #6c6c6c'>" + value + "</span>";
                }
                return str;
            }
        }
        ]],
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        nowrap: false,
        onLoadSuccess: function (data) {
            $("a[id='edit']").linkbutton({text: '修改', plain: true, iconCls: 'icon-update'});
            $("a[id='remove']").linkbutton({text: '删除', plain: true, iconCls: 'icon-delete'});
        }
    });
}

//导出到excel
function exportDg() {
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exporWelder";
            var img = new Image();
            img.src = url;  // 设置相对路径给Image, 此时会发送出请求
            url = img.src;  // 此时相对路径已经变成绝对路径
            img.src = null; // 取消请求
            window.location.href = encodeURI(url);
        }
    });
}
function numversion(){
    $.ajax({
        type : "post",
        async : false,
        url : "pmt/getParameterAll",
        data : {},
        dataType : "json", //返回数据形式为json
        success : function(result) {
            var res = eval(result.rows);
            for(var i=0;i<res.length;i++){
                $("#numVersion").textbox("setValue", res[i].numversion);
            }
        },
        error : function(errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}
//导入
function importclick() {
    $("#importdiv").dialog("open").dialog("setTitle", "从excel导入数据");
}

function importWeldingMachine() {
    var file = $("#file").val();
    if (file == null || file == "") {
        $.messager.alert("提示", "请选择要上传的文件！");
        return false;
    } else {
        $('#importfm').form('submit', {
            url: "import/importWelder",
            success: function (result) {
                if (result) {
                    var result = eval('(' + result + ')');
                    if (!result.success) {
                        $.messager.show({
                            title: 'Error',
                            msg: result.msg
                        });
                    } else {
                        $('#importdiv').dialog('close');
                        $('#welderTable').datagrid('reload');
                        $.messager.alert("提示", result.msg);
                    }
                }

            },
            error: function (errorMsg) {
                alert("数据请求失败，请联系系统管理员!");
            }
        });
    }
}

function searchData() {
    var search = "";
    var sname = $("#sname").textbox('getValue');
    var swelderno = $("#swelderno").textbox('getValue');
    var scellphone = $("#scellphone").textbox('getValue');
    var scardnum = $("#scardnum").textbox('getValue');
    var sleveid = $("#sleveid").combobox("getValue");
    var squali = $("#squali").combobox("getValue");
    var sowner = $("#sowner").combobox("getValue");
    var sback = $("#sback").textbox('getValue');
    if (sname != "") {
        if (search == "") {
            search += " tb_welder.fname LIKE " + "'%" + sname + "%'";
        } else {
            search += " AND tb_welder.fname LIKE " + "'%" + sname + "%'";
        }
    }
    if (swelderno != "") {
        if (search == "") {
            search += " fwelder_no LIKE " + "'%" + swelderno + "%'";
        } else {
            search += " AND fwelder_no LIKE " + "'%" + swelderno + "%'";
        }
    }
    if (scellphone != "") {
        if (search == "") {
            search += " FCellPhone LIKE " + "'%" + scellphone + "%'";
        } else {
            search += " AND FCellPhone LIKE " + "'%" + scellphone + "%'";
        }
    }
    if (scardnum != "") {
        if (search == "") {
            search += " FCardNUm LIKE " + "'%" + scardnum + "%'";
        } else {
            search += " AND FCardNUm LIKE " + "'%" + scardnum + "%'";
        }
    }
    if (sleveid != "") {
        if (search == "") {
            search += " d.fvalue LIKE " + "'%" + sleveid + "%'";
        } else {
            search += " AND d.fvalue LIKE " + "'%" + sleveid + "%'";
        }
    }
    if (squali != "") {
        if (search == "") {
            search += " di.fvalue LIKE " + "'%" + squali + "%'";
        } else {
            search += " AND di.fvalue LIKE " + "'%" + squali + "%'";
        }
    }
    if (sowner != "") {
        if (search == "") {
            search += " i.fid LIKE " + "'%" + sowner + "%'";
        } else {
            search += " AND i.fid LIKE " + "'%" + sowner + "%'";
        }
    }
    if (sback != "") {
        if (search == "") {
            search += " tb_welder.fback LIKE " + "'%" + sback + "%'";
        } else {
            search += " AND tb_welder.fback LIKE " + "'%" + sback + "%'";
        }
    }
    $('#welderTable').datagrid('load', {
        "searchStr": search
    });
}

//获取选择的焊机进行下发
function joinMachineCode() {
    var weldMachineTable = $('#weldMachineTable').datagrid('getSelections');
    if (weldMachineTable.length === 0) {
        alert("请先选择焊机!!!");
        return false;
    }
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
        alert("查询不到焊工信息或焊工信息过多不支持下发！");
        return false;
    }
    weldercode = weldercode.toUpperCase();
    leaveOverLength = 1600 - weldercode.length;
    console.log("焊工长度:" + weldercode.length);
    console.log("预留长度：" + leaveOverLength);
    var numVersion = $("#numVersion").val();
    numVersion = parseInt(numVersion).toString(16);
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
    $("#machineTables").dialog('close');
    //弹出下发结果列表
    returnResultTables();
    showTip('数据持续下发中...');
    //焊工白名单数据通过mq下发
    sendWelderCodeByMQ(weldMachineTable, codehead, codefoot);
}

//下发
function sendWelderCodeByMQ(weldMachineTable, codehead, codefoot) {
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
    var resultArray = [];   //结果列表数组
    var gathernoArray = [];     //采集编号数组
    //数据下发每10秒执行一次，成功关闭
    var timer = window.setInterval(function () {
        if (weldMachineTable.length > 0) {
            for (var index in weldMachineTable) {
                let feild = {};
                feild["equipmentNo"] = weldMachineTable[index].equipmentNo;
                feild["gatherId"] = weldMachineTable[index].gatherId;
                feild["insframeworkName"] = weldMachineTable[index].insframeworkName;
                feild["result"] = 2;  //默认2：无响应
                var rowIndex = resultArray.findIndex(item => item.gatherId === weldMachineTable[index].gatherId);
                if (rowIndex === -1) {
                    resultArray.push(feild);
                }
                var indexNum = $.inArray(weldMachineTable[index].gatherId, gathernoArray);
                if (indexNum === (-1)) {
                    gathernoArray.push(weldMachineTable[index].gatherId);
                }
                var gatherno = weldMachineTable[index].gatherId;
                gatherno = parseInt(gatherno).toString(16);
                if (gatherno.length < 4) {
                    var len = 4 - gatherno.length;
                    for (var i = 0; i < len; i++) {
                        gatherno = "0" + gatherno;
                    }
                }

                var datacode = (codehead + gatherno + codefoot).toUpperCase();
                console.log("数据包总长度：" + datacode.length);
                // console.log(datacode);
                var message = new Paho.MQTT.Message(datacode);
                message.destinationName = "whiteList-dataIssue";
                client.send(message);
            }
            console.log(resultArray);
            $("#giveResultTable").datagrid('loadData', resultArray);
            weldMachineTable = "";
            weldMachineTable.length = 0;
            console.log("白名单下发中！");
        } else {
            window.clearInterval(timer);
        }
    }, 5000);
    //30秒内下发无返回,即为下发超时
    var oneMinuteTimer = window.setTimeout(function () {
        weldMachineTable = "";
        weldMachineTable.length = 0;
        mqttClientUnsubscribe();
        console.log("下发超时");
        closeTip();
    }, 30000);
    //客户端收到消息
    client.onMessageArrived = function (e) {
        var data = e.payloadString;
        if (data.substring(0, 2) === "7E" && data.substring(10, 12) === "17" && data.substring(24, 26) === "7D") {
            var gatherno = parseInt(data.substring(12, 16), 16).toString(); //采集编号
            if (gatherno.length < 4) {
                var len = 4 - gatherno.length;
                for (var i = 0; i < len; i++) {
                    gatherno = "0" + gatherno;
                }
            }
            var resultStatus = parseInt(data.substring(20, 22), 16); //接收结果
            //查找结果列表中的采集编号，取出下标
            var rowIndex = resultArray.findIndex(item => item.gatherId === gatherno);
            console.log(gatherno + "--下标--" + rowIndex + "--结果--" + resultStatus);
            //根据下发结果列表刷新单条数据
            $('#giveResultTable').datagrid('getRows')[rowIndex].result = resultStatus;
            $('#giveResultTable').datagrid('refreshRow', rowIndex);
            //收到下发结果，则删除数组的采集编号，全部删除则为全部下发完成
            if ($.inArray(gatherno, gathernoArray) !== -1) {
                gathernoArray.splice($.inArray(gatherno, gathernoArray), 1);
            }
            if (gathernoArray.length === 0) {
                weldMachineTable = "";
                weldMachineTable.length = 0;
                window.clearTimeout(oneMinuteTimer);
                mqttClientUnsubscribe();
                console.log("白名单全部下发成功！");
                closeTip();
            }
        }
    }
}

function returnResultTables() {
    loadResultTable();
    $('#giveResultTable').datagrid('loadData', { total: 0, rows: [] });
    $("#resultTables").dialog("open");
    $("#resultTables").dialog({
        title: '白名单下发结果',
        width: 900,
        height: 600,
        closed: false,
        cache: false,
        content: '',
        modal: true,
        buttons: [{
            text: '朕已阅',
            iconCls: 'icon-no',
            handler: function () {
                $("#resultTables").dialog('close');
            }
        }]
    });
}

function loadResultTable() {
    $("#giveResultTable").datagrid({
        height: $("#resultTables").height(),
        width: $("#resultTables").width(),
        idField: 'id',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "",
        singleSelect: true,
        rownumbers: false,
        showPageList: false,
        columns: [[{
            field: 'equipmentNo',
            title: '焊机名称',
            width: 210,
            halign: "center",
            align: "center"
        }, {
            field: 'gatherId',
            title: '采集序号',
            width: 210,
            halign: "center",
            align: "center"
        }, {
            field: 'insframeworkName',
            title: '所属组织机构',
            width: 210,
            halign: "center",
            align: "center"
        }, {
            field: 'result',
            title: '下发结果',
            width: 210,
            halign: "center",
            align: "center",
            formatter: function (value, row, index) {
                var str = "";
                if (value === 0) {
                    str += "<span style='color: #00ee00'>成功</span>";
                } else if (value === 1) {
                    str += "<span style='color: #CC2222'>失败</span>";
                } else {
                    str += "<span style='color: blue'>未响应</span>";
                }
                return str;
            }
        }
        ]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        }
    });
}

//mq连接
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
    console.log("mqtt onConnect");
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

//白名单下发
function whiteListIssue() {
    loadMachineTables();
    organizeInitialize();
    $('#weldMachineTable').datagrid('clearChecked');
    $('#machineTables').dialog("open");
    $('#machineTables').dialog({
        title: '选择焊机',
        width: 900,
        height: 600,
        closed: false,
        cache: false,
        // href: src,
        content: '',
        modal: true,
        buttons: [
            {
                text: '下发',
                iconCls: 'icon-save',
                handler: function () {
                    joinMachineCode();
                }
            }, {
                text: '关闭',
                iconCls: 'icon-no',
                handler: function () {
                    $("#machineTables").dialog('close');
                }
            }],
        onClose: function () {
        },
        onLoad: function () {
        }
    });
}

var workAreaArray = "";

function machineInitialize() {
    //工区改变事件
    $("#workArea").combobox({
        onChange: function () {
            var workArea = $("#workArea").combobox("getValue");
            var search = "";
            if (workArea !== '') {
                search = " AND i.FPARENT = " + workArea;
            }
            $.ajax({
                type: "post",
                async: false,
                url: "weldingMachine/getInsframeworkAll",
                data: {
                    search: search
                },
                dataType: "json", //返回数据形式为json
                success: function (result) {
                    if (result) {
                        $("#organize").combobox("clear");
                        if (result.ary.length > 0) {
                            for (var i = 0; i < result.ary.length; i++) {
                                if (i === 0) {
                                    workAreaArray = "" + result.ary[i].id;
                                } else {
                                    workAreaArray = workAreaArray + "," + result.ary[i].id;
                                }
                            }
                        }
                        $('#organize').combobox({
                            valueField: 'id',
                            textField: 'name',
                            multiple: false,  //多选
                            editable: false,  //是否可编辑
                            panelHeight: 'auto',
                            data: result.ary
                        });
                        $("#organize").combobox();
                    }
                },
                error: function (errorMsg) {
                    console.log("数据请求失败，请联系系统管理员!");
                }
            });
            workAreaArray = " i.fid IN (" + workAreaArray + ")";
            $("#weldMachineTable").datagrid('options').queryParams.parent = workAreaArray; //把查询条件赋值给datagrid内部变量
            $('#weldMachineTable').datagrid('reload');
        }
    });
    //班组改变事件
    $("#organize").combobox({
        onChange: function () {
            var organize = $("#organize").combobox("getValue");
            if (organize === '') {
                organize = workAreaArray;
            } else {
                organize = " i.fid IN (" + organize + ")";
            }
            $("#weldMachineTable").datagrid('options').queryParams.parent = organize; //把查询条件赋值给datagrid内部变量
            $('#weldMachineTable').datagrid('reload');
        }
    });
}

function loadMachineTables() {
    var query = {};
    $("#weldMachineTable").datagrid({
        height: $("#machineTables").height() - 50,
        width: $("#machineTables").width(),
        idField: 'id',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: 'weldingMachine/getWedlingMachineList',
        singleSelect: false,
        rownumbers: true,
        showPageList: false,
        queryParams: query,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'id',
            title: '序号',
            width: 50,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'equipmentNo',
            title: '设备名称',
            width: 120,
            halign: "center",
            align: "center"
        }, {
            field: 'typeName',
            title: '设备类型',
            width: 100,
            halign: "center",
            align: "center"
        }, {
            field: 'statusName',
            title: '状态',
            width: 80,
            halign: "center",
            align: "center"
        }, {
            field: 'manufacturerName',
            title: '厂家',
            width: 120,
            halign: "center",
            align: "center"
        }, {
            field: 'gatherId',
            title: '采集序号',
            width: 120,
            halign: "center",
            align: "center"
        }, {
            field: 'statusId',
            title: '状态id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'insframeworkName',
            title: '所属组织机构',
            width: 180,
            halign: "center",
            align: "center"
        }, {
            field: 'gid',
            title: '采集id',
            width: 100,
            halign: "center",
            align: "left",
            hidden: true
        }
        ]],
        pagination: true,
        fitColumns: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                color.class = "rowColor";
                return color;
            }
        }
    });
}

//组织机构等选择框初始化
function organizeInitialize() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getInsframeworkAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var soptionStr = '';
                soptionStr += "<option value=''>请选择</option>";
                for (var i = 0; i < result.ary.length; i++) {
                    soptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#organize").html(soptionStr);
            }
        },
        error: function (errorMsg) {
            console.log("查询班组下拉框数据异常！");
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getAllWorkArea",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var soptionStr = '';
                soptionStr += "<option value=''>请选择</option>";
                for (var i = 0; i < result.ary.length; i++) {
                    soptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#workArea").html(soptionStr);
            }
        },
        error: function (errorMsg) {
            console.log("查询工区下拉框数据异常");
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
    $("#organize").combobox();
    $("#workArea").combobox();
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#welderTable").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}