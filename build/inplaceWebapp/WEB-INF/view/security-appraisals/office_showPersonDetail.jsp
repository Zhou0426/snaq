<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
	
	<style type="text/css">
		form div{
			margin:10px
		}
	</style>
	<script type="text/javascript">
		if(parent.$('#da').val() != "checkTask"){
			var rows = parent.$("iframe").get(0).contentWindow.$('#dg').datagrid('getSelections');
			var personId = rows[0].personId;
			var monthTime = parent.$('#da').val();
			var yearTime = parent.$('#selectTime').combobox('getValue');
		}else{
			var personId = parent.$('#personId').val();
			var monthTime = parent.$('#selectMonth').combobox('getValue');
			var yearTime = parent.$('#selectYear').combobox('getValue');
		}
		var endTime = getLastDay(yearTime,monthTime);
		if(monthTime < 10){
			monthTime = "0" + monthTime;
		}
		endTime = yearTime + "-" + monthTime + "-" + endTime;
		var beginTime = yearTime + "-" + monthTime + "-01";
		
		//隐患
		var standardIndexValue = new Array();
		var hazardValue = new Array();
		var correctPrincipalValue = new Array();
		var inconformityLevelValue = new Array();
		var specialityValue = new Array();
		var deductPointsValue = new Array();
		var machineValue = new Array();
		var inconformityItemNatureValue = new Array();
		var inconformityItemSnValue = new Array();
		//不安全行为
		var inconformityItemSnValue = new Array();
		var specialityValue = new Array();
		var unsafeActMarkValue = new Array();
		var unsafeActStandardValue = new Array();
		var UnsafeActLevelValue = new Array();
		
		$(function(){
			$('#dg').datagrid({
	            url: '${pageContext.request.contextPath}/unsafeCondition/query/queryAction_showData.action',
				idField: 'id',
				queryParams:{'departmentSn':1,'checkers':personId,'beginTime':beginTime,'endTime':endTime},
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
				          {field:'checkType',title:'检查类型',width:80,align:'center'},
				          {field:'systemAudit',title:'审核类型',width:80,align:'center',hidden:true},
				          {field:'checkers',title:'检查人',width:80,align:'left'},
				          {field:'checkerFrom',title:'检查人来自',width:100,align:'center',hidden:true},    
				          {field:'checkDateTime',title:'检查时间',width:80,align:'center'},
				          {field:'checkLocation',title:'检查地点',width:100,align:'center'},    
				          {field:'problemDescription',title:'问题描述',width:280,align:'center'},
				          {field:'riskLevel',title:'原风险等级',width:90,align:'center'},
		                  {field:'editor',width:80,title:'录入人'},
				          {field:'correctType',title:'整改类型',width:80,align:'center',hidden:true},
				          {field:'correctDeadline',title:'整改期限',width:80,align:'center',hidden:true},
				          {field:'correctProposal',title:'整改建议',width:150,align:'center',hidden:true},
				          {field:'hasCorrectConfirmed',title:'整改确认',width:80,align:'center',hidden:true},
				          {field:'hasReviewed',title:'复查',width:60,align:'center',hidden:true},
				          {field:'hasCorrectFinished',title:'整改完成',width:80,align:'center',hidden:true},
				          {field:'inconformityItemSn',title:'不符合项编号',width:120,align:'center',hidden:true,
				        	  formatter: function(value,row,index){
				        		  if(value!=null && value!=""){
				        			  inconformityItemSnValue[index]=value;
				        		  }else{
				        			  inconformityItemSnValue[index]="无";
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
				          {field:'attachments',title:'附件详情',width:90,align:'center',
				        	  formatter: function(value,row,index){
					        	  //console.log(value);
					  				if (value!=0){
					  					return "<a href='javascript:;' onclick=showUnsafecondition()>附件["+value+"]</a>";
					  				} else {
					  					return "<a href='javascript:;' onclick=showUnsafecondition()>附件[0]</a>";
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
					'<td style="width:150px;text-align:center">' + 
					'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'不符合项性质：' + '</td>' + 
					'<td style="width:70px;text-align:center">' + 
					'<p>' + inconformityItemNatureValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'不符合项等级：' + '</td>' + 
					'<td style="width:90px;text-align:center">' + 
					'<p>' + inconformityLevelValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:60px;text-align:center">'+'专业：' + '</td>' + 
					'<td style="width:60px;text-align:center">' + 
					'<p>' + specialityValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'整改负责人：' + '</td>' + 
					'<td style="width:60px;text-align:center">' + 
					'<p>' + correctPrincipalValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:60px;text-align:center">'+'扣分：' + '</td>' + 
					'<td style="width:60px;text-align:center">' + 
					'<p>' + deductPointsValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:60px;text-align:center">'+'机器：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + machineValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:100px;text-align:center">'+'危险源：' + '</td>' + 
					'<td style="width:200px;text-align:center"colspan="13">' + 
					'<p>' + hazardValue[rowIndex] + '</p>' + 
					'</td>' +
					
					'</tr><tr>' +
					
					'<td style="width:100px;text-align:center">'+'指标：' + '</td>' + 
					'<td style="width:100px;text-align:center" colspan="13">' + 
					'<p>' + standardIndexValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr></table>'; 
					} 			
			});
			$('#dg2').datagrid({
				url:'${pageContext.request.contextPath}/inconformity/item/unsafeActAction_showData.action',
				idField: 'id',
				queryParams:{'checkers':personId,'beginTime':beginTime,'endTime':endTime},
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
		                  {field:'systemAudit',title:'审核类型',width:50,align:'center',hidden:true},
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
					  					return "<a href='javascript:;' onclick=showUnsafeAct()>附件["+value+"]</a>";
					  				} else {
					  					return "<a href='javascript:;' onclick=showUnsafeAct()>附件[0]</a>";
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
		});
		 <!--展示不安全行为详情-附件-->
        function showUnsafeAct(){
        	$('#win').window({
				title:"附件详情",
				width:400,
				height:300,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/unsafeact_show" frameborder="0" width="100%" height="100%" />'
			});
        };
        <!--展示隐患详情-附件-->
        function showUnsafecondition(){
        	$('#win').window({
				title:"附件详情",
				width:400,
				height:300,
				content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/unsafeact_show" frameborder="0" width="100%" height="100%" />'
			});
        };
        function getLastDay(year,month){          
        	var new_year = year;    //取当前的年份           
        	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）           
        	if(month>12)            //如果当前大于12月，则年份转到下一年           
        		{          
         			new_month -=12;        //月份减           
         			new_year++;            //年份增           
         		}          
         	var new_date = new Date(new_year,new_month,1);                //取当年当月中的第一天           
         	var date_count =   (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月的天数         
         	var last_date =   new Date(new_date.getTime()-1000*60*60*24);//获得当月最后一天的日期  
        	return date_count;
        } 
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
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>