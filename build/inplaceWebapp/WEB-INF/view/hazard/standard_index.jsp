<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>指标统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			var a=1;
			var SindexSn = 1;
			$(function(){
					$('#standard').combobox({
						url:'${pageContext.request.contextPath}/standard/standardAction_queryStandard',
						method:'post',
						valueField:'standardSn',    
					    textField:'standardName',
					    editable:false,
						panelWidth: 320,
						panelHeight:250,
						width:295,
						onLoadSuccess:function(node,data){
							if(a==1){
								$("#standard").combobox('select',node[0].standardSn);
					            a++;
							}
						},
						onSelect: function(rec){
							SindexSn = 1;
							$('#standardIndex').combotree('clear');
							$('#standardIndex').combotree('reload');
				        }
					});
					$('#standardIndex').combotree({
						url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery',
						method:'post',
						panelWidth: 380,
						panelHeight:200,
						width:295,
						onBeforeLoad : function(node, param){
							var sard = $('#standard').combobox('getValue');
							if( sard != null && sard != "" ){
								param.standardSn = sard;
								return true;
							}else{
								return false;
							}
						},
						onLoadSuccess:function(node,data){
							if(SindexSn == 1){
								if( data.length > 0 ){
									$("#standardIndex").combotree('setValue',data[0].id);
									$('#dg').datagrid('reload');<!--发送数据-->
									SindexSn++;
								}else{
									$('#dg').datagrid('loadData', { total: 0, rows: [] });
									SindexSn++;
								}
							}
						},
						onSelect: function(rec){
							var va = $('#standard').combobox('getValue');
							if(va == null || va.length == 0){
								$.messager.alert('提示','请先选择标准！');
								$('#standardIndex').combotree('clear');
							}else{
								$('#dg').datagrid('reload');<!--发送数据-->
							}
				        }
					});
					$('#dg').datagrid({
						url: '${pageContext.request.contextPath}/department/queryDepartmentStandardIndex',
						idField: 'departmentSn',
			            toolbar:'#standardIndexDiv',
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
				                  {field:'departmentSn',title:'部门编号',width:'15%'} ,  
				                  {field:'implDepartmentName',title:'贯标单位',width:'15%'},
				                  {field:'departmentName',title:'部门名称',width:'15%'},
				                  {field:'departmentTypeName',title:'部门类型',width:'15%'},
				                  {field:'principal',title:'安全责任人',width:'20%'},
						          {field:'countNum',title:'失控次数',width:'18%',align:'center',
						        	  formatter: function(value, row, index) {
							        	  if(value!=null){
							 		      		return "<a href='javascript:;' onclick=showUnsafeCondition() style='text-decoration:none'>失控次数["+value+"]</a>";
								          }else{
								        		return "<a href='javascript:;' onclick=showUnsafeCondition() style='text-decoration:none'>失控次数[0]</a>";
									      }
									  }
							 	  }
						     ]],
							 onDblClickCell : function(){
								$('#dg').datagrid("uncheckAll");
							 },
							 onBeforeLoad : function(node, param){
								 var sindex = $("#standardIndex").combotree('getValue');
								 if( sindex != null && sindex != "" ){
									 node.standardSn = $('#standard').combobox('getValue');
									 node.standardIndexSn = sindex;
									 return true;
								 }else{
									 return false;
								 }
							 }
					});
			});
			//隐患详情
			function showUnsafeCondition(){
		    	$('#win').window({
					title:"隐患详情",
					width:1100,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/standIndex_showUnsafeCondition" frameborder="0" width="100%" height="100%" />'
				});
			};
	    </script>
</head>
<body>
		<div id="standardIndexDiv">
			<span>
				<lable for="standard">标准：</lable>
				<input id="standard" name="standard" />
			</span>
			<span>
				<lable for="standardIndex">指标：</lable>
				<input id="standardIndex" name="standardIndex" />
			</span>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>