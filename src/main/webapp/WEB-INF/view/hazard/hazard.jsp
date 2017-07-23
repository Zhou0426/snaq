<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>状态类危险源</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css"> 
			.align-center{ 
				margin:10px 5px;	/* 居中 这个是必须的，，其它的属性非必须 */ 
 				width:250px; 		/*给个宽度 顶到浏览器的两边就看不出居中效果了 */ 
				text-align:left; 	/* 文字等内容居中 */ 
			} 
			.nextPage{
				word-wrap: break-word; 
				word-break: normal;
			}
		</style> 
		<script type="text/javascript">
			var a=1;
			var departmentTypeSn="";
			var hazardSn="";
			var manageObjectSn="";
			$(function(){
					$('#departmentType').combobox({
							url:'${pageContext.request.contextPath}/hazard/manageObjectAction_load.action',
							method:'post',
							required:true,
							panelWidth: 200,
							editable:false,
							panelHeight:'auto',
							width:200,
							onLoadSuccess:function(node,data){
								if(a==1){
									$("#departmentType").combobox('setValue',node[0].id);
									searchDatagrid();
						            a++;
								}
							},
							onSelect: function(rec){
									searchDatagrid();
					        }
						});
					$('#parentHazard').textbox({
						width:250,
						buttonText:'搜索',    
					    iconCls:'icon-man', 
					    iconAlign:'left',
					    prompt:'请输入管理对象名称或编号查找',
					    onChange:function(newValue,oldValue){
						    searchDatagrid();
					    }
					});
					$('#hazardSn').textbox({
						width:250,
						buttonText:'搜索',
					    prompt:'请输入危险源名称或编号查找',
					    onChange:function(newValue,oldValue){
					    	searchDatagrid();
						 }
					});
						    
// 					    	$.post("hazard/hazardAction_show.action",{departmentTypeSn:$('#departmentTypeSn').combobox('getValue'),
// 								manageObjectSn:manageObjectSn},function(result){
// 										$('#dg').datagrid({
// 											queryParams: {
// 												name: 'easyui',
// 												subject: 'datagrid'
// 											}
// 										});
// 										$('#dg').datagrid("reload");
// 									},"text");
// 							url:'${pageContext.request.contextPath}/json/main_unsafe.json',
// 							method:'post',
// 							panelWidth: 200,
// 							panelHeight:300,
// 							width:200,
// 							onLoadSuccess:function(node,data){
// 								if(a==1){
// 									var da=$('#parentHazard').combotree('tree').tree('getRoot');
// 									if(da!=null){
// 										$("#parentHazard").combotree('setValue',da);
// 										//console.log(da.id); 
// 										var url = 'hazard/hazardAction_show.action?manageObjectSn='+da.id;
// 										$('#dg').datagrid('reload', url);
// 								        a++;
// 										}else{
// 											var url = 'hazard/hazardAction_show.action';
// 											$('#dg').datagrid('reload', url);
// 											}
// 									}
// 							},
// 							onSelect: function(rec){
// 					            var url = 'hazard/hazardAction_show.action?manageObjectSn='+rec.id;
// 					            $('#dg').datagrid('reload', url);    
// 					        }
					$('#dg').datagrid({
						//url: 'hazard/hazardAction_showHazard.action',
						idField: 'id',
			            title:'危险源',
			            toolbar:'#weixianyuan',
			            rownumbers: true,	//显示一个行号列
			            fitColumns:true,	//自动适应列
			           	fit:true,			//表格宽高自适应
			            nowrap:false,
			            striped:true,		//斑马线效果
						singleSelect:true,	//单行选中
			            loadmsg:'请等待...',	//加载等待时显示
			            pagination:true,	//显示分页组件
			            pageNumber:1,
			            pageSize:10,
			            pageList:[5,10,15,20,25,30],
						columns:[[
								  {field:'id',hidden:true},
						          {field:'departmentTypeSn',hidden:true},
						          {field:'departmentTypeName',title:'所属部门类型',width:70,align:'center'},
						          {field:'hazardSn',title:'危险源编号',width:100,align:'center'},    
						          {field:'hazardDescription',title:'危险源描述',width:300,align:'center'},    
						          {field:'resultDescription',title:'危险源后果描述',width:300,align:'center'},    
						          {field:'accidentTypeName',title:'事故类型',width:70,align:'center'},    
						          {field:'accidentTypeSn',title:'事故类型',width:70,align:'center',hidden:true},
						          {field:'riskLevel',title:'风险等级',width:70,align:'center'},
						          {field:'manageObjectName',title:'管理对象名称',width:100,align:'center',
						        	  formatter: function(value, row, index) {
							        	  if(value!=null&&value.length>0){
							 		      		return "<a href='javascript:;' onclick=showHazard() style='text-decoration:none'>"+value+"</a>";
								          }else{
								        	  return "<a href='javascript:;' onclick=showHazard() style='text-decoration:none'>"+"无"+"</a>";
									      }
									  }
							 	  },
							 	 {field:'standardIndexNum',title:'对应指标',width:70,align:'center',
						        	  formatter: function(value, row, index) {
							 		      return "<a href='javascript:;' onclick=showStandardIndex() style='text-decoration:none'>指标["+value+"]</a>";
									  }
							 	  }
						     ]],
							 onDblClickCell: function(){
								$('#dg').datagrid("uncheckAll");
							}
					});
					
					
					//权限管理
					var str="${sessionScope['permissions']}";
					//添加按钮点击事件
					if(str.indexOf("100201")==-1){
						$("#add").css('display','None');
					}else{
						$('#add').bind('click',function(){
							var parVal=$('#departmentType').combobox('getValue');
							//判断部门类型是否为空
							if(parVal!=null&parVal!=""){
								$('#win').window({
									title:"添加信息",
									width:400,
									height:360,
									content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_add" frameborder="0" width="100%" height="100%" />'
								});
							}else{
									$.messager.show({
										title:'提示信息',
										msg:'请选择部门类型',
										timeout:2000,
										showType:'slide'
									});	
							}
						});
					}
					//删除按钮点击事件
					if(str.indexOf("100202")==-1){
						$("#remove").css('display','None');
					}else{
						$('#remove').bind('click',function(){
							 //判断是否有选中行记录
							var rows=$('#dg').datagrid("getSelections")
							if(rows.length==0){
								$.messager.show({
									title:'提示信息',
									msg:'请至少选中一行记录',
									timeout:2000,
									showType:'slide'
								});														
								}else{
									$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
										if (r){
											id=rows[0].id;
											$.post("${pageContext.request.contextPath}/hazard/hazardAction_delete.action",{id:id},function(result){
												if(result=="\"success\""){
														$.messager.show({
																title:'提示',
																msg:'成功删除记录！',
																timeout:2000,
																showType:'slide'
														});	
													$('#dg').datagrid("reload");
												}else{
													$.messager.alert('警告','删除失败，请检查后重新操作！');
												}
											},"text");
											$('#dg').datagrid("unselectAll");
										}
									});
								}
							});
					}
					//修改按钮事件
					if(str.indexOf("100203")==-1){
						$("#edit").css('display','None');
					}else{
						$('#edit').bind('click',function(){
							//判断是否选中
							var rows=$('#dg').datagrid("getSelections")
				 			if(rows.length!=1){
								$.messager.show({
									title:'提示信息',
									msg:'请选择一条记录',
									timeout:2000,
									showType:'slide'
								});														
							}else{
									$('#win').window({
										title:"修改信息",
										width:400,
										height:340,
										content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_update" frameborder="0" width="100%" height="100%" />'
									});
							}
						});
					}
					//导入按钮点击事件
					if(str.indexOf("100204")==-1){
						$("#import").css('display','None');
					}else{
						$('#import').bind('click', function(){    
							$('#templateDialog').dialog('open');
					    });
					}
					//导出按钮点击事件
					if(str.indexOf("100205")==-1){
						$("#export").css('display','None');
					}else{
						$('#export').bind('click', function(){    
							$('#exportHazard').dialog('open');  
					    });
						$('#exportHazard').dialog({
							title:"导出危险源",
							width:300,
							height:150,
							modal:true,
							closed:true,
							buttons:[{
								text:'导出全部',
								handler:function(){
									
									$('#sub').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcel.action',
											queryParams:{
													departmentTypeSn:$('#departmentType').combotree('getValue'),
													manageObjectSn:$('#parentHazard').textbox('getValue'),
													hazardList:$('#hazardSn').textbox('getValue'),
													id:"all"
											}
										});	
									
									$('#exportHazard').dialog('close');
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
									
								}
							},{
								text:'导出当前',
								handler:function(){
									$('#sub').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcel.action',
										queryParams:{
												departmentTypeSn:$('#departmentType').combotree('getValue'),
												manageObjectSn:$('#parentHazard').textbox('getValue'),
												hazardList:$('#hazardSn').textbox('getValue')
										}	
									});	
								
									$('#exportHazard').dialog('close'); 
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'关闭',
								handler:function(){$('#exportHazard').dialog('close'); }
							}]
						});
					}
					//导入危险源-管理对象关联
					if(str.indexOf("100208")==-1){
						$("#importRelation").css('display','None');
					}else{
						//导入 管理对象-危险源关联 按钮点击事件
						$('#importRelation').bind('click', function(){
							$('#templateDialogRelation').dialog('open');
					    });
					}
					//导出危险源-管理对象关联
					if(str.indexOf("100209")==-1){
						$("#exportRelation").css('display','None');
					}else{
					    //导出危险源  - 管理对象关联 按钮点击事件
					    $('#exportRelation').bind('click',function(){
					    	$('#exportHazardRelation').dialog('open'); 
					    });
						
					    $('#exportHazardRelation').dialog({
							title:"导出危险源管理对象关联",
							width:300,
							height:150,
							modal:true,
							closed:true,
							buttons:[{
								text:'导出全部关联',
								handler:function(){
									
									$('#subRelation').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcelRelation.action',
											queryParams:{
													departmentTypeSn:$('#departmentType').combotree('getValue'),
													manageObjectSn:$('#parentHazard').textbox('getValue'),
													hazardList:$('#hazardSn').textbox('getValue'),
													id:"all"
											}	
										});	
									
									$('#exportHazardRelation').dialog('close');
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'导出当前关联',
								handler:function(){
									$('#subRelation').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcelRelation.action',
										queryParams:{
												departmentTypeSn:$('#departmentType').combotree('getValue'),
												manageObjectSn:$('#parentHazard').textbox('getValue'),
												hazardList:$('#hazardSn').textbox('getValue')
										}	
									});	
								
									$('#exportHazardRelation').dialog('close');
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'关闭',
								handler:function(){$('#exportHazardRelation').dialog('close'); }
							}]
						});
					}
					//导出危险源-指标关联
					if(str.indexOf("100210")==-1){
						$("#importStandardIndex").css('display','None');
					}else{
					    //导入指标-危险源关联事件
					    $('#importStandardIndex').bind('click', function(){
							$('#templateDialogStandardIndex').dialog('open');
					    });
					}
					//导出危险源-指标关联
					if(str.indexOf("100211")==-1){
						$("#exportStandardIndex").css('display','None');
					}else{
					    //导出 指标-危险源关联 按钮点击事件
					    $('#exportStandardIndex').bind('click',function(){
					    	$('#exportHazardStandardIndex').dialog('open'); 
					    });
					    $('#exportHazardStandardIndex').dialog({
							title:"导出危险源-指标关联",
							width:300,
							height:150,
							modal:true,
							closed:true,
							buttons:[{
								text:'导出全部关联',
								handler:function(){
									
									$('#subStandardIndex').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcelStandardIndex.action',
											queryParams:{
													departmentTypeSn:$('#departmentType').combotree('getValue'),
													manageObjectSn:$('#parentHazard').textbox('getValue'),
													hazardList:$('#hazardSn').textbox('getValue'),
													id:"all"
											}	
										});	
									
									$('#exportHazardStandardIndex').dialog('close');
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'导出当前关联',
								handler:function(){
									$('#subStandardIndex').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/download_exportHazardExcelStandardIndex.action',
										queryParams:{
												departmentTypeSn:$('#departmentType').combotree('getValue'),
												manageObjectSn:$('#parentHazard').textbox('getValue'),
												hazardList:$('#hazardSn').textbox('getValue')
										}	
									});	
								
									$('#exportHazardStandardIndex').dialog('close');
									//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'关闭',
								handler:function(){$('#exportHazardStandardIndex').dialog('close'); }
							}]
						});
					}
					
					
			});
			//危险源模板下载
			function download_btn(){
				var url ='${pageContext.request.contextPath}/hazard/download_downloadHazard.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			}
			//管理对象-危险源关联模板下载
			function download_btnRelation(){
				var url ='${pageContext.request.contextPath}/hazard/download_downloadRelation.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			};
			//指标-危险源关联模板下载
			function download_btnStandardIndex(){
				var url ='${pageContext.request.contextPath}/hazard/download_downloadStandardIndex.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			};
			//ajax发送请求session--导入
			function getSession(){
				$.ajax({
					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
					success: function(data){
						//console.log(data);
						$('#p').progressbar('setValue',data);
					}
				},"json");
			};
			function getProValue(){
				var timer = setInterval(function(){
					$.ajax({
						url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findProValue.action',
						success: function(data){
							$('#p').progressbar('setValue',data);
							if(data == 100){
								$.ajax({
				 					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_clearSession.action',
				 				});
								$('#winPro').window('close');
		                        clearInterval(timer);
		                    }
						}
					},"json");
				},100);
			};
			//危险源导入
			function uploadExcel(){     
		         //得到上传文件的全路径  
		         var fileName= $('#uploadExcel').filebox('getValue');
		                 if(fileName==""){     
		                    $.messager.alert('提示','请选择上传文件！','info');   
		                 }else{  
		                     //对文件格式进行校验  
		                     var d1=/\.[^\.]+$/.exec(fileName);   
		                     if(d1==".xls"){
		                    	 	 //$('#daoru').css('display','none');
		                    	 	  //进度条显示
		                    	 	  $('#p').progressbar('setValue',0);
		                    	 	  $('#winPro').window('open');
									  var timer = setInterval(getSession,500);
									  
			                          //提交表单  
			                          $('#daoru').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/manageObjectAction_importHazardExcel.action',
											success:function(result){
												  $('#p').progressbar('setValue','100');
												  clearInterval(timer);
												  $('#winPro').window('close');
						                          $('#uploadExcel').filebox('setValue','');
						                          $('#templateDialog').dialog('close');
						                          $.messager.alert('提示',"<div class='align-center nextPage'>"+result.substring(1,result.length-1)+"</div>",'info',function(){
						                        	  searchDatagrid();
						                          });

						                          //对话框
// 						                          $('#dda').empty();
// 						                          $("#dd").append("<div id='dda' class='align-center nextPage'><a>" +result.substring(1,result.length-1)+ "</a></div>");
// 						                          $('#dd').dialog('open');
											}
									  });	
			                    }else{  
				                        $.messager.alert('提示','请选择xls格式文件！','info');   
				                        $('#uploadExcel').filebox('setValue','');   
			                    }  
		                 } 
		      };
			
		    	//危险源-管理对象关联导入
				function uploadExcelRelation(){
					 //得到上传文件的全路径  
			         var fileName= $('#uploadExcelRelation').filebox('getValue');
			                 if(fileName==""){     
			                    $.messager.alert('提示','请选择上传文件！','info');   
			                 }else{  
			                     //对文件格式进行校验  
			                     var d1=/\.[^\.]+$/.exec(fileName);   
			                     if(d1==".xls"){
			                    	  //进度条显示
			                    	  $('#p').progressbar('setValue',0);
		                    	 	  $('#winPro').window('open');
									  var timer = setInterval(getSession,500);
									  
			                          //提交表单  
			                          $('#daoruRelation').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/manageObjectAction_importExcelRelation.action',
											success:function(result){
												  $('#p').progressbar('setValue','100');
												  clearInterval(timer);
												  $('#winPro').window('close');
						                          $('#uploadExcelRelation').filebox('setValue','');
						                          $('#templateDialogRelation').dialog('close');
						                          $.messager.alert('提示',"<div class='align-center nextPage'>"+result.substring(1,result.length-1)+"</div>",'info',function(){
						                        	  searchDatagrid();
						                          });
											}
									  });
			                    }else{  
			                        $.messager.alert('提示','请选择xls格式文件！','info');   
			                        $('#uploadExcelRelation').filebox('setValue','');   
			                    }  
			                 } 
				};
				//危险源-指标导入
				function uploadExcelStandardIndex(){     
			         //得到上传文件的全路径  
			         var fileName= $('#uploadExcelStandardIndex').filebox('getValue');
			                 if(fileName==""){     
			                    $.messager.alert('提示','请选择上传文件！','info');   
			                 }else{  
			                     //对文件格式进行校验  
			                     var d1=/\.[^\.]+$/.exec(fileName);   
			                     if(d1==".xls"){
			                    	 	  //进度条显示
			                    	 	  $('#p').progressbar('setValue',0);
			                    	 	  $('#winPro').window('open');
										  var timer = setInterval(getSession,500);
										  
				                          //提交表单  
				                          $('#daoruStandardIndex').form('submit', {
												url:'${pageContext.request.contextPath}/hazard/hazardAction_standardHazard.action',
												success:function(result){
													  $('#p').progressbar('setValue','100');
													  clearInterval(timer);
													  $('#winPro').window('close');
							                          $('#uploadExcelStandardIndex').filebox('setValue','');
							                          $('#templateDialogStandardIndex').dialog('close');
							                          $.messager.alert('提示',"<div class='align-center nextPage'>"+result.substring(1,result.length-1)+"</div>",'info',function(){
							                        	  searchDatagrid();
							                          });

							                          //对话框
//	 						                          $('#dda').empty();
//	 						                          $("#dd").append("<div id='dda' class='align-center nextPage'><a>" +result.substring(1,result.length-1)+ "</a></div>");
//	 						                          $('#dd').dialog('open');
												}
										  });	
				                    }else{  
					                        $.messager.alert('提示','请选择xls格式文件！','info');   
					                        $('#uploadExcelStandardIndex').filebox('setValue','');   
				                    }  
			                 } 
			      };
			//管理对象详情
			function showHazard(){
		    	$('#win').window({
					title:"管理对象详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_showManageObject" frameborder="0" width="100%" height="100%" />'
				});
			};
			//指标详情
			function showStandardIndex(){
		    	$('#win').window({
					title:"指标详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/hazard_showStandardIndex" frameborder="0" width="100%" height="100%" />'
				});
			};
			
			function searchDatagrid(){
				departmentTypeSn=$('#departmentType').combobox('getValue');
				manageObjectSn=$('#parentHazard').textbox('getValue');
				hazardSn=$('#hazardSn').textbox('getValue');
				if(hazardSn!=null && hazardSn.length>0){
					var url = '${pageContext.request.contextPath}/hazard/hazardAction_showByHazardSn.action?departmentTypeSn='+departmentTypeSn+'&hazardSn='+hazardSn;
				}else{
					var url = '${pageContext.request.contextPath}/hazard/hazardAction_showHazard.action?departmentTypeSn='+departmentTypeSn+'&manageObjectSn='+manageObjectSn;
				}
	            $('#dg').datagrid('reload', url);
			};
			
	    </script>
</head>
<body>
		<div id="weixianyuan">
			<span>
					<lable for="departmentType">部门类型：</lable>
					<input id="departmentType"  name="departmentType" />
			</span>
			<span>
					<lable for="hazardSn">危险源：</lable>
					<input id="hazardSn"  name="hazardSn" />
			</span>
			<span>
					<lable for="parentHazard">管理对象：</lable>
					<input id="parentHazard"  name="parentHazard" />
			</span>
					<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
					<a id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >修改</a>
					<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >删除</a>
					<a id="import" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导入危险源</a>
					<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-export'" >导出危险源</a>
					<a id="importRelation" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导入危险源管理对象关联</a>
					<a id="exportRelation" class="easyui-linkbutton" data-options="iconCls:'icon-export'" >导出危险源管理对象关联</a>
					<a id="importStandardIndex" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导入危险源指标关联</a>
					<a id="exportStandardIndex" class="easyui-linkbutton" data-options="iconCls:'icon-export'" >导出危险源指标关联</a>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		 
		
		<!-- 危险源导出 -->
		<div id="exportHazard"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="sub">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 危险源-管理对象关联导出 -->
		<div id="exportHazardRelation"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="subRelation">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 危险源-指标导出 -->
		<div id="exportHazardStandardIndex"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="subStandardIndex">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 危险源导入 -->
		<div id="templateDialog" title="导入危险源" modal=true draggable=false
			class="easyui-dialog" closed=true  style="width: 350px;height:220px">
			<form id="daoru" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>下载模板：</td>
						<td>
								<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_btn()">下载导入模板</a> 
						</td>
					</tr>
					<tr>
    					<td>选择文件：</td>
    					<td>
    						<input id="uploadExcel" name="uploadExcel" class="easyui-filebox" style="width:250px" data-options="prompt:'请选择文件...',buttonText: '选择文件'" />
    					</td>
    				</tr>
    				<tr>
    					<td colspan="2">
    						<a class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="uploadExcel()">导入</a>
    					</td>
    				</tr>
				</table>
			</form>
		</div>
		<!-- 危险源-管理对象关联导入 -->
		<div id="templateDialogRelation" title="导入危险源" modal=true draggable=false
			class="easyui-dialog" closed=true  style="width: 350px;height:220px">
			<form id="daoruRelation" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>下载模板：</td>
						<td>
								<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_btnRelation()">下载导入模板</a> 
						</td>
					</tr>
					<tr>
    					<td>选择文件：</td>
    					<td>
    						<input id="uploadExcelRelation" name="uploadExcel" class="easyui-filebox" style="width:250px" data-options="prompt:'请选择文件...',buttonText: '选择文件'" />
    					</td>
    				</tr>
    				<tr>
    					<td colspan="2">
    						<a class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="uploadExcelRelation()">导入</a>
    					</td>
    				</tr>
				</table>
			</form>
		</div>
		
		<!-- 危险源-指标导入 -->
		<div id="templateDialogStandardIndex" title="导入危险源-指标关联" modal=true draggable=false
			class="easyui-dialog" closed=true  style="width: 350px;height:220px">
			<form id="daoruStandardIndex" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>下载模板：</td>
						<td>
								<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_btnStandardIndex()">下载导入模板</a> 
						</td>
					</tr>
					<tr>
    					<td>选择文件：</td>
    					<td>
    						<input id="uploadExcelStandardIndex" name="uploadExcel" class="easyui-filebox" style="width:250px" data-options="prompt:'请选择文件...',buttonText: '选择文件'" />
    					</td>
    				</tr>
    				<tr>
    					<td colspan="2">
    						<a class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="uploadExcelStandardIndex()">导入</a>
    					</td>
    				</tr>
				</table>
			</form>
		</div>
		
		<!-- 进度条窗口 -->
		<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
		        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
		      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
		</div> 
		<!-- 区分危险源和危险源失控统计 -->
		<input type="hidden" id="hazardCount" value="1" />
		
</body>
</html>