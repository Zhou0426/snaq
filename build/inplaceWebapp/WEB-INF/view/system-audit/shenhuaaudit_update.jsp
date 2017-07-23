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
		var parentrows=parent.$('#dg').datagrid('getChecked');
		//审核单位
		var type=parent.$('#type').val();
		
		//审核单位
		$("#cc1").combobox({
			valueField: 'departmentSn',    
	        textField: 'departmentName',
			url:'${pageContext.request.contextPath}/system/audit/systemauditAction_getDepartment.action',
			required:true,
			editable:false,
			queryParams:{'departmentTypeSn':parentrows[0].auditedDepartment.departmentTypeSn},
			panelHeight:'auto',
		});
		//ajax提交修改
		$('#submit').click(function(){
			var standardSn=parent.$('#cc4').combobox('getValue');
			if($('#ff').form('validate')){
				$('#ff').form('submit',{
					url:'${pageContext.request.contextPath}/system/audit/systemauditAction_update',
					queryParams:{'id':parentrows[0].id},
					success:function(data){
						var data = eval('(' + data + ')');  
						if(data.status=="ok"){
							parent.$.messager.alert("提示信息",'修改成功！');
							$("#ff").form("reset");
							parent.$("#add").dialog("close");
							parent.$("#dg").datagrid("reload");
						}else if(data.status=="year"){
							parent.$.messager.alert("提示信息",'当前修改的单位本年度审核信息已经录入，无法修改！');
						}else{
							parent.$.messager.alert("提示信息",'修改失败！','error');
						}
					}
				});
			}
		});

		//数据回显
		$('#ff').form('load',{
			startDate:parentrows[0].startDate,
			endDate:parentrows[0].endDate,
			strAuditTeamLeader:parentrows[0].strAuditTeamLeader,
			strCheckers:parentrows[0].strCheckers
		});
		//被检部门回显
		var cd=parentrows[0].auditedDepartment.isnull
		if(cd==false){
			var departmentSn=parentrows[0].auditedDepartment.departmentSn;
			var departmentName=parentrows[0].auditedDepartment.departmentName;
			$("#cc1").combobox('setValue',departmentSn);
		}
		
	})
</script>
</head>
<body>
	<!-- 添加 -->
	<form id="ff" method="post">
	<input name="systemAuditType" value="神华审核" type="hidden"/>
		<table>
        	<tr>
        		<td class="label">被审单位：</td>
        		<td>
        			<input id="cc1" name="departmentSn" style="width:200px"/>
        		</td>        		
        	</tr>
        	<tr>
        		<td class="label">开始日期：</td>
        		<td>
        			<input  name="startDate" class="easyui-datebox" required style="width:200px">
        		</td>        		
        	</tr>
        	<tr>
        		<td class="label">结束日期：</td>
        		<td>
        			<input  name="endDate" class="easyui-datebox" required style="width:200px">
        		</td>        		
        	</tr>
        	<tr>
        		<td class="label">审核组长：</td>
        		<td>
        			<input name="strAuditTeamLeader" class="easyui-textbox" style="width:200px;"
        				data-options="prompt:'输入姓名'"/>
        		</td>        		
        	</tr>
        	<tr>
        		<td class="label">审核成员：</td>
        		<td>
        			<input name="strCheckers" class="easyui-textbox" style="width:200px;"
        				data-options="prompt:'不同成员用“,”隔开',multiline:'true',height:88"/>
        		</td>        		        		
        	</tr>       	
        </table>
        <div id="submit" style="padding:5px;text-align:center;margin-top:30px">
    	 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确认修改</a>
    	</div>
    </form>
</body>
</html>