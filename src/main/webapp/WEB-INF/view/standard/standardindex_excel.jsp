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
<style>
	.f{
		text-align:center;
		margin-top:20px;
	}
</style>
<script type="text/javascript">
	
	var timerId;
	function getForm(){
		
        //使用JQuery从后台获取JSON格式的数据
        $.ajax({
           type:"post",//请求方式
           url:"standard/standardindexAction_importSession.action",//发送请求地址
           timeout:30000,//超时时间：30秒
           dataType:"json",//设置返回数据的格式
           //请求成功后的回调函数 data为json格式
           success:function(data){
              if(data>=100){
                 clearInterval(timerId);
              }
              $('#p').progressbar('setValue',data);
          },
          //请求出错的处理
          error:function(){
             clearInterval(timerId);
          }
       });
	}

	 $(function(){
		var standardSn=parent.$('#cc2').combobox('getValue');
		var url="";
		var url2="";
		if(parent.$('#type').val()==1){
			url='standard/standardindexAction_importData.action?templateSn=standardindex&&standardSn='+standardSn;
			url2="standard/fileDownload.action?templateName=评分标准模板&&templateSn=standardindex";
		}else{
			url='standard/standardindexAction_importData2.action?templateSn=standardindex2&&standardSn='+standardSn;
			url2="standard/fileDownload.action?templateName=审核指南模板&&templateSn=standardindex2";
		}
		//模板下载
		$('#download').click(function(){
			var form=$("<form>");
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("method","post");
			form.attr("action",url2);
			$("body").append(form);
			form.submit();//提交表单
		});
			
		//开始禁止验证
 		$("#download").form("disableValidation");
		//触发提交事件
		$("#submit").click(function(){
			//表单数据验证
			$("#download").form("enableValidation");
			//验证通过返回true
			if($("#download").form("validate")){
				//ajax
				$('#download').form("submit",{
					url:'standard/fileDownload.action?templateName=审核指南模板', 
					success:function(){
						$("#download").form("reset");
					}
				});
			}
		});
		$('#fb').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right' 
		});
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
			required:true,//file文本域 validate不能实现及时验证
			missingMessage:'请上传正确的文件(.xls)',
			validType:"type['xls']"		
		});
		//自定义change事件实现文本框及时验证
		$("[name='fileModel.upload']").change(function(){
			$(this).validatebox("validate");
		});
		//文件上传提交
		//开始禁止验证
 		$("#upload").form("disableValidation");
		$("#submit2").click(function(){
			//开启验证
			$("#upload").form("enableValidation");
			if($("#upload").form("validate")){
				//按钮和框隐藏
				$('#form').css('display','none');
				
				$('#p').css('margin-top','35px');
				$('#p').css('display','');
				timerId=setInterval(getForm,500);
				//ajax提交
				$('#upload').form('submit',{
					url:url,
					success:function(data){
						var result = eval('(' + data + ')');
						var erNum=result.erNum;
						var error=result.error;
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
						}else{
							parent.$.messager.alert(msg,error,'error');
						}
						var url='standard/standardindexAction_queryPart.action';
						parent.$("#standard").treegrid("options").url = url;
						parent.$("#standard").treegrid("reload",{
							standardSn:standardSn
						});
						$("#upload").form("disableValidation");
						$("#upload").form("reset");
						clearInterval(timerId);
						parent.$("#standard-win").window('close');
					}
				});
			}
		});
	});

</script>
</head>
<body>
	<div id="form">
	 <!--模板导入  -->
	 <form id="upload" method='post' enctype='multipart/form-data'>
	 	<div class="f">
	 		<label for="fb">浏览:</label>
	 		<input id='fb' type='text' name='fileModel.upload' style='width:200px'/>
	 	</div>
	 </form>
	 <!-- 模板下载 --> 	        
	 <div class="f">
	    <a  href="#" id="download" style="text-decoration: none">没有模板？点我下载！</a>
	    <a id="submit2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">导入</a>   
	 </div>
	 </div>
	 <div id="p" class="easyui-progressbar" style="width:300px;margin-left:20px;display:none"></div>  
</body>
</html>