package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.AccidentDao;
import cn.jagl.aq.dao.AccidentTypeDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.NearMiss;

@SuppressWarnings("rawtypes")
@Repository("accidentDao")
public class AccidentDaoImpl extends BaseDaoHibernate5<Accident> implements AccidentDao {
	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public Accident getByAccidentSn(String accidentSn) {
		String hql="select a from Accident a where a.accidentSn="+accidentSn;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Accident)query.uniqueResult();
	}
}
