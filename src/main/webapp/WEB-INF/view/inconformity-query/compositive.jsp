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

		//第一次加载时的判断
		var a=1;
		var checked = false;
		var SindexSn=1;
		var standardIndexValue=new Array();
		var hazardValue=new Array();
		var correctPrincipalValue=new Array();
		var inconformityLevelValue=new Array();
		var specialityValue=new Array();
		var deductPointsValue=new Array();
		var machineValue=new Array();
		var inconformityItemNatureValue=new Array();
		var checkersValue=new Array();
		var inconformityItemSnValue=new Array();
		var editorDateTimeValue=new Array();
		var confirmTimeValue=new Array();
		
		//全局变量
		var dpmt="";
		var checkDeptSn = "";
		var qSpecialitySn="";
		var timeData="";
		var riskLevel="";
		var departmentSn="";
		var indexSn="";
		var checkType="";
        var checkerFrom="";
        var inconformityItemNature="";
        var checkers=""; 
        var correctPrincipal="";
        var inconformityLevel="";
        var hasCorrectConfirmed="";
        var hasReviewed="";
        var hasCorrectFinished="";
        var department="";
        var beginTime="";
        var endTime="";
		 
		 function major(val){
			 
			var $this =$('#'+val+'');
			
			//点击btn按钮变色且发送action，并附加参数值
			<!--如果已经点击，则取消点击-->
        	if($this.hasClass("active")){
            	
        		qSpecialitySn="";
            	<!--使标准加载全部-->
        		dpmt="";
				url='${pageContext.request.contextPath}/standard/standardAction_standard.action?departmentTypeSn='+dpmt,
				$('#standard').combobox('reload',url);
			
        		<!--清空专业按钮和标准指标的值-->
				clearValue();
				loadAction();<!--发送数据-->
           		$this.removeClass("active")
           		
        	}else{
            	<!--如果之前没点击，则生成点击事件-->
	        	//alert($this.attr("value"));
	        	qSpecialitySn="";
	        	dpmt=$this.attr("value");
	        	
	        	<!-- 加载标准数据 -->
				url='${pageContext.request.contextPath}/standard/standardAction_standard.action?departmentTypeSn='+dpmt,
				$('#standard').combobox('reload',url);
				
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
								        	//var th=$this.attr("value")+"##";
							        		qSpecialitySn = qSpecialitySn.replace($this.attr("value")+"##","");
							        		loadAction();<!--发送数据-->
							           		$this.removeClass("active")
							           		//console.log(qSpecialitySn);
							           		
							        	}else{

							        		if (qSpecialitySn.indexOf($this.attr("value") +"##") == -1) {
							        			qSpecialitySn+= $this.attr("value") +"##";
											}
							        		loadAction();<!--发送数据-->
							            	$this.addClass("active")
							            	//console.log(qSpecialitySn);
							        	}
									});
							}
				},"json");
				
	        	$("#unit a").removeClass("active")
            	$this.addClass("active")
        	}
        };
        <!--展示指标详情-附件-->
        function showAttachments(){
        	$('#win').window({
				title:"附件详情",
				width:400,
				height:300,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/compositive_show" frameborder="0" width="100%" height="100%" />'
			});
        };
        <!--清除专业、标准和指标的内容-->
        function clearValue(){
        	<!--清空专业组件-->
    		$("#button").empty();
        	qSpecialitySn="";
        	indexSn="";
			$('#standardindex').combotree('clear');
			$('#standard').combobox('clear');
			
        };
        <!--datagrid加载数据发送action-->
        function loadAction(){
        	$('#dg').datagrid('load', {
            	dpmt:dpmt,								//部门类型
            	checkDeptSn:checkDeptSn,				//检查部门
            	departmentSn:departmentSn,				//被检部门
            	qSpecialitySn:qSpecialitySn,			//专业
            	inconformityLevel:inconformityLevel,	//不符合项等级
            	timeData:timeData,						//按钮选择时间
            	indexSn:indexSn,						//指标编号
            	checkType:checkType,					//检查类型
	            checkerFrom:checkerFrom,				//检查人来自
	            checkers:checkers,						//检查人
	            correctPrincipal:correctPrincipal,		//整改负责人
	            hasCorrectConfirmed:hasCorrectConfirmed,//已整改确认
	            hasReviewed:hasReviewed,				//已复查
	            hasCorrectFinished:hasCorrectFinished,	//已整改完成
		        inconformityItemNature:inconformityItemNature,	//不符合项性质
            	standardSn:$('#standard').combotree('getValue'),//标准编号
            	beginTime:beginTime,
            	endTime:endTime,
            	checked : checked
	        }); 
        };

        <!--查询按钮点击事件加载数据发送action-->
//         function searchAction(){
//         	$('#dg').datagrid('load', {
//             	qSpecialitySn:qSpecialitySn,
//             	inconformityLevel:inconformityLevel,
//             	timeData:timeData,
//             	departmentSn:departmentSn,
//             	indexSn:indexSn,
//             	dpmt:dpmt,
//             	checkType:checkType,				//检查类型
// 	            checkerFrom:checkerFrom,			//检查人来自
// 	            checkers:checkers,					//检查人
// 	            correctPrincipal:correctPrincipal,	//整改负责人
// 	            hasCorrectConfirmed:hasCorrectConfirmed,//已整改确认
// 	            hasReviewed:hasReviewed,				//已复查
// 	            hasCorrectFinished:hasCorrectFinished,	//已整改完成
// 		        inconformityItemNature:inconformityItemNature,	//不符合项性质
//             	standardSn:$('#standard').combotree('getValue'),
//             	beginTime:$('#beginTime').datebox('getValue'),
//             	endTime:$('#endTime').datebox('getValue')
// 	        	}); 
//         };
        <!--重置按钮点击事件：清空标准指标的选择并发送action-->
        function undoAction(){
        	indexSn="";
			$('#standardindex').combotree('clear');
			$('#standard').combobox('clear');
        	$('#dg').datagrid('load', {
            	qSpecialitySn:qSpecialitySn,
            	inconformityLevel:inconformityLevel,
            	timeData:timeData,
            	departmentSn:departmentSn,
            	indexSn:indexSn,
            	dpmt:dpmt,
            	checkDeptSn:checkDeptSn,			//检查部门
            	checkType:checkType,				//检查类型
	            checkerFrom:checkerFrom,			//检查人来自
	            checkers:checkers,					//检查人
	            correctPrincipal:correctPrincipal,	//整改负责人
	            hasCorrectConfirmed:hasCorrectConfirmed,//已整改确认
	            hasReviewed:hasReviewed,				//已复查
	            hasCorrectFinished:hasCorrectFinished,	//已整改完成
		        inconformityItemNature:inconformityItemNature,	//不符合项性质
            	standardSn:$('#standard').combotree('getValue'),
            	checked : checked
//             	beginTime:$('#beginTime').datebox('getValue'),
//             	endTime:$('#endTime').datebox('getValue')
        	}); 
        };
        function createDepartmentType(){
        	<!--清空专业按钮和标准指标的值-->
			clearValue();
			loadAction();<!--发送数据-->
			$.post("${pageContext.request.contextPath}/unsafeCondition/query/queryAction_unitType.action",
						{departmentSn:departmentSn},function(res){
							
					//清除div组件
					$("#unit").empty();
					//如果有子集存在，则动态加载子集内容
					if(res.length>0){
							$("#unit").append("<lable for='unitType'>单位类型：&emsp;&emsp;</lable>");
							
							//加载单位类型button组件
							for(var i = 0;i < res.length;i++){
									$("#unit").append("<a class='easyui-linkbutton'  id='" + res[i].value + "' value='" + res[i].value + "'>" +res[i].text+ "</a> ");
				 				//$("#unit a").click(function(){ alert($(this).attr("value")); });
							}
							//单位类型button组件重新渲染
							$.parser.parse($('#unit').parent());
							
							//单位类型button组件的点击事件
							$("#unit a").click(function(){
								
								<!--清空专业按钮和标准指标的值-->
								clearValue();
								
								val=$(this).attr("value");
								
								<!--专业组件的加载和点击事件-->
								major(val);
								
							});
					}
				},"json");
        };
		$(function(){
					
					<!--检查类型的数据-->
					$("#checkT a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		checkType="";
			           		loadAction();<!--发送数据-->
			           		//console.log(checkType);
			        	}else{
			        		$("#checkT a").removeClass("active")
			            	$this.addClass("active")
			            	checkType=$this.attr("value");
			        		loadAction();<!--发送数据-->
			            	//console.log(checkType);
			        	}
				     });
					<!--检查人来自数据-->
					$("#checkF a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		checkerFrom="";
			           		loadAction();<!--发送数据-->
			        	}else{
			        		$("#checkF a").removeClass("active")
			            	$this.addClass("active")
			            	checkerFrom=$this.attr("value");
			        		loadAction();<!--发送数据-->
			        	}
				     });
					<!--不符合项性质的数据-->
					$("#checkN a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		inconformityItemNature="";
			           		loadAction();<!--发送数据-->
			           		//console.log(inconformityItemNature);
			        	}else{
			        		$("#checkN a").removeClass("active")
			            	$this.addClass("active")
			            	inconformityItemNature=$this.attr("value");
			        		loadAction();<!--发送数据-->
			            	//console.log(inconformityItemNature);
			        	}
				     });
			        <!--时间选择的数据-->
			        $("#shijian a").click(function(){
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		timeData="";
			           		loadAction();<!--发送数据-->
			           		//console.log(timeData);
			        	}else{
			        		$("#shijian a").removeClass("active")
			            	$this.addClass("active")
			            	timeData=$this.attr("value");
			        		loadAction();<!--发送数据-->
			            	//console.log(timeData);
			        	}
				     });
			        <!--不符合项等级的数据-->
					$("#gradebtn a").click(function(){  
			        	var $this = $(this);
			        	if($this.hasClass("active")){
			           		$this.removeClass("active")
			           		inconformityLevel="";
			           		//console.log(inconformityLevel);
			           		loadAction();<!--发送数据-->
			        	}else{
			        		$("#gradebtn a").removeClass("active")
			            	$this.addClass("active")
			            	inconformityLevel=$this.attr("value");
			            	//console.log(inconformityLevel);
			        		loadAction();<!--发送数据-->
			        	}
				      });
		
					$('#department').combotree({
						url:'${pageContext.request.contextPath}/department/treeAll.action',
						method:'post',
						valueField: 'id',    
				        textField: 'text', 
						required:true,
						queryParams:{'resourceSn':'0501'},
						panelWidth: 300,
						panelHeight:350,
						width:200,
						onLoadSuccess:function(node,data){
							//第一次加载时执行的事件
							if(a==1){
								<!--设置数据加载成功时的默认值-->
								var da=$('#department').combotree('tree').tree('getRoot');
								$('#department').combotree('setValue',da.id);
								department=da.id;
								departmentSn=da.id;
								createDepartmentType();
								a++;
								if(data.length == 1){
									var node = $('#department').combotree('tree').tree('find', data[0].id);
									$('#department').combotree('tree').tree('expand', node.target);
					        	}
							}
						},
						onSelect: function(data){
							dpmt="";
							url='${pageContext.request.contextPath}/standard/standardAction_standard.action?departmentTypeSn='+dpmt,
							$('#standard').combobox('reload',url);
							department=data.id;
							departmentSn=data.id;
							createDepartmentType();
				        }
					});
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
					$('#standard').combobox({
						url:'${pageContext.request.contextPath}/standard/standardAction_standard.action?departmentTypeSn='+dpmt,
						method:'post',
						valueField:'standardSn',    
					    textField:'standardName',
					    editable:false,
						panelWidth: 320,
						panelHeight:250,
						width:295,
// 						onLoadSuccess:function(node,data){
// 							var da=$(this).combobox('getData');
// 							if(da.length>0){
// 								$(this).combobox('setValue',da[0].standardName);
// 								$('#standardindex').combotree('clear');
// 								url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindex.action?standardSn='+da[0].standardSn,
// 								$('#standardindex').combotree('reload',url);
// 							}
// 						},
						onSelect: function(rec){
							//console.log(rec.standardSn);
							SindexSn=1;
							$('#standardindex').combotree('clear');
							url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_standardindexQuery.action?standardSn='+rec.standardSn,
							$('#standardindex').combotree('reload',url);
				        }
					});
					$('#standardindex').combotree({
						//url:'${pageContext.request.contextPath}/standard/standardindexAction_queryPart',
						method:'post',
						panelWidth: 380,
						panelHeight:200,
						width:295,
						onLoadSuccess:function(node,data){
							if(SindexSn==1){
								if(data.length>0){
									//var da=$('#standardindex').combotree('tree').tree('getRoot');
									$("#standardindex").combotree('setValue',data[0].id);
									indexSn=data[0].id;
									loadAction();<!--发送数据-->
									SindexSn++;
								}else{
									//console.log(data.length);
									indexSn="";
									$('#dg').datagrid('loadData', { total: 0, rows: [] });
									SindexSn++;
								}
							}
						},
						onSelect: function(rec){
							var va=$('#standard').combobox('getValue');
							if(va==null||va.length==0){
								$.messager.alert('提示','请先选择标准！');
								$('#standardindex').combotree('clear');
							}else{
								indexSn=rec.id;
								loadAction();<!--发送数据-->
							}
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
					//检查人checkPerson 
					$('#checkPerson').combogrid({
							delay: 500,    
						    //mode: 'remote',
							width:200,
						    panelWidth:200,
						    prompt:'请输入搜索信息',
						    //multiple:true,
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
									$('#checkPerson').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':department});
								}
							}
					});
					//整改负责人correctPrincipalSn
					$('#correctPrincipalSn').combogrid({
							delay: 500,    
						    //mode: 'remote',
							width:200,
						    panelWidth:200,
						    prompt:'请输入搜索信息',
						    idField:'personId',    
						    textField:'personName',
						    url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showPerson.action',    
						    columns:[[    
						        {field:'personId',title:'编号',width:90},    
						        {field:'personName',title:'姓名',width:108}
						    ]],
						    onSelect:function(){
						    	correctPrincipal=$('#correctPrincipalSn').combogrid('getValue');
						    	loadAction();<!--发送数据-->
							},
							onChange:function(newValue, oldValue){
								correctPrincipal=newValue;
								if(correctPrincipal==null || correctPrincipal.length==0){
									loadAction();<!--发送数据-->
								}else{
									$('#correctPrincipalSn').combogrid('grid').datagrid('load',{'q':newValue,'departmentSn':department});
								}
							}
					});
					$('#dg').datagrid({
						url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showData.action',
						idField: 'id',
			            //title:'查询结果',
			            toolbar:'#query',
			            rownumbers: true,	//显示一个行号列
			            fitColumns:true,	//自动适应列
			            //height:500,
			           	//fit:true,			//表格宽高自适应
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
						          {field:'checkedDepartmentImplType',title:'贯标单位',width:100,align:'center'},
						          {field:'checkedDepartment',title:'被检部门',width:100,align:'center'},
						          {field:'checkType',title:'检查类型',width:65,align:'center'},
						          {field:'systemAudit',title:'审核类型',width:65,align:'center'},
						          {field:'checkerFrom',title:'检查人来自',width:100,align:'center'},    
						          {field:'checkDateTime',title:'检查时间',width:70,align:'center'},
						          {field:'checkLocation',title:'检查地点',width:100,align:'center'},    
						          {field:'problemDescription',title:'问题描述',width:300,align:'center'},
						          {field:'currentRiskLevel',title:'现风险等级',width:65,align:'center'},
						          {field:'riskLevel',title:'原风险等级',hidden:true},
				                  {field:'editor',width:50,title:'录入人'},
						          {field:'correctType',title:'整改类型',width:65,align:'center'},
						          {field:'correctDeadline',title:'整改期限',width:70,align:'center'},
						          {field:'correctProposal',title:'整改建议',width:150,align:'center'},
						          {field:'hasCorrectConfirmed',title:'整改确认',width:70,align:'center',
						        	  styler: function(value,row,index){
							  				if (value == "已整改确认"){
							  					return 'background-color:#1DA600;';
							  				}else{
							  					return 'background-color:#ff5544;';
							  				}
									  }
							      },
						          {field:'hasReviewed',title:'复查',width:65,align:'center',
						        	  styler: function(value,row,index){
						        		  	if (value == "已复查"){
							  					return 'background-color:#1DA600;';
							  				}else{
							  					return 'background-color:#ff5544;';
							  				}
										}
							      },
						          {field:'hasCorrectFinished',title:'整改完成',width:70,align:'center',
						        	  styler: function(value,row,index){
						        		  if( row.correctDeadline != null && row.correctDeadline < new Date() ){
						        			  return 'background-color:#ff5544;';
						        		  }else{
						        		  	if (value == "已整改完成" ){
							  					return 'background-color:#1DA600;';
							  				}else{
							  					return '';
							  				}
						        		  }
									  }
							      },
						          {field:'inconformityItemSn',title:'不符合项编号',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  inconformityItemSnValue[index]=value;
						        		  }else{
						        			  inconformityItemSnValue[index]="无";
						        		  }
						  		  }},
						          {field:'checkers',title:'检查人',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  checkersValue[index]=value;
						        		  }else{
						        			  checkersValue[index]="无";
						        		  }
						  		  }},        
						          {field:'inconformityItemNature',title:'不符合项性质',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  inconformityItemNatureValue[index]=value;
						        		  }else{
						        			  inconformityItemNatureValue[index]="无";
						        		  }
						  		  }},
						          {field:'machine',title:'机',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  machineValue[index]=value;
						        		  }else{
						        			  machineValue[index]="无";
						        		  }
						  		  }},
						          {field:'standardIndex',title:'指标',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        		  		standardIndexValue[index]=value;
						        		  }else{
												standardIndexValue[index]="无";
						        		  }
						  		  }},
						          {field:'deductPoints',title:'扣分',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  deductPointsValue[index]=value;
						        		  }else{
						        			  deductPointsValue[index]="无";
						        		  }
						  		  }},
						          {field:'hazrd',title:'对应的危险源',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  hazardValue[index]=value;
						        		  }else{
						        			  hazardValue[index]="无";
						        		  }
						  		  }},
						          {field:'speciality',title:'所属专业',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  specialityValue[index]=value;
						        		  }else{
						        			  specialityValue[index]="无";
						        		  }
						  		  }},
						          {field:'editorDateTime',title:'录入时间',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  editorDateTimeValue[index]=value;
						        		  }else{
						        			  editorDateTimeValue[index]="无";
						        		  }
						  		  }},
						          {field:'confirmTime',title:'整改确认时间',width:100,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  confirmTimeValue[index]=value;
						        		  }else{
						        			  confirmTimeValue[index]="无";
						        		  }
						  		  }},
						          {field:'inconformityLevel',title:'不符合项等级',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  inconformityLevelValue[index]=value;
						        		  }else{
						        			  inconformityLevelValue[index]="无";
						        		  }
						  		  }},
						          {field:'correctPrincipal',title:'整改负责人',width:120,align:'center',hidden:true,
						        	  formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  correctPrincipalValue[index]=value;
						        		  }else{
						        			  correctPrincipalValue[index]="无";
						        		  }
						  		  }},
						          {field:'attachments',title:'附件详情',width:100,align:'center',
						        	  formatter: function(value,row,index){
							        	  //console.log(value);
							  				if (value!=0){
							  					return "<a href='javascript:;' onclick=showAttachments()>附件["+value+"]</a>";
							  				} else {
							  					return "<a href='javascript:;' onclick=showAttachments()>附件[0]</a>";
							  				}
						  			}}
						     ]],
							 onDblClickCell: function(){
								$('#dg').datagrid("uncheckAll");
							 },
							view: detailview, 
							detailFormatter: function(rowIndex, row){
							return '<table style="border:1"><tr>' + 
							'<td style="width:90px;text-align:center">'+'不符合项编号：' + '</td>' + 
							'<td style="width:200px;text-align:center">' + 
							'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'不符合项性质：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + inconformityItemNatureValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'不符合项等级：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + inconformityLevelValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'专业：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + specialityValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'录入时间：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + editorDateTimeValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr><tr>' +
							
							'<td style="width:90px;text-align:center">'+'检查人：' + '</td>' + 
							'<td style="width:200px;text-align:center">' + 
							'<p>' + checkersValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'整改负责人：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + correctPrincipalValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'扣分：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + deductPointsValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'机器：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + machineValue[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:90px;text-align:center">'+'整改确认时间：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + confirmTimeValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr><tr>' +
							
							'<td style="width:90px;text-align:center">'+'危险源：' + '</td>' + 
							'<td style="width:200px;text-align:center"colspan="9">' + 
							'<p>' + hazardValue[rowIndex] + '</p>' + 
							'</td>' +
							
							'</tr><tr>' +
							
							'<td style="width:90px;text-align:center">'+'指标：' + '</td>' + 
							'<td style="width:100px;text-align:center" colspan="9">' + 
							'<p>' + standardIndexValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr></table>'; 
							} 			
						});
					$('#btn9').bind('click', function(){
						$("#shijian a").removeClass("active");
			        	timeData="";
			        	beginTime=$('#beginTime').datebox('getValue');
		            	endTime=$('#endTime').datebox('getValue');
		            	loadAction();
				    }); 
				   $('#btn11').bind('click', function(){
					   	$('#standardindex').combotree('loadData', []);
						undoAction();
				    });
				   $('#panel').panel({    
					   title:'下拉选择更多',
					   closable:false,
					   collapsible:true,
					   minimizable:false,
					   maximizable:false,
					   doSize:true,
					   collapsed:true
					 });
				   $('#export').bind('click', function(){
					   var dataParams = {
							dpmt:dpmt,								//部门类型
			            	checkDeptSn:checkDeptSn,				//检查部门
				            departmentSn:departmentSn,				//被检部门
				            qSpecialitySn:qSpecialitySn,			//专业
				            inconformityLevel:inconformityLevel,	//不符合项等级
				            timeData:timeData,						//按钮选择时间
				            indexSn:indexSn,						//指标编号
				            checkType:checkType,					//检查类型
					        checkerFrom:checkerFrom,				//检查人来自
					        checkers:checkers,						//检查人
					        correctPrincipal:correctPrincipal,		//整改负责人
					        hasCorrectConfirmed:hasCorrectConfirmed,//已整改确认
					        hasReviewed:hasReviewed,				//已复查
					        hasCorrectFinished:hasCorrectFinished,	//已整改完成
						    inconformityItemNature:inconformityItemNature,	//不符合项性质
				            standardSn:$('#standard').combotree('getValue'),//标准编号
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
					   var url = '${pageContext.request.contextPath}/unsafeCondition/query/queryAction_exportQueryUnsafeCondition.action'+'?'+params;
					   //console.log(url);
 					   $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
					   
				    });
				   $('#newAdd select').combobox({
					  	width:200,
						editable:false,
					  	panelWidth:200,
					  	panelHeight:'auto',
					  	onSelect:function(rec){
					  		
					  		eval($(this).attr("id") + "='" + rec.value + "';");
					  		loadAction();<!--发送数据-->
					  		
					  		/*
					  		switch ($(this).attr("id")) {
				  			case "hasCorrectConfirmed":
				  				hasCorrectConfirmed = rec.value;
				  				break;
				  			case "hasReviewed":
				  				hasReviewed = rec.value;
				  				break;
				  			case "hasCorrectFinished":
				  				hasCorrectFinished = rec.value;
				  				break;
							default:
								break;
					  		}
					  		alert($(this).attr("id") + ": " + rec.value);
					  		
					  		*/
//  					  		console.log("hasCorrectConfirmed: " + hasCorrectConfirmed + "\r\n" + 
//  					  				"hasReviewed: " + hasReviewed + "\r\n" + 
//  					  				"hasCorrectFinished: " + hasCorrectFinished + "\r\n");
					  		
					  	}
				   }); 
				   $('#keyStandardIndex').switchbutton({
						width:50,
						height:22,
						onText:'是',
						offText:'否',
					    checked: false,
					    onChange: function(rec){ 
					      //console.log(rec); 
					      checked = rec;
					      loadAction();
					    } 
					});
				    
			});
		 
			
			
	    </script>
</head>
<body>
		<table id="dg"></table>
		<div id="query">
		<form id="ff">
			<!-- 部门和部门/单位类型的div -->
			<div class="div-height">
					<div  class="div-height div-show" >
							<lable for="department">&emsp;被检部门：&emsp;</lable><input id="department"  name="departmentSn" />
					</div>
					<div id="unit" class="div-height div-show"></div>
					<div id="checkDeptDiv" class="div-height div-show">
							<lable for="checkDept">&emsp;检查部门：</lable><input id="checkDept"  name="checkDeptSn" />
					</div>
			</div>
			<!-- 专业 -->
			<div id="button" class="div-height"></div>
			
			 <div class="div-height">
				     <!-- 检查人 -->
					<div id="checkP" class="div-show">
							<lable for="checkP">&emsp;检查人：&emsp;&emsp;</lable><input id="checkPerson"  name="checkPerson" />
					</div>
					<!-- 整改负责人 -->
					<div id="checkZ" class="div-show">
							<lable for="checkZ">整改负责人：&emsp;</lable><input id="correctPrincipalSn"  name="correctPrincipalSn" />
					</div>
					<!-- 导出数据 -->
					<div id="exportDiv" class="div-show">
							<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导出数据</a>
					</div>
			</div>

			<div id="panel" style="width:100%;height:200px;background:#fafafa;">
		                   
	     
			     	<div class="div-height">
						     <!-- 检查类型 -->
							<div id="checkT" class="div-show">
									<lable for="checkT">&emsp;检查类型：&emsp;</lable><a id="btn12" class="easyui-linkbutton"  value="0">动态检查</a>
										<a id="btn13" class="easyui-linkbutton"  value="1">定期检查</a>
										<a id="btn14" class="easyui-linkbutton"  value="2">专项检查</a>
							</div>
							<!-- 检查人来自 -->
							<div id="checkF" class="div-show">
									<lable for="checkF">&emsp;检查人来自：&emsp;</lable><a id="btn15" class="easyui-linkbutton"  style="margin-left:2px" value="0">区队</a>
										<a id="btn16" class="easyui-linkbutton"  value="1">单位</a>
										<a id="btn17" class="easyui-linkbutton"  value="2">业务部门</a>
										<a id="btn18" class="easyui-linkbutton"  value="3">高层管理人员</a>
										<a id="btn19" class="easyui-linkbutton"  value="4">神华</a>
										<a id="btn20" class="easyui-linkbutton"  value="5">外部</a>
							</div>
					</div>
					<div class="div-height">
					    <!-- 不符合项等级 -->
					    <div id="gradebtn" class="div-show">
				    		<label for="grade">&emsp;原风险等级：</label><a id="btn1" class="easyui-linkbutton"  value="0" >一般风险</a>
							<a id="btn2" class="easyui-linkbutton"  value="1" >中等风险</a>
							<a id="btn30" class="easyui-linkbutton"  value="2" >重大风险</a>
					    </div>
					    <!-- 不符合项性质 -->
						<div id="checkN" class="div-show" style="margin-left:10px">
							<lable for="checkN">不符合项性质：</lable><a id="btn21" class="easyui-linkbutton"  value="0" style="margin-left:3px" >环境</a>
								<a id="btn22" class="easyui-linkbutton"  value="1">机器设备</a>
								<a id="btn23" class="easyui-linkbutton"  value="2">行为</a>
								<a id="btn24" class="easyui-linkbutton"  value="3">过程</a>
								<a id="btn25" class="easyui-linkbutton"  value="4">记录和数据</a>
								<a id="btn26" class="easyui-linkbutton"  value="5">文件</a>
						</div>
					</div>
				<div id="newAdd" class="div-height">
			    		<div  class="div-show">
								<lable for="hasCorrectConfirmed">&emsp;已整改确认：</lable><select id="hasCorrectConfirmed" class="easyui-combobox" name="hasCorrectConfirmed" >   
									    <option value="" selected="selected">全部</option>   
									    <option value="true">已整改确认</option>   
									    <option value="false">未整改确认</option>  
								</select>
						</div>
						<div  class="div-show">
								<lable for="hasReviewed">已复查：&emsp;&emsp;&emsp;</lable><select id="hasReviewed" class="easyui-combobox" name="hasReviewed" >   
									    <option value="" selected="selected">全部</option>   
									    <option value="true">已复查</option>   
									    <option value="false">未复查</option>  
								</select>
						</div>
						<div  class="div-show" style="margin-left: 10px">
								<lable for="hasCorrectFinished">已整改完成：</lable><select id="hasCorrectFinished" class="easyui-combobox" name="hasCorrectFinished" >   
									    <option value="" selected="selected">全部</option>   
									    <option value="true">已整改完成</option>   
									    <option value="false">未整改完成</option>  
								</select>
						</div>
				</div>
				
			    <div class="div-height">
			    		<div  class="div-show">
							<lable for="standard">&emsp;标&emsp;&emsp;准：&emsp;</lable><input id="standard"  name="standardSn" />
						</div>
						<div  class="div-show" style="margin-left: 20px">
							<lable for="standardindex">指&emsp;&emsp;标：</lable>
							<input id="standardindex"  name="indexSn" />
						</div>
						<span>
							<lable for="selectbox">查看关键指标：</lable>
							<input id="keyStandardIndex" style="width:200px;height:30px">
							<a id="btn11" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
						</span>
				</div>
				<div id="shijian" class="div-show">
						<lable for="beginTime">&emsp;时&emsp;&emsp;间：&emsp;</lable><a id="btn3" class="easyui-linkbutton"  value="today">今天</a>
							<a id="btn10" class="easyui-linkbutton" value="week">本周</a>
							<a id="btn4" class="easyui-linkbutton" value="xun">本旬</a>
							<a id="btn5" class="easyui-linkbutton" value="month">本月</a>
							<a id="btn6" class="easyui-linkbutton" value="quarter">本季度</a>
							<a id="btn7" class="easyui-linkbutton" value="banyear">本半年</a>
							<a id="btn8" class="easyui-linkbutton" value="year">全年</a>
				</div>
				<div class="div-show">
						<div  class="div-show" style="margin-left:18px">
								<lable for="beginTime">开始时间：</lable>
								<input id="beginTime" name="beginTime" type="text" />
						</div>
						<div class="div-show" >
								<lable for="endTime">结束时间：</lable>
								<input id="endTime" name="endTime" type="text" />
						</div>
						<a id="btn9" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</div>

			</div>  
			
		</form>	
	</div>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	
	
</body>
</html>