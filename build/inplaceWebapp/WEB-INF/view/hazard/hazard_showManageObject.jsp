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
			var departmentType=parent.$('#departmentType').combobox('getValue');
	 		var a=row[0].hazardSn;
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/hazard/manageObjectAction_showManageObject.action?hazardList='+a,
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
				          {field:'departmentTypeName',title:'部门类型',width:100,align:'left'},
				          {field:'manageObjectSn',title:'管理对象编号',width:100,align:'left'},
				          {field:'manageObjectName',title:'管理对象名称',width:100,align:'left'},
				          {field:'manageObjectType',title:'管理对象类型',width:100,align:'left'},
				          {field:'parentName',title:'父级管理对象',width:100,align:'left'}
				 ]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
					}
				});
			$('#manageObjectList').combogrid({
				url:'${pageContext.request.contextPath}/hazard/manageObjectAction_manageObjectList.action',
				//url:'hazard/manageObjectAction_loadParent.action?departmentTypeSn='+departmentType,
				width:200,
				panelWidth:300,
				idField:'id',    
			    textField:'text', 
				prompt:'请输入搜索信息',
				editable:true,
				delay:200,
				mode: "remote",
				columns:[[    
				          {field:'id',title:'编号',width:130},    
				          {field:'text',title:'名称',width:160}
				]]
			});
	 		
	 	
			 	var str="${sessionScope['permissions']}";
			 	//添加
				if(str.indexOf("100206")==-1 || parent.$('#hazardCount').val()=="2"){
					$("#addbtn").css('display','None');
					$("#manageObjectList2").css('display','None');
				}else{
					$('#addbtn').bind('click',function(){
				 		if($('#manageObjectList').combogrid('getValue')!=""){
				 			$.post("${pageContext.request.contextPath}/hazard/hazardAction_addManageObject.action",{hazardSn:a,manageObjectSn:$('#manageObjectList').combogrid('getValue')},
						 			function(result){
					 					if(result=="SUCCESS"){
							 				$.messager.show({
												title:'提示信息',
												msg:'添加信息成功',
												timeout:1000,
												showType:'slide'
											});	
											$('#manageObjectList').combogrid('clear');
											$('#dg').datagrid("reload");
											parent.$('#dg').datagrid("reload");
						 				}else{
						 					$.messager.alert('错误','添加失败，请重新检查');
							 				}
									},"text");
					 	}else{
					 		$.messager.show({
								title:'提示信息',
								msg:'请选择添加的管理对象',
								timeout:1000,
								showType:'slide'
							});	
						}
			 		});
				}
			 	//删除
				if(str.indexOf("100207")==-1 || parent.$('#hazardCount').val()=="2"){
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
										$.post("${pageContext.request.contextPath}/hazard/hazardAction_deleteManageObject.action",{hazardSn:a,manageObjectSn:rows[0].manageObjectSn},function(result){
														if(result=="SUCCESS"){
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
			<span id="manageObjectList2">
				<input id="manageObjectList" name="manageObjectList"  />
			</span>
			<a id="addbtn" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="removebtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
	</div>
</body>
</html>