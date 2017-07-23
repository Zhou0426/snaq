package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.TemplateAttachment;

public interface TemplateAttachmentService {
	int addTemplateAttachment(TemplateAttachment templateAttachment);
	List<TemplateAttachment> getAllTemplateAttachment();
	void deleteTemplateAttachment(int id);
	List<TemplateAttachment> queryJoinDocumentTemplate(String documentTemplateSn);
	void deleteByIds(String ids);
}
