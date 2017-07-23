package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.CheckTableCheckerDao;
import cn.jagl.aq.domain.CheckTableChecker;
import cn.jagl.aq.service.CheckTableCheckerService;

/**
 * @author mahui
 * @method 
 * @date 2016年8月9日上午8:59:14
 */
@Service("checkTableCheckerService")
public class CheckTableCheckerServiceImpl implements CheckTableCheckerService {
	@Resource(name="checkTableCheckerDao")
	private CheckTableCheckerDao checkTableCheckerDao;
	@Override
	public void save(CheckTableChecker checkTableChecker) {
		checkTableCheckerDao.save(checkTableChecker);
	}
	@Override
	public void deleteByMany(String personId, String checkTableSn) {
		checkTableCheckerDao.deleteByMany(personId, checkTableSn);
	}
	@Override
	public void update(CheckTableChecker checkTableChecker) {
		checkTableCheckerDao.update(checkTableChecker);
	}
	@Override
	public CheckTableChecker getById(String checkTableSn,String personId) {
		return checkTableCheckerDao.getById(checkTableSn,personId);
	}

}
