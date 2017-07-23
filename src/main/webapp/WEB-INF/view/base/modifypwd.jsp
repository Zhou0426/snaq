<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>神华宁煤安全风险预控管理信息系统</title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<!-- 引进bootstrap所需文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function () {
// 	$(document).ready(function(){
		var error = false;
		$("#oldpass").blur(function(){
			var oldpass = $("#oldpass").val();
			if(oldpass =='') {
				showError('oldpass', '密码不能为空');
				error = true;
				return;
			}

			$.post("${pageContext.request.contextPath}/person/findpwd", {password:oldpass}, function(data){
				if(data.length>0) {
					$("#oldpass").css({"border-color":"green"});
					$("#oldpassTip").css({"display":"none"});
				} else {
					showError('oldpass', '密码错误');
					error = true;
				}
			});
		});

		$("#newpass").blur(function(){
			var oldpass = $("#oldpass").val();
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
			}
			else if(newpass == oldpass) {
				showError('newpass', '新密码不能与原密码相同');
				error = true;
			}
			else {
				$("#newpass").css({"border-color":"green"});
				$("#newpassTip").css({"display":"none"});
				error=false;
			}
		});

		$("#newpassAgain").blur(function(){
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
				return;
			}

			var newpassAgain = $("#newpassAgain").val();
			if(newpassAgain != newpass) {
				showError('newpassAgain', '与输入的新密码不一致');
				error = true;
			}
			else {
				$("#newpassAgain").css({"border-color":"green"});
				$("#newpassAgainTip").css({"display":"none"});
			}
		});
		
		$("#submit").click(function(event){
			$("#oldpass").blur();
			$("#newpass").blur();
			$("#newpassAgain").blur();

			if(!error) {			
				var newpass = $("#newpass").val();
				$.post('${pageContext.request.contextPath}/person/modifypwd', {password:newpass}, function(data) {
					if(data=="1"){
						$("#modifySuccess").css({'display':'inline'});
							setTimeout(function(){
							window.parent.location.href='${pageContext.request.contextPath}/login';
						},1000);
					}
					else{
						$.messager.alert('提示','密码修改失败!');
					}
				});
			}
			//event.preventDefault();
			return false;
		});
	});

	function showError(formSpan, errorText) {
		$("#" + formSpan).css({"border-color":"red"});
		$("#" + formSpan + "Tip").empty();
		$("#" + formSpan + "Tip").append(errorText);;
		$("#" + formSpan + "Tip").css({"display":"inline"});
	}
</script>
</head>
<body>
<div class=".container" style="margin-top:100px;width:80%;">
	<form class="form-horizontal" role="form">
	<s:if test="hasActionMessages()">   
         <s:iterator value="actionMessages"> 
         	<div class="alert alert-danger"><s:property value="actionMessages" /></div>	            
         </s:iterator>   
      </s:if> 
	  <div class="form-group">	  
	    <label for="oldpass" class="col-sm-2 control-label">旧密码</label>
	    <div class="col-sm-10">
	      <input type="password" class="form-control" style="width:250px;" id="oldpass" placeholder="请输入旧密码"><span id="oldpassTip" style="display:none;color:red;"></span>
	    </div>
	  </div>
	  <div class="form-group">
	    <label for="newpass" class="col-sm-2 control-label">新密码</label>
	    <div class="col-sm-10">
	      <input type="password" class="form-control" style="width:250px;" id="newpass" placeholder="请输入新密码"><span id="newpassTip" style="display:none;color:red;"></span>
	    </div>
	  </div>
	  <div class="form-group">
	    <label for="newpassAgain" class="col-sm-2 control-label">再次确认新密码</label>
	    <div class="col-sm-10">
	      <input type="password" class="form-control" style="width:250px;" id="newpassAgain" placeholder="请再次输入新密码"><span id="newpassAgainTip" style="display:none;color:red;"></span>
	    </div>
	  </div>
	  <div class="form-group">
	    <label class="col-sm-2 control-label">  </label>
	 	 <button type="submit" class="btn btn-primary" id="submit" style="text-align:center;" accesskey="13">确认修改</button>
	  </div>
	  <div id="modifySuccess" class="alert alert-success alert-dismissable" style="width:50%;margin-left:40%;display:none;">
		  <strong>Success!</strong> 你已成功修改密码！请重新登陆！
	</div>
	</form>
</div>
</body>
</html>