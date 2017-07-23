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
		$('#cc3').combogrid('clear');
		var pvlist = new Array();
		for(var i=0;i<data2.length;i++){
			pvlist.push({ id: data2[i].id, personName: data2[i].personName });	
		}
		$('#cc3').combogrid('setValues',pvlist);
	}
	$(function(){
		var parentrows=parent.$('#dg').datagrid('getChecked');
		//审核单位
		var type=parent.$('#type').val();
		var departmentSn="${sessionScope['departmentSn']}";
		var departmentName="${sessionScope['departmentName']}";
		//var id="${person.id}";
		// personName="${person.personName}";
		
		//组长
		$('#cc2').combogrid({
    		idField:'personId',    
   	 		textField:'personName',
   	 		value:'',
    		url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getPerson.action', 
    		delay: 500,    
		    mode:'remote',
		    prompt:'输入姓名或编号搜索人员',
		    queryParams:{'departmentSn':parentrows[0].auditedDepartment.departmentSn},
    		columns:[[    
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'personId',title:'人员编号'},    
				{field:'personName',title:'人员姓名'},    
				{field:'gender',title:'性别'},
    		]]    
		});
		//审核组员
		$('#cc3').combogrid({          
   			idField:'id',    
    		textField:'personName',
    		prompt:'下拉进行检索',
    		multiple:true,
    		editable:false,
    		panelHeight:'auto',
    		multiline:true,
    		height:66,
    		queryParams:{'departmentSn':parentrows[0].auditedDepartment.departmentSn},
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
   		 			$('#cc3').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':parentrows[0].auditedDepartment.departmentSn});
   		 		}
   		 	}
		});
		//ajax提交修改
		$('#submit').click(function(){
			var personIds="";
			var cc3=$('#cc3').combogrid('getValues');
			var standardSn=parent.$('#cc4').combobox('getValue');
			for(var i=0;i<cc3.length;i++){
				personIds+=cc3[i]+","
			}
			personIds=personIds.substring(0,personIds.length-1);
			if($('#ff').form('validate')){
				$('#ff').form('submit',{
					url:'${pageContext.request.contextPath}/system/audit/systemauditAction_update',
					queryParams:{'ids':personIds,'id':parentrows[0].id},
					success:function(data){
						var data = eval('(' + data + ')');  
						if(data.status=="ok"){
							parent.$.messager.alert("提示信息",'修改成功！');
							$("#ff").form("reset");
							parent.$("#add").dialog("close");
							parent.$("#dg").datagrid("reload");
						}else if(data.status=="month"){
							parent.$.messager.alert("提示信息",'当前修改的单位本月审核信息已经录入，无法修改！');
						}else{
							parent.$.messager.alert("提示信息",'修改失败！','error');
						}
					}
				});
			}
		});

		//审核成员表
		$('#dg').datagrid({
			idField:'id',
			singleSelect:true,
			title:'审核成员',
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
		$('#ft').click(function(){
			$("#import-ff").form("enableValidation");
			if($('#import-ff').form('validate')){
				$.messager.confirm('确认对话框', '您确定要导入当前文件吗？', function(r){
					if (r){
						$('#import-ff').form('submit',{
							url:'${pageContext.request.contextPath}/system/audit/systemauditAction_importP',
							queryParams:{'departmentSn':parentrows[0].auditedDepartment.departmentSn},
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
								$('#cc3').combogrid('clear');
								var pvlist2 = new Array();
								for(var i=0;i<rows.length;i++){
									pvlist2.push({ id: rows[i].id, personName: rows[i].personName });	
								}
								$('#cc3').combogrid('setValues',pvlist2);
								$('#dg').datagrid('loadData',result);
								$('#import').dialog('close');
							}
						});
					}
				});
			}			
		});
		//数据回显
		$('#cc1').textbox({
               value:parentrows[0].auditedDepartment.departmentName,    
               readonly:true        
        });
		$('#ff').form('load',{
			startDate:parentrows[0].startDate,
			endDate:parentrows[0].endDate
		});
		//审核组长回显
		if(parentrows[0].auditTeamLeader.isnull==false){
			$('#cc2').combogrid('setValue', {personId:parentrows[0].auditTeamLeader.personId,personName:parentrows[0].auditTeamLeader.personName});
		}
		//审核成员回显
		if(parentrows[0].auditors.num>0){
			var pvlist3 = new Array();
			var pvlist4 = new Array();
			var Ids=parentrows[0].auditors.Ids.split(',');
			var pNames=parentrows[0].auditors.personNames.split(',');
			var genders=parentrows[0].auditors.genders.split(',');
			var pIds=parentrows[0].auditors.personIds.split(',');
			for(var i=0;i<parentrows[0].auditors.num;i++){
				pvlist3.push({ id: Ids[i], personName:pNames[i] });
				pvlist4.push({ id: Ids[i], personName:pNames[i] ,personId:pIds[i],gender:genders[i]});	
			}
			$('#cc3').combogrid('setValues',pvlist3);
			$('#dg').datagrid('loadData',pvlist4);
		}
		
			    
	})
</script>
</head>
<body>
	<!-- 添加 -->
	<form id="ff" method="post">
	<input name="systemAuditType" value="单位审核" type="hidden"/>
		<table>
        	<tr>
        		<td class="label">被审单位：</td>
        		<td>
        			<input id="cc1" class="easyui-textbox" style="width:200px"/>
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
        			<input id='cc2' name="personId" style="width:200px;"/>
        		</td>        		
        	</tr>
        	<tr>
        		<td class="label">审核成员：</td>
        		<td>
        			<input id="cc3" style="width:200px;"/>
        		</td>        		        		
        	</tr>       	
        </table>
        <div id="submit" style="padding:5px;text-align:center;margin-top:10px">
    	 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确认修改</a>
    	</div>
    </form>
    <div id="">
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
    			<a id="ft" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-import'">导入</a>
    	</div>
    </div>
</body>
</html>