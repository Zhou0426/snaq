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
		//数据回显
		var rows=parent.$("#dg").datagrid("getSelections");
		//对表单数据进行填充
		$('#ff').form('load',{
			id:rows[0].id,
			documentTemplateSn:rows[0].documentTemplateSn,
			documentTemplateName:rows[0].documentTemplateName
		});
		

		//定义验证规则，通过属性选择器吧com转化为jquery对象
		$("[name=documentTemplateSn]").textbox({
  			required:true,
  			missingMessage:'模板编号必填!',
  			tipPosition:'bottom',
  			width:210
  		});
		$("[name=documentTemplateName]").textbox({
  			required:true,
  			missingMessage:'模板编号必填!',
  			tipPosition:'bottom',
  			width:210
  		});
		//页面加载时禁止使用验证对照
		$("#ff").form("disableValidation");
		//触发重置事件
		$("#reset").click(function(){
			$("#ff").form("reset");
		});
		//触发提交事件
		$("#submit").click(function(){
			//表单数据验证
			$("#ff").form("enableValidation");
			//验证通过返回true
			if($("#ff").form("validate")){
				//ajax
				$('#ff').form("submit",{
					url:'standard/documentTemplateAction_update.action',
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							$("#ff").form("reset");
							//关闭窗体
							parent.$("#win").window("close");
							//刷新dg
							parent.$("#dg").datagrid("reload");
							parent.$.messager.alert("提示信息",msg);
						}else{
							parent.$.messager.alert("提示信息",msg,'error');
						}				
					}
				});
			}
		});
	});
</script>
</head>
<body>
	<form id="ff" method="post">   
	        <input type="hidden" name="id" />   
	    <div style="margin-top: 22px">   
	        <label for="documentTemplateSn">模板编号:</label>
	        <input name="documentTemplateSn" />   
	    </div>
	    <div style="margin-top: 22px">   
	        <label for="documentTemplateName">模板名称:</label>
	        <input name="documentTemplateName" />   
	    </div>   
	    <div style="margin-top: 25px;text-align:center">
	    	<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确认修改</a>  
	    </div>
	</form>  
</body>
</html>