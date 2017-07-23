<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>模块使用统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-cellediting.js"></script>
		<script type="text/javascript">
			var a = 1;
			var yearTime = "";
			var monthTime = "";
			var resourceSn = "";
			var dateYear = new Array();//年度数组
			var nowDateMonth = new Array();//当前月份数组
			var dateMonth = new Array();//月份数组
			var date = new Date;//当前日期
			//创建年份下拉框
			for(var i = date.getFullYear(); i > 2015; i--){
				dateYear.push({"id":i,"text":i+"年"});
			};
			//创建当前月份下拉框
			for(var i = 1;i <= date.getMonth() + 1; i++){
				nowDateMonth.push({"id":i,"text":i+"月份"});
			};
			//创建往年月份下拉框
			for(var i = 1;i < 13; i++){
				dateMonth.push({"id":i,"text":i+"月份"});
			};
			//发送请求
			function loadAction(){
				//var url = "${pageContext.request.contextPath}/module/moduleStatisticsAction_query.action";
		        //$("#tg").treegrid("options").url = url;
				$('#tg').treegrid('load',{
					'yearTime':yearTime,
					'monthTime':monthTime
				});
			};
		 	$(function(){  
				$('#tg').treegrid({
				    url:'${pageContext.request.contextPath}/module/moduleStatisticsAction_query.action',    
				    idField:'id',
				    treeField:'resourceName',
		            toolbar:'#time',
				    fitColumns:true,/*列宽自动*/
					striped:true,/*斑马线效果  */
					nowrap:true,/*数据同一行*/
					fit:true,
					loadmsg:'请等待',
					rownumbers:true,/* 行号*/
					remoteSort:false,/*禁止使用远程排序*/
				    columns:[[
						{field:'id',hidden:true},
				        {field:'resourceName',title:'页面模块',width:100,align:'left'},
				        {field:'sumClickCount',title:'累计点击次数',width:80,align:'center'},
				        {field:'avgClickCountPerDay',title:'日均点击次数',width:80,align:'center'},
				        {field:'sumUserCount',title:'累计使用人数',width:80,align:'center'},
				        {field:'avgUserCountPerDay',title:'日均使用人数',width:80,align:'center'}
				    ]],
				    onBeforeExpand:function(row){
				    	//动态设置展开查询的url
				        $("#tg").treegrid('selectRow', row.resourceSn);
				        //var url = "${pageContext.request.contextPath}/module/moduleStatisticsAction_query.action?resourceSn=" + row.resourceSn + "&&yearTime=" + yearTime + "&&monthTime=" + monthTime;
				        //$("#tg").treegrid("options").url = url;
				        return true;
				    },
				    onBeforeLoad:function(){
				    	if(yearTime == "" && monthTime == ""){
				    		return false;
				    	}else{
				    		return true;
				    	}
				    }
				});
		
				$('#selectYear').combobox({
					valueField:'id',    
					textField:'text',  
					panelHeight:150,
					editable:false,
					prompt:'选择年份',
					width:150,
					onLoadSuccess:function( node, data ){
						if(node.length != 0){	
							//将根节点的值默认输出
							$('#selectYear').combobox('select', node[0].id);
							yearTime = node[0].id;
							//$('#selectMonth').combobox('loadData', nowDateMonth);
						}
					},
					onSelect:function(rec){
						yearTime = rec.id;
						if(rec.id < date.getFullYear()){
							$('#selectMonth').combobox('loadData', dateMonth);
						}else{
							$('#selectMonth').combobox('loadData', nowDateMonth);
						}
					}
				});
				$('#selectMonth').combobox({
					valueField:'id',    
					textField:'text',  
					panelHeight:200,
					editable:false,
					prompt:'选择月份',
					width:150,
					onLoadSuccess:function(node,data){
						if(node.length != 0){	
							//将根节点的值默认输出
							if(a == 1){
								$('#selectMonth').combobox('select', date.getMonth() + 1);
								monthTime=date.getMonth()+1;
							}else{
								$('#selectMonth').combobox('select', node[0].id);
								monthTime = node[0].id;
							}
							a++;
						}
					},
					onSelect:function(rec){
						monthTime = rec.id;
						loadAction();
					}
				});
		
				$('#selectYear').combobox('loadData', dateYear);
		 	});
		</script>
</head>
<body>
	<div id='time'>
		<span>
			<lable for="selectYear">选择年份：</lable>
			<input id="selectYear" />
		</span>
		<span>
			<lable for="selectMonth">选择月份：</lable>
			<input id="selectMonth" />
		</span>
	</div>
	<table id="tg"></table>
</body>
</html>