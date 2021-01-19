$(function(){
	insframeworkTree();
	dgDatagrid();
})   

function dgDatagrid(){
	$("#dg").datagrid({
		height : $("#body").height()+30,
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldingMachine/getWedlingMachineList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		pagination : true,
		fitColumns : true,
		columns : [ [  {
			field : 'id',
			title : '序号',
			width : 50,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'equipmentNo',
			title : '固定资产编号',
			width : 150,
			halign : "center",
			align : "center"
		}, {
			field : 'typeName',
			title : '设备类型',
			width : 60,
			halign : "center",
			align : "center"
		}, {
			field : 'insframeworkName',
			title : '所属项目',
			width : 80,
			halign : "center",
			align : "center"
		}, {
			field : 'manufacturerName',
			title : '厂家',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'gatherId',
			title : '采集序号',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'position',
			title : '位置',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'ip',
			title : 'ip地址',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'model',
			title : '设备型号',
			width : 100,
			halign : "center",
			align : "center"
		}, {
			field : 'statusId',
			title : '状态id',
			width : 100,
			halign : "center",
			align : "center",
			hidden: true
		}, {
			field : 'isnetworkingId',
			title : '是否联网id',
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
			title : '采集id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'edit',
			title : '编辑',
			width : 130,
			halign : "center",
			align : "left",
			formatter:function(value,row,index){
			var str = "";
			str += '<a id="mc" class="easyui-linkbutton" href="weldedjunction/getWeldmachine?fid='+row.id+'"/>';
			return str;
			}
		}] ],
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess:function(data){
	        $("a[id='mc']").linkbutton({text:'任务信息',plain:true,iconCls:'icon-search'});
	    }
    });
}
                      
function insframeworkTree(){
	$("#myTree").tree({  
		onClick : function(node){
			$("#dg").datagrid('load',{
				"searchStr" : "i.fid="+node.id
			})
		 }
	})
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#dg").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}