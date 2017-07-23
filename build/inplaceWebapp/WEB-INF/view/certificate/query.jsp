<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>神华宁煤安全风险预控管理信息系统</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<style type="text/css">
		.div-show{
			display:inline;
			margin:6px auto;
		} 
	</style> 
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script> 
		//全局变量
		var departmentSn=null;
		var certificateTypeSn=null;
		var certificateSn=null;
		var holderSn=null;
		var time=null;
		var endtime=null;
		var issuedBy=null;
		var order='ASC';//排序方式 
		var days=0;
		$(function () {
			<!--数据网络-->
			$('#dg').datagrid({
		        //url: "${pageContext.request.contextPath}/certificate/showCertificate",
		        idField: 'id',
	            toolbar:'#toolbarDiv',
		        //title:'证件查询',
		        fitColumns:true,
		        fit:true,
		        rownumbers:true,
		        singleSelect:true,
	            nowrap:false,
		        pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:30,
	            pageList:[20,25,30,50,100],
				columns : [[
				{
					field : 'id',
					title : 'id',
					width : 150,
					hidden:true   
				},{
					field : 'holderSn',
					title : 'holderSn',
					width : 150,
					hidden:true 
				},{
					field : 'certificationTypeSn',
					title : 'certificationTypeSn',
					width : 150,
					hidden:true 
				},{
					field : 'certificationTypeName',
					title : '证件名称',
					width : 150 
				},{
					field : 'certificationSn',
					title : '证件编号',
					width : 150
				},{
					field : 'issuedBy',
					title : '发证单位',
					width : 150
				},{
					field : 'holderName',
					title : '持有人',
					width : 60
				},{
					field : 'issuedDate',
					title : '发证日期',
					width : 70
				},{
					field : 'validStartDate',
					title : '开始日期',
					width : 70
				},{
					field : 'validEndDate',
					title : '到期日期',
					width : 70
				} ] ]
			});
			var first = 1;
		    <!--加载当前角色datagrid里的部门表树形菜单-->
		    $('#treeselect').combotree({
			    url: '${pageContext.request.contextPath}/department/tree',
				panelWidth: 300,
				panelHeight:350,
			    onLoadSuccess:function(node,data){
			    	if( first == 1 ){
						var da=$("#treeselect").combotree('tree').tree("getRoot");	
				    	$('#treeselect').combotree('setValue', da);
				    	var node = $("#treeselect").combotree('tree').tree('find', da.id);
				    	$("#treeselect").combotree('tree').tree('select', node.target);
				    	
				    	if(data.length == 1){
			        		$('#treeselect').combotree('tree').tree('expand', node.target);
			        	}
				    	first++;
			    	}
			    },
			    onSelect: function(node){
					departmentSn=node.id;
					query();
				}
			});   
			//证件类型
			$('#certificationType').combogrid({		
				url : '${pageContext.request.contextPath}/certificate/showCType',
			  	panelWidth:170,    
				prompt : '请输入证件类型',    	     
			    idField:'certificationTypeSn',    
			    textField:'certificationTypeName',  
		        delay:200,
		        mode:"remote",
			    columns:[[    
			        {field:'certificationTypeSn',title:'类型编号',width:60},    
			        {field:'certificationTypeName',title:'类型名称',width:100} 
			    ]],
			    onClickRow:function(index,row){
			    	var g = $('#certificationType').combogrid('grid');	// 获取数据表格对象
			    	g.datagrid('selectRow',index);
			    	var row = g.datagrid('getSelected');
			    	certificateTypeSn=row.certificationTypeSn;
			    	query();
			    }
			});
			$('#certificationSns').combogrid({		
				url : '${pageContext.request.contextPath}/certificate/certificationSnsCertificate',
			  	panelWidth:170,    
				prompt : '请输入证件编号',    	     
			    idField:'certificationSn',    
			    textField:'certificationSn',  
		        delay:200,
		        mode:"remote",
			    columns:[[    
			        {field:'certificationSn',title:'证件编号',width:150}
			    ]] ,
			    onClickRow:function(index,row){
			    	var g = $('#certificationSns').combogrid('grid');
			    	g.datagrid('selectRow',index);
			    	var row = g.datagrid('getSelected');
			    	certificateSn=row.certificationSn;
			    	query();
			    }
			});
			$('#holder').combogrid({		
				url : '${pageContext.request.contextPath}/certificate/personsCertificate',
			  	panelWidth:170,    
				prompt : '请输入持证人信息',    	     
			    idField:'personId',    
			    textField:'personName',  
		        delay:200,
		        mode:"remote",
			    columns:[[    
			        {field:'personId',title:'人员编号',width:60},    
			        {field:'personName',title:'人员姓名',width:100} 
			    ]] ,
			    onClickRow:function(index,row){
			    	var g = $('#holder').combogrid('grid');	// 获取数据表格对象
			    	g.datagrid('selectRow',index);
			    	var row = g.datagrid('getSelected');
			    	holderSn=row.personId;
			    	query();
			    }
			});
		    //发证时间
		    $('#timeselect').datebox({
		    	onChange: function(){
		    		time=$('#timeselect').datetimebox('getValue');
		    		query();
		    	}
		    }); 
		    //到期时间
		    $('#endtimeselect').datebox({
		        editable:false,
		    	onChange: function(){
		    		endtime=$('#endtimeselect').datetimebox('getValue');
		    		query();
		    	}
		    }); 
		    //天数
		    $('#daysselect').combobox({
		        editable:false,
		    	onChange: function(){
		    		days=$('#daysselect').combobox('getValue');
		    		query();
		    	}
		    });  
		    //排序
		    $('#orderselect').combobox({
		        editable:false,
		    	onChange: function(){
		    		order=$('#orderselect').combobox('getValue');
		    		query();
		    	}
		    }); 
		    //发证单位
		   $('#issuedByselect').combogrid({		
				url : '${pageContext.request.contextPath}/certificate/issuedBysCertificate',
			  	panelWidth:170,    
				prompt : '请搜索单位',    	     
			    idField:'issuedBy',    
			    textField:'issuedBy',  
		        delay:200,
		        mode:"remote",
			    columns:[[    
			        {field:'issuedBy',title:'发证单位',width:150}
			    ]] ,
			    onClickRow:function(index,row){
			    	var g = $('#issuedByselect').combogrid('grid');
			    	g.datagrid('selectRow',index);
			    	var row = g.datagrid('getSelected');
			    	issuedBy=row.issuedBy;
			    	query();
			    }
			});
		    
		});
		//查询函数
		function query(){
			$('#dg').datagrid('options').url="${pageContext.request.contextPath}/certificate/showCertificate";
			$('#dg').datagrid('load',{
				departmentSn: departmentSn,
				certificationTypeSn:certificateTypeSn,
				certificationSn:certificateSn,
				holderSn: holderSn,
				time:time,
				endtime:endtime,
				days:days,
				issuedBy:issuedBy,
				order:order//排序方式
			});
		}	
	</script>
</head>
<body>
	<div id="toolbarDiv">
		<div class="div-show">&emsp;所在部门：<input id="treeselect" style="width:180px;height:25px" /></div>
		<div class="div-show">&emsp;证件名称：<input id="certificationType" style="width:180px;height:25px" /></div>
		<div class="div-show">&emsp;证件编号：<input id="certificationSns" style="width:180px;height:25px" /></div>
		<div class="div-show">&emsp;持证人：&emsp;<input id="holder" style="width:180px;height:25px" /></div><br />
		<div class="div-show">&emsp;到期时间：<input id="endtimeselect" style="width:180px;height:25px" /></div>	
		<div class="div-show">&emsp;发证单位：<input id="issuedByselect" style="width:180px;height:25px" /></div>
		<div class="div-show">&emsp;到期天数：<select id="daysselect" class="easyui-combobox" name="order" style="width:180px;height:25px"> 
				<option>请选择</option>  
				<option value="15">15</option> 
				<option value="30">30</option> 
				<option value="60">60</option> 
			    <option value="90">90</option>   
			    <option value="300">300</option>
			</select>
		</div>
		<div class="div-show">&emsp;排序：&emsp;&emsp;<select id="orderselect" class="easyui-combobox" name="order" style="width:180px;height:25px">   
			    <option value="ASC">按到期天数升序</option>   
			    <option value="DESC">按到期天数降序</option>
			</select>
		</div>
	</div>
	<table id="dg"></table>
</body>
</html>