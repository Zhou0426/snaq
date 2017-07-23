<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>危险源失控统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			$(function(){
				var departmentTypeSn="";
				var departmentSn = "";
					$('#dg').datagrid({
						url: '${pageContext.request.contextPath}/hazard/hazardReportedAction_showCount.action',
						idField: 'id',
			            title:'危险源失控统计',
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
						          {field:'departmentTypeName',title:'所属部门类型',width:70,align:'center'},
						          {field:'hazardSn',title:'危险源编号',width:100,align:'center'},    
						          {field:'hazardDescription',title:'危险源描述',width:300,align:'center'},    
						          {field:'resultDescription',title:'危险源后果描述',width:300,align:'center'},    
						          {field:'accidentTypeName',title:'事故类型',width:70,align:'center'},    
						          {field:'accidentTypeSn',title:'事故类型',width:70,align:'center',hidden:true},
						          {field:'riskLevel',title:'风险等级',width:70,align:'center'},
						          {field:'manageObjectName',title:'管理对象名称',width:100,align:'center',
						        	  formatter: function(value, row, index) {
							        	  if(value!=null&&value.length>0){
							 		      		return "<a href='javascript:;' onclick=showHazard() style='text-decoration:none'>"+value+"</a>";
								          }else{
								        	  return "<a href='javascript:;' onclick=showHazard() style='text-decoration:none'>"+"无"+"</a>";
									      }
									  }
							 	  },
							 	  {field:'standardIndexNum',title:'对应指标',width:70,align:'center',
						        	  formatter: function(value, row, index) {
							 		      return "<a href='javascript:;' onclick=showStandardIndex() style='text-decoration:none'>指标["+value+"]</a>";
									  }
							 	  },
						          {field:'countNum',title:'失控次数',width:70,align:'center',
						        	  formatter: function(value, row, index) {
							        	  if(value!=null){
							 		      		return "<a href='javascript:;' onclick=showUnsafeCondition() style='text-decoration:none'>失控次数["+value+"]</a>";
								          }else{
								        		return "<a href='javascript:;' onclick=showUnsafeCondition() style='text-decoration:none'>失控次数[0]</a>";
									      }
									  }
							 	  }
						     ]],
							 onDblClickCell: function(){
								$('#dg').datagrid("uncheckAll");
							 },
							 onBeforeLoad:function(){
								 if(departmentTypeSn=="" || departmentTypeSn.length==0 || departmentSn=="" || departmentSn.length==0){
									 return false;
								 }else{
									 return true;
								 }
							 }
					});
					$('#departmentType').combobox({
						url:'${pageContext.request.contextPath}/hazard/manageObjectAction_load.action',
						method:'post',
						required:true,
						panelWidth: 200,
						editable:false,
						panelHeight:'auto',
						width:200,
						onLoadSuccess:function(node,data){
							if(node.length>0){
								$("#departmentType").combobox('setValue',node[0].id);
								departmentTypeSn=node[0].id;
								$('#dg').datagrid('load', {departmentTypeSn:node[0].id, departmentSn : departmentSn});
							}
						},
						onSelect: function(rec){
							departmentTypeSn=rec.id;
							$('#dg').datagrid('load', {departmentTypeSn:rec.id, departmentSn : departmentSn});
				        }
					});
					var a = 1;
					$('#department').combotree({
						url:'${pageContext.request.contextPath}/department/treeAll.action',
						method:'post',
						valueField: 'id',    
				        textField: 'text', 
						required:true,
						queryParams:{'resourceSn':'1005'},
						panelWidth: 300,
						panelHeight:350,
						width:200,
						onLoadSuccess:function(node,data){
							//第一次加载时执行的事件
							if(a==1){
								<!--设置数据加载成功时的默认值-->
								var da=$('#department').combotree('tree').tree('getRoot');
								$('#department').combotree('setValue',da.id);

								departmentSn = da.id;
								$('#dg').datagrid('load', {departmentTypeSn : departmentTypeSn, departmentSn : departmentSn});
								a++;
								if(data.length == 1){
									var node = $('#department').combotree('tree').tree('find', data[0].id);
									$('#department').combotree('tree').tree('expand', node.target);
					        	}
							}
						},
						onSelect: function(data){
							departmentSn = data.id;
							$('#dg').datagrid('load', {departmentTypeSn : departmentTypeSn, departmentSn : departmentSn});
				        }
					});
					
			});
			//隐患详情
			function showUnsafeCondition(){
		    	$('#win').window({
					title:"隐患详情",
					width:1100,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazardreported_showUnsafeCondition" frameborder="0" width="100%" height="100%" />'
				});
			};
			//管理对象详情
			function showHazard(){
		    	$('#win').window({
					title:"管理对象详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_showManageObject" frameborder="0" width="100%" height="100%" />'
				});
			};
			//指标详情
			function showStandardIndex(){
		    	$('#win').window({
					title:"指标详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_showStandardIndex" frameborder="0" width="100%" height="100%" />'
				});
			};
			
			
	    </script>
</head>
<body>
		<div id="weixianyuan">
			<span>
				<lable for="departmentType">&emsp;部门类型：</lable>
				<input id="departmentType" name="departmentType" />
			</span>
			<span>
				<lable for="department">&emsp;选择部门：</lable>
				<input id="department" name="departmentSn" />
			</span>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		<!-- 区分危险源和危险源失控统计 -->
		<input type="hidden" id="hazardCount" value="2" />
</body>
</html>