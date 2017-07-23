package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.PersonRecord;

public interface PersonRecordService {

	/**
	 * 根据人员Id和部门编号查找履历表中是否存在-距离今天最近的一条数据
	 */
	PersonRecord getByPersonIdDepartmentSn(String personId,String departmentSn);
	/**
	 * 根据人员Id查找履历表中距离今天最近的一条数据
	 */
	PersonRecord getByPersonId(String personId);
	/**
	 * 根据人员Id查找履历表中本月的记录
	 */
	List<PersonRecord> getMonthByPersonId(String personId);
	/**
	 * 根据人员Id查找履历表中所有实体集合
	 */
	List<PersonRecord> getListByPersonId(String personId);
	/**
	 * 添加实体
	 */
	void add(PersonRecord personRecord);
	/**
	 * 修改实体
	 */
	void update(PersonRecord personRecord);
	/**
	 * 多条删除
	 */
	void Manydelete(String ids);
	/**
	 * 单条删除
	 */
	void delete(int id);
}
