package cn.jagl.aq.dao.impl;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.opensymphony.xwork2.ActionContext;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.StandardIndexDao;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.util.RegExUtil;
@SuppressWarnings("unchecked")
@Repository("standardIndexDao")
public class StandardIndexDaoHibernate4 extends BaseDaoHibernate5<StandardIndex> implements StandardIndexDao {


	@Override
	public List<StandardIndex> queryJoinDocumentTemplate(String documentTemplateSn) {
		// TODO Auto-generated method stub
		String hql = "select standardIndexes FROM DocumentTemplate d WHERE d.documentTemplateSn=:documentTemplateSn";
		List<StandardIndex> standardIndexList = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("documentTemplateSn", documentTemplateSn)
				.list();
		return standardIndexList;
	}
	@Override
	public StandardIndex getByindexSn(String indexSn){
		// TODO Auto-generated method stub
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("from StandardIndex p where indexSn=:indexSn").setString("indexSn", indexSn);
				return (StandardIndex)query.uniqueResult();

	}
	@Override
	public List<StandardIndex> getAll() {
		// TODO Auto-generated method stub	
		String hql="from StandardIndex s order by indexSn asc";
		List<StandardIndex> standardIndexList=getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.list();
		return standardIndexList;
	}
	//通过ID获取（BaseDao的方法无法执行） 
	@Override
	public StandardIndex getById(int id) {
		// TODO Auto-generated method stub
		String hql="from StandardIndex s where id=:id";
		Query query =getSessionFactory().getCurrentSession()
				.createQuery(hql).setInteger("id", id);
		return (StandardIndex)query.uniqueResult();
	}

	//按条件查询加载树
	@Override
	public List<StandardIndex> getPart(String hql) {
		List<StandardIndex> standardIndexList=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.list();
		return standardIndexList;
	}
	
	@Override
	public List<StandardIndex> getByQ(String q,String standardSn,String standardType) {
		List<StandardIndex> data=new ArrayList<StandardIndex>();
		if(standardSn!=null){
			if(RegExUtil.isSn(q)){
				String hql="select s from StandardIndex s WHERE s.deleted=false AND s.indexSn like '%"+q+"%' AND s.standard.standardSn='"+standardSn+"'";
				data=getSessionFactory().getCurrentSession()
						.createQuery(hql)//设置每页起始的记录编号
						.setMaxResults(20)//设置需要查询的最大结果集
						.list();
			}else{
				FullTextSession fts = Search.getFullTextSession(getSessionFactory().getCurrentSession());
				/*try {
				fts.createIndexer().startAndWait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
				fts.getSearchFactory();
			    QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(StandardIndex.class).get();
			    org.apache.lucene.search.Query luceneQuery =  qb.keyword().onFields("indexName").matching(q).createQuery();
			    FullTextQuery query = fts.createFullTextQuery(luceneQuery, StandardIndex.class);
			    if(standardSn!=null){
			    	query.enableFullTextFilter("standardFilter").setParameter("standardSn", standardSn);
			    }
			     data= query.setMaxResults(20).list();
			}
			
		}else{
			return data;
		}		
		return data;
	}
	@Override
	public List<StandardIndex> queryJoinCheckTable(int id,int page,int rows) {
		String hql="select t.standardIndexes from CheckTable t WHERE t.id=:id";
		List<StandardIndex> list=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setInteger("id",id)
				.setFirstResult((page - 1) * rows)//设置每页起始的记录编号
				.setMaxResults(rows)
				.list();
		return list;
	}
	
	//删除父元素及子元素
	@Override
	public void deleteById(String indexSn) {
		String hql="update StandardIndex s SET s.deleted=true,s.parent=null WHERE s.indexSn like '"+indexSn+"' OR s.indexSn like '"+indexSn+".%'";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}
	@Override
	public long count(String hql) {		
		return (long) getSessionFactory().getCurrentSession()
				.createQuery(hql).uniqueResult();
	}
	private List<StandardIndex> getAllChildren(StandardIndex index) {
		List<StandardIndex> list=new ArrayList<StandardIndex>();
		String hql="select s FROM StandardIndex s WHERE s.deleted=false and s.parent.indexSn='"+index.getIndexSn()+"' order by s.showSequence";
		for (StandardIndex child:getPart(hql)){
			list.add(child);
			if(index.getChildren().size()>0){
				list.addAll(getAllChildren(child));
			}			
		}
		return list;
	}
	//按顺序输出list
	@Override
	public List<StandardIndex> exportByStandardSn(String standardSn) {
		String countHql="select count(s) FROM StandardIndex s WHERE s.deleted=false AND s.standard.standardSn='"+standardSn+"'";
		long count=count(countHql);
		List<StandardIndex> list=new ArrayList<StandardIndex>();
		String hql="select s FROM StandardIndex s WHERE s.deleted=false AND s.standard.standardSn='"+standardSn+"' and s.parent is null order by s.showSequence";
		for (StandardIndex index:getPart(hql)){
			list.add(index);
			if(index.getChildren().size()>0){
				list.addAll(getAllChildren(index));
			}
			ActionContext.getContext().getSession().put("progressValue", (int)(list.size()*80/count));
		}
		
		return list;
	}
	

}
