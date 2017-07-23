package cn.jagl.aq.domain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class StandardIndexFilterFactory{
	private String standardSn;
	/**
	* injected parameter
	*/
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	@Factory
	public Filter getFilter() {
		TermQuery query=new TermQuery(new Term("standard.standardSn",standardSn));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
}
