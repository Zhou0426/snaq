package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeActStandardDao;
import cn.jagl.aq.domain.UnsafeActStandard;
import cn.jagl.aq.service.UnsafeActStandardService;
@Service("unsafeActStandardService")
public class UnsafeActStandardServiceImpl implements UnsafeActStandardService {

	@Resource(name="unsafeActStandardDao")
	private UnsafeActStandardDao unsafeActStandardDao;
	@Override
	public UnsafeActStandard getByUnsafeActStandardSn(String unsafeActStandardSn) {
		// TODO Auto-generated method stub
		return unsafeActStandardDao.getByUnsafeActStandard(unsafeActStandardSn);
	}

	@Override
	public List<UnsafeActStandard> findByPage(String hql, int page, int rows) {
		// TODO Auto-generated method stub
		return unsafeActStandardDao.findByPage(hql, page, rows);
	}

	@Override
	public List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page, int rows) {
		// TODO Auto-generated method stub
		return unsafeActStandardDao.getStandardByFullTextQuery(q,departmentTypeSn, page, rows);
	}

	@Override
	public List<UnsafeActStandard> getByHql(String hql) {
		// TODO Auto-generated method stub
		return unsafeActStandardDao.getByHql(hql);
	}

}
