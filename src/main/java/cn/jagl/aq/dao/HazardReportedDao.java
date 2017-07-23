package cn.jagl.aq.dao;

import java.util.HashMap;
import java.util.Map;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.HazardReported;

public interface HazardReportedDao extends BaseDao<HazardReported> {

	/**
	 * ������Ա�ͽ�ɫ��ȡ��������
	 */
	public Map<String, Object> queryAllData(String personId,HashMap<String, String> roles,int page,int rows);
	/**
	 * ����id�߼�ɾ��
	 */
	public void deleteData(int id);
	/**
	 * ʧ�ش���ͳ��չʾ
	 */
	public Map<String, Object> queryUnsafeCondition(String departmentSn, String hazardSn,int page,int rows);
	/**
	 * ʧ�ش���ͳ��չʾ����
	 */
	public Map<String, Object> queryCount(String departmentSn, String departmentTypeSn, int page, int rows);
	/**
	 * ����id����
	 */
	public HazardReported getById(int id);
	/**
	 * ����Σ��Դ�ϱ���Ų��Ҷ�Ӧ��׼
	 * @param reportSn
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> showStandardIndex(String reportSn, int page, int rows);
}
