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
	var rows=parent.$('#dg').datagrid('getChecked');
	function details(id){
		$('#dg').treegrid('clearSelections');//清处多选的行
		parent.$('#child-win1').window({
			width:550,
			height:400,
			title:'符合度打分',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/system/audit/audit_score" frameborder="0" width="100%" height="100%"/>',
			onClose:function(){
				var type=parent.$("iframe").get(1).contentWindow.$("#type").val();
				$('#dg').treegrid('update',{
					id:id,
					row:{
						conformDegree:type,
					}
				});
			}
			
		});
	}
	function alldetails(id){
		$('#dg').treegrid('clearSelections');//清处多选的行
		parent.$('#child-win2').window({
			width:550,
			height:400,
			title:'符合度打分',
			cache:false,
			content:'<iframe src="${pageContext.request.contextPath}/system/audit/audit_allscore" frameborder="0" width="100%" height="100%"/>'
		});
	}
	function unsafecondition(){
		$('#dg').treegrid('clearSelections');//清处多选的行
			parent.$('#child-win').window({
				width:900,
				height:400,
				title:'相关隐患',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/system/audit/unsafecondition-detail" frameborder="0" width="100%" height="100%"/>'
			});
	}
	$(function(){
		var resources="${sessionScope['permissions']}";
		var roles="${sessionScope['roles']}";
		var rows=parent.$('#dg').datagrid('getChecked');
		var type=parent.$('#type').val();
		var editor="";
		var role="";
		if(type=="单位审核"){
			role="dwxtgly";
			editor=rows[0].editor.personId;
		}else{
			role="jtxtgly";
			editor=rows[0].editor.personId;
		}
		$('#dg').treegrid({    
		    url:'${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex',    
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来    
		    treeField:'indexName',//定义树节点字段
		    fitColumns:true,/*列宽自动*/
		    fit:true, 
			striped:true,/*斑马线效果  */
			nowrap:true,/*数据同一行*/
			loadmsg:'请等待',
			queryParams:{'standardSn':rows[0].standardSn,'auditSn':rows[0].auditSn},
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			//异步加载
  	 	    onBeforeExpand:function(row){  
		        //动态设置展开查询的url
		        $("#dg").treegrid('selectRow',row.id);
		        var url = "${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex?indexSn="+row.indexSn;
		        $("#dg").treegrid("options").url = url;
		        return true;      
		    },
		    rowStyler:function(row){
  				if (row.conformDegree<100){
  					return 'background-color:#FA5858;color:#000;';    // rowStyle是一个已经定义了的ClassName(类名)
  				}else{
  					return 'background-color:#FFFFFF;color:#000;';
      			}
  			}, 
  			checkbox: function(row){
  				if(row.integerScore!=null&&row.integerScore>=0){
  					return true;
  				}
  			},
  			onBeforeCheckNode:function(row,checked){
				if(editor!='${sessionScope.personId}' && roles.indexOf(role)==-1){
					$.messager.alert('我的消息','对不起！您没有勾选或取消勾选不打分标准的权限！','warning');
					return false;
				}
  	  		},
  			onCheckNode:function(row,checked){
  				var type=0;
  				if(checked==false){
  					type=1;
  				}
  				$.ajax({
		            type: "POST",
		            url: "${pageContext.request.contextPath}/system/audit/systemauditAction_addOrRemove.action",
		            data: {'auditSn':rows[0].auditSn,'indexSn':row.indexSn,'type':type,'standardSn':rows[0].standardSn},
		            dataType: "json",
		            success: function(data){			                	
		                   if(data.status=="nook"){
		                	   $.messager.alert('我的消息','保存失败！','error');
			               }             
		            }
		        });
  			},
		    columns:[[
		        {field:'id',title:'逻辑主键',hidden:true}, 
		        {field:'indexSn',title:'编号',hidden:true},
		        {field:'indexName',title:'考核标准（勾选不参与打分的项！）',width:80,formatter:function(value,row){
		        	return "<span title=" + value + ">" +row.indexSn+ value + "</span>";
		        }}, 
		        {field:'auditKeyPoints',title:'考核要点'},
		        {field:'integerScore',title:'设计分值'},
		        {field:'actualScore',title:'实际得分',formatter:function(value,row){
			        if(value!=null){
			        	return value.toFixed(2);
				    }
			    }},
		        {field:'conformDegree',title:'符合度（得分率）',align:'center',formatter:function(value,row){
					var str=row.indexSn;
					var s=str.substring(str.length-1);
					if(s=="P"||s=="C"||s=="D"||s=="A"){
						return '<a href="#" onClick="details('+row.id+')" style="text-decoration:none">'+value+'</a>';
					}else if(row.isParent==true){
						return '<a href="#" onClick="alldetails('+row.id+')" style="text-decoration:none">'+value+'</a>';
					}else if(value!=null){
						return value.toFixed(2);
					} 
			    }},
			    {field:'unsafecondition',title:'隐患',formatter:function(value,row,index){
					if(row.unsafecondition!=null){
						return '<a href="#" onclick="unsafecondition()" style="text-decoration:none">隐患['+value+']</a>';
					}
			    }}
		    ]]    
		}); 
	})
</script>
</head>
<body style="margin: 1px;">
	<table id="dg" ></table>
</body>
</html>