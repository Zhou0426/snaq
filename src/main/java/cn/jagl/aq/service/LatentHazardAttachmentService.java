package cn.jagl.aq.service;

import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONObject;

public interface LatentHazardAttachmentService {
	
	/**
	 * ��Ӽ�¼
	 * @param latentHazardAttachment
	 */
	void add(LatentHazardAttachment latentHazardAttachment);
	/**
	 * �����ش�������Ų�ѯ����
	 * @param latentHazardSn
	 * @return
	 */
	JSONObject queryAttachmentByLatentHazardSn(String latentHazardSn);
	/**
	 * ����id�߼�ɾ������
	 * @param id
	 */
	void deleteById(int id);
	/**
	 * ����id��ѯ����
	 * @param id
	 * @return
	 */
	LatentHazardAttachment queryById(int id);

}
