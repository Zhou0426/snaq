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
		var rows1=parent.$("iframe").get(0).contentWindow.$("#dg").treegrid("getSelected");
		var rows2=parent.$("#dg").datagrid("getChecked");
		var auditSn=rows2[0].auditSn;
		var departmentSn=rows2[0].auditedDepartment.departmentSn;
		var indexSn=rows1.indexSn;
		var id=-1;
		$('#dg').datagrid({
			fit:true,
			stript:true,
			nowrap:true,
			loadMsg:'请等待',
			rownumbers:true,
			romoteSort:false,
			singleSelect:true,
			checkOnSelect:false,
			clickToEdit:false,
			dblclickToEdit:true,
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,20,50],
			onSelect:function(index, row){
				id=row.id;
			},
			onLoadSuccess:function(data){
				var com=100;
				var rows=data['rows'];
				for(var i=0;i<rows.length;i++){
					if(com>rows[i].conformDegree){
						com=rows[i].conformDegree;
					}
				}
				$('#type').val(com);
			},
			/*按钮*/
			toolbar:[{
				text:'<input id="cc3" style="width:50px;"/>'
			},{
				id:'add',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					$('#dd').dialog('open');
				}
			},{
				id:'delete',
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					//1、获取选中行
					var row=$("#dg").datagrid("getSelected");
					if(row){
						$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
							if(r){
								$.ajax({
						            type: "POST",
						            url: "${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_delete",
						            data: {'id':row.id,'indexSn':indexSn,'auditSn':auditSn,'departmentSn':departmentSn},
						            dataType: "json",
						            success: function(data){			                	
						                   if(data.status=="nook"){
						                	   $.messager.alert('我的消息','删除失败！','error');
							               }else{
							            	   $.messager.alert('我的消息','删除成功！','info');
							               }
						                   $('#dg').datagrid('reload');            
						            }
						        });
							}
						})
					}else{
						$.messager.show({
							title:'提示',
							msg:'至少选择为一条记录！',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+100,
								bottom:''
							}
						});
					}
				}
			},{
				id:'update',
				text:'修改',
				iconCls:'icon-edit',
				handler:function(){
					//1、获取选中行
					var row=$("#dg").datagrid("getSelected");
					if(row){
						$('#cc2').combobox({
							url:'${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_queryDepartment',
							queryParams:{'departmentSn':rows2[0].auditedDepartment.departmentSn},
							valueField:'departmentSn',    
							textField:'departmentName',
							panelHeight:130,
							width:120,
							editable:false,
							required:true,
							prompt:'单位名称',
							//validType:["remote['${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_isExists?isUpdate=yes&&auditSn="+auditSn+"&&indexSn="+indexSn+"&&id="+id+"','departmentName']"],
							//invalidMessage:'该单位已经进行打分，无需重复打分！',
						});
						$('#dd2').dialog('open');
						$('#cc2').combobox('setValue',row.departmentSn);
						$('#conformDegree').combobox('setValue',row.conformDegree);
					}else{
						$.messager.show({
							title:'提示',
							msg:'请选择一条记录！',
							timeout:1000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+100,
								bottom:''
							}
						});
					}
					
				}
			}],
			columns:[[
					{field:'department',title:'单位名称',align:'center'},
					{field:'standardIndex',title:'标准编号',align:'center'},
					{field:'conformDegree',title:'符合度（得分率）',align:'center'},
					{field:'inputDateTime',title:'打分时间',align:'center'},
					{field:'editor',title:'打分人',align:'center'}
				]]
		});
		
		//cc3
		$('#cc3').combobox({
			valueField: 'label',
			textField: 'value',
			panelHeight:'auto',
			editable:false,
			onLoadSuccess:function(){
				$('#cc3').combobox('select','P')
			},
			onSelect:function(record){
				var indexSn=rows1.indexSn+"."+record.value;
				$('#dg').datagrid('options').url='${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_query';
				$('#dg').datagrid('load',{
					'auditSn':rows2[0].auditSn,
					'departmentSn':rows2[0].auditedDepartment.departmentSn,
					'indexSn':indexSn
				})
			},
			data: [{
				label: 'P',
				value: 'P'
			},{
				label: 'D',
				value: 'D'
			},{
				label: 'C',
				value: 'C'
			},
			{
				label: 'A',
				value: 'A'
			}]
		})
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
		$('#cc1').combobox({
			url:'${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_queryDepartment',
			queryParams:{'departmentSn':rows2[0].auditedDepartment.departmentSn},
			valueField:'departmentSn',    
			textField:'departmentName',
			panelHeight:130,
			width:120,
			editable:false,
			required:true,
			prompt:'单位名称'
			//validType:["remote['${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_isExists?auditSn="+rows2[0].auditSn+"&&indexSn="+rows1.indexSn+"','departmentName']"],
			//invalidMessage:'该单位已经进行打分，无需重复打分！',
		});
		$('#dd').dialog({    
		    title: '添加',    
		    width: 300,    
		    height: 100,    
		    closed: true,    
		    cache: false,  
		    modal: true
		});
		//触发重置事件
		$("#reset").click(function(){
			$("#ff").form("reset");
		});
		//添加	
		$("#submit").click(function(){
			if($('#ff').form('validate')){
				$('#ff').form('submit', {    
				    url:'${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_save',    
				    onSubmit: function(param){    
				        param.auditSn = rows2[0].auditSn;    
				        param.indexSn = rows1.indexSn+"."+$('#cc3').combobox('getValue');
				        param.parentDepartmentSn=rows2[0].auditedDepartment.departmentSn;    
				    },    
				    success:function(data){    
				    	var data = eval('(' + data + ')');  
				        if (data.status=="ok"){    
				        	$.messager.alert('我的消息','添加成功！','info');   
				        }else{
				        	$.messager.alert('我的消息','添加失败！','error');   
					    }
				        $('#dd').dialog('close');
				        $("#ff").form("reset"); 
				        $('#dg').datagrid('reload');    
				    }    
				});  
								
			}
		});
		//更新
		$('#dd2').dialog({    
		    title: '更新',    
		    width: 300,    
		    height: 100,    
		    closed: true,    
		    cache: false,  
		    modal: true  
		});
		//触发重置事件
		$("#reset2").click(function(){
			$("#ff2").form("reset");
		});
		//更新
		$("#submit2").click(function(){
			if($('#ff2').form('validate')){
				$('#ff2').form('submit', {    
				    url:'${pageContext.request.contextPath}/system/audit/systemScoreDetailsAction_update',    
				    onSubmit: function(param){ 
				    	param.id=id;   
				        param.auditSn = rows2[0].auditSn;    
				        param.indexSn = rows1.indexSn+"."+$('#cc3').combobox('getValue');
				        param.parentDepartmentSn=rows2[0].auditedDepartment.departmentSn;    
				    },    
				    success:function(data){    
				    	var data = eval('(' + data + ')');  
				        if (data.status=="ok"){    
				        	$.messager.alert('我的消息','修改成功！','info');   
				        }else{
				        	$.messager.alert('我的消息','修改失败！','error');   
					    }
				        $('#dd2').dialog('close');
				        $("#ff2").form("reset");
				        $('#dg').datagrid('reload');    
				    }    
				});  
								
			}
		});
	})
</script>
</head>
<body style="margin: 1px;">
	<input id="type" type="hidden" value="1"/>
	<table id="dg" ></table>
	<div id="dd" style="padding: 1px;">
		<form id="ff" method="post">
			<table>
				<tr>
					<td>
					<input id="cc1" name="departmentSn" class="easyui-combobox">
					</td>
					<td>
						<input class="easyui-combobox" name="conformDegree"     
        					data-options="required:true,
        					prompt:'符合度',
        					width:80,
							valueField: 'label',
							textField: 'value',
							panelHeight:'130',
							editable:false,
        					data: [{
								label: '100',
								value: '100%'
							},{
								label: '90',
								value: '90%'
							},{
								label: '80',
								value: '80%'
							},{
								label: '70',
								value: '70%'
							},{
								label: '60',
								value: '60%'
							},{
								label: '50',
								value: '50%'
							},{
								label: '40',
								value: '40%'
							},{
								label: '30',
								value: '30%'
							},{
								label: '20',
								value: '20%'
							},{
								label: '10',
								value: '10%'
							},{
								label: '0',
								value: '0%'
							}] ">
					</td>
				</tr>
				<tr style="text-align:center;padding-top:10px">
					<td colspan="2">
						<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>  
						<a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>  
					</td>
				</tr>
			</table>	
		</form>
	</div>
	
	<!-- gx -->
	<div id="dd2" style="padding: 1px;">
		<form id="ff2" method="post">
			<table>
				<tr>
					<td>
					<input id="cc2" name="departmentSn" class="easyui-combobox">
					</td>
					<td>
						<input id="conformDegree" class="easyui-combobox" name="conformDegree"     
        					data-options="required:true,
        					prompt:'符合度',
        					width:80,
							valueField: 'label',
							textField: 'value',
							panelHeight:'130',
							editable:false,
        					data: [{
								label: '100',
								value: '100%'
							},{
								label: '90',
								value: '90%'
							},{
								label: '80',
								value: '80%'
							},{
								label: '70',
								value: '70%'
							},{
								label: '60',
								value: '60%'
							},{
								label: '50',
								value: '50%'
							},{
								label: '40',
								value: '40%'
							},{
								label: '30',
								value: '30%'
							},{
								label: '20',
								value: '20%'
							},{
								label: '10',
								value: '10%'
							},{
								label: '0',
								value: '0%'
							}] ">
					</td>
				</tr>
				<tr style="text-align:center;padding-top:10px">
					<td colspan="2">
						<a id="submit2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>  
						<a id="reset2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>  
					</td>
				</tr>
			</table>	
		</form>
	</div>
</body>
</html>