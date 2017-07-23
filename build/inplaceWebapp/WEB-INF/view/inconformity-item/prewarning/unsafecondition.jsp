<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>不符合项预警</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<style type="text/css">
		.active {
			background: #B7D2FF;
			color: #000;
		}
		
		.div-height {
			margin: 6px auto;
		}
		
		.div-show {
			display: inline;
			margin: 5px auto;
		}
</style>
<script type="text/javascript">
				var a=1;
				var pag="2";
				var dpmt="";
				var timeData="";
				var riskLevel="";
				var departmentSn="";
				var qSpecialitySn="";
				var inconformityLevel="";
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
				
				var beginTime = "";
				var endTime = "";
				
				$(function(){
					$('#dg').datagrid({
						url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showData.action',
						idField: 'id',
			            title:'隐患预警',
			            toolbar:'#query',
			            rownumbers: true,		//显示一个行号列
			            fitColumns:true,		//自动适应列
			            //height:400,
			           	fit:true,				//表格宽高自适应
			            nowrap:false,
			            striped:true,			//斑马线效果
						singleSelect:true,		//单行选中
			            loadmsg:'请等待...',		//加载等待时显示
			            pagination:true,		//显示分页组件
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
						          {field:'riskLevel',title:'不符合项等级',width:65,align:'center'},
						          {field:'currentRiskLevel',title:'现风险等级',width:65,align:'center',
							  			styler: function(value,row,index){
							  				if(value=="重大风险"){
								        	  	return 'background-color:red';
											}else if(value=="中等风险"){
												return 'background-color:orange';
											}else{
												return 'background-color:yellow';
											}
							  			}
							       },
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
							  				if (value!=0){
							  					return "<a href='javascript:;' onclick=showAttachments()>附件["+value+"]</a>";
							  				} else {
							  					return "<a href='javascript:;' onclick=showAttachments()>附件[0]</a>";
							  				}
						  			}}
						     ]],
							 onDblClickCell: function(index, row){
								$(this).datagrid("uncheckAll");
							},
// 							rowStyler:function(index,row){
// 								if(row.hasCorrectFinished=="未整改完成"){
// 						        	  	return showColor(row,index);
// 								}
// 							},
							onBeforeLoad:function(){
								if(pag=="2"){
									return false;
								}else{
									return true;
								}
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
							'</tr><tr>' +
							
							'<td style="width:90px;text-align:center">'+'危险源：' + '</td>' + 
							'<td style="width:200px;text-align:center"colspan="7">' + 
							'<p>' + hazardValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr><tr>' +
							
							'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
							'<td style="width:100px;text-align:center" colspan="7">' + 
							'<p>' + standardIndexValue[rowIndex] + '</p>' + 
							'</td>' +
							'</tr></table>'; 
							}
						});
					$('#department').combotree({
						url:'${pageContext.request.contextPath}/department/treeAll.action',
						method:'post',
						valueField: 'id',    
				        textField: 'text', 
						required:true,
						queryParams:{'resourceSn':'0401'},
						panelWidth: 300,
						panelHeight:350,
						width:200,
						onLoadSuccess:function(node,data){
							<!--设置数据加载成功时的默认值-->
							var da=$('#department').combotree('tree').tree('getRoot');
							$('#department').combotree('setValue',da.id);
							//第一次加载时执行的事件
							if(a==1){
								departmentSn=da.id;
								crazyDo();
								a++;
								if(data.length == 1){
									var node = $('#department').combotree('tree').tree('find', data[0].id);
									$('#department').combotree('tree').tree('expand', node.target);
					        	}
							}
						},
						onSelect: function(data){
								dpmt="";
								departmentSn=data.id;
								crazyDo();
				        }
				});
					 <!--不符合项等级的数据-->
						$("#gradebtn a").click(function(){  
					        	var $this = $(this);
					        	if($this.hasClass("active")){
					           		$this.removeClass("active")
					           		inconformityLevel="";
					           		//console.log(riskLevel);
					           		loadAction();<!--发送数据-->
					        	}else{
					        		$("#gradebtn a").removeClass("active")
					            	$this.addClass("active")
					            	inconformityLevel=$this.attr("value");
					            	//console.log(riskLevel);
					        		loadAction();<!--发送数据-->
					        	}
					      });
					 <!--风险等级的数据-->
						$("#riskbtn a").click(function(){  
					        	var $this = $(this);
					        	if($this.hasClass("active")){
					           		$this.removeClass("active")
					           		riskLevel="";
					           		//console.log(riskLevel);
					           		loadAction();<!--发送数据-->
					        	}else{
					        		$("#riskbtn a").removeClass("active")
					            	$this.addClass("active")
					            	riskLevel=$this.attr("value");
					            	//console.log(riskLevel);
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
				           		//console.log(timeData);
				        	}else{
				        		$("#shijian a").removeClass("active")
				            	$this.addClass("active")
				            	timeData=$this.attr("value");
				        		loadAction();<!--发送数据-->
				            	//console.log(timeData);
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
							searchAction();
					    });
						$('#export').bind('click', function(){
							   var dataParams = {
									pag:pag,
					        		dpmt:dpmt,						//部门类型
						            timeData:timeData,				//按钮选择时间
						            departmentSn:departmentSn,		//部门
						            qSpecialitySn:qSpecialitySn,	//专业
						            riskLevel:riskLevel,			//风险水平
						            inconformityLevel:inconformityLevel, //不符合项等级	
					            	beginTime :beginTime,
					            	endTime : endTime
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
		});
				<!--datagrid加载数据发送action-->
		        function loadAction(){
		        	beginTime = "";
					endTime = "";
		        	//var url='${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showData.action';
		        	pag="pag";
		        	$('#dg').datagrid('load', {
		            	pag:pag,
	        			dpmt:dpmt,						//部门类型
		            	timeData:timeData,				//按钮选择时间
		            	departmentSn:departmentSn,		//部门
		            	qSpecialitySn:qSpecialitySn,	//专业
		            	riskLevel:riskLevel,			//风险水平
		            	inconformityLevel:inconformityLevel //不符合项等级	
			        }); 
		        };
		        <!--时间查询按钮发送action-->
		        function searchAction(){
		        	$("#shijian a").removeClass("active");
		        	timeData="";
		        	pag="pag";
		        	beginTime = $('#beginTime').datebox('getValue');
					endTime = $('#endTime').datebox('getValue');
		        	$('#dg').datagrid('load', {
		            	pag:pag,
	        			dpmt:dpmt,						//部门类型
		            	timeData:timeData,				//按钮选择时间
		            	departmentSn:departmentSn,		//部门
		            	qSpecialitySn:qSpecialitySn,	//专业
		            	riskLevel:riskLevel,			//风险水平
		            	inconformityLevel:inconformityLevel, //不符合项等级	
		            	beginTime :beginTime,
		            	endTime : endTime
			        }); 
		        };
				function crazyDo(){
					<!--清空专业按钮的值-->
					qSpecialitySn="";
					loadAction();<!--发送数据-->
					
					$.post("${pageContext.request.contextPath}/unsafeCondition/query/queryAction_unitType.action",
								{departmentSn:departmentSn},function(res){
									
								$("#unit").empty();
								$("#button").empty();
								
								if(res.length>0){
										$("#unit").append("<lable for='unitType'>单位类型：&emsp;&emsp;</lable>");
										
										for(var i = 0;i < res.length;i++){
											$("#unit").append("<a class='easyui-linkbutton'  id='" + res[i].value + "'  value='" + res[i].value + "'>" +res[i].text+ "</a> ");
										}
										$.parser.parse($('#unit').parent());
										
										$("#unit a").click(function(){ 
											
											<!--清空专业按钮的值-->
											qSpecialitySn="";
											
											val=$(this).attr("value");		//标签按钮的id=value
											
											dpmt=$(this).attr("value");
											
											major(val);
											
										});
// 										if(res.length==1){
// 											$('#unit').css('display','none');
// 											$('#'+res[0].value+'').click(); 
// 										}else{
// 											$('#unit').css('display','');
// 										}
								}
							},"json");
				};
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
						
						<!--清空专业组件-->
		        		$("#button").empty();
		        		<!--清空专业按钮的值-->
						qSpecialitySn="";
						loadAction();<!--发送数据-->
		           		$this.removeClass("active")
		           		
		        	}else{
		            	<!--如果之前没点击，则生成点击事件-->
			        	//alert($this.attr("value"));
			        	qSpecialitySn="";
			        	dpmt=$this.attr("value");
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
									        		qSpecialitySn = qSpecialitySn.replace($this.attr("value")+"##","");
									        		loadAction();<!--发送数据-->
									           		$this.removeClass("active")
									           		
									        	}else{

									        		if (qSpecialitySn.indexOf($this.attr("value") +"##") == -1) {
									        			qSpecialitySn+= $this.attr("value") +"##";
													}
									        		loadAction();<!--发送数据-->
									            	$this.addClass("active")
									        	}
											});
									}
						},"json");
						
			        	$("#unit a").removeClass("active")
		            	$this.addClass("active")
		        	}
		        };
		        //判断显示颜色
// 		        function showColor(row,index){
// 					var nowTime=getNowFormatDate();
// 					if(row.correctDeadline!=null){
// 							var oldTime=row.correctDeadline.replace(".0","");
// 							var a = (Date.parse(nowTime)-Date.parse(oldTime))/3600/1000;
// 							if(row.inconformityLevel=="观察项"){
// 									if(a>=8 && a<=24){
// 											return 'background-color:orange';
// 									}else if(a>24){
// 											return 'background-color:red';
// 									}else{
// 											return 'background-color:yellow';
// 									}
// 							}else if(row.inconformityLevel=="一般不符合项"){
// 									if(a<=8){
// 											return 'background-color:orange';
// 									}else{
// 											return 'background-color:red';
// 									}
// 							}else if(row.inconformityLevel=="严重不符合项"){
// 											return 'background-color:red';
// 							}
// 					}else{
// 						if(row.checkDateTime!=null){
// 							var oldTime=row.checkDateTime.replace(".0","");
// 							var a = (Date.parse(nowTime)-Date.parse(oldTime))/3600/1000;
// 							if(row.inconformityLevel=="观察项"){
// 									if(a<16){
// 											return 'background-color:yellow';
// 									}else if(a>=16 && a<=32){
// 											return 'background-color:orange';
// 									}else{
// 											return 'background-color:red';
// 									}
// 							}else if(row.inconformityLevel=="一般不符合项"){
// 									if(a<16){
// 											return 'background-color:orange';
// 									}else if(a>=16){
// 											return 'background-color:red';
// 									}
// 							}else if(row.inconformityLevel=="严重不符合项"){
// 										return 'background-color:red';
// 							}
// 						}
// 					}
// 			    };

// 				//判断现在危险源
// 				function showRiskLevel(value,row,index){
// 					var nowTime=getNowFormatDate();
// 					if(row.correctDeadline!=null){
// 							var oldTime=row.correctDeadline.replace(".0","");
// 							var a = (Date.parse(nowTime)-Date.parse(oldTime))/3600/1000;
// 							if(row.inconformityLevel=="观察项"){
// 									if(a>=8 && a<=24){
// 											return "中等风险";
// 									}else if(a>24){
// 											return "重大风险";
// 									}else{
// 											return "一般风险";
// 									}
// 							}else if(row.inconformityLevel=="一般不符合项"){
// 									if(a<=8){
// 											return "中等风险";
// 									}else{
// 											return "重大风险";
// 									}
// 							}else if(row.inconformityLevel=="严重不符合项"){
// 											return "重大风险";
// 							}
// 					}else{
// 						if(row.checkDateTime!=null){
// 							var oldTime=row.checkDateTime.replace(".0","");
// 							var a = (Date.parse(nowTime)-Date.parse(oldTime))/3600/1000;
// 							if(row.inconformityLevel=="观察项"){
// 									if(a<16){
// 											return "一般风险";
// 									}else if(a>=16 && a<=32){
// 											return "中等风险";
// 									}else{
// 											return "重大风险";
// 									}
// 							}else if(row.inconformityLevel=="一般不符合项"){
// 									if(a<16){
// 											return "中等风险";
// 									}else if(a>=16){
// 											return "重大风险";
// 									}
// 							}else if(row.inconformityLevel=="严重不符合项"){
// 										return "重大风险";
// 							}
// 						}
// 					}
// 				};
// 			    //获取当前时间(yyyy-mm-dd hh:mm:ss)
// 			    function getNowFormatDate() {
// 			        var date = new Date();
// 			        var seperator1 = "-";
// 			        var seperator2 = ":";
// 			        var month = date.getMonth() + 1;
// 			        var strDate = date.getDate();
// 			        if (month >= 1 && month <= 9) {
// 			            month = "0" + month;
// 			        }
// 			        if (strDate >= 0 && strDate <= 9) {
// 			            strDate = "0" + strDate;
// 			        }
// 			        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
// 			                + " " + date.getHours() + seperator2 + date.getMinutes()
// 			                + seperator2 + date.getSeconds();
// 			        return currentdate;
// 			    };
			    <!--展示指标详情-附件-->
		        function showAttachments(){
		        	$('#win').window({
						title:"附件详情",
						width:400,
						height:300,
						content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/compositive_show" frameborder="0" width="100%" height="100%" />'
					});
		        };
		</script>
</head>
<body>
	<table id="dg"></table>
	<div id="query">
		<!-- 部门和部门/单位类型的div -->
		<div class="div-height">
			<div class="div-height div-show">
				<lable for="department">&emsp;查询范围：&emsp;</lable><input id="department" name="departmentSn" />
			</div>
			<div id="unit" class="div-height div-show"></div>
		</div>
		<!-- 专业 -->
		<div id="button" class="div-height"></div>
		<!--现风险等级 -->
		<div class="div-height">
			<div id="riskbtn" class="div-show">
				<label for="riskLevel">现风险等级：&emsp;</label><a id="btn1"
					class="easyui-linkbutton" value="0">一般风险</a> <a id="btn2"
					class="easyui-linkbutton" value="1">中等风险</a> <a id="btn3"
					class="easyui-linkbutton" value="2">重大风险</a>
			<!-- 不符合项等级 -->
				<label for="grade">&emsp;不符合项等级：&emsp;</label><a id="btn4"
					class="easyui-linkbutton" value="0">观察项</a> <a id="btn5"
					class="easyui-linkbutton" value="1">一般不符合项</a> <a id="btn30"
					class="easyui-linkbutton" value="2" >严重不符合项</a>
				<!-- 导出数据 -->
			</div>
			<div id="exportDiv" class="div-show">
				<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-excel'">导出数据</a>
			</div>
		</div>
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
	<div id="win"
		data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>