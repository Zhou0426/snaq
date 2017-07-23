package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CheckTaskAppraisals;

public interface CheckTaskAppraisalsDao extends BaseDao<CheckTaskAppraisals> {

	/**
	 * ����hql����ѯ����
	 * @param hql
	 * @return count
	 */
	public long countHql(String hql) ;
	/**
	 * ����hql����ѯ����
	 * @param hql
	 * @return count
	 */
	public List<CheckTaskAppraisals> getByHql(String hql) ;
	/**
	 * ����ʱ�䡢����˱�źͲ��ű�Ų�������
	 * @param checkerId
	 * @param departmentSn
	 * @param times
	 * @return ʵ��
	 */
	public CheckTaskAppraisals getBycheckerSn(String checkerId,String departmentSn,String times);
	/**
	 * ��ʱִ�м���������
	 */
	public void checkTask() ;
	/**
	 * ���ҿ���
	 * ������ݺͲ��ű�Ż�ȡ���������
	 */
	public List<CheckTaskAppraisals> getByYear(String times, String departmentSn);
	/**
	 * ���ҿ���--���ȵ÷�
	 * ������ֹʱ��Ͳ��ű�Ż�ȡ��ʱ����ڸò��ſ۷��ܷ�
	 */
	public float countStandardIndexScore(String departmentSn, String ksTime, String jsTime);
}
