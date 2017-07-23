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

		function toDoThing(){
			$.ajax({
				   type: "POST",
				   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
				   success: function(msg){
					   var rec=eval('(' + msg + ')');
					   //console.log(parent.$('#mm21').attr('id'));
					   parent.parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
					   parent.parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
					   parent.parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
					   parent.parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
					   parent.parent.$('#mm25').html("需我审批的["+rec.DeferThing+"]");
					   parent.parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
				   }
			});
		};
		$(function(){
			var rows=parent.$('#dg').datagrid('getSelections');
			$('#auditRemark').textbox({
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
							url:'${pageContext.request.contextPath}/inconformity/item/unsafeConditionDeferAction_audit.action',
							queryParams:{applicationSn:rows[0].applicationSn},
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
								toDoThing();
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
	        <label for="passed">审批结果：&emsp;</label>   
	        <select  id="passed" name=passed class="easyui-combobox" 
					data-options="required:true,panelWidth: 200,panelHeight:'auto',editable:false" style="width:200px">
					<option value="true" selected="selected">同意</option>   
				    <option value="false">不同意</option>
			</select>    
	    </div>
	    <div>   
	        <label for="auditRemark">审批说明：&emsp; </label>   
	        <input id="auditRemark"  name="auditRemark" />  
	    </div>
	    
	    <div>
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    </div>
	</form>
</body>
</html>