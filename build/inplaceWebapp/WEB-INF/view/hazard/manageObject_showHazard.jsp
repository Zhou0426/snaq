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
	 		var a=row[0].manageObjectSn;
			//console.log(row);
			//console.log(row[0].manageObjectSn);
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/hazard/hazardAction_show.action?manageObjectSn='+a,
	            idField: 'id',
	            title:row[0].manageObjectName,
	            toolbar:'#btn',
	            rownumbers: true,	//显示一个行号列
	            fitColumns:true,		//自动适应列
	            //height:400,
	           	fit:true,						//表格宽高自适应
	            nowrap:false,
	            striped:true,				//斑马线效果
				singleSelect:true,		//单行选中
	            loadmsg:'请等待...',	//加载等待时显示
	            pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:5,
	            pageList:[5,10,15,20,25,30],
	      		columns:[[
				          {field:'hazardSn',title:'危险源编号',width:100,align:'left'},
				          {field:'hazardDescription',title:'危险源描述',width:250,align:'left'},
				          {field:'resultDescription',title:'危险源后果描述',width:300,align:'left'},
				          {field:'accidentTypeName',title:'事故类型',width:70,align:'left'},
				          {field:'riskLevel',title:'风险等级',width:70,align:'left'}
				 ]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
					}
				});
			$('#hazardList').combogrid({
				url:'${pageContext.request.contextPath}/hazard/hazardAction_showList.action',
				width:400,
				panelWidth:500,
				queryParams:{departmentTypeSn:row[0].departmentTypeSn},
				idField:'id',    
			    textField:'text', 
				prompt:'请输入搜索信息',
				editable:true,
				delay:200,
				mode: 'remote',
				columns:[[    
				          {field:'id',title:'编号',width:100},    
				          {field:'text',title:'名称',width:390}
				]]
			});
	 		
	 	
	 	
		 	var str="${sessionScope['permissions']}";
		 	//添加
			if(str.indexOf("100106")==-1){
				$("#addbtn").css('display','None');
				$("#hazardList2").css('display','None');
			}else{
				$('#addbtn').bind('click',function(){
			 		if($('#hazardList').combogrid('getValue')!=""){
			 			$.post("${pageContext.request.contextPath}/hazard/manageObjectAction_addHazard.action",{manageObjectSn:a,hazardList:$('#hazardList').combogrid('getValue')},
					 			function(result){
				 					if(result=="SUCCESS"){
						 				$.messager.show({
											title:'提示信息',
											msg:'添加信息成功',
											timeout:1000,
											showType:'slide'
										});	
										$('#hazardList').combogrid('clear');
										$('#dg').datagrid("reload");
										parent.$('#dg').datagrid("reload");
					 				}else{
					 					$.messager.alert('错误','添加失败，请重新检查');
						 				}
								},"text");
				 	}else{
				 		$.messager.show({
							title:'提示信息',
							msg:'请选择添加的危险源',
							timeout:1000,
							showType:'slide'
						});	
					}
		 		});
			}
		 	//删除
			if(str.indexOf("100107")==-1){
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
									//获取选中记录的id
									id=rows[0].id;
									//发送ajax请求
									$.post("${pageContext.request.contextPath}/hazard/manageObjectAction_deleteHazard.action",{manageObjectSn:a,hazardList:rows[0].hazardSn},function(result){
										if(result=="SUCCESS"){
							 				$.messager.show({
												title:'提示信息',
												msg:'删除成功',
												timeout:2000,
												showType:'slide'
											});	
											$('#dg').datagrid("unselectAll");
											$("#dg").datagrid("reload");
											parent.$('#dg').datagrid("reload");
						 				}else{
						 					$.messager.alert('错误','删除失败，请重新检查');
							 				}
										},"text");
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
			<span id="hazardList2">
				<input id="hazardList" name="hazardList"  />
			</span>
			<a id="addbtn" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="removebtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
	</div>
</body>
</html>