package cn.jagl.aq.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SpecialCheckDao;
import cn.jagl.aq.domain.SpecialCheck;

/**
 * @author mahui
 *
 * @date 2016��7��8������5:14:37
 */
@Repository("specialCheckDao")
public class SpecialCheckDaoHibernate4 extends BaseDaoHibernate5<SpecialCheck> implements SpecialCheckDao{
	//��Ż�ȡ
	@Override
	public SpecialCheck getBySpecialCheckSn(String specialCheckSn) {
		String hql="select s from SpecialCheck s WHERE specialCheckSn=:specialCheckSn";
		Query query=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("specialCheckSn",specialCheckSn);
		return (SpecialCheck)query.uniqueResult();
	}

	//��ȡ��¼��
	@Override
	public long count(String hql) {		
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	//����ɾ��
	@Override
	public void deleteByIds(String ids) {
		String hql="update SpecialCheck s SET s.deleted=true WHERE s.id in ("+ids+")";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}

}
