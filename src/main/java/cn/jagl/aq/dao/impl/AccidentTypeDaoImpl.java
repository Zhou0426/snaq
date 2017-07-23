package cn.jagl.aq.dao.impl;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.AccidentTypeDao;
import cn.jagl.aq.domain.AccidentType;
@Repository("accidentTypeDao")
public class AccidentTypeDaoImpl extends BaseDaoHibernate5<AccidentType>
	implements AccidentTypeDao 
	{
	
	public AccidentType getByAccidentTypeSn(String accidentTypeSn) {
		
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from AccidentType p where accidentTypeSn=:accidentTypeSn").setString("accidentTypeSn", accidentTypeSn);
		return (AccidentType)query.uniqueResult();
	}

}
