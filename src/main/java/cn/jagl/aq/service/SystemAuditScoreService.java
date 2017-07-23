package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.SystemAuditScore;

/**
 * @author mahui
 * @method 
 * @date 2016年7月29日下午7:22:30
 */
public interface SystemAuditScoreService {
	//添加
	public void save(SystemAuditScore systemAuditScore);
	//更新
	public void update(SystemAuditScore systemAuditScore);
	//查询
	public SystemAuditScore getByMany(String auditSn, String indexSn);
	//根据systemAuditSn和indexSn查询指标下的所有符合度
	public List<SystemAuditScore> queryByMany(String auditSn,String indexSn);
	//根据auditSn查询所有打分
	public List<SystemAuditScore> queryByAuditSn(String auditSn);
}
