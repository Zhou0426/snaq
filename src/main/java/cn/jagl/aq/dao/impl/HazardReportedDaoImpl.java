package cn.jagl.aq.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.HazardReportedDao;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.HazardReported;
import cn.jagl.aq.domain.ManageObject;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.UnsafeCondition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("hazardReportedDao")
public class HazardReportedDaoImpl extends BaseDaoHibernate5<HazardReported> implements HazardReportedDao {

	public long getByHql(String hql){
		return (long) getSessionFactory().getCurrentSession()
				.createQuery(hql).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAllData(String personId, HashMap<String, String> roles,int page,int rows) {
		String hql="select h from HazardReported h where h.deleted=false and h.reportPerson.personId='"+personId+"' order by h.auditedStatus asc";
		List<HazardReported> list=(List<HazardReported>)getSessionFactory().getCurrentSession()
										.createQuery(hql).setFirstResult((page - 1) * rows).setMaxResults(rows).list();
		JSONArray array=new JSONArray();
		for(HazardReported haz:list){
			JSONObject jo=new JSONObject();
			jo.put("id", haz.getId());
			if(haz.getDepartmentType()!=null){
				jo.put("departmentTypeName", haz.getDepartmentType().getDepartmentTypeName());
				jo.put("departmentTypeSn", haz.getDepartmentType().getDepartmentTypeSn());
			}
			jo.put("reportSn", haz.getReportSn());
			jo.put("hazardDescription", haz.getHazardDescription());
			jo.put("resultDescription", haz.getResultDescription());
			if(haz.getAccidentType()!=null){
				jo.put("accidentType", haz.getAccidentType().getAccidentTypeName());
				jo.put("accidentTypeSn", haz.getAccidentType().getAccidentTypeSn());
			}
			jo.put("riskLevel", haz.getRiskLevel());
			if(haz.getReportPerson()!=null){
				jo.put("reportPerson", haz.getReportPerson().getPersonName());
				jo.put("reportPersonId", haz.getReportPerson().getId());
			}
			if(haz.getReportDateTime()!=null){
				jo.put("reportDateTime", haz.getReportDateTime().toLocalDate().toString());
			}
			if(haz.getAuditedStatus()!=null){
				if(haz.getAuditedStatus()==false){
					jo.put("auditedStatus", "审核未通过");
				}else{
					jo.put("auditedStatus", "审核通过");
				}
			}else{
				jo.put("auditedStatus", haz.getAuditedStatus());
			}
			jo.put("auditSuggestion", haz.getAuditSuggestion());
			if(haz.getAuditor()!=null){
				jo.put("auditor", haz.getAuditor().getPersonName());
				jo.put("auditorId", haz.getAuditor().getId());
			}
			jo.put("standardIndex", haz.getStandardIndexes().size());
				
			array.add(jo);
		}
		hql=hql.replaceFirst("h", "count(*)");
		long total=this.getByHql(hql);
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", array);// rows键 存放每页记录 list
		return json;
	}

	@Override
	public void deleteData(int id) {
		String hql="update HazardReported h set h.deleted=true where h.id='"+id+"'";
		getSessionFactory().getCurrentSession().createQuery(hql).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryUnsafeCondition(String departmentSn, String hazardSn,int page, int rows) {
		String hql="select u from UnsafeCondition u where u.deleted=false"
				+ " and u.hazrd.hazardSn = '"+ hazardSn + "'"
				+ " AND u.checkedDepartment.departmentSn like '" + departmentSn + "%" +"'";
		List<UnsafeCondition> list=(List<UnsafeCondition>)getSessionFactory().getCurrentSession()
										.createQuery(hql)
										.setFirstResult((page - 1) * rows)
										.setMaxResults(rows).list();
		JSONArray jsonArray=new JSONArray();
		for(UnsafeCondition inc:list){
  		  JSONObject jo=new JSONObject();
  		  //检查人checkers
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
  		  jo.put("checkDateTime", inc.getCheckDateTime().toString());
  		  jo.put("checkLocation", inc.getCheckLocation());
  		  jo.put("inconformityItemNature", inc.getInconformityItemNature());
  		  if(inc.getMachine()!=null){
  			  jo.put("machine", inc.getMachine().getManageObjectName());
  		  }else{
  			  jo.put("machine", inc.getMachine());
  		  }
  		  if(inc.getCheckedDepartment()!=null){
  			  jo.put("checkedDepartment", inc.getCheckedDepartment().getDepartmentName());
  			  jo.put("implDepartmentName", inc.getCheckedDepartment().getImplDepartmentName());
  		  }else{
  			  jo.put("checkedDepartment", inc.getCheckedDepartment());
  		  }
  		  if(inc.getStandardIndex()!=null){
  			  jo.put("standardIndex", inc.getStandardIndex().getIndexName());
  		  }else{
  			  jo.put("standardIndex", inc.getStandardIndex());
  		  }
  		  if(inc.getSystemAudit()!=null){
  			  jo.put("systemAudit",inc.getSystemAudit().getSystemAuditType());
  		  }
  		  jo.put("problemDescription", inc.getProblemDescription());
  		  jo.put("deductPoints", inc.getDeductPoints());
  		  jo.put("correctType", inc.getCorrectType());
  		  jo.put("correctDeadline", inc.getCorrectDeadline().toString());
  		  if(inc.getHazrd()!=null){
  			  jo.put("hazrd", inc.getHazrd().getHazardDescription());
  		  }else{
  			  jo.put("hazrd", inc.getHazrd());
  		  }
  		  if(inc.getSpeciality()!=null){
  			  jo.put("speciality", inc.getSpeciality().getSpecialityName());
  		  }else{
  			  jo.put("speciality", inc.getSpeciality());
  		  }
  		  jo.put("inconformityLevel", inc.getInconformityLevel());
  		  if(inc.getInconformityLevel()!=null){
  			  if(inc.getInconformityLevel().toString()=="观察项"){
  				  jo.put("riskLevel", "一般风险");
  			  }else if(inc.getInconformityLevel().toString()=="一般不符合项"){
  				  jo.put("riskLevel", "中等风险");
  			  }else if(inc.getInconformityLevel().toString()=="严重不符合项"){
  				  jo.put("riskLevel", "重大风险");
  			  }
  		  }
  		  if(inc.getCorrectPrincipal()!=null){
  			  jo.put("correctPrincipal", inc.getCorrectPrincipal().getPersonName());
  		  }else{
  			  jo.put("correctPrincipal", inc.getCorrectPrincipal());
  		  }
  		  jo.put("correctProposal", inc.getCorrectProposal());
  		  if(inc.getCurrentRiskLevel()!=null){
  			  jo.put("currentRiskLevel", inc.getCurrentRiskLevel().toString());
  		  }
  		  jo.put("attachments", inc.getAttachments().size());
  		  if(inc.getHasCorrectConfirmed()!=null){
  			  if(inc.getHasCorrectConfirmed()!=true){
	    			  jo.put("hasCorrectConfirmed", "未整改确认");
	    		  }else{
	    			  jo.put("hasCorrectConfirmed", "已整改确认");
	    		  }
  		  }
  		  if(inc.getHasReviewed()!=null){
  			  if(inc.getHasReviewed()!=true){
	    			  jo.put("hasReviewed","未复查");
	    		  }else{
	    			  jo.put("hasReviewed","已复查");
	    		  }
  		  }
  		  if(inc.getHasCorrectFinished()!=null){
  			  if(inc.getHasCorrectFinished()!=true){
	    			  jo.put("hasCorrectFinished", "未整改完成");
	    		  }else{
	    			  jo.put("hasCorrectFinished", "已整改完成");
	    		  }
  		  }
  		  
  		  jsonArray.add(jo);
  	  }
		hql=hql.replaceFirst("u", "count(*)");
		long total=this.getByHql(hql);
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		return json;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryCount(String departmentSn, String departmentTypeSn, int page, int rows) {
		String hql="select h.hazardSn as hazardSn,count(*) as countNum"
				+ " from UnsafeCondition u LEFT JOIN u.hazrd h where u.deleted=false"
				+ " and h.hazardSn is not null and h.departmentType.departmentTypeSn=:departmentTypeSn"
				+ " AND u.checkedDepartment.departmentSn like :departmentSn group by h.hazardSn order by countNum desc";
		List<Object> list=getSessionFactory().getCurrentSession().createQuery(hql)
							.setString("departmentTypeSn", departmentTypeSn)
							.setString("departmentSn", departmentSn + "%")
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
							.setFirstResult((page - 1)*rows).setMaxResults(rows).list();
		List<Object> list2=getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentTypeSn", departmentTypeSn)
				.setString("departmentSn", departmentSn + "%")
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		JSONArray jsonArray=new JSONArray();
		if(list.size()>0){
			for(int i = 0; i < list.size(); i++){
				JSONObject jo=new JSONObject();
				Map<Object, Object> map = (Map<Object, Object>)list.get(i);
				hql="select h from Hazard h where h.hazardSn='"+map.get("hazardSn")+"'";
				Hazard hazard=(Hazard) getSessionFactory().getCurrentSession()
								.createQuery(hql).uniqueResult();
				jo.put("id", hazard.getId());
				jo.put("departmentTypeSn", hazard.getDepartmentType().getDepartmentTypeSn());
				jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
				jo.put("hazardSn", hazard.getHazardSn());
				jo.put("hazardDescription", hazard.getHazardDescription());
				jo.put("resultDescription", hazard.getResultDescription());
				jo.put("accidentTypeName", hazard.getAccidentType().getAccidentTypeName());
				jo.put("accidentTypeSn", hazard.getAccidentType().getAccidentTypeSn());
				jo.put("riskLevel", hazard.getRiskLevel());
				Set<ManageObject> Man=hazard.getManageObjects();
				if(Man.size()>0){
					String Str="";
					for(Iterator<ManageObject> iter=Man.iterator();iter.hasNext();){
						Str=Str+iter.next().getManageObjectName()+",";
					}
					Str=Str.substring(0, Str.lastIndexOf(","));
					jo.put("manageObjectName",Str);
				}
				jo.put("standardIndexNum",hazard.getStandardIndexes().size());
				jo.put("countNum", map.get("countNum"));
				jsonArray.add(jo);
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", list2.size());// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		return json;
	}
	@Override
	public HazardReported getById(int id) {
		String hql="select h from HazardReported h where h.deleted!=true and h.id='"+id+"'";
		return (HazardReported) getSessionFactory().getCurrentSession().createQuery(hql).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> showStandardIndex(String reportSn,int page,int rows) {
		String hql="select distinct s from StandardIndex s LEFT JOIN s.hazardReporteds h where s.deleted=false and h.reportSn = '"+reportSn+"'";
		List<StandardIndex> list=(List<StandardIndex>)getSessionFactory().getCurrentSession()
										.createQuery(hql).setFirstResult((page - 1) * rows).setMaxResults(rows).list();
//		List<StandardIndex> list2=(List<StandardIndex>)getSessionFactory().getCurrentSession()
//										.createQuery(hql).list();
		JSONArray jsonArray=new JSONArray();
		for(StandardIndex sIndex:list){
			JSONObject jo=new JSONObject();
			jo.put("id", sIndex.getId());
			jo.put("indexSn", sIndex.getIndexSn());
			jo.put("indexName", sIndex.getIndexName());
			if(sIndex.getIsKeyIndex() != null && sIndex.getIsKeyIndex() == true){
				jo.put("isKeyIndex", "是");
			}else{
				jo.put("isKeyIndex", "否");
			}
			if(sIndex.getSpecialities().size() > 0){
				String str="";
				for(Speciality spec:sIndex.getSpecialities()){
					str = str + spec.getSpecialityName() +",";
				}
				str = str.substring(0, str.length() - 1);
				jo.put("specialities", str);
			}else{
				jo.put("specialities", "");
			}
			jsonArray.add(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", list.size());// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		return json;
	}
	

}
