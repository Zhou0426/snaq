package cn.jagl.aq.service;

import net.sf.json.JSONArray;

public interface ModuleStatisticsService {
	/**
	 * �������º���Դ���ѯ
	 * @param year
	 * @param month
	 * @param resourceSn
	 * @return
	 */
	public JSONArray findAll(int year, int month, String resourceSn);
}
