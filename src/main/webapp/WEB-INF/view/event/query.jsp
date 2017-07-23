<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未遂事件查询</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
	<input id="treehidden" type="hidden" >
	<input id="nearMissStatehidden" type="hidden">
	<input id="nearMissAuditTypehidden" type="hidden">
	<input id="riskLevelhidden" type="hidden">
	<input id="begintimehidden" type="hidden">
	<input id="endtimehidden" type="hidden">
	<table id="dg"></table>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
	<script> 
	//全局变量
	var departmentSn = null;
	var nearMissState = null;
	var nearMissAuditType = null;
	var riskLevel = null;
	var date = new Date();
	var endtime = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	date.setDate(1);
	var begintime = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	var reasonAnalysisValue = new Array();
	var preventMeasureValue = new Array();
	var eventProcessValue = new Array();	
	var riskResultValue = new Array();
	
	$(function () {
		<!--数据网络-->
		$('#dg').datagrid({
			pageNumber: 1,
			pagination:true,
	        rownumbers:true,
	        fitColumns:true,
	        pageSize:10,
	        fit:true,
	        singleSelect:true,
	        nowrap:false,
	        pageList: [5,10,15,20],
	        toolbar:[
			{
				text:'<div style="diapaly: inline;margin:5px">部门：<input id="treeselect" name="treehidden"></div>'
			},
			{
				text:'<div style="diapaly: inline;margin:5px">审核状态：<input id="nearMissStateselect" name="nearMissStatehidden"></div>'
			},
			{
				text:'<div style="diapaly: inline;margin:5px">风险等级：<input id="riskLevelselect" name="riskLevelhidden"></div>'
			},
			{
				text:'<div style="diapaly: inline;margin:5px">开始时间：<input id="begintimeselect" name="begintimehidden"></div>'
			},
			{
				text:'<div style="diapaly: inline;margin:5px">结束时间：<input id="endtimeselect" name="endtimehidden"></div>'
			}],
			columns : [ [ {
				field : 'nearMissSn',
				title : '未遂事件编号',
				hidden:true
			
			}, {
				field : 'implDepartmentName',
				title : '贯标单位',
				width : 100
			}, {
				field : 'departmentName',
				title : '所在部门',
				width : 100
			}, {
				field : 'eventName',
				title : '事件名称',
				width : 100
			}, {
				field : 'nearMissState',
				title : '未遂事件状态',
				width : 80
			}, {
				field : 'happenLocation',
				title : '发生地点',
				width : 100
			}, {
				field : 'nearMissTypeName',
				title : '未遂事件类型',
				width : 80
			}, 
			{field:'preventMeasure',width:200,title:'防范措施',hidden:true,
	        	 formatter: function(value,row,index){
	        		  if(value!=null && value!=""){
	        			  preventMeasureValue[index]=value;
	        		  }else{
	        			  preventMeasureValue[index]="无";
	        		  }
	  		  	}
			},
			{field:'reasonAnalysis',width:200,title:'原因分析',hidden:true,
	        	 formatter: function(value,row,index){
	        		  if(value!=null && value!=""){
	        			  reasonAnalysisValue[index]=value;
	        		  }else{
	        			  reasonAnalysisValue[index]="无";
	        		  }
	  		  	}
			},{
				field : 'reportPerson',
				title : '上报人',
				width : 50
			}, {
				field : 'riskLevel',
				title : '风险等级',
				width : 50
			}, {
				field : 'riskResult',
				title : '风险后果',
				width : 200,hidden:true,
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
				width : 70
			}, 
			{field : 'eventProcess',title : '事件过程',width : 100,hidden:true,
				formatter: function(value,row,index){
	        		  if(value!=null && value!=""){
	        			  eventProcessValue[index]=value;
	        		  }else{
	        			  eventProcessValue[index]="无";
	        		  }
	  		  	}
			}, 
			{
				field : 'happenDate',
				title : '发生日期',
				width : 70
			},
			] ],
// 			view: detailview, 
// 			detailFormatter: function(rowIndex, row){
// 				return '<table style="border:1"><tr>' + 								
// 				'<td style="width:100px;text-align:center">'+'预防措施：' + '</td>' + 
// 				'<td style="width:140px;text-align:center">' + 
// 				'<p>' + preventMeasureValue[rowIndex] + '</p>' + 
// 				'</td>' +
// 				'<td style="width:100px;text-align:center">'+'原因分析：' + '</td>' + 
// 				'<td style="width:140px;text-align:center">' + 
// 				'<p>' + reasonAnalysisValue[rowIndex] + '</p>' + 
// 				'</td>' +
// 				'<td style="width:100px;text-align:center">'+'事件过程：' + '</td>' + 
// 				'<td style="width:140px;text-align:center">' + 
// 				'<p>' + eventProcessValue[rowIndex] + '</p>' + 
// 				'</td>' +
// 				'</tr></table>'; 
// 			}
			view: detailview, 
			detailFormatter: function(rowIndex, row){
				return '<table style="border:1"><tr>' +  	
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
				'<td style="width:700px;text-align:left">' + 
				'<div style="padding:10px">' + reasonAnalysisValue[rowIndex] + '</div>' + 
				'</td>' +
				'</tr></table>'; 
			}
		});
	    <!--加载当前角色datagrid里的部门表树形菜单-->
	    $('#treeselect').combotree({
		    url: '${pageContext.request.contextPath}/department/tree',
			panelWidth: 300,
			panelHeight:350,
		    onLoadSuccess:function(node,data){
		    	var selectValue = $('#treeselect').combotree('getValue');
		    	if( selectValue == null || selectValue.length == 0 ){
					var da=$("#treeselect").combotree('tree').tree("getRoot");	
			    	$('#treeselect').combotree('setValue', da);
			    	var node = $("#treeselect").combotree('tree').tree('find', da.id);
			    	$("#treeselect").combotree('tree').tree('select', node.target);
			    	
			    	if(data.length == 1){
		        		$('#treeselect').combotree('tree').tree('expand', node.target);
		        	}
		    	}
		    	
		    },
		    onSelect: function(node){
				departmentSn=node.id;
				querynearmiss();
			}
		});  
	    //审核状态
	    $('#nearMissStateselect').combobox({    
	        url:'${pageContext.request.contextPath}/json/near_miss_state.json',    
	        valueField:'id',    
	        textField:'text',
	        editable:false,
			panelHeight:'auto',
	        onLoadSuccess:function(){
	        	$('#nearMissStateselect').combobox('setValue', '全部');
		    },
	        onSelect: function(){
	        	nearMissState = $('#nearMissStateselect').combobox('getValue');
	        	if(nearMissState=='-1'){
	        		nearMissState=null;
	        	}
	        	else{
	        	 nearMissState=parseInt(nearMissState);
	        	}
				querynearmiss();
			}
 	    });  
	    //风险等级
	    $('#riskLevelselect').combobox({    
	        url:'${pageContext.request.contextPath}/json/risk_level.json',    
	        valueField:'id',    
	        textField:'text',
	        editable:false,
			panelHeight:'auto',
	        onLoadSuccess:function(){
	        	$('#riskLevelselect').combobox('setValue', '全部');
		    },
	        onSelect: function(){
	        	riskLevel=$('#riskLevelselect').combobox('getValue');
	        	if(riskLevel=='-1'){
	        		riskLevel=null;
	        	}
	        	else{
	        		riskLevel=parseInt(riskLevel);
	        	}
				querynearmiss();
			}
	    });
	    //开始时间
	    $('#begintimeselect').datebox({
	    	//value:begintime,
	        editable:false,
	    	onSelect: function(date){
	    		begintime=$('#begintimeselect').datebox('getValue');
	    		querynearmiss();
	    	}
	    }); 
	    //结束时间
	    $('#endtimeselect').datebox({ 
	    	//value:endtime,
	        editable:false,
	    	onSelect: function(date){
	    		endtime=$('#endtimeselect').datebox('getValue');
	    		querynearmiss();
	    	}
	    });
	    $('#begintimeselect').datebox('setValue',begintime);
	    $('#endtimeselect').datebox('setValue',endtime);
	});
	//查询函数
	function querynearmiss(){
		$('#dg').datagrid('options').url="${pageContext.request.contextPath}/attempted/event/queryNearMiss";
		$('#dg').datagrid('load',{
			departmentSn:departmentSn ,
			nearMissStateSn: nearMissState,
			nearMissAuditTypeSn:nearMissAuditType,
			riskLevelSn:riskLevel,
			begintime:begintime,
			endtime:endtime
		});
	}
	
	
	</script>
</body>
</html>