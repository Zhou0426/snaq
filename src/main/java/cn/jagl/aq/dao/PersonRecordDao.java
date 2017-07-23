package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PersonRecord;

public interface PersonRecordDao extends BaseDao<PersonRecord> {

	/**
	 * 根据人员Id和部门编号查找履历表中是否存在
	 */
	public PersonRecord getByPersonIdDepartmentSn(String personId,String departmentSn);
	/**
	 * 根据人员Id查找履历表中所有实体集合
	 */
	public List<PersonRecord> getListByPersonId(String personId);
	/**
	 * 根据人员Id查找履历表中距离今天最近的一条数据
	 */
	public PersonRecord getByPersonId(String personId);
	/**
	 * 多条删除
	 */
	public void Manydelete(String ids);
	/**
	 * 根据人员Id查找履历表中本月的记录
	 */
	public List<PersonRecord> getMonthByPersonId(String personId);
}
