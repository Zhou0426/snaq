<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>重大隐患审核</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css">
 			a.iconBtn { width: 28px; height: 16px; display: block; float: left; background: url('${pageContext.request.contextPath}/easyui/themes/icons/user_edit.png') no-repeat center; }
		</style>
		<script type="text/javascript">
			$(function(){
				$('#dg').datagrid({
					url: '${pageContext.request.contextPath}/latenthazard/latenthazardAction_showAuditData',
					idField: 'id',
		            //title:'重大隐患上报',
		            toolbar:'#toolbar',
		            rownumbers: true,	//显示一个行号列
		            fitColumns:true,	//自动适应列
		           	fit:true,			//表格宽高自适应
		            nowrap:false,
		            striped:true,		//斑马线效果
					singleSelect:true,	//单行选中
		            loadmsg:'请等待...',	//加载等待时显示
		            pagination:true,	//显示分页组件
		            pageNumber:1,
		            pageSize:10,
		            pageList:[5,10,15,20,25,30],
					columns:[[
							  {field:'id',hidden:true},
							  {field:'latentHazardSn',hidden:true},
							  {field:'checkClass',title:'检查分类',width:80,align:'center'},
							  {field:'departmentSn',title:'检查单位编号',hidden:true},
							  {field:'checkUnit',title:'检查单位',width:80,align:'center',
					        	  formatter:function(value,row,index){
					        		  if( row.implDepartmentName != "无" ){
					        			  value = row.implDepartmentName + value;
					        		  }
					        		  return value;
								  }
					          },
							  {field:'department',title:'贯标单位',width:80,align:'center'},
							  {field:'departmentReportTo',title:'业务部门',width:80,align:'center'},
					          {field:'latentHazardDescription',title:'重大隐患描述',width:200,align:'center'},
					          {field:'happenDateTime',title:'发生时间',width:80,align:'center'},
					          {field:'happenLocation',title:'发生地点',width:80,align:'center'},
					          {field:'status',title:'状态',width:50,align:'center',
					        	  formatter:function(value,row,index){
					        		  if( value == "已上报" && value != "已审核" ){
									 	  return "未审核<a class='iconBtn' href='javascript:;' onclick=auditLatentHazard() name='btn'></a>";
					        		  }else{
					        			  return value;
					        		  }
								  }
					          },
					          {field:'reportDateTime',title:'上报时间',width:80,align:'center'},
					          {field:'editor',title:'上报人',width:50,align:'center'},
					          {field:'auditDateTime',title:'审核时间',width:80,align:'center'},
					          {field:'auditor',title:'审核人',width:50,align:'center'},
					          {field:'auditSuggestion',title:'审核意见',width:100,align:'center'},
					          {field:'cancelDateTime',title:'销号时间',width:80,align:'center'},
					          {field:'cancelPerson',title:'销号人',width:50,align:'center'},
							  {field:'latentHazardAttachment',width:50,title:'相关附件',align:'center',
					        	  formatter:function(value,row,index){
							 	  return "<a href='javascript:;' onclick='attachment_latentHazard()' style='text-decoration:none'>附件["+value+"]</a>";
							  }}
					     ]],
						 onDblClickCell: function(){
							$('#dg').datagrid("uncheckAll");
						 }
					});
			});
			function attachment_latentHazard(){
				$('#dg').datagrid('clearSelections');//清处多选的行
				$('#win').window({
					width:600,
					height:300,
					title:'相关附件',
					cache:false,
					content:'<iframe src="${pageContext.request.contextPath}/latenthazard/reportto_attachment" frameborder="0" width="100%" height="100%"/>'
				});
			};
			function auditLatentHazard(){
				$('#dg').datagrid('clearSelections');//清处多选的行
				$('#win').window({
					width:600,
					height:300,
					title:'审核意见',
					cache:false,
					content:'<iframe src="${pageContext.request.contextPath}/latenthazard/audit_edit" frameborder="0" width="100%" height="100%"/>'
				});
			};
	    </script>
</head>
<body>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>