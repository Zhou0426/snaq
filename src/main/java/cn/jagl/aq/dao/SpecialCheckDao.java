package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SpecialCheck;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午5:15:05
 */
public interface SpecialCheckDao extends BaseDao<SpecialCheck> {
	//编号查询
	public SpecialCheck getBySpecialCheckSn(String specialCheckSn);
	//记录数
	public long count(String hql);
	//多条删除
	public void deleteByIds(String ids);

}
