package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.LatentHazard;
import net.sf.json.JSONObject;

public interface LatentHazardDao extends BaseDao<LatentHazard> {
	/**
	 * 查询个人上报记录
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showData(String personId, Integer page, Integer rows);
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
