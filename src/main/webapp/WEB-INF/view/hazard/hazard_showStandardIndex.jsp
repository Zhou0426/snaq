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
	 		var a=row[0].hazardSn;
	 		 
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/hazard/hazardAction_showStandardIndex.action?hazardSn='+a,
	            idField: 'id',
	            //title:'指标详情',
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
						  {field:'id',hidden:true},
				          {field:'indexSn',title:'指标编号',width:100,align:'left'},
				          {field:'indexName',title:'指标名称',width:120,align:'left'},
				          {field:'standardSn',title:'所属标准编号',width:70,align:'left'},
				          {field:'standardName',title:'所属标准名称',width:120,align:'left'}
				 ]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
					}
				});
			$('#standardIndexList').combogrid({
				url:'${pageContext.request.contextPath}/standard/standardindexAction_queryByQ.action',
				width:400,
				panelWidth:400,
				idField:'id',
				queryParams:{departmentTypeSn:row[0].departmentTypeSn},
			    textField:'indexName', 
				prompt:'请输入搜索信息',
				editable:true,
				delay:200,
				mode: 'remote',
				columns:[[
						{field:'id',title:'逻辑主键',hidden:true},    
						{field:'indexSn',title:'指标编号'},    
						{field:'indexName',title:'指标名称'}
				]]
			});
	 		
	 	
		 	var str="${sessionScope['permissions']}";
			if(str.indexOf("100212")==-1 || parent.$('#hazardCount').val()=="2"){
				$("#add").css('display','None');
				$("#standardIndexList2").css('display','None');
			}else{
				$('#add').bind('click',function(){
			 		if($('#standardIndexList').combogrid('getValue')!=""){
			 			$.post("${pageContext.request.contextPath}/hazard/hazardAction_addStandardIndex.action",{hazardSn:a,id:$('#standardIndexList').combogrid('getValue')},
					 			function(result){
			 						console.log(result);
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
					 				}else if(result=="login"){
					 					$.messager.alert('提示','指标已存在！');
					 				}else{
					 					$.messager.alert('错误','添加失败，请重新检查');
						 			}
								},"text");
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
			if(str.indexOf("100213")==-1 || parent.$('#hazardCount').val()=="2"){
				$("#remove").css('display','None');
			}else{
				$('#remove').bind('click',function(){
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
									$.post("${pageContext.request.contextPath}/hazard/hazardAction_deleteStandardIndex.action",{hazardSn:a,id:rows[0].id},function(result){
										if(result=="success"){
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
			<span id="standardIndexList2">
				<input id="standardIndexList" name="standardIndexList"  />
			</span>
			<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
	</div>
</body>
</html>