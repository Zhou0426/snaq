<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>综合查询</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
		<style type="text/css">
				.active{background:#B7D2FF; color:#000;}
				.div-height{margin:6px auto;}
 				.div-show{display:inline;margin:5px auto;} 
		</style>
		<script type="text/javascript">
		var a=1;
		<!--全局变量---参数-->
		var departmentSn="";
		var checkDeptSn = "";
		var specialitySn="";
		var departmentTypeSn="";
		var unsafeActStandardSn="";
		var checkTypeSn="";
		var checkerFromSn="";
		var unsafeActLevelSn="";
		var timeData="";
		var beginTime="";
		var endTime="";
		var checkers="";
        var beginTime="";
        var endTime="";
		var inconformityItemSnValue=new Array();
		var specialityValue=new Array();
		var unsafeActMarkValue=new Array();
		var unsafeActStandardValue=new Array();
		var UnsafeActLevelValue=new Array();
		
		
        <!--清除专业、标准的内容-->
        function clearValue(){
        	specialitySn="";
        	unsafeActStandardSn="";
        	$('#button').empty();//清除专业div组件
        	$('#unsafeActStandard').combogrid('clear');
			$('#unsafeActStandard').combogrid('grid').datagrid('loadData',[]);
        };
		<!--专业按钮的点击事件-->
		function major(val){
			 
			var $this =$('#'+val+'');
			
			//点击btn按钮变色且发送action，并附加参数值
			<!--如果已经点击，则取消点击-->
	       	if($this.hasClass("active")){
	           	
	       		departmentTypeSn="";
	       		clearValue();<!--清空专业按钮和标准指标的值-->
				loadAction();<!--发送数据-->
	          	$this.removeClass("active")
	          		
	       	}else{
	           	<!--如果之前没点击，则生成点击事件-->
	           	specialitySn="";
	           	departmentTypeSn=$this.attr("value");
	           	$('#unsafeActStandard').combogrid('grid').datagrid('load',{'departmentTypeSn':departmentTypeSn});
		        loadAction();<!--发送数据-->
		        $.post("${pageContext.request.contextPath}/inconformity/item/specialityAction_major.action",
						{departmentTypeSn:val},function(result){
							<!--清空专业组件，并重新加载-->
							$("#button").empty();
							//如果有子集存在，则动态加载子集内容
							if(result.length>0){
									
									$("#button").append("<lable for='speciality'>&emsp;专&emsp;&emsp;业：&emsp;</lable>");
									//加载专业button组件
									for(var i = 0;i < result.length;i++){
											$("#button").append("<a class='easyui-linkbutton'  value='" + result[i].specialitySn + "'>" +result[i].specialityName+ "</a> ");
									}
										//专业button组件重新渲染
										$.parser.parse($('#button').parent());
										
										//专业button组件的点击事件
										$("#button a").click(function(){ 
											var $this = $(this);
											
								        	if($this.hasClass("active")){
								        		specialitySn = specialitySn.replace($this.attr("value")+"##","");
								        		loadAction();<!--发送数据-->
								           		$this.removeClass("active")
								           		
								        	}else{
	
								        		if (specialitySn.indexOf($this.attr("value") +"##") == -1) {
								        			specialitySn+= $this.attr("value") +"##";
												}
								        		loadAction();<!--发送数据-->
								            	$this.addClass("active")
								        	}
										});
								}
					},"json");
					
		        	$("#unit a").removeClass("active");
	           		$this.addClass("active");
	       	}
       };
       
        <!--展示指标详情-附件-->
        function showAttachments(){
        	$('#win').window({
				title:"附件详情",
				width:400,
				height:300,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/unsafeact_show" frameborder="0" width="100%" height="100%" />'
			});
        };
        <!--拼接部门类型按钮-->
        function createDepartmentType(){
    		departmentTypeSn="";
    	    clearValue();<!--清除专业、标准的内容-->
			loadAction();<!--发送数据-->
        	$("#unit").empty();<!--清除部门类型按钮-->
        	$.post("${pageContext.request.contextPath}/unsafeCondition/query/queryAction_unitType.action",{departmentSn:departmentSn},function(res){
        		//如果有子集存在，则动态加载子集内容
				if(res.length>0){
					
					$("#unit").append("<lable for='unitType'>&emsp;单位类型：&emsp;&emsp;&emsp;</lable>");
						
					//加载单位类型button组件
					for(var i = 0;i < res.length;i++){
						$("#unit").append("<a class='easyui-linkbutton'  id='" + res[i].value + "' value='" + res[i].value + "'>" +res[i].text+ "</a> ");
		 			}
					//单位类型button组件重新渲染
					$.parser.parse($('#unit').parent());
					
					//单位类型button组件的点击事件
					$("#unit a").click(function(){ 
						
						val=$(this).attr("value");
						major(val);<!--专业组件的加载和点击事件-->
							
					});
// 					if(res.length==1){
// 						$('#unit').css('display','none');
// 						$('#'+res[0].value+'').click();
// 					}else{
// 						$('#unit').css('display','');
// 					}
				}
				
			},"json");
        };
        <!--datagrid加载数据发送action-->
        function loadAction(){
        	$('#dg').datagrid('load', {
        		departmentSn:departmentSn,//部门编号
            	checkDeptSn:checkDeptSn,//检查部门
        		departmentTypeSn:departmentTypeSn,//部门类型编号
        		specialitySn:specialitySn,//专业编号
        		unsafeActStandardSn:unsafeActStandardSn,//不安全行为标准编号
        		checkTypeSn:checkTypeSn,//检查类型编号
        		checkerFromSn:checkerFromSn,//检查人来自编号
        		unsafeActLevelSn:unsafeActLevelSn,//不安全行为等级编号
        		checkers:checkers,//检查人
        		timeData:timeData,//时间按钮的数据
        		beginTime:beginTime,
            	endTime:endTime
	        }); 
        };
        <!--查询按钮点击事件加载数据发送action-->
//         function searchAction(){
//         	$("#shijian a").removeClass("active");
//         	timeData="";
//         	$('#dg').datagrid('load', {
//         		departmentSn:departmentSn,//部门编号
//         		departmentTypeSn:departmentTypeSn,//部门类型编号
//         		specialitySn:specialitySn,//专业编号
//         		unsafeActStandardSn:unsafeActStandardSn,//不安全行为标准编号
//         		checkTypeSn:checkTypeSn,//检查类型编号
//         		checkerFromSn:checkerFromSn,//检查人来自编号
//         		unsafeActLevelSn:unsafeActLevelSn,//不安全行为等级编号
//         		checkers:checkers,//检查人
//         		timeData:timeData,//时间按钮的数据
//         		beginTime:$('#beginTime').datebox('getValue'),
//             	endTime:$('#endTime').datebox('getValue')
// 	        }); 
//         };
		$(function(){
					var first = 1;
					$('#checkDept').combotree({
						url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getDepartment.action',
						editable:false,
						queryParams:{'departmentSn':'1'},
						panelWidth: 300,
						panelHeight:350,
						width:200,
						onLoadSuccess:function(node,data){
							//第一次加载时执行的事件
							if( first == 1 ){
								first++;
								if(data.length == 1){
									var node = $('#checkDept').combotree('tree').tree('find', data[0].id);
									$('#checkDept').combotree('tree').tree('expand', node.target);
					        	}
							}
						},
						onSelect:function(rec){
							checkDeptSn = rec.id;
			            	loadAction();
						}
					});
					$('#departmentSn').combotree({
						url:'${pageContext.request.contextPath}/department/treeAll.action',
						method:'post',
						valueField: 'id',    
						textField: 'text', 
						required:true,
						queryParams:{'resourceSn':'0502'},
						panelWidth: 300,
						panelHeight:350,
						width:200,
						onLoadSuccess:function(node,data){
							if(a==1){
								<!--设置数据加载成功时的默认值-->
								var da=$('#departmentSn').combotree('tree').tree('getRoot');
								$('#departmentSn').combotree('setValue',da.id);
								departmentSn=da.id;
								createDepartmentType();
								a++;
								if(data.length == 1){
									var node = $('#departmentSn').combotree('tree').tree('find', data[0].id);
									$('#departmentSn').combotree('tree').tree('expand', node.target);
					        	}
							}	
						},
						onSelect: function(data){
							departmentSn=data.id;
							createDepartmentType();		
						}
					});
					$('#dg').datagrid({
						url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_showData.action',
						idField: 'id',
			            //title:'查询结果',
			            toolbar:'#query',
			            rownumbers: true,	//显示一个行号列
			            fitColumns:true,	//自动适应列
			           	fit:true,			//表格宽高自适应
			            nowrap:false,
			            striped:true,		//斑马线效果
						singleSelect:true,	//单行选中
			            loadmsg:'请等待...',	//加载等待时显示
			            pagination:true,	//显示分页组件
			            pageNumber:1,
			            pageSize:5,
			            pageList:[5,10,15,20,25,30],
						columns:[[
								  {field:'id',hidden:true},
						          {field:'checkedDepartmentImplType',title:'贯标单位',width:80,align:'center'},
						          {field:'checkedDepartment',title:'被检部门',width:80,align:'center'},
						          {field:'violator',title:'不安全<br />行为人员',width:50,align:'center'},
						          {field:'actDescription',title:'行为描述',width:200,align:'center'},
						          {field:'checkType',title:'检查类型',width:50,align:'center'},
				                  {field:'systemAudit',title:'审核类型',width:50,align:'center'},
				                  {field:'editor',width:50,title:'录入人',align:'center'},
						          {field:'checkers',title:'检查成员',width:70,align:'center'},        
						          {field:'checkerFrom',title:'检查人来自',width:80,align:'center'},    
						          {field:'checkDateTime',title:'检查时间',width:50,align:'center'},    
						          {field:'checkLocation',title:'检查地点',width:70,align:'center'},
						          {field:'inconformityItemSn',width:100,title:'不符合项编号',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  inconformityItemSnValue[index]=value;
						        		  }else{
						        			  inconformityItemSnValue[index]="无";
						        		  }
						  		  }},
						          {field:'speciality',title:'所属专业',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  specialityValue[index]=value;
						        		  }else{
						        			  specialityValue[index]="无";
						        		  }
						  		  }},
						          {field:'unsafeActMark',title:'不安全行为痕迹',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  unsafeActMarkValue[index]=value;
						        		  }else{
						        			  unsafeActMarkValue[index]="无";
						        		  }
						  		  }},
						          {field:'unsafeActStandard',title:'不安全行为标准',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  unsafeActStandardValue[index]=value;
						        		  }else{
						        			  unsafeActStandardValue[index]="无";
						        		  }
						  		  }},
						          {field:'UnsafeActLevel',title:'不安全行为等级',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  UnsafeActLevelValue[index]=value;
						        		  }else{
						        			  UnsafeActLevelValue[index]="无";
						        		  }
						  		  }},
						          {field:'attachments',title:'附件详情',width:50,align:'center',
						        	  formatter: function(value,row,index){
							  				if (value!=0){
							  					return "<a href='javascript:;' onclick=showAttachments()>附件["+value+"]</a>";
							  				} else {
							  					return "<a href='javascript:;' onclick=showAttachments()>附件[0]</a>";
							  				}
						  		  }}
						     ]],
						     onBeforeLoad:function(){
						    	 if(departmentSn==""){
						    		 return false;
						    	 }else{
						    		 return true;
						    	 }
						     },
							 onDblClickCell: function(){
								$('#dg').datagrid("uncheckAll");
							 },
							view: detailview, 
							detailFormatter: function(rowIndex, row){
							return '<table style="border:1"><tr>' + 								
							'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
							'<td style="width:140px;text-align:center">' + 
							'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
							'<td style="width:140px;text-align:center">' + 
							'<p>' + UnsafeActLevelValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'专业：' + '</td>' + 
							'<td style="width:100px;text-align:center" >' + 
							'<p>' + specialityValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'痕迹：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + unsafeActMarkValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr><tr>' +
							
							'<td style="width:100px;text-align:center">'+'标准：' + '</td>' + 
							'<td style="width:380px;text-align:center" colspan="7">' + 
							'<p>' + unsafeActStandardValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr></table>'; 
							}
					});
					<!--标准-->
					$('#unsafeActStandard').combogrid({
						url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_standard.action',
						method:'post',
						idField:'standardSn',
					    textField:'standardDescription',
					    delay: 500,
					    prompt:'输入名称或编号搜索相应标准',
						panelWidth:700,
						panelHeight:200,
						width:500,
						columns:[[    
						          {field:'standardSn',title:'标准编号',width:90},    
						          {field:'standardDescription',title:'标准描述',width:600}  
						]],
						onBeforeLoad:function(){
					    	 if(departmentTypeSn==""){
					    		 return false;
					    	 }else{
					    		 return true;
					    	 }
					    },
						onSelect: function(){
							unsafeActStandardSn=$('#unsafeActStandard').combogrid('getValue');
							loadAction();<!--发送数据-->
				        },
				        onChange:function(newValue, oldValue){
				        	if(newValue==null||newValue==""){
				        		loadAction();<!--发送数据-->
				        	}else{
					        	if(departmentTypeSn==""){
					        		$('#unsafeActStandard').combogrid('hidePanel');
									$.messager.alert('提示','请选择单位类型！');    
					        		$('#unsafeActStandard').combogrid('clear');
					        	}else{
						        	$('#unsafeActStandard').combogrid('grid').datagrid('load',{'q':newValue,'departmentTypeSn':departmentTypeSn});
					        	}
				        	}
			        	},
					});
					
					//检查人checkPerson 
					$('#checkPerson').combogrid({
							delay: 500,    
							width:200,
						    panelWidth:200,
						    prompt:'请输入搜索信息',
						    idField:'personId',    
						    textField:'personName',    
						    url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showPersonAll.action', 
						    columns:[[    
						        {field:'personId',title:'编号',width:90},    
						        {field:'personName',title:'姓名',width:108}
						    ]],
						    onSelect:function(){
						    	checkers=$('#checkPerson').combogrid('getValue');
						    	loadAction();<!--发送数据-->
							},
							onChange:function(newValue, oldValue){
								checkers=newValue;
								if(newValue==null || newValue.length==0){
									loadAction();<!--发送数据-->
								}else{
									$('#checkPerson').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':departmentSn});
								}
							}
					});
					<!--标准重置按钮-->
					$('#btn11').bind('click', function(){
						unsafeActStandardSn="";
						$('#unsafeActStandard').combogrid('clear');
						$('#unsafeActStandard').combogrid('grid').datagrid('load',{'departmentTypeSn':departmentTypeSn});
					   	loadAction();<!--发送数据-->
				    });
					<!--检查类型的数据-->
					$("#checkT a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		checkTypeSn="";
			           		loadAction();<!--发送数据-->
			        	}else{
			        		$("#checkT a").removeClass("active")
			            	$this.addClass("active")
			            	checkTypeSn=$this.attr("value");
			        		loadAction();<!--发送数据-->
			        	}
				    });
					<!--检查人来自数据-->
					$("#checkF a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		checkerFromSn="";
			           		loadAction();<!--发送数据-->
			        	}else{
			        		$("#checkF a").removeClass("active")
			            	$this.addClass("active")
			            	checkerFromSn=$this.attr("value");
			        		loadAction();<!--发送数据-->
			        	}
				     });
					<!--不安全行为等级的数据-->
					$("#unsafeActLevel a").click(function(){  
				        var $this = $(this);
				        if($this.hasClass("active")){
				           	$this.removeClass("active")
				           	unsafeActLevelSn="";
				           	loadAction();<!--发送数据-->
				        }else{
				        	$("#unsafeActLevel a").removeClass("active")
				            $this.addClass("active")
				            unsafeActLevelSn=$this.attr("value");
				        	loadAction();<!--发送数据-->
				        }
				    });
					<!--时间选择的数据-->
			        $("#shijian a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		timeData="";
			           		loadAction();<!--发送数据-->
			        	}else{
			        		$("#shijian a").removeClass("active")
			            	$this.addClass("active")
			            	timeData=$this.attr("value");
			        		loadAction();<!--发送数据-->
			        	}
				     });
			        $('#beginTime').datebox({
					    value: '3/4/2010',    
					    panelWidth:200,
					    width:200, 
					    required: true,
					    editable:false,
					    showSeconds: false   
					}); 
					$('#endTime').datebox({    
					    value: '3/4/2010',  
					    panelWidth:200,
					    width:200,  
					    required: true,
					    editable:false,
					    showSeconds: true   
					});
					$('#btn9').bind('click', function(){    
						$("#shijian a").removeClass("active");
			        	timeData="";
			        	beginTime=$('#beginTime').datebox('getValue');
		            	endTime=$('#endTime').datebox('getValue');
		            	loadAction();
				    }); 
				   $('#export').bind('click', function(){
					   var dataParams = {
						    departmentSn:departmentSn,//部门编号
			            	checkDeptSn:checkDeptSn,//检查部门
			        		departmentTypeSn:departmentTypeSn,//部门类型编号
			        		specialitySn:specialitySn,//专业编号
			        		unsafeActStandardSn:unsafeActStandardSn,//不安全行为标准编号
			        		checkTypeSn:checkTypeSn,//检查类型编号
			        		checkerFromSn:checkerFromSn,//检查人来自编号
			        		unsafeActLevelSn:unsafeActLevelSn,//不安全行为等级编号
			        		checkers:checkers,//检查人
			        		timeData:timeData,//时间按钮的数据
			        		beginTime:beginTime,
			            	endTime:endTime
					   };
					   $.each(dataParams, function(key, val) {
						   if(dataParams[key] == null || dataParams[key].length == 0){
							   delete dataParams[key];
						   }
						   //console.log(key);//输出键
					   });
					   var params = $.param(dataParams);
					   var url = '${pageContext.request.contextPath}/inconformity/item/unsafeActAction_exportQueryUnsafeAct.action'+'?'+params;
 					   //console.log(url);
					   $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
					   
				    });
					
			});
	    </script>
</head>
<body>
		<table id="dg"></table>
		<div id="query">
		
		
			<!-- 部门和部门/单位类型的div -->
			<div class="div-height">
					<div  class="div-height div-show" >
							<lable for="departmentSn">&emsp;被检部门：&emsp;</lable><input id="departmentSn" />
					</div>
					<div id="unit" class="div-height div-show"></div>
					<div id="checkDeptDiv" class="div-height div-show">
							<lable for="checkDept">&emsp;检查部门：</lable><input id="checkDept"  name="checkDeptSn" />
					</div>
			</div>
			
			<!-- 专业 -->
			<div id="button" class="div-height"></div>
			
			<div class="div-height">
				<!-- 检查类型 -->
				<div id="checkT" class="div-show">
					<lable for="checkT">&emsp;检查类型：&emsp;</lable><a id="btn12" class="easyui-linkbutton"  value="0">动态检查</a>
						<a id="btn13" class="easyui-linkbutton"  value="1">定期检查</a>
						<a id="btn14" class="easyui-linkbutton"  value="2">专项检查</a>
				</div>
				<!-- 检查人来自 -->
				<div id="checkF" class="div-show">
					<lable for="checkF">&emsp;&emsp;检查人来自：&emsp;&emsp;</lable><a id="btn15" class="easyui-linkbutton"  style="margin-left:2px" value="0">区队</a>
						<a id="btn16" class="easyui-linkbutton"  value="1">单位</a>
						<a id="btn17" class="easyui-linkbutton"  value="2">业务部门</a>
						<a id="btn18" class="easyui-linkbutton"  value="3">高层管理人员</a>
						<a id="btn19" class="easyui-linkbutton"  value="4">神华</a>
						<a id="btn20" class="easyui-linkbutton"  value="5">外部</a>
				</div>
			</div>
			
			<div class="div-height">
				<!-- 检查人 -->
				<div id="checkP" class="div-show">
					<lable for="checkP">&emsp;检查人：&emsp;&emsp;</lable><input id="checkPerson"  name="checkPerson" />
				</div>
				<!-- 不安全行为等级 -->
				<div id="unsafeActLevel" class="div-show">
				    <label for="unsafeActLevel">&emsp;不安全行为等级：</label><a id="btn1" class="easyui-linkbutton"  value="2" >A类不安全行为</a>
						<a id="btn2" class="easyui-linkbutton"  value="1" >B类不安全行为</a>
						<a id="btn30" class="easyui-linkbutton"  value="0" >C类不安全行为</a>
				</div>
			</div>
			
			<!-- 标准 -->
			<div class="div-height">
		    		<div  class="div-show">
						<lable for="unsafeActStandard">&emsp;标&emsp;&emsp;准：&emsp;</lable><input id="unsafeActStandard" />
					</div>
					<span>
						<a id="btn11" class="easyui-linkbutton"  data-options="iconCls:'icon-undo'">重置</a>
					</span>
					<!-- 导出数据 -->
					<span id="export" class="div-show">
							<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导出数据</a>
					</span>
			</div>
			
			<!-- 时间 -->
			<div id="shijian" class="div-show">
					<lable for="beginTime">&emsp;时&emsp;&emsp;间：&emsp;</lable><a id="btn3" class="easyui-linkbutton"  value="today">今天</a>
							<a id="btn10" class="easyui-linkbutton"  value="week">本周</a>
							<a id="btn4" class="easyui-linkbutton"  value="xun">本旬</a>
							<a id="btn5" class="easyui-linkbutton"  value="month">本月</a>
							<a id="btn6" class="easyui-linkbutton"  value="quarter">本季度</a>
							<a id="btn7" class="easyui-linkbutton"  value="banyear">本半年</a>
							<a id="btn8" class="easyui-linkbutton"  value="year">全年</a>
			</div>
			<div class="div-show">
					<div  class="div-show" style="margin-left:18px">
							<lable for="beginTime">开始时间：</lable>
							<input id="beginTime"  name="beginTime"  type="text" />
					</div>
					<div class="div-show" >
							<lable for="endTime">结束时间：</lable>
							<input id="endTime"  name="endTime"  type="text" />
					</div>
					<a id="btn9" class="easyui-linkbutton"  data-options="iconCls:'icon-search'">查询</a>
			</div>
			
		</div>
		
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>