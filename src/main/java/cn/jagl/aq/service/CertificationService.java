package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.Certification;

public interface CertificationService {
	    //hql��ѯ����
		public long countHql(String hql) ;
		//��ҳ��ѯ
		List<Certification> findByPage(String hql , int pageNo, int pageSize);
		//����֤����Ų�ѯʵ��
		public Certification getByCertificationSn(String certificationSn);	
		/**
		 * ����֤����ź�֤�����ͱ�Ų�ѯʵ��
		 * @param certificationSn
		 * @param certificationTypeSn
		 * @return
		 */
		public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn);	
		//����δ���¼�ʵ��
		void add(Certification certification);
		//ɾ��δ���¼�ʵ��
		void delete(Certification certification);
		//����δ���¼�ʵ��
		void update(Certification certification);
		//����id��ȡʵ��
		public Certification getById(int id);
		/**
		 * ����ɾ��
		 * @param ids
		 */
		void deleteByIds(String ids);
}
