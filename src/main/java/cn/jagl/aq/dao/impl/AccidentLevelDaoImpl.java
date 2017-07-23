package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.AccidentLevelDao;
import cn.jagl.aq.domain.AccidentLevel;
import cn.jagl.aq.domain.AccidentType;

@Repository("accidentLevelDao")
public class AccidentLevelDaoImpl extends BaseDaoHibernate5<AccidentLevel> implements AccidentLevelDao {

	@Override
	public AccidentLevel getByAccidentLevelSn(String accidentLevelSn) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from AccidentLevel p where accidentLevelSn=:accidentLevelSn").setString("accidentLevelSn", accidentLevelSn);
		return (AccidentLevel)query.uniqueResult();
	}

}
