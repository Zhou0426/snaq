package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeConditionReviewDao;
import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:18:51
 */
@Repository("unsafeConditionReviewDao")
@SuppressWarnings("unchecked")
public class UnsafeConditionReviewDaoImpl extends BaseDaoHibernate5<UnsafeConditionReview> implements UnsafeConditionReviewDao {
	@Override
	public List<UnsafeConditionReview> query(int id) {
		String hql="select i from UnsafeConditionReview i WHERE i.inconformityItem.id=:id";
		List<UnsafeConditionReview> list=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setParameter("id",id)
				.list();
		return list;
	}


}
