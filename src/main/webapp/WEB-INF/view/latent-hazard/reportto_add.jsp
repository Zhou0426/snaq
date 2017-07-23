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
		$(function(){
			var date=new Date();
			var first = 1;
			$('#departmentReportTo').combotree({
				url:'${pageContext.request.contextPath}/department/queryDepartmentByDepartmentTypeJTCS',
				valueField:'id',
				textField:'text',
				panelHeight:200,
				panelWidth:250,
				width:200,
				required:true,
				onLoadSuccess:function(node,data){
					if(first==1){
						if(data.length != 0){
							//将根节点的值默认输出
							$('#departmentReportTo').combotree('setValue',data[0].id);
						}
						first++;
					}
				}
			});
			$('#happenLocation').textbox({
				required:true,
				height:26,
				width:200
			});
			$('#latentHazardDescription').textbox({
				required:true,
				multiline:true,
				width:560,
				height:100
			});
			$('#happenDateTime').datetimebox({    
			    value: date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate() + " " + date.getHours() + ":" +  date.getMinutes() + ":" + date.getSeconds(),
			    width:200,
				height:26,
			    panelWidth:200,
			    editable:false,
			    required: true
			});
			$('#fb').filebox({
				width:560,
				height:26,
			    buttonText: '选择文件',
			    buttonAlign: 'right',
			    multiple: true,
			    prompt:'选择文件时按着ctrl可多选'
			});
			//集团检查单位
			$("#checkUnit").combobox({
				url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_queryDepartment',
			    editable:false,
				width:200,
				valueField: 'departmentSn',    
		        textField: 'departmentName', 
			    panelHeight:'auto'
			});
			//外部检查单位
			$('#checkUnitName').textbox({
				height:26,
				width:200
			});
			//检查分类
			$('#checkClass').combobox({
				valueField: 'label',
				textField: 'value',
				required:true,
				editable:false,
				width:200,
				panelHeight:'auto',
				data: [{
					label: '单位自查',
					value: '单位自查'
				},{
					label: '集团公司',
					value: '集团公司'
				},{
					label: '神华集团',
					value: '神华集团'
				},{
					label: '政府部门',
					value: '政府部门'
				}],
				onLoadSuccess:function(node){
					$('#checkClass').combobox('select',"单位自查");
				},
			    onSelect:function(rec){
					if(rec.value=='单位自查'){
						$("#checkUnitNameDiv").css("display",'none');
						$("#checkUnitDiv").css("display",'');
						$('#checkUnit').combobox('reload', {
							pag : "单位自查"
						});
					}else if(rec.value=='集团公司'){
						$("#checkUnitNameDiv").css("display",'none');
						$("#checkUnitDiv").css("display",'');
						$('#checkUnit').combobox('reload', {
							pag : "集团公司"
						});
					}else{
						$("#checkUnitNameDiv").css("display",'');
						$("#checkUnitDiv").css("display",'none');
					}
			    }
			});
			//弹出窗口时禁用验证
			$("#ff").form("disableValidation");
			//注册botton的事件
			$("#ok_btn").click(function(){
					//开启验证
					$("#ff").form("enableValidation");
					//如果验证成功，则提交数据
					if( ( $("#checkUnitName").textbox('getValue') != "" || $('#checkUnit').combobox('getValue') != "" ) 
							&& $('#ff').form("validate") ){
						$('#ff').form('submit', {
							url:'${pageContext.request.contextPath}/latenthazard/latenthazardAction_add',
							success: function(result){
								var data = eval( '(' + result + ')' );
								if(data.message == "success"){
									//表单重置
									$("#ff").form("reset");
									//关闭当前页面
									parent.$('#win').window("close");
									parent.$.messager.show({
										title:'提示',
										msg:'添加信息成功！',
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
						});						
					}
				});
			$("#cancel_btn").click(function(){
				//关闭窗口
				parent.$('#win').window("close")
			});
		});
		//上报模板下载 
		function download_reportToTemplate(){
			var url ='${pageContext.request.contextPath}/hazard/download_reportToTemplate.action';
			  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
		};
		//隐患报告模板下载
		function download_unsafeConditionTemplate(){
			var url ='${pageContext.request.contextPath}/hazard/download_unsafeConditionTemplate.action';
			  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
		};
	</script>
</head>
<body>
	<form id="ff" method="post" enctype="multipart/form-data">
	    <div>   
	        <label for="happenDateTime">检查时间：&emsp;&emsp;</label>   
	        <input id="happenDateTime" name="happenDateTime" />&emsp;&emsp;
	        <label for="happenLocation">发生地点：&emsp;&emsp;</label>   
	        <input id="happenLocation" name="happenLocation" />  
	    </div>
	    <div>
	    	<label for="departmentReportTo">业务部门：&emsp;&emsp; </label>   
	        <input id="departmentReportTo" name="departmentReportTo" />&emsp;&emsp;
	        <label for="status">是否上报：&emsp;&emsp;</label>   
	        <select id="status" name="status" class="easyui-combobox" 
					data-options="required:true,panelWidth: 200,panelHeight:'auto',editable:false" style="width:200px;height:26px;">
					<option value="yes" selected="selected">是</option>   
				    <option value="no">否</option>
			</select>    
	    </div>
	    <div>
	    	<label for="checkClass">检查分类：&emsp;&emsp; </label>   
	        <input id="checkClass" name="checkClass" />&emsp;&emsp;
	        
	        <label for="checkUnit">检查单位：&emsp;&emsp;</label>
	        <span id="checkUnitDiv" style="display:none;" >
		        <input id="checkUnit" name="checkUnit"/>
	        </span>
	        <span id="checkUnitNameDiv" style="display:none;" >
	        	<input id="checkUnitName" name="checkUnitName"/>
	        </span>
	    </div>
	    <div>
	        <label for="latentHazardDescription">重大隐患描述：</label>   
	        <input id="latentHazardDescription"  name="latentHazardDescription" /> 
	    </div>
	    <div>
	        <label for="template">下载模板：&emsp;&emsp;</label>
	  		<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_reportToTemplate()">下载上报模板</a>
	  		<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_unsafeConditionTemplate()">下载隐患报告模板</a>
	    </div>
	    <div id="loadFile" class="div-height">
			<label for="fb">选择文件：&emsp;&emsp;</label>
			<input name="upload" id="fb" type="text" style="width:300px">
		</div>
	    <div style="text-align:center;">
	    	<a id="ok_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
	    	<a id="cancel_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	    </div>
	</form>
</body>
</html>