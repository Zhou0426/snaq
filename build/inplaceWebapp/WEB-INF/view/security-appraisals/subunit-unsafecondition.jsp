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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<script type="text/javascript">
	function attachment(index){
		$('#dg').datagrid('clearSelections');//清处多选的行
			parent.$('#child-win').window({
				width:400,
				height:300,
				title:'隐患',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/unsafecondition_attachment" frameborder="0" width="100%" height="100%"/>'
			});
	}
	$(function(){
		var row=parent.$('#dg').datagrid('getSelected');
		var value=parent.$('#value').val();
		var type=parent.$('#type').val();
		var year=parent.$('#cc2').combobox('getValue');
		var month=parent.$('#cc3').combobox('getValue');
		$("#dg").datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryDetail',
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			queryParams:{'type':type,'value':value,'departmentSn':row.departmentSn,'year':year,'month':month},//请求远程数据发送额外的参数
			//fitColumns:false,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,15,20,25,30],
			toolbar:[{
				id:'export',
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('导出确认','您确定要导出当前数据吗？',function(r){
						if(r){
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_exportDetail");
							//添加input
												
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","type");
							input1.attr("value",type);
							
							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","value");
							input2.attr("value",value);
							
							var input3=$("<input>");
							input3.attr("type","hidden");
							input3.attr("name","departmentSn");
							input3.attr("value",row.departmentSn);
							
							var input4=$("<input>");
							input4.attr("type","hidden");
							input4.attr("name","year");
							input4.attr("value",year);
							
							var input5=$("<input>");
							input5.attr("type","hidden");
							input5.attr("name","month");
							input5.attr("value",month);
							//将表单放入body
							$("body").append(form);
							form.append(input1);
							form.append(input2);
							form.append(input3);
							form.append(input4);
							form.append(input5);
							form.submit();//提交表单
						}
					});
				}
			}],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkDateTime',title:'检查时间',width:'8%'},
				{field:'checkLocation',title:'检查地点',width:'7%'},			
				{field:'inconformityItemNature',title:'不符合项性质',width:'9%'},
				{field:'machine.manageObjectSn',title:'机',width:'7%',formatter:function(value,row,index){
					if(row.machine.isnull==false){
						return row.machine.manageObjectName;
					}
				}},
				{field:'checkedDepartment.departmentName',title:'被检部门',width:'7%',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'problemDescription',title:'问题描述',width:'30%'},
				{field:'deductPoints',title:'扣分',width:'5%'},
				{field:'inconformityLevel',title:'不符合项等级',width:'9%'},
				{field:'correctType',title:'整改类型',width:'7%'},
				{field:'correctDeadline',title:'整改期限',width:'8%'},				
				{field:'correctProposal',title:'整改建议',width:'30%'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'7%',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
				}},
				{field:'hasReviewed',title:'已复查',width:'7%',formatter:function(value,row,index){
					if(row.hasReviewed==false){
						return '否';
					}else if(row.hasReviewed==true){
						return '是';
					}
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'7%',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
				{field:'attachment',title:'相关附件',width:'7%',formatter:function(value,row,index){
					return "<a href='#' onclick='attachment("+index+")' style='text-decoration:none'>" + "附件"+"["+value+"]" + "</a>";
				}}
				]],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
					//指标
					var standardIndex="无";
					if(row.standardIndex.isnull==false){
						standardIndex=row.standardIndex.indexName;
	        		}
	        		//危险源
	        		var hazard="无"
					if(row.hazard.isnull==false){
						hazard=row.hazard.hazardDescription;
					}
					//扣分项
					var methodcount="无"
					if(row.auditMethod.num>0){
						var method=row.auditMethod.method.split(',');
						var count=row.auditMethod.count.split(',');
						methodcount="";
						for(var i=0;i<row.auditMethod.num;i++){
							methodcount+=(i+1)+"."+method[i]+"("+count[i]+")&nbsp;";
						}
					}
					//专业
					var speciality="无"
					if(row.speciality.isnull==false){
						speciality=row.speciality.specialityName;
	        		}
	        		//检查人员
	        		var persons="无";
					if(row.checkers.num>0){
						persons=row.checkers.personNames;
					}
					//整改负责人
					var person="无";
					if(row.correctPrincipal.isnull==false){
						person=row.correctPrincipal.personName;
	        		}
					//录入人
	        		var editor="无";
	        		if(row.editor.isnull==false){
	        			editor=row.editor.personName;
		        	}
				return '<table style="border:1"><tr>' + 								
				'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + standardIndex + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'危险源：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + hazard + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'扣分项：' + '</td>' + 
				'<td style="width:220px;">' + 
				'<p>' + methodcount + '</p>' + 
				'</td>' + 
				'<td style="width:50px;text-align:center">'+'专业：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + speciality + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'检查人：' + '</td>' + 
				'<td style="width:150px;">' + 
				'<p>' + persons + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'整改负责人：' + '</td>' + 
				'<td style="width:50px;">' + 
				'<p>' + person + '</p>' + 
				'</td>' + 
				'<td style="width:50px;text-align:center">'+'录入人：' + '</td>' + 
				'<td style="width:50px;">' + 
				'<p>' + editor + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'录入时间：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.editorDateTime + '</p>' + 
				'</td>' +
				'</tr></table>'; 
				} 
		});
		$('#export').linkbutton({
			plain:false
		})
	})
</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>