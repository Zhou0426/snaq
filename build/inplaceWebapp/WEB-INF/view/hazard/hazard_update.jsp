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
				var rows=parent.$("#dg").datagrid("getSelections");
				//数据回显
				$('#ff').form('load',{
						id:rows[0].id,
						departmentTypeSn:rows[0].departmentTypeSn,
						hazardSn:rows[0].hazardSn,
						hazardDescription:rows[0].hazardDescription,
						resultDescription:rows[0].resultDescription
				});
				$('#accidentType').combobox({
					url:'${pageContext.request.contextPath}/hazard/hazardAction_accidentType.action',
					valueField:'id',
					textField:'text',
					panelHeight:'auto',
					width:200,
					editable:false,
					required:true,
					onLoadSuccess:function(node,data){
						if(node.length>0){
							$(this).combobox('select',id=rows[0].accidentTypeSn);
						}
					}
				});
				$('#riskLevel').combobox('select',value=rows[0].riskLevel);
			//验证数据必填
			$('#hazardSn').textbox({
				required:true,
				width:200
			});
			$('#hazardDescription').textbox({
					required:true,
					multiline:true,
					width:200,
					height:60
			});
			$('#resultDescription').textbox({
					required:true,
					multiline:true,
					width:200,
					height:60
			});
			//弹出窗口时禁用验证
			$("#ff").form("disableValidation");
			//注册botton的事件
			$("#ok_btn").click(function(){
					//开启验证
					$("#ff").form("enableValidation");
					//如果验证成功，则提交数据
					if($('#ff').form("validate")){
						$('#ff').form('submit', {
							url:'${pageContext.request.contextPath}/hazard/hazardAction_update.action',
							success: function(data){
								if(data=="\"login\""){
									$.messager.alert('警告','危险源编号已存在，请重新输入！');
								}else{
									if(data=="\"success\""){
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
	        <label for="hazardSn">危险源编号：</label>   
	        <input id="hazardSn" name="hazardSn"  />   
	    </div>   
	    <div>   
	        <label for="hazardDescription">危险源描述：</label>   
	        <input id="hazardDescription"  name="hazardDescription"  /> 
	    </div>
	    <div>   
	        <label for="resultDescription">后果描述：&emsp; </label>   
	        <input id="resultDescription"  name="resultDescription" />  
	    </div>
	    <div>   
	        <label for="accidentType">事故类型：&emsp; </label>   
	        <input name="accidentTypeSn"  id="accidentType" /> 
	    </div>
	    <div>   
	        <label for="riskLevel">风险等级：&emsp;</label>   
	        <select  id="riskLevel" name=riskLevel class="easyui-combobox" 
					data-options="required:true,panelWidth: 200,panelHeight:'auto',editable:false" style="width:200px">
					<option value="一般风险" >一般风险</option>   
				    <option value="中等风险">中等风险</option>
				    <option value="重大风险">重大风险</option>
			</select>    
	    </div>
  		<div>
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    	<input type="hidden" name="id" />
	    	<input type="hidden" id="departmentTypeSn"  name="departmentTypeSn" />
	   
	    </div>
	</form>
</body>
</html>