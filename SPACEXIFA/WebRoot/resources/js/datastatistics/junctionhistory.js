var ipurl = "";
var xAxisData = new Array(), yAxisData = new Array(), fieldArr = new Array(), divArr = new Array();
var eleUpLine = 0, eleDownLine = 0, volUpLine = 0, volDownLine = 0;
$(function () {
    historyDatagrid();
    $("#little").hide();
    $("#body1").height($("#elebody").height() - 30);
    $.ajax({
        type: "post",
        async: true,
        url: "hierarchy/getUserInsframework",
        data: {},
        dataType: "json",
        success: function (result) {
            ipurl = result.ipurl;
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    })
    $('#ftype').combobox('clear');
    $('#ftype').combobox('loadData', [{
        "text": "正常",
        "value": "1"
    }, {
        "text": "返修",
        "value": "2"
    },]);
})
var time1 = new Array();
var vol = new Array();
var ele = new Array();
var query = {};
var searchStr = "";

function setParam() {
    var job_number_search = $("#job_number_search").textbox('getValue');
    var set_number_search = $("#set_number_search").textbox('getValue');
    var part_drawing_search = $("#part_drawing_search").textbox('getValue');
    var part_name_search = $("#part_name_search").textbox('getValue');
    var fjunction_id = $("#fjunction_id").textbox('getValue');
    var ftype = $("#ftype").combobox('getValue');
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    if (job_number_search != "") {
        if (searchStr == "") {
            searchStr += " u.JOB_NUMBER LIKE " + "'%" + job_number_search + "%'";
        } else {
            searchStr += " AND u.JOB_NUMBER LIKE " + "'%" + job_number_search + "%'";
        }
    }
    if (set_number_search != "") {
        if (searchStr == "") {
            searchStr += " u.SET_NUMBER LIKE " + "'%" + set_number_search + "%'";
        } else {
            searchStr += " AND u.SET_NUMBER LIKE " + "'%" + set_number_search + "%'";
        }
    }

    if (part_drawing_search != "") {
        if (searchStr == "") {
            searchStr += " u.PART_DRAWING_NUMBER LIKE " + "'%" + part_drawing_search + "%'";
        } else {
            searchStr += " AND u.PART_DRAWING_NUMBER LIKE " + "'%" + part_drawing_search + "%'";
        }
    }
    if (part_name_search != "") {
        if (searchStr == "") {
            searchStr += " u.PART_NAME LIKE " + "'%" + part_name_search + "%'";
        } else {
            searchStr += " AND u.PART_NAME LIKE " + "'%" + part_name_search + "%'";
        }
    }
    if (fjunction_id != "") {
        if (searchStr == "") {
            searchStr += " J.JUNCTION_NAME LIKE " + "'%" + fjunction_id + "%'";
        } else {
            searchStr += " AND J.JUNCTION_NAME LIKE " + "'%" + fjunction_id + "%'";
        }
    }
    if (ftype != "") {
        if (searchStr == "") {
            searchStr += " t.FREPAIRTYPE  =" + ftype + "";
        } else {
            searchStr += " AND t.FREPAIRTYPE ="+ ftype + "";
        }
    }
    if (dtoTime1 != "") {
        if (searchStr == "") {
            searchStr += " t.FREALSTARTTIME >= " + "to_date('" + dtoTime1 + "', 'yyyy-mm-dd hh24:mi:ss')";
        } else {
            searchStr += " AND t.FREALSTARTTIME >= " + "to_date('" + dtoTime1 + "', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
    if (dtoTime2 != "") {
        if (searchStr == "") {
            searchStr += " t.FREALENDTIME <= " + "to_date('" + dtoTime2 + "', 'yyyy-mm-dd hh24:mi:ss')";
        } else {
            searchStr += " AND t.FREALENDTIME <= " + "to_date('" + dtoTime2 + "', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
}

function historyDatagrid() {
    searchStr = "";
    setParam();
    $("#dg").datagrid({
        fitColumns: true,
        height: $("#tableDiv").height(),
        width: $("#tableDiv").width(),
        idField: 'fid',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "datastatistics/getJunctionHistoryList?searchStr=" + searchStr,
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
            align: "center"
        }, {
            field: 'job_number',
            title: '工作号',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'set_number',
            title: '部套号',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'part_number',
            title: '零件图号',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'part_name',
            title: '零件名',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'junction_name',
            title: '焊缝名称',
            width: 190,
            halign: "center",
            align: "center"
        }, {
            field: 'welder_no',
            title: '焊工姓名',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'machine_num',
            title: '焊机编号',
            width: 90,
            halign: "center",
            align: "center"
        }, {
            field: 'firsttime',
            title: '开始时间',
            width: 150,
            halign: "center",
            align: "center"
        }, {
            field: 'lasttime',
            title: '终止时间',
            width: 150,
            halign: "center",
            align: "center"
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
            }]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onSelect: function (index, row) {
            //var search = "&junction_id="+row.junction_id+"&machid="+row.machid+"&task_id="+row.task_id+"&welderid="+row.welderid;
            document.getElementById("load").style.display = "block";
            var sh = '<div id="show" style="width:150px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>';
            $("#body").append(sh);
            document.getElementById("show").style.display = "block";
            chartStr = "";
            setParam();
            query = {
                "query": {
                    "bool": {
                        "must": [
                            {"match": {"fmachine_id": row.machid}},
                            //{"match": {"fjunction_id": row.junction_id}},
                            //{"match": {"fwelder_id": row.welderid}},
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
                                    "gte": new Date(row.firsttime).toISOString(),
                                    "lte": new Date(row.lasttime).toISOString()
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
                url: 'http://192.168.11.3:9200/tb_live_data/_search?pretty=true',
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
                    for (var i in result) {
                        ele.push(result[i]._source.felectricity);
                        vol.push(result[i]._source.fvoltage);
                        time1[i] = utc2beijing(result[i]._source.fweldtime);
                    }
                    console.log(ele.length);
                    eleChart();
                    volChart();
                    document.getElementById("show").style.display = "none";
                    document.getElementById("load").style.display = "none";
                    //alert(json.hits.hits);
                    // if (result) {
                    // 	var eleVolRange = result.value;
                    // 	if(eleVolRange!=""){
                    // 		eleUpLine = eleVolRange.split(",")[1];
                    // 		eleDownLine = eleVolRange.split(",")[2];
                    // 		volUpLine = eleVolRange.split(",")[3];
                    // 		volDownLine = eleVolRange.split(",")[4];
                    // 	}
                    // 	var date = eval(result.rows);
                    // 	if(date.length==0){
                    // 		document.getElementById("load").style.display ='none';
                    // 		document.getElementById("show").style.display ='none';
                    // 		alert("该时间内未查询到相关数据")
                    // 		eleUpLine = 0;
                    // 		eleDownLine = 0;
                    // 		volUpLine = 0;
                    // 		volDownLine = 0;
                    // 	}else{
                    // 		for(var i=0;i<date.length;i++){
                    // 			ele.push(date[i].ele);
                    // 			vol.push(date[i].vol);
                    // 			time1[i] = date[i].time;
                    // 		}
                    // 		eleChart();
                    // 		volChart();
                    // 		document.getElementById("load").style.display ='none';
                    // 		document.getElementById("show").style.display ='none';
                    // 		eleUpLine = 0;
                    // 		eleDownLine = 0;
                    // 		volUpLine = 0;
                    // 		volDownLine = 0;
                    // 	}
                    // }
                },
                error: function (xhr, message, error) {
                    console.error("Error while loading data from ElaStricSerach", message);
                    throw(error);
                    alert('error');
                }
            });
            //$('#swdetailtable').datagrid('clearSelections');
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

function searchHistory() {
    searchStr = "";
    setParam();
    $('#dg').datagrid("options").url = "datastatistics/getJunctionHistoryList";
    $('#dg').datagrid('load', {
        "searchStr": searchStr
    });
}

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
                        yAxis: eleUpLine
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

function loadxmlDoc(file) {
    try {
        //IE
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
    } catch (e) {
        //Firefox, Mozilla, Opera, etc
        xmlDoc = document.implementation.createDocument("", "", null);
    }

    try {
        xmlDoc.async = false;
        xmlDoc.load(file); //chrome没有load方法
    } catch (e) {
        //针对Chrome,不过只能通过http访问,通过file协议访问会报错
        var xmlhttp = new window.XMLHttpRequest();
        xmlhttp.open("GET", file, false);
        xmlhttp.send(null);
        xmlDoc = xmlhttp.responseXML.documentElement;
    }
    return xmlDoc;
}

function curve(value) {
    eval("myChart" + value.div + "=" + "echarts.init(document.getElementById('" + value.div + "'))");
    var option = {
        backgroundColor: '#fff',
//        title : {
//            text : value.name
//        },
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
            data: []
        }],
        yAxis: [{
            type: 'value',
            max: parseFloat(value.max),
            min: parseFloat(value.min)
        }],
        series: [{
            symbolSize: 5,//气泡大小
            name: value.name,
            type: 'line',//折线图
            data: [],
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
    eval("myChart" + value.div).setOption(option);
}