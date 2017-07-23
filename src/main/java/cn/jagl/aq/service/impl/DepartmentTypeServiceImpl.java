package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.DepartmentTypeDao;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.service.DepartmentTypeService;
@Service("departmentTypeService")
public class DepartmentTypeServiceImpl implements DepartmentTypeService
{
	@Resource(name="departmentTypeDao")
	private DepartmentTypeDao departmentTypeDao;

	@Override
	public int addDepartmentType(DepartmentType departmentType)
	{
		return (Integer) departmentTypeDao.save(departmentType);
	}

	@Override
	public List<DepartmentType> getAllDepartmentTypes()
	{
		return departmentTypeDao.findAll(DepartmentType.class);
	}

	@Override
	public void deleteDepartmentType(int id)
	{
		departmentTypeDao.delete(DepartmentType.class, id);
	}

	@Override
	public DepartmentType getByDepartmentTypeSn(String departmentTypeSn) {
		return departmentTypeDao.getByDepartmentTypeSn(departmentTypeSn);
	}

	@Override
	public List<DepartmentType> getDepartmentsByParentDepartmentTypeSn(String parentDepartmentTypeSn) {
		return departmentTypeDao.getDepartmentsByParentDepartmentTypeSn(parentDepartmentTypeSn);
	}

	@Override
	public List<DepartmentType> findByPage(String hql, int pageNo, int pageSize) {
		return departmentTypeDao.findByPage(hql, pageNo, pageSize);
	}
	//����д��66666666
	@Override
	public List<DepartmentType> query(String hql) {
		return departmentTypeDao.query(hql);
	}

	@Override
	public List<DepartmentType> getByParentDepartmentTypeSn(String id) {
		return departmentTypeDao.getDepartmentsByParentDepartmentTypeSn(id);
	}

	@Override
	public List<DepartmentType> getImplDepartmentTypes(String departmentSn) {
		return departmentTypeDao.getImplDepartmentTypes(departmentSn);
	}

	@Override
	public List<DepartmentType> getAllImplDepartmentTypes() {
		return departmentTypeDao.getAllImplDepartmentTypes();
	}

	@Override
	public String getDownDepartmentTypeByParent(String departmentTypeSn) {
		return departmentTypeDao.getDownDepartmentTypeByParent(departmentTypeSn);
	}

	@Override
	public List<DepartmentType> getImplDepartmentTypesExceptSelf(String departmentSn) {
		return departmentTypeDao.getImplDepartmentTypesExceptSelf(departmentSn);
	}

	@Override
	public List<DepartmentType> getImplDepartmentTypesByLeft(String departmentSn) {
		return departmentTypeDao.getImplDepartmentTypesByLeft(departmentSn);
	}

	@Override
	public DepartmentType getDepartmentSn(String departmentSn) {
		return departmentTypeDao.getDepartmentSn(departmentSn);
	}

	@Override
	public List<String> getAllImplDepartmentTypesIncloudSelf(String departmentSn) {
		return departmentTypeDao.getAllImplDepartmentTypesIncloudSelf(departmentSn);
	}
}
