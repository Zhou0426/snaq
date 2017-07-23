package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.CheckTaskAppraisals;

public interface CheckTaskAppraisalsService {
	/**
	 * ���ʵ��
	 */
	void add(CheckTaskAppraisals checkTaskAppraisals);
	/**
	 * ����ʵ��
	 */
	void update(CheckTaskAppraisals checkTaskAppraisals);
	/**
	 * ��ҳ��ѯ
	 */
	List<CheckTaskAppraisals> findByPage(String hql , int page, int rows);
	/**
	 * ����hql����ѯ����
	 */
	long countHql(String hql) ;
	/**
	 * ����ʱ�䡢����˱�źͲ��ű�Ų�������
	 * @param checkerId
	 * @param departmentSn
	 * @param times--"2016-08-31"
	 * @return ʵ��
	 */
	CheckTaskAppraisals getBycheckerSn(String checkerId,String departmentSn,String times);
	/**
	 * ��ʱִ�м���������
	 */
	void checkTask();
	/**
	 * ������ݺͲ��ű�Ż�ȡ���������
	 */
	List<CheckTaskAppraisals> getByYear(String times,String departmentSn);
	/**
	 * ����hql����ѯ����
	 * @param hql
	 * @return list
	 */
	List<CheckTaskAppraisals> getByHql(String hql) ;
	/**
	 * ���ҿ���--���ȵ÷�
	 * ������ֹʱ��Ͳ��ű�Ż�ȡ��ʱ����ڸò��ſ۷��ܷ�
	 */
	float countStandardIndexScore(String departmentSn, String ksTime, String jsTime);
	/**
	 * ɾ����¼
	 * @param id
	 */
	void delete(int id);
}
