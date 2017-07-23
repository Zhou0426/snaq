package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.LatentHazardDao;
import cn.jagl.aq.domain.LatentHazard;
import cn.jagl.aq.service.LatentHazardService;
import net.sf.json.JSONObject;

@Service("latentHazardService")
public class LatentHazardServiceImpl implements LatentHazardService {

	@Resource(name="latentHazardDao")
	private LatentHazardDao latentHazardDao;

	@Override
	public JSONObject showData(String personId, Integer page, Integer rows) {
		return latentHazardDao.showData(personId, page, rows);
	}

	@Override
	public void add(LatentHazard latentHazard) {
		latentHazardDao.save(latentHazard);
	}

	@Override
	public LatentHazard queryById(int id) {
		return latentHazardDao.get(LatentHazard.class, id);
	}

	@Override
	public void update(LatentHazard latentHazard) {
		latentHazardDao.update(latentHazard);
	}

	@Override
	public void deleteById(int id) {
		latentHazardDao.deleteById(id);
	}

	@Override
	public JSONObject showAuditData(String departmentSn, Integer page, Integer rows) {
		return latentHazardDao.showAuditData(departmentSn, page, rows);
	}

	@Override
	public JSONObject showCancelData(Integer page, Integer rows) {
		return latentHazardDao.showCancelData(page, rows);
	}
	
	
}
