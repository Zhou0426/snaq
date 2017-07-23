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
			var interiorWorkSn=row[0].interiorWorkSn;
			var standardIndexSn=parent.$('#standardindex').combotree('getValue');
			var standardSn=parent.$('#standard').combobox('getValue');
			//console.log(standardIndexSn);
			//console.log(standardSn);
			//console.log(row);
			$('#standardindex').combotree({
				url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+standardSn,
				method:'post',
				panelWidth: 400,
				panelHeight:300,
				width:300,
				onSelect: function(rec){
					$.post("${pageContext.request.contextPath}/interior/work/interiorWork_addIndex.action",{interiorWorkSn:interiorWorkSn,
						standardIndexSn:rec.id},function(result){
							//console.log(result);
						if(result=="success"){
								$.messager.show({
										title:'提示',
										msg:'成功添加指标！',
										timeout:2000,
										showType:'slide'
								});	
							$('#standardindex').combotree('setValue',"");
							$('#dg').datagrid("reload");
							parent.$('#dg').datagrid("reload");
						}else{
							if(result="login"){
								$.messager.alert('警告','该指标已存在，请重新选择！');
							}else{
								$.messager.alert('警告','添加失败，请检查后重新操作！');
							}
						}
					},"json");
		        }
			});
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/interior/work/interiorWork_showStandard.action?interiorWorkSn='+row[0].interiorWorkSn,
				idField: 'id',
	            //title:'相关指标',
	            toolbar:'#btn',
	            rownumbers: true,		//显示一个行号列
	            fitColumns:true,		//自动适应列
	           	fit:true,				//表格宽高自适应
	            nowrap:false,
	            striped:true,			//斑马线效果
				singleSelect:true,		//单行选中
	            loadmsg:'请等待...',		//加载等待时显示
	            pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:10,
	            pageList:[10,15,20,25,30],
				columns:[[ 
						  {field:'id',hidden:true},    
				          {field:'standardName',title:'相关标准',width:120,align:'center'},    
				          {field:'indexName',title:'相关指标',width:120,align:'center'},    
				     ]],
					 onDblClickCell: function(){
						$('#dg').datagrid("uncheckAll");
					}
				});
			//权限管理
			var str="${sessionScope['permissions']}";
			//内业上传上传功能
			if(str.indexOf("110104")==-1){
				$("#remove").css('display','None');
			}else{
				$('#remove').bind('click', function(){
					var rows=$('#dg').datagrid("getSelections");
					if(rows.length==0){
						$.messager.show({
							title:'提示信息',
							msg:'请选中一行记录',
							timeout:2000,
							showType:'slide'
						});														
						}else{
							$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
									if(r){
										$.post("${pageContext.request.contextPath}/interior/work/interiorWork_deleteIndex.action",{interiorWorkSn:interiorWorkSn,
											id:rows[0].id},function(result){
													//console.log(result);
												if(result=="success"){
														$.messager.show({
															title:'提示',
															msg:'成功删除记录！',
															timeout:2000,
															showType:'slide'
														});	
													$('#dg').datagrid("reload");
													parent.$('#dg').datagrid("reload");
												}else{
													if(result="login"){
														$.messager.alert('警告','指标不能为空，请至少保留一条指标！');
													}else{
														$.messager.alert('警告','删除异常，请检查后重新操作！');
													}
												}
										},"json");
										$('#dg').datagrid("unselectAll");
									}
							});
						}
			    });
			}
			
		});
	</script>
</head>
<body>
	<div id="btn">
			<lable for="standardindex">指&emsp;标：</lable><input id="standardindex"  />
			<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
	</div>
	<table id="dg"></table>
</body>
</html>