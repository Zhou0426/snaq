package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.CheckTaskAppraisals;

public interface CheckTaskAppraisalsService {
	/**
	 * 添加实体
	 */
	void add(CheckTaskAppraisals checkTaskAppraisals);
	/**
	 * 更新实体
	 */
	void update(CheckTaskAppraisals checkTaskAppraisals);
	/**
	 * 分页查询
	 */
	List<CheckTaskAppraisals> findByPage(String hql , int page, int rows);
	/**
	 * 根据hql语句查询总数
	 */
	long countHql(String hql) ;
	/**
	 * 根据时间、检查人编号和部门编号查找数据
	 * @param checkerId
	 * @param departmentSn
	 * @param times--"2016-08-31"
	 * @return 实体
	 */
	CheckTaskAppraisals getBycheckerSn(String checkerId,String departmentSn,String times);
	/**
	 * 定时执行检查任务个数
	 */
	void checkTask();
	/**
	 * 根据年份和部门编号获取整年的数据
	 */
	List<CheckTaskAppraisals> getByYear(String times,String departmentSn);
	/**
	 * 根据hql语句查询集合
	 * @param hql
	 * @return list
	 */
	List<CheckTaskAppraisals> getByHql(String hql) ;
	/**
	 * 处室考核--季度得分
	 * 根据起止时间和部门编号获取该时间段内该部门扣分总分
	 */
	float countStandardIndexScore(String departmentSn, String ksTime, String jsTime);
	/**
	 * 删除记录
	 * @param id
	 */
	void delete(int id);
}
