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
	//判断附件是否存在并下载
	function downLoadFile(index){
		var rows=$('#muban-attachment-dg').datagrid('getRows');
		$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",{attachmentPath:rows[index].attachmentPhysicalFileName},function(result){
			if(result=="success"){
				var url ='${pageContext.request.contextPath}/standard/download.action?fileName='+rows[index].attachmentPhysicalFileName+'&&newName='+rows[index].attachmentLogicFileName;
				$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			}else{
				$.messager.alert('警告','文件已丢失，下载失败！');
			}
		},"json");
	}
	function deleteit(index){
		//alert(index);
		$('#muban-attachment-dg').datagrid('selectRow',index);//选中当前行
		var row = $('#muban-attachment-dg').datagrid('getSelected');
		var ids=row.id;
		$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
			if(r){
				//发送请求提交删除信息
                $.post("standard/templateAttachmentAction_deleteById.action",{'ids':ids,'path':row.attachmentPhysicalFileName},function(result){
					if(result=="fail"){
						parents.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
							if(r){
								//window.open("denglu","_top")
							}
						});
						
					}else{
						//删除页面，刷新
						$("#muban-attachment-dg").datagrid("clearChecked");
						$("#muban-attachment-dg").datagrid("reload");
						parent.$("#dg").datagrid("clearChecked");
						parent.$("#dg").datagrid("reload");
					}
				},"text");
			}
		});
	};

	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var parentrow=parent.$('#dg').datagrid('getSelected');
		var documentTemplateSn=parentrow.documentTemplateSn;
		$("#muban-attachment-dg").datagrid({
		    url:'standard/templateAttachmentAction_queryJoinAttachment.action',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'documentTemplateSn':documentTemplateSn},//请求远程数据发送额外的参数
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			toolbar:[{
				id:'text',
				text:"<form id='upload' method='post' enctype='multipart/form-data'><input id='fb' type='easyui-validatebox' name='fileModel.upload' style='width:270px'/><input type='hidden' name='documentTemplateSn'/><a id='submit' href='#'></a></form>"				 
			}],
		    columns:[[ 
				{field:'id',title:'逻辑主键',hidden:true},
		        {field:'attachmentLogicFileName',title:'附件名称(点击可下载)',formatter:function(value,row,index){
		        	return "<a href='javascript:' onclick='downLoadFile("+index+")'>" + value + "</a>";
					//return "<a href='standard/download.action?fileName="+row.attachmentPhysicalFileName+"&&newName="+value+"'  style='text-decoration:none;'>" + value + "</a>"; 
		        }},
		        {field:'attachmentPhysicalFileName',title:'附件物理路径',hidden:true,formatter:function(value,row,index){
					return "<a href="+ value +"  style='text-decoration:none;'>" + value + "</a>";} 
		        },
		        
		        {field:'op',title:'操作',formatter:function(value,row,index){
		        	if(roles.indexOf('jtxtgly')==-1){
		        		return "无权操作";
		    		}else{
		    			return "<a href='#' id='btn' onclick='deleteit(" + index + ")' style='text-decoration:none;'>" +"删除" + "</a>";
		    		}
				}}
				]]    
		});
  		$('#fb').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right' 
		});
		$('#submit').linkbutton({
			text:'上传',
		    iconCls:'icon-add'   
		});
		//数据回显
		$('#upload').form('load',{
			documentTemplateSn:documentTemplateSn,
		});
		//表单提交
		//自定义验证函数
		$.extend($.fn.validatebox.defaults.rules,{
			type:{
				validator:function(value,param){
					//获取文件扩展名
					//alert("value:" + value + ",param:" + param.length);
					var ext=value.substring(value.lastIndexOf(".")+1);
					var params=param[0].split(",");
					for(var i=0;i<params.length;i++){
						if(ext==params[i])
							return true;
					}
					return false;

				},
				//{0}代表传入的第一个参数
				message: '文件类型必须为:{0}'
			}
		});
		$("[name='fileModel.upload']").validatebox({
			required:true,   // file文本域 validatebox不能实现及时验证. 
			missingMessage:'文件格式错误（.xls或.xlsx）',
			// 如果验证的函数有参数,则直接在后面添中括号
			validType:"type['xls,xlsx']"
		});
		//自定义change事件实现文本框及时验证
		$("[name='fileModel.upload']").change(function(){
			$(this).validatebox("validate");
		});
		//开始禁止验证
 		$("#upload").form("disableValidation");
		$("#submit").click(function(){
			if(roles.indexOf('jtxtgly')==-1){
				$('#text').css('display','none');
			}else{
				//开启验证
				$("#upload").form("enableValidation");
				if($("#upload").form("validate")){
					//ajax提交
					$('#upload').form('submit',{
						url:'standard/templateAttachmentAction_attachmentUpload.action',
						success:function(data){
							var data = eval('(' + data + ')');  
							if(data.status=="ok"){
								$.messager.alert('我的消息','上传成功！','info');
								$("#upload").form("reset");
								$("#muban-attachment-dg").datagrid("reload");
							}else{
								$.messager.alert('我的消息','上传失败！','error');
							}
							$("#upload").form("disableValidation");
							parent.$("#dg").datagrid("reload");
						}
					});
				}
			}
		});
		//第一层权限
		if(roles.indexOf('jtxtgly')==-1){
			$('#text').css('display','none');
		}
	})
</script>
</head>
<body style="margin: 1px;">
	<table id="muban-attachment-dg"></table>  
</body>
</html>