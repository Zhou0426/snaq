package cn.jagl.aq.domain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class StandardIndexDeletedFilterFactory{
	private String deleted;
	/**
	* injected parameter
	*/
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	@Factory
	public Filter getFilter() {
		TermQuery query=new TermQuery(new Term("deleted",deleted));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
	
}
