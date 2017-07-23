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
		var rows=parent.$("#dg").datagrid("getSelections");
		//被检单位
		$("#cc6").combotree({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment.action',
			required:true,
			editable:false,
			queryParams:{'departmentSn':'1'},
			panelHeight:250
		});
		//评分标准
		$('#cc1').combobox({
			 url:'${pageContext.request.contextPath}/standard/standardAction_getStandard',    
			 valueField:'standardSn',    
			 textField:'standardName',
			 queryParams:{'standardType':'评分标准'},
			 panelHeight:'auto',
			 required:true,
			 editable:false
		});
		//数据回显
		// 对表单的数据进行填充
		$('#ff').form('load',{
			id:rows[0].id,
			periodicalCheckName:rows[0].periodicalCheckName,
			periodicalCheckType:rows[0].periodicalCheckType,
			startDate:rows[0].startDate,
			endDate:rows[0].endDate,
			standardSn:rows[0].standard.standardSn,
			strCheckers:rows[0].strCheckers
		});
		//被检部门回显
		var cd=rows[0].checkedDepartment.isnull
		if(cd==false){
			var departmentSn=rows[0].checkedDepartment.departmentSn;
			var departmentName=rows[0].checkedDepartment.departmentName;
			$("#cc6").combotree('setValue', {id:departmentSn,text:departmentName});
		}
		//提交
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/periodicalAction_update.action',
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
							$("#ff").form("reset");
							parent.$("#win").window("close");
							parent.$("#dg").datagrid("reload");
						}else{
							parent.$.messager.alert("提示信息",msg,'error');
						}						
					}
				});
			}
		});
	})

</script>
</head>
<body>
	<div id="w" style="width:350px; height: 250px; padding: 10px;">
	<form method="post" id="ff">
	<input name="id" type="hidden"/>
		<table>
			<tr>
				<td class="label">定期检查名称：</td>
				<td>
					<input name="periodicalCheckName" class="easyui-textbox" required style="width: 200px;">
				</td>
			</tr>
			<tr>
				<td class="label" width="120px">被检单位/部门：</td>
				<td>
					<input id="cc6" name="departmentSn" class="easyui-combotree" style="width: 200px;">
				</td>
			</tr>
			<tr>
				<td class="label">开始日期：</td>
				<td>
					<input name="startDate" class="easyui-datetimebox" data-options="required:true,editable:false"  style="width:200px">
				</td>
			</tr>
			<tr>
				<td class="label">结束日期：</td>
				<td>	
					<input name="endDate" class="easyui-datetimebox" data-options="required:true,editable:false" style="width:200px;">				
				</td>
			</tr>
			<tr>
				<td class="label" width="120px">依据评分标准：</td>
				<td>
					<input id="cc1" name="standardSn" style="width: 200px;">
				</td>
			</tr>
			<tr>
				<td  rowspan="3" class="label">定期检查类型：</td>
				<td class="label">
					<input type="radio" name="periodicalCheckType" value="周检" checked>周检
					<input type="radio" name="periodicalCheckType" value="旬检">旬检
					<input type="radio" name="periodicalCheckType" value="半月检">半月检
				</td>
			</tr>
			<tr>
				<td class="label">
					<input type="radio" name="periodicalCheckType" value="月检">月检
					<input type="radio" name="periodicalCheckType" value="季检">季检
					<input type="radio" name="periodicalCheckType" value="半年检">半年检
				</td>
			</tr>
			<tr>
				<td class="label">
					<input type="radio" name="periodicalCheckType" value="年检">年检
				</td>
			</tr>
			<tr>
				<td class="label">检查成员：</td>
				<td>
					<input style="width:200px;" name="strCheckers" class="easyui-textbox" 
						data-options="
							multiline:true,
							required:true,
							prompt:'不同人员用“，”隔开',
							height:66"/>
				</td>
			</tr>
		</table>
		<div id="ft" style="padding:30px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
		</div>
	</form>
	</div>
</body>
</html>