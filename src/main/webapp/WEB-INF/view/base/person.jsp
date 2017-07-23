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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/easyloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script>
<!--验证规则-->
$.extend($.fn.validatebox.defaults.rules, {    
	 mobile: {//value值为文本框中的值
	        validator: function (value) {
	            return /^13[0-9]{1}[0-9]{8}$|15[0189]{1}[0-9]{8}$|189[0-9]{8}$/.test(value);
	        },
	        message: '请输入11位数字.'
	    }   
});
<!--日期格式化-->
Date.prototype.format = function (format) {  
    var o = {  
        "M+": this.getMonth() + 1, // month  
        "d+": this.getDate(), // day  
        "h+": this.getHours(), // hour  
        "m+": this.getMinutes(), // minute  
        "s+": this.getSeconds(), // second  
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S": this.getMilliseconds()  
        // millisecond  
    }  
    if (/(y+)/.test(format))  
        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
            .substr(4 - RegExp.$1.length));  
    for (var k in o)  
        if (new RegExp("(" + k + ")").test(format))  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
    return format;  
}  
//权限管理
var str="${sessionScope['permissions']}";
<!--函数-->
$(function () {
	var showpdeptsn;
	$('#showPassword').change(function(){
		if ($('#showPassword').prop('checked')){
			$('#password').show();
		}else{
			$('#password').hide();
		}
	});
	var Common = {
		    /**
		     * 格式化日期（不含时间）
		     */
		    formatterDate: function (date) {
		        if (date == undefined) {
		            return "";
		        }
		        /*json格式时间转js时间格式*/
		        date = date.substr(1, date.length - 2);
		        var obj = eval('(' + "{Date: new " + date + "}" + ')');
		        var date = obj["Date"];
		        if (date.getFullYear() < 1900) {
		            return "";
		        }
		 
		        var datetime = date.getFullYear()
		                + "-"// "年"
		                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
		                        + (date.getMonth() + 1))
		                + "-"// "月"
		                + (date.getDate() < 10 ? "0" + date.getDate() : date
		                        .getDate());
		        return datetime;
		    }
	};
	<!--数据网络-->
	$('#dg').datagrid({       
		pageNumber: 1,
		pagination:true,
        url: "${pageContext.request.contextPath}/person/show?deleteornot=0",
        rownumbers:true,
        fitColumns:true,
       	fit:true,			//表格宽高自适应
        pageSize:50,
        singleSelect:true,
        height:650,
        pageList: [20, 30,50,100],
        loadMsg:'正在载入人员信息...',
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
        toolbar:[
		{
			text:'所属部门：<input id="cc" name="tree">'
		},
    	{
    		iconCls: 'icon-add',
    		text:'增加员工',
    		id:'addtool',
    		handler:function(){
				if(str.indexOf("140201")==-1){
					$("#addtool").css('display','none');
				}else{
					$('#form').form('reset');
					$('#form').form('disableValidation');
					$('#personId').textbox('readonly',false);
	    			$('#updatebtn').hide();
				    $('#addbtn').show();
	    			$('#win').window('open');
	    			$('#showPassword').hide();
	    			$('#password').show();
	    			$('#label2').show();
	    			$('#label1').hide();
	    			var t = $('#cc').combotree('tree');
	    			var node = t.tree('getSelected');   			
	    			$('#form').form({queryParams: {departmentSn:node.id}});
				}
    		}
    	},
        {
    		iconCls: 'icon-edit',
    		text:'编辑员工',
    		id:'edittool',
    		handler: function()
    		{
				var str="${sessionScope['permissions']}";
				if(str.indexOf("140202")==-1){
					$("#edittool").css('display','none');
				}else{
	    		   $('#updatebtn').show();
				   $('#addbtn').hide();
	       			var t = $('#cc').combotree('tree');
	       			var node = t.tree('getSelected');   
	       			$('#form').form({queryParams: {departmentSn:node.id}});
	       			var departmentSn=node.id;
	    			//数据回显
	    			var rows=$("#dg").datagrid("getSelections");
	    			var department=$('#departmentSn').val();
	    			if (rows.length!=0){
	    				if(rows[0].deleted==0){
							$('#form').form('disableValidation');
							$('#personId').textbox('readonly');
			    			$('#win').window('open');
			    			$('#password').hide();
			    			$('#showPassword').show();
			    			$('#label1').show();
			    			$('#label2').hide();
			    			$('#postLevel').combogrid('setValue',{postLevelSn:rows[0].postLevelS,postLevelName:rows[0].postLevell}); 
			    			$('#deptsn').combotree('setValue',{id:rows[0].departmentsn,text:rows[0].department});
			    			//对表单数据进行填充
			    			$('#form').form('load',{
			    				changeId:rows[0].id,
			    				departmentSn:rows[0].departmentSn,
			    				personName:rows[0].personName,
			    				idNumber:rows[0].idNumber,
			    				cellphoneNumber:rows[0].cellphoneNumber,
			    				personId:rows[0].personId,
			    				education:rows[0].education,
			    				gender:rows[0].gender,
			    				birthday:rows[0].birthday
			    			});
	    				}else{
	    					$.messager.alert('提示','该行已删除，不可继续操作');
	    				}
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
    		iconCls: 'icon-remove',
    		text:'删除员工',
    		id:'deletetool',
    		handler:function()
    		{
    			//权限管理
				var str="${sessionScope['permissions']}";
				if(str.indexOf("140203")==-1){
					$("#deletetool").css('display','none');
				}else{
	    			var row = $('#dg').datagrid('getSelected');
	    			if (row){
	    				if(row.deleted==0){
		    				$.messager.confirm('提示','您确定要删除该人员吗?',function(r){
		    					if(r){
		    						$.post('${pageContext.request.contextPath}/person/delete',{changeId:row.id},function(data){
		    							$.messager.alert('提示',data.substring(1,data.length-1));
		    							$('#dg').datagrid('reload');
		    						},'text');
		    					}
		    				});
	    				}else{
	    					$.messager.alert('提示','该行已删除，不可继续操作');
	    				}
	    			}
	    			else{
	    				$.messager.alert('提示','请先选择您要删除的员工所在行');
	    			}
				}
    		}
    	},
    	{
    		text:'已删除人员：<input id="includedeleted" name="checkbox">',
    	},
		{
			iconCls: 'icon-import',
			text:'导入或调动员工',
			id:'importtool',
			handler: function()
			{
				if(str.indexOf("140204")==-1){
					$("#importtool").css('display','none');
				}else{
					$('#winImport').window('open');
				}
			}
		},
		{
			iconCls: 'icon-excel',
			text:'导出到EXCEL',			
			id:'exporttool',
			handler: function()
			{			
				if(str.indexOf("140205")==-1){
					$("#exporttool").css('display','none');
				}else{
					var url ='${pageContext.request.contextPath}/person/exportPerson?departmentSn=' + $('#cc').combotree('getValue');
					if( !$('#includedeleted').switchbutton('options').checked==true ){
						url = url + "&deleteornot=0";
					}
	  				$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
					 //进度条显示
				    $('#p').progressbar('setValue',0);
				    $('#winPro').window('open');
  				    getProValue();
				}
			}
		},
		{
			text:'<input id="tb" name="queryp">',	
			id:'querype'
 		},
		{
 			iconCls: 'icon-undo',
			text:'取消删除',	
			id:'cancelDelete',
			handler: function()
			{
				var row = $('#dg').datagrid('getSelected');
    			if (row){
    				if(row.deleted == true){
	    				$.messager.confirm('提示','您确定要恢复该人员吗?',function(r){
	    					if(r){
	    						$.post('${pageContext.request.contextPath}/person/cancelDelete',{changeId:row.id},function(data){
	    							$.messager.alert('提示',data.substring(1,data.length-1));
	    							$('#dg').datagrid('reload');
	    			    			$('#cancelDelete').css('display','none');
	    						},'text');
	    					}
	    				});
    				}else{
    					$.messager.alert('提示','该行未删除，不可继续操作');
    				}
    			}
			}
 		}
    	],
    	onSelect:function(index,row){
    		//console.log(row.deleted);
    		if( row.deleted != undefined && row.deleted == true ){
    			$('#cancelDelete').css('display','');
    		}else{
    			$('#cancelDelete').css('display','none');
    		}
    	},
        columns:[[ 
                  {field:'id',hidden:true},   
                  {field:'postLevelS',hidden:true},
                  {field:'departmentsn',hidden:true},   
                  {field:'personId',title:'人员编号',width:60} ,  
                  {field:'personName',title:'人员姓名',width:60}, 
                  {field:'cellphoneNumber',title:'电话号码',width:80},
                  {field:'implDepartmentName',title :'贯标单位',width :100},
                  {field:'department',title:'所属部门',width:100}, 
                  {field:'postLevell',title:'岗位等级',width:100},    
                  {field:'gender',title:'性别',width:30},
                  {field:'education',title:'学历',width:30},
                  {field:'birthday',title:'出生日期',width:80,formatter: formatDatebox},
                  {field:'idNumber',title:'身份证号码',width:100}
              ]]  
   });
    <!--加载datagrid里的部门表树形菜单-->
    $('#cc').combotree({
	    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1402',
    	height:22,
		panelWidth: 300,
		panelHeight:350,
	    onLoadSuccess: function (node, data) {
	       var x = $('#cc').combotree('getValue');
           if(x.length==0){  
                var da=$("#cc").combotree('tree').tree("getRoot");	
		    	$('#cc').combotree('setValue', da);
		    	$('#dg').datagrid('load',{
					departmentSn1:da.id
	    		});
		    	var node = $("#cc").combotree('tree').tree('find', da.id);
		    	$("#cc").combotree('tree').tree('select', node.target);
		    	if(data.length == 1){
	        		$('#cc').combotree('tree').tree('expand', node.target);
	        	}
           }  
		},
		<!--当被选中时datagrid提交选中节点的编号加载相应的子部门-->
		onSelect: function(){
			var t = $('#cc').combotree('tree');
			var node = t.tree('getSelected');
			if($('#includedeleted').switchbutton('options').checked==true){
				$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show';
			}else{
				$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show?deleteornot=0';
			}
			$('#dg').datagrid('load',{
				departmentSn1:node.id
			});
		}
	});
    $('#postLevel').combogrid({		
		url : '${pageContext.request.contextPath}/person/levels',
	  	panelWidth:180,    
		prompt : '输入编号或名称检索',    	     
	    idField:'postLevelSn',    
	    textField:'postLevelName',  
        delay:200,
        mode:"remote",
        loadFilter: function(data){
    		return eval('(' + data + ')');
    	},
	    columns:[[    
	        {field:'postLevelSn',title:'岗位级别编号',width:60},    
	        {field:'postLevelName',title:'岗位级别名称',width:100} 
	    ]] 
	});
    $('#tb').textbox({
    	height:22,    
        buttonText:'搜索',    
        iconCls:'icon-search', 
        iconAlign:'left',
        onClickButton:function(){
        	var personId=$('#tb').textbox('getValue');
			var t = $('#cc').combotree('tree');
			var node = t.tree('getSelected');
        	var departmentSn=node.id;
        	if($('#includedeleted').switchbutton('options').checked==true){
				$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show';
			}else{
				$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show?deleteornot=0';
			}
        	$('#dg').datagrid('load',{personId:personId,departmentSn1:departmentSn});
        }
    });
	//第一层权限
	if(str.indexOf('140201')==-1){
		$('#addtool').css('display','none');
	}
	if(str.indexOf('140202')==-1){
		$('#edittool').css('display','none');
	}
	if(str.indexOf('140203')==-1){
		$('#deletetool').css('display','none');
	}
	if(str.indexOf('140204')==-1){
		$('#importtool').css('display','none');
	}
	if(str.indexOf('140205')==-1){
		$('#exporttool').css('display','none');
	}
	$('#cancelDelete').css('display','none');
	

	$('#addtool').linkbutton({
		plain:false
	});
	$('#edittool').linkbutton({
		plain:false
	});
	$('#deletetool').linkbutton({
		plain:false
	});
	$('#importtool').linkbutton({
		plain:false
	});
	$('#exporttool').linkbutton({
		plain:false
	});
	//窗口以及窗口内元素初始化 要先初始化textbox再初始化window
	$('#personId').textbox({
		 required:true,
		 missingMessage:'请填写员工编号'
	});
	$('#personName').textbox({
		required:true,
		missingMessage:'请填写员工姓名'
	});
	$('#idNumber').textbox({
	});
	$('#cellphoneNumber').numberbox({
		 missingMessage:'请输入11位数字',
		 validType:'length[11,11]'
	});
	$('#birthday').datebox({
	});
	$('#downloadImportTemplate').linkbutton({
	});
	$('#downloadChangeTemplate').linkbutton({
	});
	$('#importPerson').linkbutton({
	});
	$('#personForDepartment').linkbutton({
	});
	$('#excel').textbox({
	});
    $('#deptsn').combotree({
	    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=1402'		
	});  
	$('#win').window({
		width:640,
		height:330,
		title:'填写员工信息',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true
	});
	$('#winImport').window({
		width:300,
		height:350,
		title:'导入或调动员工',
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		onOpen:function(){
			$('#excel').filebox({
			});
		}
	});
	
	//事件触发函数
	//更新
	$('#updatebtn').click(function(){
		$('#form').form('enableValidation');
		$('#form').form('submit',{
			url: "${pageContext.request.contextPath}/person/update",
			success: function(data){
					$('#win').window('close');
					$.messager.alert('提示',data.substring(1,data.length-1));
					$('#dg').datagrid('reload');
					$('#form').form('reset');
			}
		});
	})
	//增加
	$('#addbtn').click(function(){
		$('#form').form('enableValidation');
		$('#form').form('submit',{
			url: "${pageContext.request.contextPath}/person/add",
			success: function(data){
				$.messager.alert('提示',data.substring(1,data.length-1));
				$('#win').window('close');
				$('#dg').datagrid('reload');
				$('#form').form('reset');
			}
		});
	})
	//导入
	$('#importPerson').click(function(){
		var ex=$('#excel').filebox('getValue');
		if(ex!=""){
			 var d1=/\.[^\.]+$/.exec(ex);   
		     if(d1==".xlsx"){
			 //进度条显示
			  $('#p').progressbar('setValue',0);
			  $('#winPro').window('open');
			  var timer = setInterval(getSession,500);
				$('#formI').form('submit',{
				url: "${pageContext.request.contextPath}/person/importPerson",
				success: function(pag){
						//var a=eval(pag);
						$('#p').progressbar('setValue','100');
						clearInterval(timer);
						$('#winPro').window('close');
						
						$('#winImport').window('close');
						$.messager.alert('提示',pag.substring(1,pag.length-1));
						$('#dg').datagrid('reload');			
					}
				});
			}
		     else{
		    	 $.messager.alert('提示','请选择.xlsx文件！');
		    	 $('#excel').filebox('setValue','');
		     }
		}else{
			$.messager.alert('提示','请选择文件！');
		}
	})
	//调动
	$('#personForDepartment').click(function(){
		var ex=$('#excel').filebox('getValue');
		if(ex!=""){
			 var d1=/\.[^\.]+$/.exec(ex);   
		     if(d1==".xlsx"){
			 //进度条显示
			  $('#p').progressbar('setValue',0);
			  $('#winPro').window('open');
			  var timer = setInterval(getSession,500);
				$('#formI').form('submit',{
				url: "${pageContext.request.contextPath}/person/change",
				success: function(pag){
						//var a=eval(pag);
						$('#p').progressbar('setValue','100');
						clearInterval(timer);
						$('#winPro').window('close');
						
						$('#winImport').window('close');
						$.messager.alert('提示',pag.substring(1,pag.length-1));
						$('#dg').datagrid('reload');			
					}
				});
			}
		     else{
		    	 $.messager.alert('提示','请选择.xlsx文件！');
		    	 $('#excel').filebox('setValue','');
		     }
		}else{
			$.messager.alert('提示','请选择文件！');
		}
	});
	//包含已删除的开关
	$('#includedeleted').switchbutton({ 
	      checked: false, 
	      width:60,
	      onText:'显示',
	      offText:'不显示',
	      onChange: function(checked){
	    	  var t = $('#cc').combotree('tree');
			  var node = t.tree('getSelected');
	    	  if(checked==true){
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show';
	    	  }else{
					$('#dg').datagrid('options').url='${pageContext.request.contextPath}/person/show?deleteornot=0';
	    	  }
			  $('#dg').datagrid('load',{
				departmentSn1:node.id
			  });
	      } 
	    }) 
});
//自定义函数

function formatDatebox(value) {  
    if (value == null || value == '') {  
        return '';  
    }  
    var dt;  
    if (value instanceof Date) {  
        dt = value;  
    } else {  
        dt = new Date(value);  
    }  
  
    return dt.format("yyyy-MM-dd");  
} 
//ajax发送请求session--导入
function getSession(){
	$.ajax({
		url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findSession.action',
		success: function(data){
			$('#p').progressbar('setValue',data);
		}
	},"json");
};
function getProValue(){
	var timer = setInterval(function(){
		$.ajax({
			url:'${pageContext.request.contextPath}/hazard/manageObjectAction_findProValue.action',
			success: function(data){
				$('#p').progressbar('setValue',data);
				if(data == 100){
					$.ajax({
	 					url:'${pageContext.request.contextPath}/hazard/manageObjectAction_clearSession.action',
	 				});
					$('#winPro').window('close');
                    clearInterval(timer);
                }
			}
		},"json");
	},100);
};
</script>
</head>
<body>
	<input id="checkbox" type="hidden">
	<input id="queryp" type="hidden" >
	<input id="tree" type="hidden" >
	<table id="dg"></table>
	<div id="win" class="easyui-window">		
		<form id="form" method="post"><br/>
		   <input type="hidden" name="changeId">			
			<div class="fitem">
				&emsp;&emsp;<label>人员编号：</label>
				&emsp;&emsp;<input id="personId" value="" name="personId" class="easyui-validatebox easyui-textbox"/>
				&emsp;&emsp;<label>性别：&emsp;&emsp;&emsp;&emsp;</label>				
				<select name="gender" class="easyui-combobox" data-options="panelHeight:'auto'" style="width: 175px ">
		        	<option value="男">男</option>
		        	<option value="女" >女</option>
	        	</select>
			</div><br/>
			<div class="fitem">
				&emsp;&emsp;<label>人员姓名：</label>
				&emsp;&emsp;<input id="personName" name="personName" class="easyui-validatebox easyui-textbox"/>
				&emsp;&emsp;<label>身份证号码：</label>
				&emsp;<input id="idNumber" name="idNumber" class="easyui-textbox"/>
			</div><br/>
			
			<div class="fitem">
				&emsp;&emsp;<label>电话号码：&emsp;&emsp; </label>
			 	<input id="cellphoneNumber" name="cellphoneNumber" class="easyui-validatebox easyui-numberbox"/>
				&emsp;&emsp;<label>出生日期：</label>&emsp;&emsp;
				<input  id="birthday"  name="birthday" type= "text" class= "easyui-datebox"> </input>   		
			</div><br/>
			<div class="fitem">
			&emsp;&emsp;<label>学历：</label>&emsp;&emsp;&emsp;&emsp;
				<select name="education" class="easyui-combobox" data-options="panelHeight:'auto'" style="width: 175px ">
		        	<option value="小学">小学</option>
		        	<option value="初中" >初中</option>
		        	<option value="高中" >高中</option>
		        	<option value="中专" >中专</option>
		        	<option value="大专" >大专</option>
		        	<option value="本科" >本科</option>
		        	<option value="硕士" >硕士</option>
		        	<option value="博士" >博士</option>
	        	</select>
				&emsp;&emsp;<label>岗位等级：</label>
				&emsp;&emsp;<input id="postLevel" name="postLevel"/> 	
				<br/><br/>
				&emsp;&emsp;<label>所属部门：</label>
				&emsp;&emsp;<input id="deptsn" class="easyui-textbox" name="deptsn"/> 
				<label id="label1">&emsp;&emsp;修改密码：</label>
				<input id="showPassword" type="checkbox" /> 
				<label id="label2">&emsp;&emsp;填写密码：&emsp;&emsp;</label>
				<input id="password" name="password" type="password" placeholder="点击输入密码"/>
			</div>	<br/>
			<div id="dlg-buttons" style="padding-left:150px">
				<a id="updatebtn" class="easyui-linkbutton" iconCls="icon-ok">确认修改</a>&emsp;&emsp;
				<a id="addbtn" class="easyui-linkbutton" iconCls="icon-ok">确认添加</a>&emsp;&emsp;
			</div>
		</form>
	</div>	
	<div id="winImport" class="easyui-window" style="padding:5px;">
		<form id="formI" method="post" enctype="multipart/form-data">		
			<br/><br/>
				<input id="excel" data-options="buttonText:'选择文件'" class="easyui-filebox" name="excel"/><br/><br/><br/><br/>
				<a id="downloadImportTemplate" class="easyui-linkbutton" iconCls="icon-mini-add" href="${pageContext.request.contextPath}/person/importTemplate">下载导入模板</a>	
				&emsp;&emsp;<a id="downloadChangeTemplate" class="easyui-linkbutton" iconCls="icon-mini-add" href="${pageContext.request.contextPath}/person/downloadChange">下载调动模板</a>
				<br/><br/><br/><a id="importPerson" class="easyui-linkbutton" iconCls="icon-import">导入新员工</a>
				&emsp;&emsp;&emsp;&emsp;<a id="personForDepartment" class="easyui-linkbutton" iconCls="icon-import">人员调动</a><br/><br/><br/>
				
		</form>
	</div>
	<!-- 进度条窗口 -->
		<div id="winPro" class="easyui-window" closed=true title="操作中,请等待..." style="width:400px;height:100px"   
		        data-options="collapsible:false,minimizable:false,maximizable:false,modal:true">   
		      <div id="p" class="easyui-progressbar" style="width:300px;margin-top:20px;margin-left:50px"></div>
		</div>
</body>
</html>