package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CheckTableChecker;

/**
 * @author mahui
 * @method 
 * @date 2016��8��9������8:55:22
 */
public interface CheckTableCheckerDao extends BaseDao<CheckTableChecker> {
	public void deleteByMany(String personId,String checkTableSn);
	public CheckTableChecker getById(String checkTableSn ,String personId);
}
