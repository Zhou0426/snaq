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
 * @date 2016��7��8������3:40:50
 */
public interface UnsafeConditionDao extends BaseDao<UnsafeCondition>{
	//������ѯ
	public UnsafeCondition getByInconformityItemSn(String inconformityItemSn);
	//��¼����ѯ
	public long count(String sql);
	//hql��¼����ѯ
	public long countHql(String hql);
	//getById
	public UnsafeCondition getById(int id);
	//����ɾ��
	public void deleteByIds(String ids);
	//��ҳ��ѯ
	public List<UnsafeCondition> query(String sql,int page,int rows);
	//��ѯ����������Ŀ
	long query(String departmentSn,String departmentTypeSn, String specialitySnIndex, String inconformityLevelSnIndex,
			String begin, String end);
	//����sql����ѯ�м��
	List<?> getBySql(String sql);
	/**
	 * ��ʱ�����ַ��յȼ�
	 * ÿ������Ӽ���һ��
	 */
	void computeNowRiskLevel();
	/**
	 * ������ѯ,����Ԥ����ʾ����
	 */
	Map<String, Object> showData(String checkDeptSn,String departmentSn,String str,String indexSn,String standardSn,String inconformityLevel,String qSpecialitySn,
			String riskLevel,String checkType,String checkerFrom,String inconformityItemNature,String correctPrincipal,
			String hasCorrectConfirmed,String hasReviewed,String hasCorrectFinished,String timeData,String checkers,
			String pag,int page,int rows,Timestamp beginTime,Timestamp endTime, boolean checked);
	/**
	 * ��ѯ�ҵ�����
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> myUnsafeCondition(String personId, int page, int rows);
	/**
	 * ����hql��ѯ����
	 * @param hql
	 * @return
	 */
	public List<UnsafeCondition> findByHql(String hql);
	/**
	 * ��ѯ����ȫ��
	 * @param hql
	 * @return
	 */
	public List<InconformityItem> findInconformityItemByHql(String hql);
	/**
	 * ��ҳ��ѯ���в���ȫ��
	 * @param hql
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<InconformityItem> findInconformityItemByPage(String hql, int page, int rows);
	
}
