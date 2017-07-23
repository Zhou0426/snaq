package cn.jagl.aq.service;

import cn.jagl.aq.domain.SystemAuditScoreDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��9��13������5:09:05
 */
public interface SystemAuditScoreDetailsService {
	//���
	public void save(SystemAuditScoreDetails systemAuditScoreDetails);
	//ɾ��
	public void delete(int id);
	//����
	public void update(SystemAuditScoreDetails systemAuditScoreDetails);
	//getBuId
	public SystemAuditScoreDetails getById(int id);
	//��ҳ��ѯ
	public JSONObject query(String auditSn, String indexSn, String departmentSn, int page, int rows);
	//��ѯ����
	public JSONArray queryDepartment(String departmentSn);
	//��������ѯ
	public boolean isExists(String auditSn, String indexSn, String departmentName);
	//����ʱ��������ѯ
	public boolean isExistsUpdate(String auditSn,String indexSn,String departmentName,int id);
	//��ѯ��ͷ��϶�
	public int minConformDegree(String auditSn,String indexSn,String departmentSn);
}
