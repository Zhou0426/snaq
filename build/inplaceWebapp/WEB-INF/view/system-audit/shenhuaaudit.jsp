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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-cellediting.js"></script>
<script type="text/javascript">
	function inconformityItem(index){
		$('#dg').datagrid('clearChecked');
		$('#dg').datagrid('checkRow',index);
		$('#win').window({
			width:900,
			height:400,
			title:'不符合项',
			modal: true,
			minimizable:false,
			maximizable:true,
			collapsible:false,
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/system/audit/audit_inconformityItem"+ frameborder="0" width="100%" height="100%"/>'
		});
	}
	function attachment(index){
		$('#dg').datagrid('clearChecked');
		$('#dg').datagrid('checkRow',index);
		$('#win').window({
			width:500,
			height:300,
			title:'附件',
			modal: true,
			minimizable:false,
			maximizable:true,
			collapsible:false,
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/system/audit/audit_attachment" frameborder="0" width="100%" height="100%"/>'
		});
	}
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/system/audit/systemauditAction_query',
			idField:'id',
			//title:'神华审核',
			queryParams:{'year':new Date().getUTCFullYear(),type:2},
			fit:true,
			stript:true,
			nowrap:false,
			fitColumns:true,/*列宽自动*/
			loadMsg:'请等待',
			rownumbers:true,
			romoteSort:true,
			singleSelect:true,
			checkOnSelect:false,
			clickToEdit:false,
			dblclickToEdit:true,
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,20,50],
			onBeforeCellEdit:function(index,field){
				$('#dg').datagrid('checkRow',index);
				var rows=$('#dg').datagrid('getChecked');
				if(resources.indexOf("080306")!=-1 && (roles.indexOf("jtxtgly")!=-1 || rows[0].editor.personId=="${sessionScope['personId']}")){
					return true;
				}else{
					$.messager.alert('我的消息','对不起！您没有编辑此记录的权限！','warning');
					return false;
				}
			},
			onEndEdit:function(index, row, changes){
				$.ajax({
		             type: "POST",
		             url: "${pageContext.request.contextPath}/system/audit/systemauditAction_lineedit",
		             data: {id:row.id,remark:row.remark, amendScore:row.amendScore},
		             dataType: "json",
		             success: function(data){
		                  if(data.status=="nook"){
		                	  $.messager.alert('我的消息','保存失败！','error');
			              }else if(data.status=="over"){
			            	  $.messager.alert('我的消息','保存失败,总分已超过100分！','error');
				          }
		                  $('#dg').datagrid('reload');       
		             }
		         });
			},
			onCheck:function(index, row){
				//添加
				if(resources.indexOf("080301")==-1){
					$('#add-button').css('display','none');
				}else{
					$('#add-button').css('display','');
				}
				//删除
				if(resources.indexOf("080302")==-1){
					$('#delete').css('display','none');
				}else{
					if(roles.indexOf("jtxtgly")!=-1 || row.editor.personId=="${sessionScope['personId']}"){
						$('#delete').css('display','');
					}else{
						$('#delete').css('display','none');
					}
				}
				//修改
				if(resources.indexOf("080303")==-1){
					$('#update').css('display','none');
				}else{
					if(roles.indexOf("jtxtgly")!=-1 || row.editor.personId=="${sessionScope['personId']}"){
						$('#update').css('display','');
					}else{
						$('#update').css('display','none');
					}
				}
				//进入审核打分
				if(resources.indexOf("080304")==-1){
					$('#score').css('display','none');
				}else{
					if(roles.indexOf("jtxtgly")!=-1 || row.editor.personId=="${sessionScope['personId']}"){
						$('#score').css('display','');
					}else{
						$('#score').css('display','none');
					}
				}
				//输出审核打分表
				if(resources.indexOf("080305")==-1){
					$('#export').css('display','none');
				}else{
					$('#export').css('display','');
				}
			},
			/*按钮*/
			toolbar:[{
				text:'<input id="cc1"/>'
			},{
				text:'<input id="cc3"/>'
			},{
				text:'<input id="cc4"/>'
			},{
				id:'clear',
				text:'清除选项',
				handler:function(){
					$('#cc1').combobox('clear');
					$('#cc3').combobox('clear');
					$('#cc4').combobox('clear');
					var url = "${pageContext.request.contextPath}/standard/standardAction_getStandard";
			        $('#cc4').combobox('reload', url);
					$('#dg').datagrid('load',{
						year:null,
						standardSn:null,
						departmentTypeSn:null,
						type:2
					});
				}
			},{
				id:'add-button',
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					if(resources.indexOf("080301")==-1){
						$('#add-button').css('display','none');
					}else{
						var value2=$('#cc3').combobox('getValue');
						var value=$('#cc4').combobox('getValue');
						if(value!="" && value2!=""){
							//添加
							$('#add').dialog({    
						    title: '添加审核信息',    
						    width: 350,    
						    height: 320,     
						    cache: false,       
						    modal: true,
						    resizable:true,
						    content:'<iframe src="${pageContext.request.contextPath}/system/audit/shenhuaaudit_add" frameborder="0" width="100%" height="100%"/>'  
							});  
						}else{
							$.messager.show({
								title:'提示消息',
								msg:'为保证插入信息的完整性，请在上方下拉框中选择单位类型和评分标准！',
								timeout:1000,
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
			},{
				id:'delete',
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					if(resources.indexOf("080302")==-1){
						$('#delete').css('display','none');
					}else{
						var rows=$('#dg').datagrid('getChecked');
						if(rows.length==0){
							// 消息将显示在顶部中间
							$.messager.show({
								title:'提示',
								msg:'至少选择一条记录！',
								showType:'show',
								timeout:1000,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
													
						}else{
							//是否为集团管理员或录入人
							if(roles.indexOf("jtxtgly")!=-1 || rows[0].editor.personId=="${sessionScope['personId']}"){
								$.messager.confirm('确认对话框', '您想要删除当前选中的记录吗？', function(r){
									if (r){
										var ids="";
										for(var i=0;i<rows.length;i++){
											ids += rows[i].id+",";
										}
										ids=ids.substring(0,ids.length-1);
										$.post("${pageContext.request.contextPath}/system/audit/systemauditAction_delete",{ids:ids},function(result){
				                        	var data= eval('(' + result + ')');
											var status=data.status;
											if(status=="ok"){
												$.messager.alert("提示信息",'删除成功！');
											}else{
												$.messager.alert("提示信息",'删除失败！','error');
											}
											//删除页面，刷新
											$("#dg").datagrid("clearChecked");
											$("#dg").datagrid("reload");
										},"text");
									}
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有删除此记录的权限！','warning');
							}												
						}
					}
				}
			},{
				id:'update',
				text:'修改',
				iconCls:'icon-edit',
				handler:function(){
					if(resources.indexOf("080303")==-1){
						$('#delete').css('display','none');
					}else{
						var rows=$("#dg").datagrid('getChecked');
						if(rows.length==1){
							if(roles.indexOf("jtxtgly")!=-1 || rows[0].editor.personId=="${sessionScope['personId']}"){
								$('#add').dialog({    
								    title: '添加审核信息',    
								    width: 350,    
								    height: 320,     
								    cache: false,       
								    modal: true,
								    resizable:true,
								    content:'<iframe src="${pageContext.request.contextPath}/system/audit/shenhuaaudit_update" frameborder="0" width="100%" height="100%"/>'  
									});
							}else{
								$.messager.alert('我的消息','对不起！您没有修改此记录的权限！','warning');
							}
						}else{
							$.messager.show({
								title:'错误提示',
								msg:'请先选择一条记录！',
								showType:'slide',
								timeout:500,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
					}
					
				}
			},{
				id:'score',
				text:'进入打分',
				iconCls:'icon-marking',
				handler:function(){
					if(resources.indexOf("080304")==-1){
						$('#score').css('display','none');
					}else{
						var rows=$("#dg").datagrid('getChecked');
						if(rows.length==1){
							if(roles.indexOf("jtxtgly")!=-1 || rows[0].editor.personId=="${sessionScope['personId']}"){
								$('#add').dialog({    
								    title: '审核打分',    
								    width:900,    
								    height:400,     
								    cache: false,       
								    modal: true,
								    resizable:true,
								    maximizable:true,
								    draggable:true,
								    onClose:function(){
										$.ajax({
								            type: "POST",
								            url: "${pageContext.request.contextPath}/system/audit/systemauditAction_score.action",
								            data: {auditSn:rows[0].auditSn,standardSn:rows[0].standardSn,id:rows[0].id},
								            dataType: "json",
								            success: function(data){
									            if(data.status=="nook"){
									            	$.messager.alert('我的消息','打分遇到未知错误！','error');
										        }else if(data.status=="over"){
										        	$.messager.alert('我的消息','总分超过100分，系统自动将修正分变为0！','error');
											    }
									            $('#dg').datagrid('reload');           
								            }
								        });
									},
								    content:'<iframe src="${pageContext.request.contextPath}/system/audit/audit_standardindex" frameborder="0" width="100%" height="100%"/>'  
									});
							}else{
								$.messager.alert('我的消息','对不起！您没有对所选记录进行打分的权限！','warning');
							}							
						}else{
							$.messager.show({
								title:'提示',
								msg:'请勾选一条记录！',
								showType:'slide',
								timeout:500,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
					}
				}
			},{
				id:'export',
				text:'输出打分表',
				iconCls:'icon-excel',
				handler:function(){
					if(resources.indexOf("080305")==-1){
						$('#export').css('display','none');
					}else{
						var rows=$('#dg').datagrid('getChecked');
						if(rows.length==1){
							if(rows[0].finalScore!=null){
								var form=$("<form>");
								form.attr("style","display:none");
								form.attr("target","");
								form.attr("method","post");
								form.attr("action","${pageContext.request.contextPath}/system/audit/systemauditAction_exportS");

								var input1=$("<input>");
								input1.attr("type","hidden");
								input1.attr("name","standardSn");
								input1.attr("value",rows[0].standardSn);
								
								
								var input2=$("<input>");
								input2.attr("type","hidden");
								input2.attr("name","auditSn");
								input2.attr("value",rows[0].auditSn);
								//将表单放入body
								$("body").append(form);
								form.append(input1);
								form.append(input2);
								form.submit();//提交表单
							}else{
								$.messager.show({
									title:'提示',
									msg:'请先进行审核评分！',
									showType:'slide',
									timeout:500,
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop+200,
										bottom:''
									}
								});
							}
							
						}else{
							$.messager.show({
								title:'提示',
								msg:'请选择一条记录！',
								showType:'slide',
								timeout:500,
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
							
					}	
				}
			},{
				id:'exportSummary',
				text:'各矿打分表',
				iconCls:'icon-excel',
				handler:function(){
					var form=$("<form>");
					form.attr("style","display:none");
					form.attr("target","");
					form.attr("method","post");
					form.attr("action","${pageContext.request.contextPath}/system/audit/systemauditAction_exportSummary");

					var input1=$("<input>");
					input1.attr("type","hidden");
					input1.attr("name","standardSn");
					input1.attr("value",$('#cc4').combobox('getValue'));								
					
					var input2=$("<input>");
					input2.attr("type","hidden");
					input2.attr("name","year");
					input2.attr("value",$('#cc1').combobox('getValue'));					
					
					var input4=$("<input>");
					input4.attr("type","hidden");
					input4.attr("name","departmentTypeSn");
					input4.attr("value",$('#cc3').combobox('getValue'));
					
					var input5=$("<input>");
					input5.attr("type","hidden");
					input5.attr("name","type");
					input5.attr("value",2);
					//将表单放入body
					$("body").append(form);
					form.append(input1);
					form.append(input2);
					form.append(input4);
					form.append(input5);
					form.submit();//提交表单				
				}
			}],
			onBeforeLoad:function(){
				$(this).datagrid('unselectAll');
			},
			/*冻结列,适合列比较多的情况*/
		  	frozenColumns:[[
		        {field:'xyz',width:50,checkbox:true}
		  	]],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'auditedDepartment.departmentName',title:'被审核单位',width:'10%',formatter:function(value,row,index){
					if(row.auditedDepartment.isnull==false){
						return row.auditedDepartment.departmentName;
					}
				}},
				{field:'strAuditTeamLeader',title:'审核组长',width:'8%'},
				{field:'strCheckers',title:'审核成员',width:'20%'},
				{field:'startDate',width:'8%',title:'审核开始日期'},
				{field:'endDate',width:'8%',title:'审核结束日期'},
				{field:'inconformityItemes',width:'8%',title:'不符合项',formatter:function(value,row,index){
					if(resources.indexOf("080307")!=-1){
						return "<a href='#' onclick='inconformityItem("+index+")' style='text-decoration:none'>" + "不符合项"+"["+value+"]" + "</a>";
					}else{
						return "无权访问"+"["+value+"]";
					}
				}},
				{field:'computedScore',title:'系统计算得分',width:'10%',sortable:true},
				{field:'amendScore',title:'修正分(双击可编辑)',width:'10%',editor:{type:'numberbox',options:{precision:2,max:100,min:-100}}},
				{field:'finalScore',title:'最终得分',width:'5%',sortable:true},
				{field:'remark',title:'备注(双击可编辑)',width:'10%',editor:'textbox',width:200},
				{field:'editor.personName',title:'录入人',width:'8%',formatter:function(value,row,index){
					if(row.editor.isnull==false){
						return row.editor.personName;
					}
				}},
				{field:'attachment',title:'附件',width:'8%',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment("+index+")' style='text-decoration:none;'>附件["+value+"]</a>"
				}}
			]]
		}).datagrid('enableCellEditing')
		var year="";
		var standardSn="";
		var departmentTypeSn="";
		//年份
		$("#cc1").combobox({   
			valueField:'year',    
			textField:'yeartext',  
			panelHeight:'auto',
			editable:false,
			prompt:'选择年份',
			width:100,
			onSelect:function(record){
				standardSn=$('#cc4').combobox('getValue');
				departmentTypeSn=$('#cc3').combobox('getValue');
				$('#dg').datagrid('load',{
					year:record.year,
					standardSn:standardSn,
					departmentTypeSn:departmentTypeSn,
					type:2
				});							
			}
		});
		var data = [];//创建年度数组
		var thisYear=new Date().getUTCFullYear();//今年
		for(var i=thisYear;i>2014;i--){
			data.push({"year":i,"yeartext":i+"年"});
		}
		$("#cc1").combobox("loadData", data);//下拉框加载数据
		$("#cc1").combobox("setValue",thisYear);//设置默认值为今年
		//单位类型
		$('#cc3').combobox({
			url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			editable:false,
			width:100,
			prompt:'单位类型',
			onSelect:function(record){
				$('#cc4').combobox("clear");
		        var url = "${pageContext.request.contextPath}/standard/standardAction_getStandard?departmentTypeSn="+record.departmentTypeSn;
		        $('#cc4').combobox('reload', url);
				year=$('#cc1').combobox('getValue');
				$('#dg').datagrid('load',{
					year:year,
					departmentTypeSn:record.departmentTypeSn,
					type:2
				}); 
			}						
		});		
		//评分标准
		$('#cc4').combobox({
			url:'${pageContext.request.contextPath}/standard/standardAction_getStandard',
			valueField:'standardSn',
			textField:'standardName',
			editable:false,
			prompt:'评分标准',
			panelHeight:'auto',
			queryParams:{'standardType':'审核指南'},
			width:200,
			onSelect:function(record){
				year=$('#cc1').combobox('getValue');
				$('#dg').datagrid('load',{
					year:year,
					standardSn:record.standardSn,
					type:2
				}); 
			}						
		});	
		//第一层权限限制
		//添加
		if(resources.indexOf("080301")==-1){
			$('#add-button').css('display','none');
		}
		//删除
		if(resources.indexOf("080302")==-1){
			$('#delete').css('display','none');
		}
		//修改
		if(resources.indexOf("080303")==-1){
			$('#update').css('display','none');
		}
		//进入审核打分
		if(resources.indexOf("080304")==-1){
			$('#score').css('display','none');
		}
		//输出审核打分表
		if(resources.indexOf("080305")==-1){
			$('#export').css('display','none');
		}
		$('#add-button').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
		$('#score').linkbutton({plain:false});
		$('#export').linkbutton({plain:false});
		$('#clear').linkbutton({plain:false});
		$('#exportSummary').linkbutton({plain:false});
	})	
</script>
</head>
<body  style="margin: 1px;">
	<input type="hidden" id="type" value="神华审核"/>
	<table id="dg"></table>
	<div id="add"></div>
	<div id="win"></div>
	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win1" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win2" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>