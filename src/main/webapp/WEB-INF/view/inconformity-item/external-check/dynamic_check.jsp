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
			$('#win').window({
				width:400,
				height:300,
				title:'相关附件',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/dynamic_check_attachment"+ frameborder="0" width="100%" height="100%"/>'
			});
	}
	function attachment_unsafeAct(index){
		$('#dg2').datagrid('clearSelections');//清处多选的行
			$('#win2').window({
				width:400,
				height:300,
				title:'相关附件',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/dynamic_check_attachment_unsafeAct" frameborder="0" width="100%" height="100%"/>'
			});
	}
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
	}
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var loginId="${sessionScope.personId}";
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{checkType:'动态检查',checkerFrom:'外部'},//请求远程数据发送额外的参数
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:false,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			onSelect:function(index,row){
				if(resources.indexOf('01070101')==-1){
					$('#unsafecondition-add').css('display','none');
				}
				if(resources.indexOf('01070102')==-1){
					$('#unsafecondition-delete').css('display','none');
				}else{
					if(loginId==row.editor.personId || (row.checkers.num>0 && $.inArray('${sessionScope.pId}', row.checkers.personIds.split(','))!=-1) || roles.indexOf('jtxtgly')!=-1){
						$('#unsafecondition-delete').css('display','');
					}else{
						$('#unsafecondition-delete').css('display','none');
					}
				}
				if(resources.indexOf('01070103')==-1){
					$('#unsafecondition-update').css('display','none');
				}else{
					if(loginId==row.editor.personId || (row.checkers.num>0 && $.inArray('${sessionScope.pId}', row.checkers.personIds.split(','))!=-1) || roles.indexOf('jtxtgly')!=-1){
						$('#unsafecondition-update').css('display','');
					}else{
						$('#unsafecondition-update').css('display','none');
					}
				}
			},
			/*按钮*/
			toolbar:[{
				id:'unsafecondition-add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					//判断是否有权限
					if(resources.indexOf('01070101')==-1){
						$('#unsafecondition-add').css('display','none');
					}else{
						$('#win').window({
			 				width:730,
			 				height:430,
			 				title:'添加不符合项-隐患',
			 				cache:false,
			 				content:'<iframe src="${pageContext.request.contextPath}/external/check/dynamic_check_add" frameborder="0" width="100%" height="100%"/>'
			 			});
					}
				}
			},{
				id:'unsafecondition-delete',
				iconCls:'icon-cancel',
				text:'删除',
				handler:function(){
					if(resources.indexOf('01070102')==-1){
						$('#unsafecondition-delete').css('display','none');
					}else{
						//1、获取选中行
						var rows=$("#dg").datagrid("getSelections");
						//2、对事件进行判断，和删除确认
						if(rows.length==0){
							$.messager.show({
								title:'提示',
								msg:'至少选择为一条记录！',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}else{
							//判断是否为录入人或jtxtgly或检查人
							if(loginId==rows[0].editor.personId || (rows[0].checkers.num>0 && $.inArray('${sessionScope.pId}', rows[0].checkers.personIds.split(','))!=-1) || roles.indexOf('jtxtgly')!=-1){
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
											if(status=="ok"){
												$.messager.alert("提示信息",msg);
											}else{
												$.messager.alert("提示信息",msg,'error');
											}
											//删除页面，刷新
											$("#dg").datagrid("clearChecked");
											$("#dg").datagrid("reload");
											toDoThing();
										},"text");
									}
								})
							}else{
								$.messager.alert('我的消息','对不起！您没有删除此记录的权限！','warning');
							}
						}
					}
				}				
			},{
				id:'unsafecondition-update',
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					if(resources.indexOf('01070103')==-1){
						$('#unsafecondition-update').css('display','none');
					}else{
						var rows=$("#dg").datagrid("getSelections");
						if(rows.length==1){
							//判断是否有权限
							if(loginId==rows[0].editor.personId || (rows[0].checkers.num>0 && $.inArray('${sessionScope.pId}', rows[0].checkers.personIds.split(','))!=-1) || roles.indexOf('jtxtgly')!=-1){
								$('#win').window({
									width:730,
									height:430,
									title:"修改记录",
									cache:false,
									content:'<iframe src="${pageContext.request.contextPath}/external/check/dynamic_check_update" frameborder="0" width="100%" height="100%"/>'								
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有修改此记录的权限！','warning');
							}
						}else{
							$.messager.show({
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
				}
			}
			],
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
				{field:'correctDeadline',title:'整改期限',width:'5%',styler: function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						if(value!=null && value.length>0){
		  					var date=new Date(value);
		  					if(date<new Date()){
		  						return 'background-color:#ff5544';
		  					}
						}
					}
	  			}},				
				{field:'correctProposal',title:'整改建议',width:'20%'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
				}},
				{field:'hasReviewed',title:'已复查',width:'5%',formatter:function(value,row,index){
					if(row.hasReviewed==false){
						return '否';
					}else if(row.hasReviewed==true){
						return '是';
					}
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
				{field:'attachment',title:'相关附件',width:'5%',formatter:function(value,row,index){
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
				'<td style="width:50px;text-align:center">'+'录入时间：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.editorDateTime + '</p>' + 
				'</td>' +
				'</tr></table>'; 
				}
		});	
		//隐患第一层权限
		if(resources.indexOf('01070101')==-1){
			$('#unsafecondition-add').css('display','none');
		}
		if(resources.indexOf('01070102')==-1){
			$('#unsafecondition-delete').css('display','none');
		}
		if(resources.indexOf('01070103')==-1){
			$('#unsafecondition-update').css('display','none');
		}
		$('#dg2').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_query.action',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			//title:"动态检查",
			queryParams:{checkTypeSn:'0',checkerFromSn:'5'},//请求远程数据发送额外的参数
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
			pageList:[5,10,15,20,25,30],
			onSelect:function(index,row){
				if(resources.indexOf('01070105')==-1){
					$('#unsafeact-add').css('display','none');
				}
				if(loginId==row.editorId || roles.indexOf('jtxtgly')!=-1){
					if(resources.indexOf('01070106')==-1){
						$('#unsafeact-remove').css('display','none');
					}else{
						$('#unsafeact-remove').css('display','');
					}
					if(resources.indexOf('01070107')==-1){
						$('#unsafeact-edit').css('display','none');
					}else{
						$('#unsafeact-edit').css('display','');
					}
				}else{
					$('#unsafeact-remove').css('display','none');
					$('#unsafeact-edit').css('display','none');
				}
			},
			/*按钮*/
			toolbar:[{
				id:'unsafeact-add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					if(resources.indexOf('01070105')==-1){
						$('#unsafeact-add').css('display','none');
					}else{
					 	$('#win2').window({
					 		width:740,
					 		height:430,
					 		title:'不符合项-不安全行为',
					 		cache:false,
					 		content:'<iframe src="${pageContext.request.contextPath}/external/check/dynamic_check_add_unsafeAct" frameborder="0" width="100%" height="100%"/>'
					 	});
					}
				}
			},{
				id:'unsafeact-remove',
				iconCls:'icon-cancel',
				text:'删除',
				handler:function(){
					if(resources.indexOf('01070106')==-1){
						$('#unsafeact-remove').css('display','none');
					}else{
						//1、获取选中行
						var rows=$("#dg2").datagrid("getSelections");
						//2、对事件进行判断，和删除确认
						if(rows.length==0){
							$.messager.show({
								title:'提示',
								msg:'至少选择为一条记录！',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}else{
							if(loginId==rows[0].editorId || roles.indexOf('jtxtgly')!=-1){
								$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
									if(r){
										var ids="";
										for(var i=0;i<rows.length;i++){
											ids += rows[i].id+",";
										}
										ids=ids.substring(0,ids.length-1);//截取字符串，除去最后一个逗号
										//alert(ids.substring(0,ids.length-1));//只是试验一下
										//4、发送请求提交删除信息
				                        $.post("${pageContext.request.contextPath}/inconformity/item/unsafeActAction_deleteUnsafeAct.action",{ids:ids},function(result){
				                        	if(result=="success"){
				                        		$.messager.show({
													title:'提示',
													msg:'成功删除信息！',
													timeout:2000,
													showType:'slide'
												});	
				                        	}else{
												$.messager.alert("提示信息","删除出错，请重新操作！",'error');
				                        	}
												//删除页面，刷新
												$("#dg2").datagrid("clearChecked");
												$("#dg2").datagrid("reload");
										},"json");
									}
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有删除此记录的权限！','warning');
							}
						}
					}
				}
				
			},{
				id:'unsafeact-edit',
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					if(resources.indexOf('01070107')==-1){
						$('#unsafeact-edit').css('display','none');
					}else{
						var rows=$("#dg2").datagrid("getSelections");
						if(rows.length==1){
							if(loginId==rows[0].editorId || roles.indexOf('jtxtgly')!=-1){
								$('#win2').window({
									width:740,
									height:430,
									title:"修改记录",
									cache:false,
									content:'<iframe src="${pageContext.request.contextPath}/shenhua/check/dynamic_check_update_unsafeAct" frameborder="0" width="100%" height="100%"/>'
										
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有修改此记录的权限！','warning');
							}
						}else{
							$.messager.show({
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
				}
			}],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'implDepartmentName',width:80,title:'贯标单位'},
				{field:'checkedDepartment',width:80,title:'被检部门'},
				{field:'violator',width:60,title:'不安全行为人员'},
				{field:'actDescription',width:200,title:'行为描述'},
				{field:'checkers',width:70,title:'检查成员'},
				{field:'checkDateTime',width:50,title:'检查时间'},
				{field:'checkLocation',width:70,title:'检查地点'},
				{field:'editor',width:50,title:'录入人'},
				{field:'editorId',width:100,hidden:true},
				{field:'checkersId',width:100,hidden:true},
				{field:'checkType',width:100,title:'检查类型',hidden:true},
				{field:'checkerFrom',width:100,title:'检查人来自',hidden:true},
				{field:'systemAudit',width:100,title:'所属的审核',hidden:true},
				{field:'inconformityItemNature',width:100,title:'不符合项性质',hidden:true},
				{field:'inconformityLevel',width:100,title:'不符合项等级',hidden:true},
				{field:'inconformityItemSn',width:100,title:'不符合项编号',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  inconformityItemSnValue[index]=value;
						  }else{
							  inconformityItemSnValue[index]="无";
						  }
					  }},
				  {field:'speciality',title:'所属专业',width:120,align:'center',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  specialityValue[index]=value;
						  }else{
							  specialityValue[index]="无";
						  }
					  }},
				  {field:'unsafeActMark',title:'不安全行为痕迹',width:120,align:'center',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  unsafeActMarkValue[index]=value;
						  }else{
							  unsafeActMarkValue[index]="无";
						  }
					  }},
				  {field:'unsafeActStandard',title:'不安全行为标准',width:120,align:'center',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  unsafeActStandardValue[index]=value;
						  }else{
							  unsafeActStandardValue[index]="无";
						  }
					  }},
				  {field:'unsafeActLevel',title:'不安全行为等级',width:120,align:'center',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  unsafeActLevelValue[index]=value;
						  }else{
							  unsafeActLevelValue[index]="无";
						  }
					  }},
				{field:'attachment',width:50,title:'相关附件',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment_unsafeAct("+index+")' style='text-decoration:none'>" + "附件"+"["+value+"]" + "</a>";
				}}
				]],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
				return '<table style="border:1"><tr>' + 								
				'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
				'<td style="width:140px;text-align:center">' + 
				'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
				'<td style="width:140px;text-align:center">' + 
				'<p>' + unsafeActLevelValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' + 
				
				'<td style="width:100px;text-align:center">'+'专业：' + '</td>' + 
				'<td style="width:140px;text-align:center" >' + 
				'<p>' + specialityValue[rowIndex] + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'痕迹：' + '</td>' + 
				'<td style="width:140px;text-align:center">' + 
				'<p>' + unsafeActMarkValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' +
				
				'<td style="width:100px;text-align:center">'+'标准：' + '</td>' + 
				'<td style="width:380px;text-align:center" colspan="3">' + 
				'<p>' + unsafeActStandardValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr></table>'; 
				}			
		});	
		//第一层权限
		if(resources.indexOf('01070105')==-1){
			$('#unsafeact-add').css('display','none');
		}
		if(resources.indexOf('01070106')==-1){
			$('#unsafeact-remove').css('display','none');
		}
		if(resources.indexOf('01070107')==-1){
			$('#unsafeact-edit').css('display','none');
		}
		$('#unsafecondition-add').linkbutton({plain:false});
		$('#unsafecondition-delete').linkbutton({plain:false});
		$('#unsafecondition-update').linkbutton({plain:false});
		$('#unsafeact-add').linkbutton({plain:false});
		$('#unsafeact-remove').linkbutton({plain:false});
		$('#unsafeact-edit').linkbutton({plain:false});
	})
</script>
</head>
<body style="margin: 1px;">
	<!--头 -->
	<div id="tt" class="easyui-tabs" data-options="fit:true">   
    <div title="隐患"  style="padding:1px;display:none;">   
        <table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
    	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>   
    </div>   
    <div title="不安全行为" style="padding:1px;display:none;"> 
    	<table id="dg2"></table>
		<div id="win2" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
    </div>  
</div>  
	
	
</body>
</html>