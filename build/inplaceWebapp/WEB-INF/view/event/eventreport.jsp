<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未遂事件统计表格</title>
<!-- 引进bootstrap所需文件 -->
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" rel="stylesheet"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>

<style type="text/css">
		.active{background:#4FB0AC; color:#000;}
</style>

	
<script type="text/javascript">
// function getUrlParam(name) {
//     var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
//     var r = window.location.search.substr(1).match(reg);  //匹配目标参数
//     if (r != null) return unescape(r[2]); return null; //返回参数值
// }
var date=new Date();
endtime=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate();
begintime=date.getFullYear()+"-0"+(date.getMonth())+"-"+date.getDate();
var departmentSn=null;
var departmentTypeSn=null;
var nearMissTypeSn=null;
var riskLevelSn=null;
var riskResultValue=new Array();
var naerMissSnValue=new Array();
var eventProcessValue=new Array();
var reasonAnalysisValue=new Array();
var preventMeasureValue=new Array();
var hasType='ture';
$(function(){
	//弹出的datagrid	    
    $('#dg').datagrid({
		pageNumber: 1,
		pagination:true,
        url: "${pageContext.request.contextPath}/attempted/event/queryNearMiss",
        rownumbers:true,
        fitColumns:true,
        pageSize:10,
        singleSelect:true,
        nowrap:false,
        fit:true,
        pageList: [5, 10,15,20],
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
		}, {
			field : 'implDepartmentName',
			title : '贯标单位',
			width : 100
		}, {
			field : 'parentDepartmentName',
			title : '发生单位',
			width : 100
		},{
			field : 'departmentName',
			title : '发生区队',
			width : 100
		}, {
			field : 'eventName',
			title : '事件名称',
			width : 100
		}, {
			field : 'nearMissState',
			title : '未遂事件状态',
			width : 100
		}, {
			field : 'happenLocation',
			title : '发生地点',
			width :100
		}, {
			field : 'nearMissTypeName',
			title : '未遂事件类型',
			width : 100
		}, {
			field : 'nearMissTypeSn',
			title : '未遂事件类型编号',
			width : 100,
			hidden:true
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
			width : 100
		}, {
			field : 'riskLevel',
			title : '风险等级',
			width : 100
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
		}, {
			field : 'happenDate',
			title : '发生日期',
			width : 100
		} ] ],
		view: detailview, 
		detailFormatter: function(rowIndex, row){
			return '<table style="border:1"><tr>' + 								
			'<td style="width:100px;text-align:center">'+'未遂事件编号：' + '</td>' + 
			'<td style="width:700px;text-align:center">' + 
			'<p>' + naerMissSnValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			

			'<td style="width:100px;text-align:center">'+'事件过程：' + '</td>' + 
			'<td style="width:700px;text-align:center">' + 
			'<p>' + eventProcessValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			
			'<td style="width:100px;text-align:center">'+'预防措施：' + '</td>' + 
			'<td style="width:700px;text-align:center" >' + 
			'<p>' + preventMeasureValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			

			'<td style="width:100px;text-align:center">'+'风险后果：' + '</td>' + 
			'<td style="width:700px;text-align:center">' + 
			'<p>' + riskResultValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr><tr>' +
			
			'<td style="width:100px;text-align:center">'+'原因分析：' + '</td>' + 
			'<td style="width:700px;text-align:center" colspan="3">' + 
			'<p>' + reasonAnalysisValue[rowIndex] + '</p>' + 
			'</td>' +
			'</tr></table>'; 
		}
   });   

    //开始时间
    $('#begintimeselect').datebox({    
    	value:begintime,
    	width:180,
	    height:27,
        editable:false,
    	onSelect: function(date){
    		begintime=$('#begintimeselect').datebox('getValue');
    		loadtable();
    	}
    }); 
    //结束时间
    $('#endtimeselect').datebox({ 
    	value:endtime,
    	width:180,
	    height:27,
        editable:false,
    	onSelect: function(date){
    		endtime=$('#endtimeselect').datebox('getValue');
    		loadtable();
    	}
    }); 
// 	$("#table a").click(function(){
// 		departmentSn=$(this).attr("data-departmentSn");
// 		riskLevelSn=$(this).attr("data-riskLevelSn");
// 		nearMissTypeSn=$(this).attr("data-nearMissTypeSn");
// 		$('#dg').datagrid('load',{
// 			departmentSn:departmentSn ,
// 			riskLevelSn:riskLevelSn,
// 			nearMissTypeSn:nearMissTypeSn,
// 			begintime:begintime,
// 			endtime:endtime
// 		});
// 		$('#win').window('open');
// 	});	
	<!--加载部门树形菜单-->
    $('#cc').combotree({
	    url: '${pageContext.request.contextPath}/department/tree',
	    height:27,
		panelWidth: 300,
		panelHeight:350,
	    onLoadSuccess: function (node,data) {
	       var x = $('#cc').combotree('getValue');		       
           if(x.length == 0){  
                var da=$("#cc").combotree('tree').tree("getRoot");	
		    	$('#cc').combotree('setValue', da);		    	
		    	var node = $("#cc").combotree('tree').tree('find', da.id);
		    	$("#cc").combotree('tree').tree('select', node.target);
		    	//nodeSelect();	
		    	if(data.length == 1){
	        		$('#cc').combotree('tree').tree('expand', node.target);
	        	}
           }        
		},
		//选择时执行选择方法动态生成部门菜单，给部门编号和部门类型编号赋值并发送Action
		onSelect: nodeSelect
	});    
    function nodeSelect(){	  
    	//选择部门
		var t = $('#cc').combotree('tree');
		var node = t.tree('getSelected'); 
		//默认将根节点编号赋值给部门编号
		departmentSn=node.id;
		$.post("${pageContext.request.contextPath}/report/typeReport",{departmentSn:departmentSn},function(rv){
			$("#typebtns").empty();
			departmentTypeSn=null;
			if(rv.length>0){					
				$("#typebtns").append("<span style='float:left;margin-top:5px'>"+"部门类型："+"</span>");
			}else{
				hasType='false';
			}
			for (var i = 0;i < rv.length;i++){
// 				if(i==0){
// 					$("#typebtns").append("<a class='active btn btn-default' data-options='plain:true' style='margin-left:5px;margin-right:15px;' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
// 					//默认将第一个部门类型按钮的值赋给部门类型
// 					departmentTypeSn=rv[i].value;
// 				}
// 				else{
				$("#typebtns").append("<a class='btn btn-default' data-options='plain:true' style='margin-left:5px;margin-right:15px;' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
// 				}	
			}
			$.parser.parse($('#typebtns').parent());
			loadtable();
			<!--部门类型按钮点击 -->
			$("#typebtns a").click(function(){
				$("#typebtns a").removeClass("active");
			    $(this).addClass("active");
				departmentTypeSn=$(this).attr("data");
				loadtable();
			});				
		},'json'); 
	};
	$('#win').window({
		title:'统计明细',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		maximized:true
	});
});
function loadtable(){
	$.post("${pageContext.request.contextPath}/attempted/event/reportNearMiss",{departmentSn:departmentSn,departmentTypeSn:departmentTypeSn,begintime:begintime,endtime:endtime,hasType:hasType},function(str){
		$("#table").empty();
		hasType="true";
		$("#table").append("<tr><td>序号</td><td>单位</td><td style='text-align:center;' colspan="+str.array[0].types.length+">事件类别</td><td style='text-align:center;' colspan='3'>风险等级</td></tr>");
		$("#table").append("<tr>");
		$("#table").append("<td colspan='2'></td>");
		for(var i=0;i<str.array[0].types.length;i++){
			$("#table").append("<td>"+str.array[0].types[i].typeName+"</td>");
		}
		$("#table").append("<td>一般风险</td><td>中等风险</td><td>严重风险</td>");
		$("#table").append("</tr>");
		for(var i=0;i<str.array.length;i++){
			$("#table").append("<tr>");
			$("#table").append("<td>"+i+"</td>");
			$("#table").append("<td>"+str.array[i].deptName+"</td>");
			for(var j=0;j<str.array[i].types.length;j++){
				var other=null;
				//$("#table").append("<td>"+str.array[i].types[j].typeCount+"</td>");
				$("#table").append("<td><a  href='#'  onClick='de("+str.array[i].deptSn+","+str.array[i].types[j].typeSn+","+other+")'>"+str.array[i].types[j].typeCount +"</a></td>");
			}
			for(var g=0;g<str.array[i].levels.length;g++){
				//$("#table").append("<td>"+str.array[i].levels[g].levelCount+"</td>");	
				var other=null;
				$("#table").append("<td><a  href='#' onClick='de("+str.array[i].deptSn+","+other+","+str.array[i].levels[g].levelSn+")'>"+str.array[i].levels[g].levelCount+"</a></td>");

			}
			$("#table").append("</tr>");
		}
	},'json');
}
function de(x,y,z){
	$('#dg').datagrid('load',{
		departmentSn:x ,
		riskLevelSn:z,
		nearMissTypeSn:y,
		begintime:begintime,
		endtime:endtime,
		departmentTypeSn:departmentTypeSn
	});
	$('#win').window('open');
}
//点击导出
function exportexcel(){ 
	var url ="${pageContext.request.contextPath}/attempted/event/exportexcelNearMiss?begintime="+begintime+"&endtime="+endtime+"&departmentSn="+departmentSn+"&departmentTypeSn="+departmentTypeSn+"&hasType="+hasType;
	$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
	hasType="true";
};
</script>
</head>
<body>	
	<div id="win" class="easyui-window" title="未遂事件明细" style="padding:5px;">		
		<table id="dg"></table>
	</div>	
	<div id="button">
		<div style="float:left;margin-top:9px;"><span style="float:left;">&emsp;选择范围：&emsp;</span><input id="cc" /></div>
		<div style="float:left;margin-left:10px;margin-top:9px;" class="btn-group btn-group-sm" id="typebtns"></div>
		<div style="float:left;margin-top:9px;"><span style="float:left;">&emsp;起始时间:&emsp;</span><select id="begintimeselect" class= "easyui-datebox"></select></div>
		<div style="float:left;margin-left:10px;margin-top:9px;"><span style="float:left;">&emsp;结束时间:&emsp;</span><select id="endtimeselect" class= "easyui-datebox"></select></div>
		<div class="btn-group btn-group-sm" style="float:left;margin-top:9px;">
			<a id="exportexcel" onclick="exportexcel()" class="btn btn-default">导出</a>
		</div>
	</div>
	<div style="float:left">
	&emsp;&emsp;<span class="label label-default">点击统计数字可查看详情:</span><br/>
	<table  class="table table-striped" border="1" ><tbody id="table"></tbody></table>
	</div>
</body>
</html>