package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.AccidentTypeDao;
import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.service.AccidentTypeService;
@Service("accidentTypeService")
public class AccidentTypeServiceImpl implements AccidentTypeService {

	@Resource(name="accidentTypeDao")
	private AccidentTypeDao accidentTypeDao;
	@Override
	public int addAccidentType(AccidentType accidentType) {
		return (Integer) accidentTypeDao.save(accidentType);
	}

	@Override
	public void deleteAccidentType(int id) {
		accidentTypeDao.delete(AccidentType.class, id);
	}

	@Override
	public List<AccidentType> getAllAccidentType() {
		return accidentTypeDao.findAll(AccidentType.class);
	}

	@Override
	public AccidentType getByAccidentTypeSn(String accidentTypeSn) {
		return accidentTypeDao.getByAccidentTypeSn(accidentTypeSn);
	}

}
