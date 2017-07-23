package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PeriodicalCheck;

/*
 * 马辉
 * 2016/7/8
 */
public interface PeriodicalCheckDao extends BaseDao<PeriodicalCheck>{
	//通过编号获取
	public PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn);
	//多条删除(隐藏)
	public void deleteByIds(String ids);
	//获取记录数
	public long count(String hql);
}
