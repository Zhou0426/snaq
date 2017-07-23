package cn.jagl.aq.service;

import cn.jagl.aq.domain.SystemAuditAttachment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ���
 * @since JDK1.8
 * @history 2017��4��18������6:11:47 ��� �½�
 */
public interface SystemAuditAttachmentService {
	//������ϵ��˱�Ų�ѯδɾ������
	public long count(String auditSn);
	
	//ɾ��
	public JSONObject delete(int id);
	
	//���
	public void save(SystemAuditAttachment systemAuditAttachment);
	
	//��ѯ����
	public JSONArray query(String auditSn);	
}
