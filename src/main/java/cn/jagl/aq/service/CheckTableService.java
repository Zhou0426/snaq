package cn.jagl.aq.service;

import cn.jagl.aq.domain.CheckTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��8��2������3:12:59
 */
public interface CheckTableService {
	//���
	public void save(CheckTable checkTable);
	//ɾ��
	public void deleteById(int id);
	//�޸�
	public void update(CheckTable checkTable);
	//��ѯ
	public JSONArray queryJoinCheck(String specialCheckSn,String periodicalCheckSn,String auditSn);
	//getById
	public CheckTable getById(int id);
	//��ѯ�ҵļ���
	public JSONObject query(String personId,int page,int rows);
	//�鿴��������
	public JSONObject queryDetails(int id,String periodicalCheckSn,String specialCheckSn,String auditSn,int page,int rows);
	//��ѯ�ҵļ���
	public long count(String personId);
	/**
	 * ��ѯδȷ�ϵļ������
	 * @param ����ɭ
	 * @return
	 */
	public long countExceptTrue(String personId);
}
