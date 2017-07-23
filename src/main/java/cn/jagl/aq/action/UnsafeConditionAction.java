package cn.jagl.aq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;


import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.domain.CheckType;
import cn.jagl.aq.domain.CheckerFrom;
import cn.jagl.aq.domain.CorrectType;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.util.RandomUtil;
import cn.jagl.util.SmsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.jagl.aq.domain.InconformityItemNature;
import cn.jagl.aq.domain.InconformityLevel;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Sms;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.domain.UnitAppraisals;

public class UnsafeConditionAction extends BaseAction<UnsafeCondition> {
	private static final long serialVersionUID = 1L;
	private int id;
	private CheckerFrom checkerFrom;//���������
	private CheckType checkType;
	private Timestamp  checkDateTime;//���ʱ��
	private String checkLocation;//���ص�
	private InconformityItemNature inconformityItemNature;//������������
	private InconformityLevel inconformityLevel;
	private String problemDescription;//��������
	private float deductPoints;//�۷�
	private CorrectType correctType;//��������
	private Timestamp correctDeadline;//��������
	private String correctProposal;//���Ľ���	
	private String personId;
	private String hazardSn;
	private String manageObjectSn;
	private String departmentSn;
	private String specialitySn;
	private int inconformityItemid;	
	private String indexId;
	private String isConfirm;
	private String periodicalCheckSn;
	private String specialCheckSn;
	private String methods;
	private String reviewId;
	private String auditSn;
	private String strCheckers;//�����Ա���ı�
	private String indexSn;	
	private String year;
	private int quarter;
	private String level;
	private String redistribution;
	private JSONObject jsonObject=new JSONObject();
	//�����ٷ���
	
	
	public String redistribution(){
		UnsafeCondition unsafeCondition=unsafeConditionService.getById(id);
		if(unsafeCondition!=null){
			unsafeCondition.setCorrectPrincipal(personService.getByPersonId(personId));
			unsafeCondition.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
			unsafeConditionService.update(unsafeCondition);
			jsonObject.put("status", "ok");
		}else{
			jsonObject.put("status", "nook");
		}
		
		return "jsonObject";
	}
	
	//��ҳ��ѯ
	public String query() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out;  
        out = response.getWriter();
		JSONArray array=new JSONArray();
		String hql="";
		String sql="";
		@SuppressWarnings("unchecked")
		HashMap<String,String> roles=(HashMap<String,String>) session.get("roles");
		Person loginPerson=personService.getByPersonId((String) session.get("personId"));
		if(indexSn!=null && indexSn.length()>0){
			if(year!=null&&year.trim().length()>0){
				SystemAudit systemAudit=null;
				if(quarter==4){
					systemAudit=systemAuditService.queryShScore(departmentSn,year);
				}else{
					systemAudit=systemAuditService.queryScore(departmentSn, quarter, year);
				}
				
				sql="select count(*) from inconformity_item where deleted=false and (index_sn='"+indexSn+"' or index_sn like '"+indexSn+".%') and audit_sn="+(systemAudit!=null?systemAudit.getAuditSn():"");
				hql="select c FROM UnsafeCondition c WHERE c.deleted=false AND c.systemAudit.auditSn="+(systemAudit!=null?systemAudit.getAuditSn():"")+" AND (c.standardIndex.indexSn='"+indexSn+"' or c.standardIndex.indexSn like '"+indexSn+".%') order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";				
			}else if(auditSn!=null&&auditSn.trim().length()>0){
				hql="select c from UnsafeCondition c WHERE c.deleted=false and (c.standardIndex.indexSn like '"+indexSn+".%' or c.standardIndex.indexSn='"+indexSn+"') and c.systemAudit.auditSn='"+auditSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
				sql="select count(*) from inconformity_item i WHERE i.deleted=false and (i.index_sn like '"+indexSn+".%' or i.index_sn='"+indexSn+"') and i.audit_sn='"+auditSn+"'";
			}else{
				if(periodicalCheckSn!=null&&periodicalCheckSn.trim().length()>0){
					hql="select c FROM UnsafeCondition c WHERE c.deleted=false AND c.periodicalCheck.periodicalCheckSn="+periodicalCheckSn+" AND c.standardIndex.indexSn='"+indexSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item where deleted=false and index_sn='"+indexSn+"' and periodical_check_sn="+periodicalCheckSn;
				}else if(specialCheckSn!=null&&specialCheckSn.trim().length()>0){
					hql="select c FROM UnsafeCondition c WHERE c.deleted=false AND c.specialCheck.specialCheckSn="+specialCheckSn+" AND c.standardIndex.indexSn='"+indexSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item where deleted=false and index_sn='"+indexSn+"' and special_check_sn="+specialCheckSn;
				}else{
					hql="select c FROM UnsafeCondition c WHERE c.deleted=false AND c.systemAudit.auditSn="+auditSn+" AND c.standardIndex.indexSn='"+indexSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item where deleted=false and index_sn='"+indexSn+"' and audit_sn="+auditSn;
				}
			}
		}else{
			if(personId!=null&&personId.length()>0){
				hql="FROM UnsafeCondition i WHERE i.deleted=false AND i.correctPrincipal.personId ='"+personId+"' order by i.hasCorrectConfirmed,i.inconformityItemSn desc";
				sql="select count(*) from inconformity_item i where i.inconformity_item_type like '����' and i.deleted=false and i.correct_principal_sn like '"+personId+"'";
			}else if(reviewId!=null&&reviewId.length()>0){
				hql="select distinct i FROM UnsafeCondition i LEFT JOIN i.checkers c LEFT JOIN i.editor o WHERE i.deleted=false AND i.hasCorrectConfirmed=true AND (c.personId ='"+reviewId+"' OR o.personId='"+reviewId+"') order by i.hasReviewed,i.inconformityItemSn desc";
				sql="select count(*) from (select distinct i.* from inconformity_item i left join inconformity_item_checker c on i.inconformity_item_sn=c.inconformity_item_sn left join person p ON c.person_id = p.person_id where i.deleted=false and i.inconformity_item_type like '����' and i.has_correct_confirmed=true and (p.person_id='"+reviewId+"' or editor_id='"+reviewId+"')) as a";
				if(roles.containsKey("dwxtgly")){
					Department department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
					if(department!=null){
						hql="select distinct i FROM UnsafeCondition i LEFT JOIN i.checkers c LEFT JOIN i.editor o WHERE i.deleted=false AND i.hasCorrectConfirmed=true AND "
								+ "(c.personId ='"+reviewId+"' OR o.personId='"+reviewId+"' OR ("
								+ "i.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%' and i.checkerFrom>3)) "
								+ "order by i.hasReviewed,i.inconformityItemSn desc";
						sql="select count(*) from ("
								+ "select distinct i.* from inconformity_item i "
								+ "left join inconformity_item_checker c on i.inconformity_item_sn=c.inconformity_item_sn "
								+ "left join person p ON c.person_id = p.person_id "
								+ "where i.deleted=false and i.inconformity_item_type like '����' and i.has_correct_confirmed=true and "
								+ "(p.person_id='"+reviewId+"' or editor_id='"+reviewId+"' or("
								+ "i.checked_department_sn like '"+department.getDepartmentSn()+"%' and i.check_from>3))) as a";
					}
				}
			}else if(periodicalCheckSn!=null&&periodicalCheckSn.length()>0){
				hql="select c from UnsafeCondition c WHERE c.deleted=false and c.periodicalCheck.periodicalCheckSn='"+periodicalCheckSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
				sql="select count(*) from inconformity_item i WHERE i.deleted=false and i.periodical_check_sn='"+periodicalCheckSn+"'";
			}else if(specialCheckSn!=null&&specialCheckSn.length()>0){
				hql="select c from UnsafeCondition c WHERE c.deleted=false and c.specialCheck.specialCheckSn='"+specialCheckSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
				sql="select count(*) from inconformity_item i WHERE i.deleted=false and i.special_check_sn='"+specialCheckSn+"'";
			}else if(auditSn!=null&&auditSn.length()>0){
				if(level!=null&&level.trim().length()>0){
					hql="select c from UnsafeCondition c WHERE c.inconformityLevel="+level+" and c.deleted=false and c.systemAudit.auditSn='"+auditSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item i WHERE i.inconformity_level="+level+" and i.deleted=false and i.audit_sn='"+auditSn+"'";
				}else{
					hql="select c from UnsafeCondition c WHERE c.deleted=false and c.systemAudit.auditSn='"+auditSn+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item i WHERE i.deleted=false and i.audit_sn='"+auditSn+"'";
				}
				
			}else if(redistribution!=null && redistribution.trim().length()>0){//�ٷ���
				Department department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
				if(department!=null){
					hql="select c FROM UnsafeCondition c WHERE c.deleted=false AND c.checkedDepartment.departmentSn='"+department.getDepartmentSn()+"' order by c.hasReviewed,c.hasCorrectConfirmed,c.inconformityItemSn desc";
					sql="select count(*) from inconformity_item where deleted=false and inconformity_item_type like '����' and checked_department_sn='"+department.getDepartmentSn()+"'";
				}else{
					hql="select c FROM UnsafeCondition c WHERE c.id<0";
					sql="select count(*) from inconformity_item where id<0";
				}
			}else{
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
				int c=10;
				switch(checkType){
				case ��̬���:
					c=0; 
					break;
				case ���ڼ��:
					c=1;
					break;
				case ר����:
					c=2;
					break;
				}
				hql="select i FROM UnsafeCondition i WHERE i.deleted=false AND checkerFrom="+f+" AND checkType="+c;
				sql="select count(*) from inconformity_item i where i.deleted=false and i.inconformity_item_type like '����' and i.check_from="+f+" and i.check_type="+c;
				
				if(f<2&&c==0){
					if(roles.containsKey("jtxtgly")){
						hql+=" order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
					}else if(roles.containsKey("fgsxtgly")){
						String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
						Department department=departmentService.getUpNerestFgs(departmentSn);
						hql+=" AND (i.editor.personId='"+loginPerson.getPersonId()+"' OR i.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
						sql+=" and (i.editor_id='"+loginPerson.getPersonId()+"' or i.checked_department_sn like '"+department.getDepartmentSn()+"%')";
					}else if(roles.containsKey("dwxtgly")){
						List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(loginPerson.getDepartment().getDepartmentSn());
						Department department;
				    	if(departmentTypes.size()>0){
				    		department=loginPerson.getDepartment();
				    	}else{
				    		department=departmentService.getUpNearestImplDepartment(loginPerson.getDepartment().getDepartmentSn());
				    	}
						hql+=" AND (i.editor.personId='"+loginPerson.getPersonId()+"' OR i.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%') order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
						sql+=" and (i.editor_id='"+loginPerson.getPersonId()+"' or i.checked_department_sn like '"+department.getDepartmentSn()+"%')";
					}else{
						hql+=" AND i.editor.personId='"+loginPerson.getPersonId()+"' order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
						sql+=" and i.editor_id='"+loginPerson.getPersonId()+"'";
					}					
				}else if(f>=2&&c==0){
					if(roles.containsKey("jtxtgly")){
						hql+=" order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
					}else{
						hql+=" AND i.editor.personId='"+loginPerson.getPersonId()+"' order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
						sql+=" and i.editor_id='"+loginPerson.getPersonId()+"'";
					}
				}else{
					hql+=" order by i.hasReviewed,i.hasCorrectConfirmed,i.inconformityItemSn desc";
				}
			}
		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");				
		for(UnsafeCondition inconformityItem:unsafeConditionService.query(hql, page, rows)){
			JSONObject jo=new JSONObject();
			jo.put("id", inconformityItem.getId());//id
			jo.put("inconformityItemSn",inconformityItem.getInconformityItemSn());//���
			jo.put("checkDateTime", format.format(inconformityItem.getCheckDateTime()));//���ʱ��
			jo.put("editorDateTime", inconformityItem.getEditorDateTime()!=null?format.format(inconformityItem.getEditorDateTime()):"");//¼��ʱ��
			jo.put("checkLocation", inconformityItem.getCheckLocation());//����
			jo.put("inconformityItemNature",inconformityItem.getInconformityItemNature());//������������
			jo.put("deductPoints",inconformityItem.getDeductPoints());
			jo.put("hasReviewed", inconformityItem.getHasReviewed());
			jo.put("hasCorrectConfirmed", inconformityItem.getHasCorrectConfirmed());
			jo.put("confirmTime", inconformityItem.getConfirmTime()!=null?inconformityItem.getConfirmTime().toString():"");
			jo.put("hasCorrectFinished", inconformityItem.getHasCorrectFinished());
			jo.put("checkType", inconformityItem.getCheckType());
			jo.put("strCheckers", inconformityItem.getStrCheckers());
			jo.put("checkerFrom",inconformityItem.getCheckerFrom());
			jo.put("unsafeConditionDefers", inconformityItem.getUnsafeConditionDefers().size());
			//��
			JSONObject mjo=new JSONObject();
			if(inconformityItem.getMachine()!=null){
				mjo.put("isnull",false);
				mjo.put("manageObjectSn", inconformityItem.getMachine().getManageObjectSn());
				mjo.put("manageObjectName", inconformityItem.getMachine().getManageObjectName());
			}else{
				mjo.put("isnull", true);
			}
			jo.put("machine", mjo);
			//���첿��
			JSONObject chdjo=new JSONObject();
			if(inconformityItem.getCheckedDepartment()!=null){
				chdjo.put("isnull", false);
				chdjo.put("departmentSn", inconformityItem.getCheckedDepartment().getDepartmentSn());
				if(inconformityItem.getCheckedDepartment().getImplDepartmentName()!=null&&!inconformityItem.getCheckedDepartment().getDepartmentName().equals(inconformityItem.getCheckedDepartment().getImplDepartmentName())){
					chdjo.put("departmentName",  inconformityItem.getCheckedDepartment().getImplDepartmentName()+inconformityItem.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("departmentName",inconformityItem.getCheckedDepartment().getDepartmentName());
				}
				
			}else{
				chdjo.put("isnull", true);
			}
			jo.put("checkedDepartment", chdjo);
			//ָ��
			JSONObject sjo=new JSONObject();
			if(inconformityItem.getStandardIndex()!=null){
				sjo.put("isnull", false);
				sjo.put("id",inconformityItem.getStandardIndex().getId());
				sjo.put("indexName", inconformityItem.getStandardIndex().getIndexName());
			}else{
				sjo.put("isnull",true);
			}
			jo.put("standardIndex", sjo);
			jo.put("problemDescription",inconformityItem.getProblemDescription());//��������
			jo.put("deductPoints", inconformityItem.getDeductPoints());//�۷�
			if(inconformityItem.getCorrectType()!=null){
				jo.put("correctType", inconformityItem.getCorrectType());
			}else{
				jo.put("correctType", "");
			}
			if(inconformityItem.getCorrectDeadline()!=null){
				jo.put("correctDeadline", format.format(inconformityItem.getCorrectDeadline()));//��������
			}else{
				jo.put("correctDeadline", null);
			}
			
			//��Ӧ��Σ��Դ
			JSONObject hjo=new JSONObject();
			if(inconformityItem.getHazrd()!=null){
				hjo.put("isnull",false);
				hjo.put("hazardSn",inconformityItem.getHazrd().getHazardSn());
				hjo.put("hazardDescription", inconformityItem.getHazrd().getHazardDescription());
			}else{
				hjo.put("isnull", true);
			}
			jo.put("hazard",hjo);
			//����רҵ
			JSONObject spjo=new JSONObject();
			if(inconformityItem.getSpeciality()!=null){
				spjo.put("isnull", false);
				spjo.put("specialitySn", inconformityItem.getSpeciality().getSpecialitySn());
				spjo.put("specialityName", inconformityItem.getSpeciality().getSpecialityName());
			}else{
				spjo.put("isnull", true);
			}
			jo.put("speciality",spjo);
			jo.put("inconformityLevel",inconformityItem.getInconformityLevel());//��������ȼ�
			//¼����
			JSONObject ejo=new JSONObject();
			if(inconformityItem.getEditor()!=null){
				ejo.put("isnull",false);
				ejo.put("personId",inconformityItem.getEditor().getPersonId());
				ejo.put("personName",inconformityItem.getEditor().getPersonName());
			}else{
				ejo.put("isnull",true);
			}
			jo.put("editor", ejo);
			//���ĸ�����
			JSONObject cpjo=new JSONObject();
			if(inconformityItem.getCorrectPrincipal()!=null){
				cpjo.put("isnull",false);
				cpjo.put("personId",inconformityItem.getCorrectPrincipal().getPersonId());
				cpjo.put("personName",inconformityItem.getCorrectPrincipal().getPersonName());
			}else{
				cpjo.put("isnull",true);
			}
			jo.put("correctPrincipal", cpjo);
			jo.put("correctProposal",inconformityItem.getCorrectProposal());//���Ľ���
			//�����Ա
			JSONObject cjo=new JSONObject();
			int i=0;
			String name="";
			String pIds="";
			for(Person person:inconformityItem.getCheckers()){
				i++;
				pIds+=person.getId()+",";
				name+=person.getPersonName()+",";
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
				pIds=pIds.substring(0,pIds.length()-1);
				cjo.put("personNames", name);
				cjo.put("personIds",pIds);
			}
			cjo.put("num",i);
			jo.put("checkers",cjo);
			jo.put("attachment",inconformityItem.getAttachments().size());
			
			//��˷���
			JSONObject amjo=new JSONObject();
			String count="";
			String method="";
			String methodId="";
			int g=0;
			for(Entry<String,Integer> am:inconformityItem.getAuditMethods().entrySet()){
				StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getBySn(am.getKey());
				if( standardIndexAuditMethod != null ){
					g++;
					//������
					method+=standardIndexAuditMethod.getAuditMethodContent()+",";
					//����Id
					methodId+=standardIndexAuditMethod.getId()+",";
					//����
					count+=am.getValue()+",";
					standardIndexAuditMethod.getId();
				}
			}
			if(g>0){
				method=method.substring(0, method.length()-1);
				methodId=methodId.substring(0,methodId.length()-1);
				count=count.substring(0,count.length()-1);
				amjo.put("method", method);
				amjo.put("count", count);
				amjo.put("methodId", methodId);
			}
			amjo.put("num", g);
			jo.put("auditMethod", amjo);
			array.add(jo);
		}
		JSONObject jo=new JSONObject();
		jo.put("total", unsafeConditionService.count(sql));
		jo.put("rows", array.toString());
		out.print(jo);
        out.flush(); 
        out.close();
		return "jsonLoad";
	}
	
	//���
	@SuppressWarnings("unused")
	public String save() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        boolean isSuccess=true;
        //��������Ϣ
        String principalName="";
        String principalNumber="";
		String str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\",\"isrepeat\":\"false\"}";
		Person pId=personService.getByPersonId((String) session.get("personId"));
		if(pId!=null){
			try{
				UnsafeCondition inconformityItem=new UnsafeCondition();
				inconformityItem.setCheckType(checkType);
				inconformityItem.setCheckerFrom(checkerFrom);
				inconformityItem.setEditor(personService.getByPersonId(pId.getPersonId()));
				//�������
				String inconformityItemSn=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
				inconformityItem.setInconformityItemSn(inconformityItemSn);
				if(checkDateTime!=null && checkDateTime.after(new Date())){
					str="{\"status\":\"nook\",\"message\":\"�޷���ӣ����ʱ�䲻�Ϸ���\",\"isrepeat\":\"false\"}";
					out.print(str);
				    out.flush(); 
				    out.close();
				    return "jsonLoad";
				}
				inconformityItem.setCheckDateTime(checkDateTime);
				inconformityItem.setCheckLocation(checkLocation);
				inconformityItem.setInconformityItemNature(inconformityItemNature);
				inconformityItem.setInconformityLevel(inconformityLevel);
				inconformityItem.setProblemDescription(problemDescription);
				inconformityItem.setDeductPoints(deductPoints);
				inconformityItem.setCorrectType(correctType);
				inconformityItem.setStrCheckers(strCheckers);
				inconformityItem.setEditorDateTime(new Date());
				if(correctType!=null){
					if(correctDeadline!=null){
						inconformityItem.setCorrectDeadline(correctDeadline);
					}else{
						String c=checkDateTime.toString();
						//System.out.println(c);
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date=null;
						date = sdf.parse(c);
						Calendar ca=Calendar.getInstance();
						ca.setTime(date);
						ca.add(Calendar.HOUR, 8);
						String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ca.getTime());
						Timestamp goodsC_date = Timestamp.valueOf(nowTime);
						inconformityItem.setCorrectDeadline(goodsC_date);
					}
				}
				inconformityItem.setCorrectProposal(correctProposal);
				inconformityItem.setDeleted(false);
				inconformityItem.setHasCorrectConfirmed(false);
				inconformityItem.setHasCorrectFinished(false);
				inconformityItem.setHasReviewed(false);
				Map<String,Integer> auditMethods=new HashMap<String,Integer>(0);
				//��˷���
				float score=0;
				StandardIndex index=null;
				if(indexId!=null&&indexId.trim().length()>0&&indexId.matches("[0-9]+")){
					index=standardindexService.getById(Integer.valueOf(indexId));
				}
				//StandardIndex index=standardindexService.getById(indexId);
				if(index!=null&&index.getChildren().size()>0){
					str="{\"status\":\"nook\",\"message\":\"��Ӧ��׼Ӧѡ��ĩ����׼��������ѡ��\",\"isrepeat\":\"false\"}";
					out.print(str);
				    out.flush(); 
				    out.close();
				    return "jsonLoad";
				}
				if(index!=null&&methods!=null&&methods.length()>0){
					//�Ƿ�ؼ�ָ��
					if(index.getIsKeyIndex()!=null&&index.getIsKeyIndex()==true){
						StandardIndex deindex=new StandardIndex();
						String auditMethodSn="";
						int count=0;
						int sumcount=0;
						for(int i=0;i<methods.split(",").length;i++){
							StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getById(Integer.parseInt(methods.split(",")[i].split("#")[0]));
							auditMethodSn=standardIndexAuditMethod.getAuditMethodSn();
							count=Integer.parseInt(methods.split(",")[i].split("#")[1]);
							auditMethods.put(auditMethodSn, count);
							if(standardIndexAuditMethod.getIndexDeducted()!=null&&standardIndexAuditMethod.getDeduction()!=null){
								deindex=standardIndexAuditMethod.getIndexDeducted();
								score+=count*standardIndexAuditMethod.getDeduction();
							}else if(standardIndexAuditMethod.getIndexDeducted()==null&&index.getAnDeduction()!=null){
								sumcount+=count;
							}
						}
						//�ж���ָͨ���Ƿ���
						float commonScore=0;
						if(index.getAnDeduction()!=null){
							if(index.getZeroTimes()!=null){
								if(index.getZeroTimes()>sumcount){
									commonScore=sumcount*index.getAnDeduction();
								}else if(index.getPercentageScore()!=null){
									commonScore=index.getPercentageScore();
								}else{
									commonScore=sumcount*index.getAnDeduction();
								}	
							}else if(index.getPercentageScore()!=null){
								if(sumcount*index.getAnDeduction()>index.getPercentageScore()){
									commonScore=index.getPercentageScore();
								}else{
									commonScore=sumcount*index.getAnDeduction();
								}
							}
						}
						score+=commonScore;
						//�жϸ���ָ���Ƿ�
						if(deindex!=null&&deindex.getPercentageScore()!=null&&score>deindex.getPercentageScore()){
							score=deindex.getPercentageScore();
						}
						
					}else{
						String auditMethodSn="";
						int count=0;
						int sumcount=0;
						for(int i=0;i<methods.split(",").length;i++){
							StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getById(Integer.parseInt(methods.split(",")[i].split("#")[0]));
							auditMethodSn=standardIndexAuditMethod.getAuditMethodSn();
							count=Integer.parseInt(methods.split(",")[i].split("#")[1]);
							auditMethods.put(auditMethodSn, count);
							sumcount+=count;
						}
						//�۷�
						if(index.getAnDeduction()!=null){
							if(index.getZeroTimes()!=null){
								if(index.getZeroTimes()>sumcount){
									score=sumcount*index.getAnDeduction();
								}else if(index.getPercentageScore()!=null){
									score=index.getPercentageScore();
								}else{
									score=sumcount*index.getAnDeduction();
								}	
							}else if(index.getPercentageScore()!=null){
								if(sumcount*index.getAnDeduction()>index.getPercentageScore()){
									score=index.getPercentageScore();
								}else{
									score=sumcount*index.getAnDeduction();
								}
							}
						}
					}
					inconformityItem.setDeductPoints(score);
					inconformityItem.setAuditMethods(auditMethods);
				}
				//���ڼ��
				inconformityItem.setPeriodicalCheck(periodicalCheckService.getByPeriodicalCheckSn(periodicalCheckSn));
				//ר����
				inconformityItem.setSpecialCheck(specialCheckService.getBySpecialCheckSn(specialCheckSn));
				//�����Ա��
				if(ids!=null&&ids.length()>0){
				List<Person> personList=personService.getByPersonIds(ids);	 
					if(personList.size()>0){
						Set<Person> set = new HashSet<Person>(personList);
					inconformityItem.setCheckers(set);
					}
				}
				//���ĸ�����
				Person principalPerson=personService.getByPersonId(personId.trim());
				if(principalPerson!=null){
					principalName=principalPerson.getPersonName();
					if(principalPerson.getCellphoneNumber()!=null&&principalPerson.getCellphoneNumber().trim().length()==11){
						principalNumber=principalPerson.getCellphoneNumber();
					}
				}
				inconformityItem.setCorrectPrincipal(principalPerson);
				//��
				inconformityItem.setMachine(manageObjectService.getByManageObjectSn(manageObjectSn));
				//���첿��
				inconformityItem.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
				//ָ��
				inconformityItem.setStandardIndex(index);
				//Σ��Դ
				if(hazardSn!=null&&hazardSn.trim().length()>0){
					inconformityItem.setHazrd(hazardService.getByHazardSn(hazardSn));
				}
				//רҵ
				inconformityItem.setSpeciality(specialityService.getBySpecialitySn(specialitySn));
				//���
				if(auditSn!=null&&auditSn.trim().length()>0){
					inconformityItem.setSystemAudit(systemAuditService.getBySn(auditSn));
				}else{
					inconformityItem.setSystemAudit(systemAuditService.getById(id));
				}
				
				//���⿪ʼ�ж�

				StandardIndex standardIndex=null;
				if(indexId!=null&&indexId.trim().length()>0&&indexId.matches("[0-9]+")){
					standardIndex=standardindexService.getById(Integer.valueOf(indexId));
				}
				if(standardIndex!=null){
					String sql="select count(*) from inconformity_item i where i.deleted=false and i.checked_department_sn like '"+departmentSn+"' and i.standard_sn like '"+standardIndex.getStandard().getStandardSn()+"' and i.index_sn like '"+standardIndex.getIndexSn()+"' and i.correct_deadline>"+"CAST('"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkDateTime)+"' AS DATETIME)";
					if(unsafeConditionService.count(sql)>0){
						str="{\"status\":\"nook\",\"message\":\"��ǰ���첿������Ӧ�Ĳ��������׼�Ѿ�¼�룬��ǰ���ʱ�����ڸò������������У���Ҫ¼����\",\"isrepeat\":\"true\"}";
						if(isConfirm.equals("true")){
							try{
								unsafeConditionService.save(inconformityItem);
								str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\",\"isrepeat\":\"true\"}";
							}catch(Exception e){
								isSuccess=false;
								str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\",\"isrepeat\":\"true\"}";
							}
							
						}
					}else{
						try{
							unsafeConditionService.save(inconformityItem);
							str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\",\"isrepeat\":\"false\"}";
						}catch(Exception e){
							isSuccess=false;
							str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\",\"isrepeat\":\"false\"}";
						}
						
					}		
				}else{
					try{
						unsafeConditionService.save(inconformityItem);
						str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\",\"isrepeat\":\"false\"}";
					}catch(Exception e){
						isSuccess=false;
						str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\",\"isrepeat\":\"false\"}";
					}
				}
				}catch(Exception e){
					isSuccess=false;
					str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\",\"isrepeat\":\"false\"}";
				}
		}else{
			isSuccess=false;
			str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\",\"isrepeat\":\"false\"}";
		}
		
		out.print(str);
	    out.flush(); 
	    out.close();
	    
	    //���ŷ���
	    if(isSuccess==true&&principalNumber.trim().length()==11){
	    	LocalDateTime now=LocalDateTime.now();
	    	//Timestamp Timestamp=new Timestamp().;
	    	String serialNumber=RandomUtil.getRandmDigital20();
	    	String messageContent=checkerFrom+"��飺�����µ�����¼�룡�뾡��ǰ��ϵͳ�鿴��¼��ʱ��:"+now.getYear()+"��"+now.getMonthValue()+"��"+now.getDayOfMonth()+"��"+now.getHour()+"ʱ"+now.getMinute()+"��";
    		Sms sms=new Sms();
    		sms.setMessageContent(messageContent);
    		sms.setResultCode(SmsUtil.sendSms(serialNumber, principalNumber, messageContent));
    		sms.setSerialNumber(serialNumber);
    		sms.setUserNumber(principalNumber);
    		sms.setSuccessTimestamp(Timestamp.valueOf(now));
    		try{
    			smsService.save(sms);
    		}catch(Exception e){
    			System.out.println(e);
    		}
    		
	    }
		//����ÿ�տ���
	    try{
	    	Byte savetype=1;
	    	Department department=null;
	    	if(checkerFrom!=null&&(checkerFrom.equals(CheckerFrom.�ⲿ) || checkerFrom.equals(CheckerFrom.ҵ����) || checkerFrom.equals(CheckerFrom.��) || checkerFrom.equals(CheckerFrom.�߲������Ա))){
	    		savetype=0;
	    		List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
	    		if(departmentTypes.size()>0){
	        		department=departmentService.getByDepartmentSn(departmentSn);
	        	}else{
	        		department=departmentService.getUpNearestImplDepartment(departmentSn);
	        	}
	    	}else{
	    		department=departmentService.getByDepartmentSn(departmentSn);
	    	}
    		LocalDate localDate = checkDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    		UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(department.getDepartmentSn(), localDate,savetype);
    		if(unitAppraisals!=null){
    			unitAppraisals.setNeedComputing(true);
    			unitAppraisalsService.update(unitAppraisals);
    		}else{
    			UnitAppraisals unitAppraisals2=new UnitAppraisals();
    			unitAppraisals2.setAppraisalsDate(localDate);
    			unitAppraisals2.setNeedComputing(true);
    			unitAppraisals2.setDepartment(department);
    			unitAppraisals2.setType(savetype);
    			unitAppraisalsService.save(unitAppraisals2);
    		}
	    }catch(Exception e){
	    	System.out.println(e);
	    	System.out.println("����ÿ�տ��˳���");
	    }
		//����ÿ�¿���
	    try{
	    	LocalDate oldTime=LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());//�ϸ������һ��
	    	if(checkDateTime.toLocalDateTime().toLocalDate().isBefore(oldTime) || checkDateTime.toLocalDateTime().toLocalDate()==oldTime){
	    		oldTime=oldTime.withDayOfMonth(1);//�ϸ���һ��
	    		//�ж��ϸ����Ƿ��Ѿ�����Ҫ����������ˣ�����У��������true
	    		String hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null and c.needComputing=true";
	    		List<CheckTaskAppraisals> list1=checkTaskAppraisalsService.getByHql(hql);
	    		//�������0������������û����Ҫ���������
	    		if(list1.size()==0){
	    			hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null";
	    			List<CheckTaskAppraisals> list2=checkTaskAppraisalsService.getByHql(hql);
	    			if(list2.size()>0){
	    				for(CheckTaskAppraisals ch:list2){
	    					ch.setNeedComputing(true);
	    					checkTaskAppraisalsService.update(ch);
	    					break;
	    				}
	    			}
	    		}
	    	}
	    }catch(Exception e){
	    	System.out.println("����ÿ�¿��˳���");
	    }
	    return "jsonLoad";
	}
	//�޸�
	public String update() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"�޸ĳɹ���\"}";
		UnsafeCondition inconformityItem=unsafeConditionService.getById(inconformityItemid);
		String oldDepartmentSn=inconformityItem.getCheckedDepartment().getDepartmentSn();
		//ԭ����ʱ��
		LocalDate oldlocalDate = inconformityItem.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		try{
		if(checkDateTime!=null && checkDateTime.after(new Date())){
			str="{\"status\":\"nook\",\"message\":\"�޷���ӣ����ʱ�䲻�Ϸ���\"}";
			out.print(str);
		    out.flush(); 
		    out.close();
		    return "jsonLoad";
		}
		inconformityItem.setCheckDateTime(checkDateTime);
		inconformityItem.setCheckLocation(checkLocation);
		inconformityItem.setInconformityItemNature(inconformityItemNature);
		inconformityItem.setInconformityLevel(inconformityLevel);
		inconformityItem.setProblemDescription(problemDescription);
		inconformityItem.setDeductPoints(deductPoints);
		inconformityItem.setCorrectType(correctType);
		inconformityItem.setStrCheckers(strCheckers);		
		if(correctType!=null){
			if(correctDeadline!=null){
				inconformityItem.setCorrectDeadline(correctDeadline);
			}else{
				String c=checkDateTime.toString();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date=null;
				date = sdf.parse(c);
				Calendar ca=Calendar.getInstance();
				ca.setTime(date);
				ca.add(Calendar.HOUR, 8);
				String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ca.getTime());
				Timestamp goodsC_date = Timestamp.valueOf(nowTime);
				inconformityItem.setCorrectDeadline(goodsC_date);
			}
		}		
		inconformityItem.setCorrectProposal(correctProposal);
		Map<String,Integer> auditMethods=new HashMap<String,Integer>(0);
		//��˷���
		float score=0;
		StandardIndex index=null;
		if(indexId!=null&&indexId.trim().length()>0&&indexId.matches("[0-9]+")){
			index=standardindexService.getById(Integer.valueOf(indexId));
		}
		if(index!=null&&index.getChildren().size()>0){
			str="{\"status\":\"nook\",\"message\":\"��Ӧ��׼Ӧѡ��ĩ����׼��������ѡ��\"}";
			out.print(str);
		    out.flush(); 
		    out.close();
		    return "jsonLoad";
		}
		if(index!=null&&methods!=null&&methods.length()>0){
			//�Ƿ�ؼ�ָ��
			if(index.getIsKeyIndex()!=null&&index.getIsKeyIndex()==true){
				StandardIndex deindex=new StandardIndex();
				String auditMethodSn="";
				int count=0;
				int sumcount=0;
				for(int i=0;i<methods.split(",").length;i++){
					StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getById(Integer.parseInt(methods.split(",")[i].split("#")[0]));
					auditMethodSn=standardIndexAuditMethod.getAuditMethodSn();
					count=Integer.parseInt(methods.split(",")[i].split("#")[1]);
					auditMethods.put(auditMethodSn, count);
					if(standardIndexAuditMethod.getIndexDeducted()!=null&&standardIndexAuditMethod.getDeduction()!=null){
						deindex=standardIndexAuditMethod.getIndexDeducted();
						score+=count*standardIndexAuditMethod.getDeduction();
					}else if(standardIndexAuditMethod.getIndexDeducted()==null&&index.getAnDeduction()!=null){
						sumcount+=count;
					}
				}
				//�ж���ָͨ���Ƿ���
				float commonScore=0;
				if(index.getAnDeduction()!=null){
					if(index.getZeroTimes()!=null){
						if(index.getZeroTimes()>sumcount){
							commonScore=sumcount*index.getAnDeduction();
						}else if(index.getPercentageScore()!=null){
							commonScore=index.getPercentageScore();
						}else{
							commonScore=sumcount*index.getAnDeduction();
						}	
					}else if(index.getPercentageScore()!=null){
						if(sumcount*index.getAnDeduction()>index.getPercentageScore()){
							commonScore=index.getPercentageScore();
						}else{
							commonScore=sumcount*index.getAnDeduction();
						}
					}
				}
				score+=commonScore;
				//�жϸ���ָ���Ƿ�
				if(deindex!=null&&deindex.getPercentageScore()!=null&&score>deindex.getPercentageScore()){
					score=deindex.getPercentageScore();
				}
								
			}else{
				String auditMethodSn="";
				int count=0;
				int sumcount=0;
				for(int i=0;i<methods.split(",").length;i++){
					StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getById(Integer.parseInt(methods.split(",")[i].split("#")[0]));
					auditMethodSn=standardIndexAuditMethod.getAuditMethodSn();
					count=Integer.parseInt(methods.split(",")[i].split("#")[1]);
					auditMethods.put(auditMethodSn, count);
					sumcount+=count;
				}
				//�۷�
				if(index.getAnDeduction()!=null){
					if(index.getZeroTimes()!=null){
						if(index.getZeroTimes()>sumcount){
							score=sumcount*index.getAnDeduction();
						}else if(index.getPercentageScore()!=null){
							score=index.getPercentageScore();
						}else{
							score=sumcount*index.getAnDeduction();
						}	
					}else if(index.getPercentageScore()!=null){
						if(sumcount*index.getAnDeduction()>index.getPercentageScore()){
							score=index.getPercentageScore();
						}else{
							score=sumcount*index.getAnDeduction();
						}
					}
				}
			}
			inconformityItem.setDeductPoints(score);
			inconformityItem.setAuditMethods(auditMethods);
		}else{
			inconformityItem.setAuditMethods(auditMethods);
		}
		
		//�����Ա��
		if(ids!=null&&ids.length()>0){
		List<Person> personList=personService.getByPersonIds(ids);	 
		if(personList.size()>0){
			Set<Person> set = new HashSet<Person>(personList);
			inconformityItem.setCheckers(set);
		}else{
			inconformityItem.setCheckers(null);
		}
		}
		//���ĸ�����
		inconformityItem.setCorrectPrincipal(personService.getByPersonId(personId));
		//��
		inconformityItem.setMachine(manageObjectService.getByManageObjectSn(manageObjectSn));
		//���첿��
		inconformityItem.setCheckedDepartment(departmentService.getByDepartmentSn(departmentSn));
		//ָ��
		inconformityItem.setStandardIndex(index);
		//Σ��Դ
		if(hazardSn!=null&&hazardSn.trim().length()>0){
			inconformityItem.setHazrd(hazardService.getByHazardSn(hazardSn));
		}
		//רҵ
		inconformityItem.setSpeciality(specialityService.getBySpecialitySn(specialitySn));
		
		unsafeConditionService.update(inconformityItem);
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
		//���ȿ��˴��	
        try{
        	//���������
        	CheckerFrom checkerFrom1=inconformityItem.getCheckerFrom();
        	Byte updatetype=1;
        	Department newdepartment=null;
        	Department olddepartment=null;
        	if(checkerFrom1!=null&&(checkerFrom1.equals(CheckerFrom.�ⲿ) || checkerFrom1.equals(CheckerFrom.ҵ����) || checkerFrom1.equals(CheckerFrom.��) || checkerFrom1.equals(CheckerFrom.�߲������Ա))){
        		updatetype=0;
        		//�²�������
    			List<DepartmentType> newdepartmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
        		
            	if(newdepartmentTypes.size()>0){
            		newdepartment=departmentService.getByDepartmentSn(departmentSn);
            	}else{
            		newdepartment=departmentService.getUpNearestImplDepartment(departmentSn);
            	}
            	//�ɲ�������
    			List<DepartmentType> olddepartmentTypes=departmentTypeService.getImplDepartmentTypes(oldDepartmentSn);
        		
            	if(olddepartmentTypes.size()>0){
            		olddepartment=departmentService.getByDepartmentSn(oldDepartmentSn);
            	}else{
            		olddepartment=departmentService.getUpNearestImplDepartment(oldDepartmentSn);
            	}
        	}else{
        		newdepartment=departmentService.getByDepartmentSn(departmentSn);
        		olddepartment=departmentService.getByDepartmentSn(oldDepartmentSn);
        	}
    		
    		//���ڸĵ�ʱ��
    		LocalDate newlocalDate = checkDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    		//�ж�ʱ���Ƿ�ı�
    		if(newlocalDate==oldlocalDate){
	        	UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(newdepartment.getDepartmentSn(), newlocalDate,updatetype);
				if(unitAppraisals!=null){
					unitAppraisals.setNeedComputing(true);
					unitAppraisalsService.update(unitAppraisals);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(newlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(newdepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
				
				UnitAppraisals oldunitAppraisals=unitAppraisalsService.queryByMany(olddepartment.getDepartmentSn(), newlocalDate,updatetype);
				if(oldunitAppraisals!=null){
					oldunitAppraisals.setNeedComputing(true);
					unitAppraisalsService.update(oldunitAppraisals);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(newlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(olddepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
    			
    		}else{
    			UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(newdepartment.getDepartmentSn(), newlocalDate,updatetype);
				if(unitAppraisals!=null){
					unitAppraisals.setNeedComputing(true);
					unitAppraisalsService.update(unitAppraisals);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(newlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(newdepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
				
				UnitAppraisals oldunitAppraisals=unitAppraisalsService.queryByMany(olddepartment.getDepartmentSn(), newlocalDate,updatetype);
				if(oldunitAppraisals!=null){
					oldunitAppraisals.setNeedComputing(true);
					unitAppraisalsService.update(oldunitAppraisals);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(newlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(olddepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
    			
				UnitAppraisals unitAppraisals3=unitAppraisalsService.queryByMany(newdepartment.getDepartmentSn(), oldlocalDate,updatetype);
				if(unitAppraisals3!=null){
					unitAppraisals3.setNeedComputing(true);
					unitAppraisalsService.update(unitAppraisals3);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(oldlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(newdepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
				
				UnitAppraisals oldunitAppraisals2=unitAppraisalsService.queryByMany(olddepartment.getDepartmentSn(), oldlocalDate,updatetype);
				if(oldunitAppraisals2!=null){
					oldunitAppraisals2.setNeedComputing(true);
					unitAppraisalsService.update(oldunitAppraisals2);
				}else{
					UnitAppraisals unitAppraisals2=new UnitAppraisals();
					unitAppraisals2.setAppraisalsDate(oldlocalDate);
					unitAppraisals2.setNeedComputing(true);
					unitAppraisals2.setDepartment(olddepartment);
					unitAppraisals2.setType(updatetype);
					unitAppraisalsService.save(unitAppraisals2);
				}
    		}
        		
        }catch(Exception e){
	    	System.out.println("����ÿ�տ��˳���");
	    }
        //����ÿ�¿���
        try{
	    	LocalDate oldTime=LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());//�ϸ������һ��
	    	if(checkDateTime.toLocalDateTime().toLocalDate().isBefore(oldTime) || checkDateTime.toLocalDateTime().toLocalDate()==oldTime){
	    		oldTime=oldTime.withDayOfMonth(1);//�ϸ���һ��
	    		//�ж��ϸ����Ƿ��Ѿ�����Ҫ����������ˣ�����У��������true
	    		String hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null and c.needComputing=true";
	    		List<CheckTaskAppraisals> list1=checkTaskAppraisalsService.getByHql(hql);
	    		//�������0������������û����Ҫ���������
	    		if(list1.size()==0){
	    			hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null";
	    			List<CheckTaskAppraisals> list2=checkTaskAppraisalsService.getByHql(hql);
	    			if(list2.size()>0){
	    				for(CheckTaskAppraisals ch:list2){
	    					ch.setNeedComputing(true);
	    					checkTaskAppraisalsService.update(ch);
	    					break;
	    				}
	    			}
	    		}
	    	}
	    }catch(Exception e){
	    	System.out.println("����ÿ�¿��˳���");
	    }
        return "jsonLoad";
	}
	//dan��ɾ��
	public void deleteByIds() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		String str="{\"status\":\"ok\",\"message\":\"ɾ���ɹ���\"}";
		try{
			unsafeConditionService.deleteByIds(ids);
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"ɾ��ʧ�ܣ�\"}";
		}
		out.print(str);
        out.flush(); 
        out.close();
        //��λ���
        try{
        	UnsafeCondition unsafeCondition=unsafeConditionService.getById(Integer.valueOf(ids.split(",")[0]));
        	if(unsafeCondition!=null){
        		CheckerFrom checkerFrom1=unsafeCondition.getCheckerFrom();
            	Byte updatetype=1;
            	if(checkerFrom1!=null&&(checkerFrom1.equals(CheckerFrom.�ⲿ) || checkerFrom1.equals(CheckerFrom.ҵ����) || checkerFrom1.equals(CheckerFrom.��) || checkerFrom1.equals(CheckerFrom.�߲������Ա))){
            		updatetype=0;
            	}
        		LocalDate localDate = unsafeCondition.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        		UnitAppraisals unitAppraisals=unitAppraisalsService.queryByMany(unsafeCondition.getCheckedDepartment().getDepartmentSn(), localDate,updatetype);
        		if(unitAppraisals!=null){
        			unitAppraisals.setNeedComputing(true);
        			unitAppraisalsService.update(unitAppraisals);
        		}
        	}
        }catch(Exception e){
        	System.out.println("����ÿ�տ��˳���");
        }
      //����ÿ�¿���
        try{
	    	LocalDate oldTime=LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());//�ϸ������һ��
	    	UnsafeCondition unsafeCondition=unsafeConditionService.getById(Integer.valueOf(ids.split(",")[0]));
	    	checkDateTime=unsafeCondition.getCheckDateTime();
	    	if(checkDateTime!=null){
	    		if(checkDateTime.toLocalDateTime().toLocalDate().isBefore(oldTime) || checkDateTime.toLocalDateTime().toLocalDate()==oldTime){
	    			oldTime=oldTime.withDayOfMonth(1);//�ϸ���һ��
	    			//�ж��ϸ����Ƿ��Ѿ�����Ҫ����������ˣ�����У��������true
	    			String hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null and c.needComputing=true";
	    			List<CheckTaskAppraisals> list1=checkTaskAppraisalsService.getByHql(hql);
	    			//�������0������������û����Ҫ���������
	    			if(list1.size()==0){
	    				hql="select c from CheckTaskAppraisals c where c.yearMonth='"+oldTime.toString()+"' and c.checker is null";
	    				List<CheckTaskAppraisals> list2=checkTaskAppraisalsService.getByHql(hql);
	    				if(list2.size()>0){
	    					for(CheckTaskAppraisals ch:list2){
	    						ch.setNeedComputing(true);
	    						checkTaskAppraisalsService.update(ch);
	    						break;
	    					}
	    				}
	    			}
	    		}
	    	}
	    }catch(Exception e){
	    	System.out.println("����ÿ�¿��˳���");
	    }
	}	
	
	public String getIndexSn(){
		return indexSn;
	}

	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}

	public String getStrCheckers() {
		return strCheckers;
	}

	public void setStrCheckers(String strCheckers) {
		this.strCheckers = strCheckers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuditSn() {
		return auditSn;
	}

	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}

	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}

	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}

	public CheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(CheckType checkType) {
		this.checkType = checkType;
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

	public InconformityItemNature getInconformityItemNature() {
		return inconformityItemNature;
	}

	public void setInconformityItemNature(InconformityItemNature inconformityItemNature) {
		this.inconformityItemNature = inconformityItemNature;
	}

	public InconformityLevel getInconformityLevel() {
		return inconformityLevel;
	}

	public void setInconformityLevel(InconformityLevel inconformityLevel) {
		this.inconformityLevel = inconformityLevel;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public float getDeductPoints() {
		return deductPoints;
	}

	public void setDeductPoints(float deductPoints) {
		this.deductPoints = deductPoints;
	}

	public CorrectType getCorrectType() {
		return correctType;
	}

	public void setCorrectType(CorrectType correctType) {
		this.correctType = correctType;
	}

	public Timestamp getCorrectDeadline() {
		return correctDeadline;
	}

	public void setCorrectDeadline(Timestamp correctDeadline) {
		this.correctDeadline = correctDeadline;
	}

	public String getCorrectProposal() {
		return correctProposal;
	}

	public void setCorrectProposal(String correctProposal) {
		this.correctProposal = correctProposal;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getHazardSn() {
		return hazardSn;
	}

	public void setHazardSn(String hazardSn) {
		this.hazardSn = hazardSn;
	}

	public String getManageObjectSn() {
		return manageObjectSn;
	}

	public void setManageObjectSn(String manageObjectSn) {
		this.manageObjectSn = manageObjectSn;
	}

	public String getDepartmentSn() {
		return departmentSn;
	}

	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}

	public String getSpecialitySn() {
		return specialitySn;
	}

	public void setSpecialitySn(String specialitySn) {
		this.specialitySn = specialitySn;
	}

	public int getInconformityItemid() {
		return inconformityItemid;
	}

	public void setInconformityItemid(int inconformityItemid) {
		this.inconformityItemid = inconformityItemid;
	}

	

	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}

	public String getPeriodicalCheckSn() {
		return periodicalCheckSn;
	}

	public void setPeriodicalCheckSn(String periodicalCheckSn) {
		this.periodicalCheckSn = periodicalCheckSn;
	}

	public String getSpecialCheckSn() {
		return specialCheckSn;
	}

	public void setSpecialCheckSn(String specialCheckSn) {
		this.specialCheckSn = specialCheckSn;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	public String getRedistribution() {
		return redistribution;
	}

	public void setRedistribution(String redistribution) {
		this.redistribution = redistribution;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
}