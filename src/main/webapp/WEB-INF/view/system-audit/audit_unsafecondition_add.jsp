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
		function Display(v){
			if(v=="立即整改"){
				$("#lastdate").css("display","none");
				$('#correctDeadline').datetimebox({      
				    required: false    
				});  

			}else{
				$("#lastdate").css("display","");
				$('#correctDeadline').datetimebox({      
				    required: true,
				    editable:false,
				    onChange:function(newValue, oldValue){
				    	var time=$('#datebox').datetimebox('getValue');
				    	if(newValue!=null&&newValue.length>0){
				    		if(time!=null&&time.length>0){
					    		//var checkTime = new Date(time);
				    			var deadLine=new Date(newValue);
						    	//checkTime.setTime(checkTime.getTime()+24*60*60*2000);
								//if(checkTime<deadLine){
									//$.messager.alert('我的提示','整改期限不得超过检查时间48小时！','warning',function(){$('#correctDeadline').datetimebox('clear');}); 
								//}else 
								if(deadLine<new Date(time)){
									$.messager.alert('我的提示','整改期限不得早于检查时间！','warning',function(){$('#correctDeadline').datetimebox('clear');}); 					
								}
						    }else{
						    	$.messager.alert('我的提示','请先选择检查时间！','info',function(){$('#correctDeadline').datetimebox('clear');});
							}
				    	}
					}
				});
			}
	}
	$(function(){
		var rows=parent.$('#dg').datagrid('getChecked');
		var principalId="";
		var principalName="";
		var personData=[];
		for(var i=0;i<rows[0].auditors.num;i++){
			var personIds=rows[0].auditors.Ids.split(",");
			var personNames=rows[0].auditors.personNames.split(",");
			personData.push({"id":personIds[i],"personName":personNames[i]});
		}
		$('#btn2').click(function(){
            if($('#j3').css('display')=="none"){
            	 $('#cc10').combotree({
                    url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_indexTree.action',
                 	editable:false,
     				queryParams:{'standardSn':rows[0].standardSn},
     				panelHeight:250,
     				prompt:'下拉选择标准！',
     				formatter:function(node){
     					return '<span title="'+ node.text+'">'+node.text+'</span>';
     				},
     				onSelect:function(row){
						//alert(row.indexId);
						$('#cc3').combogrid('setValue',{'id':row.indexId,'indexName':row.text});
		            	if(row.isKeyIndex==true){
		            		$("#cc9").combobox('setValue','严重不符合项');
		            	}else{
		            		$("#cc9").combobox('clear');
		            	}
		            	//专业
		            	var data=$('#cc5').combobox('getData');
		            	if(row.speciality.num>0){
		            		var spSn=row.speciality.specialitySn.split(',');
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
            	 $('#j3').css('display','');
            }else{
            	$('#j3').css('display','none');
            }
        });
		//检查时间
		$('#datebox').datetimebox({
			required:true,
			editable:false,
			onChange:function(newValue, oldValue){
				$('#correctDeadline').datetimebox('clear');
			}
		});
		//不符合项性质
		$('#cc2').combobox({
			valueField: 'label',
			textField: 'value',
			required:true,
			editable:false,
			panelHeight:'auto',
			data: [{
				label: '环境',
				value: '环境'
				},{
				label: '机器设备',
				value: '机器设备'
				},{
				label: '行为',
				value: '行为'
				},{
				label: '过程',
				value: '过程'
				},{
				label: '记录和数据',
				value: '记录和数据'
				},{
				label: '文件',
				value: '文件'
				}],
		    onSelect:function(rec){
					if(rec.value=='机器设备'){
						$("#i2").css("display",'');
						$("#j2").combotree({
							url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getMachine',
							queryParams:{departmentSn:$('#cc6').combotree('getValue')},
							required:true,
						    editable:false,
						    panelHeight:250,
						})
					}else{
						$("#i2").css("display",'none');
						$("#j2").combobox({
							required:false
						})
					}
		    }
		}); 
		//对应标准
		$('#text').textbox({
			multiline:true,
			required:true
		});
		$("textarea",$("#text").next("span")).blur(function(){
			$('#cc3').combogrid('grid').datagrid('reload',{'q':$('#cc3').combogrid('getText'),'text':$('#text').val(),'standardSn':rows[0].standardSn});
		});	
		$('#cc3').combogrid({          
			idField:'id',    
   	 		textField:'indexName',    
    		url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getStandardIndex.action', 
    		delay: 500,    
		    prompt:'输入名称或编号搜索相应标准',
		    fitColumns:true,
		    nowrap:false,
			onChange:function(newValue, oldValue){
				//var value=$('#text').val()+$('#cc3').combogrid('getText');
				$('#cc3').combogrid('grid').datagrid('reload',{'q':$('#cc3').combogrid('getText'),'text':$('#text').val(),'standardSn':rows[0].standardSn});
        	},
        	rowStyler:function(index,row){
  				if(row.isKeyIndex==true){
  					return 'background-color:#FF6347;color:#000;';    // rowStyle是一个已经定义了的ClassName(类名)
  				}
  			},
		    columns:[[    
		        		{field:'id',title:'逻辑主键',hidden:true},    
		        		{field:'indexSn',title:'指标编号',width:70,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},    
		        		{field:'indexName',title:'指标名称',width:200,formatter:function(value,row,index){
			        		if(value!=null){
			        			return "<span title='" + value + "'>" + value + "</span>";  
				        	}
		        		}},
		        		{field:'isKeyIndex',title:'关键指标',width:50,formatter:function(value,row,index){
		        			if(row.isKeyIndex==true){
		        				return '是';
		        			}else if(row.isKeyIndex==false){
		        				return '否';
		        			}
		        		}},
		        		{field:'standardName',title:'所属标准',width:80,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}}
		    		]],
    		
			onSelect: function(index, row){ 
				var indexId=row.id;
            	//var url = '${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getHazard.action?indexId='+indexId;    
            	//$('#cc4').combogrid('clear');
            	//$('#cc4').combogrid('grid').datagrid('reload',url);
            	if(row.isKeyIndex==true){
            		$("#cc9").combobox('setValue','严重不符合项');
            	}else{
            		$("#cc9").combobox('clear');
            	}
            	//专业
            	var data=$('#cc5').combobox('getData');
            	if(row.speciality.num>0){
            		var spSn=row.speciality.specialitySn.split(',');
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
		var methodnumber=0;
		//不符合项专业
		$('#cc5').combobox({    
			//url:'${pageContext.request.contextPath}/inconformity/item/specialityAction_query',
			panelHeight:250,
		   	valueField:'specialitySn',    
		    textField:'specialityName',
		    editable:false	 
		});
		$('#cc6').combotree({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment',
			required:true,
			editable:false,
			queryParams:{'departmentSn':rows[0].auditedDepartment.departmentSn},
			panelHeight:250,
			onChange:function(newValue, oldValue){
				//机隐藏
				$('#cc2').combobox('clear');
				$("#i2").css("display",'none');
				$("#j2").combobox({
					required:false
				})
				
				$('#cc5').combobox('options').url='${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryByDepartmentSn.action';
				$('#cc5').combobox('reload',{'departmentSn':newValue});
				$.ajax({
					type: "POST",
		            url: "${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryPrincipal",
		            data: {'departmentSn':newValue},
		            dataType: "json",
		            success:function(data){ 
						if(data.isnull==false){
							principalId=data.personId;
							principalName=data.personName;
							$('#cc8').textbox({
								value:principalName,
								editable:false
							});
						}else{
							principalId="";
							principalName="";
							$('#cc8').textbox({
								value:'',
								editable:false
							});
						}
			        }
				});
			}
		});
		$('#cc6').combotree('setValue',[{id:rows[0].auditedDepartment.departmentSn,text:rows[0].auditedDepartment.departmentName}]);
		//检查人员
		$('#cc7').combobox({       
   			valueField:'id',    
    		textField:'personName',
    		multiple:true,
    		editable:false,
    		panelHeight:150,
    		data:personData
   			//url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getPersonByIds.action',
   			//queryParams: {"ids":pIds}
		});
		//提交
		$('#reset').click(function(){
			$("#ff").form("clear");
			$("#lastdate").css("display","none");
			$('#correctDeadline').datetimebox({      
			    required: false,
			    value:'',
			    editable:false
			});
			$("#i2").css("display",'none');
			$("#j2").combobox({
				required:false
			});
			$("#cc6").textbox('setValue',rows[0].auditedDepartment.departmentName);
		});
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			//开启验证
			$("#ff").form("enableValidation");
			//将检查人员数组处理一下
			var personIds="";
			var cc7=$('#cc7').combogrid('getValues');
			for(var i=0;i<cc7.length;i++){
				personIds+=cc7[i]+","
			}
			personIds=personIds.substring(0,personIds.length-1);

			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_save.action',
					queryParams:{'personId':principalId,'ids':personIds,isConfirm:"false",'id':rows[0].id},
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						var isrepeat=result.isrepeat;
						if(status=="ok"){ 
					        	$.messager.show({
									title:'我的消息',
									msg:msg,
									showType:'show',
									timeout:1000,
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop+150,
										bottom:''
									}
								});
								$("#ff").form("disableValidation");													
								//不符合项性质
								$('#cc2').combobox('clear');
								//问题描述
								$('#text').textbox('clear');
								//对应标准
								$("#cc3").combogrid('clear');
								$("#cc3").combogrid('grid').datagrid('loadData',[]);
								//不符合项专业
								$('#cc5').combobox('clear');
								//不符合项等级
								$('#cc9').combobox('clear');
								//整改建议
								$('#correctProposal').textbox('clear');
								//立即整改
								$('#radio').attr('checked', false);
								$("#lastdate").css("display","none");
								$('#correctDeadline').datetimebox({      
								    required: false,
								    value:'',
								    editable:false
								});
								$("#i2").css("display",'none');
								$("#j2").combobox({
									required:false
								})
							parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("reload");
						}else if(isrepeat=="true"){
							$.messager.confirm('确认对话框', msg, function(r){
								if (r){
									$('#ff').form('submit', {
										url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_save.action',
										queryParams:{'personId':principalId,'ids':personIds,isConfirm:"true",id:rows[0].id},
										success: function(result){    
									        var secondresult = eval('(' + result + ')');
									        var secondstatus=secondresult.status;
									        var secondmsg=secondresult.message;
									        if (secondstatus=="ok"){    
										        	$.messager.show({
														title:'我的消息',
														msg:secondmsg,
														showType:'show',
														timeout:1000,
														style:{
															right:'',
															top:document.body.scrollTop+document.documentElement.scrollTop+150,
															bottom:''
														}
													});
													$("#ff").form("disableValidation");													
													//不符合项性质
													$('#cc2').combobox('clear');
													//问题描述
													$('#text').textbox('clear');
													//对应标准
													$("#cc3").combogrid('clear');
													$("#cc3").combogrid('grid').datagrid('loadData',[]);
													//不符合项专业
													$('#cc5').combobox('clear');
													//不符合项等级
													$('#cc9').combobox('clear');
													//整改建议
													$('#correctProposal').textbox('clear');
													//立即整改
													$('#radio').attr('checked', false);
													$("#lastdate").css("display","none");
													$('#correctDeadline').datetimebox({      
													    required: false,
													    value:'',
													    editable:false
													});
													$("#i2").css("display",'none');
													$("#j2").combobox({
														required:false
													})
												parent.$('#dg').datagrid('reload');
												parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("reload");    
									        }else{
									        	parent.$.messager.alert("提示信息",secondmsg,'error');
									        }    
									    }    
									}); 
								}
							});
							
						}else{
							parent.$.messager.alert("提示信息",msg,'error');
						}				
					}
				});
			}
		});
		$('#datebox').datetimebox('calendar').calendar({
			validator:function(date){
				var mydate = new Date();
				if(mydate>=date){
					return true;
				}else{
					return false;
				}
			}
		});
		
	})

</script>
</head>
<body>
	<div id="w" style="padding: 5px;">
	<form method="post" id="ff">
		<table>
			<tr>
				<td class="label" width="130px">被检单位/部门：</td>
				<td>
					<input id="cc6" name="departmentSn" class="easyui-combotree" style="width: 200px;">
				</td>
				<td class="label" width="130px">检查时间：</td>
				<td width="200px">
					<input id="datebox" name="checkDateTime" class="easyui-datetimebox" data-options="required:true,editable:false" style="width:190px">
				</td>
			</tr>
			<tr>
				<td class="label">不符合项性质：</td>
				<td>	
					<input id="cc2" name="inconformityItemNature" style="width: 200px;"/>				
				</td>
				
				<td class="label">检查地点：</td>
				<td>	
					<input name="checkLocation" class="easyui-textbox" style="width:190px;">				
				</td>
			</tr>
			<tr id="i2" style="display:none">
				<td class="label">机器设备：</td>
				<td><input id="j2" name="manageObjectSn" class="easyui-combobox" style="width: 200px;"></td>
			</tr>
			<tr>
				<td class="label">检查成员：</td>
				<td colspan="3">
					<input id="cc7" style="width:530px;">
				</td>
			</tr>
			<tr>
				<td class="label">问题描述：</td>
				<td colspan="3">
					<input id="text" name="problemDescription" class="easyui-textbox" data-options="multiline:true" style="width:530px; height:44px;">
				</td>
			</tr>
			<tr>
				<td class="label">对应标准：</td>
				<td colspan="3">
					<input name="indexId" id="cc3" style="width: 530px;"/>
				</td>
			</tr>
			<tr>
				<td></td>
				<td class="label"><a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">没搜到标准？点我查找</a></td>
				<td id="j3" colspan="2" style="display: none">
					<input id="cc10" style="width:320px;"/>
				</td>
			</tr>
			
			<tr>
				<td class="label">不符合项专业：</td>
				<td>
					<input id="cc5" name="specialitySn" style="width: 200px;">  
				</td>
				<td class="label">不符合项等级：</td>
				<td>
					<input id="cc9" class="easyui-combobox" name="inconformityLevel" style="width:190px;" data-options="
							valueField: 'label',
							textField: 'value',
							required:true,
		    				editable:false,
		   					panelHeight:'auto',
							data: [{
							label: '观察项',
							value: '观察项'
							},{
							label: '一般不符合项',
							value: '一般不符合项'
							},{
							label: '严重不符合项',
							value: '严重不符合项'
							}]" />
				</td>
			</tr>
			<tr>
				<td class="label">整改类型：</td>
				<td>
					<input type="radio" name="correctType" value="立即整改" onClick="Display(this.value)" checked>立即整改
					<input type="radio" id="radio" name="correctType" value="限期整改" onClick="Display(this.value)">限期整改
				</td>
				<td class="label">整改负责人：</td>
				<td>
					<input id="cc8" class="easyui-textbox" readonly style="width: 190px;"/>
				</td>
			</tr>
			<tr id="lastdate" style="display:none">
				<td class="label">整改期限：</td>
				<td>
					<input name="correctDeadline" id="correctDeadline" class="easyui-datetimebox" style="width: 200px">
				</td>
			</tr>
			<tr>
				<td class="label">整改建议：</td>
				<td colspan="3">
					<input name="correctProposal" id="correctProposal" class="easyui-textbox" style="width:530px;height:44px;" 
						data-options="multiline:'true'">
				</td>
			</tr>
		</table>
		<div id="ft" style="padding: 5px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
		</div>
	</form>
	</div>
</body>
</html>