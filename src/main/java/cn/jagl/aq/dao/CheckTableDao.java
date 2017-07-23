package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CheckTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��8��2������3:09:09
 */
public interface CheckTableDao extends BaseDao<CheckTable> {
	//��ѯ�ҵļ���
	public JSONObject query(String personId,int page,int rows);
	//�鿴��������
	public JSONObject queryDetails(int id,String periodicalCheckSn,String specialCheckSn,String auditSn,int page,int rows);
	//�ҵļ����¼��
	public long count(String personId);
	//��ѯ����
	public JSONArray queryJoinCheck(String specialCheckSn, String periodicalCheckSn,String auditSn);
	/**
	 * ��ѯδȷ�ϵļ������
	 * @param ����ɭ
	 * @return
	 */
	public long countExceptTrue(String personId);
}
