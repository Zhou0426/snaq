package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.CheckTaskAppraisalsDao;
import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.service.CheckTaskAppraisalsService;

@Service("checkTaskAppraisalsService")
public class CheckTaskAppraisalsServiceImpl implements CheckTaskAppraisalsService {

	@Resource(name="checkTaskAppraisalsDao")
	private CheckTaskAppraisalsDao checkTaskAppraisalsDao;
	@Override
	public void add(CheckTaskAppraisals checkTaskAppraisals) {
		checkTaskAppraisalsDao.save(checkTaskAppraisals);
	}

	@Override
	public void update(CheckTaskAppraisals checkTaskAppraisals) {
		checkTaskAppraisalsDao.update(checkTaskAppraisals);
	}

	@Override
	public List<CheckTaskAppraisals> findByPage(String hql, int page, int rows) {
		return checkTaskAppraisalsDao.findByPage(hql, page, rows);
	}

	@Override
	public long countHql(String hql) {
		return checkTaskAppraisalsDao.countHql(hql);
	}

	@Override
	public CheckTaskAppraisals getBycheckerSn(String checkerId, String departmentSn,String times) {
		return checkTaskAppraisalsDao.getBycheckerSn(checkerId, departmentSn,times);
	}

	@Override
	//@Scheduled(cron="0 58 18 * * ?")   //每天两点执行
	public void checkTask() {
		checkTaskAppraisalsDao.checkTask();
	}

	@Override
	public List<CheckTaskAppraisals> getByYear(String times, String departmentSn) {
		return checkTaskAppraisalsDao.getByYear(times,departmentSn);
	}

	@Override
	public List<CheckTaskAppraisals> getByHql(String hql) {
		return checkTaskAppraisalsDao.getByHql(hql);
	}

	@Override
	public float countStandardIndexScore(String departmentSn, String ksTime, String jsTime) {
		return checkTaskAppraisalsDao.countStandardIndexScore(departmentSn, ksTime, jsTime);
	}

	@Override
	public void delete(int id) {
		checkTaskAppraisalsDao.delete(CheckTaskAppraisals.class, id);
	}

}
