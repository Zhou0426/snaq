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
		//审核单位
		var type=parent.$('#type').val();
		//var departmentSn="${sessionScope['departmentSn']}";
		//var departmentName="${person.department.departmentName}";
		//var id="${person.id}";
		//var personName="${person.personName}";
		//审核单位
		$("#cc1").combobox({
			valueField: 'departmentSn',    
	        textField: 'departmentName',
			url:'${pageContext.request.contextPath}/system/audit/systemauditAction_getDepartment.action',
			required:true,
			editable:false,
			queryParams:{'departmentTypeSn':parent.$('#cc3').combobox('getValue')},
			panelHeight:'auto',
		});

		//ajax提交
		$('#submit').click(function(){
			var standardSn=parent.$('#cc4').combobox('getValue');
			if($('#ff').form('validate')){
				$('#ff').form('submit',{
					url:'${pageContext.request.contextPath}/system/audit/systemauditAction_save',
					queryParams:{'standardSn':standardSn,'systemAuditType':type},
					success:function(data){
						var data = eval('(' + data + ')');  
						if(data.status=="ok"){
							parent.$.messager.alert("提示信息",'添加成功！');
							$("#ff").form("reset");
							parent.$("#add").dialog("close");
							parent.$("#dg").datagrid("reload");
						}else if(data.status=="year"){
							parent.$.messager.alert("提示信息",'当前单位本年度审核已经录入，无需重复录入！');
						}else{
							parent.$.messager.alert("提示信息",'添加失败！','error');
						}
					}
				});
			}
		});
			    
	})
</script>
</head>
<body>
	<!-- 添加 -->
	<form id="ff" method="post">
	<input name="editorId" type="hidden" value="${sessionScope.personId}"/>
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
        <div id="submit" style="padding:5px;text-align:center;margin-top:20px">
    	 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
    	</div>
    </form>
</body>
</html>