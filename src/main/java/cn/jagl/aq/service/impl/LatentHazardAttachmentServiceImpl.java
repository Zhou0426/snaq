package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.LatentHazardAttachmentDao;
import cn.jagl.aq.domain.LatentHazardAttachment;
import cn.jagl.aq.service.LatentHazardAttachmentService;
import net.sf.json.JSONObject;

@Service("latentHazardAttachmentService")
public class LatentHazardAttachmentServiceImpl implements LatentHazardAttachmentService {

	@Resource(name="latentHazardAttachmentDao")
	private LatentHazardAttachmentDao latentHazardAttachmentDao;

	@Override
	public void add(LatentHazardAttachment latentHazardAttachment) {
		latentHazardAttachmentDao.save(latentHazardAttachment);
	}

	@Override
	public JSONObject queryAttachmentByLatentHazardSn(String latentHazardSn) {
		return latentHazardAttachmentDao.queryAttachmentByLatentHazardSn(latentHazardSn);
	}

	@Override
	public void deleteById(int id) {
		latentHazardAttachmentDao.deleteById(id);
	}

	@Override
	public LatentHazardAttachment queryById(int id) {
		return latentHazardAttachmentDao.get(LatentHazardAttachment.class, id);
	}
	
}
