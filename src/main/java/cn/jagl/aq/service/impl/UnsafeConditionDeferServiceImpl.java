package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeConditionDeferDao;
import cn.jagl.aq.domain.UnsafeConditionDefer;
import cn.jagl.aq.service.UnsafeConditionDeferService;
import net.sf.json.JSONArray;
@Service("unsafeConditionDeferService")
public class UnsafeConditionDeferServiceImpl implements UnsafeConditionDeferService {
	@Resource(name="unsafeConditionDeferDao")
	private UnsafeConditionDeferDao unsafeConditionDeferDao;

	@Override
	public UnsafeConditionDefer getBySn(String applicationSn) {
		return unsafeConditionDeferDao.getBySn(applicationSn);
	}

	@Override
	public List<UnsafeConditionDefer> queryByPage(String hql, int page, int rows) {
		return unsafeConditionDeferDao.findByPage(hql, page, rows);
	}

	@Override
	public void save(UnsafeConditionDefer unsafeConditionDefer) {
		unsafeConditionDeferDao.save(unsafeConditionDefer);
	}

	@Override
	public void update(UnsafeConditionDefer unsafeConditionDefer) {
		unsafeConditionDeferDao.update(unsafeConditionDefer);
	}

	@Override
	public long getByhql(String hql) {
		return unsafeConditionDeferDao.getByhql(hql);
	}

	@Override
	public JSONArray getListByPersonId(String personId) {
		return unsafeConditionDeferDao.getListByPersonId(personId);
	}
	
	
}
