<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内业上传</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<style type="text/css">
			.div-height{margin:6px auto;}
 			.div-show{display:inline;margin:5px auto;}
 			a.shenghe { width: 28px; height: 16px; display: block; float: left; background: url('${pageContext.request.contextPath}/easyui/themes/icons/edit_add.png') no-repeat center; }
	</style>
	<script type="text/javascript">
		var a=1;
		var b=1;
		var standardSn="";
		var standardIndexSn="";
		function loadAction(){
			$('#dg').datagrid('load',{
				standardSn:standardSn,
				standardIndexSn:standardIndexSn
			});
		};
// 		function downLoadInteriorWorkFile(index){
// 			var rows=$('#dg').datagrid('getRows');
// 			$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",{attachmentPath:rows[index].attachmentPath},function(result){
// 				if(result=="success"){
// 					var url ='${pageContext.request.contextPath}/interior/work/download_InteriorWorkFile.action?attachmentPath='+rows[index].attachmentPath+'&interiorName='+rows[index].interiorWorkNname;
// 					$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
// 				}else{
// 					$.messager.alert('警告','文件已丢失，下载失败！');
// 				}
// 			},"json");
// 		};
		$(function(){
		   $('#standard').combobox({
				url:'${pageContext.request.contextPath}/interior/work/interiorWork_standard.action?departmentSn='+${sessionScope.departmentSn},
				method:'post',
				valueField:'id',    
			    textField:'text',
			    editable:false,
				panelWidth: 300,
				panelHeight:'auto',
				width:300,
				onLoadSuccess:function(node,data){
					if(a==1){
						if(node.length>0){
							standardSn=node[0].id;
							$(this).combobox('setValue',node[0].id);
							$('#standardindex').combotree('clear');
							url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+node[0].id,
							$('#standardindex').combotree('reload',url);
							a++;
						}
					}
				},
				onSelect: function(rec){
					b=1;
					standardSn=rec.id;
					$('#standardindex').combotree('clear');
					url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+rec.id,
					$('#standardindex').combotree('reload',url);
		        }
			});
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/interior/work/interiorWork_showInterior',
				idField: 'id',
	            title:'内业上传',
	            toolbar:'#tb',
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
				          {field:'interiorWorkSn',title:'内业资料编号',hidden:true},
				          {field:'interiorWorkNname',title:'内业资料名称',width:100,align:'center'},    
				          {field:'uploader',title:'上传人',width:30,align:'center'},    
				          {field:'uploadDatetime',title:'上传时间',width:50,align:'center'},    
				          {field:'standardIndexes',title:'相关指标',width:200,
					        	formatter: function(value,row,index){
					  				return '<a class="shenghe" href="javascript:;" onclick=showHazard() iconCls="icon-add"></a>'+value;
					  			}
					       },    
				          {field:'implDepartmentName',title:'贯标单位',width:50,align:'center'},
				          {field:'department',title:'上传人所在部门',width:50,align:'center'},
				          {field:'attachmentPath',title:'附件详情',width:30,align:'center',
				        	  formatter: function(value, row, index) {
				        		  return "<a href='javascript:' onclick='showInteriorWorkFile()'>附件[" + value + "]</a>";	
							}
						  }
// 				          {field:'attachmentPath',title:'附件路径',width:150,align:'center',hidden:true}
				     ]],
					 onDblClickCell: function(){
						$('#dg').datagrid("uncheckAll");
					},
					onLoadSuccess:function(){
						//内业上传指标添加
						if(str.indexOf("110103")==-1){
							$('a.shenghe').css('display','None');
						}
					},
					onBeforeLoad:function(){
						if(standardIndexSn==""||standardIndexSn.length==0){
							return false;
						}else{
							return true;
						}
					}
				});
				$('#standardindex').combotree({
					//url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindex.action?standardSn='+standardSn,
					method:'post',
					panelWidth:500,
					panelHeight:500,
					width:300,
					onLoadSuccess:function(node,data){
						if(b==1){
							//console.log(data[0].id);
							if(data.length>0){
								$('#standardindex').combotree('setValue',data[0].id);
								standardIndexSn=data[0].id;
								loadAction();<!--发送数据-->
								b++;
							}else{
								standardIndexSn="";
								loadAction();<!--发送数据-->
							}
						}
					},
					onSelect: function(rec){
						standardIndexSn=rec.id;
						loadAction();<!--发送数据-->
			        }
				});
				$('#fb').filebox({    
				    buttonText: '选择文件', 
				    buttonAlign: 'right',
				    multiple:true,
				    prompt:'请选择文件'
				});
				//弹出窗口时禁用验证
				$("#ff").form("disableValidation");
			
			//权限管理
			var str="${sessionScope['permissions']}";
			//内业上传上传功能
			if(str.indexOf("110101")==-1 && str.indexOf("1101")==-1){
				$("#loadFile").css('display','None');
			}else{
				$('#btn').bind('click', function(){
					var as=$('#standardindex').combotree('getValue');
					var fileName= $('#fb').filebox('getValue');
					if(as!=null && as!=""){
						if(fileName==""){
							 $.messager.alert('提示','请选择上传文件！','info');   
						}else{
							//开启验证
							$("#ff").form("enableValidation");
							//如果验证成功，则提交数据
							if($('#ff').form("validate")){
								$('#ff').form('submit', {
									url:'${pageContext.request.contextPath}/interior/work/interiorWork_add.action',
									queryParams:{
										standardSn:standardSn,
										standardIndexSn:standardIndexSn
									},
									success: function(result){
										if(result=="\"success\""){
												$.messager.show({
													title:'提示',
													msg:'成功上传内业资料！',
													timeout:2000,
													showType:'slide'
												});	
												loadAction();<!--发送数据-->
												$('#interiorName').textbox('clear');
												$('#fb').filebox('setValue','');   
										}else{
												$.messager.alert('警告','添加失败，请检查后重新操作！');
										}
									}
								});
							}
						}
					}else{
						$.messager.alert('提示','请选择指标！');
					}
						
			    });
			}
			//内业上传删除
			if(str.indexOf("110102")==-1){
				$("#remove").css('display','None');
			}else{
				$('#remove').bind('click', function(){
					var rows=$('#dg').datagrid("getSelections");
					if(rows.length==0){
						$.messager.show({
							title:'提示信息',
							msg:'请选中一行记录',
							timeout:2000,
							showType:'slide'
						});														
						}else{
							$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
									if(r){
										$.post("${pageContext.request.contextPath}/interior/work/interiorWork_deleteInteriorWork.action",{id:rows[0].id,attachmentPath:rows[0].attachmentPath},function(result){
												if(result=="success"){
														$.messager.show({
															title:'提示',
															msg:'成功删除记录！',
															timeout:2000,
															showType:'slide'
														});	
													$('#dg').datagrid("reload");
												}else{
														$.messager.alert('警告','删除异常，请检查后重新操作！');
												}
										},"json");
										$('#dg').datagrid("unselectAll");
									}
							});
						}
			    });
			}
			
		});
		//添加指标
		function showHazard(){
			var str="${sessionScope['permissions']}";
			if(str.indexOf("110103")!=-1){
				$('#win').window({
					title:"添加指标",
					width:500,
					height:450,
					content:'<iframe src="${pageContext.request.contextPath}/interior/work/interior-work/upload_add" frameborder="0" width="100%" height="100%" />'
				});
			}else{
				$('a.shenghe').css('display','None');
			}
		};
		//附件详情
		function showInteriorWorkFile(){
	    	$('#win').window({
				title:"附件详情",
				width:800,
				height:400,
				content:'<iframe src="${pageContext.request.contextPath}/interior/work/upload_showAttachment" frameborder="0" width="100%" height="100%" />'
			});
		};

	</script>
</head>
<body>
		<div id="tb">
				<form id="ff"  method="post" enctype="multipart/form-data">
						<div class="div-height">   
							      <label for="departmentType">&emsp;所属部门：${sessionScope.departmentName}</label>
						</div>
				    	<div class="div-height">
					    		<div class="div-show">
										<lable for="standard">&emsp;标&emsp;&emsp;准：</lable><input id="standard"  />
								</div>
								<div class="div-show" style="margin-left:10px">
										<lable for="standardindex">指&emsp;&emsp;标：</lable><input id="standardindex"  />
								</div>
						</div>
						<div class="div-height">
							      <label for="interiorName">&emsp;内业名称：</label><input id="interiorName" name="interiorName"
							       class="easyui-textbox" data-options="prompt:'请输入文件名称'" style="width:300px" required> 
						</div>
						<div id="loadFile" class="div-height">
								<label for="fb">&emsp;选择文件：</label><input  name="upload"  id="fb" type="text" style="width:300px">
								<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="margin-left:10px">上传</a>
								<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
						</div>
				</form>
		</div>
		<table id="dg"></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>