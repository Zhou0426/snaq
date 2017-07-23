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
		//专业
		$('#cc3').combobox({    
			url:'${pageContext.request.contextPath}/inconformity/item/specialityAction_query',
			panelHeight:'auto',
		   	valueField:'id',    
		    textField:'specialityName',
		    editable:false,   
		    multiple:true,
		    required:true,
		    formatter: function (row) {
                //var opts = $(this).combobox('options');
                return '<input type="checkbox" class="combobox-checkbox"><span style="font-size:14px;">'+row.specialityName+'</span>';
            },
            onSelect: function (row) {
                //console.log(row);
                var opts = $(this).combobox('options');
                var el = opts.finder.getEl(this, row[opts.valueField]);
                el.find('input.combobox-checkbox')._propAttr('checked', true);
            },
            onUnselect: function (row) {
                var opts = $(this).combobox('options');
                var el = opts.finder.getEl(this, row[opts.valueField]);
                el.find('input.combobox-checkbox')._propAttr('checked', false);
            }
		});
		//提交
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			var sIds="";
			var cc3=$('#cc3').combobox('getValues');			
			for(var j=0;j<cc3.length;j++){
				sIds+=cc3[j]+","
			};
			sIds=sIds.substring(0,sIds.length-1);
			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/specialAction_save.action',
					queryParams:{specialityIds:sIds},
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
	<div id="w" style="width:340px; height:250px; padding:10px;">
	<form method="post" id="ff">
		<input name="personId" value="${sessionScope.personId}" type="hidden"/>
		<input name="checkerFrom" value="神华" type="hidden">
		<table>
			<tr>
				<td class="label">专项检查名称：</td>
				<td>
					<input name="specialCheckName" class="easyui-textbox" required style="width: 200px;">
				</td>
			</tr>
			<tr>
				<td class="label" width="120px">被检单位/部门：</td>
				<td>
					<input id="cc6" name="departmentSn" class="easyui-combotree" style="width: 200px;">
				</td>
			</tr>
			<tr>
				<td class="label">编辑人员：</td>
				<td>	
					<input value="${sessionScope.personName}" class="easyui-textbox" readonly style="width: 200px;"/>			
				</td>
			</tr>
			<tr>
				<td class="label" width="120px">开始日期：</td>
				<td>
					<input name="startDate" class="easyui-datetimebox" data-options="required:true,editable:false" style="width:200px">
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
				<td class="label">检查专业：</td>
				<td>	
					<input id="cc3" style="width: 200px;"/>			
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
		<div id="ft" style="padding: 25px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
		</div>
	</form>
	</div>
</body>
</html>