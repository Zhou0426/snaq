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
	
	<style type="text/css">
		form div{
			margin:10px
		}
	</style>
	<script type="text/javascript">
		var rows=parent.$('#dg').datagrid('getSelections');
		var monthTime=parent.$('#da').val();
		var yearTime=parent.$('#selectTime').combobox('getValue');
		var titleValue=rows[0].departmentName+"&nbsp;-&nbsp;"+monthTime+"月份得分详情";
		$(function(){
			parent.$('#win').window('setTitle',titleValue);
			$('#dg').datagrid({
	            url: '${pageContext.request.contextPath}/office/appraisals/office_loadDetailData.action',
	            idField: 'personId',
	            queryParams:{'departmentSn':rows[0].departmentSn,'yearTime':yearTime,'monthTime':monthTime},
	            rownumbers: true,		//显示一个行号列
	            fitColumns:true,		//自动适应列
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
					{field:'personId',title:'人员编号',hidden:true},
					{field:'personName',title:'人员名称',width:100,align:'center'},
					{field:'checkTaskCount',title:'检查任务量',width:100,align:'center'},
					{field:'realCheckCount',title:'实际完成量',width:100,align:'center',formatter:function(value,index,row){
						if(value != 0){
							return '<a href = "javascript:" onclick=showPersonDetail() style = "text-decoration:none">' + value + '</a>';
						}else{
							return value;
						}
					}},
					{field:'rank',title:'任务完成率',width:100,align:'center',formatter:function(value,index,row){
						return value + '%';
					}}
				]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
				 }
			});
		});
		function showPersonDetail(){
			parent.$('#winDetail').window({
	 			width:800,
	 			height:400,
	 			title:"检查详情",
	 			cache:false,
	 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/office_showPersonDetail" frameborder="0" width="100%" height="100%"/>'
	 		});
		}
	</script>
</head>
<body>
	<table id="dg"></table>
</body>
</html>