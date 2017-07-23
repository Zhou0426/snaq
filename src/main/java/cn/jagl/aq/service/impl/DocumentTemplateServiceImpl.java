package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.DocumentTemplateDao;
import cn.jagl.aq.domain.DocumentTemplate;
import cn.jagl.aq.service.DocumentTemplateService;
@Service("documentTemplateService")
public class DocumentTemplateServiceImpl implements DocumentTemplateService {
	@Resource(name="documentTemplateDao")
	private DocumentTemplateDao documentTemplateDao;
	@Override
	public int addDocumentTemplate(DocumentTemplate documentTemplate) {
		// TODO Auto-generated method stub
		return (Integer)documentTemplateDao.save(documentTemplate);
	}

	@Override
	public void deleteDocumentTemplateByIds(String ids) {
		// TODO Auto-generated method stub
		
		documentTemplateDao.deleteByIds(ids);
	}
	@Override
	public List<DocumentTemplate> getAllDocumentTemplates() {
		// TODO Auto-generated method stub
		return documentTemplateDao.findAll(DocumentTemplate.class);
	}
	@Override
	public void update(DocumentTemplate documentTemplate) {
		// TODO Auto-generated method stub
		documentTemplateDao.update(documentTemplate);
	}

	@Override
	public long count(String hql) {
		// TODO Auto-generated method stub
		return documentTemplateDao.count(hql);
	}

	@Override
	public List<DocumentTemplate> query(int page, int rows) {
		// TODO Auto-generated method stub
		return documentTemplateDao.query(page, rows);
	}

	@Override
	public DocumentTemplate getByDocumentTemplateSn(String documentTemplateSn) {
		// TODO Auto-generated method stub
		return documentTemplateDao.getByDocumentTemplateSn(documentTemplateSn);
	}

	@Override
	public DocumentTemplate getById(int id) {
		// TODO Auto-generated method stub
		return documentTemplateDao.get(DocumentTemplate.class, id);
	}
	//根据指标编号查询相关模板
	@Override
	public List<DocumentTemplate> queryJoinStandardIndex(String indexSn) {
		// TODO Auto-generated method stub
		return documentTemplateDao.queryJoinStandardIndex(indexSn);
	}
	//每条只显示15条
	@Override
	public List<DocumentTemplate> queryByQ(String hql,int pageNo,int pageSize) {
		// TODO Auto-generated method stub
		return documentTemplateDao.findByPage(hql, pageNo, pageSize);
	}
}
