$(function(){
	InsframeworkCombobox();
	//page();
	wpslibDatagrid();
})

var searchStr = "";
function wpslibDatagrid(){
	parameterStr1();
	var url1 = encodeURI("weldtask/getTaskview?search="+searchStr);
	$("#taskviewtable").datagrid( {
		fitColumns : false,
		height : '90%',
		width : '100%',
		idField : 'fid',
		pageSize : 200,
		pageList : [ 10, 20, 50, 100, 200 ],
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
			field : 'weldername',
			title : '焊工姓名',
			width : 100,
			halign : "center",
			align : "left"
		},{
			field : 'conname',
			title : '焊机编号',
			width : 100,
			halign : "center",
			align : "left"
		},{
			field : 'fproduct_drawing_no',
			title : '电子跟踪卡号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwpsnum',
			title : '任务编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_number',
			title : '产品序号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'dianame',
			title : '产品图号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_name',
			title : '产品名称',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fproduct_version',
			title : '产品版本',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fprocessname',
			title : '工艺规程编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwps_lib_version',
			title : '工艺规程版本号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fjunction',
			title : '焊缝编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fwelding_area',
			title : '焊接部位',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fspec_number',
			title : '工序号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fspec_name',
			title : '工序名称',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fspec_version',
			title : '工序版本',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fstep_number',
			title : '工步号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fstep_name',
			title : '工步名称',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fstep_version',
			title : '工步版本',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'fitem',
			title : '组织机构',
			width : 100,
			halign : "center",
			align : "center"
		},{
			field : 'flag',
			title : '工艺来源标志',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'flag_name',
			title : '工艺来源',
			width : 80,
			halign : "center",
			align : "center"
		}, {
			field : 'fstarttime',
			title : '任务开始时间',
			width : 100,
			halign : "center",
			align : "center"
		},{
			field : 'endtime',
			title : '任务结束时间',
			width : 100,
			halign : "center",
			align : "center"
		},{
			field : 'fstatus',
			title : '状态id',
//			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'touch_name',
			title : '任务状态',
			width : 100,
			halign : "center",
			align : "center",
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

function serach(){
	wpslibDatagrid();
}

function parameterStr1(){
	searchStr = "";
	var dt1 = $("#dtoTime1").datetimebox('getValue');
	var dt2 = $("#dtoTime2").datetimebox('getValue');
	var item = $("#item").combobox('getValue');
	var product_drawing_no = $("#product_drawing_no").val();
	var product_name = $("#product_name").val();
	var taskno = $("#taskno").val();
	var fwps_lib_num = $("#fwps_lib_num").val();
	var fwelded_junction_no = $("#fwelded_junction_no").val();
	var product_number = $("#product_number").val();
	var junction_name = $("#junction_name").val();
	//var welderno = $("#welderno").val();
	if(product_drawing_no != ""){
		if(searchStr == ""){
			searchStr += " fproduct_drawing_no LIKE "+"'%" + product_drawing_no + "%'";
		}else{
			searchStr += " AND fproduct_drawing_no LIKE "+"'%" + product_drawing_no + "%'";
		}
	}
	if(product_name != ""){
		if(searchStr == ""){
			searchStr += " l.fproduct_name LIKE "+"'%" + product_name + "%'";
		}else {
			searchStr += " AND l.fproduct_name LIKE "+"'%" + product_name + "%'";
		}
	}
	if(taskno != ""){
		if(searchStr == ""){
			searchStr += " j.ftask_no LIKE "+"'%" + taskno + "%'";
		}else{
			searchStr += " AND j.ftask_no LIKE "+"'%" + taskno + "%'";
		}
	}
	if(fwps_lib_num != ""){
		if(searchStr == ""){
			searchStr += " l.fwps_lib_version LIKE "+"'%" + fwps_lib_num + "%'";
		}else{
			searchStr += " AND l.fwps_lib_version LIKE "+"'%" + fwps_lib_num + "%'";
		}
	}
	if(fwelded_junction_no != ""){
		if(searchStr == ""){
			searchStr += " j.fwelded_junction_no LIKE "+"'%" + fwelded_junction_no + "%'";
		}else{
			searchStr += " AND j.fwelded_junction_no LIKE "+"'%" + fwelded_junction_no + "%'";
		}
	}
	if(product_number != ""){
		if(searchStr == ""){
			searchStr += " p.fproduct_number=" + product_number;
		}else{
			searchStr += " AND p.fproduct_number=" + product_number;
		}
	}
	if(item != ""){
		if(searchStr == ""){
			searchStr += " j.fitemId LIKE "+"'%" + item + "%'";
		}else{
			searchStr += " AND j.fitemId LIKE "+"'%" + item + "%'";
		}
	}
	if(junction_name != ""){
		if(searchStr == ""){
			searchStr += " u.fjunction=" + junction_name;
		}else{
			searchStr += " AND u.fjunction=" + junction_name;
		}
	}
	if(dt1 != ""){
		if(searchStr == ""){
			searchStr += " t.frealstarttime >to_date('" +dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
		}else{
			searchStr += " AND t.frealstarttime >to_date('"+dt1+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
	}
	if(dt2 != ""){
		if(searchStr == ""){
			searchStr += " t.frealstarttime < to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
		}else{
			searchStr += " AND t.frealstarttime <to_date('"+dt2+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
	}
}

//导出到Excel
function exportExcel(){
	parameterStr1();
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exportTaskview?search="+ encodeURI(encodeURI(searchStr))+""; 
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = url;
		}
	});
}

//所属项目
function InsframeworkCombobox(){
	$.ajax({  
  type : "post",  
  async : false,
  url : "weldingMachine/getInsframeworkAll",  
  data : {},  
  dataType : "json", //返回数据形式为json  
  success : function(result) {  
      if (result) {
          var optionStr = '';
          for (var i = 0; i < result.ary.length; i++) {  
              optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                      + result.ary[i].name + "</option>";
          }
          $("#fitemId").html(optionStr);
          $("#item").html(optionStr);
      }  
  },  
  error : function(errorMsg) {  
      alert("数据请求失败，请联系系统管理员!");  
  }  
	}); 
	$("#fitemId").combobox();
	$("#item").combobox();
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#taskviewtable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}