<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>任务分配</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-cellediting.js"></script>
		<style type="text/css"> 
			.align-center{ 
				margin:10px 5px;	/* 居中 这个是必须的，，其它的属性非必须 */ 
 				width:250px; 		/*给个宽度 顶到浏览器的两边就看不出居中效果了 */ 
				text-align:left; 	/* 文字等内容居中 */ 
			} 
			.nextPage{
				word-wrap: break-word; 
				word-break: normal;
			}
		</style> 
		<script type="text/javascript">
		var departmentSn = "";
		var yearTime = "";
		var monthTime = "";
		var checked = true;
		var a = 1;
		//权限管理
		var str = "${sessionScope['permissions']}";
		var dateYear = new Array();//年度数组
		var nowDateMonth = new Array();//当前月份数组
		var dateMonth = new Array();//月份数组
		var date = new Date;//当前日期
		//创建年份下拉框
		for(var i = date.getFullYear(); i > 2014; i--){
			dateYear.push({"id":i,"text":i+"年"});
		};
		//创建当前月份下拉框
		for(var i = 1;i <= date.getMonth() + 1; i++){
			nowDateMonth.push({"id":i,"text":i+"月份"});
		};
		//创建往年月份下拉框
		for(var i = 1; i < 13; i++){
			dateMonth.push({"id":i,"text":i+"月份"});
		};
		//发送请求
		function loadAction(){
			$('#dg').datagrid('load',{
				'departmentSn':departmentSn,
				'yearTime':yearTime,
				'monthTime':monthTime,
				'checked':checked,
				'searchContext': $('#searchCheckTask').textbox('getValue')
			});
		};
		function toDoThing(){
			$.ajax({
				   type: "POST",
				   url: '${pageContext.request.contextPath}/person/findSessionActionP.action',
				   success: function(msg){
					   var rec=eval('(' + msg + ')');
					   //console.log(parent.$('#mm21').attr('id'));
					   parent.$('#toDoList').html("待办事项["+rec.countThing+"]");
					   parent.$('#mm24').html("检查任务["+rec.checkTask+"]");
					   parent.$('#mm21').html("需我整改的["+rec.correctCount+"]");
					   parent.$('#mm22').html("需我复查的["+rec.reviewCount+"]");
					   parent.$('#mm23').html("我的检查表["+rec.myCheckTable+"]");
				   }
			});
		};
		function showPersonDetail(personId){
			$('#personId').attr('value',personId);
			$('#win').window({
	 			width:900,
	 			height:400,
	 			title:"检查详情",
	 			cache:false,
	 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/office_showPersonDetail" frameborder="0" width="100%" height="100%"/>'
	 		});
		}
		$(function(){
			$('#selectbox').switchbutton({
				width:50,
				height:22,
				onText:'是',
				offText:'否',
			    checked: true, 
			    onChange: function(rec){ 
			      //console.log(rec); 
			      checked = rec;
			      loadAction();
			    } 
			});
			$('#department').combotree({
				url:'${pageContext.request.contextPath}/department/treeAll.action',//'${pageContext.request.contextPath}/office/appraisals/office_loadDepartment.action',
				valueField: 'id',    
		        textField: 'text',
				method:'post',
				queryParams:{'resourceSn':'1804'},
				required:true,
				editable:false,
				width:200,
				panelWidth: 300,
				panelHeight:350,
				onLoadSuccess:function(node,data){
					if(a==1){
						if(data.length!=0){
							//将根节点的值默认输出
							$('#department').combotree('setValue',data[0].id);
							departmentSn=$('#department').combotree('getValue');
							$('#dg').datagrid('load',{
								'departmentSn':departmentSn,
								'checked':checked,
								'yearTime':date.getFullYear(),
								'monthTime':date.getMonth()+1
							});
							if(data.length == 1){
								var node = $('#department').combotree('tree').tree('find', data[0].id);
								$('#department').combotree('tree').tree('expand', node.target);
				        	}
						}
						a++;
					}
				},
				onSelect:function(record){
					departmentSn=record.id;
					loadAction();
				} 
			});
			$('#selectYear').combobox({
				valueField:'id',    
				textField:'text',  
				panelHeight:150,
				editable:false,
				prompt:'选择年份',
				width:150,
				onLoadSuccess:function(node,data){
					if(node.length!=0){	
						//将根节点的值默认输出
						$('#selectYear').combobox('setValue',node[0].id);
						yearTime=node[0].id;
					}
				},
				onSelect:function(rec){
					yearTime=rec.id;
					if(rec.id<date.getFullYear()){
						$('#selectMonth').combobox('loadData',dateMonth);
					}else{
						$('#selectMonth').combobox('loadData',nowDateMonth);
					}
				}
			});
			$('#selectMonth').combobox({
				valueField:'id',    
				textField:'text',  
				panelHeight:200,
				editable:false,
				prompt:'选择月份',
				width:150,
				onLoadSuccess:function(node,data){
					if(node.length!=0){	
						//将根节点的值默认输出
						$('#selectMonth').combobox('setValue',node[0].id);
						monthTime=node[0].id;
						loadAction();
					}
				},
				onSelect:function(rec){
					monthTime=rec.id;
					loadAction();
				}
			});
			$('#searchCheckTask').textbox({
				width:200,
				buttonText:'搜索',    
			    iconCls:'icon-man', 
			    iconAlign:'left',
			    prompt:'请输入人员姓名或编号',
			    onChange:function(newValue,oldValue){
			    	loadAction();
			    }
			});
			$('#dg').datagrid({
	            url: '${pageContext.request.contextPath}/office/appraisals/office_loadData.action',
	            idField: 'id',
	            title:'任务分配',
	            toolbar:'#danwei',
	            rownumbers: true,		//显示一个行号列
	            fitColumns:true,		//自动适应列
	            //height:400,
	           	fit:true,				//表格宽高自适应
	            nowrap:false,
	            striped:true,			//斑马线效果
				singleSelect:true,		//单行选中
	            loadmsg:'请等待...',		//加载等待时显示
				clickToEdit:false,
				dblclickToEdit:true,
	            pagination:true,		//显示分页组件
	            pageNumber:1,
	            pageSize:10,
	            pageList:[5,10,15,20,25,30],
	      		columns:[[
					{field:'id',title:'主键',hidden:true},
					{field:'departmentSn',title:'部门编号',hidden:true},
					{field:'implDepartmentName',title:'贯标单位',width:80,align:'center'},
					{field:'departmentName',title:'部门名称',width:80,align:'center'},
					{field:'personId',title:'检查人编号',width:50,align:'center'},
					{field:'personName',title:'检查人姓名',width:50,align:'center'},
					{field:'checkTaskCount',title:'隐患检查任务数量(双击可编辑)',width:100,align:'center',editor:{type:'numberbox',options:{precision:0,min:0}}},
					{field:'unsafeActCheckTaskCount',title:'不安全行为检查任务数量(双击可编辑)',width:100,align:'center',editor:{type:'numberbox',options:{precision:0,min:0}}},
					{field:'realCheckCount',title:'隐患已完成数量',width:80,align:'center',formatter:function(value,row,index){
						if(value != 0){
							if(row.personId == "admin"){
								return '<a href = "javascript:" onclick=showPersonDetail("admin") style = "text-decoration:none">' + value + '</a>';
							}
							return '<a href = "javascript:" onclick=showPersonDetail('+ row.personId +') style = "text-decoration:none">' + value + '</a>';
						}else{
							return value;
						}
					}},
					{field:'unsafeActRealCheckCount',title:'不安全行为已完成数量',width:80,align:'center',formatter:function(value,row,index){
						if(value != 0){
							if(row.personId == "admin"){
								return '<a href = "javascript:" onclick=showPersonDetail("admin") style = "text-decoration:none">' + value + '</a>';
							}
							return '<a href = "javascript:" onclick=showPersonDetail('+ row.personId +') style = "text-decoration:none">' + value + '</a>';
						}else{
							return value;
						}
					}},
					{field:'yearMonth',title:'时间',width:50,align:'center',
						formatter: function(value,row,index){
							if (value!="" && value!=null){
								return value.substring(0,value.lastIndexOf("-")).replace("-","年")+"月";
							}
						}
					}
				]],
				onBeforeCellEdit:function(index,field){
					if(str.indexOf("180401")!=-1){
						return true;
					}else{
						$.messager.alert('我的消息','对不起！您没有编辑此记录的权限！','warning');
						return false;
					}
				},
				onEndEdit:function(index, row, changes){
// 					console.log(row);
// 					console.log(changes);
					$.ajax({
			             type: "POST",
			             url: "${pageContext.request.contextPath}/office/appraisals/office_updateData.action",
			             data: {'departmentSn':row.departmentSn,'checkerId':row.personId,'checkTaskCount':row.checkTaskCount,'unsafeActCheckTaskCount':row.unsafeActCheckTaskCount,'times':row.yearMonth},
			             dataType: "json",
			             success: function(data){
			            	 //console.log(data);
			            	 if(data!="success"){
			            		 $.messager.alert('错误消息','保存失败,请重新操作！','error');
			            	 }
		            		$('#dg').datagrid('reload');
		            		if(row.personId=="${sessionScope.personId}"){
			            		toDoThing();
		            		}
			             }
			         });
				},
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
				 },
				 onBeforeLoad:function(){
					 if(departmentSn=="" || departmentSn.length==0){
						 return false;
					 }else{
						 return true;
					 }
				 }
			}).datagrid('enableCellEditing');
			
			$('#selectYear').combobox('loadData',dateYear);
			$('#selectMonth').combobox('loadData',nowDateMonth);
			//$('#selectYear').combobox('setValue',date.getFullYear());
			$('#selectMonth').combobox('setValue',date.getMonth()+1);
			monthTime=date.getMonth()+1;
			
			if(str.indexOf("180404")==-1){
				$('#add').css('display','none');
			}else{
				$('#add').bind('click', function(){
					$('#win').window({
						title:"添加信息",
						width:480,
						height:200,
						content:'<iframe src="${pageContext.request.contextPath}/base/checkTask_add" frameborder="0" width="100%" height="100%" />'
					});
			    });
			}
			//删除按钮点击事件
			if(str.indexOf("180405")==-1){
				$("#remove").css('display','None');
			}else{
				$('#remove').bind('click',function(){
					 //判断是否有选中行记录
					var object = $('#dg').datagrid("cell");
					if( object == null || object.index == null ){
						$.messager.show({
							title:'提示信息',
							msg:'请点击一行记录',
							timeout:2000,
							showType:'slide'
						});														
					}else{
							var rows = $('#dg').datagrid('getRows')[object.index];
							$.messager.confirm('删除确认对话框', '是否要删除选中的记录', function(r){
								if (r){
									$.post("${pageContext.request.contextPath}/office/appraisals/office_deleteData.action",{ id : rows.id },function(result){
										if( result == "success" ){
												$.messager.show({
													title:'提示',
													msg:'成功删除记录！',
													timeout:2000,
													showType:'slide'
												});	
											$('#dg').datagrid("reload");
										}else{
											$.messager.alert('警告','删除失败，请检查后重新操作！');
										}
									},"json");
									$('#dg').datagrid("unselectAll");
								}
							});
						}
					});
			}
			if(str.indexOf("180401")==-1){
				$('#dg').datagrid('disableCellEditing');
			}
			
			if(str.indexOf("180402")==-1){
				$('#import').css('display','none');
			}else{
				//导入任务分配表按钮点击事件
				$('#import').bind('click', function(){
					$('#templateDialog').dialog('open');
			    });
			}
			
			if(str.indexOf("180403")==-1){
				$('#export').css('display','none');
			}else{
			    //导出任务分配表按钮点击事件
				$('#export').bind('click', function(){
					$('#exportCheckTask').dialog('open'); 
			    });
				$('#exportCheckTask').dialog({
					title:"导出任务分配表",
					width:300,
					height:150,
					modal:true,
					closed:true,
					buttons:[{
						text:'导出全部',
						handler:function(){
								$('#sub').form('submit', {
									url:'${pageContext.request.contextPath}/hazard/download_exportCheckTask.action',
									queryParams:{
											departmentSn:$('#department').combotree('getValue'),
											yearTime:$('#selectYear').combobox('getValue'),
											monthTime:$('#selectMonth').combobox('getValue'),
											checked:checked,
											id:"all"
									}
								});	
								$('#exportCheckTask').dialog('close'); 
								//进度条打开
								$('#p').progressbar('setValue',0);
								$('#winPro').window('open');
								getProValue();
						}
					},{
						text:'导出当前',
						handler:function(){
							$('#sub').form('submit', {
								url:'${pageContext.request.contextPath}/hazard/download_exportCheckTask.action',
								queryParams:{
									departmentSn:$('#department').combotree('getValue'),
									yearTime:$('#selectYear').combobox('getValue'),
									monthTime:$('#selectMonth').combobox('getValue'),
									checked:checked
								}	
							});	
								$('#exportCheckTask').dialog('close');
								//进度条打开
							$('#p').progressbar('setValue',0);
							$('#winPro').window('open');
							getProValue();
						}
					},{
						text:'关闭',
						handler:function(){$('#exportCheckTask').dialog('close'); }
					}]
				});
			}
					
					
			}); 
			//任务分配表模板下载
			function download_btn(){
				var url ='${pageContext.request.contextPath}/hazard/download_checkTaskDownLoad.action';
				  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
			};
			//ajax发送请求session--导入
			function getSession(){
				$.ajax({
					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
					success: function(data){
						$('#p').progressbar('setValue',data);
					}
				},"json");
			};
			//ajax发送请求session--导出
			function getProValue(){
				var timer = setInterval(function(){
					$.ajax({
						url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findProValue.action',
						success: function(data){
							$('#p').progressbar('setValue',data);
							if(data == 100){
								$.ajax({
				 					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_clearSession.action',
				 				});
								$('#winPro').window('close');
		                        clearInterval(timer);
		                    }
						}
					},"json");
				},100);
			};
			//管理对象导入
			function uploadExcel(){     
		         //得到上传文件的全路径  
		         var fileName= $('#uploadExcel').filebox('getValue');
		                 if(fileName==""){     
		                    $.messager.alert('提示','请选择上传文件！','info');   
		                 }else{  
		                     //对文件格式进行校验  
		                     var d1=/\.[^\.]+$/.exec(fileName);   
		                     if(d1==".xls"){
		                    	 
		                    	  //进度条显示
		                    	  $('#p').progressbar('setValue',0);
	                    	 	  $('#winPro').window('open');
								  var timer = setInterval(getSession,500);
								  
		                          //提交表单  
		                          $('#daoru').form('submit', {
										url:'${pageContext.request.contextPath}/hazard/manageObjectAction_importCheckTask.action',
										success:function(result){
											  $('#p').progressbar('setValue','100');
											  clearInterval(timer);
											  $('#winPro').window('close');
					                          $('#uploadExcel').filebox('setValue','');
					                          $('#templateDialog').dialog('close');
					                          $.messager.alert('提示',"<div class='align-center nextPage'>"+result.substring(1,result.length-1)+"</div>",'info',function(){
					                        	  $('#dg').datagrid('reload');
					                          });
										}
								  });
		                    }else{  
		                        $.messager.alert('提示','请选择xls格式文件！','info');   
		                        $('#uploadExcel').filebox('setValue','');   
		                    }  
		                 } 
		      };
			
		</script>
</head>
<body>
	<div id='danwei'>
		<span>
			<lable for="department">选择部门：</lable>
			<input id="department" />
		</span>
		<span>
			<lable for="selectYear">选择年份：</lable>
			<input id="selectYear" />
		</span>
		<span>
			<lable for="selectMonth">选择月份：</lable>
			<input id="selectMonth" />
		</span>
		<span>
			<lable for="selectbox">是否含下级部门：</lable>
			<input id="selectbox" style="width:200px;height:30px">
		</span>
		<span>
			<lable for="searchCheckTask">搜索：</lable>
			<input id="searchCheckTask" name="searchCheckTask" />
		</span>
			<a id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
			<a id="remove" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">删除</a>
			<a id="import" class="easyui-linkbutton" data-options="iconCls:'icon-excel'">导入任务分配表</a>
			<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-export'">导出任务分配表</a>
	</div>
		<table id="dg" ></table>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
		
		<!-- 任务分配表导出 -->
		<div id="exportCheckTask"  style="width: 200px;height:150px,font-size:40px,height:75px;line-height:75px;text-align:center">
				<form id="sub">
						<font >确认导出到Excel？请选择导出范围</font>
				</form>
		</div>
		<!-- 任务分配表导入 -->
		<div id="templateDialog" title="导入任务分配表" modal=true draggable=false
			class="easyui-dialog" closed=true  style="width: 350px;height:220px">
			<form id="daoru" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>下载模板：</td>
						<td>
							<a class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="download_btn()">下载导入模板</a> 
						</td>
					</tr>
					<tr>
    					<td>选择文件：</td>
    					<td>
    						<input id="uploadExcel" name="uploadExcel" class="easyui-filebox" style="width:250px" data-options="prompt:'请选择文件...',buttonText: '选择文件'" />
    					</td>
    				</tr>
    				<tr>
    					<td colspan="2">
    						<a class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="uploadExcel()">导入</a>
    					</td>
    				</tr>
				</table>
			</form>
		</div>
		
		<!-- 进度条窗口 -->
		<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
		        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
		      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
		</div>
		<input id="da" value ="checkTask" type="hidden" />
		<input id="personId" type="hidden" />
		
</body>
</html>