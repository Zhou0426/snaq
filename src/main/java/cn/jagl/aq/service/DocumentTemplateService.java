package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.DocumentTemplate;

public interface DocumentTemplateService {
	// ����ʵ��
	int addDocumentTemplate(DocumentTemplate documentTemplate);
	// ����IDɾ��ʵ��
	void deleteDocumentTemplateByIds(String ids);
	// ��ȡ����ʵ��
	List<DocumentTemplate> getAllDocumentTemplates();
	//����
	void update(DocumentTemplate documentTemplate);
	//����
	long count(String hql);
	//��ҳ��ѯ
    List<DocumentTemplate> query(int page,int rows);
    //ͨ��sn��ѯ
    DocumentTemplate getByDocumentTemplateSn(String documentTemplateSn);
    //Id��ѯ
    DocumentTemplate getById(int id);
    //����ָ���Ų�ѯ���ģ��
  	List<DocumentTemplate> queryJoinStandardIndex(String indexSn);
  	//ÿ��ֻ��ʾ15���������ڵ����ѯ
  	List <DocumentTemplate> queryByQ(String hql,int pageNo,int pageSize);
}
