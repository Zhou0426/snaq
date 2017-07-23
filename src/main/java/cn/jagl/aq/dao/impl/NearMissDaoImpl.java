package cn.jagl.aq.dao.impl;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.NearMissDao;
import cn.jagl.aq.domain.NearMiss;
import cn.jagl.aq.domain.Role;
@Repository("nearMissDao")
public class NearMissDaoImpl extends BaseDaoHibernate5<NearMiss>
implements NearMissDao{
	@Override
	public long findTotal(String nearMissState) {
		String hql="select count(*) from NearMiss n where n.nearMissState in "+nearMissState;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	}

	@Override
	public List<String> getAllNearMissSn() {
		String hql="select n.nearMissSn from NearMiss n";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}

	@Override
	public NearMiss getByNearMissSn(String nearMissSn) {
		String hql="select n from NearMiss n where n.nearMissSn="+nearMissSn;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (NearMiss)query.uniqueResult();
	}
	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
}
