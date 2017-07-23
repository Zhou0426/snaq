<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>神华宁煤安全风险预控管理信息系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
</head>
<body>
	<input id="treehidden" type="hidden" >
	<input id="accidentLevelhidden" type="hidden">
	<input id="begintimehidden" type="hidden">
	<input id="endtimehidden" type="hidden">
	<table id="dg"></table>
	<script> 
	//全局变量
	var departmentSn=null;
	var accidentLevel=null;
	var date=new Date();
	var endtime=date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	date.setDate(1);
	var begintime=date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	var accidentSnValue=new Array();
	var accidentProcessValue=new Array();
	var directReasonValue=new Array();
	var indirectReasonValue=new Array();
	var precautionMeasureValue=new Array();
	$(function () {
		<!--数据网络-->
		$('#dg').datagrid({
			pageNumber: 1,
			pagination:true,
	       // url: "${pageContext.request.contextPath}/accident/queryAccident",
	        rownumbers:true,
	        fitColumns:true,
	        fit:true,
	        pageSize:10,
	        singleSelect:true,
	        pageList: [5, 10,15,20],
	        nowrap:false,
	        toolbar:[
			{
				text:'事故部门：<input id="treeselect" name="treehidden">'
			},			
			{
				text:'事故等级：<input id="accidentLevelselect" name="accidentLevelhidden">'
			},
			{
				text:'开始时间：<input id="begintimeselect" name="begintimehidden">'
			},
			{
				text:'结束时间：<input id="endtimeselect" name="endtimehidden">'
			}],
			columns : [ [ 
	             {field : 'accidentSn',title : '事故编号',width : 150,hidden:true,
				    	formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  accidentSnValue[index]=value;
			        		  }else{
			        			  accidentSnValue[index]="无";
			        		  }
			  		  	  }	
				    }, 
				    {field : 'accidentName',title : '事故名称',width : 150},
				    {field : 'happenLocation',title : '发生地点',width : 150},
				    {field : 'departmentName',title : '发生部门',width : 150}, 
				    {field : 'accidentProcess',title : '发生过程',width : 150,hidden:true,
				    	formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  accidentProcessValue[index]=value;
			        		  }else{
			        			  accidentProcessValue[index]="无";
			        		  }
			  		  	  }	
				    }, 
					{field : 'reasonArticle',title : '致因物',width : 150}, 
					{field : 'directReason',title : '直接原因',width : 150,hidden:true,
				    	formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  directReasonValue[index]=value;
			        		  }else{
			        			  directReasonValue[index]="无";
			        		  }
			  		  	  }	},
					{field : 'indirectReason',title : '间接原因',width : 150,hidden:true,
						    	formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  indirectReasonValue[index]=value;
					        		  }else{
					        			  indirectReasonValue[index]="无";
					        		  }
					  		  	  }	}, 
					{field : 'precautionMeasure',title : '预防措施',width : 100,hidden:true,
								    	formatter: function(value,row,index){
							        		  if(value!=null && value!=""){
							        			  precautionMeasureValue[index]=value;
							        		  }else{
							        			  precautionMeasureValue[index]="无";
							        		  }
							  		  	  }	},
					{field : 'directEconomicLoss',title : '直接经济损失',width : 100},
					{field : 'indirectEconomicLoss',title : '间接经济损失',width : 100},
					{field : 'deadCount',title : '死亡人数',width : 70},
					{field : 'seriousInjureCount',title : '重伤人数',width : 70},
					{field : 'fleshInjureCount',title : '轻伤人数',width : 70}, 
					{field : 'happenDateTime',title : '发生时间',width : 80}, 
					{field : 'accidentType',title : '事故类型',width : 80},
					{field : 'accidentLevel',title : '事故等级',width : 80},
					{field : 'editor',title : '编辑人',width :70} 
			] ],
			view: detailview, 
			detailFormatter: function(rowIndex, row){
				return '<table style="border:1"><tr>' + 								
				'<td style="width:100px;text-align:center">'+'事故编号：' + '</td>' + 
				'<td style="width:700px;text-align:center">' + 
				'<p>' + accidentSnValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' +
				

				'<td style="width:100px;text-align:center">'+'发生过程：' + '</td>' + 
				'<td style="width:700px;text-align:center">' + 
				'<p>' + accidentProcessValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' +
				
				'<td style="width:100px;text-align:center">'+'直接原因：' + '</td>' + 
				'<td style="width:700px;text-align:center" >' + 
				'<p>' + directReasonValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' +
				

				'<td style="width:100px;text-align:center">'+'间接原因：' + '</td>' + 
				'<td style="width:700px;text-align:center">' + 
				'<p>' + indirectReasonValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr><tr>' +
				
				'<td style="width:100px;text-align:center">'+'预防措施：' + '</td>' + 
				'<td style="width:700px;text-align:center" colspan="3">' + 
				'<p>' + precautionMeasureValue[rowIndex] + '</p>' + 
				'</td>' +
				'</tr></table>'; 
			}
		
		});
		var first = 1;
	    <!--加载当前角色datagrid里的部门表树形菜单-->
	    $('#treeselect').combotree({
		    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1502',
			panelWidth: 300,
			panelHeight:350,
		    onLoadSuccess:function(node,data){
		    	if( first == 1 ){
					var da=$("#treeselect").combotree('tree').tree("getRoot");	
			    	$('#treeselect').combotree('setValue', da);
			    	var node = $("#treeselect").combotree('tree').tree('find', da.id);
			    	$("#treeselect").combotree('tree').tree('select', node.target);
	
			    	if(data.length == 1){
		        		$('#treeselect').combotree('tree').tree('expand', node.target);
		        	}
			    	first++;
		    	}
		    },
		    onSelect: function(node){
				departmentSn=node.id;
				queryaccident();
			}
		});   
	    //事故等级
	    $('#accidentLevelselect').combobox({    
	        url:'${pageContext.request.contextPath}/accident/levelAccident',    
	        valueField:'accidentLevelSn',    
	        textField:'accidentLevelName',
	        editable:false,
	        onSelect: function(){
	        	accidentLevel=$('#accidentLevelselect').combobox('getValue');
	        	queryaccident();
			}
	    });
	    //开始时间
	    $('#begintimeselect').datetimebox({    
	    	value:begintime,
	        editable:false,
	    	onChange: function(){
	    		begintime=$('#begintimeselect').datetimebox('getValue');
	    		queryaccident();
	    	}
	    }); 
	    //结束时间
	    $('#endtimeselect').datetimebox({ 
	    	value:endtime,
	        editable:false,
	    	onChange: function(){
	    		endtime=$('#endtimeselect').datetimebox('getValue');
	    		queryaccident();
	    	}
	    }); 
	});
	//查询函数
	function queryaccident(){
		$('#dg').datagrid('options').url='${pageContext.request.contextPath}/accident/queryAccident';
		$('#dg').datagrid('load',{
			departmentSn:departmentSn ,
			begintime:begintime,
			endtime:endtime,
			accidentLevelSn:accidentLevel
		});
	}
	
	
	</script>
</body>
</html>