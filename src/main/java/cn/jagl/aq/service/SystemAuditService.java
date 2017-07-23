package cn.jagl.aq.service;

import java.io.InputStream;
import java.util.List;

import cn.jagl.aq.domain.SystemAudit;

/**
 * @author mahui
 * @method 
 * @date 2016��7��25������12:53:10
 */
public interface SystemAuditService {
	//���
	public void save(SystemAudit systemAudit);
	//�޸�
	public void update(SystemAudit systemAudit);
	//getBySn
	public SystemAudit getBySn(String auditSn);
	//��ѯ
	public List<SystemAudit> query(String hql,int page,int rows);
	//��ѯ����
	public long count(String hql);
	//����ɾ��
	public void deleteByIds(String ids);
	//getById
	public SystemAudit getById(int id);
	//���ݲ��ű�Ż�ȡ������˵÷�
	public SystemAudit queryScore(String departmentSn,int quarter,String year);
	//��ȡ����ۿ۵ķ���
	public int countNotScore(String auditSn);
	//�ж��Ƿ�ȫѡ
	public boolean isChecked(String auditSn,String indexSn);
	//����ָ���ź���˱���ж��Ƿ�Ӧ�ô��
	boolean isScore(String auditSn,String indexSn);
	//��ȡĳ��ָ������۷ֵ��ܷ�
	public int countNotScoreBySn(String auditSn,String indexSn);
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
	
	//���ݲ��ű�Ż�ȡ������˵÷�
	SystemAudit queryShScore(String departmentSn,String year);
}
