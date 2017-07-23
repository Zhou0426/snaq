<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>神华宁煤安全风险预控管理信息系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>

//权限管理
var str="${sessionScope['permissions']}";
$(function () {
	var roleSn;
	<!--第一个数据网络-->
	$('#dg').datagrid({       
		pageNumber: 1,
		pagination:true,
        url: "${pageContext.request.contextPath}/role/roleMenu?deleteornot=0",
        rownumbers:true,
        fitColumns:true,
        pageSize:20,
        fit:true,
        nowrap:false,
        singleSelect:true,
        pageList: [5, 10,15,20],
        loadMsg:'正在载入您拥有的角色菜单...',
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
        toolbar:[{
				iconCls: 'icon-add',
				text:'增加角色',
				id:'addroletool',
				handler: function()
	  				{
						if(str.indexOf('140304')==-1){
							$('#addroletool').css('display','none');
						}else{
							$('#formcrud').form('reset');
					    	$('#addroleformbtn').show();
					    	$('#roleSnInput').textbox('readonly',false);
			    			$('#updateroleformbtn').hide();
			  				$('#wincrud').window('open');	
						}					
			  		}
		        },
  				{
  	  				iconCls: 'icon-edit',
  	  				text:'修改角色',
  	  				id:'editroletool',			
  	  				handler: function()
  		  				{  	
		  	  				if(str.indexOf('140305')==-1){
								$('#editroletool').css('display','none');
							}else{
	  			    			var rows=$("#dg").datagrid("getSelections");
	  			    			if (rows.length!=0){
	  			  					var roleSn=rows[0].roleSn;
	  			  	   		    	if(roleSn!="jtxtgly"&&roleSn!="dwxtgly"&&roleSn!="fgsxtgly"){
		  			    				if(rows[0].deleted==0){
			  	  		    				$('#roleType').combobox('setValue',{id:rows[0].roleTypeName,text:rows[0].roleTypeName}) ;
			  				    			$('#formcrud').form('load',{
			  				    				roleSn:rows[0].roleSn,
			  				    				roleName:rows[0].roleName,
			  				    				roleType:rows[0].roleTypeName
			  				    			});
			  		  		    			$('#updateroleformbtn').show();
			  		  		    			$('#addroleformbtn').hide();
			  		  		    			$('#roleSnDiv').hide();	
			  						    	$('#roleSnInput').textbox('readonly');
				  			  				$('#wincrud').window('open');
		  			    				}else{
											$.messager.alert('提示','该行已删除，不可继续操作');
										}
	  			  	   		    	}
	  			  	   		    	else{
		  			  	   		    	$.messager.alert('提示','不能编辑此类角色');
	  			  	   		    	}
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
  	  				iconCls: 'icon-cancel',
  	  				text:'删除角色',
  	  				id:'deleteroletool',			
  	  				handler: function()
  		  				{		
	  	  				if(str.indexOf('140306')==-1){
							$('#deleteroletool').css('display','none');
						}else{
  	  						var row = $('#dg').datagrid('getSelected');
  		    				if (row){
  			  					var roleSn=row.roleSn;
  			  	   		    	if(roleSn!="jtxtgly"&&roleSn!="dwxtgly"&&roleSn!="fgsxtgly"){
//    	  		    					if(row.personNum==0){
									if(row.deleted==0){
  		  		    					$.messager.confirm('选择是否删除','您确定要删除该角色吗?',function(r){   					
  		  			    					if(r){  			    						
  		  			    						$.post('${pageContext.request.contextPath}/role/delete',{roleSn:row.roleSn},function(data){ 			    							
  		    	  		    						$.messager.alert('提示',data.substring(1,data.length-1));
  		  			    	    					$('#dg').datagrid('reload'); 
  		  			    						},'text');
  		  			    					}
  		  			    				});
									}else{
										$.messager.alert('提示','该行已删除，不可继续操作');
									}
//   	  		    					}
//   	  		    					else{
//   	  		    						$.messager.alert('提示','请先移除角色里的人员');
//   	  		    					}
	  		    				}else{
	  			  	   		    	$.messager.alert('提示','不能删除此类角色');
	  		    				}
  		    				}
	    					else{
	    						$.messager.alert('提示','请先选择您要删除的角色所在的行！');
	  		  				}
  		  				}
  		  			}
  		  				
  	  			},{
  	  				text:'已删除角色：<input id="includedeleted" name="checkbutton">'
  	  			},
  				{
  	  				iconCls: 'icon-marking',
	  				text:'设置角色权限',
	  				id:'rolepower',			
	  				handler: function()
	  				{	  		
	  					if(str.indexOf('140303')==-1){
							$('#rolepower').css('display','none');
						}else{
		  			    	var row = $('#dg').datagrid('getSelected');
		  			    	if(row){
			  					var roleSn=row.roleSn;
			  	   		    	if(roleSn!="jtxtgly"){
			  	   		   			$("#winpower").panel({title:row.roleName});
			  						$('#winpower').window('open');
			  						//加载相应的权限
			  						$('#cc').tree({
			    						url: '${pageContext.request.contextPath}/role/resourcetree?roleSn='+roleSn
			  						});
			  	   		    	}else{
		   	    					$.messager.alert('提示','不能为集团系统管理员分配权限！');
			  	   		    	}
		  			    	}else{
		  			    		$.messager.alert('提示','请先选择一个角色进行授权！');
		  			    	}
						}
	  				}
  				},
  				{
  				iconCls: 'icon-import',
  				text:'批量设置角色',
  				id:'importtool',			
  				handler: function()
	  				{
						if(str.indexOf("140301")==-1){
							$("#importtool").css('display','none');
	  					}else{
		  					$('#winImport').window('open');
	  					}						
	  				}
  				}
  				
  				],	
        columns:[[
                  {field:'roleTypeName',hidden:true} ,
                  {field:'roleSn',title:'角色编号'},   
                  {field:'roleName',title:'角色名称',width:5} ,                 
                  {field:'personNum',title:'人员',width:5,
                	  formatter: function(value,row,index){
          					return '<a href="#" onClick="openwin('+index+')">'+value+'人</a>';          				
          			}
                  } 
              ]],
       onClickRow:function(index,row){
    		$('#dg').datagrid('selectRow',index);
 			var rows=$('#dg').datagrid("getSelections");
			roleSn=rows[0].roleSn;	    	
       }
   });
	//下拉框显示当前部门及其子部门下的所有人
	<!--弹出的数据网络-->
	$('#windg').datagrid({       
		pageNumber: 1,
		pagination:true,
        url: "${pageContext.request.contextPath}/role/personDetail",
        rownumbers:true,
        fitColumns:true,
        pageSize:10,
        singleSelect:true,
        height:372,
        pageList: [5, 10,15,20],
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
		toolbar:[{
   	    		iconCls: 'icon-remove',
   	    		text:'删除授权',
   	    		id:'deletepersontool',
   	    		handler:function()
   	    		{
   					if(str.indexOf("140302")==-1){
   						$("#deletepersontool").css('display','none');
   					}else{
	   	    			var row = $('#windg').datagrid('getSelected');
	   	    			var rowslength=$('#windg').datagrid('getData').total;
	   	    			if (row){
	   	    				if(rowslength==1 && row.roleSn=="jtxtgly"){
	   	    					$.messager.alert('提示','不能继续删除！');		   	    				
	   	    				}else{
	   	    					$.messager.confirm('提示','您确定要删除该人员吗?',function(r){
		   	    					if(r){
		   	    						$.get('${pageContext.request.contextPath}/role/roleDecPerson',{personId:row.personId,roleSn:row.roleSn},function(data){
		   	    							$.messager.alert('提示',data.substring(1,data.length-1));
		   	    							$('#dg').datagrid('reload');
			   	    						$('#windg').datagrid('reload');
		   	    						},'text');
	   	    						} 
		   	    				});
	   	    				}
	   	    			}
	   	    			else{  
	   	    				$.messager.alert('提示','请先选择您要删除的行！');

	   	    			}
   					}
   	    		}
   	    	}
   	    	],	
        columns:[[  
                  {field:'roleSn',title:'角色编号',hidden:true} ,
                  {field:'personId',title:'人员编号',width:3} ,                 
                  {field:'personName',title:'人员姓名',width:3},         
                  {field:'departmentName',title:'部门名称',width:5},
                  {field:'implDepartmentName',title:'贯标单位',width:5}
              ]]
   });
	//人下拉框
	$('#person').combogrid({    
	    panelWidth:170,    
	    prompt:'输入编号或姓名搜索',    	     
	    idField:'personId',    
	    textField:'personName',  
        url: "${pageContext.request.contextPath}/role/personss",
        delay:200,
        mode:"remote",
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
	    columns:[[    
	        {field:'personId',title:'人员编号',width:60},    
	        {field:'personName',title:'人员姓名',width:100} 
	    ]],
	    onClickRow:function(index,row){
	    	var g = $('#person').combogrid('grid');	// 获取数据表格对象
	    	g.datagrid('selectRow',index);
	    	var row = g.datagrid('getSelected');
	    	$.post('${pageContext.request.contextPath}/role/roleForPerson',{personId:row.personId,roleSn:roleSn},function(data){
	    		$.messager.show({
					title:'提示',
					msg:data.substring(1,data.length-1),
					showType:'show',
					timeout:500,
					style:{
						top:'50'
					}
				});
		    	$('#person').combogrid('setValue',"");
		    	$('#dg').datagrid('reload');
		    	$('#windg').datagrid('reload');
	    	},'text'); 
	    }
	});  

	$('#cc').tree({
	    url: '${pageContext.request.contextPath}/role/resourcetree?roleSn=',
	    checkbox:true,
	    method:'get',
	    lines: true,
	    cascadeCheck:false,
// 	    onLoadSuccess:function(node, data){
// 	    	$('#cc').tree('expandAll');
// 	    },
	    onCheck:function(node, checked){
	    	//$('#cc').tree('options').cascadeCheck=true;
	    	var row = $('#dg').datagrid('getSelected');
    		var roleSn=row.roleSn;
    		var resourceSn=node.id;
    		var resourceSns="";
	    	if(checked){
	    		//得到所有已选择的节点
	    		var resources=$('#cc').tree('getChecked');
	    		for(var i=0;i<resources.length;i++){
	    			resourceSns+=resources[i].id+",";
	    		}
	    		resourceSns=resourceSns.substring(0, resourceSns.lastIndexOf(","));
		    	$.get('${pageContext.request.contextPath}/role/resourceForRole',{resourceSns:resourceSns,roleSn:roleSn},function(){
		    	},'json');
	    	}
	    	else{
	    		resourceSns=node.id+",";
	    		//得到所有未选择的节点
	    		var resources=$('#cc').tree('getChecked','unchecked');
	    		for(var i=0;i<resources.length;i++){
	    			resourceSns+=resources[i].id+",";
	    		}
	    		resourceSns=resourceSns.substring(0, resourceSns.lastIndexOf(","));
		    	$.get('${pageContext.request.contextPath}/role/resourceDeForRole',{resourceSns:resourceSns,roleSn:roleSn},function(){
		    	},'json');
	    	}
	    }		
	}); 


	 $('#roleTypeCombo').combobox({    
		        url:'${pageContext.request.contextPath}/role/roleType',    
		        valueField:'roleTypeName',    
		        textField:'roleTypeName',
		        panelHeight:80,
		        editable:false,
		        onSelect: function(){
		        	var roleType=$('#roleTypeCombo').combobox('getValue');
		        	$('#form').form('load',{
		        		roleType:roleType
		        	});
				}
		    });
	//确认添加
	 $('#addroleformbtn').click(function(){	
		 $('#formcrud').form('submit',{
		 		url: "${pageContext.request.contextPath}/role/add",
		 		success:function(data){
		 			$.messager.alert('提示',data.substring(1,data.length-1));
					$('#dg').datagrid('reload'); 
					$('#wincrud').window('close');
		 		}
		 	});
	 });
	//确认修改
	 $('#updateroleformbtn').click(function(){
		 $('#formcrud').form('submit',{
		 		url: "${pageContext.request.contextPath}/role/update",
		 		success:function(data){
		 			$.messager.alert('提示',data.substring(1,data.length-1));
					$('#dg').datagrid('reload');
					$('#wincrud').window('close');
		 		}
		 	});
	 });
	//包含已删除的开关
	$('#includedeleted').switchbutton({ 
	      checked: false, 
	      width:60,
	      onText:'显示',
	      offText:'不显示',
	      onChange: function(checked){
	    	  if(checked==true){
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/role/roleMenu';
	    	  }else{
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/role/roleMenu?deleteornot=0';
	    	  }
			  $('#dg').datagrid('reload');
	      } 
	});
	$('#winpower').window({   
	    modal:true ,
	    maximizable:false,
	    maximized:true,
	    minimizable:false,
	    collapsible:false
	    
	 });
// 	$('#win').window({
// 	    maximizable:false,
// 	    minimizable:false,
// 	    collapsible:false,
// 	    closed:true,
// 	    title:'',
// 	    closable:true
// 	 });
	//第一层权限
	if(str.indexOf('140301')==-1){
		$('#importtool').css('display','none');
	};
	if(str.indexOf('140302')==-1){
		$('#deletepersontool').css('display','none');
	};
	if(str.indexOf('140303')==-1){
		$('#rolepower').css('display','none');
	};
	if(str.indexOf('140304')==-1){
		$('#addroletool').css('display','none');
	};
	if(str.indexOf('140305')==-1){
		$('#editroletool').css('display','none');
	};
	if(str.indexOf('140306')==-1){
		$('#deleteroletool').css('display','none');
	};	
	
	
	$('#addroletool').linkbutton({
		plain:false
	});
	$('#editroletool').linkbutton({
		plain:false
	});
	$('#deleteroletool').linkbutton({
		plain:false
	});
	$('#importtool').linkbutton({
		plain:false
	});
	$('#rolepower').linkbutton({
		plain:false
	});
	$('#deletepersontool').linkbutton({
		plain:false
	});
	$('#importrole').click(function(){
		var ex=$('#excel').filebox('getValue');
		if(ex!=""){
			 var d1=/\.[^\.]+$/.exec(ex);   
		     if(d1==".xlsx"){
			 //进度条显示
			  $('#p').progressbar('setValue',0);
			  $('#winPro').window('open');
			  var timer = setInterval(getSession,500);
				$('#form').form('submit',{
				url: "${pageContext.request.contextPath}/role/importRole",
				success: function(data){
						$('#p').progressbar('setValue','100');
						clearInterval(timer);
						$('#winPro').window('close');
						
						$('#winImport').window('close');
						$.messager.alert('提示',data.substring(1,data.length-1));
						$('#dg').datagrid('reload');			
					}
				});
			}
		     else{
		    	 $.messager.alert('提示','请选择.xlsx文件！');
		    	 $('#excel').filebox('setValue','');
		     }
		}else{
			$.messager.alert('提示','请选择文件！');
		}
	});
	//设置窗口和其中元素的初始化
	$('#win').window({
		 title:'', 
		 modal:true,
		 maximizable:false, 
		 minimizable:false, 
		 collapsible:false,
		 closable:true,
		 closed:true,
		 width:500,
		 height:450
	});
	$('#excel').textbox({
	});
	$('#downloadrole').linkbutton({
	});
	$('#addroleformbtn').linkbutton({
	});
	$('#updateroleformbtn').linkbutton({
	});
	$('#roleSnInput').textbox({
	});
	$('#roleNameInput').textbox({
	});
	$('#winImport').window({
		width:250,
		height:250,
		title:'批量分配角色',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		onOpen:function(){
			$('#excel').filebox({
			});
		}
	});
	$('#wincrud').window({
		width:300,
		height:270,
		title:'填写角色信息',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true
	});
});

//打开窗口
function openwin(index){
	$('#person').combogrid('setValue',"");
	$('#dg').datagrid('selectRow',index);
	$('#win').window('open');
	var rows=$('#dg').datagrid("getSelections");
	roleSn=rows[0].roleSn;
	//$('#win').window('options').title=rows[0].roleName;
	//session.setParameter('title',rows[0].roleName);
	// $("#win").attr({title:rows[0].roleName});
	$("#win").panel({title:rows[0].roleName});
	if(rows.length!=0){
// 		$.get('${pageContext.request.contextPath}/role/personDetail',{roleSn:roleSn},function(){
//     	}); 
		$('#windg').datagrid('load',{roleSn:roleSn});
		//$('#person').datagrid('reload');
	}
}

//ajax发送请求session--导入
function getSession(){
	$.ajax({
		url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
		success: function(data){
			$('#p').progressbar('setValue',data);
		}
	},"json");
};
</script>
</head>
<body>
	<input id="checkbutton" type="hidden">
	<!-- roleMenu-->
	<table id="dg"></table>
	<!-- 分配角色窗口 -->
	<div id="win" class="easyui-window" style="padding:5px;">
		<input id="person" name="person" />
		<table id="windg"></table>
	</div>
	<!-- 导入窗口 -->
	<div id="winImport" class="easyui-window" title="批量角色分配窗口" style="padding:5px;">
		<form id="form" method="post" enctype="multipart/form-data">		
			<br/><br/>
				<input id="excel" data-options="buttonText:'选择文件'" class="easyui-filebox" name="excel"/><br/><br/><br/><br/>
				<a id="downloadrole" class="easyui-linkbutton" href="${pageContext.request.contextPath}/role/downloadRole" iconCls="icon-export">下载分配模板</a>
				<br/><br/><a class="easyui-linkbutton" iconCls="icon-import" id="importrole">分配角色</a>
		</form>
	</div>
	<!-- 分配角色权限窗口 -->
	<div id="winpower" class="easyui-window" title="分配角色权限" closed="true">
		<ul id="cc"></ul>
	</div>
	<!-- 角色增删改窗口 -->
	<div id="wincrud" class="easyui-window" style="padding:5px;">
		<form id="formcrud" method="post">		
			<br/>				
				<label>角色编号：</label>
				<input id="roleSnInput" class="easyui-textbox" name="roleSn"/><br/><br/>
				<label>角色名称：</label>
				<input id="roleNameInput" class="easyui-textbox" name="roleName"/><br/><br/>
				<label>角色类型：</label>
				<input class="easyui-combobox" required="true" id="roleTypeCombo" name="roleType"/>
				<br/><br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<a iconCls="icon-add" id="addroleformbtn" class="easyui-linkbutton">确认添加</a>
				<br/><br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<a iconCls="icon-edit" id="updateroleformbtn" class="easyui-linkbutton">确认修改</a>
		</form>
	</div>
</body>
</html>