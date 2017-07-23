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

	function unsafecondition(){
		$('#dg').treegrid('clearSelections');//清处多选的行
			parent.$('#child-win').window({
				width:900,
				height:400,
				title:'相关隐患',
				cache:false,
				content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/unitquarter-detail" frameborder="0" width="100%" height="100%"/>'
			});
	}
	$(function(){
		var rows=parent.$('#dg').datagrid('getSelections');
		var value=parent.$('#value').val();
		var title=rows[0].departmentName+" 第"+value+"季度得分详情";
		var year=parent.$('#cc2').combobox('getValue');
		parent.$('#win').window('setTitle',title);
		$.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryQuarterDetail",
            data: {'year':year,'quarter':value,'departmentSn':rows[0].departmentSn},
            dataType: "json",
            success: function(data){
                  var auditSn=data.auditSn;
                  var standardSn=data.standardSn;                
                  $('#dg').treegrid({    
          		    url:'${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex',    
          			idField:"id",// id字段为标识字段,那么此字段状态将会被dg记录下来    
          		    treeField:'indexName',//定义树节点字段
          		    fitColumns:true,/*列宽自动*/
          		    fit:true,
          			nowrap:true,/*数据同一行*/
          			loadmsg:'请等待',
          			queryParams:{'standardSn':standardSn,'auditSn':auditSn},
          			rownumbers:true,/* 行号*/
          			remoteSort:false,/*禁止使用远程排序*/
          			rowStyler:function(row){
          				if (row.conformDegree<100){
          					return 'background-color:#FA5858;color:#000;';    // rowStyle是一个已经定义了的ClassName(类名)
          				}else{
          					return 'background-color:#FFFFFF;color:#000;';
              			}
          			},      			
          			//异步加载
           	 	    onBeforeExpand:function(row){  
         		        //动态设置展开查询的url
         		        $("#dg").treegrid('selectRow',row.id);
         		        var url = "${pageContext.request.contextPath}/system/audit/systemauditAction_getStandardIndex?indexSn="+row.indexSn;
         		        $("#dg").treegrid("options").url = url;
         		        return true;      
          		    },
          		    toolbar:[{
              		    text:'<table>'+
                  		    '<tr><td>'+'季度得分：'+
                  			'</td><td>'+(data.score).toFixed(2)+
                  		    '</td></tr>'+
                  		    '</table>'
              		}],
          		    columns:[[
          		        {field:'id',title:'逻辑主键',hidden:true}, 
          		        {field:'indexSn',title:'编号',hidden:true},
          		        {field:'indexName',title:'考核标准',width:80,formatter:function(value,row){
          		        	return "<span title=" + value + ">" +row.indexSn+ value + "</span>";
          		        }}, 
          		        {field:'auditKeyPoints',title:'考核要点'},
          		        {field:'integerScore',title:'整数分数'},
          		        {field:'conformDegree',title:'符合度（得分率）',formatter:function(value,row,index){
        					var str=row.indexSn;
        					var s=str.substring(str.length-1);
        					if(s=="P"||s=="C"||s=="D"||s=="A"){
        						return value
        					}else if(value<100){
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
            }
        });
	})
	
</script>
</head>
<body style="margin: 1px;">
	<table id="dg"></table>
</body>
</html>