package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SystemAuditScoreDao;
import cn.jagl.aq.domain.SystemAuditScore;
import cn.jagl.aq.service.SystemAuditScoreService;

/**
 * @author mahui
 * @method 
 * @date 2016年7月29日下午7:39:47
 */
@Service("systemAuditScoreService")
public class SystemAuditScoreServiceImpl implements SystemAuditScoreService {

	@Resource(name="systemAuditScoreDao")
	private SystemAuditScoreDao systemAuditScoreDao;
	@Override
	public void save(SystemAuditScore systemAuditScore) {
		systemAuditScoreDao.save(systemAuditScore);
	}

	@Override
	public void update(SystemAuditScore systemAuditScore) {
		systemAuditScoreDao.update(systemAuditScore);
	}

	@Override
	public SystemAuditScore getByMany(String auditSn, String indexSn) {
		return systemAuditScoreDao.getByMany(auditSn, indexSn);
	}
	//根据systemAuditSn和indexSn查询指标下的所有符合度
	@Override
	public List<SystemAuditScore> queryByMany(String auditSn, String indexSn) {
		return systemAuditScoreDao.queryByMany(auditSn, indexSn);
	}

	//根据auditSn查询所有打分
	@Override
	public List<SystemAuditScore> queryByAuditSn(String auditSn) {
		// TODO Auto-generated method stub
		return systemAuditScoreDao.queryByAuditSn(auditSn);
	}

}
