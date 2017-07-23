package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Speciality;

public interface SpecialityService {
	// ����ʵ��
	int addSpeciality(Speciality speciality);
	// ����IDɾ��ʵ��
	void deleteSpeciality(int id);
	// ��ȡ����ʵ��
	List<Speciality> getAllSpecialitys();
	//���ݱ�Ż�ȡʵ��
	Speciality getBySpecialitySn(String specialitySn);
	//����hql��ȡ����(mh)
	List<Speciality> getByHql(String hql);
	//����ids��ȡ����(mh)
	List<Speciality> getByIds(String ids);
	//ͨ�����ű�Ż�ȡרҵ
	List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn);
}
