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
<!--验证规则-->
$.extend($.fn.validatebox.defaults.rules, {    
	Number: {//value值为文本框中的值
	        validator: function (value) {
	           // return /^13[0-9]{1}[0-9]{8}$|15[0189]{1}[0-9]{8}$|189[0-9]{8}$/.test(value);
	            var reg =/^[0-9]*$/;  
	            return reg.test(value); 
	        },
	        message: '只能输入数字'
	    }   
});
//全局变量
var showpdeptsn=null;
var departmentSn0=null;
var data=null;
//权限管理
var str="${sessionScope['permissions']}";
<!--数据网络-->
$(function () {
	$('#dg').datagrid({
		//url: "${pageContext.request.contextPath}/department/show?deleteornot=0",
		pageNumber: 1,
		pagination:true,
        rownumbers:true,
        fitColumns:true,
        fit:true,
        pageSize:50,
        singleSelect:true,
        height:650,
        pageList: [20, 30,50,100],
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
        toolbar:[
		{
			text:'上级部门：<input id="cc" name="tree">'
		},
		{
			iconCls: 'icon-import',
			text:'导入部门',
			id:'importtool',
			handler: function()
			{
				if(str.indexOf("140104")==-1){
					$("#importtool").css('display','none');
				}else{
					$('#winImport').window('open');
				}
			}
		},
		{
			iconCls: 'icon-excel',
			text:'导出到EXCEL',
			id:'exporttool',
			handler: function()
			{
				if(str.indexOf("140105")==-1){
					$("#exporttool").css('display','none');
				}else{
					var url ='${pageContext.request.contextPath}/department/export';
	  				$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
				}
			}
		},        
        {
    		iconCls: 'icon-edit',
    		text:'编辑部门',
    		id:'edittool',
    		handler: function()
    		{
				if(str.indexOf("140102")==-1){
					$("#edittool").css('display','none');
				}else{	
	    			//数据回显
	    			var rows=$("#dg").datagrid("getSelections");
	    			if (rows.length!=0){	
						if(rows[0].deleted==0){
							var departmentSnValue = $('#cc').combotree('getValue');
							$('#departmentTypeSn').combobox('reload',{
					    	   departmentSn:departmentSnValue
					    	});
			       			var t = $('#cc').combotree('tree');
			       			var node = t.tree('getSelected');   			
			       			$('#form').form({queryParams: {parentDepartmentSn:node.id}});  
			    			$('#pdepartmentSn').textbox('setValue',node.id);
			    			$('#pdepartmentSn').textbox('readonly'); 	
			    			$('#updatebtn').show();
			    			$('#addbtn').hide();
			    			$('#departmentSn').textbox('readonly');
							$('#form').form('disableValidation');
			    			$('#win').window('open');
			    			var t = $('#cc').combotree('tree');
			    			var node = t.tree('getSelected');  
			    			$('#departmentTypeSn').combobox('setValue',rows[0].departmentTypeSn);
			    			//$('#principalp').combogrid('setValue',{personId:rows[0].principalSn,personName:rows[0].principal});
			    			$('#principal').combogrid('setValue',{personId:rows[0].principalSn,personName:rows[0].principal});
			    			//对表单数据进行填充
			    			$('#form').form('load',{
			    				updateId:rows[0].id,
			    				departmentSn:rows[0].departmentSn,
			    				departmentName:rows[0].departmentName,
			    				dutyman:rows[0].dutyman,
			    				showSequence:rows[0].showSequence
			    			});
						}else{
	    					$.messager.alert('提示','该行已删除，不可继续操作');
	    				}
					}
					else
					{
						$.messager.show({
							title:'消息提示',
							msg:'请先选择您要编辑的行！',
							showType:'null',
							style:{
								top:'50'
							}
						});
					}
				}
			}
    	},
    	{
    		iconCls: 'icon-add',
    		text:'增加部门',
    		id:'addtool',
    		handler:function(){
				//添加
				if(str.indexOf("140101")==-1){
					$("#addtool").css('display','none');
				}else{
					var departmentSnValue = $('#cc').combotree('getValue');
					$('#departmentTypeSn').combobox('reload',{
			    	   departmentSn:departmentSnValue
			    	});
					$('#form').form('reset');
	    			var t = $('#cc').combotree('tree');
	    			var node = t.tree('getSelected');   			
	    			$('#form').form({queryParams: {parentDepartmentSn:node.id}});
	    			$('#pdepartmentSn').textbox('setValue',node.id);
	    			$('#pdepartmentSn').textbox('readonly'); 	
	    			$('#departmentSn').textbox('readonly',false); 	
	    			$('#addbtn').show();
	    			$('#updatebtn').hide(); 
	    			$('#win').window('open');
// 	    	    	var da=$("#departmentTypeSn").combobox('tree').tree("getRoot");	
// 	    	    	$('#departmentTypeSn').combobox('setValue',da.id);
					$('#form').form('disableValidation');
				}
    		}
    	},
    	{
    		iconCls: 'icon-remove',
    		text:'删除部门',
    		id:'deletetool',
    		handler:function()
    		{
    			//权限管理
				var str="${sessionScope['permissions']}";
				if(str.indexOf("140103")==-1){
					$("#deletetool").css('display','none');
				}else{
    				var row = $('#dg').datagrid('getSelected');
	    			if (row){
	    				if(row.deleted==0){
		    				$.messager.confirm('选择是否删除','您确定要删除该部门吗?',function(r){   					
		    					if(r){
		    						$.post('${pageContext.request.contextPath}/department/delete',{updateId:row.id},function(data){
		    							var data = eval('(' + data + ')');
		    							$.messager.alert('提示',data);
		    	    					$('#dg').datagrid('reload'); 
		    						},'text');
		    					};
		    				});
	    				}else{
	    					$.messager.alert('提示','该行已删除，不可继续操作');
	    				}
	    			}
	    			else{
	    				$.messager.show({
							title:'消息提示',
							msg:'请先选择您要删除的行！',
							showType:'null',
							style:{
								top:'50'
							}
						});
	    			}
				}
    		}
    	},{
				text:'已删除部门：<input id="includedeleted" name="checkbutton">'
		},
		{
 			iconCls: 'icon-undo',
			text:'取消删除',
			id:'cancelDelete',
			handler: function()
			{
				var row = $('#dg').datagrid('getSelected');
    			if (row){
    				if(row.deleted == true){
	    				$.messager.confirm('提示','您确定要恢复该部门吗?',function(r){
	    					if(r){
	    						$.post('${pageContext.request.contextPath}/department/cancelDelete',{changeId:row.id},function(data){
	    							$.messager.alert('提示',data.substring(1,data.length-1));
	    							$('#dg').datagrid('reload');
	    			    			$('#cancelDelete').css('display','none');
	    						},'text');
	    					}
	    				});
    				}else{
    					$.messager.alert('提示','该行未删除，不可继续操作');
    				}
    			}
			}
 		}
    	],
    	onSelect:function(index,row){
    		if( row.deleted != undefined && row.deleted == true ){
    			$('#cancelDelete').css('display','');
    		}else{
    			$('#cancelDelete').css('display','none');
    		}
    	},
        columns:[[ 
                  {field:'id',hidden:true},
                  {field:'departmentTypeSn',hidden:true},
                  {field:'principalSn',hidden:true},
                  {field:'departmentSn',title:'部门编号',width:100} ,  
                  {field:'departmentName',title:'部门名称',width:100},
                  {field:'departmentType',title:'部门类型',width:100},
                  {field:'showSequence',title:'显示顺序',width:100},
                  {field:'principal',title:'安全责任人',width:100}
              ]] ,
              onClickRow:function(){
//             		$('#principalp').combogrid('grid').datagrid('load');
              }
   });
    <!--加载datagrid里的部门表树形菜单-->
    $('#cc').combotree({
    	height:22,
	    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1401',
		panelWidth: 300,
		panelHeight:350,
	    onLoadSuccess: function (node, data) {
	       var x = $('#cc').combotree('getValue');
           if(x==""||x==null){
                var da=$("#cc").combotree('tree').tree("getRoot");	
		    	$('#cc').combotree('setValue', da);
				if($('#includedeleted').switchbutton('options').checked==true){
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/department/show';
				}else{
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/department/show?deleteornot=0';
				}
		    	$('#dg').datagrid('load',{
					parentDepartmentSn1:da.id
	    		});
		    	var node = $("#cc").combotree('tree').tree('find', da.id);
		    	$("#cc").combotree('tree').tree('select', node.target);
		    	if(data.length == 1){
	        		$('#cc').combotree('tree').tree('expand', node.target);
	        	}
		        <!--表单中的部门类型-->
		        $('#departmentTypeSn').combobox({
		    	    url: '${pageContext.request.contextPath}/department/queryDepartmentTypeByDepartmentSn',
		    	    valueField:'id',
		    	    textField:'text',
		        	width:175,
		        	editable:false,
		    	    queryParams:{departmentSn:$('#cc').combotree('getValue')}
// 		     	    onLoadSuccess: function (result) {	
// 		     	    	//console.log(result);
// 			 	    	$('#departmentTypeSn').combobox('setValue', result[0].id);
// 			 		}
		    	}); 
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
//      $('#departmentTypeSn').combobox({
//     	editable:false
// 	    url: '${pageContext.request.contextPath}/department/treeDT'
// 	    onLoadSuccess: function () {	
// 	    	var da=$("#departmentTypeSn").combotree('tree').tree("getRoot");	
// 	    	$('#departmentTypeSn').combotree('setValue', da);
// 		}		
//  	});  
// 	$('#principalp').combogrid({	
// 		url : "${pageContext.request.contextPath}/certificate/personsCertificate?item=1",
// 	  	panelWidth:170,    
// 		prompt : '输入人员编号或姓名检索',
// 	    idField:'personId',    
// 	    textField:'personName',  
//         delay:200,
//         mode:"remote",
//         onBeforeLoad: function(param){
//         	var row= $('#dg').datagrid('getSelected');
//         	if(row!=null)
//     			param.departmentSn = row.departmentSn;
//     	},
//         queryParams:{
//         	departmentSn:''         
// 		},
// 	    columns:[[    
// 	        {field:'personId',title:'人员编号',width:60},    
// 	        {field:'personName',title:'人员姓名',width:100} 
// 	    ]] 
// 	});
	//第一层权限
	if(str.indexOf('140101')==-1){
		$('#addtool').css('display','none');
	}
	if(str.indexOf('140102')==-1){
		$('#edittool').css('display','none');
	}
	if(str.indexOf('140103')==-1){
		$('#deletetool').css('display','none');
	}
	if(str.indexOf('140104')==-1){
		$('#importtool').css('display','none');
	}
	if(str.indexOf('140105')==-1){
		$('#exporttool').css('display','none');
	}
	$('#cancelDelete').css('display','none');

	$('#addtool').linkbutton({
		plain:false
	});
	$('#edittool').linkbutton({
		plain:false
	});
	$('#deletetool').linkbutton({
		plain:false
	});
	$('#importtool').linkbutton({
		plain:false
	});
	$('#exporttool').linkbutton({
		plain:false
	});
	//窗口以及窗口内表单元素生成与设置
	$('#pdepartmentSn').textbox({
	});
	$('#departmentSn').textbox({
		required:true,
		validType:'Number'		
	});
	$('#departmentName').textbox({ 
		required:true,
		missingMessage:'请输入部门名称'
	});
	$('#showSequence').textbox({ 
		required:true,
		missingMessage:'请输入显示顺序（数字）'
	});
	//安全责任人
	$('#principal').combogrid({   
		//required:true, 
	    panelWidth:170,    
	    delay: 500,    
	    mode: 'remote',    
	    url: '${pageContext.request.contextPath}/role/personss',  
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
	    idField: 'personId',    
	    textField: 'personName',   
	    prompt:'输入编号或姓名检索',
	    columns: [[    
	        {field:'personId',title:'编号',width:60,sortable:true},    
	        {field:'personName',title:'姓名',width:100,sortable:true}    
	    ]]    
	});
	$('#win').window({
		width:300,
		height:400,
		title:'填写部门信息',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		onClose:function(){
		}
	});
	//导入窗口元素设置
	$('#excel').textbox({
	});
	$('#winImport').window({
		width:230,
		height:260,
		title:'导入部门窗口',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		onClose:function(){
		},
		onOpen:function(){
			$('#excel').filebox({
				buttonText:'选择文件'
			});
		}
	});
	$('#downloadImportTemplate').linkbutton({});
	$('#importDepartmentBtn').linkbutton({});
	
	//元素点击事件
	
	//确认添加
	$('#addbtn').click(function(){
		$('#form').form('enableValidation');
		$('#form').form('submit',{
			url: "${pageContext.request.contextPath}/department/add",
			success: function(data){
				var data = eval('(' + data + ')');
				$.messager.alert('提示',data);  
				$('#win').window('close');			
				$('#dg').datagrid('reload');
				$('#form').form('reset');
			}
		});  
	});
	//确认修改
	$('#updatebtn').click(function(){
		$('#form').form('enableValidation');
		var res = $('#principal').combogrid('grid').datagrid('getSelected');	// 获取数据表格对象
		//console.log(res);
		if( res == null || res.length == 0 ){
			$.messager.alert('提示',"请选择安全负责人！");
		}else{
			$('#form').form('submit',{
				url: "${pageContext.request.contextPath}/department/update",
				success: function(data){
					$('#win').window('close');
		    	 	$.messager.alert('提示',data.substring(1,data.length-1));
					$('#dg').datagrid('reload');
					$('#form').form('reset');
				}
			});
		}
	});
	//确认导入
	$('#importDepartmentBtn').click(function(){
		var ex=$('#excel').filebox('getValue');
		if(ex!=""){
			 var d1=/\.[^\.]+$/.exec(ex);   
		    if(d1==".xlsx"){
			 //进度条显示
			  $('#p').progressbar('setValue',0);
			  $('#winPro').window('open');
			  var timer = setInterval(getSession,500);
			$('#formI').form('submit',{
				url: "${pageContext.request.contextPath}/department/importDepartment",
				success: function(data){
					    //data = eval('(' + data + ')');
						$('#p').progressbar('setValue','100');
						clearInterval(timer);
						$('#winPro').window('close');
						$('#winImport').window('close');
						$.messager.alert('提示',data.substring(1,data.length-1));
						$('#dg').datagrid('reload');			
					}
			});
		    }    
		    else{
		    	 $.messager.alert('提示','请选择.xlsx文件！');
		    	 $('#excel').filebox('setValue','');
		     }
		}else{
			$.messager.alert('提示','请选择文件！');
		}
	});
	//包含已删除的开关
	$('#includedeleted').switchbutton({ 
	      checked: false,
	      width:60,
	      onText:'显示',
	      offText:'不显示',
	      onChange:function(checked){
	    	  if(checked==true){
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/department/show';
					$('#cc').combotree('options').url='${pageContext.request.contextPath}/department/treeAllIncludeDeleted?resourceSn=1401';
	    	  }else{
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/department/show?deleteornot=0';	
					$('#cc').combotree('options').url='${pageContext.request.contextPath}/department/treeAll?resourceSn=1401';
	    	  }
			  //$('#dg').datagrid('reload');
			  //var a = $('#cc').combotree('getValue');
			  //var b = $('#cc').combotree('getText');
			  var node = $('#cc').combotree('tree').tree('getRoot');
			  $('#cc').combotree('reload');
			  $('#cc').combotree('setValue',{id:node.id,text:node.text});
		      $("#cc").combotree('tree').tree('select', node.target);
	      } 
	});
	
});
function getProValue(){
	var timer = setInterval(function(){
		$.ajax({
			url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findProValue.action',
			success: function(data){
				$('#p').progressbar('setValue',data);
				if(data == 100){
					$.ajax({
	 					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_clearSession.action',
	 				});
					$('#winPro').window('close');
                    clearInterval(timer);
                }
			}
		},"json");
	},100);
};
//ajax发送请求session--导入
function getSession(){
	$.ajax({
		url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
		success: function(data){
			$('#p').progressbar('setValue',data);
		}
	},"json");
};
</script>
</head>
<body>
	<input id="checkdelete" type="hidden">
	<input id="tree" type="hidden" >
	<table id="dg"></table>
	<div id="win" class="easyui-window"  style="padding:5px;">
		<form id="form" method="post">
			<input type="hidden" name="updateId">
			<div class="fitem">
				<label>部门类别</label>
				&emsp;&emsp;&emsp;&emsp;<input id="departmentTypeSn" name="departmentTypeSn" class="easyui-combobox">
			</div><br/>
			<div class="fitem">
				<label>父部门编号：</label>
				&emsp;&emsp;<input id="pdepartmentSn" iconCls="icon-lock"  class="easyui-textbox"/>
			</div><br/>
			<div id="departmentDiv" class="fitem">
				<label>部门编号：</label>
				&emsp;&emsp;&emsp;<input id="departmentSn" name="departmentSn" class="easyui-textbox"/>
			</div><br/>
			<div class="fitem">
				<label>部门名称：</label>
				&emsp;&emsp;&emsp;<input id="departmentName" name="departmentName" class="easyui-validatebox easyui-textbox"/>
			</div><br/>
			<div class="fitem">
				<label>显示顺序：</label>
				&emsp;&emsp;&emsp;<input id="showSequence" name="showSequence" class="easyui-validatebox easyui-textbox"/>
			</div><br/>
			<div class="fitem">
				<label>安全责任人：</label>
				&emsp;&emsp;<input id="principal" name="principal"/>
			</div><br/>
			<br/><br/>
			<div id="dlg-buttons" style="padding-left:35px">
				&emsp;&emsp;&emsp;&emsp;<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok">确认添加</a>
				&emsp;&emsp;&emsp;<a id="updatebtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>
			</div>
		</form>
	</div>
	<div id="winImport" class="easyui-window" style="padding:5px;">
		<form id="formI" method="post" enctype="multipart/form-data">		
			<br/><br/>
				<input id="excel" type="text" name="excel"/><br/><br/><br/><br/>
				<a id="downloadImportTemplate" class="easyui-linkbutton" iconCls="icon-mini-add" href="${pageContext.request.contextPath}/department/download">下载导入模板</a>	
				<br/><br/><br/><a id="importDepartmentBtn" class="easyui-linkbutton" iconCls="icon-import">导入部门</a>				
		</form>
	</div>
	<!-- 进度条窗口 -->
	<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
	        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
	      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
	</div>
</body>
</html>