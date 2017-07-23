package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditScore;

/**
 * @author mahui
 * @method 
 * @date 2016年7月29日下午7:11:34
 */
public interface SystemAuditScoreDao extends BaseDao<SystemAuditScore> {
	//根据systemAuditSn和indexSn查询
	public SystemAuditScore getByMany(String auditSn,String indexSn);
	//根据systemAuditSn和indexSn查询指标下的所有符合度
	public List<SystemAuditScore> queryByMany(String auditSn,String indexSn);
	//根据auditSn查询所有打分
	public List<SystemAuditScore> queryByAuditSn(String auditSn);
}
