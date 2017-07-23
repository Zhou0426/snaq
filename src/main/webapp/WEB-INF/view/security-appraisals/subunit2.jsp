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
<style type="text/css">
	.myPanelHead {
    	height:22px
	}
</style>
<script type="text/javascript">
	var sortValue="排名";
	//排序
	function sortData(data,sort,order){
		sortValue=sort;
		if(order=="asc"){
			data.sort(compare);
			addRank(data);
		}else{
			data.sort(compares);
			addRanks(data);
		}
		//data.reverse();
		return data;
	};
	//排序方法重写--正序排列
	function compare(value1, value2) {
	   if (parseFloat(value1[sortValue]) < parseFloat(value2[sortValue])) {
	       return 1;
	   } else if (parseFloat(value1[sortValue]) > parseFloat(value2[sortValue])) {
	       return -1;
	   } else {
	       return 0;
	   }
	};
	//排序方法重写--倒序排列
	function compares(value1, value2) {
	   if (parseFloat(value1[sortValue]) > parseFloat(value2[sortValue])) {
	       return 1;
	   } else if (parseFloat(value1[sortValue]) < parseFloat(value2[sortValue])) {
	       return -1;
	   } else {
	       return 0;
	   }
	};
	//加一列rank属性--正序排列
	function addRank(data){
		data[0]["rank"]=1;
		for( var i = 1; i < data.length; i++){
			if(parseFloat(data[i-1][sortValue])>parseFloat(data[i][sortValue])){
				data[i]["rank"]=parseFloat(data[i-1]["rank"])+1;
			}else{
				data[i]["rank"]=parseFloat(data[i-1]["rank"]);
			}
		}
	};
	//加一列rank属性--倒序排列
	function addRanks(data){
		data[data.length-1]["rank"]=1;
		for( var i = data.length - 2; i > -1; i--){
			if(parseFloat(data[i+1][sortValue]) > parseFloat(data[i][sortValue])){
				data[i]["rank"]=parseFloat(data[i+1]["rank"])+1;
			}else{
				data[i]["rank"]=parseFloat(data[i+1]["rank"]);
			}
		}
	};
	$(function(){
		$('#dg').datagrid({
			//url:'${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryScore',
			idField:'departmentSn',
			headerCls:'myPanelHead',
			title:'<a id="btn">友情提示：点击链接即可查看详情！</a> ',
			//queryParams:{'year':new Date().getUTCFullYear()},
			fit:true,
			stript:true,
			nowrap:true,
			loadMsg:'请等待',
			rownumbers:true,
			remoteSort:false,
			singleSelect:true,
			onLoadSuccess:function(){
				//month点击
				$('[name="month"]').click(function(e){
					$('#value').attr('value',$(e.target).attr('value'));
					$('#dg').datagrid('clearSelections');
					$('#win').window({
			 			width:600,
			 			height:400,
			 			title:' ',
			 			cache:false,
			 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunitmonth" frameborder="0" width="100%" height="100%"/>'
			 		});
				});
			},
			onSortColumn:function(sort, order){
				var data=sortData($('#dg').datagrid('getData').rows,sort,order);
            	$('#dg').datagrid('loadData',data);
			},			
			/*按钮*/
			toolbar:[{
				text:'<input id="cc1"/>'
			},{
				text:'<input id="cc2"/>'
			},{
				id:'export',
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('导出确认','您确定要导出当前数据吗？',function(r){
						if(r){
							var titles="部门名称,一月,二月,三月,第一季度平均得分,四月,五月,六月,第二季度平均得分,七月,八月,九月,第三季度平均得分,十月,十一月,十二月,第四季度平均得分,年度,"+sortValue;
							//向后台发送请求
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/security/appraisals/appraisalsAction_export");
							//添加input		
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","titles");
							input1.attr("value",titles);

							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","data");
							input2.attr("value",JSON.stringify($('#dg').datagrid('getRows')));

							var input3=$("<input>");
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
			},{
				id:'exportmonth',
				text:'导出月度考核',
				iconCls:'icon-excel',
				handler:function(){
					$('#month').css('display','');
					var month=$('#cc3').combobox('getValue');
					if(month!=null && month.length>0){
						$.messager.confirm('导出确认','您确定要导出吗？',function(r){
							if(r){
								var form=$("<form>");
								form.attr("style","display:none");
								form.attr("target","");
								form.attr("method","post");
								form.attr("action","${pageContext.request.contextPath}/security/appraisals/appraisalsAction_exportMonthByDepartmentTypeSn");
								//添加input		
								var input1=$("<input>");
								input1.attr("type","hidden");
								input1.attr("name","departmentTypeSn");
								input1.attr("value",$('#cc1').combobox('getValue'));

								var input2=$("<input>");
								input2.attr("type","hidden");
								input2.attr("name","departmentTypeName");
								input2.attr("value",$('#cc1').combobox('getText'));
								
								var input3=$("<input>");
								input3.attr("type","hidden");
								input3.attr("name","month");
								input3.attr("value",month);

								var input4=$("<input>");
								input4.attr("type","hidden");
								input4.attr("name","year");
								input4.attr("value",$('#cc2').combobox('getValue'));
								
								var input5=$("<input>");
								input5.attr("type","hidden");
								input5.attr("name","type");
								input5.attr("value",1);
								//将表单放入body
								$("body").append(form);
								form.append(input1);
								form.append(input2);
								form.append(input3);
								form.append(input4);
								form.append(input5);
								form.submit();//提交表单
							}
						})
						
					}else{
						$.messager.show({
							title:'提示',
							msg:'请先选择月份！',
							timeout:1000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
								bottom:''
							}
						});
					}
				}
			},{
				id:'exportmonth2',
				text:'导出区队月度考核',
				iconCls:'icon-excel',
				handler:function(){
					var row=$('#dg').datagrid('getSelected');
					if(row){
						$('#month').css('display','');
						var month=$('#cc3').combobox('getValue');						
						if(month!=null && month.length>0){
							var year=$('#cc2').combobox('getValue');
							var title="你确定要导出"+row.departmentName+" "+year+"年"+month+"月份的检查分数详情吗？";
							$.messager.confirm('导出确认',title,function(r){
								if(r){
									var form=$("<form>");
									form.attr("style","display:none");
									form.attr("target","");
									form.attr("method","post");
									form.attr("action","${pageContext.request.contextPath}/security/appraisals/appraisalsAction_exportMonthByDepartmentSn");
									//添加input		
									var input1=$("<input>");
									input1.attr("type","hidden");
									input1.attr("name","departmentSn");
									input1.attr("value",row.departmentSn);

									var input2=$("<input>");
									input2.attr("type","hidden");
									input2.attr("name","departmentName");
									input2.attr("value",row.departmentName);
									
									var input3=$("<input>");
									input3.attr("type","hidden");
									input3.attr("name","month");
									input3.attr("value",month);

									var input4=$("<input>");
									input4.attr("type","hidden");
									input4.attr("name","year");
									input4.attr("value",year);
									
									var input5=$("<input>");
									input5.attr("type","hidden");
									input5.attr("name","type");
									input5.attr("value",1);
									//将表单放入body
									$("body").append(form);
									form.append(input1);
									form.append(input2);
									form.append(input3);
									form.append(input4);
									form.append(input5);									
									form.submit();//提交表单
								}
							})
						}else{
							$.messager.show({
								title:'提示',
								msg:'请先选择月份！',
								timeout:1000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
					}else{
						$.messager.show({
							title:'提示',
							msg:'请先要选择导出的区队！',
							timeout:1000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop+200,
								bottom:''
							}
						});
					}
				}
				
			},{
				id:'month',
				text:'<input id="cc3"/>'
			}],
			frozenColumns:[[
				{field:'departmentName',title:'部门名称'},
			]],
			columns:[[
				{field:'departmentSn',title:'部门编号',hidden:true},
				{field:'1',title:'一月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="1" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'2',title:'二月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="2" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'3',title:'三月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="3" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'season1',title:'第一季度平均得分',sortable:true,align:'center',formatter:function(value,index,row){
					return (value).toFixed(2);
				}},
				{field:'4',title:'四月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="4" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'5',title:'五月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="5" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'6',title:'六月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="6" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'season2',title:'第二季度平均得分',sortable:true,align:'center',formatter:function(value,index,row){
					return (value).toFixed(2);
				}},
				{field:'7',title:'七月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="7" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'8',title:'八月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="8" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'9',title:'九月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="9" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'season3',title:'第三季度平均得分',sortable:true,align:'center',formatter:function(value,index,row){
					return (value).toFixed(2);
				}},
				{field:'10',title:'十月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="10" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'11',title:'十一月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="11" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'12',title:'十二月',sortable:true,formatter:function(value,index,row){
					return '<a href="#" name="month" value="12" style="text-decoration:none">'+(value).toFixed(2)+'</a>';
				}},
				{field:'season4',title:'第四季度平均得分',sortable:true,align:'center',formatter:function(value,index,row){
					return (value).toFixed(2);
				}},
				{field:'year',title:'年度',sortable:true,formatter:function(value,index,row){
					return (value).toFixed(2)
				}},
				{field:'rank',title:'排名'},
				]]
			
		});
		//提示
		$('#btn').linkbutton({    
    		iconCls: 'icon-tip'   
		});
		
		
		//单位
		$('#cc1').combobox({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_getUpNearestImplDepartment',    
			valueField:'departmentSn',    
			textField:'departmentName',
			panelHeight:'auto',
			editable:false,
			prompt:'所属单位',
			onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].departmentSn);
				}
			},
			onSelect:function(record){
				$('#month').css('display','none');
				$('#season').css('display','none');
				$('#cc3').combobox('clear');
				$('#cc4').combobox('clear');
				var url='${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryScore';
				$('#dg').datagrid('options').url=url;
				$('#dg').datagrid('reload',{
					'departmentTypeSn':record.departmentTypeSn,
					'year':$("#cc2").combobox('getValue'),
					'type':1
				});
			}						
		});
		//年份
		$("#cc2").combobox({   
			valueField:'year',    
			textField:'yeartext',  
			panelHeight:'auto',
			editable:false,
			prompt:'选择年份',
			width:100,
			onSelect:function(record){
				$('#month').css('display','none');
				$('#season').css('display','none');
				$('#cc3').combobox('clear');
				$('#cc4').combobox('clear');
				var url='${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryScore';
				$('#dg').datagrid('options').url=url;
				$('#dg').datagrid('reload',{
					'departmentTypeSn':$("#cc1").combobox('getValue'),
					'year':record.year,
					'type':1
				});					
			}
		});
		var data = [];//创建年度数组
		var thisYear=new Date().getUTCFullYear();//今年
		for(var i=thisYear;i>2014;i--){
			data.push({"year":i,"yeartext":i+"年"});
		}
		$("#cc2").combobox("loadData", data);//下拉框加载数据
		$("#cc2").combobox("setValue",thisYear);//设置默认值为今年	
		
		//月份排名
		$('#cc3').combobox({
			valueField: 'month',
			textField: 'monthtext',
			editable:false,
			panelHeight:'auto',
			width:100,
			prompt:'选择导出月份'
		});
		var data2=[];
		var thisMonth=new Date().getUTCMonth();//当前月
		for(var i=1;i<13;i++){
			data2.push({"month":i,"monthtext":i+"月份"});
		}
		$("#cc3").combobox("loadData", data2);//下拉框加载数据
		
		$('#month').css('display','none');
		$('#export').linkbutton({plain:false});	
		$('#exportmonth').linkbutton({plain:false});	
		$('#exportmonth2').linkbutton({plain:false});
	})
	
</script>
</head>
<body style="margin: 1px;">
	<input type="hidden" id="value"/>
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win2" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>