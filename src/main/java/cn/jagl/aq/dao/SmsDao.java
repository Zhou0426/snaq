package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Sms;

/**
 * @author mahui
 *
 */
public interface SmsDao extends BaseDao<Sms> {
	//public get By serialNumber
	public Sms getBySerialNumber(String serialNumber);
}
