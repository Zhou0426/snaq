package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import cn.jagl.aq.domain.CheckType;
import cn.jagl.aq.domain.CheckerFrom;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.InconformityItemNature;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Sms;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeActMark;
import cn.jagl.aq.domain.UnsafeActStandard;
import cn.jagl.util.RandomUtil;
import cn.jagl.util.RegExUtil;
import cn.jagl.util.SmsUtil;

public class UnsafeActAction extends BaseAction<UnsafeAct> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private CheckType checkType;//������ͣ���̬��顢���ڼ�顢ר����	
	private CheckerFrom checkerFrom;//���������
	private String inconformityItemSn;//����������
	private String checkers;//����Ա
	private String checkedDepartmentSn;//���첿��
	private String checkDeptSn;//��鲿��
	private Timestamp checkDateTime;//���ʱ��
	private String checkLocation;//���ص�
	private InconformityItemNature inconformityItemNature;//������������
	private String specialitySn;//����רҵ
	private String attachments;//����
	private String violatorId;//����ȫ��Ϊ��Ա
	private String inconformityLevel;//����ȫ��Ϊ�ȼ��򲻷�����ȼ�--û�õ�
	private String actDescription;//��Ϊ����
	private UnsafeActMark unsafeActMark;//�ۼ����кۡ��޺�
	private String unsafeActStandardSn;//����ȫ��Ϊ��׼
	private String unsafeActLevelSn;//����ȫ��Ϊ�ȼ����
	private Long total;//����
	private String q;//ǰ̨����������������
	private String pag;//��װ����
	private String checkTypeSn;//������͵���ֵ
	private String checkerFromSn;//��������Ե���ֵ
	private String periodicalCheckSn;//���ڼ����
	private String specialCheckSn;//ר������
	private String auditSn;
	private String departmentSn;
	private String departmentTypeSn;//�������ͱ��
	private String timeData;//ʱ�䰴ť������
	private Timestamp beginTime;//��ʼʱ��
	private Timestamp endTime;//����ʱ��
	private InputStream inputStream;//�����
	private String fileName;//�����ļ���
	private Map<String, Object> mapArray=new HashMap<String, Object>();
	
	
	
	public String getCheckDeptSn() {
		return checkDeptSn;
	}
	public void setCheckDeptSn(String checkDeptSn) {
		this.checkDeptSn = checkDeptSn;
	}
	public String getInconformityLevel() {
		return inconformityLevel;
	}
	public void setInconformityLevel(String inconformityLevel) {
		this.inconformityLevel = inconformityLevel;
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
	public String getUnsafeActLevelSn() {
		return unsafeActLevelSn;
	}
	public void setUnsafeActLevelSn(String unsafeActLevelSn) {
		this.unsafeActLevelSn = unsafeActLevelSn;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}
	public String getSpecialCheckSn() {
		return specialCheckSn;
	}
	public void setSpecialCheckSn(String specialCheckSn) {
		this.specialCheckSn = specialCheckSn;
	}
	public String getPeriodicalCheckSn() {
		return periodicalCheckSn;
	}
	public void setPeriodicalCheckSn(String periodicalCheckSn) {
		this.periodicalCheckSn = periodicalCheckSn;
	}
	public String getPag() {
		return pag;
	}
	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getCheckTypeSn() {
		return checkTypeSn;
	}
	public void setCheckTypeSn(String checkTypeSn) {
		this.checkTypeSn = checkTypeSn;
	}
	public String getCheckerFromSn() {
		return checkerFromSn;
	}
	public void setCheckerFromSn(String checkerFromSn) {
		this.checkerFromSn = checkerFromSn;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInconformityItemSn() {
		return inconformityItemSn;
	}
	public void setInconformityItemSn(String inconformityItemSn) {
		this.inconformityItemSn = inconformityItemSn;
	}
	public String getCheckers() {
		return checkers;
	}
	public void setCheckers(String checkers) {
		this.checkers = checkers;
	}
	public String getCheckedDepartmentSn() {
		return checkedDepartmentSn;
	}
	public void setCheckedDepartmentSn(String checkedDepartmentSn) {
		this.checkedDepartmentSn = checkedDepartmentSn;
	}
	public Timestamp getCheckDateTime() {
		return checkDateTime;
	}
	public void setCheckDateTime(Timestamp checkDateTime) {
		this.checkDateTime = checkDateTime;
	}
	public String getCheckLocation() {
		return checkLocation;
	}
	public void setCheckLocation(String checkLocation) {
		this.checkLocation = checkLocation;
	}
	
	public CheckType getCheckType() {
		return checkType;
	}
	public void setCheckType(CheckType checkType) {
		this.checkType = checkType;
	}
	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}
	public UnsafeActMark getUnsafeActMark() {
		return unsafeActMark;
	}
	public void setUnsafeActMark(UnsafeActMark unsafeActMark) {
		this.unsafeActMark = unsafeActMark;
	}
	public String getUnsafeActStandardSn() {
		return unsafeActStandardSn;
	}
	public void setUnsafeActStandardSn(String unsafeActStandardSn) {
		this.unsafeActStandardSn = unsafeActStandardSn;
	}
	public InconformityItemNature getInconformityItemNature() {
		return inconformityItemNature;
	}
	public void setInconformityItemNature(InconformityItemNature inconformityItemNature) {
		this.inconformityItemNature = inconformityItemNature;
	}
	public String getSpecialitySn() {
		return specialitySn;
	}
	public void setSpecialitySn(String specialitySn) {
		this.specialitySn = specialitySn;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	public String getViolatorId() {
		return violatorId;
	}
	public void setViolatorId(String violatorId) {
		this.violatorId = violatorId;
	}
	public String getActDescription() {
		return actDescription;
	}
	public void setActDescription(String actDescription) {
		this.actDescription = actDescription;
	}
	
	
	@SuppressWarnings("unchecked")
	public String query() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray=new JSONArray();
		StringBuffer hqll=new StringBuffer();
		hqll.append("select u from UnsafeAct u where u.deleted=false");
		//��̬�������
		if(checkTypeSn!=null && !"".equals(checkTypeSn) && checkerFromSn!=null && !"".equals(checkerFromSn)){
			int checkTypeNum=Integer.parseInt(checkTypeSn);
			int checkerFromNum=Integer.parseInt(checkerFromSn);
			hqll.append(" and u.checkType='"+checkTypeNum+"' and u.checkerFrom='"+checkerFromNum+"'");
		}
		//���ڼ��
		if(periodicalCheckSn!=null && !"".equals(periodicalCheckSn)){
			hqll.append(" and u.periodicalCheck.periodicalCheckSn='"+periodicalCheckSn+"'");
		}
		//ר����
		if(specialCheckSn!=null && !"".equals(specialCheckSn)){
			hqll.append(" and u.specialCheck.specialCheckSn='"+specialCheckSn+"'");
		}
		if(auditSn!=null){
			hqll.append(" and u.systemAudit.auditSn='"+auditSn+"'");
		}
		
		//�ж���Ա����ʾ����
		HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
		Person loginPerson=personService.getByPersonId((String) session.get("personId"));
		//�ж��Ƿ���ڼ���ϵͳ����Ա
		if(!roles.containsKey("jtxtgly")){
			//���Լ�����������ӻ�λ���Ƕ�̬����
			if(checkTypeSn!=null && !"".equals(checkTypeSn) && checkerFromSn!=null && !"".equals(checkerFromSn) && Integer.parseInt(checkTypeSn) == 0 && Integer.parseInt(checkerFromSn) < 2){
				if(roles.containsKey("dwxtgly")){
					List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(loginPerson.getDepartment().getDepartmentSn());
					Department department;
			    	if(departmentTypes.size()>0){
			    		department=loginPerson.getDepartment();
			    	}else{
			    		department=departmentService.getUpNearestImplDepartment(loginPerson.getDepartment().getDepartmentSn());
			    	}
			    	hqll.append(" AND (u.editor.personId='"+loginPerson.getPersonId()+"' OR u.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%')");
				}else{
					hqll.append(" AND u.editor.personId='"+loginPerson.getPersonId()+"'");
				}
			//��̬��顢���������ҵ�������ϵ�
			}else if(checkTypeSn!=null && !"".equals(checkTypeSn) && checkerFromSn!=null && !"".equals(checkerFromSn) && Integer.parseInt(checkTypeSn) == 0 && Integer.parseInt(checkerFromSn) >= 2){
				hqll.append(" AND u.editor.personId='"+loginPerson.getPersonId()+"'");
			}
		}
		hqll.append(" order by u.checkDateTime desc");
		String hql=hqll.toString();
		//��ҳ��ѯ
		jsonList=unsafeActService.findByPage(hql, page, rows);
		//��ѯ����
		hql=hql.replaceFirst("u", "count(*)");
		total=unsafeActService.countByHql(hql);
		//����
		for(UnsafeAct unsafeAct:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", unsafeAct.getId());//id
			jo.put("inconformityItemSn",unsafeAct.getInconformityItemSn());//���
			jo.put("checkDateTime", unsafeAct.getCheckDateTime());//���ʱ��
			jo.put("checkLocation", unsafeAct.getCheckLocation());//����
			jo.put("inconformityItemNature",unsafeAct.getInconformityItemNature());//������������
			jo.put("checkType", unsafeAct.getCheckType());
			jo.put("checkerFrom", unsafeAct.getCheckerFrom());
			Set<Person> persons=unsafeAct.getCheckers();
			if(persons.size()>0){
				String name="";
				String pId="";
				for(Person per:persons){
					name+=per.getPersonName()+",";
					pId+=per.getId()+",";
				}
				name=name.substring(0, name.length()-1);
				pId=pId.substring(0, pId.length()-1);
				jo.put("checkersId", pId);
				jo.put("checkers", name);
			}else{
				jo.put("checkersId", "��");
				jo.put("checkers", unsafeAct.getStrCheckers());
			}
			if(unsafeAct.getSystemAudit()!=null){
				jo.put("systemAudit", unsafeAct.getSystemAudit().getSystemAuditType());
				jo.put("systemAuditSn", unsafeAct.getSystemAudit().getAuditSn());
			}
			if(unsafeAct.getCheckedDepartment()!=null){
				jo.put("checkedDepartmentSn",unsafeAct.getCheckedDepartment().getDepartmentSn());
				jo.put("checkedDepartment",unsafeAct.getCheckedDepartment().getDepartmentName());
				jo.put("implDepartmentName",unsafeAct.getCheckedDepartment().getImplDepartmentName());
			}
			if(unsafeAct.getSpeciality()!=null){
				jo.put("specialitySn", unsafeAct.getSpeciality().getSpecialitySn());
				jo.put("speciality", unsafeAct.getSpeciality().getSpecialityName());
			}
			jo.put("inconformityLevel",unsafeAct.getInconformityLevel());//��������ȼ�
			jo.put("attachment",unsafeAct.getAttachments().size());
			if(unsafeAct.getViolator()!=null){
				jo.put("violator", unsafeAct.getViolator().getPersonName());
				jo.put("violatorId", unsafeAct.getViolator().getPersonId());
			}
			jo.put("actDescription", unsafeAct.getActDescription());
			jo.put("unsafeActMark", unsafeAct.getUnsafeActMark());
			if(unsafeAct.getUnsafeActStandard()!=null){
				jo.put("unsafeActStandard", unsafeAct.getUnsafeActStandard().getStandardDescription());
				jo.put("unsafeActStandardSn", unsafeAct.getUnsafeActStandard().getStandardSn());
				jo.put("unsafeActLevel", unsafeAct.getUnsafeActStandard().getUnsafeActLevel());
			}
			if(unsafeAct.getEditor()!=null){
				jo.put("editor", unsafeAct.getEditor().getPersonName());
				jo.put("editorId", unsafeAct.getEditor().getPersonId());
			}else{
				jo.put("editor", "��");
				jo.put("editorId", "��");
			}
			jsonArray.put(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total�� ����ܼ�¼���������
        json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
        out.print(JSONObject.valueToString(json));
		out.flush();
		out.close();
		return SUCCESS;
		
	}
	
//	//��ѯ����ȫ��Ϊ��Ӧ�ı�׼ 
//	public String findStandardShow() throws IOException{
//		HttpServletResponse response=ServletActionContext.getResponse();  
//        response.setContentType("text/html");  
//        response.setContentType("text/plain; charset=utf-8");
//        PrintWriter out = response.getWriter();
//        JSONArray jsonArray=new JSONArray();
//        String hql="";
//		if(q!=null && !"".equals(q)){
//			hql="select u from UnsafeActStandard u where u.standardSn like '%"+q+"%' or u.standardDescription like '%"+q+"%'";
//		}else{
//			hql="select u from UnsafeActStandard u";
//		}
//		List<UnsafeActStandard> unsafeActStandardList=unsafeActStandardService.findByPage(hql, 1, 25);
//		for(UnsafeActStandard unsafeActStandard:unsafeActStandardList){
//			JSONObject jo=new JSONObject();
//			jo.put("id", unsafeActStandard.getStandardSn());
//			jo.put("text", unsafeActStandard.getStandardDescription());
//			jo.put("value", unsafeActStandard.getUnsafeActLevel());
//			Set<Speciality> set=unsafeActStandard.getSpecialities();
//			if(set.size()>0){
//				String a="";
//				for(Speciality spec:set){
//					a+=spec.getSpecialitySn()+",";
//				}
//				jo.put("speciality", a);
//			}
//			jsonArray.put(jo);
//		}
//		out.println(jsonArray.toString()); 
//	    out.flush(); 
//	    out.close(); 
//		
//		return SUCCESS;
//	}
	//��ѯ����ȫ��Ϊ��Ӧ�ı�׼ 
		public String findStandard() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        JSONArray jsonArray=new JSONArray();
			if(q!=null && !"".equals(q)){
				if(!RegExUtil.isSn(q)){
					q=q+actDescription;
				}
			}else{
				q=actDescription;
			}
			Person person=personService.getByPersonId(violatorId);
			Department de=departmentService.getUpNearestImplDepartment(person.getDepartment().getDepartmentSn());
			List<UnsafeActStandard> unsafeActStandardList=unsafeActStandardService.getStandardByFullTextQuery(q,de.getDepartmentType().getDepartmentTypeSn(),1, 30);
			for(UnsafeActStandard unsafeActStandard:unsafeActStandardList){
				JSONObject jo=new JSONObject();
				jo.put("id", unsafeActStandard.getStandardSn());
				jo.put("text", unsafeActStandard.getStandardDescription());
				jo.put("value", unsafeActStandard.getUnsafeActLevel());
				Set<Speciality> set=unsafeActStandard.getSpecialities();
				if(set.size()>0){
					String a="";
					for(Speciality spec:set){
						a+=spec.getSpecialitySn()+",";
					}
					jo.put("speciality", a);
				}else{
					jo.put("speciality", "");
				}
				jsonArray.put(jo);
			}
			out.println(jsonArray.toString()); 
		    out.flush(); 
		    out.close(); 
			
			return SUCCESS;
		}
	//��Ӳ���ȫ��Ϊ
	public String saveUnsafeAct(){
		UnsafeAct unsafeAct=new UnsafeAct();
		unsafeAct.setCheckDateTime(checkDateTime);
		unsafeAct.setCheckLocation(checkLocation);
		unsafeAct.setActDescription(actDescription);
		unsafeAct.setInconformityItemNature(inconformityItemNature);
		unsafeAct.setCheckType(checkType);
		unsafeAct.setCheckerFrom(checkerFrom);
		unsafeAct.setUnsafeActMark(unsafeActMark);
		unsafeAct.setDeleted(false);
		Person person = new Person();
		try{
			unsafeAct.setInconformityItemSn(new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date()));
			person=personService.getByPersonId(violatorId);
			unsafeAct.setViolator(person);
			Department department;
			if(departmentSn!=null&&departmentSn.trim().length()>0){
				department=departmentService.getByDepartmentSn(departmentSn);
			}else{
				department=person.getDepartment();
			}
			unsafeAct.setCheckedDepartment(department);
			Speciality speciality=specialityService.getBySpecialitySn(specialitySn);
			unsafeAct.setSpeciality(speciality);
			UnsafeActStandard unsafeActStandard=unsafeActStandardService.getByUnsafeActStandardSn(unsafeActStandardSn);
			//���ڼ��
			if(periodicalCheckService.getByPeriodicalCheckSn(periodicalCheckSn)!=null){
				unsafeAct.setPeriodicalCheck(periodicalCheckService.getByPeriodicalCheckSn(periodicalCheckSn));
			}
			//ר����
			if(specialCheckService.getBySpecialCheckSn(specialCheckSn)!=null){
				unsafeAct.setSpecialCheck(specialCheckService.getBySpecialCheckSn(specialCheckSn));
			}
			if(unsafeActStandard!=null){
				if(unsafeActStandard.getChildren().size()!=0){
					pag=LOGIN;
					return "jsonList";
				}else{
					unsafeAct.setUnsafeActStandard(unsafeActStandard);
				}
			}
			//�����Ա��
			if(ids.length()>0){
				if(checkerFrom == null && pag == null){
					unsafeAct.setStrCheckers(ids);
				}else{
					if(checkerFrom != null && (checkerFrom.equals(CheckerFrom.��) || checkerFrom.equals(CheckerFrom.�ⲿ))){
						unsafeAct.setStrCheckers(ids);
					}else{
						List<Person> personList=personService.getByPersonIds(ids);
						Set<Person> set = new HashSet<Person>(personList);
						unsafeAct.setCheckers(set);
					}
				}
			}
			//���
			unsafeAct.setSystemAudit(systemAuditService.getById(id));
			//¼����
			String pId=(String) session.get("personId");
			Person person2 = personService.getByPersonId(pId);
			unsafeAct.setEditor(person2);
			unsafeActService.save(unsafeAct);
			
			//�����Ÿ�����ȫ��Ϊ��Ա�������ڲ��ŵİ�ȫ������
			String serialNumber = "";
			String messageContent = "";
			Sms sms = null;
			if(person.getCellphoneNumber() != null && person.getCellphoneNumber().length() == 11){
				//����ȫ��Ϊ��Ա
				serialNumber = RandomUtil.getRandmDigital20();
				messageContent = "�����µĲ���ȫ��Ϊ��¼��";
				if(actDescription != null && !"".equals(actDescription)){
					messageContent = messageContent + "��" + actDescription;
				}
				if(checkDateTime !=null && !"".equals(checkDateTime)){
					messageContent = messageContent + "�����ʱ��Ϊ��" + checkDateTime;
				}
				if(checkLocation != null && !"".equals(checkLocation)){
					messageContent = messageContent + "�����ص�Ϊ��" + checkLocation;
				}
				if(unsafeActStandard.getUnsafeActLevel() != null){
					messageContent = messageContent + "���ȼ�Ϊ��" + unsafeActStandard.getUnsafeActLevel().toString();
				}
				
				messageContent = messageContent + "���뾡��ǰ��ϵͳ�鿴��";
				int result = SmsUtil.sendSms(serialNumber, person.getCellphoneNumber(), messageContent);
				//д��ʵ��Sms
				sms = new Sms();
				sms.setSerialNumber(serialNumber);
				sms.setUserNumber(person.getCellphoneNumber());
				sms.setMessageContent(messageContent);
				sms.setResultCode(result);
				sms.setSuccessTimestamp(new Timestamp(System.currentTimeMillis()));
				smsService.save(sms);
				//����ȫ��Ϊ��Ա���ڲ��ŵİ�ȫ������
				if(person.getDepartment().getPrincipal() != null && person.getDepartment().getPrincipal().getCellphoneNumber() != null && person.getDepartment().getPrincipal().getCellphoneNumber().length() == 11){
					
					serialNumber = RandomUtil.getRandmDigital20();
					messageContent = "������ܵĲ������µĲ���ȫ��Ϊ��Ա��¼�룬����ȫ��Ϊ��ԱΪ��" + person.getPersonName();
					
					if(actDescription != null && !"".equals(actDescription)){
						messageContent = messageContent + "����Ϊ����Ϊ��" + actDescription;
					}
					if(checkDateTime !=null && !"".equals(checkDateTime)){
						messageContent = messageContent + "�����ʱ��Ϊ��" + checkDateTime;
					}
					if(checkLocation != null && !"".equals(checkLocation)){
						messageContent = messageContent + "�����ص�Ϊ��" + checkLocation;
					}
					if(unsafeActStandard.getUnsafeActLevel() != null){
						messageContent = messageContent + "���ȼ�Ϊ��" + unsafeActStandard.getUnsafeActLevel().toString();
					}
					
					
					messageContent = messageContent + "���뾡��ǰ��ϵͳ�鿴��";
					result = SmsUtil.sendSms(serialNumber, person.getDepartment().getPrincipal().getCellphoneNumber(), messageContent);
					//д��ʵ��Sms
					sms = new Sms();
					sms.setSerialNumber(serialNumber);
					sms.setUserNumber(person.getDepartment().getPrincipal().getCellphoneNumber());
					sms.setMessageContent(messageContent);
					sms.setResultCode(result);
					sms.setSuccessTimestamp(new Timestamp(System.currentTimeMillis()));
					smsService.save(sms);
				}
			}
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
			return "jsonList";
		}		
		return "jsonList";
	}
	//ɾ������ȫ��Ϊ
	public String deleteUnsafeAct(){
		try{
			unsafeActService.deleteByIds(ids);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
			return "jsonList";
		}
		return "jsonList";
	}
	//����ʵ��
	public String updateUnsafeAct(){
		try{
			UnsafeAct unsafeAct=unsafeActService.getById(id);
			Person person=personService.getByPersonId(violatorId);
			unsafeAct.setViolator(person);
			Department department;
			if(departmentSn!=null&&departmentSn.length()>0){
				department=departmentService.getByDepartmentSn(departmentSn);
			}else{
				department=person.getDepartment();
			}
			unsafeAct.setCheckedDepartment(department);
			unsafeAct.setCheckDateTime(checkDateTime);
			unsafeAct.setCheckLocation(checkLocation);
			//�����Ա��
			if(ids.length()>0){
				if(unsafeAct.getCheckerFrom()==null){
					unsafeAct.setStrCheckers(ids);
				}else{
					if(unsafeAct.getCheckerFrom().equals(CheckerFrom.��) || unsafeAct.getCheckerFrom().equals(CheckerFrom.�ⲿ)){
						unsafeAct.setStrCheckers(ids);
					}else{
						List<Person> personList=personService.getByPersonIds(ids);
						Set<Person> set = new HashSet<Person>(personList);
						unsafeAct.setCheckers(set);
					}
				}
			}else{
				unsafeAct.setCheckers(null);
				unsafeAct.setStrCheckers(null);
			}
			unsafeAct.setActDescription(actDescription);
			UnsafeActStandard unsafeActStandard=unsafeActStandardService.getByUnsafeActStandardSn(unsafeActStandardSn);
			if(unsafeActStandard!=null){
				if(unsafeActStandard.getChildren().size()!=0){
					pag=LOGIN;
					return "jsonList";
				}else{
					unsafeAct.setUnsafeActStandard(unsafeActStandard);
				}
			}else{
				unsafeAct.setUnsafeActStandard(null);
			}
			Speciality speciality=specialityService.getBySpecialitySn(specialitySn);
			unsafeAct.setSpeciality(speciality);
			unsafeAct.setUnsafeActMark(unsafeActMark);
			unsafeActService.update(unsafeAct);
			
			//�����Ÿ�����ȫ��Ϊ��Ա�������ڲ��ŵİ�ȫ������
			String serialNumber = "";
			String messageContent = "";
			Sms sms = null;
			if(person.getCellphoneNumber() != null && person.getCellphoneNumber().length() == 11){
				//����ȫ��Ϊ��Ա
				serialNumber = RandomUtil.getRandmDigital20();
				messageContent = "�����µĲ���ȫ��Ϊ���޸�";
				if(actDescription != null && !"".equals(actDescription)){
					messageContent = messageContent + "��" + actDescription;
				}
				if(checkDateTime !=null && !"".equals(checkDateTime)){
					messageContent = messageContent + "�����ʱ��Ϊ��" + checkDateTime;
				}
				if(checkLocation != null && !"".equals(checkLocation)){
					messageContent = messageContent + "�����ص�Ϊ��" + checkLocation;
				}
				if(unsafeActStandard.getUnsafeActLevel() != null){
					messageContent = messageContent + "���ȼ�Ϊ��" + unsafeActStandard.getUnsafeActLevel().toString();
				}
				
				
				messageContent = messageContent + "���뾡��ǰ��ϵͳ�鿴��";
				int result = SmsUtil.sendSms(serialNumber, person.getCellphoneNumber(), messageContent);
				//д��ʵ��Sms
				sms = new Sms();
				sms.setSerialNumber(serialNumber);
				sms.setUserNumber(person.getCellphoneNumber());
				sms.setMessageContent(messageContent);
				sms.setResultCode(result);
				sms.setSuccessTimestamp(new Timestamp(System.currentTimeMillis()));
				smsService.save(sms);
				//����ȫ��Ϊ��Ա���ڲ��ŵİ�ȫ������
				if(person.getDepartment().getPrincipal() != null && person.getDepartment().getPrincipal().getCellphoneNumber() != null && person.getDepartment().getPrincipal().getCellphoneNumber().length() == 11){
					
					serialNumber = RandomUtil.getRandmDigital20();
					messageContent = "������ܵĲ������µĲ���ȫ��Ϊ��Ա���޸ģ�����ȫ��Ϊ��ԱΪ��" + person.getPersonName();
					if(actDescription != null && !"".equals(actDescription)){
						messageContent = messageContent + "����Ϊ����Ϊ��" + actDescription;
					}
					if(checkDateTime !=null && !"".equals(checkDateTime)){
						messageContent = messageContent + "�����ʱ��Ϊ��" + checkDateTime;
					}
					if(checkLocation != null && !"".equals(checkLocation)){
						messageContent = messageContent + "�����ص�Ϊ��" + checkLocation;
					}
					if(unsafeActStandard.getUnsafeActLevel() != null){
						messageContent = messageContent + "���ȼ�Ϊ��" + unsafeActStandard.getUnsafeActLevel().toString();
					}
					
					
					messageContent = messageContent + "���뾡��ǰ��ϵͳ�鿴��";
					result = SmsUtil.sendSms(serialNumber, person.getDepartment().getPrincipal().getCellphoneNumber(), messageContent);
					//д��ʵ��Sms
					sms = new Sms();
					sms.setSerialNumber(serialNumber);
					sms.setUserNumber(person.getDepartment().getPrincipal().getCellphoneNumber());
					sms.setMessageContent(messageContent);
					sms.setResultCode(result);
					sms.setSuccessTimestamp(new Timestamp(System.currentTimeMillis()));
					smsService.save(sms);
				}
			}
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
			return "jsonList";
		}
		return "jsonList";
	}
	/**
	 * ��������--����ȫ��Ϊ��ѯ
	 * departmentSn-���ű��
	 * departmentTypeSn-�������ͱ��
	 * specialitySn-רҵ���
	 * unsafeActStandardSn-����ȫ��Ϊ��׼���
	 * @author ����ɭ
	 * @throws IOException 
	 */
	public String showData() throws IOException{
		String str = "";
       if(departmentTypeSn != null && !"".equals(departmentTypeSn) 
    		   && departmentSn != null && !"".equals(departmentSn))
       {
		  	str = departmentService.getDownDepartmentByDepartmentType(departmentSn,departmentTypeSn);
		  	str = str.replaceAll("i.departmentSn", "i.checkedDepartment.departmentSn");
       }
       if(checkers != null && !"".equals(checkers))
       {
    	   	List<?> unsafeConditionList = unsafeConditionService.getBySql(
    			   "select inconformity_item_sn from inconformity_item_checker"
    			   + " where person_id='" + checkers + "'");
    	   
			checkers = "(";
			
			for(Object un : unsafeConditionList)
			{
				checkers = checkers + "'" + un + "',";
			}
			
			checkers = checkers.substring(0, checkers.length()-1);
			
			checkers += ")";
	   }
       mapArray = unsafeActService.showData(checkDeptSn, departmentSn, 
    		   str, specialitySn, unsafeActStandardSn, checkerFromSn, 
    		   checkTypeSn, unsafeActLevelSn, timeData, beginTime, 
    		   endTime, checkers, page, rows);
       return SUCCESS;
	}
	/**
	 * �����������ѯ����Ա���µļ������
	 */
	public String showDataPersonal() throws IOException{
			String str="";
			//ʱ��Ϊ����
			if((beginTime == null || "".equals(beginTime)) 
					&& (endTime == null || "".equals(endTime)) 
					&& (timeData ==null || "".equals(timeData))){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    		Calendar cal =Calendar.getInstance();
	    		
	    		cal.set(Calendar.DAY_OF_MONTH,1);		//����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
	    		beginTime=Timestamp.valueOf(df.format(cal.getTime())+" 00:00:00");
    			  
    			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
    			endTime=Timestamp.valueOf(df.format(cal.getTime())+" 00:00:00");
			}
			if(checkers != null && !"".equals(checkers))
	        {
	    	   	List<?> unsafeConditionList = unsafeConditionService.getBySql(
	    			   "select inconformity_item_sn from inconformity_item_checker"
	    			   + " where person_id='" + checkers + "'");
	    	   
				checkers = "(";
				
				for(Object un : unsafeConditionList)
				{
					checkers = checkers + "'" + un + "',";
				}
				
				checkers = checkers.substring(0, checkers.length()-1);
				
				checkers += ")";
		    }
			mapArray = unsafeActService.showData(checkDeptSn, departmentSn, 
		    		   str, specialitySn, unsafeActStandardSn, checkerFromSn, 
		    		   checkTypeSn, unsafeActLevelSn, timeData, beginTime, 
		    		   endTime, checkers, page, rows);
		
		return SUCCESS;
	}
	/**
	 * ��ѯ�ҵĲ���ȫ��Ϊ
	 * @return
	 */
	public String myUnsafeAct(){
		String personId = (String) session.get("PersonId");
		mapArray=unsafeActService.queryMyUnsafeAct(personId, page, rows);
		return SUCCESS;
	}
	//���ݲ������Ͳ��Ҳ���ȫ��Ϊ��׼
	public String standard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        JSONArray jsonArray=new JSONArray();
        String hql="";
        if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
        	if(q!=null && !"".equals(q)){
        		hql="select u from UnsafeActStandard u where u.departmentType.departmentTypeSn='"+departmentTypeSn+"' and (u.standardSn like '%"+q+"%' or u.standardDescription like '%"+q+"%')";
        	}else{
        		hql="select u from UnsafeActStandard u where u.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
        	}
        	List<UnsafeActStandard> unsafeActStandardList=unsafeActStandardService.findByPage(hql, 1, 30);
        	for(UnsafeActStandard uns:unsafeActStandardList){
        		JSONObject jo=new JSONObject();
        		jo.put("standardSn", uns.getStandardSn());
        		jo.put("standardDescription", uns.getStandardDescription());
        		jsonArray.put(jo);
        	}
        }
        out.print(jsonArray.toString());
		out.flush();
		out.close();
		return SUCCESS;
	}
	//����ȫ��Ϊ��ԱԤ��
	public String showViolator() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray=new JSONArray();
        String hql="";
        
          //ƴ��hql���
		  StringBuffer hqll = new StringBuffer();
		  hqll.append("select distinct i from Person i LEFT JOIN i.unsafeActs u where u.id is not null and u.deleted=false");
		  //ʱ��ɸѡ
		  if(endTime!=null && !"".equals(endTime)){
			  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			  Calendar calendar = Calendar.getInstance();
		      calendar.setTime(endTime);
		      calendar.add(Calendar.YEAR, -1);
		      
			  hqll.append(" AND u.checkDateTime between '"+df.format(calendar.getTime())+" 00:00:00' and '"+df.format(endTime)+" 23:59:59'");
		  }else{
			  hqll.append(" AND u.checkDateTime >= '"+LocalDate.now().minusYears(1)+"'");
		  }
		  //�жϲ��������Ƿ�Ϊ�����ж��Ƿ�ѡ��������
		  if(departmentTypeSn !=null && !"".equals(departmentTypeSn) && departmentSn!=null && !"".equals(departmentSn)){
		  		
		  	String str=departmentService.getDownDepartmentByDepartmentType(departmentSn,departmentTypeSn);
		  	str=str.replaceAll("i.departmentSn", "i.department.departmentSn");
		  	hqll.append(" AND ("+str+")");
		  		
		  }else{
		  	hqll.append(" AND i.department.departmentSn like '"+departmentSn+"%'");
		  }
		  
		  //��ѯ���ݵ�hql���
	  	  hql=hqll.toString();
	  	  //��ѯ������hql���
	  	  String countHql=hql.replaceFirst("distinct i", "count(distinct i.id)");
	  	  
	  	  List<Person> personList=personService.findByPage(hql, page, rows);
	  	  Long total=personService.getCountByHql(countHql);
	  	  Long count=0l;
	  	  for(Person person:personList){
			JSONObject jo=new JSONObject();
			jo.put("personId", person.getPersonId());
			jo.put("personName",person.getPersonName());
			if(person.getDepartment()!=null){
				jo.put("implDepartmentName",person.getDepartment().getImplDepartmentName());
				jo.put("department",person.getDepartment().getDepartmentName());
			}
			//��ѯ����ȫ��Ϊ����
			hql="select count(*) from UnsafeAct u where u.violator.personId='"+person.getPersonId()+"' and u.deleted=false";
			count=unsafeActService.countByHql(hql);
			jo.put("unsafeActs",count);
			//�жϷ��յȼ�
			if(count>=3){
				jo.put("riskLevel","�ش����");
			}else{
				int r=0;
				for(UnsafeAct unsafeAct:person.getUnsafeActs()){
					if(unsafeAct.getUnsafeActStandard()!=null){
						if(unsafeAct.getUnsafeActStandard().getUnsafeActLevel().toString().equals("A�಻��ȫ��Ϊ")){
							r=3;
							break;
						}else if(unsafeAct.getUnsafeActStandard().getUnsafeActLevel().toString().equals("B�಻��ȫ��Ϊ")){
							r+=2;
						}else if(unsafeAct.getUnsafeActStandard().getUnsafeActLevel().toString().equals("C�಻��ȫ��Ϊ")){
							r+=1;
						}
					}
				}
				if(r>=3){
					jo.put("riskLevel","�ش����");
				}else if(r==2){
					jo.put("riskLevel","�еȷ���");
				}else{
					jo.put("riskLevel","һ�����");
				}
			}
			jsonArray.put(jo);
	  	  }
	  	  Map<String, Object> json = new HashMap<String, Object>();
	  	  json.put("total", total);// total�� ����ܼ�¼���������
	  	  json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
	  	  out.println(JSONObject.valueToString(json));
	  	  out.flush();
	  	  out.close();
	  	  return SUCCESS;
	}
	public String prewarningShow() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray=new JSONArray();
		String hql="select u from UnsafeAct u where u.deleted=false and u.violator.personId='"+violatorId+"'";
		String countHql=hql.replaceFirst("u", "count(*)");
	  	  
	  	  List<UnsafeAct> jsonList=unsafeActService.findByPage(hql, page, rows);
	  	  total=unsafeActService.countByHql(countHql);
	  	  
	  	  
	  	  for(UnsafeAct inc:jsonList){
	  		JSONObject jo=new JSONObject();
	  		  //�����checkers
	  		  Set<Person> set=inc.getCheckers();
	  			  if(set.size()>0){
	  				  String s="";
	  				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
	  					  s+=iter.next().getPersonName()+",";
	  				  }
	  				  s=s.substring(0, s.lastIndexOf(","));
	  				  jo.put("checkers", s);
	  			  }
	  		  jo.put("id", inc.getId());
	  		  jo.put("checkType", inc.getCheckType());
	  		  jo.put("checkerFrom", inc.getCheckerFrom());
	  		  jo.put("inconformityItemSn", inc.getInconformityItemSn());
	  		  jo.put("checkDateTime", inc.getCheckDateTime());
	  		  jo.put("checkLocation", inc.getCheckLocation());
	  		  if(inc.getCheckedDepartment()!=null){
	  			  jo.put("checkedDepartment", inc.getCheckedDepartment().getDepartmentName());
	  			  jo.put("checkedDepartmentImplType", inc.getCheckedDepartment().getImplDepartmentName());
	  		  }else{
	  			  jo.put("checkedDepartment", inc.getCheckedDepartment());
	  		  }
	  		  if(inc.getSpeciality()!=null){
	  			  jo.put("speciality", inc.getSpeciality().getSpecialityName());
	  		  }
	  		  if(inc.getViolator()!=null){
	  			  jo.put("violator", inc.getViolator().getPersonName());
	  		  }
	  		  jo.put("actDescription", inc.getActDescription());
	  		  jo.put("unsafeActMark", inc.getUnsafeActMark());
	  		  if(inc.getUnsafeActStandard()!=null){
	  			  jo.put("unsafeActStandard", inc.getUnsafeActStandard().getStandardDescription());
	  			  jo.put("unsafeActLevel", inc.getUnsafeActStandard().getUnsafeActLevel());
	  		  }
	  		  if(inc.getSystemAudit()!=null){
	  			  jo.put("systemAudit",inc.getSystemAudit().getSystemAuditType());
	  		  }
	  		  jo.put("attachments", inc.getAttachments().size());
	  		  jsonArray.put(jo);
	  	  }
	    
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("total", total);// total�� ����ܼ�¼���������
			json.put("rows", jsonArray);// rows�� ���ÿҳ��¼ list
			out.print(JSONObject.valueToString(json));
			out.flush();
			out.close();
			return SUCCESS;
	}
	//��������ȫ��Ϊ��ѯ
	public String exportQueryUnsafeAct() throws IOException{
		String str="";
		Map<String, Object> map=new HashMap<String, Object>();
	    if(departmentTypeSn !=null && !"".equals(departmentTypeSn) && departmentSn!=null && !"".equals(departmentSn)){
			str=departmentService.getDownDepartmentByDepartmentType(departmentSn,departmentTypeSn);
			str=str.replaceAll("i.departmentSn", "i.checkedDepartment.departmentSn");
	    }
	    if(checkers!=null && !"".equals(checkers)){
	    	List<?> unsafeConditionList=unsafeConditionService.getBySql("select inconformity_item_sn from inconformity_item_checker where person_id='"+checkers+"'");
	    	checkers="(";
		  	for(Object un:unsafeConditionList){
		  		checkers=checkers+"'"+un+"',";
		  	}
		  	checkers=checkers.substring(0, checkers.length()-1);
		  	checkers+=")";
		 }
	    map=unsafeActService.showData(checkDeptSn, departmentSn, str, specialitySn, unsafeActStandardSn, 
	    					checkerFromSn, checkTypeSn, unsafeActLevelSn, timeData, beginTime, 
	    					endTime, checkers, 0, 0);
	    String name="����ȫ��Ϊ��ѯ���ݵ���";
		String titles="����������,��굥λ,���첿��,����ȫ��Ϊ��Ա,��Ϊ����,"
				+ "����רҵ,����ȫ��Ϊ�ۼ�,����ȫ��Ϊ��׼,����ȫ��Ϊ�ȼ�,"
				+ "�������,�������,¼����,����Ա,���������,���ʱ��,���ص�";
		String cellValue="inconformityItemSn,checkedDepartmentImplType,"
				+ "checkedDepartment,violator,actDescription,speciality,"
				+ "unsafeActMark,unsafeActStandard,UnsafeActLevel,checkType,"
				+ "systemAudit,editor,checkers,checkerFrom,checkDateTime,checkLocation";
	    
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
	 * ����ȫ��Ϊ������
	 * @return
	 * @throws IOException 
	 */
	public String reportExcel() throws IOException{
		List<UnsafeAct> list=new ArrayList<UnsafeAct>();
		StringBuffer hqls = new StringBuffer();				
		StringBuffer hqlc = new StringBuffer();
//		String as=departmentService.getDownDepartmentByDepartmentType(departmentSnIndex, departmentTypeSn);
//		as=as.replaceAll("i.departmentSn", "p.checkedDepartment.departmentSn");
		String departmentTypeSns="";
		if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			departmentTypeSns=departmentTypeService.getDownDepartmentTypeByParent(departmentTypeSn);
		}
		 hqls.append ( "select p FROM UnsafeAct p WHERE p.deleted=false and p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
		 hqlc.append ( "select Count(*) FROM UnsafeAct p WHERE p.deleted=false and p.checkedDepartment.departmentSn like '"+departmentSnIndex+"%' and p.checkDateTime>'"+begin+"' and p.checkDateTime<'"+end+"'");
		 if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			 hqls.append(" and p.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSns);
			 hqlc.append(" and p.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSns);
		 }
		 if(specialitySnIndex.length()>0){
				hqls.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
				hqlc.append(" AND p.speciality.specialitySn='"+specialitySnIndex+"'");	
		 }	
		if(inconformityLevelSnIndex.length()>0){			  
			hqls.append(" AND p.inconformityLevel='"+inconformityLevelSnIndex+"'");	
			hqlc.append(" AND p.inconformityLevel="+inconformityLevelSnIndex+"'");
		}
		hqls.append(" ORDER BY p.checkDateTime desc");	
		String hql=hqls.toString();	
		String hqlcc=hqlc.toString();			
		total = unsafeConditionService.countHql(hqlcc);
		list=unsafeActService.query(hql,0,0);
		//setUnsafeActs(unsafeActService.query(hql,page,rows));		
		net.sf.json.JSONArray array=new net.sf.json.JSONArray();
		for(UnsafeAct unsafeAct:list){
			net.sf.json.JSONObject jo=new net.sf.json.JSONObject();
			
			jo.put("id", unsafeAct.getId());
			if(unsafeAct.getCheckType()!=null){
				jo.put("checkType",unsafeAct.getCheckType().toString());
			}
			if(unsafeAct.getCheckerFrom()!=null){
				jo.put("checkerFrom",unsafeAct.getCheckerFrom().toString());
			}
			jo.put("inconformityItemSn",unsafeAct.getInconformityItemSn());
			if(unsafeAct.getCheckDateTime()!=null){			
				jo.put("checkDateTime",unsafeAct.getCheckDateTime().toString());
			}
			if(unsafeAct.getCheckLocation()!=null){
				jo.put("checkLocation",unsafeAct.getCheckLocation().toString());
			}
			if(unsafeAct.getCheckedDepartment()!=null){
				jo.put("checkedDepartment",unsafeAct.getCheckedDepartment().getDepartmentName());
  			  	jo.put("checkedDepartmentImplType",unsafeAct.getCheckedDepartment().getImplDepartmentName());
			}
			if(unsafeAct.getSpeciality().getSpecialityName()!=null){
				jo.put("speciality",unsafeAct.getSpeciality().getSpecialityName());
			}
			if(unsafeAct.getViolator()!=null){
				jo.put("violator",unsafeAct.getViolator().getPersonName());
			}
			jo.put("actDescription",unsafeAct.getActDescription());
			jo.put("unsafeActMark",unsafeAct.getUnsafeActMark());
			if(unsafeAct.getUnsafeActStandard()!=null){
				jo.put("unsafeActStandard", unsafeAct.getUnsafeActStandard().getStandardDescription());
				jo.put("UnsafeActLevel", unsafeAct.getUnsafeActStandard().getUnsafeActLevel());
			}
			if(unsafeAct.getSystemAudit()!=null){
				jo.put("systemAudit",unsafeAct.getSystemAudit().getSystemAuditType());
			}
			if(unsafeAct.getEditor()!=null){
			    jo.put("editor", unsafeAct.getEditor().getPersonName());
		    }else{
			    jo.put("editor", "��");
		    }
			jo.put("specialCheck",unsafeAct.getSpecialCheck() );	
			jo.put("periodicalCheck",unsafeAct.getPeriodicalCheck() );
			//�����
			Set<Person> set=unsafeAct.getCheckers();
			if(set.size()>0){
				  String s="";
				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
					  s+=iter.next().getPersonName()+",";
				  }
				  s=s.substring(0, s.lastIndexOf(","));
				  jo.put("checkers", s);
			}

			jo.put("attachments", unsafeAct.getAttachments().size());
			array.add(jo);			
		}
		
//		String str="{\"total\":"+total+",\"rows\":"+array+"}";
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total�� ����ܼ�¼���������
        json.put("rows", array);// rows�� ���ÿҳ��¼ list
        String name="����ȫ��Ϊ�������鵼��";
		String titles="����������,��굥λ,���첿��,����ȫ��Ϊ��Ա,��Ϊ����,"
				+ "����רҵ,����ȫ��Ϊ�ۼ�,����ȫ��Ϊ��׼,����ȫ��Ϊ�ȼ�,"
				+ "�������,�������,¼����,����Ա,���������,���ʱ��,���ص�";
		String cellValue="inconformityItemSn,checkedDepartmentImplType,"
				+ "checkedDepartment,violator,actDescription,speciality,"
				+ "unsafeActMark,unsafeActStandard,UnsafeActLevel,checkType,"
				+ "systemAudit,editor,checkers,checkerFrom,checkDateTime,checkLocation";
	    
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
