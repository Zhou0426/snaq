package cn.jagl.aq.service;

import cn.jagl.aq.domain.NoticeAttachment;

public interface NoticeAttachmentService {

	/**
	 * ���¹��渽��
	 * @param noticeAttachment
	 */
	void update(NoticeAttachment noticeAttachment);

	/**
	 * ���ӹ��渽��
	 * @param noticeAttachment
	 */
	void add(NoticeAttachment noticeAttachment);
	/**
	 * ����id���Ҹ���
	 * @param id
	 * @return
	 */
	NoticeAttachment getById(int id);

}
