package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.HazardDao;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.service.HazardService;
@Service("hazardService")
public class HazardServiceImpl implements HazardService {

	@Resource(name="hazardDao")
	private HazardDao hazardDao;
	@Override
	public int addHazard(Hazard hazard) {
		// TODO Auto-generated method stub
		return (Integer) hazardDao.save(hazard);
	}

	@Override
	public void deleteHazard(int id) {
		// TODO Auto-generated method stub
		Hazard hazard=this.getById(Hazard.class, id);
		hazard.getManageObjects().clear();
		hazard.getStandardIndexes().clear();
		this.updateHazard(hazard);
		hazardDao.delete(Hazard.class, id);
	}

	@Override
	public List<Hazard> getByHazard(String q,String departmentTypeSn, int page, int rows) {
		// TODO Auto-generated method stub
		return hazardDao.getByHazard( q, departmentTypeSn, page, rows);
	}

	@Override
	public Hazard getByHazardSn(String hazardSn) {
		// TODO Auto-generated method stub
		return hazardDao.getByHazardSn(hazardSn);
	}

	@Override
	public void updateHazard(Hazard hazard) {
		// TODO Auto-generated method stub
		hazardDao.update(hazard);
	}

	@Override
	public List<Hazard> getByPage(String hql, int page, int rows) {
		// TODO Auto-generated method stub
		return hazardDao.findByPage(hql, page, rows);
	}

	@Override
	public Long findCount(Class<Hazard> Hazard) {
		// TODO Auto-generated method stub
		return hazardDao.findCount(Hazard);
	}

	@Override
	public Hazard getById(Class<Hazard> hazard, int id) {
		// TODO Auto-generated method stub
		return hazardDao.get(hazard, id);
	}

	@Override
	public Long getCountByHazardSn(String departmentTypeSn, String hazardSn) {
		// TODO Auto-generated method stub
		return hazardDao.getCountByHazardSn(departmentTypeSn, hazardSn);
	}

	@Override
	public List<Hazard> getHazardsByHql(String hql) {
		// TODO Auto-generated method stub
		return hazardDao.getHazardsByHql(hql);
	}

	@Override
	public Long getCountByHazard(String departmentTypeSn, String hazardSn) {
		// TODO Auto-generated method stub
		return hazardDao.getCountByHazard(departmentTypeSn, hazardSn);
	}	

}
