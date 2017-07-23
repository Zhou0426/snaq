package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.TemplateAttachmentDao;
import cn.jagl.aq.domain.TemplateAttachment;
import cn.jagl.aq.service.TemplateAttachmentService;
@Service("templateAttachmentService")
public class TemplateAttachmentServiceImpl implements TemplateAttachmentService {
	@Resource(name="templateAttachmentDao")
	private TemplateAttachmentDao templateAttachmentDao;

	@Override
	public int addTemplateAttachment(TemplateAttachment templateAttachment) {
		// TODO Auto-generated method stub
		return (Integer) templateAttachmentDao.save(templateAttachment);
	}

	@Override
	public List<TemplateAttachment> getAllTemplateAttachment() {
		// TODO Auto-generated method stub
		return templateAttachmentDao.findAll(TemplateAttachment.class);
	}

	@Override
	public void deleteTemplateAttachment(int id) {
		// TODO Auto-generated method stub
		templateAttachmentDao.delete(TemplateAttachment.class, id);
	}
	@Override
	public List<TemplateAttachment> queryJoinDocumentTemplate(String documentTemplateSn) {
		// TODO Auto-generated method stub
		return templateAttachmentDao.queryJoinDocumentTemplate(documentTemplateSn);
	}

	@Override
	public void deleteByIds(String ids) {
		// TODO Auto-generated method stub
		templateAttachmentDao.deleteByIds(ids);
	}

}
