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
		if(powers==true){
			$('#d-a').datagrid('selectRow',index);//选中当前行
			var row = $('#d-a').datagrid('getSelected');
			var ids=row.id;
			$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
				if(r){
					//发送请求提交删除信息
	                $.post("${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_deleteByIds.action",{'ids':ids},function(result){
						//删除页面，刷新
						$("#d-a").datagrid("clearChecked");
						$("#d-a").datagrid("reload");
						parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("clearChecked");
						parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("reload");
					},"text");
				}
			});
		}else{
			parent.$.messager.alert('我的消息','对不起！您没有删除此附件的权限！','warning');
		}		
	}
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
	}
	$(function(){
		var parentrow=parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("getSelected");
		var inconformityItemSn=parentrow.inconformityItemSn;
		//判断权限
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var loginId="${sessionScope['personId']}";
		//检查人员
		var checkers="";
		//录入人
		var editor="";
		if(parentrow.checkers.num>0){
			checkers=parentrow.checkers.personIds.split(',');
		}
		if(parentrow.editor.isnull==false){
			editor=parentrow.editor.personId;
		}
		if(parentrow.hasOwnProperty("checkerFrom")){
			if(parentrow.checkerFrom=="区队" || parentrow.checkerFrom=="单位"){
				if(roles.indexOf("dwxtgly")!=-1 || loginId==editor || $.inArray("${sessionScope['pId']}",checkers)!=-1){
					powers=true;
				}
			}else{
				if(roles.indexOf("jtxtgly")!=-1 || loginId==editor || $.inArray("${sessionScope['pId']}",checkers)!=-1){
					powers=true;
				}
			}
		}else{
			var type=parent.$('#type').val();
			if(type=="单位审核"){
				if(roles.indexOf("dwxtgly")!=-1 || loginId==editor || $.inArray("${sessionScope['pId']}",checkers)!=-1){
					powers=true;
				}
			}else{
				if(roles.indexOf("jtxtgly")!=-1 || loginId==editor || $.inArray("${sessionScope['pId']}",checkers)!=-1){
					powers=true;
				}
			}
		}
		$("#d-a").datagrid({
		    url:'${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_queryJoinInconformityItem',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'inconformityItemSn':inconformityItemSn},//请求远程数据发送额外的参数
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			toolbar:[{
				id:'text',
				text:"<form id='upload' method='post' enctype='multipart/form-data'><input id='fb' type='text' name='fileModel.upload' style='width:270px'/><input name='inconformityItemSn' type='hidden'/><a id='submit' href='#'>提交</a></form>"				 
			}],
		    columns:[[ 
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'attachmentType',title:'附件类型'},
		        {field:'logicalFileName',title:'附件逻辑路径',formatter:function(value,row,index){
		        	return "<a href='javascript:' onclick='downLoadFile("+index+")'>" + value + "</a>";
					//return "<a href='${pageContext.request.contextPath}/standard/download.action?fileName="+row.physicalFileName+"&&newName="+value+"'>" + value + "</a>";
		        }},
		        {field:'physicalFileName',title:'附件物理路径',hidden:true,formatter:function(value,row,index){
					return "<a href="+ value +">" + value + "</a>";} 
		        },
		        
		        {field:'op',title:'操作',formatter:function(value,row,index){
		        	if(powers==true){
		        		return "<a href='#' id='btn' onclick='deleteit(" + index + ")'>" +"删除" + "</a>";
		        	}else{
		        		return "无权操作";
			        }
				}}
		    ]]    
		});
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
						url:'${pageContext.request.contextPath}/inconformity/item/inconformityAttachmentAction_attachmentUpload.action',
						success:function(){
							$("#upload").form("disableValidation");
							$("#upload").form("reset");
							$("#d-a").datagrid("reload");
							parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("reload");
						}
					});
				}
			}else{
				parent.$.messager.alert('我的消息','对不起！您没有在此提交附件的权限！','warning');
			}
		});

		//权限验证
		if(powers==false){
			$('#text').css('display','none');
		}
	});
</script>
</head>
<body style="margin: 1px;">
	<table id="d-a"></table>  
</body>
</html>