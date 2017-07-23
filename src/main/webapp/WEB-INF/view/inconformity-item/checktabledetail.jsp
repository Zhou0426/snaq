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
<script type="text/javascript">
	function unsafecondition(index){
		$('#dg').datagrid('clearSelections');
		var type=parent.$('#details').val();
		if(type=="my"){
			parent.$('#win').dialog('maximize');
		}else{
			parent.$('#child-win').dialog('maximize');
		}
		$('#win').window({
			width:900,
			height:400,
			title:'不符合项',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/checktable_unsafecondition" frameborder="0" width="100%" height="100%"/>'
		});
	}
	$(function(){
		var type=parent.$('#details').val();
		var specialCheckSn="";
		var periodicalCheckSn="";
		var auditSn="";
		if(type=="my"){
			var row=parent.$('#dg').datagrid('getSelections');
			var id=row[0].id;
			if(row[0].checkType=="定期检查"){
				periodicalCheckSn=row[0].periodicalCheckSn;
			}else if(row[0].checkType=="专项检查"){
				specialCheckSn=row[0].specialCheckSn;
			}else{
				auditSn=row[0].auditSn;
			}
		}else{
			var row=parent.$("iframe").get(0).contentWindow.$('#dg').datagrid('getSelected');
			var id=row.id;
			var type1=parent.$('#type').val();
			if(type1=="periodical"){
				var rows=parent.$('#dg').datagrid('getSelections');
				periodicalCheckSn=rows[0].periodicalCheckSn;
			}else if(type1=="季度审核" || type1=="单位审核"){
				var rows=parent.$('#dg').datagrid('getChecked');
				auditSn=rows[0].auditSn;
			}else{
				var rows=parent.$('#dg').datagrid('getSelections');
				specialCheckSn=rows[0].specialCheckSn;
			}
		}
		
		$("#dg").datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'id':id,'periodicalCheckSn':periodicalCheckSn,'specialCheckSn':specialCheckSn,'auditSn':auditSn},//请求远程数据发送额外的参数
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:false,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:20,
			pageList:[20,50,100,200],
			/*按钮*/
			toolbar:[{
				text:'导出为excel',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('确认对话框', '您想要导出这些数据吗？', function(r){
						if (r){
							var titles="指标编号,指标名称,审核方法,专业编号,专业名称,百分制分数,是否关键指标,单次扣分,几次扣完,父级编号,共享指标";
							//向后台发送请求
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/standard/standardindexAction_export.action");
							//添加input
												
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","title");
							input1.attr("value","检查表");
							
							
							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","titles");
							input2.attr("value",titles);
							
							var input3=$("<input>");
							input3.attr("type","hidden");
							input3.attr("name","templateSn");
							input3.attr("value","standardindex");
							
							var input4=$("<input>");
							input4.attr("type","hidden");
							input4.attr("name","id");
							input4.attr("value",id);

							var input5=$("<input>");
							input5.attr("type","hidden");
							input5.attr("name","getTable");
							input5.attr("value","yes");
							//将表单放入body
							$("body").append(form);
							form.append(input1);
							form.append(input2);
							form.append(input3);
							form.append(input4);
							form.append(input5);
							form.submit();//提交表单
						}
					});					
				}
			}],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:'true'}, 
		        {field:'indexSn',title:'指标编号'},
		        {field:'indexName',title:'指标名称',width:80,formatter:function(value,row){
		        	return "<span title=" + value + ">" +value + "</span>";
		        }},
		        {field:'auditMethod',title:'审核方法',width:80},    
		        {field:'percentageScore',title:'百分制分数'},
		        {field:'isKeyIndex',title:'是否关键指标',formatter:function(value,row,index){
		        	if(row.isKeyIndex==true){
		        		return '是';
		        	}else if(row.isKeyIndex==false){
		        		return '否';
		        	}
		        }},
		        {field:'anDeduction',title:'单次扣分'},
		        {field:'zeroTimes',title:'几次扣完'},	
		        {field:'unsafecondition',title:'隐患',formatter:function(value,row,index){
					return '<a href="#" onclick="unsafecondition('+index+')" style="text-decoration:none">隐患['+value+']</a>'
				}}
			]]
		});

	})
</script>

</head>
<body style="margin: 1px;">
	<!--头 -->
	<input id="checkdetail" value="checkdetail" type="hidden">
	<table id="dg" style="margin: 1px;"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>