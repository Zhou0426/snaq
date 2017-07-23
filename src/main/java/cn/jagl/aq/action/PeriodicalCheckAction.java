package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.CheckerFrom;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Gender;
import cn.jagl.aq.domain.PeriodicalCheck;
import cn.jagl.aq.domain.PeriodicalCheckType;
import cn.jagl.aq.service.PeriodicalCheckService;
import cn.jagl.aq.service.PersonService;
import cn.jagl.aq.domain.Person;
/**
 * @author mahui
 *
 * @date 2016��7��8������3:07:10
 */
public class PeriodicalCheckAction extends BaseAction<PeriodicalCheck> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String periodicalCheckSn;//���ڼ����
	private String periodicalCheckName;//���ڼ������
	private PeriodicalCheckType periodicalCheckType;
	private CheckerFrom checkerFrom;
	private Date startDate;//��ʼ����
	private Date endDate;//��������
	private String personId;
	private String inconformityItemSn;
	private String className;
	private String standardSn;
	private String strCheckers;//�����Ա���ı�
	private String departmentSn;
	
	//��ҳ��ѯ
	@SuppressWarnings("unchecked")
	public String query() throws IOException{
		int f=10;		
		switch(checkerFrom){
		case ����:
			f=0;
			break;
		case ��λ:
			f=1;
			break;
		case ҵ����:
			f=2;
			break;
		case �߲������Ա:
			f=3;
			break;
		case ��:
			f=4;
			break;
		case �ⲿ:
			f=5;
			break;
		}
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out;  
        out = response.getWriter();        
		String hql1="FROM PeriodicalCheck p WHERE p.deleted=false AND checkerFrom="+f+" order by p.periodicalCheckSn desc";
		String hql2="select count(*) FROM PeriodicalCheck p WHERE p.deleted=false AND p.checkerFrom="+f;
		if(f<2){
			HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
			
			if(roles.containsKey("jtxtgly")){
				hql1="FROM PeriodicalCheck p WHERE p.deleted=false AND checkerFrom="+f+" order by p.periodicalCheckSn desc";
				hql2="select count(*) FROM PeriodicalCheck p WHERE p.deleted=false AND p.checkerFrom="+f;
			}else if(roles.containsKey("fgsxtgly")){
				String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
				Department department=departmentService.getUpNerestFgs(departmentSn);
				hql1="FROM PeriodicalCheck p WHERE p.deleted=false AND checkerFrom="+f+" and (p.checkedDepartment is null or p.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by p.periodicalCheckSn desc";
				hql2="select count(*) FROM PeriodicalCheck p WHERE p.deleted=false AND p.checkerFrom="+f+" and (p.checkedDepartment is null or p.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%')";
			}else if(roles.containsKey("dwxtgly")){
				Department department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
				hql1="FROM PeriodicalCheck p WHERE p.deleted=false AND checkerFrom="+f+" and (p.checkedDepartment is null or p.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by p.periodicalCheckSn desc";
				hql2="select count(*) FROM PeriodicalCheck p WHERE p.deleted=false AND p.checkerFrom="+f+" and (p.checkedDepartment is null or p.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%')";
			}else{
				String str=(String) session.get("personId");
				hql1="select distinct s FROM PeriodicalCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"') order by s.periodicalCheckSn desc";
				hql2="select distinct count(*) FROM PeriodicalCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"')";
			}
		}else{
			HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
			if(!roles.containsKey("jtxtgly")){
				String str=(String) session.get("personId");
				hql1="select distinct s FROM PeriodicalCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"') order by s.periodicalCheckSn desc";
				hql2="select distinct count(*) FROM PeriodicalCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"')";
			}
		}
		JSONArray array=new JSONArray();
		for(PeriodicalCheck periodicalCheck:periodicalCheckService.query(hql1, page, rows)){
			JSONObject jo=new JSONObject();
			jo.put("id",periodicalCheck.getId());
			jo.put("checkerFrom",periodicalCheck.getCheckerFrom());
			jo.put("checkType", "���ڼ��");
			jo.put("periodicalCheckSn",periodicalCheck.getPeriodicalCheckSn());
			jo.put("periodicalCheckName", periodicalCheck.getPeriodicalCheckName());
			jo.put("periodicalCheckType", periodicalCheck.getPeriodicalCheckType());
			jo.put("startDate", periodicalCheck.getStartDate());
			jo.put("endDate", periodicalCheck.getEndDate());
			jo.put("inconformityItem",periodicalCheck.getInconformityItem().size());
			jo.put("checkTable",periodicalCheck.getCheckTables().size());
			jo.put("strCheckers", periodicalCheck.getStrCheckers());
			JSONObject ejo=new JSONObject();
			if(periodicalCheck.getEditor()!=null){
				ejo.put("isnull", false);
				ejo.put("personId",periodicalCheck.getEditor().getPersonId());
				ejo.put("personName", periodicalCheck.getEditor().getPersonName());
			}else{
				ejo.put("isnull",true);
			}
			jo.put("editor",ejo);
			//��Ӧ���ֱ�׼
			JSONObject st=new JSONObject();
			if(periodicalCheck.getStandard()!=null){
				st.put("isnull",false);
				st.put("standardSn",periodicalCheck.getStandard().getStandardSn());
				st.put("standardName",periodicalCheck.getStandard().getStandardName());
			}else{
				st.put("isnull",true);
			}
			jo.put("standard",st);
			//�����Ա
			JSONObject cjo=new JSONObject();
			int i=0;
			String name="";
			String pIds="";
			String personIds="";
			String genders="";
			for(Person person:periodicalCheck.getCheckers()){
				i++;
				pIds+=person.getId()+",";
				personIds+=person.getPersonId()+",";
				name+=person.getPersonName()+",";
				if(person.getGender()!=null && person.getGender().equals(Gender.��)){
					genders+="��,";
				}else{
					genders+="Ů,";
				}
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
				pIds=pIds.substring(0,pIds.length()-1);
				personIds=personIds.substring(0,personIds.length()-1);
				genders=genders.substring(0, genders.length()-1);
				cjo.put("personNames", name);
				cjo.put("personIds",pIds);
				cjo.put("personIds2", personIds);
				cjo.put("genders", genders);
			}
			cjo.put("num",i);
			jo.put("checkers",cjo);
			
			//���첿��
			JSONObject chdjo=new JSONObject();
			if(periodicalCheck.getCheckedDepartment()!=null){
				chdjo.put("isnull", false);
				chdjo.put("departmentSn", periodicalCheck.getCheckedDepartment().getDepartmentSn());
				if(periodicalCheck.getCheckedDepartment().getImplDepartmentName()!=null&&!periodicalCheck.getCheckedDepartment().getDepartmentName().equals(periodicalCheck.getCheckedDepartment().getImplDepartmentName())){
					chdjo.put("departmentName",  periodicalCheck.getCheckedDepartment().getImplDepartmentName()+periodicalCheck.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("departmentName",periodicalCheck.getCheckedDepartment().getDepartmentName());
				}
				
			}else{
				chdjo.put("isnull", true);
			}
			jo.put("checkedDepartment", chdjo);
			array.put(jo);
		}
		Map<String,Object> json=new HashMap<String, Object>();	
		json.put("total", periodicalCheckService.count(hql2));
		json.put("rows", array);
		String Str= JSONObject.valueToString(json);
		out.print(Str);
		out.flush();
		out.close();
		return SUCCESS;
	}
	//���
	public void save() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\"}";
		try{
			PeriodicalCheck periodicalCheck=new PeriodicalCheck();
			//�������
			String periodicalCheckSn=new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			periodicalCheck.setPeriodicalCheckSn(periodicalCheckSn);
			periodicalCheck.setCheckerFrom(checkerFrom);
			periodicalCheck.setPeriodicalCheckName(periodicalCheckName);
			periodicalCheck.setPeriodicalCheckType(periodicalCheckType);
			periodicalCheck.setStartDate(startDate);
			periodicalCheck.setEndDate(endDate);
			periodicalCheck.setDeleted(false);
			periodicalCheck.setEditor(personService.getByPersonId(personId));
			periodicalCheck.setStandard(standardService.getByStandardSn(standardSn));
			periodicalCheck.setStrCheckers(strCheckers);
			//���첿��
			periodicalCheck.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
			if(ids!=null&&ids.length()>0){
				Set<Person> checkers=new HashSet<Person>(personService.getByPersonIds(ids));
				periodicalCheck.setCheckers(checkers);
			}				
			periodicalCheckService.save(periodicalCheck);
		}catch(Exception e){
			str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//����ɾ��
	public void deleteByIds() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"ɾ���ɹ���\"}";
		try{
			periodicalCheckService.deleteByIds(ids);
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"ɾ��ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	//����
	public void update() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"�޸ĳɹ���\"}";
		try{
			PeriodicalCheck periodicalCheck=periodicalCheckService.getById(id);
			periodicalCheck.setPeriodicalCheckName(periodicalCheckName);
			periodicalCheck.setPeriodicalCheckType(periodicalCheckType);
			periodicalCheck.setStartDate(startDate);
			periodicalCheck.setEndDate(endDate);
			periodicalCheck.setStandard(standardService.getByStandardSn(standardSn));
			periodicalCheck.setStrCheckers(strCheckers);
			//���첿��
			periodicalCheck.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
			if(ids!=null&&ids.length()>0){
				Set<Person> checkers=new HashSet<Person>(personService.getByPersonIds(ids));
				periodicalCheck.setCheckers(checkers);
			}else{
				periodicalCheck.setCheckers(null);
			}
			periodicalCheckService.update(periodicalCheck);
			
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getStrCheckers() {
		return strCheckers;
	}
	public void setStrCheckers(String strCheckers) {
		this.strCheckers = strCheckers;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	JSONArray message;//��ʾ��Ϣ
	public JSONArray getMessage() {
		return message;
	}
	public void setMessage(JSONArray message) {
		this.message = message;
	}
	
	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}
	public String getInconformityItemSn() {
		return inconformityItemSn;
	}
	public void setInconformityItemSn(String inconformityItemSn) {
		this.inconformityItemSn = inconformityItemSn;
	}
	public PeriodicalCheckType getPeriodicalCheckType() {
		return periodicalCheckType;
	}
	public void setPeriodicalCheckType(PeriodicalCheckType periodicalCheckType) {
		this.periodicalCheckType = periodicalCheckType;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPeriodicalCheckSn() {
		return periodicalCheckSn;
	}
	public void setPeriodicalCheckSn(String periodicalCheckSn) {
		this.periodicalCheckSn = periodicalCheckSn;
	}
	public String getPeriodicalCheckName() {
		return periodicalCheckName;
	}
	public void setPeriodicalCheckName(String periodicalCheckName) {
		this.periodicalCheckName = periodicalCheckName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public PeriodicalCheckService getPeriodicalCheckService() {
		return periodicalCheckService;
	}
	public void setPeriodicalCheckService(PeriodicalCheckService periodicalCheckService) {
		this.periodicalCheckService = periodicalCheckService;
	}
}
