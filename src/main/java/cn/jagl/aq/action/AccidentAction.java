package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.request.SessionScope;

import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentLevel;
import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.NearMiss;
import cn.jagl.aq.domain.NearMissType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeCondition;

@SuppressWarnings({ "serial", "rawtypes" })
public class AccidentAction extends BaseAction {
	private Integer page;
	private Integer rows;//��ҳ����
	

	private Date begintime;
	private Date endtime;
	private String accidentLevel;
	private String departmentSn;//��ѯ����
	private InputStream excelStream; 
    private String excelFileName;   
	
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getExcelFileName() {
		return excelFileName;
	}
	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	public String getAccidentName() {
		return accidentName;
	}
	public void setAccidentName(String accidentName) {
		this.accidentName = accidentName;
	}
	public String getHappenLocation() {
		return happenLocation;
	}
	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	public String getAccidentProcess() {
		return accidentProcess;
	}
	public void setAccidentProcess(String accidentProcess) {
		this.accidentProcess = accidentProcess;
	}
	public Integer getDeadCount() {
		return deadCount;
	}
	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}
	public Integer getSeriousInjureCount() {
		return seriousInjureCount;
	}
	public void setSeriousInjureCount(Integer seriousInjureCount) {
		this.seriousInjureCount = seriousInjureCount;
	}
	public Integer getFleshInjureCount() {
		return fleshInjureCount;
	}
	public void setFleshInjureCount(Integer fleshInjureCount) {
		this.fleshInjureCount = fleshInjureCount;
	}
	public Float getDirectEconomicLoss() {
		return directEconomicLoss;
	}
	public void setDirectEconomicLoss(Float directEconomicLoss) {
		this.directEconomicLoss = directEconomicLoss;
	}
	public Float getIndirectEconomicLoss() {
		return indirectEconomicLoss;
	}
	public void setIndirectEconomicLoss(Float indirectEconomicLoss) {
		this.indirectEconomicLoss = indirectEconomicLoss;
	}
	public String getReasonArticle() {
		return reasonArticle;
	}
	public void setReasonArticle(String reasonArticle) {
		this.reasonArticle = reasonArticle;
	}
	public String getDirectReason() {
		return directReason;
	}
	public void setDirectReason(String directReason) {
		this.directReason = directReason;
	}
	public String getIndirectReason() {
		return indirectReason;
	}
	public void setIndirectReason(String indirectReason) {
		this.indirectReason = indirectReason;
	}
	public String getPrecautionMeasure() {
		return precautionMeasure;
	}
	public void setPrecautionMeasure(String precautionMeasure) {
		this.precautionMeasure = precautionMeasure;
	}
	private String accidentSn;//���ݱ�Ÿ���
	private String accidentName;//�¹�����
	private Date happenDateTime;//����ʱ��
	public Date getHappenDateTime() {
		return happenDateTime;
	}
	public void setHappenDateTime(Date happenDateTime) {
		this.happenDateTime = happenDateTime;
	}
	private String happenLocation;//�����ص�
	private String accidentTypeSn;//�¹�����
	private String accidentLevelSn;//�¹ʵȼ�
	private String accidentProcess;//��������
	private Integer deadCount;//��������
	private Integer seriousInjureCount;//��������
	private Integer fleshInjureCount;//��������
	private Float directEconomicLoss;//ֱ�Ӿ�����ʧ
	private Float indirectEconomicLoss;//��Ӿ�����ʧ
	private String reasonArticle;//������
	private String directReason;//ֱ��ԭ��
	private String indirectReason;//���ԭ��
	private String precautionMeasure;//Ԥ����ʩ
	
	public String getAccidentSn() {
		return accidentSn;
	}
	public void setAccidentSn(String accidentSn) {
		this.accidentSn = accidentSn;
	}
	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getAccidentLevel() {
		return accidentLevel;
	}
	public void setAccidentLevel(String accidentLevel) {
		this.accidentLevel = accidentLevel;
	}
	//��ѯ����
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//��ҳ��ʾ
	public String show() throws IOException{
		out();
		JSONArray array=new JSONArray();
		StringBuffer hqlentity=new StringBuffer();
		StringBuffer hqlcount=new StringBuffer();
		
		
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
    	Department department=person.getDepartment();
    	
    	//��ʱ��ʾ����¼�������ڵ�λ��¼������¼����¹�
    	//String hqlentity="select a from Accident a where a.deleted=0 and a.department.departmentSn = '"+department.getDepartmentSn()+"' and a.editor.personId='"+person.getPersonId()+"'";
    	//String hqlcount="select count(a) from Accident a where a.deleted=0 and a.department.departmentSn = '"+department.getDepartmentSn()+"' and a.editor.personId='"+person.getPersonId()+"'";
    	
    	//���ݽ�ɫ�鿴�Լ�¼���
    	hqlentity.append("select a from Accident a where a.deleted=0 and a.editor.personId='"+person.getPersonId()+"'");
    	hqlcount.append("select count(a) from Accident a where a.deleted=0 and a.editor.personId='"+person.getPersonId()+"'");

    	Department implDepartment=departmentService.getUpNearestImplDepartment(department.getDepartmentSn());
    	if(implDepartment!=null){
    		hqlentity.append(" and a.department.departmentSn like '"+implDepartment.getDepartmentSn()+"%'");
    		hqlcount.append(" and a.department.departmentSn like '"+implDepartment.getDepartmentSn()+"%'");
    	}
    	List<Accident> accidents=accidentService.findByPage(hqlentity.toString(), page, rows);
    	long total=accidentService.countHql(hqlcount.toString());
		for(Accident accident:accidents){
			 JSONObject jo=new JSONObject();
			 //�ַ���
			 jo.put("accidentSn",accident.getAccidentSn());
			 jo.put("accidentName",accident.getAccidentName());		
			 jo.put("happenLocation", accident.getHappenLocation());
			 jo.put("accidentProcess",accident.getAccidentProcess());
			 jo.put("reasonArticle",accident.getReasonArticle());
			 jo.put("directReason",accident.getDirectReason());
			 jo.put("indirectReason",accident.getIndirectReason());
			 jo.put("precautionMeasure",accident.getPrecautionMeasure());
			 //������
			 jo.put("directEconomicLoss",accident.getDirectEconomicLoss());
			 jo.put("indirectEconomicLoss",accident.getIndirectEconomicLoss());
			 //����
			 jo.put("deadCount",accident.getDeadCount());
			 jo.put("seriousInjureCount",accident.getSeriousInjureCount());
			 jo.put("fleshInjureCount",accident.getFleshInjureCount());
			 if(accident.getDepartment()!=null){
				 jo.put("deptSn",accident.getDepartment().getDepartmentSn());
				 jo.put("departmentName",accident.getDepartment().getDepartmentName());
				 jo.put("implDepartmentName",accident.getDepartment().getImplDepartmentName());
			 }
			 if(accident.getHappenDateTime()!=null){
				 //������	 
//				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
//				 String dateString = dateFormat.format(accident.getHappenDateTime().toString());
//				 jo.put("happenDateTime",dateString);
				    ZoneId zone = ZoneId.systemDefault();
				    Instant instant = accident.getHappenDateTime().atZone(zone).toInstant();
				    Date date = Date.from(instant);
				    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
					String dateString = dateFormat.format(date);
				    jo.put("happenDateTime",dateString);
			 }	
			 //��
			 if(accident.getAccidentType()!=null){
				 jo.put("accidentType",accident.getAccidentType().getAccidentTypeName());
			 }
			 if(accident.getAccidentLevel()!=null){
				 jo.put("accidentLevel",accident.getAccidentLevel().getAccidentLevelName());
			 }
			 if(accident.getEditor()!=null){
				 jo.put("editor",accident.getEditor().getPersonName());	 
			 }
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//�¹����
	public String add() throws IOException{
		out();
		String message="";
		try{
			Accident accident=new Accident();
			LocalDateTime localDateTime = happenDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			accident.setHappenDateTime(localDateTime);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");			
			accidentSn=df.format(new Date());
			accident.setAccidentSn(accidentSn);
			AccidentLevel accidentLevel=accidentLevelService.getByAccidentLevelSn(accidentLevelSn);
			accident.setAccidentLevel(accidentLevel);
			AccidentType accidentType=accidentTypeService.getByAccidentTypeSn(accidentTypeSn);
			accident.setAccidentType(accidentType);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			accident.setEditor(person);
			Department department=departmentService.getByDepartmentSn(departmentSn);
			accident.setDepartment(department);
			accident.setDirectReason(directReason);
			accident.setIndirectReason(indirectReason);
			accident.setHappenLocation(happenLocation);
			accident.setAccidentName(accidentName);
			accident.setAccidentProcess(accidentProcess);
			accident.setDeadCount(deadCount);
			accident.setDirectEconomicLoss(directEconomicLoss);
			accident.setFleshInjureCount(fleshInjureCount);
			accident.setIndirectEconomicLoss(indirectEconomicLoss);
			accident.setPrecautionMeasure(precautionMeasure);
			accident.setReasonArticle(reasonArticle);
			accident.setSeriousInjureCount(seriousInjureCount);
			accident.setDeleted(false);
			accidentService.add(accident);
			message="����¹ʳɹ���";
		}
		catch(Exception e){
			message="����¹�ʧ�ܣ����������";
		}
		out().print(message);
        out().flush(); 
        out().close();      
		return SUCCESS;
	}
	public String getAccidentTypeSn() {
		return accidentTypeSn;
	}
	public void setAccidentTypeSn(String accidentTypeSn) {
		this.accidentTypeSn = accidentTypeSn;
	}
	public String getAccidentLevelSn() {
		return accidentLevelSn;
	}
	public void setAccidentLevelSn(String accidentLevelSn) {
		this.accidentLevelSn = accidentLevelSn;
	}
	//�¹ʸ���
	public String update() throws IOException{
		out();
		Accident accident=accidentService.getByAccidentSn(accidentSn);
		AccidentLevel accidentLevel=accidentLevelService.getByAccidentLevelSn(accidentLevelSn);
		accident.setAccidentLevel(accidentLevel);
		AccidentType accidentType=accidentTypeService.getByAccidentTypeSn(accidentTypeSn);
		accident.setAccidentType(accidentType);
//		Person person=((Person)ServletActionContext.getRequest().getSession().getAttribute("person"));
//		accident.setEditor(person);
//		Department department=person.getDepartment();
//		accident.setDepartment(department);
		Department department=departmentService.getByDepartmentSn(departmentSn);
		accident.setDepartment(department);
		accident.setDirectReason(directReason);
		accident.setIndirectReason(indirectReason);
		accident.setHappenLocation(happenLocation);
		accident.setAccidentName(accidentName);
		accident.setAccidentProcess(accidentProcess);
		accident.setDeadCount(deadCount);
		accident.setDirectEconomicLoss(directEconomicLoss);
		accident.setFleshInjureCount(fleshInjureCount);
		LocalDateTime localDateTime = happenDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		accident.setHappenDateTime(localDateTime);
		accident.setIndirectEconomicLoss(indirectEconomicLoss);
		accident.setPrecautionMeasure(precautionMeasure);
		accident.setReasonArticle(reasonArticle);
		accident.setSeriousInjureCount(seriousInjureCount);
		//accident.setDeleted(false);
		accidentService.update(accident);
		String message="���³ɹ���";
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	//�¹�ɾ��
	public String delete() throws IOException{
		out();
		Accident accident=accidentService.getByAccidentSn(accidentSn);
		//accidentService.delete(accident);
		accident.setDeleted(true);
		accidentService.update(accident);
		String message="ɾ���ɹ���";
		out().print(message);
		out().flush(); 
		out().close();
		return SUCCESS;
		
	}
	//�¹ʲ�ѯ
	public String query() throws IOException{
		out();
        JSONArray array=new JSONArray();
        StringBuffer hqlentity = new StringBuffer();	
        StringBuffer hqlcount = new StringBuffer();	
		LocalDateTime begintimelocal = begintime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //��ѯ�������� ���Ŵ�ǰ̨������
        hqlentity.append("select a from Accident a where a.deleted=0 and a.department.departmentSn like '"+departmentSn+"%' and a.happenDateTime between '"+begintimelocal+"' and '"+endtimelocal+"'");
	    hqlcount.append("select count(a) from Accident a where a.deleted=0 and a.department.departmentSn like '"+departmentSn+"%' and a.happenDateTime between '"+begintimelocal+"' and '"+endtimelocal+"'");
	    //���Ӳ�ѯ����
	    if(accidentLevelSn.length()!=0){
	    	hqlentity.append(" and a.accidentLevel.accidentLevelSn='"+accidentLevelSn+"'");
	    	hqlcount.append(" and a.accidentLevel.accidentLevelSn='"+accidentLevelSn+"'");
	    }
	    List<Accident> accidents=(List<Accident>)accidentService.findByPage(hqlentity.toString(), page, rows);
	    long total=accidentService.countHql(hqlcount.toString()); 	    
		for(Accident accident:accidents){
			JSONObject jo=new JSONObject();
			 //�ַ���
			 jo.put("accidentSn",accident.getAccidentSn());
			 jo.put("accidentName",accident.getAccidentName());		
			 jo.put("happenLocation", accident.getHappenLocation());
			 jo.put("accidentProcess",accident.getAccidentProcess());
			 if(accident.getDepartment()!=null){
				 jo.put("departmentName",accident.getDepartment().getDepartmentName());
			 }
			 jo.put("reasonArticle",accident.getReasonArticle());
			 jo.put("directReason",accident.getDirectReason());
			 jo.put("indirectReason",accident.getIndirectReason());
			 jo.put("precautionMeasure",accident.getPrecautionMeasure());
			 //������
			 jo.put("directEconomicLoss",accident.getDirectEconomicLoss());
			 jo.put("indirectEconomicLoss",accident.getIndirectEconomicLoss());
			 //����
			 jo.put("deadCount",accident.getDeadCount());
			 jo.put("seriousInjureCount",accident.getSeriousInjureCount());
			 jo.put("fleshInjureCount",accident.getFleshInjureCount());
			 if(accident.getHappenDateTime()!=null){
				 //����
				    ZoneId zone = ZoneId.systemDefault();
				    Instant instant = accident.getHappenDateTime().atZone(zone).toInstant();
				    Date date = Date.from(instant);
				    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
					String dateString = dateFormat.format(date);
				    jo.put("happenDateTime",dateString);
			 }	
			 //��
			 if(accident.getAccidentType()!=null){
				 jo.put("accidentType",accident.getAccidentType().getAccidentTypeName());
			 }
			 if(accident.getAccidentLevel()!=null){
				 jo.put("accidentLevel",accident.getAccidentLevel().getAccidentLevelName());
			 }
			 if(accident.getEditor()!=null){
				 jo.put("editor",accident.getEditor().getPersonName());	 
			 }
			 
			 array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
        return SUCCESS;
	}
	//�¹�����
	public String type() throws IOException{
		out();
		List<AccidentType> accidentTypes = accidentTypeService.getAllAccidentType();
		JSONArray array=new JSONArray();
		for(AccidentType accidentType:accidentTypes){
			JSONObject jo=new JSONObject();
			jo.put("accidentTypeName", accidentType.getAccidentTypeName());
			jo.put("accidentTypeSn", accidentType.getAccidentTypeSn());
			array.put(jo);
		}
		out().print(array);
        out().flush(); 
        out().close();  
		return SUCCESS;
	}
	//�¹ʵȼ�
	public String level() throws IOException{
		out();
		List<AccidentLevel> accidentLevels = accidentLevelService.getAllAccidentLevel();
		JSONArray array=new JSONArray();
		for(AccidentLevel accidentLevel:accidentLevels){
			JSONObject jo=new JSONObject();
			jo.put("accidentLevelName", accidentLevel.getAccidentLevelName());
			jo.put("accidentLevelSn", accidentLevel.getAccidentLevelSn());
			array.put(jo);
		}
		out().print(array);
        out().flush(); 
        out().close();  
		return SUCCESS;
	}
	
	private String itemSn;
	private String departmentTypeSn;
	private String departmentSnIndex;
	private String accidentTypeSnIndex;
	private String accidentLevelSnIndex;
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getDepartmentSnIndex() {
		return departmentSnIndex;
	}
	public void setDepartmentSnIndex(String departmentSnIndex) {
		this.departmentSnIndex = departmentSnIndex;
	}
	public String getAccidentTypeSnIndex() {
		return accidentTypeSnIndex;
	}
	public void setAccidentTypeSnIndex(String accidentTypeSnIndex) {
		this.accidentTypeSnIndex = accidentTypeSnIndex;
	}
	public String getAccidentLevelSnIndex() {
		return accidentLevelSnIndex;
	}
	public void setAccidentLevelSnIndex(String accidentLevelSnIndex) {
		this.accidentLevelSnIndex = accidentLevelSnIndex;
	}
	public String getItemSn() {
		return itemSn;
	}
	public void setItemSn(String itemSn) {
		this.itemSn = itemSn;
	}
	private String clickEchart;
	public String getClickEchart() {
		return clickEchart;
	}
	public void setClickEchart(String clickEchart) {
		this.clickEchart = clickEchart;
	}
	//�¹ʱ���
	public String report() throws IOException{
		out();
		JSONObject data=new JSONObject();
		ArrayList<String> xName=new ArrayList<String>();
		ArrayList<Long> count=new ArrayList<Long>();
		List<Department> departments=new ArrayList<Department>();
		List<AccidentLevel> accidentLevels=new ArrayList<AccidentLevel>();
		List<AccidentType> accidentTypes=new ArrayList<AccidentType>();
		ArrayList<String> departmentSns=new ArrayList<String>();
		ArrayList<String> accidentTypeSns=new ArrayList<String>();
		ArrayList<String> accidentLevelSns=new ArrayList<String>();
		ArrayList<Integer> childrenSizes=new ArrayList<Integer>();
		String type="";
		deptTypeSn="";
		if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			deptTypeSn=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		}
		if(itemSn.equals("0")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
			for(Department department:departments){	
				childrenSizes.add(department.getChildDepartments().size());
			} 
			data.put("childrenSizes",childrenSizes);
			type="0";
		}
		if(itemSn.equals("1")){
	        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
			for(Department department:departments){	
				departmentSnIndex=department.getDepartmentSn();
				count.add(hqlquery());
				xName.add(department.getDepartmentName());
				departmentSns.add(department.getDepartmentSn());
				childrenSizes.add(department.getChildDepartments().size());
			} 
			data.put("departmentSns",departmentSns);
			data.put("childrenSizes",childrenSizes);
			type="1";
		}
		if(itemSn.equals("2")){
	        accidentTypes=accidentTypeService.getAllAccidentType();
			for(AccidentType accidentType:accidentTypes){
				accidentTypeSnIndex=accidentType.getAccidentTypeSn();
				count.add(hqlquery());
				xName.add(accidentType.getAccidentTypeName());
				accidentTypeSns.add(accidentType.getAccidentTypeSn());
				childrenSizes.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			data.put("accidentTypeSns",accidentTypeSns);
			data.put("childrenSizes",childrenSizes);
			type="2";
		}
		if(itemSn.equals("3")){
			accidentLevels=accidentLevelService.getAllAccidentLevel();
			for(AccidentLevel accidentLevel:accidentLevels){
				accidentLevelSnIndex=accidentLevel.getAccidentLevelSn();
				count.add(hqlquery());
				xName.add(accidentLevel.getAccidentLevelName());
				accidentLevelSns.add(accidentLevel.getAccidentLevelSn());
				childrenSizes.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
			}
			data.put("accidentLevelSns",accidentLevelSns);
			data.put("childrenSizes",childrenSizes);
			type="3";
		}
		data.put("type",type);
		data.put("xName",xName);
		data.put("count",count);
		out().print(data); 
        out().flush(); 
        out().close();  
		return SUCCESS;
	}
	String deptTypeSn;
	//���������Ƿ����ƴ��hql��䲢���õײ�ͨ�ú�����ѯ��Ŀ
	public long hqlquery(){
		LocalDateTime begintimelocal = begintime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //String str=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		StringBuffer hql = new StringBuffer();		
	    hql.append("select COUNT(*) FROM Accident i WHERE i.deleted=false and i.department.departmentSn like '"+departmentSnIndex+"%' and i.happenDateTime>='"+begintimelocal+"' and i.happenDateTime<='"+endtimelocal+"'");		
		if(accidentTypeSnIndex!=null&&accidentTypeSnIndex.length()>0){
		    hql.append(" and i.accidentType.accidentTypeSn='"+accidentTypeSnIndex+"'");		
		}
		if(accidentLevelSnIndex!=null&&accidentLevelSnIndex.length()>0){
		    hql.append(" and i.accidentLevel.accidentLevelSn='"+accidentLevelSnIndex+"'");		
		}	
		if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			hql.append(" and i.department.departmentType.departmentTypeSn in "+deptTypeSn);		
		}
	    return accidentService.countHql(hql.toString());
	}
	//�鿴��ϸ
		public String detail() throws IOException{
			out();				
			StringBuffer hqle = new StringBuffer();				
			StringBuffer hqlc = new StringBuffer();
			LocalDateTime begintimelocal = begintime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();  
			String departmentTypeSns="";
			if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
				departmentTypeSns=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
			}
			 hqle.append ( "select p FROM Accident p WHERE p.deleted=false and p.department.departmentSn like '"+departmentSnIndex+"%' and p.happenDateTime>'"+begintimelocal+"' and p.happenDateTime<'"+endtimelocal+"'");
			 hqlc.append ( "select Count(*) FROM Accident p WHERE p.deleted=false and p.department.departmentSn like '"+departmentSnIndex+"%' and p.happenDateTime>'"+begintimelocal+"' and p.happenDateTime<'"+endtimelocal+"'");
			 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
					hqle.append(" and p.department.departmentType.departmentTypeSn in "+departmentTypeSns);		
					hqlc.append(" and p.department.departmentType.departmentTypeSn in "+departmentTypeSns);		
				}
			 if(accidentTypeSnIndex!=null&&accidentTypeSnIndex.length()>0){
					hqle.append(" AND p.accidentType.accidentTypeSn='"+accidentTypeSnIndex+"'");	
					hqlc.append(" AND p.accidentType.accidentTypeSn='"+accidentTypeSnIndex+"'");	
			 }	
			if(accidentLevelSnIndex!=null&&accidentLevelSnIndex.length()>0){			  
				hqle.append(" AND p.accidentLevel.accidentLevelSn='"+accidentLevelSnIndex+"'");	
				hqlc.append(" AND p.accidentLevel.accidentLevelSn='"+accidentLevelSnIndex+"'");	
			}			
			long total=accidentService.countHql(hqlc.toString());		
			List<Accident> accidents=accidentService.findByPage(hqle.toString(),page,rows);		
			JSONArray array=new JSONArray();
			for(Accident accident:accidents){
				JSONObject jo=new JSONObject();
				 //�ַ���
				 jo.put("accidentSn",accident.getAccidentSn());
				 jo.put("accidentName",accident.getAccidentName());		
				 jo.put("happenLocation", accident.getHappenLocation());
				 jo.put("accidentProcess",accident.getAccidentProcess());
				 jo.put("implDepartmentName",accident.getDepartment().getImplDepartmentName());
				 if(accident.getDepartment()!=null){
					 jo.put("departmentName",accident.getDepartment().getDepartmentName());
				 }
				 jo.put("reasonArticle",accident.getReasonArticle());
				 jo.put("directReason",accident.getDirectReason());
				 jo.put("indirectReason",accident.getIndirectReason());
				 jo.put("precautionMeasure",accident.getPrecautionMeasure());
				 //�߼���
				 jo.put("deleted",accident.getDeleted());
				 //������
				 jo.put("directEconomicLoss",accident.getDirectEconomicLoss());
				 jo.put("indirectEconomicLoss",accident.getIndirectEconomicLoss());
				 //����
				 jo.put("deadCount",accident.getDeadCount());
				 jo.put("seriousInjureCount",accident.getSeriousInjureCount());
				 jo.put("fleshInjureCount",accident.getFleshInjureCount());
				 //������	 
				 jo.put("happenDateTime",accident.getHappenDateTime());	
				 //��
				 if(accident.getAccidentType()!=null){
					 jo.put("accidentType",accident.getAccidentType().getAccidentTypeName());
				 }
				 if(accident.getAccidentLevel()!=null){
					 jo.put("accidentLevel",accident.getAccidentLevel().getAccidentLevelName());
				 }
				 if(accident.getEditor()!=null){
					 jo.put("editor",accident.getEditor().getPersonName());	 
				 }
				 
				array.put(jo);			
			}
			
			String str="{\"total\":"+total+",\"rows\":"+array+"}";
			out().print(str);
	        out().flush(); 
	        out().close();	
			return SUCCESS;
		}
		 
		//�����ɱ���ʱ�����ļ�����ڷ������ϲ�������������
		public String exportexcel(){
			JSONObject data=new JSONObject();
			ArrayList<String> xName=new ArrayList<String>();
			ArrayList<Long> count=new ArrayList<Long>();
			List<Department> departments=new ArrayList<Department>();
			List<AccidentLevel> accidentLevels=new ArrayList<AccidentLevel>();
			List<AccidentType> accidentTypes=new ArrayList<AccidentType>();
			ArrayList<String> departmentSns=new ArrayList<String>();
			ArrayList<String> accidentTypeSns=new ArrayList<String>();
			ArrayList<String> accidentLevelSns=new ArrayList<String>();
			ArrayList<Integer> childrenSizes=new ArrayList<Integer>();
			if(departmentSnIndex.equals("null")){
				departmentSnIndex=null;
			}
			String departmentName=departmentService.getByDepartmentSn(departmentSnIndex).getDepartmentName();
			if(departmentTypeSn.equals("null")){
				departmentTypeSn=null;
			}
			if(accidentTypeSnIndex.equals("null")){
				accidentTypeSnIndex=null;
			}
			if(accidentLevelSnIndex.equals("null")){
				accidentLevelSnIndex=null;
			}
			//���ѡ������ձ�������ʱ���������в��������г�
			StringBuffer hqls = new StringBuffer();
			String departmentTypeSns="";
			LocalDateTime begintimelocal = begintime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime endtimelocal = endtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();  
			if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
				departmentTypeSns=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
			}
			hqls.append ( "select p FROM Accident p WHERE p.deleted=false and p.department.departmentSn like '"+departmentSnIndex+"%' and p.happenDateTime>'"+begintimelocal+"' and p.happenDateTime<'"+endtimelocal+"'");
			 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
				 hqls.append(" and p.department.departmentType.departmentTypeSn in "+departmentTypeSns);		
				}
			 if(accidentTypeSnIndex!=null&&accidentTypeSnIndex.length()>0){
				 hqls.append(" AND p.accidentType.accidentTypeSn='"+accidentTypeSnIndex+"'");	
			}	
			if(accidentLevelSnIndex!=null&&accidentLevelSnIndex.length()>0){			  
				hqls.append(" AND p.accidentLevel.accidentLevelSn='"+accidentLevelSnIndex+"'");	
			}				
			List<Accident> accidents=accidentService.findByPage(hqls.toString(),1,10000);		
			
			String type="";
			deptTypeSn="";
			if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
				deptTypeSn=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
			}
			if(itemSn.equals("0")){
		        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
				for(Department department:departments){	
					childrenSizes.add(department.getChildDepartments().size());
				} 
				data.put("childrenSizes",childrenSizes);
				type="0";
			}
			if(itemSn.equals("1")){
		        departments=departmentService.getDepartmentsNew(departmentSnIndex, departmentTypeSn,clickEchart);
				for(Department department:departments){	
					departmentSnIndex=department.getDepartmentSn();
					count.add(hqlquery());
					xName.add(department.getDepartmentName());
					departmentSns.add(department.getDepartmentSn());
					childrenSizes.add(department.getChildDepartments().size());
				} 
				data.put("departmentSns",departmentSns);
				data.put("childrenSizes",childrenSizes);
				type="1";
			}
			if(itemSn.equals("2")){
		        accidentTypes=accidentTypeService.getAllAccidentType();
				for(AccidentType accidentType:accidentTypes){
					accidentTypeSnIndex=accidentType.getAccidentTypeSn();
					count.add(hqlquery());
					xName.add(accidentType.getAccidentTypeName());
					accidentTypeSns.add(accidentType.getAccidentTypeSn());
					childrenSizes.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
				}
				data.put("accidentTypeSns",accidentTypeSns);
				data.put("childrenSizes",childrenSizes);
				type="2";
			}
			if(itemSn.equals("3")){
				accidentLevels=accidentLevelService.getAllAccidentLevel();
				for(AccidentLevel accidentLevel:accidentLevels){
					accidentLevelSnIndex=accidentLevel.getAccidentLevelSn();
					count.add(hqlquery());
					xName.add(accidentLevel.getAccidentLevelName());
					accidentLevelSns.add(accidentLevel.getAccidentLevelSn());
					childrenSizes.add(departmentService.getByDepartmentSn(departmentSnIndex).getChildDepartments().size());
				}
				data.put("accidentLevelSns",accidentLevelSns);
				data.put("childrenSizes",childrenSizes);
				type="3";
			}
			
			
			XSSFWorkbook wb = new XSSFWorkbook(); 
			XSSFSheet sheet = null;
			String name="";
			//��̬���ɱ���
	        if(!itemSn.equals("0")){
	        	if(itemSn.equals("1")){
	        		name=departmentName+"�����ű�";
	        	}
	            if(itemSn.equals("2")){
	            	name=departmentName+"���¹����ͱ�";
	            }if(itemSn.equals("3")){
	            	name=departmentName+"���¹ʵȼ���";
	            }
	        }else{
	        	name=departmentName+"";
	        }
			sheet = wb.createSheet(name);
	        sheet.setColumnWidth(1,100*100);
	        XSSFRow row = sheet.createRow((int) 0);  
	        XSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        XSSFCell cell = row.createCell(0) ;
	        if(itemSn.equals("1")){
	            cell.setCellValue("����");  
	        }if(itemSn.equals("2")){
	            cell.setCellValue("�¹�����");  
	        }if(itemSn.equals("3")){
	            cell.setCellValue("�¹ʵȼ�");    
	        }
	        cell.setCellStyle(style);  
	        cell = row.createCell(1);  
	        cell.setCellValue("����������Ŀ");  
	        cell.setCellStyle(style);
	    
	        for (int i = 0; i < count.size(); i++)  
	        {  
	            row = sheet.createRow((int) i + 1);
	            row.createCell( 0).setCellValue(xName.get(i));
	            row.createCell( 1).setCellValue(count.get(i));
	        }
	        //��ϸ���ܱ�
			XSSFSheet detailSheet = null;
			detailSheet = wb.createSheet(name+"��ϸ");
			detailSheet.setColumnWidth(1,100*100);
	        XSSFRow detailRow = detailSheet.createRow((int) 0);  
	        XSSFCellStyle detailStyle = wb.createCellStyle();  
	        detailStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        //��һ�� ����       
	        XSSFCell detailCell = detailRow.createCell(0) ;
	        detailCell.setCellValue("�¹ʱ��");  
	        detailCell=detailRow.createCell(1) ;
	        detailCell.setCellValue("�¹�����");  
	        detailCell=detailRow.createCell(2) ;
	        detailCell.setCellValue("�����ص�"); 
	        detailCell=detailRow.createCell(3) ;
	        detailCell.setCellValue("��굥λ"); 
	        detailCell=detailRow.createCell(4) ;
	        detailCell.setCellValue("��������"); 
	        detailCell=detailRow.createCell(5) ;
	        detailCell.setCellValue("ԭ�����"); 
	        detailCell=detailRow.createCell(6) ;
	        detailCell.setCellValue("ֱ�Ӿ�����ʧ"); 
	        detailCell=detailRow.createCell(7) ;
	        detailCell.setCellValue("ֱ��ԭ��"); 
	        detailCell=detailRow.createCell(8) ;
	        detailCell.setCellValue("���ԭ��"); 
	        detailCell=detailRow.createCell(9) ;
	        detailCell.setCellValue("Ԥ����ʩ"); 
	        detailCell=detailRow.createCell(10) ;
	        detailCell.setCellValue("��Ӿ�����ʧ"); 
	        detailCell=detailRow.createCell(11) ;
	        detailCell.setCellValue("��������"); 
	        detailCell=detailRow.createCell(12) ;
	        detailCell.setCellValue("��������"); 
	        detailCell=detailRow.createCell(13) ;
	        detailCell.setCellValue("��������"); 
	        detailCell=detailRow.createCell(14) ;
	        detailCell.setCellValue("����ʱ��"); 
	        detailCell=detailRow.createCell(15) ;
	        detailCell.setCellValue("�¹�����"); 
	        detailCell=detailRow.createCell(16) ;
	        detailCell.setCellValue("�¹ʵȼ�"); 
	        detailCell=detailRow.createCell(17) ;
	        detailCell.setCellValue("�༭��"); 
	        //ѭ�����ɱ������
	        for (int i = 0; i < accidents.size(); i++)  
	        {  
	        	detailRow = detailSheet.createRow((int) i + 1);
	            Accident u=accidents.get(i);
	            detailRow.createCell( 0).setCellValue(u.getAccidentSn());
           	 	detailRow.createCell( 1).setCellValue(u.getAccidentName());
           	 	detailRow.createCell( 2).setCellValue(u.getHappenLocation());
           	 	if(u.getDepartment()!=null){
               	 	detailRow.createCell( 3).setCellValue(u.getDepartment().getImplDepartmentName());
               	 	detailRow.createCell( 4).setCellValue(u.getDepartment().getDepartmentName());
           	 	}
           	 	detailRow.createCell( 5).setCellValue(u.getReasonArticle());
           	 	if(u.getDirectEconomicLoss()!=null)
           	 	detailRow.createCell( 6).setCellValue(u.getDirectEconomicLoss());
           	 	detailRow.createCell( 7).setCellValue(u.getDirectReason());
           	 	detailRow.createCell( 8).setCellValue(u.getIndirectReason());
           	 	detailRow.createCell( 9).setCellValue(u.getPrecautionMeasure());
           	 	if(u.getIndirectEconomicLoss()!=null)
           	 	detailRow.createCell( 10).setCellValue(u.getIndirectEconomicLoss());
           	 	if(u.getDeadCount()!=null)
           	 	detailRow.createCell( 11).setCellValue(u.getDeadCount());
           	 	if(u.getSeriousInjureCount()!=null)
           	 	detailRow.createCell( 12).setCellValue(u.getSeriousInjureCount());
           	 	if(u.getFleshInjureCount()!=null)
           	 	detailRow.createCell( 13).setCellValue(u.getFleshInjureCount());
           	 	if(u.getHappenDateTime()!=null)
           	 	detailRow.createCell( 14).setCellValue(u.getHappenDateTime().toString());	
           	 	if(u.getAccidentType()!=null)
           	 	detailRow.createCell( 15).setCellValue(u.getAccidentType().getAccidentTypeName());
           	 	if(u.getAccidentLevel()!=null)
           	 	detailRow.createCell( 16).setCellValue(u.getAccidentLevel().getAccidentLevelName());
           	 	if(u.getEditor()!=null)
           	 	detailRow.createCell( 17).setCellValue(u.getEditor().getPersonName());
				 
	        }
	        
	        try  
	        {  
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	            wb.write(fout);
	            wb.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
	  
	            excelStream = is;          
	            //��̬�����ļ���
	            excelFileName =URLEncoder.encode(name+".xlsx", "UTF-8");
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }
			return "download";
		}
	}

