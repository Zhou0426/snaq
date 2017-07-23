package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.service.DepartmentService;
import cn.jagl.aq.service.DepartmentTypeService;
import cn.jagl.aq.service.PersonService;
import cn.jagl.aq.service.StandardIndexService;
import cn.jagl.aq.service.StandardService;
import cn.jagl.aq.service.UnsafeConditionService;

public class UnsafeConditionQueryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//获取部门树形列表的id
	private String pag;//赋值传送前台的参数
	private String departmentTypeSn;//部门类型编号
	private String dpmt;//查询用的部门类型
	private String checkDeptSn;//检查部门
	private String departmentSn;//部门编号
	private String standardSn;//标准编号
	private String indexSn;//指标id
	private String qSpecialitySn;//接收专业拼接的数组
	private String riskLevel;//风险等级
	private Timestamp beginTime;//开始时间
	private Timestamp endTime;//结束时间
	private String timeData;//时间段
	private String checkType;//检查类型
	private String inconformityLevel;//不符合项等级
	private String checkerFrom;//检查人来自
	private String inconformityItemNature;//不符合项性质
	private String checkers;//检查人
	private String personId;//根据个人Id查询隐患
	private String correctPrincipal;//整改负责人
	private String hasCorrectConfirmed;//已整改确认
	private String hasReviewed;//已复查
	private String hasCorrectFinished;//已整改完成
	private boolean checked;//是否是关键指标
	private int page;//页码
	private int rows;//条数
	private String q;//模糊搜索发送的参数值
	private InputStream inputStream;//输出流
	private String fileName;//下载文件名
	private Map<String, Object> mapArray=new HashMap<String, Object>();
	
	
	@Resource(name="standardService")
	private StandardService standardService;//标准
	@Resource(name="standardIndexService")
	private StandardIndexService standardindexService;//指标
	@Resource(name="departmentService")
	private DepartmentService departmentService;//查询范围，部门
	@Resource(name="departmentTypeService")
	private DepartmentTypeService departmentTypeService;//部门类型是否为贯标单位获取
	@Resource(name="unsafeConditionService")
	private UnsafeConditionService unsafeConditionService;//不符合标准查询
	@Resource(name="personService")
	private PersonService personService;//人员
	
	
	
	
	public String getCheckDeptSn() {
		return checkDeptSn;
	}
	public void setCheckDeptSn(String checkDeptSn) {
		this.checkDeptSn = checkDeptSn;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, Object> getMapArray() {
		return mapArray;
	}
	public void setMapArray(Map<String, Object> mapArray) {
		this.mapArray = mapArray;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getHasCorrectConfirmed() {
		return hasCorrectConfirmed;
	}
	public void setHasCorrectConfirmed(String hasCorrectConfirmed) {
		this.hasCorrectConfirmed = hasCorrectConfirmed;
	}
	public String getHasReviewed() {
		return hasReviewed;
	}
	public void setHasReviewed(String hasReviewed) {
		this.hasReviewed = hasReviewed;
	}
	public String getHasCorrectFinished() {
		return hasCorrectFinished;
	}
	public void setHasCorrectFinished(String hasCorrectFinished) {
		this.hasCorrectFinished = hasCorrectFinished;
	}
	public String getInconformityLevel() {
		return inconformityLevel;
	}
	public void setInconformityLevel(String inconformityLevel) {
		this.inconformityLevel = inconformityLevel;
	}
	public String getqSpecialitySn() {
		return qSpecialitySn;
	}
	public void setqSpecialitySn(String qSpecialitySn) {
		this.qSpecialitySn = qSpecialitySn;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(String checkerFrom) {
		this.checkerFrom = checkerFrom;
	}
	public String getInconformityItemNature() {
		return inconformityItemNature;
	}
	public void setInconformityItemNature(String inconformityItemNature) {
		this.inconformityItemNature = inconformityItemNature;
	}
	public String getCheckers() {
		return checkers;
	}
	public void setCheckers(String checkers) {
		this.checkers = checkers;
	}
	public String getCorrectPrincipal() {
		return correctPrincipal;
	}
	public void setCorrectPrincipal(String correctPrincipal) {
		this.correctPrincipal = correctPrincipal;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getQSpecialitySn() {
		return qSpecialitySn;
	}
	public void setQSpecialitySn(String qSpecialitySn) {
		this.qSpecialitySn = qSpecialitySn;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getTimeData() {
		return timeData;
	}
	public void setTimeData(String timeData) {
		this.timeData = timeData;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getDpmt() {
		return dpmt;
	}
	public void setDpmt(String dpmt) {
		this.dpmt = dpmt;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	//根据树形菜单选中项加载单位类型
	public String unitType() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
		List<DepartmentType> departmentTypes=new ArrayList<DepartmentType>();
		departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
		JSONArray jsonArray=new JSONArray();
		if(departmentTypes==null||departmentTypes.size()==0){ 			//部门类型为空，该部门为部门类型下的子部门
			
		}else{
			for(DepartmentType de:departmentTypes){
				JSONObject jo=new JSONObject();
				jo.put("text", de.getDepartmentTypeName());
				jo.put("value", de.getDepartmentTypeSn());
				jsonArray.put(jo);
			}
		}
		out.print(jsonArray.toString());
		out.flush();
		out.close();
		return SUCCESS;
	}

		/**
		 * 不符合项查询--根据标准查询指标
		 * @author 蒋宇森
		 * @throws IOException 
		 */
			public String standardindexQuery() throws IOException{
				HttpServletResponse response=ServletActionContext.getResponse();  
		        response.setContentType("text/html");  
		        response.setContentType("text/plain; charset=utf-8");
		        List<StandardIndex> indexList=null;
		        PrintWriter out;  
		        out = response.getWriter();
		        String hql="";
		        JSONArray tree=new JSONArray();
				if(id!=null){
					hql="FROM StandardIndex s where s.parent.indexSn='"+id+"' AND deleted=false";
				}else{
					hql="FROM StandardIndex s where s.standard.standardSn='"+standardSn+"' AND s.parent is null  AND deleted=false";
				}
				indexList=standardindexService.getPart(hql);
				for(StandardIndex standardIndex:indexList){
					JSONObject jo=new JSONObject();
					jo.put("id", standardIndex.getIndexSn());
					if(standardIndex.getIndexName()!=null && standardIndex.getIndexName().trim().length()>0){
						jo.put("text", standardIndex.getIndexSn()+standardIndex.getIndexName());
					}else{
						jo.put("text", standardIndex.getIndexSn());
					}
					if(standardIndex.getChildren().size()>0){
						jo.put("state","closed");
					}else{
						jo.put("state", "open");
					}
					tree.put(jo);
				}
				out.print(tree.toString());
		        out.flush(); 
		        out.close();
				return SUCCESS;
			}
		/**
		 * datagrid数据传输
		 * 接收到的参数：
		 * qSpecialitySn：array
	       inconformityLevel:不符合项等级
	       riskLevel:风险等级
	       departmentSn
	       indexSn
	       timeData
	       beginTime
	       endTime
		 * @throws IOException
		 */
		public String showData() throws IOException{
			if(departmentSn!=null && !"".equals(departmentSn)){
				String str="";
				if(dpmt !=null && !"".equals(dpmt) && departmentSn!=null && !"".equals(departmentSn)){
		    		  str=departmentService.getDownDepartmentByDepartmentType(departmentSn,dpmt);
		    		  str=str.replaceAll("i.departmentSn", "i.checkedDepartment.departmentSn");
		    	}
				mapArray=unsafeConditionService.showData(checkDeptSn,departmentSn,str,indexSn,standardSn,inconformityLevel,qSpecialitySn,
						riskLevel,checkType,checkerFrom,inconformityItemNature,correctPrincipal,
						hasCorrectConfirmed,hasReviewed,hasCorrectFinished,timeData,checkers,
						pag,page,rows,beginTime,endTime,checked);
			}else{
				mapArray.put("total", 0);// total键 存放总记录数，必须的
				mapArray.put("rows", "");// rows键 存放每页记录 list
			}
			return SUCCESS;
		}
		/**
		 * 查询被检部门在本指标下的隐患数据
		 * @return
		 */
		public String showStandardIndexData(){
			String str="";
			mapArray = unsafeConditionService.showData(checkDeptSn,departmentSn,
					str,indexSn,standardSn,inconformityLevel,qSpecialitySn,
					riskLevel,checkType,checkerFrom,inconformityItemNature,correctPrincipal,
					hasCorrectConfirmed,hasReviewed,hasCorrectFinished,timeData,checkers,
					pag,page,rows,beginTime,endTime,checked);
			return SUCCESS;
		}
		/**
		 * 检查任务点击查询该人员本月的检查任务
		 */
		public String showDataPersonal() throws IOException{
				String str="";
				//时间为本周
				if((beginTime == null || "".equals(beginTime)) && (endTime == null || "".equals(endTime)) && (timeData ==null || "".equals(timeData))){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    		Calendar cal =Calendar.getInstance();
		    		
		    		cal.set(Calendar.DAY_OF_MONTH,1);		//设置为1号,当前日期既为本月第一天
		    		beginTime=Timestamp.valueOf(df.format(cal.getTime())+" 00:00:00");
	    			  
	    			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
	    			endTime=Timestamp.valueOf(df.format(cal.getTime())+" 00:00:00");
				}
				mapArray=unsafeConditionService.showData(checkDeptSn,departmentSn,str,indexSn,standardSn,inconformityLevel,qSpecialitySn,
						riskLevel,checkType,checkerFrom,inconformityItemNature,correctPrincipal,
						hasCorrectConfirmed,hasReviewed,hasCorrectFinished,timeData,checkers,
						pag,page,rows,beginTime,endTime,checked);
			
			return SUCCESS;
		}
		
		/**
		 * 不符合项查询--展示人员
		 * @author 蒋宇森
		 * @throws IOException 
		 */
		public String showPerson() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out= response.getWriter();
	        String hql="";
	        JSONArray jsonArray=new JSONArray();
	        if(q!=null&&q.trim().length()>0){
	        	hql="select p from Person p where (p.personId like '%"+q+"%' or p.personName like '%"+q+"%') AND p.department.departmentSn LIKE '"+departmentSn+"%'";
		        List<Person> list=personService.findByPage(hql, 1, 30);
		        for(Person de:list){
					JSONObject jo=new JSONObject();
					jo.put("personId", de.getPersonId());
					jo.put("personName", de.getPersonName());
					jsonArray.put(jo);
				}
	        }
	        out.print(jsonArray.toString());
	        out.flush(); 
	        out.close();
			return SUCCESS;
		}
		public String showPersonAll() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out= response.getWriter();
	        String hql="";
	        JSONArray jsonArray=new JSONArray();
	        if(q!=null&&q.trim().length()>0){
	        	hql="select p from Person p where p.personId like '%"+q+"%' or p.personName like '%"+q+"%'";
		        List<Person> list=personService.findByPage(hql, 1, 50);
		        for(Person de:list){
					JSONObject jo=new JSONObject();
					jo.put("personId", de.getPersonId());
					jo.put("personName", de.getPersonName());
					jsonArray.put(jo);
				}
	        }
	        out.print(jsonArray.toString());
	        out.flush(); 
	        out.close();
			return SUCCESS;
		}
		/**
		 * 查询我的隐患
		 * @return
		 */
		public String myUnsafeCondition(){
			String personId = (String) ServletActionContext.getRequest().getSession().getAttribute("personId");
			mapArray = unsafeConditionService.myUnsafeCondition(personId, page, rows);
			return SUCCESS;
		}
		//导出隐患查询内容
		public String exportQueryUnsafeCondition() throws IOException{
			String str="";
			Map<String, Object> map=new HashMap<String, Object>();
			if(dpmt !=null && !"".equals(dpmt) && departmentSn!=null && !"".equals(departmentSn)){
	    		  str=departmentService.getDownDepartmentByDepartmentType(departmentSn,dpmt);
	    		  str=str.replaceAll("i.departmentSn", "i.checkedDepartment.departmentSn");
	    	}
			map=unsafeConditionService.showData(checkDeptSn,departmentSn,str,indexSn,standardSn,inconformityLevel,qSpecialitySn,
					riskLevel,checkType,checkerFrom,inconformityItemNature,correctPrincipal,
					hasCorrectConfirmed,hasReviewed,hasCorrectFinished,timeData,checkers,
					pag,page,rows,beginTime,endTime, checked);
			
			String name="隐患查询数据导出";
			String titles="不符合项编号,不符合项性质,不符合项等级,贯标单位,被检部门,"
					+ "检查类型,审核类型,检查人,检查人来自,检查时间,检查地点,问题描述,"
					+ "指标,扣分,对应危险源,所属专业,机,风险等级,录入人,整改负责人,"
					+ "整改类型,整改期限,整改建议,整改确认,整改确认时间,复查,整改完成,录入时间";
			String cellValue="inconformityItemSn,inconformityItemNature,inconformityLevel,"
					+ "checkedDepartmentImplType,checkedDepartment,checkType,systemAudit,"
					+ "checkers,checkerFrom,checkDateTime,checkLocation,problemDescription,"
					+ "standardIndex,deductPoints,hazrd,speciality,machine,riskLevel,editor,"
					+ "correctPrincipal,correctType,correctDeadline,correctProposal,"
					+ "hasCorrectConfirmed,confirmTime,hasReviewed,hasCorrectFinished,editorDateTime";
            inputStream = this.listToExcel(map, name, titles, cellValue);
			fileName = URLEncoder.encode(name+".xls","UTF-8");
			return LOGIN;
		}
		private String specialitySnIndex;
		private String inconformityLevelSnIndex;
		private String departmentSnIndex;
		private String begin;
		private String end;
		/**
		 * 隐患报表导出
		 * @return
		 * @throws IOException 
		 */
		public String reportExcel() throws IOException{
			List<UnsafeCondition> list=new ArrayList<UnsafeCondition>();
			StringBuffer hqls = new StringBuffer();				
			StringBuffer hqlc = new StringBuffer();
			String str0="";
	        if(departmentTypeSn != null && !"".equals(departmentTypeSn)){
	        	str0 = departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
	        	hqls.append ( "select p FROM UnsafeCondition p WHERE"
	        			+ " p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
	        			+ " and p.checkedDepartment.departmentType.departmentTypeSn in "+str0+""
	        			+ " and p.deleted=false  and p.checkDateTime>'"+begin+"'"
	        			+ " and p.checkDateTime<'"+end+"'");
	        	hqlc.append ( "select Count(*) FROM UnsafeCondition p WHERE"
	        			+ " p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
	        			+ " and p.checkedDepartment.departmentType.departmentTypeSn in "+str0+""
	        			+ " and p.deleted=false and p.checkDateTime>'"+begin+"'"
	        			+ " and p.checkDateTime<'"+end+"'");
	        }else{
	        	hqls.append ( "select p FROM UnsafeCondition p WHERE"
	        			+ " p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
	        			+ " and p.deleted=false  and p.checkDateTime>'"+begin+"'"
	        			+ " and p.checkDateTime<'"+end+"'");
	        	hqlc.append ( "select Count(*) FROM UnsafeCondition p WHERE"
	        			+ " p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%'"
	        			+ " and p.deleted=false and p.checkDateTime>'"+begin+"'"
	        			+ " and p.checkDateTime<'"+end+"'");
	        }
			 if(specialitySnIndex.length()>0){
					hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
					hqlc.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
			 }	
			if(inconformityLevelSnIndex.length()>0){			  
				hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
				hqlc.append(" AND p.inconformityLevel="+inconformityLevelSnIndex);	
			}
			hqls.append(" ORDER BY p.checkDateTime desc");	
			String hql = hqls.toString();	
			String hqlcc = hqlc.toString();			
			long total = unsafeConditionService.countHql(hqlcc);
			list = unsafeConditionService.query(hql,page,rows);		
			net.sf.json.JSONArray array = new net.sf.json.JSONArray();
			for(UnsafeCondition unsafeCondition:list){
				net.sf.json.JSONObject jo = new net.sf.json.JSONObject();
							
				if(unsafeCondition.getCheckType()!=null){
					jo.put("checkType",unsafeCondition.getCheckType().toString());
				}
				if(unsafeCondition.getCheckerFrom()!=null){
					jo.put("checkerFrom",unsafeCondition.getCheckerFrom().toString());
				}
				jo.put("inconformityItemSn",unsafeCondition.getInconformityItemSn());
				if(unsafeCondition.getCheckDateTime()!=null){			
					jo.put("checkDateTime",unsafeCondition.getCheckDateTime().toString());
				}
				if(unsafeCondition.getCheckLocation()!=null){
					jo.put("checkLocation",unsafeCondition.getCheckLocation().toString());
				}
				jo.put("inconformityItemNature", unsafeCondition.getInconformityItemNature());
				if(unsafeCondition.getMachine()!=null){
					jo.put("machine", unsafeCondition.getMachine().getManageObjectName());
		  		}
				if(unsafeCondition.getCheckedDepartment()!=null){
					jo.put("checkedDepartment",unsafeCondition.getCheckedDepartment().getDepartmentName());
	  			  	jo.put("checkedDepartmentImplType", unsafeCondition.getCheckedDepartment().getImplDepartmentName());
				}
				if(unsafeCondition.getStandardIndex()!=null){
					jo.put("standardIndex", unsafeCondition.getStandardIndex().getIndexName());
				}
				if(unsafeCondition.getSystemAudit()!=null){
					jo.put("systemAudit",unsafeCondition.getSystemAudit().getSystemAuditType());
	  		  	}
				jo.put("problemDescription",unsafeCondition.getProblemDescription());
				jo.put("deductPoints", unsafeCondition.getDeductPoints());
				jo.put("correctType", unsafeCondition.getCorrectType());
				if(unsafeCondition.getCorrectDeadline()!=null){
					jo.put("correctDeadline",unsafeCondition.getCorrectDeadline().toString());
				}
				if(unsafeCondition.getHazrd()!=null){
					jo.put("hazrd",unsafeCondition.getHazrd().getHazardDescription());
				}
				if(unsafeCondition.getSpeciality()!=null){
					jo.put("speciality",unsafeCondition.getSpeciality().getSpecialityName());
				}
		  		if(unsafeCondition.getEditorDateTime() != null){
		  			jo.put("editorDateTime", unsafeCondition.getEditorDateTime().toString());
		  		}else{
		  			jo.put("editorDateTime", "无");
		  		}
		  		if(unsafeCondition.getConfirmTime() != null){
		  			jo.put("confirmTime", unsafeCondition.getConfirmTime().toString());
		  		}else{
		  			jo.put("confirmTime", "无");
		  		}
				jo.put("inconformityLevel", unsafeCondition.getInconformityLevel());
				if(unsafeCondition.getCurrentRiskLevel()!=null){
		  			  jo.put("riskLevel", unsafeCondition.getCurrentRiskLevel().toString());
		  		  }else{
		  			  jo.put("riskLevel", "");
		  		  }
				if(unsafeCondition.getCorrectPrincipal()!=null){
					jo.put("correctPrincipal",unsafeCondition.getCorrectPrincipal().getPersonName().toString());
				}
				jo.put("correctProposal",unsafeCondition.getCorrectProposal());
				jo.put("attachments",unsafeCondition.getAttachments().size());
				if(unsafeCondition.getCheckers() != null && unsafeCondition.getCheckers().size() != 0){
					String str="";
					for(Person pe:unsafeCondition.getCheckers()){
						str+=pe.getPersonName()+";";
					}
					str=str.substring(0,str.length()-1);
					jo.put("checkers",str);
				}
				if(unsafeCondition.getEditor()!=null){
					jo.put("editor", unsafeCondition.getEditor().getPersonName());
				}else{
					jo.put("editor", "无");
				}
				if(unsafeCondition.getHasCorrectConfirmed()!=null){
					if(unsafeCondition.getHasCorrectConfirmed()!=true){
						jo.put("hasCorrectConfirmed", "未整改确认");
					}else{
						jo.put("hasCorrectConfirmed", "已整改确认");
					}
				}
				if(unsafeCondition.getHasReviewed()!=null){
					if(unsafeCondition.getHasReviewed()!=true){
						jo.put("hasReviewed","未复查");
					}else{
						jo.put("hasReviewed","已复查");
					}
				}
				if(unsafeCondition.getHasCorrectFinished()!=null){
					if(unsafeCondition.getHasCorrectFinished()!=true){
						jo.put("hasCorrectFinished", "未整改完成");
					}else{
						jo.put("hasCorrectFinished", "已整改完成");
					}
				}
				array.add(jo);			
			}
			
			//String str="{\"total\":"+total+",\"rows\":"+array+"}";
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", total);// total键 存放总记录数，必须的
	        json.put("rows", array);// rows键 存放每页记录 list
	        String name="隐患详情数据导出";
			String titles="不符合项编号,不符合项性质,不符合项等级,贯标单位,被检部门,"
					+ "检查类型,审核类型,检查人,检查人来自,检查时间,检查地点,问题描述,"
					+ "指标,扣分,对应危险源,所属专业,机,风险等级,录入人,整改负责人,"
					+ "整改类型,整改期限,整改建议,整改确认,整改确认时间,复查,整改完成,录入时间";
			String cellValue="inconformityItemSn,inconformityItemNature,inconformityLevel,"
					+ "checkedDepartmentImplType,checkedDepartment,checkType,systemAudit,"
					+ "checkers,checkerFrom,checkDateTime,checkLocation,problemDescription,"
					+ "standardIndex,deductPoints,hazrd,speciality,machine,riskLevel,editor,"
					+ "correctPrincipal,correctType,correctDeadline,correctProposal,"
					+ "hasCorrectConfirmed,confirmTime,hasReviewed,hasCorrectFinished,editorDateTime";
            inputStream = this.listToExcel(json, name, titles, cellValue);
			fileName = URLEncoder.encode(name+".xls","UTF-8");
			return LOGIN;
		}
		/**
		 * 根据map集合导出至excel
		 * @param map
		 * @return 
		 * @throws IOException 
		 */
		@SuppressWarnings("deprecation")
		private InputStream listToExcel(Map<String, Object> map, 
				String name, String titles, String cellValue) throws IOException{
			
			
			// 第一步，创建一个workbook，对应一个Excel文件
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	        HSSFSheet hssfSheet = workbook.createSheet(name);
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // 第四步，创建单元格，并设置值表头 设置表头居中
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();//表头样式
	        HSSFCellStyle hssfCellStyle2 = workbook.createCellStyle();//内容样式

	        hssfCellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        //居中样式
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell;
	        for(int i=0;i<titles.split(",").length;i++){
	        	hssfSheet.setColumnWidth(i, 70*70);
	        	hssfCell = hssfRow.createCell(i);
		        hssfCell.setCellValue(titles.split(",")[i]);//列名2
		        hssfCell.setCellStyle(hssfCellStyle);
		        hssfCell.setCellStyle(hssfCellStyle2);
	        }
	        net.sf.json.JSONArray array=net.sf.json.JSONArray.fromObject(map.get("rows"));
	        // 第五步，写入实体数据
	        for (int i = 0; i < array.size(); i++) {
	        	hssfRow = hssfSheet.createRow(i+1);
	        	
    			// 第六步，创建行、单元格，并设置值
	        	net.sf.json.JSONObject jo=array.getJSONObject(i);
	        	String[] data=cellValue.split(",");
	        	for(int j=0;j<data.length;j++){
	        		hssfRow.createCell(j);
	        		hssfRow.getCell(j).setCellStyle(hssfCellStyle2);
	        		if(jo.containsKey(data[j])){
	        			hssfRow.getCell(j).setCellValue(jo.getString(data[j]));
	        		}
	        	}
	        }
	        // 第七步，将文件存到指定位置
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        	workbook.write(fout);
        	workbook.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
	            
	        return is;
		}
		public StandardService getStandardService() {
			return standardService;
		}
		public void setStandardService(StandardService standardService) {
			this.standardService = standardService;
		}
		public StandardIndexService getStandardindexService() {
			return standardindexService;
		}
		public void setStandardindexService(StandardIndexService standardindexService) {
			this.standardindexService = standardindexService;
		}
		public DepartmentService getDepartmentService() {
			return departmentService;
		}
		public void setDepartmentService(DepartmentService departmentService) {
			this.departmentService = departmentService;
		}
		public DepartmentTypeService getDepartmentTypeService() {
			return departmentTypeService;
		}
		public void setDepartmentTypeService(DepartmentTypeService departmentTypeService) {
			this.departmentTypeService = departmentTypeService;
		}
		public UnsafeConditionService getUnsafeConditionService() {
			return unsafeConditionService;
		}
		public void setUnsafeConditionService(UnsafeConditionService unsafeConditionService) {
			this.unsafeConditionService = unsafeConditionService;
		}
		public PersonService getPersonService() {
			return personService;
		}
		public void setPersonService(PersonService personService) {
			this.personService = personService;
		}
		public String getSpecialitySnIndex() {
			return specialitySnIndex;
		}
		public void setSpecialitySnIndex(String specialitySnIndex) {
			this.specialitySnIndex = specialitySnIndex;
		}
		public String getInconformityLevelSnIndex() {
			return inconformityLevelSnIndex;
		}
		public void setInconformityLevelSnIndex(String inconformityLevelSnIndex) {
			this.inconformityLevelSnIndex = inconformityLevelSnIndex;
		}
		public String getDepartmentSnIndex() {
			return departmentSnIndex;
		}
		public void setDepartmentSnIndex(String departmentSnIndex) {
			this.departmentSnIndex = departmentSnIndex;
		}
		public String getBegin() {
			return begin;
		}
		public void setBegin(String begin) {
			this.begin = begin;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		
		
		
		
		
		
		
		
		
		
}
