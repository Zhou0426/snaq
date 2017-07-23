package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.LatentHazardAttachment;
import net.sf.json.JSONObject;

public interface LatentHazardAttachmentDao extends BaseDao<LatentHazardAttachment> {
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

}
