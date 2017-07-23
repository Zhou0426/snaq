<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>安全日报</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		//生成信息表格
    	$('#dg').datagrid({
    		url:'dailyreport/superviseItemAction_show.action?personId=${sessionScope.personId}',
    		rownumbers: false,
    		nowrap:false,
    		fit:true,
			fitColumns: true,
			singleSelect: true,
			toolbar:[{
				id:'add',
				text:'上传',
				iconCls:'icon-add',
				handler:function(){
					$('#dg').datagrid('reload');
					$.messager.alert('我的消息','上传成功！','info');
				}
			}],
			onLoadSuccess: function(data){
				console.log('asd');
				var json = data.rows;
				var js = [];
				var jo;
				var index=0;
				var rowspan=1;
	  			for(var i=0;i<json.length;i++){
	  				if(i == 0){
	  					index = 0;	
	  				}else if(i == (json.length-1)){
	  					if(json[i].parentSn == json[i-1].parentSn){
	  						rowspan++;
	  						if(rowspan != 1){
		  						jo = {"index":index,"rowspan":rowspan};
		  						js.push(jo);	
	  						}
	  					}else{
	  						if(rowspan != 1){
		  						jo = {"index":index,"rowspan":rowspan};
		  						js.push(jo);	
	  						}
	  						index = i;
	  						rowspan = 1;
	  						if(rowspan != 1){
		  						jo = {"index":index,"rowspan":rowspan};
		  						js.push(jo);	
	  						}
	  					}
	  				}else{
	  					if(json[i].parentSn == json[i-1].parentSn){
	  						rowspan++;
	  					}else{
	  						if(rowspan != 1){
		  						jo = {"index":index,"rowspan":rowspan};
		  						js.push(jo);	
	  						}
	  						index = i;
	  						rowspan = 1;
	  					}
	  				}
				}
    			var merges = js; 			
    			for(var i=0; i<merges.length; i++){
    				$('#dg').datagrid('mergeCells',{
    					index:merges[i].index,
    					field:'parentName',
    					rowspan:merges[i].rowspan
    				});
    			}
    			for(var i=0; i<merges.length; i++){
    				$('#dg').datagrid('mergeCells',{
    					index:merges[i].index,
    					field:'order',
    					rowspan:merges[i].rowspan
    				});
    			}
  				$(".datagrid-view2 .datagrid-body td[field='reportSketch']").dblclick(function(){	
 					var ri = $(this).parent().attr("datagrid-row-index");					
 					var ori = $(this).text();
					$(this).empty().append("<input type='text' class='easyui-textbox' style='width:99.5%' value='" + ori + "' />");					
					var td = $(this);					
					$(this).find("input").focus().blur(function(){	
						var row=$("#dg").datagrid("getRows")[ri];
						td.html('<div style="height:auto;text-align:center;" class="datagrid-cell datagrid-cell-c2-reportSketch">' + $(this).val() + '</div>');						
						$("#dg").datagrid("getRows")[ri].reportSketch = $(this).val();
						$(this).remove();
						if(row!=null){
							$.ajax({
								url:'dailyreport/superviseDailyReportDetailsAction_saveOrUpdate.action',
								dataType:'json',
								method:'POST',
								data:{superviseItemSn:row.itemSn,reportSketch:$(this).val(),type:'info'},
								success:function(data){
									if(data.status=="nook"){
										$.messager.alert('我的消息','保存失败！','error');
										$("#dg").datagrid('load');
									}
								}
							})
						}						
					});					
				});
  				$(".datagrid-view2 .datagrid-body td[field='reportDetails']").dblclick(function(){	
 					var ri = $(this).parent().attr("datagrid-row-index");	
 					var ori = $(this).text();
					$(this).empty().append("<textarea style='width:99.5%;height:66px;'>"+ori+"</textarea>");					
					var td = $(this);					
					$(this).find("textarea").focus().blur(function(){
 						td.html('<div style="text-align:center;height:100%;width:100%" class="datagrid-cell datagrid-cell-c2-reportDetails">' + $(this).val() + '</div>');						
						$("#dg").datagrid("getRows")[ri].reportDetails = $(this).val();
						var row=$("#dg").datagrid("getRows")[ri];
						$(this).remove();	
						if(row!=null){
							$.ajax({
								url:'dailyreport/superviseDailyReportDetailsAction_saveOrUpdate.action',
								dataType:'json',
								method:'POST',
								data:{superviseItemSn:row.itemSn,reportDetails:$(this).val(),type:'details',order:row.order},
								success:function(data){
									if(data.status=="nook"){
										$.messager.alert('我的消息','保存失败！','error');
										
									}
									$("#dg").datagrid('load');
								}
							})
						}
					});
					
				});
			},
			columns:[[
				{field:'order',width:'5%',title:'序号',align:'center',styler:function(value,row,index){
						return 'background-color:#FFFFFF;';
				}},
				{field:'parentName',width:'12%',title:'监察内容',align:'center',styler:function(value,row,index){
						return 'background-color:#FFFFFF;';
				}},
                {field:'itemName',width:'13%',title:'监察内容细分',align:'center'},
                {field:'reportSketch',width:'20%',title:'监察信息',align:'center',editor:'text'},
                {field:'reportDetails',width:'50%',title:'监察详细信息',align:'center',editor:'text'},
                {field:'itemSn', hidden:true},
                {field:'parentSn',hidden:true}  
		    ]] 
    	})
    	
    	$('#add').linkbutton({
    		plain:false
    	})
	})
</script>
</head>
<body style="margin:1px;">
    <table id="dg"></table>    
</body>
</html>