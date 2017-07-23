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
		var departmentTypeSn=parent.$('#cc').combobox('getValue');
		$('#ff').form('load',{
			departmentTypeSn:departmentTypeSn
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
					url:'standard/standardAction_save.action',
					//queryParams{departmentTypeSn:departmentTypeSn},
				    onSubmit: function(){    
				    	departmentTypeSn=departmentTypeSn;    
				    },    
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
							$("#ff").form("reset");
							//关闭窗体
							parent.$("#st-win").window("close");
							//刷新dg
							parent.$("#st").datagrid("reload");
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
		<input type="hidden" name="departmentTypeSn"/>
	    <div style="margin-top: 8px">   
	        <label for="standardSn">标准编号:</label>
	        <input name="standardSn" class="easyui-textbox"  data-options="width:240,required:true,missingMessage:'标准编号必填!'" />   
	    </div>
	    <div style="margin-top: 8px">   
	        <label for="standardName">标准名称:</label>
	        <input class="easyui-textbox" name="standardName" data-options="width:240,required:true,missingMessage:'标准名称必填!'"/>   
	    </div>
	    <div style="margin-top: 8px">   
	        <label for="standardType">标准类型:</label>
			<input  class="easyui-combobox" name="standardType" data-options="
				valueField: 'label',
				textField: 'value',
				editable:false,
				width:240,
				panelHeight:'auto',
				required:'true',
				missingMessage:'标准类型必选!',
				data: [{
					label: '审核指南',
					value: '审核指南'
				},{
					label: '评分标准',
					value: '评分标准'
				}]" />

			  
	    </div>  
	    <div style="margin-top: 25px;text-align:center">
	    	<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>  
	    	<a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>  
	    </div>
	</form>  
</body>
</html>