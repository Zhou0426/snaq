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
</head>
<body>	
	<input id="treehidden" type="hidden" >	
	<input id="begintimehidden" type="hidden">
	<input id="endtimehidden" type="hidden">
	<table id="dg"></table>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script> 
	//全局变量 分页时默认值为空
	var departmentSn=null;
	var first = 1;
	var date=new Date();
	var begintime=date.getFullYear()+"-01-01";
	var endtime=date.getFullYear()+"-12-31";
	$(function () {
		<!--数据网络-->
		$('#dg').datagrid({
			pageNumber: 1,
			pagination:true,
	        url: "${pageContext.request.contextPath}/review/show",
	        rownumbers:true,
	        fitColumns:true,
	        pageSize:10,
	        singleSelect:true,
	        fit:true,
	        pageList: [5, 10,15,20],
	        loadMsg:'正在载入管理评审信息...',
	        loadFilter: function(data){
	    		return eval('(' + data + ')');
	    	},
	    	onBeforeLoad:function(){
	    		if(departmentSn==null){
	    			return false;
	    		}else{
	    			return true;
	    		}
	    	},
	        toolbar:[
	     			{
	     				text:'评审部门：<input id="treeselect" name="treehidden">'
	     			},
     				{
     					text:'开始时间：<input id="begintimeselect" name="begintimehidden">'
     				},
     				{
     					text:'结束时间：<input id="endtimeselect" name="endtimehidden">'     				
	     			},
	     			{
						iconCls : 'icon-export',
						text : '导出成word',
						id:'exporttool',
						handler : function() {
							var rows = $("#dg").datagrid("getSelections");
							if ( rows.length > 0 ){
								var url ='${pageContext.request.contextPath}/review/export?id='+rows[0].id;
							  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
							}else{
								$.messager.show({
									title:'消息提示',
									msg:'请先选择您要导出的管理评审！',
									showType:'null',
									style:{
										top:'50'
									}
								});
							}
						}
					}],
	        columns:[[ 
	                  {field:'id',hidden:true},
	                  {field:'departmentName',title:'评审部门',width:100},
	                  {field:'reviewSn',title:'评审编号',width:100},
	                  {field:'reviewYear',title:'评审年份',width:100},
	                  {field:'reviewDate',title:'评审日期',width:100},
	                  {field:'reviewLocation',title:'评审地点',width:100},
	                  {field:'reviewEmceeName',title:'评审主持人',width:100},
	                  {field:'personNum',title:'参与人数',width:100,
	                	  formatter: function(value,row,index){
	            			   	return '<a href="javascript:;" onClick="persondetails('+index+')" style="text-decoration:none">参与人数['+value+']</a>'; 
	            			}
	                  },
	                  {field:'reviewDetail',title:'评审详情' ,width:100,
	                	  formatter: function(value,row,index){
	            			   	return '<a href="javascript:;" onClick="reviewDetail()" style="text-decoration:none">查看</a>'; 
	            		  }
	                  },
					  {field:'attachmentNum',title:'评审详情' ,width:100,
	                	  formatter: function(value,row,index){
	            			   	return '<a href="javascript:;" onClick="attachmentdetails('+index+')" style="text-decoration:none">附件['+value+']</a>'; 
	            			}
	                  },
	                  {field:'personIds',hidden:true},
	                  {field:'persons',hidden:true},
	                  {field:'purpose',hidden:true},
	                  {field:'reviewInput',hidden:true},
	                  {field:'reviewOutput',hidden:true},
	                  {field:'correctPrevention',hidden:true},
	                  {field:'resultConclusion',hidden:true},
	                  {field:'scope',hidden:true},
	                  {field:'reviewResult',hidden:true}
	      		 ]] 
		});
		$('#exporttool').linkbutton({
			plain:false
		});
	    <!--加载当前角色datagrid里的部门表树形菜单-->
	    $('#treeselect').combobox({
		    url: '${pageContext.request.contextPath}/review/managementReview_showDepartment',
		    method:'post',
		    valueField:'id',
		    textField:'text',
			//required:true,
			panelWidth: 200,
			editable:false,
			panelHeight:300,
			width:200,
		    onLoadSuccess:function(node,data){
		    	if(first == 1 && node.length != 0){
					//var da=$("#treeselect").combobox('tree').tree("getRoot");	
			    	//$("#treeselect").combobox('select', node[0].id);
			    	$('#treeselect').combobox('setValue', node[0].id);
			    	departmentSn = node[0].id;
					query();
			    	first++;
		    	}
		    },
		    onSelect: function(rec){
				departmentSn = rec.id;
				query();
			}
		}); 
	    //各种查询条件 

	    //开始时间
	    $('#begintimeselect').datebox({    
	    	value:begintime,
			editable:false,
	    	onChange: function(){
	    		begintime=$('#begintimeselect').datetimebox('getValue');
	    		query();
	    	}
	    }); 
	    //结束时间
	    $('#endtimeselect').datebox({ 
	    	value:endtime,
			editable:false,
	    	onChange: function(){
	    		endtime=$('#endtimeselect').datetimebox('getValue');
	    		query();
	    	}
	    }); 

		$('#attachmentwin').window({
			title:'评审附件',
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closed:true,
			width:300,
			height:300
		});
		//附件dg
		$("#attachmentdg").datagrid({
			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来
			striped:true,/*斑马线效果  */
			nowrap:false,/*数据同一行*/
			fitColumns:true,/*列宽自动*/
			fit:true,
			loadmsg:'请等待',
			rownumbers:true,/* 行号*/
			remoteSort:false,/*禁止使用远程排序*/
			singleSelect:true,
			checkOnSelect:false,
	        loadFilter: function(data){
	    		return eval('(' + data + ')');
	    	},
			toolbar:[{
				text:"<form id='attachmentform' method='post' name='fm' enctype='multipart/form-data'><input id='fb' type='text' name='upload'/><a id='submit' name='sc' href='#'>上传</a></form>"				 
			}],
			columns:[[ 
		        {field:'physicalFileName',title:'附件名称',formatter:function(value,row,index){
		        	return "<a href='javascript:' onclick='downloadAttachment("+index+")' style='text-decoration:none'>" + value + "</a>";
					} 
		        },
		        {field:'logicalFileName',title:'附件逻辑路径',hidden:true},
		        {field:'reviewId',title:'附件逻辑路径',hidden:true},
		        {field:'id',title:'操作',formatter:function(value,row,index){
					return "<a href='javascript:;' onclick='de(" + index + ")' style='text-decoration:none'>" +"删除" + "</a>";} 
		        }
		   ]]  
		});
		$('#fb').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right',
		    multiple:true,
		    width:230,
		    height:30
		});
		$('#attachmentform').form({
		});
		$('#submit').linkbutton({
			onClick:function(){
				var row=$('#dg').datagrid("getSelections");
				var y=parseInt(row[0].id); 
				$('#attachmentform').form('submit', {
					url:"${pageContext.request.contextPath}/review/addAttachment?id="+y,
					success: function(data){
						$.messager.alert('提示',data.substring(1,data.length-1)); 
						$('#dg').datagrid('reload');
						$('#attachmentwin').window('close');
					}
				})  
			}
		});	
	});
	function de(x,y){
		$.post("${pageContext.request.contextPath}/managementreview/delete",{attid:x,id:y},function(text){
			$.messager.alert('提示',text); 
			$('#dg').datagrid('reload');
			});
		$('#detailswin').window('close');
		$('#dg').datagrid('reload');
	};
	//打开窗口
// 	function details(value){
// 		$.post("${pageContext.request.contextPath}/managementreview/showManagementReview",{id:value},function(str){
// 			$("#detailsshow").empty();
// 			$("#detailsshow").append("<p>评审单位:"+ str.rows[0].departmentName+"</p>");
// 			$("#detailsshow").append("<p>评审目的:"+ str.rows[0].purpose+"</p>");
// 			$("#detailswin").append("<p>评审依据:"+ str.rows[0].reviewBasis+"</p>");
// 			$("#detailswin").append("<p>评审内容:"+ str.rows[0].reviewContent+"</p>");
// 			$("#detailswin").append("<p>评审地点:"+ str.rows[0].reviewLocation+"</p>");
// 			$("#detailswin").append("<p>评审方法:"+ str.rows[0].reviewMethod+"</p>");
// 			$("#detailswin").append("<p>评审要求:"+ str.rows[0].reviewRequirement+"</p>");
// 			$("#detailswin").append("<p>评审范围:"+ str.rows[0].scope+"</p>");
// 			$("#detailswin").append("<p>评审输入:"+ str.rows[0].reviewInput+"</p>");
// 			$("#detailswin").append("<p>评审输出："+ str.rows[0].reviewOutput+"</p>");		
// 			$("#detailswin").append("<p>预防措施："+ str.rows[0].correctPrevention+"</p>");		
// 			$("#detailswin").append("<p>评审结果："+ str.rows[0].resultConclusion+"</p>");	
// 			$("#detailswin").append("<p>参与人："+ str.rows[0].persons+"</p>");	
// 			$("#detailswin").append("<p>点击下载附件：</p>");
// 		},'json');
//     	$('#detailswin').window('open');
// 	}
	//查询函数
	function query(){
		$('#dg').datagrid('load',{
			departmentSn: departmentSn,
			begintime:begintime,
			endtime:endtime
		});
	};	

	function persondetails(index){
		$('#dg').datagrid('selectRow',index);
		var row=$('#dg').datagrid("getSelections");
		var value=row[0].id;
		value=parseInt(value);
		$.post("${pageContext.request.contextPath}/review/show",{id:value},function(data){
			var data= eval('(' + data + ')');
			$("#detailsshow").empty();	
			var personArray = data.rows[0].persons.split(",");
			
			var appendData = "<tr>"; 
			
			for( var i in personArray ){
				if( i % 5 == 0 ){
					appendData += '</tr><tr><td>' + personArray[i] + '</td>';
				}else{
					appendData += '<td>' + personArray[i] + '</td>';
				}
			}
			
			$("#detailsshow").append(appendData);
		},'json');
    	$('#detailswin').window('open');
	};
	function attachmentdetails(index){
		$('#dg').datagrid('selectRow',index);
		var row=$('#dg').datagrid("getSelections");
		var value=row[0].id;
		value=parseInt(value);
		$.post("${pageContext.request.contextPath}/review/show",{id:value},function(data){
			$("#detailsshow").empty();
			var data= eval('(' + data + ')');
			if(data.rows[0].a.length>0){
				var aa=data.rows[0].a.split("##");
				var bb=data.rows[0].b.split(",");
				var cc=data.rows[0].c.split(",");
				$('#dg').datagrid('selectRow',index);
				var row=$('#dg').datagrid("getSelections");
				var value=row[0].id;
				value=parseInt(value);
				for(var i=0;i<aa.length;i++){
					$("#detailsshow").append("<tr>");
					$("#detailsshow").append("<td><a title='点击附件名下载' style='color:#006666' href='${pageContext.request.contextPath}/interior/work/download_InteriorWorkFile.action?attachmentPath="+aa[i]+"&interiorName="+bb[i]+"'>"+bb[i]+"</a></td>");
				//	$("#detailsshow").append("<td><a href='${pageContext.request.contextPath}/managementreview/delete?attid="+cc[i]+"&id="+data.rows[0].id+"'>删除</a></td>");
					$("#detailsshow").append("<td><a href='#' style='text-decoration:none;color:#000000' onClick='de("+cc[i]+","+data.rows[0].id+")'>删除</a></td>");	
					$("#detailsshow").append("</tr>");
				}
			}
		},'json');
    	$('#detailswin').window('open');
	};
	function attachmentdetails(index){
		$('#dg').datagrid('selectRow',index);
		var row=$('#dg').datagrid("getSelections");
		var value=row[0].id;
		value=parseInt(value);
		$('#attachmentdg').datagrid('options').url='${pageContext.request.contextPath}/review/attachment';
		$('#attachmentdg').datagrid('load',{id:value});
    	$('#attachmentwin').window('open');
	};
	function downloadAttachment(index){
		$('#attachmentdg').datagrid('selectRow',index);
		var row=$('#attachmentdg').datagrid("getSelections");
		var url ="${pageContext.request.contextPath}/interior/work/download_InteriorWorkFile.action?attachmentPath="+row[0].logicalFileName+"&interiorName="+row[0].physicalFileName+"'";
		$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
		
	};	
	function de(index){
		$('#attachmentdg').datagrid('selectRow',index);
		var row=$('#attachmentdg').datagrid("getSelections");
		var x=parseInt(row[0].id);
		var y=parseInt(row[0].reviewId);
		$.post("${pageContext.request.contextPath}/review/deleteAttachment",{attid:x,id:y},function(data){
				$.messager.alert('提示',data.substring(1,data.length-1)); 
				$('#dg').datagrid('reload');
				$('#attachmentdg').datagrid('options').url='${pageContext.request.contextPath}/review/attachment';
				$('#attachmentdg').datagrid('load',{id:y});
			});
	    	
	};
	//查看评审详情
	function reviewDetail(){
		$('#detailWin').window({
			title:"评审详情",
			width:870,
			height:400,
			content:'<iframe src="${pageContext.request.contextPath}/managementreview/detail" frameborder="0" width="100%" height="100%" />'
		});
	}
	</script>
	
	<!-- 详情窗口 -->
	<div id="detailswin" class="easyui-window"  title="详细信息" closed="true" style="width: 350px; height: 250px; padding: 5px;">
		<table id="detailsshow" style="border:1;text-align:center;font-size:15px" >
		</table>
	</div>
	<!-- 附件窗口 -->
	<div id="attachmentwin" class="easyui-window">
		<table id="attachmentdg"></table>
	</div>
	<!-- 详情窗口 -->
	<div id="detailWin" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
</body>

</html>