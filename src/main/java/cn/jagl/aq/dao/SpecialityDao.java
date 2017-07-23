package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Speciality;
/*
 * ���2106/7/6
 * 
 */
public interface SpecialityDao extends BaseDao<Speciality> {
	//���ݱ�Ż�ȡ��mh��
	public Speciality getBySpecialitySn(String specialitySn);
	//����hql��ȡ����(mh)
	public List<Speciality> getByHql(String hql);
	//����ids��ȡ����
	public List<Speciality> getByIds(String ids);
	//ͨ�����ű�Ż�ȡרҵ
	List<Speciality> getSpecialitysByDepartmentTypeSn(String departmentTypeSn);
}
