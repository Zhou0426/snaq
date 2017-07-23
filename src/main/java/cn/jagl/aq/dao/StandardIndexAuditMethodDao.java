package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.StandardIndexAuditMethod;

public interface StandardIndexAuditMethodDao extends BaseDao<StandardIndexAuditMethod>{
	//根据指标Id查询
	public List<StandardIndexAuditMethod> queryJoinStandardIndex(int id);
	//根据SN查询
	public StandardIndexAuditMethod getBySn(String auditMethodSn);
	//getByID
	public StandardIndexAuditMethod getById(int id);
}
