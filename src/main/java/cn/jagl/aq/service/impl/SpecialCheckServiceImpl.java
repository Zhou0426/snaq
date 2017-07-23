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
 * @date 2016��7��8������5:17:07
 */
@Service("specialCheckService")
public class SpecialCheckServiceImpl implements SpecialCheckService {
	@Resource(name="specialCheckDao")
	private SpecialCheckDao specialCheckDao;

	//���
	@Override
	public void save(SpecialCheck specialCheck) {
		specialCheckDao.save(specialCheck);
	}

	//����
	@Override
	public void update(SpecialCheck specialCheck) {
		specialCheckDao.update(specialCheck);
	}

	//����ɾ��
	@Override
	public void deleteByIds(String ids) {
		specialCheckDao.deleteByIds(ids);
	}

	//��Ż�ȡ
	@Override
	public SpecialCheck getBySpecialCheckSn(String specialCheckSn) {
		return (SpecialCheck)specialCheckDao.getBySpecialCheckSn(specialCheckSn);
	}

	//��¼��
	@Override
	public long count(String hql) {
		return (Long)specialCheckDao.count(hql);
	}

	//��ҳ��ѯ
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
