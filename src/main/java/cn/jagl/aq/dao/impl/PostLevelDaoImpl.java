package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.AccidentDao;
import cn.jagl.aq.dao.PostLevelDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.PostLevel;

@Repository("postLevelDao")
public class PostLevelDaoImpl extends BaseDaoHibernate5<PostLevel> implements PostLevelDao {

	@Override
	public PostLevel getBySn(String levelSn) {
		String hql="select a from PostLevel a where a.postLevelSn='"+levelSn+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (PostLevel)query.uniqueResult();
	}


	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

}
