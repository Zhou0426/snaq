package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SpecialCheckDao;
import cn.jagl.aq.domain.SpecialCheck;
import cn.jagl.aq.service.SpecialCheckService;
/**
 * @author mahui
 *
 * @date 2016年7月8日下午5:17:07
 */
@Service("specialCheckService")
public class SpecialCheckServiceImpl implements SpecialCheckService {
	@Resource(name="specialCheckDao")
	private SpecialCheckDao specialCheckDao;

	//添加
	@Override
	public void save(SpecialCheck specialCheck) {
		specialCheckDao.save(specialCheck);
	}

	//更新
	@Override
	public void update(SpecialCheck specialCheck) {
		specialCheckDao.update(specialCheck);
	}

	//多条删除
	@Override
	public void deleteByIds(String ids) {
		specialCheckDao.deleteByIds(ids);
	}

	//编号获取
	@Override
	public SpecialCheck getBySpecialCheckSn(String specialCheckSn) {
		return (SpecialCheck)specialCheckDao.getBySpecialCheckSn(specialCheckSn);
	}

	//记录数
	@Override
	public long count(String hql) {
		return (Long)specialCheckDao.count(hql);
	}

	//分页查询
	@Override
	public List<SpecialCheck> query(String hql, int page, int rows) {
		return specialCheckDao.findByPage(hql, page, rows);
	}

	//getById
	@Override
	public SpecialCheck getById(int id) {
		return (SpecialCheck)specialCheckDao.get(SpecialCheck.class, id);
	}
}
