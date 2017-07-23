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
			var d=parent.$('#departmentTypeSn').combobox('getText');
			var e=parent.$('#checked_mansger').combotree('getText');
			var f=parent.$('#departmentTypeSn').combobox('getValue');
			var g=parent.$('#checked_mansger').combotree('getValue');
			$('#departmentType').textbox({
					width:200,
					value:d,
					disabled:true
				});
			$('#parentManageObject').textbox({
				width:200,
				value:e,
				disabled:true
				});
			$('#departmentTypeSn').combo({value:f});
			$('#parentManageObjectSn').combo({value:g});
			//console.log($('#parentManageObjectSn').validatebox('getValue'));
			$("input[name=manageObjectSn]").validatebox({
					required:true,
					missingMessage:'请输入管理对象编号'
				});
			$("input[name=manageObjectName]").validatebox({
				required:true,
				missingMessage:'请输入管理对象名称'
			});
			$("input[name=manageObjectType]").validatebox({
				required:true,
				missingMessage:'请选择管理对象类型'
			});
			//弹出窗口时禁用验证
			$('#ff').form("disableValidation");
			//注册botton的事件
			$('#ok_btn').click(function(){
					//开启验证
					$('#ff').form("enableValidation");
					//如果验证成功，则提交数据
					if($('#ff').form("validate")){
						//调用submit方法，提交数据
						$('#ff').form('submit', {
							url :'${pageContext.request.contextPath}/hazard/manageObjectAction_add.action',
							success: function(result){
								if(result=="\"login\""){
									$.messager.alert('警告','管理对象编号已存在，请重新输入！');
								}else{
									if(result=="\"success\""){
										//表单重置
										$('#ff').form("reset");
										//关闭当前页面
										parent.$('#win').window("close");
										parent.$.messager.show({
											title:'提示',
											msg:'添加信息成功！',
											timeout:2000,
											showType:'slide'
										});	
										}else{
											//表单重置
											$('#ff').form("reset");
											//关闭当前页面
											parent.$('#win').window("close");
											parent.$.messager.alert('警告','添加失败，请检查后重新操作！');
										}
									//刷新dg
									parent.$("#dg").datagrid("reload");
								}
							}
						});						
					}
				});
			$('#cancel_btn').click(function(){
				//关闭窗口
				parent.$('#win').window("close")
			});
			$('#reset_btn').click(function(){
				//重置窗口
				$('#ff').form("clear");
				//默认选中
				$('#departmentType').textbox({value:d,disabled:true});
				$('#parentManageObject').textbox({value:e,disabled:true});
				$('#departmentTypeSn').combo({value:f});
				$('#parentManageObjectSn').combo({value:g});
				$('#manageObjectType').combobox('select',value="人");
				//禁用验证
				$('#ff').form("disableValidation");
			});
		});
	</script>
</head>
<body>
	<form id="ff" method="post">   
	    <div>   
	        <label for="departmentType">所属部门类型：</label>   
	        <input id="departmentType"  />   
	    </div>
	    <div>
	        <label for="manageObjectSn">管理对象编号：</label>   
	        <input class="easyui-textbox" name="manageObjectSn" style="width: 190px "/>   
	    </div>
	    <div>   
	        <label for="manageObjectName">管理对象名称：</label>   
	        <input class="easyui-textbox" name="manageObjectName" style="width: 190px "/>   
	    </div>
	    <div>   
	        <label for="manageObjectType">管理对象类型：</label>   
<!-- 	        <input type="text" name="manageObjectType" style="width: 190px "/>  -->
	        <select id="manageObjectType" name="manageObjectType" class="easyui-combobox" data-options="panelHeight:'auto'" style="width: 190px ">
	        	<option value="人" selected="selected">人</option>
	        	<option value="机" >机</option>
	        	<option value="环" >环</option>
	        	<option value="管" >管</option>
	        </select>
	    </div>
	    <div>
	        <label for="parentManageObject">父级管理对象：</label>   
	        <input id="parentManageObject"  />
	    </div>
	    <div>
	    	<a id="ok_btn"  class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="reset_btn"  class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
	    	<a id="cancel_btn"  class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    	<div style="display:none">
	    		<input  id="departmentTypeSn" name="departmentTypeSn"  type="hidden" />
	    		<input id="parentManageObjectSn" name="parentManageObjectSn" type="hidden" />
	    	</div>
	    </div>
	</form>
</body>
</html>