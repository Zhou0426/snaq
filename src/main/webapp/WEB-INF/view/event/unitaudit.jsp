<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未遂事件单位审核</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>	
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
</head>

<body>
	<!-- 未遂事件数据列表 -->
	<table id="dg"></table>
	<!-- 审核窗口 -->
	<div id="win" class="easyui-window" title="审核窗口 " closed="true" style="width: 400px; height: 300px; padding: 5px;">
		<br/>
		<form id="form" method="post">
		<div  style="margin-left: 30px">
			<div style="diapaly: inline">
				<label style="padding-right: 25px">审核信息：</label> 
				<input name="auditInfo" class="easyui-textbox" style="width: 300px; height: 80px" multiline="true" /> 					
			</div><br/><br />
			<div id="dlg-buttons" >
				&emsp;&emsp;<a id="passbtn" class="easyui-linkbutton" iconCls="icon-ok" >通过</a>
				&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<a id="impassbtn" class="easyui-linkbutton" iconCls="icon-no">不通过</a>
			</div>
		</div>
		</form>
	</div>
<script>
//权限管理
var str="${sessionScope['permissions']}";
var riskResultValue=new Array();
var naerMissSnValue=new Array();
var eventProcessValue=new Array();
var reasonAnalysisValue=new Array();
var preventMeasureValue=new Array();
$(function() {
	var nearMissSn;
	<!--数据网络-->
	$('#dg').datagrid({
		pageNumber : 1,
		pagination : true,
		url : "${pageContext.request.contextPath}/attempted/event/showNearMiss?range=单位审核",
		rownumbers : true,
		fitColumns : true,
		fit:true,
		pageSize : 10,
		singleSelect : true,
		height : 350,
		nowrap:false,
		pageList : [ 5, 10, 15, 20 ],
		toolbar : [{
				iconCls : 'icon-add',
				text : '审核',
				id:'audittool',
				handler : function() {
					var str="${sessionScope['permissions']}";
					if(str.indexOf("160301")==-1){
						$("#audittool").css('display','none');
					}else{
							var row = $('#dg').datagrid('getSelected');
							if (row) {
								if(row.nearMissState=='单位审核通过'||row.nearMissState=='单位审核未通过'){
									$.messager.alert('提示','单位已经审核');
								}
								else{
									$('#win').window('open');
									nearMissSn=row.nearMissSn;
								}							
							}
							else {
								$.messager.alert('提示','请先选择要审核的行');
							}
						}
					}
				}],
				columns : [ [ {
					field : 'nearMissSn',
					title : '未遂事件编号',
					width : 1,hidden:true,
			    	formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  naerMissSnValue[index]=value;
		        		  }else{
		        			  naerMissSnValue[index]="无";
		        		  }
		  		  	  }	
				},  {
					field : 'parentDepartmentName',
					title : '发生单位',
					width : 100
				},{
					field : 'departmentName',
					title : '发生区队',
					width : 100
				}, {
					field : 'nearMissTypeName',
					title : '未遂事件类型',
					width : 100
				}, {
					field : 'eventName',
					title : '事件名称',
					width : 120
				}, {
					field : 'happenLocation',
					title : '发生地点',
					width :100
				}, {
					field : 'happenDate',
					title : '发生日期',
					width : 100
				} , {
					field : 'nearMissTypeSn',
					title : '未遂事件类型编号',
					hidden: true,
					width : 100
				}, {
					field : 'nearMissState',
					title : '未遂事件状态',
					width : 80
				},{
					field : 'preventMeasure',
					title : '防范措施',
					width : 100,hidden:true,
			    	formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  preventMeasureValue[index]=value;
		        		  }else{
		        			  preventMeasureValue[index]="无";
		        		  }
		  		  	  }	
				}, {
					field : 'reasonAnalysis',
					title : '原因分析',
					width : 100,hidden:true,
			    	formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  reasonAnalysisValue[index]=value;
		        		  }else{
		        			  reasonAnalysisValue[index]="无";
		        		  }
		  		  	  }	
				}, {
					field : 'reportPersonName',
					title : '上报人',
					width : 70
				}, {
					field : 'riskLevel',
					title : '风险等级',
					width : 70
				}, {
					field : 'riskResult',
					title : '风险后果',
					width : 100,hidden:true,
			    	formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  riskResultValue[index]=value;
		        		  }else{
		        			  riskResultValue[index]="无";
		        		  }
		  		  	  }	
				}, {
					field : 'reportTime',
					title : '上报时间',
					width : 100
				}, {
					field : 'eventProcess',
					title : '事件过程',
					width : 100,hidden:true,
			    	formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  eventProcessValue[index]=value;
		        		  }else{
		        			  eventProcessValue[index]="无";
		        		  }
		  		  	  }	
				}] ],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
					return '<table style="border:1"><tr>' + 								
					'<td style="width:100px;text-align:center">'+'未遂事件编号：' + '</td>' + 
					'<td style="width:700px;text-align:left">' + 
					'<div style="padding:10px">' + naerMissSnValue[rowIndex] + '</div>' + 
					'</td>' +
					'</tr><tr>' +
					

					'<td style="width:100px;text-align:center">'+'事件过程：' + '</td>' + 
					'<td style="width:700px;text-align:left">' + 
					'<div style="padding:10px">' + eventProcessValue[rowIndex] + '</div>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:100px;text-align:center">'+'预防措施：' + '</td>' + 
					'<td style="width:700px;text-align:left" >' + 
					'<div style="padding:10px">' + preventMeasureValue[rowIndex] + '</div>' + 
					'</td>' +
					'</tr><tr>' +
					

					'<td style="width:100px;text-align:center">'+'风险后果：' + '</td>' + 
					'<td style="width:700px;text-align:left">' + 
					'<div style="padding:10px">' + riskResultValue[rowIndex] + '</div>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:100px;text-align:center">'+'原因分析：' + '</td>' + 
					'<td style="width:700px;text-align:left" colspan="3">' + 
					'<div style="padding:10px">' + reasonAnalysisValue[rowIndex] + '</div>' + 
					'</td>' +
					'</tr></table>'; 
				}
	});
	$('#passbtn').bind('click', function(){    
		$('#form').form('submit', {
			url:"${pageContext.request.contextPath}/attempted/event/changeStateNearMiss?range=单位审核通过&nearMissSn="+nearMissSn,
			success: function(message){
				$.messager.alert('提示',message);
				$('#dg').datagrid('reload');
			}
		})  
    });
	$('#impassbtn').bind('click', function(){
		$('#form').form('submit', {
			url:"${pageContext.request.contextPath}/attempted/event/changeStateNearMiss?range=单位审核未通过&nearMissSn="+nearMissSn,
			success: function(message){
				$.messager.alert('提示',message); 
				$('#dg').datagrid('reload');
			}
		})  
    });
	if(str.indexOf('160301')==-1){
		$('#audittool').css('display','none');
	}
	$('#audittool').linkbutton({
		plain:false
	});
});		
	
</script>
</body>
</html>