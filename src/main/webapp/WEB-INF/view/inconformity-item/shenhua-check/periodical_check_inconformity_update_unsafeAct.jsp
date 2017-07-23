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
		.tr-height{height:35px;}
</style>
<script type="text/javascript">
	function selectPerson(){
		var violatorValue=$('#violator').combogrid('getValue');
		if($.trim(violatorValue)!="" && violatorValue!=null){
			return true;
		}else{
			return false;
		}
	};
	$(function(){
		var a=1;
		var b=1;
		var rows=parent.$("iframe").get(0).contentWindow.$("#dg2").datagrid("getSelections");
		var actDescriptionSn="";
		//行为描述
		$('#actDescription').textbox({
			multiline:true,
			onChange:function(newValue, oldValue){
				if(selectPerson()==true){
					actDescriptionSn=newValue;
					//$('#cc3').combogrid('clear');
					$('#cc3').combogrid('grid').datagrid('load',{'q':$('#cc3').combogrid('getValue'),'actDescription':actDescriptionSn,'violatorId':$('#violator').combogrid('getValue')});
				}else{
					$.messager.alert('提示','请先选择不安全行为人员！');
				}
			}
		});
		//对应标准
		$('#cc3').combogrid({          
    		idField:'id',
   	 		textField:'text',    
    		url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_findStandard.action', 
    		delay: 500, 
    		required:true,
    		panelHeight:180,
		    //mode:'remote',
		    prompt:'输入名称或编号搜索相应标准',
		    columns:[[    
		        		{field:'id',title:'标准编号',width:95},    
		        		{field:'text',title:'标准名称',width:430}
		    		]],
			onBeforeLoad:function(){
				   if(actDescriptionSn=="" && $('#cc3').combogrid('getValue')==""){
				    	return false;
				   }else{
				    	return true;
				   }
			},
        	onChange:function(newValue, oldValue){
        		if(selectPerson()==true){
	        		if(newValue==null||newValue==""){
	        			$('#unsafeActLevel').textbox('setValue',"");
	        		}
	        		$('#cc3').combogrid('grid').datagrid('load',{'q':newValue,'actDescription':actDescriptionSn,'violatorId':$('#violator').combogrid('getValue')});
	        		
				}else{
					$('#cc3').combogrid('hidePanel');
					$.messager.alert('提示','请先选择不安全行为人员！');
				}
        	},
			onSelect: function(index, row){
				//不安全行为等级
//				console.log(row);
				$('#unsafeActLevel').textbox('setValue',row.value);
            	//专业
             	if(row.speciality!=null && row.speciality!=""){
             		var spSn=row.speciality.split(',');
             		$('#cc5').combobox({
             			formatter: function(row){
                     			if($.inArray(row.specialitySn, spSn)!=-1){
                     				return '<span style="color:red">' + row.specialityName + '</span>';
                     			}else{
                     				return '<span>' + row.specialityName + '</span>';
                     			}           				
             			}
             		});
             	}else{
            		$('#cc5').combobox({
            			formatter: function(row){
                    		return '<span>' + row.specialityName + '</span>';         				
            			}
            		});
             	}
        	}
		});
		
		//不安全行为专业
		$('#cc5').combobox({    
			url:'${pageContext.request.contextPath}/inconformity/item/specialityAction_query',
			panelHeight:200,
			required:true, 
		   	valueField:'specialitySn',    
		    textField:'specialityName',
		    editable:false	 
		});
		//被检单位
		var department="";
		if(rows[0].checkerFrom=="单位"){
			$("#cc6").combotree({
				url:'${pageContext.request.contextPath}/department/treeAll.action',
				required:true,
				editable:false,
				queryParams:{'resourceSn':'01'},
				panelWidth: 300,
				panelHeight:340,
				onLoadSuccess:function(node,data){
					if(a==1 && data.length>0){
						department=$('#cc6').combotree('tree').tree('getRoot').id;
						a++;
						if(data.length == 1){
							var node = $('#cc6').combotree('tree').tree('find', data[0].id);
			        		$('#cc6').combotree('tree').tree('expand', node.target);
			        	}
					}
				},
				onSelect:function(rec){
					department=rec.id;
					$('#violator').combogrid('clear');
				}
			});
		}else{
			$("#cc6").combotree({
				url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment.action',
				required:true,
				editable:false,
				queryParams:{'departmentSn':'1'},
				panelWidth: 300,
				panelHeight:340,
				onLoadSuccess:function(node,data){
					if(a==1 && data.length>0){
						department=$('#cc6').combotree('tree').tree('getRoot').id;
						a++;
						if(data.length == 1){
							var node = $('#cc6').combotree('tree').tree('find', data[0].id);
			        		$('#cc6').combotree('tree').tree('expand', node.target);
			        	}
					}
				},
				onSelect:function(rec){
					department=rec.id;
					$('#violator').combogrid('clear');
				}
			});
		}
		//不安全行为人员
		$('#violator').combogrid({    
			delay: 500,
			required:true,
			width:200,
		    panelWidth:200,
		    prompt:'请输入搜索信息',
   		 	iconAlign:'right',
		    idField:'personId',    
		    textField:'personName',    
		    url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showPerson.action',
    		columns:[[
				        {field:'personId',title:'编号',width:90},    
				        {field:'personName',title:'姓名',width:108}
			]],
			onChange:function(newValue, oldValue){
				$('#violator').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':department});
			},
			onSelect:function(){
				$('#actDescription').textbox('clear');
				$('#cc3').combogrid('clear');
			}
		});
		//检查时间
		$('#checkDateTime').datetimebox('calendar').calendar({
			validator:function(date){
				var mydate = new Date();
				if( mydate >= date && (mydate - date) <= 48 * 60 * 60 * 1000){
					return true;
				}else{
					return false;
				}
			}
		});

		$('#btn2').click(function(){
			if(selectPerson()==true){
	            if($('#j3').css('display')=="none"){
	            	 $('#cc10').combotree({
	                    url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_findAllStandard.action',
	                 	editable:false,
	     				queryParams:{'violatorId':$('#violator').combogrid('getValue')},
	     				panelHeight:200,
	     				prompt:'下拉选择标准！',
	     				formatter:function(node){
	     					return '<span title="'+ node.text+'">'+node.text+'</span>';
	     				},
	     				onSelect:function(row){
							$('#cc3').combogrid('setValue',{'id':row.id,'text':row.text});

							$.ajax({
								   type: "POST",
								   url: '${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_questLevel.action',
								   data:{'id':row.id},
								   success: function(msg){
									   var rec=eval('(' + msg + ')');
									   //console.log(rec[0].level);
									   //console.log(rec[0].speciality);
									   $('#unsafeActLevel').textbox('setValue',rec[0].level);
										//专业
						             	if(rec[0].speciality!=null && rec[0].speciality!=""){
						             		var spSn=rec[0].speciality.split(',');
						             		$('#cc5').combobox({
						             			formatter: function(row){
						                     			if($.inArray(row.specialitySn, spSn)!=-1){
						                     				return '<span style="color:red">' + row.specialityName + '</span>';
						                     			}else{
						                     				return '<span>' + row.specialityName + '</span>';
						                     			}           				
						             			}
						             		});
						             	}else{
						            		$('#cc5').combobox({
						            			formatter: function(row){
						                    		return '<span>' + row.specialityName + '</span>';         				
						            			}
						            		});
						             	}
								   }
							});
	         			}
	                 });
	            	 $('#j3').css('display','');
	            }else{
	            	$('#j3').css('display','none');
	            }
			}else{
				$.messager.alert('提示','请先选择不安全行为人员！');
			}
        });
		//数据回显
		//不安全行为人员回显
		var cp=rows[0].violator;
		if($.trim(cp)!=null && $.trim(cp)!=""){
			$('#violator').combogrid('setValue', [{personId:rows[0].violatorId,personName:cp}]);
		}
		$('#ff').form('load',{
			id:rows[0].id,
			ids:rows[0].checkers,
			checkDateTime:rows[0].checkDateTime,
			checkLocation:rows[0].checkLocation,
			specialitySn:rows[0].specialitySn,
			unsafeActMark:rows[0].unsafeActMark,
			unsafeActLevel:rows[0].unsafeActLevel,
			actDescription:rows[0].actDescription
		});
		//1、被检单位
		var deptName=rows[0].checkedDepartment;
		var deptSn=rows[0].checkedDepartmentSn;
		if($.trim(deptName)!=null && $.trim(deptName)!=""){
			$('#cc6').combotree('setValue',[{id:deptSn,text:deptName}]);
		}
		
		//对应标准数据回显
		var st=rows[0].unsafeActStandardSn;
		if($.trim(st)!=null && $.trim(st)!=""){
			//$('#cc3').combogrid('grid').datagrid('load',{'q':rows[0].unsafeActStandard});
			$('#cc3').combogrid('setValue', [{id:st,text:rows[0].unsafeActStandard}]);
		}
		//提交
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			

			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_updateUnsafeAct.action',
					success:function(data){
						var result = eval(data);
						if(result=="login"){
							$.messager.alert('警告','对应标准应选择末级标准，请重新选择');
							$('#cc3').combogrid('clear');
							$('#cc5').combobox('clear');
							$('#unsafeActLevel').textbox('clear');
						}else if(result=="success"){
							parent.$.messager.show({
								title:'提示',
								msg:'添加信息成功！',
								timeout:2000,
								showType:'slide'
							});	
	 						$("#ff").form("reset");
	 						parent.$("#child-win").window("close");
	 						parent.$("iframe").get(0).contentWindow.$("#dg2").datagrid("reload");
	 						parent.$("#dg").datagrid("reload");  
						}else{
							parent.$.messager.alert("警告","添加异常，请重新操作！",'error');
							$("#ff").form("reset");
							parent.$("#child-win").window("close");
							parent.$("iframe").get(0).contentWindow.$("#dg2").datagrid("reload"); 
	 						parent.$("#dg").datagrid("reload"); 
						}
					}
				});
			}
		});
	})

</script>
</head>
<body>
	<div id="w" style="width:680px; height: 300px; padding: 10px;">
	<form method="post" id="ff">
		<input type="hidden" name="id">
		<table>
			<tr class="tr-height">
				<td class="label">被检单位/部门:</td>
				<td>
					<input id="cc6" name="checkedDepartmentSn" style="width: 200px;">
				</td>
				<td class="label">不安全行为人员:</td>
				<td >
					<input name="violatorId" id="violator" style="width: 200px;"/>
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">检查时间：</td>
				<td>
					<input id="checkDateTime" name="checkDateTime" class="easyui-datetimebox" 
						data-options="required:true,editable:false" style="width:200px">
				</td>
				<td class="label">检查地点:</td>
				<td>	
					<input name="checkLocation" class="easyui-textbox" style="width:200px;">				
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">检查成员:</td>
				<td colspan="3">
					<input id="cc7" class="easyui-textbox" style="width:530px;" name="ids"
						data-options="required:true,
										height:44,
										prompt:'不同人员用“,”隔开',
										multiline:true"/>
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">行为描述:</td>
				<td colspan="3">
					<input name="actDescription" id="actDescription" style="width:530px; height:44px;">
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">对应标准:</td>
				<td colspan="3">
					<input name="unsafeActStandardSn" id="cc3" style="width: 530px;"/>
				</td>
			</tr>
			<tr>
				<td></td>
				<td class="label"><a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">没搜到标准？点我查找</a></td>
				<td id="j3" colspan="2" style="display: none">
					<input id="cc10" style="width:320px;"/>
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">不安全行为专业:</td>
				<td>
					<input id="cc5" name="specialitySn" style="width: 200px;">  
				</td>
				<td class="label">不安全行为等级:</td>
				<td>
					<input id="unsafeActLevel" name="unsafeActLevel" type="text" style="width:200px"
						class="easyui-textbox" data-options="readonly:true">
				</td>
			</tr>
			<tr class="tr-height">
				<td class="label">不安全行为痕迹:</td>
				<td >
					<select id="unsafeActMark" class="easyui-combobox" name="unsafeActMark" 
						data-options="panelWidth:200,panelHeight:'auto',editable:false" style="width:200px;">   
					    <option value="有痕">有痕</option>   
					    <option value="无痕">无痕</option> 
					</select>
				</td>
			</tr>
		</table>
		<div id="ft" style="padding: 5px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">修改</a>
		</div>
	</form>
	</div>
</body>
</html>