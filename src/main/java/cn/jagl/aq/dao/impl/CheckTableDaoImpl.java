package cn.jagl.aq.dao.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.CheckTableDao;
import cn.jagl.aq.dao.StandardIndexDao;
import cn.jagl.aq.dao.UnsafeConditionDao;
import cn.jagl.aq.domain.CheckTable;
import cn.jagl.aq.domain.CheckTableChecker;
import cn.jagl.aq.domain.PeriodicalCheck;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.SpecialCheck;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.SystemAudit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author mahui
 * @method 
 * @date 2016年8月2日下午3:11:45
 */
@Repository("checkTableDao")
public class CheckTableDaoImpl extends BaseDaoHibernate5<CheckTable> implements CheckTableDao {

	@Resource(name="standardIndexDao")
	private StandardIndexDao standardIndexDao;
	@Resource(name="unsafeConditionDao")
	private UnsafeConditionDao unsafeConditionDao;
	//查询我的检查表
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject query(String personId,int page,int rows) {
		//String hql="select t from CheckTable t INNER JOIN t.checkTableCheckers c WHERE c.hasCompletedCheck=false AND c.checker.personId like '"+personId+"'";
		String hql="select t from CheckTable t INNER JOIN t.checkTableCheckers c WHERE c.checker.personId like '"+personId+"' order by c.hasCompletedCheck asc, t.checkTableSn desc";
		JSONArray array=new JSONArray();
		List<CheckTable> list=new ArrayList<CheckTable>();
		list=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setFirstResult((page-1)*rows)
				.setMaxResults(rows)
				.list();
		for(CheckTable checkTable:list){
			JSONObject jo=new JSONObject();
			jo.put("checkTableSn",checkTable.getCheckTableSn());
			jo.put("id", checkTable.getId());
			PeriodicalCheck periodicalCheck=checkTable.getPeriodicalCheck();
			SpecialCheck specialCheck=checkTable.getSpecialCheck();
			SystemAudit audit=checkTable.getSystemAudit();
			if(periodicalCheck!=null){
				jo.put("checkType", "定期检查");
				jo.put("periodicalCheckSn", periodicalCheck.getPeriodicalCheckSn());
				jo.put("startDate",periodicalCheck.getStartDate().toString());
				jo.put("endDate", periodicalCheck.getEndDate().toString());
				jo.put("inconformityItem", periodicalCheck.getInconformityItem().size());
				jo.put("checkerFrom", periodicalCheck.getCheckerFrom());
				jo.put("standardSn", periodicalCheck.getStandard().getStandardSn());
			}else if(specialCheck!=null){
				jo.put("checkType", "专项检查");
				jo.put("startDate",specialCheck.getStartDate().toString());
				jo.put("endDate", specialCheck.getEndDate().toString());
				jo.put("specialCheckSn", specialCheck.getSpecialCheckSn());
				jo.put("inconformityItem", specialCheck.getInconformityItem().size());
				jo.put("checkerFrom", specialCheck.getCheckerFrom());
				jo.put("standardSn", specialCheck.getStandard().getStandardSn());
			}else if(audit!=null){
				jo.put("checkType", audit.getSystemAuditType());	
				jo.put("startDate", audit.getStartDate().toString());
				jo.put("endDate", audit.getEndDate().toString());
				jo.put("auditSn", audit.getAuditSn());
				jo.put("inconformityItem", audit.getInconformityItemes().size());
				jo.put("standardSn", audit.getStandard().getStandardSn());
			}
			//检查人员
			int num=0;
			String personIds="";
			String personNames="";
			String pIds="";
			JSONObject ch=new JSONObject();
			String confirm="未确认";
			for(CheckTableChecker  checkTableChecker:checkTable.getCheckTableCheckers()){
				Person person=checkTableChecker.getChecker();
				if(person!= null){
					num++;
					personIds+=person.getPersonId()+",";
					personNames+=person.getPersonName()+",";
					pIds+=person.getId()+",";
					if(checkTableChecker.getChecker().getPersonId().equals(personId)){
						if(checkTableChecker.getHasCompletedCheck()==true){
							if(checkTableChecker.getAffirmDateTime()!=null){
								confirm="已确认["+checkTableChecker.getAffirmDateTime().getYear()+"-"+checkTableChecker.getAffirmDateTime().getMonth().getValue()+"-"+checkTableChecker.getAffirmDateTime().getDayOfMonth()+"]";
							}else{
								confirm="已确认[未知]";
							}
							
						}
					}
				}
				
			}
			jo.put("confirm", confirm);
			if(num>0){
				personIds=personIds.substring(0,personIds.length()-1);
				personNames=personNames.substring(0,personNames.length()-1);
				pIds=pIds.substring(0, pIds.length()-1);
				ch.put("num", num);
				ch.put("personIds",personIds);
				ch.put("pIds", pIds);
				ch.put("personNames",personNames);
			}else{
				ch.put("num", num);
			}
			jo.put("checkers", ch);
			
			array.add(jo);
			
		}
		JSONObject jo=new JSONObject();
		jo.put("total",count(personId));
		jo.put("rows", array);
		return jo;
	}

	@Override
	public long count(String personId) {
		String hql="select count(t) from CheckTable t INNER JOIN t.checkTableCheckers c WHERE c.checker.personId like '"+personId+"'";		
		return (long) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.uniqueResult();
	}
	
	@Override
	public long countExceptTrue(String personId) {
		String hql="select count(t) from CheckTable t INNER JOIN t.checkTableCheckers c WHERE c.hasCompletedCheck=false AND c.checker.personId like '"+personId+"'";		
		return (long) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.uniqueResult();
	}

	//查询检查表
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray queryJoinCheck(String specialCheckSn, String periodicalCheckSn, String auditSn) {
		String hql="select c from CheckTable c";
		if(specialCheckSn!=null && specialCheckSn.trim().length()>0){
			hql+=" WHERE c.specialCheck.specialCheckSn="+specialCheckSn;
		}
		if(periodicalCheckSn!=null && periodicalCheckSn.trim().length()>0){
			hql+=" WHERE c.periodicalCheck.periodicalCheckSn="+periodicalCheckSn;
		}
		if(auditSn!=null&&auditSn.trim().length()>0){
			hql+=" WHERE c.systemAudit.auditSn="+auditSn;
		}
		List<CheckTable> list=new ArrayList<CheckTable>();
		list=getSessionFactory().getCurrentSession().createQuery(hql).list();
		JSONArray array=new JSONArray();
		for(CheckTable checkTable:list){
			JSONObject jo=new JSONObject();
			jo.put("id",checkTable.getId());
			jo.put("checkTableSn",checkTable.getCheckTableSn());
			//检查人员
			int num=0;
			String personIds="";
			String personNames="";
			JSONObject ch=new JSONObject();
			for(CheckTableChecker  checkTableChecker:checkTable.getCheckTableCheckers()){
				Person person=checkTableChecker.getChecker();
				if(person!= null){
					num++;
					personIds+=person.getPersonId()+",";
					personNames+=person.getPersonName()+",";
				}
				
			}
			if(num>0){
				personIds=personIds.substring(0,personIds.length()-1);
				personNames=personNames.substring(0,personNames.length()-1);
				ch.put("num", num);
				ch.put("personIds",personIds);
				ch.put("personNames",personNames);
			}else{
				ch.put("num", num);
			}
			jo.put("checkers", ch);
			array.add(jo);
		}
		return array;
	}

	//查看检查表详情
	@Override
	public JSONObject queryDetails(int id, String periodicalCheckSn, String specialCheckSn, String auditSn, int page,
			int rows) {
		List<StandardIndex> list=standardIndexDao.queryJoinCheckTable(id,page,rows);
		JSONArray array=new JSONArray();
		String sql="";
		if(periodicalCheckSn!=null&&periodicalCheckSn.length()>0){
			for(StandardIndex standardIndex:list){
				JSONObject jo=new JSONObject();
				jo.put("id", standardIndex.getId());
				jo.put("indexSn", standardIndex.getIndexSn());
				jo.put("indexName", standardIndex.getIndexName());
				jo.put("percentageScore", standardIndex.getPercentageScore());
				jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
				jo.put("anDeduction", standardIndex.getAnDeduction());
				jo.put("zeroTimes", standardIndex.getZeroTimes());
				//专业
				JSONObject sjo=new JSONObject();
	        	int i=0;
	        	String specialitySn="";
	        	for(Speciality speciality:standardIndex.getSpecialities()){
	        		i++;
	        		specialitySn+=speciality.getSpecialitySn()+",";
	        	}
	        	if(i>0){
	        		specialitySn=specialitySn.substring(0,specialitySn.length()-1);
	        		sjo.put("specialitySn", specialitySn);
	        	}
	        	sjo.put("num",i);
	        	jo.put("speciality",sjo);
				try{
					sql="select count(*) from inconformity_item where index_sn='"+standardIndex.getIndexSn()+"' and periodical_check_sn="+periodicalCheckSn;
					jo.put("unsafecondition", unsafeConditionDao.count(sql));
				}catch(Exception e){
					jo.put("unsafecondition", 0);
				}
				String method="";
				for(StandardIndexAuditMethod StandardIndexAuditMethod:standardIndex.getAuditMethods()){
					method+=StandardIndexAuditMethod.getAuditMethodContent();
				}
				jo.put("auditMethod", method);
				array.add(jo);
			}
		}else if(specialCheckSn!=null&&specialCheckSn.trim().length()>0){			
			for(StandardIndex standardIndex:list){
				JSONObject jo=new JSONObject();
				jo.put("id", standardIndex.getId());
				jo.put("indexSn", standardIndex.getIndexSn());
				jo.put("indexName", standardIndex.getIndexName());
				jo.put("percentageScore", standardIndex.getPercentageScore());
				jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
				jo.put("anDeduction", standardIndex.getAnDeduction());
				jo.put("zeroTimes", standardIndex.getZeroTimes());
				//专业
				JSONObject sjo=new JSONObject();
	        	int i=0;
	        	String specialitySn="";
	        	for(Speciality speciality:standardIndex.getSpecialities()){
	        		i++;
	        		specialitySn+=speciality.getSpecialitySn()+",";
	        	}
	        	if(i>0){
	        		specialitySn=specialitySn.substring(0,specialitySn.length()-1);
	        		sjo.put("specialitySn", specialitySn);
	        	}
	        	sjo.put("num",i);
	        	jo.put("speciality",sjo);
				try{
					sql="select count(*) from inconformity_item where deleted=false and index_sn='"+standardIndex.getIndexSn()+"' and special_check_sn="+specialCheckSn;
					jo.put("unsafecondition", unsafeConditionDao.count(sql));
				}catch(Exception e){
					jo.put("unsafecondition", 0);
				}
				String method="";
				for(StandardIndexAuditMethod StandardIndexAuditMethod:standardIndex.getAuditMethods()){
					method+=StandardIndexAuditMethod.getAuditMethodContent();
				}
				jo.put("auditMethod", method);
				array.add(jo);
			}
		}else{
			for(StandardIndex standardIndex:list){
				JSONObject jo=new JSONObject();
				jo.put("id", standardIndex.getId());
				jo.put("indexSn", standardIndex.getIndexSn());
				jo.put("indexName", standardIndex.getIndexName());
				jo.put("percentageScore", standardIndex.getPercentageScore());
				jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
				jo.put("anDeduction", standardIndex.getAnDeduction());
				jo.put("zeroTimes", standardIndex.getZeroTimes());
				//专业
				JSONObject sjo=new JSONObject();
	        	int i=0;
	        	String specialitySn="";
	        	for(Speciality speciality:standardIndex.getSpecialities()){
	        		i++;
	        		specialitySn+=speciality.getSpecialitySn()+",";
	        	}
	        	if(i>0){
	        		specialitySn=specialitySn.substring(0,specialitySn.length()-1);
	        		sjo.put("specialitySn", specialitySn);
	        	}
	        	sjo.put("num",i);
	        	jo.put("speciality",sjo);
				try{
					sql="select count(*) from inconformity_item where deleted=false and index_sn='"+standardIndex.getIndexSn()+"' and audit_sn="+auditSn;
					jo.put("unsafecondition", unsafeConditionDao.count(sql));
				}catch(Exception e){
					jo.put("unsafecondition", 0);
				}
				String method="";
				for(StandardIndexAuditMethod StandardIndexAuditMethod:standardIndex.getAuditMethods()){
					method+=StandardIndexAuditMethod.getAuditMethodContent();
				}
				jo.put("auditMethod", method);
				array.add(jo);
			}
		}
		JSONObject jo=new JSONObject();
		jo.put("total", get(CheckTable.class, id).getStandardIndexes().size());
		jo.put("rows", array);
		return jo;
	}


}
