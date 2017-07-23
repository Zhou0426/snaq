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
import cn.jagl.aq.dao.HazardDao;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.util.RegExUtil;
@Repository("hazardDao")
public class HazardDaoImpl extends BaseDaoHibernate5<Hazard>
	implements HazardDao 
{

	public Hazard getByHazardSn(String hazardSn){
		
		Query query=getSessionFactory().getCurrentSession()
				.createQuery("select p from Hazard p where hazardSn=:hazardSn").setString("hazardSn", hazardSn);
		return (Hazard)query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hazard> getByHazard(String q,String departmentTypeSn, int page, int rows) {
		List<Hazard> data=new ArrayList<Hazard>();
		if(q!=null && !"".equals(q)){
			if(RegExUtil.isSn(q)){
				String hql="select h from Hazard h where h.departmentType.departmentTypeSn= ? and h.hazardSn like ?";
				data=(List<Hazard>)getSessionFactory().getCurrentSession()
						.createQuery(hql).setString(0, departmentTypeSn).setString(1, "%"+q+"%")
						.setFirstResult((page - 1) * rows)
						.setMaxResults(rows)
						.list();
			}else{
				FullTextSession fts = Search.getFullTextSession(getSessionFactory().getCurrentSession());
				fts.getSearchFactory();
				QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(Hazard.class).get();
				org.apache.lucene.search.Query luceneQuery =  qb.keyword().onFields("hazardDescription","resultDescription").matching(q).createQuery();
				FullTextQuery query = fts.createFullTextQuery(luceneQuery, Hazard.class);
				query.enableFullTextFilter("hazardDepartmentTypeFilter").setParameter("departmentTypeSn", departmentTypeSn);
				data=(List<Hazard>)query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
			}
		}
		return data;
	}
	@Override
	public Long getCountByHazard(String departmentTypeSn, String hazardSn) {
		// TODO Auto-generated method stub
		long data = 0;
		if(hazardSn!=null && !"".equals(hazardSn)){
			if(RegExUtil.isSn(hazardSn)){
				String hql="select count(*) from Hazard h where h.departmentType.departmentTypeSn= ? and h.hazardSn like ?";
				data=(long) getSessionFactory().getCurrentSession()
						.createQuery(hql).setString(0, departmentTypeSn).setString(1, "%"+hazardSn+"%")
						.uniqueResult();
			}else{
				FullTextSession fts = Search.getFullTextSession(getSessionFactory().getCurrentSession());
				fts.getSearchFactory();
				QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(Hazard.class).get();
				org.apache.lucene.search.Query luceneQuery =  qb.keyword().onFields("hazardDescription","resultDescription").matching(hazardSn).createQuery();
				FullTextQuery query = fts.createFullTextQuery(luceneQuery, Hazard.class);
				query.enableFullTextFilter("hazardDepartmentTypeFilter").setParameter("departmentTypeSn", departmentTypeSn);
				data=(long) query.list().size();
			}
		}
		return data;
	}
	
	
//	public List<Hazard> getByHazard(String q) {
//		String hql="select h from Hazard h ";
//		Query query=getSessionFactory().getCurrentSession()
//				.createQuery(hql)
//				.setFirstResult(0)
//				.setMaxResults(20);
//		if(q!=null){
//			hql="select h from Hazard h where h.hazardDescription like ? or h.hazardSn like ? or h.resultDescription like ?";
//			query=getSessionFactory().getCurrentSession()
//					.createQuery(hql)
//					.setFirstResult(0)
//					.setMaxResults(20);;
//			query.setString(0, "%"+q+"%");
//			query.setString(1, "%"+q+"%");
//			query.setString(2, "%"+q+"%");
//			
//		}
//		return (List<Hazard>)query.list();
//	}

	@Override
	public Long getCountByHazardSn(String departmentTypeSn, String hazardSn) {
		// TODO Auto-generated method stub
		String hql;
		if(hazardSn!=null&hazardSn.trim().length()>0){
			hql="select count(*) from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"' and (h.hazardSn like '%"+hazardSn+"%' or h.hazardDescription like '%"+hazardSn+"%')";
		}else{
			hql="select count(*) from Hazard h where h.departmentType.departmentTypeSn ='"+departmentTypeSn+"'";
		}
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hazard> getHazardsByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<Hazard>)getSessionFactory().getCurrentSession().createQuery(hql).list();
	}
	

}
