package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.InteriorWorkDao;
import cn.jagl.aq.domain.InteriorWork;
import cn.jagl.aq.service.InteriorWorkService;
@Service("interiorWorkService")
public class InteriorWorkServiceImpl implements InteriorWorkService {
	@Resource(name="interiorWorkDao")
	private InteriorWorkDao interiorWorkDao;
	
	//添加interiorWork
	@Override
	public int addInteriorWork(InteriorWork interiorWork) {
		// TODO Auto-generated method stub
		return (Integer) interiorWorkDao.save(interiorWork);
	}

	@Override
	public void deletInteriorWork(int id) {
		// TODO Auto-generated method stub
		interiorWorkDao.delete(InteriorWork.class, id);
	}
	//根据编号得到内业资料
	@Override
	public InteriorWork getByInteriorWorkSn(String InteriorWorkSn) {
		// TODO Auto-generated method stub
		return interiorWorkDao.getByInteriorWorkSn(InteriorWorkSn);
	}
	
	//分页获取数据
	@Override
	public List<InteriorWork> findByPage(String hql, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return interiorWorkDao.findByPage(hql, pageNo,pageSize);
	}

	@Override
	public List<InteriorWork> getCountByHql(String hql) {
		// TODO Auto-generated method stub
		return interiorWorkDao.getCountByHql(hql);
	}

	@Override
	public void update(InteriorWork interiorWork) {
		// TODO Auto-generated method stub
		interiorWorkDao.update(interiorWork);
	}

	@Override
	public long getByHql(String hql) {
		// TODO Auto-generated method stub
		return interiorWorkDao.getByHql(hql);
	}

	@Override
	public long getBySql(String sql) {
		// TODO Auto-generated method stub
		return interiorWorkDao.getBySql(sql);
	}

	
}
