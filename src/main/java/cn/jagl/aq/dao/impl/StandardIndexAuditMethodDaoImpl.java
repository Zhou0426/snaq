package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.StandardIndexAuditMethodDao;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
@Repository("standardIndexAuditMethodDao")
public class StandardIndexAuditMethodDaoImpl extends BaseDaoHibernate5<StandardIndexAuditMethod> implements StandardIndexAuditMethodDao {

	//根据指标Id查询
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardIndexAuditMethod> queryJoinStandardIndex(int id) {
		String hql="select s from StandardIndexAuditMethod s WHERE s.standardIndex.id=:id";
		return (List<StandardIndexAuditMethod>) getSessionFactory().getCurrentSession().createQuery(hql)
			    	.setParameter("id", id).list();
	}

	@Override
	public StandardIndexAuditMethod getBySn(String auditMethodSn) {
		String hql="select s from StandardIndexAuditMethod s WHERE s.auditMethodSn=:auditMethodSn ";
		return (StandardIndexAuditMethod) getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("auditMethodSn", auditMethodSn).uniqueResult();
	}

	@Override
	public StandardIndexAuditMethod getById(int id) {
		String hql="select s from StandardIndexAuditMethod s WHERE s.id=:id";
		return (StandardIndexAuditMethod) getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("id", id).uniqueResult();
	}
	
	
}
