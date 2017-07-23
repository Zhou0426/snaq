package cn.jagl.aq.service;
import java.util.List;

import cn.jagl.aq.domain.CertificationType;
import net.sf.json.JSONObject;

public interface CertificationTypeService {	
	//hql��ѯ����
	public long countHql(String hql) ;
	//��ҳ��ѯ
	List<CertificationType> findByPage(String hql , int pageNo, int pageSize);
	//���ݱ�Ÿ���ʵ��
	public CertificationType getByCertificationTypeSn(String certificationTypeSn);
	//����δ���¼�ʵ��
	void add(CertificationType certificationType);
	//ɾ��δ���¼�ʵ��
	void delete(CertificationType certificationType);
	//����δ���¼�ʵ��
	void update(CertificationType certificationType);
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
