package cn.jagl.aq.dao;

import net.sf.json.JSONArray;

public interface ModuleStatisticsDao {

	/**
	 * �������º���Դ���ѯ
	 * @param year
	 * @param month
	 * @param resourceSn
	 * @return
	 */
	public JSONArray findAll(int year, int month, String resourceSn);
}
