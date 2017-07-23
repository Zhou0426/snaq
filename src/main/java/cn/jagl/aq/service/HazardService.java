package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Hazard;

public interface HazardService {

	int addHazard(Hazard hazard);
	
	void deleteHazard(int id);
	/**
	 * 根据值模糊查询数据并分页
	 * @param q
	 * @param departmentTypeSn
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Hazard> getByHazard(String q,String departmentTypeSn, int page, int rows);
	/**
	 * 根据值模糊查询数据总数
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
	
	//模糊查询的总数
	Long getCountByHazardSn(String departmentTypeSn,String hazardSn);
	//根据hql语句查询
	List<Hazard> getHazardsByHql(String hql);
}
