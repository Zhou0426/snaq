<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>证件类型</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>
	var str="${sessionScope['permissions']}";
	$(function() {
		<!--数据网络-->
		$('#dg').datagrid({
							pageNumber : 1,
							pagination : true,
							url : "${pageContext.request.contextPath}/certificate/showCType",
							rownumbers : true,
							fitColumns : true,
							fit:true,
							pageSize : 10,
							singleSelect : true,
							pageList : [ 5, 10, 15, 20 ],
							toolbar : [
									{
										iconCls : 'icon-add',
										text : '增加',
										id:'addtool',
										handler : function() {
											if(str.indexOf("170301")==-1){
												$("#addtool").css('display','none');
											}else{
												$('#addbtn').show();
												$('#editbtn').hide();
												$('#form').form('reset');
												$('#win').window('open');
												$('#form').form('disableValidation');
											}
										}
									},
									{
										iconCls : 'icon-edit',
										text : '修改',
										id:'edittool',
										handler : function() {	
											if(str.indexOf("170302")==-1){
												$("#edittool").css('display','none');
											}else{
							    			//数据回显
								    			var rows=$("#dg").datagrid("getSelections");
								    			if (rows.length>0){
													$('#editbtn').show();
													$('#addbtn').hide();	
									    			$('#win').window('open');  
									    			//对表单数据进行填充
									    			$('#form').form('load',{
									    				id:rows[0].id,
									    				certificationTypeSn:rows[0].certificationTypeSn,
									    				certificationTypeName:rows[0].certificationTypeName
									    			});
								    			}
												else
												{
													$.messager.show({
														title:'消息提示',
														msg:'请先选择您要编辑的行！',
														showType:'null',
														style:{
															top:'50'
														}
													});
												}
											}
										}
									},
									{
										iconCls : 'icon-remove',
										text : '删除',
										id:'deletetool',
										handler : function() {
											if(str.indexOf("170303")==-1){
												$("#deletetool").css('display','none');
											}else{
												var row = $('#dg').datagrid('getSelected');
												if (row) {
													$.messager.confirm('选择是否删除','您确定要删除该证件类型吗?',function(fn) {
														if(fn==true){
															$.post('${pageContext.request.contextPath}/certificate/deleteCType',{id:row.id},function(message){
																$.messager.alert('提示',message); 
																$('#dg').datagrid('reload');
															});
														}
													});
												} 
												else {
													$.messager.alert('提示','请先选择要删除的行');
												}
											}
										}
									} ],
							columns : [ [ {
								field : 'id',
								title : 'id',
								width : 150,
								hidden:true
						
							}, {
								field : 'certificationTypeSn',
								title : '类型编号',
								width : 150
							} , {
								field : 'certificationTypeName',
								title : '类型名称',
								width : 150
							}] ]
						});
		$('#addbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/certificate/addCType",
				success: function(message){
					$.messager.alert('提示',message); 
					$('#dg').datagrid('reload'); 
	    			$('#win').window('close');		
				}
			});
	    });
		$('#editbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/certificate/updateCType",
				success: function(message){
					$.messager.alert('提示',message); 
					$('#dg').datagrid('reload');	 
	    			$('#win').window('close');		
				}
			})  
	    });
		
		if(str.indexOf("170301")==-1){
			$("#addtool").css('display','none');
		}
		if(str.indexOf("170302")==-1){
			$("#edittool").css('display','none');
		}
		if(str.indexOf("170303")==-1){
			$("#deletetool").css('display','none');
		}
		
		$('#addtool').linkbutton({
			plain:false
		});
		$('#edittool').linkbutton({
			plain:false
		});
		$('#deletetool').linkbutton({
			plain:false
		});
		
	})
</script>
</head>
<body>
	<!-- 证件类型数据列表 -->
	<table id="dg"></table>
	<!-- 证件类型填写窗口-->
	<div id="win" class="easyui-window" title="填写证件类型信息" closed="true" style="width: 400px; height: 200px; padding: 5px;">
		<br />
		<form id="form" method="post">
			<div style="margin-left: 30px;">
				<div style="diapaly: inline">				
					 <input name="id" hidden/>	
					 <label style="padding-right: 25px">类型编号：</label>
					 <input name="certificationTypeSn" class="easyui-textbox"/> 
					<br/><br/>
					 <label style="padding-right: 25px">类型名称：</label>
					 <input name="certificationTypeName" class="easyui-textbox"/> 
				</div><br /><br />
				<div id="dlg-buttons" style="text-align:center">
					<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok" >确认添加</a>
					<a id="editbtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>
				</div>
			</div>
		</form>
	</div>
</body>
</html>