package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeActStandard;

public interface UnsafeActStandardDao extends BaseDao<UnsafeActStandard> {

	//根据标准编号获取标准
	public UnsafeActStandard getByUnsafeActStandard(String unsafeActStandardSn);
	//根据参数模糊查询
	public List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page,int rows);
	//根据hql语句查询不安全行为标准
	public List<UnsafeActStandard> getByHql(String hql);
}
