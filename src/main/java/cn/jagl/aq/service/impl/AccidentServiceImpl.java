package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.AccidentDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.service.AccidentService;

@Service("accidentService")
public class AccidentServiceImpl implements AccidentService{
	@Resource(name="accidentDao")
	private AccidentDao accidentDao;

	@Override
	public long countHql(String hql) {
		return accidentDao.countHql(hql);
	}

	@Override
	public List<Accident> findByPage(String hql, int pageNo, int pageSize) {
		return accidentDao.findByPage(hql, pageNo, pageSize);
	}

	@Override
	public Accident getByAccidentSn(String accidentSn) {
		return  accidentDao.getByAccidentSn(accidentSn);
	}

	@Override
	public void add(Accident accident) {
		accidentDao.save(accident);
	}

	@Override
	public void delete(Accident accident) {
		accidentDao.delete(accident);
	}

	@Override
	public void update(Accident accident) {
		accidentDao.update(accident);
	}



}
