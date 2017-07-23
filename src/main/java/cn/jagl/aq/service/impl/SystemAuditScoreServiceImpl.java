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
 * @date 2016��7��29������7:39:47
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
	//����systemAuditSn��indexSn��ѯָ���µ����з��϶�
	@Override
	public List<SystemAuditScore> queryByMany(String auditSn, String indexSn) {
		return systemAuditScoreDao.queryByMany(auditSn, indexSn);
	}

	//����auditSn��ѯ���д��
	@Override
	public List<SystemAuditScore> queryByAuditSn(String auditSn) {
		// TODO Auto-generated method stub
		return systemAuditScoreDao.queryByAuditSn(auditSn);
	}

}
