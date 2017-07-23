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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>   
<!--数据网络-->
$(function () {
	$('#dg').datagrid({
		pageNumber: 1,
		pagination:true,
        url: "${pageContext.request.contextPath}/department/showAction",
        rownumbers:true,
        fitColumns:true,
        pageSize:10,
        singleSelect:true,
        toolbar:'#toolbar',
        width:1000,
        height:350,
        pageList: [5, 10,15,20],
        toolbar:[
		{
			text:'上级部门：<input id="cc" name="tree">',
			handler: function()
			{
				
			}
		}],
        columns:[[ 
                  {field:'id',title:'部门id',width:100,hidden:true},
                  {field:'departmentSn',title:'部门编号',width:100} ,  
                  {field:'departmentName',title:'部门名称',width:100},
                  {field:'departmentType',title:'部门类型',width:100,
                	  formatter: function(value)
                	  {
          					return value.departmentTypeName;
          			  }  
                  },
                  {field:'dutyman',title:'安全责任人',width:100},
                  {field:'showSequence',title:'显示顺序',width:100}
              ]]  
   });
    var p = $('#dg').datagrid('getPager');
    $(p).pagination({ 
        beforePageText: '第',
        afterPageText: '页  共 {pages} 页 ', 
        displayMsg: '共{total}条数据',
    }); 
    $('#dept_tree').combotree({  
    });
    <!--加载datagrid里的部门表树形菜单-->
    $('#cc').combotree({
	    url: '${pageContext.request.contextPath}/department/tree',
	    onLoadSuccess: function () {
	       var x = $('#cc').combotree('getValue');
           if(x.length==0){  
                var da=$("#cc").combotree('tree').tree("getRoot");	
		    	$('#cc').combotree('setValue', da);
		    	$('#dg').datagrid('load',{
					parentDepartmentSn1:da.id
	    		});
		    	$('#cc').combotree('select', da); 
           }
		},
		<!--当被选中时datagrid提交选中节点的编号加载相应的子部门-->
		onSelect: function(){
			var t = $('#cc').combotree('tree');
			var node = t.tree('getSelected');	
			$('#dg').datagrid('load',{
				parentDepartmentSn1:node.id
			});
		}
	}); 
    <!--表单中的部门类型-->
    $('#departmentTypeSn').combotree({
	    url: '${pageContext.request.contextPath}/department/treeDT',	   	     
	    onLoadSuccess: function () {	
	    	var da=$("#departmentTypeSn").combotree('tree').tree("getRoot");	
	    	$('#departmentTypeSn').combotree('setValue', da);
		}		
	});  
});
</script>
</head>
<body>
	<input id="tree" type="hidden" >
	<table id="dg"></table>
</body>
</html>