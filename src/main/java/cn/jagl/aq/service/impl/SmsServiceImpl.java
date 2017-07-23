package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SmsDao;
import cn.jagl.aq.domain.Sms;
import cn.jagl.aq.service.SmsService;

/**
 * @author mahui
 *
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService {

	@Resource(name="smsDao")
	private SmsDao smsDao;
	@Override
	public void save(Sms sms) {
		smsDao.save(sms);
	}

	@Override
	public void update(Sms sms) {
		smsDao.update(sms);
	}

	@Override
	public void delete(int id) {
		smsDao.delete(Sms.class, id);
	}

	@Override
	public Sms getBySerialNumber(String serialNumber) {
		return smsDao.getBySerialNumber(serialNumber);
	}

}
