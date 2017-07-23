<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		$(function(){
			var rows = parent.$("#dg").datagrid("getSelections");
    			$('#form').form('load',{
    				purpose:rows[0].purpose,
    				reviewBasis:rows[0].reviewBasis,
    				reviewContent:rows[0].reviewContent,
    				reviewMethod:rows[0].reviewMethod,
    				reviewRequirement:rows[0].reviewRequirement,
    				reviewInput:rows[0].reviewInput,
    				reviewResult:rows[0].reviewResult,
    				correctPrevention:rows[0].correctPrevention,
    				resultConclusion:rows[0].resultConclusion,
    				reviewOutput:rows[0].reviewOutput,
    				scope:rows[0].scope
    				
    			});
		});
	</script>
</head>
<body>
	<form id="form">
			<div id="tt" class="easyui-tabs" style="width:830px;height:340px;">   
			    <div title="评审目的  "  style="padding:10px;"> 
			      <input class="easyui-textbox" multiline="true" style="width:800px;height:289px" name="purpose"/>
			    </div>   
			    <div title="评审范围"   style="overflow:auto;padding:10px;">
			      <input class="easyui-textbox" name="scope" multiline="true" style="width:800px;height:289px" />
			    </div>   
			    <div title="评审依据" style="padding:10px;">   
			      <input class="easyui-textbox"  name="reviewBasis" multiline="true" style="width:800px;height:289px"/>
			    </div>   
			    <div title="评审内容" style="padding:10px;">   
			      <input class="easyui-textbox"  name="reviewContent" multiline="true" style="width:800px;height:289px" />
			    </div>   
			    <div title="评审要求" style="overflow:auto;padding:10px;"> 
			      <input class="easyui-textbox" name="reviewRequirement" multiline="true" style="width:800px;height:289px" />
			    </div>   
			    <div title="评审方法"  style="padding:10px;">   
			      <input class="easyui-textbox" name="reviewMethod" multiline="true" style="width:800px;height:289px" />
			    </div> 
			    <div title="评审输入"style="padding:10px;">   
			      <input class="easyui-textbox"  name="reviewInput" multiline="true" style="width:800px;height:289px"/>
			    </div>   
			    <div title="评审结果" style="overflow:auto;padding:10px;">
			      <input class="easyui-textbox" name="reviewResult" multiline="true" style="width:800px;height:289px"/>
			    </div>
			    <div title="预防措施" style="padding:10px;">  
			      <input class="easyui-textbox" name="correctPrevention" multiline="true" style="width:800px;height:289px"/> 
			    </div> 
			    <div title="结果总结" style="padding:10px;">
			      <input class="easyui-textbox" name="resultConclusion" multiline="true" style="width:800px;height:289px"/>
			    </div>   
			    <div title="评审输出" style="overflow:auto;padding:10px;">
			      <input class="easyui-textbox" name="reviewOutput" multiline="true" style="width:800px;height:289px"/>
			    </div>  
			</div>
		</form>
</body>
</html>