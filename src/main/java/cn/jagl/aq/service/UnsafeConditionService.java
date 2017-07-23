package cn.jagl.aq.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.UnsafeCondition;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:05
 */
public interface UnsafeConditionService {
	//添加
	void save(UnsafeCondition unsafeCondition);
	//更新
	void update(UnsafeCondition unsafeCondition);
	//多条删除
	void deleteByIds(String ids);
	//记录数
	long count(String sql);
	//hql查询总数
	long countHql(String hql);
	//分页查询
	List<UnsafeCondition> query(String hql,int page,int rows);
	//getById
	UnsafeCondition getById(int id);
	//通过编号获取
	UnsafeCondition getByInconformityItemSn(String inconformityItemSn);		
	//查询不符合项数目
	long query(String departmentSn,String departmentTypeSn,String specialitySnIndex, String inconformityLevelSnIndex,
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
	 * 查询所有不安全项(page，rows为0则查询所有)
	 * @param hql
	 * @param i
	 * @param j
	 * @return
	 */
	List<InconformityItem> queryInconformityItem(String hql, int i, int j);
}
