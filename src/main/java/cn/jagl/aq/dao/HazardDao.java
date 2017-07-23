package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Hazard;

public interface HazardDao extends BaseDao<Hazard> {

	public Hazard getByHazardSn(String hazardSn);
	
	/**
	 * 根据值模糊查询数据并分页
	 * @param q
	 * @param departmentTypeSn
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Hazard> getByHazard(String q, String departmentTypeSn, int page, int rows);
	/**
	 * 根据值模糊查询数据总数
	 * @param hazardSn
	 * @param departmentTypeSn
	 * @return
	 */
	public Long getCountByHazard(String departmentTypeSn, String hazardSn);
	
	//分页查询时的总数
	public Long getCountByHazardSn(String departmentTypeSn,String hazardSn);
	
	//根据hql语句查询
	public List<Hazard> getHazardsByHql(String hql);
	
}
