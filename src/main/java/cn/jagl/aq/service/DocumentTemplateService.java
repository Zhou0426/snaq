package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.DocumentTemplate;

public interface DocumentTemplateService {
	// 保存实体
	int addDocumentTemplate(DocumentTemplate documentTemplate);
	// 根据ID删除实体
	void deleteDocumentTemplateByIds(String ids);
	// 获取所有实体
	List<DocumentTemplate> getAllDocumentTemplates();
	//更新
	void update(DocumentTemplate documentTemplate);
	//总数
	long count(String hql);
	//分页查询
    List<DocumentTemplate> query(int page,int rows);
    //通过sn查询
    DocumentTemplate getByDocumentTemplateSn(String documentTemplateSn);
    //Id查询
    DocumentTemplate getById(int id);
    //根据指标编号查询相关模板
  	List<DocumentTemplate> queryJoinStandardIndex(String indexSn);
  	//每条只显示15条或者用于单表查询
  	List <DocumentTemplate> queryByQ(String hql,int pageNo,int pageSize);
}
