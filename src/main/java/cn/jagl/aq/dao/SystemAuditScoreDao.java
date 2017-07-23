package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditScore;

/**
 * @author mahui
 * @method 
 * @date 2016��7��29������7:11:34
 */
public interface SystemAuditScoreDao extends BaseDao<SystemAuditScore> {
	//����systemAuditSn��indexSn��ѯ
	public SystemAuditScore getByMany(String auditSn,String indexSn);
	//����systemAuditSn��indexSn��ѯָ���µ����з��϶�
	public List<SystemAuditScore> queryByMany(String auditSn,String indexSn);
	//����auditSn��ѯ���д��
	public List<SystemAuditScore> queryByAuditSn(String auditSn);
}
