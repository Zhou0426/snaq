package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SystemAuditAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *	����
 * @author ���
 * @since JDK1.8
 * @history 2017��4��18������6:10:19 ��� �½�
 */
public interface SystemAuditAttachmentDao extends BaseDao<SystemAuditAttachment> {
	//������ϵ��˱�Ų�ѯδɾ������
	public long count(String auditSn);
	
	//ɾ��
	public JSONObject delete(int id);
	
	//��ѯ����
	public JSONArray query(String auditSn);
}
