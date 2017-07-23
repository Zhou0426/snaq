package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.jagl.aq.dao.DepartmentUseStatDao;
import cn.jagl.aq.dao.PersonUseStatDao;
import cn.jagl.aq.domain.DepartmentUseStat;
import cn.jagl.aq.domain.PersonUseStat;
import cn.jagl.aq.service.DepartmentUseStatService;
import cn.jagl.aq.service.PersonUseStatService;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PersonUseStatDao;
import cn.jagl.aq.domain.PersonUseStat;
import cn.jagl.aq.service.PersonUseStatService;
@Service("departmentUseStatService")
public class DepartmentUseStatServiceImpl implements DepartmentUseStatService{

	@Resource(name="departmentUseStatDao")
	private DepartmentUseStatDao departmentUseStatDao;
	@Override
	public List<DepartmentUseStat> findByPage(String hql, int pageNo, int pageSize) {
		return departmentUseStatDao.findByPage(hql, pageNo, pageSize);
	}
	@Override
	public long countHql(String hql) {
		return departmentUseStatDao.countHql(hql);
	}
}
