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
import java.util.Date;
import java.util.List;

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

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.NearMiss;
import cn.jagl.aq.domain.NearMissAudit;
import cn.jagl.aq.domain.NearMissAuditType;
import cn.jagl.aq.domain.NearMissState;
import cn.jagl.aq.domain.NearMissType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.RiskLevel;

public class NearMissAction extends BaseAction<NearMiss>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer page;
	private Integer rows;
	private String nearMissSn;
	private String departmentSn;
	private String eventName;
	private java.sql.Date happenDate;
	private String happenLocation;
	private String nearMissTypeSn;//δ���¼����ͱ��
	private String riskResult;
	private RiskLevel riskLevel;
	private Integer riskLevelSn;
	private Integer nearMissAuditTypeSn;
	private String eventProcess;
	private String reasonAnalysis;
	private String preventMeasure;
	private String personId;
	private Timestamp reportTime;
	private String message;
	private NearMissState  nearMissState;
	private Integer nearMissStateSn;
	private String range;//�������ϱ����ǵ�λ��˻��߼������
	private java.sql.Date begintime;
	private java.sql.Date endtime;
	//δ���¼���˱�
	private NearMissAuditType nearMissAuditType;//������
	private String auditInfo;//�����Ϣ
	private Person auditor;//�����	
	private Timestamp auditTime;//���ʱ��
	private InputStream excelStream; 
	private String excelFileName;
	private String departmentTypeSn;
	
	
	
	
	//��ҳ��ʾ����δ���¼������б�
	public String show() throws IOException{
		out();
        JSONArray array=new JSONArray();
        String nearMissState;
		String pId=(String) session.get("personId");
		Person person=personService.getByPersonId(pId);
//    	Department department=new Department();
    	String hql="";
    	String hqlcount="";
    	long total;
    	//��ʾ������¼����¼�
        if(range.equals("�ϱ�")){
        	nearMissState="(0,1)";
        	hqlcount = "select count(n) FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState+" and n.reportPerson.personId ='"+person.getPersonId()+"'";
    		hql = "FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState+" and n.reportPerson.personId ='"+person.getPersonId()+"'";
        }
        //��ʾ�����굥λ�µ��¼� �ܵ��ҳ���ʾ�Ѿ��ǵ�λ����Ա
        else if(range.equals("��λ���")){
        	nearMissState="(1,2,3)";
    		departmentSn=person.getDepartment().getDepartmentSn();
    		if(departmentService.getByDepartmentSn(departmentSn).getParentDepartment()!=null){
        		departmentSn=departmentService.getUpNearestImplDepartment(departmentSn).getDepartmentSn();
    		}
        	hqlcount = "select count(n) FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState+" and n.department.departmentSn like '"+departmentSn+"%'";		
    		hql = "FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState+" and n.department.departmentSn like '"+departmentSn+"%'";		
        	
        } 
        //��˾��� ��ʾȫ�����µ��¼� �Ѿ��Ǽ��Ź���Ա
        else{
        	nearMissState="(2,3,4,5)";   
        	hqlcount = "select count(n) FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState;		
    		hql = "FROM NearMiss n where n.deleted=false and n.nearMissState in "+nearMissState;
        	    	
        }
        hql = hql + " order by n.id desc";
    	total=nearMissService.countHql(hqlcount);
		List<NearMiss> events=(List<NearMiss>)nearMissService.findByPage(hql, page, rows);
		for(NearMiss nearMiss:events){
	        JSONObject jo=new JSONObject();
			jo.put("nearMissSn", nearMiss.getNearMissSn());
			jo.put("nearMissTypeSn", nearMiss.getNearMissType().getNearMissTypeSn());
			jo.put("nearMissTypeName", nearMiss.getNearMissType().getNearMissTypeName());
			jo.put("happenDate", nearMiss.getHappenDate());
			jo.put("riskLevel", nearMiss.getRiskLevel());
			jo.put("nearMissState", nearMiss.getNearMissState());
			jo.put("deleted", nearMiss.getDeleted());
			if(nearMiss.getReportPerson()!=null){
				jo.put("reportPersonName", nearMiss.getReportPerson().getPersonName());
			}
			jo.put("departmentName", nearMiss.getDepartment().getDepartmentName());
			if( nearMiss.getDepartment().getParentDepartment() != null ){
				jo.put("parentDepartmentName", nearMiss.getDepartment().getParentDepartment().getDepartmentName());
			}
			jo.put("eventName", nearMiss.getEventName());
			jo.put("eventProcess", nearMiss.getEventProcess());
		    jo.put("happenLocation", nearMiss.getHappenLocation());
			jo.put("preventMeasure", nearMiss.getPreventMeasure());
			jo.put("reasonAnalysis", nearMiss.getReasonAnalysis());
			jo.put("riskResult", nearMiss.getRiskResult());
			if(nearMiss.getReportTime()!=null){
				jo.put("reportTime", nearMiss.getReportTime().toString().substring(0, nearMiss.getReportTime().toString().length()-2));
			}
			array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
        return SUCCESS;
	}
	//δ���¼����
	public String add() throws IOException{
		out();		
		try{
			NearMiss nearMiss=new NearMiss();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");			
			nearMissSn=df.format(new Date());
			nearMiss.setNearMissSn(nearMissSn);
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");			
			String rt=dt.format(new Date());
			nearMiss.setReportTime(Timestamp.valueOf(rt));
			if(nearMissTypeSn==null||nearMissTypeSn.trim().length()==0){
				message="�¼������Ǳ�����";
				return SUCCESS;
			}
			else{
				NearMissType nearMissType=nearMissTypeService.getByNearMissTypeSn(nearMissTypeSn);
				nearMiss.setNearMissType(nearMissType);	
			}
			nearMiss.setNearMissState(NearMissState.δ�ϱ�);
			nearMiss.setEventName(eventName);
			nearMiss.setHappenDate(happenDate);
			nearMiss.setHappenLocation(happenLocation);
			nearMiss.setRiskResult(riskResult);
			nearMiss.setRiskLevel(riskLevel);
			nearMiss.setEventProcess(eventProcess);
			nearMiss.setReasonAnalysis(reasonAnalysis);
			nearMiss.setPreventMeasure(preventMeasure);
			nearMiss.setDeleted(false);
			//�ϱ��˲�����Ϊ��
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
        	person=personService.getByPersonId(person.getPersonId());
        	nearMiss.setReportPerson(person);
			Department department=person.getDepartment();
			nearMiss.setDepartment(department);
			nearMissService.add(nearMiss);
			message="���δ���¼��ɹ���";
			}
		catch(Exception e){
			message="���δ���¼�ʧ�ܣ����������";
		}
		out().print(message);
        out().flush(); 
        out().close();         
		return SUCCESS;
	}

	//δ���¼�ɾ��
	public String delete() throws IOException{
		out();
		try{
			NearMiss nearMiss=nearMissService.getByNearMissSn(nearMissSn);
			nearMiss.setDeleted(true);
			nearMissService.update(nearMiss);
			//nearMissService.deleteById(nearMiss.getId());
			message="ɾ��δ���¼��ɹ���";
			}
		catch(Exception e){
			message="ɾ��δ���¼�ʧ�ܣ����������";
		}
		out().print(message);
        out().flush(); 
        out().close();         
		return SUCCESS;
	}
	//δ���¼�����
	public String update() throws IOException{
		out();
		NearMiss nearMiss=nearMissService.getByNearMissSn(nearMissSn);
		nearMiss.setEventName(eventName);
		nearMiss.setEventProcess(eventProcess);
		nearMiss.setHappenDate(happenDate);
		nearMiss.setHappenLocation(happenLocation);
		NearMissType nearMissType=nearMissTypeService.getByNearMissTypeSn(nearMissTypeSn);
		nearMiss.setNearMissType(nearMissType);
		nearMiss.setPreventMeasure(preventMeasure);
		nearMiss.setReasonAnalysis(reasonAnalysis);
		nearMiss.setRiskLevel(riskLevel);
		nearMiss.setRiskResult(riskResult);
		nearMissService.update(nearMiss);
		return SUCCESS;
	}
	//δ���¼�״̬�ı�
	public String changeState() throws IOException{
		out();
		NearMiss nearMiss=nearMissService.getByNearMissSn(nearMissSn);
		if(range.equals("�ϱ�")){
			nearMiss.setNearMissState(NearMissState.���ϱ�);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");			
			nearMiss.setReportTime(Timestamp.valueOf(df.format(new Date())));
			nearMissService.update(nearMiss);
			message="�ϱ��ɹ�";
		}
		else if(range.equals("��λ���ͨ��")){
			nearMiss.setNearMissState(NearMissState.��λ���ͨ��);
			nearMissService.update(nearMiss);
			
			NearMissAudit nearMissAudit=new NearMissAudit();
			nearMissAudit.setAuditInfo(auditInfo);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			person=personService.getByPersonId(person.getPersonId());
			nearMissAudit.setAuditor(person);
			nearMissAudit.setAuditTime(new Timestamp(System.currentTimeMillis()));
			nearMissService.getByNearMissSn(nearMissSn);
			nearMissAudit.setNearMiss(nearMiss);
			nearMissAudit.setNearMissAuditType(NearMissAuditType.��λ���);
			nearMissAudit.setNearMissState(NearMissState.��λ���ͨ��);
			nearMissAuditService.add(nearMissAudit);
			message="��λ�����ͨ��";
		}
		else if(range.equals("��λ���δͨ��")){
			nearMiss.setNearMissState(NearMissState.��λ���δͨ��);
			nearMissService.update(nearMiss);
			
			NearMissAudit nearMissAudit=new NearMissAudit();
			nearMissAudit.setAuditInfo(auditInfo);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			person=personService.getByPersonId(person.getPersonId());
			nearMissAudit.setAuditor(person);
			nearMissAudit.setAuditTime(new Timestamp(System.currentTimeMillis()));
			nearMissService.getByNearMissSn(nearMissSn);
			nearMissAudit.setNearMiss(nearMiss);
			nearMissAudit.setNearMissAuditType(NearMissAuditType.��λ���);
			nearMissAudit.setNearMissState(NearMissState.��λ���δͨ��);
			nearMissAuditService.add(nearMissAudit);
			message="��λ���δͨ��";
		}
		else if(range.equals("��˾���ͨ��")){
			nearMiss.setNearMissState(NearMissState.��˾���ͨ��);
			nearMissService.update(nearMiss);
			
			NearMissAudit nearMissAudit=new NearMissAudit();
			nearMissAudit.setAuditInfo(auditInfo);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			person=personService.getByPersonId(person.getPersonId());
			nearMissAudit.setAuditor(person);
			nearMissAudit.setAuditTime(new Timestamp(System.currentTimeMillis()));
			nearMissService.getByNearMissSn(nearMissSn);
			nearMissAudit.setNearMiss(nearMiss);
			nearMissAudit.setNearMissAuditType(NearMissAuditType.��˾���);
			nearMissAudit.setNearMissState(NearMissState.��˾���δͨ��);
			nearMissAuditService.add(nearMissAudit);
			message="��˾�����ͨ��";
		}
		else if(range.equals("��˾���δͨ��")){
			nearMiss.setNearMissState(NearMissState.��˾���δͨ��);
			nearMissService.update(nearMiss);
			
			NearMissAudit nearMissAudit=new NearMissAudit();
			nearMissAudit.setAuditInfo(auditInfo);
			String pId=(String) session.get("personId");
			Person person=personService.getByPersonId(pId);
			person=personService.getByPersonId(person.getPersonId());
			nearMissAudit.setAuditor(person);
			nearMissAudit.setAuditTime(new Timestamp(System.currentTimeMillis()));
			nearMissService.getByNearMissSn(nearMissSn);
			nearMissAudit.setNearMiss(nearMiss);
			nearMissAudit.setNearMissAuditType(NearMissAuditType.��˾���);
			nearMissAudit.setNearMissState(NearMissState.��˾���δͨ��);
			nearMissAuditService.add(nearMissAudit);
			message="��˾���δͨ��";
		}
		else{
			message="����ʧ�ܣ��������";
		}
		out().print(message);
        out().flush(); 
        out().close();    
		return SUCCESS;
	}
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//��ȡ����δ���¼�����
	public String type() throws IOException{
		out();
		List<NearMissType> nearMissTypes = nearMissTypeService.getAllNearMissType();
		JSONArray array=new JSONArray();
		for(NearMissType nearMissType:nearMissTypes){
			JSONObject jo=new JSONObject();
			jo.put("nearMissTypeName", nearMissType.getNearMissTypeName());
			jo.put("nearMissTypeSn", nearMissType.getNearMissTypeSn());
			array.put(jo);
		}
		out().print(array);
        out().flush(); 
        out().close();  
		return SUCCESS;
	}
	//��ѯ
	public String query () throws IOException{
		out();
        JSONArray array=new JSONArray();
        StringBuffer hqlnearmiss = new StringBuffer();	
        StringBuffer hqlcount = new StringBuffer();	
	    hqlnearmiss.append("select n from NearMiss n where n.deleted=false and n.happenDate between '"+begintime+"' and '"+endtime+"' and n.department.departmentSn like '"+departmentSn+"%'");
	    hqlcount.append("select count(n) from NearMiss n where n.deleted=false and n.happenDate between '"+begintime+"' and '"+endtime+"' and n.department.departmentSn like '"+departmentSn+"%'");
	    if(nearMissStateSn!=null){
	    	hqlnearmiss.append(" and n.nearMissState="+nearMissStateSn);
	    	hqlcount.append(" and n.nearMissState="+nearMissStateSn);
	    }
		if(riskLevelSn!=null){
			hqlnearmiss.append(" and n.riskLevel="+riskLevelSn);
			hqlcount.append(" and n.riskLevel="+riskLevelSn);
		}
		if(nearMissTypeSn!=null && !nearMissTypeSn.equals("")){
			hqlnearmiss.append(" and n.nearMissType.nearMissTypeSn='"+nearMissTypeSn+"'");
			hqlcount.append(" and n.nearMissType.nearMissTypeSn='"+nearMissTypeSn+"'");
		}
	    List<NearMiss> events=(List<NearMiss>)nearMissService.findByPage(hqlnearmiss.toString(), page, rows);
	    long total=nearMissService.countHql(hqlcount.toString()); 	    
		for(NearMiss nearMiss:events){
	        JSONObject jo=new JSONObject();
			jo.put("nearMissSn", nearMiss.getNearMissSn());
			jo.put("nearMissTypeSn", nearMiss.getNearMissType().getNearMissTypeSn());
			jo.put("nearMissTypeName", nearMiss.getNearMissType().getNearMissTypeName());
			jo.put("happenDate", nearMiss.getHappenDate());
			jo.put("riskLevel", nearMiss.getRiskLevel());
			jo.put("nearMissState", nearMiss.getNearMissState());
			jo.put("deleted", nearMiss.getDeleted());
			jo.put("deleted", nearMiss.getDepartment().getImplDepartmentName());
			
			if(nearMiss.getReportPerson()!=null){
				jo.put("reportPerson", nearMiss.getReportPerson().getPersonName());
			}
			jo.put("departmentName", nearMiss.getDepartment().getDepartmentName());
			if(nearMiss.getDepartment()!=null){
				jo.put("implDepartmentName",nearMiss.getDepartment().getImplDepartmentName());
			}
			jo.put("eventName", nearMiss.getEventName());
			jo.put("eventProcess", nearMiss.getEventProcess());
		    jo.put("happenLocation", nearMiss.getHappenLocation());
			jo.put("preventMeasure", nearMiss.getPreventMeasure());
			jo.put("reasonAnalysis", nearMiss.getReasonAnalysis());
			jo.put("riskResult", nearMiss.getRiskResult());
			if(nearMiss.getReportTime()!=null){
				jo.put("reportTime", nearMiss.getReportTime().toString().substring(0, nearMiss.getReportTime().toString().length()-2));
			}
			array.put(jo);
		}
		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
        return SUCCESS;
	}
	//δ���¼�ͳ��
	public String report() throws IOException{
		out();
//		if(hasType.equals("false")){
//			departmentTypeSn=departmentService.getByDepartmentSn(departmentSn).getDepartmentType().getDepartmentTypeSn();
//		}
		List<Department> departments=new ArrayList<Department>();
		if(departmentTypeSn!=null && !departmentTypeSn.equals("")){
			 departments=departmentService.getDepartments(departmentSn, departmentTypeSn);				
		}
		else{
			departments.add((Department)departmentService.getByDepartmentSn(departmentSn));
		}
		List<NearMissType>	nearMissTypes=nearMissTypeService.getAllNearMissType();
		JSONArray array=new JSONArray();
		for(Department de:departments){
			JSONObject jo=new JSONObject();
			jo.put("deptName", de.getDepartmentName());
			jo.put("deptSn", de.getDepartmentSn());
			JSONArray types=new JSONArray();
			for(NearMissType type:nearMissTypes){
				JSONObject js=new JSONObject();	
				String hql="select count(n) from NearMiss n where n.deleted=false and n.department.departmentSn like '"+de.getDepartmentSn()+"%' and n.nearMissType.nearMissTypeSn="+type.getNearMissTypeSn()+" and n.happenDate between '"+begintime+"' and '"+endtime+"'";
				js.put("typeCount", nearMissService.countHql(hql));
				js.put("typeName", type.getNearMissTypeName());
				js.put("typeSn", type.getNearMissTypeSn());
				types.put(js);
			}
			jo.put("types", types);
			JSONArray levels=new JSONArray();
	 		for(int i=0;i<3;i++){
	 			JSONObject jl=new JSONObject();	
				String hql="select count(n) from NearMiss n where n.deleted=false and n.department.departmentSn like '"+de.getDepartmentSn()+"%'and n.riskLevel="+i+" and n.happenDate between '"+begintime+"' and '"+endtime+"'";
				jl.put("levelCount", nearMissService.countHql(hql));
				jl.put("levelSn", i);
				levels.put(jl);
			}
			jo.put("levels", levels);
			array.put(jo);
		}
		String str="{\"array\":"+array+"}";
		out().print(str);
		out().flush(); 
		out().close();
		return SUCCESS;
	}
	private String hasType;
	//������excel
	public String exportexcel() throws IOException{
		List<Department> departments=new ArrayList<Department>();
//		//�����ѡ��������û�в������ͣ�����ʾ��ǰ���ż��������Ӳ���
//		if(hasType.equals("false")){
//			departmentTypeSn=departmentService.getByDepartmentSn(departmentSn).getDepartmentType().getDepartmentTypeSn();
//		}
		//�������˲������ͣ�����ʾ�ò���������Ϊ�ò������͵Ĳ���
		if(departmentTypeSn!=null && !departmentTypeSn.equals("") && !departmentTypeSn.equals("null")){
			 departments=departmentService.getDepartments(departmentSn, departmentTypeSn);				
		}
		//���û��ѡ�������� Ĭ����ʾ��ǰ����
		else{
			departments.add(departmentService.getByDepartmentSn(departmentSn));
		}
		List<NearMissType>	nearMissTypes=nearMissTypeService.getAllNearMissType();
		List<String> typeName=new ArrayList<String>();
		List<String> deptName=new ArrayList<String>();
		List<Long> count=new ArrayList<Long>();
		List<NearMiss> events=new ArrayList<NearMiss>();
		String deptSns="";
		for(NearMissType type:nearMissTypes){
			typeName.add(type.getNearMissTypeName());
		}
		for(Department de:departments){
			deptName.add(de.getDepartmentName());
			for(NearMissType type:nearMissTypes){
				String hql="select count(n) from NearMiss n where n.deleted=false and n.department.departmentSn like '"+de.getDepartmentSn()+"%' and n.nearMissType.nearMissTypeSn="+type.getNearMissTypeSn()+" and n.happenDate between '"+begintime+"' and '"+endtime+"'";
				count.add(nearMissService.countHql(hql));
			}
	 		for(int i=0;i<3;i++){
				String hql="select count(n) from NearMiss n where n.deleted=false and n.department.departmentSn like '"+de.getDepartmentSn()+"%'and n.riskLevel="+i+" and n.happenDate between '"+begintime+"' and '"+endtime+"'";
				count.add(nearMissService.countHql(hql));
			}
	 		deptSns+=" n.department.departmentSn like '"+de.getDepartmentSn()+"%' or ";
		}
		deptSns=deptSns.substring(0,deptSns.length()-4);
		XSSFWorkbook wb = new XSSFWorkbook(); 
		XSSFSheet sheet = null;
		String name="";
		String departmentName=departmentService.getByDepartmentSn(departmentSn).getDepartmentName();
		String  departmentTypeName="";
		if(departmentTypeSn!=null && !departmentTypeSn.equals("") && !departmentTypeSn.equals("null")){
			departmentTypeName=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn).getDepartmentTypeName();
		}
		//��̬���ɱ���
        name=departmentName+"-"+departmentTypeName;
		sheet = wb.createSheet(name);
        sheet.setColumnWidth(1,100*100);
        //��һ��
        XSSFRow row = sheet.createRow((int) 0);  
        XSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //��һ�е�1����Ԫ��
        XSSFCell cell = row.createCell(0) ;
        cell.setCellValue("���"); 
        cell.setCellStyle(style);  
        //��һ�е�2����Ԫ��
        cell = row.createCell(1);  
        cell.setCellValue("��λ");  
        cell.setCellStyle(style);
        //��һ�е�3����Ԫ�� �������ռ�����
        cell = row.createCell(2); 
        cell.setCellValue("�¼����");  
        cell.setCellStyle(style);
        //��һ�е�4����Ԫ��
        cell = row.createCell(3);  
        cell.setCellValue("���յȼ�");  
        cell.setCellStyle(style);
//        //�ڶ���
        row = sheet.createRow((int) 1);
        for (int i = 0;i < typeName.size()+5; i++)  
        { 
        	if(i<2){
	            //��2�е�1,2����Ԫ��
        		row.createCell(i) .setCellValue("");
        	}
            else if(i==typeName.size()+2){
                row.createCell(i).setCellValue("һ�����");
            }
            else if(i==typeName.size()+3){
                row.createCell(typeName.size()+3).setCellValue("�еȷ���");
            }
            else if(i==typeName.size()+4){
                row.createCell(typeName.size()+4).setCellValue("��������");
            }
            else{
                row.createCell(i).setCellValue(typeName.get(i-2));
            }
        }
        //���ɱ������ //��������Խ��
        for (int i = 2; i < deptName.size()+2; i++)  
        {  
        	//��
            row = sheet.createRow((int) i);
            //��
            row.createCell( 0).setCellValue(i-1);
            row.createCell( 1).setCellValue(deptName.get(i-2));
            //ÿ�е�һ��ͳ��������count�����ڵ�λ��
        	int x=(i-2)*(typeName.size()+3);
            for (int j = 2; j <typeName.size()+5; j++)  
            {  
                row.createCell(j).setCellValue(count.get(x));
                x++;
            }
        }
        //��ϸ��
		XSSFSheet detailSheet = null;
		detailSheet = wb.createSheet(name+"��ϸ");
		detailSheet.setColumnWidth(1,100*100);
        XSSFRow detailRow = detailSheet.createRow((int) 0);  
        XSSFCellStyle detailStyle = wb.createCellStyle();  
        detailStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        XSSFCell detailCell = detailRow.createCell(0) ;
        detailCell.setCellValue("�¼����");  
        detailCell=detailRow.createCell(1) ;
        detailCell.setCellValue("�¼�����"); 
        detailCell=detailRow.createCell(2) ;
        detailCell.setCellValue("�¼�����"); 
        detailCell=detailRow.createCell(3) ;
        detailCell.setCellValue("�����ص�"); 
        detailCell=detailRow.createCell(4) ;
        detailCell.setCellValue("Ԥ����ʩ"); 
        detailCell=detailRow.createCell(5) ;
        detailCell.setCellValue("ԭ�����"); 
        detailCell=detailRow.createCell(6) ;
        detailCell.setCellValue("���"); 
        detailCell=detailRow.createCell(7) ;
        detailCell.setCellValue("����ʱ��"); 
        detailCell=detailRow.createCell(8) ;
        detailCell.setCellValue("�ϱ�ʱ��"); 
        detailCell=detailRow.createCell(9) ;
        detailCell.setCellValue("������λ"); 
        detailCell=detailRow.createCell(10) ;
        detailCell.setCellValue("�¼�״̬"); 
        detailCell=detailRow.createCell(11) ;
        detailCell.setCellValue("�¼�����"); 
        detailCell=detailRow.createCell(12) ;
        detailCell.setCellValue("�ϱ���"); 
        detailCell=detailRow.createCell(13) ;
        detailCell.setCellValue("Σ�յȼ�"); 
        StringBuffer hqlnearmiss = new StringBuffer();
//        if(departmentTypeSn!=null && !departmentTypeSn.equals("") && !departmentTypeSn.equals("null")){
    	hqlnearmiss.append("select n from NearMiss n where ("+deptSns+") and n.deleted=false and n.happenDate between '"+begintime+"' and '"+endtime+"'");
//        }else{
    	//hqlnearmiss.append("select n from NearMiss n where n.department.departmentSn like '"+departmentSn+"%' and n.deleted=false and n.happenDate between '"+begintime+"' and '"+endtime+"'");
	    events=(List<NearMiss>)nearMissService.findByPage(hqlnearmiss.toString(), 1, 10000);	    
		for(int i=0;i< events.size();i++){
        	detailRow = detailSheet.createRow((int) i + 1);
            NearMiss u=events.get(i);
            detailRow.createCell( 0).setCellValue(u.getNearMissSn());
            detailRow.createCell( 1).setCellValue(u.getEventName());
            detailRow.createCell( 2).setCellValue(u.getEventProcess());
            detailRow.createCell( 3).setCellValue(u.getHappenLocation());
            detailRow.createCell( 4).setCellValue(u.getPreventMeasure());
            detailRow.createCell( 5).setCellValue(u.getReasonAnalysis());
            detailRow.createCell( 6).setCellValue(u.getRiskResult());
            if(u.getHappenDate()!=null)
            detailRow.createCell( 7).setCellValue(u.getHappenDate().toString());
            if(u.getReportTime()!=null)
            detailRow.createCell( 8).setCellValue(u.getReportTime().toString());
            if(u.getDepartment()!=null)
            detailRow.createCell( 9).setCellValue(u.getDepartment().getDepartmentName());
            if(u.getNearMissState()!=null)
            detailRow.createCell( 10).setCellValue(u.getNearMissState().toString());
            if(u.getNearMissType()!=null)
            detailRow.createCell( 11).setCellValue(u.getNearMissType().getNearMissTypeName());
            if(u.getReportPerson()!=null)
            detailRow.createCell( 12).setCellValue(u.getReportPerson().getPersonName());
            if(u.getRiskLevel()!=null)
            detailRow.createCell( 13).setCellValue(u.getRiskLevel().toString());         
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
	public String getHasType() {
		return hasType;
	}
	public void setHasType(String hasType) {
		this.hasType = hasType;
	}
	public Integer getRiskLevelSn() {
		return riskLevelSn;
	}
	public void setRiskLevelSn(Integer riskLevelSn) {
		this.riskLevelSn = riskLevelSn;
	}
	public Integer getNearMissAuditTypeSn() {
		return nearMissAuditTypeSn;
	}
	public void setNearMissAuditTypeSn(Integer nearMissAuditTypeSn) {
		this.nearMissAuditTypeSn = nearMissAuditTypeSn;
	}
	
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
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
	public NearMissAuditType getNearMissAuditType() {
		return nearMissAuditType;
	}
	public void setNearMissAuditType(NearMissAuditType nearMissAuditType) {
		this.nearMissAuditType = nearMissAuditType;
	}
	public String getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}
	public Person getAuditor() {
		return auditor;
	}
	public void setAuditor(Person auditor) {
		this.auditor = auditor;
	}
	public Timestamp getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public NearMissState getNearMissState() {
		return nearMissState;
	}
	public void setNearMissState(NearMissState nearMissState) {
		this.nearMissState =  nearMissState;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNearMissSn() {
		return nearMissSn;
	}
	public void setNearMissSn(String nearMissSn) {
		this.nearMissSn = nearMissSn;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}

	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public java.sql.Date getHappenDate() {
		return happenDate;
	}

	public void setHappenDate(java.sql.Date happenDate) {
		this.happenDate = happenDate;
	}

	public String getHappenLocation() {
		return happenLocation;
	}

	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	public String getNearMissTypeSn() {
		return nearMissTypeSn;
	}

	public void setNearMissTypeSn(String nearMissTypeSn) {
		this.nearMissTypeSn = nearMissTypeSn;
	}

	public String getRiskResult() {
		return riskResult;
	}

	public void setRiskResult(String riskResult) {
		this.riskResult = riskResult;
	}
	public String getEventProcess() {
		return eventProcess;
	}

	public void setEventProcess(String eventProcess) {
		this.eventProcess = eventProcess;
	}

	public String getReasonAnalysis() {
		return reasonAnalysis;
	}

	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}

	public String getPreventMeasure() {
		return preventMeasure;
	}

	public void setPreventMeasure(String preventMeasure) {
		this.preventMeasure = preventMeasure;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Timestamp getReportTime() {
		return reportTime;
	}

	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getNearMissStateSn() {
		return nearMissStateSn;
	}
	public void setNearMissStateSn(Integer nearMissStateSn) {
		this.nearMissStateSn = nearMissStateSn;
	}
	public java.sql.Date getBegintime() {
		return begintime;
	}
	public void setBegintime(java.sql.Date begintime) {
		this.begintime = begintime;
	}
	public java.sql.Date getEndtime() {
		return endtime;
	}
	public void setEndtime(java.sql.Date endtime) {
		this.endtime = endtime;
	}
}
