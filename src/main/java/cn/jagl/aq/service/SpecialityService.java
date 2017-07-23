package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Speciality;

public interface SpecialityService {
	// 保存实体
	int addSpeciality(Speciality speciality);
	// 根据ID删除实体
	void deleteSpeciality(int id);
	// 获取所有实体
	List<Speciality> getAllSpecialitys();
	//根据编号获取实体
	Speciality getBySpecialitySn(String specialitySn);
	//根据hql获取集合(mh)
	List<Speciality> getByHql(String hql);
	//根据ids获取集合(mh)
	List<Speciality> getByIds(String ids);
	//通过部门编号获取专业
	List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn);
}
