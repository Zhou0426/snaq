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
	function detail(){
			$('#win').dialog({
			width:800,
			height:400,
			title:'检查表',
			cache:false,
			maximizable:true,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/checktabledetail" frameborder="0" width="100%" height="100%"/>'
		});		
	}
	function inconformityItem(index){
		$('#dg').datagrid('clearSelections');
		$('#win').window({
			width:900,
			height:400,
			title:'不符合项',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/mychecktable_inconformityItem" frameborder="0" width="100%" height="100%"/>'
		});
	}
	function confirm(index){
		$('#dg').datagrid('clearSelections');
		$('#dg').datagrid('selectRow',index);
		$.messager.confirm('确认对话框', '您确定已经检查完毕，要确认吗？', function(r){
			if (r){
				var rows=$('#dg').datagrid('getSelections');
				var checkTableSn=rows[0].checkTableSn;
				$.ajax({
		            type: "POST",
		            url: "${pageContext.request.contextPath}/inconformity/item/checkTableAction_confirm",
		            data: {'checkTableSn':checkTableSn,'personId':"${sessionScope['personId']}"},
		            dataType: "json",
		            success: function(data){
			            if(data.status=="ok"){
			            	$.messager.alert('我的消息','确认成功！','info');							
				        }else{
				        	$.messager.alert('我的消息','确认失败！','error');
						}
						$('#dg').datagrid('clearSelections');
						$('#dg').datagrid('reload');
						toDoThing();
		            }
				});
			}
		});
	};
	function toDoThing(){
		$.ajax({
			   type: "POST",
			   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
			   success: function(msg){
				   var rec=eval('(' + msg + ')');
				   parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
				   parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
				   parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
				   parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
				   parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
			   }
		});
	};
	$(function(){		
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_queryByPersonId',
			idField:"checkTableSn",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'personId':"${sessionScope['personId']}"},//请求远程数据发送额外的参数
			fit:true,
			fitColumns:true,/*列宽自动*/
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			/*按钮*/
			toolbar:[{
				text:'"${sessionScope.personName}"的检查表',
				iconCls:'icon-tip',
			}],
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkType',title:'检查类型'},
				{field:'startDate',title:'开始日期'},
				{field:'endDate',title:'结束日期'},
				{field:'checkers',title:'检查人',width:100,formatter:function(value,row,index){
					if(row.checkers.num>0){
						return row.checkers.personNames;
					}
				}},
				{field:'confirm',title:'已检查确认',formatter:function(value,row,index){
					if(value!="未确认"){
						return value;
					}else{
						return '<a href="#" onclick="confirm('+index+')" style="text-decoration:none">点击确认</a>';
					}
				}},
				{field:'inconformityItem',title:'不符合项',formatter:function(value,row,index){
					return '<a href="#" onclick="inconformityItem('+index+')" style="text-decoration:none">不符合项['+value+']</a>'
				}},
				{field:'standardIndexs',title:'检查表详情',formatter:function(value,row,index){
					return '<a href="#" onclick="detail()" style="text-decoration:none">查看详情</a>';
				}}
			]]
		});
	})
</script>

</head>
<body style="margin: 1px;">
	<input id="details" value="my" type="hidden"/>
	<!--头 -->
	<table id="dg" style="margin: 1px;"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>