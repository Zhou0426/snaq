package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.NearMissDao;
import cn.jagl.aq.domain.NearMiss;
import cn.jagl.aq.service.NearMissService;
@Service("nearMissService")
public class NearMissServiceImpl implements NearMissService{
	@Resource(name="nearMissDao")
	private NearMissDao nearMissDao;

	@Override
	public List<NearMiss> findByPage(String hql, int pageNo, int pageSize) {
		return nearMissDao.findByPage(hql, pageNo, pageSize);
	}
	@Override
	public long findTotal(String nearMissState) {
		return nearMissDao.findTotal(nearMissState);
	}
	@Override
	public List<String> getAllNearMissSn() {
		return nearMissDao.getAllNearMissSn();
	}
	@Override
	public NearMiss getByNearMissSn(String nearMissSn) {
		return nearMissDao.getByNearMissSn(nearMissSn);
	}
	@Override
	public void delete(NearMiss nearMiss) {
		nearMissDao.delete(nearMiss);
		
	}
	@Override
	public void update(NearMiss nearMiss) {
		nearMissDao.update(nearMiss);
	}
	@Override
	public void add(NearMiss nearMiss) {
		nearMissDao.save(nearMiss);
	}
	@Override
	public void deleteById(int id) {
		nearMissDao.delete(NearMiss.class, id);
	}
	@Override
	public long countHql(String hql) {
		return nearMissDao.countHql(hql);
	}
}
