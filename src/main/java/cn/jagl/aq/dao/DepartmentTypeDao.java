package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DepartmentType;

public interface DepartmentTypeDao extends BaseDao<DepartmentType>
{
	public DepartmentType getByDepartmentTypeSn(String departmentTypeSn);
	public List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn);
	public List<DepartmentType> query(String hql);
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn);
	public List<DepartmentType> getAllImplDepartmentTypes();
	public String getDownDepartmentTypeByParent(String departmentTypeSn);
	/**
	 * 获取部门下面的贯标单位类型（不包含本部门）
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypesExceptSelf(String departmentSn);
	/**
	 * 获取指定部门编号范围内的贯标单位类型列表 - 左查询
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypesByLeft(String departmentSn);
	/**
	 * 根据部门编号查询该部门所属部门类型的所有子部门类型
	 * @param departmentSn
	 * @return
	 */
	DepartmentType getDepartmentSn(String departmentSn);
	/**
	 * 根据部门获取该部门下所有的贯标单位类型，包含其本身的部门类型
	 * @param departmentSn
	 * @return
	 */
	List<String> getAllImplDepartmentTypesIncloudSelf(String departmentSn);
}
