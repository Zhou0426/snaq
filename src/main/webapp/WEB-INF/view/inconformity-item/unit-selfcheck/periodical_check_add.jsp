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
	function deleted(index){
		$('#dg').datagrid('deleteRow',index);
		var data=$('#dg').datagrid('getData');
		var data2=$('#dg').datagrid('getRows');
		$('#dg').datagrid('loadData',data);
		$('#dg').datagrid('clearSelections');
		$('#cc2').combogrid('clear');
		var pvlist = new Array();
		for(var i=0;i<data2.length;i++){
			pvlist.push({ id: data2[i].id, personName: data2[i].personName });	
		}
		$('#cc2').combogrid('setValues',pvlist);
	}
	$(function(){
		var departmentSn="${sessionScope['departmentSn']}";
		$.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/system/audit/systemauditAction_getNearDeaprtment",
            data: {departmentSn:departmentSn},
            dataType: "json",
            success: function(data){
				departmentSn=data.departmentSn;
				//被检单位		
				$("#cc6").combotree({
					url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment',
					required:true,
					editable:false,
					queryParams:{'departmentSn':departmentSn},
					panelHeight:250
				});
				
				$('#cc6').combotree('setValue',departmentSn);
            }
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
			
		//检查人员
		$('#cc2').combogrid({           
   			idField:'id',    
    		textField:'personName',
    		height:66,
    		multiple:true,
    		multiline:true,
    		editable:false,
    		prompt:'下拉进行检索',
   			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getPerson.action',
   			toolbar:[{
   				text:'<input id="tb" style="width:180px">', 
   			}],
   			onSelect:function(index,row){
   				$('#dg').datagrid('appendRow',{
   					id: row.id,
   					personId:row.personId,
   					personName:row.personName,
   					gender:row.gender
   				});   				   				
   	   		},
   	   		onUnselect:function(index,row){
   	   			$('#dg').datagrid('selectRecord',row.id);
   	   			var g=$('#dg').datagrid('getSelected');
   	   			var f=$('#dg').datagrid('getRowIndex',g); 	   			
   	   			$('#dg').datagrid('deleteRow',f);
   	   	   	},
   			columns:[[    
   	        	{field:'id',title:'逻辑主键',hidden:true},
        		{field:'personId',title:'人员编号',width:80},    
       	 		{field:'personName',title:'人员姓名',width:80},    
        		{field:'gender',title:'性别',width:40},    
    
    		]]    
		});
		//搜索人员
		$('#tb').textbox({    
    		buttonText:'搜索',    
    		iconCls:'icon-man', 
   		 	iconAlign:'left',
   		 	prompt:'输入姓名或编号搜索人员',
   		 	onChange:function(newValue, oldValue){
   		 		if(newValue!=""){
   		 			$('#cc2').combogrid('grid').datagrid('load',{q:newValue,'departmentSn':departmentSn});
   		 		}
   		 	}
		});
		
		//提交
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			//将检查人员数组处理一下
			var personIds="";
			var cc2=$('#cc2').combogrid('getValues');
			for(var i=0;i<cc2.length;i++){
				personIds+=cc2[i]+","
			}
			personIds=personIds.substring(0,personIds.length-1);
			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/periodicalAction_save.action',
					queryParams:{ids:personIds},
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
		
		//检查成员表
		$('#dg').datagrid({
			idField:'id',
			singleSelect:true,
			title:'检查成员',
			toolbar:[{
				text:'导入成员',
				iconCls:'icon-import',
				handler:function(){
					$('#import').dialog('open');
				}
			},{
				text:'导出成员',
				iconCls:'icon-excel',
				handler:function(){
					//var dg=$('#cc3').combobox('getValues');
					var dg=$('#dg').datagrid('getRows');
					var personIds="";
					var personNames="";
					for(var i=0;i<dg.length;i++){
						personIds+=dg[i].personId+",";
						personNames+=dg[i].personName+",";
					}
					personIds=personIds.substring(0,personIds.length-1);
					personNames=personNames.substring(0,personNames.length-1);
					
					//$.post("${pageContext.request.contextPath}/system/audit/systemauditAction_exportP",{personIds:personIds,personNames:personNames});
					//向后台发送请求
					var form=$("<form>");
					form.attr("style","display:none");
					form.attr("target","");
					form.attr("method","post");
					form.attr("action","${pageContext.request.contextPath}/system/audit/systemauditAction_exportP");

					var input1=$("<input>");
					input1.attr("type","hidden");
					input1.attr("name","personIds");
					input1.attr("value",personIds);
					
					
					var input2=$("<input>");
					input2.attr("type","hidden");
					input2.attr("name","personNames");
					input2.attr("value",personNames);
					//将表单放入body
					$("body").append(form);
					form.append(input1);
					form.append(input2);
					form.submit();//提交表单
				}
			}],        
		    columns:[[      
   	        	{field:'id',title:'逻辑主键',hidden:true},
        		{field:'personId',title:'人员编号',width:80},    
       	 		{field:'personName',title:'人员姓名',width:80},    
        		{field:'gender',title:'性别',width:40},
		        {field:'delete',title:'删除',width:100,formatter:function(value,row,index){
					return '<a href="#" onclick="deleted('+index+')" style="text-decoration:none;">删除</a>'
			    }}    
		    ]]    
		});
		//人员导入
		$('#import').dialog({    
		    title: '人员导入',    
		    width: 300,    
		    height: 130,    
		    closed: true,    
		    cache: false,
		    modal: true   
		});
		
		//自定义验证函数
		$.extend($.fn.validatebox.defaults.rules,{
			type:{
				validator:function(value,param){
					//获取文件扩展名
					//alert("value:" + value + ",param:" + param.length);
					var ext=value.substring(value.lastIndexOf(".")+1);
					var params=param[0].split(",");
					for(var i=0;i<params.length;i++){
						if(ext==params[i])
							return true;
					}
					return false;

				},
				//{0}代表传入的第一个参数
				message: '文件类型必须为:{0}'
			}
		});
		$("[name='excel']").validatebox({
			required:true,//file文本域 validate不能实现及时验证
			missingMessage:'请上传正确的文件',
			validType:"type['xls']"		
		});
		$("#import-ff").form("disableValidation");
		$('#import-btn').click(function(){
			$("#import-ff").form("enableValidation");
			if($('#import-ff').form('validate')){
				$.messager.confirm('确认对话框', '您确定要导入当前文件吗？', function(r){
					if (r){
						$('#import-ff').form('submit',{
							url:'${pageContext.request.contextPath}/system/audit/systemauditAction_importP',
							success:function(data){
								var result = eval('(' + data + ')');
								var rows=result.rows;
								var error=result.error;
								if(error[0].errornum>0){
									parent.$.messager.alert('我的消息',error[0].error,'info');
								}else{
									parent.$.messager.alert('我的消息','导入成功！','info');
								}
								$("#import-ff").form("disableValidation");
								$("#import-ff").form("reset");
								$('#cc2').combogrid('clear');
								var pvlist2 = new Array();
								for(var i=0;i<rows.length;i++){
									pvlist2.push({ id: rows[i].id, personName: rows[i].personName });	
								}
								$('#cc2').combogrid('setValues',pvlist2);
								$('#dg').datagrid('loadData',result);
								$('#import').dialog('close');
							}
						});
					}
				});
			}			
		});
	})

</script>
</head>
<body>
	<div id="w" style="width:350px; height:250px; padding: 10px;">
	<form method="post" id="ff">
		<input name="personId" value="${sessionScope.personId}" type="hidden"/>
		<input name="checkerFrom" value="单位" type="hidden"/>
		<table>
			<tr>
				<td class="label">编辑人员：</td>
				<td>	
					<input value="${sessionScope.personName}" class="easyui-textbox" readonly style="width: 200px;"/>			
				</td>				
			</tr>
			<tr>
				<td class="label" width="120px">定期检查名称：</td>
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
					<input id="cc2" style="width:200px;">
				</td>
			</tr>
		</table>
		<div id="ft" style="margin-top: 20px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
		</div>
	</form>
	<div>
    	<table id="dg"></table>
    </div>
    <div id="import">
    	<table>
    		<tr>
    			<td colspan="2">
    				<form id="import-ff" method="post" enctype='multipart/form-data'>
    					<input class="easyui-filebox" name='excel' style="width:250px" data-options="buttonText: '选择文件'";>
    				</form>    				
    			</td>
    		</tr>    		
    	</table>
    	<div style="margin-top: 15px;text-align:center">
    			<a href="${pageContext.request.contextPath}/system/audit/systemauditAction_personTemplate" style="text-decoration: none">没有模板？点我下载！</a>
    			<a id="import-btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-import'">导入</a>
    	</div>
    </div>
	</div>
</body>
</html>