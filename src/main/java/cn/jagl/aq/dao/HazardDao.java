package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Hazard;

public interface HazardDao extends BaseDao<Hazard> {

	public Hazard getByHazardSn(String hazardSn);
	
	/**
	 * ����ֵģ����ѯ���ݲ���ҳ
	 * @param q
	 * @param departmentTypeSn
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Hazard> getByHazard(String q, String departmentTypeSn, int page, int rows);
	/**
	 * ����ֵģ����ѯ��������
	 * @param hazardSn
	 * @param departmentTypeSn
	 * @return
	 */
	public Long getCountByHazard(String departmentTypeSn, String hazardSn);
	
	//��ҳ��ѯʱ������
	public Long getCountByHazardSn(String departmentTypeSn,String hazardSn);
	
	//����hql����ѯ
	public List<Hazard> getHazardsByHql(String hql);
	
}
