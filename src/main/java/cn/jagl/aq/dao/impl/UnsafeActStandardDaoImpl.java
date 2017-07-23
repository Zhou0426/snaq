package cn.jagl.aq.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeActStandardDao;
import cn.jagl.aq.domain.UnsafeActStandard;
import cn.jagl.util.RegExUtil;

@Repository("unsafeActStandardDao")
public class UnsafeActStandardDaoImpl extends BaseDaoHibernate5<UnsafeActStandard> implements UnsafeActStandardDao {

	@Override
	public UnsafeActStandard getByUnsafeActStandard(String unsafeActStandardSn) {
		// TODO Auto-generated method stub
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from UnsafeActStandard p where standardSn=:standardSn").setString("standardSn", unsafeActStandardSn);
		return (UnsafeActStandard)query.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page,int rows){
		List<UnsafeActStandard> data=new ArrayList<UnsafeActStandard>();
		if(departmentTypeSn!=null && !"".equals(departmentTypeSn)){
			if(RegExUtil.isSn(q)){
				String hql="select u from UnsafeActStandard u where u.standardSn like '%"+q+"%' AND u.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
				data=getSessionFactory().getCurrentSession()
						.createQuery(hql)
						.setFirstResult((page - 1) * rows)//设置每页起始的记录编号
						.setMaxResults(rows)//设置需要查询的最大结果集
						.list();
			}else{
				FullTextSession fts = Search.getFullTextSession(getSessionFactory().getCurrentSession());
				fts.getSearchFactory();
				QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(UnsafeActStandard.class).get();
				org.apache.lucene.search.Query luceneQuery =  qb.keyword().onFields("standardDescription").matching(q).createQuery();
				FullTextQuery query = fts.createFullTextQuery(luceneQuery, UnsafeActStandard.class);
				query.enableFullTextFilter("departmentTypeFilter").setParameter("departmentTypeSn", departmentTypeSn);
				data=(List<UnsafeActStandard>)query.setFirstResult((page-1)*rows).setMaxResults(rows).list();
			}
		}
	    return data;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UnsafeActStandard> getByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<UnsafeActStandard>)getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
	}

}
