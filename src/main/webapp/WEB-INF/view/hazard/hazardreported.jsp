<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>状态类危险源</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css"> 
			.align-center{ 
				margin:10px 5px;	/* 居中 这个是必须的，，其它的属性非必须 */ 
 				width:250px; 		/*给个宽度 顶到浏览器的两边就看不出居中效果了 */ 
				text-align:left; 	/* 文字等内容居中 */ 
			} 
			.nextPage{
				word-wrap: break-word; 
				word-break: normal;
			}
		</style> 
		<script type="text/javascript">
			$(function(){
					$('#dg').datagrid({
						url: '${pageContext.request.contextPath}/hazard/hazardReportedAction_showData.action',
						idField: 'id',
			            title:'危险源上报',
			            toolbar:'#weixianyuan',
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
								  {field:'departmentTypeSn',hidden:true},
								  {field:'departmentTypeName',title:'部门类型名称',width:120,align:'center'},
						          {field:'reportSn',title:'上报编号',width:100,align:'center'},
						          {field:'hazardDescription',title:'危险源描述',width:200,align:'center'},
						          {field:'resultDescription',title:'后果描述',width:200,align:'center'},
						          {field:'accidentType',title:'事故类型',width:100,align:'center'},  
						          {field:'accidentTypeSn',hidden:true}, 
						          {field:'riskLevel',title:'风险等级',width:100,align:'center'},
						          {field:'reportPerson',title:'上报人',width:100,align:'center'},  
						          {field:'reportPersonId',hidden:true},    
						          {field:'reportDateTime',title:'上报时间',width:100,align:'center'},    
						          {field:'auditedStatus',title:'审核结果',width:100,align:'center',
						        	  formatter: function(value, row, index) {
							        	  if(value!=null){
							        		  return value;
							        	  }else{
							        		  return "未审核";
							        	  }
									  }
							 	  },
						          {field:'auditSuggestion',title:'审核意见',width:200,align:'center',
						        	  formatter: function(value, row, index) {
						        		  if(value!=null){
							        		  return value;
							        	  }else{
							        		  return "无";
							        	  }
									  }
							 	  },
						          {field:'auditor',title:'审核人',width:100,align:'center',
						        	  formatter: function(value, row, index) {
						        		  if(value!=null){
							        		  return value;
							        	  }else{
							        		  return "无";
							        	  }
									  }
							 	  },
							 	 {field:'standardIndex',title:'指标',width:100,align:'center',
						        	  formatter: function(value, row, index) {
						        		  if(value!=null){
						        			  return "<a href='javascript:;' onclick=showStandardIndex() style='text-decoration:none'>指标["+value+"]</a>";
							        	  }
									  }
							 	  }, 
						         {field:'auditorId',hidden:true},
						     ]],
							 onDblClickCell: function(){
								$('#dg').datagrid("uncheckAll");
							}
					});
					
					
					//权限管理
					var str="${sessionScope['permissions']}";
					//添加按钮点击事件
					if(str.indexOf("100301")==-1){
						$("#add").css('display','None');
					}else{
						$('#add').bind('click',function(){
							$('#win').window({
								title:"添加信息",
								width:370,
								height:360,
								content:'<iframe src="${pageContext.request.contextPath}/hazard/hazardreported_add" frameborder="0" width="100%" height="100%" />'
							});
						});
					}
					//添加按钮点击事件
					if(str.indexOf("100302")==-1){
						$("#remove").css('display','None');
					}else{
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
										if (r){
											id=rows[0].id;
											$.post("${pageContext.request.contextPath}/hazard/hazardReportedAction_deleteData.action",{id:id},function(result){
												if(result=="\"success\""){
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
											},"text");
											$('#dg').datagrid("unselectAll");
										}
									});
								}
							});
					}
// 					//修改按钮事件
// 						$('#edit').bind('click',function(){
// 							//判断是否选中
// 							var rows=$('#dg').datagrid("getSelections")
// 				 			if(rows.length!=1){
// 								$.messager.show({
// 									title:'提示信息',
// 									msg:'请选择一条记录',
// 									timeout:2000,
// 									showType:'slide'
// 								});														
// 							}else{
// 									$('#win').window({
// 										title:"修改信息",
// 										width:370,
// 										height:340,
// 										content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_update" frameborder="0" width="100%" height="100%" />'
// 									});
// 							}
// 						});
					
			});
			function showStandardIndex(){
				$('#win').window({
					title:"指标详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazardreported_showStandardIndex" frameborder="0" width="100%" height="100%" />'
				});
			}
	    </script>
</head>
<body>
		<div id="weixianyuan">
			<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
<!-- 		<a id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >修改</a> -->
			<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >删除</a>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		
		
		
</body>
</html>