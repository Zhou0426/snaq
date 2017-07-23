<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>管理对象</title>
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
		var first=1;
		$(function(){  
					$('#departmentTypeSn').combobox({
						url:'${pageContext.request.contextPath}/hazard/manageObjectAction_load.action',
						method:'post',
						//required:true,
						panelWidth: 200,
						editable:false,
						panelHeight:300,
						width:200,
						onLoadSuccess:function(node,data){
							//console.log(node.length);
							if(first==1){
								//判断加载父级管理对象是否为空
								if(node.length!=0){	
									//将根节点的值默认输出
									$('#departmentTypeSn').combobox('setValue',node[0].id);
									//级联父级管理对象
						            var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_loadParent.action?departmentTypeSn='+node[0].id;
						            $('#checked_mansger').combotree('reload', url);    
									first++;
								}
							}
						},
						onSelect: function(rec){
							a=1;
							$('#checked_mansger').combotree('clear');
				            var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_loadParent.action?departmentTypeSn='+rec.id;
				            $('#checked_mansger').combotree('reload', url);    
				        }
					});
					$('#checked_mansger').combotree({
						//url:'hazard/manageObjectAction_loadParent.action',
						method:'post',
						panelWidth: 200,
						panelHeight:'300',
						width:200,
						onLoadSuccess:function(node,data){
							if(a==1){
							//获取根节点
							var da=$('#checked_mansger').combotree('tree').tree('getRoot');
								//获取输出的值
								//var c=$('#checked_mansger').combotree('getValue');
								//判断加载父级管理对象是否为空
								if(da!=null){	
									//将根节点的值默认输出
									$('#checked_mansger').combotree('setValue',da);
									//var d=da.id;
									//if ( a==1 && d==c) {
									var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_show.action?parentManageObjectSn='+da.id;
									$('#dg').datagrid('reload', url);
							        a++;
									//	}
								}else{
									var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_show.action';
						            $('#dg').datagrid('reload', url);    
									}
							}
						},
						onSelect: function(rec){
				            var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_show.action?parentManageObjectSn='+rec.id;
				            $('#dg').datagrid('reload', url);   
				        }
					});
					$('#dg').datagrid({
			            //url: 'hazard/manageObjectAction_show.action',
			            idField: 'id',
			            title:'管理对象',
			            toolbar:'#danwei',
			            rownumbers: true,		//显示一个行号列
			            fitColumns:true,		//自动适应列
			            //height:400,
			           	fit:true,				//表格宽高自适应
			            nowrap:false,
			            striped:true,			//斑马线效果
						singleSelect:true,		//单行选中
			            loadmsg:'请等待...',		//加载等待时显示
			            pagination:true,		//显示分页组件
			            pageNumber:1,
			            pageSize:10,
			            pageList:[5,10,15,20,25,30],
			      		columns:[[
								  {field:'ids',hidden:true},
						          {field:'departmentTypeSn',hidden:true},
						          {field:'departmentTypeName',title:'所属部门类型',width:100,align:'center'},
						          {field:'manageObjectSn',title:'管理对象编号',width:100,align:'center'},
						          {field:'manageObjectName',title:'管理对象名称',width:100,align:'center'},
						          {field:'manageObjectType',title:'管理对象类型',width:100,align:'center'},
						          {		field:'hazardNum',
							          	title:'危险源',
							          	width:100,
							          	align:'center',
								        formatter: function(value, row, index) {  
							 		        return "<a href='javascript:;' onclick=showHazard() style='text-decoration:none'>详情["+value+"]"+"</a>";
									}
								}
						 ]],
						 onDblClickCell: function(){
							 $('#dg').datagrid("uncheckAll");
						 }
					});
					
					
					//权限管理
					var str="${sessionScope['permissions']}";
					//添加
					if(str.indexOf("100101")==-1){
						$("#add").css('display','none');
					}else{
						//添加按钮点击事件
						$('#add').bind('click', function(){
							var parVal=$('#checked_mansger').combotree('tree').tree('getRoot');
							//console.log(parVal);
							//判断父级管理对象是否为空
							if(parVal!=null){
									$('#win').window({
										title:"添加信息",
										width:400,
										height:280,
										content:'<iframe src="${pageContext.request.contextPath}/hazard/manageObject_add" frameborder="0" width="100%" height="100%" />'
									});  
								}else{
									$.messager.show({
										title:'提示信息',
										msg:'请选择父级管理对象',
										timeout:2000,
										showType:'slide'
									});	
								} 
					    });
					}
					//删除
					if(str.indexOf("100102")==-1){
						$("#removebtn").css('display','None');
					}else{
						//删除按钮点击事件
						$('#removebtn').bind('click', function(){    
							//判断是否有选中行记录
							var rows=$('#dg').datagrid("getSelections")
							if(rows.length==0){
								$.messager.show({
									title:'提示信息',
									msg:'请选中一行记录',
									timeout:2000,
									showType:'slide'
								});														
								}else{
									$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
										if (r){
											//获取选中记录的id
											ids=rows[0].ids;
											//发送ajax请求
											$.post("${pageContext.request.contextPath}/hazard/manageObjectAction_delete.action",{ids:ids},function(result){
														if(result=="\"success\""){
																$('#dg').datagrid("reload");
																$.messager.show({
																	title:'提示',
																	msg:'成功删除记录！',
																	timeout:2000,
																	showType:'slide'
																});	
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
					//修改
					if(str.indexOf("100103")==-1){
						$("#editbtn").css('display','None');
					}else{
						//修改按钮点击事件
						$('#editbtn').bind('click', function(){    
							//判断是否选中
							var rows=$('#dg').datagrid("getSelections");
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
										height:218,
										content:'<iframe src="${pageContext.request.contextPath}/hazard/manageObject_update" frameborder="0" width="100%" height="100%" />'
										});
									}  
					    });
					}
					//导入
					if(str.indexOf("100104")==-1){
						$("#import").css('display','None');
					}else{
						//导入管理对象按钮点击事件
						$('#import').bind('click', function(){
							$('#templateDialog').dialog('open');
					    });
						
					}
					//导出
					if(str.indexOf("100105")==-1){
						$("#export").css('display','None');
					}else{
					    //导出管理对象按钮点击事件
						$('#export').bind('click', function(){
							$('#exportManageObject').dialog('open'); 
					    });
						$('#exportManageObject').dialog({
							title:"导出管理对象",
							width:300,
							height:150,
							modal:true,
							closed:true,
							buttons:[{
								text:'导出全部',
								handler:function(){
										$('#sub').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/download_exportExcel.action',
											queryParams:{
													departmentTypeSn:$('#departmentTypeSn').combobox('getValue'),
													parentManageObjectSn:$('#checked_mansger').combotree('getValue'),
													id:"all"
											}	
										});	
										$('#exportManageObject').dialog('close'); 
										//进度条打开
										$('#p').progressbar('setValue',0);
										$('#winPro').window('open');
										getProValue();
								}
							},{
								text:'导出当前',
								handler:function(){
									$('#sub').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/download_exportExcel.action',
										queryParams:{
												departmentTypeSn:$('#departmentTypeSn').combobox('getValue'),
												parentManageObjectSn:$('#checked_mansger').combotree('getValue')
										}	
									});	
	 								$('#exportManageObject').dialog('close');
	 								//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'关闭',
								handler:function(){$('#exportManageObject').dialog('close'); }
							}]
						});
					}
					//管理对象-危险源导入
					if(str.indexOf("100108")==-1){
						$("#importRelation").css('display','None');
					}else{
						//导入管理对象-危险源关联点击事件
						$('#importRelation').bind('click', function(){
							$('#templateDialogRelation').dialog('open');
					    });
					}
					//管理对象-危险源导出
					if(str.indexOf("100109")==-1){
						$("#exportRelation").css('display','None');
					}else{
						//导出管理对象-危险源关联按钮点击事件
					    $('#exportRelation').bind('click',function(){
					    	$('#exportManageObjectRelation').dialog('open'); 
					    });
					    $('#exportManageObjectRelation').dialog({
							title:"导出管理对象危险源关联",
							width:300,
							height:150,
							modal:true,
							closed:true,
							buttons:[{
								text:'导出全部关联',
								handler:function(){
										$('#subRelation').form('submit', {
											url:'${pageContext.request.contextPath}/hazard/download_exportExcelRelation.action',
											queryParams:{
													departmentTypeSn:$('#departmentTypeSn').combobox('getValue'),
													parentManageObjectSn:$('#checked_mansger').combotree('getValue'),
													id:"all"
											}	
										});	
										$('#exportManageObjectRelation').dialog('close'); 
										//进度条打开
										$('#p').progressbar('setValue',0);
										$('#winPro').window('open');
										getProValue();
								}
							},{
								text:'导出当前关联',
								handler:function(){
									$('#subRelation').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/download_exportExcelRelation.action',
										queryParams:{
												departmentTypeSn:$('#departmentTypeSn').combobox('getValue'),
												parentManageObjectSn:$('#checked_mansger').combotree('getValue')
										}	
									});	
	 								$('#exportManageObjectRelation').dialog('close');
	 								//进度条打开
									$('#p').progressbar('setValue',0);
									$('#winPro').window('open');
									getProValue();
								}
							},{
								text:'关闭',
								handler:function(){$('#exportManageObjectRelation').dialog('close'); }
							}]
						});
					}
					
					
					
					
					
					
					
					
			}); 
			//管理对象模板下载
			function download_btn(){
				var url ='${pageContext.request.contextPath}/hazard/download_download.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			};
			//管理对象危险源关联模板下载
			function download_btnRelation(){
				var url ='${pageContext.request.contextPath}/hazard/download_downloadRelation.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			};
			//ajax发送请求session--导入
			function getSession(){
				$.ajax({
					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
					success: function(data){
						$('#p').progressbar('setValue',data);
					}
				},"json");
			};
			//ajax发送请求session--导出
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
			//管理对象导入
			function uploadExcel(){     
		         //得到上传文件的全路径  
		         var fileName= $('#uploadExcel').filebox('getValue');
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
		                          $('#daoru').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/manageObjectAction_importExcel.action',
										success:function(result){
											  $('#p').progressbar('setValue','100');
											  clearInterval(timer);
											  $('#winPro').window('close');
					                          $('#uploadExcel').filebox('setValue','');
					                          $('#templateDialog').dialog('close');
					                          $.messager.alert('提示',"<div class='align-center nextPage'>"+result.substring(1,result.length-1)+"</div>",'info',function(){
					                        	  var c=$('#checked_mansger').combotree('getValue');
						                          var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_show.action?parentManageObjectSn='+c;
										          $('#dg').datagrid('reload', url);
					                          });
										}
								  });
		                    }else{  
		                        $.messager.alert('提示','请选择xls格式文件！','info');   
		                        $('#uploadExcel').filebox('setValue','');   
		                    }  
		                 } 
		      };
			//管理对象-危险源关联导入
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
					                        	  var c=$('#checked_mansger').combotree('getValue');
						                          var url = '${pageContext.request.contextPath}/hazard/manageObjectAction_show.action?parentManageObjectSn='+c;
										          $('#dg').datagrid('reload', url);
					                          });
										}
								  });
		                    }else{  
		                        $.messager.alert('提示','请选择xls格式文件！','info');   
		                        $('#uploadExcelRelation').filebox('setValue','');   
		                    }  
		                 } 
			};
		    function showHazard(){
		    	$('#win').window({
					title:"危险源详情",
					width:800,
					height:400,
					content:'<iframe src="${pageContext.request.contextPath}/hazard/manageObject_showHazard" frameborder="0" width="100%" height="100%" />'
					});
			};
			
			
		</script>
</head>
<body>
	<div id='danwei'>
		<span>
			<lable for="checked_department">部门类型：</lable>
			<input id="departmentTypeSn"  name="departmentTypeSn" />
		</span>
		<span>
			<lable for="standard">父级管理对象：</lable>
			<input id="checked_mansger" name="parentManageObject"  />
		</span>
			<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="editbtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
			<a id="removebtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">删除</a>
			<a id="import" class="easyui-linkbutton" data-options="iconCls:'icon-excel'">导入管理对象</a>
			<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-export'">导出管理对象</a>
			<a id="importRelation" class="easyui-linkbutton" data-options="iconCls:'icon-excel'">导入管理对象危险源关联</a>
			<a id="exportRelation" class="easyui-linkbutton" data-options="iconCls:'icon-export'">导出管理对象危险源关联</a>
	</div>
		<table id="dg" ></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		
		<!-- 管理对象导出 -->
		<div id="exportManageObject"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="sub">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 管理对象危险源关联导出 -->
		<div id="exportManageObjectRelation"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="subRelation">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 管理对象导入 -->
		<div id="templateDialog" title="导入管理对象" modal=true draggable=false
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
		
		<!-- 管理对象危险源关联导入 -->
		<div id="templateDialogRelation" title="导入管理对象危险源关联" modal=true draggable=false
			class="easyui-dialog" closed=true  style="width: 350px;height:220px">
			<form id="daoruRelation" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>下载模板：</td>
						<td>
							<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_btnRelation()">下载关联导入模板</a> 
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
		
		<!-- 进度条窗口 -->
		<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
		        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
		      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
		</div>
		
		
</body>
</html>