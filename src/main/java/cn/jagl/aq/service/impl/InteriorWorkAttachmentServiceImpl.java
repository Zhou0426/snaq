package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.InteriorWorkAttachmentDao;
import cn.jagl.aq.domain.InteriorWorkAttachment;
import cn.jagl.aq.service.InteriorWorkAttachmentService;
import net.sf.json.JSONObject;

@Service("interiorWorkAttachmentService")
public class InteriorWorkAttachmentServiceImpl implements InteriorWorkAttachmentService {

	@Resource(name="interiorWorkAttachmentDao")
	private InteriorWorkAttachmentDao interiorWorkAttachmentDao;

	@Override
	public JSONObject queryByInteriorWorkSn(String interiorWorkSn, Integer page, Integer rows) {
		return interiorWorkAttachmentDao.queryByInteriorWorkSn(interiorWorkSn, page, rows);
	}

	@Override
	public void update(InteriorWorkAttachment interiorAttachment) {
		interiorWorkAttachmentDao.update(interiorAttachment);
	}

	@Override
	public InteriorWorkAttachment getById(int id) {
		return interiorWorkAttachmentDao.get(InteriorWorkAttachment.class, id);
	}

	@Override
	public void add(InteriorWorkAttachment interiorAttachment) {
		interiorWorkAttachmentDao.save(interiorAttachment);
	}
	
}
