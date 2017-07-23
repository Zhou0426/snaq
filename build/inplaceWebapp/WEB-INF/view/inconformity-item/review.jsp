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
	function edit(index){
		$('#dg').datagrid('clearSelections');	
		$('#dd').dialog('open');
		$('#dg').datagrid('selectRow',index);
		var row=$('#dg').datagrid('getSelected');
		//复查信息查询
		$("#d-c").datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/reviewAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'id':row.id},//请求远程数据发送额外的参数
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:false,
			toolbar:[{
				id:'add',
				text:'添加复查确认信息',
				iconCls:'icon-add',
				handler:function(){
					$('#dd2').dialog('open');
					}
				}],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:'true'},
				{field:'reviewer',title:'复查人',width:80,formatter:function(value,row,index){
					if(row.reviewer!=null){
						return row.reviewer.personName;
					}
				}},
				{field:'inputTime',title:'录入时间',width:90},
				{field:'reviewTime',title:'复查时间'},
				{field:'reviewInfo',title:'整改确认信息'},
				{field:'hasCorrectFinished',title:'整改完成',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else{
						return '是';
					}
				}}				
			]]
		});	
		$('#add').linkbutton({plain:false});
	};
	function toDoThing(){
		$.ajax({
			   type: "POST",
			   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
			   success: function(msg){
				   var rec=eval('(' + msg + ')');
				   //console.log(parent.$('#mm21').attr('id'));
				   parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
				   parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
				   parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
				   parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
				   parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
			   }
		});
	};
	$(function(){
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			title:"整改复查",
			queryParams:{'reviewId':"${sessionScope['personId']}"},//请求远程数据发送额外的参数
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
				{field:'checkDateTime',title:'检查时间',width:'5%'},
				{field:'checkLocation',title:'检查地点',width:'5%'},			
				{field:'inconformityItemNature',title:'不符合项性质',width:'5%'},
				{field:'machine.manageObjectSn',title:'机',width:'5%',formatter:function(value,row,index){
					if(row.machine.isnull==false){
						return row.machine.manageObjectName;
					}
				}},
				{field:'checkedDepartment.departmentName',title:'被检部门',width:'5%',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'problemDescription',title:'问题描述',width:'20%'},
				{field:'deductPoints',title:'扣分',width:'3%'},
				{field:'inconformityLevel',title:'不符合项等级',width:'5%'},
				{field:'correctType',title:'整改类型',width:'5%'},
				{field:'correctDeadline',title:'整改期限',width:'5%'},				
				{field:'correctProposal',title:'整改建议',width:'20%'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
				}},
				{field:'confirmTime',title:'整改确认时间',width:'5%'},
				{field:'hasReviewed',title:'已复查',formatter:function(value,row,index){
					if(value==false){
						return "<div style='width:70px;'>否&nbsp;"+"<a name='btn' href='#' onclick='edit("+index+")'></a></div>";
					}else if(value==true){
						return "是";
					}else{
						return "";
					}
					
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
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
			return '<table style="border:1"><tr>' + 
			'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
			'<td style="width:220px;text-align:center">' + 
			'<p>' + standardIndex + '</p>' + 
			'</td>' +
			'<td style="width:50px;text-align:center">'+'危险源：' + '</td>' + 
			'<td style="width:220px;text-align:center">' + 
			'<p>' + hazard + '</p>' + 
			'</td>' +
			'<td style="width:50px;text-align:center">'+'扣分项：' + '</td>' + 
			'<td style="width:220px;text-align:center">' + 
			'<p>' + methodcount + '</p>' + 
			'</td>' + 
			'<td style="width:50px;text-align:center">'+'专业：' + '</td>' + 
			'<td style="width:100px;text-align:center">' + 
			'<p>' + speciality + '</p>' + 
			'</td>' +
			'<td style="width:50px;text-align:center">'+'检查人员：' + '</td>' + 
			'<td style="width:150px;text-align:center">' + 
			'<p>' + persons + '</p>' + 
			'</td>' +
			'<td style="width:50px;text-align:center">'+'整改负责人：' + '</td>' + 
			'<td style="width:50px;text-align:center">' + 
			'<p>' + person + '</p>' + 
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
		    title: '复查确认',    
		    width: 650,    
		    height: 300,    
		    closed: true,    
		    cache: false,  
		    modal: true,
		    onClose:function(){
				$('#dg').datagrid('reload');
			}  
		});
		$('#dd2').dialog({    
		    title: '复查确认',    
		    width: 410,    
		    height: 230,    
		    closed: true,    
		    cache: false,  
		    modal: true   
		});
		//复查人
		$('#cc1').combogrid({
    		idField:'personId',    
   	 		textField:'personName',
   	 		value:'',
    		url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getPerson.action', 
    		delay: 500,    
		    mode:'remote',
		    prompt:'搜索复查人员',
		    queryParams:{'departmentSn':"${sessionScope['departmentSn']}"},
		    required:true,
    		columns:[[    
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'personId',title:'人员编号'},    
				{field:'personName',title:'人员姓名'},    
				{field:'gender',title:'性别'},
    		]]    
		});
		//触发重置事件
		$("#reset").click(function(){
			$("#ff").form("reset");
		});
		//整改确认整改	
		$("#submit").click(function(){
			$.messager.confirm('确认','一经提交，无法再次更改！您确认想要提交吗？',function(r){    
			    if (r){    
			    	var row=$('#dg').datagrid('getSelected');
			    	$('#ff').form("submit",{    
			    	    url:'${pageContext.request.contextPath}/inconformity/item/reviewAction_save',
			    	    queryParams:{'id':row.id},   
			    	    success:function(data){    
			    	    	var result = eval('(' + data + ')');
			    	    	if(result=="ok"){
			    	    		$.messager.alert("提示信息","添加成功！");
			    	    		$("#ff").form("reset");
			    	    		$('#dd2').dialog("close");
			    	    		$('#d-c').datagrid('reload');
			    	    		toDoThing();
			    	    		
			    	    	}else{
			    	    		$.messager.alert("提示信息","添加失败！",'error');
				    	    }   
			    	    }    
			    	}); 
			    }    
			});
		})
	})
</script>
</head>
<body style="margin: 1px;">
	<!--头 -->  
   	<table id="dg"></table>
	<div id="dd" style="padding: 1px;">
		<table id="d-c"></table>
	</div>
	<div id="dd2">
		<form id="ff" method="post">
			<table>
				<tr>
					<td colspan="2">
					<input name="reviewInfo" class="easyui-textbox"
						data-options="multiline:true,height:88,width:380,prompt:'在此输入复查信息，可不填！'">
					</td>
				</tr>
				<tr>
					<td>
						<input class="easyui-datetimebox" name="reviewTime"     
        					data-options="required:true,editable:false,showSeconds:false,prompt:'复查时间'">
					</td>
					<td>
					<input id="cc1" name="personId" style="width: 200px;"/>
					</td>
				</tr>
				<tr>
					<td>是否整改完成：				
						<input type="radio" name="hasCorrectFinished" value=true checked>是
						<input type="radio" name="hasCorrectFinished" value=false>否
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
</body>
</html>