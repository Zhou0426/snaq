package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.DepartmentType;

public interface DepartmentTypeService
{
	
	int addDepartmentType(DepartmentType departmentType);

	List<DepartmentType> getAllDepartmentTypes();
	
	void deleteDepartmentType(int id);
	
	DepartmentType getByDepartmentTypeSn(String departmentTypeSn);
	List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn);

	List<DepartmentType> findByPage(String hql,int pageNo, int pageSize);
	List<DepartmentType> query(String hql);

	List<DepartmentType> getByParentDepartmentTypeSn(String id);
	/**
	 * 获取指定部门编号范围内的贯标单位类型列表
	 * @param departmentSn
	 * @return
	 */
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn);

	List<DepartmentType> getAllImplDepartmentTypes();
	String getDownDepartmentTypeByParent(String departmentTypeSn);
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
