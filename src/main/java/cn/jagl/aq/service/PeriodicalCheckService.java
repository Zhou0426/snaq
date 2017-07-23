package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.PeriodicalCheck;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:19
 */
public interface PeriodicalCheckService {
	//添加
	void save(PeriodicalCheck periodicalCheck);
	//更新
	void update(PeriodicalCheck periodicalCheck);
	//多条删除
	void deleteByIds(String ids);
	//编号获取
	PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn);
	//总记录数
	long count(String hql);
	//分页查询
	List<PeriodicalCheck> query(String hql,int page,int rows);
	//getById
	PeriodicalCheck getById(int id);
}
