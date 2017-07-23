package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PersonRecordDao;
import cn.jagl.aq.domain.PersonRecord;
import cn.jagl.aq.service.PersonRecordService;

@Service("personRecordService")
public class PersonRecordServiceImpl implements PersonRecordService {

	@Resource(name="personRecordDao")
	private PersonRecordDao personRecordDao;

	@Override
	public PersonRecord getByPersonIdDepartmentSn(String personId, String departmentSn) {
		// TODO Auto-generated method stub
		return personRecordDao.getByPersonIdDepartmentSn(personId, departmentSn);
	}

	@Override
	public void add(PersonRecord personRecord) {
		personRecordDao.save(personRecord);
	}

	@Override
	public void update(PersonRecord personRecord) {
		personRecordDao.update(personRecord);	
	}

	@Override
	public List<PersonRecord> getListByPersonId(String personId) {
		// TODO Auto-generated method stub
		return personRecordDao.getListByPersonId(personId);
	}
	@Override
	public PersonRecord getByPersonId(String personId) {
		// TODO Auto-generated method stub
		return personRecordDao.getByPersonId(personId);
	}

	@Override
	public void Manydelete(String ids) {
		// TODO Auto-generated method stub
		personRecordDao.Manydelete(ids);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		personRecordDao.delete(PersonRecord.class, id);;
	}

	@Override
	public List<PersonRecord> getMonthByPersonId(String personId) {
		// TODO Auto-generated method stub
		return personRecordDao.getMonthByPersonId(personId);
	}
}
