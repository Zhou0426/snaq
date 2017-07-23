package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.PeriodicalCheck;

/*
 * ���
 * 2016/7/8
 */
public interface PeriodicalCheckDao extends BaseDao<PeriodicalCheck>{
	//ͨ����Ż�ȡ
	public PeriodicalCheck getByPeriodicalCheckSn(String periodicalCheckSn);
	//����ɾ��(����)
	public void deleteByIds(String ids);
	//��ȡ��¼��
	public long count(String hql);
}
