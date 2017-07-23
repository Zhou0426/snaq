package cn.jagl.aq.dao.impl;



import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.InteriorWorkDao;

import cn.jagl.aq.domain.InteriorWork;

@Repository("interiorWorkDao")
public class InteriorWorkDaoHibernate4 extends BaseDaoHibernate5<InteriorWork> implements InteriorWorkDao {

	@SuppressWarnings({ "rawtypes" })
	public InteriorWork getByInteriorWorkSn(String InteriorWorkSn){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from InteriorWork p where p.interiorWorkSn=:InteriorWorkSn").setString("InteriorWorkSn", InteriorWorkSn);
				return (InteriorWork)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InteriorWork> getCountByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<InteriorWork>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
	}

	@Override
	public long getByHql(String hql) {
		// TODO Auto-generated method stub
		return (long) getSessionFactory().getCurrentSession()
				.createQuery(hql).uniqueResult();
	}

	@Override
	public long getBySql(String sql) {
		// TODO Auto-generated method stub
		return ((BigInteger) getSessionFactory().getCurrentSession()
				.createSQLQuery(sql).uniqueResult()).longValue();
	}
	
}
