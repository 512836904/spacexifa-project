$(function () {
    //dgDatagrid();
    //workgas();
    // setTimeout(function(){
    //     setWorkCharts();
    // }, 2000);
    wpslibDatagrid();
})

var chartStr = "";
var searchStr = "";
var workary = [];
function setParam() {
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    var junctionname = $("#junctionname").val();
    var ftype = $("#ftype").combobox('getValue');
   // var ftype = $("#ftype").val();
    if(junctionname != ""){
        if(searchStr == ""){
            searchStr += " junction_name LIKE "+"'%" + junctionname + "%'";
        }else{
            searchStr += " AND junction_name LIKE "+"'%" + junctionname + "%'";
        }
    }
    chartStr += "&dtoTime1=" + dtoTime1 + "&dtoTime2=" + dtoTime2+"&ftype=" + ftype;
}

function wpslibDatagrid(){
    parameterStr1();
    var url1 = encodeURI("datastatistics/getjunctionalltime?search="+searchStr);
    $("#taskviewtable").datagrid( {
        fitColumns : false,
        height: $("#wpsTableDiv").height(),
        width: $("#wpsTableDiv").width(),
        idField : 'fid',
        pageSize : 50,
        pageList : [ 10, 20, 30, 40, 50 ],
        url : url1,
        singleSelect : true,
        rownumbers : true,
        showPageList : false,
        columns : [ [ {
            field : 'fid',
            title : '序号',
//			width : 30,
            halign : "center",
            align : "left",
            hidden:true
        },{
            field : 'fprefix_number',
            title : '工作号',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fsuffix_number',
            title : '部套号',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fproduct_number',
            title : '工艺编号',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'junctionname',
            title : '焊缝名称',
            width : 200,
            halign : "center",
            align : "left"
        }, {
            field : 'fwelder_name',
            title : '焊工姓名',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'worktime',
            title : '焊接时长',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'alltime',
            title : '工作时长',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'electric',
            title : '电能消耗(KW/H)',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'materialwast',
            title : '焊丝消耗(KG)',
            width : 100,
            halign : "center",
            align : "left"
        },{
            field : 'airwast',
            title : '气体消耗(L)',
            width : 100,
            halign : "center",
            align : "left"
        }, {
            field : 'weldingproductivity',
            title : '焊接效率(%)',
            width : 100,
            halign : "center",
            align : "left"
        }] ],
        pagination : true,
        rowStyler: function(index,row){
            if ((index % 2)!=0){
                //处理行代背景色后无法选中
                var color=new Object();
                return color;
            }
        }
    });
}


// function dgDatagrid() {
//     setParam();
//     var url1 = encodeURI("datastatistics/getWorkpieceData?search="+searchStr+"&chartStr="+chartStr);
//     var column = new Array();
//     $.ajax({
//         type: "post",
//         async: false,
//         url: url1,
//         data: {},
//         dataType: "json", //返回数据形式为json
//         success: function (result) {
//             if (result) {
//                 workary = result.arys;
//                 var str = ["焊缝名称", "焊接时间", "工作时间", "焊接效率(%)", "焊丝消耗(KG)", "电能消耗(KWH)", "气体消耗(L)", "规范符合率(%)"];
//                 for (var i = 0; i < str.length; i++) {
//                     column.push({
//                         field: "t" + i,
//                         title: str[i],
//                         width: 200,
//                         halign: "center",
//                         align: "left",
//                         sortable: true,
//                         sorter: function (a, b) {
//                             return (a > b ? 1 : -1);
//                         }
//                     });
//                 }
//                 var grid = {
//                     fitColumns: true,
//                     height: 600,
//                     width: 1050,
//                     url: url1,
//                     pageSize: 50,
//                     pageList: [10, 20, 30, 40, 50],
//                     singleSelect: true,
//                     rownumbers: true,
//                     showPageList: false,
//                     pagination: true,
//                     remoteSort: false,
//                     nowrap: false,
//                     columns: [column],
//                     rowStyler: function (index, row) {
//                         if ((index % 2) != 0) {
//                             //处理行代背景色后无法选中
//                             var color = new Object();
//                             return color;
//                         }
//                     },
//                     onBeforeLoad: function (param) {
//                         $("#chartLoading").hide();
//                     }
//                 };
//                 $('#dg').datagrid(grid);
//                 $('#dg').datagrid('loadData', result.rows);
//             }
//         },
//         error: function (errorMsg) {
//             alert("数据请求失败，请联系系统管理员!");
//         }
//     });
// }

function serach() {
    // $("#chartLoading").show();
    // chartStr = "";
    // setTimeout(function () {
    //     dgDatagrid();
    // }, 500);
    wpslibDatagrid();
}

function parameterStr1(){
    searchStr = "";
    var dt1 = $("#dtoTime1").datetimebox('getValue');
    var dt2 = $("#dtoTime2").datetimebox('getValue');
    //var item = $("#item").combobox('getValue');
    var product_drawing_no = $("#product_drawing_no").val();
    var product_name = $("#product_name").val();
    var taskno = $("#taskno").val();
    var fwps_lib_num = $("#fwps_lib_num").val();
    var fwelded_junction_no = $("#fwelded_junction_no").val();
    var product_number = $("#product_number").val();
    var junction_name = $("#fwelded_junction_no").val();
    var welderno = $("#welderno").val();
    var ftype = $("#ftype").combobox('getValue');
    if(welderno != ""){
        if(searchStr == ""){
            searchStr += " e.fname LIKE "+"'%" + welderno + "%'";
        }else{
            searchStr += " AND e.fname LIKE "+"'%" + welderno + "%'";
        }
    }
    if(ftype != ""){
        if(searchStr == ""){
            searchStr += " w.FPRODUCT_NUMBER_ID = '" + ftype + "'";
        }else{
            searchStr += " AND w.FPRODUCT_NUMBER_ID = '" + ftype + "'";
        }
    }
    if(product_drawing_no != ""){
        if(searchStr == ""){
            searchStr += " j.JOB_NUMBER LIKE "+"'%" + product_drawing_no + "%'";
        }else{
            searchStr += " AND j.JOB_NUMBER LIKE "+"'%" + product_drawing_no + "%'";
        }
    }
    if(product_name != ""){
        if(searchStr == ""){
            searchStr += " j.SET_NUMBER LIKE "+"'%" + product_name + "%'";
        }else {
            searchStr += " AND j.SET_NUMBER LIKE "+"'%" + product_name + "%'";
        }
    }
    if(junction_name != ""){
        if(searchStr == ""){
            searchStr += " u.junction_name LIKE "+"'%" + junction_name + "%'";
        }else {
            searchStr += " AND u.junction_name LIKE "+"'%" + junction_name + "%'";
        }
    }
    if(taskno != ""){
        if(searchStr == ""){
            searchStr += " c.fname LIKE "+"'%" + taskno + "%'";
        }else{
            searchStr += " AND c.fname LIKE "+"'%" + taskno + "%'";
        }
    }
    if(fwelded_junction_no != ""){
        if(searchStr == ""){
            searchStr += " u.junction_name LIKE "+"'%" + fwelded_junction_no + "%'";
        }else{
            searchStr += " AND u.junction_name LIKE "+"'%" + fwelded_junction_no + "%'";
        }
    }

    if(dt1 != ""){
        if(searchStr == ""){
            searchStr += " fstarttime >to_date('" +dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
        }else{
            searchStr += " AND fstarttime >to_date('"+dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
    if(dt2 != ""){
        if(searchStr == ""){
            searchStr += " fendtime < to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
        }else{
            searchStr += " AND fendtime <to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
        }
    }
}


//导出到Excel
function exportExcel() {
    chartStr = "";
    setParam();
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exportWorkpieceData";
            var img = new Image();
            img.src = url;  // 设置相对路径给Image, 此时会发送出请求
            url = img.src;  // 此时相对路径已经变成绝对路径
            img.src = null; // 取消请求
            window.location.href = encodeURI(url + chartStr);
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
        height: 600,
        width: 1050
    });
}

function itemcombobox() {
    /*	$.ajax({
          type : "post",
          async : false,
          url : "datastatistics/getAllInsframework",
          data : {},
          dataType : "json", //返回数据形式为json
          success : function(result) {
              if (result) {
                  var optionStr = '';
                  for (var i = 0; i < result.ary.length; i++) {
                      optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                              + result.ary[i].name + "</option>";
                  }
                  $("#item").html(optionStr);
              }
          },
          error : function(errorMsg) {
              alert("数据请求失败，请联系系统管理员!");
          }
        });
        $("#item").combobox();*/

    $.ajax({
        type: "post",
        async: false,
        url: "weldtask/getOperateArea",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                if (result.type == 23) {
                    var zoptionStr = "";
                    var boptionStr = "";
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    for (var j = 0; j < result.banzu.length; j++) {
                        boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"
                            + result.banzu[j].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#bitem").html(boptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', result.ary[0].id);
                    $("#bitem").combobox();
                    $("#bitem").combobox('select', result.banzu[0].id);
//		        	$("#zitem").combobox({disabled: true});
//		        	$("#bitem").combobox({disabled: true});
                } else if (result.type == 22) {
                    var zoptionStr = "";
                    var boptionStr = '<option value="0">请选择</option>';
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    for (var j = 0; j < result.banzu.length; j++) {
                        boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"
                            + result.banzu[j].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#bitem").html(boptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', result.ary[0].id);
                    $("#bitem").combobox();
                    $("#bitem").combobox('select', 0);
//		        	$("#zitem").combobox({disabled: true});
                } else {
                    $("#bitem").combobox({disabled: true});
                    var zoptionStr = '<option value="0">请选择</option>';
                    for (var i = 0; i < result.ary.length; i++) {
                        zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    $("#zitem").html(zoptionStr);
                    $("#zitem").combobox();
                    $("#zitem").combobox('select', 0);
                }

            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });

    $("#zitem").combobox({
        onChange: function (newValue, oldValue) {
            if (oldValue != "") {
                $.ajax({
                    type: "post",
                    async: false,
                    url: "weldtask/getTeam?searchStr=" + " and i.fparent=" + newValue,
                    data: {},
                    dataType: "json", //返回数据形式为json
                    success: function (result) {
                        if (result) {
                            var boptionStr = '<option value="0">请选择</option>';
                            for (var i = 0; i < result.ary.length; i++) {
                                boptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                                    + result.ary[i].name + "</option>";
                            }
                            $("#bitem").html(boptionStr);
                            $("#bitem").combobox();
                            $("#bitem").combobox('select', 0);
                            $("#bitem").combobox({disabled: false});
                        }
                    },
                    error: function (errorMsg) {
                        alert("数据请求失败，请联系系统管理员!");
                    }
                });
            }
        }
    })

    $("#zitem").combobox('select', 0);
}


function workgas() {
    // 1. 实例化对象
    var myChart = echarts.init(document.querySelector("#workgas"));
    // 2. 指定配置和数据
    var option = {
        tooltip: {
            trigger: "axis",
            axisPointer: {
                type: "shadow",
                label: {
                    show: true
                }
            }
        },
        grid: {
            top: "30%",
            left: "5%",
            right: "5%",
            bottom: "0%",
            containLabel: true
        },
        legend: {
            data: ["工作时间", "焊接时间"],
            top: "top",
            left: "7%",
            itemWidth: 16, // 图例标记图形宽度
            itemHeight: 10,
            icon: 'rect', // 图例项的icon
            textStyle: {
                color: 'rgb(92, 183, 215)',
            }
        },
        xAxis: {
            data: ['工区一', '工区二', '工区三', '工区四', '工区五'],
            axisLine: {
                show: false, //隐藏X轴轴线
                lineStyle: {
                    color: "#3d5269",
                    width: 1
                }
            },
            axisTick: {
                show: false, //隐藏X轴刻度
                alignWithLabel: true
            },
            axisLabel: {
                show: true,
                textStyle: {
                    color: 'rgb(92, 183, 215)' //X轴文字颜色
                    // fontWeight: 'bold'
                },
                interval: 0
            }
        },
        yAxis: [{
            type: "value",
            name: "工作时间(H)",
            nameLocation: 'end', // 坐标轴名称显示位置
            min: 0,
            max: 100,
            interval: 20,
            nameTextStyle: {
                // left: '2%',
                color: 'rgb(92, 183, 215)',
            },
            splitLine: {
                show: true,
                lineStyle: { // 分割线样式
                    color: 'rgb(14, 33, 127)' // 统一分割线颜色
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            axisLabel: {
                show: true,
                textStyle: {
                    color: 'rgb(92, 183, 215)'
                }
            }
        },
            {
                type: "value",
                name: "焊接时间（h）",
                min: 0,
                max: 2000,
                interval: 400,
                nameTextStyle: {
                    color: 'rgb(92, 183, 215)'
                },
                position: "right",
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: "#396A87",
                        width: 2
                    }
                },
                axisLabel: {
                    show: true,
                    formatter: "{value}", //右侧Y轴文字显示
                    textStyle: {
                        color: 'rgb(92, 183, 215)'
                    }
                }
            }
        ],
        dataZoom: [
            {
                id: 'dataZoomX',
                type: 'slider',
                xAxisIndex: [0],
                filterMode: 'filter'
            }
        ],
        series: [{
            name: "工作时间",
            type: "bar",
            yAxisIndex: 0,
            barWidth: 18,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 1,
                        color: "#ff5d1f"
                    },
                        {
                            offset: 0,
                            color: "#ffc11f"
                        }
                    ])
                }
            },
            data: [25, 45, 55, 25.2, 58]
        },
            {
                name: "焊接时间",
                type: "bar",
                yAxisIndex: 1,
                barWidth: 18,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 1,
                            color: "#058bf0"
                        },
                            {
                                offset: 0,
                                color: "#27d7e8"
                            }
                        ])
                    }
                },
                data: [835.07, 1256.78, 1632.57, 800, 1235.91]
            }
        ]
    }

    // 3. 把配置给实例对象
    myChart.setOption(option);
    // 4. 让图表跟随屏幕自动的去适应
    window.addEventListener("resize", function () {
        myChart.resize();
    });
}

function setWorkCharts(){
    var ins =[];
    var data1 = [];
    var data2 = [];
    if (workary.length > 0){
        for (var index in workary){
            var name = workary[index].name;
            var starttime = workary[index].starttime;
            var worktime = workary[index].worktime;
            ins.push(name);
            data1.push(starttime);
            data2.push(worktime);
        }
    }
    var teamgas = echarts.init(document.querySelector("#workgas"));
    var option = teamgas.getOption();
    option.xAxis[0].data = ins;
    // option.xAxis[0].axisLabel.formatter =
    //     function(value) {
    //         return value.split("").join("\n");
    //     }
    // option = newline(option, 6, 'xAxis')

    option.xAxis[0].axisLabel.formatter =
        function (value) {
            var newParamsName = "";
            var paramsNameNumber = value.length;
            var provideNumber = 20;
            var rowNumber = Math.ceil(paramsNameNumber / provideNumber);
            if (paramsNameNumber > provideNumber) {
                for (var p = 0; p < rowNumber; p++) {
                    var tempStr = "";
                    var start = p * provideNumber;
                    var end = start + provideNumber;
                    if (p == rowNumber - 1) {
                        tempStr = value.substring(start, paramsNameNumber);
                    } else {
                        tempStr = value.substring(start, end) + "\n";
                    }
                    newParamsName += tempStr;
                }
            } else {
                newParamsName = value;
            }
            return newParamsName
        }
    option.series[0].data = data1;
    if(data1.length>0){
        option.yAxis[0].max=Math.ceil(Math.max(...data1));
        option.yAxis[0].min=0;
        option.yAxis[0].interval=Math.ceil(Math.max(...data1)/5);
    }
    option.series[1].data = data2;
    if(data2.length>0){
        option.yAxis[1].max=Math.ceil(Math.max(...data2));
        option.yAxis[1].min=0;
        option.yAxis[1].interval=Math.ceil(Math.max(...data2)/5);
    }
    teamgas.setOption(option);
}