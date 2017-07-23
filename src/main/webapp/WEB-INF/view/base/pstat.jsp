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
$(function() {
	<!--数据网络-->
	$('#dg').datagrid({
		pageNumber : 1,
		pagination : true,
		rownumbers : true,
		fitColumns : true,
		pageSize : 10,
		singleSelect : true,
		fit:true,
		nowrap:false,
		pageList : [ 5, 10, 15, 20 ],
        loadMsg:'正在载入人员使用统计信息...',
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
        toolbar:[
                 {
     				text:'部门：<input id="treeselect" name="treehidden">'
     			}, 
     			{
     				text:''
     			}
        ],
		columns:[[ 
				  {field:'id'}
	  		 ]] ,
	  	   onClickRow:function(index,row){
	   			$('#dg').datagrid('selectRow',index);    	
	      }
	}); 
	<!--加载部门表树形菜单-->
    $('#treeselect').combotree({
	    url: '${pageContext.request.contextPath}/department/tree',
	    onLoadSuccess:function(node,data){
			var da=$("#treeselect").combotree('tree').tree("getRoot");	
	    	$('#treeselect').combotree('setValue', da);
	    	var node = $("#treeselect").combotree('tree').tree('find', da.id);
	    	$("#treeselect").combotree('tree').tree('select', node.target);
	    },
	    onSelect: function(node){
			departmentSn=node.id;	
			query();
		}
	});  
});
//查询函数
function query(){
	$('#dg').datagrid('options').url="${pageContext.request.contextPath}/personUseStat/show";
	$('#dg').datagrid('load',{
		departmentSn:departmentSn 
	});
}
</script>
</head>
<body>
	<table id="dg"></table>
	<input id="treehidden" type="hidden" >
</body>
</html>