package cn.jagl.aq.service;

import cn.jagl.aq.domain.ManagementReviewAttachment;

public interface ManagementReviewAttachmentService {

	int add(ManagementReviewAttachment managementReviewAttachment);
	void delete(ManagementReviewAttachment managementReviewAttachment);	
	//�����߼�·����
	ManagementReviewAttachment getByPath(String path);
	void update(ManagementReviewAttachment attachment);
	ManagementReviewAttachment getById(int attid);
}
