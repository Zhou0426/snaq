package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CheckTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年8月2日下午3:09:09
 */
public interface CheckTableDao extends BaseDao<CheckTable> {
	//查询我的检查表
	public JSONObject query(String personId,int page,int rows);
	//查看检查表详情
	public JSONObject queryDetails(int id,String periodicalCheckSn,String specialCheckSn,String auditSn,int page,int rows);
	//我的检查表记录数
	public long count(String personId);
	//查询检查表
	public JSONArray queryJoinCheck(String specialCheckSn, String periodicalCheckSn,String auditSn);
	/**
	 * 查询未确认的检查表个数
	 * @param 蒋宇森
	 * @return
	 */
	public long countExceptTrue(String personId);
}
