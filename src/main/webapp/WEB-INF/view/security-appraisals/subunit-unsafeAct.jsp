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
	var inconformityItemSnValue=new Array();
	var specialityValue=new Array();
	var unsafeActMarkValue=new Array();
	var unsafeActStandardValue=new Array();
	var unsafeActLevelValue=new Array();
	function attachment_unsafeAct(index){
		$('#dg').datagrid('clearSelections');//清处多选的行
			parent.$('#child-win').window({
				width:400,
				height:300,
				title:'相关附件',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/unsafeAct_attachment" frameborder="0" width="100%" height="100%"/>'
			});
	};
	$(function(){
		var row=parent.$('#dg').datagrid('getSelected');
		var value=parent.$('#value').val();
		var year=parent.$('#cc2').combobox('getValue');
		var month=parent.$('#cc3').combobox('getValue');
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryDetail.action',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			//title:"动态检查",
			queryParams:{'value':value,'departmentSn':row.departmentSn,'year':year,'month':month},//请求远程数据发送额外的参数
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
			toolbar:[{
				id:'export',
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('导出确认','您确定要导出当前数据吗？',function(r){
						if(r){
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_exportDetail");
							//添加input
							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","value");
							input2.attr("value",value);
							
							var input3=$("<input>");
							input3.attr("type","hidden");
							input3.attr("name","departmentSn");
							input3.attr("value",row.departmentSn);
							
							var input4=$("<input>");
							input4.attr("type","hidden");
							input4.attr("name","year");
							input4.attr("value",year);
							
							var input5=$("<input>");
							input5.attr("type","hidden");
							input5.attr("name","month");
							input5.attr("value",month);
							//将表单放入body
							$("body").append(form);
							form.append(input2);
							form.append(input3);
							form.append(input4);
							form.append(input5);
							form.submit();//提交表单
						}
					});
				}
			}],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkedDepartment',width:100,title:'被检部门'},
				{field:'violator',width:100,title:'不安全行为人员'},
				{field:'actDescription',width:100,title:'行为描述'},
				{field:'checkers',width:100,title:'检查成员'},
				{field:'checkDateTime',width:100,title:'检查时间'},
				{field:'checkLocation',width:100,title:'检查地点'},
				{field:'editor',width:100,title:'录入人'},
				{field:'editorId',width:100,hidden:true},
				{field:'checkersId',width:100,hidden:true},
				{field:'checkType',width:100,title:'检查类型',hidden:true},
				{field:'checkerFrom',width:100,title:'检查人来自',hidden:true},
				{field:'systemAudit',width:100,title:'所属的审核',hidden:true},
				{field:'inconformityItemNature',width:100,title:'不符合项性质',hidden:true},
				{field:'inconformityLevel',width:100,title:'不符合项等级',hidden:true},
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
				  {field:'unsafeActLevel',title:'不安全行为等级',width:120,align:'center',hidden:true,
					  formatter: function(value,row,index){
						  if(value!=null && value!=""){
							  unsafeActLevelValue[index]=value;
						  }else{
							  unsafeActLevelValue[index]="无";
						  }
					  }},
				{field:'attachment',width:100,title:'相关附件',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment_unsafeAct("+index+")' style='text-decoration:none'>" + "附件"+"["+value+"]" + "</a>";
				}}
				]],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
					return '<table style="border:1"><tr>' + 								
					'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
					'<td style="width:140px;text-align:center">' + 
					'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
					'<td style="width:140px;text-align:center">' + 
					'<p>' + unsafeActLevelValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' + 
					
					'<td style="width:100px;text-align:center">'+'专业：' + '</td>' + 
					'<td style="width:140px;text-align:center" >' + 
					'<p>' + specialityValue[rowIndex] + '</p>' + 
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
		$('#export').linkbutton({
			plain:false
		})
	})
</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>