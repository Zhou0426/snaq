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

	var checkedDepartmentImplTypeValue=new Array();
	var unsafeActStandardValue=new Array();
	var unsafeActMarkValue=new Array();
	var actDescriptionValue=new Array();
	var unsafeActLevelValue=new Array();
	var checkedDepartmentValue=new Array();
	var inconformityItemSnValue=new Array();

	function attachment_unsafeAct(index){
			$('#win2').window({
				width:400,
				height:300,
				title:'相关附件',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/prewarning/unsafeact_show_attachment" frameborder="0" width="100%" height="100%"/>'
			});
	};
	
	$(function(){
		var row2=parent.$('#dg').datagrid("getSelections");
		$('#dg2').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_prewarningShow.action',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			//title:"动态检查",
			queryParams:{violatorId:row2[0].personId},//请求远程数据发送额外的参数
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
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkType',width:100,title:'检查类型'},
				{field:'systemAudit',width:100,title:'审核类型'},
				{field:'checkDateTime',width:100,title:'检查时间'},
				{field:'checkLocation',width:100,title:'检查地点'},
				{field:'checkers',width:100,title:'检查成员'},
				{field:'checkerFrom',width:100,title:'检查人来自'},
				{field:'speciality',width:100,title:'专业'},
				{field:'violator',title:'不安全行为人员',hidden:true},
				{field:'inconformityItemSn',width:100,title:'不符合项编号',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  inconformityItemSnValue[index]=value;
		        		  }else{
		        			  inconformityItemSnValue[index]="无";
		        		  }
		  		  }},
				{field:'checkedDepartment',width:100,title:'被检部门',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  checkedDepartmentValue[index]=value;
		        		  }else{
		        			  checkedDepartmentValue[index]="无";
		        		  }
		  		  }},    
				{field:'unsafeActLevel',width:100,title:'不安全行为等级',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  unsafeActLevelValue[index]=value;
		        		  }else{
		        			  unsafeActLevelValue[index]="无";
		        		  }
		  		  }},
				{field:'actDescription',width:100,title:'行为描述',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  actDescriptionValue[index]=value;
		        		  }else{
		        			  actDescriptionValue[index]="无";
		        		  }
		  		  }},
				{field:'unsafeActMark',width:100,title:'痕迹',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  unsafeActMarkValue[index]=value;
		        		  }else{
		        			  unsafeActMarkValue[index]="无";
		        		  }
		  		  }},
				{field:'unsafeActStandard',width:100,title:'不安全行为标准',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  unsafeActStandardValue[index]=value;
		        		  }else{
		        			  unsafeActStandardValue[index]="无";
		        		  }
		  		  }},
				{field:'checkedDepartmentImplType',width:100,title:'贯标单位',hidden:true,
		        	  formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  checkedDepartmentImplTypeValue[index]=value;
		        		  }else{
		        			  checkedDepartmentImplTypeValue[index]="无";
		        		  }
		  		  }},
				{field:'attachments',width:100,title:'相关附件',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment_unsafeAct("+index+")' style='text-decoration:none'>" + "附件"+"["+value+"]" + "</a>";
				}}
			]],
			view: detailview, 
			detailFormatter: function(rowIndex, row){
			return '<table style="border:1"><tr>' + 
			'<td style="width:100px;text-align:center">'+'贯标单位：' + '</td>' + 
			'<td style="width:140px;text-align:center">' + 
			'<p>' + checkedDepartmentImplTypeValue[rowIndex] + '</p>' + 
			'</td>' +
			'<td style="width:100px;text-align:center">'+'被检部门：' + '</td>' + 
			'<td style="width:140px;text-align:center">' + 
			'<p>' + checkedDepartmentValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			
			'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
			'<td style="width:140px;text-align:center">' + 
			'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
			'</td>' +
			'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
			'<td style="width:140px;text-align:center">' + 
			'<p>' + unsafeActLevelValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			
			'<td style="width:100px;text-align:center">'+'行为描述：' + '</td>' + 
			'<td style="width:140px;text-align:center" >' + 
			'<p>' + actDescriptionValue[rowIndex] + '</p>' + 
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
	});
</script>
</head>
<body>
    	<table id="dg2" ></table>
		<div id="win2" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>