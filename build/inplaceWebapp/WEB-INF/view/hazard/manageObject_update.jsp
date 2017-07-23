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
	<style type="text/css">
		form div{
			margin:10px
		}
	</style>
	<script type="text/javascript">
		$(function(){
			var rows=parent.$('#dg').datagrid("getSelections");
			//数据回显
			$('#ff').form('load',{
				ids:rows[0].ids,
				manageObjectSn:rows[0].manageObjectSn,
				manageObjectName:rows[0].manageObjectName,
				manageObjectType:rows[0].manageObjectType
			});	
			$("input[name=manageObjectSn]").validatebox({
					required:true,
					missingMessage:'请输入管理对象编号'
				});
			$("input[name=manageObjectName]").validatebox({
					required:true,
					missingMessage:'请输入管理对象名称'
			});
			$("#manageObjectType").combobox({
					required:true,
					missingMessage:'请输入管理对象类型',
					width:180
			});
			//弹出窗口时禁用验证
			$("#ff").form("disableValidation");
			//注册botton的点击事件
			$("#ok_btn").click(function(){
					//开启验证
					$("#ff").form("enableValidation");
					//如果验证成功，则提交数据
					if($('#ff').form("validate")){
						//调用submit方法，提交数据
						$('#ff').form('submit', {
							url:'${pageContext.request.contextPath}/hazard/manageObjectAction_update.action',
							success: function(result){
								if(result=="\"login\""){
									$.messager.alert('警告','管理对象编号已存在，请重新输入！');
									}else{
										if(result=="\"success\""){
											//表单重置
											$('#ff').form("reset");
											//关闭当前窗体
											parent.$('#win').window("close");
											parent.$.messager.show({
												title:'提示信息',
												msg:'更新成功！',
												timeout:2000,
												showType:'slide'
											});	
											}else{
												//表单重置
												$('#ff').form("reset");
												//关闭当前窗体
												parent.$('#win').window("close");
												parent.$.messager.alert('警告','更新失败，请检查后重新操作！');
											}
										//刷新dg
										parent.$("#dg").datagrid("reload");	
									}
							}
						});	
					}
				});
			$("#cancel_btn").click(function(){
				//关闭窗口
				parent.$('#win').window("close")
			});
		});
	</script>
</head>
<body>
	<form id="ff" method="post">
	    <div>   
	        <label for="manageObjectSn">管理对象编号：</label>   
	        <input class="easyui-textbox" name="manageObjectSn" />   
	    </div>
	    <div>   
	        <label for="manageObjectName">管理对象名称：</label>   
	        <input class="easyui-textbox" name="manageObjectName" />   
	    </div>
	    <div>   
	        <label for="manageObjectType">管理对象类型：</label>   
	        <select id="manageObjectType" name="manageObjectType" data-options="panelHeight:'auto',editable:false">   
	        		<option value="人">人</option>   
				    <option value="机">机</option>   
				    <option value="环">环</option>   
				    <option value="管">管</option> 
	        </select>
	    </div>
	    <div>
	    	<a id="ok_btn" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
	    	<a id="cancel_btn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			<input name="ids" type="hidden" />
	    </div>
	</form>
</body>
</html>