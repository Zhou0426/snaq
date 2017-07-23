<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>不安全行为预警</title>
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
				var departmentTypeSn="";
				var departmentSn="";
				var endTime="";
				
				$(function(){
					$('#dg').datagrid({
						url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_showViolator.action',
						idField: 'id',
			            title:'不安全行为预警',
			            toolbar:'#query',
			            rownumbers: true,		//显示一个行号列
			            fitColumns:true,		//自动适应列
			           	fit:true,				//表格宽高自适应
			            nowrap:false,
			            striped:true,			//斑马线效果
						singleSelect:true,		//单行选中
			            loadmsg:'请等待...',		//加载等待时显示
			            pagination:true,		//显示分页组件
			            pageNumber:1,
			            pageSize:20,
			            pageList:[10,15,20,25,30,35,40],
						columns:[[
								  {field:'personId',title:'不安全行为人员编号',width:100,align:'center'},
						          {field:'personName',title:'不安全行为人员姓名',width:100,align:'center'},
						          {field:'implDepartmentName',title:'贯标单位',width:100,align:'center'},
						          {field:'department',title:'所属部门',width:100,align:'center'},
						          {field:'riskLevel',title:'风险等级',width:100,align:'center',
						        	  styler: function(value,row,index){
						  				if (value == "重大风险"){
						  					return 'background-color:red;';
						  				}else if(value == "中等风险"){
						  					return 'background-color:orange;';
						  				}else if(value == "一般风险"){
						  					return 'background-color:yellow;';
						  				}
									  }
						          },
						          {field:'unsafeActs',title:'详情',width:100,align:'center',
						        	  formatter: function(value,row,index){
							  				if (value!=null){
							  					return "<a href='javascript:;' style='text-decoration:none' onclick=showDetails("+index+")>详情["+value+"]</a>";
							  				} else {
							  					return "<a href='javascript:;' style='text-decoration:none' onclick=showDetails("+index+")>详情[0]</a>";
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
						}
					});
					$('#department').combotree({
						url:'${pageContext.request.contextPath}/department/treeAll.action',
						method:'post',
						valueField: 'id',    
				        textField: 'text', 
						required:true,
						queryParams:{'resourceSn':'0402'},
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
					$('#endTime').datebox({    
					    value: '3/4/2010',  
					    panelWidth:200,
					    width:150,
					    editable:false,
					    onSelect:function(date){
					    	loadAction();<!--发送数据-->
					    }
					});
					var buttons = $.extend([], $.fn.datebox.defaults.buttons);
						//添加今天按钮
						buttons.splice(0, 1, {
							text: '今天',
							handler: function(target){
								var nowTime=getNowFormatDate();
								$('#endTime').datebox('setValue', nowTime);	// 设置日期输入框的值
								$('#endTime').datebox('hidePanel');
								loadAction();<!--发送数据-->
							}
						});
					$('#endTime').datebox({
						buttons: buttons
					});

		});
				//获取当前时间(yyyy-mm-dd hh:mm:ss)
			    function getNowFormatDate() {
			        var date = new Date();
			        var seperator1 = "-";
			        var month = date.getMonth() + 1;
			        var strDate = date.getDate();
			        if (month >= 1 && month <= 9) {
			            month = "0" + month;
			        }
			        if (strDate >= 0 && strDate <= 9) {
			            strDate = "0" + strDate;
			        }
			        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
			        return currentdate;
			    };
				<!--datagrid加载数据发送action-->
		        function loadAction(){
		        	$('#dg').datagrid('load', {
		        			departmentTypeSn:departmentTypeSn,	//部门类型
			            	departmentSn:departmentSn,			//部门
			            	endTime:$('#endTime').datebox('getValue')
			        }); 
		        };
				function crazyDo(){
					
					loadAction();<!--发送数据-->
					
					$.post("${pageContext.request.contextPath}/unsafeCondition/query/queryAction_unitType.action",
								{departmentSn:departmentSn},function(res){
									
								$("#unit").empty();
								
								if(res.length>0){
										$("#unit").append("<lable for='unitType'>&emsp;单位类型：&emsp;</lable>");
											
										for(var i = 0;i < res.length;i++){
												$("#unit").append("<a class='easyui-linkbutton'  id='" + res[i].value + "'  value='" + res[i].value + "'>" +res[i].text+ "</a> ");
										}
										$.parser.parse($('#unit').parent());
										
										$("#unit a").click(function(){											
											<!--如果已经点击，则取消点击-->
								        	if($(this).hasClass("active")){
								            	
								        		departmentTypeSn="";
												loadAction();<!--发送数据-->
												$(this).removeClass("active")
								           		
								        	}else{
								            	<!--如果之前没点击，则生成点击事件-->
									        	departmentTypeSn=$(this).attr("value");
									        	loadAction();<!--发送数据-->
									        	
									        	$("#unit a").removeClass("active")
								            	$(this).addClass("active")
								        	}
										});
								}
							},"json");
				};
		       
		        <!--展示详情-->
		        function showDetails(index){
		        	$("#dg").datagrid('selectRow',index);
		        	$('#win').window({
						title:"不安全行为详情",
						maximizable:true,
						width:900,
						height:400,
						content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/prewarning/unsafeact_show" frameborder="0" width="100%" height="100%" />'
					});
		        };
		</script>
</head>
<body>
	<table id="dg"></table>
	<div id="query">
		<div class="div-height">
				<!-- 部门div -->
				<div class="div-height div-show">
					<lable for="department">&emsp;查询范围：&emsp;</lable><input id="department" name="departmentSn" />
				</div>
				<!-- 部门类型 -->
				<div id="unit" class="div-height div-show"></div>
				<!-- 时间 -->
				<div  class="div-height div-show" style="margin-left:18px">
					<lable for="endTime">截止时间：</lable>
					<input id="endTime"  name="endTime"  type="text" />
				</div>
		</div>
			
	</div>
	<div id="win"
		data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>