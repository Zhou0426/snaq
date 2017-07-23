package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONObject;

public interface LatentHazardAttachmentDao extends BaseDao<LatentHazardAttachment> {
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

}
