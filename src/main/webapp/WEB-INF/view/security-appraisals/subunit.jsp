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
	$(function(){
		var departmentSn="";
		$('#dg').datagrid({
			//url:'${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryScore',
			idField:'departmentSn',
			headerCls:'myPanelHead',
			title:'<a id="btn">友情提示：点击链接即可查看详情！</a> ',
			//queryParams:{'year':new Date().getUTCFullYear()},
			fit:true,
			fitColumns:true,
			stript:true,
			nowrap:true,
			loadMsg:'请等待',
			rownumbers:true,
			romoteSort:false,
			singleSelect:true,
			onLoadSuccess:function(){
				//总数
				$('[name="sum"]').click(function(e){
					$('#value').attr('value',$(e.target).attr('value'));
					$('#type').attr('value','sum');
					$('#dg').datagrid('clearSelections');
					$('#win').window({
			 			width:900,
			 			height:400,
			 			title:'隐患详情',
			 			cache:false,
			 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunit-unsafecondition" frameborder="0" width="100%" height="100%"/>'
			 		});
				});
				//超期
				$('[name="over"]').click(function(e){
					$('#value').attr('value',$(e.target).attr('value'));
					$('#type').attr('value','over');
					$('#dg').datagrid('clearSelections');
					$('#win').window({
			 			width:900,
			 			height:400,
			 			maximizable:true,
			 			title:' ',
			 			cache:false,
			 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunit-unsafecondition" frameborder="0" width="100%" height="100%"/>'
			 		});
				});
				//瞒报
				$('[name="cover"]').click(function(e){
					$('#value').attr('value',$(e.target).attr('value'));
					$('#type').attr('value','cover');
					$('#dg').datagrid('clearSelections');
					$('#win').window({
			 			width:900,
			 			height:400,
			 			maximizable:true,
			 			title:' ',
			 			cache:false,
			 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunit-unsafecondition" frameborder="0" width="100%" height="100%"/>'
			 		});
				});
				//不安全行为
				$('[name="act"]').click(function(e){
					$('#value').attr('value',$(e.target).attr('value'));
					$('#dg').datagrid('clearSelections');
					$('#win').window({
			 			width:900,
			 			height:400,
			 			maximizable:true,
			 			title:'不安全行为详情',
			 			cache:false,
			 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunit-unsafeAct" frameborder="0" width="100%" height="100%"/>'
			 		});
				});
			},	
			/*按钮*/
			toolbar:[{
				text:'<input id="cc4"/>'
			},{
				text:'<input id="cc1"/>'
			},{
				text:'<input id="cc2"/>'
			},{
				text:'<input id="cc3"/>'
			},{
				id:'export',
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.messager.confirm('导出确认','您确定要导出当前数据吗？',function(r){
						if(r){
							var title=$("#cc4").combobox('getText')+''+$("#cc1").combobox('getText')+''+$("#cc2").combobox('getText')+''+$("#cc3").combobox('getText')+"考核";
							//向后台发送请求
							var form=$("<form>");
							form.attr("style","display:none");
							form.attr("target","");
							form.attr("method","post");
							form.attr("action","${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_export");
							//添加input		
							var input1=$("<input>");
							input1.attr("type","hidden");
							input1.attr("name","title");
							input1.attr("value",title);

							var input2=$("<input>");
							input2.attr("type","hidden");
							input2.attr("name","text");
							input2.attr("value",JSON.stringify($('#dg').datagrid('getRows')));

							//将表单放入body
							$("body").append(form);
							form.append(input1);
							form.append(input2);
							form.submit();//提交表单
						}
					}); 
				}
			}],
			columns:[[
					{field:'departmentSn',title:'部门编号',align:'center', rowspan:3,hidden:true},
					{field:'departmentName',title:'部门名称',align:'center', rowspan:3},
					{title:'隐患',align:'center',colspan:12},
					{title:'不安全行为',align:'center',colspan:4},
				],[
				   {title:'观察项',align:'center',colspan:3},
				   {title:'一般不符合项',align:'center',colspan:3},
				   {title:'严重不符合项',align:'center',colspan:3},
				   {title:'合计',colspan:3},
				   {field:'2',title:'A类',align:'center',rowspan:2,formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="act" value="2" style="text-decoration:none;color:red;">'+value+'</a>';
						}else{
							return '<a href="#" name="act" value="2" style="text-decoration:none">'+value+'</a>';
						}
					}},
				   {field:'1',title:'B类',align:'center',rowspan:2,formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="act" value="1" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="act" value="1" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'0',title:'C类',align:'center',rowspan:2,formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="act" value="0" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="act" value="0" style="text-decoration:none">'+value+'</a>';
						}
					}},
				   {field:'3',title:'合计',align:'center',rowspan:2,formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="act" value="3" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="act" value="3" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				],[
				   {field:'0s1',title:'总数',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="sum" value="0" style="text-decoration:none;color:red">'+value+'</a>';
					   }else{
						   return '<a href="#" name="sum" value="0" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'0s2',title:'超期',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="over" value="0" style="text-decoration:none;color:red">'+value+'</a>';
					   }else{
						   return '<a href="#" name="over" value="0" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'0s3',title:'整改瞒报',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="cover" value="0" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="cover" value="0" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'1s1',title:'总数',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="sum" value="1" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="sum" value="1" style="text-decoration:none">'+value+'</a>';
						}
					  
					}},
				   {field:'1s2',title:'超期',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="over" value="1" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="over" value="1" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'1s3',title:'整改瞒报',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="cover" value="1" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="cover" value="1" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'2s1',title:'总数',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="sum" value="2" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="sum" value="2" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'2s2',title:'超期',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="over" value="2" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="over" value="2" style="text-decoration:none">'+value+'</a>';
						}   
					}},
				   {field:'2s3',title:'整改瞒报',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="cover" value="2" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="cover" value="2" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'3s1',title:'总数',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="sum" value="3" style="text-decoration:none;color:red;">'+value+'</a>';
						}else{
							return '<a href="#" name="sum" value="3" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'3s2',title:'超期',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="over" value="3" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="over" value="3" style="text-decoration:none">'+value+'</a>';
						}
					   
					}},
				   {field:'3s3',title:'整改瞒报',align:'center',formatter:function(value,index,row){
					   if(value!=null && value>0){
						   return '<a href="#" name="cover" value="3" style="text-decoration:none;color:red;">'+value+'</a>';
					   }else{
						   return '<a href="#" name="cover" value="3" style="text-decoration:none">'+value+'</a>';
						}
					   
					}}
				]]
			
		});
		//提示
		$('#btn').linkbutton({    
    		iconCls: 'icon-tip'   
		});
		//所有贯标单位
		$('#cc4').combobox({
			url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryDepartment',
			queryParams:{'departmentSn':"${sessionScope['departmentSn']}"},
			valueField:'departmentSn',    
			textField:'departmentName',
			panelHeight:'auto',
			width:120,
			editable:false,
			prompt:'单位名称',
			onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].departmentSn);
				}
			},
			onSelect:function(record){
				departmentSn=record.departmentSn;
				$.ajax({
					type:"POST",
					url:'${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_queryAllDepartmentType',
					data:{'departmentSn':record.departmentSn},
					dataType:'json',
					success:function(data){
						$('#cc1').combobox('loadData',data);
					}
				});
			}
		});
		//单位类型
		$('#cc1').combobox({
			//url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
			valueField:'departmentTypeSn',    
			textField:'departmentTypeName',
			panelHeight:'auto',
			width:120,
			editable:false,
			prompt:'单位类型',
			onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].departmentTypeSn);
				}
			},
			onSelect:function(record){
				var url='${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_query';
				$('#dg').datagrid('options').url=url;
				$('#dg').datagrid('reload',{
					'departmentTypeSn':record.departmentTypeSn,
					'departmentSn':departmentSn,
					'year':$("#cc2").combobox('getValue'),
					'month':$("#cc3").combobox('getValue')
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
				var url='${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_query';
				$('#dg').datagrid('options').url=url;
				$('#dg').datagrid('reload',{
					'departmentTypeSn':$("#cc1").combobox('getValue'),
					'year':record.year,
					'month':$('#cc3').combobox('getValue'),
					'departmentSn':departmentSn
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
			prompt:'选择月份',
			onSelect:function(record){
				var url='${pageContext.request.contextPath}/inconformity/item/inconformityItemAction_query';
				$('#dg').datagrid('options').url=url;
				$('#dg').datagrid('reload',{
					'departmentTypeSn':$("#cc1").combobox('getValue'),
					'year':$('#cc2').combobox('getValue'),
					'month':record.month,
					'departmentSn':departmentSn
				}); 
			}
		});
		var data2=[];
		var thisMonth=new Date().getUTCMonth();//当前月
		for(var i=1;i<13;i++){
			data2.push({"month":i,"monthtext":i+"月份"});
		}
		$("#cc3").combobox("loadData", data2);//下拉框加载数据
		$("#cc3").combobox("setValue", thisMonth+1);
		$('#export').linkbutton({plain:false});
	})
	
</script>
</head>
<body style="margin: 1px;">
	<input type="hidden" id="value"/>
	<input type="hidden" id="type"/>
	<input type="hidden" id="subunit" value="subunit"/>
	<table id="dg"></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>