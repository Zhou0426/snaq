package cn.jagl.aq.dao;

import java.util.HashMap;
import java.util.Map;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.HazardReported;

public interface HazardReportedDao extends BaseDao<HazardReported> {

	/**
	 * 根据人员和角色获取所有数据
	 */
	public Map<String, Object> queryAllData(String personId,HashMap<String, String> roles,int page,int rows);
	/**
	 * 根据id逻辑删除
	 */
	public void deleteData(int id);
	/**
	 * 失控次数统计展示
	 */
	public Map<String, Object> queryUnsafeCondition(String departmentSn, String hazardSn,int page,int rows);
	/**
	 * 失控次数统计展示隐患
	 */
	public Map<String, Object> queryCount(String departmentSn, String departmentTypeSn, int page, int rows);
	/**
	 * 根据id查找
	 */
	public HazardReported getById(int id);
	/**
	 * 根据危险源上报编号查找对应标准
	 * @param reportSn
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> showStandardIndex(String reportSn, int page, int rows);
}
