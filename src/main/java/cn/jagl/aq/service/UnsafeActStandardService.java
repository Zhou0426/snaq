package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeActStandard;

public interface UnsafeActStandardService {

	//根据标准编号获取标准
	UnsafeActStandard getByUnsafeActStandardSn(String unsafeActStandardSn);
	//分页获取标准
	List<UnsafeActStandard> findByPage(String hql,int page,int rows);
	//根据参数模糊查询
	List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page,int rows);
	//根据hql语句查询不安全行为标准--不用
	List<UnsafeActStandard> getByHql(String hql);
}
