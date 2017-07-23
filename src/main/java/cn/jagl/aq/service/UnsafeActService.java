package cn.jagl.aq.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.domain.UnsafeAct;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:05
 */
public interface UnsafeActService {
	//查询数目
	long query(String departmentSn,String departmentTypeSn,String specialitySnIndex, String inconformityLevelSnIndex,
				String begin, String end);
	//分页获取数据
	List<UnsafeAct> query(String sql,int page,int rows);
	//根据hql语句查询总数
	long countByHql(String hql);
	//分页查询
	List<UnsafeAct> findByPage(String hql, int page, int rows);
	//添加实体
	int save(UnsafeAct unsafeAct);
	//删除实体
	void deleteByIds(String ids);
	//根据ID获取实体
	UnsafeAct getById(int id);
	//更新实体
	void update(UnsafeAct unsafeAct); 
	//getBySn
	UnsafeAct getBySn(String unsafeActSn);
	/**
	 * 查询不安全行为
	 * @param departmentSn
	 * @param str
	 * @param specialitySn
	 * @param unsafeActStandardSn
	 * @param checkerFromSn
	 * @param checkTypeSn
	 * @param unsafeActLevelSn
	 * @param timeData
	 * @param beginTime
	 * @param endTime
	 * @param checkers
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> showData(String checkDeptSn, String departmentSn, String str, String specialitySn, String unsafeActStandardSn,
			String checkerFromSn, String checkTypeSn, String unsafeActLevelSn, String timeData, Timestamp beginTime,
			Timestamp endTime, String checkers, int page, int rows);
	/**
	 * 查询我的不安全行为
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> queryMyUnsafeAct(String personId, Integer page, Integer rows);
		
}
