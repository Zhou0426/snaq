package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.StandardDao;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import cn.jagl.aq.service.StandardService;
@Service("standardService")
public class StandardServiceImpl implements StandardService {
	@Resource(name="standardDao")
	private StandardDao standardDao;

	@Override
	public int addStandard(Standard standard) {
		return (Integer) standardDao.save(standard);
	}

	@Override
	public List<Standard> getAllStandards() {
		return standardDao.findAll(Standard.class);
	}

	@Override
	public void deleteStandard(int id) {
		standardDao.delete(Standard.class,id);

	}

	@Override
	public Standard getByStandardSn(String standardSn) {
		return standardDao.getByStandardtSn(standardSn);
	}

	@Override
	public long count(String hql) {
		return standardDao.count(hql);
	}

	@Override
	public List<Standard> query(String hql,int page, int rows) {
		return standardDao.query(hql,page, rows);
	}

	@Override
	public void update(Standard standard) {
		// 更新
		standardDao.update(standard);
		
	}

	@Override
	public Standard getById(int id) {
		return (Standard)standardDao.get(Standard.class, id);
	}

	@Override
	public void hidden(String ids) {
		standardDao.hidden(ids);
	}
	
	//获取所有审核指南或指标
	@Override
	public List<Standard> queryByStandardType(StandardType standardType,String departmentTypeSn) {
		return standardDao.queryByStandardType(standardType,departmentTypeSn);
	}

	@Override
	public List<Standard> getStandardByDepartmentTypeSns(List<String> departmentTypeSns) {
		return standardDao.getStandardByDepartmentTypeSns(departmentTypeSns);
	}



}
