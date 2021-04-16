var eleUpLine = 0, eleDownLine = 0, volUpLine = 0, volDownLine = 0;
var emqurl = "";
var maxele = "";
var fjunction_id = "";
var welderid = "";
var dtoTime1 = "";
var dtoTime2 = "";
var product_drawing_no = "";
var product_name = "";
var taskno = "";
var fwelded_junction_no = "";
var junction_name = "";
var weldername = "";
var weldernum = "";
var zitem = "";
var bitem = "";
var chartvalue = "";
var part_number = "";
var part_name = "";
var machinenum = "";
var fstatus = "";
$(function () {
    $("#little").hide();
    $("#body1").height($("#elebody").height() - 30);
    var fjunction_id = $("#fjunction_id").val();
    $.ajax({
        type: "post",
        async: false,
        url: "datastatistics/getMaxele?fjunction="+fjunction_id,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                maxele=result.maxele;
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: "td/AllTdbf",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                //127.0.0.1:8083
                // websocketURL = eval(result.web_socket);
                websocketURL = result.web_socket;
                if(websocketURL.includes("10.110.11.3")){
                    //websocketURL = "10.110.11.3:9200"
                    emqurl='http://10.110.11.3:9200/tb_live_data/_search?pretty=true'
                }else{
                    //websocketURL = "192.168.11.3:9200"
                    emqurl='http://192.168.11.3:9200/tb_live_data/_search?pretty=true'
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    Junction();
})

function setParam() {
    chartStr = "";
    fjunction_id = $("#fjunction_id").val();
    welderid = $("#fid").val();
    machinid = $("#machin_id").val();
    dtoTime1 = $("#dto1").val();
    dtoTime2 = $("#dto2").val();
    chartStr = "?fjunction_id=" + fjunction_id + "&welderid=" + welderid + "&machineid=" + machinid + "&dtoTime1=" + dtoTime1+ "&dtoTime2=" + dtoTime2;
}
function goback(){
    fjunction_id = $("#fjunction_id").val();
    welderid = $("#fid").val();
    machinid = $("#machin_id").val();
    Time1 = $("#dtoTime1").val();
    Time2 = $("#dtoTime2").val();
    product_drawing_no = $("#product_drawing_no").val();
    product_name = $("#product_name").val();
    part_number = $("#part_number").val();
    part_name = $("#part_name").val();
    taskno = $("#taskno").val();
    fwelded_junction_no = $("#fwelded_junction_no").val();
    fjunction_id = $("#fjunction_id").val();
    weldername = $("#weldername").val();
    weldernum = $("#weldernum").val();
    machinenum = $("#machinenum").val();
    zitem = $("#zitem").val();
    bitem = $("#bitem").val();
    type = $("#type").val();
    fstatus = $("#fstatus").val();
    if(zitem != ""){
        chartvalue += "&zitem="+zitem;
    }
    if(bitem != ""){
        chartvalue += "&bitem="+bitem;
    }
    if(Time1 != ""){
        chartvalue += "&Time1="+Time1;
    }
    if(Time2 != ""){
        chartvalue += "&Time2="+Time2;
    }
    if(product_drawing_no != ""){
        chartvalue += "&product_drawing_no="+product_drawing_no;
    }
    if(product_name != ""){
        chartvalue += "&product_name="+product_name;
    }
    if(part_number != ""){
        chartvalue += "&part_number="+part_number;
    }
    if(part_name != ""){
        chartvalue += "&part_name="+encodeURI(part_name);
    }
    if(taskno != ""){
        chartvalue += "&taskno="+taskno;
    }
    if(fwelded_junction_no != ""){
        chartvalue += "&fwelded_junction_no="+encodeURI(fwelded_junction_no);
    }
    if(weldername != ""){
        chartvalue += "&weldername="+encodeURI(weldername);
    }
    if(weldernum != ""){
        chartvalue += "&weldernum="+encodeURI(weldernum);
    }
    if(machinenum != ""){
        chartvalue += "&machinenum="+encodeURI(machinenum);
    }
    if(type != ""){
        chartvalue += "&type="+type;
    }
    if(fstatus != ""){
        chartvalue += "&fstatus="+fstatus;
    }
    window.location.href=encodeURI("datastatistics/goweldpartermeter?chartvalue="+chartvalue);
}
var time1 = new Array();
var vol = new Array();
var ele = new Array();
var query = {};

function Junction() {
    setParam();
    $("#dg").datagrid({
        fitColumns: true,
        height: $("body").height() / 2,
        width: $("body").width(),
        idField: 'id',
        pageSize: 50,
        pageList: [10, 20, 30, 40, 50],
        url: "weldedjunction/getWeldingJun" + chartStr,
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'id',
            title: '序号',
            width: 30,
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'taskno',
            title: '任务编号',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'job_number',
            title: '工作号',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'set_number',
            title: '部套号',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'part_number',
            title: '零件图号',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'part_name',
            title: '零件名',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'junction_name',
            title: '焊缝名称',
            width: 90,
            halign: "center",
            align: "left"
        }, {
            field: 'welder_no',
            title: '焊工姓名',
            width: 90,
            halign: "center",
            align: "left"
        }, /*{
			field : 'maxElectricity',
			title : '电流上限',
			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'minElectricity',
			title : '电流下限',
			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'maxValtage',
			title : '电压上限',
			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'minValtage',
			title : '电压下限',
			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'machine_num',
			title : '焊机编号',
			width : 150,
			halign : "center",
			align : "left"
		},*/
            {
                field: 'firsttime',
                title: '开始时间',
                width: 150,
                halign: "center",
                align: "left"
            }, {
                field: 'lasttime',
                title: '终止时间',
                width: 150,
                halign: "center",
                align: "left"
            },
            // {
            // 	field : 'worktime',
            // 	title : '焊接时间(h)',
            // 	width : 150,
            // 	halign : "center",
            // 	align : "left"
            // },
            {
                field: 'machid',
                title: '焊机id',
                width: 90,
                halign: "center",
                align: "left",
                hidden: true
            }, {
                field: 'welderid',
                title: '焊工id',
                width: 90,
                halign: "center",
                align: "left",
                hidden: true
            }, {
                field: 'junction_id',
                title: '焊缝id',
                width: 90,
                halign: "center",
                align: "left",
                hidden: true
            }, {
                field: 'task_id',
                title: '任务id',
                width: 90,
                halign: "center",
                align: "left",
                hidden: true
            },]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onSelect: function (index, row) {
            document.getElementById("load").style.display = "block";
            var sh = '<div id="show" style="width:150px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>';
            $("#bodys").append(sh);
            document.getElementById("show").style.display = "block";
            chartStr = "";
            var firsttime = row.firsttime.substring(0,19);
            setParam();
            query = {
                "query": {
                    "bool": {
                        "must": [
                            //{"match": {"fmachine_id": row.machid}},
                            {"match": {"fjunction_id": fjunction_id}},
                            {"match": {"fwelder_id": welderid}},
                            {
                                "bool": {
                                    "should": [
                                        {
                                            "match": {
                                                "fstatus": "3"
                                            }
                                        },
                                        {
                                            "match": {
                                                "fstatus": "5"
                                            }
                                        },
                                        {
                                            "match": {
                                                "fstatus": "7"
                                            }
                                        },
                                        {
                                            "match": {
                                                "fstatus": "99"
                                            }
                                        }
                                    ]
                                }
                            }
                        ],
                        "filter": {
                            "range": {
                                "fuploaddatetime": {
                                    "gte": new Date(row.firsttime.substring(0,19)).toISOString(),
                                    "lte": new Date(row.lasttime.substring(0,19)).toISOString()
                                }
                            }
                        }
                    }
                },
                "from": 0,
                "size": 5000,
                "sort": [
                    {
                        "fuploaddatetime": {
                            "order": "asc"
                        }
                    }
                ]
            }
            $.ajax({
               // url: 'http://192.168.11.3:9200/tb_live_data/_search?pretty=true',
                url: emqurl,
                type: 'post',
                //url: "rep/historyCurve"+chartStr+"&fid="+encodeURIComponent($('#taskno').val())+"&mach="+$('#machid').val()+"&welderid="+$("#welderid").val()+"&fweld_bead="+fweld_bead+"&fsolder_layer="+fsolder_layer,
                contentType: 'application/json',
                crossDomain: true,
                async: true,
                data: JSON.stringify(query),
                dataType: "json",
                processData: false,      //序列化 data
                success: function (json, statusText, xhr) {
                    //$("#table").bootstrabool('load',json.hits.hits);
                    var result = json.hits.hits;
                    ele.splice(0,ele.length);
                    vol.splice(0,vol.length);
                    for (var i in result) {
                        ele.push(result[i]._source.felectricity);
                        vol.push(result[i]._source.fvoltage);
                        time1[i] = utc2beijing(result[i]._source.fweldtime);
                    }
                    // console.log(ele.length);
                    eleChart();
                    volChart();
                    document.getElementById("show").style.display = "none";
                    document.getElementById("load").style.display = "none";
                },
                error: function (xhr, message, error) {
                    console.error("Error while loading data from ElaStricSerach", message);
                    throw(error);
                    alert('error');
                }
            });
            // $('#swdetailtable').datagrid('clearSelections');
            // $('#swdetail').window('close');
        }
    });
}

function utc2beijing(utc_datetime) {
    // 转为正常的时间格式 年-月-日 时:分:秒
    var T_pos = utc_datetime.indexOf('T');
    var Z_pos = utc_datetime.indexOf('Z');
    var year_month_day = utc_datetime.substr(0, T_pos);
    var hour_minute_second = utc_datetime.substr(T_pos + 1, Z_pos - T_pos - 1);
    var new_datetime = year_month_day + " " + hour_minute_second; // 2017-03-31 08:02:06

    // 处理成为时间戳
    timestamp = new Date(Date.parse(new_datetime));
    timestamp = timestamp.getTime();
    timestamp = timestamp / 1000;

    // 增加8个小时，北京时间比utc时间多八个时区
    var timestamp = timestamp + 8 * 60 * 60;

    // 时间戳转为时间
    var beijing_datetime = new Date(parseInt(timestamp) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    return beijing_datetime; // 2017-03-31 16:02:06
}

// function loadChart(fsolder_layer,fweld_bead){
// 	time1 = new Array();
// 	vol = new Array();
// 	ele = new Array();
// 	document.getElementById("load").style.display="block";
// 	var sh = '<div id="show" style="width:150px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>';
// 	$("#bodys").append(sh);
// 	document.getElementById("show").style.display="block";
// 	chartStr = "";
// 	setParam();
// 	$.ajax({
// 		   type: "post",
// 		   url: "rep/historyCurve"+chartStr+"&fid="+encodeURIComponent($('#taskno').val())+"&mach="+$('#machid').val()+"&welderid="+$("#welderid").val()+"&fweld_bead="+fweld_bead+"&fsolder_layer="+fsolder_layer,
// 		   dataType: "json",
// 		   data: {},
// 		   success: function (result) {
// 		      if (result) {
// 		    	  var eleVolRange = result.value;
// 		    	  if(eleVolRange!=""){
// 		    		  eleUpLine = eleVolRange.split(",")[1];
// 		    		  eleDownLine = eleVolRange.split(",")[2];
// 		    		  volUpLine = eleVolRange.split(",")[3];
// 		    		  volDownLine = eleVolRange.split(",")[4];
// 		    	  }
// 		    	  var date = eval(result.rows);
// 		    	  if(date.length==0){
// 		    		  document.getElementById("load").style.display ='none';
// 		    		  document.getElementById("show").style.display ='none';
// 		    		  alert("该时间内未查询到相关数据")
// 		    		  eleUpLine = 0;
// 		    		  eleDownLine = 0;
// 		    		  volUpLine = 0;
// 		    		  volDownLine = 0;
// 		    	  }else{
// 			    	  for(var i=0;i<date.length;i++){
// 			    		  ele.push(date[i].ele);
// 			    		  vol.push(date[i].vol);
// 			    		  time1[i] = date[i].time;
// 			    	  }
// 			    	  eleChart();
// 			    	  volChart();
// 		    		  document.getElementById("load").style.display ='none';
// 		    		  document.getElementById("show").style.display ='none';
// 		    		  eleUpLine = 0;
// 		    		  eleDownLine = 0;
// 		    		  volUpLine = 0;
// 		    		  volDownLine = 0;
// 		    	  }
// 		      }
// 		   },
// 		   error: function () {
// 		      alert('error');
// 		   }
// 		});
// 	$('#swdetailtable').datagrid('clearSelections');
// 	$('#swdetail').window('close');
// }


function eleChart() {
    var myChart = echarts.init(document.getElementById('body1'));
    var option = {
        backgroundColor: '#fff',
        title: {
            text: '电流'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: false
                },
                dataView: {
                    show: false,
                    readOnly: false
                },
                restore: {
                    show: false
                },
                saveAsImage: {}
            }
        },
        visualMap: {
            show: false,
            dimension: 1,
            pieces: [],  //pieces的值由动态数据决定
            outOfRange: {
                color: "#A020F0"
            }
        },
        dataZoom: [
            {
                type: 'slider',
                show: true,
                xAxisIndex: [0]
            },
            {
                type: 'inside',
                xAxisIndex: [0]
            }
        ],
        grid: {
            left: '8%',//组件距离容器左边的距离
            right: '5%',
            top: "5%",
            bottom: 60
        },
        xAxis: [{
            type: 'category',
            data: time1
        }],
        yAxis: [{
            type: 'value',
            max: 500,
            min: 0
        }],
        series: [{
            symbolSize: 5,//气泡大小
            name: '电流',
            type: 'line',//折线图
            data: ele,
            markLine: {
                data: [
                    {
                        name: '预设电流上限',
                        yAxis: maxele
                    },
                    {
                        name: '预设电流下限',
                        yAxis: eleDownLine
                    }
                ],
                lineStyle: {
                    normal: {
                        color: 'red',
                    },
                },
            },
            itemStyle: {
                normal: {
                    color: "#A020F0",
                    lineStyle: {
                        color: "#A020F0"
                    }
                }
            }
        }]
    };
    var max = Math.max.apply(Math, ele); //数据的最大值
    option.series[0].data = ele;
    option.visualMap.pieces[0] = {gte: maxele, lte: max, color: 'red'};
    myChart.setOption(option);
}


function volChart() {
    var myChart = echarts.init(document.getElementById('body2'));
    var option = {
        backgroundColor: '#fff',
        title: {
            text: '电压'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: false
                },
                dataView: {
                    show: false,
                    readOnly: false
                },
                restore: {
                    show: false
                },
                saveAsImage: {}
            }
        },
        dataZoom: [//缩放
            {
                type: 'slider',
                show: true,
                xAxisIndex: [0]
            },
            {
                type: 'inside',
                xAxisIndex: [0]
            }
        ],
        grid: {
            left: '8%',//组件距离容器左边的距离
            right: '5%',
            top: "5%",
            bottom: 60,
            y2: $("#body2").height()//图表高度
        },
        xAxis: [{
            type: 'category',
            data: time1
        }],
        yAxis: [{
            type: 'value',
            max: 60,
            min: 0
        }],
        series: [{
            symbolSize: 5,//气泡大小
            name: '电压',
            type: 'line',//折线图
            data: vol,
            markLine: {
                data: [
                    {
                        name: '预设电压上限',
                        yAxis: volUpLine
                    },
                    {
                        name: '预设电压下限',
                        yAxis: volDownLine
                    }
                ],
                lineStyle: {
                    normal: {
                        color: 'red',
                    },
                },
            },
            itemStyle: {
                normal: {
                    color: "#87CEFA",
                    lineStyle: {
                        color: "#87CEFA"
                    }
                }
            }
        }]
    };
    myChart.setOption(option);
}

function serachCompanyOverproof() {
    Junction();
}

function fullScreen() {
    var row = $("#dg").datagrid('getSelected');
    if (row == null) {
        alert("请先选择焊口");
    } else {
        $("#elebody").height('50%');
        $("#elebody").css({'top': '0px'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('50%');
        $("#body2").css({'top': '50%'});
        echarts.init(document.getElementById('body1')).resize();
        echarts.init(document.getElementById('body2')).resize();
        $("#full").hide();
        $("#little").show();
    }
}

function theSmallScreen() {
    $("#elebody").height('25%');
    $("#elebody").css({'top': '58%'});
    $("#body1").height($("#elebody").height() - 23);
    $("#body2").height('20%');
    $("#body2").css({'top': '82%'});
    echarts.init(document.getElementById('body1')).resize();
    echarts.init(document.getElementById('body2')).resize();
    $("#full").show();
    $("#little").hide();
}

//导出到excel
function exportExcel(solder_layer, weld_bead) {
    setParam();
    var curveStr = "&taskno=" + encodeURIComponent($('#taskno').val()) + "&mach=" + $('#machid').val() + "&welderid=" + $("#welderid").val() + "&solder_layer=" + solder_layer + "&weld_bead=" + weld_bead
        + "&board=" + $('#boardlength').val();
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exportLiveData";
            var img = new Image();
            img.src = url;  // 设置相对路径给Image, 此时会发送出请求
            url = img.src;  // 此时相对路径已经变成绝对路径
            img.src = null; // 取消请求
            window.location.href = encodeURI(url + chartStr + curveStr);
        }
    });
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#dg").datagrid('resize', {
        height: $("#dgtb").height() / 2,
        width: $("#dgtb").width()
    });
    if ($("#full").is(":hidden")) {//全屏模式
        $("#elebody").height('50%');
        $("#elebody").css({'top': '0px'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('50%');
        $("#body2").css({'top': '50%'});
    } else {
        $("#elebody").height('25%');
        $("#elebody").css({'top': '58%'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('20%');
        $("#body2").css({'top': '82%'});
    }
    echarts.init(document.getElementById('body1')).resize();
    echarts.init(document.getElementById('body2')).resize();
}