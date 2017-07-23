package cn.jagl.aq.dao.impl;



import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DocumentTemplateDao;
import cn.jagl.aq.domain.DocumentTemplate;
import cn.jagl.aq.domain.StandardIndex;
@SuppressWarnings("unchecked")
@Repository("documentTemplateDao")
public class DocumentTemplateDaoHibernate4 extends BaseDaoHibernate5<DocumentTemplate> implements DocumentTemplateDao{

	@Override
	public void deleteByIds(String ids) {
		// TODO Auto-generated method stub
		String hql="delete from DocumentTemplate WHERE id in("+ids+")";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}

	@Override
	public long count(String hql) {
		// TODO Auto-generated method stub
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public List<DocumentTemplate> query(int page, int rows) {
		// TODO Auto-generated method stub
		String hql="FROM DocumentTemplate d";
		List<DocumentTemplate> documentTemplateList=getSessionFactory().getCurrentSession().createQuery(hql)
				.setFirstResult((page - 1) * rows)
				.setMaxResults(rows) //
				.list();
		return documentTemplateList;
	}

	@Override
	public DocumentTemplate getByDocumentTemplateSn(String documentTemplateSn) {
		// TODO Auto-generated method stub
		String hql="select d FROM DocumentTemplate d WHERE documentTemplateSn=:documentTemplateSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql).setString("documentTemplateSn", documentTemplateSn);
			
		return (DocumentTemplate)query.uniqueResult();
	}
	//根据指标编号查询相关模板
	@Override
	public List<DocumentTemplate> queryJoinStandardIndex(String indexSn) {
		// TODO Auto-generated method stub
		String hql="select documentTemplates FROM StandardIndex s WHERE s.indexSn=:indexSn";
		//String hql="select d FROM DocumentTemplate d JOIN d.standardIndexs s WHERE s.indexSn=:indexSn";
		//String hql="FROM DocumentTemplate d WHERE d.standardIndexs.indexSn=:indexSn";
		List<DocumentTemplate> documentTemplateList = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("indexSn",indexSn)
				.list();
		return documentTemplateList;
	}


}
