<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>神华宁煤安全风险预控管理信息系统</title>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/ligerui-common.css">
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/ligerui-menu.css">
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/datePicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/demo.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
<!-- 引进bootstrap所需文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" rel="stylesheet"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/date.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<style type="text/css">
		.active{background:#4FB0AC; color:#000;}
</style>

</head>
<body>
	<div id="win" class="easyui-window" title="事故明细信息" closed="true" style="width:800px;height:300px;padding:5px;">				
		<table id="dg"></table>
	</div>	
	<div id="button" style="float:left;margin-right:10px;margin-top:10px">
		<div style="float:left;margin-right:10px;margin-top:5px">
			<span style="float:left;">&emsp;选择范围：&emsp;</span>
			<input id="cc" style="width:180px;"/>
		</div>
		<div id="typebtns" class="btn-group btn-group-sm" style="float:left;margin-right:10px">				
		</div>
		<div id="xtypebtns" class="btn-group btn-group-sm" style="float:left;margin-right:10px">
			<span style="float:left;margin-top:5px">统计口径：</span>			
			<a class="active btn btn-default" data="1">按发生部门</a>
			<a class="btn btn-default" data="2">按事故类别</a>
			<a class="btn btn-default" data="3">按事故等级</a>
			<a class="btn btn-default" data="4" iconCls="icon-undo">重置</a>
		</div>
		<div id="timediv" class="btn-group btn-group-sm" style="float:left;margin-right:10px;margin-top:5px">
				<span style="float:left;">&emsp;起止日期:&emsp;</span>
				<select id="begintimeselect" style="width:180px;"></select>
				<select id="endtimeselect" style="width:180px;"></select>
		</div>
		<div class="btn-group btn-group-sm">
			<a id="exportexcel" onclick="exportexcel()" class="btn btn-default">导出</a>
		</div>
		<br/>
	</div>	
	<br/><br/>		
	<div id="main" style="width: 100%;height:500px;float:left"></div>
	<script type="text/javascript">
	var clickEchart="";
	var clickEchartBefore="";
	var myChart = echarts.init(document.getElementById('main'));//定义直方图
	var itemSn=1;
	var date=new Date();
	var endtime=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate();
	var begintime=date.getFullYear()+"-0"+(date.getMonth())+"-"+date.getDate();
	var departmentSnIndex;
	var accidentTypeSnIndex=null;
	var accidentLevelSnIndex=null;
	var chidrenSizeIndex;
	var accidentTypeSns=[];
    var accidentLevelSns=[];
    var departmentSns=[];
    var type;
    var childrenSizes=null;
    var childrenSizeIndex=null;
    var xName;
    var departmentNameIndex=null;
    var itemSn1;
    var departmentTypeSn=null;
    var accidentSnValue=[];
    var accidentNameValue=[];
    var editorValue=[];
    var deadCountValue=[];
    var fleshInjureCountValue=[];
    var seriousInjureCountValue=[];
    var directEconomicLossValue=[];
    var indirectEconomicLossValue=[];
    var directReasonValue=[];
    var indirectReasonValue=[];
	//全局变量
	$(function () {	
		 menu1 = $.ligerMenu({ 
		      top: 20, 
		      left: 100, 
		      width: 120, 
		      items:[ { text: '部门', click: loadechart},
		        { text: '事故类型', click: loadechart},
		        { text: '事故等级 ', click: loadechart} ,
				{ text: '查看明细 ', click: loadechart} ]
		    });
		    menu2= $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '部门', click: loadechart},
			        { text: '事故等级', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu3 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '部门', click: loadechart},
			        { text: '事故类型', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu4 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '部门', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu5 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [ { text: '事故类型', click: loadechart},
					        { text: '事故等级', click: loadechart},
							{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu6 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '事故等级', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu7 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			          { text: '事故类型', click: loadechart},
						{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu8 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 160, 
			      items: [
							{ text: '查看明细 ', click: loadechart} 		              
			      ]
			    });
		$(document).bind("contextmenu", function(){ return false; });
		<!--加载部门树形菜单-->
	    $('#cc').combotree({
		    url: '${pageContext.request.contextPath}/department/tree',
			panelWidth: 300,
			panelHeight:350,
		    onLoadSuccess: function (node,data) {
		       var x = $('#cc').combotree('getValue');		       
	           if(x.length==0){  
	                var da=$("#cc").combotree('tree').tree("getRoot");	
			    	$('#cc').combotree('setValue', da);		    	
			    	var node = $("#cc").combotree('tree').tree('find', da.id);
			    	$("#cc").combotree('tree').tree('select', node.target);	
			    	
			    	if(data.length == 1){
		        		$('#cc').combotree('tree').tree('expand', node.target);
		        	}
	           }        
			},
			//选择时执行选择方法动态生成部门菜单，给部门编号和部门类型编号赋值并发送Action
			onSelect: function(){
				nodeSelect();
				loadechart();
			}
		});       
	    function nodeSelect(){	  
			var t = $('#cc').combotree('tree');
			var node = t.tree('getSelected'); 
			departmentSnIndex=node.id;	
			$.post("${pageContext.request.contextPath}/report/typeReport",{departmentSn:departmentSnIndex},function(rv){
				$("#typebtns").empty();
 					if(rv.length>0){					
 						$("#typebtns").append("<span style='float:left;margin-top:5px'>"+"部门类型："+"</span>");
 					}
					for (var i = 0;i < rv.length;i++){
						$("#typebtns").append("<a class='btn btn-default' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
					}
				$.parser.parse($('#typebtns').parent());
				<!--部门类型按钮点击 -->
				$("#typebtns a").click(function(){
					if($(this).hasClass("active"))
			    	{
			       		$(this).removeClass("active");
			       		departmentTypeSn=null;
			    	}
			    	else
			    	{
						$("#typebtns a").removeClass("active");
				        $(this).addClass("active");
						departmentTypeSn=$(this).attr("data");
			        }
					loadechart();
				});				
			},'json'); 
		}
	    $("#xtypebtns a").click(function(){ 	
	    	var $this = $(this); 
	    	itemSn=$(this).attr("data");
    		$("#xtypebtns a").removeClass("active");
        	$this.addClass("active");
	    	if(itemSn=='4'){
	    		itemSn=0;
    			accidentTypeSnIndex=null;
	    		accidentLevelSnIndex=null; 
	    		var da=$("#cc").combotree('tree').tree("getRoot");	
	    		$('#cc').combotree('setValue', da);	
	    		nodeSelect();
	    	}
	    	loadechart();
	    }); 
	     $('#begintimeselect').datebox({
	    	 value:begintime,
	    	 editable:false,
	    	 onChange: function(){
	    		begintime=$('#begintimeselect').datetimebox('getValue');
	    		loadechart();
	    	 }
		}); 	
	     $('#endtimeselect').datebox({
	    	 value:endtime,
	    	 editable:false,
	    	 onChange: function(){
	    		endtime=$('#endtimeselect').datetimebox('getValue');
	    		loadechart();
	    	 }
		});
		 myChart.setOption({
			 grid:{
	    			x:50,
	    			y:50,
	    			x2:50,
	    			y2: 150			    			
	    	 },
			tooltip : {
		        trigger: 'axis',
		        axisPointer : {
		            type : ''
		        }
		    },
		     title: {
		         text: '事故报表',
		         subtext: '直方图分析展示',
		         x: 'center'
		     },
		     legend: {
		         data:['部门']
		     },
		     xAxis: {
		         data: []
		     },
		     yAxis: {},
		     series: [{
		    	 markLine : {
		                lineStyle: {
		                    normal: {
		                        type: 'dashed'
		                    }
		                },
		                data : [
		                    [{type : 'min'}, {type : 'max'}]
		                ]
		            },
		         name: '不符合项',
		         type: 'bar', barWidth: '50'
		     }]
		 }); 		 
		 myChart.on('mousedown', function (params) {
		          if (params.event.event.which == 3) {
			          var os = $("#main").offset();		
				      var x=params.dataIndex;
			    	  childrenSizeIndex=childrenSizes[x];
				      if(type=='0'){
				      }
			          if(type=='1'){
						  departmentSnIndex=departmentSns[x];
						  departmentNameIndex=xName[x];
			          	}
			          if(type=='2'){
						  accidentTypeSnIndex=accidentTypeSns[x];
			          }
			          if(type=='3'){
						  accidentLevelSnIndex=accidentLevelSns[x];
			          }
					if(childrenSizeIndex!=0){
				    	if(accidentLevelSnIndex==null & accidentTypeSnIndex==null){
				        	  menu1.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				    	}
				          if(accidentLevelSnIndex==null&accidentTypeSnIndex!=null){
				        	  menu2.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }
				          if(accidentLevelSnIndex!=null&accidentTypeSnIndex==null){
				        	  menu3.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }					    	
				          if(accidentLevelSnIndex!=null&accidentTypeSnIndex!=null){
				        	  menu4.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          } 
					    }
				          
				      else{
				    	if(accidentLevelSnIndex==null & accidentTypeSnIndex==null){
				        	  menu5.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				    	}
				          if(accidentLevelSnIndex==null&accidentTypeSnIndex!=null){
				        	  menu6.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }
				          if(accidentLevelSnIndex!=null&accidentTypeSnIndex==null){
				        	  menu7.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }					    	
				          if(accidentLevelSnIndex!=null&accidentTypeSnIndex!=null){
				        	  menu8.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          } 
			          }	
				   }
	     });
		 
		 
		//弹出的datagrid	    
		    $('#dg').datagrid({
				pageNumber: 1,
				pagination:true,
		        url: "${pageContext.request.contextPath}/accident/detailAccident",
		        rownumbers:true,
		        fitColumns:true,
		        pageSize:10,
		        singleSelect:true,
		        fit:true,
		        nowrap:false,
		        pageList: [5,10,15,20],
		        columns : [ [ {
					field : 'accidentSn',
					title : '事故编号',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  accidentSnValue[index]=value;
		        		  }else{
		        			  accidentSnValue[index]="无";
		        		  }
					}
				},  {
					field : 'accidentName',
					title : '事故名称',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  accidentNameValue[index]=value;
		        		  }else{
		        			  accidentNameValue[index]="无";
		        		  }
					}
				},{
					field : 'happenLocation',
					title : '发生地点',
					width : 50
				},{
					field : 'implDepartmentName',
					title : '单位',
					width : 50
				},
				{
					field : 'implDepartmentName',
					title : '贯标单位',
					width : 50
				},
				{
					field : 'departmentName',
					title : '发生部门',
					width : 50
				}, {
					field : 'accidentProcess',
					title : '事件过程',
					width : 50
				}, {
					field : 'reasonArticle',
					title : '致因物',
					width : 50
				}, {
					field : 'directReason',
					title : '直接原因',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  directReasonValue[index]=value;
		        		  }else{
		        			  directReasonValue[index]="无";
		        		  }
					}
				},{
					field : 'indirectReason',
					title : '间接原因',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  indirectReasonValue[index]=value;
		        		  }else{
		        			  indirectReasonValue[index]="无";
		        		  }
					}
				}, {
					field : 'precautionMeasure',
					title : '预防措施',
					width : 50
				}, {
					field : 'directEconomicLoss',
					title : '直接经济损失',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  directEconomicLossValue[index]=value;
		        		  }else{
		        			  directEconomicLossValue[index]="无";
		        		  }
					}
				},{
					field : 'indirectEconomicLoss',
					title : '间接经济损失',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  indirectEconomicLossValue[index]=value;
		        		  }else{
		        			  indirectEconomicLossValue[index]="无";
		        		  }
					}
				}, {
					field : 'deadCount',
					title : '死亡人数',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  deadCountValue[index]=value;
		        		  }else{
		        			  deadCountValue[index]="无";
		        		  }
					}
				}, {
					field : 'seriousInjureCount',
					title : '重伤人数',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  seriousInjureCountValue[index]=value;
		        		  }else{
		        			  seriousInjureCountValue[index]="无";
		        		  }
					}
				}, {
					field : 'fleshInjureCount',
					title : '轻伤人数',
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  fleshInjureCountValue[index]=value;
		        		  }else{
		        			  fleshInjureCountValue[index]="无";
		        		  }
					}
				}, {
					field : 'happenDateTime',
					title : '发生时间',
					width : 50
				}, {
					field : 'accidentType',
					title : '事故类型',
					width : 50
				}, {
					field : 'accidentLevel',
					title : '事故等级',
					width : 50
				}, {
					field : 'editor',
					title : '编辑人',
					width : 300,
					hidden:true,
					formatter: function(value,row,index){
		        		  if(value!=null && value!=""){
		        			  editorValue[index]=value;
		        		  }else{
		        			  editorValue[index]="无";
		        		  }
					}
				} ] ]  ,
	              onBeforeLoad:function(){
	            	  if(itemSn1!=0){
	            		  return false;
	            	  }else{
	            		  return true;
	            	  }
	              },
	              view: detailview, 
					detailFormatter: function(rowIndex, row){
					return '<table style="border:1"><tr>' + 
					'<td style="width:90px;text-align:center">'+'事故编号：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + accidentSnValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'事故名字：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + accidentNameValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'编辑人：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + editorValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:90px;text-align:center">'+'死亡人数：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + deadCountValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'重伤人数：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + seriousInjureCountValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'轻伤人数：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + fleshInjureCountValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:90px;text-align:center">'+'直接原因：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + directReasonValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'间接原因：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + indirectReasonValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'直接经济损失：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + directEconomicLossValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:50px;text-align:center">'+'间接经济损失：' + '</td>' + 
					'<td style="width:100px;text-align:center" colspan="5">' + 
					'<p>' + indirectEconomicLossValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr></table>'; 
					}
		   });   
	
	});
	
	function loadechart(){
		//右键选择菜单
  		if(event.srcElement.innerText!=null){
  	    	itemSn1 =event.srcElement.innerText;
  	    	item =event.srcElement.innerText;
  	    	if(item=='部门'||item=='专业'||item=='危险等级'){
				itemSn1=0;
				clickEchart="1";
			    clickEchartBefore="1";
				departmentTypeSn=null;
			    $('#cc').combotree('setValue',{id: departmentSnIndex,text:departmentNameIndex});
			    $.post("${pageContext.request.contextPath}/report/typeReport",{departmentSn:departmentSnIndex},function(rv){
					$("#typebtns").empty();
 					if(rv.length>0){					
 						$("#typebtns").append("<span style='float:left;font-weight:bold;margin-top:5px'>"+"部门类型："+"</span>");
 					}
					for (var i = 0;i < rv.length;i++){
						$("#typebtns").append("<a class='btn btn-default' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
					}
					$.parser.parse($('#typebtns').parent());
					itemSn1=1;
					<!--部门类型按钮点击 -->
 					$("#typebtns a").click(function(){
 						if($(this).hasClass("active"))
 				    	{
 				       		$(this).removeClass("active");
 				       		departmentTypeSn=null;
 				    	}
 				    	else
 				    	{
 							$("#typebtns a").removeClass("active");
 					        $(this).addClass("active");
 							departmentTypeSn=$(this).attr("data");
 				        }
 	 					loadechart();	
 					});			
 					item=null;
 					loadechart();		
 				},'json'); 
  	    	}
	    	if(item=='部门'){
				itemSn="1";
				$('#xtypebtns a').removeClass("active");
			    $('#deptbtn').addClass("active");
	    	}
			if(item=='事故类型'){
				itemSn="2";					 
				$('#xtypebtns a').removeClass("active");
			   // $('#spbtn').addClass("active");
			}
	    	if(item=='事故等级'){
				itemSn="3";
				$('#xtypebtns a').removeClass("active");
			    //$('#levelbtn').addClass("active");
	    	}
	    	if(item=='查看明细'){
	    		//显示对应的不符合项数据
	    		itemSn1=0;
    			$('#win').window('open');
	    		$('#dg').datagrid('load',{
		    		begintime:begintime,
		    		endtime:endtime,
		    		accidentTypeSnIndex:accidentTypeSnIndex,
		    		accidentLevelSnIndex:accidentLevelSnIndex,
		    		departmentSnIndex:departmentSnIndex,
		    		departmentTypeSn:departmentTypeSn
    			});
	    	} 
  		}
        if(itemSn1!='0'){
			$.get('${pageContext.request.contextPath}/accident/reportAccident',{departmentTypeSn:departmentTypeSn,begintime:begintime,endtime:endtime,accidentTypeSnIndex:accidentTypeSnIndex,accidentLevelSnIndex:accidentLevelSnIndex,departmentSnIndex:departmentSnIndex,itemSn:itemSn,clickEchart:clickEchart},function (data) {
			     // 填入数据
			     accidentTypeSns=data.accidentTypeSns;
			     accidentLevelSns=data.accidentLevelSns;
			     departmentSns=data.departmentSns;
			     type=data.type;
			     childrenSizes=data.childrenSizes;
			     xName=data.xName;
			     clickEchart="";
			     myChart.setOption({
			         xAxis: {
			        	data: data.xName,
			        	axisLabel: {rotate:0, show: true, margin:2,interval:0,
			        		formatter:function(val){
			        			if(val!=null){
				        		    return val.split("").join("\n");
			        			}
			        		}	
			        	}
			         },	
			         series: [{
			             name: '事故数目',
			             data:  data.count
			         }]
			     });
				   //用于使chart自适应高度和宽度
					window.onresize = myChart.resize;
		},'json'); 
	}
	}
	//点击导出
	function exportexcel(){ 
		var url ="${pageContext.request.contextPath}/accident/exportexcelAccident?itemSn="+itemSn+"&begintime="+begintime+"&endtime="+endtime+"&accidentTypeSnIndex="+accidentTypeSnIndex+"&accidentLevelSnIndex="+accidentLevelSnIndex+"&departmentSnIndex="+departmentSnIndex+"&departmentTypeSn="+departmentTypeSn+"&clickEchart="+clickEchartBefore;
		$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
	};
	</script>
</body>
</html>