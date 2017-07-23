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
	function edit(){
		parent.$('#child-win').dialog({
			width:800,
			height:400,
			title:'生成检查表',
			cache:false,
			maximizable:true,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/edittable" frameborder="0" width="100%" height="100%"/>'
		});
	}
	function detail(){
		parent.$('#child-win').dialog({
			width:800,
			height:400,
			title:'检查表',
			cache:false,
			maximizable:true,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/checktabledetail" frameborder="0" width="100%" height="100%"/>'
		});		
	}
	function toDoThing(){
		$.ajax({
			   type: "POST",
			   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
			   success: function(msg){
				   var rec=eval('(' + msg + ')');
				   //console.log(parent.parent.$('#mm21').attr('id'));
				   parent.parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
				   parent.parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
				   parent.parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
				   parent.parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
				   parent.parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
			   }
		});
	}
	$(function(){
		var pvlist=new Array();
		var type=parent.$('#type').val();
		//var rows=parent.$('#dg').datagrid('getSelections');
		var specialCheckSn="";
		var periodicalCheckSn="";
		var auditSn="";
		if(type=="periodical"){
			var rows=parent.$('#dg').datagrid('getSelections');
			periodicalCheckSn=rows[0].periodicalCheckSn;
			//检查人员
			if(rows[0].checkers.num>0){
				var personIds=rows[0].checkers.personIds2.split(',');
				var personNames=rows[0].checkers.personNames.split(',');
				for(var i=0;i<rows[0].checkers.num;i++){
					pvlist.push({personId:personIds[i],personName:personNames[i]});
				}
			}
		}else if(type=="季度审核" || type=="单位审核"){
			var rows=parent.$('#dg').datagrid('getChecked');
			auditSn=rows[0].auditSn;
			//检查人员
			if(rows[0].auditors.num>0){
				var personIds=rows[0].auditors.personIds.split(',');
				var personNames=rows[0].auditors.personNames.split(',');
				for(var i=0;i<rows[0].auditors.num;i++){
					pvlist.push({personId:personIds[i],personName:personNames[i]});
				}
			}
		}else{
			var rows=parent.$('#dg').datagrid('getSelections');
			specialCheckSn=rows[0].specialCheckSn;
			//检查人员
			if(rows[0].checkers.num>0){
				var personIds=rows[0].checkers.personIds2.split(',');
				var personNames=rows[0].checkers.personNames.split(',');
				for(var i=0;i<rows[0].checkers.num;i++){
					pvlist.push({personId:personIds[i],personName:personNames[i]});
				}
			}
		}
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_queryJoinCheck',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'periodicalCheckSn':periodicalCheckSn,'specialCheckSn':specialCheckSn,'auditSn':auditSn},//请求远程数据发送额外的参数
			fitColumns:true,/*列宽自动*/
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			//fit:true,
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			/*按钮*/
			toolbar:[{
				id:'add',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					$('#dd2').css('display','none');
					$('#dd').css('display','');	
				}
			},{
				id:'delete',
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					$('#dd2').css('display','none');
					$('#dd').css('display','none');
					var row=$('#dg').datagrid('getSelected');
					if(row){
						parent.$.messager.confirm('确认对话框', '您想要删除选中的数据吗？', function(r){
							if (r){
								$.ajax({
						            type: "POST",
						            url: "${pageContext.request.contextPath}/inconformity/item/checkTableAction_delete",
						            data: {id:row.id},
						            dataType: "json",
						            success: function(data){
						                	if(data.status=="ok"){
						                		parent.$.messager.alert('我的消息','删除成功！','info');
							                }else{
							                	parent.$.messager.alert('我的消息','删除失败！','error');
								            }
						                	$('#dg').datagrid('clearSelections');
											toDoThing();
						                	$('#dg').datagrid('reload');
										    parent.$('#dg').datagrid('reload');           
						            }
						        });
							}
						});
					}else{
						parent.$.messager.show({
							title:'我的消息',
							msg:'请先选择一条记录！',
							showType:'show',
							timeout:500,
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
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
					var row=$('#dg').datagrid('getSelected');
					$('#dd').css('display','none');
					if(row){
						$('#cc2').combobox('setValues',row.checkers.personIds);
						$('#dd2').css('display','');						
					}else{
						parent.$.messager.show({
							title:'我的消息',
							msg:'请先选择一条记录！',
							showType:'show',
							timeout:500,
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
								bottom:''
							}
						});
					}
				}
			}],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkTableSn',title:'检查表编号'},
				{field:'checkers',title:'检查人',formatter:function(value,row,index){
					if(row.checkers.num>0){
						return row.checkers.personNames;
					}
				}},
				{field:'standardIndexs',title:'检查表详情',formatter:function(value,row,index){
					return '<a href="#" onclick="detail()" style="text-decoration:none">查看详情</a>';
				}},
				{field:'editTable',title:'编辑检查表',formatter:function(value,row,index){
					return '<a href="#" onclick="edit()" style="text-decoration:none">点我编辑</a>';
				}}
			]]
		});
		$('#cc1').combobox({
			panelHeight:'auto',
			valueField:'personId',
			textField:'personName',
			editable:false,
			multiple:true,
			required:true,
			width:260,
			prompt:'添加时选中一行原有数据即可复制其检查表',
		});
		$('#cc2').combobox({
			panelHeight:'auto',
			valueField:'personId',
			textField:'personName',
			editable:false,
			multiple:true,
			required:true,
			width:260
		});
		$('#cc1').combobox('loadData',pvlist);
		$('#cc2').combobox('loadData',pvlist);		
		//添加提交
		$('#submit').click(function(){
			if($('#ff').form('validate')){
				var row=$('#dg').datagrid('getSelected');
				var id=0;
				if(row!=null){
					id=row.id;
				}
				$('#ff').form('submit', {    
				    url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_save', 
				    queryParams:{'id':id,'periodicalCheckSn':periodicalCheckSn,'specialCheckSn':specialCheckSn,'auditSn':auditSn},      
				    success:function(data){
				    	var data = eval('(' + data + ')');
				    	if(data.status=="ok"){
							parent.$.messager.alert('我的消息','添加成功！','info');
							$('#cc1').combobox('clear');
							$('#dd').css('display','none');
					    }else{
					    	parent.$.messager.alert('我的消息','添加失败！','error');
						}
						toDoThing();
					    $('#dg').datagrid('reload');
					    parent.$('#dg').datagrid('reload');
				    }    
				});
			}
		});
		//更新操作
		$('#submit2').click(function(){
			var row=$('#dg').datagrid('getSelected');
			if($('#ff2').form('validate')){
				$('#ff2').form('submit', {    
				    url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_update', 
				    queryParams:{'id':row.id},      
				    success:function(data){
				    	var data = eval('(' + data + ')');
				    	if(data.status=="ok"){
							parent.$.messager.alert('我的消息','修改成功！','info');
							$('#cc2').combobox('clear');
							$('#dd2').css('display','none');
					    }else{
					    	parent.$.messager.alert('我的消息','修改失败！','error');
						}
						toDoThing();
					    $('#dg').datagrid('reload');       
				    }    
				});
			}
		});
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
	})
</script>

</head>
<body style="margin: 1px;">
	<!--头 -->
	<table id="dg" style="margin: 1px;"></table>
	<div id="dd" style="margin-top:20px;display:none">
		<form id="ff" method="post">
			<table style="margin-left:50px">
				<tr>
					<td><input id="cc1" name="personIds"></td>
					<td>
						<a href="#" id="submit" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dd2" style="margin-top:20px;display:none">
		<form id="ff2" method="post">
			<table style="margin-left:50px">
				<tr>
					<td><input id="cc2" name="personIds"></td>
					<td>
						<a href="#" id="submit2" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确认修改</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>