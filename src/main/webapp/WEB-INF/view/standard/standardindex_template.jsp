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
		var roles="${sessionScope['roles']}";
		var row=parent.$("#standard").treegrid("getSelected");
		$("#s-m").datagrid({
		url:'standard/standardindexAction_queryDocumentTemplate?indexSn='+row.indexSn,//要改为一个action
		idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
		queryParams:{type:""},//请求远程数据发送额外的参数
		fitColumns:true,/*列宽自动*/
		striped:true,/*斑马线效果  */
		nowrap:true,/*数据同一行*/
		loadmsg:'请等待',
		rownumbers:true,/* 行号*/
		remoteSort:false,/*禁止使用远程排序*/
		singleSelect:true,
		checkOnSelect:false,
		/*按钮*/
		toolbar:[{
			id:'text',
			text:'<form id="ff" method="post"><input id="cc1" name="documentTemplateSn"></form>'
			},{
				id:'add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					if(roles.indexOf('jtxtgly')==-1){
						$('#text,#add,#delete').css('display','none');
					}else{
						$('#ff').form('enableValidation');
			 			if($('#ff').form('validate')){
			 				$('#ff').form('submit',{    
			 				    url:"standard/standardindexAction_addDocumentTemplate.action", 
			 				    queryParams:{'indexSn':row.indexSn},      
			 				    success:function(data){
			 				    	var data = eval('(' + data + ')');      
			 				        if(data=="ok"){
			 				        	parent.$.messager.alert('我的消息','添加成功！','info');										
				 				    }else{
				 				    	parent.$.messager.alert('我的消息','添加失败！','error');
					 				}
			 				     	//删除页面，刷新
									$("#s-m").datagrid("clearSelections");
									$("#s-m").datagrid("reload");
									//刷新主表
									var url='standard/standardindexAction_queryPart.action';
					 	        	parent.$("#standard").treegrid("options").url = url;
					 	        	parent.$('#standard').treegrid('reload',{
					 	        		standardSn:parent.$('#cc2').combobox('getValue')	        		
					 	        	});
					 	        	$('#cc1').combogrid('clear');
					 	        	$('#ff').form('disableValidation'); 
			 				    }    
			 				});
				 		}
					}
				}					
		},{
			id:'delete',
			iconCls:'icon-remove',
			text:'删除',
			handler:function(){
				if(roles.indexOf('jtxtgly')==-1){
					$('#text,#add,#delete').css('display','none');
				}else{
					//1、获取选中行
					var rows=$("#s-m").datagrid("getSelections");
					//2、对事件进行判断，和删除确认
					if(rows.length==0){
						$.messager.show({
							title:'提示',
							msg:'至少选择为一条记录！',
							timeout:2000,
							showType:'slide'
						});
					}else{
						$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
							if(r){
								var documentTemplateSn=rows[0].documentTemplateSn
								//4、发送请求提交删除信息
		                        $.post("standard/standardindexAction_deleteDocumentTemplate.action",{"documentTemplateSn":documentTemplateSn,"indexSn":row.indexSn},function(result){
		                        	var result = eval('(' + result + ')');
									if(result=="ok"){
										parent.$.messager.alert('我的信息','删除成功！',"info");
									}else{
										parent.$.messager.alert('我的信息','删除失败！',"error");
									}
									//删除页面，刷新
									$("#s-m").datagrid("clearSelections");
									$("#s-m").datagrid("reload");
									//刷新主表
									var url='standard/standardindexAction_queryPart.action';
					 	        	//var url="";
					 	        	parent.$("#standard").treegrid("options").url = url;
					 	        	parent.$('#standard').treegrid('reload',{
					 	        		standardSn:parent.$('#cc2').combobox('getValue')	        		
					 	        	});
								},"text");
							}
						});
					}
				}
			}
		}],
		/*列*/
		columns:[[
			{field:'id',title:'逻辑主键',width:100,hidden:true},
			{field:'documentTemplateSn',title:'模板编号',width:100},
			{field:'documentTemplateName',title:'模板名称',width:200,formatter:function(value,row,index){
				return "<span title=" + value + ">" + value + "</span>";
			}}
			]]
		});
		//combogrid
		$('#cc1').combogrid({ 
			url:'standard/documentTemplateAction_queryByQ.action',     		     
			idField:'documentTemplateSn',    
		    textField:'documentTemplateName',
		    width:350,
		    delay:500,
		    prompt:'输入编号或名称进行检索，选中后点击添加按钮进行添加',
		    mode: 'remote',
		    required:true,        
		    columns:[[    
		        {field:'documentTemplateSn',title:'模板编号'},    
		        {field:'documentTemplateName',title:'模板名称'}
			]]     
		});
		$('#ff').form('disableValidation');

		//第一层权限
		if(roles.indexOf('jtxtgly')==-1){
			$('#text,#add,#delete').css('display','none');
		}
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
	})

</script>
</head>
<body style="margin: 1px;">
	<!--头 -->
	<table id="s-m"></table>
</body>
</html>