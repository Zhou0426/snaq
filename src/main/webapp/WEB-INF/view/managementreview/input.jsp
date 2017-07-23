<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评审录入</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/login.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	

<script>
	var str="${sessionScope['permissions']}";
	$(function() {
		<!--数据网络-->
		$('#dg').datagrid({
			pageNumber : 1,
			pagination : true,
			url : "${pageContext.request.contextPath}/review/show",
			rownumbers : true,
			fitColumns : true,
			pageSize : 10,
			singleSelect : true,
			fit:true,
			nowrap:false,
			pageList : [ 5, 10, 15, 20 ],
	        loadMsg:'正在载入管理评审信息...',
	        loadFilter: function(data){
	    		return eval('(' + data + ')');
	    	},
			toolbar : [
					{
						iconCls : 'icon-export',
						text : '导出成word',
						id:'exporttool',
						handler : function() {
							if(str.indexOf("090101")==-1){
								$("#exporttool").css('display','none');
							}else{
								var rows=$("#dg").datagrid("getSelections");
								if (rows.length>0){
									var url ='${pageContext.request.contextPath}/review/export?id='+rows[0].id;
								  $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
								}
								else{
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
						}
					},{

						iconCls : 'icon-add',
						text : '增加',
						id:'addtool',
						handler : function() {
							$("#yearChoose").combobox("setValue",thisYear);//设置默认值为今年
							if(str.indexOf("090102")==-1){
								$("#addtool").css('display','none');
							}else{
								$('#addbtn').show();
								$('#editbtn').hide();
								$('#win').window('open');
								$('#form').form('disableValidation');
							}
						}
					},
					{
						iconCls : 'icon-edit',
						text : '修改',
						id:'edittool',
						handler : function() {	
							if(str.indexOf("090103")==-1){
								$("#edittool").css('display','none');
							}else{
			    			//数据回显
				    			var rows=$("#dg").datagrid("getSelections");
				    			var num=rows[0].personNum;
				    			if (rows.length>0){
									$('#editbtn').show();
									$('#addbtn').hide();	
					    			$('#win').window('open');  
					    			//对表单数据进行填充
					    			$('#reviewEmcee').combogrid('setValue',{personId:rows[0].reviewEmceeSn,personName:rows[0].reviewEmceeName});
	// 				    			$('#mutiselect').combogrid('setValue',rows[0].personIds);
	// 				    			$('#mutiselect').combogrid('setText',rows[0].persons);
					    			//$('#mutiselect').combogrid('setValue', {id:rows[0].personIds,personName:rows[0].persons});
					    			var pSn=rows[0].personIds;
					    			var pNa=rows[0].persons;
					    			var pIds=pSn.split(',');
					    			var pNas=pNa.split(',');
					    			var pvlist = new Array();
					    			for(var i=0;i<num;i++){				
					    				pvlist.push({ id: pIds[i], personName: pNas[i] });	
					    			};
					    			$('#mutiselect').combogrid('setValues', pvlist); 
					    			$('#form').form('load',{
					    				id:rows[0].id,
					    				departmentSn:rows[0].departmentSn,
					    				purpose:rows[0].purpose,
					    				reviewBasis:rows[0].reviewBasis,
					    				reviewContent:rows[0].reviewContent,
					    				reviewDate:rows[0].reviewDate,
					    				reviewLocation:rows[0].reviewLocation,
					    				reviewMethod:rows[0].reviewMethod,
					    				reviewRequirement:rows[0].reviewRequirement,
					    				reviewInput:rows[0].reviewInput,
					    				reviewResult:rows[0].reviewResult,
					    				correctPrevention:rows[0].correctPrevention,
					    				resultConclusion:rows[0].resultConclusion,
					    				reviewOutput:rows[0].reviewOutput,
					    				reviewResult:rows[0].reviewResult,
					    				scope:rows[0].scope,
					    				reviewSn:rows[0].reviewSn,
					    				reviewYear:rows[0].reviewYear
					    				
					    			});
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
						iconCls : 'icon-remove',
						text : '删除',
						id:'deletetool',
						handler : function() {
							if(str.indexOf("090104")==-1){
								$("#deletetool").css('display','none');
							}else{
								var row = $('#dg').datagrid('getSelected');
								if (row) {
									$.messager.confirm('选择是否删除','您确定要删除该评审吗?',function(fn) {
										if(fn==true){
											$.post('${pageContext.request.contextPath}/review/delete',{id:parseInt(row.id)},function(message){
												$.messager.alert('提示',message); 
												$('#dg').datagrid('reload');
											});
										}
									});
								} 
								else {
									$.messager.alert('提示','请先选择要删除的评审');
								}
							}
						}
				}],
		columns:[[ 
				  {field:'id',hidden:true},
                  {field:'reviewSn',hidden:true},
                  {field:'departmentName',title:'评审部门',width:1},
                  {field:'reviewYear',title:'评审年份',width:1},
                  {field:'reviewDate',title:'评审日期',width:1},
                  {field:'reviewLocation',title:'评审地点',width:1},
                  {field:'reviewEmceeName',title:'评审主持人',width:1},
                  {field:'personNum',title:'参与人数', width:1,
                	  formatter: function(value,row,index){
            			   	return '<a href="javascript:;" onClick="persondetails('+index+')" style="text-decoration:none">参与人数['+value+']</a>'; 
            			}
                  },
                  {field:'reviewDetail',title:'评审详情' ,width:1,
                	  formatter: function(value,row,index){
            			   	return '<a href="javascript:;" onClick="reviewDetail()" style="text-decoration:none">查看</a>'; 
            		  }
                  },
                  {field:'attachmentNum',title:'附件数' ,width:1,
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
      		 ]] ,
      	   onClickRow:function(index,row){
       			$('#dg').datagrid('selectRow',index);    	
          }
			
		});
		$('#addbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			var personIds=$('#mutiselect').combobox('getValues');
// 			if(personIds.substring(personIds.length-1).equals(',')){
// 					personIds=personIds.substring(0,personIds.lastIndexOf(",")-1);
// 			};
			$('#form').form('load', {
				id:22
			});
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/review/add?personIds="+personIds,
				success: function(data){
					$.messager.alert('提示',data.substring(1,data.length-1)); 
					$('#dg').datagrid('reload');
					$('#win').window('close');
					$('#form').form('reset');
				}
			});
	    });
// 		$('#uploadbtn').bind('click', function(){   
// 			$('#form0').form('submit', {
// 				url:"${pageContext.request.contextPath}/managementreview/updateManagementReview?personIds=0",
// 				success: function(message){
// 					$.messager.alert('提示',message); 
// 					$('#dg').datagrid('reload');
// 					$('#detailwin').window('close');
// 					$('#form0').form('reset');
// 				}
// 			});
// 	    });
		$('#editbtn').bind('click', function(){    
			$('#form').form('enableValidation');
			var personIds=$('#mutiselect').combobox('getValues');
			$('#form').form('submit', {
				url:"${pageContext.request.contextPath}/review/update?personIds="+personIds,
				success: function(data){
					$.messager.alert('提示',data.substring(1,data.length-1)); 
					$('#dg').datagrid('reload');
					$('#win').window('close');
					$('#form').form('reset');
				}
			})  
	    });
		//评审主持人
		$('#reviewEmcee').combogrid({		
			url : '${pageContext.request.contextPath}/certificate/personsCertificate?item=1',
		  	panelWidth:200,  
			prompt : '请选择评审主持人',    	     
		    idField:'personId',    
		    textField:'personName',  
	        delay:200,
	        mode:"remote",
		    columns:[[  
				{field:'id',hidden:true},  
		        {field:'personId',title:'人员编号',width:60},    
		        {field:'personName',title:'人员姓名',width:100} 
		    ]] 
		});
		//参与人
		$('#mutiselect').combogrid({    
    		panelWidth:340,       
   			idField:'id',  
		    width:830,
		    height:30,
    		textField:'personName',
    		editable:false,
    		prompt:'下拉进行检索',
    		multiple:true,
    		url : '${pageContext.request.contextPath}/certificate/personsCertificate?item=1',
    		toolbar:[{
   				text:'<input id="tb" style="width:300px">', 
   			}],
   			columns:[[
				{field:'id',title:'人员id'},
        		{field:'personId',title:'人员编号',width:120},    
       	 		{field:'personName',title:'人员姓名',width:120}     
    		]]    
		});

		//搜索人员
		$('#tb').textbox({    
    		buttonText:'搜索',    
    		iconCls:'icon-man', 
   		 	iconAlign:'left',
   		 	prompt:'输入编号或姓名搜索人员',
   		 	onChange:function(newValue, oldValue){
   		 		if(newValue!=""){
   		 			$('#mutiselect').combogrid('grid').datagrid('load',{'q':newValue});
   		 		}
   		 	}
		});		
		$("#yearChoose").combobox({   
			valueField:'year',    
			textField:'year',  
			panelHeight:'auto',
			editable:false
		});		
		var data = [];//创建年度数组
		var startYear;//起始年份
		var thisYear=new Date().getUTCFullYear();//今年
		var endYear=thisYear+1;//结束年份
		//数组添加值（2012-2016）//根据情况自己修改
		startYear=2014;
		for(startYear;startYear<endYear;startYear++){
			data.push({"year":startYear});			    
		}
		$("#yearChoose").combobox("loadData", data);//下拉框加载数据
		$("#yearChoose").combobox("setValue",thisYear);//设置默认值为今年
		
		
		//第一层权限


		if(str.indexOf("090101")==-1){
			$("#exporttool").css('display','none');
		}
		if(str.indexOf("090102")==-1){
			$("#addtool").css('display','none');
		}
		if(str.indexOf("090103")==-1){
			$("#edittool").css('display','none');
		}
		if(str.indexOf("090104")==-1){
			$("#deletetool").css('display','none');
		}
		
		$('#addtool').linkbutton({
			plain:false
		});
		$('#edittool').linkbutton({
			plain:false
		});

		$('#deletetool').linkbutton({
			plain:false
		});
		$('#exporttool').linkbutton({
			plain:false
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
		$('#win').window({
			closed:true,
			onClose:function(){
				$('#tb').textbox('clear');
				$("#form").form("reset");
			}
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
		        	return "<a href='javascript:' onclick='downloadAttachment("+index+")'>" + value + "</a>";
					} 
		        },
		        {field:'logicalFileName',title:'附件逻辑路径',hidden:true},
		        {field:'reviewId',title:'附件逻辑路径',hidden:true},
		        {field:'id',title:'操作',formatter:function(value,row,index){
					return "<a href='#' onclick='de(" + index + ")'>" +"删除" + "</a>";} 
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
	

// 	function submit(){
// 		var row=$('#dg').datagrid("getSelections");
// 		var y=parseInt(row[0].id); 
// 		$('#attachmentform').form('submit', {
// 			url:"${pageContext.request.contextPath}/review/addAttachment?id="+y,
// 			success: function(data){
// 				$.messager.alert('提示',data.substring(1,data.length-1)); 
// 				$('#dg').datagrid('reload');
// 			}
// 		})  
// 	};	
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
// 	function attachmentdetails(index){
// 		$('#dg').datagrid('selectRow',index);
// 		var row=$('#dg').datagrid("getSelections");
// 		var value=row[0].id;
// 		value=parseInt(value);
// 		$.post("${pageContext.request.contextPath}/review/show",{id:value},function(data){
//     		var data= eval('(' + data + ')');
// 			$("#detailsshow").empty();
// 			//$("#form0").append("<input type='hidden' value='"+value+"' name='id'>");
// 			if(data.rows[0].a.length>0){
// 				var aa=data.rows[0].a.split("##");
// 				var bb=data.rows[0].b.split(",");
// 				var cc=data.rows[0].c.split(",");
// 				$('#dg').datagrid('selectRow',index);
// 				var row=$('#dg').datagrid("getSelections");
// 				var value=row[0].id;
// 				value=parseInt(value);
// 				for(var i=0;i<aa.length;i++){
// 					$("#detailsshow").append("<tr>");
// 					$("#detailsshow").append("<td><a title='点击附件名下载' style='color:#006666' href='${pageContext.request.contextPath}/interior/work/download_InteriorWorkFile.action?attachmentPath="+aa[i]+"&interiorName="+bb[i]+"'>"+bb[i]+"</a></td>");
// 					$("#detailsshow").append("<td><a href='#' style='text-decoration:none;color:#000000' onClick='de("+cc[i]+","+data.rows[0].id+")'>删除</a></td>");	
// 					$("#detailsshow").append("</tr>");
// 				}
// 			}
// 		},'json');
//     	$('#detailswin').window('open');
// 	};
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
		
	}
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
</head>
<body>
	<form id="fm" type="hidden"> 
		<input id="upload" type="hidden">
		<a id="sc" type="hidden"></a>
	</form>
	<table id="dg"></table>
	<!-- 评审填写窗口-->
	<div id="win" class="easyui-window" title="填写评审信息" closed="true" style="width: 900px; height: 410px; padding: 5px;">
		<br />
		<form id="form" method="post" enctype="multipart/form-data">
			<div style="margin-left: 30px;">
				<div style="diapaly: inline">				
					 <input name="id" hidden/>
					 <input hidden name="reviewSn" />
					 <label style="padding-right: 25px">评审年份：</label>
					 <input class="easyui-conbobox" id="yearChoose" style="width: 225px;height:20px" name="reviewYear" />
					 <label style="padding-left: 85px;padding-right: 25px">评审地点：</label>
					 <input class="easyui-textbox"  style="width: 225px;height:20px" name="reviewLocation"/><br/><br/>
					 <label style="padding-right: 25px">评审日期：</label>
					 <input class="easyui-datebox" required="true" style="width: 225px;height:20px" name="reviewDate" editable="false"/>
					 <label style="padding-left: 85px;padding-right: 15px">评审主持人：</label>
					 <input id="reviewEmcee"  required="true" style="width: 225px;height:20px" name="reviewEmceeSn"/><br/>
					 
					 <label style="padding-right: 14px">评审参与人：</label>
					 <input class="easyui-textbox" name="personIds" required="true" id="mutiselect"/><br/>
					 
<!-- 					 <label style="padding-right: 25px">上传附件：</label> -->
<!-- 					 <input id="fb" type="text" name="upload"> -->
					
				</div><br />
				<div id="tt" class="easyui-tabs" style="width:830px;height:150px;">   
				    <div title="评审目的  "  style="padding:20px;"> 
				      <input class="easyui-textbox" multiline="true" style="width:760px;height:90px" name="purpose"/>
				    </div>   
				    <div title="评审范围"   style="overflow:auto;padding:20px;">
				      <input class="easyui-textbox" name="scope" multiline="true" style="width:760px;height:90px" />
				    </div>   
				    <div title="评审依据" style="padding:20px;">   
				      <input class="easyui-textbox"  name="reviewBasis" multiline="true" style="width:760px;height:90px"/>
				    </div>   
				    <div title="评审内容" style="padding:20px;">   
				      <input class="easyui-textbox"  name="reviewContent" multiline="true" style="width:760px;height:90px" />
				    </div>   
				    <div title="评审要求" style="overflow:auto;padding:20px;"> 
				      <input class="easyui-textbox" name="reviewRequirement" multiline="true" style="width:760px;height:90px" />
				    </div>   
				    <div title="评审方法"  style="padding:20px;">   
				      <input class="easyui-textbox" name="reviewMethod" multiline="true" style="width:760px;height:90px" />
				    </div> 
				    <div title="评审输入"style="padding:20px;">   
				      <input class="easyui-textbox"  name="reviewInput" multiline="true" style="width:760px;height:90px"/>
				    </div>   
				    <div title="评审结果" style="overflow:auto;padding:20px;">
				      <input class="easyui-textbox" name="reviewResult" multiline="true" style="width:760px;height:90px"/>
				    </div>
				    <div title="预防措施" style="padding:20px;">  
				      <input class="easyui-textbox" name="correctPrevention" multiline="true" style="width:760px;height:90px"/> 
				    </div> 
				    <div title="结果总结" style="padding:20px;">
				      <input class="easyui-textbox" name="resultConclusion" multiline="true" style="width:760px;height:90px"/>
				    </div>   
				    <div title="评审输出" style="overflow:auto;padding:20px;">
				      <input class="easyui-textbox" name="reviewOutput" multiline="true" style="width:760px;height:90px"/>
				    </div>  
				</div> <br />
				<div id="dlg-buttons" style="text-align:center">
					<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok" >确认添加</a>
					<a id="editbtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>
				</div>
			</div>
		</form>
	</div>
	<!-- 附件管理窗口 -->
	<div id="detailswin" class="easyui-window"  title="参与人详情" closed="true" style="width: 330px; height: 250px; padding: 5px;">
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