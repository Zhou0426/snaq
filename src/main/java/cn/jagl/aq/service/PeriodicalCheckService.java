package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.PeriodicalCheck;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:05:19
 */
public interface PeriodicalCheckService {
	//���
	void save(PeriodicalCheck periodicalCheck);
	//����
	void update(PeriodicalCheck periodicalCheck);
	//����ɾ��
	void deleteByIds(String ids);
	//��Ż�ȡ
	PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn);
	//�ܼ�¼��
	long count(String hql);
	//��ҳ��ѯ
	List<PeriodicalCheck> query(String hql,int page,int rows);
	//getById
	PeriodicalCheck getById(int id);
}
