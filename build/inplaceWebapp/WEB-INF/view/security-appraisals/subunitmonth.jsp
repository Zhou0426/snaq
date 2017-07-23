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
		var rows=parent.$('#dg').datagrid('getSelections');
		var value=parent.$('#value').val();
		var title=rows[0].departmentName+" "+value+"月份得分详情";
		var year=parent.$('#cc2').combobox('getValue');
		parent.$('#win').window('setTitle',title);
		
		$.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/security/appraisals/appraisalsAction_queryMonthDetail",
            data: {'year':year,'month':value,'departmentSn':rows[0].departmentSn,'type':1},
            dataType: "json",
            success: function(data){
                  var i=0;
                  $('#cc').calendar({
                	  formatter:function(date){
        				i++;
          				if(date.getMonth()==(value-1) && date.getFullYear()==year){
              				if(data[i+"d"]>=0){
              					return date.getDate()+'</br>'+"得分："+(data[i+"d"]).toFixed(2);
                  			}else{
                  				return date.getDate();
                      		}         					
          				}else{
              				i=0;
          					return date.getDate();
          				}
          			}
                  });
            }
        });
		//日历
		$('#cc').calendar({  
			fit:true,  
			year:year,
			month:value,
			validator: function(date){
				if((date.getMonth()+1) == value){
					return true;
				}else {                                    
					return false;                                
				}
			},
			onSelect: function(date){
 				parent.$('#child-win').window({    
				    width:900,    
				    height:400,
				    title:' ',
				    modal:true,
				    cache:false,
				    content:'<iframe src="${pageContext.request.contextPath}/security/appraisals/subunitmonth-detail" frameborder="0" width="100%" height="100%"/>'
				});
			}

		});
	})
	
</script>
</head>
<body style="margin: 1px;">
	<input id="date" type="hidden"/>
	<div id="cc" style="width:180px;height:180px;"></div>
</body>
</html>