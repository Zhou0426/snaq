package cn.jagl.aq.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeAct;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:40:50
 */
public interface UnsafeActDao extends BaseDao<UnsafeAct>{
	//��ѯ����ȫ��Ϊ��Ŀ
	public long query(String departmentSn,String departmentTypeSn, String specialitySnIndex, String inconformityLevelSnIndex,
			String begin, String end);
	
	public long countByHql(String hql);

	public void deleteByIds(String ids);

	public UnsafeAct getById(int id);
	
	public UnsafeAct getBySn(String unsafeActSn);

	/**
	 * ��ѯ����ȫ��Ϊ
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
	public Map<String, Object> showData(String checkDeptSn, String departmentSn, String str, String specialitySn, String unsafeActStandardSn,
			String checkerFromSn, String checkTypeSn, String unsafeActLevelSn, String timeData, Timestamp beginTime,
			Timestamp endTime, String checkers, int page, int rows);

	/**
	 * ��ѯ�ҵĲ���ȫ��Ϊ
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> queryMyUnsafeAct(String personId, Integer page, Integer rows);
	/**
	 * ����hql��ѯ��������
	 * @param hql
	 * @return
	 */
	public List<UnsafeAct> findByHql(String hql);

}
