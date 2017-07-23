<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	
		function downLoadFile(index){
			var rows=$('#dg').datagrid('getRows');
			$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",{attachmentPath:rows[index].physicalFileName},function(result){
				if(result=="success"){
					var url ='${pageContext.request.contextPath}/standard/download.action?fileName='+rows[index].physicalFileName+'&&newName='+rows[index].logicalFileName;
					$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
				}else{
					$.messager.alert('警告','文件已丢失，下载失败！');
				}
			},"json");
		};
		$(function(){	
			var row=parent.$('#dg2').datagrid("getSelections");
			$('#dg').datagrid({
				url: '${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_queryJoinUnsafeAct',
				queryParams:{'inconformityItemSn':row[0].inconformityItemSn},//请求远程数据发送额外的参数
				idField: 'id',
	            rownumbers: true,	//显示一个行号列
	            fitColumns:true,	//自动适应列
	           	fit:true,			//表格宽高自适应
	            nowrap:false,
	            striped:true,		//斑马线效果
				singleSelect:true,	//单行选中
	            loadmsg:'请等待...',	//加载等待时显示
	            pagination:true,	//显示分页组件
	            pageNumber:1,
	            pageSize:5,
	            pageList:[5,10,15,20,25,30],
	      		columns:[[
				         	{field:'id',title:'逻辑主键',hidden:true},	         	
							{field:'attachmentType',title:'附件类型',width:100},
					        {field:'logicalFileName',title:'附件名称',width:100,formatter:function(value,row,index){
					        	return "<a href='javascript:' onclick='downLoadFile("+index+")'>" + value + "</a>";
								} 
					        },
					        {field:'physicalFileName',title:'附件物理路径',width:100,hidden:true,formatter:function(value,row,index){
								return "<a href="+ value +">" + value + "</a>";} 
					        }
				 ]],
				 onDblClickCell: function(){
					$('#dg').datagrid("uncheckAll");
				 }
			});
				
		});
	</script>
</head>
<body>
	<table id="dg"></table>
</body>
</html>