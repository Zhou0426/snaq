package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.InteriorWorkAttachment;
import net.sf.json.JSONObject;

public interface InteriorWorkAttachmentDao extends BaseDao<InteriorWorkAttachment> {

	/**
	 * ���ݱ�ŷ�ҳ��ѯ��������
	 * @param interiorWorkSn
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject queryByInteriorWorkSn(String interiorWorkSn, Integer page, Integer rows);

}
