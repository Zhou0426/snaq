<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/customize/unit/unsafeActDeductionPointAction_query',
			queryParams:{
				type:0
			},
			idField:'id',
			fit:true,
			stript:true,
			nowrap:false,
			fitColumns:true,/*列宽自动*/
			loadMsg:'请等待',
			rownumbers:true,
			romoteSort:true,
			singleSelect:true,
			checkOnSelect:false,
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,20,50],
			/*按钮*/
			toolbar:[{
				text:'<input id="cc1"/>'
			},{
				id:'add',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					if(roles.indexOf("jtxtgly")==-1){
						$('#add').css('display','none');
					}else{
						$('#win').dialog({    
						    title: '添加',    
						    width: 350,    
						    height: 300,     
						    cache: false,       
						    modal: true,
						    resizable:true,
						    content:'<iframe src="${pageContext.request.contextPath}/customize/unit/unit_add" frameborder="0" width="100%" height="100%"/>'  
						});  
					}
				}
			},{
				id:'delete',
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					if(roles.indexOf("jtxtgly")==-1){
						$('#delete').css('display','none');
					}else{
						var row=$('#dg').datagrid('getSelected');
						if(row){
							$.messager.confirm('确认对话框', '您想要删除当前选中的记录吗？', function(r){
								if (r){
									$.post("${pageContext.request.contextPath}/customize/unit/unsafeActDeductionPointAction_delete",{id:row.id},function(result){
			                        	var data= eval('(' + result + ')');
										var status=data.status;
										if(status=="ok"){
											$.messager.alert("提示信息",'删除成功！');
										}else{
											$.messager.alert("提示信息",'删除失败！','error');
										}
										//删除页面，刷新
										$("#dg").datagrid("clearSelections");
										$("#dg").datagrid("reload");
									},"text");
								}
							});
																		
						}else{
							// 消息将显示在顶部中间
							$.messager.show({
								title:'提示',
								msg:'请选择一条记录！',
								showType:'show',
								timeout:1000,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});	
						}
						
					}
					
				}
			},{
				id:'update',
				text:'修改',
				iconCls:'icon-edit',
				handler:function(){
					if(roles.indexOf("jtxtgly")==-1){
						$('#update').css('display','none');
					}else{
						var row=$('#dg').datagrid('getSelected');
						if(row){
							$('#win').dialog({    
							    title: '修改',    
							    width: 350,    
							    height:300,     
							    cache: false,       
							    modal: true,
							    resizable:true,
							    content:'<iframe src="${pageContext.request.contextPath}/customize/unit/unit_update" frameborder="0" width="100%" height="100%"/>'  
							});
						}else{
							$.messager.show({
								title:'我的消息',
								msg:'请勾选所要修改的记录！',
								showType:'slide',
								timeout:500,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
					}
					
				}
			}],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'departmentTypeName',title:'单位类型',width:'25%',align:'center'},
				{field:'year',title:'年份',width:'25%',align:'center'},
				{field:'unsafeActLevel',title:'不安全行为等级',width:'25%',align:'center'},
				{field:'deductionPoints',title:'扣分',width:'24%',align:'center'},
			]]
		})
		//单位类型
		$('#cc1').combobox({
			url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			prompt:'单位类型',
			editable:false,
			onSelect:function(record){
				$('#dg').datagrid('load',{
					departmentTypeSn:record.departmentTypeSn,
					type:0
				}); 
			}						
		});		
		//第一层按钮权限
		//添加
		if(roles.indexOf("jtxtgly")==-1){
			$('#add').css('display','none');
			$('#delete').css('display','none');
			$('#update').css('display','none');
		}
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
	})	
</script>
</head>
<body  style="margin: 1px;">
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>