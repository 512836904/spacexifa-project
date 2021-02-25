<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE  HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">

<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <title>焊接管理展板</title>
    <link rel="stylesheet" href="resources/css/css/indexz.css"/>
    <link rel="stylesheet" href="resources/css/css/element-ui.css">
    <script type="text/javascript" src="resources/js/js/jquery.js"></script>
    <script type="text/javascript" src="resources/js/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resources/js/js/echarts.js"></script>
    <script type="text/javascript" src="resources/js/js/echarts.min1.js"></script>
    <script type="text/javascript" src="resources/js/js/flexible.js"></script>
    <script type="text/javascript" src="resources/js/swfobject.js"></script>
    <script type="text/javascript" src="resources/js/web_socket.js"></script>
    <script type="text/javascript" src="resources/js/tableExcel.js"></script>
    <script type="text/javascript" src="resources/js/js/vues.js"></script>
    <script type="text/javascript" src="resources/js/js/paho-mqtt.js"></script>
    <script type="text/javascript" src="resources/js/js/paho-mqtt-min.js"></script>
    <script type="text/javascript" src="resources/js/js/indexs.js"></script>
</head>

<body>
<!-- 头部的盒子 -->
<header>
    <h1><span>焊接管理界面</span></h1>
    <div class="leftCompany"></div>
    <div class="rightTime"></div>
    <div class="logo" style="float:left;"><img id="logo_img" style="width: 50%;height: 40%;" class="img" src="resources/images/images/logo.png"/></div>
    <div class="exchange" id="quanping">
        <button style="background-color: #022986;width: 170%;height: 150%;" onclick="javascript:requestFullScreen()">
            <span style="color: #ffffff;">全屏</span></button>
    </div>
    <script>
        var t = null;
        t = setTimeout(time, 1000); //開始运行
        function time() {
            clearTimeout(t); //清除定时器
            dt = new Date();
            var y = dt.getFullYear();
            var mt = dt.getMonth() + 1;
            var day = dt.getDate();
            var h = dt.getHours(); //获取时
            var m = dt.getMinutes(); //获取分
            var s = dt.getSeconds(); //获取秒
            document.querySelector(".rightTime").innerHTML = h + ":" + m + ":" + s + "  丨  " + y + "-" + mt + "-" + day;
            t = setTimeout(time, 1000); //设定定时器，循环运行
        }
    </script>

</header>

<!-- 页面主体部分 -->
<section class="mainbox">
    <!-- 第一列 -->
    <div class="column1">
        <div class="panel panel-header">
            <h2></h2>
            <div class="chart" id="Left1" style="width:7.0rem;height: 3.9rem;"></div>
            <div class="panel-footer"></div>
        </div>

        <div class="panel">
            <div class="panel01" style="float:left">
                <h2 class="h2-side"><span>焊工工作时间排行 ( h )</span></h2>
            </div>
            <div id="one_day" class="pane501" style="float:left">
                <a onclick="javascript:one(1);">
                    <img id="u3913_img" class="img" src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day1" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one(0);">
                    <img id="u3913_img" class="img" src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <div class="chart" id="Left2" style="overflow:auto;"></div>
                <div class="panel-footer"></div>
            </div>
        </div>

        <div class="panel">
            <div class="panel01" id="allworkgas" style="float:left">
                <h2 class="h2-side"><span>工区电能、气体消耗量 ( KWH、L )</span></h2>
            </div>
            <div class="panel01" style="float:left;display:none;" id="allteamgas">
                <h2 class="h2-side"><span>班组电能、气体消耗量 ( KWH、L )</span></h2>
            </div>
            <div id="one_day4" class="pane501" style="float:left">
                <a onclick="javascript:one_2(1);">
                    <img id="u3913_img" class="img" src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day5" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_2(0);">
                    <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <div class="chart" id="vchart1">
                    <div id="workgas" style="height:90%;width:98%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:2%;height:10%;float:left;padding-top:1.3rem;">
                        <button type="button" id="button_1" onclick="javascript:gobutton_2();" style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"/>
                        </button>
                    </div>
                </div>
                <div class="chart" id="vchart2" style="display:none;">
                    <div id="teamgas" style="height:90%;width:99%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:1%;height:10%;float:left;padding-top:1.3rem;">
                        <button type="button" id="button_2" onclick="javascript:gobutton_1();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </div>


    <!-- 第二列 -->
    <div class="column2">
        <div class="panel" style="height: 6.05rem;">
            <div class="panel01" style="float:left" id="allworkarea">
                <h2 class="h2-mid"><span>工区焊接工艺规范符合率 ( % )</span></h2>
            </div>
            <div class="panel01" style="float:left;display:none;" id="allteamwork">
                <h2 class="h2-mid"><span>班组焊接工艺规范符合率 ( % )</span></h2>
            </div>
            <div id="one_day2" class="pane501" style="float:left">
                <a onclick="javascript:one_1(1);">
                    <img id="u3914_img" class="img " src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3915" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day3" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_1(0);">
                    <img id="u3914_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3915" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <div class="chart2" id="pchart1">
                    <div id="workareas" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:10%;height:10%;float:left;padding-top:2.5rem;">
                        <button type="button" id="button_9" onclick="javascript:gobutton_10();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG">
                        </button>
                    </div>
                </div>
                <div class="chart2" id="pchart2" style="display:none;">
                    <div id="teamworks" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:10%;height:10%;float:left;padding-top:2.5rem;">
                        <button type="button" id="button_10" onclick="javascript:gobutton_9();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
        <div class="panel" style="height: 6.05rem;">
            <!-- 0：查询焊工焊接工作时长 -->
            <input type="hidden" id="standbyTime" readonly="readonly" value="0">
            <div class="panel01" style="float:left" id="allworkwelder">
                <h2 class="h2-mid"><span>工区焊工人均工作时间 ( h )</span></h2>
            </div>
            <div class="panel01" style="float:left;display:none;" id="allteamwelder">
                <h2 class="h2-mid"><span>班组焊工人均工作时间 ( h )</span></h2>
            </div>
            <div id="one_day6" class="pane501" style="float:left">
                <a onclick="javascript:one_3(1);">
                    <img id="u3914_img" class="img " src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3915" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day7" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_3(0);">
                    <img id="u3914_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3915" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <div class="chart2" id="vchart3">
                    <div id="workwelders" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:10%;height:10%;float:left;padding-top:2.5rem;">
                        <button type="button" id="button_3" onclick="javascript:gobutton_4();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="chart2" id="vchart4" style="display:none;">
                    <div id="teamwelder" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:10%;height:10%;float:left;padding-top:2.5rem;">
                        <button type="button" id="button_4" onclick="javascript:gobutton_3();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </div>

    <!-- 第三列 -->
    <div class="column3">
        <div class="panel">
            <div class="panel01" style="float:left;" id="allworkmateral">
                <h2 class="h2-side"><span>工区耗材消耗量 ( KG )</span></h2>
            </div>
            <div class="panel01" style="float:left;display:none;" id="allteammateral">
                <h2 class="h2-side"><span>班组耗材消耗量 ( kG )</span></h2>
            </div>
            <div id="one_day8" class="pane501" style="float:left">
                <a onclick="javascript:one_4(1);">
                    <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day9" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_4(0);">
                    <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <!--<div class="chart" id="Right1"></div>-->
                <div class="chart" id="vchart5">
                    <div id="workmaterals" style="height:90%;width:98%;padding-left:5px;float:left"></div>
                    <div style="height:10%;width:2%;padding-top:1.0rem;float:left">
                        <button type="button" id="button_5" onclick="javascript:gobutton_6();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="chart" id="vchart6" style="display:none;">
                    <div id="teammaterals"
                         style="height:90%;width:98%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:2%;height:10%;float:left;padding-top:1.0rem;">
                        <button type="button" id="button_6" onclick="javascript:gobutton_5();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
        <div class="panel">
            <div class="panel01" style="float:left;" id="allworkradio">
                <h2 class="h2-side"><span>工区设备使用率 ( % )</span></h2>
            </div>
            <div class="panel01" style="float:left;display:none;" id="allteamradio">
                <h2 class="h2-side"><span>班组设备使用率 ( % )</span></h2>
            </div>
            <div id="one_day10" class="pane501" style="float:left">
                <a onclick="javascript:one_5(1);">
                    <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当日情况</span></h2>
                </a>
            </div>
            <div id="one_day11" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_5(0);">
                    <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02">
                <div class="chart" id="vchart7">
                    <div id="workradios" style="height:90%;width:98%;padding-left:5px;float:left"></div>
                    <div style="height:10%;width:2%;padding-top:1.0rem;float:left">
                        <button type="button" id="button_8" onclick="javascript:gobutton_8();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="chart" id="vchart8" style="display:none;">
                    <div id="teamradios" style="height:90%;width:98%;overflow:auto;padding-left:5px;float:left;"></div>
                    <div style="width:2%;height:10%;float:left;padding-top:1.0rem;">
                        <button type="button" id="button_7" onclick="javascript:gobutton_7();"
                                style="width:50px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;">
                            <img style="width: 100%" src="resources/images/images/jiantou.PNG"></button>
                    </div>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
        <div class="panel">
            <div class="panel01">
                <h2 class="h2-side"><span>工作号/部套号相关信息</span></h2>
            </div>
            <div id="one_day12" class="pane501" style="float:left">
                <a onclick="javascript:one_6(1);">
                    <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当日情况</span></h2>
                </a>

            </div>
            <div id="one_day13" class="pane501" style="float:left;display:none;">
                <a onclick="javascript:one_6(0);">
                    <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                    <h2 id="u3912" class="h2-mid"><span>当月情况</span></h2>
                </a>
            </div>
            <div class="panel02" style="overflow: auto;">
                <div class="chart" id="Right3" style="width: 100%;">
                    <div id="app">
                        <el-table :fit="true" :data="tableData" :cell-style="{padding: '0', height: '28px'}"
                                  height="300" border style="width: 100%;overflow: auto;">

                            <el-table-column prop="gzh" label="工作号"></el-table-column>

                            <el-table-column prop="bth" label="部套号"></el-table-column>

                            <el-table-column prop="ljm" label="零件名"></el-table-column>

                            <el-table-column prop="gzzsc" label="工作总时长"></el-table-column>

                            <el-table-column prop="zhcxh" label="总耗材消耗"></el-table-column>

                        </el-table>
                    </div>
                </div>

                <div class="panel-footer"></div>
            </div>

        </div>
    </div>

</section>


</body>
<script type="text/javascript" src="resources/js/js/index.js"></script>
<script type="text/javascript" src="resources/js/js/loadIndexData.js"></script>
<script>
    var startTime = 0;
    var appvue;
    //import {loadWorkNumber} from 'resources/js/js/loadIndexData.js'
    appvue = new Vue({
        el: '#app',
        data() {
            return {
                tableData: [{
                    gzh: 'A001233-1',
                    bth: 'FTG100601',
                    ljm: '基础法兰',
                    gzzsc: '15',
                    zhcxh: '200'
                }, {
                    gzh: 'A001233-1',
                    bth: 'FTG100601',
                    ljm: '基础法兰',
                    gzzsc: '15',
                    zhcxh: '200'
                }, {
                    gzh: 'A001233-1',
                    bth: 'FTG100601',
                    ljm: '基础法兰',
                    gzzsc: '15',
                    zhcxh: '200'
                }, {
                    gzh: 'A001233-1',
                    bth: 'FTG100601',
                    ljm: '基础法兰',
                    gzzsc: '15',
                    zhcxh: '200'
                }, {
                    gzh: 'A001233-1',
                    bth: 'FTG100601',
                    ljm: '基础法兰',
                    gzzsc: '15',
                    zhcxh: '200'
                }]
            }
        },
        created: function () {
            // this.tableData = tablearray
        }
    });

    function loadJobSetNumber(day) {
        startTime = day;
        $.ajax({
            type: "post",
            async: true,
            url: "frontEnd/findJobSetNumber",
            data: {
                startTime: startTime
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                if (result) {
                    var tablearray = [];
                    var data = result.ary;
                    for (var index in data) {
                        let feild = {};
                        feild["gzh"] = data[index].JOB_NUMBER;
                        feild["bth"] = data[index].SET_NUMBER;
                        feild["ljm"] = data[index].PART_NAME;
                        feild["gzzsc"] = Number(data[index].worktime).toFixed(1);
                        feild["zhcxh"] = Number(data[index].wirefeedrate).toFixed(2);
                        tablearray.push(feild);
                    }
                    appvue.$delete(appvue.tableData);
                    Object.assign(appvue.$data, appvue.$options.data());
                    appvue.tableData = tablearray;
                }
            }
        });
    }

    //捕捉屏幕变化（）
    document.addEventListener("fullscreenchange", FShandler);
    document.addEventListener("webkitfullscreenchange", FShandler);
    document.addEventListener("mozfullscreenchange", FShandler);
    document.addEventListener("MSFullscreenChange", FShandler);

    function FShandler() {
        if (document.exitFullscreen) {
            $("#quanping").show();
        }
    }

    // window.onresize = function (){
    //     if (document.exitFullscreen){
    //         $("#quanping").show();
    //     }
    // }
</script>
<style>
    /* Element 样式覆盖 */

    .el-table {
        background-color: transparent;
    }

    .el-table th,
    .el-table tr,
    .el-table td {
        background-color: transparent;
        color: #00f2f1;
        text-align: center;
    }

    .el-table th {
        background-color: rgba(20, 60, 133, 0.4);
    }

    .el-table td,
    .el-table th.is-leaf,
    .el-table--border,
    .el-table--group {
        border-color: #193882;
    }

    .el-table--border::after,
    .el-table--group::after,
    .el-table::before {
        background-color: #193882;
    }

    /* 表格hover */
    .el-table--enable-row-hover .el-table__body tr:hover > td {
        background-color: transparent;
    }


    /* element-ui table的右侧滚动条的样式   ::代表全局 */
    ::-webkit-scrollbar {
        width: 2px;
        height: 5px;
        /*background-color: #16889A;*/
    }

    /*  滚动条的滑块 */
    ::-webkit-scrollbar-thumb {
        background-color: #16889a;
        border-radius: 10px;
    }
</style>

</html>