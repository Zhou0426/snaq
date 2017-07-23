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
			var standardIndexValue = new Array();
			var hazardValue = new Array();
			var correctPrincipalValue = new Array();
			var inconformityLevelValue = new Array();
			var specialityValue = new Array();
			var deductPointsValue = new Array();
			var machineValue = new Array();
			var inconformityItemNatureValue = new Array();
			var checkersValue = new Array();
			var inconformityItemSnValue = new Array();
			var editorDateTimeValue=new Array();
			var confirmTimeValue=new Array();
			
			<!-- 不安全行为 -->
			var inconformityItemSnValueUnsafeAct = new Array();
			var specialityValueUnsafeAct = new Array();
			var unsafeActMarkValueUnsafeAct = new Array();
			var unsafeActStandardValueUnsafeAct = new Array();
			var UnsafeActLevelValueUnsafeAct = new Array();
			var timeDataUnsafeAct = "";
			//全局变量
			var timeData = "";
			var checkers = "${sessionScope.personId}";
			 
	        <!--展示指标详情-附件-->
	        function showAttachments(){
	        	$('#win').window({
					title:"附件详情",
					width:400,
					height:300,
					content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/compositive_show" frameborder="0" width="100%" height="100%" />'
				});
	        };
	        <!--按钮点击事件加载数据发送action-->
	        function loadAction(){
	        	$('#dg').datagrid('load', {
	        		departmentSn:1,
	        		checkers:checkers,
	        		timeData:timeData
		        });
	        };
	        <!--查询按钮点击事件加载数据发送action-->
	        function searchAction(){
	        	$("#shijian a").removeClass("active");
	        	timeData="";
	        	$('#dg').datagrid('load', {
	        		departmentSn:1,
	        		checkers:checkers,
		            beginTime:$('#beginTime').datebox('getValue'),
		            endTime:$('#endTime').datebox('getValue')
		        });
	        };
	        
	        
	        <!-- 不安全行为 -->
	        <!--按钮点击事件加载数据发送action-->
	        function loadActionUnsafeAct(){
	        	$('#dg2').datagrid('load', {
	        		departmentSn:1,
	        		checkers:checkers,
	        		timeData:timeDataUnsafeAct
		        });
	        };
	        <!--查询按钮点击事件加载数据发送action-->
	        function searchActionUnsafeAct(){
	        	$("#shijianUnsafeAct a").removeClass("active");
	        	timeDataUnsafeAct = "";
	        	$('#dg2').datagrid('load', {
	        		departmentSn:1,
	        		checkers:checkers,
		            beginTime:$('#beginTimeUnsafeAct').datebox('getValue'),
		            endTime:$('#endTimeUnsafeAct').datebox('getValue')
		        });
	        };
	        <!--展示指标详情-附件-->
	        function showAttachmentsUnsafeAct(){
	        	$('#win').window({
					title:"附件详情",
					width:400,
					height:300,
					content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/unsafeactPersonal_show" frameborder="0" width="100%" height="100%" />'
				});
	        };
			$(function(){
				$.post("${pageContext.request.contextPath}/office/appraisals/office_checkTaskGetByCheckerId.action",{checkerId:"${sessionScope.personId}"},function(result){
					//var as=result.split("##");
					//$('#checkTask').html("&emsp;本月任务数量:"+as[0]+"&emsp;本月任务完成量:"+parseFloat(as[1]));
					//console.log(result);
					var data = "";
					for (var da in result) {
						if(da == "本月隐患任务数量"){
							data = data + "&emsp;" + da + "：" + result[da];
							break;
						}
					}
					for (var da in result) {
						if(da == "不安全行为任务数量"){
							data = data + "&emsp;" + da + "：" + result[da];
							break;
						}
					}
					for (var da in result) {
						if(da != "不安全行为任务数量" && da != "本月隐患任务数量"){
							data = data + "&emsp;" + da + "：" + result[da];
						}
					}
					$('#checkTask').html(data);
				},"json");
				$('#btn9').bind('click', function(){
					searchAction();
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
				$('#dg').datagrid({
					url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showDataPersonal.action',
					idField: 'id',
		            toolbar:'#query',
		            queryParams:{departmentSn:1,checkers:checkers},
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
					          {field:'checkedDepartmentImplType',title:'贯标单位',width:100,align:'center'},
					          {field:'checkedDepartment',title:'被检部门',width:100,align:'center'},
					          {field:'checkType',title:'检查类型',width:65,align:'center'},
					          {field:'systemAudit',title:'审核类型',width:65,align:'center'},
					          {field:'checkerFrom',title:'检查人来自',width:100,align:'center'},    
					          {field:'checkDateTime',title:'检查时间',width:70,align:'center'},
					          {field:'checkLocation',title:'检查地点',width:100,align:'center'},    
					          {field:'problemDescription',title:'问题描述',width:300,align:'center'},
					          {field:'riskLevel',title:'原风险等级',width:65,align:'center'},
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
						'<td style="width:90px;text-align:center">'+'扣分项：' + '</td>' + 
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
						'<td style="width:200px;text-align:center" colspan="9">' + 
						'<p>' + hazardValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr><tr>' +
						
						'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
						'<td style="width:100px;text-align:center" colspan="9">' + 
						'<p>' + standardIndexValue[rowIndex] + '</p>' + 
						'</td>' +
						'</tr></table>'; 
						} 			
				}); 

				$('#btnUnsafeAct9').bind('click', function(){
					searchActionUnsafeAct();
			    });
		        <!--时间选择的数据-->
		        $("#shijianUnsafeAct a").click(function(){
		        	var $this = $(this);
		        	if($this.hasClass("active")){
		           		$this.removeClass("active")
		           		timeDataUnsafeAct="";
		           		loadActionUnsafeAct();<!--发送数据-->
		        	}else{
		        		$("#shijianUnsafeAct a").removeClass("active")
		            	$this.addClass("active")
		            	timeDataUnsafeAct = $this.attr("value");
		        		loadActionUnsafeAct();<!--发送数据-->
		        	}
			     });
				$('#beginTimeUnsafeAct').datebox({
				    value: '3/4/2010',    
				    panelWidth:200,
				    width:200, 
				    required: true,
				    editable:false,
				    showSeconds: false   
				}); 
				$('#endTimeUnsafeAct').datebox({    
				    value: '3/4/2010',  
				    panelWidth:200,
				    width:200,  
				    required: true,
				    editable:false,
				    showSeconds: true   
				});
				$('#dg2').datagrid({
					url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_showDataPersonal.action',
					idField: 'id',
		            //title:'查询结果',
		            toolbar:'#queryUnsafeAct',
		            queryParams:{departmentSn:1, checkers:checkers},
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
					        			  inconformityItemSnValueUnsafeAct[index]=value;
					        		  }else{
					        			  inconformityItemSnValueUnsafeAct[index]="无";
					        		  }
					  		  }},
					          {field:'speciality',title:'所属专业',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  specialityValueUnsafeAct[index]=value;
					        		  }else{
					        			  specialityValueUnsafeAct[index]="无";
					        		  }
					  		  }},
					          {field:'unsafeActMark',title:'不安全行为痕迹',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  unsafeActMarkValueUnsafeAct[index]=value;
					        		  }else{
					        			  unsafeActMarkValueUnsafeAct[index]="无";
					        		  }
					  		  }},
					          {field:'unsafeActStandard',title:'不安全行为标准',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  unsafeActStandardValueUnsafeAct[index]=value;
					        		  }else{
					        			  unsafeActStandardValueUnsafeAct[index]="无";
					        		  }
					  		  }},
					          {field:'UnsafeActLevel',title:'不安全行为等级',width:120,align:'center',hidden:true,
					        	  formatter: function(value,row,index){
					        		  if(value!=null && value!=""){
					        			  UnsafeActLevelValueUnsafeAct[index]=value;
					        		  }else{
					        			  UnsafeActLevelValueUnsafeAct[index]="无";
					        		  }
					  		  }},
					          {field:'attachments',title:'附件详情',width:50,align:'center',
					        	  formatter: function(value,row,index){
					  				if (value!=0){
					  					return "<a href='javascript:;' onclick=showAttachmentsUnsafeAct()>附件["+value+"]</a>";
					  				} else {
					  					return "<a href='javascript:;' onclick=showAttachmentsUnsafeAct()>附件[0]</a>";
					  				}
					  		  }}
					     ]],
						 onDblClickCell: function(){
							$('#dg').datagrid("uncheckAll");
						 },
						view: detailview, 
						detailFormatter: function(rowIndex, row){
						return '<table style="border:1"><tr>' + 								
							'<td style="width:100px;text-align:center">'+'不符合项编号：' + '</td>' + 
							'<td style="width:140px;text-align:center">' + 
							'<p>' + inconformityItemSnValueUnsafeAct[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'不安全行为等级：' + '</td>' + 
							'<td style="width:140px;text-align:center">' + 
							'<p>' + UnsafeActLevelValueUnsafeAct[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'专业：' + '</td>' + 
							'<td style="width:100px;text-align:center" >' + 
							'<p>' + specialityValueUnsafeAct[rowIndex] + '</p>' + 
							'</td>' +
							'<td style="width:100px;text-align:center">'+'痕迹：' + '</td>' + 
							'<td style="width:100px;text-align:center">' + 
							'<p>' + unsafeActMarkValueUnsafeAct[rowIndex] + '</p>' + 
							'</td>' +
							'</tr><tr>' +
							
							'<td style="width:100px;text-align:center">'+'标准：' + '</td>' + 
							'<td style="width:380px;text-align:center" colspan="7">' + 
							'<p>' + unsafeActStandardValueUnsafeAct[rowIndex] + '</p>' + 
							'</td>' +
							'</tr></table>'; 
						}
				});
					    
			});
		 
			
			
	    </script>
</head>
<body>
		<div id="tt" class="easyui-tabs" data-options="fit:true">   
		    <div title="隐患"  style="padding:1px;display:none;">  
				<table id="dg"></table>
			</div>   
		    <div title="不安全行为" style="padding:1px;display:none;"> 
		    	<table id="dg2"></table>
		    </div>  
		</div> 
		<div id="query">
			<form id="ff">
				<div class="div-height"><a id="checkTask"></a></div>
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
				
			</form>	
		</div>
		<div id="queryUnsafeAct">
			<form id="ffUnsafeAct">
				<div id="shijianUnsafeAct" class="div-show">
						<lable for="beginTimeUnsafeAct">&emsp;时&emsp;&emsp;间：&emsp;</lable><a id="btnUnsafeAct3" class="easyui-linkbutton"  value="today">今天</a>
								<a id="btnUnsafeAct10" class="easyui-linkbutton"  value="week">本周</a>
								<a id="btnUnsafeAct4" class="easyui-linkbutton"  value="xun">本旬</a>
								<a id="btnUnsafeAct5" class="easyui-linkbutton"  value="month">本月</a>
								<a id="btnUnsafeAct6" class="easyui-linkbutton"  value="quarter">本季度</a>
								<a id="btnUnsafeAct7" class="easyui-linkbutton"  value="banyear">本半年</a>
								<a id="btnUnsafeAct8" class="easyui-linkbutton"  value="year">全年</a>
				</div>
				<div class="div-show">
						<div class="div-show" style="margin-left:18px">
								<lable for="beginTimeUnsafeAct">开始时间：</lable>
								<input id="beginTimeUnsafeAct" name="beginTimeUnsafeAct" type="text" />
						</div>
						<div class="div-show" >
								<lable for="endTimeUnsafeAct">结束时间：</lable>
								<input id="endTimeUnsafeAct" name="endTimeUnsafeAct" type="text" />
						</div>
						<a id="btnUnsafeAct9" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</div> 
				
			</form>	
		</div>
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	
</body>
</html>