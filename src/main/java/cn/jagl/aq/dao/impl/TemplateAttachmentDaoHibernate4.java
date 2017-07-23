package cn.jagl.aq.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.TemplateAttachmentDao;
import cn.jagl.aq.domain.TemplateAttachment;
@SuppressWarnings("unchecked")
@Repository("templateAttachmentDao")
public class TemplateAttachmentDaoHibernate4 extends BaseDaoHibernate5<TemplateAttachment> implements TemplateAttachmentDao {

	@Override
	public List<TemplateAttachment> queryJoinDocumentTemplate(String documentTemplateSn) {
		// TODO Auto-generated method stub
		String hql = "select t FROM TemplateAttachment t LEFT JOIN  t.documentTemplate d WHERE d.documentTemplateSn=:documentTemplateSn";
		List<TemplateAttachment> templateAttachmentList = getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("documentTemplateSn", documentTemplateSn)
				.list();
		return templateAttachmentList;
	}

	@Override
	public void deleteByIds(String ids) {
		// TODO Auto-generated method stub
		String hql="delete from TemplateAttachment WHERE id in("+ids+")";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}


}
