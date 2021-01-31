<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>云智能焊接管控系统</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="icon" href="resources/images/title.ico" type="img/x-icon"/>
    <link rel="stylesheet" type="text/css" href="resources/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="resources/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/common.css">
    <link rel="stylesheet" type="text/css" href="resources/css/iconfont.css">

    <script type="text/javascript" src="resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resources/js/index.js"></script>
    <!-- 	<script type="text/javascript" src="resources/js/indexFauit.js"></script> -->
    <!-- 	<script type="text/javascript" src="resources/js/indexFauit2.js"></script> -->
    <script type="text/javascript" src="resources/js/swfobject.js"></script>
    <script type="text/javascript" src="resources/js/web_socket.js"></script>
    <script type="text/javascript" src="resources/js/paho-mqtt.js"></script>
    <script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script>
    <style type="text/css">
        a {
            text-decoration: none;
            color: inherit;
            outline: none;
        }
    </style>
</head>

<body class="easyui-layout">
<!-- 头部 -->
<div region="north" style="height: 98px;" id="north">
    <div class="head-wrap">
        <a href="" class="logo"><img src="resources/images/weldmeslog.png"/></a>
        <div class="time">
            <a href="void(0)" id="headtime" style="color:#fff;"></a>
        </div>
        <div class="search-wrap">
            <a href="void(0)" id="username">欢迎您：</a>&nbsp;|
            <img src="resources/images/1_06.png"/>
            <a href="user/logout">注销</a>
        </div>
    </div>
</div>

<div region="west" hide="true" split="true" title="导航菜单" style="width: 200px;" id="west"
     data-options="iconCls:'icon-navigation'">
    <div class="easyui-accordion" border="false" id="accordiondiv"></div>
</div>

<div id="mainPanle" region="center" style="background: white; overflow-y: hidden;width:400px">
    <div id="tabs" class="easyui-tabs" fit="true" border="false"></div>
    <div id="tabMenu" class="easyui-menu" style="width:150px">
        <div id="refreshtab">刷新</div>
        <div id="closetab">关闭标签页</div>
        <div id="closeLeft">关闭左侧标签页</div>
        <div id="closeRight">关闭右侧标签页</div>
        <div id="closeOther">关闭其他标签页</div>
        <div id="closeAll">关闭全部标签页</div>
    </div>
</div>

<div data-options="region:'south',split:true" style="height:40px;">
    <div class="tenghan-bottom">
        <div style="width: 100%;height: 100%;">
            <div id="fauitContent" style="padding-top: 20px;">
                <div id="scrollcontent"></div>
            </div>
        </div>

        <div style="float:left;color:#fff;width:23%;" id="bottomlog">
            &nbsp;
        </div>
        <div style="float:left;color:#fff;width:53%;height:55px;position: relative;" id="bottomlog">
            <!-- <div style="float:left;width:640px;margin: auto;left:0;right:0;position: absolute;text-align:center;">
                <span style="float:right;padding-top:15px;">
                    &nbsp;&nbsp;&nbsp;&nbsp;重准备 强协调 控过程&nbsp;&nbsp;|&nbsp;&nbsp;抓策划 解扣子 接地气&nbsp;&nbsp;|&nbsp;&nbsp;想明白 写下来 照着干
                </span>
                <span style="float:right;padding-top:15px;font-size:16px;">改革创新  提质增效</span>
            </div> -->
        </div>
        <!-- 			<div style="float:left;color:#fff;width:23%;text-align:right;padding-top:15px;" id="bottomlog">
                        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-msg" style="border: none;">消息</a>
                    </div> -->
    </div>
</div>
<script type="text/javascript">

    var ary = new Array();
    var fauitContent, timer, fauitstr;

    var receivealarm = false;
    $(function () {
        $.ajax({
            url: 'user/getUserPermission',
            type: 'post',
            async: false,
            data: {},
            dataType: 'json',
            success: function (result) {
                if (result && result.flag) {
                    if (result.receivealarm === 1) {
                        receivealarm = true;
                    } else {
                        console.log("您的账号不具有接收权限！");
                        return false;
                    }
                } else {
                    console.log("后台查询用户权限异常，请稍后再试！");
                    return false;
                }
            },
            error: function (e) {
                console.log("查询登录用户下发权限异常，请稍后再试！");
                return false;
            }
        });
        if (receivealarm) {
            fauitContent = document.getElementById('fauitContent');
            //$("#fauitContent").width($("body").width() - 260);
            fauitContent.style.width = "100%";
            mqttJoint();
        }
    });

    window.setTimeout(function () {
        if (receivealarm) {
            console.log("ary.length:" + ary.length);
            var num = 5;
            if (ary.length < 5) {
                num = ary.length;
            }
            for (var i = 0; i < num; i++) {
                $("#scrollcontent").append(ary[i]);
            }
            ary.length = 0;
            timer = window.setInterval(move, 500);
            fauitContent.onmouseover = function () {
                window.clearInterval(timer);
            };
            fauitContent.onmouseout = function () {
                timer = window.setInterval(move, 500);
            };
        }
    }, 3000);

    var timeflag = 0;

    function move() {
        console.log("move:" + fauitstr);
        if (!fauitstr) {
            console.log("timeflag:" + timeflag);
            timeflag++;
            if (timeflag >= 200) {
                $("#scrollcontent").html("");
                timeflag = 0;
            }
        } else {
            fauitContent.scrollLeft++;
            if (fauitContent.scrollLeft + $("#fauitContent").width() >= $("#scrollcontent").width()) {//当前数据是否已经显示完成
                $("#scrollcontent").append(ary.pop());
            }
        }
    }

    var str = "";

    function content(fauit, gatherno) {
        str = "<span style='color:red'>采集编号:" + gatherno + "" + fauit + "!!!  " + " </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        // console.log(fauit+index);
        ary.push(str);
        fauitstr = str;
    }


    //连接mq
    var client, clientId;

    function mqttJoint() {
        clientId = Math.random().toString().substr(3, 8) + Date.now().toString(36);
        client = new Paho.MQTT.Client('localhost', parseInt(8083), clientId);
        var options = {
            timeout: 5,
            keepAliveInterval: 10,
            cleanSession: false,
            useSSL: false,
            onSuccess: onConnect,
            onFailure: function (e) {
                console.log("failed:" + e.errorCode);
            },
            reconnect: true
        }
        client.onConnectionLost = onConnectionLost;
        client.onMessageArrived = onMessageArrived;
        client.connect(options);
    }

    function onConnect() {
        console.log("onConnect mqtt");
        client.subscribe("weldmesrealdata", {
            qos: 0,
            onSuccess: function (e) {
                console.log("订阅成功");
            },
            onFailure: function (e) {
                console.log("订阅失败：" + e.errorCode);
            }
        });
    }

    function onConnectionLost(responseObject) {
        if (responseObject.errorCode !== 0) {
            console.log("onConnectionLost:" + responseObject.errorMessage);
        }
    }

    function onMessageArrived(message) {
        var redata = message.payloadString;
        if (redata.length === 405 || redata.length % 135 === 0) {
            for (var i = 0; i < redata.length; i += 135) {
                var mstatus = redata.substring(36 + i, 38 + i);//故障状态
                var gatherno = redata.substring(8 + i, 12 + i);
                switch (mstatus) {
                    case "01":
                        content("E-010 焊枪开关OFF等待", gatherno);
                        break;
                    case "02":
                        content("E-000工作停止", gatherno);
                        break;
                    case "10":
                        content("E-100控制电源异常", gatherno);
                        break;
                    case "15":
                        content("E-150一次输入电压过高", gatherno);
                        break;
                    case "16":
                        content("E-160一次输入电压过低", gatherno);
                        break;
                    case "20":
                        content("E-200一次二次电流检出异常", gatherno);
                        break;
                    case "21":
                        content("E-210电压检出异常", gatherno);
                        break;
                    case "22":
                        content("E-220逆变电路反馈异常", gatherno);
                        break;
                    case "30":
                        content("E-300温度异常", gatherno);
                        break;
                    case "70":
                        content("E-700输出过流异常", gatherno);
                        break;
                    case "71":
                        content("E-710输入缺相异常", gatherno);
                        break;
                    case "98":
                        content("超规范停机", gatherno);
                        break;
                    case "99":
                        content("超规范报警", gatherno);
                        break;
                    default:
                        break;
                }
            }
        }
    }
</script>
</body>
</html>