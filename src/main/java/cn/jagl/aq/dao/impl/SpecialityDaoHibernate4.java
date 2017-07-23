package cn.jagl.aq.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SpecialityDao;
import cn.jagl.aq.domain.Speciality;
/*
 * Âí»Ô
 * 2016/7/6
 */
@SuppressWarnings("unchecked")
@Repository("specialityDao")
public class SpecialityDaoHibernate4 extends BaseDaoHibernate5<Speciality> implements SpecialityDao {
	@Override
	public Speciality getBySpecialitySn(String specialitySn) {
		Query query=getSessionFactory().getCurrentSession()
				.createQuery("select s from Speciality s where specialitySn=:specialitySn")
				.setString("specialitySn", specialitySn);
		return (Speciality)query.uniqueResult();
	}

	@Override
	public List<Speciality> getByHql(String hql) {
		List<Speciality> specialityList=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.list();
		return specialityList;
	}

	@Override
	public List<Speciality> getByIds(String ids) {
		String hql="select s FROM Speciality s WHERE id in("+ids+")";
		List<Speciality> specialityList=getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		return specialityList;
	}

	@Override
	public List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn) {
		Query query=getSessionFactory().getCurrentSession()
				.createQuery("select s FROM Speciality s WHERE s.departmentType.departmentTypeSn =:departmentTypeSn").setString("departmentTypeSn", departmentTypeSn);
		return (List<Speciality>)query.list();
	}

}
