<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>证件统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-cellediting.js"></script>
		<script type="text/javascript">
		var departmentSn = "";
		var judgeFirst = 1;
		//发送请求
		function loadAction(){
			$('#dg').datagrid('load',{
				'departmentSn':departmentSn
			});
		};
		$(function(){
			$('#department').combotree({
				url : '${pageContext.request.contextPath}/department/treeAll.action',//'${pageContext.request.contextPath}/office/appraisals/office_loadDepartment.action',
				valueField : 'id',    
		        textField : 'text',
				method : 'POST',
				queryParams:{'resourceSn' : '1702'},
				required : true,
				editable : false,
				width : 200,
				panelWidth : 300,
				panelHeight  :350,
				onLoadSuccess : function(node, data){
					if( judgeFirst == 1 ){
						if( data.length != 0 ){
							//将根节点的值默认输出
							$('#department').combotree( 'setValue', data[0].id );
							departmentSn = $( '#department').combotree( 'getValue' );
							$('#dg').datagrid('load', {
								'departmentSn':departmentSn
							});
							if(data.length == 1){
								var node = $('#department').combotree('tree').tree('find', data[0].id);
								$('#department').combotree('tree').tree('expand', node.target);
				        	}
						}
						judgeFirst++;
					}
				},
				onSelect:function(record){
					departmentSn=record.id;
					loadAction();
				} 
			});
			$('#dg').datagrid({
	            url: '${pageContext.request.contextPath}/certificate/certificateType_showData',
	            idField: 'id',
	            toolbar:'#danwei',
	            rownumbers: true,		//显示一个行号列
	            fitColumns:true,		//自动适应列
	           	fit:true,				//表格宽高自适应
	            nowrap:false,
	            striped:true,			//斑马线效果
				singleSelect:true,		//单行选中
	            loadmsg:'请等待...',		//加载等待时显示
				clickToEdit:false,
				dblclickToEdit:true,
	            pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:10,
	            pageList:[5,10,15,20,25,30],
	      		columns:[[{
					field : 'certificationTypeSn',
					title : '类型编号',
					width : 150
				} , {
					field : 'certificationTypeName',
					title : '类型名称',
					width : 150
				} , {
					field : 'count',
					title : '数量',
					width : 150
				}]],
				onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
				},
				onBeforeLoad:function(){
					 if(departmentSn=="" || departmentSn.length==0){
						 return false;
					 }else{
						 return true;
					 }
				}
			});
			
		}); 
		</script>
</head>
<body>
	<div id="danwei">
		<span>
			<lable for="department">选择部门：</lable>
			<input id="department" />
		</span>
	</div>
	<table id="dg" ></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>