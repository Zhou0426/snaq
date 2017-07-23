<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事故录入</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<script>
	var str="${sessionScope['permissions']}";
	var accidentSnValue=new Array();
	var accidentProcessValue=new Array();
	var directReasonValue=new Array();
	var indirectReasonValue=new Array();
	var precautionMeasureValue=new Array();
	$(function() {
					<!--数据网络-->
					$('#dg').datagrid({
							pageNumber : 1,
							pagination : true,
							url : "${pageContext.request.contextPath}/accident/showAccident",
							rownumbers : true,
							fitColumns : true,
					        fit:true,
							pageSize : 10,
							singleSelect : true,
							nowrap:false,
							pageList : [ 5, 10, 15, 20 ],
							toolbar : [
									{
										iconCls : 'icon-add',
										text : '增加',
										id:'addtool',
										handler : function() {
											if(str.indexOf("150101")==-1){
												$("#addtool").css('display','none');
											}else{
												$('#addbtn').show();
												$('#editbtn').hide();
												$('#win').window('open');
												$('#form').form('disableValidation');
											}
										}
									},
									{
										iconCls : 'icon-edit',
										text : '修改',
										id:'edittool',
										handler : function() {
											if(str.indexOf("150102")==-1){
												$("#edittool").css('display','none');
											}else{	
								    			//数据回显
								    			var rows=$("#dg").datagrid("getSelections");
								    			if (rows.length>0){
													$('#editbtn').show();
													$('#addbtn').hide();	
									    			$('#win').window('open');
// 		   							    			$('#departmentSn').combobox('setValue',rows[0].deptSn);
// 		   							    			$('#departmentSn').combobox('setText',rows[0].departmentName);
									    			//$('#departmentSn').combobox('setValue',rows[0].departmentSn); 
									    			//$('#departmentSn').combobox('setValue',{value:rows[0].departmentSn,text:rows[0].departmentName});
									    			//对表单数据进行填充
									    			$('#form').form('load',{
									    				accidentSn:rows[0].accidentSn,
									    				accidentName:rows[0].accidentName,
									    				happenDateTime:rows[0].happenDateTime,
									    				happenLocation:rows[0].happenLocation,
									    				accidentProcess:rows[0].accidentProcess,
									    				reasonArticle:rows[0].reasonArticle,
									    				directReason:rows[0].directReason,
									    				indirectReason:rows[0].indirectReason,
									    				precautionMeasure:rows[0].precautionMeasure,							    				
									    				directEconomicLoss:rows[0].directEconomicLoss,
									    				indirectEconomicLoss:rows[0].indirectEconomicLoss,
									    				deadCount:rows[0].deadCount,
									    				seriousInjureCount:rows[0].seriousInjureCount,								    				
									    				fleshInjureCount:rows[0].fleshInjureCount,
									    				happenDateTime:rows[0].happenDateTime,
									    				accidentTypeSn:rows[0].accidentType,
									    				accidentLevelSn:rows[0].accidentLevel
									    			
									    			});
								    			}
												else
												{
													$.messager.show({
														title:'消息提示',
														msg:'请先选择您要编辑的行！',
														showType:'null',
														style:{
															top:'50'
														}
													});
												}
											}
										}
									},
									{
										iconCls : 'icon-remove',
										text : '删除',
										id:'deletetool',
										handler : function() {
											if(str.indexOf("150103")==-1){
												$("#deletetool").css('display','none');
											}else{
												var row = $('#dg').datagrid('getSelected');
												if (row) {
													$.messager.confirm('选择是否删除','您确定要删除该事故吗?',function(fn) {
														if(fn==true){
															$.post('${pageContext.request.contextPath}/accident/deleteAccident',{accidentSn:row.accidentSn},function(message){
																$.messager.alert('提示',message); 
																$('#dg').datagrid('reload');
															});
														}
													});
												} 
												else {
													$.messager.alert('提示','请先选择要删除的行');
												}
											}
										}
									} ],
							columns : [ [ 
							    {field : 'accidentSn',title : '事故编号',width : 150,hidden:true,
							    	formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  accidentSnValue[index]=value;
						        		  }else{
						        			  accidentSnValue[index]="无";
						        		  }
						  		  	  }	
							    }, 
							    {field : 'accidentName',title : '事故名称',width : 150},
							    {field : 'happenLocation',title : '发生地点',width : 150},
							    {field : 'departmentName',title : '发生部门',width : 150}, 
							    {field : 'accidentProcess',title : '发生过程',width : 150,hidden:true,
							    	formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  accidentProcessValue[index]=value;
						        		  }else{
						        			  accidentProcessValue[index]="无";
						        		  }
						  		  	  }	
							    }, 
								{field : 'reasonArticle',title : '致因物',width : 150}, 
								{field : 'directReason',title : '直接原因',width : 150,hidden:true,
							    	formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  directReasonValue[index]=value;
						        		  }else{
						        			  directReasonValue[index]="无";
						        		  }
						  		  	 }
								},
								{field : 'indirectReason',title : '间接原因',width : 150,hidden:true,
							    	formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  indirectReasonValue[index]=value;
						        		  }else{
						        			  indirectReasonValue[index]="无";
						        		  }
						  		  	 }	
						  		}, 
								{field : 'precautionMeasure',title : '预防措施',width : 100,hidden:true,
							    	formatter: function(value,row,index){
						        		  if(value!=null && value!=""){
						        			  precautionMeasureValue[index]=value;
						        		  }else{
						        			  precautionMeasureValue[index]="无";
						        		  }
						  		  	 }	
								},
								{field : 'directEconomicLoss',title : '直接经济损失',width : 100},
								{field : 'indirectEconomicLoss',title : '间接经济损失',width : 100},
								{field : 'deadCount',title : '死亡人数',width : 70},
								{field : 'seriousInjureCount',title : '重伤人数',width : 70},
								{field : 'fleshInjureCount',title : '轻伤人数',width : 70}, 
								{field : 'happenDateTime',title : '发生时间',width : 80}, 
								{field : 'accidentType',title : '事故类型',width : 80},
								{field : 'accidentLevel',title : '事故等级',width : 80},
								{field : 'editor',title : '编辑人',width : 70},
								{field : 'deptSn',hidden:true}
								] ],
								view: detailview, 
								detailFormatter: function(rowIndex, row){
									return '<table style="border:1"><tr>' + 								
									'<td style="width:100px;text-align:center">'+'事故编号：' + '</td>' + 
									'<td style="width:700px;text-align:center">' + 
									'<p>' + accidentSnValue[rowIndex] + '</p>' + 
									'</td>' +
									'</tr><tr>' +
									

									'<td style="width:100px;text-align:center">'+'发生过程：' + '</td>' + 
									'<td style="width:700px;text-align:center">' + 
									'<p>' + accidentProcessValue[rowIndex] + '</p>' + 
									'</td>' +
									'</tr><tr>' +
									
									'<td style="width:100px;text-align:center">'+'直接原因：' + '</td>' + 
									'<td style="width:700px;text-align:center" >' + 
									'<p>' + directReasonValue[rowIndex] + '</p>' + 
									'</td>' +
									'</tr><tr>' +
									

									'<td style="width:100px;text-align:center">'+'间接原因：' + '</td>' + 
									'<td style="width:700px;text-align:center">' + 
									'<p>' + indirectReasonValue[rowIndex] + '</p>' + 
									'</td>' +
									'</tr><tr>' +
									
									'<td style="width:100px;text-align:center">'+'预防措施：' + '</td>' + 
									'<td style="width:700px;text-align:center" colspan="3">' + 
									'<p>' + precautionMeasureValue[rowIndex] + '</p>' + 
									'</td>' +
									'</tr></table>'; 
								}
							
						});
		$('#addbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/accident/addAccident",
				success: function(message){
					$.messager.alert('提示',message); 
					$('#dg').datagrid('reload');
					$('#win').window('close');
					$('#form').form('reset');
				}
			});
	    });
		$('#editbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/accident/updateAccident",
				success: function(message){
					$.messager.alert('提示',message); 
					$('#dg').datagrid('reload');
					$('#win').window('close');
					$('#form').form('reset');
				}
			})  
	    });
		//事故类型编号下拉框加载
		$('#accidentType').combobox({
			url : '${pageContext.request.contextPath}/accident/typeAccident',
			valueField : 'accidentTypeSn',
			textField : 'accidentTypeName',
	        editable:false,
			prompt : '请选择事故类型'
		});

		//事故等级编号下拉框加载
		$('#accidentLevel').combobox({
			url : '${pageContext.request.contextPath}/accident/levelAccident',
			valueField : 'accidentLevelSn',
			textField : 'accidentLevelName',
	        editable:false,
			prompt : '请选择事故等级'
		});
		
		if(str.indexOf("150101")==-1){
			$("#addtool").css('display','none');
		}
		if(str.indexOf("150102")==-1){
			$("#edittool").css('display','none');
		}
		if(str.indexOf("150103")==-1){
			$("#deletetool").css('display','none');
		}

		$('#addtool').linkbutton({
			plain:false
		});
		$('#edittool').linkbutton({
			plain:false
		});

		$('#deletetool').linkbutton({
			plain:false
		}); 
	    $('#departmentSn').combotree({
		    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1501'
		});  
	})
</script>
</head>
<body>
	<table id="dg"></table>
	<!-- 事故填写窗口-->
	<div id="win" class="easyui-window" title="填写事故信息" closed="true" style="width: 950px; height: 400px; padding: 5px;">
		<br />
		<form id="form" method="post">
			<div style="margin-left: 30px;">
				<div style="diapaly: inline">
					 <input name="accidentSn" hidden/>
					 <label style="padding-right: 25px">事故名称：</label>
					 <input name="accidentName" style="height:20px" class="easyui-textbox"> 								 					 
					 <label style="padding-right: 25px">发生时间：</label> 
					 <input name="happenDateTime" style="height:20px" required="true" class="easyui-datetimebox" editable="false"/> 
					 <label style="padding-right: 25px">发生地点：</label> 
					 <input name="happenLocation" style="height:20px" class="easyui-textbox"/> 
					 <br/><br/>
					 <label style="padding-right: 25px">轻伤人数：</label>	
					 <input name="fleshInjureCount" style="height:20px" class="easyui-numberbox" precision="0"> 
					 <label style="padding-right: 25px">死亡人数：</label>
					 <input name="deadCount" style="height:20px" class="easyui-numberbox" precision="0"> 
					 <label style="padding-right: 25px">重伤人数：</label>
					 <input name="seriousInjureCount" style="height:20px" class="easyui-numberbox" precision="0"> 
					 <br/><br/>
					 <label>间接经济损失：</label>
					 <input name="indirectEconomicLoss" style="height:20px" class="easyui-numberbox" precision="2">
					 <label style="padding-right: 35px">致因物：</label>
					 <input name="reasonArticle" style="height:20px" class="easyui-textbox"> 
					 <label style="padding-right: 5px">直接经济损失：</label>
					 <input name="directEconomicLoss" style="height:20px" class="easyui-numberbox" precision="2"> 
					 <br/><br/>
					 <label style="padding-right: 25px">发生部门：</label> 
					 <input id="departmentSn" name="departmentSn" class="easyui-textbox" style="height:20px" data-options="panelHeight:'190px'"/>
					 <label style="padding-right: 25px">事故类型：</label>
					 <input id="accidentType" class="easyui-textbox" style="height:20px" data-options="panelHeight:'auto'" name="accidentTypeSn" />
					 <label style="padding-right: 25px">事故等级：</label>
					 <input id="accidentLevel" class="easyui-textbox" style="height:20px" data-options="panelHeight:'auto'" name="accidentLevelSn" />					 
					 <br/><br/>
				</div><br />
				<div id="tt" class="easyui-tabs" style="width:830px;height:150px;">   
				    <div title="发生过程 "  style="padding:20px;"> 
				      <input class="easyui-textbox" multiline="true" style="width:760px;height:80px" name="accidentProcess"/>
				    </div>   
				    <div title="直接原因"   style="overflow:auto;padding:20px;">
				      <input class="easyui-textbox" name="directReason" multiline="true" style="width:760px;height:80px" />
				    </div>   
				    <div title="间接原因" style="padding:20px;">   
				      <input class="easyui-textbox"  name="indirectReason" multiline="true" style="width:760px;height:80px"/>
				    </div>   
				    <div title="预防措施" style="padding:20px;">   
				      <input class="easyui-textbox"  name="precautionMeasure" multiline="true" style="width:760px;height:80px" />
				    </div>  
				</div> <br />
				<div id="dlg-buttons" style="text-align:center">
					<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok" >确认添加</a>
					<a id="editbtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>
				</div>
			</div>
		</form>
	</div>
</body>
</html>