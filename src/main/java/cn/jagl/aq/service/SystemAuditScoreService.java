package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.SystemAuditScore;

/**
 * @author mahui
 * @method 
 * @date 2016��7��29������7:22:30
 */
public interface SystemAuditScoreService {
	//���
	public void save(SystemAuditScore systemAuditScore);
	//����
	public void update(SystemAuditScore systemAuditScore);
	//��ѯ
	public SystemAuditScore getByMany(String auditSn, String indexSn);
	//����systemAuditSn��indexSn��ѯָ���µ����з��϶�
	public List<SystemAuditScore> queryByMany(String auditSn,String indexSn);
	//����auditSn��ѯ���д��
	public List<SystemAuditScore> queryByAuditSn(String auditSn);
}
