package cn.jagl.aq.service;

import java.util.HashMap;
import java.util.Map;

import cn.jagl.aq.domain.HazardReported;

public interface HazardReportedService {

	/**
	 * 根据人员和角色获取所有数据
	 */
	public Map<String, Object> queryAllData(String personId,HashMap<String, String> roles,int page,int rows);
	/**
	 * 根据id逻辑删除
	 */
	public void deleteData(int id);
	/**
	 * 根据id查找数据
	 */
	public HazardReported getById(int id);
	/**
	 * 添加数据
	 */
	public void addData(HazardReported hazardReported);
	/**
	 * 更新数据
	 */
	public void updateData(HazardReported hazardReported);
	/**
	 * 失控次数统计展示
	 */
	public Map<String, Object> queryCount(String departmentSn, String departmentTypeSn,int page,int rows);
	/**
	 * 失控次数统计展示隐患
	 * @param departmentSn
	 * @param hazardSn
	 */
	public Map<String, Object> queryUnsafeCondition(String departmentSn, String hazardSn, int page,int rows);
	/**
	 * 根据危险源上报编号查找对应标准
	 * @param reportSn
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> showStandardIndex(String reportSn, int page, int rows);
}
