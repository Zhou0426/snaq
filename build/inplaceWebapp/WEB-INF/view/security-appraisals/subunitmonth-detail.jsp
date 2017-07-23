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
	$(function(){
		var date=parent.$("iframe").get(0).contentWindow.$("#cc").calendar("options").current; 
		var row=parent.$('#dg').datagrid('getSelected');
		var departmentSn=row.departmentSn;
		var datevalue=date.toISOString().slice(0,10);
		
		$.ajax({
			url:'${pageContext.request.contextPath}/security/appraisals/appraisalsAction_scoreByDay',
			data:{
				'date':datevalue,
				'departmentSn':departmentSn,
				'type':1
			},
			dataType:'json',
			success:function(data){
				var title="扣分详情  总分："+data.score+" 扣分："+data.descore.toFixed(2);
				parent.$('#child-win').window('setTitle',title);
			}
		})
		$('#dg').datagrid({
				url:"${pageContext.request.contextPath}/security/appraisals/appraisalsAction_scoreDetail",
				idField:'',
				queryParams:{'date':datevalue,'departmentSn':departmentSn,'type':1},
				fit:true,
				fitColumns:true,
				stript:true,
				nowrap:false,
				loadMsg:'请等待',
				rownumbers:true,
				romoteSort:false,
				singleSelect:true,
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
								form.attr("action","${pageContext.request.contextPath}/security/appraisals/appraisalsAction_exportUnsafeCondition");
								//添加input
													
								var input1=$("<input>");
								var input2=$("<input>");
								var input3=$("<input>");
								input1.attr("type","hidden");
								input1.attr("name","date");
								input1.attr("value",datevalue);
								
								input2.attr("type","hidden");
								input2.attr("name","departmentSn");
								input2.attr("value",departmentSn);
								
								input3.attr("type","hidden");
								input3.attr("name","type");
								input3.attr("value",1);
								//将表单放入body
								$("body").append(form);
								form.append(input1);
								form.append(input2);
								form.append(input3);
								form.submit();//提交表单
							}
						});
					}
				}],
				columns:[[
			        {field:'indexSn',title:'指标编号'},
			        {field:'indexName',title:'指标名称'},
			        {field:'deductPoints',title:'扣分'},    
			        {field:'percentageScore',title:'分数'},
			        {field:'isKeyIndex',title:'是否关键指标',formatter:function(value,row,index){
			        	if(row.isKeyIndex==true){
			        		return '是';
			        	}else if(row.isKeyIndex==false){
			        		return '否';
			        	}
			        }},
			        {field:'jointIndexIdCode',title:'联合指标'}
				]],
				view: detailview, 
				detailFormatter: function(rowIndex, row){
					//机
					var machine="无";
					if(row.machine.isnull==false){
						machine=row.machine.manageObjectName;
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
				'<td style="width:100px;text-align:center">'+'检查时间：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.checkDateTime + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'检查地点：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.checkLocation + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'不符合项性质：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.inconformityItemNature + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'机：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + machine + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'问题描述：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.problemDescription + '</p>' + 
				'</td>' +
				'<td style="width:100px;text-align:center">'+'不符合项等级：' + '</td>' + 
				'<td style="width:100px;">' + 
				'<p>' + row.inconformityLevel + '</p>' + 
				'</td>' +
				'<td style="width:50px;text-align:center">'+'录入人：' + '</td>' + 
				'<td style="width:50px;">' + 
				'<p>' + editor + '</p>' + 
				'</td>' +
				'</tr><tr>'+
				'<td style="text-align:center">'+'危险源：' + '</td>' + 
				'<td colspan="2">' + 
				'<p>' + hazard + '</p>' + 
				'</td>' +
				'<td style="text-align:center">'+'扣分项：' + '</td>' + 
				'<td colspan="2">' + 
				'<p>' + methodcount + '</p>' + 
				'</td>' + 
				'<td style="text-align:center">'+'专业：' + '</td>' + 
				'<td colspan="2">' + 
				'<p>' + speciality + '</p>' + 
				'</td>' +
				'<td style="text-align:center">'+'检查人：' + '</td>' + 
				'<td colspan="2">' + 
				'<p>' + persons + '</p>' + 
				'</td>' +
				'<td style="text-align:center">'+'整改负责人：' + '</td>' + 
				'<td>' + 
				'<p>' + person + '</p>' + 
				'</td>' + 
				'</tr></table>'; 
				} 
			});
		$('#export').linkbutton({plain:false});
	})

</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>