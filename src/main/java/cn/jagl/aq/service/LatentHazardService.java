package cn.jagl.aq.service;

import cn.jagl.aq.domain.LatentHazard;
import net.sf.json.JSONObject;

public interface LatentHazardService {
	
	/**
	 * 查询个人上报记录
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showData(String personId, Integer page, Integer rows);
	/**
	 * 新增数据
	 * @param latentHazard
	 */
	void add(LatentHazard latentHazard);
	/**
	 * 根据id查询数据
	 * @param id
	 * @return
	 */
	LatentHazard queryById(int id);
	/**
	 * 更新数据
	 * @param latentHazard
	 */
	void update(LatentHazard latentHazard);
	/**
	 * 逻辑删除重大隐患上报记录
	 * @param id
	 */
	void deleteById(int id);
	/**
	 * 查询本部门审核记录
	 * @param departmentSn
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showAuditData(String departmentSn, Integer page, Integer rows);
	/**
	 * 查询销号记录
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showCancelData(Integer page, Integer rows);

}
