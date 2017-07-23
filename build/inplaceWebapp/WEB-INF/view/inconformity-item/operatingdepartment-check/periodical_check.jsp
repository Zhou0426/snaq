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
<script type="text/javascript">
	function inconformityItem(index){
		$('#dg').datagrid('clearSelections');
		$('#win').window({
			width:900,
			height:400,
			title:'不符合项',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/operatingdepartment/check/periodical_check_inconformityItem"+ frameborder="0" width="100%" height="100%"/>'
		});
	}
	function checkTable(index){
		$('#dg').datagrid('clearSelections');
		$('#win').window({
			width:500,
			height:300,
			title:'检查表',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/checktable" frameborder="0" width="100%" height="100%"/>'
		});
	}
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var loginId="${sessionScope.personId}";
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/inconformity/item/periodicalAction_query.action',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			title:"定期检查",
			queryParams:{checkerFrom:'业务部门'},
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			onSelect:function(index,row){
				if(resources.indexOf('01040201')==-1){
					$('#add').css('display','none');
				}
				if(resources.indexOf('01040202')==-1){
					$('#delete').css('display','none');
				}else{
					if(row.editor.personId==loginId || roles.indexOf('jtxtgly')!=-1){
						$('#delete').css('display','');
					}else{
						$('#delete').css('display','none');
					}
				}
				if(resources.indexOf('01040203')==-1){
					$('#update').css('display','none');
				}else{
					if(row.editor.personId==loginId || roles.indexOf('jtxtgly')!=-1){
						$('#update').css('display','');
					}else{
						$('#update').css('display','none');
					}
				}
			},
			/*按钮*/
			toolbar:[{
				id:'add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					//判断是否有权限
					if(resources.indexOf('01040201')==-1){
						$('#add').css('display','none');
					}else{
						$('#win').window({
			 				width:420,
			 				height:500,
			 				title:'定期检查',
			 				cache:false,
			 				content:'<iframe src="${pageContext.request.contextPath}/operatingdepartment/check/periodical_check_add" frameborder="0" width="100%" height="100%"/>'
			 			});
					}
				}
			},{
				id:'delete',
				iconCls:'icon-remove',
				text:'删除',
				handler:function(){
					if(resources.indexOf('01040202')==-1){
						$('#delete').css('display','none');
					}else{
						//1、获取选中行
						var rows=$("#dg").datagrid("getSelections");
						//2、对事件进行判断，和删除确认
						if(rows.length==0){
							$.messager.show({
								title:'提示',
								msg:'至少选择为一条记录！',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}else{
							if(rows[0].editor.personId==loginId || roles.indexOf('jtxtgly')!=-1){
								$.messager.confirm('删除确认','您确定要删除选中的数据吗？',function(r){
									if(r){
										//3、获取要删除的id值 1,2,3===> String ids  delete from category where id in (1,2,3)
										var ids="";
										for(var i=0;i<rows.length;i++){
											ids += rows[i].id+",";
										}
										ids=ids.substring(0,ids.length-1);//截取字符串，除去最后一个逗号
										//alert(ids.substring(0,ids.length-1));//只是试验一下
										//4、发送请求提交删除信息
				                        $.post("${pageContext.request.contextPath}/inconformity/item/periodicalAction_deleteByIds.action",{ids:ids},function(result){
				                        	var data= eval('(' + result + ')');
											var status=data.status;
											var msg=data.message;
											if(status=="ok"){
												parent.$.messager.alert("提示信息",msg);
											}else{
												parent.$.messager.alert("提示信息",msg,'error');
											}
											//删除页面，刷新
											$("#d-i").datagrid("clearChecked");
											$("#d-i").datagrid("reload");
										},"text");
									}
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有删除此记录的权限！','warning');
							}
						}
					}
				}
				
			},{
				id:'update',
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					if(resources.indexOf('01040203')==-1){
						$('#update').css('display','none');
					}else{
						var rows=$("#dg").datagrid("getSelections");
						if(rows.length==1){
							//判断是否有权限
							if(rows[0].editor.personId==loginId || roles.indexOf('jtxtgly')!=-1){
								$('#win').window({
									width:420,
									height:500,
									title:"修改记录",
									cache:false,
									content:'<iframe src="${pageContext.request.contextPath}/operatingdepartment/check/periodical_check_update" frameborder="0" width="100%" height="100%"/>'
								});
							}else{
								$.messager.alert('我的消息','对不起！您没有修改此记录的权限！','warning');
							}
						}else{
							$.messager.show({
								title:'错误提示',
								msg:'一次只能更新一条记录！',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							});
						}
					}
				}
			}
			],
			/*列*/
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'periodicalCheckSn',title:'定期检查编号'},
				{field:'periodicalCheckName',title:'定期检查名称'},
				{field:'checkedDepartment.departmentName',title:'被检部门',formatter:function(value,row,index){
					if(row.checkedDepartment.isnull==false){
						return row.checkedDepartment.departmentName;
					}
				}},
				{field:'standardName',title:'依据评分标准',width:100,formatter:function(value,row,index){
					if(row.standard.isnull==false){
						return row.standard.standardName;
					}
				}},
				{field:'periodicalCheckType',title:'定期检查类型'},
				{field:'startDate',title:'开始日期'},			
				{field:'endDate',title:'结束日期'},
				{field:'editor',title:'编辑人员',formatter:function(value,row,index){
					if(row.editor.isnull==false){
						return row.editor.personName;
					}
				}},
				{field:'checkers',title:'检查人员',width:200,formatter:function(value,row,index){
					if(row.checkers.num>0){
						return row.checkers.personNames;
					}
				}},
				{field:'checkTable',title:'检查表',formatter:function(value,row,index){
					return '<a href="#" onclick="checkTable('+index+')" style="text-decoration:none">检查表['+value+']</a>'
				}},
				{field:'inconformityItem',title:'不符合项',formatter:function(value,row,index){
					return '<a href="#" onclick="inconformityItem('+index+')" style="text-decoration:none">不符合项['+value+']</a>'
				}}
				]]			
		});	
		//第一层权限
		if(resources.indexOf('01040201')==-1){
			$('#add').css('display','none');
		}
		if(resources.indexOf('01040202')==-1){
			$('#delete').css('display','none');
		}
		if(resources.indexOf('01040203')==-1){
			$('#update').css('display','none');
		}
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
	})
</script>
</head>
<body style="margin: 1px">
	<!--头 -->
	<table id="dg"></table>
	<input id="type" type="hidden" value="periodical" />
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
    <div id="child-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>