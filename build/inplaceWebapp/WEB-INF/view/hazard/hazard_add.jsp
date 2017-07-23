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
		var d=parent.$('#departmentType').combobox('getText');
		var e=null;
		var f=parent.$('#departmentType').combobox('getValue');
		$(function(){
			//$('#departmentTypeSn').combo({value:f});
			$('#ff').form('load',{departmentTypeSn:f});
			$('#departmentType').textbox({
				width:200,
				value:d,
				disabled:true
			});
			$('#accidentType').combobox({
				url:'${pageContext.request.contextPath}/hazard/hazardAction_accidentType.action',
				valueField:'id',
				textField:'text',
				panelHeight:'auto',
				editable:false,
				width:200,
				required:true,
				onLoadSuccess:function(node,data){
					e=node[0].id;
					if(node.length>0){
						$(this).combobox('select',node[0].id);
					}
				}
			});
			//验证
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
							url:'${pageContext.request.contextPath}/hazard/hazardAction_add.action',
							success: function(result){
								if(result=="\"login\""){
									$.messager.alert('警告','危险源编号已存在，请重新输入！');
									}else{
										if(result=="\"success\""){
											//表单重置
											$("#ff").form("reset");
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
											$("#ff").form("reset");
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
			$("#cancel_btn").click(function(){
				//关闭窗口
				parent.$('#win').window("close")
			});
			$("#reset_btn").click(function(){
				//重置窗口
				$("#ff").form("clear");
				//默认选中
				$('#departmentType').textbox({value:d,disabled:true});
				$('#ff').form('load',{departmentTypeSn:f});
				//$('#departmentTypeSn').combobox('setValue',f);
				$('#accidentType').combobox('select',e);
				$('#riskLevel').combobox('select',value="一般风险");
				//禁用验证
				$("#ff").form("disableValidation");
			});
		});
	</script>
</head>
<body>
	<form id="ff" method="post">   
	    <div>   
	        <label for="departmentType">部门类型：&emsp;</label>   
	        <input id="departmentType"  /> 
	    </div>
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
					<option value="一般风险" selected="selected">一般风险</option>   
				    <option value="中等风险">中等风险</option>
				    <option value="重大风险">重大风险</option>
			</select>    
	    </div>
	    
	    <div>
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="reset_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    	<input  id="departmentTypeSn" name="departmentTypeSn" type="hidden" />
	    </div>
	</form>
</body>
</html>