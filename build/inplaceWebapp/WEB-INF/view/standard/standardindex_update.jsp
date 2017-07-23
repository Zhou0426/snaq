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
.input{
	margin-top:10px
}
#button{
	text-align:center
}
</style>
<script type="text/javascript">
	$(function(){
		//专业
		$('#cc1').combobox({    
			url:'${pageContext.request.contextPath}/inconformity/item/specialityAction_query',
			panelHeight:'auto',
		   	valueField:'specialitySn',    
		    textField:'specialityName',
		    editable:false,
		    width:240,   
		    multiple:true
		});
		$('#cc2').combobox({
			valueField: 'label',
			textField: 'value',
			panelHeight:'auto',
			width:240,
			editable:false,
			data: [{
				label: 'true',
				value: '是'
			},{
				label: 'false',
				value: '否'
			}]

		});
			
		//数据回显
		var row=parent.$("#standard").treegrid("getSelected");
		if(row.isKeyIndex==false){
			$('#cc2').combobox('setValue','false');
		}
		if(row.isKeyIndex==true){
			$('#cc2').combobox('setValue','true');
		}
		//对表单数据进行填充
		$('#standard-update').form('load',{
			id:row.id,
			indexSn:row.indexSn,
			parentindexSn:row.parentindexSn,
			indexName:row.indexName,
			auditMethod:row.auditMethod,
			percentageScore:row.percentageScore,
			//isKeyIndex:row.isKeyIndex,
			anDeduction:row.anDeduction,
			zeroTimes:row.zeroTimes
		});
		if(row.speciality.num>0){
			$('#cc1').combobox('setValues',row.speciality.specialitySns);
		}
		//数字框
		$('#nn1').numberbox({    
		    min:0,
		    width:240,
		    precision:2
		});  
		$('#nn3').numberbox({    
		    min:0,
		    width:240,
		    precision:2
		}); 
		$('#nn4').numberbox({    
		    min:0,
		    width:240,
		    precision:0
		});
		//提交开始禁止验证
		$("#standard-update").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#standard-update").form("enableValidation");
			if($("#standard-update").form("validate")){
				//ajax提交
				$("#standard-update").form("submit",{
					url:'standard/standardindexAction_updateStandardIndex.action',
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
							$("#standard-update").form("reset");
							parent.$("#standard-win").window("close");
							var url='standard/standardindexAction_queryPart.action';
							parent.$("#standard").treegrid("options").url = url;
							parent.$('#standard').treegrid('reload',{
			 	        		standardSn:parent.$('#cc2').combobox('getValue')       		
			 	        	});
						}else{
							parent.$.messager.alert("提示信息",msg,'error');
						}
						
					}
				});
			}
		});
		
	})
</script>
</head>
<body>
	<form id="standard-update" method="post">
		<input type="hidden" name="id"/>
		<div class="input">
			<lable for="indexSn">指  标  编  号：</lable>
			<input class="easyui-textbox" name="indexSn" data-options="required:true,missingMessage:'指标名称必填!',width:240"/>
		</div>
		<div class="input">
			<lable for="parentindexSn">父  级  编 号：</lable>
			<input class="easyui-textbox" name="parentindexSn" data-options="prompt:'顶级标准不填！',width:240"/>
		</div>
		<div class="input">
			<lable for="indexName">指  标  名  称：</lable>
			<input class="easyui-textbox" name="indexName" data-options="multiline:true,required:true,missingMessage:'指标名称必填!',height:66,width:240"/>
		</div>
		<div class="input">
			<lable for="auditKeyPoints">审  核  要  点：    </lable>
			<input class="easyui-textbox" name="auditKeyPoints" data-options="multiline:true,height:66,width:240"/>
		</div>
		<div class="input">
			<lable for="percentageScore">百 分制分数：  </lable>
			<input id="nn1" class="easyui-textbox" name="percentageScore"/>
		</div>
		<div class="input">
			<lable for="specialitySn">所  属 专 业：    </lable>
			<input id="cc1" name="specialitySn"/>
		</div>
		<div class="input">
			<lable for="isKeyIndex">是否关键指标:</lable>
			<input id="cc2" name="isKeyIndex"/>
				
		</div>
		<div class="input">
			<lable for="anDeduction">单  次  扣  分：    </lable>
			<input id="nn3" class="easyui-textbox" name="anDeduction"/>
		</div>
		<div class="input">
			<lable for="zeroTimes">几  次  扣  完：    </lable>
			<input id="nn4" class="easyui-textbox" name="zeroTimes"/>
		</div>
		<div class="input" id="button">
		<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">确认修改</a> 			
		</div>
	</form>
</body>
</html>