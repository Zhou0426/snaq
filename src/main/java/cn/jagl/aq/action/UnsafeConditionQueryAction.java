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
	private String id;//��ȡ���������б��id
	private String pag;//��ֵ����ǰ̨�Ĳ���
	private String departmentTypeSn;//�������ͱ��
	private String dpmt;//��ѯ�õĲ�������
	private String checkDeptSn;//��鲿��
	private String departmentSn;//���ű��
	private String standardSn;//��׼���
	private String indexSn;//ָ��id
	private String qSpecialitySn;//����רҵƴ�ӵ�����
	private String riskLevel;//���յȼ�
	private Timestamp beginTime;//��ʼʱ��
	private Timestamp endTime;//����ʱ��
	private String timeData;//ʱ���
	private String checkType;//�������
	private String inconformityLevel;//��������ȼ�
	private String checkerFrom;//���������
	private String inconformityItemNature;//������������
	private String checkers;//�����
	private String personId;//���ݸ���Id��ѯ����
	private String correctPrincipal;//���ĸ�����
	private String hasCorrectConfirmed;//������ȷ��
	private String hasReviewed;//�Ѹ���
	private String hasCorrectFinished;//���������
	private boolean checked;//�Ƿ��ǹؼ�ָ��
	private int page;//ҳ��
	private int rows;//����
	private String q;//ģ���������͵Ĳ���ֵ
	private InputStream inputStream;//�����
	private String fileName;//�����ļ���
	private Map<String, Object> mapArray=new HashMap<String, Object>();
	
	
	@Resource(name="standardService")
	private StandardService standardService;//��׼
	@Resource(name="standardIndexService")
	private StandardIndexService standardindexService;//ָ��
	@Resource(name="departmentService")
	private DepartmentService departmentService;//��ѯ��Χ������
	@Resource(name="departmentTypeService")
	private DepartmentTypeService departmentTypeService;//���������Ƿ�Ϊ��굥λ��ȡ
	@Resource(name="unsafeConditionService")
	private UnsafeConditionService unsafeConditionService;//�����ϱ�׼��ѯ
	@Resource(name="personService")
	private PersonService personService;//��Ա
	
	
	
	
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
	//�������β˵�ѡ������ص�λ����
	public String unitType() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
		List<DepartmentType> departmentTypes=new ArrayList<DepartmentType>();
		departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
		JSONArray jsonArray=new JSONArray();
		if(departmentTypes==null||departmentTypes.size()==0){ 			//��������Ϊ�գ��ò���Ϊ���������µ��Ӳ���
			
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
		 * ���������ѯ--���ݱ�׼��ѯָ��
		 * @author ����ɭ
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
		 * datagrid���ݴ���
		 * ���յ��Ĳ�����
		 * qSpecialitySn��array
	       inconformityLevel:��������ȼ�
	       riskLevel:���յȼ�
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
				mapArray.put("total", 0);// total�� ����ܼ�¼���������
				mapArray.put("rows", "");// rows�� ���ÿҳ��¼ list
			}
			return SUCCESS;
		}
		/**
		 * ��ѯ���첿���ڱ�ָ���µ���������
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
		 * �����������ѯ����Ա���µļ������
		 */
		public String showDataPersonal() throws IOException{
				String str="";
				//ʱ��Ϊ����
				if((beginTime == null || "".equals(beginTime)) && (endTime == null || "".equals(endTime)) && (timeData ==null || "".equals(timeData))){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    		Calendar cal =Calendar.getInstance();
		    		
		    		cal.set(Calendar.DAY_OF_MONTH,1);		//����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
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
		 * ���������ѯ--չʾ��Ա
		 * @author ����ɭ
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
		 * ��ѯ�ҵ�����
		 * @return
		 */
		public String myUnsafeCondition(){
			String personId = (String) ServletActionContext.getRequest().getSession().getAttribute("personId");
			mapArray = unsafeConditionService.myUnsafeCondition(personId, page, rows);
			return SUCCESS;
		}
		//����������ѯ����
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
			
			String name="������ѯ���ݵ���";
			String titles="����������,������������,��������ȼ�,��굥λ,���첿��,"
					+ "�������,�������,�����,���������,���ʱ��,���ص�,��������,"
					+ "ָ��,�۷�,��ӦΣ��Դ,����רҵ,��,���յȼ�,¼����,���ĸ�����,"
					+ "��������,��������,���Ľ���,����ȷ��,����ȷ��ʱ��,����,�������,¼��ʱ��";
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
		 * ����������
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
		  			jo.put("editorDateTime", "��");
		  		}
		  		if(unsafeCondition.getConfirmTime() != null){
		  			jo.put("confirmTime", unsafeCondition.getConfirmTime().toString());
		  		}else{
		  			jo.put("confirmTime", "��");
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
					jo.put("editor", "��");
				}
				if(unsafeCondition.getHasCorrectConfirmed()!=null){
					if(unsafeCondition.getHasCorrectConfirmed()!=true){
						jo.put("hasCorrectConfirmed", "δ����ȷ��");
					}else{
						jo.put("hasCorrectConfirmed", "������ȷ��");
					}
				}
				if(unsafeCondition.getHasReviewed()!=null){
					if(unsafeCondition.getHasReviewed()!=true){
						jo.put("hasReviewed","δ����");
					}else{
						jo.put("hasReviewed","�Ѹ���");
					}
				}
				if(unsafeCondition.getHasCorrectFinished()!=null){
					if(unsafeCondition.getHasCorrectFinished()!=true){
						jo.put("hasCorrectFinished", "δ�������");
					}else{
						jo.put("hasCorrectFinished", "���������");
					}
				}
				array.add(jo);			
			}
			
			//String str="{\"total\":"+total+",\"rows\":"+array+"}";
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", total);// total�� ����ܼ�¼���������
	        json.put("rows", array);// rows�� ���ÿҳ��¼ list
	        String name="�����������ݵ���";
			String titles="����������,������������,��������ȼ�,��굥λ,���첿��,"
					+ "�������,�������,�����,���������,���ʱ��,���ص�,��������,"
					+ "ָ��,�۷�,��ӦΣ��Դ,����רҵ,��,���յȼ�,¼����,���ĸ�����,"
					+ "��������,��������,���Ľ���,����ȷ��,����ȷ��ʱ��,����,�������,¼��ʱ��";
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
		 * ����map���ϵ�����excel
		 * @param map
		 * @return 
		 * @throws IOException 
		 */
		@SuppressWarnings("deprecation")
		private InputStream listToExcel(Map<String, Object> map, 
				String name, String titles, String cellValue) throws IOException{
			
			
			// ��һ��������һ��workbook����Ӧһ��Excel�ļ�
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
	        HSSFSheet hssfSheet = workbook.createSheet(name);
	        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
	        HSSFRow hssfRow = hssfSheet.createRow(0);
	        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
	        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();//��ͷ��ʽ
	        HSSFCellStyle hssfCellStyle2 = workbook.createCellStyle();//������ʽ

	        hssfCellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        hssfCellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        //������ʽ
	        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont font = workbook.createFont();
	        font.setColor(HSSFColor.VIOLET.index);
	        font.setFontHeightInPoints((short) 10);
	        hssfCellStyle.setFont(font);
	        
	        HSSFCell hssfCell;
	        for(int i=0;i<titles.split(",").length;i++){
	        	hssfSheet.setColumnWidth(i, 70*70);
	        	hssfCell = hssfRow.createCell(i);
		        hssfCell.setCellValue(titles.split(",")[i]);//����2
		        hssfCell.setCellStyle(hssfCellStyle);
		        hssfCell.setCellStyle(hssfCellStyle2);
	        }
	        net.sf.json.JSONArray array=net.sf.json.JSONArray.fromObject(map.get("rows"));
	        // ���岽��д��ʵ������
	        for (int i = 0; i < array.size(); i++) {
	        	hssfRow = hssfSheet.createRow(i+1);
	        	
    			// �������������С���Ԫ�񣬲�����ֵ
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
	        // ���߲������ļ��浽ָ��λ��
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
