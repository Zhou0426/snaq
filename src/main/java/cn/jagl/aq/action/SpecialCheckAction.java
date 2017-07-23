package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.SpecialCheck;
import cn.jagl.aq.domain.Speciality;

/**
 * @author mahui
 *
 * @date 2016��7��8������5:18:19
 */
public class SpecialCheckAction extends BaseAction<SpecialCheck> {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String specialCheckName;//ר��������
	private CheckerFrom checkerFrom;
	private Date startDate;//��ʼ����
	private Date endDate;//��������
	private String personId;
	private String specialityIds;
	private String specialCheckSn;
	private String standardSn;
	private String strCheckers;
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
		String hql1="FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" order by s.specialCheckSn desc";
		String hql2="select count(*) FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f;
		if(f<2){
			HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
			if(roles.containsKey("jtxtgly")){
				hql1="FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" order by s.specialCheckSn desc";
				hql2="select count(*) FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f;
			}else if(roles.containsKey("fgsxtgly")){
				String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
				Department department=departmentService.getUpNerestFgs(departmentSn);
				hql1="select s FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.checkedDepartment is null or s.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by s.specialCheckSn desc";
				hql2="select count(*) FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.checkedDepartment is null or s.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%')";
			}else if(roles.containsKey("dwxtgly")){
				Department department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
				hql1="select s FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.checkedDepartment is null or s.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by s.specialCheckSn desc";
				hql2="select count(*) FROM SpecialCheck s WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.checkedDepartment is null or s.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%')";
			}else{
				String str=(String) session.get("personId");
				hql1="select distinct s FROM SpecialCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"') order by s.specialCheckSn desc";
				hql2="select distinct count(*) FROM SpecialCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"')";
			}
		}else{
			HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
			if(!roles.containsKey("jtxtgly")){
				String str=(String) session.get("personId");
				hql1="select distinct s FROM SpecialCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"') order by s.specialCheckSn desc";
				hql2="select distinct count(*) FROM SpecialCheck s left join s.checkers p WHERE s.deleted=false AND s.checkerFrom="+f+" and (s.editor.personId='"+str+"' or p.personId='"+str+"')";
			}
		}
		JSONArray array=new JSONArray();
		for(SpecialCheck specialCheck:specialCheckService.query(hql1, page, rows)){
			JSONObject jo=new JSONObject();
			jo.put("id",specialCheck.getId());
			jo.put("checkerFrom",specialCheck.getCheckerFrom());
			jo.put("checkType", "ר����");
			jo.put("specialCheckSn",specialCheck.getSpecialCheckSn());
			jo.put("specialCheckName", specialCheck.getSpecialCheckName());
			jo.put("startDate", specialCheck.getStartDate());
			jo.put("endDate", specialCheck.getEndDate());
			jo.put("inconformityItem", specialCheck.getInconformityItem().size());
			jo.put("checkTable", specialCheck.getCheckTables().size());
			jo.put("strCheckers", specialCheck.getStrCheckers());
			JSONObject ejo=new JSONObject();
			if(specialCheck.getEditor()!=null){
				ejo.put("isnull", false);
				ejo.put("personId",specialCheck.getEditor().getPersonId());
				ejo.put("personName", specialCheck.getEditor().getPersonName());
			}else{
				ejo.put("isnull",true);
			}
			jo.put("editor",ejo);
			//��Ӧ��׼
			JSONObject st=new JSONObject();
			if(specialCheck.getStandard()!=null){
				st.put("isnull",false);
				st.put("standardSn",specialCheck.getStandard().getStandardSn());
				st.put("standardName",specialCheck.getStandard().getStandardName());
			}else{
				st.put("isnull", true);
			}
			jo.put("standard",st);
			//�����Ա
			JSONObject cjo=new JSONObject();
			int i=0;
			String name="";
			String pIds="";
			String personIds="";
			String genders="";
			for(Person person:specialCheck.getCheckers()){
				i++;
				personIds+=person.getPersonId()+",";
				pIds+=person.getId()+",";
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
				personIds.substring(0,personIds.length()-1);
				genders=genders.substring(0, genders.length()-1);
				cjo.put("personNames", name);
				cjo.put("personIds",pIds);
				cjo.put("personIds2", personIds);
				cjo.put("genders", genders);
			}
			cjo.put("num",i);
			jo.put("checkers",cjo);
			
			//רҵ
			JSONObject sjo=new JSONObject();
			int j=0;
			String sName="";
			String sId="";
			for(Speciality speciality:specialCheck.getSpecialities()){
				j++;
				sName+=speciality.getSpecialityName()+",";
				sId+=speciality.getId()+",";
				
			}
			if(j>0){
				sName=sName.substring(0,sName.length()-1);
				sId=sId.substring(0,sId.length()-1);
				sjo.put("sSn",sId);
				sjo.put("sName", sName);
			}
			sjo.put("num",j);
			jo.put("specialities", sjo);
			
			//���첿��
			JSONObject chdjo=new JSONObject();
			if(specialCheck.getCheckedDepartment()!=null){
				chdjo.put("isnull", false);
				chdjo.put("departmentSn", specialCheck.getCheckedDepartment().getDepartmentSn());
				if(specialCheck.getCheckedDepartment().getImplDepartmentName()!=null&&!specialCheck.getCheckedDepartment().getDepartmentName().equals(specialCheck.getCheckedDepartment().getImplDepartmentName())){
					chdjo.put("departmentName",  specialCheck.getCheckedDepartment().getImplDepartmentName()+specialCheck.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("departmentName",specialCheck.getCheckedDepartment().getDepartmentName());
				}
				
			}else{
				chdjo.put("isnull", true);
			}
			jo.put("checkedDepartment", chdjo);
			
			array.put(jo);
		}
		Map<String,Object> json=new HashMap<String, Object>();	
		json.put("total", specialCheckService.count(hql2));
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
			SpecialCheck specialCheck=new SpecialCheck();
			String specialCheckSn=new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			specialCheck.setSpecialCheckSn(specialCheckSn);
			specialCheck.setCheckerFrom(checkerFrom);
			specialCheck.setSpecialCheckName(specialCheckName);
			specialCheck.setStartDate(startDate);
			specialCheck.setEndDate(endDate);
			specialCheck.setDeleted(false);
			specialCheck.setEditor(personService.getByPersonId(personId));
			specialCheck.setStandard(standardService.getByStandardSn(standardSn));
			specialCheck.setStrCheckers(strCheckers);
			//���첿��
			specialCheck.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
			if(ids!=null&&ids.length()>0){
				Set<Person> checkers=new HashSet<Person>(personService.getByPersonIds(ids));				
				specialCheck.setCheckers(checkers);
			}
			if(specialityIds.length()>0){
				Set<Speciality> specialities=new HashSet<Speciality>(specialityService.getByIds(specialityIds));
				specialCheck.setSpecialities(specialities);			
			}
			specialCheckService.save(specialCheck);
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
			specialCheckService.deleteByIds(ids);
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
			SpecialCheck specialCheck=specialCheckService.getById(id);
			specialCheck.setSpecialCheckName(specialCheckName);
			specialCheck.setStartDate(startDate);
			specialCheck.setEndDate(endDate);
			specialCheck.setStandard(standardService.getByStandardSn(standardSn));
			specialCheck.setStrCheckers(strCheckers);
			//���첿��
			specialCheck.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
			if(ids!=null&&ids.length()>0){
				Set<Person> checkers=new HashSet<Person>(personService.getByPersonIds(ids));
				specialCheck.setCheckers(checkers);
			}else{
				specialCheck.setCheckers(null);
			}
			if(specialityIds!=null&&specialityIds.length()>0){
				Set<Speciality> specialities=new HashSet<Speciality>(specialityService.getByIds(specialityIds));
				specialCheck.setSpecialities(specialities);
			}else{
				specialCheck.setSpecialities(null);
			}
			specialCheckService.update(specialCheck);			
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
	public String getSpecialCheckSn() {
		return specialCheckSn;
	}
	public void setSpecialCheckSn(String specialCheckSn) {
		this.specialCheckSn = specialCheckSn;
	}
	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}

	public String getSpecialityIds() {
		return specialityIds;
	}
	public void setSpecialityIds(String specialityIds) {
		this.specialityIds = specialityIds;
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
	public String getSpecialCheckName() {
		return specialCheckName;
	}
	public void setSpecialCheckName(String specialCheckName) {
		this.specialCheckName = specialCheckName;
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
}
