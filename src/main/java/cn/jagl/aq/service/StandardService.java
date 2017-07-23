package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;

public interface StandardService {
	int addStandard(Standard standard);
	List<Standard> getAllStandards();
	void deleteStandard(int id);
	Standard getByStandardSn(String standardSn);
	//查询总数
	long count(String hql);
	//分页
	List<Standard> query(String hql,int page,int rows);
	//更新
	void update(Standard standard);
	//id
	Standard getById(int id);
	//隐藏多个记录
	void hidden(String ids);
	//获取指定的审核指南或指标
	List<Standard> queryByStandardType(StandardType standardType,String departmentTypeSn);
	/**
	 * 根据部门类型编号集合查询标准
	 * @param departmentTypeSns
	 * @return
	 */
	List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns);
}
