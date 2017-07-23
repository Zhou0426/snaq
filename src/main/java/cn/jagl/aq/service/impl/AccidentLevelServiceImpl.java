package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.AccidentLevelDao;
import cn.jagl.aq.domain.AccidentLevel;
import cn.jagl.aq.service.AccidentLevelService;

@Service("accidentLevelService")
public class AccidentLevelServiceImpl implements AccidentLevelService{
	@Resource(name="accidentLevelDao")
	private AccidentLevelDao accidentLevelDao;

	@Override
	public List<AccidentLevel> getAllAccidentLevel() {
		return accidentLevelDao.findAll(AccidentLevel.class);
	}

	@Override
	public AccidentLevel getByAccidentLevelSn(String accidentLevelSn) {
		return accidentLevelDao.getByAccidentLevelSn(accidentLevelSn);
	}


}