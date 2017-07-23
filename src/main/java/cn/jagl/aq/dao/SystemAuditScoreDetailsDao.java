package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��9��13������5:08:03
 */
public interface SystemAuditScoreDetailsDao extends BaseDao<SystemAuditScoreDetails> {
	public JSONObject query(String auditSn,String indexSn,String departmentSn,int page,int rows);
	//��ѯ����
	public JSONArray queryDepartment(String departmentSn);
	//��������ѯ
	public boolean isExists(String auditSn,String indexSn,String departmentName);
	//����ʱ��������ѯ
	public boolean isExistsUpdate(String auditSn,String indexSn,String departmentName,int id);
	//��ѯ��ͷ��϶�
	public int minConformDegree(String auditSn,String indexSn,String departmentSn);
	//getById
	public SystemAuditScoreDetails getById(int id); 
}
