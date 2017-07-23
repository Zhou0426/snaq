<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>公告栏</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		
</head>
<body>
		<table id="dgEast"></table>
		<script type="text/javascript">
			$(function(){
					$('#dgEast').datagrid({
						url: '${pageContext.request.contextPath}/notice/noticeAction_showData',
						idField: 'id',
			            //rownumbers: true,	//显示一个行号列
			            fitColumns:true,	//自动适应列
			           	fit:true,			//表格宽高自适应
			            nowrap:false,
			            striped:true,		//斑马线效果
						singleSelect:true,	//单行选中
			            loadmsg:'请等待...',	//加载等待时显示
// 			            pagination:true,	//显示分页组件
// 			            pageNumber:1,
// 			            pageSize:10,
// 			            pageList:[5,10,15,20,25,30],
						columns:[[
							  {field:'id',hidden:true},
					          {field:'title',title:"公告",width:'100%',align:'center',
					        	  formatter: function(value, row, index) {
						 		      return "<a href='javascript:;' onclick=showNotice('" + index + "') style='text-decoration:none'>"+value+"</a>";
								  }
						 	  }
					     ]],
						 onDblClickCell: function(){
							$('#dgEast').datagrid("uncheckAll");
						 }
					});
			});
			//判断附件是否存在并下载
			function downLoadFile(index,event)
			{
				var rows = $('#dgEast').datagrid('getRows');
				$.post("${pageContext.request.contextPath}/interior/work/interiorWork_queryInteriorWorkFile.action",
						{attachmentPath:rows[index].attachment[event].physicalFileName},function(result)
				{
					if(result=="success")
					{
						var url ='${pageContext.request.contextPath}/standard/download.action?fileName='+rows[index].attachment[event].physicalFileName+'&&newName='+rows[index].attachment[event].logicalFileName;
						$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
					}else
					{
// 						$.messager.alert('警告','文件已丢失，下载失败！');
						swal("警告!", "文件已丢失，下载失败！", "error")
					}
				},"json");
			}
			//公告详情
			function showNotice(event){
				$('#dgEast').datagrid('selectRow', event);
				var rows = $('#dgEast').datagrid('getSelected');
// 				console.log(rows);
				var attachmentText = "";
				if( rows.attachment != "no" )
				{
					attachmentText = "附件：";
					for( var o in rows.attachment )
					{
						attachmentText = attachmentText + "<a href='javascript:' onclick=downLoadFile('" + event + "'," + o + ")"
															+" style='text-decoration:none'>" + rows.attachment[o].logicalFileName 
														+ "</a>，"
					}
					attachmentText = attachmentText.substring(0, attachmentText.length - 1);
				}
				var showText = "<div>发布人:"+ rows.author +"&emsp;&emsp;"
									+ "发布时间: " + rows.dateTime 
							 + "</div><br>" + rows.content 
							 + "<br><div>" + attachmentText + "</div>";
				swal({
					  title: "<h2>"+ rows.title +"</h2>",
					  text: showText,
					  html: true
				});
			};
	    </script>
</body>
</html>