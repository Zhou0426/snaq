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
			parent.$('#child-win2').window({
				width:400,
				height:300,
				title:'隐患',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/unsafecondition_attachment" frameborder="0" width="100%" height="100%"/>'
			});
	}
	$(function(){
		var row=parent.$('#dg').datagrid('getChecked');
		var row2=parent.$("iframe").get(0).contentWindow.$("#dg").treegrid("getSelected");	
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/unsafeconditionAction_query',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			//title:"动态检查",
			queryParams:{'auditSn':row[0].auditSn,'indexSn':row2.indexSn},//请求远程数据发送额外的参数
			//fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'checkDateTime',title:'检查时间',width:'5%'},
				{field:'checkLocation',title:'检查地点',width:'5%'},			
				{field:'inconformityItemNature',title:'不符合项性质',width:'5%'},
				{field:'machine.manageObjectSn',title:'机',width:'5%',formatter:function(value,row,index){
					if(row.machine.isnull==false){
						return row.machine.manageObjectName;
					}
				}},
				{field:'checkedDepartment.departmentName',title:'被检部门',width:'5%',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'problemDescription',title:'问题描述',width:'20%'},
				{field:'deductPoints',title:'扣分',width:'3%'},
				{field:'inconformityLevel',title:'不符合项等级',width:'5%'},
				{field:'correctType',title:'整改类型',width:'5%'},
				{field:'correctDeadline',title:'整改期限',width:'5%'},				
				{field:'correctProposal',title:'整改建议',width:'20%'},
				{field:'hasCorrectConfirmed',title:'整改确认',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectConfirmed==false){
						return '否';
					}else if(row.hasCorrectConfirmed==true){
						return '是';
					}
				}},
				{field:'hasReviewed',title:'已复查',width:'5%',formatter:function(value,row,index){
					if(row.hasReviewed==false){
						return '否';
					}else if(row.hasReviewed==true){
						return '是';
					}
				}},
				{field:'hasCorrectFinished',title:'整改完成',width:'5%',formatter:function(value,row,index){
					if(row.hasCorrectFinished==false){
						return '否';
					}else if(row.hasCorrectFinished==true){
						return '是';
					}
				}},
				{field:'attachment',title:'相关附件',width:'5%',formatter:function(value,row,index){
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
				'<td style="width:100px">' + 
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
				'</tr></table>'; 
				} 							
		});	
	})

</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>