<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>危险源审核</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
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
							        	  if(value!=null&&value.length>0){
							        		  return value;
							        	  }else{
							        		  return "未审核 <a name='btn' href='#'></a>";
							        	  }
									  }
							 	  },
						          {field:'auditSuggestion',title:'审核意见',width:200,align:'center',
						        	  formatter: function(value, row, index) {
						        		  if(value!=null&&value.length>0){
							        		  return value;
							        	  }else{
							        		  return "无";
							        	  }
									  }
							 	  },
						          {field:'auditor',title:'审核人',width:100,align:'center',
						        	  formatter: function(value, row, index) {
						        		  if(value!=null&&value.length>0){
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
							},
							onLoadSuccess:function(){
								$('[name=btn]').linkbutton({    
								    iconCls: 'icon_user_edit'   
								});
								$('[name=btn]').click(function(){
									$('#win').window({
										title:"审核",
										width:370,
										height:240,
										content:'<iframe src="${pageContext.request.contextPath}/hazard/hazardauditor_edit" frameborder="0" width="100%" height="100%" />'
									});
								});
							}
					});
// 					function showedit(){
// 						$('#win').window({
// 							title:"审核",
// 							width:370,
// 							height:340,
// 							content:'<iframe src="${pageContext.request.contextPath}/hazard/hazardauditor_edit" frameborder="0" width="100%" height="100%" />'
// 						});
// 					};
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
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		
</body>
</html>