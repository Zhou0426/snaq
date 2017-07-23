package cn.jagl.aq.service;

import cn.jagl.aq.domain.CheckTableChecker;

/**
 * @author mahui
 * @method 
 * @date 2016��8��9������8:57:55
 */
public interface CheckTableCheckerService {
	public void save(CheckTableChecker checkTableChecker);
	public void deleteByMany(String personId, String checkTableSn);
	public void update(CheckTableChecker checkTableChecker);
	public CheckTableChecker getById(String checkTableSn,String personId);
}
