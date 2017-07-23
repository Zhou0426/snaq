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
<style type="text/css">
        #fm{
            margin:0;
            padding:10px 30px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:5px;
        }
        .fitem label{
            display:inline-block;
            width:80px;
        }
        .fitem input{
            width:160px;
        }
</style>
</head>
<body>
    <table id="dg" class="easyui-datagrid" style="width:100%;height:660px;">
        <thead>
            <tr>
                <th data-options="field:'check',width:'15%'">监察内容</th>
                <th data-options="field:'check_detail',width:'15%'">监察内容细分</th>
                <th data-options="field:'check_info',width:'69%'" editor="{type:'validatebox',options:{required:false}}">监察信息</th>
            </tr>
        </thead>
    </table>
    <div id="toolbar">
    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="submit()">提交</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="save()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">修改</a>
    </div>

    <script type="text/javascript">
        //生成信息表格
    	$('#dg').datagrid({
    		url:'dailyreport/superviseContentAction_show.action',
    		title:'安全日报上传',
    		iconCls:'icon-edit',
    		toolbar:'#toolbar',
    		rownumbers: true,
			fitColumns: true,
			singleSelect: true,
			onLoadSuccess: function(){
    			var merges = [{index:0,rowspan:2},
    			              {index:2,rowspan:3},
    			              {index:5,rowspan:2},
    			              {index:7,rowspan:2},
    			              {index:10,rowspan:3},
    			              {index:13,rowspan:2},
    			              {index:15,rowspan:2},
    			              {index:17,rowspan:2},
    			              {index:19,rowspan:2}];
    			for(var i=0; i<merges.length; i++){
    				$('#dg').datagrid('mergeCells',{
    					index:merges[i].index,
    					field:'check',
    					rowspan:merges[i].rowspan
    				});
    			}								
 				$(".datagrid-view2 .datagrid-body td[field='check_info']").click(function(){					
 					var ri = $(this).parent().attr("datagrid-row-index");					
 					var ori = $(this).text();//取到input中现有值
					$(this).empty().append("<input type='text' class='easyui-textbox' style='width:99.5%' value='" + ori + "' />");					
					var td = $(this);					
					$(this).find("input").focus().blur(function(){						
 						td.html('<div style="height:auto;" class="datagrid-cell datagrid-cell-c2-check_info">' + $(this).val() + '</div>');
						/* td.text($(this).val()); */						
						$("#dg").datagrid("getRows")[ri].check_info = $(this).val();
						$(this).remove();						
					});					
				});				
			}
    	});
		//提交今日安全日报
 		function submit(){
			var rows = $("#dg").datagrid("getRows");
			var json = [];
			var json_object;
			for(var i=0;i<rows.length;i++){
				json_object = {"check":rows[i].check,"check_detail":rows[i].check_detail,"check_info":rows[i].check_info};
				json.push(json_object);
			}
			if(json != null){
	  			$.ajax({
					type:'post',  
/*  				contentType: 'application/json; charset=utf-8', */
					datatype:'json',
					url:'dailyreport/superviseDailyReportDetailsAction_upload.action',
					data:{json:JSON.stringify(json), departmentSn:"你好"},
	  				success:function(data){
	 					 alert(data);
	 					 location.reload();
	 				} 
				});						
			}else{
				alert("监察信息为空，请检查后再提交！");
			}	
		}	
		//修改今日安全日报
        var url;
        function edit(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('center').dialog('setTitle','修改安全日报内容');
                $('#fm').form('load',row);
                url = 'update_user.php?id='+row.id;
            }
        }
        function saveUser(){
            $('#fm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.errorMsg){
                        $.messager.show({
                            title: 'Error',
                            msg: result.errorMsg
                        });
                    } else {
                        $('#dlg').dialog('close');        // close the dialog
                        $('#dg').datagrid('reload');    // reload the user data
                    }
                }
            });
        }
    </script>
</body>
</html>