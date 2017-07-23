package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年9月13日下午5:08:03
 */
public interface SystemAuditScoreDetailsDao extends BaseDao<SystemAuditScoreDetails> {
	public JSONObject query(String auditSn,String indexSn,String departmentSn,int page,int rows);
	//查询部门
	public JSONArray queryDepartment(String departmentSn);
	//多条件查询
	public boolean isExists(String auditSn,String indexSn,String departmentName);
	//更新时多条件查询
	public boolean isExistsUpdate(String auditSn,String indexSn,String departmentName,int id);
	//查询最低符合度
	public int minConformDegree(String auditSn,String indexSn,String departmentSn);
	//getById
	public SystemAuditScoreDetails getById(int id); 
}
