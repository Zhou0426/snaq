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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<script type="text/javascript">
	function redistribution(index){
		$('#dg').datagrid('clearSelections');	
		$('#dd').dialog('open');
		$('#dg').datagrid('selectRow',index);
		var row=$('#dg').datagrid('getSelected');
		$('#personName').textbox({
			value:'',
			editable:false
		});
		$('#personId').textbox({
			value:'',
			editable:false
		});
		$("#department").combotree({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment',
			required:true,
			editable:false,
			queryParams:{'departmentSn':row.checkedDepartment.departmentSn},
			panelHeight:250,
			onSelect:function(node){
				if(node.personName!=null && node.personName.length>0){
					$('#personName').textbox({
						value:node.personName,
						editable:false
					});
					$('#personId').textbox({
						value:node.personId,
						editable:false
					});
				}else{
					$('#personName').textbox({
						value:'',
						editable:false
					});
					$('#personId').textbox({
						value:'',
						editable:false
					});
				}
				
			}
		});
	}
	$(function(){
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'redistribution':"${sessionScope['personId']}"},//请求远程数据发送额外的参数
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'inconformityItemSn',title:'编号',hidden:true},
				{field:'checkDateTime',title:'检查时间',width:'5%',align:'center'},
				{field:'checkLocation',title:'检查地点',width:'5%',align:'center'},			
				{field:'inconformityItemNature',title:'不符合项性质',width:'5%',align:'center'},
				{field:'machine.manageObjectSn',title:'机',width:'5%',align:'center',formatter:function(value,row,index){
					if(row.machine.isnull==false){
						return row.machine.manageObjectName;
					}
				}},
				{field:'checkedDepartment.departmentName',title:'被检部门',width:'5%',align:'center',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'problemDescription',title:'问题描述',width:'18%',align:'center'},
				{field:'deductPoints',title:'扣分',width:'3%',align:'center'},
				{field:'inconformityLevel',title:'不符合项等级',width:'5%',align:'center'},
				{field:'correctType',title:'整改类型',width:'5%',align:'center'},
				{field:'correctDeadline',title:'整改期限',width:'5%',align:'center'},				
				{field:'correctProposal',title:'整改建议',width:'17%',align:'center'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'5%',align:'center',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
					
				}},
				{field:'hasReviewed',title:'已复查',width:'5%',align:'center',formatter:function(value,row,index){
					if(row.hasReviewed==false){
						return '否';
					}else if(row.hasReviewed==true){
						return '是';
					}
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'5%',align:'center',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
				{field:'redistribution',title:'再分配',width:'5%',align:'center',formatter:function(value,row,index){
					return "<a name='btn' href='#' onclick='redistribution("+index+")'></a>";
				}}
				]],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
					//指标
					var standardIndex="无";
					if(row.standardIndex.isnull==false){
						standardIndex=row.standardIndex.indexName;
	        		}
	        		//危险源
	        		var hazard="无"
					if(row.hazard.isnull==false){
						hazard=row.hazard.hazardDescription;
					}
					//扣分项
					var methodcount="无"
					if(row.auditMethod.num>0){
						var method=row.auditMethod.method.split(',');
						var count=row.auditMethod.count.split(',');
						methodcount="";
						for(var i=0;i<row.auditMethod.num;i++){
							methodcount+=(i+1)+"."+method[i]+"("+count[i]+")&nbsp;";
						}
					}
					//专业
					var speciality="无"
					if(row.speciality.isnull==false){
						speciality=row.speciality.specialityName;
	        		}
	        		//检查人员
	        		var persons="无";
					if(row.checkers.num>0){
						persons=row.checkers.personNames;
					}
					//整改负责人
					var person="无";
					if(row.correctPrincipal.isnull==false){
						person=row.correctPrincipal.personName;
	        		}
					//录入人
	        		var editor="无";
	        		if(row.editor.isnull==false){
	        			editor=row.editor.personName;
		        	}
				return '<table style="border:1"><tr>' + 								
				'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + standardIndex + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'危险源：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + hazard + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'扣分项：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + methodcount + '</p>' + 
				'</td>' + 
				'<td style="width:50px;text-align:center">'+'专业：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + speciality + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'检查人：' + '</td>' + 
				'<td style="width:150px;">' + 
				'<p>' + persons + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'整改负责人：' + '</td>' + 
				'<td style="width:50px;">' + 
				'<p>' + person + '</p>' + 
				'</td>' + 
				'<td style="width:50px;text-align:center">'+'录入人：' + '</td>' + 
				'<td style="width:50px;">' + 
				'<p>' + editor + '</p>' + 
				'</td>' +
				'</tr></table>'; 
				},
				onLoadSuccess:function(){
					$('[name=btn]').linkbutton({    
					    iconCls: 'icon_user_edit'   
					});  
				}		
		});
		$('#dd').dialog({    
		    title: '隐患再分配',    
		    width: 300,    
		    height: 150,    
		    closed: true,    
		    cache: false,  
		    modal: true,
		    onClose:function(){
				$('#dg').datagrid('reload');
			}  
		});
		//整改确认整改	
		$("#submit").click(function(){      
	    	var row=$('#dg').datagrid('getSelected');
	    	$('#ff').form("submit",{    
	    	    url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_redistribution.action',
	    	    queryParams:{'id':row.id},   
	    	    success:function(data){    
	    	    	var result = eval('(' + data + ')');
	    	    	if(result.status=="ok"){
	    	    		$.messager.alert("提示信息","分配成功！");
	    	    		$("#ff").form("reset");
	    	    		$('#dd').dialog("close");
	    	    	}else{
	    	    		$.messager.alert("提示信息","分配失败！",'error');
		    	    }   
	    	    }    
	    	}); 
		})

		
	})
</script>
</head>
<body style="padding: 1px;"> 
	<!--头 -->  
   	<table id="dg"></table>
	<div id="dd" >
		<form id="ff" method="post" style="margin-top:5px;">
			<div style="display:none"><input id="personId" name="personId" class="easyui-textbox"/></div>
			<table>
				<tr>
					<td>被分配部门</td>
					<td>
						<input id="department" name="departmentSn" class="easyui-combobox" style="width:200px">
					</td>
				</tr>
				<tr>
					<td>
						<label for="personName">部门负责人：</label>
					</td>
					<td>
						<input id="personName" class="easyui-textbox" required style="width:200px">  
					</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan="2">
						<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确定</a>
					</td>
				</tr>
			</table>	
		</form>
	
	</div>
</body>
</html>