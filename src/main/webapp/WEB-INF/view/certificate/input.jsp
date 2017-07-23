<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事故录入</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>
//ajax发送请求session--导入
function getSession(){
	$.ajax({
		url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
		success: function(data){
			$('#p').progressbar('setValue',data);
		}
	},"json");
};
<!--确认导入-->
function importcer(){
	var ex=$('#excel').filebox('getValue');
	if(ex!=""){
		 var d1=/\.[^\.]+$/.exec(ex);   
	    if(d1==".xlsx"){
		 //进度条显示
		  $('#p').progressbar('setValue',0);
		  $('#winPro').window('open');
		  var timer = setInterval(getSession,500);
		$('#form').form('submit',{
			url: "${pageContext.request.contextPath}/certificate/import",
			success: function(pag){
					//var a=eval(pag);
					$('#p').progressbar('setValue','100');
					clearInterval(timer);
					$('#winPro').window('close');
					
					$('#winImport').window('close');
					$.messager.alert('提示',pag.substring(1,pag.length-1));
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
};
var str="${sessionScope['permissions']}";
$(function() {
	<!--数据网络-->
	$('#dg').datagrid({
						url : "${pageContext.request.contextPath}/certificate/showCertificate",
						rownumbers : true,
						fitColumns : true,
						fit:true,
						pageNumber : 1,
						pagination : true,
						pageSize : 100,
						//singleSelect : true,
						pageList : [ 100, 150, 200, 250 ],
						toolbar : [
								{
									id:'importtool',
									iconCls : 'icon-import',
									text : '证件导入',
									handler: function()
					  				{
										if(str.indexOf("170104")==-1){
											$("#importtool").css('display','none');
										}else{
						  					$('#winImport').window('open');
										}
					  				},
								},
								{
									iconCls : 'icon-add',
									text : '增加',
									id:'addtool',
									handler : function() {
										if(str.indexOf("170101")==-1){
											$("#addtool").css('display','none');
										}else{
							    			$('#addbtn').show();
							    			$('#editbtn').hide(); 
											$('#form0').form('reset');
							    			$('#win').window('open');	 
							    			$('#form0').form('disableValidation');
										}
									}
								},
								{
									iconCls : 'icon-edit',
									text : '修改',
									id:'edittool',
									handler : function() {	
										if(str.indexOf("170102")==-1){
											$("#edittool").css('display','none');
										}else{
						    			//数据回显
							    			var rows=$("#dg").datagrid("getSelections");
							    			if (rows.length>0){
												$('#editbtn').show();
												$('#addbtn').hide();	
								    			$('#win').window('open'); 
								    			$('#form0').form('disableValidation'); 
								    			//对表单数据进行填充
								    			$('#form0').form('load',{
								    				id:rows[0].id,
								    				issuedBy:rows[0].issuedBy,
								    				issuedDate:rows[0].issuedDate,
								    				validStartDate:rows[0].validStartDate,
									    				validEndDate:rows[0].validEndDate,
									    				certificationSn:rows[0].certificationSn
								    			});
								    			$('#holder').combogrid('setValue',rows[0].holderSn);
								    			$('#certificationType').combogrid('setValue',rows[0].certificationTypeSn);
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
									iconCls : 'icon-remove',
									text : '删除',
									id:'deletetool',
									handler : function() {
										if(str.indexOf("170103")==-1){
											$("#deletetool").css('display','none');
										}else{
											var row = $('#dg').datagrid('getChecked');
											var ids = "";
											for( var i in row ){
												ids = ids + row[i].id + ",";
											}
											ids = ids.substring(0, ids.length - 1);
											console.log(ids);
											if (row) {
												$.messager.confirm('选择是否删除','您确定要删除该证件吗?',function(fn) {
													if(fn){
														$.post('${pageContext.request.contextPath}/certificate/deleteCertificate',{ids:ids},function(message){
															$.messager.alert('提示',message); 
															$('#dg').datagrid('reload');
														});
													}
												});
											} 
											else {
												$.messager.alert('提示','请先选择要删除的行');
											}
										}
									}
								} ],
						columns : [ [
			            {	field:'xyz',
			            	checkbox:true
						},{
							field : 'id',
							hidden:true   
						}, {
							field : 'holderSn',
							hidden:true 
						},{
							field : 'certificationTypeSn',
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
	$('#addbtn').bind('click', function(){    
		$('#form0').form('enableValidation');
		$.post("${pageContext.request.contextPath}/person/exists",{personId:$('#holder').combobox('getValue')},function(text, status){if(text.exists){
			$('#form0').form('submit', {
				url:"${pageContext.request.contextPath}/certificate/addCertificate",
				success: function(message){
					$.messager.alert('提示',message); 
					$('#dg').datagrid('reload'); 
	    			$('#win').window('close');		
				}
			});
    	 }else{
    		 $.messager.alert('警告','必须选择持证人！');    
      }},'json');
    });
	$('#editbtn').bind('click', function(){    
		$('#form0').form('enableValidation');
		$('#form0').form('submit', {
			url:"${pageContext.request.contextPath}/certificate/updateCertificate",
			success: function(message){
				$.messager.alert('提示',message); 
				$('#dg').datagrid('reload');	 
    			$('#win').window('close');	
			}
		}) ; 
    });
	//证件类型
	$('#certificationType').combogrid({		
		url : '${pageContext.request.contextPath}/certificate/showCType',
	  	panelWidth:170,    
		prompt : '请选择证件类型',    	     
	    idField:'certificationTypeSn',    
	    textField:'certificationTypeName',  
        delay:200,
        mode:"remote",
	    columns:[[    
	        {field:'certificationTypeSn',title:'类型编号',width:60},    
	        {field:'certificationTypeName',title:'类型名称',width:100} 
	    ]] 
	});
	$('#holder').combogrid({		
		url : '${pageContext.request.contextPath}/certificate/personsCertificate',
	  	panelWidth:170,    
		prompt : '请搜索持有人',    	     
	    idField:'personId',    
	    textField:'personName',  
        delay:200,
        mode:"remote",
	    columns:[[    
	        {field:'personId',title:'人员编号',width:60},    
	        {field:'personName',title:'人员姓名',width:100} 
	    ]] 
	});
	
	if(str.indexOf("170101")==-1){
		$("#addtool").css('display','none');
	}
	if(str.indexOf("170102")==-1){
		$("#edittool").css('display','none');
	}
	if(str.indexOf("170103")==-1){
		$("#deletetool").css('display','none');
	}
	if(str.indexOf("170104")==-1){
		$("#importtool").css('display','none');
	}
	
	$('#importtool').linkbutton({
		plain:false
	});
	$('#addtool').linkbutton({
		plain:false
	});
	$('#edittool').linkbutton({
		plain:false
	});
	$('#deletetool').linkbutton({
		plain:false
	});
})
</script>
</head>
<body>
	<!-- 未遂事件数据列表 -->
	<table id="dg"></table>
	<!-- 事故填写窗口-->
	<div id="win" class="easyui-window" title="填写证件信息" closed="true" style="width: 360px; height: 360px; padding: 5px;">
		<br />
		<form id="form0" method="post">
			<div style="margin-left: 30px;">
				<div style="diapaly: inline">				
					 <input name="id" hidden/>
					 <label style="padding-right: 25px">证件编号：</label>
					 <input class="easyui-textbox" required="true" style="height: 25px" name="certificationSn"> 	
					 <br/><br/>	
					 <label style="padding-right: 25px">证件类型：</label>
					 <input id="certificationType" required="true" data-options="editable:false"  style="height: 25px" name="certificationTypeSn"> 	
					 <br/><br/>	
					 <label style="padding-right: 38px">持有人：</label>
					 <input id="holder" class="easyui-validatebox" style="height: 25px" name="holderSn"> 	
					 <br/> <br/>							 					 
					 <label style="padding-right: 25px">发证时间：</label> 
					 <input name="issuedDate" required="true" data-options="editable:false" style="height: 25px" class="easyui-datebox"/> 
					 <br/> <br/>	
					 <label style="padding-right: 25px">开始时间：</label> 
					 <input name="validStartDate" required="true" data-options="editable:false" style="height: 25px" class="easyui-datebox"/> 
					 <br/> <br/>
					 <label style="padding-right: 25px">到期时间：</label> 
					 <input name="validEndDate" required="true" data-options="editable:false"  style="height: 25px" class="easyui-datebox"/> 
					 <br/> <br/>	
					 <label style="padding-right: 25px">发证单位：</label>
					 <input name="issuedBy" required="true" style="height: 25px" class="easyui-textbox"/> 
				</div><br />
				<div id="dlg-buttons" style="text-align:center">
					<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok" >确认添加</a>
					<a id="editbtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>
				</div>
			</div>
		</form>
	</div>
	<!-- 导入窗口 -->
	<div id="winImport" class="easyui-window" title="导入窗口" closed="true" style="width:210px;height:180px;padding:5px;">
		<form id="form" method="post" enctype="multipart/form-data">
				<input id="excel" data-options="buttonText:'选择文件'" class="easyui-filebox" name="excel"/>
				<br/><br/><a href="${pageContext.request.contextPath}/certificate/import/download" class="easyui-linkbutton" iconCls="icon-download">下载模板</a>
				<br/><br/><a class="easyui-linkbutton" iconCls="icon-import" onclick="importcer()">导入证件</a>
		</form>
	</div>
	<!-- 进度条窗口 -->
	<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
	        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
	      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
	</div>
</body>
</html>