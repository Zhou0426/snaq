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
<style type="text/css">
	#fb{
	float:left
	}
	#submit{
		height:22px
	}
</style>
<script type="text/javascript">
	var powers=false;
	function deleteit(index){
		//alert(index);
		$('#d-a').datagrid('selectRow',index);//选中当前行
		var row = $('#d-a').datagrid('getSelected');
		var ids=row.id;
		if(powers==true){
			$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
				if(r){
					//发送请求提交删除信息
	                $.post("${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_deleteByIds.action",{'ids':ids},function(result){
						if(result=="fail"){
							parents.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
								if(r){
									//window.open("denglu","_top")
								}
							});
						}else{
							//删除页面，刷新
							$("#d-a").datagrid("clearChecked");
							$("#d-a").datagrid("reload");
							parent.$("#dg2").datagrid("clearChecked");
							parent.$("#dg2").datagrid("reload");
						}
					},"text");
				}
			});
		}else{
			$.messager.alert('我的消息','对不起！您没有删除此附件的权限！','warning');
		}
	};
	//判断附件是否存在并下载
	function downLoadFile(index){
		var rows=$('#d-a').datagrid('getRows');
		$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",{attachmentPath:rows[index].physicalFileName},function(result){
			if(result=="success"){
				var url ='${pageContext.request.contextPath}/standard/download.action?fileName='+rows[index].physicalFileName+'&&newName='+rows[index].logicalFileName;
				$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			}else{
				$.messager.alert('警告','文件已丢失，下载失败！');
			}
		},"json");
	};
	$(function(){
		var parentrow=parent.$('#dg2').datagrid('getSelections');
		var inconformityItemSn=parentrow[0].inconformityItemSn;
		//判断权限	
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var loginId="${sessionScope['personId']}";
		
		if(parentrow[0].hasOwnProperty("checkerFrom")){
			if(parentrow[0].checkerFrom=="区队" || parentrow[0].checkerFrom=="单位"){
				if(roles.indexOf("dwxtgly")!=-1 || loginId==parentrow[0].editorId || $.inArray('${sessionScope.pId}',parentrow[0].checkersId.split(','))!=-1){
					powers=true;
				}
			}else{
				if(roles.indexOf("jtxtgly")!=-1 || loginId==parentrow[0].editorId || $.inArray('${sessionScope.pId}',parentrow[0].checkersId.split(','))!=-1){
					powers=true;
				}
			}
		}else{
			if(parentrow[0].systemAudit=="单位内审"){
				if(roles.indexOf("dwxtgly")!=-1 || loginId==parentrow[0].editorId || $.inArray('${sessionScope.pId}',parentrow[0].checkersId.split(','))!=-1){
					powers=true;
				}
			}else{
				if(roles.indexOf("jtxtgly")!=-1 || loginId==parentrow[0].editorId || $.inArray('${sessionScope.pId}',parentrow[0].checkersId.split(','))!=-1){
					powers=true;
				}
			}
		}
		//var id=parentrow.id;
		$("#d-a").datagrid({
		    url:'${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_queryJoinUnsafeAct',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'inconformityItemSn':inconformityItemSn},//请求远程数据发送额外的参数
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			fitColumns:true,/*列宽自动*/
			fit:true,
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			toolbar:[{
				text:"<form id='upload' method='post' enctype='multipart/form-data'><input id='fb' type='text' name='fileModel.upload' style='width:270px'/><input name='inconformityItemSn' type='hidden'/><a id='submit' href='#'>提交</a></form>"				 
			}],
		    columns:[[ 
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'attachmentType',title:'附件类型'},
		        {field:'logicalFileName',title:'附件名称',formatter:function(value,row,index){
		        	return "<a href='javascript:' onclick='downLoadFile("+index+")'>" + value + "</a>";
		        	//return "<a href='${pageContext.request.contextPath}/standard/download.action?fileName="+row.physicalFileName+"&&newName="+value+"'>" + value + "</a>";
					} 
		        },
		        {field:'physicalFileName',title:'附件物理路径',hidden:true,formatter:function(value,row,index){
					return "<a href="+ value +">" + value + "</a>";} 
		        },
		        
		        {field:'op',title:'操作',formatter:function(value,row,index){
		        	if(powers==false){
		        		return "";
		        	}else{
						return "<a href='#' id='btn' onclick='deleteit(" + index + ")'>" +"删除" + "</a>";} 
		        	}
		        }
		    ]]    
		});
		if(powers==false){
			$('#upload').hide();
		}
  		$('#fb').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right' 
		});
		$('#submit').linkbutton({
		    iconCls:'icon-add'   
		});
		$('#upload').form('load',{
			inconformityItemSn:inconformityItemSn,
		});
		//表单提交
		$("[name='fileModel.upload']").validatebox({
			required:true,//file文本域 validate不能实现及时验证
			missingMessage:'请选择一个文件',	
		});
		//开始禁止验证
 		$("#upload").form("disableValidation");
		$("#submit").click(function(){
			if(powers==true){
				//开启验证
				$("#upload").form("enableValidation");
				if($("#upload").form("validate")){
					//ajax提交
					$('#upload').form('submit',{
						url:'${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_attachmentUpload_unsafeAct.action',
						success:function(){
							$("#upload").form("disableValidation");
							$("#upload").form("reset");
							$("#d-a").datagrid("reload");
							parent.$("#dg2").datagrid("reload");
						}
					});
				}
			}else{
				$.messager.alert('我的消息','对不起！您没有提交此附件的权限！','warning');
			}
		});
	});
</script>
</head>
<body>
	<table id="d-a"></table>  
</body>
</html>