package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Certification;

public interface CertificationDao extends BaseDao<Certification>{


	//hql��ѯ����
	public long countHql(String hql) ;
	//�����¹ʱ�Ÿ���ʵ��
	public Certification getByCertificationSn(String certificationSn);	
	//����id��ȡʵ��
	public Certification getById(int id);
	/**
	 * ����֤����ź�֤�����ͱ�Ų�ѯʵ��
	 * @param certificationSn
	 * @param certificationTypeSn
	 * @return
	 */
	public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn);
	/**
	 * ����ɾ��
	 * @param ids
	 */
	public void deleteByIds(String ids);
}
