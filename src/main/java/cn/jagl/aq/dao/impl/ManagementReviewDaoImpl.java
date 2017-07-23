package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.ManagementReviewDao;
import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.ManagementReview;
@Repository("managementReviewDao")
public class ManagementReviewDaoImpl extends BaseDaoHibernate5<ManagementReview> implements ManagementReviewDao {

	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public ManagementReview getById(int id) {
		String hql="select m from ManagementReview m where m.id="+id;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (ManagementReview)query.uniqueResult();
	}
	@Override
	public ManagementReview getBySn(String sn) {
		String hql="select m from ManagementReview m where m.reviewSn='"+sn+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (ManagementReview)query.uniqueResult();
	}
}
