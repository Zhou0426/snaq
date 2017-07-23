<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>安全日报-日报统计</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		var url="dailyreport/superviseDailyReportDetailsAction_search";
		var isFirst=true;
		$('#dg').datagrid({
			//url:'',
			rownumbers: true, 
			fit:true,
			fitColumns: true,
			singleSelect: true,
			pagination:false,
			toolbar:[{
				text:'部门类型：<input id="departmentType" class="easyui-combobox"/>'
			},{
				text:'开始日期：<input id="cc1"/>'
			},{
				text:'结束日期：<input id="cc2"/>'
			},{
				id:'export',
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('确认','您确认想要导出当前记录吗？',function(r){    
					    if (r){    
					    	//向后台发送请求
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/dailyreport/superviseDailyReportDetailsAction_export");
							//添加input		
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","data");
							input1.attr("value",JSON.stringify($('#dg').datagrid('getRows')));
							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","departmentTypeSn");
							input2.attr("value",$('#departmentType').combobox('getValue'));
							var input3=$("<input>");
							input3.attr("type","hidden");
							input3.attr("name","startDate");
							input3.attr("value",$('#cc1').combobox('getValue'));
							var input4=$("<input>");
							input4.attr("type","hidden");
							input4.attr("name","endDate");
							input4.attr("value",$('#cc2').combobox('getValue'));
							//将表单放入body
							$("body").append(form);
							form.append(input1);
							form.append(input2);
							form.append(input3);
							form.append(input4);
							form.submit();//提交表单  
					    }    
					}); 
					
				}
			}],
			columns:[[  
		          {field:'departmentName',title:'部门名称',width:'33.3%',align:'center'},  
		          {field:'submitDays',title:'上报天数',width:'33.3%',align:'center'},  
		          {field:'blankDays',title:'缺报天数',width:'33.3%',align:'center'},
		          {field:'departmentSn',title:'部门编号',hidden:'hidden'}
			]]
		});
		$('#departmentType').combobox({});
		$('#cc1').datebox({    
		    editable:false,
		    onChange: function(newValue,oldValue){
		    	if(!isFirst){
		    		var date=$('#cc2').datebox('getValue');
			        if(date!=null && date.length>0){
			        	var date1=new Date(newValue);
			        	var date2=new Date(date);
			        	if(date1>date2){
							$.messager.alert('我的提示','开始日期必须小于结束日期！','warning',function(){$('#cc1').datebox('clear');}); 					
						}else{
							$('#dg').datagrid('options').url=url;
			            	$('#dg').datagrid('load',{
			            		departmentTypeSn: $('#departmentType').combobox('getValue'),
			            		startDate:$('#cc1').datebox('getValue'),
			            		endDate:$('#cc2').datebox('getValue')
			            	});	 
						}
			        }
		    	}
		    }
		});
		$('#cc2').datebox({    
			editable:false,
			onChange: function(newValue,oldValue){
				if(!isFirst){
					var date=$('#cc1').datebox('getValue');
			        if(date!=null && date.length>0){
			        	var date1=new Date(date);
			        	var date2=new Date(newValue);
			        	if(date1>date2){
							$.messager.alert('我的提示','结束日期不得早于开始日期！','warning',function(){$('#cc2').datebox('clear');}); 					
						}else{
							$('#dg').datagrid('options').url=url;
			            	$('#dg').datagrid('load',{
			            		departmentTypeSn: $('#departmentType').combobox('getValue'),
			            		startDate:$('#cc1').datebox('getValue'),
			            		endDate:$('#cc2').datebox('getValue')
			            	});	 
						}
			        }
				}
		    }
		});
		var date=new Date();
		var thisMonth=date.getUTCMonth()+1;
		var thisYear=date.getUTCFullYear();
		var thisDay=date.getDate();
		var cc1=thisYear+"-"+thisMonth+"-"+thisDay;
		
		date.setMonth(date.getMonth()-1);
		
		var thisMonth=date.getUTCMonth()+1;
		var thisYear=date.getUTCFullYear();
		var thisDay=date.getDate();
		
		var cc2=thisYear+"-"+thisMonth+"-"+thisDay;
		$('#cc1').datebox('setValue',cc2);
		$('#cc2').datebox('setValue',cc1);
		$('#departmentType').combobox({
	    	url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			editable:false,
			prompt:'单位类型',
			onLoadSuccess: function(data) {
	            if(data.length>0){
	            	$('#departmentType').combobox('setValue', data[0].departmentTypeSn);
	            }
	        },
	        onChange:function(newValue,oldValue){
	            //在用户选择列表项的时候触发
	            var departmentTypeSn = newValue;
	            var startDate = $('#cc1').datebox('getValue');
	            var endDate = $('#cc2').datebox('getValue');
	            if(startDate.length!=0 && endDate.length!=0){
	            	$('#dg').datagrid('options').url=url;
	            	$('#dg').datagrid('load',{
	            		departmentTypeSn: departmentTypeSn,
	            		startDate:startDate,
	            		endDate:endDate
	            	});	            	
	            }
	            isFirst=false;
	        }
		});
		/* 
	    $('#month').combobox({
	    	valueField: 'month',
			textField: 'monthtext',
			editable:false,
			panelHeight:'auto',
			width:100,
			prompt:'选择月份',
	    	onChange:function(newValue,oldValue){
	            //在用户选择列表项的时候触发
	            var departmentTypeSn = $('#departmentType').combobox('getValue');
	            var year = $('#year').combobox('getValue');
	            var month = newValue;
	            if(departmentTypeSn.length>0 && year.length>0){
	            	$('#dg').datagrid('options').url=url;
	            	$('#dg').datagrid('load',{
	            		departmentTypeSn: departmentTypeSn,
	            		year:year,
	            		month:month
	            	});	            	
	        	}
	    	}
	    });
	    var data2=[];
		var thisMonth=new Date().getUTCMonth();//当前月
		for(var i=1;i<13;i++){
			data2.push({"month":i,"monthtext":i+"月份"});
		}
		$("#month").combobox("loadData", data2);//下拉框加载数据
	    
		$('#year').combobox({
			valueField:'year',    
			textField:'yeartext',  
			panelHeight:'auto',
			editable:false,
			width:100,
			prompt:'选择年份',
			onChange:function(newValue,oldValue){
	            //在用户选择列表项的时候触发
	            var departmentTypeSn = $('#departmentType').combobox('getValue');
	            var year = newValue;
	            var month = $('#month').combobox('getValue');
	            if(departmentTypeSn.length!=0 && month.length!=0){
	            	$('#dg').datagrid('options').url=url;
	            	$('#dg').datagrid('load',{
	            		departmentTypeSn: departmentTypeSn,
	            		year:year,
	            		month:month
	            	});	            	
	            }
	        }
		});
		var data = [];//创建年度数组
		var thisYear=new Date().getUTCFullYear();//今年
		for(var i=thisYear;i>2014;i--){
			data.push({"year":i,"yeartext":i+"年"});
		}
		$("#year").combobox("loadData", data);//下拉框加载数据
		$("#year").combobox("setValue",thisYear);//设置默认值为今年
		$("#month").combobox("setValue",thisMonth+1); */
		$('#export').linkbutton({    
		    plain: false   
		}); 
	})
	    
    </script>
</head>
<body style="margin:1px;">
	<table id="dg"></table>
</body>
</html>