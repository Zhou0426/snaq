package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.jagl.aq.dao.ManagementReviewAttachmentDao;
import cn.jagl.aq.domain.ManagementReviewAttachment;
import cn.jagl.aq.service.ManagementReviewAttachmentService;
@Service("managementReviewAttachmentService")
public class ManagementReviewAttachmentServiceImpl implements ManagementReviewAttachmentService {

	@Resource(name="managementReviewAttachmentDao")
	private ManagementReviewAttachmentDao managementReviewAttachmentDao;
	@Override
	public int add(ManagementReviewAttachment managementReviewAttachment) {
		// TODO Auto-generated method stub
		return (int) managementReviewAttachmentDao.save(managementReviewAttachment);
	}
	@Override
	public ManagementReviewAttachment getByPath(String path) {
		// TODO Auto-generated method stub
		return managementReviewAttachmentDao.getByPath(path);
	}
	@Override
	public void delete(ManagementReviewAttachment managementReviewAttachment) {
		managementReviewAttachmentDao.delete(managementReviewAttachment);
		
	}
	@Override
	public void update(ManagementReviewAttachment attachment) {
		// TODO Auto-generated method stub
		managementReviewAttachmentDao.update(attachment);
	}
	@Override
	public ManagementReviewAttachment getById(int attid) {
		// TODO Auto-generated method stub
		return managementReviewAttachmentDao.getById(attid);
	}

}
