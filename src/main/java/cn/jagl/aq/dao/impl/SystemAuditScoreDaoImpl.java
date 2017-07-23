package cn.jagl.aq.dao.impl;


import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SystemAuditScoreDao;
import cn.jagl.aq.domain.SystemAuditScore;

/**
 * @author mahui
 * @method 
 * @date 2016年7月29日下午7:18:42
 */
@Repository("systemAuditScoreDao")
public class SystemAuditScoreDaoImpl extends BaseDaoHibernate5<SystemAuditScore> implements SystemAuditScoreDao {

	
	@Override
	public SystemAuditScore getByMany(String auditSn, String indexSn) {
		String hql="select s from SystemAuditScore s WHERE s.systemAudit.auditSn=:auditSn AND s.standardIndex.indexSn=:indexSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("auditSn", auditSn)
				.setParameter("indexSn", indexSn);
		try{
			return (SystemAuditScore) query.uniqueResult();
		}catch(NoResultException e){
			return null;
		}		
	}

	//根据systemAuditSn和indexSn查询指标下的所有符合度
	@SuppressWarnings("unchecked")
	@Override
	public List<SystemAuditScore> queryByMany(String auditSn, String indexSn) {
		String hql="select s from SystemAuditScore s WHERE s.systemAudit.auditSn=:auditSn AND s.standardIndex.indexSn like '"+indexSn+".%'";
		return (List<SystemAuditScore>) getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("auditSn", auditSn)
				.list();
	}

	//根据auditSn查询所有打分
	@SuppressWarnings("unchecked")
	@Override
	public List<SystemAuditScore> queryByAuditSn(String auditSn) {
		String hql="select s from SystemAuditScore s WHERE s.systemAudit.auditSn=:auditSn";
		return (List<SystemAuditScore>) getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("auditSn", auditSn)
				.list();
	}

}
