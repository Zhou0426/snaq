package cn.jagl.aq.service;

import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONObject;

public interface LatentHazardAttachmentService {
	
	/**
	 * 添加记录
	 * @param latentHazardAttachment
	 */
	void add(LatentHazardAttachment latentHazardAttachment);
	/**
	 * 根据重大隐患编号查询附件
	 * @param latentHazardSn
	 * @return
	 */
	JSONObject queryAttachmentByLatentHazardSn(String latentHazardSn);
	/**
	 * 根据id逻辑删除附件
	 * @param id
	 */
	void deleteById(int id);
	/**
	 * 根据id查询数据
	 * @param id
	 * @return
	 */
	LatentHazardAttachment queryById(int id);

}
