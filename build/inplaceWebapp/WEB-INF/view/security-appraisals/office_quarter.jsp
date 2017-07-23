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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<script type="text/javascript">
	$(function(){
		var rows=parent.$('#dg').datagrid('getSelections');
		var value=parent.$('#da').val();
		var yearTime=parent.$('#selectTime').combobox('getValue');
		var titleValue;
		var month=0;
		var totalValue=0;
		if(value=="1"){
			titleValue="第一季度";
			for(var i=1;i<4;i++){
				month+=rows[0][i]*1;
			}
			totalValue=rows[0].season1*1;
		}else if(value=="2"){
			titleValue="第二季度";
			for(var i=4;i<7;i++){
				month+=rows[0][i]*1;
			}
			totalValue=rows[0].season2*1;
		}else if(value=="3"){
			titleValue="第三季度";
			for(var i=7;i<10;i++){
				month+=rows[0][i]*1;
			}
			totalValue=rows[0].season3*1;
		}else{
			titleValue="第四季度";
			for(var i=10;i<13;i++){
				month+=rows[0][i]*1;
			}
			totalValue=rows[0].season4*1;
		}
		month=(month/3).toFixed(2);
		var standardIndexScore=((totalValue-month*0.6)/0.4).toFixed(2);
		parent.$('#win').window('setTitle',rows[0].departmentName+" - "+titleValue+"得分详情");
		

		$('#dg').datagrid({
			url:"${pageContext.request.contextPath}/security/appraisals/appraisalsAction_scoreDetailOffice",
			idField:'',
			queryParams:{'year':yearTime,'quarter':value,'departmentSn':rows[0].departmentSn},
			fit:true,
			fitColumns:true,
			stript:true,
			nowrap:false,
			loadMsg:'请等待',
			rownumbers:true,
			romoteSort:false,
			singleSelect:true,
		    toolbar:[{
		    text:'<table>'+
	  		    '<tr><td>'+'总分：'+totalValue+' = '+
	  		    '</td><td>'+'月平均得分：'+
	  		    '</td><td>'+month+
	  		  	'*60%</td><td>&nbsp;'+titleValue+'指标得分: '+
	  			'</td><td>'+standardIndexScore+
	  		    '*40%</td></tr>'+
	  		    '</table>'
			}],
			columns:[[
		        {field:'indexSn',title:'指标编号',width:100},
		        {field:'indexName',title:'指标名称',width:100},
		        {field:'deductPoints',title:'扣分',width:30},    
		        {field:'percentageScore',title:'分数',width:30},
		        {field:'isKeyIndex',title:'是否关键指标',width:50,formatter:function(value,row,index){
		        	if(row.isKeyIndex==true){
		        		return '是';
		        	}else if(row.isKeyIndex==false){
		        		return '否';
		        	}
		        }}    
			]],
			view: detailview, 
			detailFormatter: function(rowIndex, row){
				//机
				var machine="无";
				if(row.machine.isnull==false){
					machine=row.machine.manageObjectName;
				}
        		//危险源
        		var hazard="无"
				if(row.hazard.isnull==false){
					hazard=row.hazard.hazardDescription;
				}
				//扣分项
				var methodcount="无"
				if(row.auditMethod.num>0){
					var method=row.auditMethod.method.split(',');
					var count=row.auditMethod.count.split(',');
					methodcount="";
					for(var i=0;i<row.auditMethod.num;i++){
						methodcount+=(i+1)+"."+method[i]+"("+count[i]+")&nbsp;";
					}
				}
				//专业
				var speciality="无"
				if(row.speciality.isnull==false){
					speciality=row.speciality.specialityName;
        		}
        		//检查人员
        		var persons="无";
				if(row.checkers.num>0){
					persons=row.checkers.personNames;
				}
				//整改负责人
				var person="无";
				if(row.correctPrincipal.isnull==false){
					person=row.correctPrincipal.personName;
        		}
				//录入人
        		var editor="无";
        		if(row.editor.isnull==false){
        			editor=row.editor.personName;
	        	}
			return '<table style="border:1"><tr>' + 
			'<td style="width:90px;text-align:center">'+'检查时间：' + '</td>' + 
			'<td style="width:60px;">' + 
			'<p>' + row.checkDateTime + '</p>' + 
			'</td>' +
			'<td style="width:90px;text-align:center">'+'检查地点：' + '</td>' + 
			'<td style="width:75px;">' + 
			'<p>' + row.checkLocation + '</p>' + 
			'</td>' +
			'<td style="width:60px;text-align:center">'+'问题描述：' + '</td>' + 
			'<td style="width:120px;">' + 
			'<p>' + row.problemDescription + '</p>' + 
			'</td></tr><tr>' +
			'<td style="width:90px;text-align:center">'+'不符合项性质：' + '</td>' + 
			'<td style="width:60px;">' + 
			'<p>' + row.inconformityItemNature + '</p>' + 
			'</td>' +
			'<td style="width:90px;text-align:center">'+'不符合项等级：' + '</td>' + 
			'<td style="width:75px;">' + 
			'<p>' + row.inconformityLevel + '</p>' + 
			'</td>' +
			'<td style="width:60px;text-align:center">'+'专业：' + '</td>' + 
			'<td style="width:120px;">' + 
			'<p>' + speciality + '</p>' + 
			'</td></tr><tr>' +
			'<td style="width:90px;text-align:center">'+'机：' + '</td>' + 
			'<td style="width:60px;">' + 
			'<p>' + machine + '</p>' + 
			'</td>' +
			'<td style="width:90px;text-align:center">'+'扣分项：' + '</td>' + 
			'<td style="width:75px;">' + 
			'<p>' + methodcount + '</p>' + 
			'</td>' + 
			'<td style="width:60px;text-align:center">'+'危险源：' + '</td>' + 
			'<td style="width:120px;">' + 
			'<p>' + hazard + '</p>' + 
			'</td></tr><tr>' +
			'<td style="width:90px;text-align:center">'+'整改负责人：' + '</td>' + 
			'<td style="width:60px;">' + 
			'<p>' + person + '</p>' + 
			'</td>' + 
			'<td style="width:90px;text-align:center">'+'录入人：' + '</td>' + 
			'<td style="width:75px;">' + 
			'<p>' + editor + '</p>' + 
			'</td>' +
			'<td style="width:60px;text-align:center">'+'检查人：' + '</td>' + 
			'<td style="width:120px;">' + 
			'<p>' + persons + '</p>' + 
			'</td>' +
			'</tr></table>'; 
			} 
		});
// 		$.ajax({
//             type: "POST",
//             url: "${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryQuarterDetail",
//             data: {'year':yearTime,'quarter':quarter,'departmentSn':rows[0].departmentSn},
//             dataType: "json",
//             success: function(data){
//                   var auditSn=data.auditSn;
//                   var standardSn=data.standardSn;
//                   var score=(data.score*1).toFixed(2);
//                   month=((month-score*0.4)/0.6).toFixed(2);
//                   $('#dg').treegrid({    
//           		    url:'${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex',    
//           			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来    
//           		    treeField:'indexName',//定义树节点字段
//           		    fitColumns:true,/*列宽自动*/
//           		    fit:true,
//           			nowrap:true,/*数据同一行*/
//           			loadmsg:'请等待',
//           			queryParams:{'standardSn':standardSn,'auditSn':auditSn},
//           			rownumbers:true,/* 行号*/
//           			remoteSort:false,/*禁止使用远程排序*/
//           			rowStyler:function(row){
//           				if (row.conformDegree<100){
//           					return 'background-color:#FFF8D7;color:#000;';    // rowStyle是一个已经定义了的ClassName(类名)
//           				}else{
//           					return 'background-color:#FFFFFF;color:#000;';
//               			}
//           			},      			
//           			//异步加载
//            	 	    onBeforeExpand:function(row){  
//          		        //动态设置展开查询的url
//          		        $("#dg").treegrid('selectRow',row.id);
//          		        var url = "${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex?indexSn="+row.indexSn;
//          		        $("#dg").treegrid("options").url = url;
//          		        return true;      
//           		    },
//           		    toolbar:[{
//               		    text:'<table>'+
//                   		    '<tr><td>'+'月平均得分：'+
//                   		    '</td><td>'+month+
//                   		  	'*60%</td><td>'+'&nbsp;半年度考核得分：'+
//                   			'</td><td>'+data.score+
//                   		    '*40%</td></tr>'+
//                   		    '</table>'
//               		}],
//           		    columns:[[
//           		        {field:'id',title:'逻辑主键',hidden:true}, 
//           		        {field:'indexSn',title:'编号',hidden:true},
//           		        {field:'indexName',title:'考核标准',width:80,formatter:function(value,row){
//           		        	return "<span title=" + value + ">" +row.indexSn+ value + "</span>";
//           		        }}, 
//           		        {field:'auditKeyPoints',title:'考核要点'},
//           		        {field:'integerScore',title:'整数分数'},
//           		        {field:'conformDegree',title:'符合度（得分率）',formatter:function(value,row,index){
//         					var str=row.indexSn;
//         					var s=str.substring(str.length-1);
//         					if(s=="P"||s=="C"||s=="D"||s=="A"){
//         						return value
//         					} 
//         			    }}
//           		    ]]    
//           		}); 
//             }
//         });
	})
	
</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>