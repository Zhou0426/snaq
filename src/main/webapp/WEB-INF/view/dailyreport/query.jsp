<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>安全日报</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/pdfobject.js"></script>
<style>
	.pdfobject-container { width:100%;height: 900px;}
	.pdfobject { border: 1px solid #666; }
</style>
</head>
<body style="padding:1px;"> 
	<table id="dg" class="easyui-datagrid"></table>
 	<div id="toolbar">
 		<label>&emsp;部门类型：</label><input class="easyui-combobox" id="departmentType" name='departmentType'/>
 		<label>&emsp;&nbsp;开始日期：</label><input class="easyui-datebox" data-options="sharedCalendar:'#date'" id="start_date" />
 		<label>&emsp;结束日期：</label><input class="easyui-datebox" data-options="sharedCalendar:'#date'" id="end_date" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchReport()">查询</a>
    </div>
    <!-- 日历插件 -->
    <div id="date" class="easyui-calendar"></div>
    <div id="pdf"></div>
	<script type="text/javascript">
		var massage = "";
		var sw = false;
		if(${sessionScope['checkTask']} != 0){
			massage = massage + "您有" + ${sessionScope['checkTask']} + "个检查任务<br />";
			sw = true;
		}
		if(${sessionScope['myCheckTable']} != 0){
			massage = massage + "您有" + ${sessionScope['myCheckTable']} + "个检查表<br />";
			sw = true;
		}
		if(${sessionScope['correctCount']} != 0){
			massage = massage + "您有" + ${sessionScope['correctCount']} + "个整改事项<br />";
			sw = true;
		}
		if(${sessionScope['DeferThing']} != 0){
			massage = massage + "您有" + ${sessionScope['DeferThing']} + "个审批事项<br />";
			sw = true;
		}
		if(${sessionScope['reviewCount']} != 0){
			massage = massage + "您有" + ${sessionScope['reviewCount']} + "个复查事项<br />";
			sw = true;
		}
		if(sw){
			massage = massage + "请尽快处理!";
			$.messager.show({
				title:'消息提示',
				msg:massage,
				height:130,
				timeout:0,
				showType:'slide'
			});	
		}
		$('#dg').datagrid({
			url:'dailyreport/superviseDailyReportAction_search.action',
 			fit:true,
			toolbar:'#toolbar',
			rownumbers: true,
			fitColumns: true,
			singleSelect: true,
			pagination:true,
			pageList: [20,40,80,150],
			pageNumber:1, 
			pageSize:20,
			columns:[[  
			          {field:'reportDate',title:'安全日报日期',width:'25%',align:'center'},  
			          {field:'department',title:'安全日报类型',width:'25%',align:'center'},  
			          {field:'pdf',title:'安全日报在线预览',hidden:true,width:'34%',align:'center',formatter:function(value,row,index){
			        		return "<a href='#' onClick=seeOnline('"+index+"') style='text-decoration:none'>在线预览</a>";
			          }},
			          {field:'fileName',title:'安全日报文件下载',width:'48%',align:'center',formatter:function(value,row,index){
			        	  return "<a href='dailyreport/superviseDailyReportAction_download?excelFile="+value+"&&departmentTypeSn="+row.departmentTypeSn+"&&reportDate="+row.reportDate+"' style='text-decoration:none'>下载</a>";
			          }}
			]]
		});		
		
		$('#departmentType').combobox({
			url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			editable:false,
			prompt:'单位类型'
		})
		
		
	    function searchReport(){
 	    	var startDate = $('#start_date').datebox('getValue');
	    	var endDate = $('#end_date').datebox('getValue');
	    	var departmentTypeSn = $('#departmentType').combobox('getValue');
	    	if(startDate!=null || endDate!=null || departmentTypeSn!=null){
		    	$('#dg').datagrid('load',{
		    		startDate:startDate,
		    		endDate:endDate,
		    		departmentTypeSn:departmentTypeSn
		    	});
			}else{
				alert("查询条件为空！"); 
			} 
	    }
 		
 		function seeOnline(index){
 			$('#dg').datagrid('selectRow',index);
 			var row=$('#dg').datagrid('getSelected');
 			if(row!=null){
 				$.ajax({
 	 				type:'post',
 	 				url:'dailyreport/superviseDailyReportAction_seeOnline.action',
 	 				data:{excelFileName:row.pdf,reportDate:row.reportDate,departmentTypeSn:row.departmentTypeSn},
 	 				success:function(data){
 	 					window.open("dailyreport/openPDF?realUrl="+data.path, '_blank ');
 	 				}
 	 			}); 
 			}
  			
 		}

	</script>
</body>
</html>