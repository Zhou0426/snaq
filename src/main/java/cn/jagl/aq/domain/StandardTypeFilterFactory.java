package cn.jagl.aq.domain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class StandardTypeFilterFactory{
	private String standardType;
	/**
	* injected parameter
	*/
	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}
	@Factory
	public Filter getFilter() {
		TermQuery query=new TermQuery(new Term("standard.standardType",standardType));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
}
