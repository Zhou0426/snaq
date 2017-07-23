package cn.jagl.aq.service;

import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年9月13日下午5:09:05
 */
public interface SystemAuditScoreDetailsService {
	//添加
	public void save(SystemAuditScoreDetails systemAuditScoreDetails);
	//删除
	public void delete(int id);
	//更新
	public void update(SystemAuditScoreDetails systemAuditScoreDetails);
	//getBuId
	public SystemAuditScoreDetails getById(int id);
	//分页查询
	public JSONObject query(String auditSn, String indexSn, String departmentSn, int page, int rows);
	//查询部门
	public JSONArray queryDepartment(String departmentSn);
	//多条件查询
	public boolean isExists(String auditSn, String indexSn, String departmentName);
	//更新时多条件查询
	public boolean isExistsUpdate(String auditSn,String indexSn,String departmentName,int id);
	//查询最低符合度
	public int minConformDegree(String auditSn,String indexSn,String departmentSn);
}
