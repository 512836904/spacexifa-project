<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE  HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">

<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <title>质量管理展示</title>
	<script type="text/javascript" src="resources/js/js/jquery.js"></script>
	<script type="text/javascript" src="resources/js/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/js/echarts.js"></script>
	<script type="text/javascript" src="resources/js/js/echarts.min1.js"></script>
	<script type="text/javascript" src="resources/js/js/flexible.js"></script>
	<!-- import Vue before Element -->
	<script type="text/javascript" src="resources/js/js/vues.js"></script>
	<!-- import JavaScript -->
	<script type="text/javascript" src="resources/js/js/indexs.js"></script>
    <link rel="stylesheet" href="resources/css/css/indexq.css" />

</head>

<body>
    <!-- 头部的盒子 -->
    <header>
        <h1><span>质量管理展示</span></h1>
        <div class="leftCompany"></div>
        <div class="rightTime"></div>
		<div class="logo" style="float:left;"><img id="logo_img" style="width: 50%;" class="img" src="resources/images/images/logo.png"/></div>
        <div class="exchange" id="quanping">
            <button style="background-color: #022986;width: 170%;height: 150%;" onclick="javascript:requestFullScreen()"><span style="color: #ffffff;">全屏</span></button>
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
			<div class="panel" style="height:6.05rem;">
                <div class="panel01" style="float:left" id="workareas">
                    <h2 class="h2-mid"><span>工区焊接工艺规范符合率 ( % )</span></h2>
                </div>
				 <div class="panel01" style="float:left;display:none;" id="teamwork" >
                    <h2 class="h2-mid"><span>班组焊接工艺规范符合率 ( % )</span></h2>
                </div>
				<div id="one_day" class="pane501" style="float:left">
                    <a onclick="javascript:one(1);">
                        <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当日情况</span></h2>
                    </a>
                </div>
				<div  id="one_day1" class="pane501" style="float:left;display:none;">
                    <a onclick="javascript:one(0);">
                        <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当月情况</span></h2>
                    </a>
                </div>
                <div class="panel02">
                    <div class="chart1" id="pchart1">
					<div id="Left1" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
					<div style="width:10%;height:10%;float:left;padding-top:2.5rem;" >
						 <button type="button" id="button_1" onclick="javascript:gobutton_2();" style="width:65px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;"><img style="width: 100%" src="resources/images/images/jiantou.PNG" ></button></div>
					</div>
					<div class="chart2" id="pchart2" style="display:none;">
					<div id="Left2" style="height:90%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
					<div style="width:10%;height:10%;float:left;padding-top:2.5rem;" >
						 <button type="button" id="button_2"  onclick="javascript:gobutton_1();" style="width:65px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;"><img style="width: 100%" src="resources/images/images/jiantou.PNG" ></button></div>
					</div>
                    <div class="panel-footer"></div>
                </div>
            </div>
			
			<div class="panel" style="height: 6.05rem;">
                <div class="panel01" style="float:left">
                    <h2 class="h2-mid"><span>超规范信息表</span></h2>
                </div>
				<div id="one_day2" class="pane501" style="float:left">
                    <a onclick="javascript:one_1(1);">
                        <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当日情况</span></h2>
                    </a>
                </div>
				<div  id="one_day3" class="pane501" style="float:left;display:none;">
                    <a onclick="javascript:one_1(0);">
                        <img id="u3913_img" class="img " src="resources/images/images/c09_u3493.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当月情况</span></h2>
                    </a>
                </div>
                <div class="panel02">
                    <div class="chart" id="Left3" style="width: 100%;">
                        <div id="app">
                            <el-table :fit="true" :data="tableData" :cell-style="{padding: '0', height: '34px'}"
                                      height="350" border style="width: 100%;overflow: auto;">

                                <el-table-column prop="work_area" label="工区"></el-table-column>

                                <el-table-column prop="person" label="人员"></el-table-column>

                                <el-table-column prop="gzh" label="工作号"></el-table-column>

                                <el-table-column prop="bth" label="部套号"></el-table-column>

                                <el-table-column prop="ljm" label="零件名"></el-table-column>
								
								<el-table-column prop="time" label="时间"></el-table-column>

                            </el-table>
                        </div>
                    </div>
					<div class="panel-footer"></div>
                </div>
            </div>
        </div>

        <!-- 第二列 -->
        <div class="column2">
            <div class="panel" style="height: 6.05rem;">
                <div class="panel01" style="float:left">
                    <h2 class="h2-mid"><span>工作号/部套号焊接工艺规范符合率 ( % )</span></h2>
                </div>
				<div id="one_day4" class="pane501" style="float:left">
                    <a onclick="javascript:one_2(1);">
                        <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当日情况</span></h2>
                    </a>
                </div>
				<div  id="one_day5" class="pane501" style="float:left;display:none;">
                    <a onclick="javascript:one_2(0);">
                        <img id="u3913_img" class="img" src="resources/images/images/c09_u3493.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当月情况</span></h2>
                    </a>
                </div>
                <div class="panel03">
                    <div class="chart4" id="Right1" style="height:98%;width:98%;overflow:auto;"> </div>
					<div class="panel-footer"></div>
                </div>
            </div>
            <div class="panel" style="height: 6.05rem;">
                <div class="panel01" id="workstandr" style="float:left">
                    <h2 class="h2-mid"><span>工区超规范停机次数（次）</span></h2>
                </div>
				 <div class="panel01" id="teamstander" style="float:left;display:none;">
                    <h2 class="h2-mid"><span>班组超规范停机次数（次）</span></h2>
                </div>
				<div id="one_day6" class="pane501" style="float:left">
                    <a onclick="javascript:one_3(1);">
                        <img id="u3913_img" class="img " src="resources/images/images/c11_u3913.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当日情况</span></h2>
                    </a>
                </div>
				<div  id="one_day7" class="pane501" style="float:left;display:none;">
                    <a onclick="javascript:one_3(0);">
                        <img id="u3913_img" class="img" src="resources/images/images/c09_u3493.png"/>
                        <h2 id="u3912" class="h2-mid" ><span>当月情况</span></h2>
                    </a>
                </div>
                <div class="panel02">
					<div class="chart1" id="schart1">
					<div id="Right2_1" style="height:100%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
					<div style="width:9%;height:10%;float:left;padding-top:2.5rem;">
						 <button type="button" id="button_3" onclick="javascript:gobutton_4();" style="width:65px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;"><img style="width: 100%" src="resources/images/images/jiantou.PNG"></button></div>
					</div>
					<div class="chart2" id="schart2" style="display:none;">
					<div id="Right2_0" style="height:100%;width:90%;overflow:auto;padding-left:5px;float:left;"></div>
					<div style="width:9%;height:10%;float:left;padding-top:2.5rem;" >
						 <button type="button" id="button_4"  onclick="javascript:gobutton_3();" style="width:65px;height:40px;border-width: 0px;border-radius:3px;background:#070b3f;cursor:pointer;outline: none;"><img style="width: 100%" src="resources/images/images/jiantou.PNG"></button></div>
					</div>
                    <div class="panel-footer"></div>
                </div>
            </div>
        </div>
    </section>

</body>

<script src="resources/js/js/quality.js"></script>
<script src="resources/js/js/loadQualityData.js"></script>

<script>
    var startTime = 0;
    var appvue;
    appvue = new Vue({
        el: '#app',
        data() {
            return {
                tableData: [{
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
					work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }, {
                    work_area: '二工区',
                    person: '张三',
					gzh: '0',
					bth: '11',
                    ljm: '基础法兰',
                    time: '2020-11-11'
                }]
            }
        }
    });
    //捕捉屏幕变化（）
    document.addEventListener ("fullscreenchange", FShandler);
    document.addEventListener ("webkitfullscreenchange", FShandler);
    document.addEventListener ("mozfullscreenchange", FShandler);
    document.addEventListener ("MSFullscreenChange", FShandler);
    function FShandler() {
        if (document.exitFullscreen){
            $("#quanping").show();
        }
    }

    //查询超规范信息
    function loadSupergage(day){
        startTime = day;
        $.ajax({
            type: "post",
            async: true,
            url: "frontEnd/findSupergageInfo",
            data: {
                startTime: startTime
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                if (result) {
                    var tablearray = [];
                    var data = result.ary;
                    var supergage_status = result.supergage_status;//超规范是否展示
                    if(supergage_status==1) {
                        for (var index in data) {
                            //查询对象数组是否存在某个值，存在则返回下标，不存在返回-1
                            var gzh_num = tablearray.map(a => a.gzh).indexOf(data[index].job_number);
                            var bth_num = tablearray.map(a => a.bth).indexOf(data[index].set_number);
                            var ljm_num = tablearray.map(a => a.ljm).indexOf(data[index].part_name);
                            var work_area_num = tablearray.map(a => a.work_area).indexOf(data[index].insname);
                            var person_num = tablearray.map(a => a.person).indexOf(data[index].name);
                            var time_num = tablearray.map(a => a.time).indexOf(data[index].starttime);
                            if (gzh_num == -1 || bth_num == -1 || ljm_num == -1 || work_area_num == -1 || person_num == -1 || time_num == -1) {
                                let feild = {};
                                feild["gzh"] = data[index].job_number;
                                feild["bth"] = data[index].set_number;
                                feild["ljm"] = data[index].part_name;
                                feild["work_area"] = data[index].insname;
                                feild["person"] = data[index].name;
                                feild["time"] = data[index].starttime;
                                tablearray.push(feild);
                            }
                        }
                    }
                    appvue.$delete(appvue.tableData);
                    Object.assign(appvue.$data,appvue.$options.data());
                    appvue.tableData = tablearray;
                }
            }
        });
    }
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
    .el-table--enable-row-hover .el-table__body tr:hover>td {
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