<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内业查询</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<style type="text/css">
.div-height {
	margin: 6px auto;
}

.div-show {
	display: inline;
	margin: 5px auto;
}
</style>
<script type="text/javascript">
		var a=1;
		var b=1;
		var standardSn="";
		var standardIndexSn="";
		var interiorName="";
		var departmentSn="";
		function loadAction(){
			$('#dg').datagrid('load',{
				standardSn:standardSn,
				standardIndexSn:standardIndexSn,
				interiorName:interiorName,
				departmentSn:departmentSn
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
			
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/interior/work/interiorWork_queryInterior.action',
				idField: 'id',
	            title:'内业查询',
	            toolbar:'#tb',
	            rownumbers: true,	//显示一个行号列
	            fitColumns:true,		//自动适应列
	            //height:400,
	           	fit:true,						//表格宽高自适应
	            nowrap:false,
	            striped:true,				//斑马线效果
				singleSelect:true,		//单行选中
	            loadmsg:'请等待...',	//加载等待时显示
	            pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:15,
	            pageList:[15,20,25,30,50],
				columns:[[
						  {field:'id',hidden:true},
				          {field:'interiorWorkSn',title:'内业资料编号',width:120,align:'center',hidden:true},
				          {field:'interiorWorkNname',title:'内业资料名称',width:100,align:'center'},    
				          {field:'uploader',title:'上传人',width:30,align:'center'},    
				          {field:'uploadDatetime',title:'上传时间',width:50,align:'center'},    
				          {field:'standardIndexes',title:'相关指标',width:200,align:'center'},
				          {field:'implDepartmentName',title:'贯标单位',width:50,align:'center'},
				          {field:'department',title:'上传人所在部门',width:50,align:'center'},
				          {field:'attachmentPath',title:'附件详情',width:30,align:'center',
				        	  formatter: function(value, row, index) {
				        		  return "<a href='javascript:' onclick='showInteriorWorkFile()'>附件[" + value + "]</a>";	
							}
						  }
				     ]],
					 onDblClickCell: function(){
						$('#dg').datagrid("uncheckAll");
					 },
					 onBeforeLoad:function(){
							if(standardSn==""||standardSn.length==0){
								return false;
							}else{
								return true;
							}
					 }
				});
			$('#department').combotree({
				url:'${pageContext.request.contextPath}/department/treeAll.action',
				method:'post',
				valueField: 'id',    
		        textField: 'text', 
				required:true,
				queryParams:{'resourceSn':'1102'},
				panelWidth: 300,
				panelHeight:350,
				width:200,
				onLoadSuccess:function(node,data){
					<!--设置数据加载成功时的默认值-->
					var da=$('#department').combotree('tree').tree('getRoot');
					$('#department').combotree('setValue',da.id);
					//第一次加载时执行的事件
					if(b==1){
						departmentSn=da.id;
						url='${pageContext.request.contextPath}/interior/work/interiorWork_standardQuery.action?departmentSn='+da.id;
						$('#standard').combobox('reload',url);
						b++;
						if(data.length == 1){
							var node = $('#department').combotree('tree').tree('find', data[0].id);
							$('#department').combotree('tree').tree('expand', node.target);
			        	}
					}
				},
				onSelect: function(data){
					a=1;
					departmentSn=data.id;
					url='${pageContext.request.contextPath}/interior/work/interiorWork_standardQuery.action?departmentSn='+data.id;
					$('#standard').combobox('reload',url);
					
		        }
			});
			$('#standard').combobox({
				//url:'${pageContext.request.contextPath}/interior/work/interiorWork_standard.action?departmentSn='+${sessionScope["person"].department.departmentSn},
				method:'post',
				valueField:'id',    
			    textField:'text',
			    editable:false,
				panelWidth: 400,
				panelHeight:'auto',
				width:300,
				onLoadSuccess:function(node,data){
					if(a==1){
						if(node.length>0){
							standardSn=node[0].id;
							standardIndexSn="";
							$(this).combobox('setValue',node[0].id);
							$('#standardindex').combotree('clear');
							url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+node[0].id,
							$('#standardindex').combotree('reload',url);
							loadAction();<!--发送数据-->
							a++;
						}
					}
				},
				onSelect: function(rec){
					standardSn=rec.id;
					standardIndexSn="";
					$('#standardindex').combotree('clear');
					url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+rec.id,
					$('#standardindex').combotree('reload',url);
					loadAction();<!--发送数据-->
		        }
			});
			$('#standardindex').combotree({
				//url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindex.action?standardSn='+standardSn,
				method:'post',
				panelWidth:500,
				panelHeight:500,
				width:300,
				onSelect: function(rec){
					standardIndexSn=rec.id;
					loadAction();<!--发送数据-->
		        }
			});
			$('#interiorName').textbox({
				onChange:function(newValue, oldValue){
					interiorName=newValue;
					loadAction();<!--发送数据-->
				}
			});
			$('#btn11').bind('click', function(){
				$('#standardindex').combotree('clear');
				standardIndexSn="";
				loadAction();<!--发送数据-->
		    });
			
			
		});
		//附件详情
		function showInteriorWorkFile(){
	    	$('#win').window({
				title:"附件详情",
				width:800,
				height:400,
				content:'<iframe src="${pageContext.request.contextPath}/interior/work/upload_queryAttachment" frameborder="0" width="100%" height="100%" />'
			});
		};

	</script>
</head>
<body>
	<div id="tb">
		<div class="div-height">
			<label for="department">&emsp;所属部门：</label>
			<input id="department"  name="department" />
		</div>
		<div class="div-height">
			<div class="div-show">
				<lable for="standard">&emsp;标&emsp;&emsp;准：</lable>
				<input id="standard" />
			</div>
			<div class="div-show" style="margin-left: 10px">
				<lable for="standardindex">指&emsp;&emsp;标：</lable>
				<input id="standardindex" />
			</div>
			<span>
				<a id="btn11" class="easyui-linkbutton"  data-options="iconCls:'icon-undo'">重置</a>
			</span>
		</div>
		<div class="div-height">
			<label for="interiorName">&emsp;搜&emsp;&emsp;索：</label>
			<input id="interiorName" name="interiorName" class="easyui-textbox"
				data-options="prompt:'请输入内业名称或上传人'" style="width: 300px">
		</div>
	</div>
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>