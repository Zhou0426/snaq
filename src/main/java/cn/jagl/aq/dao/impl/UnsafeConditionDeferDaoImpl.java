package cn.jagl.aq.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeConditionDeferDao;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.domain.UnsafeConditionDefer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("unsafeConditionDeferDao")
public class UnsafeConditionDeferDaoImpl extends BaseDaoHibernate5<UnsafeConditionDefer> implements UnsafeConditionDeferDao{

	@Override
	public UnsafeConditionDefer getBySn(String applicationSn) {
		String hql="select s from UnsafeConditionDefer s where s.applicationSn=:applicationSn";
		return (UnsafeConditionDefer) getSessionFactory().getCurrentSession().createQuery(hql).setString("applicationSn", applicationSn).uniqueResult();
		 
	}

	@Override
	public long getByhql(String hql) {
		return (long) getSessionFactory().getCurrentSession().createQuery(hql).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getListByPersonId(String personId) {
		String hql = "select distinct u from UnsafeConditionDefer u LEFT JOIN u.unsafecondition.checkers c where u.unsafecondition.editor.personId='" + personId + "' OR c.personId = '" + personId + "' Order By u.auditDatetime Asc";
		List<UnsafeConditionDefer> list = getSessionFactory().getCurrentSession().createQuery(hql).list();
		
		JSONArray jsonArray=new JSONArray();
		
		for(UnsafeConditionDefer unsafeConditionDefer:list){
			
			JSONObject jo=new JSONObject();
			
			if(unsafeConditionDefer.getUnsafecondition() != null){
				UnsafeCondition inc = unsafeConditionDefer.getUnsafecondition();
				//检查人checkers
	    		  Set<Person> set = inc.getCheckers();
	    			  if( set.size() > 0 ){
	    				  String s="";
	    				  for( Iterator<Person> iter = set.iterator(); iter.hasNext();){
	    					  s += iter.next().getPersonName() + ",";
	    				  }
	    				  s = s.substring(0, s.lastIndexOf(",") );
	    				  jo.put("checkers", s);
	    			  }else{
	    				  jo.put("checkers", "");
	    			  }
	    		  jo.put("checkType", inc.getCheckType());
	    		  jo.put("checkerFrom", inc.getCheckerFrom());
	    		  jo.put("inconformityItemSn", inc.getInconformityItemSn());
	    		  if(inc.getCheckDateTime() != null){
	    			  jo.put("checkDateTime", inc.getCheckDateTime().toString());
	    		  }else{
	    			  jo.put("checkDateTime", inc.getCheckDateTime());
	    		  }
	    		  jo.put("checkLocation", inc.getCheckLocation());
	    		  jo.put("inconformityItemNature", inc.getInconformityItemNature());
	    		  if(inc.getMachine() != null){
	    			  jo.put("machine", inc.getMachine().getManageObjectName());
	    		  }else{
	    			  jo.put("machine", inc.getMachine());
	    		  }
	    		  if(inc.getCheckedDepartment() != null){
	    			  jo.put("checkedDepartment", inc.getCheckedDepartment().getDepartmentName());
	    			  jo.put("checkedDepartmentImplType", inc.getCheckedDepartment().getImplDepartmentName());
	    		  }else{
	    			  jo.put("checkedDepartment", inc.getCheckedDepartment());
	    			  jo.put("checkedDepartmentImplType", "");
	    		  }
	    		  if(inc.getStandardIndex() != null){
	    			  jo.put("standardIndex", inc.getStandardIndex().getIndexName());
	    		  }else{
	    			  jo.put("standardIndex", inc.getStandardIndex());
	    		  }
	    		  if(inc.getSystemAudit() != null){
	    			  jo.put("systemAudit",inc.getSystemAudit().getSystemAuditType());
	    		  }else{
	    			  jo.put("systemAudit","");
	    		  }
	    		  jo.put("problemDescription", inc.getProblemDescription());
	    		  jo.put("deductPoints", inc.getDeductPoints());
	    		  jo.put("correctType", inc.getCorrectType());
	    		  if(inc.getCorrectDeadline() != null){
	    			  jo.put("correctDeadline", inc.getCorrectDeadline().toString());
	    		  }else{
	    			  jo.put("correctDeadline", inc.getCorrectDeadline());
	    		  }
	    		  if(inc.getHazrd() != null){
	    			  jo.put("hazrd", inc.getHazrd().getHazardDescription());
	    		  }else{
	    			  jo.put("hazrd", inc.getHazrd());
	    		  }
	    		  if(inc.getSpeciality() != null){
	    			  jo.put("speciality", inc.getSpeciality().getSpecialityName());
	    		  }else{
	    			  jo.put("speciality", inc.getSpeciality());
	    		  }
	    		  jo.put("inconformityLevel", inc.getInconformityLevel());
	    		  if(inc.getInconformityLevel() != null){
	    			  if(inc.getInconformityLevel().toString() == "观察项"){
	    				  jo.put("riskLevel", "一般风险");
	    			  }else if(inc.getInconformityLevel().toString() == "一般不符合项"){
	    				  jo.put("riskLevel", "中等风险");
	    			  }else if(inc.getInconformityLevel().toString() == "严重不符合项"){
	    				  jo.put("riskLevel", "重大风险");
	    			  }
	    		  }else{
	    			  jo.put("riskLevel", "");
	    		  }
	    		  if(inc.getCorrectPrincipal() != null){
	    			  jo.put("correctPrincipal", inc.getCorrectPrincipal().getPersonName());
	    		  }else{
	    			  jo.put("correctPrincipal", inc.getCorrectPrincipal());
	    		  }
	    		  jo.put("correctProposal", inc.getCorrectProposal());
	    		  if(inc.getCurrentRiskLevel() != null){
	    			  jo.put("currentRiskLevel", inc.getCurrentRiskLevel().toString());
	    		  }else{
	    			  jo.put("currentRiskLevel", "");
	    		  }
	    		  jo.put("attachments", inc.getAttachments().size());
	    		  if(inc.getEditor() != null){
					  jo.put("editor", inc.getEditor().getPersonName());
					  jo.put("editorId", inc.getEditor().getPersonId());
				  }else{
					  jo.put("editor", "无");
					  jo.put("editorId", "无");
				  }
			}
			jo.put("id", unsafeConditionDefer.getId());
			jo.put("applicationSn", unsafeConditionDefer.getApplicationSn());
			jo.put("reason", unsafeConditionDefer.getReason());
			if(unsafeConditionDefer.getApplyDateTime() != null){
				jo.put("applyDateTime", "无");
			}else{
				jo.put("applyDateTime", unsafeConditionDefer.getApplyDateTime().toString());
			}
			if(unsafeConditionDefer.getApplicant() != null){
				jo.put("applicant", unsafeConditionDefer.getApplicant().getPersonName());
			}else{
				jo.put("applicant", "无");
			}
			jo.put("applyDeferTo", unsafeConditionDefer.getApplyDeferTo().toString());
			if(unsafeConditionDefer.getAuditor() != null){
				jo.put("auditor", unsafeConditionDefer.getAuditor().getPersonName());
			}else{
				jo.put("auditor", "无");
			}
			if(unsafeConditionDefer.getPassed() != null && unsafeConditionDefer.getPassed() == true){
				jo.put("passed", "同意");
			}else if(unsafeConditionDefer.getPassed() != null && unsafeConditionDefer.getPassed() == false){
				jo.put("passed", "不同意");
			}else{
				jo.put("passed", unsafeConditionDefer.getPassed());
			}
			jo.put("auditRemark", unsafeConditionDefer.getAuditRemark());
			if(unsafeConditionDefer.getAuditDatetime() != null){
				jo.put("auditDatetime", unsafeConditionDefer.getAuditDatetime().toString());
			}else{
				jo.put("auditDatetime", "无");
			}
			jsonArray.add(jo);
		}
		return jsonArray;
	}

}
