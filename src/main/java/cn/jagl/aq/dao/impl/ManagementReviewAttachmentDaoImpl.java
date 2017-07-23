package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.ManagementReviewAttachmentDao;
import cn.jagl.aq.domain.ManagementReview;
import cn.jagl.aq.domain.ManagementReviewAttachment;

@Repository("managementReviewAttachmentDao")
public class ManagementReviewAttachmentDaoImpl extends BaseDaoHibernate5<ManagementReviewAttachment> implements ManagementReviewAttachmentDao{

	@Override
	public ManagementReviewAttachment getByPath(String path) {
		// TODO Auto-generated method stub
		path=path.substring(path.lastIndexOf("/"), path.length());
		String hql="select m from ManagementReviewAttachment m where m.logicalFileName like '%"+path+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (ManagementReviewAttachment)query.uniqueResult();
	}

	@Override
	public ManagementReviewAttachment getById(int attid) {
		String hql="select m from ManagementReviewAttachment m where m.id ="+attid+"";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (ManagementReviewAttachment)query.uniqueResult();
	}

}
