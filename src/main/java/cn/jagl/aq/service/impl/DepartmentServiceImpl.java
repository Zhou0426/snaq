package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import cn.jagl.aq.service.DepartmentService;
import net.sf.json.JSONObject;
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService
{
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Override
	public int addDepartment(Department department)
	{
		return (Integer) departmentDao.save(department);
	}

	@Override
	public List<Department> getAllDepartments()
	{
		return departmentDao.findAll(Department.class);
	}

	@Override
	public void deleteDepartment(int id)
	{
		departmentDao.delete(Department.class, id);
	}
	
	@Override
	public void updateDepartment(Department department)
	{
		departmentDao.update(department);
	}

	@Override
	public Department getByDepartmentSn(String departmentSn) {
		return departmentDao.getByDepartmentSn(departmentSn);
	}
	@Override
	public List<Department> findByPage(String hql , int pageNo, int pageSize) {
		return departmentDao.findByPage(hql, pageNo, pageSize);
	}
	@Override
	public	long findCount(Class<Department> department) {
		return departmentDao.findCount(department);
	}

	@Override
	public List<Department> findByPage(String hql, int pageNo, int pageSize, Object... params) {
		return departmentDao.findByPage(hql, pageNo, pageSize,params);
	}
	
	public List<Department> getDepartmentsByParentDepartmentSn(String parentDepartmentSn){
				return departmentDao.getDepartmentsByParentDepartmentSn(parentDepartmentSn);
	}
	public List<Department> getDepartmentsByParentDepartmentSnNotDeleted(String parentDepartmentSn){
		return departmentDao.getDepartmentsByParentDepartmentSnNotDeleted(parentDepartmentSn);
	}
	public int getChildDepartmentsCount(Department department){
		return departmentDao.getChildDepartmentsCount(department);
	}
	public Department getById(int id){
		return departmentDao.get(Department.class, id);
	}
	@Override
	public long findNum(String parentDepartmentSn) {
		return departmentDao.findNum(parentDepartmentSn);
	}

	@Override
	public List<Department> getDepartments(String departmentTypeSn) {
		return departmentDao.getDepartments(departmentTypeSn);
	}

	@Override
	public Department getUpNearestImplDepartment(String departmentSn) {		
		return departmentDao.getUpNearestImplDepartment(departmentSn);
	}

	@Override
	public Department getUpNearestByDepartmentType(String departmentSn, String departmentTypeSn) {
		return departmentDao.getUpNearestByDepartmentType(departmentSn, departmentTypeSn);
	}

	@Override
	public String getDownDepartmentByDepartmentType(String departmentSn, String departmentTypeSn) {
		return departmentDao.getDownDepartmentByDepartmentType(departmentSn, departmentTypeSn);
	}
	
	@Override
	public List<Department> getDepartments(String parentDepartmentSn, String departmentTypeSn) {
		return departmentDao.getDepartments(parentDepartmentSn, departmentTypeSn);
	}

	@Override
	public List<String> getAllDepartmentSn() {
		return departmentDao.getAllDepartmentSn();
	}

	@Override
	public List<String> getDepartmentSnByParentDepartmentSn(String parentDepartmentSn) {
		return departmentDao.getDepartmentSnByParentDepartmentSn(parentDepartmentSn);
	}

	@Override
	public Standard getImplStandards(String departmentSn, StandardType standardType) {		
		return departmentDao.getImplStandards(departmentSn, standardType);
	}

	@Override
	public long countHql(String hql) {
		
		return departmentDao.countHql(hql);
	}

	@Override
	public List<Department> getDepartmentsNew(String parentDepartmentSn, String departmentTypeSn,String clickEchart) {
		return departmentDao.getDepartmentsNew(parentDepartmentSn, departmentTypeSn,clickEchart);
	}

	@Override
	public List<Department> getAllDepartmentsByType(String departmentTypeSn) {
		return departmentDao.getAllDepartmentsByType(departmentTypeSn);
	}
	/**
	 * @author mahui
	 * @method 根据部门编号查询该部门下所有的贯标单位
	 * @date 2016年9月2日下午8:20:29
	 */
	@Override
	public List<Department> queryDepartment(String departmentSn) {
		return departmentDao.queryDepartment(departmentSn);
	}

	@Override
	//@Scheduled(cron="0 41 20 * * ?")   //每天三点执行
	public void setDepartmentNameByDepartment() {
		departmentDao.getDepartmentNameByDepartment();
	}

	@Override
	public Standard queryStandardByDepartmentType(String departmentTypeSn, StandardType standardType) {
		return departmentDao.queryStandardByDepartmentType(departmentTypeSn, standardType);
	}

	@Override
	public Department getUpNerestFgs(String departmentSn) {
		return departmentDao.getUpNerestFgs(departmentSn);
	}

	@Override
	public JSONObject queryDepartmentStandardIndex(String standardSn, String standardIndexSn, int page, int rows) {
		return departmentDao.queryDepartmentStandardIndex(standardSn, standardIndexSn, page, rows);
	}
}
