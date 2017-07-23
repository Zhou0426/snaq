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
		function muban(id){
			$('#standard').treegrid('selectRow',id);//选中当前行
			var row = $('#standard').treegrid('getSelected');
			//alert(row);ok!
			    if (row!=null){  
					    $('#standard-win').window({
						width:400,
						height:300,
						title:'相关模板',
						cache:false,
						content:'<iframe src="${pageContext.request.contextPath}/standard/standardindex_template"+ frameborder="0" width="100%" height="100%"/>'
					});}
		}

		var timerId;
		function getForm(){			
	        //使用JQuery从后台获取JSON格式的数据
	        $.ajax({
	           type:"post",//请求方式
	           url:"standard/standardindexAction_exportSession.action",//发送请求地址
	           timeout:30000,//超时时间：30秒
	           dataType:"json",//设置返回数据的格式
	           //请求成功后的回调函数 data为json格式
	           success:function(data){
		          	var result=data.split(",");
		 		  	if(result[0]=="ok"){
		 		  		clearInterval(timerId);
		 		  		$('#dd').dialog('close');
		 		  		$.messager.alert('我的消息','导出成功！','info');
			 		}else if(result[0]=="nook"){
			 			clearInterval(timerId);
		 		  		$('#dd').dialog('close');
		 		  		$.messager.alert('我的消息','导出遇到未知错误，请稍后再试！','error');
				 	}else{
				 		if(result[1]>=100){
		                 	clearInterval(timerId);
		              	}
				 		$('#p').progressbar('setValue',result[1]);
					}              	
	          },
	          //请求出错的处理
	          error:function(){
	            clearInterval(timerId);
	 		  	$.messager.alert('我的消息','进度条显示异常，但并不影响正常导出！','error');
	          }
	       });
		}		


		function auditmethod(id){
			$('#standard').treegrid('selectRow',id);//选中当前行
			var row = $('#standard').treegrid('getSelected');
			//alert(row);ok!
			    if (row!=null){  
					    $('#standard-win').window({
						width:500,
						height:300,
						title:'审核方法',
						cache:false,
						content:'<iframe src="${pageContext.request.contextPath}/standard/standardindex_auditmethod"+ frameborder="0" width="100%" height="100%"/>'
					});}
		};
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		$('#standard').treegrid({    
		    url:'standard/standardindexAction_queryPart.action',    
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来    
		    treeField:'indexName',//定义树节点字段
		    fitColumns:true,/*列宽自动*/
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			fit:true,
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			//异步加载
  	 	    onBeforeExpand:function(row){  
		        //动态设置展开查询的url
		        $("#standard").treegrid('selectRow',row.id);
		        var url = "standard/standardindexAction_queryPart.action?indexSn="+row.indexSn;
		        $("#standard").treegrid("options").url = url;
		        return true;      
		    },
			toolbar:'#tb',
		    columns:[[
		        {field:'id',title:'逻辑主键',hidden:true}, 
		        {field:'indexSn',title:'编号',hidden:true},
		        {field:'indexName',title:'考核标准',sortable:true,width:80,formatter:function(value,row){
		        	var indexSn=row.indexSn;
			        if(row.indexSn.split('-').length==2){
			        	return "<span title=" + value + ">" +row.indexSn.split('-')[1]+ value + "</span>";
				    }else{
						return "<span title=" + value + ">" +row.indexSn+ value + "</span>";
				    }
		        }},
		        {field:'pdca',title:'所属阶段'},    
		        {field:'auditKeyPoints',title:'考核要点'},
		        {field:'isKeyIndex',title:'是否关键指标',formatter:function(value,row,index){
		        	if(row.isKeyIndex==true){
		        		return '是';
		        	}else if(row.isKeyIndex==false){
		        		return '否';
		        	}
		        }},
		        {field:'integerScore',title:'设计分值'}
		    ]]    
		}); 
  		//下拉列表框
  		
		$('#cc1').combobox({    
		    url: '${pageContext.request.contextPath}/department/departmentType_query.action',
			panelHeight:'auto',
			editable:false,
		    valueField:'departmentTypeSn',    
		    textField:'departmentTypeName',
		    onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].departmentTypeSn);
				}
			},
 	        onSelect: function(rec){
 	        	$('#cc2').combobox("clear");
		        var url = "standard/standardAction_queryByStandardType.action?departmentTypeSn="+rec.departmentTypeSn;
		        $('#cc2').combobox('reload', url);  
	        } 
		});
		$('#cc2').combobox({    
		    url:'standard/standardAction_queryByStandardType.action',
			panelHeight:'auto',
			queryParams:{standardType:"审核指南"},
		    valueField:'standardSn',    
		    textField:'standardName',
		    editable:false,
		    onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].standardSn);
				}else{
					var url='standard/standardindexAction_queryPart.action';
	 	        	//var url="";
	 	        	$("#standard").treegrid("options").url = url;
	 	        	$('#standard').treegrid('reload',{
	 	        		standardSn:null	        		
	 	        	});
				}
			},
 	        onSelect: function(rec){
 	        	var url='standard/standardindexAction_queryPart.action';
 	        	//var url="";
 	        	$("#standard").treegrid("options").url = url;
 	        	$('#standard').treegrid('reload',{
 	        		standardSn:rec.standardSn	        		
 	        	});
 	        }
		});	
		//导出进度窗
		$('#dd').dialog({    
		    title: '导出进度',    
		    width: 350,    
		    height: 80,    
		    closed:true,    
		    cache:false   
		});
		//添加
		$('#add').click(function(){
			if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070101')==-1){
				$('#add').css('display','none');
			}else{
				if($('#cc2').combobox('getValue')==""){
		 			$.messager.show({
						title:'提示',
						msg:'请先选择单位类型和评分标准！',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop+200,
							bottom:''
						}
					});
		 		}else{
	 			    $('#standard-win').window({
	 				width:400,
	 				height:400,
	 				title:'添加',
	 				cache:false,
	 				content:'<iframe src="${pageContext.request.contextPath}/standard/standardindex2_add" frameborder="0" width="100%" height="100%"/>'
		 			});
		 		}
			}
		});
		//删除
		$('#delete').click(function(){
			if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070102')==-1){
				$('#delete').css('display','none');
			}else{
				var row=$("#standard").treegrid("getSelected");
				//2、对事件进行判断，和删除确认
				if(row==null){
					$.messager.show({
						title:'提示',
						msg:'至少选择为一条记录！',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop+200,
							bottom:''
						}
					});
				}else{
					$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
						if(r){
							//3、获取要删除的id值 1,2,3===> String ids  delete from category where id in (1,2,3)
							var id=row.id;
	                        $.post("standard/standardindexAction_deleteById.action",{id:id},function(result){
								var data= eval('(' + result + ')');
								var status=data.status;
								var msg=data.message;
								if(result=="fail"){
									parents.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
										if(r){
											//window.open("denglu","_top")
										}
									});
									
								}else{
									if(status=="ok"){
										$.messager.alert("提示信息",msg);
									}else{
										$.messager.alert("提示信息",msg,'error');
									}
									//删除页面，刷新
									$("#standard").treegrid("clearChecked");
									var url='standard/standardindexAction_queryPart.action';
					 	        	$("#standard").treegrid("options").url = url;
					 	        	$('#standard').treegrid('reload',{
					 	        		standardSn:$('#cc2').combobox('getValue')       		
					 	        	});
								}
							},"text");
						}
					});
				}
			}
		});
		//修改
		$('#update').click(function(){
			if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070103')==-1){
				$('#update').css('display','none');
			}else{
				var row=$("#standard").treegrid("getSelected");
				if(row!=null){
					//发送ajax请求判断是否有权限
					$.post("",null,function(result){
						if(result=="fail"){
							parent.$.messager.confirm('权限不足提示','权限不足，是否注销重新登录？',function(r){
								if(r){
									//window.open("","_top");
								}
							});
						}else{
								$('#standard-win').window({
								width:400,
								height:400,
								title:"修改记录",
								cache:false,
								content:'<iframe src="${pageContext.request.contextPath}/standard/standardindex2_update" frameborder="0" width="100%" height="100%"/>'
							
							});
						}
					},"text");
				}else{
					$.messager.show({
						title:'错误提示',
						msg:'请选择一条记录！',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop+200,
							bottom:''
						}
					});
				}
			}
		});
		//导入
		$('#import').click(function(){
			if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070104')==-1){
				$('#import').css('display','none');
			}else{
				var standardSn=$('#cc2').combobox('getValue');
				if(standardSn.length>0){
					$("#standard-win").window({
						width:360,
						height:150,
						title:"Excel导入",
						cache:false,
						content:'<iframe src="${pageContext.request.contextPath}/standard/standardindex_excel" frameborder="0" width="100%" height="100%"/>'							
					});
				}else{						
					$.messager.show({
						title:'提示',
						msg:'请先选择一条评分标准！',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop+200,
							bottom:''
						}
					});
				}
			}
		});
		//导出
		$('#export').click(function(){
			if(resources.indexOf('070105')==-1){
				$('#export').css('display','none');
			}else{
				var title=$('#cc2').combobox('getText');
				var standardSn=$('#cc2').combobox('getValue');
				if(standardSn.length>0){
					$.messager.confirm('导出确认','您确定要导出当前数据吗？',function(r){
						if(r){
							var titles="指标编号,指标名称,所属阶段,审核要点,是否关键指标,整数分数,父级编号";
							//向后台发送请求
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","standard/standardindexAction_export.action");
							//添加input
												
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","title");
							input1.attr("value",title);
							
							
							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","titles");
							input2.attr("value",titles);
			
							
							var input5=$("<input>");
							input5.attr("type","hidden");
							input5.attr("name","templateSn");
							input5.attr("value","standardindex2");
							
							var input6=$("<input>");
							input6.attr("type","hidden");
							input6.attr("name","standardSn");
							input6.attr("value",standardSn);
							//将表单放入body
							$("body").append(form);
							form.append(input1);
							form.append(input2);
							form.append(input5);
							form.append(input6);
							form.submit();//提交表单
							$('#dd').dialog('open');
							timerId=setInterval(getForm,500);
						}
					});
				}else{						
					$.messager.show({
						title:'提示',
						msg:'请先选择一条评分标准！',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop+200,
							bottom:''
						}
					});
				}
				
			}
		});	
		//第一层权限限制
		if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070101')==-1){
			$('#add').css('display','none');
		}
		if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070102')==-1){
			$('#delete').css('display','none');
		}
		if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070103')==-1){
			$('#update').css('display','none');
		}
		if(roles.indexOf('jtxtgly')==-1 || resources.indexOf('070104')==-1){
			$('#import').css('display','none');
		}		
		if(resources.indexOf('070105')==-1){
			$('#export').css('display','none');
		}	
	})
</script>
</head>
<body style="margin: 1px;">
	<input id="type" value="2" type="hidden">
	<table id="standard" ></table>
	<div id="standard-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
    <div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
    <!-- toolbar -->
    <div id="tb">
    	<span>
    		<lable for="departmentType">单位类型：</lable>
			<input id="cc1"  name="departmentType" />
    	</span>
    	<span>
    		<lable for="standard">评分标准：</lable>
    		<input id="cc2" name="standard" style="width:270px">
    	</span>
		<a id="add" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a>
		<a id="delete" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:false">删除</a>
		<a id="update" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:false">修改</a>
		<a id="import" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-excel',plain:false">导入</a>
		<a id="export" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-excel',plain:false">导出</a>			
	</div>
	 <!-- 导出进度条 -->
    <div id="dd">
    	<div id="p" class="easyui-progressbar" style="width:315px;margin-top:10px;margin-left:10px;"></div>
    </div>
</body>
</html>