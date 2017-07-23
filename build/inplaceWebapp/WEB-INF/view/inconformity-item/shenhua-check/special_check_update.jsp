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
			specialCheckName:rows[0].specialCheckName,
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
		
		//专业
		$('#cc3').combobox({    
			url:'${pageContext.request.contextPath}/inconformity/item/specialityAction_query',
			panelHeight:'auto',
		   	valueField:'id',    
		    textField:'specialityName',
		    editable:false,   
		    multiple:true,
		    required:true,
		    onLoadSuccess:function(){
		    	var num2=rows[0].specialities.num;
		    	if(num2>0){
					var sId=rows[0].specialities.sSn;
					var sIds=sId.split(',');
					for(var i=0;i<sIds.length;i++){
						$('#'+sIds[i]+'').attr('checked', true);
					}
				}		    	
			},
		    formatter: function (row) {
                //var opts = $(this).combobox('options');
                return '<input id="'+row.id+'" type="checkbox" class="combobox-checkbox"><span style="font-size:14px;">'+row.specialityName+'</span>';
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
		
		
		//专业回显
		var num2=rows[0].specialities.num;
		if(num2>0){
			var sId=rows[0].specialities.sSn;
			var sIds=sId.split(',');
			$('#cc3').combobox('setValues',sIds);
		}
		//提交
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			var specialityIds="";
			var cc3=$('#cc3').combobox('getValues');
			for(var j=0;j<cc3.length;j++){
				specialityIds+=cc3[j]+","
			};
			specialityIds=specialityIds.substring(0,specialityIds.length-1);
			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/specialAction_update.action',
					queryParams:{specialityIds:specialityIds},
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
	<div id="w" style="width:350px; height: 290px; padding: 10px;">
	<form method="post" id="ff">
	<input name="id" type="hidden"/>
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
		<div id="ft" style="padding: 35px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
		</div>
	</form>
	</div>
</body>
</html>