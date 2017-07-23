package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PeriodicalCheckDao;
import cn.jagl.aq.domain.PeriodicalCheck;
import cn.jagl.aq.service.PeriodicalCheckService;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:56
 */
@Service("periodicalCheckService")
public class PeriodicalCheckServiceImpl implements PeriodicalCheckService {
	@Resource(name="periodicalCheckDao")
	private PeriodicalCheckDao periodicalCheckDao;

	//添加
	@Override
	public void save(PeriodicalCheck periodicalCheck) {
		periodicalCheckDao.save(periodicalCheck);
	}

	//更新
	@Override
	public void update(PeriodicalCheck periodicalCheck) {
		periodicalCheckDao.update(periodicalCheck);
	}

	//多条删除
	@Override
	public void deleteByIds(String ids) {
		periodicalCheckDao.deleteByIds(ids);		
	}

	
	//编号获取
	@Override
	public PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn) {
		return (PeriodicalCheck)periodicalCheckDao.getByPeriodicalCheckSn(periodicalCheckSn);
	}

	//记录数
	@Override
	public long count(String hql) {
		return (Long)periodicalCheckDao.count(hql);
	}

	//分页查询
	@Override
	public List<PeriodicalCheck> query(String hql, int page, int rows) {
		return periodicalCheckDao.findByPage(hql, page, rows);
	}

	//getById
	@Override
	public PeriodicalCheck getById(int id) {
		return (PeriodicalCheck)periodicalCheckDao.get(PeriodicalCheck.class, id);
	}

}
