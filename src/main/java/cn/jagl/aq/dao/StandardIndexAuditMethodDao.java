package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.StandardIndexAuditMethod;

public interface StandardIndexAuditMethodDao extends BaseDao<StandardIndexAuditMethod>{
	//����ָ��Id��ѯ
	public List<StandardIndexAuditMethod> queryJoinStandardIndex(int id);
	//����SN��ѯ
	public StandardIndexAuditMethod getBySn(String auditMethodSn);
	//getByID
	public StandardIndexAuditMethod getById(int id);
}
