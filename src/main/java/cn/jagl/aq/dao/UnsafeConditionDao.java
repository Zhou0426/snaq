package cn.jagl.aq.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.UnsafeCondition;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:40:50
 */
public interface UnsafeConditionDao extends BaseDao<UnsafeCondition>{
	//单个查询
	public UnsafeCondition getByInconformityItemSn(String inconformityItemSn);
	//记录数查询
	public long count(String sql);
	//hql记录数查询
	public long countHql(String hql);
	//getById
	public UnsafeCondition getById(int id);
	//多条删除
	public void deleteByIds(String ids);
	//分页查询
	public List<UnsafeCondition> query(String sql,int page,int rows);
	//查询不符合项数目
	long query(String departmentSn,String departmentTypeSn, String specialitySnIndex, String inconformityLevelSnIndex,
			String begin, String end);
	//根据sql语句查询中间表
	List<?> getBySql(String sql);
	/**
	 * 定时计算现风险等级
	 * 每隔五分钟计算一次
	 */
	void computeNowRiskLevel();
	/**
	 * 隐患查询,隐患预警显示数据
	 */
	Map<String, Object> showData(String checkDeptSn,String departmentSn,String str,String indexSn,String standardSn,String inconformityLevel,String qSpecialitySn,
			String riskLevel,String checkType,String checkerFrom,String inconformityItemNature,String correctPrincipal,
			String hasCorrectConfirmed,String hasReviewed,String hasCorrectFinished,String timeData,String checkers,
			String pag,int page,int rows,Timestamp beginTime,Timestamp endTime, boolean checked);
	/**
	 * 查询我的隐患
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> myUnsafeCondition(String personId, int page, int rows);
	/**
	 * 根据hql查询数据
	 * @param hql
	 * @return
	 */
	public List<UnsafeCondition> findByHql(String hql);
	/**
	 * 查询不安全项
	 * @param hql
	 * @return
	 */
	public List<InconformityItem> findInconformityItemByHql(String hql);
	/**
	 * 分页查询所有不安全项
	 * @param hql
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<InconformityItem> findInconformityItemByPage(String hql, int page, int rows);
	
}
