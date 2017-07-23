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
			    required: false,
			    value:'',
			    editable:false
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
				    		var checkTime = new Date(time);
			    			var deadLine=new Date(newValue);
							if(deadLine<checkTime){
								$.messager.alert('我的提示','整改期限不得早于检查时间！','warning',function(){$('#correctDeadline').datetimebox('clear');}); 					
							}
					    }else{
					    	$.messager.alert('我的提示','请先选择检查时间！','info',function(){$('#correctDeadline').datetimebox('clear');});
						}
			    	}
				} 
			});
		}
	};
	function toDoThing(){
		$.ajax({
			   type: "POST",
			   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
			   success: function(msg){
				   var rec=eval('(' + msg + ')');
				   //console.log(parent.parent.$('#mm21').attr('id'));
				   parent.parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
				   parent.parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
				   parent.parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
				   parent.parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
				   parent.parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
			   }
		});
	};
	$(function(){
		var rows=parent.$("#dg").datagrid("getSelections");
		var rows2=parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("getSelections");
		var sIds="";
		if(rows[0].specialities.num>0){
			sIds=rows[0].specialities.sSn;
		}
		var principalId="";
		var principalName="";
		var cd2=false;
		var cd3=false;
		$('#btn2').click(function(){
            if($('#j3').css('display')=="none"){
            	 $('#cc10').combotree({
                     url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_indexTree.action',
                 	editable:false,
     				queryParams:{'standardSn':rows[0].standard.standardSn},
     				panelHeight:250,
     				prompt:'下拉选择标准！',
     				formatter:function(node){
     					return '<span title="'+ node.text+'">'+node.text+'</span>';
     				},
     				onSelect:function(row){
						//alert(row.indexId);
						$('#cc3').combogrid('setValue',{'id':row.indexId,'indexName':row.text});

						var indexId=row.indexId;
		            	var url = '${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getHazard.action?indexId='+indexId;
		            	$('#cc4').combogrid('clear');
		            	$('#cc4').combogrid('grid').datagrid('options').url=url;
		            	$('#cc4').combogrid('grid').datagrid('reload');
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
		            	//审核方法
		            	$('#cc1').combobox("clear");
		            	methodnumber=0;
		            	var url="${pageContext.request.contextPath}/standard/auditmethodAction_query?id="+row.indexId;
		            	$('#cc1').combobox('reload', url);
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
		    onChange:function(rec){
		    	var value=$('#cc2').combobox('getValue');
					if(value=='机器设备'){
						$("#i2").css("display",'');
						var dpt=$('#cc6').combotree('getValue');
						if(dpt==null || dpt.length==0){
							dpt=rows2[0].checkedDepartment.departmentSn;
						}
						$("#j2").combotree({
							url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getMachine',
							queryParams:{departmentSn:dpt},
							required:true,
						    editable:false,
						    panelHeight:250,
						    onLoadSuccess:function(){
						    	if(cd3==false){
						    		$("#j2").combotree('setValue',{id:rows2[0].machine.manageObjectSn,text:rows2[0].machine.manageObjectName})
						    		cd3=true;
						    	}	
						    }
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
			//var text=$('#text').val()+$('#cc3').combogrid('getText');
			$('#cc3').combogrid('grid').datagrid('reload',{'q':$('#cc3').combogrid('getText'),'text':$('#text').val(),'standardSn':rows[0].standard.standardSn});
		});	
		//对应标准
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
				if($("#cc6").combotree('getValue').length>0){
					$('#cc3').combogrid('grid').datagrid('reload',{'q':$('#cc3').combogrid('getText'),'text':$('#text').val(),'standardSn':rows[0].standard.standardSn});
				}
        	},
		    columns:[[    
		        		{field:'id',title:'逻辑主键',hidden:true},    
		        		{field:'indexSn',title:'指标编号',width:50,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},    
		        		{field:'indexName',title:'指标名称',width:200,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
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
    		onSelect: function(index,row){ 
				var indexId=row.id;
            	var url = '${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getHazard.action?indexId='+indexId;
            	$('#cc4').combogrid('clear');
            	$('#cc4').combogrid('grid').datagrid('options').url=url;
            	$('#cc4').combogrid('grid').datagrid('reload');
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
            	//审核方法
            	$('#cc1').combobox("clear");
            	methodnumber=0;
            	var url="${pageContext.request.contextPath}/standard/auditmethodAction_query?id="+row.id;
            	$('#cc1').combobox('reload', url);
        	} 
		}); 
		var methodnumber=0;
		var methodnum=rows2[0].auditMethod.num;  
		var url="${pageContext.request.contextPath}/standard/auditmethodAction_query";
		if(methodnum>0){
			url+="?id="+rows2[0].standardIndex.id;
		}
		//审核方法
		$('#cc1').combobox({
			url:url,       
		    valueField:'id',   
		    textField:'auditMethodContent',
		    editable:false,
		    multiple:true,
		    panelHeight:'auto',
		    prompt:'请先选择标准，然后再选审核方法与出现次数！',
		    onLoadSuccess:function(){
		    	$('[name=method]').numberbox({
		    		min:1,
				    precision:0,
			    	prompt:'出现次数',
			    	onChange:function(newValue, oldValue){
				    	if(newValue!=null&&newValue!=''&&newValue!=0){
				    		$('#cc1').combobox('select', this.id);
					    }
						
				    }          
				});
				if(methodnum>0){
					for(var i=0;i<methodnum;i++){
						$('#'+methodId[i]+'').numberbox('setValue', count[i]);
					}
				}
			},
			formatter: function(row){
				methodnumber++;
				return '<input name="method" id="'+row.id+'" style="width:80px"/>&nbsp;'+methodnumber+"."+row.auditMethodContent;
			},
			onSelect:function(record){
				if($('#'+record.id+'').numberbox('getValue')==''){
					$('#'+record.id+'').numberbox('setValue', 1);
				}				
			},
			onUnselect:function(record){
				$('#'+record.id+'').numberbox('setValue', null);
			}
		});
		//审核方法回显
		
		if(methodnum>0){
			var count=rows2[0].auditMethod.count.split(',');
			var methodId=rows2[0].auditMethod.methodId.split(',');
			$('#cc1').combobox('setValues',methodId);		
		} 
		//对应危险源
		var hz2=rows2[0].hazard.isnull;
		$('#cc4').combogrid({ 
    		idField:'hazardSn',    
   	 		textField:'hazardDescription',
   	 		editable:false,
    		url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getHazard.action', 
    		prompt:'危险源根据所选标准进行加载',
    		onLoadSuccess:function(data){
				if(data.total>0){					
					if(hz2==false){
						$('#cc4').combogrid('setValue',[{hazardSn:rows2[0].hazard.hazardSn,hazardDescription:rows2[0].hazard.hazardDescription}]);
						hz2=true;
					}/* else{
						$(this).combogrid('grid').datagrid('selectRow', 0); 
					} */				
				}
			},
			onSelect:function(){
				var value=$("#cc4").combogrid('getValue');
				if(value!=''){
					var row=$("#cc4").combogrid('grid').datagrid('getSelected');
					if(row.riskLevel=='重大风险'){
						$("#cc9").combobox('setValue','严重不符合项');
					}else{
						var rowcc3=$('#cc3').combogrid('grid').datagrid('getSelected');
						if(rowcc3.isKeyIndex==false){
							$("#cc9").combobox('clear');
						}				
					}
				}
			},
			columns:[[    
		        		{field:'id',title:'逻辑主键',hidden:true},    
		        		{field:'hazardSn',title:'危险源（危害）编号',width:120,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},    
		        		{field:'hazardDescription',title:'危险源（危害）名称',width:330,formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}},
		        		{field:'riskLevel',title:'风险等级',formatter:function(value,row,index){
		        			return "<span title='" + value + "'>" + value + "</span>";  
		        		}}
		    ]]   
		});
		//不符合项专业
		$('#cc5').combobox({    
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getSpecialityByIds',
			queryParams:{'ids':sIds},
			panelHeight:250,
		   	valueField:'specialitySn',    
		    textField:'specialityName',
		    editable:false
		});
		//被检单位
		$("#cc6").combotree({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment.action',
			required:true,
			queryParams:{'departmentSn':'1'},
			editable:false,
			panelHeight:250,
			onChange:function(newValue, oldValue){
				$('#j3').css('display','none');
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
				if(cd2==false){
					cd2=true;
				}else{
					//机隐藏
					$('#cc2').combobox('clear');
					$("#i2").css("display",'none');
					$("#j2").combobox({
						required:false
					})
				}
			}
		});		
		
		//数据回显
		// 对表单的数据进行填充
		$('#ff').form('load',{
			inconformityItemNature:rows2[0].inconformityItemNature,
			checkDateTime:rows2[0].checkDateTime,
			checkLocation:rows2[0].checkLocation,
			specialitySn:rows2[0].speciality.specialitySn,
			problemDescription:rows2[0].problemDescription,
			inconformityLevel:rows2[0].inconformityLevel,
			correctProposal:rows2[0].correctProposal,
			strCheckers:rows2[0].strCheckers
		});
		
		//被检部门回显
		var cd=rows2[0].checkedDepartment.isnull
		if(cd==false){
			var departmentSn=rows2[0].checkedDepartment.departmentSn;
			var departmentName=rows2[0].checkedDepartment.departmentName;
			$("#cc6").combotree('setValue', {id:departmentSn,text:departmentName});
		}
		//指标回显
		var st=rows2[0].standardIndex.isnull;
		if(st==false){
			$('#cc3').combogrid('grid').datagrid('reload',{q:rows2[0].standardIndex.id,advancedSearch:true});
			$('#cc3').combogrid('setValue', [{id:rows2[0].standardIndex.id,indexName:rows2[0].standardIndex.indexName}]);
		}
		//对应危险源
		var hz=rows2[0].hazard.isnull;
		if(hz==false){
			var url = '${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getHazard.action?indexId='+rows2[0].standardIndex.id;
        	//$('#cc4').combogrid('clear');
        	$('#cc4').combogrid('grid').datagrid('options').url=url;
        	$('#cc4').combogrid('grid').datagrid('reload');
			//$('#cc4').combogrid('grid').datagrid('reload',{'indexSn':rows[0].standardIndex.indexSn});
			$('#cc4').combogrid('setValue',[{hazardSn:rows2[0].hazard.hazardSn,hazardDescription:rows2[0].hazard.hazardDescription}]);
		}
		//整改类型回显
		if(rows2[0].correctType=="限期整改"){
			$("#ct2").attr("checked","checked");
			$("#lastdate").css("display","");
			$('#correctDeadline').datetimebox({      
			    required: true,
			    value:rows2[0].correctDeadline,
			    editable:false,
			    onChange:function(newValue, oldValue){
			    	var time=$('#datebox').datetimebox('getValue');
			    	if(newValue!=null&&newValue.length>0){
			    		if(time!=null){
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
		}else if(rows2[0].correctType=="立即整改"){
			$("#ct1").attr("checked","checked");
			$("#correctDeadline").attr("value","");
		}		
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
			})
		});
		//开始禁止验证
		$("#ff").form("disableValidation");
		$("#submit").click(function(){
			if($('#cc3').combogrid('getValue')!=null&&$('#cc3').combogrid('getValue').length>0){
				var ccc1=$('#cc1').combobox('getValues');
				if(ccc1!=null&&ccc1.length>0){
					
				}else{
					$('#cc1').combobox({
	            		required:true
	            	});
				}
			}else{
				$('#cc1').combobox({
            		required:false
            	});
			}
			//开启验证
			$("#ff").form("enableValidation");
			//将审核方法数据处理
			var methods="";
			var cc1=$('#cc1').combobox('getValues');
			for(var i=0;i<cc1.length;i++){
				methods+=cc1[i]+"#"+$('#'+cc1[i]+'').val()+","
			}
			methods=methods.substring(0,methods.length-1);
			if($("#ff").form("validate")){
				//ajax提交
				$("#ff").form("submit",{
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_update.action',
					queryParams:{'personId':principalId,'methods':methods,'inconformityItemid':rows2[0].id},
					success:function(data){
						var result = eval('(' + data + ')');
						var status=result.status;
						var msg=result.message;
						if(status=="ok"){
							parent.$.messager.alert("提示信息",msg);
							$("#ff").form("reset");
							parent.$("#child-win").window("close");
							parent.$("iframe").get(0).contentWindow.$("#d-i").datagrid("reload");
							toDoThing();
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
					<input id="cc7" class="easyui-textbox" style="width:530px;" name="strCheckers"
						data-options="required:true,
								height:44,
								prompt:'不同人员用“，”隔开',
								multiline:true
								"/>
				</td>
			</tr>
			<tr>
				<td class="label">问题描述：</td>
				<td colspan="3">
					<input id="text" name="problemDescription" class="easyui-textbox" style="width:530px; height:44px;"
						data-options="prompt:'上标使用：<sup>上标</sup>；下标使用：<sub>下标</sub>；',multiline:true">
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
			<!-- 审核方法 -->
			<tr>
				<td class="label">审核方法：</td>
				<td colspan="3">
					<input  id="cc1" style="width: 530px;"/>
				</td>
			</tr>
			<tr>
				<td class="label">对应危险源：</td>
				<td colspan="3">
					<input name="hazardSn" id="cc4" style="width:530px;"/>
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
					<input id="ct1" type="radio" name="correctType" value="立即整改" onClick="Display(this.value)" checked>立即整改
					<input id="ct2" type="radio" name="correctType" value="限期整改" onClick="Display(this.value)">限期整改
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
					<input name="correctProposal" class="easyui-textbox" style="width:530px;height:44px;"
						data-options="multiline:'true'">
				</td>
			</tr>
		</table>
		<div id="ft" style="padding: 5px; text-align: center;">
			<a id="submit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
			<a id="reset" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
		</div>
	</form>
	</div>
</body>
</html>