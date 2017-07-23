package cn.jagl.aq.domain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class DeptFilterFactory{
	private String deptId;
	/**
	* injected parameter
	*/
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	@Factory
	public Filter getFilter() {
		PhraseQuery query=new PhraseQuery();
		query.add(new Term("department.deptId", deptId));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
	@Key      //method generating the FilterKey 
	 public FilterKey getKey() { 
	  StandardFilterKey key = new StandardFilterKey();  //use the default implementation 
	  key.addParameter(deptId);  //parameters are available 
	  return key; 
	 } 
}
