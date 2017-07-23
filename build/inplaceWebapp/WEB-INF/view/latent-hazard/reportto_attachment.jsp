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
<style type="text/css">
	#fb{
	float:left
	}
	#submit{
		height:22px
	}
</style>
<script type="text/javascript">
	var parentrow = parent.$('#dg').datagrid('getSelections');
	var latentHazardSn = parentrow[0].latentHazardSn;
	function deleteit(index){
		$('#d-a').datagrid('selectRow',index);//选中当前行
		var row = $('#d-a').datagrid('getSelected');
		var id = row.id;
		$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
			if(r){
				//发送请求提交删除信息
                $.post("${pageContext.request.contextPath}/latenthazard/latenthazardAction_deleteById.action",{'id':id},function(result){
					if(result.message == "success"){
						//删除页面，刷新
						$("#d-a").datagrid("clearChecked");
						$("#d-a").datagrid("reload");
						parent.$("#dg").datagrid("clearChecked");
						parent.$("#dg").datagrid("reload");
					}else if(result.message == "login"){
						$.messager.alert('系统提示','删除出错，只能删除本人上传的文件');
					}else if(result.message == "input"){
						$.messager.alert('系统提示','删除出错，该重大隐患' + result.status + '，不能删除附件');
					}else{
						$.messager.alert('系统提示','删除出错，请关闭页面后重新操作');
					}
				},"json");
			}
		});
	};
	//判断附件是否存在并下载
	function downLoadFile(index){
		var rows=$('#d-a').datagrid('getRows');
		$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",{attachmentPath:rows[index].physicalFileName},function(result){
			if(result=="success"){
				var url ='${pageContext.request.contextPath}/interior/work/download_InteriorWorkFile.action?attachmentPath='+rows[index].physicalFileName+'&interiorName='+rows[index].logicalFileName;
				$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			}else{
				$.messager.alert('警告','文件已丢失，下载失败！');
			}
		},"json");
	};
	$(function(){
		
		$("#d-a").datagrid({
		    url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_showDataAttachment',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'latentHazardSn':latentHazardSn},//请求远程数据发送额外的参数
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			fit:true,
			fitColumns:true,/*列宽自动*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			toolbar:"#upload",
		    columns:[[ 
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'attachmentType',title:'附件类型',hidden:true},
		        {field:'physicalFileName',title:'附件物理路径',hidden:true},
		        {field:'logicalFileName',title:'附件名称(点击可下载)',width:100,align:'center',
					formatter:function(value,row,index){
		        		return "<a href='javascript:;' onclick='downLoadFile("+index+")' style='text-decoration:none'>" + value + "</a>";
					} 
		        },
		        {field:'uploadPerson',title:'上传人',align:'center',width:50},
		        {field:'uploadDateTime',title:'上传时间',align:'center',width:70},
		        {field:'op',title:'操作',width:20,align:'center',
		        	formatter:function(value,row,index){
						return "<a href='javascript:;' id='btn' onclick='deleteit(" + index + ")' style='text-decoration:none'>删除</a>";
					} 
		        }
		    ]]    
		});
  		$('#fb').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right' 
		});
		$('#submit').linkbutton({
		    iconCls:'icon-add'   
		});
		//表单提交
		$("[name='upload']").validatebox({
			required:true,//file文本域 validate不能实现及时验证
			missingMessage:'请选择文件',
		    multiple: true
		});
		//开始禁止验证
 		$("#upload").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#upload").form("enableValidation");
			if($("#upload").form("validate")){
				//ajax提交
				//console.log( parentrow[0].id )
				$('#upload').form('submit',{
					url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_addAttachment',
					queryParams:{id:parentrow[0].id},
					success:function(data){
						var data = eval('(' + data + ')');
						if( data.message != "success" ){
							$.messager.alert('系统提示','添加出错，请关闭页面重新操作');
						}
						$("#upload").form("disableValidation");
						$("#upload").form("reset");
						$("#d-a").datagrid("reload");
						parent.$("#dg").datagrid("reload");
					}
				});
			}
		});
	});
</script>
</head>
<body>
	<table id="d-a"></table> 
	<form id="upload" method="post" enctype="multipart/form-data">
		<input id="fb" type="text" name="upload" style="width:270px;height:26px;"/>
		<a id="submit" href="#">提交</a>
	</form> 
</body>
</html>