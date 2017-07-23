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

	$(function(){
		var indexSn="";
		var type=parent.$('#type').val();
		var standardSn="";
		if(type=="季度审核" || type=="单位审核"){
			var rows=parent.$('#dg').datagrid('getChecked');
			standardSn=rows[0].standardSn;
		}else{
			var rows=parent.$('#dg').datagrid('getSelections');
			standardSn=rows[0].standard.standardSn;
		}
		var row=parent.$("iframe").get(0).contentWindow.$('#dg').datagrid('getSelected');
		var id=row.id;
		$('#dg').treegrid({    
		    url:'${pageContext.request.contextPath}/inconformity/item/checkTableAction_editTable',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来    
		    treeField:'indexName',//定义树节点字段
		    fitColumns:true,/*列宽自动*/
		    fit:true, 
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			checkbox: true,
			//onlyLeafCheck:true,
			queryParams:{'standardSn':standardSn,'checkTableId':row.id},
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/			
			onCheckNode:function(row,checked){
				if(checked==true){
					if(row.haschildren==false){
						indexSn=row.indexSn+","+"open"+","+"check";
					}else{
						indexSn=row.indexSn+","+"close"+","+"check";
					}					
				}else{
					if(row.haschildren==false){
						indexSn=row.indexSn+","+"open"+","+"uncheck";
					}else{
						indexSn=row.indexSn+","+"close"+","+"uncheck";
					}	
				}
				$.ajax({
		            type: "POST",
		            url: "${pageContext.request.contextPath}/inconformity/item/checkTableAction_addOrRemove",
		            data: {str:indexSn,id:id,standardSn:standardSn},
		            dataType: "json",
		            success: function(data){			                	
		                   if(data.status=="add"){
		                	   $.messager.alert('我的消息','保存失败,选中无效！','error');
			               }else if(data.status=="remove"){
			            	   $.messager.alert('我的消息','保存失败,取消选中无效！','error');
			               }else if(data.status=="update"){
			            	   $.messager.alert('我的消息','保存失败！','error');
				           }             
		            }
		        });
			},
			//异步加载
  	 	    onBeforeExpand:function(row){  
		        //动态设置展开查询的url
		        $("#dg").treegrid('selectRow',row.id);
		        var url = "${pageContext.request.contextPath}/inconformity/item/checkTableAction_editTable?indexSn="+row.indexSn;
		        $("#dg").treegrid("options").url = url;
		        return true;      
		    },
		    toolbar:[{
				text:'提示：勾选完毕后，直接关闭窗口即可！',
				iconCls:'icon-tip'
			}],
		    columns:[[
		        {field:'id',title:'逻辑主键',hidden:true}, 
		        {field:'indexSn',title:'编号',hidden:true},
		        {field:'indexName',title:'标准名称',width:80,formatter:function(value,row){
		        	return "<span title=" + value + ">" +row.indexSn+ value + "</span>";
		        }}
		    ]]    
		});
	})
</script>
</head>
<body style="margin: 1px;">
	<table id="dg" style="margin: 1px;"></table>
</body>
</html>