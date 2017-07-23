<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>综合查询</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
		<script type="text/javascript">

			var standardIndexValue=new Array();
			var hazardValue=new Array();
			var correctPrincipalValue=new Array();
			var inconformityLevelValue=new Array();
			var specialityValue=new Array();
			var deductPointsValue=new Array();
			var machineValue=new Array();
			var inconformityItemNatureValue=new Array();
			var checkersValue=new Array();
			var inconformityItemSnValue=new Array();
			var applyDateTimeValue=new Array();
			var editorValue=new Array();
			var checkTypeValue=new Array();
			var systemAuditValue=new Array();
			var checkerFromValue=new Array();
			var correctTypeValue=new Array();
			
			$(function(){
				$('#dg').datagrid({
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeConditionDeferAction_showData.action',
					idField: 'id',
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
							  {field:'applicationSn',hidden:true},
					          {field:'checkedDepartmentImplType',title:'贯标单位',width:100,align:'center'},
					          {field:'checkedDepartment',title:'被检部门',width:100,align:'center'},
					          {field:'checkDateTime',title:'检查时间',width:70,align:'center'},
					          {field:'checkLocation',title:'检查地点',width:100,align:'center'},    
					          {field:'problemDescription',title:'问题描述',width:300,align:'center'},
					          {field:'riskLevel',title:'原风险等级',width:65,align:'center'},
					          {field:'correctDeadline',title:'整改期限',width:70,align:'center'},
					          {field:'correctProposal',title:'整改建议',width:150,align:'center'},
					          {field:'reason',title:'申请理由',width:80,align:'center'},
					          {field:'applicant',title:'申请人',width:50,align:'center'},
					          {field:'applyDeferTo',title:'申请延期到',width:70,align:'center'},
					          {field:'passed',title:'审批结果',width:80,align:'center',
					        	  formatter: function(value, row, index) {
						        	  if(value != null && value.length > 0){
						        		  return value;
						        	  }else{
						        		  return "未审核 <a name='btn' href='#'></a>";;
						        	  }
								  }
						 	  },
					          {field:'auditRemark',title:'审批说明',width:80,align:'center',
					        	  formatter: function(value, row, index) {
					        		  if(value != null && value.length > 0){
						        		  return value;
						        	  }else{
						        		  return "无";
						        	  }
								  }
						 	  },
					          {field:'auditor',title:'审批人',width:50,align:'center',
					        	  formatter: function(value, row, index) {
					        		  if(value != null && value.length > 0){
						        		  return value;
						        	  }else{
						        		  return "无";
						        	  }
								  }
						 	  },
					          {field:'auditDatetime',title:'审批时间',width:70,align:'center',
					        	  formatter: function(value, row, index) {
					        		  if(value != null && value.length > 0){
						        		  return value;
						        	  }else{
						        		  return "无";
						        	  }
								  }
						 	  },
					
					          {field:'applyDateTime',title:'申请时间',width:150,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  applyDateTimeValue[index] = value;
					        		  }else{
					        			  applyDateTimeValue[index] = "无";
					        		  }
					  		  }},
			                  {field:'editor',width:50,title:'录入人',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  editorValue[index] = value;
					        		  }else{
					        			  editorValue[index] = "无";
					        		  }
					  		  }},
					          {field:'checkType',title:'检查类型',width:65,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  checkTypeValue[index] = value;
					        		  }else{
					        			  checkTypeValue[index] = "无";
					        		  }
					  		  }},
					          {field:'systemAudit',title:'审核类型',width:65,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  systemAuditValue[index] = value;
					        		  }else{
					        			  systemAuditValue[index] = "无";
					        		  }
					  		  }},
					          {field:'checkerFrom',title:'检查人来自',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  checkerFromValue[index] = value;
					        		  }else{
					        			  checkerFromValue[index] = "无";
					        		  }
					  		  }},
					          {field:'correctType',title:'整改类型',width:65,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value != null && value != ""){
					        			  correctTypeValue[index] = value;
					        		  }else{
					        			  correctTypeValue[index] = "无";
					        		  }
					  		  }},
					          
					          
					          {field:'inconformityItemSn',title:'不符合项编号',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  inconformityItemSnValue[index]=value;
					        		  }else{
					        			  inconformityItemSnValue[index]="无";
					        		  }
					  		  }},
					          {field:'checkers',title:'检查人',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  checkersValue[index]=value;
					        		  }else{
					        			  checkersValue[index]="无";
					        		  }
					  		  }},        
					          {field:'inconformityItemNature',title:'不符合项性质',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  inconformityItemNatureValue[index]=value;
					        		  }else{
					        			  inconformityItemNatureValue[index]="无";
					        		  }
					  		  }},
					          {field:'machine',title:'机',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  machineValue[index]=value;
					        		  }else{
					        			  machineValue[index]="无";
					        		  }
					  		  }},
					          {field:'standardIndex',title:'指标',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        		  		standardIndexValue[index]=value;
					        		  }else{
											standardIndexValue[index]="无";
					        		  }
					  		  }},
					          {field:'deductPoints',title:'扣分',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  deductPointsValue[index]=value;
					        		  }else{
					        			  deductPointsValue[index]="无";
					        		  }
					  		  }},
					          {field:'hazrd',title:'对应的危险源',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  hazardValue[index]=value;
					        		  }else{
					        			  hazardValue[index]="无";
					        		  }
					  		  }},
					          {field:'speciality',title:'所属专业',width:100,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  specialityValue[index]=value;
					        		  }else{
					        			  specialityValue[index]="无";
					        		  }
					  		  }},
					          {field:'inconformityLevel',title:'不符合项等级',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  inconformityLevelValue[index]=value;
					        		  }else{
					        			  inconformityLevelValue[index]="无";
					        		  }
					  		  }},
					          {field:'correctPrincipal',title:'整改负责人',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  correctPrincipalValue[index]=value;
					        		  }else{
					        			  correctPrincipalValue[index]="无";
					        		  }
					  		  }},
					          {field:'attachments',title:'附件详情',width:100,align:'center',
					        	  formatter: function(value,row,index){
						        	  //console.log(value);
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
						onLoadSuccess:function(){
							$('[name=btn]').linkbutton({    
							    iconCls: 'icon_user_edit'   
							});
							$('[name=btn]').click(function(){
								$('#win').window({
									title:"审批",
									width:370,
									height:240,
									content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/showByDefer_edit" frameborder="0" width="100%" height="100%" />'
								});
							});
						},
						view: detailview, 
						detailFormatter: function(rowIndex, row){
						return '<table style="border:1"><tr>' + 
						'<td style="width:90px;text-align:center">'+'不符合项编号：' + '</td>' + 
						'<td style="width:200px;text-align:center">' + 
						'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'不符合项性质：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + inconformityItemNatureValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'不符合项等级：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + inconformityLevelValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'专业：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + specialityValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'检查类型：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + checkTypeValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'审核类型：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + systemAuditValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr><tr>' +
						
						'<td style="width:90px;text-align:center">'+'检查人：' + '</td>' + 
						'<td style="width:200px;text-align:center">' + 
						'<p>' + checkersValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'检查人来自：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + checkerFromValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'整改负责人：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + correctPrincipalValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'录入人：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + editorValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'扣分：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + deductPointsValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'机器：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + machineValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr><tr>' +
						
						'<td style="width:90px;text-align:center">'+'危险源：' + '</td>' + 
						'<td style="width:200px;text-align:center"colspan="9">' + 
						'<p>' + hazardValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'整改类型：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + correctTypeValue[rowIndex] + '</p>' + 
						'</td>' +
						
						'</tr><tr>' +
						
						'<td style="width:90px;text-align:center">'+'指标：' + '</td>' + 
						'<td style="width:100px;text-align:center" colspan="9">' + 
						'<p>' + standardIndexValue[rowIndex] + '</p>' + 
						'</td>' +
						'<td style="width:90px;text-align:center">'+'申请时间：' + '</td>' + 
						'<td style="width:100px;text-align:center">' + 
						'<p>' + applyDateTimeValue[rowIndex] + '</p>' + 
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