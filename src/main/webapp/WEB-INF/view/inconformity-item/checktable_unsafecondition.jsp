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

	var inconformityItemSnValue=new Array();
	var specialityValue=new Array();
	var unsafeActMarkValue=new Array();
	var unsafeActStandardValue=new Array();
	var unsafeActLevelValue=new Array();
	
	function attachment(index){
		$('#dg').datagrid('clearSelections');//清处多选的行
			parent.$('#child-win').window({
				width:400,
				height:300,
				title:'相关附件',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/inconformity_attachment"+ frameborder="0" width="100%" height="100%"/>'
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
	};
	$(function(){
		var periodicalCheckSn="";
		var specialCheckSn="";
		var auditSn="";
		//检查表详情所选行
		var parentrow=parent.$('#dg').datagrid('getSelections');
		//判断是从哪个页面打开
		var type=parent.parent.$('#details').val();
		if(type=="my"){
			//从我的检查表打开
			var row=parent.parent.$("#dg").datagrid("getSelected");
			if(row.checkType=="定期检查"){
				periodicalCheckSn=row.periodicalCheckSn;
			}else if(row.checkType=="专项检查"){
				specialCheckSn=row.specialCheckSn;
			}else{
				auditSn=row.auditSn;
			}
		}else{
			var type1=parent.parent.$('#type').val();
			if(type1=="periodical"){
				var row=parent.parent.$('#dg').datagrid('getSelections');
				periodicalCheckSn=row[0].periodicalCheckSn;
			}else if(type1=="季度审核" || type1=="单位审核"){
				var row=parent.parent.$('#dg').datagrid('getChecked');
				auditSn=row[0].auditSn;
			}else{
				var row=parent.parent.$('#dg').datagrid('getSelections');
				specialCheckSn=row[0].specialCheckSn;
			}
		}		
		//var periodicalCheckSn=row.periodicalCheckSn;
		$("#d-i").datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_query',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'periodicalCheckSn':periodicalCheckSn,'specialCheckSn':specialCheckSn,'auditSn':auditSn,'indexSn':parentrow[0].indexSn},//请求远程数据发送额外的参数
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
			pageSize:10,
			pageList:[10,15,20,25,30],
			toolbar:[{
				id:'add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					parent.$('#child-win').window({
		 				width:730,
		 				height:410,
		 				title:'添加不符合项-隐患',
		 				cache:false,
		 				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/my_unsafecondition_add" frameborder="0" width="100%" height="100%"/>'
		 			});
				}
			},{
				id:'delete',
				iconCls:'icon-remove',
				text:'删除',
				handler:function(){
					var rows=$('#d-i').datagrid("getSelections");
					if(rows.length==0){
						parent.$.messager.show({
							title:'提示',
							msg:'请选择一条记录！',
							timeout:1000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
								bottom:''
							}
						});
					}else{
						//var inconformityItemSn=rows[0].inconformityItemSn;
						$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
							if(r){
								//3、获取要删除的id值 1,2,3===> String ids  delete from category where id in (1,2,3)
								var ids="";
								for(var i=0;i<rows.length;i++){
									ids += rows[i].id+",";
								}
								ids=ids.substring(0,ids.length-1);//截取字符串，除去最后一个逗号
								//alert(ids.substring(0,ids.length-1));//只是试验一下
								//4、发送请求提交删除信息
		                        $.post("${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_deleteByIds.action",{ids:ids},function(result){
		                        	var data= eval('(' + result + ')');
									var status=data.status;
									var msg=data.message;
									if(result=="fail"){
										parents.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
											if(r){
												//window.open("denglu","_top")
											}
										});
										
									}else{
										if(status=="ok"){
											$.messager.alert("提示信息",msg);
										}else{
											$.messager.alert("提示信息",msg,'error');
										}
										//删除页面，刷新
										$("#d-i").datagrid("clearSelections");
										$("#d-i").datagrid("reload");
										parent.$('#dg').datagrid('reload');
										parent.parent.$('#dg').datagrid('reload');
										toDoThing();
									}
								},"text");
							}
						});;
					}
				}
			},{
				id:'update',
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					var rows2=$("#d-i").datagrid("getSelections");
					if(rows2.length==1){
						//发送ajax请求判断是否有权限
						$.post("",null,function(result){
							if(result=="fail"){
								parent.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
									if(r){
										//window.open("","_top");
									}
								});
							}else{
									parent.$('#child-win').window({
									width:730,
									height:410,
									title:"修改记录",
									cache:false,
									content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/my_unsafecondition_update" frameborder="0" width="100%" height="100%"/>'
								
								});
							}
						},"text");
					}else{
						parent.$.messager.show({
							title:'错误提示',
							msg:'一次只能更新一条记录！',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
								bottom:''
							}
						});
					}
				}
			}],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkDateTime',title:'检查时间',width:'8%'},
				{field:'checkLocation',title:'检查地点',width:'7%'},			
				{field:'inconformityItemNature',title:'不符合项性质',width:'9%'},
				{field:'machine.manageObjectSn',title:'机',width:'7%',formatter:function(value,row,index){
					if(row.machine.isnull==false){
						return row.machine.manageObjectName;
					}
				}},
				{field:'checkedDepartment.departmentName',title:'被检部门',width:'7%',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'problemDescription',title:'问题描述',width:'30%'},
				{field:'deductPoints',title:'扣分',width:'5%'},
				{field:'inconformityLevel',title:'不符合项等级',width:'9%'},
				{field:'correctType',title:'整改类型',width:'7%'},
				{field:'correctDeadline',title:'整改期限',width:'8%',styler: function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						if(value!=null && value.length>0){
		  					var date=new Date(value);
		  					if(date<new Date()){
		  						return 'background-color:#ff5544';
		  					}
						}
					}
	  			}},				
				{field:'correctProposal',title:'整改建议',width:'30%'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'7%',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
				}},
				{field:'hasReviewed',title:'已复查',width:'7%',formatter:function(value,row,index){
					if(row.hasReviewed==false){
						return '否';
					}else if(row.hasReviewed==true){
						return '是';
					}
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'7%',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
				{field:'attachment',title:'相关附件',width:'7%',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment("+index+")' style='text-decoration:none'>" + "附件"+"["+value+"]" + "</a>";
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
				} 
		});
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
	})
	
</script>
</head>
<body style="margin: 1px">	
     <table id="d-i"></table>      
</body>
</html>