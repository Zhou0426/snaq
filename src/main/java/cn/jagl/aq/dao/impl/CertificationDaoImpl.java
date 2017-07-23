package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.CertificationDao;
import cn.jagl.aq.domain.Certification;
@Repository("certificationDao")
public class CertificationDaoImpl extends BaseDaoHibernate5<Certification> implements CertificationDao{

	//hql²éÑ¯×ÜÊý
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public Certification getByCertificationSn(String certificationSn) {
		String hql="select c from Certification c where c.certificationSn='" + certificationSn + "'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Certification)query.uniqueResult();
	}

	@Override
	public Certification getById(int id) {
		String hql="select c from Certification c where c.id="+id;
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Certification)query.uniqueResult();
	}

	@Override
	public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn) {
		String hql="select c from Certification c where c.certificationSn='" + certificationSn + "' and c.certificationType.certificationTypeSn = '" + certificationTypeSn + "'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Certification)query.uniqueResult();
	}

	@Override
	public void deleteByIds(String ids) {
		String[] str = ids.split(",");
		String sql="DELETE FROM certification where id in (:ids)";
		getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.setParameterList("ids", str).executeUpdate();
	}
}
