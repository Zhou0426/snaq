<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>重大隐患上报</title>
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
					url: '${pageContext.request.contextPath}/latenthazard/latenthazardAction_showData',
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
							  {field:'departmentReportToSn',hidden:true},
							  {field:'departmentReportTo',title:'业务部门',width:80,align:'center'},
					          {field:'latentHazardDescription',title:'重大隐患描述',width:200,align:'center'},
					          {field:'happenDateTime',title:'发生时间',width:80,align:'center'},
					          {field:'happenLocation',title:'发生地点',width:80,align:'center'},
					          {field:'status',title:'状态',width:50,align:'center',
					        	  formatter:function(value,row,index){
					        		  if( value == "未上报" ){
									 	  return "未上报<a class='iconBtn' href='javascript:;' onclick=reportLatentHazard("+row.id+") name='btn'></a>";
					        		  }else{
					        			  return value;
					        		  }
								  }
					          },
					          {field:'reportDateTime',title:'上报时间',width:80,align:'center'},
					          {field:'auditDateTime',title:'审核时间',width:80,align:'center'},
					          {field:'auditor',title:'审核人',width:50,align:'center'},
					          {field:'auditSuggestion',title:'审核意见',width:100,align:'center'},
					          {field:'cancelDateTime',title:'销号时间',width:80,align:'center'},
					          {field:'cancelPerson',title:'销号人',width:50,align:'center'},
							  {field:'latentHazardAttachment',width:50,title:'相关附件',align:'center',
					        	  formatter:function(value,row,index){
							 	  return "<a href='javascript:;' onclick='attachment_latentHazard(" + index + ")' style='text-decoration:none'>附件["+value+"]</a>";
							  }}
					     ]],
						 onDblClickCell: function(){
							$('#dg').datagrid("uncheckAll");
						 }
					});
					
					
					//权限管理
					$('#add').bind('click',function(){
						$('#win').window({
							title:"添加信息",
							width:780,
							height:380,
							content:'<iframe src="${pageContext.request.contextPath}/latenthazard/reportto_add" frameborder="0" width="100%" height="100%" />'
						});
					});
					//修改按钮
					$('#edit').bind('click',function(){
						//判断是否选中
						var rows = $('#dg').datagrid("getSelections");
			 			if( rows.length != 1 ){
							$.messager.show({
								title:'提示信息',
								msg:'请选择一条记录',
								timeout:2000,
								showType:'slide'
							});														
						}else if( rows[0].status != "未上报" ){
							$.messager.alert('系统提示','该重大隐患已上报，不能修改'); 													
						}else{
							$('#win').window({
								title:"修改信息",
								width:780,
								height:340,
								content:'<iframe src="${pageContext.request.contextPath}/latenthazard/reportto_update" frameborder="0" width="100%" height="100%" />'
							});
						}
					});
					//删除按钮点击事件
					$('#remove').bind('click',function(){
						 //判断是否有选中行记录
						var rows=$('#dg').datagrid("getSelections")
						if(rows.length==0){
							$.messager.show({
								title:'提示信息',
								msg:'请至少选中一行记录',
								timeout:2000,
								showType:'slide'
							});														
						}else{
							$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
								if(r){
									id = rows[0].id;
									$.post("${pageContext.request.contextPath}/latenthazard/latenthazardAction_delete",{id:id},function(result){
										if(result.message == "success"){
												$.messager.show({
													title:'提示',
													msg:'成功删除记录！',
													timeout:2000,
													showType:'slide'
												});	
											$('#dg').datagrid("reload");
										}else{
											$.messager.alert('警告','删除失败，请检查后重新操作！');
										}
									},"json");
									$('#dg').datagrid("unselectAll");
								}
							});
						}
					});
			});
			function attachment_latentHazard(index){
				$('#dg').datagrid('clearSelections');//清处多选的行
				$('#win').window({
					width:600,
					height:300,
					title:'相关附件',
					cache:false,
					content:'<iframe src="${pageContext.request.contextPath}/latenthazard/reportto_attachment" frameborder="0" width="100%" height="100%"/>'
				});
			};
			function reportLatentHazard(event){
				$.messager.confirm('确认','一旦上报将无法修改，您确认想要上报吗？',function(r){
				    if (r){
				    	$.post("${pageContext.request.contextPath}/latenthazard/latenthazardAction_reportLatentHazard",{id:event},function(result){
							if(result.message == "success"){
								$.messager.show({
									title:'提示',
									msg:'成功上报！',
									timeout:2000,
									showType:'slide'
								});	
							}else{
								$.messager.alert('警告','操作失败，请检查后重新操作！');
							}
							$('#dg').datagrid("reload");
						},"json");
				    }
				});
			};
	    </script>
</head>
<body>
		<div id="toolbar">
			<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
			<a id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >修改</a>
			<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >删除</a>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		
		
		
</body>
</html>