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
 * @date 2016��7��8������3:05:56
 */
@Service("periodicalCheckService")
public class PeriodicalCheckServiceImpl implements PeriodicalCheckService {
	@Resource(name="periodicalCheckDao")
	private PeriodicalCheckDao periodicalCheckDao;

	//���
	@Override
	public void save(PeriodicalCheck periodicalCheck) {
		periodicalCheckDao.save(periodicalCheck);
	}

	//����
	@Override
	public void update(PeriodicalCheck periodicalCheck) {
		periodicalCheckDao.update(periodicalCheck);
	}

	//����ɾ��
	@Override
	public void deleteByIds(String ids) {
		periodicalCheckDao.deleteByIds(ids);		
	}

	
	//��Ż�ȡ
	@Override
	public PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn) {
		return (PeriodicalCheck)periodicalCheckDao.getByPeriodicalCheckSn(periodicalCheckSn);
	}

	//��¼��
	@Override
	public long count(String hql) {
		return (Long)periodicalCheckDao.count(hql);
	}

	//��ҳ��ѯ
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
