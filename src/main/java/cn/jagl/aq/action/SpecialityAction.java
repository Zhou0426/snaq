package cn.jagl.aq.action;

import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.service.SpecialityService;
/* 
 * ���
 * 2016/7/6
 */
public class SpecialityAction extends BaseAction<Speciality> {
	private static final long serialVersionUID = 1L;
	private String departmentTypeSn;//�������ͱ��
	
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	//��ѯ����Ϊɾ����רҵ
	public String query(){
		String hql="select s FROM Speciality s WHERE s.deleted=false";
		jsonList=specialityService.getByHql(hql);
		return "jsonList";
	}
	
	//���ݵ�λ����ѡ��רҵ
	public String major(){
		String hql="select s from Speciality s where s.deleted=false and s.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
		jsonList=specialityService.getByHql(hql);
		return "jsonList";
	}
}
