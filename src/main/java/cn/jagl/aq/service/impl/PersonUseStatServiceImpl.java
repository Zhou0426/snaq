package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PersonUseStatDao;
import cn.jagl.aq.domain.PersonUseStat;
import cn.jagl.aq.service.PersonUseStatService;
@Service("personUseStatService")
public class PersonUseStatServiceImpl implements PersonUseStatService{
	@Resource(name="personUseStatDao")
	private PersonUseStatDao personUseStatDao;
	@Override
	public List<PersonUseStat> findByPage(String hql, int pageNo, int pageSize) {
		return personUseStatDao.findByPage(hql, pageNo, pageSize);
	}
	@Override
	public long countHql(String hql) {
		return personUseStatDao.countHql(hql);
	}
}
