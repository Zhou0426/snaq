package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.DocumentTemplate;
import cn.jagl.aq.domain.TemplateAttachment;

public interface DocumentTemplateDao extends BaseDao<DocumentTemplate> {
	public void deleteByIds(String ids);
	public long count(String hql);
	public List<DocumentTemplate> query(int page,int rows);
	public DocumentTemplate getByDocumentTemplateSn(String documentTemplateSn);
	//根据指标编号查询相关模板
	public List<DocumentTemplate> queryJoinStandardIndex(String indexSn);
}
