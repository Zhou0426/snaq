<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员使用统计</title>
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
var order=null;
var sort=null;
$(function() {
	<!--数据网络-->
	$('#dg').datagrid({
		rownumbers : true,
		fit:true,
		fitColumns : true,
		singleSelect : true,
		nowrap:false,
		remoteSort: true,
		pagination : true,
		pageNumber : 1,
		pageSize : 20,
		pageList : [ 10, 20, 50,100 ],
        loadMsg:'正在载入人员使用统计信息...',
        toolbar:[
                 {
     				text:'部门：<input id="treeselect" name="treehidden">'
     			}, 
     			{
     				text:'年：<input id="yearselect" name="yearhidden">'
     			}, 
     			{
     				text:'月：<input id="monthselect" name="monthhidden">'
     			}
        ],		
		columns:[[ 
				   {field:'id',hidden:true},
				   {field:'impDepartmentName',title:'单位',width:70},
				   {field:'person',title:'人员',width:70},
				   {field:'loginCount',title:'登陆次数',sortable:true,width:70},
				   {field:'avgLoginCountPerDay',title:'日均登陆次数',sortable:true,width:70,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}
				   },
				   {field:'sumOnlineTime',title:'累计在线时间',sortable:true,width:70},
				   {field:'avgOnlineTimePerDay',title:'日均在线时间',sortable:true,width:70,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}},
				   {field:'avgOnlineTimePerLogin',title:'平均每次登陆在线时间',sortable:true,width:70,
					   formatter: function(value,row,index){
							return value.toFixed(4);
						}
					},
					{field:'statDateTime',title:'统计时间',sortable:true,width:70,
						   formatter: function(value,row,index){
		                         var unixTimestamp = new Date(value);  
		                         return unixTimestamp.toLocaleString();  
						   }
					   },				
				   {field:'loginDetail',title:'登陆详情',width:70,
					   formatter: function(value,row,index){
           			   	return '<a href="#" onClick="detail('+index+')">登陆详情</a>'; 
           			}
				   }
	  		 ]] ,
	  	   onClickRow:function(index,row){
	   			$('#dg').datagrid('selectRow',index);    	
	      }
	}); 
	<!--详情-->
	$('#detaildg').datagrid({
		rownumbers : true,
		fit:true,
		fitColumns : true,
		singleSelect : true,
		nowrap:false,
		pagination : true,
		pageSize : 10,
		pageNumber : 1,
		pageList : [ 5, 10, 15, 20 ],
        loadMsg:'正在载入人员详情信息...',
		columns:[[ 
				   {field:'id',hidden:true},
				   {field:'personId',hidden:true},
				   {field:'jsessionId',title:'会话编号'},
				   {field:'loginDateTime',title:'登陆时间'},
				   {field:'lastOperationDateTime',title:'最后一次操作时间'},
				   {field:'internetIp',title:'外网Ip'},
				   {field:'internalIp',title:'内网Ip'}
				   
	  		 ]] ,
	  	   onClickRow:function(index,row){
	   			$('#detaildg').datagrid('selectRow',index);    	
	      }
	}); 
	<!--加载部门表树形菜单-->
    $('#treeselect').combotree({
	    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1302',
    	height:22,
		panelWidth: 300,
		panelHeight:350,
	    onLoadSuccess:function(node,data){
	    	var x = $('#treeselect').combotree('getValue');	
	    	if(x.length==0){  
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
			query();
		}
	});  
    $('#yearselect').combobox({
	    url: '${pageContext.request.contextPath}/departmentUseStat/year',
    	valueField:'year',    
		textField:'year',  
 		panelHeight:'auto',
    	height:22,
    	width:80,
    	onChange: function(){
    		year=$('#yearselect').combobox('getValue');
    		query();
    	}
     }); 
	$("#yearselect").combobox("setValue",year);//设置默认值为今年
    $('#monthselect').combobox({  
    	height:22,
    	width:60,
    	onChange: function(){
    		month=$('#monthselect').combobox('getValue');
    		query();
    	},
    	valueField:'month',    
		textField:'month', 
		panelHeight:'auto'
    }); 

	var data = [];//创建年度数组
	var startMonth;
	var endMonth=13;
	//数组添加值（2012-2016）//根据情况自己修改
	startMonth=1;
	for(startMonth;startMonth<endMonth;startMonth++){
		data.push({"month":startMonth});			    
	}
	$("#monthselect").combobox("loadData", data);//下拉框加载数据
	$("#monthselect").combobox("setValue",month);//设置默认值	
});
//查询函数
function query(){
	$('#dg').datagrid('options').url="${pageContext.request.contextPath}/personUseStat/show";
	if(departmentSn!=null && departmentSn.length!=0 && year!=null && month!=null){
		$('#dg').datagrid('load',{
			departmentSn:departmentSn,
			year:year,
			month:month
// 			sort:sort,
// 			order:order
		});
	}
}
function detail(index){
	$('#dg').datagrid('selectRow',index);
	var row=$('#dg').datagrid("getSelections");
	var value=row[0].personId;
	$('#detaildg').datagrid('options').url='${pageContext.request.contextPath}/personUseStat/detail';
	$('#detaildg').datagrid('load',{personId:value});
	$('#detailwin').window('open');
};
</script>
</head>
<body>
	<table id="dg"></table>
	<input id="treehidden" type="hidden" >
	<input id="yearhidden" type="hidden">
	<input id="monthhidden" type="hidden">
	<input id="orderhidden" type="hidden">
	<!-- 详情窗口 -->
	<div id="detailwin" class="easyui-window"  title="登陆详情" closed="true" style="width: 1030px; height: 400px; padding: 5px;">
		<table id="detaildg" >
		</table>	
	</div>
</body>
</html>