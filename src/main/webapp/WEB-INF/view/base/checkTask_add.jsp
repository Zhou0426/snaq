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
	<style type="text/css">
		form div{
			margin:10px
		}
	</style>
	<script type="text/javascript">
		
		var d=parent.$('#department').combobox('getValue');
		var e=parent.$('#selectYear').combobox('getValue');
		var f=parent.$('#selectMonth').combobox('getValue');
		$(function(){
			$('#ff').form('load',{
				departmentSn:d,
				yearTime:e,
				monthTime:f
			});
			$('#person').combogrid({
			    url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showPersonAll.action', 
				delay: 500,    
				width:200,
			    panelWidth:200,
			    prompt:'请输入搜索信息',
			    idField:'personId',    
			    textField:'personName',    
			    columns:[[    
					        {field:'personId',title:'编号',width:90},    
					        {field:'personName',title:'姓名',width:108}
				]],
				onChange:function(newValue, oldValue){
					if(newValue!=null && newValue.length!=0){
						$('#person').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':d});
					}
				}
			});
			//隐患
			$('#checkTaskCount').textbox({
				required:true,
				width:200
			});
			//不安全行为
			$('#unsafeActCheckTaskCount').textbox({
				required:true,
				width:200
			});
			//弹出窗口时禁用验证
			$("#ff").form("disableValidation");
			//注册botton的事件
			$("#ok_btn").click(function(){
					//开启验证
					$("#ff").form("enableValidation");
					//如果验证成功，则提交数据
					if($('#ff').form("validate")){
						$('#ff').form('submit', {
							url:'${pageContext.request.contextPath}/office/appraisals/office_addData.action',
							success: function(result){
								if(result=="\"login\""){
									parent.$.messager.alert('提示','该人员在当前时间记录已存在，请重新选择！');
									}else{
										if(result=="\"success\""){
											//表单重置
											$("#ff").form("reset");
											//关闭当前页面
											parent.$('#win').window("close");
											parent.$.messager.show({
												title:'提示',
												msg:'添加成功！',
												timeout:2000,
												showType:'slide'
											});	
										}else{
											//表单重置
											$("#ff").form("reset");
											//关闭当前页面
											parent.$('#win').window("close");
											parent.$.messager.alert('警告','添加失败，请检查后重新操作！');
										}
										//刷新dg
										parent.$("#dg").datagrid("reload");
									}
							}
						});						
					}
				});
			$("#cancel_btn").click(function(){
				//关闭窗口
				parent.$('#win').window("close")
			});
		});
	</script>
</head>
<body>
	<form id="ff" method="post">
	    <div>   
	        <label for="person">&emsp;选择检查人：&emsp;&emsp;&emsp;</label>   
	        <input id="person" name="checkerId"  />   
	    </div>
	    <div>   
	        <label for="checkTaskCount">隐患检查任务数量：&emsp;</label>   
	        <input id="checkTaskCount" name="checkTaskCount" />  
	    </div>
	    <div>   
	        <label for="unsafeActCheckTaskCount">不安全行为任务数量：</label>   
	        <input id="unsafeActCheckTaskCount" name="unsafeActCheckTaskCount" />  
	    </div>
	    
	    <div>
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    	<input  id="departmentSn" name="departmentSn" type="hidden" />
	    	<input  id="yearTime" name="yearTime" type="hidden" />
	    	<input  id="monthTime" name="monthTime" type="hidden" />
	    </div>
	</form>
</body>
</html>