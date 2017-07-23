<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门使用统计</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>
var str="${sessionScope['permissions']}";
var departmentSn=null;
var date=new Date();
var year=date.getFullYear();
var month=date.getMonth()+1;
$(function() {
	<!--数据网络-->
	$('#dg').treegrid({
		//url:'${pageContext.request.contextPath}/departmentUseStat/show',
		idField:'id',
	    treeField:'departmentName',
	    fitColumns:true,/*列宽自动*/
		striped:true,/*斑马线效果  */
		nowrap:true,/*数据同一行*/
		fit:true,
		loadmsg:'请等待',
		rownumbers:true,/* 行号*/
		remoteSort:false,/*禁止使用远程排序*/
        toolbar:[
     			{
     				text:'年：<input id="yearselect" name="yearhidden">'
     			}, 
     			{
     				text:'月：<input id="monthselect" name="monthhidden">'
     			}
        ],
        onLoadSuccess:function(row, data){
        	if(data.length != 0 && data[0].id == "1"){
	        	//console.log(data[0].id);
	        	$('#dg').treegrid('expand',data[0].id);
        	}
        },
		columns:[[ 
				   {field:'id',hidden:true},
				   {field:'departmentName',title:'部门',width:150},
				   {field:'loginPersonCount',title:'累计登陆人数',width:80},
				   {field:'avgLoginCount',title:'日均登陆人数',width:80,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}
				   },
				   {field:'loginRatio',title:'登陆比率',width:80,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}},
				   {field:'avgLoginRatioPerDay',title:'日均登陆比率',width:80,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}
				   },
				   {field:'sumOnlineTime',title:'累计在线时间',width:80},
				   {field:'avgOnlineTimePerCapitalDay',title:'人均每天在线时间',width:100,
					   formatter: function(value,row,index){
						return value.toFixed(4);
						}
					},
					{field:'statDateTime',title:'统计时间',width:80,
						   formatter: function(value,row,index){
		                         var unixTimestamp = new Date(value);  
		                         return unixTimestamp.toLocaleString();  
						   }
					},
			]],
 	        onBeforeExpand: function(row){
				$("#dg").treegrid('selectRow', row.id);
 	       		return true;
	        }
	});   
    $('#yearselect').combobox({
    	height:22,
    	width:80,
    	onChange: function(){
    		year=$('#yearselect').combobox('getValue');
    		query();
    	},
    	valueField:'year',    
		textField:'year',  
 		panelHeight:'auto', 
        url: '${pageContext.request.contextPath}/departmentUseStat/year'
     }); 
	$("#yearselect").combobox("setValue",year);//设置默认值为今年
    $('#monthselect').combobox({
    	height:22,
    	width:60,
    	onSelect: function(){
    		month=$('#monthselect').combobox('getValue');
    		query();
    	},
    	valueField:'month',    
		textField:'month',  
		panelHeight:'auto'
    }); 

	var data = [];//创建年度数组
	var startMonth;//起始年份
	var endMonth=13;//结束年份
	//数组添加值（2012-2016）//根据情况自己修改
	startMonth=1;
	for(startMonth;startMonth<endMonth;startMonth++){
		data.push({"month":startMonth});			    
	}
	$("#monthselect").combobox("loadData", data);//下拉框加载数据
	$("#monthselect").combobox("setValue",month);//设置默认值为当月
});
//查询函数
function query(){
	$('#dg').treegrid('options').url="${pageContext.request.contextPath}/departmentUseStat/show";
	$('#dg').treegrid('load',{
		year:year,
		month:month		
	});
}
</script>
</head>
<body>
	<table id="dg"></table>
	<input id="yearhidden" type="hidden">
	<input id="monthhidden" type="hidden">
</body>
</html>