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
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		$('#st').datagrid({
			url:'standard/standardAction_query.action',//要改为一个action
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			fitColumns:true,/*列宽自动*/
			fit:true,
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:false,
			checkOnSelect:false,
			/*分页相关参数配置*/
			pagination:true,
			pageNumber:1,
			pageSize:5,
			pageList:[5,10,15,20],
			/*按钮*/
			toolbar:[{
				text:'单位类型：<input id="cc">'				 
			},{
				id:'add',
				iconCls:'icon-add',
				text:'添加',
				handler:function(){
					//判断是否有权限
					if(roles.indexOf('jtxtgly')==-1){
						$('#add,#delete,#update').css('display','none');
					}else{
						if($('#cc').combobox('getValue')==""){
							$.messager.show({
								title:'提示',
								msg:'请先选择单位类型和评分标准！',
								timeout:1000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop+200,
									bottom:''
								}
							})
						}else{
							$('#st-win').window({
				 				width:380,
				 				height:210,
				 				title:'添加',
				 				cache:false,
				 				content:'<iframe src="${pageContext.request.contextPath}/standard/standard_add" frameborder="0" width="100%" height="100%"/>'
				 			});
						}
					}
				}
			},{
				id:'delete',
				iconCls:'icon-remove',
				text:'删除',
				handler:function(){
					if(roles.indexOf('jtxtgly')==-1){
						$('#add,#delete,#update').css('display','none');
					}else{
						//1、获取选中行
						var rows=$("#st").datagrid("getSelections");
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
			                        $.post("standard/standardAction_hidden.action",{ids:ids},function(result){
			                        	var data= eval('(' + result + ')');
										var status=data.status;
										var msg=data.message;
										if(status=="ok"){
											$.messager.alert("提示信息",msg);
										}else{
											$.messager.alert("提示信息",msg,'error');
										}
										//删除页面，刷新
										$("#st").datagrid("clearChecked");
										$("#st").datagrid("reload");
									},"text");
								}
							});
						}
					}
				}	
			},{
				id:'update',
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					if(roles.indexOf('jtxtgly')==-1){
						$('#add,#delete,#update').css('display','none');
					}else{
						var rows=$("#st").datagrid("getSelections");
						if(rows.length==1){
							$('#st-win').window({
								width:380,
								height:210,
								title:"修改记录",
								cache:false,
								content:'<iframe src="${pageContext.request.contextPath}/standard/standard_update" frameborder="0" width="100%" height="100%"/>'
							});
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
			}],
			/*列*/
			columns:[[
				{field:'id',title:'逻辑主键',hidden:true},
				{field:'standardSn',title:'标准编号',width:100},
				{field:'standardName',title:'标准名称',width:200,formatter:function(value,row,index){
					return "<span title=" + value + ">" + value + "</span>";
				}},
				{field:'standardType',title:'标准类型'},
				{field:'deleted',title:'是否删除',hidden:true}				
				]]
		});
		//权限
		 	var str="${sessionScope['permissions']}";
			if(str.indexOf("070303")==-1){
			$('#update').css('display','none');
			}
			$('#cc').combobox({    
		    url:'${pageContext.request.contextPath}/department/departmentType_query.action',    
		    valueField:'departmentTypeSn',    
		    textField:'departmentTypeName',
		    panelHeight:'auto',
			editable:false,
			onLoadSuccess:function(){
				var data=$(this).combobox('getData');
				if(data.length>0){
					$(this).combobox('select',data[0].departmentTypeSn);
				}
			},
 	        onSelect: function(rec){
 	        	$('#st').datagrid('load',{
 	        		departmentTypeSn:rec.departmentTypeSn	        		
 				});
	        }
		}); 
		//第一层权限限制
		if(roles.indexOf('jtxtgly')==-1){
			$('#add,#delete,#update').css('display','none');
		}
		$('#add').linkbutton({plain:false});
		$('#delete').linkbutton({plain:false});
		$('#update').linkbutton({plain:false});
	})
</script>
</head>
<body style="margin: 1px;">
	<!--头 -->
	<table id="st"></table>
	<div id="st-win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>
</html>