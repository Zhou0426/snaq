package cn.jagl.aq.dao;

import java.io.InputStream;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAudit;

/**
 * @author mahui
 * @method 
 * @date 2016��7��25������12:46:50
 */
public interface SystemAuditDao extends BaseDao<SystemAudit> {
	//��ȡ��¼��
	long count(String hql);
	//����ɾ��
	void deleteByIds(String ids);
	//���ݲ��ű�Ż�ȡ������˵÷�
	SystemAudit queryScore(String departmentSn,int quarter,String year);
	//getBySn;
	SystemAudit getBySn(String auditSn);
	//��ȡ����ۿ۵ķ���
	int countNotScore(String auditSn);
	//�ж��Ƿ�ȫѡ
	boolean isChecked(String auditSn,String indexSn);
	//����ָ���ź���˱���ж��Ƿ�Ӧ�ô��
	boolean isScore(String auditSn,String indexSn);
	//��ȡĳ��ָ������۷ֵ��ܷ�
	int countNotScoreBySn(String auditSn,String indexSn);
	//���ݲ��ű�Ż�ȡ������˵÷�
	SystemAudit queryShScore(String departmentSn,String year);
	//�����˴�ֱ�
	InputStream export(String standardSn,String auditSn);
	//�������÷����
	InputStream exportSummary(String year,String month,int type,String standardSn,String departmentTypeSn);
	/**
	 * @method ��ϵ�����������
	 * @author mahui
	 * @return String
	 */
	InputStream exportUnsafeCondition(String auditSn);
}
