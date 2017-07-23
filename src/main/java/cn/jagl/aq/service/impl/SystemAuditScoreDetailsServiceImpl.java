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
 * @date 2016年9月13日下午5:11:45
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

	//多条件查询
	@Override
	public boolean isExists(String auditSn, String indexSn, String departmentName) {
		return systemAuditScoreDetailsDao.isExists(auditSn, indexSn, departmentName);
	}

	//添加
	@Override
	public void save(SystemAuditScoreDetails systemAuditScoreDetails) {
		systemAuditScoreDetailsDao.save(systemAuditScoreDetails);
	}

	@Override
	public int minConformDegree(String auditSn, String indexSn, String departmentSn) {
		return systemAuditScoreDetailsDao.minConformDegree(auditSn, indexSn, departmentSn);
	}

	//删除
	@Override
	public void delete(int id) {
		systemAuditScoreDetailsDao.delete(SystemAuditScoreDetails.class, id);
	}
	//更新时多条件查询
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
