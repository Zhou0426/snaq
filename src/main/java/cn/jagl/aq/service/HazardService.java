package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Hazard;

public interface HazardService {

	int addHazard(Hazard hazard);
	
	void deleteHazard(int id);
	/**
	 * ����ֵģ����ѯ���ݲ���ҳ
	 * @param q
	 * @param departmentTypeSn
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Hazard> getByHazard(String q,String departmentTypeSn, int page, int rows);
	/**
	 * ����ֵģ����ѯ��������
	 * @param hazardSn
	 * @param departmentTypeSn
	 * @return
	 */
	Long getCountByHazard(String departmentTypeSn, String hazardSn);
	
	Hazard getByHazardSn(String hazardSn);
	
	void updateHazard(Hazard hazard);
	
	List<Hazard> getByPage(String hql , int page, int rows);
	
	Long findCount(Class<Hazard> Hazard);
	
	Hazard getById(Class<Hazard> hazard,int id);
	
	//ģ����ѯ������
	Long getCountByHazardSn(String departmentTypeSn,String hazardSn);
	//����hql����ѯ
	List<Hazard> getHazardsByHql(String hql);
}
