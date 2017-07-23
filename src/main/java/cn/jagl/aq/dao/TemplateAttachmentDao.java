package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.TemplateAttachment;

public interface TemplateAttachmentDao extends BaseDao<TemplateAttachment>{
	
	public List<TemplateAttachment> queryJoinDocumentTemplate(String documentTemplateSn);
	public void deleteByIds(String ids);
}
