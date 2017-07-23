package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.PeriodicalCheckDao;
import cn.jagl.aq.domain.PeriodicalCheck;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:04:31
 */
@Repository("periodicalCheckDao")
public class PeriodicalCheckDaoHibernate4 extends BaseDaoHibernate5<PeriodicalCheck> implements PeriodicalCheckDao {

	//��Ż�ȡ
	@Override
	public PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn) {
		String hql="select p from PeriodicalCheck p WHERE periodicalCheckSn=:periodicalCheckSn";
		Query query=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("periodicalCheckSn", periodicalCheckSn);
		return (PeriodicalCheck)query.uniqueResult();
	}
	//������¼ɾ��
	@Override
	public void deleteByIds(String ids) {
		String hql="update PeriodicalCheck p SET p.deleted=true WHERE p.id in ("+ids+")";
		getSessionFactory().getCurrentSession().createQuery(hql).executeUpdate();
	}
	
	//��ȡ��¼��
	@Override
	public long count(String hql) {
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql).uniqueResult();
	}

}
