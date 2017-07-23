<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>处室考核</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-cellediting.js"></script>

		<script type="text/javascript">
		var nowYear=new Array();
		var sortValue="";
		var date=new Date;
		var departmentSn = "";
		var times="";
		
		for(var i=date.getFullYear() ; i > 2015; i--){
			nowYear.push({"id" : i, "text" : i + "年"});
		};
		function query(){
			var url='${pageContext.request.contextPath}/office/appraisals/office_loadOfficeData.action';
			$('#dg').datagrid('options').url=url;
			$('#dg').datagrid('reload',{
				'times':times
			});
		};
		$(function(){
// 			var a = 1;
// 			$("#department").combotree({
// 				url:'${pageContext.request.contextPath}/office/appraisals/office_loadDepartment.action',
// 				required:true,
// 				editable:false,
// 				panelHeight:200,
// 				width:200,
// 				onLoadSuccess:function(node,data){
// 					if(a==1 && data.length>0){
// 						departmentSn = $('#department').combotree('tree').tree('getRoot').id;
// 						$('#department').combotree('setValue',departmentSn);
// 						query();
// 						a++;
// 					}
// 				},
// 				onSelect:function(rec){
// 					departmentSn = rec.id;
// 					query();
// 				}
// 			});
			$('#selectTime').combobox({
				required:true,
				editable:false,
				width:130,
				panelWidth: 130,
				panelHeight:110,
				valueField: 'id',
				textField: 'text',
				onLoadSuccess:function(node,data){
					if(node.length>0){
						$('#selectTime').combobox('setValue',node[0].id);
						times=node[0].id;
						query();
						//$('#dg').datagrid('load',{'times':times});
					}
				},
				onSelect:function(record){
					times=record.id;
					query();
				}
			});
			//times=date.getFullYear();
			$('#dg').datagrid({
	            //url: '${pageContext.request.contextPath}/office/appraisals/office_loadOfficeData.action',
	            idField: 'departmentSn',
	            title:'处室考核',
	            toolbar:'#danwei',
	            //queryParams:{'times':times},
	            rownumbers: true,		//显示一个行号列
	            fitColumns:true,		//自动适应列
	           	//fit:true,				//表格宽高自适应
	            nowrap:false,
	            striped:true,			//斑马线效果
				singleSelect:true,		//单行选中
				remoteSort:false,		//不在服务器端进行排序
	            loadmsg:'请等待...',		//加载等待时显示,
//				showFooter: true,
// 	            pagination:true,		//显示分页组件
// 	            pageNumber:1,
// 	            pageSize:15,
// 	            pageList:[5,10,15,20,25,30],
	            onLoadSuccess:function(){
	            	$('[name="time"]').click(function(){
	            		var $this=$(this).attr('value');
	            		var reg=/^[0-9]*$/;
	            		if(!reg.test($this)){
	            			$('#da').attr('value',$this.substring($this.length-1,$this.length));
	            			$('#win').window({
					 			width:600,
					 			height:400,
					 			title:" ",
					 			cache:false,
					 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/office_quarter" frameborder="0" width="100%" height="100%"/>'
					 		});
	            		}else{
		            		$('#da').attr('value',$this);
		            		$('#win').window({
					 			width:600,
					 			height:400,
					 			title:" ",
					 			cache:false,
					 			content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/office_show" frameborder="0" width="100%" height="100%"/>'
					 		});
	            		}
	            	});
	            },
	            onSortColumn:function(sort, order){
	            	var data=sortData($('#dg').datagrid('getData').rows,sort,order);
	            	$('#dg').datagrid('loadData',data);
	            },
	      		columns:[[
					{field:'departmentSn',title:'部门编号',hidden:true},
					{field:'departmentName',title:'部门名称',width:100,align:'center'},
					{field:'1',title:'一月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="1" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'2',title:'二月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="2" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'3',title:'三月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="3" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'season1',title:'第一季度',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="season1" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'4',title:'四月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="4" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'5',title:'五月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="5" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'6',title:'六月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="6" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'season2',title:'第二季度',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="season2" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'7',title:'七月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="7" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'8',title:'八月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="8" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'9',title:'九月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="9" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'season3',title:'第三季度',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="season3" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'10',title:'十月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="10" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'11',title:'十一月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="11" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'12',title:'十二月',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="12" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'season4',title:'第四季度',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return '<a name="time" href="javascript:" value="season4" style="text-decoration:none">'+value+'</a>';
					}},
					{field:'year',title:'年度',width:100,align:'center',sortable:true,formatter:function(value,index,row){
						return value;
					}},
					{field:'rank',title:'排名',width:100,align:'center'}
				]],
				 onDblClickCell: function(){
					 $('#dg').datagrid("uncheckAll");
				 }
// 				 onBeforeLoad:function(){
// 					 if(times=="" || times.length==0){
// 						 return false;
// 					 }else{
// 						 return true;
// 					 }
// 				 }
			});
			$('#selectTime').combobox('loadData',nowYear);
			
			//权限管理
			var str="${sessionScope['permissions']}";
			//添加按钮点击事件
			if(str.indexOf("180301")==-1){
				$("#export").css('display','None');
			}else{
				$('#export').bind('click', function(){
					var input=$("<input>");
						input.attr("type","hidden");
						input.attr("name","rowsData");
						input.attr("value",JSON.stringify($('#dg').datagrid('getRows')));
					var url ='${pageContext.request.contextPath}/hazard/download_downloadOfficeExcel.action?yearTime='+$('#selectTime').combobox('getValue');
					  $('<form method="post" action="' + url + '"></form>').appendTo('body').append(input).submit().remove();
				});
			}
			
			
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
		}); 
			
		</script>
</head>
<body>
	<div id='danwei'>
<!-- 		<span> -->
<!-- 			<lable for="department">&emsp;检查部门：</lable><input id="department"  name="department" /> -->
<!-- 		</span> -->
		<span>
			<lable for="selectTime">选择时间：</lable>
			<input id="selectTime" />
		</span>
		<span>
			<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导出考核数据</a>
		</span>
	</div>
	<table id="dg" ></table>
	<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="winDetail" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<input id="da" type="hidden" />
		
</body>
</html>