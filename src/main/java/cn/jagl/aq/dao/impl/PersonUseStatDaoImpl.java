package cn.jagl.aq.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.PersonUseStatDao;
import cn.jagl.aq.domain.PersonUseStat;
@Repository("personUseStatDao")
public class PersonUseStatDaoImpl extends BaseDaoHibernate5<PersonUseStat> implements PersonUseStatDao {

	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
}
