package cn.jagl.aq.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.UnsafeCondition;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:05:05
 */
public interface UnsafeConditionService {
	//���
	void save(UnsafeCondition unsafeCondition);
	//����
	void update(UnsafeCondition unsafeCondition);
	//����ɾ��
	void deleteByIds(String ids);
	//��¼��
	long count(String sql);
	//hql��ѯ����
	long countHql(String hql);
	//��ҳ��ѯ
	List<UnsafeCondition> query(String hql,int page,int rows);
	//getById
	UnsafeCondition getById(int id);
	//ͨ����Ż�ȡ
	UnsafeCondition getByInconformityItemSn(String inconformityItemSn);		
	//��ѯ����������Ŀ
	long query(String departmentSn,String departmentTypeSn,String specialitySnIndex, String inconformityLevelSnIndex,
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
	 * ��ѯ���в���ȫ��(page��rowsΪ0���ѯ����)
	 * @param hql
	 * @param i
	 * @param j
	 * @return
	 */
	List<InconformityItem> queryInconformityItem(String hql, int i, int j);
}
