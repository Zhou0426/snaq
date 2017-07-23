package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SystemAuditScoreDetailsDao;
import cn.jagl.aq.domain.SystemAuditScoreDetails;
import cn.jagl.aq.service.SystemAuditScoreDetailsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��9��13������5:11:45
 */
@Service("systemAuditScoreDetailsService")
public class SystemAuditScoreDetailsServiceImpl implements SystemAuditScoreDetailsService {
	@Resource(name="systemAuditScoreDetailsDao")
	private SystemAuditScoreDetailsDao systemAuditScoreDetailsDao;

	@Override
	public JSONObject query(String auditSn, String indexSn, String departmentSn, int page, int rows) {
		return systemAuditScoreDetailsDao.query(auditSn, indexSn, departmentSn, page, rows);
	}

	@Override
	public JSONArray queryDepartment(String departmentSn) {
		return systemAuditScoreDetailsDao.queryDepartment(departmentSn);
	}

	//��������ѯ
	@Override
	public boolean isExists(String auditSn, String indexSn, String departmentName) {
		return systemAuditScoreDetailsDao.isExists(auditSn, indexSn, departmentName);
	}

	//���
	@Override
	public void save(SystemAuditScoreDetails systemAuditScoreDetails) {
		systemAuditScoreDetailsDao.save(systemAuditScoreDetails);
	}

	@Override
	public int minConformDegree(String auditSn, String indexSn, String departmentSn) {
		return systemAuditScoreDetailsDao.minConformDegree(auditSn, indexSn, departmentSn);
	}

	//ɾ��
	@Override
	public void delete(int id) {
		systemAuditScoreDetailsDao.delete(SystemAuditScoreDetails.class, id);
	}
	//����ʱ��������ѯ
	@Override
	public boolean isExistsUpdate(String auditSn, String indexSn, String departmentName, int id) {
		return systemAuditScoreDetailsDao.isExistsUpdate(auditSn, indexSn, departmentName, id);
	}

	@Override
	public void update(SystemAuditScoreDetails systemAuditScoreDetails) {
		systemAuditScoreDetailsDao.update(systemAuditScoreDetails);
	}

	@Override
	public SystemAuditScoreDetails getById(int id) {
		return systemAuditScoreDetailsDao.getById(id);
	}
}
