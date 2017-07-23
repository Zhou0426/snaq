package cn.jagl.aq.service;

import cn.jagl.aq.domain.InteriorWorkAttachment;

public interface InteriorWorkAttachmentService {

	/**
	 * ���ݱ�ŷ�ҳ��ѯ��������
	 * @param interiorWorkSn
	 * @param page
	 * @param rows
	 * @return
	 */
	net.sf.json.JSONObject queryByInteriorWorkSn(String interiorWorkSn, Integer page, Integer rows);

	/**
	 * ����
	 * @param interiorAttachment
	 */
	void add(InteriorWorkAttachment interiorAttachment);
	
	/**
	 * ����
	 */
	void update(InteriorWorkAttachment interiorAttachment);

	/**
	 * ����id��ѯ����
	 * @param id
	 * @return
	 */
	InteriorWorkAttachment getById(int id);

}
