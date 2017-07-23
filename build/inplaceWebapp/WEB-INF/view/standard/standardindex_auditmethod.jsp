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
		var row=parent.$("#standard").treegrid("getSelected");
		//获取所有父节点
		var parentrow=parent.$("#standard").treegrid("getParent",row.id);
		var list=[];
		while(parentrow!=null){
			var text=parentrow.indexSn;
			if(text.split("-").length>1){
				text=text.split("-")[1];
			}
			list.push({indexSn: parentrow.indexSn,text:text});
			parentrow=parent.$("#standard").treegrid("getParent",parentrow.id);
		}
		var standardSn=parent.$('#cc2').combobox('getValue');
		var roles="${sessionScope['roles']}";
		$("#s-a").datagrid({
		url:'${pageContext.request.contextPath}/standard/auditmethodAction_query',//要改为一个action
		idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
		queryParams:{'id':row.id},//请求远程数据发送额外的参数
		fitColumns:true,/*列宽自动*/
		striped:true,/*斑马线效果  */
		nowrap:false,/*数据同一行*/
		loadmsg:'请等待',
		rownumbers:true,/* 行号*/
		remoteSort:false,/*禁止使用远程排序*/
		singleSelect:true,
		checkOnSelect:false,
		/*按钮*/
		toolbar:[{
			id:'add',
			iconCls:'icon-add',
			text:'添加',
			handler:function(){
				if(roles.indexOf('jtxtgly')==-1){
					$('#add,#delete').css('display','none');
				}else{
					$('#dd').dialog('open');
				}
			}
		},{
			id:'delete',
			iconCls:'icon-remove',
			text:'删除',
			handler:function(){
				if(roles.indexOf('jtxtgly')==-1){
					$('#add,#delete').css('display','none');
				}else{
					//1、获取选中行
					var rows=$("#s-a").datagrid("getSelected");
					//2、对事件进行判断，和删除确认
					if(rows.length==0){
						$.messager.show({
							title:'提示',
							msg:'请选择一条记录！',
							timeout:2000,
							showType:'slide'
						});
					}else{
						$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
							if(r){
								//4、发送请求提交删除信息
		                        $.post("${pageContext.request.contextPath}/standard/auditmethodAction_delete",{id:rows.id},function(data){
		                        		var result = eval('(' + data + ')');
		                        		if(result=="ok"){
		                        			$.messager.alert("提示信息","删除成功！");
		                        			//删除页面，刷新
											$("#s-a").datagrid("clearChecked");
											$("#s-a").datagrid("reload");
											//刷新主表
											var url='standard/standardindexAction_queryPart.action';
							 	        	//var url="";
							 	        	parent.$("#standard").treegrid("options").url = url;
							 	        	parent.$('#standard').treegrid('reload',{
							 	        		standardSn:standardSn        		
							 	        	});

				                       	}else{
				                       		$.messager.alert("提示信息","删除失败！",'error');
					                    }
										
								},"text");
							}
						});
					}
				}				
			}			
		}],
		/*列*/
		columns:[[
			{field:'auditMethodContent',title:'审核方法',width:'50%',align:'center',formatter:function(value,row,index){
				return "<span title=" + value + ">" + value + "</span>";
			}},
			{field:'indexDeductedSn',title:'所扣上级指标',width:'20%',align:'center'},
			{field:'deduction',title:'所扣上级指标分数',width:'25%',align:'center'}
			]]		
		});
		$('#dd').dialog({    
		    title: '审核方法添加',    
		    width: 330,    
		    height: 200,    
		    closed: true,    
		    cache: false,  
		    modal: true   
		});
		$('#cc1').combobox({     
		    valueField:'indexSn',    
		    textField:'text',
		    panelHeight:100,
		    prompt:'非关键指标此项不选',
		    data:list
		});
		//触发重置事件
		$("#reset").click(function(){
			$("#ff").form("reset");
		});
		//提交
		$("#ff").form("disableValidation");
		$('#submit').click(function(){
			$('#ff').form("enableValidation");
			//验证通过返回true
			if($("#ff").form("validate")){
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/standard/auditmethodAction_save',
					queryParams:{'id':row.id},        
				    success:function(data){    
				    	var result = eval('(' + data + ')');
						if(result=="ok"){
							$.messager.alert("提示信息","添加成功！");
							$("#ff").form("reset");
							$('#dd').dialog("close");
							$('#s-a').datagrid('reload');
							var url='standard/standardindexAction_queryPart.action';
							parent.$("#standard").treegrid("options").url = url;
							parent.$("#standard").treegrid("reload",{
								standardSn:standardSn
							}); 
						}else{
							$.messager.alert("提示信息","添加失败！",'error');
						}
						$("#ff").form("disableValidation");
				    } 
				});
			}
		});
		//第一层权限
		if(roles.indexOf('jtxtgly')==-1){
			$('#add,#delete').css('display','none');
		}
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		
		if(row.isKeyIndex!=true){
    		$('#cc1').combobox({disabled:true});
    		$('#cc2').numberbox({disabled:true}); 
    	}
	})

</script>
</head>
<body style="margin: 1px;">
	<!--头 -->
	<table id="s-a"></table>
	<div id="dd">
		<form id="ff" method="post">
			<table>
				<tr>
					<td colspan="2">
					<input name="auditMethodContent" class="easyui-textbox" style="width:300px"
						data-options="multiline:true,height:44,prompt:'在此输入内容',required:true">
					</td>
				</tr>
				<tr>
					<td>
					<label for="indexDeductedSn">所扣上级指标:</label>
					</td>
					<td>
					<input id="cc1" name="indexDeductedSn" class="easyui-combobox" style="width:200px">
					</td>
				</tr>
				<tr>
					<td>
					<label for="deduction">所扣上级分数:</label>
					</td>
					<td>
					<input id="cc2" name="deduction" class="easyui-numberbox" style="width:200px"
						data-options="prompt:'非关键指标此项不填',min:0,precision:2">
					</td>
				</tr>
				<tr style="margin-top:20px;text-align:center;">
					<td colspan="2">
						<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
						<a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>  
					</td>
				</tr>
			</table>
			
			
		</form>
	</div>
</body>
</html>