$(function(){
	insframeworkTree();
	weldingMachineDatagrid();
});

function weldingMachineDatagrid(){
	$("#weldingmachineTable").datagrid( {
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		pageSize : 200,
		pageList : [ 10, 20, 50, 100, 200 ],
		url : "weldingMachine/getWedlingMachineList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false, 
		fitColumns : true,
        columns : [ [ {
			field : 'id',
			title : '序号',
			width : 50,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'equipmentNo',
			title : '设备名称',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'typeName',
			title : '设备类型',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'joinTime',
			title : '入厂时间',
			width : 170,
			halign : "center",
			align : "left"
		}, {
			field : 'insframeworkName',
			title : '所属项目',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'statusName',
			title : '状态',
			width : 80,
			halign : "center",
			align : "left"
		} , {
			field : 'manufacturerName',
			title : '厂家',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'isnetworkingId',
			title : '是否在网',
			width : 100,
			halign : "center",
			align : "center",
			formatter:function (value,row,index){
				var str = "";
				if (value == 0){
					str += "是";
				}else if (value == 1){
					str += "否";
				}
				return str;
			}
		}, {
			field : 'gatherId',
			title : '采集序号',
			width : 200,
			halign : "center",
			align : "center"
		}, {
			field : 'position',
			title : '位置',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'ip',
			title : '手持终端IP地址',
			width : 150,
			halign : "center",
			align : "center"
		}, {
			field : 'modelname',
			title : '型号',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'model',
			title : '型号id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'statusId',
			title : '状态id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'manuno',
			title : '厂商id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'typeId',
			title : '类型id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'iId',
			title : '项目id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'gid',
			title : '采集模块id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}/*, {
			field : 'edit',
			title : '编辑',
			width : 150,
			halign : "center",
			align : "left",
			formatter:function(value,row,index){
				var str = "";
				str += '<a id="edit" class="easyui-linkbutton" href="javascript:editMachine('+row.iId+','+row.id+','+true+')"/>';
				str += '<a id="remove" class="easyui-linkbutton" href="javascript:editMachine('+row.iId+','+row.id+','+false+')"/>';
//				str += '<a id="maintain" class="easyui-linkbutton" href="weldingMachine/goMaintain?wid='+row.id+'"/>';
				return str;
			}
		}*/
		] ],
		pagination : true,
//		fitColumns : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
//                color.class="rowColor";
                return color;
            }
        }
// 		onLoadSuccess:function(data){
// 	        $("a[id='edit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
// 	        $("a[id='remove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
// //	        $("a[id='maintain']").linkbutton({text:'维修记录',plain:true,iconCls:'icon-update'});
// 		}
	});
}

function editMachine(flags){
	var id="",wid="";
	var row = null;
	row = $('#weldingmachineTable').datagrid('getSelected');
	if (row) {
		id = row.iId;
		wid = row.id;
	}else{
		alert("请先选择一条数据。");
		return;
	}
	$.ajax({  
        type : "post",  
        async : false,
        url : "insframework/getUserAuthority?id="+id,  
        data : {},  
        dataType : "json", //返回数据形式为json  
        success : function(result) {
            if (result) {
        		if(result.afreshLogin==null || result.afreshLogin==""){
            		if(result.flag){
//            			var url = "";
            			if(flags){
            				editWeldingMachine();
//            				url = "weldingMachine/goEditWeldingMachine?wid="+wid;
            			}else{
//            				url = "weldingMachine/goremoveWeldingMachine?wid="+wid;
            				removeWeldingMachine();
            			}
//	       				var img = new Image();
//	       			    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
//	       			    url = img.src;  // 此时相对路径已经变成绝对路径
//	       			    img.src = null; // 取消请求
//	       				window.location.href = encodeURI(url);
            		}else{
            			alert("对不起，您不能对你的上级或同级部门的数据进行编辑");
            		}
            	}else{
            		$.messager.confirm("提示",result.afreshLogin,function(data){
		        		 if(data){
		        			var url = "login.jsp";
		       				var img = new Image();
		       			    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		       			    url = img.src;  // 此时相对路径已经变成绝对路径
		       			    img.src = null; // 取消请求
		     				 top.location.href = url;
		     			 }
		     		 });
		        }
           }
        },  
        error : function(errorMsg) {  
            alert("数据请求失败，请联系系统管理员!");  
        }  
   });
}

//导出到excel
function exportWeldingMachine(){
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exporWeldingMachine";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url);
		}
	});
}

//导入
function importclick(){
	$("#importdiv").dialog("open").dialog("setTitle","从excel导入数据");
}

function importWeldingMachine(){
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return false;
	}else{
		$('#importfm').form('submit', {
			url : "import/importWeldingMachine",
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
					if (!result.success) {
						$.messager.show( {
							title : 'Error',
							msg : result.msg
						});
					} else {
						$('#importdiv').dialog('close');
						$('#weldingmachineTable').datagrid('reload');
						$.messager.alert("提示", result.msg);
					}
				}
				
			},  
		    error : function(errorMsg) {  
		        alert("数据请求失败，请联系系统管理员!");  
		    } 
		});
	}
}

function insframeworkTree(){
	$("#myTree").tree({  
		onClick : function(node){
			$("#weldingmachineTable").datagrid('load',{
				"parent" : node.id
			})
		 }
	})
}

function searchData(){
	var search = "";
	var sequipmentNo = $("#sequipmentNo").textbox('getValue');
	var stId = $("#stId").combobox("getValue");
	var sjoinTime = $("#sjoinTime").datetimebox('getValue');
	var siId = $("#siId").combobox("getValue");
	var smanuno = $("#smanuno").combobox("getValue");
	var sgid = $("#sgid").combobox("getValue");
	var sposition = $("#sposition").textbox('getValue');
	var sip = $("#sip").textbox('getValue');
	var smodel = $("#smodel").combobox("getValue");
	// var sid = "";
	// if($("input[name='statusId']:checked").val()){
	// 	sid = $("input[name='statusId']:checked").val();
	// }
	// var isnetworking = "";
	// if($("input[name='sisnetworkingId']:checked").val()){
	// 	isnetworking = $("input[name='sisnetworkingId']:checked").val();
	// }
	if(sequipmentNo != ""){
		if(search == ""){
			search += " fequipment_no LIKE "+"'%" + sequipmentNo + "%'";
		}else{
			search += " AND fequipment_no LIKE "+"'%" + sequipmentNo + "%'";
		}
	}
	if(stId != ""){
		if(search == ""){
			search += " di.fvalue LIKE "+"'%" + stId + "%'";
		}else {
			search += " AND di.fvalue LIKE "+"'%" + stId + "%'";
		}
	}
	if(sjoinTime != ""){
		if(search == ""){
			search += " fjoin_time=to_date(substr('" + sjoinTime + "',1,19), 'yyyy-mm-dd hh24:mi:ss')";
		}else{
			search += " AND fjoin_time=to_date(substr('" + sjoinTime + "',1,19), 'yyyy-mm-dd hh24:mi:ss')";
		}
	}
	if(siId != ""){
		if(search == ""){
			search += " i.fid = "+ siId;
		}else{
			search += " AND i.fid = "+ siId;
		}
	}
	if(smanuno != ""){
		if(search == ""){
			search += " w.fmanufacturer_id LIKE "+"'%" + smanuno + "%'";
		}else{
			search += " AND w.fmanufacturer_id LIKE "+"'%" + smanuno + "%'";
		}
	}
	if(sgid != ""){
		if(search == ""){
			search += " g.fid = "+ sgid;
		}else{
			search += " AND g.fid = " + sgid;
		}
	}
	if(sposition != ""){
		if(search == ""){
			search += " fposition LIKE "+"'%" + sposition + "%'";
		}else{
			search += " AND fposition LIKE "+"'%" + sposition + "%'";
		}
	}
	if(sip != ""){
		if(search == ""){
			search += " w.fIP LIKE "+"'%" + sip + "%'";
		}else{
			search += " AND w.fIP LIKE "+"'%" + sip + "%'";
		}
	}
	if(smodel != ""){
		if(search == ""){
			search += " dict.fvalue LIKE "+"'%" + smodel + "%'";
		}else{
			search += " AND dict.fvalue LIKE "+"'%" + smodel + "%'";
		}
	}
	// if(sid != ""){
	// 	if(search == ""){
	// 		search += " d.fvalue LIKE "+"'%" + sid + "%'";
	// 	}else{
	// 		search += " AND d.fvalue LIKE "+"'%" + sid + "%'";
	// 	}
	// }
	// if(isnetworking != ""){
	// 	if(search == ""){
	// 		search += " fisnetworking LIKE "+"'%" + isnetworking + "%'";
	// 	}else{
	// 		search += " AND fisnetworking LIKE "+"'%" + isnetworking + "%'";
	// 	}
	// }
	$('#weldingmachineTable').datagrid('load', {
		"searchStr" : search
	});
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#weldingmachineTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}

