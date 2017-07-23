<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		$(function(){	
			var row=parent.$('#dg').datagrid("getSelections");
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/hazard/hazardReportedAction_showStandardIndex.action?reportSn='+ row[0].reportSn,
	            toolbar:'#btn',
	            rownumbers: true,	//显示一个行号列
	            fitColumns:true,	//自动适应列
	           	fit:true,			//表格宽高自适应
	            nowrap:false,
	            striped:true,		//斑马线效果
				singleSelect:true,	//单行选中
	            loadmsg:'请等待...',	//加载等待时显示
	            pagination:true,	//显示分页组件
	            pageNumber:1,
	            pageSize:5,
	            pageList:[5,10,15,20,25,30],
	      		columns:[[
				          {field:'id',title:'主键',hidden : true},
				          {field:'indexSn',title:'指标编号',width:80,align:'center'},
				          {field:'indexName',title:'指标名称',width:150,align:'center'},
				          {field:'isKeyIndex',title:'是否关键指标',width:50,align:'center'},
				          {field:'specialities',title:'专业',width:80,align:'center'}
				 ]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
				}
			});
			$('#standardIndexList').combogrid({
				url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryStandard.action',
				width:530,
				idField:'indexSn',    
			    textField:'indexName',
				prompt:'请输入搜索信息',
				editable:true,
				delay:500,
				columns:[[    
				        {field:'id',title:'逻辑主键',hidden:true},    
		        		{field:'indexSn',title:'指标编号',width:80,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},    
		        		{field:'indexName',title:'指标名称',width:250,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},
		        		{field:'isKeyIndex',title:'是否关键指标',width:100,formatter:function(value,row,index){
		        			if(row.isKeyIndex==true){
		        				return '是';
		        			}else if(row.isKeyIndex==false){
		        				return '否';
		        			}
		        		}},
		        		{field:'standardName',title:'所属标准',width:80,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}}
				]],
				onChange:function(newValue, oldValue){
					$('#standardIndexList').combogrid('grid').datagrid('load',{'q':newValue,'departmentTypeSn':row[0].departmentTypeSn});
				}
			});
	 		
	 	
			 	var str="${sessionScope['permissions']}";
			 	//添加
				if(str.indexOf("100303")==-1){
					$("#addbtn").css('display','None');
					$("#standardIndexList2").css('display','None');
				}else{
					$('#addbtn').bind('click',function(){
				 		if($('#standardIndexList').combogrid('getValue')!=""){
				 			$.post("${pageContext.request.contextPath}/hazard/hazardReportedAction_addStandardIndex.action",{id:row[0].id,indexSn:$('#standardIndexList').combogrid('getValue')},
						 			function(result){
					 					if(result=="success"){
							 				$.messager.show({
												title:'提示信息',
												msg:'添加信息成功',
												timeout:1000,
												showType:'slide'
											});	
											$('#standardIndexList').combogrid('clear');
											$('#dg').datagrid("reload");
											parent.$('#dg').datagrid("reload");
						 				}else{
						 					$.messager.alert('错误','添加失败，请重新检查');
							 			}
									},"json");
					 	}else{
					 		$.messager.show({
								title:'提示信息',
								msg:'请选择添加的指标',
								timeout:1000,
								showType:'slide'
							});	
						}
			 		});
				}
			 	//删除
				if(str.indexOf("100304")==-1){
					$("#removebtn").css('display','None');
				}else{
					$('#removebtn').bind('click',function(){
				 		//判断是否有选中行记录
						var rows=$('#dg').datagrid("getSelections")
						if(rows.length==0){
							$.messager.show({
								title:'提示信息',
								msg:'请选中一行记录',
								timeout:2000,
								showType:'slide'
							});														
							}else{
								$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
									if (r){
										//发送ajax请求
										$.post("${pageContext.request.contextPath}/hazard/hazardReportedAction_deleteStandardIndex.action",{id:row[0].id,indexSn:rows[0].indexSn},function(result){
														if(result=="success"){
															$.messager.show({
																title:'提示信息',
																msg:'删除成功',
																timeout:2000,
																showType:'slide'
															});	
															$('#dg').datagrid("unselectAll");
															$('#dg').datagrid("reload");
															parent.$('#dg').datagrid("reload");
														}else{
															$.messager.alert('错误','删除失败，请重新检查');
														}
												},"json");
										}
									});
								}
					 		});
				 	
				}
		});
	</script>
</head>
<body>
	<table id="dg"></table>
	<div id="btn">
			<span id="standardIndexList2">
				<input id="standardIndexList" name="standardIndexList"  />
			</span>
			<a id="addbtn" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="removebtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
	</div>
</body>
</html>