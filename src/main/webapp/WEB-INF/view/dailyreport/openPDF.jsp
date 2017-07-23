<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线预览</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/pdfobject.js"></script>
<style>
	.pdfobject-container { width:100%;height: 900px;}
	.pdfobject { border: 1px solid #666; }
</style>
</head>
<body>
	<div id="pdf"></div>	
	<script>PDFObject.embed("${param.realUrl}"+"&&reportDate=${param.reportDate}"+"&&departmentTypeSn=${param.departmentTypeSn}", "#pdf");</script>
</body>
</html>