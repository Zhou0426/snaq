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
			margin:15px
		}
	</style>
	<script type="text/javascript">
		$(function(){
			var date=new Date();
			var first = 1;
			var rows = parent.$("#dg").datagrid("getSelections");
			//集团检查单位
			$("#checkUnit").combobox({
				url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_queryDepartment',
			    editable:false,
				width:200,
				valueField: 'departmentSn',    
		        textField: 'departmentName', 
			    panelHeight:'auto'
			});
			//外部检查单位
			$('#checkUnitName').textbox({
				height:26,
				width:200
			});
			//检查分类
			$('#checkClass').combobox({
				valueField: 'label',
				textField: 'value',
				required:true,
				editable:false,
				width:200,
				panelHeight:'auto',
				data: [{
					label: '单位自查',
					value: '单位自查'
				},{
					label: '集团公司',
					value: '集团公司'
				},{
					label: '神华集团',
					value: '神华集团'
				},{
					label: '政府部门',
					value: '政府部门'
				}],
				onLoadSuccess:function(node){
					if(rows[0].checkClass == 'undefind'){
						$('#checkClass').combobox('select',"单位自查");
					}else{
						$('#checkClass').combobox('select', rows[0].checkClass);
						if(rows[0].checkClass=='单位自查' || rows[0].checkClass=='集团公司'){
							$('#checkUnit').combobox('setValue',rows[0].departmentSn);
						}else{
							$('#checkUnitName').textbox('setValue', rows[0].checkUnit);
						}
					}
				},
			    onSelect:function(rec){
					if(rec.value=='单位自查'){
						$("#checkUnitNameDiv").css("display",'none');
						$("#checkUnitDiv").css("display",'');
						$('#checkUnit').combobox('reload', {
							pag : "单位自查"
						});
					}else if(rec.value=='集团公司'){
						$("#checkUnitNameDiv").css("display",'none');
						$("#checkUnitDiv").css("display",'');
						$('#checkUnit').combobox('reload', {
							pag : "集团公司"
						});
					}else{
						$("#checkUnitNameDiv").css("display",'');
						$("#checkUnitDiv").css("display",'none');
					}
			    }
			});
			//数据回显
			$('#ff').form('load',{
				id : rows[0].id,
				status : "no",
				checkClass : rows[0].checkClass,
				happenLocation : rows[0].happenLocation,
				happenDateTime : rows[0].happenDateTime.replace("T", " "),
				latentHazardDescription : rows[0].latentHazardDescription
			});
			$('#departmentReportTo').combotree({
				url:'${pageContext.request.contextPath}/department/queryDepartmentByDepartmentTypeJTCS',
				valueField:'id',
				textField:'text',
				panelHeight:200,
				panelWidth:250,
				width:200,
				required:true,
				onLoadSuccess:function(node,data){
					if(first==1){
						if(data.length != 0){
							//将根节点的值默认输出
							$('#departmentReportTo').combotree('setValue', {
								id : rows[0].departmentReportToSn, 
								text : rows[0].departmentReportTo
							});
						}
						first++;
					}
				}
			});
			$('#happenLocation').textbox({
				required:true,
				height:26,
				width:200
			});
			$('#latentHazardDescription').textbox({
				required:true,
				multiline:true,
				width:560,
				height:100
			});
			$('#happenDateTime').datetimebox({    
			    width:200,
				height:26,
			    panelWidth:200,
			    editable:false,
			    required: true
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
							url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_update',
							success: function(result){
								var data = eval( '(' + result + ')' );
								if(data.message == "success"){
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.show({
										title:'提示',
										msg:'更新信息成功！',
										timeout:2000,
										showType:'slide'
									});	
								}else if(data.message == "login"){
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.alert('系统提示','更新失败，请不要修改其他人添加的重大隐患！');
								}else{
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.alert('系统提示','更新失败，请检查后重新操作！');
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
	<form id="ff" method="post" enctype="multipart/form-data">
	    <div>   
	        <label for="happenDateTime">发生时间：&emsp;&emsp;</label>   
	        <input id="happenDateTime" name="happenDateTime" />&emsp;&emsp;
	        <label for="happenLocation">发生地点：&emsp;&emsp;</label>   
	        <input id="happenLocation" name="happenLocation" />  
	    </div>
	    <div>
	    	<label for="departmentReportTo">业务部门：&emsp;&emsp; </label>   
	        <input id="departmentReportTo" name="departmentReportTo" />&emsp;&emsp;
	        <label for="status">是否上报：&emsp;&emsp;</label>   
	        <select id="status" name="status" class="easyui-combobox" 
					data-options="required:true,panelWidth: 200,panelHeight:'auto',editable:false" style="width:200px;height:26px;">
				<option value="yes"">是</option>   
				<option value="no">否</option>
			</select>    
	    </div>
	    <div>
	    	<label for="checkClass">检查分类：&emsp;&emsp; </label>   
	        <input id="checkClass" name="checkClass" />&emsp;&emsp;
	        
	        <label for="checkUnit">检查单位：&emsp;&emsp;</label>
	        <span id="checkUnitDiv" style="display:none;" >
		        <input id="checkUnit" name="checkUnit"/>
	        </span>
	        <span id="checkUnitNameDiv" style="display:none;" >
	        	<input id="checkUnitName" name="checkUnitName"/>
	        </span>
	    </div>
	    <div>
	        <label for="latentHazardDescription">重大隐患描述：</label>   
	        <input id="latentHazardDescription"  name="latentHazardDescription" /> 
	    </div>
	    <div style="text-align:center;">
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    </div>
	    <input type="hidden" name="id" />
	</form>
</body>
</html>