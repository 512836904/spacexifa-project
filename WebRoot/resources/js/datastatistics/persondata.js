$(function () {
    dgDatagrid();
})

var chartStr = "";

function setParam() {
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    chartStr += "?dtoTime1=" + dtoTime1 + "&dtoTime2=" + dtoTime2;
}

function dgDatagrid() {
    setParam();
    var grid = {
//            			 fitColumns : true,
        height: $("#body").height(),
        width: $("#body").width(),
        url: "datastatistics/getPersonData" + chartStr,
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        pagination: true,
        remoteSort: false,
        nowrap: false,
        columns: [
            [
                {
                    field: 't0',
                    title: '焊工编号',
                    width: 150,
                    halign: "center",
                    align: "left",
                    sortable: true,
                    sorter: function (a, b) {
                        return (a > b ? 1 : -1);
                    }
                },{
                    field: 't1',
                    title: '焊工名称',
                    width: 150,
                    halign: "center",
                    align: "left",
                    sortable: true,
                    sorter: function (a, b) {
                        return (a > b ? 1 : -1);
                    }
                },  {
                field: 't2',
                title: '焊接任务数',
                width: 80,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't3',
                title: '焊接时间',
                width: 150,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't4',
                title: '工作时间',
                width: 150,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't5',
                title: '焊接效率(%)',
                width: 120,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't6',
                title: '焊丝消耗(KG)',
                width: 120,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't7',
                title: '电能消耗(KWH)',
                width: 120,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't8',
                title: '气体消耗(L)',
                width: 120,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }, {
                field: 't9',
                title: '规范符合率(%)',
                width: 120,
                halign: "center",
                align: "left",
                sortable: true,
                sorter: function (a, b) {
                    return (a > b ? 1 : -1);
                }
            }
            ]
        ],
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onBeforeLoad: function (param) {
            $("#chartLoading").hide();
        }
    };
    $('#dg').datagrid(grid);
    // var column = new Array();
    // $.ajax({
    //      type : "post",
    //      async : false,
    //      url : "datastatistics/getPersonData"+chartStr,
    //      data : {},
    //      dataType : "json", //返回数据形式为json
    //      success : function(result) {
    //          if (result) {
    //          	 for(var i=0;i<result.ary.length;i++){
    //          		 column.push({field:"t"+i,title:result.ary[i].title,width:100,halign : "center",align : "left",sortable : true,
    //          				sorter : function(a,b){
    //          					return (a>b?1:-1);
    //          				}});
    //           	 }
    //          	 var grid = {
    //          			 fitColumns : true,
    //      				 height : $("#body").height(),
    //      				 width : $("#body").width(),
    //      				 url : "datastatistics/getPersonData"+chartStr,
    //      				 pageSize : 10,
    //      				 pageList : [ 10, 20, 30, 40, 50 ],
    //      				 singleSelect : true,
    //      				 rownumbers : true,
    //      				 showPageList : false,
    //      				 pagination : true,
    //      				 remoteSort : false,
    //      				 nowrap : false,
    //      				 columns : [column],
    //      				 rowStyler: function(index,row){
    //      					 if ((index % 2)!=0){
    //      		            	 //处理行代背景色后无法选中
    //      		            	 var color=new Object();
    //      		                 return color;
    //      		             }
    //      		         },
    //      		         onBeforeLoad : function(param){
    //   		        		$("#chartLoading").hide();
    //      		         }
    //               };
    //          	 $('#dg').datagrid(grid);
    //          	 $('#dg').datagrid('loadData', result.rows);
    //          }
    //      },
    //      error : function(errorMsg) {
    //          alert("数据请求失败，请联系系统管理员!");
    //      }
    // });
}

function serach() {
    $("#chartLoading").show();
    chartStr = "";
    setTimeout(function () {
        dgDatagrid();
    }, 500);
}

//导出到Excel
function exportExcel() {
    chartStr = "";
    setParam();
    $.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！", function (result) {
        if (result) {
            var url = "export/exportPersonData";
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
        height: $("#body").height(),
        width: $("#body").width()
    });
}