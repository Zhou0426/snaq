package cn.jagl.aq.service;

import cn.jagl.aq.domain.InteriorWorkAttachment;

public interface InteriorWorkAttachmentService {

	/**
	 * 根据编号分页查询附件数据
	 * @param interiorWorkSn
	 * @param page
	 * @param rows
	 * @return
	 */
	net.sf.json.JSONObject queryByInteriorWorkSn(String interiorWorkSn, Integer page, Integer rows);

	/**
	 * 增加
	 * @param interiorAttachment
	 */
	void add(InteriorWorkAttachment interiorAttachment);
	
	/**
	 * 更新
	 */
	void update(InteriorWorkAttachment interiorAttachment);

	/**
	 * 根据id查询附件
	 * @param id
	 * @return
	 */
	InteriorWorkAttachment getById(int id);

}
