package cn.jagl.aq.domain;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class DepartmentTypeFilterFactory{
	private String departmentTypeSn;
	/**
	* injected parameter
	*/
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	@Factory
	public Filter getFilter() {
		TermQuery query=new TermQuery(new Term("departmentType.departmentTypeSn",departmentTypeSn));
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
}
