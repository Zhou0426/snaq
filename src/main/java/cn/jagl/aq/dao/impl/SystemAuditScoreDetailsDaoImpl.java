package cn.jagl.aq.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SystemAuditScoreDetailsDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
/**
 * @author mahui
 * @method 
 * @date 2016年9月13日下午5:08:38
 */
@Repository("systemAuditScoreDetailsDao")
public class SystemAuditScoreDetailsDaoImpl extends BaseDaoHibernate5<SystemAuditScoreDetails> implements SystemAuditScoreDetailsDao{

	@Override
	public JSONObject query(String auditSn, String indexSn, String departmentSn, int page, int rows) {
		String hql="select a from SystemAuditScoreDetails a where a.systemAudit.auditSn=:auditSn and a.standardIndex.indexSn=:indexSn and a.department.departmentSn like:departmentSn";
		JSONArray array=new JSONArray();
		@SuppressWarnings("unchecked")
		List<SystemAuditScoreDetails> list=getSessionFactory().getCurrentSession()
											.createQuery(hql)
											.setString("auditSn", auditSn)
											.setString("indexSn", indexSn)
											.setString("departmentSn", departmentSn+"%")
											.setFirstResult(rows*(page-1))
											.setMaxResults(rows)
											.list();
		for(SystemAuditScoreDetails systemAuditScoreDetails:list){
			JSONObject jo=new JSONObject();
			jo.put("id", systemAuditScoreDetails.getId());
			jo.put("conformDegree", systemAuditScoreDetails.getConformDegree());
			jo.put("editor", systemAuditScoreDetails.getEditor().getPersonName());
			jo.put("inputDateTime", systemAuditScoreDetails.getInputDateTime().toString());
			jo.put("standardIndex", systemAuditScoreDetails.getStandardIndex().getIndexSn());
			jo.put("department", systemAuditScoreDetails.getDepartment().getDepartmentName());
			jo.put("departmentSn", systemAuditScoreDetails.getDepartment().getDepartmentSn());
			array.add(jo);
		}
		hql=hql.replaceFirst("a", "count(a)");
		JSONObject jo=new JSONObject();
		jo.put("total", getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("auditSn", auditSn)
				.setString("indexSn", indexSn)
				.setString("departmentSn", departmentSn+"%")
				.uniqueResult());
		jo.put("rows", array);
		return jo;
	}

	//查询部门
	@Override
	public JSONArray queryDepartment(String departmentSn) {
		String hql="select d from Department d where d.deleted=false and d.departmentSn like:departmentSn";
		JSONArray array=new JSONArray();
		@SuppressWarnings("unchecked")
		List<Department> list=getSessionFactory().getCurrentSession()
								.createQuery(hql)
								.setString("departmentSn", departmentSn+"%")
								.list();
		for(Department department:list){
			JSONObject jo=new JSONObject();
			jo.put("departmentSn", department.getDepartmentSn());
			jo.put("departmentName", department.getDepartmentName());
			array.add(jo);
		}
		return array;
	}

	//多条件查询
	@Override
	public boolean isExists(String auditSn, String indexSn, String departmentName) {
		String hql="select count(a) from SystemAuditScoreDetails a where a.systemAudit.auditSn=:auditSn and a.standardIndex.indexSn=:indexSn and a.department.departmentName like:departmentName";
		boolean b=true;
		long s=(long) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("auditSn", auditSn)
				.setString("indexSn", indexSn)
				.setString("departmentName", departmentName)
				.uniqueResult();
		if(s>0){
			b=false;
		}
		return b;
	}

	//查询最小符合度
	@Override
	public int minConformDegree(String auditSn, String indexSn, String departmentSn) {
		String hql="select min(s.conformDegree) from SystemAuditScoreDetails s where s.systemAudit.auditSn=:auditSn and s.standardIndex.indexSn=:indexSn and s.department.departmentSn like:departmentSn";
		Query query=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("auditSn", auditSn)
				.setString("indexSn", indexSn)
				.setString("departmentSn", departmentSn+"%");
		if(query.uniqueResult()==null){
			return -1;
		}
		return (int) query.uniqueResult();
	}

	//更新时多条件查询
	@Override
	public boolean isExistsUpdate(String auditSn, String indexSn, String departmentName, int id) {
		String hql="select count(a) from SystemAuditScoreDetails a where a.id<>:id and a.systemAudit.auditSn=:auditSn and a.standardIndex.indexSn=:indexSn and a.department.departmentName like:departmentName";
		boolean b=true;
		long s=(long) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setInteger("id", id)
				.setString("auditSn", auditSn)
				.setString("indexSn", indexSn)
				.setString("departmentName", departmentName)
				.uniqueResult();
		if(s>0){
			b=false;
		}
		return b;
	}

	@Override
	public SystemAuditScoreDetails getById(int id) {
		String hql="select s from SystemAuditScoreDetails s where id=:id";
		return (SystemAuditScoreDetails) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setInteger("id", id)
				.uniqueResult();
	}

}
