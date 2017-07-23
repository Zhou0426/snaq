<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>不安全行为</title>
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
			var UnsafeActLevelValue=new Array();
			
			$(function(){
				$('#dg').datagrid({
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_myUnsafeAct',
					idField: 'id',
		            //title:'查询结果',
		            toolbar:'#query',
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
							  {field:'id',hidden:true},
					          {field:'checkedDepartmentImplType',title:'贯标单位',width:80,align:'center'},
					          {field:'checkedDepartment',title:'被检部门',width:80,align:'center'},
					          {field:'violator',title:'不安全<br />行为人员',width:50,align:'center'},
					          {field:'actDescription',title:'行为描述',width:200,align:'center'},
					          {field:'checkType',title:'检查类型',width:50,align:'center'},
			                  {field:'systemAudit',title:'审核类型',width:50,align:'center'},
			                  {field:'editor',width:50,title:'录入人',align:'center'},
					          {field:'checkers',title:'检查成员',width:70,align:'center'},        
					          {field:'checkerFrom',title:'检查人来自',width:80,align:'center'},    
					          {field:'checkDateTime',title:'检查时间',width:50,align:'center'},    
					          {field:'checkLocation',title:'检查地点',width:70,align:'center'},
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
					          {field:'UnsafeActLevel',title:'不安全行为等级',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  UnsafeActLevelValue[index]=value;
					        		  }else{
					        			  UnsafeActLevelValue[index]="无";
					        		  }
					  		  }},
					          {field:'attachments',title:'附件详情',width:50,align:'center',
					        	  formatter: function(value,row,index){
						  				if (value!=0){
						  					return "<a href='javascript:;' onclick=showAttachments()>附件["+value+"]</a>";
						  				} else {
						  					return "<a href='javascript:;' onclick=showAttachments()>附件[0]</a>";
						  				}
					  		  }}
					     ]],
						 onDblClickCell: function(){
							$('#dg').datagrid("uncheckAll");
						 },
						view: detailview, 
						detailFormatter: function(rowIndex, row){
						return '<table style="border:1"><tr>' + 								
						'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
						'<td style="width:140px;text-align:center">' + 
						'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
						'<td style="width:140px;text-align:center">' + 
						'<p>' + UnsafeActLevelValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:100px;text-align:center">'+'专业：' + '</td>' + 
						'<td style="width:100px;text-align:center" >' + 
						'<p>' + specialityValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:100px;text-align:center">'+'痕迹：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + unsafeActMarkValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr><tr>' +
						
						'<td style="width:100px;text-align:center">'+'标准：' + '</td>' + 
						'<td style="width:380px;text-align:center" colspan="7">' + 
						'<p>' + unsafeActStandardValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr></table>'; 
						}
				});
			});

	        <!--展示指标详情-附件-->
	        function showAttachments(){
	        	$('#win').window({
					title:"附件详情",
					width:400,
					height:300,
					content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/compositive_show" frameborder="0" width="100%" height="100%" />'
				});
	        };
			
			
	    </script>
</head>
<body>
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>