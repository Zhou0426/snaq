package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Speciality;
/*
 * 马辉2106/7/6
 * 
 */
public interface SpecialityDao extends BaseDao<Speciality> {
	//根据编号获取（mh）
	public Speciality getBySpecialitySn(String specialitySn);
	//根据hql获取集合(mh)
	public List<Speciality> getByHql(String hql);
	//根据ids获取集合
	public List<Speciality> getByIds(String ids);
	//通过部门编号获取专业
	List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn);
}
