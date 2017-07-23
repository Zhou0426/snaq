package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CheckTaskAppraisals;

public interface CheckTaskAppraisalsDao extends BaseDao<CheckTaskAppraisals> {

	/**
	 * 根据hql语句查询总数
	 * @param hql
	 * @return count
	 */
	public long countHql(String hql) ;
	/**
	 * 根据hql语句查询集合
	 * @param hql
	 * @return count
	 */
	public List<CheckTaskAppraisals> getByHql(String hql) ;
	/**
	 * 根据时间、检查人编号和部门编号查找数据
	 * @param checkerId
	 * @param departmentSn
	 * @param times
	 * @return 实体
	 */
	public CheckTaskAppraisals getBycheckerSn(String checkerId,String departmentSn,String times);
	/**
	 * 定时执行检查任务个数
	 */
	public void checkTask() ;
	/**
	 * 处室考核
	 * 根据年份和部门编号获取整年的数据
	 */
	public List<CheckTaskAppraisals> getByYear(String times, String departmentSn);
	/**
	 * 处室考核--季度得分
	 * 根据起止时间和部门编号获取该时间段内该部门扣分总分
	 */
	public float countStandardIndexScore(String departmentSn, String ksTime, String jsTime);
}
