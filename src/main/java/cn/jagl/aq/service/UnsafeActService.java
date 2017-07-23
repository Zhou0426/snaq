package cn.jagl.aq.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.jagl.aq.domain.UnsafeAct;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:05:05
 */
public interface UnsafeActService {
	//��ѯ��Ŀ
	long query(String departmentSn,String departmentTypeSn,String specialitySnIndex, String inconformityLevelSnIndex,
				String begin, String end);
	//��ҳ��ȡ����
	List<UnsafeAct> query(String sql,int page,int rows);
	//����hql����ѯ����
	long countByHql(String hql);
	//��ҳ��ѯ
	List<UnsafeAct> findByPage(String hql, int page, int rows);
	//���ʵ��
	int save(UnsafeAct unsafeAct);
	//ɾ��ʵ��
	void deleteByIds(String ids);
	//����ID��ȡʵ��
	UnsafeAct getById(int id);
	//����ʵ��
	void update(UnsafeAct unsafeAct); 
	//getBySn
	UnsafeAct getBySn(String unsafeActSn);
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
	Map<String, Object> showData(String checkDeptSn, String departmentSn, String str, String specialitySn, String unsafeActStandardSn,
			String checkerFromSn, String checkTypeSn, String unsafeActLevelSn, String timeData, Timestamp beginTime,
			Timestamp endTime, String checkers, int page, int rows);
	/**
	 * ��ѯ�ҵĲ���ȫ��Ϊ
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> queryMyUnsafeAct(String personId, Integer page, Integer rows);
		
}
