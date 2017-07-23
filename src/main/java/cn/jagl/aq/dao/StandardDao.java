package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;

public interface StandardDao extends BaseDao<Standard> {
	//单个查询
	Standard getByStandardtSn(String standardSn);
	//单标查询
	List<Standard> query(String hql,int page,int rows);
	//查询个数
	long count(String hql);
	//隐藏多个记录
	void hidden(String ids);
	//获取指定条件的审核指南或指标
	List<Standard> queryByStandardType(StandardType standardtype,String departmentTypeSn);
	/**
	 * 根据部门类型编号集合查询标准
	 * @param departmentTypeSns
	 * @return
	 */
	List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns);
}
