package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.NoticeAttachmentDao;
import cn.jagl.aq.domain.NoticeAttachment;
import cn.jagl.aq.service.NoticeAttachmentService;

@Service("noticeAttachmentService")
public class NoticeAttachmentServiceImpl implements NoticeAttachmentService {

	@Resource(name="noticeAttachmentDao")
	private NoticeAttachmentDao noticeAttachmentDao;

	@Override
	public void update(NoticeAttachment noticeAttachment) {
		noticeAttachmentDao.update(noticeAttachment);
	}

	@Override
	public void add(NoticeAttachment noticeAttachment) {
		noticeAttachmentDao.save(noticeAttachment);
	}

	@Override
	public NoticeAttachment getById(int id) {
		return noticeAttachmentDao.get(NoticeAttachment.class, id);
	}
	
}
