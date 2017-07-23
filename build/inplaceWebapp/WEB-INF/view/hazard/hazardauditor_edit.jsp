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
			var rows=parent.$('#dg').datagrid('getSelections');
			$('#auditSuggestion').textbox({
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
							url:'${pageContext.request.contextPath}/hazard/hazardReportedAction_audit.action',
							queryParams:{id:rows[0].id},
							success: function(result){
								if(result=="\"success\""){
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.show({
										title:'提示',
										msg:'审核成功！',
										timeout:2000,
										showType:'slide'
									});	
								}else{
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.alert('警告','审核失败，请检查后重新操作！');
								}
								//刷新dg
								parent.$("#dg").datagrid("reload");
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
	        <label for="auditedStatus">审核结果：&emsp;</label>   
	        <select  id="auditedStatus" name=auditedStatus class="easyui-combobox" 
					data-options="required:true,panelWidth: 200,panelHeight:'auto',editable:false" style="width:200px">
					<option value="true" selected="selected">同意</option>   
				    <option value="false">不同意</option>
			</select>    
	    </div>
	    <div>   
	        <label for="auditSuggestion">审核意见：&emsp; </label>   
	        <input id="auditSuggestion"  name="auditSuggestion" />  
	    </div>
	    
	    <div>
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    </div>
	</form>
</body>
</html>