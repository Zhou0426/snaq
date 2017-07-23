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
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/date.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/datagrid-detailview.js"></script>
<!-- 引进bootstrap所需文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" rel="stylesheet"></script>
<style type="text/css">
		.active{background:#4FB0AC; color:#000;}
</style>

</head>
<body>
	<div id="win" class="easyui-window" title="不符合项明细信息" closed="true" style="width:1200px;height:400px;padding:5px;">			
		<table id="dg"></table>
	</div>	
	<div id="winAttachments" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div>
	<div id="button" style="margin-left:20px;margin-top:20px;">
		<div style="float:left;margin-right:10px">
			<span style="float:left;margin-top:5px"><b>选择范围：</b></span>
			<input id="cc" />
		</div>
		<div id="typebtns" class="btn-group btn-group-sm" style="float:left;margin-right:10px">			
		</div>
		<div id="xtypebtns" class="btn-group btn-group-sm" style="float:left;margin-right:10px">
			<span style="float:left;margin-top:5px"><b>统计口径：</b></span>				
			<a id="deptbtn" class="btn btn-default active" data="1">按部门</a>
			<a id="spbtn" class="btn btn-default" data="2" >按专业</a>
			<a id="levelbtn" class="btn btn-default" data="3">按风险等级</a>
			<a id="undobtn" class="btn btn-default" data="4">重置</a>
		</div>
		<div id="timebtns" class="btn-group btn-group-sm" style="float:left;margin-right:10px;margin-right:20px">
			<span style="float:left;margin-top:5px"><b>报表类型：</b></span>			
			<a id="rb" class="active  btn btn-default" data="1">日报</a> 
			<a class="btn btn-default" data="2">周报</a>
			<a class="btn btn-default" data="3">月报</a>	
			<a class="btn btn-default" data="4">季报</a>
			<a class="btn btn-default" data="5" >半年报</a>
			<a class="btn btn-default" data="6" >年报</a>
		</div>
	    <div style="float:left;">
<!-- 	    	<label class="label-info">选择日期：</label> -->
			<div id="daydiv" style="float:left;margin-right:10px">
					<select id="dayselect" style="width:120px;height:40px;" class= "easyui-datebox" editable="false"></select>
			</div>	
			<div id="container" style="float:left;margin-right:10px"> 
					<form name="chooseDateForm" id="chooseDateForm" action="datePicker.html#">					
						<fieldset>
							<!-- <legend>请选择周数</legend> 		 -->			
		                    <ol>
		                        <li>
									<input name="date1" id="date1" class="date-pick" />
								</li>
							</ol>
						</fieldset>
					</form>
		    </div>
			<div id="yeardiv" style="float:left;margin-right:10px" name="xx">
				<input id="yearselect" name="year" class="easyui-combobox" style="height:27px;width:70px;" />
			</div> 
			<div id="halfyeardiv" style="float:left;margin-right:10px">
				<select id="halfyearselect" class="easyui-combobox" name="halfyear" style="height:27px;width:70px;">   
				    <option value="1">上半年</option>   
				    <option value="7">下半年</option>  
				</select>
			</div>
			<div id="seasondiv" style="float:left;margin-right:10px">
				<select id="seasonselect" class="easyui-combobox" name="season" style="height:27px;width:70px;">   
				    <option value="1">春季</option>   
				    <option value="4">夏季</option>   
				    <option value="7">秋季</option>   
				    <option value="10">冬季</option>  
				</select>
			</div> 
			<div id="monthdiv" style="float:left;margin-right:10px">
				<select id="monthselect" class="easyui-combobox" name="month" style="height:27px;width:70px;">   
				    <option value="1">1</option>   
				    <option value="2">2</option>   
				    <option value="3">3</option>   
				    <option value="4">4</option>   
				    <option value="5">5</option>  
				    <option value="6">6</option>   
				    <option value="7">7</option>   
				    <option value="8">8</option>   
				    <option value="9">9</option>   
				    <option value="10">10</option>  
				    <option value="11">11</option>   
				    <option value="12">12</option> 
				</select>
			</div>
		</div>
		<div class="btn-group btn-group-sm">
			<a id="exportexcel" onclick="exportexcel()" class="btn btn-default">导出</a>
		</div>
<!-- 		<form id="form"></form> -->
	</div>	
	<br/>
	<div id="main" style="float:left;width:100%;height:500px;"></div>
	
	<div id="exportDiv">
		<a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-excel'" >导出详情</a>
		<form id="exportForm">
		</form>
	</div>
	<script type="text/javascript">
	//全局变量
	var clickEchart="";
	var clickEchartBefore="";
	var departmentTypeSn=null;
	var begin=null;//记录开始时间
	var end=null;//结束时间
	var year=null;//年
	var month=null;//月
	var season=null;//日
	var halfyear=null;//半年
	var showtime="1";//所选报表时间类型，根据类型拼接不同下拉框的时间
	var type=null;//后台传回来的此时横坐标的类型，根据此时横坐标的类型更新三个不同的查询条件（departmentSnIndex,inconformitySnIndex,specialitySnIndex)
	var myChart = echarts.init(document.getElementById('main'));//定义直方图
 	var menu=null;                 //上下文菜单，ligerMenu
    var itemName, itemNum;    //图表中被右键单击的数据项目名称和数值
	var departmentSns= [];//从后台获取的所有部门编号编号集合
	var specialitySns= [];//从后台获取的所有专业编号集合
	var inconformityLevelSns= [];//从后台获取的所有危险的等级编号集合
    var item=null;//右键菜单所选择的值
    var itemSn='1';//右键菜单所选择的标识传到后台表示用户选择按什么分类 默认itemSn=1 携带部门编号和部门类型编号查询
    var departmentSnIndex=null;  //用户所选柱子的部门编号索引
    var specialitySnIndex=null;//用户所选柱子的专业编号索引
    var inconformityLevelSnIndex=null;//用户所选的危险等级编号索引
    var children=null;//当横坐标为部门时的所有子部门集合
    var childrenIndex=null;//获得当前柱子部门下的所有子部门，看是否为空，用来判断是否有子部门 如果没有 以后下钻都去掉部门这个选项
    var departmentNames=null;
    var departmentNameIndex=null;
    var inconformityitem=[];
    var whichIndex=null;
    var itemSn1=null;
    
	var hazardValue=new Array();
	var machineValue=new Array();
	var checkersValue=new Array();
	var specialityValue=new Array();
	var deductPointsValue=new Array();
	var standardIndexValue=new Array();
	var correctPrincipalValue=new Array();
	var inconformityLevelValue=new Array();
	var inconformityItemSnValue=new Array();
	var inconformityItemNatureValue=new Array();
	var checkTypeValue = new Array();
	var systemAuditValue=new Array();
	var checkerFromValue=new Array();
	
	var nowYear = new Array();
	var dateValue = new Date;
	for(var i = dateValue.getFullYear() ; i > 2015; i--){
		nowYear.push({"id" : i, "text" : i + "年"});
	};
	$(function () {
		
		$('#export').bind('click', function(){    
			$('#exportForm').form('submit', {
				url:'${pageContext.request.contextPath}/unsafeCondition/query/queryAction_reportExcel',
				queryParams:{
						departmentTypeSn:departmentTypeSn,
			    		begin:begin,
			    		end:end,
			    		specialitySnIndex:specialitySnIndex,
			    		inconformityLevelSnIndex:inconformityLevelSnIndex,
			    		departmentSnIndex:departmentSnIndex,
			    		departmentTypeSn:departmentTypeSn
				}
			});
	    });
		
		
		  //初始化后面要显示的上下文菜单
		    menu1 = $.ligerMenu({ 
		      top: 20, 
		      left: 100, 
		      width: 120, 
		      items:[ { text: '部门', click: loadechart},
		        { text: '专业', click: loadechart},
		        { text: '危险等级 ', click: loadechart} ,
				{ text: '查看明细 ', click: loadechart} ]
		    });
		    menu2= $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '部门', click: loadechart},
			        { text: '危险等级', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu3 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '部门', click: loadechart},
			        { text: '专业', click: loadechart},
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
			      items: [ { text: '专业', click: loadechart},
					        { text: '危险等级', click: loadechart},
							{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu6 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			        { text: '危险等级', click: loadechart},
					{ text: '查看明细 ', click: loadechart} 
			      ]
			    });
		    menu7 = $.ligerMenu({ 
			      top: 20, 
			      left: 100, 
			      width: 120, 
			      items: [
			          { text: '专业', click: loadechart},
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
        //取消浏览器默认右键上下文菜单的显示
        $(document).bind("contextmenu", function(){ return false; });
		$("#container" ).hide();
		$("#halfyeardiv" ).hide();
		$("#yeardiv" ).hide();
		$("#monthdiv" ).hide();
		$("#seasondiv" ).hide();
		$('.date-pick').datePicker({selectWeek:true,closeOnSelect:true});
		<!--加载部门树形菜单-->
	    $('#cc').combotree({
		    url: '${pageContext.request.contextPath}/department/treeAll?resourceSn=0601',
	    	width:200,
	    	height:27,
			panelWidth: 300,
			panelHeight:350,
		    onLoadSuccess: function (node, data) {
		    	//部门树形菜单加载完成后获取下拉框里的值，此时为空
		       var x = $('#cc').combotree('getValue');		       
	           if( x.length == 0 ){  
	        	   //当下拉框里的值为空时，将值设为根节点，并找到该节点的id，选择该节点的Dom对象
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
			//onChange: nodeSelect		
		});       
	    function nodeSelect(){	  
	    	//选择部门
			var t = $('#cc').combotree('tree');
			var node = t.tree('getSelected'); 
			//默认将根节点编号赋值给部门编号
			departmentSnIndex=node.id;
			//$("#cc").combotree('tree').tree('expand',node.target);	
			//选择部门下拉框时根据部门编号动态生成部门类型菜单
			$.post("${pageContext.request.contextPath}/report/typeReport",{departmentSn:node.id},function(rv){
				$("#typebtns").empty();
				if(rv.length>0){					
					$("#typebtns").append("<span style='float:left;font-weight:bold;margin-top:5px'>"+"部门类型："+"</span>");
				}
				for (var i = 0;i < rv.length;i++){
						$("#typebtns").append("<a class='btn btn-default' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
				}

				//现在时间和部门编号itemSn为1，部门类型被赋值后第一次发送Action。
				//loadechart();
				$.parser.parse($('#typebtns').parent());
				<!--部门类型按钮点击 -->
				//部门类型样式变换
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
	    
	    
	    <!--日期按钮点击 -->
	    $("#timebtns a").click(function(){  
	    	var $this = $(this);
    		$("#timebtns a").removeClass("active");
        	$this.addClass("active");
    		timebtnchange();
	    	showtime=$(this).attr("data");
	    	if(showtime=='1'){
	    		$('#daydiv').show();
// 	    		var date=new Date();
// 	    		begin=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate()+" 00:00:00";
// 	    		end=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate()+" 23:59:59";	 
	    		begin=$('#dayselect').datebox('getValue')+" 00:00:00";
	    		end=$('#dayselect').datebox('getValue')+" 23:59:59";	 
	    	}
	    	if(showtime=='2'){
	    		$("#container" ).show();
	    		var time=$('#date1').val().toString();
				var year1=time.substring(6,10);
				var month1=time.substring(3,5);
				var day1=time.substring(0,2);
				var day2=parseInt(day1)+6;
				var x=day2.toString();
				begin= year1+'-'+month1+'-'+ day1 +" 00:00:00";
				end= year1+'-'+month1+'-'+ x +" 00:00:00";
	    	}
	    	if(showtime=='3'){
	    		$('#yeardiv').show();
	    		$('#yearselect').combobox('setValue', dateValue.getFullYear());
	    		$('#monthdiv').show();
// 	    		year='2016';
// 	    		month='1';
				year=$('#yearselect').combobox('getValue');
				month=$('#monthselect').combobox('getValue');
	    	}
	    	if(showtime=='4'){
	    		$('#yeardiv').show();
	    		$('#yearselect').combobox('setValue', dateValue.getFullYear());
	    		$('#seasondiv').show();
// 	    		year='2016';
// 	    		season='1';
				year=$('#yearselect').combobox('getValue');
				season=$('#seasonselect').combobox('getValue');
	    	}
	    	if(showtime=='5'){
	    		$('#yeardiv').show();
	    		$('#yearselect').combobox('setValue', dateValue.getFullYear());
	    		$('#halfyeardiv').show();
// 	    		year='2016';
// 	    		halfyear='1';
				year=$('#yearselect').combobox('getValue');
				halfyear=$('#halfyearselect').combobox('getValue');
	    	}
	    	if(showtime=='6'){
	    		$('#yeardiv').show();
	    		$('#yearselect').combobox('setValue', dateValue.getFullYear());
	    		//year='2016';
				year=$('#yearselect').combobox('getValue');
	    	}
	    	loadechart();
	    }); 
	    //横坐标类型按钮点击
	    $("#xtypebtns a").click(function(){ 
	    	//改变样式	    	
	    	var $this = $(this); 
	    	itemSn=$(this).attr("data");
    		$("#xtypebtns a").removeClass("active");
        	$this.addClass("active");
        	//只有点击重置按钮时才清空下面所选的额专业和等级条件和默认根节点和默认第一个部门和默认时间，如果不选重置的话前面选择的所有条件都在
	    	if(itemSn=='4'){
	    		//itemSn=0时调用方法查询子部门判断右键菜单
	    		itemSn=0;
    			specialitySnIndex=null;
	    		inconformityLevelSnIndex=null; 
	    		departmentTypeSn=null;
	    		var da=$("#cc").combotree('tree').tree("getRoot");	
	    		$('#cc').combotree('setValue', da);	
	    		nodeSelect();	    		
	    		timebtnchange();
	    		$('#daydiv').show();	   
				showtime=null;
	    		begin=$('#dayselect').datebox('getValue')+" 00:00:00";
	    		end=$('#dayselect').datebox('getValue')+" 23:59:59"; 	    		
	    		//设置默认按钮样式
	    		$("#timebtns a").removeClass("active");
	    		$("#typebtns a").removeClass("active");
	    		$("#xtypebtns a").removeClass("active");
	    		$("#rb").addClass("active");
	    		$("#undobtn").addClass("active");
	    	}
	    	loadechart();
	    }); 
	    <!--选中day下拉框-->
	     $('#dayselect').datebox({
	    	value:"14/5/2016",
	    	height:27,
	    	onSelect: function(date){
	    		if(date.getDate()<10)
		    	{	if(date.getMonth()<10){
			    		begin=date.getFullYear()+"-0"+(date.getMonth()+1)+"-0"+date.getDate()+" 00:00:00";
			    		end=date.getFullYear()+"-0"+(date.getMonth()+1)+"-0"+date.getDate()+" 23:59:59";
		    		}
			    	else
			    	{
			    		begin=date.getFullYear()+"-"+(date.getMonth()+1)+"-0"+date.getDate()+" 00:00:00";
			    		end=date.getFullYear()+"-"+(date.getMonth()+1)+"-0"+date.getDate()+" 23:59:59";
			    	}
		    	}
		    	else
		    	{
		    		if(date.getMonth()<10){
			    		begin=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate()+" 00:00:00";
			    		end=date.getFullYear()+"-0"+(date.getMonth()+1)+"-"+date.getDate()+" 23:59:59";
	    			}
			    	else
			    	{
			    		begin=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" 00:00:00";
			    		end=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" 23:59:59";
			    	}
		    	}
	    		loadechart(); 
	    	}
		}); 	
	    //默认将时间设置为当前日期
		begin=$('#dayselect').datebox('getValue')+" 00:00:00";
		end=$('#dayselect').datebox('getValue')+" 23:59:59";
	    <!--week下拉框改变-->
	     $('#date1').change(function(){
			var time=$(this).val().toString();
			var year1=time.substring(6,10);
			var month1=time.substring(3,5);
			var day1=time.substring(0,2);
			var day2=parseInt(day1)+6;
			var x=day2.toString();
			begin= year1+'-'+month1+'-'+ day1 +" 00:00:00";
			end= year1+'-'+month1+'-'+ x +" 00:00:00";
			loadechart();
		});
	     <!--other下拉框改变-->
	     $('#yearselect').combobox({
	    	 panelHeight:150,
	         editable:false,
	    	 valueField: 'id',
			 textField: 'text',
	         onLoadSuccess:function(node, data){
	        	 //$('#yearselect').combobox('select', node[0].id);
	         },
		     onSelect:function(record){
		 		year=record.id;
		 		loadechart();
		     }
	     });
		 $('#yearselect').combobox('loadData', nowYear);
	     $('#monthselect').combobox({
	    	 panelHeight:'auto',
	         editable:false,
		     onSelect:function(param){
		 		month=param.value;
		 		loadechart();
		     }
	     });
	     $('#seasonselect').combobox({
	    	 panelHeight:'auto',
	         editable:false,
		     onSelect:function(param){
		 		season=param.value;
		 		loadechart();
		     }
	    	});
	     $('#halfyearselect').combobox({
	    	 panelHeight:'auto',
	         editable:false,
		     onSelect:function(param){
		 		halfyear=param.value;
		 		loadechart();
		     }
	    	});	     

	 	// echarts的初始化设置。显示标题，图例和空的坐标轴
		 myChart.setOption({
			tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		     title: {
		         text: '隐患报表',
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
		    	//点击时判断是否有子部门，如果没有子部门，就不显示部门
		          if (params.event.event.which == 3) {
			          var os = $("#main").offset();		
				      var x=params.dataIndex;//判断所选择的柱子编号
				      whichIndex=x;
			    	  childrenIndex=children[x];
				      //根据已经选择的标准更新保存 不同的值
				      if(type=='0'){
				    	  //childrenIndex=children[x];
				      }
			          if(type=='1'){
						  departmentSnIndex=departmentSns[x];
						  departmentNameIndex=departmentNames[x];
						  //childrenIndex=children[x];
			          	}
			          if(type=='2'){
						  specialitySnIndex=specialitySns[x];
			          }
			          if(type=='3'){
						  inconformityLevelSnIndex=inconformityLevelSns[x];
			          }
						if(childrenIndex!=0){				   
							if(inconformityLevelSnIndex==null & specialitySnIndex==null){
				        	  menu1.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				    		}
					          if(inconformityLevelSnIndex==null&specialitySnIndex!=null){
					        	  menu2.show({
							            top: params.event.offsetY + os.top, 
							            left: params.event.offsetX + os.left 
							      });
					          }
					          if(inconformityLevelSnIndex!=null&specialitySnIndex==null){
					        	  menu3.show({
							            top: params.event.offsetY + os.top, 
							            left: params.event.offsetX + os.left 
							      });
					          }
					    	
					          if(inconformityLevelSnIndex!=null&specialitySnIndex!=null){
					        	  menu4.show({
							            top: params.event.offsetY + os.top, 
							            left: params.event.offsetX + os.left 
							      });
					          } 
					          
						 }
				      else{
			        	  if(inconformityLevelSnIndex==null & specialitySnIndex==null){
				        	  menu5.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }
				          if(inconformityLevelSnIndex==null&specialitySnIndex!=null){
				        	  menu6.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }
				          if(inconformityLevelSnIndex!=null&specialitySnIndex==null){
				        	  menu7.show({
						            top: params.event.offsetY + os.top, 
						            left: params.event.offsetX + os.left 
						      });
				          }
				          if(inconformityLevelSnIndex!=null&specialitySnIndex!=null){
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
	        url: "${pageContext.request.contextPath}/report/detailAction",
	        rownumbers:true,
	        fitColumns:true,
            toolbar:'#exportDiv',
	        singleSelect:true,
	        fit:true,
	        nowrap:false,
	        pageSize:10,
	        pageList: [5, 10,15,20],
	        columns:[[ 
	                  {field:'checkedDepartmentImplType',title:'贯标单位',width:50,align:'center'},
			          {field:'dpartmentName',title:'被检部门',width:30,align:'center'},
			          {field:'checkDateTime',title:'检查时间',width:40,align:'center'},    
			          {field:'checkLocation',title:'检查地点',width:70,align:'center'},    
			          {field:'problemDescription',title:'问题描述',width:100,align:'center'},
	                  {field:'correctType',title:'整改类型',width:30,align:'center'},
			          {field:'correctDeadline',title:'整改期限',width:40,align:'center'},
			          {field:'correctProposal',title:'整改建议',width:60,align:'center'},
			          {field:'hasCorrectConfirmed',title:'整改确认',width:25,align:'center',
			        	  styler: function(value,row,index){
				  				if (value == "已整改确认"){
				  					return 'background-color:#1DA600;';
				  				}else{
				  					return 'background-color:#ff5544;';
				  				}
						  }
				      },
			          {field:'hasReviewed',title:'复查',width:30,align:'center',
			        	  styler: function(value,row,index){
			        		  	if (value == "已复查"){
				  					return 'background-color:#1DA600;';
				  				}else{
				  					return 'background-color:#ff5544;';
				  				}
							}
				      },
			          {field:'hasCorrectFinished',title:'整改完成',width:25,align:'center',
			        	  styler: function(value,row,index){
			        		  if( row.correctDeadline != null && row.correctDeadline < new Date() ){
			        			  return 'background-color:#ff5544;';
			        		  }else{
			        		  	if (value == "已整改完成" ){
				  					return 'background-color:#1DA600;';
				  				}else{
				  					return '';
				  				}
			        		  }
						  }
				      },
			          {field:'attachments',title:'附件',width:25,align:'center',
			        	  formatter: function(value,row,index){
				  				if (value!=0){
				  					return "<a href='javascript:;' onclick=showAttachments()>附件["+value+"]</a>";
				  				} else {
				  					return "<a href='javascript:;' onclick=showAttachments()>附件[0]</a>";
				  				}
			  		  }},
			          {field:'checkType',title:'检查类型',width:65,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  checkTypeValue[index]=value;
			        		  }else{
			        			  checkTypeValue[index]="无";
			        		  }
			  		  }},
			          {field:'systemAudit',title:'审核类型',width:65,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  systemAuditValue[index]=value;
			        		  }else{
			        			  systemAuditValue[index]="无";
			        		  }
			  		  }},
			          {field:'checkerFrom',title:'检查人来自',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  checkerFromValue[index]=value;
			        		  }else{
			        			  checkerFromValue[index]="无";
			        		  }
			  		  }},    
			          {field:'inconformityItemSn',title:'不符合项编号',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  inconformityItemSnValue[index]=value;
			        		  }else{
			        			  inconformityItemSnValue[index]="无";
			        		  }
			  		  }},
			          {field:'checkers',title:'检查人',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  checkersValue[index]=value;
			        		  }else{
			        			  checkersValue[index]="无";
			        		  }
			  		  }},        
			          {field:'inconformityItemNature',title:'不符合项性质',width:120,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  inconformityItemNatureValue[index]=value;
			        		  }else{
			        			  inconformityItemNatureValue[index]="无";
			        		  }
			  		  }},
			          {field:'machine',title:'机',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  machineValue[index]=value;
			        		  }else{
			        			  machineValue[index]="无";
			        		  }
			  		  }},
			          {field:'standardIndex',title:'指标',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        		  		standardIndexValue[index]=value;
			        		  }else{
									standardIndexValue[index]="无";
			        		  }
			  		  }},
			          {field:'deductPoints',title:'扣分',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  deductPointsValue[index]=value;
			        		  }else{
			        			  deductPointsValue[index]="无";
			        		  }
			  		  }},
			          {field:'hazrd',title:'对应的危险源',width:120,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  hazardValue[index]=value;
			        		  }else{
			        			  hazardValue[index]="无";
			        		  }
			  		  }},
			          {field:'speciality',title:'所属专业',width:100,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  specialityValue[index]=value;
			        		  }else{
			        			  specialityValue[index]="无";
			        		  }
			  		  }},
			          {field:'inconformityLevel',title:'不符合项等级',width:120,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  inconformityLevelValue[index]=value;
			        		  }else{
			        			  inconformityLevelValue[index]="无";
			        		  }
			  		  }},
			          {field:'correctPrincipal',title:'整改负责人',width:120,align:'center',hidden:true,
			        	  formatter: function(value,row,index){
			        		  if(value!=null && value!=""){
			        			  correctPrincipalValue[index]=value;
			        		  }else{
			        			  correctPrincipalValue[index]="无";
			        		  }
			  		  }}
			     ]],
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
					'<td style="width:90px;text-align:center">'+'不符合项编号：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + inconformityItemSnValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'不符合项性质：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + inconformityItemNatureValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'不符合项等级：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + inconformityLevelValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'检查类型：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + checkTypeValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:90px;text-align:center">'+'检查人：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + checkersValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'整改负责人：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + correctPrincipalValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'扣分项：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + deductPointsValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'审核类型：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + systemAuditValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:90px;text-align:center">'+'危险源：' + '</td>' + 
					'<td style="width:200px;text-align:center">' + 
					'<p>' + hazardValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'专业：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + specialityValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'机器：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + machineValue[rowIndex] + '</p>' + 
					'</td>' +
					'<td style="width:90px;text-align:center">'+'检查人来自：' + '</td>' + 
					'<td style="width:100px;text-align:center">' + 
					'<p>' + checkerFromValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr><tr>' +
					
					'<td style="width:50px;text-align:center">'+'指标：' + '</td>' + 
					'<td style="width:100px;text-align:center" colspan="7">' + 
					'<p>' + standardIndexValue[rowIndex] + '</p>' + 
					'</td>' +
					'</tr></table>'; 
					}
	   });

	});
	<!--展示指标详情-附件-->
    function showAttachments(){
    	$('#winAttachments').window({
			title:"附件详情",
			width:400,
			height:300,
			content:'<iframe src="${pageContext.request.contextPath}/inconformity/item/query/inconformity-query/compositive_show" frameborder="0" width="100%" height="100%" />'
		});
    };
	<!--显示隐藏日期选择器-->
	function timebtnchange(){
		$('#daydiv').hide();
		$("#container" ).hide();
		$("#halfyeardiv" ).hide();
		$("#yeardiv" ).hide();
		$("#monthdiv" ).hide();
		$("#seasondiv" ).hide();
	};
	function loadechart(){
		//判断选择的哪个时间段的报表拼接不同的下拉框值
		if(showtime==3){
			begin=year+'-'+month+'-01 00:00:00';
			end=year+'-'+month+'-31 23:59:59'
		}
		if(showtime==4){
			begin=year+'-'+season+'-01 00:00:00';
			var x=parseInt(season)+2;
			end=year+'-'+x+'-31 23:59:59'
		}
		if(showtime==5){
			begin=year+'-'+halfyear+'-01 00:00:00';
			var x=parseInt(halfyear)+5;
			end=year+'-'+x+'-31 23:59:59'
		}
		if(showtime==6){
			begin=year+'-01-01 00:00:00';
			end=year+'-12-31 23:59:59'
		}
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
 						$("#typebtns").append("<a class='btn btn-default' data-options='plain:true' data='" + rv[i].value+"'>"+ rv[i].text+"</a>");					
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
			if(item=='专业'){
				itemSn="2";					 
				$('#xtypebtns a').removeClass("active");
			    $('#spbtn').addClass("active");
			}
	    	if(item=='危险等级'){
				itemSn="3";
				$('#xtypebtns a').removeClass("active");
			    $('#levelbtn').addClass("active");
	    	}
	    	if(item=='查看明细'){
	    		//显示对应的不符合项数据
	    		itemSn1=0;
    			$('#win').window('open');
	    		$('#dg').datagrid('load',{
		    		departmentTypeSn:departmentTypeSn,
		    		begin:begin,
		    		end:end,
		    		specialitySnIndex:specialitySnIndex,
		    		inconformityLevelSnIndex:inconformityLevelSnIndex,
		    		departmentSnIndex:departmentSnIndex,
		    		departmentTypeSn:departmentTypeSn
    			});
	    	} 
  		}
        if(itemSn1!='0'){
			$.get('${pageContext.request.contextPath}/report/reportReport',{departmentTypeSn:departmentTypeSn,begin:begin,end:end,specialitySnIndex:specialitySnIndex,inconformityLevelSnIndex:inconformityLevelSnIndex,departmentSnIndex:departmentSnIndex,itemSn:itemSn,clickEchart:clickEchart},function (data) {
			     // 填入数据
			     specialitySns=data.specialitySns;
			     inconformityLevelSns=data.inconformityLevelSns;
			     departmentSns=data.departmentSns;
			     departmentNames=data.departmentName;
			     type=data.type;
			     children=data.children;
			     //清空判断是否下钻的选择
			     clickEchart="";
			     myChart.setOption({
			    	 grid:{
			    			x:50,
			    			y:50,
			    			x2:50,
			    			y2: 150			    			
			    	 },
			         xAxis: {
			        	data: data.departmentName,
			        	axisLabel: {rotate:0, show: true, margin:2,interval:0,
			        		formatter:function(val){
			        			if(val!=null){
				        		    return val.split("").join("\n");
			        			}
		                        //只显示前两个字符，鼠标放上去显示全
		                        //return value.substr(0,6)
			        		}	
			        	}
			         },	
			         series: [{
			             name: '按照部门不符合项',
			             data:  data.inconformityItemCount
			         }]
			     });
			   //用于使chart自适应高度和宽度
				window.onresize = myChart.resize;
		},'json'); 
	}
}


//点击导出
function exportexcel(){ 
	var url ="${pageContext.request.contextPath}/report/exportexcelReport?itemSn="+itemSn+"&begin="+begin+"&end="+end+"&specialitySnIndex="+specialitySnIndex+"&inconformityLevelSnIndex="+inconformityLevelSnIndex+"&departmentSnIndex="+departmentSnIndex+"&departmentTypeSn="+departmentTypeSn+"&clickEchart="+clickEchartBefore+"&showtime="+showtime;
	$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
};
	</script>
</body>
</html>