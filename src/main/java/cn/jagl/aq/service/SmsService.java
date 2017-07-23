package cn.jagl.aq.service;

import cn.jagl.aq.domain.Sms;

/**
 * @author mahui
 *
 */
public interface SmsService {
	//save
	public void save(Sms sms);
	//update
	public void update(Sms sms);
	//delete
	public void delete(int id);
	//public get By serialNumber
	public Sms getBySerialNumber(String serialNumber);
}
