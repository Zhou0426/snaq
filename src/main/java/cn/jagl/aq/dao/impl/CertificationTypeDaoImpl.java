package cn.jagl.aq.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.CertificationTypeDao;
import cn.jagl.aq.domain.CertificationType;
import net.sf.json.JSONObject;

@Repository("certificationTypeDao")
public class CertificationTypeDaoImpl extends BaseDaoHibernate5<CertificationType> implements CertificationTypeDao {
	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public CertificationType getByCertificationTypeSn(String certificationTypeSn) {
		String hql="select c from CertificationType c where c.certificationTypeSn="+certificationTypeSn;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (CertificationType)query.uniqueResult();
	}

	@Override
	public CertificationType getById(int id) {
		String hql="select c from CertificationType c where c.id="+id;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (CertificationType)query.uniqueResult();
	}

	@Override
	public CertificationType getByCertificationTypeName(String stringValue) {
		String hql = "select c from CertificationType c where c.certificationTypeName='"+stringValue+"'";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		return (CertificationType)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject queryStatistics(String departmentSn, int page, int rows) {
		
		JSONObject jsonObject = new JSONObject();
		
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT ct.certificationTypeSn, ct.certificationTypeName,"
				+ " COUNT(DISTINCT c.holder) AS countPerson"
				+ " FROM CertificationType ct"
				+ " LEFT JOIN ct.certificationes c"
				+ " WHERE c.holder.department.departmentSn LIKE :departmentSn"
				+ " GROUP BY ct.certificationTypeSn, ct.certificationTypeName");
		
		List<Object> list = getSessionFactory().getCurrentSession()
								.createQuery(hql.toString())
								.setString("departmentSn", departmentSn + "%")
								.setFirstResult((page - 1) * rows)
								.setMaxResults(rows)
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
								.list();
		long total = getSessionFactory().getCurrentSession()
						.createQuery(hql.toString())
						.setString("departmentSn", departmentSn + "%")
						.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
						.list().size();
		
		net.sf.json.JSONArray array=new net.sf.json.JSONArray();
		
//		for(CertificationType certificationType : certificationTypes){
		for(int i = 0; i < list.size(); i++){
			
			 Map<Object, Object> map = (Map<Object, Object>)list.get(i);
			
			 net.sf.json.JSONObject jo = new net.sf.json.JSONObject();
			 
			 jo.put("certificationTypeSn", map.get("0"));
			 jo.put("certificationTypeName", map.get("1"));
			 jo.put("count", map.get("countPerson"));
			 
			 array.add(jo);
			 
		}
		jsonObject.put("rows", array);
		jsonObject.put("total", total);
		return jsonObject;
	}

}
