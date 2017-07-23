package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CertificationType;
import net.sf.json.JSONObject;

public interface CertificationTypeDao extends BaseDao<CertificationType>{

	//hql��ѯ����
	public long countHql(String hql) ;
	//���ݱ�Ÿ���ʵ��
	public CertificationType getByCertificationTypeSn(String certificationTypeSn);
	//����id��ȡʵ��
	public CertificationType getById(int id);
	public CertificationType getByCertificationTypeName(String stringValue);
	/**
	 * ���ݲ��ű�Ų�ѯ�ò���֤�����ͳ����˵�����
	 * @param departmentSn
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject queryStatistics(String departmentSn, int page, int rows);
}
