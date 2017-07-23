package cn.jagl.aq.dao.impl;


import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SmsDao;
import cn.jagl.aq.domain.Sms;
/**
 * @author mahui
 *
 */
@Repository("smsDao")
public class SmsDaoImpl extends BaseDaoHibernate5<Sms> implements SmsDao {

	@Override
	public Sms getBySerialNumber(String serialNumber) {
		String hql="select s from Sms s where s.serialNumber=:serialNumber";
		return (Sms) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("serialNumber",serialNumber)
					.uniqueResult();
	}

}
