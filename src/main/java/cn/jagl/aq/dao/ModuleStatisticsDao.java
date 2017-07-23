package cn.jagl.aq.dao;

import net.sf.json.JSONArray;

public interface ModuleStatisticsDao {

	/**
	 * 根据年月和资源项查询
	 * @param year
	 * @param month
	 * @param resourceSn
	 * @return
	 */
	public JSONArray findAll(int year, int month, String resourceSn);
}
