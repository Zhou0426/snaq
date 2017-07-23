package cn.jagl.aq.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.NearMissAuditDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.NearMissAudit;
@Repository("nearMissAuditDao")
public class NearMissAuditDaoImpl extends BaseDaoHibernate5<NearMissAudit>
implements NearMissAuditDao{


}
