package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SpecialityDao;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.service.SpecialityService;
@Service("specialityService")
public class SpecialityServiceImpl implements SpecialityService {
	@Resource(name="specialityDao")
	private SpecialityDao specialityDao;

	@Override
	public int addSpeciality(Speciality speciality) {
		// TODO Auto-generated method stub
		return (Integer)specialityDao.save(speciality);
	}

	@Override
	public void deleteSpeciality(int id) {
		// TODO Auto-generated method stub
		specialityDao.delete(Speciality.class, id);
	}

	@Override
	public List<Speciality> getAllSpecialitys() {
		// TODO Auto-generated method stub
		return specialityDao.findAll(Speciality.class);
	}

	@Override
	public Speciality getBySpecialitySn(String specialitySn) {
		// TODO Auto-generated method stub
		return specialityDao.getBySpecialitySn(specialitySn);
	}

	@Override
	public List<Speciality> getByHql(String hql) {
		// TODO Auto-generated method stub
		return specialityDao.getByHql(hql);
	}

	@Override
	public List<Speciality> getByIds(String ids) {
		// TODO Auto-generated method stub
		return specialityDao.getByIds(ids);
	}

	@Override
	public List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn) {
		return specialityDao.getSpecialitysByDepartmentTypeSn(departmentTypeSn);
	}
}
