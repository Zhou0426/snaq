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
		
		$('#cc1').combobox({
			url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			editable:false					
		});
		
		var year="";
		//年份
		$("#cc2").combobox({   
			valueField:'year',    
			textField:'yeartext',  
			panelHeight:'auto',
			editable:false,
			required:true
		});
		var data = [];//创建年度数组
		var thisYear=new Date().getUTCFullYear();//今年
		for(var i=thisYear;i>2015;i--){
			data.push({"year":i,"yeartext":i+"年"});
		}
		$("#cc2").combobox("loadData", data);//下拉框加载数据
		$("#cc2").combobox("setValue",thisYear);//设置默认值为今年
		
		var row=parent.$('#dg').datagrid('getSelected');
		$('#ff').form('load',{
			departmentTypeSn:row.departmentTypeSn,
			year:row.year,
			deductionPoints:row.deductionPoints,
			level:row.unsafeActLevel=="A类不安全行为"?"2":(row.unsafeActLevel=="B类不安全行为"?1:0)
			//level:row.unsafeActLevel
		});
		
		//页面加载时禁止使用验证对照
		$("#ff").form("disableValidation");
		
		//触发提交事件
		$("#submit").click(function(){
			//表单数据验证
			$("#ff").form("enableValidation");
			//验证通过返回true
			if($("#ff").form("validate")){
				//ajax
				$('#ff').form("submit",{
					url:'${pageContext.request.contextPath}/customize/unit/unsafeActDeductionPointAction_update',
					queryParams:{type:0,id:row.id},  
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
							$("#ff").form("reset");
							//关闭窗体
							parent.$("#win").window("close");
							//刷新dg
							parent.$("#dg").datagrid("reload");
						}else{
							parent.$.messager.alert("提示信息",msg,'error');
						}
						
					}
				});
			}
		});
	});
</script>
</head>
<body>
	<form id="ff" method="post">
	    <div style="margin-top: 8px">   
	        <label for="departmentTypeSn">单位类型:</label>
	        <input id="cc1" name="departmentTypeSn" class="easyui-combobox"  data-options="width:240,required:true,missingMessage:'单位类型必选!'" />   
	    </div>
	    <div style="margin-top: 15px">   
	        <label for="level">行为等级:</label>
	        <input  class="easyui-combobox" name="level" data-options="
				valueField: 'label',
				textField: 'value',
				editable:false,
				width:240,
				panelHeight:'auto',
				required:'true',
				missingMessage:'不安全行为类型必选!',
				data: [{
					label: '2',
					value: 'A类不安全行为'
				},{
					label: '1',
					value: 'B类不安全行为'
				},{
					label: '0',
					value: 'C类不安全行为'
				}]" />
	    </div>
	    <div style="margin-top: 15px">   
	        <label for="year">选择年份:</label>
	        <input id="cc2" name="year" class="easyui-combobox"  data-options="width:240,required:true,missingMessage:'年份必选!'" />   
	    </div>
	    <div style="margin-top: 15px">   
	        <label for="deductionPoints">单次扣分:</label>
	        <input name="deductionPoints" class="easyui-numberbox"  data-options="
	        	width:240,
	        	required:true,
	        	missingMessage:'单次扣分必填!',
	        	precision:2,
	        	min:0,
	        	max:10
	        	" />   
	    </div> 
	    <div style="margin-top: 25px;text-align:center">
	    	<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
	    </div>
	</form>  
</body>
</html>